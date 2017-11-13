package org.medfordhistorical.overthemystic;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.android.telemetry.location.LocationEnginePriority;
import com.mapbox.services.android.telemetry.location.LostLocationEngine;
import com.mapbox.services.android.telemetry.permissions.PermissionsListener;
import com.mapbox.services.android.telemetry.permissions.PermissionsManager;
import com.mapbox.services.api.directions.v5.DirectionsCriteria;
import com.mapbox.services.api.directions.v5.MapboxDirections;
import com.mapbox.services.api.directions.v5.models.DirectionsResponse;
import com.mapbox.services.api.directions.v5.models.DirectionsRoute;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.models.Position;


import java.util.List;
import java.util.ArrayList;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.services.Constants.PRECISION_6;


public class MapActivity extends AppCompatActivity implements LocationEngineListener,
        PermissionsListener, SelectedSitesRecyclerViewAdapter.ClickListener {

    private MapboxMap mapboxMap;
    private MapView mapView;
    public Location originLocation;
    private PermissionsManager permissionsManager;
    private LocationLayerPlugin locationPlugin;
    private LocationEngine locationEngine;
    private SelectedSitesRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private List<Site> sites;
    private int[] siteIds;
    private DirectionsRoute currentRoute;
    private NavigationMapRoute navigationMapRoute;
    private MapboxDirections directionsApiClient;
    private static final LatLng MOCK_DEVICE_LOCATION_LAT_LNG = new LatLng(42.4075, -71.1190);
    private String ACCESS_TOKEN = "pk.eyJ1IjoibWVkZm9yZGhpc3RvcmljYWwiLCJhIjoiY2o4ZXNiNHN2M" +
            "TZycjMzb2ttcWp0dDJ1aiJ9.zt52s3jkwqtDc1I2Fv5cJg";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, ACCESS_TOKEN);
        setContentView(R.layout.activity_map);
        setTitle("Start Navigation");

        sites = new ArrayList<>();

        Intent intent = getIntent();
        siteIds = intent.getIntArrayExtra("siteIds");

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {

                MapActivity.this.mapboxMap = mapboxMap;

                setSites(siteIds);
                enableLocationPlugin();
                addFakeLocationMarkerToMap();
                setUpRecyclerView();
            }
        });
    }

    public void setSites(int[] siteIds) {
        Realm realm = Realm.getDefaultInstance();

        if(siteIds == null) {
            sites = realm.copyFromRealm(QueryUtils.getSitesFromDatabase());
            Log.d("sites", sites.toString());
        }
        else {
            for(int i = 0; i < siteIds.length; i++) {
                Site site = realm.copyFromRealm(QueryUtils.getSiteFromDatabase(siteIds[i]));
                sites.add(site);
                mapboxMap.addMarker(new MarkerOptions()
                        .position(site.getLocation())
                        .title(site.getName()));
            }
        }
    }

    public void setUpRecyclerView() {
        recyclerView = findViewById(R.id.rvSite);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SelectedSitesRecyclerViewAdapter(sites, mapboxMap, getApplicationContext(), this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
    }

    public void addFakeLocationMarkerToMap() {
        mapboxMap.addMarker(new MarkerOptions()
                .position(MOCK_DEVICE_LOCATION_LAT_LNG)
                .icon(IconFactory.getInstance(this.getApplicationContext()).fromResource(R.drawable.blue_user_location)));
    }

    @Override
    public void onItemClick(int position) {

        LatLng selectedLocationLatLng = sites.get(position).getLocation();
        CameraPosition newCameraPosition = new CameraPosition.Builder()
                .target(selectedLocationLatLng)
                .build();

        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCameraPosition), 1200);
        mapboxMap.selectMarker(mapboxMap.getMarkers().get(position));

        getDirections(selectedLocationLatLng.getLatitude(), selectedLocationLatLng.getLongitude());

    }

    @Override
    public void onNavButtonClick(int position, String type) {

    }

    public void drawRoute(DirectionsRoute route, String method) {

        if (mapboxMap.getPolylines().size() > 0) {
            mapboxMap.removePolyline(mapboxMap.getPolylines().get(0));
        }

        LineString lineString = LineString.fromPolyline(route.getGeometry(), PRECISION_6);
        List<Position> coordinates = lineString.getCoordinates();
        LatLng[] polylineDirectionsPoints = new LatLng[coordinates.size()];

        for (int i = 0; i < coordinates.size(); i++) {
            polylineDirectionsPoints[i] = new LatLng(
                    coordinates.get(i).getLatitude(),
                    coordinates.get(i).getLongitude());
        }

        mapboxMap.addPolyline(new PolylineOptions()
                .add(polylineDirectionsPoints));

    }

    private void getDirections(double destinationLatCoordinate, double destinationLongCoordinate) {

        Position mockCurrentLocation = Position.fromLngLat(MOCK_DEVICE_LOCATION_LAT_LNG.getLongitude(),
                MOCK_DEVICE_LOCATION_LAT_LNG.getLatitude());

        Position destinationMarker = Position.fromLngLat(destinationLongCoordinate, destinationLatCoordinate);

        directionsApiClient = new MapboxDirections.Builder()
                .setOrigin(mockCurrentLocation)
                .setDestination(destinationMarker)
                .setOverview(DirectionsCriteria.OVERVIEW_FULL)
                .setProfile(DirectionsCriteria.PROFILE_WALKING)
                .setAccessToken(ACCESS_TOKEN)
                .build();

        directionsApiClient.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {

                if (response.body() == null) {
                    Log.e("MapActivity", "No routes found, make sure you set the right user and access token.");
                }

                else if (response.body().getRoutes().size() < 1) {
                    Log.e("MapActivity", "No routes found");
                }

                else {
                    currentRoute = response.body().getRoutes().get(0);
                    drawRoute(currentRoute, "walk");

                }

            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                Log.e("MapActivity", throwable.toString());
            }
        });
    }

    public void startNavigation() {
        if (originLocation == null)
            enableLocationPlugin();

        Position origin = Position.fromCoordinates(originLocation.getLongitude(), originLocation.getLatitude());
        Position destination = Position.fromCoordinates(-71.12012, 42.40742);

        boolean simulateRoute = true;

        if(originLocation != null) {
            NavigationLauncher.startNavigation(this, origin, destination, null, simulateRoute);
        }
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationPlugin() {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            // Create an instance of LOST location engine
            initializeLocationEngine();

//            locationPlugin = new LocationLayerPlugin(mapView, mapboxMap, locationEngine);
//            locationPlugin.setLocationLayerEnabled(LocationLayerMode.TRACKING);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressWarnings( {"MissingPermission"})
    private void initializeLocationEngine() {
        locationEngine = new LostLocationEngine(MapActivity.this);
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();

        Location lastLocation = locationEngine.getLastLocation();
        if (lastLocation != null) {
            originLocation = lastLocation;
            setCameraPosition(lastLocation);
        } else {
            locationEngine.addLocationEngineListener(this);
        }
    }

    private void setCameraPosition(Location location) {
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 13));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationPlugin();
        } else {
            finish();
        }
    }

    @Override
    @SuppressWarnings( {"MissingPermission"})
    public void onConnected() {
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            originLocation = location;
            setCameraPosition(location);
            locationEngine.removeLocationEngineListener(this);
        }
    }

    @Override
    @SuppressWarnings( {"MissingPermission"})
    protected void onStart() {
        super.onStart();
        mapView.onStart();

        if (locationEngine != null) {
            locationEngine.requestLocationUpdates();
            locationEngine.addLocationEngineListener(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();

        if (locationEngine != null) {
            locationEngine.removeLocationUpdates();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (locationEngine != null) {
            locationEngine.deactivate();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
}

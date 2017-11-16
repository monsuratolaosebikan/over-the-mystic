package org.medfordhistorical.overthemystic;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
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
import com.vlonjatg.progressactivity.ProgressFrameLayout;


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
    private DirectionsRoute currentRoute;
    private MapboxDirections directionsApiClient;
    private LocationManager locationManager;
    ProgressFrameLayout progressFrameLayout;
    CountingIdlingResource idlingResource = new CountingIdlingResource("Load data from server");
    private String ACCESS_TOKEN = "pk.eyJ1IjoibWVkZm9yZGhpc3RvcmljYWwiLCJhIjoiY2o4ZXNiNHN2M" +
            "TZycjMzb2ttcWp0dDJ1aiJ9.zt52s3jkwqtDc1I2Fv5cJg";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, ACCESS_TOKEN);
        setContentView(R.layout.activity_map);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        QueryUtils.getSitesFromServer(getApplicationContext(), idlingResource);

        sites = new ArrayList<>();

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        progressFrameLayout = findViewById(R.id.frame);

        checkGPSEnabled();
        checkInternetEnabled();

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {

                MapActivity.this.mapboxMap = mapboxMap;

                setSites();
                enableLocationPlugin();
                setUpRecyclerView();
            }
        });
    }

    private View.OnClickListener errorInternetClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkInternetEnabled();
        }
    };

    private View.OnClickListener errorGPSClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkGPSEnabled();
        }
    };

    public void checkInternetEnabled() {
        Drawable errorDrawable = getDrawable(R.drawable.ic_wifi_off);
        List<Integer> skipIds = new ArrayList<>();

        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            progressFrameLayout.showContent();
        } else {
            progressFrameLayout.showError(errorDrawable,
                    "No Connection",
                    "We could not establish a connection. Make sure wifi or data is enabled.",
                    "Try Again", errorInternetClickListener, skipIds);
        }
    }

    public void checkGPSEnabled() {

        Drawable errorDrawable = getDrawable(R.drawable.ic_location_off);
        List<Integer> skipIds = new ArrayList<>();
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            if(originLocation == null) {
                //let the user enter a location?
            }

            progressFrameLayout.showContent();

        } else {
            progressFrameLayout.showError(errorDrawable,
                    "No Location",
                    "We could not get your location. Make sure it's enabled in settings",
                    "Try Again", errorGPSClickListener, skipIds);
        }
    }


    public void setSites() {
        Realm realm = Realm.getDefaultInstance();
        sites = realm.copyFromRealm(QueryUtils.getSitesFromDatabase());
        for(int i = 0; i < sites.size(); i++) {
            mapboxMap.addMarker(new MarkerOptions()
                    .position(sites.get(i).getLocation())
                    .title(sites.get(i).getName()));
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
        Intent intent = new Intent(this, ViewLocationDetail.class);
        intent.putExtra("siteId", sites.get(position).getId());
        intent.putExtra("siteName", sites.get(position).getName());
        intent.putExtra("siteDesc", sites.get(position).getShortDesc());
        intent.putExtra("imageUrl", sites.get(position).getImageUrl());
        intent.putExtra("audioUrl", sites.get(position).getAudioUrl());

        double longitude = sites.get(position).getLongitude();
        double latitude = sites.get(position).getLatitude();

        boolean simulateRoute = false;

        if(originLocation != null) {
            Position origin = Position.fromCoordinates(originLocation.getLongitude(), originLocation.getLatitude());
            Position destination = Position.fromCoordinates(longitude, latitude);

            startActivity(intent);

            NavigationLauncher.startNavigation(this, origin, destination, null, simulateRoute);
        }
        else {
            checkGPSEnabled();
            Toast.makeText(this,"Could not get your location", Toast.LENGTH_LONG).show();
        }

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
                .add(polylineDirectionsPoints))
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.mapbox_navigation_route_layer_blue));
    }

    private void getDirections(double destinationLatCoordinate, double destinationLongCoordinate) {

        if(originLocation == null) return;

        Position mockCurrentLocation = Position.fromLngLat(originLocation.getLongitude(),
                originLocation.getLatitude());

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

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationPlugin() {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            // Create an instance of LOST location engine
            initializeLocationEngine();

            locationPlugin = new LocationLayerPlugin(mapView, mapboxMap, locationEngine);
            locationPlugin.setLocationLayerEnabled(LocationLayerMode.TRACKING);
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

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_activity_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

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

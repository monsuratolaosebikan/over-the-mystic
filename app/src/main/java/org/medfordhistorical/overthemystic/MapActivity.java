package org.medfordhistorical.overthemystic;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.android.telemetry.location.LocationEnginePriority;
import com.mapbox.services.android.telemetry.location.LostLocationEngine;
import com.mapbox.services.android.telemetry.permissions.PermissionsListener;
import com.mapbox.services.android.telemetry.permissions.PermissionsManager;
import com.mapbox.services.api.directions.v5.DirectionsCriteria;
import com.mapbox.services.api.directions.v5.models.DirectionsResponse;
import com.mapbox.services.api.directions.v5.models.DirectionsRoute;
import com.mapbox.services.commons.models.Position;

import java.util.List;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class MapActivity extends AppCompatActivity implements LocationEngineListener, PermissionsListener {

    private MapboxMap mapboxMap;
    private MapView mapView;
    private Location originLocation;
    private PermissionsManager permissionsManager;
    private LocationLayerPlugin locationPlugin;
    private LocationEngine locationEngine;
    private RecyclerView recyclerView;
    private List<Site> sites;
    private DirectionsRoute currentRoute;
    private NavigationMapRoute navigationMapRoute;
    private String ACCESS_TOKEN = "pk.eyJ1IjoibWVkZm9yZGhpc3RvcmljYWwiLCJhIjoiY2o4ZXNiNHN2M" +
            "TZycjMzb2ttcWp0dDJ1aiJ9.zt52s3jkwqtDc1I2Fv5cJg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, ACCESS_TOKEN);
        setContentView(R.layout.activity_map);

        recyclerView = (RecyclerView) findViewById(R.id.rvSite);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        //adds markers to map
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {

                MapActivity.this.mapboxMap = mapboxMap;

                enableLocationPlugin();

                setSites();

                SelectedSitesRecyclerViewAdapter adapter = new SelectedSitesRecyclerViewAdapter(sites, mapboxMap);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
                SnapHelper snapHelper = new LinearSnapHelper();
                snapHelper.attachToRecyclerView(recyclerView);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_navigate:
                startNavigation();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setSites() {
        sites = new ArrayList<>();

        LatLng[] locations = new LatLng[] {
                new LatLng(42.40742, -71.12012),
                new LatLng(42.40678, -71.11652),
                new LatLng(42.40817, -71.11375),
                new LatLng(42.40909, -71.1154),
                new LatLng(42.41235, -71.11163),
                new LatLng(42.41312, -71.11096),
                new LatLng(42.41679, -71.11527)
        };

        String[] locationNames = new String[]{ "Ballou Hall", "Mystic River",
                "Tufts University", "Tisch Library", "Women's Center", "Tufts Park",
                "Stearns Street" };

        for (int i = 0; i < locations.length; i++) {
            Site site = new Site();
            site.setName(i + 1 +  ". " + locationNames[i]);
            site.setShortDesc(
                    "Ballou Hall is a historic academic building on the campus of" +
                            " Tufts University in Medford, Massachusetts.  "
            );
            site.setLocation(locations[i]);
            sites.add(site);

            mapboxMap.addMarker(new MarkerOptions()
                    .position(locations[i])
                    .title(locationNames[i])
                    .snippet("Welcome jumbos!"));

            getRoute(locations, "bike");
        }
    }

    public void getRoute(LatLng[] locations, String method) {

        String profile;

        if (method.toLowerCase().equals("bike")) profile = DirectionsCriteria.PROFILE_WALKING;
        else  profile = DirectionsCriteria.PROFILE_CYCLING;


        List<Position> coordinates = new ArrayList<>();

        for (int i = 0; i < locations.length; i++) {
            Position coordinate = Position.fromCoordinates(locations[i].getLongitude(), locations[i].getLatitude());
            coordinates.add(coordinate);
        }

        NavigationRoute.Builder navigationRouteBuilder = NavigationRoute.builder()
                .accessToken(Mapbox.getAccessToken())
                .profile(profile)
                .origin(coordinates.get(0))
                .destination(coordinates.get(coordinates.size()-1));

        for (int index = 1; index < coordinates.size(); index++) {
            navigationRouteBuilder.addWaypoint(coordinates.get(index));
        }

        navigationRouteBuilder.build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if (response.body() == null) {
                            Log.e("nav", "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().getRoutes().size() < 1) {
                            Log.e("nav", "No routes found");
                            return;
                        }

                        currentRoute = response.body().getRoutes().get(0);

                        // Draw the route on the map
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e("nav", "Error: " + throwable.getMessage());
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

    @Override
    @SuppressWarnings( {"MissingPermission"})
    protected void onStart() {
        super.onStart();
        if (locationEngine != null) {
            locationEngine.requestLocationUpdates();
        }
        if (locationPlugin != null) {
            locationPlugin.onStart();
        }
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates();
        }
        if (locationPlugin != null) {
            locationPlugin.onStop();
        }
        mapView.onStop();
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

}

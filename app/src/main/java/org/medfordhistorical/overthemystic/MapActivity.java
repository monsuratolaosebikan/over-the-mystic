package org.medfordhistorical.overthemystic;

import android.graphics.Color;
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

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
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


public class MapActivity extends AppCompatActivity {

    public MapboxMap mapboxMap;
    private MapView mapView;
    private RecyclerView recyclerView;
    private List<Site> sites;
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

                Realm realm = Realm.getDefaultInstance();

                MapActivity.this.mapboxMap = mapboxMap;

                mapboxMap.setMyLocationEnabled(true);

                setSites();

                SelectedSitesRecyclerViewAdapter adapter = new SelectedSitesRecyclerViewAdapter(realm.where(Site.class).findAll(), mapboxMap);
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
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
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
                      .title("Tufts University")
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

        MapboxDirections client = new MapboxDirections.Builder()
                .setAccessToken(ACCESS_TOKEN)
                .setProfile(profile)
                .setOrigin(coordinates.get(0))
                .setDestination(coordinates.get(coordinates.size()-1))
                .setCoordinates(coordinates)
                .setOverview("full")
                .build();


        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                if (response.body() == null || response.body().getRoutes().size() == 0) {
                    Log.e("Map", "No routes found");
                    return;
                }

                DirectionsRoute route = response.body().getRoutes().get(0);
                drawRoute(route);

            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                Log.e("Map", "Error: " + t.getMessage());
            }
        });
    }

    public void drawRoute(DirectionsRoute route) {

        LineString lineString = LineString.fromPolyline(route.getGeometry(), PRECISION_6);
        List<Position> coordinates = lineString.getCoordinates();
        LatLng[] points = new LatLng[coordinates.size()];
        for (int i = 0; i < coordinates.size(); i++) {
            points[i] = new LatLng(
                    coordinates.get(i).getLatitude(),
                    coordinates.get(i).getLongitude());
        }

        mapboxMap.addPolyline(new PolylineOptions()
                .add(points)
                .color(Color.parseColor("#303F9F"))
                .width(6));
    }

}

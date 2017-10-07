package org.medfordhistorical.overthemystic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.List;
import java.util.ArrayList;


public class MapActivity extends AppCompatActivity {

    public MapboxMap mapboxMap;
    private MapView mapView;
    private RecyclerView recyclerView;
    private List<Site> sites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1IjoibWVkZm9yZGhpc3RvcmljYWwiLCJhIjoiY2o4ZXNiNHN2MTZycjMzb2ttcWp0dDJ1aiJ9.zt52s3jkwqtDc1I2Fv5cJg");
        setContentView(R.layout.activity_map);

        recyclerView = (RecyclerView) findViewById(R.id.rvSite);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        //adds markers to map
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {

                MapActivity.this.mapboxMap = mapboxMap;

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
        }
    }
}

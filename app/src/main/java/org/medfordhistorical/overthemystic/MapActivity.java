package org.medfordhistorical.overthemystic;

import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

public class MapActivity extends AppCompatActivity {

    private MapView mapView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1IjoibWVkZm9yZGhpc3RvcmljYWwiLCJhIjoiY2o4ZXNiNHN2MTZycjMzb2ttcWp0dDJ1aiJ9.zt52s3jkwqtDc1I2Fv5cJg");
        setContentView(R.layout.activity_map);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        //adds markers to map
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {

                Pair<Double, Double>[] locations = new Pair[] {
                        new Pair(42.40742, -71.12012),
                        new Pair(42.40678, -71.11652),
                        new Pair(42.40817, -71.11375),
                        new Pair(42.40909, -71.1154),
                        new Pair(42.41235, -71.11163),
                        new Pair(42.41312, -71.11096),
                        new Pair(42.41679, -71.11527)
                };

                for (int i = 0; i < locations.length; i++) {
                    mapboxMap.addMarker(new MarkerOptions()
                            .position(new LatLng(locations[i].first, locations[i].second))
                            .title("Tufts University")
                            .snippet("Welcome jumbos!"));
                }
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
}

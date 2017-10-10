package org.medfordhistorical.overthemystic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.commons.models.Position;

public class NavigationActivity extends AppCompatActivity {

    public MapboxMap mapboxMap;
    private MapView mapView;
//    private List<Site> sites;
    private String ACCESS_TOKEN = "pk.eyJ1IjoibWVkZm9yZGhpc3RvcmljYWwiLCJhIjoiY2o4ZXNiNHN2M" +
                                "TZycjMzb2ttcWp0dDJ1aiJ9.zt52s3jkwqtDc1I2Fv5cJg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, ACCESS_TOKEN);

        setContentView(R.layout.activity_navigation);


        Position origin = Position.fromCoordinates(-77.03613, 38.90992);
        Position destination = Position.fromCoordinates(-77.0365, 38.8977);

        boolean simulateRoute = true;

        // Call this method with Context from within an Activity
        NavigationLauncher.startNavigation(this, origin, destination, null, simulateRoute);
    }
}

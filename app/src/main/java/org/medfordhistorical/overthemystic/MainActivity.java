package org.medfordhistorical.overthemystic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user taps one of the home screen buttons */
    public void launchTour(View view) {
        // Do something in response to button
        switch (view.getId()) {
            case R.id.bike:
                // call bike-specific function
                Intent bikeIntent = new Intent(this, DisplayBikeTourActivity.class);
                startActivity(bikeIntent);
                break;
            case R.id.walk:
                // call walking-specific function
                Intent walkingIntent = new Intent(this, DisplayWalkingTourActivity.class);
                startActivity(walkingIntent);
                break;
        }
    }

}

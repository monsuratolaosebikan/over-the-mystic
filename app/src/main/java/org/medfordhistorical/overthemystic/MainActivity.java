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

    /** Called when the user taps one of the homescreen buttons */
    public void launchTour(View view) {
        // Do something in response to button
        switch (view.getId()) {
            case R.id.bike:
                Log.d("MainActivity", "Bike");
                break;
            case R.id.walk:
                Log.d("MainActivity", "Walk");
                break;
        }
    }
    
}

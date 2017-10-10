package org.medfordhistorical.overthemystic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public static final String tourType = "org.medfordhistorical.overthemystic.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user taps one of the home screen buttons */
    public void launchTourSelection(View view) {
        Intent intent = new Intent(this, StartTourActivity.class);
        // Do something in response to button
        switch (view.getId()) {
            case R.id.bike:
                // call bike-specific function
                intent.putExtra("tourType", "bike");
                startActivity(intent);
                break;
            case R.id.walk:
                // call walking-specific function
                intent.putExtra("tourType", "walking");
                startActivity(intent);
                break;
        }
    }

}

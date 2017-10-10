package org.medfordhistorical.overthemystic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ViewLocationDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_detail);
    }

    public void controlAudio(View view) {
        // Do something in response to button
        switch (view.getId()) {
            case R.id.playButton:
                break;
            /*case R.id.rewindButton:
                break;
            case R.id.fastforwardButton:
                break;*/
        }
    }
}
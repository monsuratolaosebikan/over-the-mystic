package org.medfordhistorical.overthemystic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

public class StartTourActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_tour);

        Intent intent = getIntent();

        String tourType = intent.getStringExtra("tourType");
        ((TextView)findViewById(R.id.tourWelcome)).setText("Welcome to the " + tourType + " tour!");
    }
}
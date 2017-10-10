package org.medfordhistorical.overthemystic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

public class SiteSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_selection);
    }

    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);


}

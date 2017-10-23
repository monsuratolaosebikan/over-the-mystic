package org.medfordhistorical.overthemystic;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class StartTourActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    ViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_tour);
        setTitle("New Tour");
        Intent intent = getIntent();
        String tourType = intent.getStringExtra("tourType");

        FloatingActionButton goToMapBtn = (FloatingActionButton) findViewById(R.id.go_to_map_btn);
        goToMapBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartTourActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        List<Site> sites = getSites();

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        adapter = new ViewAdapter(getApplicationContext(), sites);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    public List<Site> getSites(){
        List<Site> siteList = QueryUtils.getSitesFromDatabase();
        return siteList;

    }




}
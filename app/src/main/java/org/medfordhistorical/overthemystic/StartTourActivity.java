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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

import io.realm.Realm;

public class StartTourActivity extends AppCompatActivity {
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_tour);
        QueryUtils.getSitesFromServer(getApplicationContext());

        final List<Site> sites = getSites();

        gridView = (GridView) findViewById(R.id.gridview);
        final SitesAdapter adapter = new SitesAdapter(this, sites);
        gridView.setAdapter(adapter);

        FloatingActionButton goToMapBtn = (FloatingActionButton) findViewById(R.id.go_to_map_btn);
        goToMapBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartTourActivity.this, MapActivity.class);
                intent.putExtra("siteIds", adapter.getSitesSelected());
                startActivity(intent);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Site site = sites.get(position);
                site.toggleSelected();
                adapter.notifyDataSetChanged();
            }
        });
    }

    public List<Site> getSites(){
        Realm realm = Realm.getDefaultInstance();
        List<Site> siteList = realm.copyFromRealm(QueryUtils.getSitesFromDatabase());
        return siteList;
    }
}
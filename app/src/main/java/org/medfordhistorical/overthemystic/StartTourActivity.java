package org.medfordhistorical.overthemystic;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.test.espresso.idling.CountingIdlingResource;
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

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class StartTourActivity extends AppCompatActivity {
    private GridView gridView;
    CountingIdlingResource idlingResource = new CountingIdlingResource("Load Data from server");
    public boolean isFirstStart;
    Context mcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_start_tour);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Intro App Initialize SharedPreferences
                SharedPreferences getSharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                isFirstStart = getSharedPreferences.getBoolean("firstStart", true);

                //  Check either activity or app is open very first time or not and do action
                if (true) { //isFirstStart for real

                    //  Launch application introduction screen
                    Intent i = new Intent(StartTourActivity.this, Onboarding.class);
                    startActivity(i);
                    SharedPreferences.Editor e = getSharedPreferences.edit();
                    e.putBoolean("firstStart", false);
                    e.apply();
                }
            }
        });
        t.start();

        QueryUtils.getSitesFromServer(getApplicationContext(), idlingResource);

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

    public CountingIdlingResource getIdlingResource() {
        return idlingResource;
    }
}
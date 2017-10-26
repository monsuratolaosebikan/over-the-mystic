package org.medfordhistorical.overthemystic;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;

public class StartTourActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    ArrayList<Site> siteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_tour);
        setTitle("New Tour");
        Intent intent = getIntent();
        String tourType = intent.getStringExtra("tourType");
        QueryUtils.getSitesFromServer(getApplicationContext());

        FloatingActionButton goToMapBtn = (FloatingActionButton) findViewById(R.id.go_to_map_btn);
        goToMapBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartTourActivity.this, MapActivity.class);
//                int[] ids =  {7,8,4,10};
//                intent.putExtra("siteIds",ids);
                startActivity(intent);
            }
        });


        ArrayList<Site> sites = getSites();

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        ViewAdapter adapter = new ViewAdapter(getApplicationContext(), sites);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    public ArrayList<Site> getSites(){
        siteList = new ArrayList<Site>();

        String[] imgList = {"https://c1.staticflickr.com/1/133/386986339_48fec1ff5b.jpg",
                "https://res.cloudinary.com/simpleview/image/fetch/c_fill,f_auto,h_346,q_50,w_461/https://SouthwestOntario.simpleviewcrm.com/images/listings/original_Jumbo_The_Elephant_Monument_1.jpg",
                "http://wiseeyesociety.weebly.com/uploads/4/7/7/1/47713501/2355511_orig.jpg",
                "http://chaplaincy.tufts.edu/wp-content/uploads/Goddard_Chapel2.jpg",
                "https://www.masoncontractors.org/images/projects/tufts-university-west-hall-project/tufts-university-west-hall-project-1.jpg"};

        String[] nameList = {"Ballou","Bo", "Braker", "Goddard", "West"};

        for(int i = 0; i < imgList.length; i++) {
            Site newSite = new Site();
            newSite.setName(nameList[i]);
            newSite.setImageUrl(imgList[i]);
            siteList.add(newSite);
        }

        return siteList;
    }

}
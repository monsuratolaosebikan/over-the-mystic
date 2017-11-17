package org.medfordhistorical.overthemystic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class Onboarding extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        addSlide(AppIntroFragment.newInstance("Welcome", "Explore Medford history by foot or bike.", R.drawable.over_the_mystic, getResources().getColor(R.color.alternativeDarkColor)));
        addSlide(AppIntroFragment.newInstance("Choose a Location", "Scroll through historical sites to see what's available.", R.drawable.nav, getResources().getColor(R.color.alternativeDarkColor)));
        addSlide(AppIntroFragment.newInstance("Navigation", "Click the navigation button on a site to choose whether to waxlk or bike.", R.drawable.nav_expanded, getResources().getColor(R.color.alternativeDarkColor)));
        addSlide(AppIntroFragment.newInstance("Arrival", "Once you've arrived, close navigation to see information on the site.", R.drawable.route, getResources().getColor(R.color.alternativeDarkColor)));
        addSlide(AppIntroFragment.newInstance("Site Information", "You can click the play button to start or pause narration.", R.drawable.location_detail, getResources().getColor(R.color.alternativeDarkColor)));
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }
}
package org.medfordhistorical.overthemystic;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class Onboarding extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        addSlide(AppIntroFragment.newInstance("Welcome", "Explore Medford history by foot or bike.", R.drawable.over_the_mystic_logo, getResources().getColor(R.color.alternativeDarkColor)));
        addSlide(AppIntroFragment.newInstance("Choose a Location", "Scroll through historical sites to see what's available.", R.drawable.nav, getResources().getColor(R.color.alternativeDarkColor)));
        addSlide(AppIntroFragment.newInstance("Navigation", "Click the navigation button on a site to choose whether to waxlk or bike.", R.drawable.nav_expanded, getResources().getColor(R.color.alternativeDarkColor)));
        addSlide(AppIntroFragment.newInstance("Arrival", "Once you've arrived, close navigation to see information on the site.", R.drawable.route, getResources().getColor(R.color.alternativeDarkColor)));
        addSlide(AppIntroFragment.newInstance("Site Information", "You can click the play button to start or pause narration.", R.drawable.location_detail, getResources().getColor(R.color.alternativeDarkColor)));
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
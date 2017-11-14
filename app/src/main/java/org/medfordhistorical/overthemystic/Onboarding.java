package org.medfordhistorical.overthemystic;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class Onboarding extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.newInstance("Welcome to Over the Mystic", "Explore Medford history by foot or bike.", R.drawable.over_the_mystic_logo, getResources().getColor(R.color.lightGrey)));
        addSlide(AppIntroFragment.newInstance("Choose a Location", "Scroll through historical sites to see what's available.", R.drawable.ic_action_navigate, getResources().getColor(R.color.primaryColor)));
        addSlide(AppIntroFragment.newInstance("Navigate", "Click the navigation button on a site to choose whether to walk or bike.", R.drawable.ic_action_navigate, getResources().getColor(R.color.primaryColor)));
        addSlide(AppIntroFragment.newInstance("Site Information", "You can click the play button to start or pause narration.", R.drawable.over_the_mystic_logo, getResources().getColor(R.color.primaryColor)));
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
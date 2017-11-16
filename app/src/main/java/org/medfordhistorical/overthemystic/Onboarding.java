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
    public void init(Bundle savedInstanceState) {
        getSupportActionBar().hide();

        addSlide(AppIntroSampleSlider.newInstance(R.layout.slide1));
        addSlide(AppIntroSampleSlider.newInstance(R.layout.slide2));
        addSlide(AppIntroSampleSlider.newInstance(R.layout.slide3));
        addSlide(AppIntroSampleSlider.newInstance(R.layout.slide4));
        addSlide(AppIntroSampleSlider.newInstance(R.layout.slide5));
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
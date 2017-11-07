package org.medfordhistorical.overthemystic;

import android.content.Intent;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ViewLocationDetailActivityTest {

    @Rule
    public ActivityTestRule<ViewLocationDetail> mActivityTestRule = new ActivityTestRule<ViewLocationDetail>(ViewLocationDetail.class) {


        @Override
        protected Intent getActivityIntent() {
            Intent intent = new Intent();
            intent.putExtra("siteId", 1);
            intent.putExtra("siteName", "Ballou Hall");
            intent.putExtra("siteDesc", "Test text");
            intent.putExtra("imageUrl", "http://174.138.43.181/directus/storage/uploads/00000000003.jpg");
            intent.putExtra("audioUrl", "http://174.138.43.181/directus/storage/uploads/00000000004.mp3");
            return intent;
        }
    };

    @Test
    public void descriptionText_isVisibleWihCorrectText() throws Exception{

        onView(withId(R.id.locationDescription)).check(matches(isDisplayed())).check(matches(withText("Test text")));
    }

    @Test
    public void clickAudioFabButton_changesFabIcon() throws Exception {
        ViewInteraction audioFab = onView(withId(R.id.play));

        audioFab.perform(click());

        audioFab.perform(click());
    }
}

package org.medfordhistorical.overthemystic;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MapActivityTest {

    @Rule
    public ActivityTestRule<MapActivity> mActivityTestRule = new ActivityTestRule<>(MapActivity.class);

    @Test
    public void clickSiteNavigationFabButton_showsFabMenu() throws Exception{

        ViewInteraction sitesRecyclerView = onView(withId(R.id.rvSite));

        sitesRecyclerView.perform(
                RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.navigate)));
    }

    @Test
    public void clickSiteNavigationFabButtonBike_opensNavigationUI() throws Exception {

        ViewInteraction sitesRecyclerView = onView(withId(R.id.rvSite));

        sitesRecyclerView.perform(
                RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.navigate_bike)));

        sitesRecyclerView.check(doesNotExist());
    }

    @Test
    public void closeNavigationView_showsSiteDetailView() throws Exception {
        ViewInteraction sitesRecyclerView = onView(withId(R.id.rvSite));

        sitesRecyclerView.perform(
                RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.navigate_bike)));

        pressBack();

        onView(withId(R.id.locationDescription)).check(matches(isDisplayed()));
    }

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }

}

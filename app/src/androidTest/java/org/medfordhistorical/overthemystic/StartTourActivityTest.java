package org.medfordhistorical.overthemystic;

import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class StartTourActivityTest {

    @Rule
    public ActivityTestRule<StartTourActivity> mActivityTestRule = new ActivityTestRule<>(StartTourActivity.class);

    @Test
    public void clickGoToMapButton_opensMapUI() throws Exception{
        Espresso.registerIdlingResources(mActivityTestRule.getActivity().getIdlingResource());

        ViewInteraction floatingActionButton =
                onView(allOf(withId(R.id.go_to_map_btn), isDisplayed()));

        floatingActionButton.perform(click());

        onView(withId(R.id.mapView)).check(matches(isDisplayed()));
    }

    @Test
    public void clickSite_addsSelectedOverlay() throws Exception {
        Espresso.registerIdlingResources(mActivityTestRule.getActivity().getIdlingResource());

        DataInteraction site = onData(anything()).inAdapterView(withId(R.id.gridview)).atPosition(0);

        //check that overlay image is not visible at first
        site.onChildView(withId(R.id.overlayImg)).check(matches(not(isDisplayed())));

        //click first site
        onData(anything())
                .inAdapterView(withId(R.id.gridview))
                .atPosition(0)
                .perform(click());

        //check that overlay image is now visible
        site.onChildView(withId(R.id.overlayImg)).check(matches(isDisplayed()));

        //click first site again to deselect
        onData(anything())
                .inAdapterView(withId(R.id.gridview))
                .atPosition(0)
                .perform(click());

        //check that overlay image is not visible once site is deselected
        site.onChildView(withId(R.id.overlayImg)).check(matches(not(isDisplayed())));
    }

    @Test
    public void addSitesToVisitList() throws Exception {
        Espresso.registerIdlingResources(mActivityTestRule.getActivity().getIdlingResource());

        //select 3 sites
        onData(anything()).atPosition(0).perform(click());
        onData(anything()).atPosition(1).perform(click());
        onData(anything()).atPosition(2).perform(click());

        //go to map view
        onView(allOf(withId(R.id.go_to_map_btn), isDisplayed())).perform(click());

        ViewInteraction sitesRecyclerView = onView(withId(R.id.rvSite));

        //scroll to 2nd site and click to verify existence
        sitesRecyclerView.perform(RecyclerViewActions.scrollToPosition(1));
        sitesRecyclerView.perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        //scroll to 3rd site and click to verify existence
        sitesRecyclerView.perform(RecyclerViewActions.scrollToPosition(2));
        sitesRecyclerView.perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        //scroll to 1st site and click to verify existence
        sitesRecyclerView.perform(RecyclerViewActions.scrollToPosition(0));
        sitesRecyclerView.perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }
}

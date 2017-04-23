package com.vehicles;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.vehicles.api.ApiProvider;
import com.vehicles.api.MockApiEndpoint;
import com.vehicles.util.MatcherUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.vehicles.util.MatcherUtils.withRecyclerView;

@RunWith(AndroidJUnit4.class)
public class VehicleListActivityTest {

    @Rule
    public IntentsTestRule<VehicleListActivity> activityRule = new IntentsTestRule<>(VehicleListActivity.class, true, false);

    @Before
    public void setup() {
        ApiProvider.getInstance().setMockApiEndpoint(new MockApiEndpoint());
    }

    @Test
    public void shouldShowVehicleVrnList() {
        // Start activity
        Intent startIntent = new Intent();
        activityRule.launchActivity(startIntent);

        checkVisibleViews();

        MatcherUtils.rotateScreen(activityRule);

        checkVisibleViews();
    }

    @Test
    public void shouldOpenVehicleDetailWhenItemClicked() {
        // Start activity
        Intent startIntent = new Intent();
        activityRule.launchActivity(startIntent);

        // Click on first item
        onView(withRecyclerView(R.id.vehicles_list).atPosition(0)).perform(click());

        // Verify Vehicle detail page is visible
        checkVehicleDetailViews();

        MatcherUtils.rotateScreen(activityRule);

        checkVehicleDetailViews();
    }

    private void checkVisibleViews() {
        onView(withId(R.id.vehicles_list)).check(matches(isDisplayed()));

        onView(withRecyclerView(R.id.vehicles_list).atPositionOnView(0, R.id.vehicle_name)).check(matches(isDisplayed()));
        onView(withRecyclerView(R.id.vehicles_list).atPositionOnView(0, R.id.vehicle_name)).check(matches(withText("vrn1")));

        onView(withRecyclerView(R.id.vehicles_list).atPositionOnView(1, R.id.vehicle_name)).check(matches(isDisplayed()));
        onView(withRecyclerView(R.id.vehicles_list).atPositionOnView(1, R.id.vehicle_name)).check(matches(withText("vrn2")));
    }

    private void checkVehicleDetailViews() {
        onView(withId(R.id.vehicle_id)).check(matches(isDisplayed()));
        onView(withId(R.id.vehicle_id_value)).check(matches(isDisplayed()));
        onView(withId(R.id.vrn)).check(matches(isDisplayed()));
        onView(withId(R.id.vrn_value)).check(matches(isDisplayed()));
        onView(withId(R.id.county)).check(matches(isDisplayed()));
        onView(withId(R.id.country_value)).check(matches(isDisplayed()));
        onView(withId(R.id.color)).check(matches(isDisplayed()));
        onView(withId(R.id.color_value)).check(matches(isDisplayed()));
        onView(withId(R.id.type)).check(matches(isDisplayed()));
        onView(withId(R.id.type_value)).check(matches(isDisplayed()));
        onView(withId(R.id.default_id)).check(matches(isDisplayed()));
        onView(withId(R.id.default_value)).check(matches(isDisplayed()));

        onView(withId(R.id.vehicle_id_value)).check(matches(withText("id1")));
        onView(withId(R.id.vrn_value)).check(matches(withText("vrn1")));
        onView(withId(R.id.country_value)).check(matches(withText("country1")));
        onView(withId(R.id.color_value)).check(matches(withText("color1")));
        onView(withId(R.id.type_value)).check(matches(withText("type1")));
        onView(withId(R.id.default_value)).check(matches(withText("true")));
    }

}

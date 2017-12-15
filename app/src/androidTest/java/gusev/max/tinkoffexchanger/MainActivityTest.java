package gusev.max.tinkoffexchanger;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import org.junit.Rule;
import org.junit.Test;

import gusev.max.tinkoffexchanger.screen.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by v on 15/12/2017.
 */

public class MainActivityTest {

    private static final String TAG = "MainActivityTest";

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void currencyRecyclerTest() {
        try {
            Thread.sleep(5000);
            onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        } catch (InterruptedException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    @Test
    public void bottomMenuTest() {
        try {
            Thread.sleep(5000);
            onView(withId(R.id.bottom_navigation_view)).check(matches(isDisplayed()));
        } catch (InterruptedException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    @Test
    public void historyTest() {
        try {
            Thread.sleep(5000);
            onView(withId(R.id.history_button)).perform(click());
            onView(withId(R.id.historyRecyclerView)).check(matches(isDisplayed()));
            onView(withId(R.id.action_filter)).check(matches(isDisplayed()));
        } catch (InterruptedException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    @Test
    public void analyticsTest() {
        try {
            Thread.sleep(5000);
            onView(withId(R.id.trends_button)).perform(click());
            onView(withId(R.id.analytics_chart)).check(matches(isDisplayed()));
            onView(withId(R.id.trends_recycler)).check(matches(isDisplayed()));
            onView(withId(R.id.trends_radio_group)).check(matches(isDisplayed()));
        } catch (InterruptedException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    @Test
    public void exchangeOnBackPressTest() {
        try {
            Thread.sleep(500);
            onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
            pressBack();
            onView(withId(R.id.bottom_navigation_view)).check(matches(isDisplayed()));
        } catch (InterruptedException e) {
            Log.d(TAG, e.getMessage());
        }
    }
}
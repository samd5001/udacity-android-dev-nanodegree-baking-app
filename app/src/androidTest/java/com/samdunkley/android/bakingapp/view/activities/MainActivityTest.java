package com.samdunkley.android.bakingapp.view.activities;

import android.content.Intent;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.samdunkley.android.bakingapp.R;
import com.samdunkley.android.bakingapp.utils.FetchIdlingResource;
import com.samdunkley.android.bakingapp.utils.NetworkUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule
            = new ActivityTestRule<>(MainActivity.class, false, false);

    private FetchIdlingResource fetchIdlingResource;

    @Before
    public void setupTests() {
        fetchIdlingResource = NetworkUtils.getFetchIdlingResource();
        IdlingRegistry.getInstance().register(fetchIdlingResource);
        Intents.init();
    }

    @Test
    public void clickRecipe_RecipeDetailListOpens() {
        activityTestRule.launchActivity(new Intent());
        onView(ViewMatchers.withId(R.id.recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        intended(hasComponent(RecipeDetailsListActivity.class.getName()));
    }

    @After
    public void cleanupTests() {
        if (fetchIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(fetchIdlingResource);
        }
        Intents.release();
    }

}

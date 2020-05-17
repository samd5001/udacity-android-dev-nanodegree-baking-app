package com.samdunkley.android.bakingapp.view.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.widget.TextView;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.samdunkley.android.bakingapp.BuildConfig;
import com.samdunkley.android.bakingapp.R;
import com.samdunkley.android.bakingapp.model.Recipe;
import com.samdunkley.android.bakingapp.model.RecipeStep;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailsListActivityTest {

    @Rule
    public ActivityTestRule<RecipeDetailsListActivity> activityTestRule
            = new ActivityTestRule<>(RecipeDetailsListActivity.class, false, false);
    private Recipe mockRecipe;
    private Intent mockIntent;

    public static boolean largeDevice(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @Before
    public void setupTests() {
        mockRecipe = new Recipe(1, "A Recipe", 1, "http://someimageurl.com", new ArrayList<>(), new ArrayList<>());
        mockIntent = new Intent();
        mockIntent.putExtra(RecipeDetailsListActivity.EXTRA_RECIPE, mockRecipe);
        mockIntent.putExtra(RecipeDetailsListActivity.EXTRA_NAME, mockRecipe.getName());
        Intents.init();
    }

    @Test
    public void performClick_ClickIngredient() {
        RecipeDetailsListActivity testActivity = activityTestRule.launchActivity(mockIntent);

        onView(allOf(instanceOf(TextView.class), withParent(withResourceName("action_bar"))))
                .check(matches(withText(mockRecipe.getName())));

        onView(withId(R.id.ingredients_link)).perform(click());
        if (largeDevice(testActivity)) {
            onView(withId(R.id.ingredient_list)).check(matches(isDisplayed()));
        } else {
            intended(hasComponent(RecipeDetailActivity.class.getName()));
        }
    }

    @Test
    public void performClick_ClickStep() {
        mockRecipe.getSteps().add(new RecipeStep("0", "Introduction", "INNNNNTRRRROOOOOODDDDUCCCCTTTTIIIIIIOOOON", "", ""));
        mockIntent.putExtra(RecipeDetailsListActivity.EXTRA_RECIPE, mockRecipe);

        RecipeDetailsListActivity testActivity = activityTestRule.launchActivity(mockIntent);

        onView(allOf(instanceOf(TextView.class), withParent(withResourceName("action_bar"))))
                .check(matches(withText(mockRecipe.getName())));

        onView(withId(R.id.detail_id)).check(matches(withText("Introduction")));
        onView(withId(R.id.detail_text)).check(matches(withText("")));
        onView(withId(R.id.recipestep_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        if (largeDevice(testActivity)) {
            onView(withId(R.id.step_fragment)).check(matches(isDisplayed()));
        } else {
            intended(hasComponent(RecipeDetailActivity.class.getName()));
        }
    }

    @Test
    public void performClick_WidgetToggleOn() {
        RecipeDetailsListActivity testActivity = activityTestRule.launchActivity(mockIntent);

        testActivity.getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE).edit().clear().commit();

        // Closes and launches to reflect updated shared preferences;
        testActivity.finish();
        activityTestRule.launchActivity(mockIntent);

        onView(allOf(instanceOf(TextView.class), withParent(withResourceName("action_bar"))))
                .check(matches(withText(mockRecipe.getName())));

        onView(withId(R.id.pref_show_in_widget)).perform(click());
        onView(withText(R.string.set_widget)).inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void performClick_WidgetToggleOff() {
        RecipeDetailsListActivity testActivity = activityTestRule.launchActivity(mockIntent);

        testActivity.getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE).edit().putInt(RecipeDetailsListActivity.PREF_ID, mockRecipe.getId()).commit();

        // Closes and launches to reflect updated shared preferences;
        testActivity.finish();
        activityTestRule.launchActivity(mockIntent);

        onView(allOf(instanceOf(TextView.class), withParent(withResourceName("action_bar"))))
                .check(matches(withText(mockRecipe.getName())));

        onView(withId(R.id.pref_show_in_widget)).perform(click());
        onView(withText(R.string.clear_widget)).inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @After
    public void cleanupTests() {
        Intents.release();
    }

}

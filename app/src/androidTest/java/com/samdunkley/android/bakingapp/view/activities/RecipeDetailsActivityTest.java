package com.samdunkley.android.bakingapp.view.activities;

import android.content.Intent;
import android.widget.TextView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.samdunkley.android.bakingapp.R;
import com.samdunkley.android.bakingapp.model.Recipe;
import com.samdunkley.android.bakingapp.model.RecipeIngredient;
import com.samdunkley.android.bakingapp.model.RecipeStep;
import com.samdunkley.android.bakingapp.view.fragments.RecipeIngredientsFragment;
import com.samdunkley.android.bakingapp.view.fragments.RecipeStepFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailsActivityTest {

    @Rule
    public ActivityTestRule<RecipeDetailActivity> activityTestRule
            = new ActivityTestRule<>(RecipeDetailActivity.class, false, false);
    private Recipe mockRecipe;
    private Intent mockIntent;

    @Before
    public void setupTests() {
        mockRecipe = new Recipe(1, "A Recipe", 1, "http://someimageurl.com", new ArrayList<>(), new ArrayList<>());
        mockRecipe.getSteps().add(new RecipeStep("0", "Introduction", "INNNNNTRRRROOOOOODDDDUCCCCTTTTIIIIIIOOOON", "", ""));
        mockRecipe.getSteps().add(new RecipeStep("1", "Step 1", "Do Stuff", "", ""));
        mockRecipe.getIngredients().add(new RecipeIngredient(2.0, "kg", "Flour"));
        mockIntent = new Intent();
        mockIntent.putExtra(RecipeDetailsListActivity.EXTRA_RECIPE, mockRecipe);
        mockIntent.putExtra(RecipeDetailsListActivity.EXTRA_NAME, mockRecipe.getName());
    }

    @Test
    public void renderView_RendersIngredients() {
        mockIntent.putExtra(RecipeIngredientsFragment.INGREDIENTS_ARG, mockRecipe.getIngredients());
        activityTestRule.launchActivity(mockIntent);

        onView(allOf(instanceOf(TextView.class), withParent(withResourceName("action_bar"))))
                .check(matches(withText(mockRecipe.getName())));

        onView(withId(R.id.next)).check(matches(isDisplayed()));
        onView(withId(R.id.previous)).check(matches(not(isDisplayed())));
        onView(withId(R.id.ingredient_list)).check(matches(isDisplayed()));
        mockIntent.removeExtra(RecipeIngredientsFragment.INGREDIENTS_ARG);
    }

    @Test
    public void renderView_RendersStep() {
        mockIntent.putExtra(RecipeStepFragment.STEP_ARG, 0);
        activityTestRule.launchActivity(mockIntent);

        onView(allOf(instanceOf(TextView.class), withParent(withResourceName("action_bar"))))
                .check(matches(withText(mockRecipe.getName())));

        onView(withId(R.id.next)).check(matches(isDisplayed()));
        onView(withId(R.id.previous)).check(matches(isDisplayed()));
        onView(withId(R.id.step_fragment)).check(matches(isDisplayed()));
        mockIntent.removeExtra(RecipeStepFragment.STEP_ARG);
    }

    @Test
    public void renderView_RendersLastStep() {
        mockIntent.putExtra(RecipeStepFragment.STEP_ARG, 1);
        activityTestRule.launchActivity(mockIntent);

        onView(allOf(instanceOf(TextView.class), withParent(withResourceName("action_bar"))))
                .check(matches(withText(mockRecipe.getName())));

        onView(withId(R.id.next)).check(matches(not(isDisplayed())));
        onView(withId(R.id.previous)).check(matches(isDisplayed()));
        onView(withId(R.id.step_fragment)).check(matches(isDisplayed()));
        mockIntent.removeExtra(RecipeStepFragment.STEP_ARG);
    }

    @Test
    public void performClick_IncrementIngredients() {
        mockIntent.putExtra(RecipeIngredientsFragment.INGREDIENTS_ARG, mockRecipe.getIngredients());
        activityTestRule.launchActivity(mockIntent);

        onView(allOf(instanceOf(TextView.class), withParent(withResourceName("action_bar"))))
                .check(matches(withText(mockRecipe.getName())));

        onView(withId(R.id.next)).check(matches(isDisplayed()));
        onView(withId(R.id.previous)).check(matches(not(isDisplayed())));
        onView(withId(R.id.ingredient_list)).check(matches(isDisplayed()));

        onView(withId(R.id.next)).perform(click());

        onView(withId(R.id.next)).check(matches(isDisplayed()));
        onView(withId(R.id.previous)).check(matches(isDisplayed()));
        onView(withId(R.id.step_fragment)).check(matches(isDisplayed()));

        mockIntent.removeExtra(RecipeIngredientsFragment.INGREDIENTS_ARG);
    }

    @Test
    public void performClick_IncrementStep() {
        mockIntent.putExtra(RecipeStepFragment.STEP_ARG, 0);
        activityTestRule.launchActivity(mockIntent);

        onView(allOf(instanceOf(TextView.class), withParent(withResourceName("action_bar"))))
                .check(matches(withText(mockRecipe.getName())));

        onView(withId(R.id.next)).check(matches(isDisplayed()));
        onView(withId(R.id.previous)).check(matches(isDisplayed()));
        onView(withId(R.id.step_fragment)).check(matches(isDisplayed()));

        onView(withId(R.id.next)).perform(click());

        onView(withId(R.id.next)).check(matches(not(isDisplayed())));
        onView(withId(R.id.previous)).check(matches(isDisplayed()));
        onView(withId(R.id.step_fragment)).check(matches(isDisplayed()));

        mockIntent.removeExtra(RecipeStepFragment.STEP_ARG);
    }

    @Test
    public void performClick_DecrementStepFromLastToIngredients() {
        mockIntent.putExtra(RecipeStepFragment.STEP_ARG, 1);
        activityTestRule.launchActivity(mockIntent);

        onView(allOf(instanceOf(TextView.class), withParent(withResourceName("action_bar"))))
                .check(matches(withText(mockRecipe.getName())));

        onView(withId(R.id.next)).check(matches(not(isDisplayed())));
        onView(withId(R.id.previous)).check(matches(isDisplayed()));
        onView(withId(R.id.step_fragment)).check(matches(isDisplayed()));

        onView(withId(R.id.previous)).perform(click());

        onView(withId(R.id.next)).check(matches(isDisplayed()));
        onView(withId(R.id.previous)).check(matches(isDisplayed()));
        onView(withId(R.id.step_fragment)).check(matches(isDisplayed()));

        onView(withId(R.id.previous)).perform(click());

        onView(withId(R.id.next)).check(matches(isDisplayed()));
        onView(withId(R.id.previous)).check(matches(not(isDisplayed())));
        onView(withId(R.id.ingredient_list)).check(matches(isDisplayed()));

        mockIntent.removeExtra(RecipeStepFragment.STEP_ARG);
    }

}

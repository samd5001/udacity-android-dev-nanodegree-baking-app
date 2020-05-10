package com.samdunkley.android.bakingapp.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import com.samdunkley.android.bakingapp.R;

/**
 * An activity representing a single RecipeInfo detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeDetailsListActivity}.
 */
public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            Fragment fragment = null;

            Intent intent = getIntent();

            if (actionBar != null && intent.hasExtra(RecipeDetailsListActivity.EXTRA_NAME)) {
                actionBar.setTitle(intent.getStringExtra(RecipeDetailsListActivity.EXTRA_NAME));
            }

            if (intent.hasExtra(RecipeIngredientsFragment.INGREDIENTS_ARG)) {
                arguments.putParcelableArrayList(RecipeIngredientsFragment.INGREDIENTS_ARG,
                        getIntent().getParcelableArrayListExtra(RecipeIngredientsFragment.INGREDIENTS_ARG));
                fragment = new RecipeIngredientsFragment();
                fragment.setArguments(arguments);
            }

            if (intent.hasExtra(RecipeStepFragment.STEP_ARG)) {
                arguments.putParcelable(RecipeStepFragment.STEP_ARG,
                        getIntent().getParcelableExtra(RecipeStepFragment.STEP_ARG));
                fragment = new RecipeStepFragment();
                fragment.setArguments(arguments);
            }


            if (fragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.recipe_detail_container, fragment)
                        .commit();
            }


        }
    }

}

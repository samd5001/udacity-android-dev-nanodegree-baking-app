package com.samdunkley.android.bakingapp.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.samdunkley.android.bakingapp.R;
import com.samdunkley.android.bakingapp.model.Recipe;
import com.samdunkley.android.bakingapp.view.fragments.RecipeIngredientsFragment;
import com.samdunkley.android.bakingapp.view.fragments.RecipeStepFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity {

    private static final int INGREDIENT_POSITION = -1;
    private static final String POSITION_STATE = "position";

    private Recipe recipe;
    private Integer position;
    private String name;

    @BindView(R.id.bottom_nav)
    BottomNavigationView bottomNavigationMenu;
    private Menu navigationMenu;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(RecipeDetailsListActivity.EXTRA_RECIPE, recipe);
        outState.putInt(POSITION_STATE, position);
        outState.putString(RecipeDetailsListActivity.EXTRA_NAME, name);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();

        if (savedInstanceState != null) {
            recipe = savedInstanceState.getParcelable(RecipeDetailsListActivity.EXTRA_RECIPE);
            position = savedInstanceState.getInt(POSITION_STATE);
            name = savedInstanceState.getString(RecipeDetailsListActivity.EXTRA_NAME);
        } else {
            name = intent.hasExtra(RecipeDetailsListActivity.EXTRA_NAME) ? intent.getStringExtra(RecipeDetailsListActivity.EXTRA_NAME) : null;
            recipe = intent.hasExtra(RecipeDetailsListActivity.EXTRA_RECIPE) ? intent.getParcelableExtra(RecipeDetailsListActivity.EXTRA_RECIPE) : null;
            position = intent.hasExtra(RecipeIngredientsFragment.INGREDIENTS_ARG) ? INGREDIENT_POSITION : intent.hasExtra(RecipeStepFragment.STEP_ARG) ? intent.getIntExtra(RecipeStepFragment.STEP_ARG, 0) : null;
        }

        navigationMenu = bottomNavigationMenu.getMenu();

        bottomNavigationMenu.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.previous:
                    decrementFragment();
                    return true;
                case R.id.next:
                    incrementFragment();
                    return true;
                default:
                    return false;
            }
        });

        if (actionBar != null && name != null) {
            actionBar.setTitle(intent.getStringExtra(RecipeDetailsListActivity.EXTRA_NAME));
        }

        if (recipe == null || position == null) {
            return;
         }

        if (INGREDIENT_POSITION == position) {
            setIngredientsFragment();
        } else {
            setStepFragment();
        }
    }

    private void setMenuVisibility() {
        int maxPosition = recipe.getSteps().size() - 1;
        if (position < maxPosition) {
            navigationMenu.findItem(R.id.next).setVisible(true);
        }

        if (position == maxPosition) {
            navigationMenu.findItem(R.id.next).setVisible(false);
        }

        if (position > INGREDIENT_POSITION) {
            navigationMenu.findItem(R.id.previous).setVisible(true);
        }

        if (position == INGREDIENT_POSITION) {
            navigationMenu.findItem(R.id.previous).setVisible(false);
        }
    }

    private void decrementFragment() {
        if (position >= 0) {
            position--;
            updateFragment();
            setMenuVisibility();
        }
    }

    private void incrementFragment() {
        if (position < recipe.getSteps().size() - 1) {
            position++;
            updateFragment();
            setMenuVisibility();

        }
    }

    private void updateFragment() {
        if (position == INGREDIENT_POSITION) {
            setIngredientsFragment();
        } else {
            setStepFragment();
        }
    }

    private void setIngredientsFragment() {
        Bundle arguments = new Bundle();
        arguments.putParcelableArrayList(RecipeIngredientsFragment.INGREDIENTS_ARG, recipe.getIngredients());
        RecipeIngredientsFragment fragment = new RecipeIngredientsFragment();
        fragment.setArguments(arguments);
        setFragment(fragment);
        setMenuVisibility();

    }

    private void setStepFragment() {
        Bundle arguments = new Bundle();
        arguments.putParcelable(RecipeStepFragment.STEP_ARG,
                recipe.getSteps().get(position));
        RecipeStepFragment fragment = new RecipeStepFragment();
        fragment.setArguments(arguments);
        setFragment(fragment);
        setMenuVisibility();

    }

    private void setFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_detail_container, fragment)
                    .commit();
        }

    }



}

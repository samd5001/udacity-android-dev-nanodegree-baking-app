package com.samdunkley.android.bakingapp.view.activities;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.samdunkley.android.bakingapp.BuildConfig;
import com.samdunkley.android.bakingapp.R;

import com.samdunkley.android.bakingapp.adapters.RecipeStepsAdapter;
import com.samdunkley.android.bakingapp.model.Recipe;
import com.samdunkley.android.bakingapp.model.RecipeIngredient;
import com.samdunkley.android.bakingapp.model.RecipeStep;
import com.samdunkley.android.bakingapp.utils.TouchListener;
import com.samdunkley.android.bakingapp.view.fragments.RecipeIngredientsFragment;
import com.samdunkley.android.bakingapp.view.fragments.RecipeStepFragment;
import com.samdunkley.android.bakingapp.widget.IngredientsWidgetProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailsListActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE = "recipe";
    public static final String EXTRA_NAME = "recipe_name";
    public static final String PREF_NAME = EXTRA_NAME;
    public static final String PREF_ID = "recipe_id";
    public static final String PREF_INGREDIENTS = "recipe_ingredients";

    private Recipe recipe;
    private boolean twoPane;
    private SharedPreferences prefs;

    @BindView(R.id.recipestep_list) RecyclerView stepList;
    @BindView(R.id.ingredients_link) TextView ingredientLink;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(EXTRA_RECIPE, recipe);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (prefs.getInt(PREF_ID, 0) == recipe.getId()) {
            menu.findItem(R.id.pref_show_in_widget).setChecked(true).setIcon(R.drawable.baseline_star_24);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.pref_show_in_widget) {
            if (item.isChecked()) {
                item.setChecked(false).setIcon(R.drawable.baseline_star_outline_24);
                prefs.edit().clear().apply();
                Toast.makeText(this, R.string.clear_widget, Toast.LENGTH_SHORT);
            } else {
                item.setChecked(true).setIcon(R.drawable.baseline_star_24);
                StringBuilder ingredientsString = new StringBuilder();

                for (RecipeIngredient recipeIngredient : recipe.getIngredients()) {
                    ingredientsString.append(recipeIngredient.getQuantity()).append(" ").append(recipeIngredient.getIngredient()).append("\n");
                }

                prefs
                        .edit()
                        .putInt(PREF_ID, recipe.getId())
                        .putString(PREF_NAME, recipe.getName())
                        .putString(PREF_INGREDIENTS, ingredientsString.toString())
                        .apply();
                Toast.makeText(this, R.string.set_widget, Toast.LENGTH_SHORT);
            }

            Intent intent = new Intent(this, IngredientsWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
            // since it seems the onUpdate() is only fired on that:
            int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), IngredientsWidgetProvider.class));
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            sendBroadcast(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail_list);
        ButterKnife.bind(this);

        if (findViewById(R.id.recipe_detail_container) != null) {
            twoPane = true;
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();

        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_RECIPE)) {
            recipe = savedInstanceState.getParcelable(EXTRA_RECIPE);
        } else if (intent != null && intent.hasExtra(EXTRA_RECIPE)) {
            recipe = intent.getParcelableExtra(EXTRA_RECIPE);
        }

        if (recipe != null) {
            setupRecyclerView();
            setupIngredientsLink();

            if (actionBar != null) {
                actionBar.setTitle(recipe.getName());
            }
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        prefs = getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE);
    }

    private void setupRecyclerView() {
        stepList.setAdapter(new RecipeStepsAdapter(recipe.getSteps()));
        stepList.addOnItemTouchListener(new TouchListener(this.getApplicationContext(), (view, position) -> {
            RecipeStep recipeDetail = recipe.getSteps().get(position);
            if (twoPane) {
                Bundle arguments = new Bundle();

                arguments.putParcelable(RecipeStepFragment.STEP_ARG, recipeDetail);

                RecipeStepFragment fragment = new RecipeStepFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipe_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, RecipeDetailActivity.class);
                intent.putExtra(EXTRA_NAME, recipe.getName());
                intent.putExtra(RecipeStepFragment.STEP_ARG, position);
                intent.putExtra(RecipeDetailsListActivity.EXTRA_RECIPE, recipe);

                context.startActivity(intent);
            }
        }));
    }

    private void setupIngredientsLink() {
        ingredientLink.setOnClickListener(view -> {
            if (twoPane) {
                Bundle arguments = new Bundle();
                arguments.putParcelableArrayList(RecipeIngredientsFragment.INGREDIENTS_ARG, recipe.getIngredients());

                RecipeIngredientsFragment fragment = new RecipeIngredientsFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipe_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, RecipeDetailActivity.class);
                intent.putExtra(EXTRA_NAME, recipe.getName());
                intent.putExtra(RecipeIngredientsFragment.INGREDIENTS_ARG, recipe.getIngredients());
                intent.putExtra(RecipeStepFragment.STEP_ARG, -1);
                intent.putExtra(RecipeDetailsListActivity.EXTRA_RECIPE, recipe);
                context.startActivity(intent);
            }
        });
    }
}

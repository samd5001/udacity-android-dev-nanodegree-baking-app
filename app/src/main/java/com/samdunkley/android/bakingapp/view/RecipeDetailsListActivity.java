package com.samdunkley.android.bakingapp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.samdunkley.android.bakingapp.R;

import com.samdunkley.android.bakingapp.adapters.RecipeStepsAdapter;
import com.samdunkley.android.bakingapp.model.Recipe;
import com.samdunkley.android.bakingapp.model.RecipeStep;
import com.samdunkley.android.bakingapp.utils.TouchListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailsListActivity extends AppCompatActivity {

    static final String EXTRA_RECIPE = "recipe";
    public static final String EXTRA_NAME = "recipe_name";

    private Recipe recipe;
    private boolean twoPane;

    @BindView(R.id.recipestep_list) RecyclerView stepList;
    @BindView(R.id.ingredients_link) TextView ingredientLink;

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

        if (intent != null && intent.hasExtra(EXTRA_RECIPE)) {
            recipe = intent.getParcelableExtra(EXTRA_RECIPE);

            if (recipe != null) {
                setupRecyclerView();
                setupIngredientsLink();

                if (actionBar != null) {
                    actionBar.setTitle(recipe.getName());
                }
            }
        }


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
                intent.putExtra(RecipeStepFragment.STEP_ARG, recipeDetail);
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
                context.startActivity(intent);
            }
        });
    }
}

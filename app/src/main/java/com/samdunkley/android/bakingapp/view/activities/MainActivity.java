package com.samdunkley.android.bakingapp.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.samdunkley.android.bakingapp.R;
import com.samdunkley.android.bakingapp.adapters.RecipeAdapter;
import com.samdunkley.android.bakingapp.model.Recipe;
import com.samdunkley.android.bakingapp.utils.NetworkUtils;
import com.samdunkley.android.bakingapp.utils.TouchListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String RECIPE_STATE_KEY = "recipes";

    @BindView(R.id.recipe_list)
    RecyclerView recipesView;

    private ArrayList<Recipe> recipes;


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(RECIPE_STATE_KEY, recipes);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        boolean getData = true;

        if (savedInstanceState != null && savedInstanceState.containsKey(RECIPE_STATE_KEY)) {
            getData = false;
            recipes = savedInstanceState.getParcelableArrayList(RECIPE_STATE_KEY);
        } else {
            recipes = new ArrayList<>();
        }

        recipesView.setHasFixedSize(true);
        int columns = calculateNoOfColumns();
        if (columns == 1) {
            recipesView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            recipesView.setLayoutManager(new GridLayoutManager(this, columns));
        }

        RecipeAdapter recipeAdapter = new RecipeAdapter(recipes);
        recipesView.setAdapter(recipeAdapter);

        recipesView.addOnItemTouchListener(new TouchListener(this.getApplicationContext(), (view, position) -> {
            Intent intent = new Intent(this, RecipeDetailsListActivity.class);
            intent.putExtra(RecipeDetailsListActivity.EXTRA_RECIPE, recipes.get(position));
            startActivity(intent);
        }));

        if (getData) {
            NetworkUtils.getAndSetRecipes(recipes, recipeAdapter, this);
        }

    }

    private int calculateNoOfColumns() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (screenWidthDp / 320 + 0.5); // +0.5 for correct rounding to int.
    }
}

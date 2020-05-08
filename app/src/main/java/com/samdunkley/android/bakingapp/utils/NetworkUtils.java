package com.samdunkley.android.bakingapp.utils;

import androidx.annotation.NonNull;

import com.samdunkley.android.bakingapp.adapters.RecipeAdapter;
import com.samdunkley.android.bakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {

    private static final String RECIPE_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    private static final Retrofit retrofit = new Retrofit
            .Builder()
            .baseUrl(RECIPE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    public static void getAndSetRecipes(@NonNull ArrayList<Recipe> recipes, RecipeAdapter adapter) {
        Call<List<Recipe>> callGetRecipes = retrofit.create(RecipeEndpointInterface.class).getRecipes();


        callGetRecipes.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                List<Recipe> responseRecipes = response.body();
                recipes.clear();
                recipes.addAll(responseRecipes);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                t.getCause();
            }
        });
    }
}

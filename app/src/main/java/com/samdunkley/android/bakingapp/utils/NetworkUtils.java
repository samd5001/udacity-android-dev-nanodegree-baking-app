package com.samdunkley.android.bakingapp.utils;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.samdunkley.android.bakingapp.R;
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

    private static FetchIdlingResource fetchIdlingResource;


    public static void getAndSetRecipes(@NonNull ArrayList<Recipe> recipes, RecipeAdapter adapter, Context context) {
        Call<List<Recipe>> callGetRecipes = retrofit.create(RecipeEndpointInterface.class).getRecipes();


        callGetRecipes.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                List<Recipe> responseRecipes = response.body();
                if (responseRecipes != null) {
                    recipes.clear();
                    recipes.addAll(responseRecipes);
                    adapter.notifyDataSetChanged();
                }

                if (fetchIdlingResource == null) {
                    fetchIdlingResource = new FetchIdlingResource();
                }
                fetchIdlingResource.setIdleState();

            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                t.printStackTrace();
                if (fetchIdlingResource == null) {
                    fetchIdlingResource = new FetchIdlingResource();
                }
                fetchIdlingResource.setIdleState();
                Toast.makeText(context, R.string.network_error_text, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static FetchIdlingResource getFetchIdlingResource() {
        if (fetchIdlingResource == null) {
            fetchIdlingResource = new FetchIdlingResource();
        }

        return fetchIdlingResource;
    }

}

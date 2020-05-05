package com.samdunkley.android.bakingapp.utils;

import com.samdunkley.android.bakingapp.model.Recipe;

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


    public void getAndSetRecipes() {
        Call<List<Recipe>> callGetRecipes = retrofit.create(RecipeEndpointInterface.class).getRecipes();


        callGetRecipes.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                // TODO: handle response
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                // TODO: handle error
            }
        });
    }
}

package com.samdunkley.android.bakingapp.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.samdunkley.android.bakingapp.R;
import com.samdunkley.android.bakingapp.adapters.RecipeIngredientsAdapter;
import com.samdunkley.android.bakingapp.model.RecipeIngredient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeIngredientsFragment extends Fragment {

    public static final String INGREDIENTS_ARG = "recipe_ingredients";
    @BindView(R.id.ingredient_list)
    RecyclerView ingredientList;
    private ArrayList<RecipeIngredient> ingredients;

    public RecipeIngredientsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            ingredients = getArguments().getParcelableArrayList(INGREDIENTS_ARG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_ingredients_list, container, false);
        ButterKnife.bind(this, view);


        Context context = view.getContext();
        ingredientList.setLayoutManager(new LinearLayoutManager(context));
        ingredientList.setAdapter(new RecipeIngredientsAdapter(ingredients));
        return ingredientList;
    }

}

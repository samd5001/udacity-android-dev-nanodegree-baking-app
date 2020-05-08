package com.samdunkley.android.bakingapp.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.samdunkley.android.bakingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.recipe_image) ImageView recipeImageView;
    @BindView(R.id.recipe_name) TextView recipeNameView;

    RecipeHolder(@NonNull View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onClick(View view) {

    }
}
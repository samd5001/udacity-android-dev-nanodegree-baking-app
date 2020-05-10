package com.samdunkley.android.bakingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.samdunkley.android.bakingapp.R;
import com.samdunkley.android.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {

    private List<Recipe> recipes;

    public RecipeAdapter(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View recipeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card, parent, false);
        return new RecipeHolder(recipeView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeHolder holder, int position) {
        Recipe recipe = recipes.get(position);

        if (!recipe.getImage().isEmpty()) {
            Picasso
                    .get()
                    .load(recipe.getImage())
                    .error(R.drawable.no_image_found)
                    .fit()
                    .into(holder.recipeImageView);
        } else {
            holder.recipeImageView.setImageResource(R.drawable.no_image_found);
        }
        holder.recipeNameView.setText(recipe.getName());
    }

    @Override
    public int getItemCount() {
        return recipes == null ? 0 : recipes.size();
    }

    class RecipeHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recipe_image) ImageView recipeImageView;
        @BindView(R.id.recipe_name) TextView recipeNameView;

        RecipeHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
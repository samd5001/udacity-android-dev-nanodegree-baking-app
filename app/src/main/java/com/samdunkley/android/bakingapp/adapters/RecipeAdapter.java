package com.samdunkley.android.bakingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.samdunkley.android.bakingapp.R;
import com.samdunkley.android.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeHolder> {

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

}
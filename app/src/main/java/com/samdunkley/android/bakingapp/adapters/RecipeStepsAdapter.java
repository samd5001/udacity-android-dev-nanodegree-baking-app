package com.samdunkley.android.bakingapp.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.samdunkley.android.bakingapp.R;
import com.samdunkley.android.bakingapp.model.RecipeStep;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.RecipeStepHolder> {

    private final List<RecipeStep> recipeSteps;

    public RecipeStepsAdapter(
                                  List<RecipeStep> recipeSteps
                                  ) {
        this.recipeSteps = recipeSteps;
    }

    @Override
    public RecipeStepHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_detail_list_content, parent, false);
        return new RecipeStepHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecipeStepHolder holder, int position) {
        RecipeStep recipeStep = recipeSteps.get(position);

        holder.detailIdView.setText(recipeStep.getId());
        holder.detailTextView.setText(recipeStep.getShortDescription());
    }

    @Override
    public int getItemCount() {
        return recipeSteps.size();
    }

    class RecipeStepHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.detail_id)
        TextView detailIdView;
        @BindView(R.id.detail_text)
        TextView detailTextView;

        RecipeStepHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

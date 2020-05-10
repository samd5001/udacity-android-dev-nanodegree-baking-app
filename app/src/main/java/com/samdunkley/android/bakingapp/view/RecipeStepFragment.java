package com.samdunkley.android.bakingapp.view;

import android.os.Bundle;

import com.google.android.exoplayer2.SimpleExoPlayer;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.samdunkley.android.bakingapp.R;
import com.samdunkley.android.bakingapp.model.RecipeStep;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepFragment extends Fragment {

    public static final String STEP_ARG = "recipe_step";

    private RecipeStep recipeStep;

    @BindView(R.id.recipe_step_image) ImageView thumbnail;
    @BindView(R.id.recipe_step_instruction) TextView instructions;
    SimpleExoPlayer exoPlayer;

    public RecipeStepFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(STEP_ARG)) {

            recipeStep = getArguments().getParcelable(STEP_ARG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!recipeStep.getVideoURL().isEmpty()) {
            setupPlayer();
        }

        if (!recipeStep.getThumbnailURL().isEmpty()) {
                Picasso
                        .get()
                        .load(recipeStep.getThumbnailURL())
                        .error(R.drawable.no_image_found)
                        .fit()
                        .into(thumbnail);
        } else {
                thumbnail.setImageResource(R.drawable.no_image_found);
        }

        instructions.setText(recipeStep.getDescription());
    }

    public void setupPlayer() {
    }

}

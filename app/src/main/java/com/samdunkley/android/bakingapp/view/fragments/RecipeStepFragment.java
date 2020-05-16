package com.samdunkley.android.bakingapp.view.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.exoplayer2.SimpleExoPlayer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.samdunkley.android.bakingapp.R;
import com.samdunkley.android.bakingapp.model.RecipeStep;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepFragment extends Fragment {

    public static final String STEP_ARG = "recipe_step";
    private static final String POSITION = "position";
    private static final String PLAY_WHEN_READY = "play";

    private RecipeStep recipeStep;

    @BindView(R.id.recipe_step_image) ImageView thumbnail;
    @BindView(R.id.recipe_step_instruction) TextView instructions;
    @BindView(R.id.recipe_step_exo_player) PlayerView playerView;

    private SimpleExoPlayer exoPlayer;
    private long playerPosition = 0;
    private boolean playWhenReady = false;

    public RecipeStepFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(STEP_ARG)) {
            recipeStep = getArguments().getParcelable(STEP_ARG);
        }

        if (savedInstanceState != null) {
            playerPosition = savedInstanceState.getLong(POSITION);
            playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
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
    public void onPause() {
        super.onPause();
        if (exoPlayer != null) {
            playerPosition = exoPlayer.getCurrentPosition();
            playWhenReady = exoPlayer.getPlayWhenReady();
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!recipeStep.getVideoURL().isEmpty()) {
            setupPlayer();
        } else if (!recipeStep.getThumbnailURL().isEmpty()) {
                Picasso
                        .get()
                        .load(recipeStep.getThumbnailURL())
                        .error(R.drawable.no_image_found)
                        .fit()
                        .into(thumbnail);

                thumbnail.setVisibility(View.VISIBLE);
        }

        instructions.setText(recipeStep.getDescription());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(POSITION, playerPosition);
        outState.putBoolean(PLAY_WHEN_READY, playWhenReady);
    }

    private void setupPlayer() {
        Context context = getContext();

        if (context != null) {
            if (exoPlayer == null) {
                exoPlayer = new SimpleExoPlayer.Builder(context).build();
            }

            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
            playerView.setPlayer(exoPlayer);

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                    Util.getUserAgent(context, "BakingApp"));

            MediaSource videoSource =
                    new ProgressiveMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(Uri.parse(recipeStep.getVideoURL()));
            exoPlayer.prepare(videoSource);
            if (playerPosition > 0) {
                exoPlayer.seekTo(playerPosition);
            }
            exoPlayer.setPlayWhenReady(playWhenReady);

            playerView.setVisibility(View.VISIBLE);
        }
    }



}

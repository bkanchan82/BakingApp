package com.example.android.bakingapp;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.bakingapp.Utilities.RecipeStep;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

/**
 * Created by kanchan on 28-02-2018.
 */

public class VideoPlaybackFragment extends Fragment {

    private static final String TAG = VideoPlaybackFragment.class.getSimpleName();
    private static final String RECIPE_STEP = "recipe_step";
    private static final String VIDEO_SEEKER_POSITION = "video_seekbar_position";
    private static final String VIDEO_PLAYING_STATUS = "video_playing_status";

    private SimpleExoPlayerView mPlayerView;
    private ImageView mRecipeStepImageView;
    private SimpleExoPlayer mExoPlayer;

    private RecipeStep recipeStep;
    private long videoPositionOnSeekBar;
    private boolean playWhenReady = true;

    public void setRecipe(RecipeStep recipe) {
        recipeStep = recipe;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_video_palyback, container, false);
        mPlayerView = view.findViewById(R.id.video_view);
        mRecipeStepImageView = view.findViewById(R.id.iv_display_recipe_step_thumbnail);

        videoPositionOnSeekBar = C.TIME_UNSET;
        if (savedInstanceState != null) {
            recipeStep = savedInstanceState.getParcelable(RECIPE_STEP);
            videoPositionOnSeekBar = savedInstanceState.getLong(VIDEO_SEEKER_POSITION, C.TIME_UNSET);
            playWhenReady = savedInstanceState.getBoolean(VIDEO_PLAYING_STATUS, true);

        }

        return view;
    }


    private void showVideo() {
        mPlayerView.setVisibility(View.VISIBLE);
        mRecipeStepImageView.setVisibility(View.GONE);
    }

    private void showImage(RecipeStep recipeStep) {
        mPlayerView.setVisibility(View.GONE);
        mRecipeStepImageView.setVisibility(View.VISIBLE);

        if (recipeStep.getThumbnailUrl() != null && recipeStep.getThumbnailUrl().length() > 0) {
            Picasso.with(getActivity()).load(recipeStep.getThumbnailUrl()).into(mRecipeStepImageView);
        }

    }

    private void initializePlayer(RecipeStep recipeStep) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
//            hideSystemUi();

            Log.d(TAG, "Video Url : " + recipeStep.getVideoUrl());
            if (recipeStep.getVideoUrl() != null && recipeStep.getVideoUrl().length() > 0) {
                showVideo();
                Uri uri = Uri.parse(recipeStep.getVideoUrl());
                String userAgent = Util.getUserAgent(getActivity(), "Backing App");
                MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(
                        getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
                mExoPlayer.prepare(mediaSource);
                Log.d(TAG,"Video Posiotion at seekbar : "+videoPositionOnSeekBar);
                mExoPlayer.seekTo(videoPositionOnSeekBar);
                mExoPlayer.setPlayWhenReady(playWhenReady);
            } else {
                showImage(recipeStep);
            }
        }
    }

   /* @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        mPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }*/


    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            videoPositionOnSeekBar = mExoPlayer.getCurrentPosition();
            playWhenReady = mExoPlayer.getPlayWhenReady();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (recipeStep != null) {
            initializePlayer(recipeStep);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(RECIPE_STEP, recipeStep);
//        videoPositionOnSeekBar = mExoPlayer.getCurrentPosition();
        outState.putLong(VIDEO_SEEKER_POSITION, videoPositionOnSeekBar);
        outState.putBoolean(VIDEO_PLAYING_STATUS, playWhenReady);

    }
}

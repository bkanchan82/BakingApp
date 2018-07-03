package com.example.android.bakingapp;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.android.bakingapp.Utilities.RecipeStep;

import java.util.ArrayList;

public class VideoPlaybackActivity extends AppCompatActivity
        implements StepNavigationFragment.OnFragmentInteractionListener {

    private static final String TAG = VideoPlaybackActivity.class.getSimpleName();
    public static final String SELECTED_RECIPE_STEP_ARRAY = "selected_recipe_step_array";
    public static final String SELECTED_RECIPE_STEP = "selected_recipe_step";
    private static final String SELECTED_RECIPE = "selected_recipe";

    private ArrayList<RecipeStep> mRecipeStepArrayList;
    private int mStep;
    private RecipeStep mRecipeStep;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_playback);

        if(getSupportActionBar()!= null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Bundle bundle = getIntent().getExtras();
        mRecipeStepArrayList = bundle.getParcelableArrayList(SELECTED_RECIPE_STEP_ARRAY);
        mStep = bundle.getInt(SELECTED_RECIPE_STEP);

        // Only create new fragments when there is no previously saved state
        if (savedInstanceState != null) {
            mRecipeStepArrayList = savedInstanceState.getParcelableArrayList(SELECTED_RECIPE_STEP_ARRAY);
            mRecipeStep = savedInstanceState.getParcelable(SELECTED_RECIPE);
            mStep = savedInstanceState.getInt(SELECTED_RECIPE_STEP);

            Log.d(TAG, "Step Count : " + mStep + " mRecipe Array list count : " + mRecipeStepArrayList.size());

            if (mStep <= mRecipeStepArrayList.size()) {
                mRecipeStep = mRecipeStepArrayList.get(mStep);
            }

        }else{
            // Add the fragment to its container using a FragmentManager and a Transaction
            FragmentManager fragmentManager = getSupportFragmentManager();

            VideoPlaybackFragment masterRecipeFragment = new VideoPlaybackFragment();
            if (mStep <= mRecipeStepArrayList.size()) {
                mRecipeStep = mRecipeStepArrayList.get(mStep);
            }
            if (mRecipeStep != null) {
                masterRecipeFragment.setRecipe(mRecipeStepArrayList.get(mStep));
            }

            fragmentManager.beginTransaction()
                    .add(R.id.fl_video_player_container, masterRecipeFragment)
                    .commit();

            StepDescriptionFragment stepDescriptionFragment = new StepDescriptionFragment();

            if (mRecipeStep != null) {
                stepDescriptionFragment.setRecipeStep(mRecipeStep);
            }
            fragmentManager.beginTransaction()
                    .add(R.id.fl_display_step_description, stepDescriptionFragment)
                    .commit();

            StepNavigationFragment stepNavigationFragment = new StepNavigationFragment();

            fragmentManager.beginTransaction()
                    .add(R.id.fl_navigation, stepNavigationFragment)
                    .commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedMenuId = item.getItemId();

        switch (selectedMenuId) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(int previousOrNext) {

        Log.d(TAG,"On Fragment Interaction : "+previousOrNext);

        if (previousOrNext == 0) {
            mStep--;
        } else if (previousOrNext == 1) {
            mStep++;
        }

        if (mStep < mRecipeStepArrayList.size() && mStep >= 0) {
            mRecipeStep = mRecipeStepArrayList.get(mStep);
        } else if (mStep < -1) {
            mStep = mRecipeStepArrayList.size() - 1;
            mRecipeStep = mRecipeStepArrayList.get(mStep);
        } else {
            mRecipeStep = mRecipeStepArrayList.get(0);
            mStep = 0;
        }


        VideoPlaybackFragment masterRecipeFragment = new VideoPlaybackFragment();

        if (mRecipeStep != null) {
            masterRecipeFragment.setRecipe(mRecipeStepArrayList.get(mStep));
        }

        // Add the fragment to its container using a FragmentManager and a Transaction
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.fl_video_player_container, masterRecipeFragment)
                .commit();

        StepDescriptionFragment stepDescriptionFragment = new StepDescriptionFragment();

        if (mRecipeStep != null) {
            stepDescriptionFragment.setRecipeStep(mRecipeStep);
        }
        fragmentManager.beginTransaction()
                .replace(R.id.fl_display_step_description, stepDescriptionFragment)
                .commit();

        StepNavigationFragment stepNavigationFragment = new StepNavigationFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.fl_navigation, stepNavigationFragment)
                .commit();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SELECTED_RECIPE_STEP_ARRAY, mRecipeStepArrayList);
        outState.putInt(SELECTED_RECIPE_STEP, mStep);
        outState.putParcelable(SELECTED_RECIPE, mRecipeStep);
    }
}

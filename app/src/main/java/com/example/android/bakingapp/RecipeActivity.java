package com.example.android.bakingapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.android.bakingapp.Utilities.Ingredient;
import com.example.android.bakingapp.Utilities.Recipe;
import com.example.android.bakingapp.Utilities.RecipeStep;
import com.example.android.bakingapp.data.IngredientsColumns;
import com.example.android.bakingapp.data.RecipeColumns;
import com.example.android.bakingapp.data.RecipeProvider;
import com.example.android.bakingapp.data.StepsColumns;

import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity implements
        MasterStepListAdapter.RecipeStepsAdapterOnItemClickListener {

    private static final String TAG = RecipeActivity.class.getSimpleName();
    public static final String SELECTED_RECIPE = "selected_recipe";
    public static final String SELECTED_RECIPE_ID = "selected_recipe_id";
    private static final String SAVED_RECIPE_STEP_LAYOUT_MANAGER = "saved_recipe_step_layout_manager";
    private static final String SAVED_INGREDIENT_LAYOUT_MANAGER = "saved_ingredient_layout_manager";
    private static final String SCROLL_POSITION = "scroll_position";

    private boolean twoPane;
    private ArrayList<RecipeStep> recipeStepArrayList;
    // A single-pane display refers to phone screens, and two-pane to larger tablet screens

    private MasterStepListAdapter mMasterStepListAdapter;
//    private final ArrayList<RecipeStep> mStepArrayList = new ArrayList<>();

    RecyclerView recyclerViewIngredient;
    RecyclerView recyclerViewSteps;

    ImageView markAsFavoriteIv;
    ScrollView scrollView;

    Recipe mRecipe = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (intent.hasExtra(SELECTED_RECIPE)) {
            mRecipe = intent.getParcelableExtra(SELECTED_RECIPE);
        }else if(intent.hasExtra(SELECTED_RECIPE_ID)){
            int recipeId = intent.getIntExtra(SELECTED_RECIPE_ID,0);
            Cursor selectedRecipeCursor = getContentResolver().query(RecipeProvider.Recipes.URI,
                    null,
                    RecipeColumns._ID+" = '"+recipeId+"'",
                    null,
                    null);
            if(selectedRecipeCursor.moveToFirst()){
                mRecipe = new Recipe(recipeId,
                        selectedRecipeCursor.getString(selectedRecipeCursor.getColumnIndex(RecipeColumns.COLUMN_RECIPE_NAME)),
                        selectedRecipeCursor.getString(selectedRecipeCursor.getColumnIndex(RecipeColumns.COLUMN_IMAGE_URL)),
                        selectedRecipeCursor.getInt(selectedRecipeCursor.getColumnIndex(RecipeColumns.COLUMN_SERVING_COUNT)),
                        selectedRecipeCursor.getString(selectedRecipeCursor.getColumnIndex(RecipeColumns.COLUMN_MARK_FAVORITE)));

            }
            selectedRecipeCursor.close();
        }

        TextView recipeTitleTextView = findViewById(R.id.tv_display_recipe_title);
        scrollView = findViewById(R.id.scroll_view);
        markAsFavoriteIv = findViewById(R.id.iv_mark_as_favorite);
        changeFavoriteIcon();
        if (mRecipe != null) {
            recipeTitleTextView.setText(mRecipe.getRecipeName());
        }


        recyclerViewIngredient = findViewById(R.id.rv_display_ingredient_list);
        recyclerViewSteps = findViewById(R.id.rv_display_recipe_steps);

        // Create the adapter
        // This adapter takes in the context and an ArrayList of ALL the image resources to display
        MasterIngredientListAdapter masterIngredientListAdapter = new MasterIngredientListAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        // Set the adapter on the GridView
        recyclerViewIngredient.setLayoutManager(linearLayoutManager);
        recyclerViewIngredient.setHasFixedSize(true);
        recyclerViewIngredient.setAdapter(masterIngredientListAdapter);

        mMasterStepListAdapter = new MasterStepListAdapter(this);
        LinearLayoutManager stepsLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        // Set the adapter on the GridView
        recyclerViewSteps.setLayoutManager(stepsLinearLayoutManager);
        recyclerViewSteps.setHasFixedSize(true);

        recyclerViewSteps.setAdapter(mMasterStepListAdapter);

        Log.d(TAG, mRecipe.getRecipeName());
        RecipeStep recipeStep = null;
        if (mRecipe != null) {

            String[] mProjection = new String[]{
                    IngredientsColumns._ID,
                    IngredientsColumns.COLUMN_INGREDIENT,
                    IngredientsColumns.COLUMN_MEASURING_UNIT,
                    IngredientsColumns.COLUMN_QUANTITY,
                    IngredientsColumns.COLUMN_RECIPE_ID
            };
            int ID_INDEX = 0;
            int INGREDIENT_INDEX = 1;
            int MEASURING_UNIT_INDEX = 2;
            int QUANTITY_INDEX = 3;
            int RECIPE_ID_INDEX = 4;


            Cursor ingredientCursor = getContentResolver().query(
                    RecipeProvider.Ingredients.URI,
                    mProjection,
                    IngredientsColumns.COLUMN_RECIPE_ID + "=?",
                    new String[]{String.valueOf(mRecipe.getId())},
                    null
            );

            Log.d(TAG, "INGREDIENT COUNT : " + ingredientCursor.getCount());
            ArrayList<Ingredient> ingredients = new ArrayList<>();
            if (ingredientCursor.moveToFirst()) {
                do {
//                    int id, String recipeId, int quantity, String measuringUnit, String ingredient
                    ingredients.add(new Ingredient(ingredientCursor.getInt(ID_INDEX),
                            String.valueOf(ingredientCursor.getInt(RECIPE_ID_INDEX)),
                            ingredientCursor.getInt(QUANTITY_INDEX),
                            ingredientCursor.getString(MEASURING_UNIT_INDEX),
                            ingredientCursor.getString(INGREDIENT_INDEX)));
                } while (ingredientCursor.moveToNext());

                masterIngredientListAdapter.setIngredientArrayList(ingredients);
            }
            ingredientCursor.close();


            recipeStepArrayList = new ArrayList<>();
            String[] mStepsProjection = new String[]{
                    StepsColumns._ID,
                    StepsColumns.COLUMN_RECIPE_ID,
                    StepsColumns.COLUMN_DESCRIPTION,
                    StepsColumns.COLUMN_SHORT_DESCRIPTION,
                    StepsColumns.COLUMN_VIDEO_URL,
                    StepsColumns.COLUMN_THUMBNAIL_URL
            };
            int STEP_ID_INDEX = 0;
            int STEP_RECIPE_ID_INDEX = 1;
            int STEP_DESCRIPTION_INDEX = 2;
            int STEP_SHORT_DESCRIPTION_INDEX = 3;
            int STEP_VIDEO_URL_INDEX = 4;
            int STEP_THUMBNAIL_URL_INDEX = 4;


            Cursor stepsCursor = getContentResolver().query(
                    RecipeProvider.Steps.URI,
                    mStepsProjection,
                    IngredientsColumns.COLUMN_RECIPE_ID + "=?",
                    new String[]{String.valueOf(mRecipe.getId())},
                    null
            );


            if (stepsCursor.moveToFirst()) {
                recipeStep = new RecipeStep(stepsCursor.getInt(STEP_ID_INDEX),
                        String.valueOf(stepsCursor.getInt(STEP_RECIPE_ID_INDEX)),
                        stepsCursor.getString(STEP_SHORT_DESCRIPTION_INDEX),
                        stepsCursor.getString(STEP_DESCRIPTION_INDEX),
                        stepsCursor.getString(STEP_VIDEO_URL_INDEX),
                        stepsCursor.getString(STEP_THUMBNAIL_URL_INDEX));
                do {
                    recipeStepArrayList.add(new RecipeStep(stepsCursor.getInt(STEP_ID_INDEX),
                            String.valueOf(stepsCursor.getInt(STEP_RECIPE_ID_INDEX)),
                            stepsCursor.getString(STEP_SHORT_DESCRIPTION_INDEX),
                            stepsCursor.getString(STEP_DESCRIPTION_INDEX),
                            stepsCursor.getString(STEP_VIDEO_URL_INDEX),
                            stepsCursor.getString(STEP_THUMBNAIL_URL_INDEX)));
                } while (stepsCursor.moveToNext());

                mMasterStepListAdapter.setRecipeArrayList(recipeStepArrayList);
            }
            stepsCursor.close();

        }

        if (savedInstanceState != null) {
            Parcelable recipeStepLayoutManagerSavedState = savedInstanceState.getParcelable(SAVED_RECIPE_STEP_LAYOUT_MANAGER);
            Parcelable ingredientLayoutManagerSavedState = savedInstanceState.getParcelable(SAVED_RECIPE_STEP_LAYOUT_MANAGER);
            restoreLayoutManagerPosition(recipeStepLayoutManagerSavedState, ingredientLayoutManagerSavedState);

            final int[] position = savedInstanceState.getIntArray(SCROLL_POSITION);
            if(position != null)
                scrollView.post(new Runnable() {
                    public void run() {
                        scrollView.scrollTo(position[0], position[1]);
                    }
                });
        }


        if (findViewById(R.id.video_playback_layout) == null) {
            twoPane = false;

        } else {
            twoPane = true;

            if (recipeStep != null) {

                if (savedInstanceState == null) {

                    // Add the fragment to its container using a FragmentManager and a Transaction
                    FragmentManager fragmentManager = getSupportFragmentManager();

                    VideoPlaybackFragment videoPlaybackFragment = new VideoPlaybackFragment();

                    videoPlaybackFragment.setRecipe(recipeStep);

                    fragmentManager.beginTransaction()
                            .add(R.id.fl_video_player_container, videoPlaybackFragment)
                            .commit();

                    StepDescriptionFragment stepDescriptionFragment = new StepDescriptionFragment();

                    stepDescriptionFragment.setRecipeStep(recipeStep);
                    fragmentManager.beginTransaction()
                            .add(R.id.fl_display_step_description, stepDescriptionFragment)
                            .commit();
                }
            }

        }
    }

    private void restoreLayoutManagerPosition(Parcelable recipeStepLayoutManagerSavedState, Parcelable ingredientLayoutManagerSavedState) {
        if (recipeStepLayoutManagerSavedState != null) {
            recyclerViewSteps.getLayoutManager().onRestoreInstanceState(recipeStepLayoutManagerSavedState);
        }
        if (ingredientLayoutManagerSavedState != null) {
            recyclerViewIngredient.getLayoutManager().onRestoreInstanceState(ingredientLayoutManagerSavedState);
        }
    }

    private void changeFavoriteIcon(){
        if (Boolean.parseBoolean(mRecipe.getMarkAsFavorite())) {
            markAsFavoriteIv.setImageResource(R.drawable.ic_favorite_red_24dp);
        } else {
            markAsFavoriteIv.setImageResource(R.drawable.ic_favorite_gray_24dp);
        }
    }


    public void markAsFavorite(View view) {
        if (Boolean.parseBoolean(mRecipe.getMarkAsFavorite())) {
            mRecipe.setMarkAsFavorite("false");
        } else {
            mRecipe.setMarkAsFavorite("true");
        }
        changeFavoriteIcon();

        ContentValues values = new ContentValues();
        values.put(RecipeColumns.COLUMN_RECIPE_NAME,mRecipe.getRecipeName());
        values.put(RecipeColumns.COLUMN_SERVING_COUNT,mRecipe.getServingCount());
        values.put(RecipeColumns.COLUMN_IMAGE_URL,mRecipe.getImageUrl());
        values.put(RecipeColumns.COLUMN_MARK_FAVORITE,mRecipe.getMarkAsFavorite());
        int updatedRowCount = getContentResolver().update(RecipeProvider.Recipes.URI,
                values,
                RecipeColumns._ID+" = '"+mRecipe.getId()+"'",
                null);
        Log.d(TAG,"Recipe update row count : "+updatedRowCount);

    }
/*

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState != null)
        {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }
*/


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putParcelable(SAVED_RECIPE_STEP_LAYOUT_MANAGER, recyclerViewSteps.getLayoutManager().onSaveInstanceState());
        outState.putParcelable(SAVED_INGREDIENT_LAYOUT_MANAGER, recyclerViewIngredient.getLayoutManager().onSaveInstanceState());
        outState.putIntArray(SCROLL_POSITION,
                new int[]{ scrollView.getScrollX(), scrollView.getScrollY()});

        super.onSaveInstanceState(outState);
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
    public void onRecipeStepItemClickListener(RecipeStep recipeStep) {
        if (twoPane) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            VideoPlaybackFragment videoPlaybackFragment = new VideoPlaybackFragment();

            videoPlaybackFragment.setRecipe(recipeStep);

            fragmentManager.beginTransaction()
                    .replace(R.id.fl_video_player_container, videoPlaybackFragment)
                    .commit();

            StepDescriptionFragment stepDescriptionFragment = new StepDescriptionFragment();

            stepDescriptionFragment.setRecipeStep(recipeStep);
            fragmentManager.beginTransaction()
                    .replace(R.id.fl_display_step_description, stepDescriptionFragment)
                    .commit();
        } else {
            int i = 0;
            for (RecipeStep rS : recipeStepArrayList) {
                if (rS.equals(recipeStep)) {
                    break;
                }
                i++;
            }
            Intent intent = new Intent(this, VideoPlaybackActivity.class);
            intent.putExtra(VideoPlaybackActivity.SELECTED_RECIPE_STEP_ARRAY, recipeStepArrayList);
            intent.putExtra(VideoPlaybackActivity.SELECTED_RECIPE_STEP, i);
            startActivity(intent);
        }
    }
}

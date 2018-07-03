package com.example.android.bakingapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.bakingapp.Utilities.Recipe;
import com.example.android.bakingapp.Utilities.RecipeJsonUtils;
import com.example.android.bakingapp.app.RecipeAppController;
import com.example.android.bakingapp.data.RecipeColumns;
import com.example.android.bakingapp.data.RecipeProvider;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        RecipeBookAdapter.RecipeBookAdapterOnItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String RECIPE_ARRAY_LIST = "recipe_array_list";
    private static final String SAVED_RECIPE_LAYOUT_MANAGER = "saved_recipe_layout_manager";

    //mDisplayErrorTV will display error message if there is any error in loading the movie list
    private TextView mDisplayErrorTV;

    //mDisplayRecipesRV is used to display the movie list on the entire screen
    private RecyclerView mDisplayRecipesRV;

    //loadingRecipesListPb will show progress bar while it will load movie list from network
    private ProgressBar loadingRecipesListPb;

    private RecipeBookAdapter adapter;

    private static ArrayList<Recipe> mPopularRecipes;


    private static final String[] mProjection = new String[]{RecipeColumns._ID,
            RecipeColumns.COLUMN_RECIPE_NAME,
            RecipeColumns.COLUMN_IMAGE_URL,
            RecipeColumns.COLUMN_SERVING_COUNT,
            RecipeColumns.COLUMN_MARK_FAVORITE};
    private static final int RECIPE_ID_INDEX = 0;
    private static final int RECIPE_NAME_INDEX = 1;
    private static final int RECIPE_IMAGE_URL_INDEX = 2;
    private static final int RECIPE_SERVING_COUNT_INDEX = 3;
    private static final int RECIPE_FAVORITE_INDEX = 4;

    private NetworkReciver mNetworkReciver;
    private IntentFilter mNetworkIntentFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        startService(new Intent(this,RecipeDataService.class));
        //get the java object from xml layout resource
        mDisplayErrorTV = findViewById(R.id.tv_display_error);
        mDisplayRecipesRV = findViewById(R.id.rv_display_recipes);
        loadingRecipesListPb = findViewById(R.id.pb_loading_recipes);

        final int columns = getResources().getInteger(R.integer.grid_view_column_count);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(columns, StaggeredGridLayoutManager.VERTICAL);
        mDisplayRecipesRV.setLayoutManager(gridLayoutManager);

        mDisplayRecipesRV.setHasFixedSize(true);

        adapter = new RecipeBookAdapter(this,this);
        mDisplayRecipesRV.setAdapter(adapter);

        if (savedInstanceState != null) {

            ArrayList<Recipe> data = savedInstanceState.getParcelableArrayList(RECIPE_ARRAY_LIST);
            if (data != null && data.size() > 0) {
                loadingRecipesListPb.setVisibility(View.INVISIBLE);
                adapter.setRecipeArrayList(data);
                Parcelable recipeLayoutManagerSavedState = savedInstanceState.getParcelable(SAVED_RECIPE_LAYOUT_MANAGER);
                if (recipeLayoutManagerSavedState != null) {
                    mDisplayRecipesRV.getLayoutManager().onRestoreInstanceState(recipeLayoutManagerSavedState);
                }
                showMovies();
            } else {
                loadRecipesFromDatabase();
            }
        } else {
            loadRecipesFromDatabase();
        }

        mNetworkReciver = new NetworkReciver();
        mNetworkIntentFilter = new IntentFilter();
        mNetworkIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mNetworkReciver, mNetworkIntentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNetworkReciver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPE_ARRAY_LIST, mPopularRecipes);
        outState.putParcelable(SAVED_RECIPE_LAYOUT_MANAGER, mDisplayRecipesRV.getLayoutManager().onSaveInstanceState());
    }

    private void showMovies() {
        mDisplayErrorTV.setVisibility(View.INVISIBLE);
        mDisplayRecipesRV.setVisibility(View.VISIBLE);
    }


    //This is called when an error occurred in loading movie list
    private void showErrorMessage() {
        mDisplayErrorTV.setVisibility(View.VISIBLE);
        mDisplayRecipesRV.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onRecipeItemClickListener(Recipe recipeObjecct) {
        Log.d(TAG, recipeObjecct.getRecipeName());


        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(RecipeActivity.SELECTED_RECIPE, recipeObjecct);
        startActivity(intent);

    }

    private void loadRecipe() {
        showMovies();
        adapter.setRecipeArrayList(null);
        try {

            String uriString = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, uriString,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.v(TAG, "Recipe Response : " + response.toString());
                            loadingRecipesListPb.setVisibility(View.INVISIBLE);
                            try {
                                ArrayList<Recipe> recipes = RecipeJsonUtils.insertRecipeFromJson(response, MainActivity.this);

                                if (recipes != null && recipes.size() > 0) {
                                    mPopularRecipes = recipes;
                                    adapter.setRecipeArrayList(recipes);
                                    showMovies();
                                } else {
                                    showErrorMessage();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    loadingRecipesListPb.setVisibility(View.INVISIBLE);
                    showErrorMessage();
                    error.printStackTrace();
                }
            });
            RecipeAppController.getInstance().addToRequestQueue(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadRecipesFromDatabase() {
        new AsyncTask<Void, Void, ArrayList<Recipe>>() {
            @Override
            protected ArrayList<Recipe> doInBackground(Void... voids) {

                Cursor movieCursor = getContentResolver().query(RecipeProvider.Recipes.URI,
                        mProjection,
                        null,
                        null,
                        null);
                if (movieCursor.moveToFirst()) {
                    ArrayList<Recipe> popularMovieArrayList = new ArrayList<>();
                    do {
                        Recipe popularMovie = new Recipe(movieCursor.getInt(RECIPE_ID_INDEX),
                                movieCursor.getString(RECIPE_NAME_INDEX),
                                movieCursor.getString(RECIPE_IMAGE_URL_INDEX),
                                movieCursor.getInt(RECIPE_SERVING_COUNT_INDEX),
                                movieCursor.getString(RECIPE_FAVORITE_INDEX));
                        popularMovieArrayList.add(popularMovie);
                    } while (movieCursor.moveToNext());
                    movieCursor.close();
                    return popularMovieArrayList;
                } else {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(ArrayList<Recipe> popularMovieArrayList) {
                loadingRecipesListPb.setVisibility(View.INVISIBLE);
                if (popularMovieArrayList == null) {
                    loadRecipe();
                } else {
                    showMovies();
                    if (mPopularRecipes != null) {
                        mPopularRecipes.clear();
                    } else {
                        mPopularRecipes = new ArrayList<>();
                    }
                    mPopularRecipes.addAll(popularMovieArrayList);
                    adapter.setRecipeArrayList(popularMovieArrayList);
                }

            }

        }.execute();

    }


    private class NetworkReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            NetworkInfo info = extras.getParcelable("networkInfo");
            NetworkInfo.State networkState = info.getState();
            Log.d("Test Internet", info.toString() + " , " + networkState.toString());

        }
    }
}

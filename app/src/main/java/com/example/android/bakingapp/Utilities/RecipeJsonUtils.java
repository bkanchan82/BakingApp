package com.example.android.bakingapp.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.android.bakingapp.data.IngredientsColumns;
import com.example.android.bakingapp.data.RecipeColumns;
import com.example.android.bakingapp.data.RecipeProvider;
import com.example.android.bakingapp.data.StepsColumns;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kanchan on 24-02-2018.
 */

public class RecipeJsonUtils {

    private static final String TAG = RecipeJsonUtils.class.getSimpleName();

    private static final String RECIPE_ID = "id";
    private static final String RECIPE_NAME = "name";
    private static final String RECIPE_INGREDIENTS = "ingredients";
    private static final String RECIPE_INGREDIENT_QUANTITY = "quantity";
    private static final String RECIPE_INGREDIENT_MEASURE = "measure";
    private static final String RECIPE_INGREDIENT = "ingredient";
    private static final String RECIPE_STEPS = "steps";
    private static final String RECIPE_STEPS_ID = "id";
    private static final String RECIPE_STEPS_SHORT_DESCRIPTION = "shortDescription";
    private static final String RECIPE_STEPS_DESCRIPTION = "description";
    private static final String RECIPE_STEPS_VIDEO_URL = "videoURL";
    private static final String RECIPE_STEPS_THUMBNAIL_URL = "thumbnailURL";
    private static final String RECIPE_SERVINGS = "servings";
    private static final String RECIPE_IMAGE_URL = "image";


    public static ArrayList<Recipe> insertRecipeFromJson(String jsonString, Context context) throws JSONException {
        JSONArray recipeJsonArray = new JSONArray(jsonString);

        ArrayList<Recipe> recipes = new ArrayList<>();

        for(int i = 0 ; i < recipeJsonArray.length();i++){
            JSONObject recipeJson = recipeJsonArray.getJSONObject(i);

            String recipeId = recipeJson.getString(RECIPE_ID);
            String recipeName = recipeJson.getString(RECIPE_NAME);
            int recipeServings = recipeJson.getInt(RECIPE_SERVINGS);
            String recipeImageUrl = recipeJson.getString(RECIPE_IMAGE_URL);
            String markAsFavorite = "false";



            ContentValues values = new ContentValues();
            values.put(RecipeColumns.COLUMN_RECIPE_NAME,recipeName);
            values.put(RecipeColumns.COLUMN_SERVING_COUNT,recipeServings);
            values.put(RecipeColumns.COLUMN_IMAGE_URL,recipeImageUrl);
            values.put(RecipeColumns.COLUMN_MARK_FAVORITE,markAsFavorite);
            Uri uri = context.getContentResolver().insert(RecipeProvider.Recipes.URI,values);
            Log.d(TAG,"Recipe Insertion uri : "+uri.getLastPathSegment());

            String dbId = uri.getLastPathSegment();

            recipes.add(new Recipe(Integer.parseInt(dbId),recipeName,recipeImageUrl,recipeServings,markAsFavorite));

            JSONArray ingredientsJsonArray = recipeJson.getJSONArray(RECIPE_INGREDIENTS);
            for(int j = 0 ; j < ingredientsJsonArray.length() ; j++){
                JSONObject ingredientsJson = ingredientsJsonArray.getJSONObject(j);
                insertIngredientsFromJson(ingredientsJson,context,recipeId);
            }

            JSONArray stepsJsonArray = recipeJson.getJSONArray(RECIPE_STEPS);
            for(int j = 0 ; j < stepsJsonArray.length() ; j++){
                JSONObject stepsJson = stepsJsonArray.getJSONObject(j);
                insertStepsFromJson(stepsJson,context,recipeId);
            }

        }
        return recipes;
    }


    private static void insertIngredientsFromJson(JSONObject ingredientsJson,Context context,String recipeId) throws JSONException {

            String recipeIngredient = ingredientsJson.getString(RECIPE_INGREDIENT);
            String ingredientMeasure = ingredientsJson.getString(RECIPE_INGREDIENT_MEASURE);
            int ingredientQuantity = ingredientsJson.getInt(RECIPE_INGREDIENT_QUANTITY);

            ContentValues values = new ContentValues();
            values.put(IngredientsColumns.COLUMN_RECIPE_ID,recipeId);
            values.put(IngredientsColumns.COLUMN_INGREDIENT,recipeIngredient);
            values.put(IngredientsColumns.COLUMN_MEASURING_UNIT,ingredientMeasure);
            values.put(IngredientsColumns.COLUMN_QUANTITY,ingredientQuantity);

            Uri uri = context.getContentResolver().insert(RecipeProvider.Ingredients.URI,values);
            Log.d(TAG,"Ingredient Insertion uri : "+uri.toString());

    }


    private static void insertStepsFromJson(JSONObject stepsJson,Context context,String recipeId) throws JSONException {

//        String stepId = stepsJson.getString(RECIPE_STEPS_ID);
        String stepShortDescription = stepsJson.getString(RECIPE_STEPS_SHORT_DESCRIPTION);
        String stepDescription = stepsJson.getString(RECIPE_STEPS_DESCRIPTION);
        String stepVideoUrl = stepsJson.getString(RECIPE_STEPS_VIDEO_URL);
        String stepsThumbnailUrl = stepsJson.getString(RECIPE_STEPS_THUMBNAIL_URL);

        ContentValues values = new ContentValues();
        values.put(StepsColumns.COLUMN_RECIPE_ID,recipeId);
        values.put(StepsColumns.COLUMN_DESCRIPTION,stepDescription);
        values.put(StepsColumns.COLUMN_SHORT_DESCRIPTION,stepShortDescription);
        values.put(StepsColumns.COLUMN_VIDEO_URL,stepVideoUrl);
        values.put(StepsColumns.COLUMN_THUMBNAIL_URL,stepsThumbnailUrl);
        Uri uri = context.getContentResolver().insert(RecipeProvider.Steps.URI,values);
        Log.d(TAG,"Step Insertion uri : "+uri.toString());

    }


}

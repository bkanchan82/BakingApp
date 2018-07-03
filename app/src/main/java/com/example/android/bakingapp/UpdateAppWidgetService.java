package com.example.android.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.android.bakingapp.data.RecipeColumns;
import com.example.android.bakingapp.data.RecipeProvider;
import com.example.android.bakingapp.widgets.RecipeCollectionAppWidgetProvider;

/**
 * Created by kanchan on 04-03-2018.
 */

public class UpdateAppWidgetService extends IntentService {

    public static final String ACTION_UPDATE_RECIPE_WIDGETS = "com.example.android.bakingapp.update_recipe_widgets";

    public UpdateAppWidgetService(){
        super(UpdateAppWidgetService.class.getSimpleName());
    }

    /**
     * Starts this service to perform UpdatePlantWidgets action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionUpdatePlantWidgets(Context context) {
        Intent intent = new Intent(context, UpdateAppWidgetService.class);
        intent.setAction(ACTION_UPDATE_RECIPE_WIDGETS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_RECIPE_WIDGETS.equals(action)) {
                handleActionUpdateWidgets();
            }
        }
    }

    private void handleActionUpdateWidgets(){

        //Query to get the plant that's most in need for water (last watered)
        Uri RECIPE_URI = RecipeProvider.Recipes.URI;
        Cursor cursor = getContentResolver().query(
                RECIPE_URI,
                new String[]{RecipeColumns._ID},
                RecipeColumns.COLUMN_MARK_FAVORITE+" = 'true'",
                null,
                null
        );
        int recipeId = -1;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            recipeId = cursor.getInt(cursor.getColumnIndex(RecipeColumns._ID));
            cursor.close();
        }else{
            Cursor recipeCursor = getContentResolver().query(
                    RECIPE_URI,
                    new String[]{RecipeColumns._ID},
                    null,
                    null,
                    null
            );
            recipeCursor.moveToFirst();
            recipeId = recipeCursor.getInt(recipeCursor.getColumnIndex(RecipeColumns._ID));
            recipeCursor.close();

        }
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeCollectionAppWidgetProvider.class));
        //Now update all widgets
        RecipeCollectionAppWidgetProvider.updateRecipeWidgets(this, appWidgetManager, recipeId, appWidgetIds);
    }

}

package com.example.android.bakingapp.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.android.bakingapp.MainActivity;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.UpdateAppWidgetService;

/**
 * Created by Kanchan.
 */

public class RecipeCollectionAppWidgetProvider extends AppWidgetProvider {

    private static final String TAG = RecipeCollectionAppWidgetProvider.class.getSimpleName();
    public static final String EXTRA_LABEL = "TASK_TEXT";
    public static final String EXTRA_RECIPE_ID = "extra_recipe_id";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        UpdateAppWidgetService.startActionUpdatePlantWidgets(context);
    }


    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager,
                                           int imgRes, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, imgRes, appWidgetId);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int recipeId, int appWidgetId) {

            RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.collection_widget);

        // click event handler for the title, launches the app when the user clicks on title
        Intent titleIntent = new Intent(context, MainActivity.class);
        PendingIntent titlePendingIntent = PendingIntent.getActivity(context, 0, titleIntent, 0);
        views.setOnClickPendingIntent(R.id.widgetTitleLabel, titlePendingIntent);

        Intent intent = new Intent(context, RecipeWidgetRemoteViewsService.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        views.setRemoteAdapter(R.id.widgetListView, intent);

        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            // refresh all your widgets
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, RecipeCollectionAppWidgetProvider.class);
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.widgetListView);
        }
        super.onReceive(context, intent);
    }
}

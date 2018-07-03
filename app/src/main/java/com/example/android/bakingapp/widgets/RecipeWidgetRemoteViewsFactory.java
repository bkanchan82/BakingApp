package com.example.android.bakingapp.widgets;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.data.IngredientsColumns;
import com.example.android.bakingapp.data.RecipeProvider;

/**
 * Created by kanchan on 02-03-2018.
 */

class RecipeWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = RecipeWidgetRemoteViewsFactory.class.getSimpleName();

    private final Context mContext;
    private Cursor mCursor;
    private int recipeId;

    public RecipeWidgetRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
        recipeId = intent.getIntExtra(RecipeCollectionAppWidgetProvider.EXTRA_RECIPE_ID,0);
        Log.d(TAG,"AppWidget id : "+ recipeId);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

        if (mCursor != null) {
            mCursor.close();
        }

        final long identityToken = Binder.clearCallingIdentity();
        Uri uri = RecipeProvider.Ingredients.URI;
        mCursor = mContext.getContentResolver().query(uri,
                null,
                IngredientsColumns.COLUMN_RECIPE_ID+"='"+recipeId+"'",
                null,
                null);

        Binder.restoreCallingIdentity(identityToken);

    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION ||
                mCursor == null || !mCursor.moveToPosition(position)) {
            return null;
        }

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.collection_widget_list_item);

        String ingredientString = mContext.getString(R.string.widget_ingredient_screen,
                mCursor.getString(mCursor.getColumnIndex(IngredientsColumns.COLUMN_INGREDIENT)),
                mCursor.getString(mCursor.getColumnIndex(IngredientsColumns.COLUMN_MEASURING_UNIT)),
                mCursor.getInt(mCursor.getColumnIndex(IngredientsColumns.COLUMN_QUANTITY)));

        rv.setTextViewText(R.id.widgetIngredientNameLabel, ingredientString);

        Intent fillInIntentName = new Intent();
        fillInIntentName.putExtra(RecipeCollectionAppWidgetProvider.EXTRA_LABEL, mCursor.getString(mCursor.getColumnIndex(IngredientsColumns.COLUMN_INGREDIENT)));
        rv.setOnClickFillInIntent(R.id.widgetIngredientNameLabel, fillInIntentName);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return mCursor.moveToPosition(position) ? mCursor.getLong(0) : position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}

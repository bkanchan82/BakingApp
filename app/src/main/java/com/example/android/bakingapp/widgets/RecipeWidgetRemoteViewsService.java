package com.example.android.bakingapp.widgets;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

/**
 * Created by Kanchan.
 */

public class RecipeWidgetRemoteViewsService extends RemoteViewsService {
    private static final String TAG = "WidgetService";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d(TAG, "onGetViewFactory: " + "Service called");
        return new RecipeWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

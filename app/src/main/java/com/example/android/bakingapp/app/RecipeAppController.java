package com.example.android.bakingapp.app;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by kanchan on 23-02-2018.
 */

public class RecipeAppController extends Application {

    private RequestQueue mRequestQueue;
    private static final String TAG = RecipeAppController.class.getSimpleName();
    private static RecipeAppController mInstance;


    public static synchronized RecipeAppController getInstance(){
        return mInstance;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(this.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

}

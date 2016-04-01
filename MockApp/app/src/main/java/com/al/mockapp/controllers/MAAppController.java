package com.al.mockapp.controllers;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by vineeth on 31/03/16
 */
public class MAAppController extends Application {

    public static final String TAG = MAAppController.class
            .getSimpleName();
    private static MAAppController mInstance;

    private RequestQueue mRequestQueue;

    public static Context getContext() {
        return mInstance;
    }

    public static synchronized MAAppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}

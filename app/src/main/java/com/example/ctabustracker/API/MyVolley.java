package com.example.ctabustracker.API;

import android.annotation.SuppressLint;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MyVolley {
    @SuppressLint("StaticFieldLeak")
    private static MyVolley mInstance;
    private RequestQueue mRequestQueue;
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private MyVolley(final Context context){
        MyVolley.context = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized MyVolley getInstance(final Context context) {
        if (mInstance == null) {
            mInstance = new MyVolley(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(final Request<T> req) {
        getRequestQueue().add(req);
    }

}

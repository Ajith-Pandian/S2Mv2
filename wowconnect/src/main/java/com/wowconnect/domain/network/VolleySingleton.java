package com.wowconnect.domain.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by thoughtchimp on 10/4/2016.
 */
public class VolleySingleton {
    private  static VolleySingleton mInstance;
    private RequestQueue mRequestQueue;
    private  Context mCtx;

    private VolleySingleton(Context context) {
        mCtx = context;
        mRequestQueue = getThisRequestQueue();
    }

    public static synchronized VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    private RequestQueue getThisRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    private final String TAG = "VolleySingleton";

    public <T> void addToRequestQueue(Request<T> req) {
        Log.d(TAG, "addToRequestQueue: added");
        req.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        getThisRequestQueue().add(req);
    }
}

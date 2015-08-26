package com.tarunsoft.offerdunia.Handler;

/**
 * Created by tsharma3 on 8/13/2015.
 */
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AppController getInstance() {

        if (mInstance == null) {
            mInstance = new AppController();
        }
        return mInstance;
    }


    public RequestQueue getRequestQueue(Context ctx) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(ctx);
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag , Context ctx) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue(ctx).add(req);
    }

    public <T> void addToRequestQueue(Request<T> req , Context ctx) {
        req.setTag(TAG);
        getRequestQueue(ctx).add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
package com.example.domainlayer.utils;

import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

import static com.example.domainlayer.Constants.KEY_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.KEY_DEVICE_TYPE;
import static com.example.domainlayer.Constants.TEMP_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.TEMP_DEVICE_TYPE;

/**
 * Created by thoughtchimp on 12/5/2016.
 */
public class VolleyStringRequest extends StringRequest {
    private static StatusCodeListener mStatusCodeListener;
    Response.ErrorListener volleyErrorListener;
    private int mStatusCode;

    private VolleyStringRequest(int method, String url, Response.Listener<String> listener,
                                VolleyErrListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public VolleyStringRequest(int method, String url, Response.Listener<String> listener,
                               VolleyErrListener errorListener, StatusCodeListener statusCodeListener) {
        this(method, url, listener, errorListener);
        mStatusCodeListener = statusCodeListener;
        // this = errorListener;
    }

/*    public void onErrorResponse(VolleyError error) {
        NetworkResponse response = error.networkResponse;
        checkStatusCode(response.statusCode);
    }*/

    public static StatusCodeListener getmStatusCodeListener() {
        return mStatusCodeListener;
    }

    public  void removeStatusListener() {
        mStatusCodeListener = null;
    }

    public int getStatusCode() {
        return mStatusCode;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        checkStatusCode(response.statusCode);
        return super.parseNetworkResponse(response);
    }

    private void checkStatusCode(int statusCode) {
        switch (statusCode) {
            case HttpStatusCodes.BAD_REQUEST:
                mStatusCodeListener.onBadRequest();
                break;
            case HttpStatusCodes.UNAUTHORIZED:
                mStatusCodeListener.onUnauthorized();
                break;
            case HttpStatusCodes.NOT_FOUND:
                mStatusCodeListener.onNotFound();
                break;
            case HttpStatusCodes.CONFLICT:
                mStatusCodeListener.onConflict();
                break;
            case HttpStatusCodes.TIMEOUT:
                mStatusCodeListener.onTimeout();
                break;
        }
    }

    public interface StatusCodeListener {
        public void onBadRequest();

        void onUnauthorized();

        void onNotFound();

        void onConflict();

        void onTimeout();
    }

   /* @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> header = new ArrayMap<>();
        header.put(KEY_ACCESS_TOKEN, TEMP_ACCESS_TOKEN);
        header.put(KEY_DEVICE_TYPE, TEMP_DEVICE_TYPE);
        return header;
    }*/

    public static class VolleyErrListener implements Response.ErrorListener {
        StatusCodeListener statusCodeListener;

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("VolleyErrListener", "onErrorResponse: calledx");
            NetworkResponse response = error.networkResponse;
            statusCodeListener = VolleyStringRequest.getmStatusCodeListener();
            if (response != null)
                checkStatusCode(response.statusCode);
            // checkStatusCode(response.statusCode);
        }

        private void checkStatusCode(int statusCode) {

            switch (statusCode) {
                case HttpStatusCodes.BAD_REQUEST:
                    statusCodeListener.onBadRequest();
                    break;
                case HttpStatusCodes.UNAUTHORIZED:
                    statusCodeListener.onUnauthorized();
                    break;
                case HttpStatusCodes.NOT_FOUND:
                    statusCodeListener.onNotFound();
                    break;
                case HttpStatusCodes.CONFLICT:
                    statusCodeListener.onConflict();
                    break;
                case HttpStatusCodes.TIMEOUT:
                    statusCodeListener.onTimeout();
                    break;
            }
        }

    }
}

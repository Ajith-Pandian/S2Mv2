package com.wowconnect.ui.customUtils;

import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wowconnect.SharedPreferenceHelper;
import com.wowconnect.domain.Constants;
import com.wowconnect.domain.utils.HttpStatusCodes;

import java.util.Map;

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

    public void removeStatusListener() {
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


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new ArrayMap<>();
        headers.put(Constants.KEY_ACCEPT, Constants.HEADER_ACCEPT);
        headers.put(Constants.KEY_AUTHORIZATION, SharedPreferenceHelper.getAccessToken());
        return headers;
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
        void onBadRequest();

        void onUnauthorized();

        void onNotFound();

        void onConflict();

        void onTimeout();
    }


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

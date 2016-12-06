package com.example.uilayer.exceptionHandler;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by thoughtchimp on 12/5/2016.
 */
public class VolleyStringRequest extends StringRequest {
    private static StatusCodeListener statusCodeListener;
    Response.ErrorListener volleyErrorListener;
    private int mStatusCode;

    private VolleyStringRequest(int method, String url, Response.Listener<String> listener,
                                VolleyErrListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public VolleyStringRequest(int method, String url, Response.Listener<String> listener,
                               VolleyErrListener errorListener, StatusCodeListener statusCodeListener) {
        this(method, url, listener, errorListener);
        this.statusCodeListener = statusCodeListener;
        // this = errorListener;
    }

/*    public void onErrorResponse(VolleyError error) {
        NetworkResponse response = error.networkResponse;
        checkStatusCode(response.statusCode);
    }*/

    public static StatusCodeListener getStatusCodeListener() {
        return statusCodeListener;
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


    public interface StatusCodeListener {
        public void onBadRequest();

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
            statusCodeListener = VolleyStringRequest.getStatusCodeListener();
            if(response!=null)
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

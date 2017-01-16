package com.example.uilayer;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.Constants;
import com.example.domainlayer.database.DataBaseUtil;
import com.example.domainlayer.network.VolleySingleton;
import com.example.domainlayer.utils.VolleyStringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static com.example.domainlayer.Constants.KEY_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.KEY_DEVICE_TYPE;
import static com.example.domainlayer.Constants.TEMP_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.TEMP_DEVICE_TYPE;

/**
 * Created by thoughtchimp on 12/20/2016.
 */

public class NetworkHelper {
    private final Context context;
    private NetworkListener networkListener;

    public NetworkHelper(Context context) {
        this.context = context;
    }

    public void removeListener() {
        this.networkListener = null;
    }

    public void getUserDetails(NetworkListener networkListener) {
        this.networkListener = networkListener;
        VolleyStringRequest otpRequest = new VolleyStringRequest(Request.Method.GET, Constants.USER_DETAILS_URL + String.valueOf(new DataBaseUtil(context).getUser().getId()),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                        Log.d("UserDetails", "onResponse:otp " + response);
                        storeResponse(response);
                        NetworkHelper.this.networkListener.onFinish();
                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("UserDetails", "onResponse: " + error);
                    }
                }, new VolleyStringRequest.StatusCodeListener() {
            String TAG = "VolleyStringReq";

            @Override
            public void onBadRequest() {
                Log.d(TAG, "onBadRequest: ");
            }

            @Override
            public void onUnauthorized() {
                Log.d(TAG, "onUnauthorized: ");
            }

            @Override
            public void onNotFound() {
                Log.d(TAG, "onNotFound: ");
            }

            @Override
            public void onConflict() {
                Log.d(TAG, "onConflict: ");
            }

            @Override
            public void onTimeout() {
                Log.d(TAG, "onTimeout: ");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new ArrayMap<>();
                header.put(KEY_ACCESS_TOKEN, TEMP_ACCESS_TOKEN);
                header.put(KEY_DEVICE_TYPE, TEMP_DEVICE_TYPE);
                return header;
            }

        };

        VolleySingleton.getInstance(context).addToRequestQueue(otpRequest);
    }

    private void storeResponse(String response) {
        try {
            JSONObject responseJson = new JSONObject(response);
            com.example.domainlayer.temp.DataHolder.getInstance(context).saveUserDetails(responseJson);
        } catch (JSONException ex) {
            Log.e("networkHelper", "storeResponse: ", ex);
        }
    }

    public interface NetworkListener {
        void onFinish();
    }
}

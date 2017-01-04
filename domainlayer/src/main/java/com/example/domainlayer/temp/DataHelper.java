package com.example.domainlayer.temp;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.network.VolleySingleton;
import com.example.domainlayer.utils.VolleyStringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Map;

import static com.example.domainlayer.Constants.KEY_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.KEY_DEVICE_TYPE;
import static com.example.domainlayer.Constants.MILES_TRAININGS_URL;
import static com.example.domainlayer.Constants.SECTIONS_URL;
import static com.example.domainlayer.Constants.TEMP_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.TEMP_DEVICE_TYPE;

/**
 * Created by thoughtchimp on 12/9/2016.
 */

public class DataHelper {


    Context context;

    public DataHelper(Context context)
    {
        this.context=context;
    }
    void fetchSections()
    {
        VolleyStringRequest sectionsRequest = new VolleyStringRequest(Request.Method.GET, SECTIONS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Sections", "onResponse: " + response);
                        try{
                        DataHolder.getInstance(context)
                                .setSectionsList(new DataParser()
                                        .getSectionsListFromJson(new JSONArray(response)));}
                        catch (JSONException ex){
                            Log.e("sectionResponse", "onResponse: ", ex);
                        }
                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("Sections", "onErrorResponse: " + error);

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

        VolleySingleton.getInstance(context).addToRequestQueue(sectionsRequest);
    }
}

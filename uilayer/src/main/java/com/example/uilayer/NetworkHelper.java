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
import com.example.domainlayer.models.Bulletin;
import com.example.domainlayer.models.BulletinMessage;
import com.example.domainlayer.models.DbUser;
import com.example.domainlayer.models.SclActs;
import com.example.domainlayer.models.Sections;
import com.example.domainlayer.network.VolleySingleton;
import com.example.domainlayer.temp.*;
import com.example.domainlayer.utils.VolleyStringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import static com.example.domainlayer.Constants.IS_LIKED;
import static com.example.domainlayer.Constants.KEY_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.KEY_ACTIVITIES;
import static com.example.domainlayer.Constants.KEY_BODY;
import static com.example.domainlayer.Constants.KEY_DEVICE_TYPE;
import static com.example.domainlayer.Constants.KEY_ID;
import static com.example.domainlayer.Constants.KEY_IMAGE;
import static com.example.domainlayer.Constants.KEY_LIKES_COUNT;
import static com.example.domainlayer.Constants.KEY_MESSAGE;
import static com.example.domainlayer.Constants.KEY_SECTIONS;
import static com.example.domainlayer.Constants.KEY_TIMESTAMP;
import static com.example.domainlayer.Constants.KEY_TITLE;
import static com.example.domainlayer.Constants.KEY_TYPE;
import static com.example.domainlayer.Constants.KEY_USER_ID;
import static com.example.domainlayer.Constants.TEMP_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.TEMP_DEVICE_TYPE;
import static com.example.domainlayer.Constants.TYPE_BULLETIN;

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
            saveUserDetailsForThisSession(responseJson);
        } catch (JSONException ex) {
            Log.e("networkHelper", "storeResponse: ", ex);
        }
    }

    public interface NetworkListener {
        void onFinish();
    }


    public void saveUserDetailsForThisSession(JSONObject loginResultJson)
    {
        DbUser user = new DbUser();
        DataParser dataParser = new DataParser();
        ArrayList<Sections> sectionsList = new ArrayList<>();
        ArrayList <SclActs> sclActList = new ArrayList<>();
        try {
            user.setFirstName(loginResultJson.getString(Constants.KEY_FIRST_NAME));
            user.setId(loginResultJson.getInt(Constants.KEY_ID));
            user.setLastName(loginResultJson.getString(Constants.KEY_LAST_NAME));
            user.setEmail(loginResultJson.getString(Constants.KEY_EMAIL));
            user.setPhoneNum(loginResultJson.getString(Constants.KEY_PHONE_NUM));
            // user.setLastLogin(loginResultJson.getString(Constants.KEY_LAST_LOGIN));
            user.setLastLogin("null");
            user.setSchoolId(loginResultJson.getInt(Constants.KEY_SCHOOL_ID));

            //TODO for s2m and school admin
           /* JSONArray schoolsArray=new JSONArray(loginResultJson.getString(KEY_SCHOOLS));
            ArrayList<Schools> schoolsList=new ArrayList<>();
            for (int i = 0; i < schoolsArray.length(); i++) {
                JSONObject schoolObject=schoolsArray.getJSONObject(i);
                Schools school=new Schools();
                school.setId(schoolObject.getInt(KEY_ID));
                school.setName(schoolObject.getString(KEY_NAME));
                school.setName(schoolObject.getString(KEY_LOGO));
                schoolsList.add(i,school);
            }
            user.setSchoolsList(schoolsList);*/
            user.setSchoolName(loginResultJson.getString(Constants.KEY_SCHOOL_NAME));

            user.setWow(loginResultJson.getString(Constants.KEY_WOW));
            user.setAvatar(loginResultJson.getString(Constants.KEY_AVATAR));
            user.setMiles(loginResultJson.getString(Constants.KEY_MILES));
            user.setType(loginResultJson.getString(Constants.KEY_TYPE));

/*
            //Shared preferences
            setSchoolId(loginResultJson.getInt(Constants.KEY_SCHOOL_ID));
            setUserId(loginResultJson.getInt(Constants.KEY_ID));
            setAccessToken(loginResultJson.getString(Constants.KEY_ACCESS_TOKEN));*/

            JSONObject bulletinJson = loginResultJson.getJSONObject(TYPE_BULLETIN);
            Bulletin bulletin = new Bulletin();
            bulletin.setId(bulletinJson.getInt(KEY_ID));
            bulletin.setUserId(bulletinJson.getInt(KEY_USER_ID));
            bulletin.setMsg(getMessage(bulletinJson.getString(KEY_MESSAGE)));
            bulletin.setType(bulletinJson.getString(KEY_TYPE));
            bulletin.setTimeStamp(bulletinJson.getString(KEY_TIMESTAMP));
            bulletin.setLiked(bulletinJson.getInt(IS_LIKED));
            user.setBulletin(bulletin);

            JSONArray sectionsArray = loginResultJson.getJSONArray(KEY_SECTIONS);
            user.setSectionsList(dataParser.getSectionsListFromJson(sectionsArray));

            JSONArray schoolActivities = loginResultJson.getJSONArray(KEY_ACTIVITIES);

            for (int i = 0; i < schoolActivities.length(); i++) {
                JSONObject schoolActivity = schoolActivities.getJSONObject(i);
                SclActs sclActivities
                        = new SclActs(schoolActivity.getInt(KEY_ID),
                        schoolActivity.getInt(KEY_USER_ID),
                        "",
                        // schoolActivity.getString(KEY_SCHOOL_NAME),
                        schoolActivity.getString(KEY_MESSAGE),
                        //getMessage(schoolActivity.getString(KEY_MESSAGE)),
                        schoolActivity.getString(KEY_TYPE),
                        schoolActivity.getString(KEY_TIMESTAMP),
                        schoolActivity.getInt(KEY_LIKES_COUNT),
                        schoolActivity.getInt(IS_LIKED)
                );
                sclActList.add(sclActivities);
            }
            user.setSclActs(sclActList);

            DataHolder.getInstance(context).setUser(user);
        } catch (JSONException exception) {
            Log.e("DataHolder", "saveUserDetails: ", exception);
        }
    }


    private BulletinMessage getMessage(String message) {
        BulletinMessage msg = null;

        try {
            JSONObject msgObject = new JSONObject(message);
            msg = new BulletinMessage(msgObject.getString(KEY_TITLE),
                    msgObject.getString(KEY_IMAGE),
                    msgObject.getString(KEY_BODY));
        } catch (JSONException ex) {
            Log.e("GetMsg", "getMessage: ", ex);
        }
        return msg;
    }
}

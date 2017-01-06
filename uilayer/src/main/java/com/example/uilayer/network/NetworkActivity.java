package com.example.uilayer.network;

import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.Constants;
import com.example.domainlayer.models.User;
import com.example.domainlayer.network.VolleySingleton;
import com.example.domainlayer.utils.VolleyStringRequest;
import com.example.uilayer.DataHolder;
import com.example.uilayer.R;
import com.example.uilayer.customUtils.VerticalSpaceItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.domainlayer.Constants.KEY_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.KEY_DEVICE_TYPE;
import static com.example.domainlayer.Constants.NETWORK_URL_SUFFIX;
import static com.example.domainlayer.Constants.SCHOOLS_URL;
import static com.example.domainlayer.Constants.TEMP_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.TEMP_ACCESS_TOKEN1;
import static com.example.domainlayer.Constants.TEMP_DEVICE_TYPE;

public class NetworkActivity extends AppCompatActivity {
    @BindView(R.id.recycler_network)
    RecyclerView networkRecycler;
    private VolleyStringRequest networkRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setupWindowAnimations();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        networkRecycler.setLayoutManager(layoutManager);
        networkRecycler.addItemDecoration(new VerticalSpaceItemDecoration(5,1,true));

        getNetworkProfileInfo();
    }

    void getNetworkProfileInfo() {
        networkRequest = new VolleyStringRequest(Request.Method.GET, SCHOOLS_URL + "2" + NETWORK_URL_SUFFIX,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("networkRequest", "onResponse: " + response);
                        setNetworkProfiles(response);
                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("networkRequest", "onErrorResponse: " + error);

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
                header.put(KEY_ACCESS_TOKEN, TEMP_ACCESS_TOKEN1);
                header.put(KEY_DEVICE_TYPE, TEMP_DEVICE_TYPE);
                return header;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(networkRequest);

    }


    public void setNetworkProfiles(String profilesString) {

        ArrayList<User> usersList = new ArrayList<>();
        try {
            JSONArray profilesArray = new JSONArray(profilesString);
            for (int i = 0; i < profilesArray.length(); i++) {
                JSONObject userJson = profilesArray.getJSONObject(i);
                User user = new User();
                user.setFirstName(userJson.getString(Constants.KEY_FIRST_NAME));
                user.setId(userJson.getInt(Constants.KEY_ID));
                user.setLastName(userJson.getString(Constants.KEY_LAST_NAME));
                user.setEmail(userJson.getString(Constants.KEY_EMAIL));
                user.setPhoneNum(userJson.getString(Constants.KEY_PHONE_NUM));
                //  user.setLastLogin(userJson.getString(Constants.KEY_LAST_LOGIN));
                // user.setSchoolId(userJson.getInt(Constants.KEY_SCHOOL_ID));
                user.setSchoolName(userJson.getString(Constants.KEY_SCHOOL_NAME));

                user.setWow(userJson.getString(Constants.KEY_WOW));
                user.setAvatar(userJson.getString(Constants.KEY_AVATAR));
                user.setMiles(userJson.getString(Constants.KEY_MILES));
                user.setTrainings(userJson.getString(Constants.KEY_TRAINING));
                // user.setType(userJson.getString(Constants.KEY_TYPE));
                usersList.add(i, user);
            }
            DataHolder.getInstance(getApplicationContext()).setNetworkProfiles(usersList);
            networkRecycler.setAdapter(new NetworkAdapter(getApplicationContext(), usersList));
        } catch (JSONException exception) {
            Log.e("DataHolder", "saveUserDetails: ", exception);
        }
    }


    @SuppressWarnings("NewApi")
    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        networkRequest.removeStatusListener();
        networkRequest = null;
        super.onDestroy();
    }
}

package com.example.uilayer.network;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.Constants;
import com.example.domainlayer.database.DataBaseUtil;
import com.example.domainlayer.models.DbUser;
import com.example.domainlayer.network.VolleySingleton;
import com.example.domainlayer.temp.DataParser;
import com.example.uilayer.NewDataHolder;
import com.example.uilayer.NewDataParser;
import com.example.uilayer.SharedPreferenceHelper;
import com.example.uilayer.customUtils.VolleyStringRequest;
import com.example.uilayer.R;
import com.example.uilayer.customUtils.VerticalSpaceItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.domainlayer.Constants.KEY_OPTIONS;
import static com.example.domainlayer.Constants.KEY_SECTIONS;
import static com.example.domainlayer.Constants.KEY_USERS;
import static com.example.domainlayer.Constants.SEPERATOR;

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
        networkRecycler.addItemDecoration(new VerticalSpaceItemDecoration(5, 1, true));

        getNetworkProfileInfo();
    }


    void getNetworkProfileInfo() {
        networkRequest = new VolleyStringRequest(Request.Method.GET, Constants.SCHOOLS_URL +
                SharedPreferenceHelper.getSchoolId() + SEPERATOR + KEY_USERS,
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
        });
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(networkRequest);

    }


    public void setNetworkProfiles(String profilesString) {

        ArrayList<DbUser> usersList = new ArrayList<>();
        NewDataParser dataParser = new NewDataParser();
        try {
            JSONObject usersObject = new JSONObject(profilesString);
            JSONArray profilesArray = usersObject.getJSONArray(KEY_USERS);
            for (int i = 0; i < profilesArray.length(); i++) {
                JSONObject userJson = profilesArray.getJSONObject(i);
                DbUser user = new DbUser();
                user.setId(userJson.getInt(Constants.KEY_ID));
                user.setFirstName(userJson.getString(Constants.KEY_FIRST_NAME));
                if (!userJson.isNull(Constants.KEY_LAST_NAME))
                    user.setLastName(userJson.getString(Constants.KEY_LAST_NAME));
                user.setEmail(userJson.getString(Constants.KEY_EMAIL));
                user.setPhoneNum(userJson.getString(Constants.KEY_MOBILE_NO));
                user.setAvatar(userJson.getString(Constants.KEY_PROFILE_PICTURE));
                user.setWow(userJson.getString(Constants.KEY_WOW_COUNT));
                user.setMiles(userJson.getString(Constants.KEY_MILES_COMPLETION_COUNT));
                user.setTrainings(userJson.getString(Constants.KEY_TRAININGS_COMPLETION_COUNT));

                JSONObject optionsObject = userJson.getJSONObject(KEY_OPTIONS);
                if(!optionsObject.isNull(Constants.KEY_ANNIVERSARY))
                user.setAnniversary(optionsObject.getString(Constants.KEY_ANNIVERSARY));
                if(!optionsObject.isNull(Constants.KEY_GENDER))
                user.setGender(optionsObject.getString(Constants.KEY_GENDER));
                if(!optionsObject.isNull(Constants.KEY_DOB))
                user.setDob(optionsObject.getString(Constants.KEY_DOB));


                user.setWow(userJson.getString(Constants.KEY_WOW_COUNT));
                ArrayList<String> roles = dataParser.getStringsFromArray(userJson.getJSONArray(Constants.KEY_ROLES));
                user.setRoles(roles);
                user.setType(userJson.getString(Constants.KEY_USER_TYPE));
                user.setSectionsList(new DataParser().getSectionsListFromJson(userJson.getJSONArray(KEY_SECTIONS), true));
                usersList.add(i, user);
            }
            new DataBaseUtil(this).setNetworkUsers(usersList);
            NewDataHolder.getInstance(this).setNetworkUsers(usersList);
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

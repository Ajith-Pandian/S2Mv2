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
        networkRecycler.addItemDecoration(new VerticalSpaceItemDecoration(5, 1, true));

        //getNetworkProfileInfo();
        String dummyNetworkUsers="[\n" +
                "  {\n" +
                "    \"id\": 4,\n" +
                "    \"firstName\": \"Harsh\",\n" +
                "    \"lastName\": \"Dunno\",\n" +
                "    \"avatar\": \"http://fixcapitalism.com/wp-content/uploads/2015/07/Screen-Shot-2015-08-05-at-3.07.56-AM-256x256.png\",\n" +
                "    \"email\": \"harsh@gmail.com\",\n" +
                "    \"role\": \"teacher\",\n" +
                "    \"phone\": \"1234567890\",\n" +
                "    \"schoolName\": \"School2\",\n" +
                "    \"wows\": 1,\n" +
                "    \"miles\": 32,\n" +
                "    \"trainings\": 28\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 9,\n" +
                "    \"firstName\": \"fname\",\n" +
                "    \"lastName\": \"lname\",\n" +
                "    \"avatar\": \"\",\n" +
                "    \"email\": \"ameil@gmail.com\",\n" +
                "    \"role\": \"teacher\",\n" +
                "    \"phone\": \"1234567653\",\n" +
                "    \"schoolName\": \"School2\",\n" +
                "    \"wows\": 0,\n" +
                "    \"miles\": 0,\n" +
                "    \"trainings\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 10,\n" +
                "    \"firstName\": \"fname\",\n" +
                "    \"lastName\": \"lname\",\n" +
                "    \"avatar\": \"\",\n" +
                "    \"email\": \"ameil@gmail.com\",\n" +
                "    \"role\": \"teacher\",\n" +
                "    \"phone\": \"1234567683\",\n" +
                "    \"schoolName\": \"School2\",\n" +
                "    \"wows\": 0,\n" +
                "    \"miles\": 0,\n" +
                "    \"trainings\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 19,\n" +
                "    \"firstName\": \"fname\",\n" +
                "    \"lastName\": \"lname\",\n" +
                "    \"avatar\": \"\",\n" +
                "    \"email\": \"ameil@gmail.com\",\n" +
                "    \"role\": \"teacher\",\n" +
                "    \"phone\": \"3132336742\",\n" +
                "    \"schoolName\": \"School2\",\n" +
                "    \"wows\": 0,\n" +
                "    \"miles\": 0,\n" +
                "    \"trainings\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 28,\n" +
                "    \"firstName\": \"fname\",\n" +
                "    \"lastName\": \"lname\",\n" +
                "    \"avatar\": \"\",\n" +
                "    \"email\": \"ameil@gmail.com\",\n" +
                "    \"role\": \"teacher\",\n" +
                "    \"phone\": \"3132222155\",\n" +
                "    \"schoolName\": \"School2\",\n" +
                "    \"wows\": 0,\n" +
                "    \"miles\": 0,\n" +
                "    \"trainings\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 30,\n" +
                "    \"firstName\": \"first_name\",\n" +
                "    \"lastName\": \"last_name\",\n" +
                "    \"avatar\": \"\",\n" +
                "    \"email\": \"thisisemailid@gmail.com\",\n" +
                "    \"role\": \"teacher\",\n" +
                "    \"phone\": \"3132212155\",\n" +
                "    \"schoolName\": \"School2\",\n" +
                "    \"wows\": 0,\n" +
                "    \"miles\": 0,\n" +
                "    \"trainings\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 31,\n" +
                "    \"firstName\": \"first_name\",\n" +
                "    \"lastName\": \"last_name\",\n" +
                "    \"avatar\": \"\",\n" +
                "    \"email\": \"thisisemailid@gmail.com\",\n" +
                "    \"role\": \"teacher\",\n" +
                "    \"phone\": \"3132212145\",\n" +
                "    \"schoolName\": \"School2\",\n" +
                "    \"wows\": 0,\n" +
                "    \"miles\": 0,\n" +
                "    \"trainings\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 32,\n" +
                "    \"firstName\": \"This is first name\",\n" +
                "    \"lastName\": \"this is last name\",\n" +
                "    \"avatar\": \"\",\n" +
                "    \"email\": \"thisisemail@email.com\",\n" +
                "    \"role\": \"teacher\",\n" +
                "    \"phone\": \"1234567810\",\n" +
                "    \"schoolName\": \"School2\",\n" +
                "    \"wows\": 0,\n" +
                "    \"miles\": 0,\n" +
                "    \"trainings\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 33,\n" +
                "    \"firstName\": \"hi\",\n" +
                "    \"lastName\": \"sechi\",\n" +
                "    \"avatar\": \"\",\n" +
                "    \"email\": \"ahijdgsjkd@gmail.com\",\n" +
                "    \"role\": \"teacher\",\n" +
                "    \"phone\": \"8346153886\",\n" +
                "    \"schoolName\": \"School2\",\n" +
                "    \"wows\": 0,\n" +
                "    \"miles\": 0,\n" +
                "    \"trainings\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 35,\n" +
                "    \"firstName\": \"teacher\",\n" +
                "    \"lastName\": \"new\",\n" +
                "    \"avatar\": \"\",\n" +
                "    \"email\": \"ann@bb.cjn\",\n" +
                "    \"role\": \"teacher\",\n" +
                "    \"phone\": \"9678437944\",\n" +
                "    \"schoolName\": \"School2\",\n" +
                "    \"wows\": 0,\n" +
                "    \"miles\": 0,\n" +
                "    \"trainings\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 36,\n" +
                "    \"firstName\": \"teacher\",\n" +
                "    \"lastName\": \"q\",\n" +
                "    \"avatar\": \"\",\n" +
                "    \"email\": \"gs@bb.nn\",\n" +
                "    \"role\": \"teacher\",\n" +
                "    \"phone\": \"9770884542\",\n" +
                "    \"schoolName\": \"School2\",\n" +
                "    \"wows\": 0,\n" +
                "    \"miles\": 0,\n" +
                "    \"trainings\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 37,\n" +
                "    \"firstName\": \"ahfhfgdg\",\n" +
                "    \"lastName\": \"dgdhd\",\n" +
                "    \"avatar\": \"\",\n" +
                "    \"email\": \"sgshsjs@gsjs.com\",\n" +
                "    \"role\": \"teacher\",\n" +
                "    \"phone\": \"1239999999\",\n" +
                "    \"schoolName\": \"School2\",\n" +
                "    \"wows\": 0,\n" +
                "    \"miles\": 0,\n" +
                "    \"trainings\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 38,\n" +
                "    \"firstName\": \"hhhhh\",\n" +
                "    \"lastName\": \"A\",\n" +
                "    \"avatar\": \"\",\n" +
                "    \"email\": \"hgggdhd@gnsvs.cm\",\n" +
                "    \"role\": \"teacher\",\n" +
                "    \"phone\": \"8978464525\",\n" +
                "    \"schoolName\": \"School2\",\n" +
                "    \"wows\": 0,\n" +
                "    \"miles\": 0,\n" +
                "    \"trainings\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 39,\n" +
                "    \"firstName\": \"gydbd\",\n" +
                "    \"lastName\": \"A\",\n" +
                "    \"avatar\": \"\",\n" +
                "    \"email\": \"huh@nnn.mm\",\n" +
                "    \"role\": \"teacher\",\n" +
                "    \"phone\": \"6794643945\",\n" +
                "    \"schoolName\": \"School2\",\n" +
                "    \"wows\": 0,\n" +
                "    \"miles\": 0,\n" +
                "    \"trainings\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 40,\n" +
                "    \"firstName\": \"jghjhhj\",\n" +
                "    \"lastName\": \"A\",\n" +
                "    \"avatar\": \"\",\n" +
                "    \"email\": \"fuh@vv.com\",\n" +
                "    \"role\": \"teacher\",\n" +
                "    \"phone\": \"9368395368\",\n" +
                "    \"schoolName\": \"School2\",\n" +
                "    \"wows\": 0,\n" +
                "    \"miles\": 0,\n" +
                "    \"trainings\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 41,\n" +
                "    \"firstName\": \"hhhh ghh\",\n" +
                "    \"lastName\": \"A\",\n" +
                "    \"avatar\": \"\",\n" +
                "    \"email\": \"hhh@ccc.nnn\",\n" +
                "    \"role\": \"teacher\",\n" +
                "    \"phone\": \"5398888888\",\n" +
                "    \"schoolName\": \"School2\",\n" +
                "    \"wows\": 0,\n" +
                "    \"miles\": 0,\n" +
                "    \"trainings\": 0\n" +
                "  }\n" +
                "]";
        setNetworkProfiles(dummyNetworkUsers);
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


    void setNetworkUsers() {
        ArrayList<User> userArrayList = new ArrayList<>();
        User user = new User();
        user.setFirstName("AAA");
        user.setLastName("AAA");
        user.setPhoneNum("1234567890");
        user.setAvatar("http://fixcapitalism.com/wp-content/uploads/2015/07/Screen-Shot-2015-08-05-at-3.07.56-AM-256x256.png");
        userArrayList.add(user);
        User user1 = new User();
        user1.setFirstName("BBB");
        user1.setLastName("BBB");
        user1.setPhoneNum("1234567890");
        user1.setAvatar("http://fixcapitalism.com/wp-content/uploads/2015/07/Screen-Shot-2015-08-05-at-3.07.56-AM-256x256.png");
        userArrayList.add(user1);
        User user2 = new User();
        user2.setFirstName("CCC");
        user2.setLastName("CCC");
        user2.setPhoneNum("1234567890");
        user2.setAvatar("http://fixcapitalism.com/wp-content/uploads/2015/07/Screen-Shot-2015-08-05-at-3.07.56-AM-256x256.png");
        userArrayList.add(user2);
        User user3 = new User();
        user3.setFirstName("AAA");
        user3.setLastName("AAA");
        user3.setPhoneNum("1234567890");
        user3.setAvatar("http://fixcapitalism.com/wp-content/uploads/2015/07/Screen-Shot-2015-08-05-at-3.07.56-AM-256x256.png");
        userArrayList.add(user3);
        User user4 = new User();
        user4.setFirstName("BBB");
        user4.setLastName("BBB");
        user4.setPhoneNum("1234567890");
        user4.setAvatar("http://fixcapitalism.com/wp-content/uploads/2015/07/Screen-Shot-2015-08-05-at-3.07.56-AM-256x256.png");
        userArrayList.add(user4);
        User user5 = new User();
        user5.setFirstName("CCC");
        user5.setLastName("CCC");
        user5.setPhoneNum("1234567890");
        user5.setAvatar("http://fixcapitalism.com/wp-content/uploads/2015/07/Screen-Shot-2015-08-05-at-3.07.56-AM-256x256.png");
        userArrayList.add(user5);
        userArrayList.addAll(userArrayList);
        userArrayList.addAll(userArrayList);
        com.example.domainlayer.temp.DataHolder.getInstance(this).setTeachersList(userArrayList);
        DataHolder.getInstance(getApplicationContext()).setNetworkProfiles(userArrayList);
        networkRecycler.setAdapter(new NetworkAdapter(getApplicationContext(), userArrayList));
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
        if (networkRequest!=null) {
        networkRequest.removeStatusListener();
        networkRequest = null;
        }
        super.onDestroy();
    }
}

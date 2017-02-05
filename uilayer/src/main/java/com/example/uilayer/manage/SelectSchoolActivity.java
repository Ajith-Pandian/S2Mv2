package com.example.uilayer.manage;

import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.Constants;
import com.example.domainlayer.models.Schools;
import com.example.domainlayer.network.VolleySingleton;
import com.example.uilayer.customUtils.VolleyStringRequest;
import com.example.uilayer.R;
import com.example.uilayer.customUtils.VerticalSpaceItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.domainlayer.Constants.BASE_URL;
import static com.example.domainlayer.Constants.KEY_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.KEY_DEVICE_TYPE;
import static com.example.domainlayer.Constants.KEY_SCHOOL;
import static com.example.domainlayer.Constants.KEY_SCHOOLS;
import static com.example.domainlayer.Constants.SCHOOLS_URL;
import static com.example.domainlayer.Constants.TEMP_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.TEMP_DEVICE_TYPE;

public class SelectSchoolActivity extends AppCompatActivity {
    @BindView(R.id.recycler_school)
    RecyclerView schoolRecycler;
    VolleyStringRequest getSchoolsRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_school);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        schoolRecycler.setLayoutManager(layoutManager);
        schoolRecycler.addItemDecoration(new VerticalSpaceItemDecoration(5, 1, true));
        // schoolRecycler.setAdapter(new SchoolsAdapter(this, getSchools()));
        loadSchools();
    }

    ArrayList<Schools> getSchools() {
        ArrayList<Schools> schoolsArrayList = new ArrayList<>();
        Schools school1 = new Schools();
        school1.setName("Diwan-BalluBhai School");
        school1.setLocality("Sector 28 , Faridabad");
        school1.setLogo("http://www.civil-site.com/wp-content/uploads/2015/11/MJ-High-School-11.jpg");
        schoolsArrayList.add(school1);
        Schools school2 = new Schools();
        school2.setName("Indian Academy");
        school2.setLogo("http://www.civil-site.com/wp-content/uploads/2015/11/MJ-High-School-11.jpg");
        school2.setLocality("Bannergatta, Bangalore");
        schoolsArrayList.add(school2);
        Schools school3 = new Schools();
        school3.setName("The Shri Ram School");
        school3.setLogo("https://image.freepik.com/free-vector/school-building_23-2147515924.jpg");
        school3.setLocality("M.G.Road, Gurgaon");
        schoolsArrayList.add(school3);
        return schoolsArrayList;
    }

    void loadSchools() {
        getSchoolsRequest = new VolleyStringRequest(Request.Method.GET, BASE_URL + KEY_SCHOOLS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("getSchoolsRequest", "onResponse: " + response);
                        updateSchools(response);
                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("getSchoolsRequest", "onErrorResponse: " + error);

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
        VolleySingleton.getInstance(this).addToRequestQueue(getSchoolsRequest);
    }

    @Override
    protected void onDestroy() {
        getSchoolsRequest.removeStatusListener();
        super.onDestroy();
    }

    void updateSchools(String schoolsResponse) {
        ArrayList<Schools> schoolsArrayList = new ArrayList<>();
        try {
            JSONObject schoolsJson = new JSONObject(schoolsResponse);
            JSONArray schoolsArray = schoolsJson.getJSONArray(KEY_SCHOOLS);
            for (int i = 0; i < schoolsArray.length(); i++) {
                JSONObject userJson = schoolsArray.getJSONObject(i);
                Schools school = new Schools();

                school.setId(userJson.getInt(Constants.KEY_ID));
                school.setName(userJson.getString(Constants.KEY_NAME));
                school.setLocality(userJson.getString(Constants.KEY_LOCALITY));
                school.setCity(userJson.getString(Constants.KEY_CITY));
                school.setLogo(userJson.getString(Constants.KEY_LOGO));
                if (i == 0)
                    school.setActive(true);
                else
                    school.setActive(false);

                schoolsArrayList.add(i, school);
            }
            schoolRecycler.setAdapter(new SchoolsAdapter(this, schoolsArrayList));
        } catch (JSONException exception) {
            Log.e("Select School Activity", "updateSchools: ", exception);
        }
    }
}

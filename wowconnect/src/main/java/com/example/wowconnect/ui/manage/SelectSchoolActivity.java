package com.example.wowconnect.ui.manage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.wowconnect.domain.Constants;
import com.example.wowconnect.domain.network.VolleySingleton;
import com.example.wowconnect.SharedPreferenceHelper;
import com.example.wowconnect.models.Schools;
import com.example.wowconnect.ui.customUtils.VolleyStringRequest;
import com.example.wowconnect.R;
import com.example.wowconnect.ui.customUtils.VerticalSpaceItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.wowconnect.domain.Constants.BASE_URL;
import static com.example.wowconnect.domain.Constants.KEY_SCHOOLS;

public class SelectSchoolActivity extends AppCompatActivity {
    @BindView(R.id.recycler_school)
    RecyclerView schoolRecycler;
    VolleyStringRequest getSchoolsRequest;
    boolean isFirstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_school);
        ButterKnife.bind(this);

        if (getSupportActionBar()!=null)
            getSupportActionBar().setTitle("Select School");
        if (getIntent() != null)
            isFirstTime = getIntent().getBooleanExtra("isFirstTime", false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        schoolRecycler.setLayoutManager(layoutManager);
        schoolRecycler.addItemDecoration(new VerticalSpaceItemDecoration(5, 1, true));
        // schoolRecycler.setAdapter(new SchoolsAdapter(this, getSchools()));
        loadSchools();
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
                JSONObject schoolJson = schoolsArray.getJSONObject(i);
                Schools school = new Schools();
                int id = schoolJson.getInt(Constants.KEY_ID);
                school.setId(id);
                school.setName(schoolJson.getString(Constants.KEY_NAME));
                school.setLocality(schoolJson.getString(Constants.KEY_LOCALITY));
                school.setCity(schoolJson.getString(Constants.KEY_CITY));
                school.setLogo(schoolJson.getString(Constants.KEY_LOGO));
                if (SharedPreferenceHelper.getSchoolId() == id)
                    school.setActive(true);
                else
                    school.setActive(false);

                schoolsArrayList.add(i, school);
            }
            schoolRecycler.setAdapter(new SchoolsAdapter(this, schoolsArrayList,isFirstTime));
        } catch (JSONException exception) {
            Log.e("Select School Activity", "updateSchools: ", exception);
        }
    }
}

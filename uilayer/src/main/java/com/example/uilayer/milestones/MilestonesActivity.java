package com.example.uilayer.milestones;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.Constants;
import com.example.domainlayer.models.milestones.TMiles;
import com.example.domainlayer.network.VolleySingleton;
import com.example.uilayer.NewDataHolder;
import com.example.uilayer.NewDataParser;
import com.example.uilayer.SharedPreferenceHelper;
import com.example.uilayer.customUtils.VolleyStringRequest;
import com.example.uilayer.R;
import com.example.uilayer.milestones.adapters.MilesAdapter;
import com.example.uilayer.milestones.betterAdapter.model.Mile;
import com.example.uilayer.milestones.betterAdapter.model.Milestones;
import com.example.uilayer.milestones.betterAdapter.model.Training;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.domainlayer.Constants.BASE_URL;
import static com.example.domainlayer.Constants.KEY_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.KEY_ARCHIVE;
import static com.example.domainlayer.Constants.KEY_ARCHIVED;
import static com.example.domainlayer.Constants.KEY_CONTENT;
import static com.example.domainlayer.Constants.KEY_DEVICE_TYPE;
import static com.example.domainlayer.Constants.KEY_ID;
import static com.example.domainlayer.Constants.KEY_CONTENT_INDEX;
import static com.example.domainlayer.Constants.KEY_DESCRIPTION;
import static com.example.domainlayer.Constants.KEY_MILES;
import static com.example.domainlayer.Constants.KEY_MILES_TRAININGS;
import static com.example.domainlayer.Constants.KEY_SECTION;
import static com.example.domainlayer.Constants.KEY_SECTIONS;
import static com.example.domainlayer.Constants.KEY_TITLE;
import static com.example.domainlayer.Constants.KEY_TYPE;
import static com.example.domainlayer.Constants.KEY_UNDOABLE_ID;
import static com.example.domainlayer.Constants.SEPERATOR;
import static com.example.domainlayer.Constants.TEMP_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.TEMP_DEVICE_TYPE;

public class MilestonesActivity extends AppCompatActivity {

    @BindView(R.id.recycler_milestones)
    RecyclerView recyclerView;
    MilesAdapter milestonesAdapter;
    String sectionName = "", className = "";
    boolean isIntro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milestones);
        ButterKnife.bind(this);


        if (getIntent() != null) {
            Intent intent = getIntent();
            className = intent.getStringExtra("class_name");
            sectionName = intent.getStringExtra("section_name");
            isIntro = intent.getBooleanExtra("is_intro", false);
        }
        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(className + " " + sectionName);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                mLayoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);
        ArrayList<TMiles> milesArrayList;

        if (isIntro) {
            milesArrayList = NewDataHolder.getInstance(getApplicationContext()).getIntroTrainingsList();
            milestonesAdapter = new MilesAdapter(this, milesArrayList, -1, true);
        } else {
            milesArrayList = NewDataHolder.getInstance(getApplicationContext()).getMilesList();
            milestonesAdapter = new MilesAdapter(this, milesArrayList, -1, false);
        }


        recyclerView.setAdapter(milestonesAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_milestones, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_archive:
                getArchiveData();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void getArchiveData() {
        VolleyStringRequest archiveRequest = new VolleyStringRequest(Request.Method.GET,

                Constants.SCHOOLS_URL + SharedPreferenceHelper.getSchoolId() + SEPERATOR
                        + KEY_SECTIONS + SEPERATOR
                        + String.valueOf(NewDataHolder.getInstance(this).getCurrentSectionId()) + SEPERATOR
                        + KEY_CONTENT + SEPERATOR + KEY_ARCHIVED,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("archiveRequest", "onResponse: " + response);

                        try {
                            JSONObject archiveResponse = new JSONObject(response);

                            ArrayList<TMiles> archiveList = new NewDataParser().getMiles(archiveResponse.getString(KEY_MILES_TRAININGS));
                            com.example.uilayer.DataHolder.getInstance(getApplicationContext()).
                                    setArchiveData(archiveList);

                            if (archiveList.size() > 0) {
                                startActivity(new Intent(MilestonesActivity.this, ArchiveActivity.class));
                            } else
                                Toast.makeText(MilestonesActivity.this, "No Archives", Toast.LENGTH_SHORT).show();


                        } catch (JSONException ex) {
                            Log.e("archiveRequest", "onResponse: ", ex);
                        }
                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("introMileDetails", "onErrorResponse: " + error);

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

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(archiveRequest);
    }


}

package com.example.uilayer.milestones;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.models.milestones.TMiles;
import com.example.domainlayer.network.VolleySingleton;
import com.example.uilayer.customUtils.VolleyStringRequest;
import com.example.uilayer.DataHolder;
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

import static com.example.domainlayer.Constants.ARCHIVE_URL;
import static com.example.domainlayer.Constants.KEY_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.KEY_ARCHIVE;
import static com.example.domainlayer.Constants.KEY_DEVICE_TYPE;
import static com.example.domainlayer.Constants.KEY_ID;
import static com.example.domainlayer.Constants.KEY_IS_TRAINING;
import static com.example.domainlayer.Constants.KEY_MILESTONE_ID;
import static com.example.domainlayer.Constants.KEY_MILE_INDEX;
import static com.example.domainlayer.Constants.KEY_NOTE;
import static com.example.domainlayer.Constants.KEY_TITLE;
import static com.example.domainlayer.Constants.KEY_TYPE;
import static com.example.domainlayer.Constants.KEY_UNDOABLE_ID;
import static com.example.domainlayer.Constants.TEMP_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.TEMP_DEVICE_TYPE;

public class MilestonesActivity extends AppCompatActivity {
    @BindView(R.id.recycler_milestones)
    RecyclerView recyclerView;
    MilesAdapter milestonesAdapter;
    boolean isBottom;
    String sectionName, className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milestones);
        ButterKnife.bind(this);


        if (getIntent() != null) {
            Intent intent = getIntent();
            className = intent.getStringExtra("class_name");
            sectionName = intent.getStringExtra("section_name");
            getSupportActionBar().setTitle(className + " " + sectionName);
        }
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        // loadAdapterItems();

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                mLayoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);

        milestonesAdapter = new MilesAdapter(this, DataHolder.getInstance(getApplicationContext()).getMilesList(),-1);

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
        VolleyStringRequest archiveRequest = new VolleyStringRequest(Request.Method.GET, ARCHIVE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("archiveRequest", "onResponse: " + response);

                        try {
                            JSONObject introResponse = new JSONObject(response);
                            TMiles miles = null;
                            ArrayList<TMiles> archiveList = new ArrayList<>();
                            JSONArray archiveArray = new JSONArray(introResponse.getString(KEY_ARCHIVE));
                            for (int j = 0; j < archiveArray.length(); j++) {
                                JSONObject milesJson = archiveArray.getJSONObject(j);
                                miles = new TMiles(milesJson.getInt(KEY_ID),
                                        milesJson.getInt(KEY_MILESTONE_ID),
                                        milesJson.getInt(KEY_MILE_INDEX),
                                        milesJson.getInt(KEY_IS_TRAINING),
                                        milesJson.getString(KEY_TITLE),
                                        milesJson.getString(KEY_NOTE),
                                        milesJson.getString(KEY_TYPE)
                                );
                                archiveList.add(j, miles);
                            }

                            com.example.uilayer.DataHolder.getInstance(getApplicationContext()).
                                    setArchiveData(archiveList);
                            com.example.uilayer.DataHolder.getInstance(getApplicationContext()).
                                    setUndoableId(introResponse.getInt(KEY_UNDOABLE_ID));
                            startActivity(new Intent(MilestonesActivity.this, ArchiveActivity.class));

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
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new ArrayMap<>();
                header.put(KEY_ACCESS_TOKEN, TEMP_ACCESS_TOKEN);
                header.put(KEY_DEVICE_TYPE, TEMP_DEVICE_TYPE);
                return header;
            }

        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(archiveRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    void loadAdapterItems() {
        ArrayList<Milestones> list = new ArrayList<>();
        list.add(new Mile(1, 1, "Mile", "Mile one"));
        list.add(new Mile(1, 2, "Mile", "Mile two"));
        list.add(new Training(1, 2, "Training", "Training one"));
        list.add(new Training(1, 2, "Training", "Training two"));
        list.add(new Mile(1, 3, "Mile", "Mile three"));
        list.add(new Training(1, 2, "Training", "Training three"));
        list.add(new Mile(1, 4, "Mile", "Mile four"));
        list.add(new Mile(1, 5, "Mile", "Mile five"));
        // milestonesAdapter = new MilesAdapter(getApplicationContext(), list);
    }
}

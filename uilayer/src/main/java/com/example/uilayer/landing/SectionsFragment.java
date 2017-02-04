package com.example.uilayer.landing;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.Constants;
import com.example.domainlayer.models.DbUser;
import com.example.domainlayer.models.Sections;
import com.example.domainlayer.models.milestones.TMileData;
import com.example.domainlayer.network.VolleySingleton;
import com.example.domainlayer.temp.DataHolder;
import com.example.domainlayer.temp.DataParser;
import com.example.uilayer.NewDataHolder;
import com.example.uilayer.customUtils.Utils;
import com.example.uilayer.customUtils.VerticalSpaceItemDecoration;
import com.example.uilayer.customUtils.VolleyStringRequest;
import com.example.uilayer.R;
import com.example.uilayer.adapters.SectionsAdapter;
import com.example.uilayer.customUtils.HorizontalSpaceItemDecoration;
import com.example.uilayer.milestones.MilesActivity;
import com.example.uilayer.models.SectionDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.example.domainlayer.Constants.INTRO_TRAINING_URL;
import static com.example.domainlayer.Constants.KEY_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.KEY_BODY;
import static com.example.domainlayer.Constants.KEY_DEVICE_TYPE;
import static com.example.domainlayer.Constants.KEY_ID;
import static com.example.domainlayer.Constants.KEY_INTRO_CONTENT;
import static com.example.domainlayer.Constants.KEY_TITLE;
import static com.example.domainlayer.Constants.KEY_TYPE;
import static com.example.domainlayer.Constants.TEMP_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.TEMP_DEVICE_TYPE;
import static com.example.domainlayer.Constants.TYPE_AUDIO;
import static com.example.domainlayer.Constants.TYPE_IMAGE;
import static com.example.domainlayer.Constants.TYPE_VIDEO;


/**
 * Created by thoughtchimp on 11/7/2016.
 */

public class SectionsFragment extends Fragment {
    @BindView(R.id.sections_recycle_grid)
    RecyclerView sectionsGrid;
    @BindView(R.id.layout_no_sections)
    RelativeLayout noSectionsLayout;
    @BindView(R.id.imageIntroductory)
    ImageView imageViewIntroductory;
    @BindView(R.id.card)
    CardView introCardView;
    @BindView(R.id.layout_intro_training)
    LinearLayout cardLayout;
    List<SectionDetails> sectionDetails;
    List<Sections> sectionDetails1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sections,
                container, false);
        ButterKnife.bind(this, view);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        sectionsGrid.setLayoutManager(layoutManager);
        sectionsGrid.addItemDecoration(new HorizontalSpaceItemDecoration(getActivity(), 3, 3, 3));
        //  Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user_placeholder);
        // imageViewIntroductory.setImageDrawable(Utils.getInstance().getCirclularImage(getActivity(), imageBitmap));
        cardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getIntroTrainings();
            }
        });
        return view;
    }

    private void openActivity(Class<?> activityClass, boolean isMile) {
        Intent intent = new Intent(getActivity(), activityClass);
        intent.putExtra("isMile", isMile);
        if (introTrainingId != -1) {
            intent.putExtra(KEY_ID, isMile);
        }
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }

    int introTrainingId = -1;

    void getIntroTrainings() {

        VolleyStringRequest introTrainingsRequest = new VolleyStringRequest(Request.Method.GET, INTRO_TRAINING_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("introMileDetails", "onResponse: " + response);

                        try {
                            JSONObject introResponse = new JSONObject(response);
                            introTrainingId = introResponse.getInt(KEY_ID);
                            /*com.example.uilayer.DataHolder.getInstance(getContext()).
                                    setCurrentMileData(new DataParser().getMilesData(introResponse.getString(KEY_INTRO_CONTENT)))*/
                            ;
                            com.example.uilayer.DataHolder.getInstance(getContext()).
                                    setCurrentMileData(getMileData((introResponse.getString(KEY_INTRO_CONTENT))));
                            com.example.uilayer.DataHolder.getInstance(getContext()).setCurrentMileTitle(introResponse.getString(Constants.KEY_TITLE));


                        } catch (JSONException ex) {
                            Log.e("introMileDetails", "onResponse: ", ex);
                        }
                        com.example.uilayer.DataHolder.getInstance(getContext()).setCurrentSection("Training");
                        com.example.uilayer.DataHolder.getInstance(getContext()).setCurrentClass("Intro");
                        openActivity(MilesActivity.class, false);
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
                Utils.getInstance().showToast(getResources().getString(R.string.er_no_intro_trainings));
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

        VolleySingleton.getInstance(getContext()).addToRequestQueue(introTrainingsRequest);
    }

    ArrayList<TMileData> getMileData(String milesDataString) {
        ArrayList<TMileData> sectionsArrayList = new ArrayList<>();
        try {
            // JSONObject jsonObject = new JSONObject(milesDataString);
            JSONArray sectionsArray = new JSONArray(milesDataString);
            for (int i = 0; i < sectionsArray.length(); i++) {
                JSONObject milesDatObject = sectionsArray.getJSONObject(i);
                String type = milesDatObject.getString(KEY_TYPE);
                TMileData mileData
                        = new TMileData(milesDatObject.getInt(KEY_ID),
                        milesDatObject.getString(KEY_TITLE), type
                );
                ArrayList<String> urlsList = new ArrayList<>();
                ArrayList<String> imagesList = new ArrayList<>();
                if (type.equals(TYPE_VIDEO) || type.equals(TYPE_AUDIO)) {
                    JSONArray urlsArray = new JSONArray(milesDatObject.getString(KEY_BODY));

                    for (int j = 0; j < urlsArray.length(); j++) {
                        JSONObject data = urlsArray.getJSONObject(j);
                        String url = data.getString("url");
                        String image = data.getString("image");
                        Log.d("URLS", "getMilesData: " + url);

                        urlsList.add(url);
                        imagesList.add(image);
                    }
                    mileData.setUrlsList(urlsList);
                    mileData.setImagesList(imagesList);
                } else if (type.equals(TYPE_IMAGE)) {
                    JSONArray urlsArray = new JSONArray(milesDatObject.getString(KEY_BODY));

                    for (int j = 0; j < urlsArray.length(); j++) {
                        JSONObject data = urlsArray.getJSONObject(j);
                        String url = data.getString("url");
                        urlsList.add(url);
                    }
                    mileData.setUrlsList(urlsList);
                } else
                    mileData.setBody(milesDatObject.getString(KEY_BODY));
                if (urlsList.size() == 1)
                    mileData.setSingle(true);
                sectionsArrayList.add(mileData);
            }
        } catch (JSONException ex) {

            Log.e("ex", "getMilesData: ", ex);
        }

        return sectionsArrayList;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sectionDetails = new ArrayList<>();
        sectionDetails.add(new SectionDetails("Class 1", "Section A", 25));
        sectionDetails.add(new SectionDetails("Class 2", "Section B", 30));
        sectionDetails.add(new SectionDetails("Class 3", "Section C", 40));
        sectionDetails.add(new SectionDetails("Class 4", "Section D", 63));
        sectionDetails.add(new SectionDetails("Class 5", "Section E", 30));
        sectionDetails.add(new SectionDetails("Class 6", "Section F", 90));
        sectionDetails.add(new SectionDetails("Class 7", "Section G", 46));
        sectionDetails.add(new SectionDetails("Class 8", "Section H", 56));
        sectionDetails.add(new SectionDetails("Class 9", "Section I", 30));
        sectionDetails.add(new SectionDetails("Class 9", "Section I", 97));
        sectionDetails.add(new SectionDetails("Class 9", "Section I", 37));

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        DbUser user = NewDataHolder.getInstance(getActivity()).getUser();
        if (user != null) {
            sectionDetails1 = user.getSectionsList();
        }
        //sectionDetails1 = new ArrayList<>();
        if (sectionDetails1 != null && sectionDetails1.size() > 0) {
            noSectionsLayout.setVisibility(View.GONE);
            sectionsGrid.setAdapter(new SectionsAdapter(actionBar.getThemedContext(), sectionDetails1, 3, null, false));
        } else {
            noSectionsLayout.setVisibility(View.VISIBLE);
            sectionsGrid.setVisibility(View.GONE);
        }
    }
}
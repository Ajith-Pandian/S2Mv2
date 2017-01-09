package com.example.uilayer.landing;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.models.Sections;
import com.example.domainlayer.network.VolleySingleton;
import com.example.domainlayer.temp.DataHolder;
import com.example.domainlayer.temp.DataParser;
import com.example.domainlayer.utils.VolleyStringRequest;
import com.example.uilayer.R;
import com.example.uilayer.adapters.SectionsAdapter;
import com.example.uilayer.customUtils.HorizontalSpaceItemDecoration;
import com.example.uilayer.customUtils.Utils;
import com.example.uilayer.milestones.MilesActivity;
import com.example.uilayer.models.SectionDetails;

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
import static com.example.domainlayer.Constants.KEY_DEVICE_TYPE;
import static com.example.domainlayer.Constants.KEY_INTRO_CONTENT;
import static com.example.domainlayer.Constants.KEY_SCHOOL_ID;
import static com.example.domainlayer.Constants.MILES_TRAININGS_URL;
import static com.example.domainlayer.Constants.TEMP_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.TEMP_DEVICE_TYPE;


/**
 * Created by thoughtchimp on 11/7/2016.
 */

public class SectionsFragment extends Fragment {
    @BindView(R.id.sections_recycle_grid)
    RecyclerView sectionsGrid;
    @BindView(R.id.imageIntroductory)
    ImageView imageViewIntroductory;
    @BindView(R.id.card)
    CardView introCardView;
    @BindView(R.id.layout_intro_training)
    LinearLayout cardLayout;
    List<Sections> sectionDetails;
    List<Sections> sectionDetails1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
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
               // getIntroTrainings();
                String dummyIntroTraining="{\n" +
                        "  \"id\": 11,\n" +
                        "  \"tilte\": \"Introducing Wow Connect\",\n" +
                        "  \"note\": \"This is the introduction to Wow connect\",\n" +
                        "  \"contentMetaSequence\": [\n" +
                        "    {\n" +
                        "      \"id\": 17,\n" +
                        "      \"title\": \"m_content\",\n" +
                        "      \"body\": \"[\\\"https://cdn.pixabay.com/photo/2016/03/28/12/35/cat-1285634_960_720.png\\\",\\\"https://cdn.pixabay.com/photo/2016/03/28/12/35/cat-1285634_960_720.png\\\",\\\"https://cdn.pixabay.com/photo/2016/03/28/12/35/cat-1285634_960_720.png\\\"]\",\n" +
                        "      \"type\": \"image\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"id\": 19,\n" +
                        "      \"title\": \"m_content\",\n" +
                        "      \"body\": \"[\\\"https://cdn.pixabay.com/photo/2016/03/28/12/35/cat-1285634_960_720.png\\\",\\\"https://cdn.pixabay.com/photo/2016/03/28/12/35/cat-1285634_960_720.png\\\",\\\"https://cdn.pixabay.com/photo/2016/03/28/12/35/cat-1285634_960_720.png\\\"]\",\n" +
                        "      \"type\": \"image\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"id\": 18,\n" +
                        "      \"title\": \"m_content\",\n" +
                        "      \"body\": \"[\\\"https://cdn.pixabay.com/photo/2016/03/28/12/35/cat-1285634_960_720.png\\\",\\\"https://cdn.pixabay.com/photo/2016/03/28/12/35/cat-1285634_960_720.png\\\",\\\"https://cdn.pixabay.com/photo/2016/03/28/12/35/cat-1285634_960_720.png\\\"]\",\n" +
                        "      \"type\": \"image\"\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}";
                {
                    try {
                        JSONObject introResponse = new JSONObject(dummyIntroTraining);
                        com.example.uilayer.DataHolder.getInstance(getActivity()).
                                setCurrentMileData(new DataParser().getMilesData(introResponse.getString(KEY_INTRO_CONTENT)));
                        com.example.uilayer.DataHolder.getInstance(getContext()).setCurrentSection("Training ");
                        com.example.uilayer.DataHolder.getInstance(getContext()).setCurrentClass("Intro ");
                        com.example.uilayer.DataHolder.getInstance(getContext()).setCurrentMileTitle("This is the intro training title");
                        openActivity(MilesActivity.class, false);
                    } catch (JSONException ex) {
                        Log.e("introMileDetails", "onResponse: ", ex);
                    }
                }
            }
        });
        return view;
    }

    private void openActivity(Class<?> activityClass, boolean isMile) {
        Intent intent = new Intent(getActivity(), activityClass);
        intent.putExtra("isMile", isMile);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }

    void getIntroTrainings() {
        VolleyStringRequest introTrainingsRequest = new VolleyStringRequest(Request.Method.GET, INTRO_TRAINING_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("introMileDetails", "onResponse: " + response);

                        try {
                            JSONObject introResponse = new JSONObject(response);
                            com.example.uilayer.DataHolder.getInstance(getActivity()).
                                    setCurrentMileData(new DataParser().getMilesData(introResponse.getString(KEY_INTRO_CONTENT)));
                        } catch (JSONException ex) {
                            Log.e("introMileDetails", "onResponse: ", ex);
                        }
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sectionDetails = new ArrayList<>();
        sectionDetails.add(new Sections(1,"Class 1", "Section A", 25,100,2,"Milestone 1",2));
        sectionDetails.add(new Sections(1,"Class 2", "Section B", 30,100,2,"Milestone 2",2));
        sectionDetails.add(new Sections(1,"Class 3", "Section C", 40,100,2,"Milestone 3",2));
        sectionDetails.add(new Sections(1,"Class 4", "Section D", 63,100,2,"Milestone 4",2));
        sectionDetails.add(new Sections(1,"Class 5", "Section E", 30,100,2,"Milestone 5",2));
        sectionDetails.add(new Sections(1,"Class 6", "Section F", 90,100,2,"Milestone 6",2));
        sectionDetails.add(new Sections(1,"Class 7", "Section G", 46,100,2,"Milestone 7",2));
        sectionDetails.add(new Sections(1,"Class 8", "Section H", 56,100,2,"Milestone 8",2));
        sectionDetails.add(new Sections(1,"Class 9", "Section I", 30,100,2,"Milestone 9",2));
        sectionDetails.add(new Sections(1,"Class 9", "Section J", 97,100,2,"Milestone 10",2));
        sectionDetails.add(new Sections(1,"Class 9", "Section K", 37,100,2,"Milestone 11",2));

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
//        sectionDetails1 = DataHolder.getInstance(getActivity()).getUser().getSectionsList();
        //sectionsGrid.setAdapter(new SectionsAdapter(actionBar.getThemedContext(), sectionDetails1, 3, null, false));
        sectionsGrid.setAdapter(new SectionsAdapter(actionBar.getThemedContext(), sectionDetails, 3, null, false));

    }
}
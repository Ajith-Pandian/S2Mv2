package com.example.uilayer.network;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.models.Sections;
import com.example.domainlayer.models.User;
import com.example.domainlayer.network.VolleySingleton;
import com.example.uilayer.customUtils.VolleyStringRequest;
import com.example.uilayer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.domainlayer.Constants.KEY_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.KEY_CLASS;
import static com.example.domainlayer.Constants.KEY_COMPLETED_MILES;
import static com.example.domainlayer.Constants.KEY_DEVICE_TYPE;
import static com.example.domainlayer.Constants.KEY_EMAIL;
import static com.example.domainlayer.Constants.KEY_ID;
import static com.example.domainlayer.Constants.KEY_MILESTONE;
import static com.example.domainlayer.Constants.KEY_MILESTONE_ID;
import static com.example.domainlayer.Constants.KEY_PHONE_NUM;
import static com.example.domainlayer.Constants.KEY_SCHOOL_NAME;
import static com.example.domainlayer.Constants.KEY_SECTION;
import static com.example.domainlayer.Constants.KEY_TOTAL_MILES;
import static com.example.domainlayer.Constants.KEY_USER_ID;
import static com.example.domainlayer.Constants.NETWORK_SECTIONS_URL;
import static com.example.domainlayer.Constants.PREFIX_CLASS;
import static com.example.domainlayer.Constants.PREFIX_SECTION;
import static com.example.domainlayer.Constants.TEMP_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.TEMP_DEVICE_TYPE;

/**
 * Created by thoughtchimp on 12/21/2016.
 */

public class ProfileFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String IS_BASIC = "is_basic";
    private static final String USER = "user";
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    User user;
    VolleyStringRequest getNetworkSectionsRequest;

    public ProfileFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ProfileFragment newInstance(boolean isBasic, User user) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putBoolean(IS_BASIC, isBasic);
        args.putSerializable(USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_recycler, container, false);
        ButterKnife.bind(this, rootView);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

        user = (User) getArguments().getSerializable(USER);
        if (getArguments().getBoolean(IS_BASIC)) {
            BaseInfoAdapter adapter = new BaseInfoAdapter(getActivity(),
                    getProfileFromUser());
            recyclerView.setAdapter(adapter);
        } else {
            loadSections();
        }

        return rootView;
    }

    void loadSections() {
        getNetworkSectionsRequest = new VolleyStringRequest(Request.Method.POST, NETWORK_SECTIONS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("getNetworkSecRequest", "onResponse: " + response);
                        updateSections(response);
                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("getNetworkSecRequest", "onErrorResponse: " + error);

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


            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new ArrayMap<>();
                params.put(KEY_USER_ID, String.valueOf(user.getId()));
                Log.d("Userid", "getParams: "+String.valueOf(user.getId()));
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(getNetworkSectionsRequest);
    }

    void updateSections(String networkSectionsString) {
        ArrayList<Sections> sectionsList = new ArrayList<>();
        try {

            JSONArray sectionsArray = new JSONArray(networkSectionsString);
            for (int i = 0; i < sectionsArray.length(); i++) {
                JSONObject sectionObj = sectionsArray.getJSONObject(i);
                Sections section = new Sections();
                JSONObject secOnj = sectionObj.getJSONObject(KEY_SECTION);
                section.setId(secOnj.getInt(KEY_ID));
                section.set_Class((PREFIX_CLASS + secOnj.getString(KEY_CLASS)));
                section.setSection((PREFIX_SECTION + secOnj.getString(KEY_SECTION)));
               // section.setNumOfStuds(sectionObj.getInt(KEY_COMPLETED_MILES));
                section.setCompletedMiles(sectionObj.getInt(KEY_COMPLETED_MILES));
                section.setTotalMiles(sectionObj.getInt(KEY_TOTAL_MILES));
                section.setMilestoneId(sectionObj.getInt(KEY_MILESTONE_ID));
                section.setMilestoneName("M"+sectionObj.getString(KEY_MILESTONE));
                //section.setMilestoneName("M10");
                sectionsList.add(i, section);
            }
        } catch (JSONException ex) {
            Log.e("updateSections", "updateSections: ", ex);
        }
        ProfileSectionsAdapter adapter = new ProfileSectionsAdapter(getActivity(),
                sectionsList);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(mDividerItemDecoration);
        recyclerView.setAdapter(adapter);
    }

    ArrayList<ProfileModel> getProfileFromUser() {
        ArrayList<ProfileModel> profileModels = new ArrayList<>();
        profileModels.add(new ProfileModel(R.drawable.email, user.getEmail(), KEY_EMAIL));
        profileModels.add(new ProfileModel(R.drawable.phone, user.getPhoneNum(), KEY_PHONE_NUM));
        profileModels.add(new ProfileModel(R.drawable.scholl, user.getSchoolName(), KEY_SCHOOL_NAME));
        return profileModels;
    }


}
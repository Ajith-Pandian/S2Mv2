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
import com.example.domainlayer.models.DbUser;
import com.example.domainlayer.models.Sections;
import com.example.domainlayer.models.User;
import com.example.domainlayer.network.VolleySingleton;
import com.example.uilayer.NewDataHolder;
import com.example.uilayer.SharedPreferenceHelper;
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
    DbUser user;
    VolleyStringRequest getNetworkSectionsRequest;

    public ProfileFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ProfileFragment newInstance(boolean isBasic) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putBoolean(IS_BASIC, isBasic);
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

        user = NewDataHolder.getInstance(getContext()).getCurrentNetworkUser();
        if (getArguments().getBoolean(IS_BASIC)) {
            BaseInfoAdapter adapter = new BaseInfoAdapter(getActivity(),
                    getProfileFromUser());
            recyclerView.setAdapter(adapter);
        } else {
            ProfileSectionsAdapter adapter = new ProfileSectionsAdapter(getActivity(),
                    user.getSectionsList());
            DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(mDividerItemDecoration);
            recyclerView.setAdapter(adapter);
        }

        return rootView;
    }



    ArrayList<ProfileModel> getProfileFromUser() {
        ArrayList<ProfileModel> profileModels = new ArrayList<>();
        profileModels.add(new ProfileModel(R.drawable.email, user.getEmail(), KEY_EMAIL));
        profileModels.add(new ProfileModel(R.drawable.phone, user.getPhoneNum(), KEY_PHONE_NUM));
        profileModels.add(new ProfileModel(R.drawable.scholl, SharedPreferenceHelper.getSchoolName(), KEY_SCHOOL_NAME));
        return profileModels;
    }


}
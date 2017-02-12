package com.example.wowconnect.ui.network;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.wowconnect.NewDataHolder;
import com.example.wowconnect.SharedPreferenceHelper;
import com.example.wowconnect.R;
import com.example.wowconnect.domain.Constants;
import com.example.wowconnect.models.DbUser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;



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
    @BindView(R.id.layout_no_sections)
    RelativeLayout noSectionsLayout;

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
        NewDataHolder holder = NewDataHolder.getInstance(getContext());
        //user = .getCurrentNetworkUser();
        if (getArguments().getBoolean(IS_BASIC)) {
            BaseInfoAdapter adapter = new BaseInfoAdapter(getActivity(),
                    getProfileFromUser());
            recyclerView.setAdapter(adapter);
        } else {
            DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(mDividerItemDecoration);
            DbUser currentProfile = NewDataHolder.getInstance(getContext()).getCurrentNetworkUser();
            if (currentProfile.getSectionsList() != null && currentProfile.getSectionsList().size() > 0) {
                noSectionsLayout.setVisibility(View.GONE);
                ProfileSectionsAdapter adapter = new ProfileSectionsAdapter(getActivity(), currentProfile.getSectionsList());
                recyclerView.setAdapter(adapter);
            } else {
                noSectionsLayout.setVisibility(View.VISIBLE);
            }
        }

        return rootView;
    }


    ArrayList<ProfileModel> getProfileFromUser() {
        ArrayList<ProfileModel> profileModels = new ArrayList<>();
        DbUser currentProfile = NewDataHolder.getInstance(getContext()).getCurrentNetworkUser();
        profileModels.add(new ProfileModel(R.drawable.email, currentProfile.getEmail(), Constants.TEXT_EMAIL));
        profileModels.add(new ProfileModel(R.drawable.phone, currentProfile.getPhoneNum(), Constants.TEXT_PHONE_NUMBER));
        profileModels.add(new ProfileModel(R.drawable.scholl, SharedPreferenceHelper.getSchoolName(), Constants.TEXT_SCHOOL_NAME));
        if (currentProfile.getGender() != null && !currentProfile.getGender().equals(""))
            profileModels.add(new ProfileModel(R.drawable.gender, currentProfile.getGender(), Constants.TEXT_GENDER));
        if (currentProfile.getDob() != null && !currentProfile.getDob().equals(""))
            profileModels.add(new ProfileModel(R.drawable.birthday, currentProfile.getDob(), Constants.TEXT_DOB));
        if (currentProfile.getAnniversary() != null && !currentProfile.getAnniversary().equals(""))
            profileModels.add(new ProfileModel(R.drawable.anniversary, currentProfile.getAnniversary(), Constants.TEXT_ANNIVERSARY));
        return profileModels;
    }


}
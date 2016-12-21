package com.example.uilayer.network;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.domainlayer.models.User;
import com.example.domainlayer.models.milestones.TMiles;
import com.example.uilayer.DataHolder;
import com.example.uilayer.R;
import com.example.uilayer.milestones.adapters.MilesAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.domainlayer.Constants.KEY_EMAIL;
import static com.example.domainlayer.Constants.KEY_PHONE_NUM;
import static com.example.domainlayer.Constants.KEY_SCHOOL_NAME;

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


        if (getArguments().getBoolean(IS_BASIC))
        { BaseInfoAdapter adapter=new BaseInfoAdapter(getActivity(),
                getProfileFromUser((User)getArguments().getSerializable(USER)));
            recyclerView.setAdapter(adapter);
        }
        else
        {}


        return rootView;
    }

    ArrayList<TMiles> getMilesOrTrainingsList(int type) {
        //  ArrayList<TMiles> actualList = DataHolder.getInstance(getActivity()).getArchiveData();
        ArrayList<TMiles> filteredList = new ArrayList<>();/*
            for (int i = 0; i < actualList.size(); i++) {
                if (actualList.get(i).getIsTraining() == type)
                    filteredList.add(actualList.get(i));
            }
            Collections.reverse(filteredList);*/
        return filteredList;
    }

    ArrayList<ProfileModel> getProfileFromUser(User user) {
        ArrayList<ProfileModel> profileModels = new ArrayList<>();
        profileModels.add(new ProfileModel(R.drawable.email, user.getEmail(), KEY_EMAIL));
        profileModels.add(new ProfileModel(R.drawable.phone, user.getPhoneNum(), KEY_PHONE_NUM));
        profileModels.add(new ProfileModel(R.drawable.scholl, user.getSchoolName(), KEY_SCHOOL_NAME));
        return profileModels;
    }




}
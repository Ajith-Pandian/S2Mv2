package com.example.uilayer.landing;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.domainlayer.database.DataBaseUtil;
import com.example.domainlayer.models.User;
import com.example.uilayer.R;
import com.example.uilayer.customUtils.Utils;

import java.sql.SQLException;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.uilayer.Constants.SUFFIX_MILES;
import static com.example.uilayer.Constants.SUFFIX_WOWS;

/**
 * Created by thoughtchimp on 11/7/2016.
 */

public class HomeFragment extends Fragment {
    final String TAG = "HomeFragment";
    @BindView(R.id.profile_image)
    ImageView profileImage;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.designation)
    TextView designation;
    @BindView(R.id.text_wow)
    TextView textWow;
    @BindView(R.id.text_miles)
    TextView textMiles;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,
                container, false);
        ButterKnife.bind(this, view);
        try {
            loadUserData();
        } catch (SQLException ex) {
            Log.e(TAG, "onCreateView: ", ex);
        }
        Bitmap imageBitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.profile);

        Utils.getInstance().getCirclularImage(getActivity(), imageBitmap);

        return view;
    }

    void loadUserData() throws SQLException {
        User user = new DataBaseUtil(getActivity()).getUser();
        String nameString = user.getFirstName() + " " + user.getLastName();
        name.setText(nameString);
        textWow.setText(user.getWow() + SUFFIX_WOWS);
        textMiles.setText(user.getMiles() + SUFFIX_MILES);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
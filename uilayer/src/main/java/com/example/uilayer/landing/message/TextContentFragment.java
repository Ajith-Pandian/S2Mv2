package com.example.uilayer.landing.message;


import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.domainlayer.models.Ticket;
import com.example.uilayer.R;
import com.example.uilayer.customUtils.VerticalSpaceItemDecoration;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.domainlayer.Constants.FB_CHILD_TICKET_DETAILS;


/**
 * Created by thoughtchimp on 11/7/2016.
 */

public class TextContentFragment extends Fragment {

    private static final String TEXT = "text";
    @BindView(R.id.text_content)
    TextView textContent;

    public TextContentFragment() {

    }

    public static TextContentFragment newInstance(String textMessage) {
        TextContentFragment fragment = new TextContentFragment();
        Bundle args = new Bundle();
        args.putString(TEXT, textMessage);
        fragment.setArguments(args);
        return fragment;
    }

    @TargetApi(17)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text_content,
                container, false);
        ButterKnife.bind(this, view);
        String msg = "";
        if (getArguments() != null) {
            msg = getArguments().getString(TEXT);
        }
        textContent.setText(msg);
        return view;
    }


}
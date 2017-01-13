package com.example.uilayer.message;


import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uilayer.R;

import butterknife.BindView;
import butterknife.ButterKnife;


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
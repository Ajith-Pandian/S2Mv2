package com.example.uilayer.landing;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.uilayer.R;
import com.example.uilayer.adapters.SectionsAdapter;
import com.example.uilayer.customUtils.HorizontalSpaceItemDecoration;
import com.example.uilayer.customUtils.Utils;
import com.example.uilayer.models.SectionDetails;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by thoughtchimp on 11/7/2016.
 */

public class SectionsFragment extends Fragment {
    @BindView(R.id.sections_recycle_grid)
    RecyclerView sectionsGrid;
    @BindView(R.id.imageIntroductory)
    ImageView imageViewIntroductory;

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
        sectionsGrid.addItemDecoration(new HorizontalSpaceItemDecoration(getActivity(), 3,3,3));
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user_placeholder);
        imageViewIntroductory.setImageDrawable(Utils.getInstance().getCirclularImage(getActivity(), imageBitmap));
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<SectionDetails> sectionDetails = new ArrayList<>();
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
        sectionsGrid.setAdapter(new SectionsAdapter(actionBar.getThemedContext(), sectionDetails));

    }
}
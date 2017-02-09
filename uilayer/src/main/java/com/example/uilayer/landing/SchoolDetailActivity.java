package com.example.uilayer.landing;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.example.uilayer.NewDataHolder;
import com.example.uilayer.R;
import com.example.uilayer.SharedPreferenceHelper;
import com.example.uilayer.adapters.SchoolActivitiesAdapter;
import com.example.uilayer.models.SchoolDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SchoolDetailActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.image_school)
    ImageView schoolImage;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.recycler_school_activities)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        collapsingToolbarLayout.setTitle(SharedPreferenceHelper.getSchoolName());
        Picasso.with(this).load(SharedPreferenceHelper.getSchoolImage()).into(schoolImage);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                mLayoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(new
                SchoolActivitiesAdapter(getApplicationContext(), NewDataHolder.getInstance(this).getSclActList()));
    }


}

package com.example.wowconnect.ui.landing;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.wowconnect.NewDataHolder;
import com.example.wowconnect.R;
import com.example.wowconnect.SharedPreferenceHelper;
import com.example.wowconnect.models.Schools;
import com.example.wowconnect.models.SclActs;
import com.example.wowconnect.ui.adapters.SchoolActivitiesAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

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
            ActionBar actionBar = getSupportActionBar();
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        }
        Schools school = NewDataHolder
                .getInstance(this)
                .getSchoolById(SharedPreferenceHelper.getSchoolId());
        collapsingToolbarLayout.setTitle(school.getName() + " ");
        if (school.getLogo() != null && !school.getLogo().isEmpty())
            Picasso.with(this).load(school.getLogo()).into(schoolImage);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                mLayoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);
        recyclerView.setNestedScrollingEnabled(false);
        ArrayList<SclActs> sclActsArrayList = NewDataHolder.getInstance(this).getSclActList();
        Collections.reverse(sclActsArrayList);
        recyclerView.setAdapter(new
                SchoolActivitiesAdapter(getApplicationContext(), sclActsArrayList));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

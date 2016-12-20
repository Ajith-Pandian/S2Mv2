package com.example.uilayer.landing;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.uilayer.R;
import com.example.uilayer.adapters.SchoolActivitiesAdapter;
import com.example.uilayer.models.SchoolDetails;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SchoolDetailActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_school_activities)
    RecyclerView recyclerView;
    ArrayList<SchoolDetails> strings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                mLayoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);
        recyclerView.setNestedScrollingEnabled(false);
        strings = new ArrayList<>();
        strings.add(new SchoolDetails("St.Jhons Hr Sec School", getResources().getString(R.string.school_msg_one),
                "09.11.2016", "2.15 PM", "123"));
        strings.add(new SchoolDetails("St.Jhons Hr Sec School", getResources().getString(R.string.school_msg_two),
                "09.12.2017", "7.26 AM", "456"));
        strings.add(new SchoolDetails("St.Jhons Hr Sec School", getResources().getString(R.string.school_msg_one),
                "10.11.2018", "2.46 AM", "789"));
        strings.add(new SchoolDetails("St.Jhons Hr Sec School", getResources().getString(R.string.school_msg_two),
                "09.11.2019", "5.32 PM", "102"));
        strings.add(new SchoolDetails("St.Jhons Hr Sec School", getResources().getString(R.string.school_msg_one),
                "09.11.2020", "9.05 PM", "098"));
        recyclerView.setAdapter(new SchoolActivitiesAdapter(getApplicationContext(), strings));
    }


}

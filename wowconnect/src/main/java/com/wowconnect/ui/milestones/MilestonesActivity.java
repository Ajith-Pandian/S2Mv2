package com.wowconnect.ui.milestones;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wowconnect.NetworkHelper;
import com.wowconnect.NewDataHolder;
import com.wowconnect.R;
import com.wowconnect.models.milestones.TMiles;
import com.wowconnect.ui.milestones.adapters.MilesAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MilestonesActivity extends AppCompatActivity {

    @BindView(R.id.recycler_milestones)
    RecyclerView recyclerView;
    @BindView(R.id.layout_no_data)
    RelativeLayout noDataLayout;
    MilesAdapter milestonesAdapter;
    String sectionName = "", className = "";
    boolean isIntro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milestones);
        ButterKnife.bind(this);


        if (getIntent() != null) {
            Intent intent = getIntent();
            className = intent.getStringExtra("class_name");
            sectionName = intent.getStringExtra("section_name");
            isIntro = intent.getBooleanExtra("is_intro", false);
        }
        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(className + " " + sectionName);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                mLayoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);


    }

    void showNoDataLayout() {
        recyclerView.setVisibility(View.GONE);
        noDataLayout.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<TMiles> milesArrayList;
        if (isIntro) {
            milesArrayList = NewDataHolder.getInstance(getApplicationContext()).getIntroTrainingsList();
        } else {
            milesArrayList = NewDataHolder.getInstance(getApplicationContext()).getMilesList();
        }

        if (milesArrayList != null && milesArrayList.size() > 0) {
            milestonesAdapter = new MilesAdapter(this, milesArrayList, false, isIntro, null);
            recyclerView.setAdapter(milestonesAdapter);
        } else
            showNoDataLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (!isIntro) {
            getMenuInflater().inflate(R.menu.menu_milestones, menu);
            return true;
        } else
            return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_archive:
                getArchiveData();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void getArchiveData() {
        new NetworkHelper(this).getArchiveContent(NewDataHolder.getInstance(this).getCurrentSectionId(), new NetworkHelper.NetworkListener() {
            @Override
            public void onFinish() {
                ArrayList<TMiles> archiveList = NewDataHolder.getInstance(MilestonesActivity.this).getArchiveList();

                if (archiveList.size() > 0) {
                    startActivity(new Intent(MilestonesActivity.this, ArchiveActivity.class));
                } else
                    Toast.makeText(MilestonesActivity.this, "No Archives", Toast.LENGTH_SHORT).show();
            }
        });

    }


}

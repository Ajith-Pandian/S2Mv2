package com.example.uilayer.milestones;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.example.uilayer.DataHolder;
import com.example.uilayer.R;
import com.example.uilayer.milestones.adapters.MilesAdapter;
import com.example.uilayer.milestones.betterAdapter.model.Mile;
import com.example.uilayer.milestones.betterAdapter.model.Milestones;
import com.example.uilayer.milestones.betterAdapter.model.Training;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MilestonesActivity extends AppCompatActivity {
    @BindView(R.id.recycler_milestones)
    RecyclerView recyclerView;
    MilesAdapter milestonesAdapter;
    boolean isBottom;
String sectionName,className;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milestones);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            Intent intent = getIntent();
             className = intent.getStringExtra("class_name");
             sectionName = intent.getStringExtra("section_name");
            getSupportActionBar().setTitle(className + " " + sectionName);
        }
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
       // loadAdapterItems();

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                mLayoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);

        milestonesAdapter = new MilesAdapter(getApplicationContext(), DataHolder.getInstance(getApplicationContext()).getMilesList());

        recyclerView.setAdapter(milestonesAdapter);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    void loadAdapterItems() {
        ArrayList<Milestones> list = new ArrayList<>();
        list.add(new Mile(1, 1, "Mile", "Mile one"));
        list.add(new Mile(1, 2, "Mile", "Mile two"));
        list.add(new Training(1, 2, "Training", "Training one"));
        list.add(new Training(1, 2, "Training", "Training two"));
        list.add(new Mile(1, 3, "Mile", "Mile three"));
        list.add(new Training(1, 2, "Training", "Training three"));
        list.add(new Mile(1, 4, "Mile", "Mile four"));
        list.add(new Mile(1, 5, "Mile", "Mile five"));
       // milestonesAdapter = new MilesAdapter(getApplicationContext(), list);
    }
}

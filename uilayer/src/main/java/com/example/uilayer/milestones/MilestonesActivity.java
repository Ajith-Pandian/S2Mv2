package com.example.uilayer.milestones;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.uilayer.R;
import com.example.uilayer.milestones.betterAdapter.MilestonesAdapter;
import com.example.uilayer.milestones.betterAdapter.model.Mile;
import com.example.uilayer.milestones.betterAdapter.model.Training;
import com.example.uilayer.milestones.betterAdapter.viewmodel.MilesVieModel;
import com.example.uilayer.milestones.betterAdapter.viewmodel.TrainingViewModel;
import com.example.uilayer.milestones.betterAdapter.viewmodel.ViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MilestonesActivity extends AppCompatActivity {
    @BindView(R.id.recycler_milestones)
    RecyclerView recyclerView;
    MilestonesAdapter milestonesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milestones);
        ButterKnife.bind(this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        loadAdapterItems();
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                mLayoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);
        recyclerView.setAdapter(milestonesAdapter);
    }

    void loadAdapterItems() {
        ArrayList<ViewModel> list = new ArrayList<>();
        list.add(new MilesVieModel(new Mile(1, 2, "Mile", "Mile one")));
        list.add(new MilesVieModel(new Mile(1, 2, "Mile", "Mile two")));
        list.add(new TrainingViewModel(new Training(1, 2, "Mile", "Training one")));
        list.add(new TrainingViewModel(new Training(1, 2, "Mile", "Training two")));
        list.add(new MilesVieModel(new Mile(1, 2, "Mile", "Mile three")));
        list.add(new TrainingViewModel(new Training(1, 2, "Mile", "Training three")));
        list.add(new MilesVieModel(new Mile(1, 2, "Mile", "Mile four")));
        list.add(new MilesVieModel(new Mile(1, 2, "Mile", "Mile five")));
        milestonesAdapter = new MilestonesAdapter(list);
    }
}

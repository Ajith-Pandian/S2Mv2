package com.example.uilayer.milestones;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.example.uilayer.R;
import com.example.uilayer.milestones.adapters.MCQAnswersAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MCQActivity extends AppCompatActivity {
    @BindView(R.id.toolbar_activity_mcq)
    Toolbar toolbar;
    @BindView(R.id.list_answers)
    ListView listView;
    @BindView(R.id.text_question)
    TextView textQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcq);
        ButterKnife.bind(this);
        toolbar.setTitle("Quiz");
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<String> options = new ArrayList<>();
        options.add(getResources().getString(R.string.options_feedback_1));
        options.add(getResources().getString(R.string.options_feedback_2));
        options.add(getResources().getString(R.string.options_feedback_3));
        options.add(getResources().getString(R.string.options_feedback_4));
        MCQAnswersAdapter adapter = new MCQAnswersAdapter(getApplicationContext(), options);
        listView.setAdapter(adapter);
    }

}

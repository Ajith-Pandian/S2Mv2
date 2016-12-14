package com.example.uilayer.milestones;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uilayer.R;
import com.example.uilayer.customUtils.Utils;
import com.example.uilayer.milestones.adapters.OptionsAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrainingActivity extends AppCompatActivity {
    @BindView(R.id.image_sub_training)
    ImageView imageView;
    @BindView(R.id.bottom_sheet_mile)
    FrameLayout bottomSheet;
    @BindView(R.id.layout_frame)
    FrameLayout rootFrameLayout;
    @BindView(R.id.button_thumbs_up)
    ImageView imageThumsUp;
    @BindView(R.id.button_thumbs_down)
    ImageView imageThumsDown;
    @BindView(R.id.text_title_mile_bottom_sheet)
    TextView mileTitle;
    @BindView(R.id.close_icon)
    ImageView imageIconClose;
    @BindView(R.id.button_complete)
    Button buttonComplete;
    @BindView(R.id.button_submit_bottom_sheet)
    Button buttonSubmit;
    @BindView(R.id.toolbar_activity_training)
    Toolbar toolbar;
    @BindView(R.id.list_view_options_bottom_sheet)
    ListView listOptions;
    View.OnClickListener sheetShowListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int state = 0;
            switch (view.getId()) {
                case R.id.close_icon:
                    state = BottomSheetBehavior.STATE_HIDDEN;
                    break;
                case R.id.button_complete:
                    state = BottomSheetBehavior.STATE_COLLAPSED;
                    invalidateActivation();
                    imageThumsUp.setActivated(true);
                    break;
            }
            BottomSheetBehavior.from(bottomSheet)
                    .setState(state);
        }
    };
    BottomSheetBehavior bottomSheetBehavior;
    OptionsAdapter optionsAdapter;
    int selected_option = -1;
    View.OnClickListener thumbsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            invalidateActivation();
            switch (view.getId()) {
                case R.id.button_thumbs_up:
                    loadOptions(true);
                    break;
                case R.id.button_thumbs_down:
                    loadOptions(false);
                    break;
            }
            view.setActivated(true);
            BottomSheetBehavior.from(bottomSheet)
                    .setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    };
    @BindView(R.id.layout_collapse_in)
    RelativeLayout layoutCollapse;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        boolean isUp = false;

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            switch (newState) {
                case BottomSheetBehavior.STATE_HIDDEN:
                    rootFrameLayout.getForeground().setAlpha(0);
                    selected_option = -1;
                    break;
                case BottomSheetBehavior.STATE_COLLAPSED:
                    rootFrameLayout.getForeground().setAlpha(200);
                    loadOptions(true);
                    listOptions.setAlpha(0f);
                    // changeLayoutParams(16);
                    isUp = true;
                    break;
                case BottomSheetBehavior.STATE_EXPANDED:
                    // changeLayoutParams(48);
                    isUp = false;
                    break;
            }

        }

        void changeLayoutParams(int pixel) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layoutCollapse.getLayoutParams();
            layoutParams.topMargin = Utils.getInstance().getPixelAsDp(getApplicationContext(), pixel);
            layoutCollapse.setLayoutParams(layoutParams);
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            if (slideOffset < 0) {
                float alpha = (1 + slideOffset) * 200;
                rootFrameLayout.getForeground().setAlpha((int) (alpha));
            } else if (slideOffset > 0) {
                float alpha = (55 * slideOffset) + 200;
                rootFrameLayout.getForeground().setAlpha((int) (alpha));
                listOptions.setAlpha(slideOffset);

                if (isUp) {
                    Log.d("Slide", "onSlide: UP");
                    changeLayoutParams((int) (48 * slideOffset));
                } else {
                    Log.d("Slide", "onSlide: DOWN");

                    changeLayoutParams((int) (48 - 48 * (1 - slideOffset)));

                }
            }

        }
    };

    void loadOptions(boolean isUp) {
        ArrayList<String> options = new ArrayList<>();
        if (isUp) {
            options.add(getResources().getString(R.string.options_feedback_1));
            options.add(getResources().getString(R.string.options_feedback_2));
            options.add(getResources().getString(R.string.options_feedback_3));
            options.add(getResources().getString(R.string.options_feedback_4));
        } else {
            options.add(getResources().getString(R.string.options_feedback_down_1));
            options.add(getResources().getString(R.string.options_feedback_down_2));
            options.add(getResources().getString(R.string.options_feedback_down_3));
            options.add(getResources().getString(R.string.options_feedback_down_4));
        }
        optionsAdapter = new OptionsAdapter(getApplicationContext(), options);
        listOptions.setAdapter(optionsAdapter);
        selected_option = -1;
    }

    void invalidateActivation() {
        imageThumsDown.setActivated(false);
        imageThumsUp.setActivated(false);
    }

    @SuppressWarnings("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        getSupportActionBar().setTitle("Training");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Picasso.with(getApplicationContext()).load("http://img.youtube.com/vi/9Wbmhg7NhFI/0.jpg").into(imageView);

        rootFrameLayout.getForeground().setAlpha(0);
        BottomSheetBehavior.from(bottomSheet)
                .setState(BottomSheetBehavior.STATE_HIDDEN);

        initBottomSheet();
        ViewCompat.setElevation(bottomSheet, 16);
        ViewCompat.setElevation(rootFrameLayout, 14);

        buttonComplete.setOnClickListener(sheetShowListener);
        listOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != selected_option) {//  optionsAdapter.notifyDataSetChanged();
                    for (int j = 0; j < adapterView.getChildCount(); j++) {
                        adapterView.getChildAt(j).setActivated(false);
                    }
                    view.setActivated(true);
                    selected_option = i;
                }

            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected_option != -1) {
                    showToast("Submitted successfully ");
                    BottomSheetBehavior.from(bottomSheet)
                            .setState(BottomSheetBehavior.STATE_HIDDEN);
                } else
                    showToast("Please select one option to submit");

            }
        });
    }

    void showToast(String msg) {
        Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT).show();
    }

    void initBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(mBottomSheetBehaviorCallback);

        imageThumsUp.setOnClickListener(thumbsClickListener);
        imageThumsDown.setOnClickListener(thumbsClickListener);

        imageIconClose.setOnClickListener(sheetShowListener);
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

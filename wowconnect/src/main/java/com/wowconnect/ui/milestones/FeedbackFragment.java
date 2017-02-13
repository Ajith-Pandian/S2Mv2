package com.wowconnect.ui.milestones;

import android.app.Dialog;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.wowconnect.NewDataParser;
import com.wowconnect.R;
import com.wowconnect.ui.milestones.adapters.OptionsAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ajit on 02-02-2017.
 */

public class FeedbackFragment extends BottomSheetDialogFragment {

    @BindView(R.id.list_view_options)
    ListView listOptions;
    @BindView(R.id.button_submit)
    Button buttonSubmit;

    static FeedbackFragment newInstance;
    BottomSheetBehavior bottomSheetBehavior;
    Dialog thisDialog;

    public static FeedbackFragment getNewInstance() {
        newInstance = new FeedbackFragment();
        return newInstance;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && getDialog().getWindow() != null) {
            final WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getDialog().getWindow().setAttributes(params);
        }
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_bottom_feedback, null);
        dialog.setContentView(contentView);
        thisDialog = dialog;
        ButterKnife.bind(this, contentView);
        //initViews();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            bottomSheetBehavior = ((BottomSheetBehavior) behavior);
            bottomSheetBehavior.setBottomSheetCallback(mBottomSheetBehaviorCallback);
            //bottomSheetBehavior.setPeekHeight(Utils.getInstance().getPixelAsDp(dialog.getContext(), 300));
          /*  bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    switch (newState) {
                        case BottomSheetBehavior.STATE_DRAGGING:
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            break;

                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                }
            });*/
        }
    }

    int selected_option;

    void loadOptions(boolean isUp) {
        ArrayList<String> options = new ArrayList<>();
        if (isUp) {
            options = new NewDataParser().getS2mConfiguration().getMileFeedbackUpOptions();

        } else {
            options = new NewDataParser().getS2mConfiguration().getMileFeedbackDownOptions();

        }
        OptionsAdapter optionsAdapter = new OptionsAdapter(getActivity(), options);
        listOptions.setAdapter(optionsAdapter);
        selected_option = -1;
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        boolean isUp = false;
        @BindView(R.id.layout_collapse_in_mile)
        RelativeLayout layoutCollapse;

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            switch (newState) {
                case BottomSheetBehavior.STATE_HIDDEN:
                    selected_option = -1;
                    break;
                case BottomSheetBehavior.STATE_COLLAPSED:
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
          /* RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layoutCollapse.getLayoutParams();
            layoutParams.topMargin = Utils.getInstance().getPixelAsDp(getApplicationContext(), pixel);
            layoutCollapse.setLayoutParams(layoutParams);*/
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            if (slideOffset < 0) {
                float alpha = (1 + slideOffset) * 200;
            } else if (slideOffset > 0) {
                float alpha = (55 * slideOffset) + 200;
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

}
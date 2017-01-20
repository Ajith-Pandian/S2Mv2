package com.example.uilayer.milestones;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.models.milestones.TMileData;
import com.example.domainlayer.network.VolleySingleton;
import com.example.domainlayer.utils.VolleyStringRequest;
import com.example.uilayer.DataHolder;
import com.example.uilayer.R;
import com.example.uilayer.milestones.adapters.OptionsAdapter;
import com.example.uilayer.milestones.fragments.MilesAudioFragment;
import com.example.uilayer.milestones.fragments.MilesImageFragment;
import com.example.uilayer.milestones.fragments.MilesTextFragment;
import com.example.uilayer.milestones.fragments.MilesVideoFragment;
import com.example.uilayer.models.AudioMiles;
import com.example.uilayer.models.ImageMiles;
import com.example.uilayer.models.VideoMiles;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.domainlayer.Constants.FEEDBACK_CREATE_URL;
import static com.example.domainlayer.Constants.KEY_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.KEY_DEVICE_TYPE;
import static com.example.domainlayer.Constants.KEY_IS_TRAINING;
import static com.example.domainlayer.Constants.KEY_MILE_ID;
import static com.example.domainlayer.Constants.KEY_REASON;
import static com.example.domainlayer.Constants.KEY_SCHOOL_ID;
import static com.example.domainlayer.Constants.KEY_SECTION_ID;
import static com.example.domainlayer.Constants.KEY_THUMBS;
import static com.example.domainlayer.Constants.KEY_TITLE;
import static com.example.domainlayer.Constants.TEMP_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.TEMP_DEVICE_TYPE;
import static com.example.domainlayer.Constants.THUMBS_DOWN;
import static com.example.domainlayer.Constants.THUMBS_UP;
import static com.example.domainlayer.Constants.TYPE_AUDIO;
import static com.example.domainlayer.Constants.TYPE_IMAGE;
import static com.example.domainlayer.Constants.TYPE_TEXT;
import static com.example.domainlayer.Constants.TYPE_VIDEO;

public class MilesActivity extends AppCompatActivity implements MilesTextFragment.OnFragmentInteractionListener
        , MilesVideoFragment.OnFragmentInteractionListener,
        MilesAudioFragment.OnFragmentInteractionListener, MilesImageFragment.OnFragmentInteractionListener {
    final int REQUEST_CODE = 111;
    @BindView(R.id.miles_fragment_container)
    LinearLayout milesFragmentContainer;
    @BindView(R.id.layout_background)
    LinearLayout backgrounLayout;
    @BindView(R.id.image_background)
    ImageView imageBackgroundActivity;
    @BindView(R.id.fragment_panel)
    ScrollView scrollView;
    @BindView(R.id.text_title_mile)
    TextView textTiltle;
    ArrayList<TMileData> mileDataArrayList;
    @BindView(R.id.toolbar_activity_miles)
    Toolbar toolbar;
    @BindView(R.id.layout_frame_miles)
    FrameLayout forgroundLayout;
    BottomSheetBehavior bottomSheetBehavior;
    OptionsAdapter optionsAdapter;
    int selected_option = -1;
    @BindView(R.id.button_thumbs_up)
    ImageView imageThumsUp;
    @BindView(R.id.button_thumbs_down)
    ImageView imageThumsDown;
    @BindView(R.id.bottom_sheet_mile)
    FrameLayout bottomSheet;
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
    @BindView(R.id.list_view_options_bottom_sheet)
    ListView listOptions;
    @BindView(R.id.button_complete)
    Button buttonComplete;
    @BindView(R.id.button_submit_bottom_sheet)
    Button buttonSubmit;
    @BindView(R.id.close_icon)
    ImageView imageIconClose;

    TextView toolbarTitle, toolbarSubTitle;
    ImageButton backButton;
    ArrayList<String> options ;
    boolean isThumbsUp;
    View.OnClickListener thumbsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            invalidateActivation();
            switch (view.getId()) {
                case R.id.button_thumbs_up:
                    loadOptions(isThumbsUp = true);
                    break;
                case R.id.button_thumbs_down:
                    loadOptions(isThumbsUp = false);
                    break;
            }
            view.setActivated(true);
            BottomSheetBehavior.from(bottomSheet)
                    .setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    };
    boolean isMile;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        boolean isUp = false;
        @BindView(R.id.layout_collapse_in_mile)
        RelativeLayout layoutCollapse;

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            switch (newState) {
                case BottomSheetBehavior.STATE_HIDDEN:
                    forgroundLayout.getForeground().setAlpha(0);
                    selected_option = -1;
                    break;
                case BottomSheetBehavior.STATE_COLLAPSED:
                    forgroundLayout.getForeground().setAlpha(200);
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
                forgroundLayout.getForeground().setAlpha((int) (alpha));
            } else if (slideOffset > 0) {
                float alpha = (55 * slideOffset) + 200;
                forgroundLayout.getForeground().setAlpha((int) (alpha));
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

    @Override
    public void onImageFragmentInteraction(Uri uri) {

    }

    @Override
    public void onVideoFragmentInteraction(Uri uri) {

    }

    @Override
    public void onAudioFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    void invalidateActivation() {
        imageThumsDown.setActivated(false);
        imageThumsUp.setActivated(false);
    }

    void loadOptions(boolean isUp) {
        options=new ArrayList<>();
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
    DataHolder holder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        savedInstanceState = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miles);
        ButterKnife.bind(this);


        toolbarTitle = (TextView) toolbar.findViewById(R.id.text_title_toolbar);
        toolbarSubTitle = (TextView) toolbar.findViewById(R.id.text_subtitle_toolbar);
        backButton = (ImageButton) toolbar.findViewById(R.id.button_back_toolbar);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // toolbar.setTitle("Quiz");
        setSupportActionBar(toolbar);


        holder = DataHolder.getInstance(this);
        if (getIntent().getBooleanExtra("isMile", false)) {
            toolbarTitle.setText("Miles");
            isMile = true;
        } else
        {    toolbarTitle.setText("Training");
        isMile = false;}

        String title = holder.getCurrentClass() + " " + holder.getCurrentSection();
        toolbarSubTitle.setText(title);

        forgroundLayout.getForeground().setAlpha(0);
        initTypeView();
        initBottomSheet();
        BottomSheetBehavior.from(bottomSheet)
                .setState(BottomSheetBehavior.STATE_HIDDEN);


        textTiltle.setText(holder.getCurrentMileTitle());

        addFragments();

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
                    sendFeedback();
                    showToast("Submitted SuccessFully");
                    BottomSheetBehavior.from(bottomSheet)
                            .setState(BottomSheetBehavior.STATE_HIDDEN);
                   // finish();
                } else
                    showToast("Please select one option to submit");

            }
        });

    }

    void sendFeedback() {

        VolleyStringRequest feedbackRequest = new VolleyStringRequest(Request.Method.POST, FEEDBACK_CREATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("FeedBack", "onResponse: " + response);
                        showToast("Submitted successfully ");
                        BottomSheetBehavior.from(bottomSheet)
                                .setState(BottomSheetBehavior.STATE_HIDDEN);
                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("FeedBack", "onErrorResponse: " + error);

                    }
                }, new VolleyStringRequest.StatusCodeListener() {
            String TAG = "VolleyStringReq";

            @Override
            public void onBadRequest() {
                Log.d(TAG, "onBadRequest: ");
            }

            @Override
            public void onUnauthorized() {
                Log.d(TAG, "onUnauthorized: ");
            }

            @Override
            public void onNotFound() {
                Log.d(TAG, "onNotFound: ");
            }

            @Override
            public void onConflict() {
                Log.d(TAG, "onConflict: ");
            }

            @Override
            public void onTimeout() {
                Log.d(TAG, "onTimeout: ");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new ArrayMap<>();
                String thumbs;
                int isTraining;
                if (isMile)
                    isTraining = 0;
                else
                    isTraining = 1;
                params.put(KEY_IS_TRAINING, String.valueOf(isTraining));
                params.put(KEY_MILE_ID, "4");
                params.put(KEY_SCHOOL_ID, "2");
                params.put(KEY_SECTION_ID, "4");
                if (isThumbsUp)
                    thumbs = THUMBS_UP;
                else
                    thumbs = THUMBS_DOWN;
                params.put(KEY_THUMBS, thumbs);


                params.put(KEY_REASON, options.get(selected_option));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new ArrayMap<>();
                header.put(KEY_ACCESS_TOKEN, TEMP_ACCESS_TOKEN);
                header.put(KEY_DEVICE_TYPE, TEMP_DEVICE_TYPE);

                return header;
            }

        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(feedbackRequest);
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

    void initTypeView() {
        Resources resources = getResources();
        Drawable background;
        int titleTextColor, buttonTextColor, buttonBackgroundColor;
        int greenPrimary = resources.getColor(R.color.colorPrimary);
        int white = resources.getColor(android.R.color.white);
        if (isMile) {
            background = resources.getDrawable(R.drawable.background_landing);
            buttonBackgroundColor = white;
            buttonTextColor = greenPrimary;
            titleTextColor = white;
            buttonComplete.setOnClickListener(sheetShowListener);
        } else {
            background = resources.getDrawable(R.drawable.bg_training);
            buttonBackgroundColor = greenPrimary;
            buttonTextColor = white;
            titleTextColor = greenPrimary;
            buttonComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivityForResult(new Intent(MilesActivity.this, MCQActivity.class).putExtra(KEY_TITLE,
                            holder.getCurrentMileTitle()), REQUEST_CODE);
                }
            });
        }
        // scrollView.setBackgroundDrawable(background);
        scrollView.setBackgroundDrawable(background);
        buttonComplete.setBackgroundColor(buttonBackgroundColor);
        buttonComplete.setTextColor(buttonTextColor);
        textTiltle.setTextColor(titleTextColor);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                boolean result = data.getBooleanExtra("isSuccess", false);
                if (result) {
                    BottomSheetBehavior.from(bottomSheet)
                            .setState(BottomSheetBehavior.STATE_COLLAPSED);
                    invalidateActivation();
                    imageThumsUp.setActivated(true);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    void initBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(mBottomSheetBehaviorCallback);

        imageThumsUp.setOnClickListener(thumbsClickListener);
        imageThumsDown.setOnClickListener(thumbsClickListener);

        imageIconClose.setOnClickListener(sheetShowListener);
    }

    void showToast(String msg) {
        Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT).show();
    }

    void addFragments() {
        mileDataArrayList = DataHolder.getInstance(getApplicationContext()).getCurrentMileData();
        Fragment fragment = null;
        for (int i = 0; i < mileDataArrayList.size(); i++) {
            TMileData mileData = mileDataArrayList.get(i);
            String type = mileData.getType();
            switch (type) {
                case TYPE_TEXT:
                    fragment = MilesTextFragment.newInstance(mileData.getTitle(),
                            mileData.getBody());
                    break;
                case TYPE_VIDEO:

                    ArrayList<VideoMiles> milesList = new ArrayList<>();
                    for (int j = 0; j < mileData.getUrlsList().size(); j++) {
                        milesList.add(new VideoMiles(j, j, mileData.getUrlsList().get(j)));
                    }
                    fragment = MilesVideoFragment.newInstance("VIDEOS", milesList);
                    break;
                case TYPE_AUDIO:
                    ArrayList<AudioMiles> audioMilesList = new ArrayList<>();
                    for (int j = 0; j < mileData.getUrlsList().size(); j++) {
                        String[] imageAndContentUrls = mileData.getUrlsList().get(j).split("\\$");
                        audioMilesList.add(new AudioMiles(j, j, imageAndContentUrls[0], imageAndContentUrls[1]));
                    }
                    fragment = MilesAudioFragment.newInstance("AUDIO", audioMilesList);
                    break;
                case TYPE_IMAGE:
                    ArrayList<ImageMiles> imageMilesList = new ArrayList<>();
                    for (int j = 0; j < mileData.getUrlsList().size(); j++) {
                        imageMilesList.add(new ImageMiles(j, j, mileData.getTitle(), mileData.getUrlsList().get(j)));
                    }
                    fragment = MilesImageFragment.newInstance("IMAGES", imageMilesList);
                    break;

            }
            if (fragment != null)
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(milesFragmentContainer.getId(), fragment)
                        .commit();

        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}

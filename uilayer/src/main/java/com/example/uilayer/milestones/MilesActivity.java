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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.models.milestones.TMileData;
import com.example.domainlayer.network.VolleySingleton;
import com.example.uilayer.NetworkHelper;
import com.example.uilayer.NewDataHolder;
import com.example.uilayer.NewDataParser;
import com.example.uilayer.SharedPreferenceHelper;
import com.example.uilayer.customUtils.Utils;
import com.example.uilayer.customUtils.VolleyStringRequest;
import com.example.uilayer.R;
import com.example.uilayer.milestones.adapters.OptionsAdapter;
import com.example.uilayer.milestones.fragments.MilesAudioFragment;
import com.example.uilayer.milestones.fragments.MilesImageFragment;
import com.example.uilayer.milestones.fragments.MilesTextFragment;
import com.example.uilayer.milestones.fragments.MilesVideoFragment;
import com.example.uilayer.models.AudioMiles;
import com.example.uilayer.models.ImageMiles;
import com.example.uilayer.models.MCQs;
import com.example.uilayer.models.McqOptions;
import com.example.uilayer.models.VideoMiles;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static com.example.domainlayer.Constants.KEY_ANSWER;
import static com.example.domainlayer.Constants.KEY_COMPLETE;
import static com.example.domainlayer.Constants.KEY_CONTENT;
import static com.example.domainlayer.Constants.KEY_DOWN;
import static com.example.domainlayer.Constants.KEY_ID;
import static com.example.domainlayer.Constants.KEY_MCQS;
import static com.example.domainlayer.Constants.KEY_OPTIONS;
import static com.example.domainlayer.Constants.KEY_QUESTION;
import static com.example.domainlayer.Constants.KEY_REASON;
import static com.example.domainlayer.Constants.KEY_SECTIONS;
import static com.example.domainlayer.Constants.KEY_THUMBS;
import static com.example.domainlayer.Constants.KEY_TITLE;
import static com.example.domainlayer.Constants.KEY_UP;
import static com.example.domainlayer.Constants.MCQS_SUFFIX;
import static com.example.domainlayer.Constants.MILESTONES_URL;
import static com.example.domainlayer.Constants.SCHOOLS_URL;
import static com.example.domainlayer.Constants.SEPERATOR;
import static com.example.domainlayer.Constants.TRAININGS_SUFFIX;
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
    LinearLayout backgroundLayout;
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
    FrameLayout foregroundLayout;
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
    ArrayList<String> options;
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
    boolean isMile, isCompletable;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        boolean isUp = false;
        @BindView(R.id.layout_collapse_in_mile)
        RelativeLayout layoutCollapse;

        @Override
        public void onStateChanged(@NonNull final View bottomSheet, int newState) {
            switch (newState) {
                case BottomSheetBehavior.STATE_HIDDEN:
                    foregroundLayout.getForeground().setAlpha(0);
                    foregroundLayout.setVisibility(GONE);
                    selected_option = -1;
                    break;
                case BottomSheetBehavior.STATE_COLLAPSED:
                    foregroundLayout.setVisibility(View.VISIBLE);
                    foregroundLayout.getForeground().setAlpha(200);
                    foregroundLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            foregroundLayout.setVisibility(GONE);
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        }
                    });
                    loadOptions(true);
                    listOptions.setAlpha(0f);
                    // changeLayoutParams(16);
                    isUp = true;
                    break;
                case BottomSheetBehavior.STATE_EXPANDED:
                    //changeLayoutParams(48);
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
                foregroundLayout.getForeground().setAlpha((int) (alpha));
            } else if (slideOffset > 0) {
                float alpha = (55 * slideOffset) + 200;
                foregroundLayout.getForeground().setAlpha((int) (alpha));
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

    SubmitListener submitListener;

    void invalidateActivation() {
        imageThumsDown.setActivated(false);
        imageThumsUp.setActivated(false);
    }

    void loadOptions(boolean isUp) {
        options = new ArrayList<>();
        if (isUp) {
            options = new NewDataParser().getS2mConfiguration().getMileFeedbackUpOptions();
        } else {
            options = new NewDataParser().getS2mConfiguration().getMileFeedbackDownOptions();
        }
        optionsAdapter = new OptionsAdapter(getApplicationContext(), options);
        listOptions.setAdapter(optionsAdapter);
        selected_option = -1;
    }

    NewDataHolder holder;
    int thisMileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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


        holder = NewDataHolder.getInstance(this);
        if (getIntent().getBooleanExtra("isMile", false)) {
            toolbarTitle.setText("Miles");
            isMile = true;
        } else {
            toolbarTitle.setText("Training");
            isMile = false;
        }

        thisMileId = getIntent().getIntExtra(KEY_ID, -1);
        isCompletable = getIntent().getBooleanExtra("isCompletable", false);

        String title = holder.getCurrentClassName() + " " + holder.getCurrentSectionName();
        toolbarSubTitle.setText(title);

        foregroundLayout.getForeground().setAlpha(0);
        foregroundLayout.setVisibility(GONE);
        initTypeView();
        initBottomSheet();
        BottomSheetBehavior.from(bottomSheet)
                .setState(BottomSheetBehavior.STATE_HIDDEN);


        textTiltle.setText(holder.getCurrentMileTitle());
        if (!isFragmentsAdded)
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
                    //sendFeedback();
                    //finish();
                    submitThis();
                } else
                    showToast("Please select one option to submit");

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NewDataHolder.getInstance(this).getResultsMap() != null) {
            Log.d("onResume", "result: " + NewDataHolder.getInstance(this).getResultsMap().toString());
        }
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
            if (isCompletable)
                buttonComplete.setOnClickListener(sheetShowListener);
            else
                buttonComplete.setVisibility(GONE);
        } else {
            background = resources.getDrawable(R.drawable.bg_training);
            buttonBackgroundColor = greenPrimary;
            buttonTextColor = white;
            titleTextColor = greenPrimary;
            if (isCompletable)
                buttonComplete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getMcqs();
                    }
                });
            else
                buttonComplete.setVisibility(GONE);
        }

        // scrollView.setBackgroundDrawable(background);
        scrollView.setBackgroundDrawable(background);
        buttonComplete.setBackgroundColor(buttonBackgroundColor);
        buttonComplete.setTextColor(buttonTextColor);
        textTiltle.setTextColor(titleTextColor);
    }

    void getMcqs() {
        VolleyStringRequest mcqsRequest = new VolleyStringRequest(Request.Method.GET,
                MILESTONES_URL + SEPERATOR + NewDataHolder.getInstance(this).getCurrentMilestoneId()
                        + TRAININGS_SUFFIX + SEPERATOR + thisMileId + MCQS_SUFFIX,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("MCQ Details", "onResponse: " + response);
                        loadQuestions(response);
                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("MCQ Details", "onErrorResponse: " + error);
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
                Utils.getInstance().showToast(getString(R.string.er_no_mcq));
            }

            @Override
            public void onConflict() {
                Log.d(TAG, "onConflict: ");
            }

            @Override
            public void onTimeout() {
                Log.d(TAG, "onTimeout: ");
            }
        });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(mcqsRequest);
    }


    void loadQuestions(String mcqString) {
        ArrayList<MCQs> mcqsArrayList = new ArrayList<>();
        try {
            JSONObject mcqsObject = new JSONObject(mcqString);
            JSONArray mcqsJsonArray = mcqsObject.getJSONArray(KEY_MCQS);
            if (mcqsJsonArray != null && mcqsJsonArray.length() > 0) {
                for (int i = 0; i < mcqsJsonArray.length(); i++) {
                    JSONObject mcqsJson = mcqsJsonArray.getJSONObject(i);
                    MCQs mcq = new MCQs();
                    mcq.setId(mcqsJson.getInt(KEY_ID));
                    mcq.setAnswer(mcqsJson.getString(KEY_ANSWER));
                    mcq.setQuestion(mcqsJson.getString(KEY_QUESTION));
                    ArrayList<McqOptions> optionsList = new ArrayList<>();

                    JSONArray optionsArray = new JSONArray(mcqsJson.getString(KEY_OPTIONS));
                    for (int ii = 0; ii < optionsArray.length(); ii++) {
                        String optionText = optionsArray.getString(ii);
                        McqOptions options = new McqOptions();
                        options.setText(optionText);
                        optionsList.add(options);
                    }

                    mcq.setOptions(optionsList);
                    mcqsArrayList.add(i, mcq);
                }
                NewDataHolder.getInstance(this).setCurrentMileId(thisMileId);
                startActivityForResult(new Intent(MilesActivity.this, MCQActivity.class)
                        .putExtra(KEY_TITLE, holder.getCurrentMileTitle())
                        .putExtra(KEY_QUESTION, mcqsArrayList), REQUEST_CODE);
            } else Utils.getInstance().showToast("No Quiz found");

        } catch (JSONException ex) {
            Log.e("MCQ", "loadQuestions: ", ex);
        }
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

    boolean isFragmentsAdded = false;

    void addFragments() {
        isFragmentsAdded = true;
        mileDataArrayList = NewDataHolder.getInstance(getApplicationContext()).getMilesDataList();
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
                    for (int j = 0; j < mileData.getVideoIds().size(); j++) {
                        milesList.add(
                                new VideoMiles(j,
                                        mileData.getVideoIds().get(j),
                                        mileData.getImagesList().get(j),
                                        mileData.getHdImagesList().get(j)));
                    }
                    fragment = MilesVideoFragment.newInstance(mileData.getTitle(), milesList);
                    break;
                case TYPE_AUDIO:
                    ArrayList<AudioMiles> audioMilesList = new ArrayList<>();
                    for (int j = 0; j < mileData.getUrlsList().size(); j++) {
                        String audioUrl = mileData.getUrlsList().get(j);
                        String imageUrl = mileData.getImagesList().get(j);
                        audioMilesList.add(new AudioMiles(j, j, imageUrl, audioUrl));
                    }
                    fragment = MilesAudioFragment.newInstance(mileData.getTitle(), audioMilesList);
                    break;
                case TYPE_IMAGE:
                    ArrayList<ImageMiles> imageMilesList = new ArrayList<>();
                    for (int j = 0; j < mileData.getUrlsList().size(); j++) {
                        imageMilesList.add(new ImageMiles(j, j, mileData.getTitle(), mileData.getUrlsList().get(j)));
                    }
                    fragment = MilesImageFragment.newInstance(mileData.getTitle(), imageMilesList);
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    VolleyStringRequest submitRequest;

    void submitThis() {
        submitRequest = new VolleyStringRequest(Request.Method.POST,
                SCHOOLS_URL + SharedPreferenceHelper.getSchoolId() + SEPERATOR +
                        KEY_SECTIONS + SEPERATOR + NewDataHolder.getInstance(this).getCurrentSectionId() + SEPERATOR +
                        KEY_CONTENT + SEPERATOR + NewDataHolder.getInstance(this).getCurrentMileId() + SEPERATOR + KEY_COMPLETE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("sendResults", "onResponse: " + response);
                        Utils.getInstance().showToast("Submitted successfully");
                        new NetworkHelper(MilesActivity.this).getMilestoneContent(
                                NewDataHolder.getInstance(MilesActivity.this).getCurrentSectionId(),
                                new NetworkHelper.NetworkListener() {
                                    @Override
                                    public void onFinish() {
                                        finish();
                                    }
                                });
                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("sendResults", "onErrorResponse: " + error);

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
                if (isThumbsUp)
                    thumbs = KEY_UP;
                else
                    thumbs = KEY_DOWN;
                params.put(KEY_THUMBS, thumbs);
                params.put(KEY_REASON, options.get(selected_option));
                if (!isMile && NewDataHolder.getInstance(MilesActivity.this).getResultsMap() != null)
                    params.putAll(NewDataHolder.getInstance(MilesActivity.this).getResultsMap());

                for (Map.Entry<String, String> entry : params.entrySet()) {
                    System.out.println(entry.getKey() + " ::: " + entry.getValue());
                }
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(submitRequest);
    }

}

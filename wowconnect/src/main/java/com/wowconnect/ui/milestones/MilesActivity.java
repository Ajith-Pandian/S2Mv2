package com.wowconnect.ui.milestones;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import com.wowconnect.NetworkHelper;
import com.wowconnect.NewDataHolder;
import com.wowconnect.NewDataParser;
import com.wowconnect.R;
import com.wowconnect.SharedPreferenceHelper;
import com.wowconnect.domain.Constants;
import com.wowconnect.domain.network.VolleySingleton;
import com.wowconnect.models.S2mConfiguration;
import com.wowconnect.models.mcq.MCQs;
import com.wowconnect.models.mcq.McqOptions;
import com.wowconnect.models.miles.AudioMiles;
import com.wowconnect.models.miles.ImageMiles;
import com.wowconnect.models.miles.VideoMiles;
import com.wowconnect.models.milestones.TMileData;
import com.wowconnect.ui.customUtils.Utils;
import com.wowconnect.ui.customUtils.VolleyStringRequest;
import com.wowconnect.ui.milestones.adapters.OptionsAdapter;
import com.wowconnect.ui.milestones.fragments.MilesAudioFragment;
import com.wowconnect.ui.milestones.fragments.MilesImageFragment;
import com.wowconnect.ui.milestones.fragments.MilesTextFragment;
import com.wowconnect.ui.milestones.fragments.MilesVideoFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;


public class MilesActivity extends AppCompatActivity implements
        MilesTextFragment.OnFragmentInteractionListener, MilesVideoFragment.OnFragmentInteractionListener,
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
    TextView textTitle;
    ArrayList<TMileData> mileDataArrayList;
    @BindView(R.id.toolbar_activity_miles)
    Toolbar toolbar;
    @BindView(R.id.layout_frame_miles)
    FrameLayout foregroundLayout;
    BottomSheetBehavior bottomSheetBehavior;
    OptionsAdapter optionsAdapter;
    int selected_option = -1;
    @BindView(R.id.button_thumbs_up)
    ImageView imageThumbsUp;
    @BindView(R.id.button_thumbs_down)
    ImageView imageThumbsDown;
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
                    imageThumbsUp.setActivated(true);
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
    @BindView(R.id.layout_collapse_in_mile)
    RelativeLayout layoutCollapse;
    @BindView(R.id.text_qus_mile_bottom_sheet)
    TextView textMileQuestion;
    @BindView(R.id.text_title_mile_bottom_sheet)
    TextView textMileTitleBottomSheet;
    @BindView(R.id.text_qus_feedback_bottom_sheet)
    TextView textFeedbackQuestion;
    TextView toolbarTitle, toolbarSubTitle;
    ImageButton backButton;
    ArrayList<String> options;
    boolean isThumbsUp = true;
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
    boolean isMile, isCompletable, isIntro;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        boolean isUp = false;


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
                    textFeedbackQuestion.setAlpha(0f);
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
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layoutCollapse.getLayoutParams();
            layoutParams.topMargin = Utils.getInstance().getPixelAsDp(getApplicationContext(), pixel);
            layoutCollapse.setLayoutParams(layoutParams);
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
                textFeedbackQuestion.setAlpha(slideOffset);


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
        imageThumbsDown.setActivated(false);
        imageThumbsUp.setActivated(false);
    }

    void loadOptions(boolean isUp) {
        options = new ArrayList<>();
        S2mConfiguration configuration = new NewDataParser().getS2mConfiguration();
        if (isUp) {
            options = configuration.getMileFeedbackUpOptions();
            textFeedbackQuestion.setText(isMile ?
                    configuration.getMileFeedbackUpQuestion()
                    : configuration.getTrainingFeedbackUpQuestion());
        } else {
            options = configuration.getMileFeedbackDownOptions();
            textFeedbackQuestion.setText(isMile ?
                    configuration.getMileFeedbackDownQuestion()
                    : configuration.getTrainingFeedbackDownQuestion());
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
        isMile = getIntent().getBooleanExtra("isMile", false);
        isIntro = getIntent().getBooleanExtra("isIntro", false);
        toolbarTitle.setText(isMile ? "Miles" : "Training");


        thisMileId = getIntent().getIntExtra(Constants.KEY_ID, -1);
        isCompletable = getIntent().getBooleanExtra("isCompletable", false);

        String title = holder.getCurrentClassName() + " " + holder.getCurrentSectionName();
        toolbarSubTitle.setText(title);

        foregroundLayout.getForeground().setAlpha(0);
        foregroundLayout.setVisibility(GONE);
        initTypeView();
        initBottomSheet();
        BottomSheetBehavior.from(bottomSheet)
                .setState(BottomSheetBehavior.STATE_HIDDEN);


        textTitle.setText(holder.getCurrentMileTitle());
        if (!isFragmentsAdded)
            addFragments();


        listOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != selected_option) {// optionsAdapter.notifyDataSetChanged();
                    for (int j = 0; j < adapterView.getChildCount(); j++) {
                        adapterView.getChildAt(j).setBackgroundDrawable(ContextCompat.getDrawable(MilesActivity.this, R.drawable.background_options));
                    }
                    if (isThumbsUp)
                        view.setBackgroundDrawable(ContextCompat.getDrawable(MilesActivity.this, R.drawable.bg_options_selected_green));
                    else {
                        view.setBackgroundDrawable(ContextCompat.getDrawable(MilesActivity.this, R.drawable.bg_options_selected_red));
                    }
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
                    completeThis();
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


    public void addFragmentOnlyOnce(FragmentManager fragmentManager, int containerId, Fragment fragment, String tag) {
        // Make sure the current transaction finishes first
        fragmentManager.executePendingTransactions();
        Log.d("addFragmentOnlyOnce", "addFragmentOnlyOnce: " + tag);
        // If there is no fragment yet with this tag...
        if (fragmentManager.findFragmentByTag(tag) == null) {
            // Add it
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(containerId, fragment, tag);
            transaction.commit();
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
                        if (NewDataHolder.getInstance(MilesActivity.this).getCurrentMileMcqs() != null &&
                                NewDataHolder.getInstance(MilesActivity.this).getCurrentMileMcqs().size() > 0) {
                            NewDataHolder.getInstance(MilesActivity.this).setCurrentMileId(thisMileId);
                            startActivityForResult(new Intent(MilesActivity.this, MCQActivity.class)
                                    .putExtra(Constants.KEY_TITLE, holder.getCurrentMileTitle())
                                    .putExtra(Constants.KEY_QUESTION,
                                            NewDataHolder.getInstance(MilesActivity.this).getCurrentMileMcqs()), REQUEST_CODE);
                        } else allowTrainingToComplete();
                    }
                });
            else
                buttonComplete.setVisibility(GONE);
        }

        // scrollView.setBackgroundDrawable(background);
        scrollView.setBackgroundDrawable(background);
        buttonComplete.setBackgroundColor(buttonBackgroundColor);
        buttonComplete.setTextColor(buttonTextColor);
        textTitle.setTextColor(titleTextColor);
    }


    void getMcqs() {
        VolleyStringRequest mcqsRequest = new VolleyStringRequest(Request.Method.GET,
                Constants.MILESTONES_URL + Constants.SEPERATOR + NewDataHolder.getInstance(this).getCurrentMilestoneId()
                        + Constants.TRAININGS_SUFFIX + Constants.SEPERATOR + thisMileId + Constants.MCQS_SUFFIX,
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
                allowTrainingToComplete();
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


    void allowTrainingToComplete() {
        Utils.getInstance().showToast(getString(R.string.er_no_mcq));
        buttonComplete.setOnClickListener(sheetShowListener);
        buttonComplete.performClick();
    }

    void loadQuestions(String mcqString) {
        ArrayList<MCQs> mcqsArrayList = new ArrayList<>();
        try {
            JSONObject mcqsObject = new JSONObject(mcqString);
            JSONArray mcqsJsonArray = mcqsObject.getJSONArray(Constants.KEY_MCQS);
            if (mcqsJsonArray != null && mcqsJsonArray.length() > 0) {
                for (int i = 0; i < mcqsJsonArray.length(); i++) {
                    JSONObject mcqsJson = mcqsJsonArray.getJSONObject(i);
                    MCQs mcq = new MCQs();
                    mcq.setId(mcqsJson.getInt(Constants.KEY_ID));
                    mcq.setAnswer(mcqsJson.getString(Constants.KEY_ANSWER));
                    mcq.setQuestion(mcqsJson.getString(Constants.KEY_QUESTION));
                    ArrayList<McqOptions> optionsList = new ArrayList<>();

                    JSONArray optionsArray = new JSONArray(mcqsJson.getString(Constants.KEY_OPTIONS));
                    for (int ii = 0; ii < optionsArray.length(); ii++) {
                        String optionText = optionsArray.getString(ii);
                        McqOptions options = new McqOptions();
                        options.setText(optionText);
                        optionsList.add(options);
                    }

                    mcq.setOptions(optionsList);
                    mcqsArrayList.add(i, mcq);
                }
            }


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
                    imageThumbsUp.setActivated(true);
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

        imageThumbsUp.setOnClickListener(thumbsClickListener);
        imageThumbsDown.setOnClickListener(thumbsClickListener);

        textMileTitleBottomSheet.setText(holder.getCurrentMileTitle());
        textMileQuestion.setText(isMile ?
                new NewDataParser().getS2mConfiguration().getMileQuestion()
                : new NewDataParser().getS2mConfiguration().getTrainingQuestion());
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
            String fragmentTag = "fragment";
            TMileData mileData = mileDataArrayList.get(i);
            String type = mileData.getType();
            switch (type) {
                case Constants.TYPE_TEXT:
                    fragment = MilesTextFragment.newInstance(mileData.getTitle(),
                            mileData.getBody());
                    break;
                case Constants.TYPE_VIDEO:

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
                case Constants.TYPE_AUDIO:
                    ArrayList<AudioMiles> audioMilesList = new ArrayList<>();
                    for (int j = 0; j < mileData.getUrlsList().size(); j++) {
                        String audioUrl = mileData.getUrlsList().get(j);
                        String imageUrl = mileData.getImagesList().get(j);
                        audioMilesList.add(new AudioMiles(j, j, imageUrl, audioUrl));
                    }
                    fragment = MilesAudioFragment.newInstance(mileData.getTitle(), audioMilesList);
                    break;
                case Constants.TYPE_IMAGE:
                    ArrayList<ImageMiles> imageMilesList = new ArrayList<>();
                    for (int j = 0; j < mileData.getUrlsList().size(); j++) {
                        imageMilesList.add(new ImageMiles(j, j, mileData.getTitle(), mileData.getUrlsList().get(j)));
                    }
                    fragment = MilesImageFragment.newInstance(mileData.getTitle(), imageMilesList);
                    break;

            }

            if (fragment != null)
                addFragmentOnlyOnce(getSupportFragmentManager(),
                        milesFragmentContainer.getId(),
                        fragment,
                        "fragment" + i);
        }


    }

    @Override
    protected void onDestroy() {
        if (completeContentRequest != null) {
            completeContentRequest.removeStatusListener();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        else

            super.onBackPressed();
    }

    VolleyStringRequest completeContentRequest;

    void completeThis() {
        String mileTrainingCompleteUrl = Constants.SCHOOLS_URL + SharedPreferenceHelper.getSchoolId() + Constants.SEPERATOR +
                Constants.KEY_SECTIONS + Constants.SEPERATOR + NewDataHolder.getInstance(this).getCurrentSectionId()
                + Constants.SEPERATOR + Constants.KEY_CONTENT + Constants.SEPERATOR
                + NewDataHolder.getInstance(this).getCurrentMileId() + Constants.SEPERATOR + Constants.KEY_COMPLETE;
        String introTrainingCompleteUrl = Constants.INTRO_TRAININGS_URL + Constants.SEPERATOR + NewDataHolder.getInstance(this).getCurrentMileId()
                + Constants.SEPERATOR + Constants.KEY_COMPLETE;
        String completeUrl = isIntro ? introTrainingCompleteUrl : mileTrainingCompleteUrl;
        completeContentRequest = new VolleyStringRequest(Request.Method.POST, completeUrl
                ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("completeContentRequest", "onResponse: " + response);
                        Utils.getInstance().showToast("Submitted successfully");
                        if (isIntro)
                            new NetworkHelper(MilesActivity.this).getMilestoneContent(
                                    NewDataHolder.getInstance(MilesActivity.this).getCurrentSectionId(),
                                    new NetworkHelper.NetworkListener() {
                                        @Override
                                        public void onFinish() {
                                            finish();
                                        }
                                    });
                        else
                            new NetworkHelper(MilesActivity.this).getIntroTrainings(
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
                        Log.d("completeContentRequest", "onErrorResponse: " + error);

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
                    thumbs = Constants.KEY_UP;
                else
                    thumbs = Constants.KEY_DOWN;
                params.put(Constants.KEY_THUMBS, thumbs);
                params.put(Constants.KEY_REASON, options.get(selected_option));
                if (!isMile && NewDataHolder.getInstance(MilesActivity.this).getResultsMap() != null)
                    params.putAll(NewDataHolder.getInstance(MilesActivity.this).getResultsMap());

                for (Map.Entry<String, String> entry : params.entrySet()) {
                    System.out.println(entry.getKey() + " ::: " + entry.getValue());
                }
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(completeContentRequest);
    }


}

package com.wowconnect.ui.milestones;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wowconnect.NewDataHolder;
import com.wowconnect.SharedPreferenceHelper;
import com.wowconnect.domain.Constants;
import com.wowconnect.domain.network.VolleySingleton;
import com.wowconnect.ui.customUtils.VolleyStringRequest;
import com.wowconnect.R;
import com.wowconnect.ui.milestones.adapters.MCQAnswersAdapter;
import com.wowconnect.models.mcq.MCQs;
import com.wowconnect.models.mcq.McqOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;



public class MCQActivity extends AppCompatActivity {
    @BindView(R.id.toolbar_activity_mcq)
    Toolbar toolbar;
    @BindView(R.id.list_answers)
    ListView listView;
    @BindView(R.id.text_question)
    TextView textQuestion;
    @BindView(R.id.button_submit)
    Button buttonSubmit;
    int currentQuestion;
    ArrayList<MCQs> mcqsArrayList;
    int selected_option = -1;
    MCQAnswersAdapter adapter;
    ArrayList<McqOptions> currentOptions;
    Map<String, String> choicesResult = new ArrayMap<>();
    int selectedOption;
    TextView toolbarTitle, toolbarSubTitle;
    ImageButton backButton;
    VolleyStringRequest choiceResultRequest;

    private final String NEXT = "Next", SUBMIT = "Submit", FINISH = "Finish";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcq);
        ButterKnife.bind(this);

        toolbarTitle = (TextView) toolbar.findViewById(R.id.text_title_toolbar);
        toolbarSubTitle = (TextView) toolbar.findViewById(R.id.text_subtitle_toolbar);
        backButton = (ImageButton) toolbar.findViewById(R.id.button_back_toolbar);
        // toolbar.setTitle("Quiz");
        setSupportActionBar(toolbar);
        if (getIntent() != null) {
            mcqsArrayList = (ArrayList<MCQs>) getIntent().getSerializableExtra(Constants.KEY_QUESTION);
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbarTitle.setText("Quiz");
        //toolbarSubTitle.setText("Training Name");

        loadQuestions();
        if (getIntent() != null) {
            toolbarSubTitle.setText(getIntent().getStringExtra(Constants.KEY_TITLE));
        } else
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        setListViewState(true);
    }

    void loadQuestions() {

        Log.d("MCQS", "loadQuestions: " + mcqsArrayList.toString());
        if (mcqsArrayList.size() > 0) {
            showQuestions(0);
        } /*else {
            //setContentView(R.layout.layout_error);
            setContentView(Utils.getInstance().getErrorView(this, getResources().getDrawable(R.drawable.no_sections), getString(R.string.er_no_mcq)));
            // Utils.getInstance().showToast();
        }*/
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String buttonText = buttonSubmit.getText().toString();
                if (buttonText.equals(NEXT))
                    if (currentQuestion < mcqsArrayList.size() - 1)
                        showQuestions(currentQuestion + 1);
                    else {
                        buttonSubmit.setText(FINISH);
                        selected_option = -1;
                    }
                else if (buttonText.equals(SUBMIT)) {
                    if (selected_option != -1) {
                        if (currentQuestion < mcqsArrayList.size() - 1)
                            buttonSubmit.setText(NEXT);
                        else
                            buttonSubmit.setText(FINISH);
                        checkResult(selected_option);
                        setListViewState(false);
                        //storeResult();
                        storeInResult();
                    } else
                        showToast("Please select one option to submit");
                } else if (buttonText.equals(FINISH)) {
                    // sendResults();
                    //finish();
                    sendResultsBack();
                }
            }
        });
    }

    void sendResultsBack() {

        Map<String, String> resultsMap = new HashMap<>();
        resultsMap.put(Constants.KEY_CHOICES, resultArray.toString());
        resultsMap.put(Constants.KEY_RESULT, String.valueOf(getPercentage()));
        NewDataHolder.getInstance(this).setResultsMap(resultsMap);

        Intent returnIntent = new Intent();
        returnIntent.putExtra("isSuccess", true);
        setResult(Activity.RESULT_OK, returnIntent);

        finish();
    }

    int getPercentage() {
        return (int) (((float) correctCount / (float) mcqsArrayList.size()) * 100);
    }

    Map<String, String> mcqResult;

    void storeResult() {
        choicesResult.put(String.valueOf(mcqsArrayList.get(currentQuestion).getId()), currentOptions.get(selectedOption).getText());
    }

    @Override
    protected void onDestroy() {
        if (choiceResultRequest != null) {
            choiceResultRequest.removeStatusListener();
        }
        super.onDestroy();
    }


    JSONArray resultArray = new JSONArray();

    void storeInResult() {
        try {

            JSONObject mcqResult = new JSONObject();
            mcqResult.put(Constants.KEY_QUESTION, mcqsArrayList.get(currentQuestion).getQuestion());
            JSONArray optionsArray = new JSONArray();
            ArrayList<McqOptions> options = mcqsArrayList.get(currentQuestion).getOptions();

            for (int i = 0; i < options.size(); i++) {
                optionsArray.put(options.get(i).getText());
            }

            mcqResult.put(Constants.KEY_OPTIONS, optionsArray);
            mcqResult.put(Constants.KEY_ANSWER, currentOptions.get(selectedOption).getText());
            resultArray.put(mcqResult);
            Log.d("storeInResult", resultArray.toString());

        } catch (Exception e) {
            Log.d("storeInResult", "Error: " + e);
        }

    }

    void sendResults() {
        final JSONArray array = new JSONArray();
        try {

            for (Map.Entry<String, String> entry : choicesResult.entrySet()) {   //print keys and values
                System.out.println(entry.getKey() + " : " + entry.getValue());
                JSONObject obj = new JSONObject();
                obj.put(entry.getKey(), entry.getValue());
                array.put(obj.toString());
            }
            System.out.println(array.toString());
        } catch (Exception e) {
            Log.d("sendResults", "sendResults: " + e);
        }


        choiceResultRequest = new VolleyStringRequest(Request.Method.POST,
                Constants.SCHOOLS_URL + SharedPreferenceHelper.getSchoolId() + Constants.SEPERATOR +
                        Constants.KEY_SECTIONS + Constants.SEPERATOR + NewDataHolder.getInstance(this).getCurrentSectionId() + Constants.SEPERATOR +
                        Constants.KEY_CONTENT + NewDataHolder.getInstance(this).getCurrentMileId() + Constants.SEPERATOR + Constants.KEY_COMPLETE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("sendResults", "onResponse: " + response);

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
                params.put("choices", array.toString());
                params.put("result", "70");

                for (Map.Entry<String, String> entry : params.entrySet()) {
                    System.out.println(entry.getKey() + " : " + entry.getValue());
                }
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(choiceResultRequest);
    }

    void setListViewState(boolean state) {
        listView.setEnabled(state);
    }

    int correctCount;

    void checkResult(int selected) {
        selectedOption = selected;
        resetOptionStates(currentOptions);
        if (mcqsArrayList.get(currentQuestion).getAnswer().equals(currentOptions.get(selected).getText())) {
            currentOptions.get(selected).setRight(true);
            correctCount++;
        } else {
            currentOptions.get(selected).setWrong(true);
            currentOptions.get(getCorrectOptionPosition()).setRight(true);
        }
        adapter.notifyDataSetChanged();
    }

    int getCorrectOptionPosition() {
        for (int i = 0; i < currentOptions.size(); i++) {
            if (mcqsArrayList.get(currentQuestion).getAnswer().equals(currentOptions.get(i).getText())) {
                return i;
            }
        }
        return 0;
    }

    void showToast(String msg) {
        Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT).show();
    }

    void showQuestions(int position) {
        buttonSubmit.setText(SUBMIT);
        selected_option = -1;
        setListViewState(true);
        textQuestion.setText(mcqsArrayList.get(position).getQuestion());
        currentOptions = mcqsArrayList.get(position).getOptions();
        adapter = new MCQAnswersAdapter(this, currentOptions);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onClickRow(i);
            }
        });
        currentQuestion = position;
    }

    public void onClickRow(int position) {
        resetOptionStates(currentOptions);
        currentOptions.get(position).setSelected(true);
        adapter.notifyDataSetChanged();
        selected_option = position;
    }

    void resetOptionStates(ArrayList<McqOptions> options) {
        for (int i = 0; i < options.size(); i++) {
            McqOptions ops = options.get(i);
            ops.setNotSelected(true);
            ops.setSelected(false);
            ops.setRight(false);
            ops.setWrong(false);
        }
    }


}

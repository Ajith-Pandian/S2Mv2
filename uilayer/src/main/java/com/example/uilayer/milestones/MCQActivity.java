package com.example.uilayer.milestones;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.network.VolleySingleton;
import com.example.domainlayer.temp.DataParser;
import com.example.domainlayer.utils.VolleyStringRequest;
import com.example.uilayer.DataHolder;
import com.example.uilayer.R;
import com.example.uilayer.S2MApplication;
import com.example.uilayer.milestones.adapters.MCQAnswersAdapter;
import com.example.uilayer.models.MCQs;
import com.example.uilayer.models.McqOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.domainlayer.Constants.KEY_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.KEY_ANSWER;
import static com.example.domainlayer.Constants.KEY_ID;
import static com.example.domainlayer.Constants.KEY_OPTIONS;
import static com.example.domainlayer.Constants.KEY_QUESTION;
import static com.example.domainlayer.Constants.KEY_SCHOOL_ID;
import static com.example.domainlayer.Constants.MCQ_RESULT_URL;
import static com.example.domainlayer.Constants.MCQ_URL;
import static com.example.domainlayer.Constants.MILES_TRAININGS_URL;
import static com.example.domainlayer.Constants.TEMP_ACCESS_TOKEN;

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

        toolbarTitle.setText("Quiz");
        toolbarSubTitle.setText("Training Name");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getMcqs();
        setListViewState(false);

    }

    void loadQuestions(String mcqString) {
        mcqsArrayList = new ArrayList<>();
        try {
            JSONArray mcqsJsonArray = new JSONArray(mcqString);
            for (int i = 0; i < mcqsJsonArray.length(); i++) {
                JSONObject mcqsJson = mcqsJsonArray.getJSONObject(i);
                MCQs mcq = new MCQs();
                mcq.setId(mcqsJson.getInt(KEY_ID));
                mcq.setAnswer(mcqsJson.getString(KEY_ANSWER));
                mcq.setQuestion(mcqsJson.getString(KEY_QUESTION));

                JSONObject jObject = new JSONObject(mcqsJson.getString(KEY_OPTIONS));
                Iterator<?> keys = jObject.keys();
                ArrayList<McqOptions> optionsList = new ArrayList<>();
                while (keys.hasNext()) {
                    McqOptions options = new McqOptions();
                    String key = (String) keys.next();
                    String value = jObject.getString(key);
                    options.setLabel(key);
                    options.setText(value);
                    optionsList.add(options);
                }
                mcq.setOptions(optionsList);
                mcqsArrayList.add(i, mcq);
            }

        } catch (JSONException ex) {
            Log.e("MCQ", "loadQuestions: ", ex);
        }
        Log.d("MCQS", "loadQuestions: " + mcqsArrayList.toString());
        showQuestions(0);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonSubmit.getText().equals("Next"))
                    if (currentQuestion < mcqsArrayList.size() - 1)
                        showQuestions(currentQuestion + 1);
                    else {
                        buttonSubmit.setText("FINISH");
                        //get feed back and go to next
                        sendResults();
                        //finish();
                    }
                else {
                    if (selected_option != -1) {
                        buttonSubmit.setText("Next");
                        checkResult(selected_option);
                        setListViewState(false);

                    } else
                        showToast("Please select one option to submit");
                }
                storeResult();

            }
        });
    }

    void storeResult() {
        choicesResult.put(String.valueOf(mcqsArrayList.get(currentQuestion).getId()), currentOptions.get(selectedOption).getLabel());
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
        VolleyStringRequest choiceResultRequest = new VolleyStringRequest(Request.Method.POST, MCQ_RESULT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("sendResults", "onResponse: " + response);
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("isSuccess",true);
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
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

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new ArrayMap<>();
                header.put(KEY_ACCESS_TOKEN, TEMP_ACCESS_TOKEN);
                return header;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

        };

        VolleySingleton.getInstance(this).addToRequestQueue(choiceResultRequest);
    }

    void setListViewState(boolean state) {
        listView.setEnabled(state);
    }

    void checkResult(int selected) {
        selectedOption = selected;
        resetOptionStates(currentOptions);
        if (mcqsArrayList.get(currentQuestion).getAnswer().equals(currentOptions.get(selected).getLabel())) {
            currentOptions.get(selected).setRight(true);
        } else {
            currentOptions.get(selected).setWrong(true);
            currentOptions.get(getCorrectOptionPosition()).setRight(true);
        }
        adapter.notifyDataSetChanged();
    }

    int getCorrectOptionPosition() {
        for (int i = 0; i < currentOptions.size(); i++) {
            if (mcqsArrayList.get(currentQuestion).getAnswer().equals(currentOptions.get(i).getLabel())) {
                return i;
            }
        }
        return 0;
    }

    void showToast(String msg) {
        Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT).show();
    }

    void showQuestions(int position) {
        buttonSubmit.setText("Submit");
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

    void getMcqs() {
        VolleyStringRequest milesRequest = new VolleyStringRequest(Request.Method.GET, MCQ_URL,
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
                        Log.d("MilesDetails", "onErrorResponse: " + error);

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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new ArrayMap<>();
                header.put(KEY_ACCESS_TOKEN, TEMP_ACCESS_TOKEN);
                return header;
            }

        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(milesRequest);
    }

}

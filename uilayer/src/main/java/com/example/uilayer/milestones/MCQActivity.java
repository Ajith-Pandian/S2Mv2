package com.example.uilayer.milestones;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ExpandedMenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.network.VolleySingleton;
import com.example.domainlayer.temp.DataParser;
import com.example.domainlayer.utils.VolleyStringRequest;
import com.example.uilayer.DataHolder;
import com.example.uilayer.R;
import com.example.uilayer.milestones.adapters.MCQAnswersAdapter;
import com.example.uilayer.models.MCQs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.domainlayer.Constants.KEY_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.KEY_ANSWER;
import static com.example.domainlayer.Constants.KEY_ID;
import static com.example.domainlayer.Constants.KEY_OPTIONS;
import static com.example.domainlayer.Constants.KEY_QUESTION;
import static com.example.domainlayer.Constants.KEY_SCHOOL_ID;
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
    int currentPosition;
    ArrayList<MCQs> mcqsArrayList;

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
        getMcqs();
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

                Map<String, String> map = new HashMap<>();
                Map<String, String> treemap = new TreeMap<>(map);
                JSONObject jObject = new JSONObject(mcqsJson.getString(KEY_OPTIONS));
                Iterator<?> keys = jObject.keys();

                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    String value = jObject.getString(key);
                    treemap.put(key, value);
                }
                mcq.setOptions(treemap);
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
                if (currentPosition < mcqsArrayList.size() - 1)
                    showQuestions(currentPosition + 1);
                else
                { buttonSubmit.setText("FINISH");}
            }
        });
    }

    void showQuestions(int position) {
        textQuestion.setText(mcqsArrayList.get(position).getQuestion());
        MCQAnswersAdapter adapter = new MCQAnswersAdapter(getApplicationContext(), mcqsArrayList.get(position).getOptions());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //  adapter.setStates(0);
            }
        });
        currentPosition = position;
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

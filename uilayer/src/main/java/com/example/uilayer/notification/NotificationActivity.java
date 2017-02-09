package com.example.uilayer.notification;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.models.Notification;
import com.example.domainlayer.models.milestones.TMiles;
import com.example.domainlayer.network.VolleySingleton;
import com.example.uilayer.NewDataParser;
import com.example.uilayer.R;
import com.example.uilayer.SharedPreferenceHelper;
import com.example.uilayer.customUtils.Utils;
import com.example.uilayer.customUtils.VolleyStringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.domainlayer.Constants.BASE_URL;
import static com.example.domainlayer.Constants.INTRO_TRAININGS_URL;
import static com.example.domainlayer.Constants.KEY_CREATE;
import static com.example.domainlayer.Constants.KEY_CREATED_AT;
import static com.example.domainlayer.Constants.KEY_DATA;
import static com.example.domainlayer.Constants.KEY_HAS_READ;
import static com.example.domainlayer.Constants.KEY_ID;
import static com.example.domainlayer.Constants.KEY_INTRO_TRAININGS;
import static com.example.domainlayer.Constants.KEY_NOTIFICATIONS;
import static com.example.domainlayer.Constants.KEY_NUM_OF_STUDS;
import static com.example.domainlayer.Constants.KEY_TYPE;
import static com.example.domainlayer.Constants.KEY_USERS;
import static com.example.domainlayer.Constants.SEPERATOR;
import static com.example.domainlayer.Constants.USER_URL;

public class NotificationActivity extends AppCompatActivity {
    @BindView(R.id.notification_recycler)
    RecyclerView notificationRecycler;

    ArrayList<Notification> notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        notificationRecycler.setLayoutManager(layoutManager);
        notificationRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        getAllNotifications();
    }


    ArrayList<Notification> getAllNotifications() {
        {
            notifications = new ArrayList<>();

            VolleyStringRequest notificationsRequest = new VolleyStringRequest(Request.Method.GET,
                    //BASE_URL + KEY_USERS + SEPERATOR + SharedPreferenceHelper.getUserId() + SEPERATOR + KEY_NOTIFICATIONS,
                    USER_URL + KEY_NOTIFICATIONS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("notificationsRequest", "onResponse: " + response);

                            try {
                                JSONObject notificationResponse = new JSONObject(response);
                                JSONArray notificationsArray = notificationResponse.getJSONArray(KEY_NOTIFICATIONS);

                                for (int i = 0; i < notificationsArray.length(); i++) {
                                    JSONObject notificationJson = notificationsArray.getJSONObject(i);
                                    Notification notification = new Notification();
                                    notification.setId(notificationJson.getInt(KEY_ID));
                                    notification.setData(notificationJson.getString(KEY_DATA));
                                    notification.setType(notificationJson.getString(KEY_TYPE));
                                    if (!notificationJson.isNull(KEY_HAS_READ))
                                        notification.setRead(notificationJson.getBoolean(KEY_HAS_READ));
                                    notification.setDate(notificationJson.getString(KEY_CREATED_AT));
                                    notifications.add(notification);
                                }
                                notificationRecycler.setAdapter(new NotificationAdapter(NotificationActivity.this, notifications));

                            } catch (JSONException ex) {
                                Log.e("notificationsRequest", "onResponse: ", ex);
                            }
                        }
                    },
                    new VolleyStringRequest.VolleyErrListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            super.onErrorResponse(error);
                            Log.d("notificationsRequest", "onErrorResponse: " + error);

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
                    Utils.getInstance().showToast(getResources().getString(R.string.er_no_intro_trainings));
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

            VolleySingleton.getInstance(this).addToRequestQueue(notificationsRequest);

            return notifications;
        }
    }
}

package com.wowconnect.ui.notification;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wowconnect.R;
import com.wowconnect.domain.network.VolleySingleton;
import com.wowconnect.models.Notification;
import com.wowconnect.ui.customUtils.Utils;
import com.wowconnect.ui.customUtils.VolleyStringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.wowconnect.domain.Constants.KEY_CREATED_AT;
import static com.wowconnect.domain.Constants.KEY_HAS_READ;
import static com.wowconnect.domain.Constants.KEY_ICON;
import static com.wowconnect.domain.Constants.KEY_ID;
import static com.wowconnect.domain.Constants.KEY_MESSAGE;
import static com.wowconnect.domain.Constants.KEY_NOTIFICATIONS;
import static com.wowconnect.domain.Constants.KEY_TITLE;
import static com.wowconnect.domain.Constants.KEY_TYPE;
import static com.wowconnect.domain.Constants.USER_URL;

public class NotificationActivity extends AppCompatActivity {
    @BindView(R.id.notification_recycler)
    RecyclerView notificationRecycler;
    @BindView(R.id.layout_no_data)
    RelativeLayout noDataLayout;

    ArrayList<Notification> notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        notificationRecycler.setLayoutManager(layoutManager);
        notificationRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        getAllNotifications();

    }

    VolleyStringRequest notificationsRequest;

    ArrayList<Notification> getAllNotifications() {

        notifications = new ArrayList<>();

        notificationsRequest = new VolleyStringRequest(Request.Method.GET,
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
                                notification.setTitle(notificationJson.getString(KEY_TITLE));
                                notification.setData(notificationJson.getString(KEY_MESSAGE));
                                notification.setType(notificationJson.getString(KEY_TYPE));
                                if (!notificationJson.isNull(KEY_ICON))
                                    notification.setIcon(notificationJson.getString(KEY_ICON));
                                if (!notificationJson.isNull(KEY_HAS_READ))
                                    notification.setRead(notificationJson.getBoolean(KEY_HAS_READ));
                                notification.setDate(notificationJson.getString(KEY_CREATED_AT));
                                notifications.add(notification);
                            }
                            if (notifications.size() > 0) {
                                noDataLayout.setVisibility(View.GONE);
                                notificationRecycler.setVisibility(View.VISIBLE);
                                notificationRecycler.setAdapter(new NotificationAdapter
                                        (NotificationActivity.this, notifications));
                            } else {
                                notificationRecycler.setVisibility(View.GONE);
                                noDataLayout.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException ex) {
                            Log.e("notificationsRequest", "onResponse: ", ex);
                        }
                    }
                }

                ,
                new VolleyStringRequest.VolleyErrListener()

                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("notificationsRequest", "onErrorResponse: " + error);

                    }
                }

                , new VolleyStringRequest.StatusCodeListener()

        {
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
                Utils.getInstance().showToast("No Notifications");
            }

            @Override
            public void onConflict() {
                Log.d(TAG, "onConflict: ");
            }

            @Override
            public void onTimeout() {
                Log.d(TAG, "onTimeout: ");
            }
        }

        );

        VolleySingleton.getInstance(this).

                addToRequestQueue(notificationsRequest);

        return notifications;
    }

    @Override
    protected void onDestroy() {
        if (notificationsRequest != null) {
            notificationsRequest.removeStatusListener();
        }
        super.onDestroy();
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

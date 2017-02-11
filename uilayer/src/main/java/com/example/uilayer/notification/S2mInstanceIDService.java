package com.example.uilayer.notification;

import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.Constants;
import com.example.domainlayer.network.VolleySingleton;
import com.example.uilayer.NetworkHelper;
import com.example.uilayer.NewDataHolder;
import com.example.uilayer.S2MApplication;
import com.example.uilayer.SharedPreferenceHelper;
import com.example.uilayer.customUtils.VolleyStringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.Map;

/**
 * Created by Ajit on 26-01-2017.
 */

public class S2mInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Token: " + FirebaseInstanceId.getInstance().getToken());
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        if (SharedPreferenceHelper.isLoggedIn())
            new NetworkHelper(this).sendFirebaseTokenToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
}
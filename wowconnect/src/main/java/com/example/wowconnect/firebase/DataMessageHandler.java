package com.example.wowconnect.firebase;

import android.content.Context;
import android.util.Log;

import com.example.wowconnect.NetworkHelper;
import com.example.wowconnect.domain.Constants;

import java.util.ArrayList;

/**
 * Created by Ajit on 12-02-2017.
 */

public class DataMessageHandler {
    private final String TAG = "DataMessageHandler";
    private ArrayList<String> networkTypes, sectionTypes, activityTypes;

    public DataMessageHandler() {
        prepareDataMessageTypes();
    }

    private void prepareDataMessageTypes() {
        networkTypes = new ArrayList<>();
        sectionTypes = new ArrayList<>();
        activityTypes = new ArrayList<>();

        networkTypes.add(Constants.FB_DATA_TYPE_USER_JOINED_SCHOOL);//1
        networkTypes.add(Constants.FB_DATA_TYPE_USER_UPDATED);
        networkTypes.add(Constants.FB_DATA_TYPE_USER_LEFT_SCHOOL);
        networkTypes.add(Constants.FB_DATA_WOW_GIVEN);

        sectionTypes.add(Constants.FB_DATA_SECTION_ADDED);
        sectionTypes.add(Constants.FB_DATA_SECTION_DELETED);
        sectionTypes.add(Constants.FB_DATA_SECTION_ASSIGNED);//2

        activityTypes.add(Constants.FB_DATA_BULLETIN_BOARD);
        activityTypes.add(Constants.FB_DATA_MILES_COMPLETED);
        activityTypes.add(Constants.FB_DATA_INTRO_TRAININGS_COMPLETED);
        activityTypes.add(Constants.FB_DATA_TRAININGS_COMPLETED);
        activityTypes.add(Constants.FB_DATA_GOT_WOW);
        activityTypes.add(Constants.FB_DATA_BIRTHDAY);
        activityTypes.add(Constants.FB_DATA_ANNIVERSARY);
        activityTypes.add(Constants.FB_DATA_SECTION_ASSIGNED);//2
        activityTypes.add(Constants.FB_DATA_TYPE_USER_JOINED_SCHOOL);//1

    }

    public void validateAndUpdateDb(Context context, String dataMessageType) {
        if (networkTypes.contains(dataMessageType))
            updateNetworkDb(context);
        if (activityTypes.contains(dataMessageType))
            updateActivitiesDb(context);
        if(sectionTypes.contains(dataMessageType))
            updateSectionsDb(context);

    }

    private void updateNetworkDb(Context context) {
        NetworkHelper networkHelper = new NetworkHelper(context);
        networkHelper.getNetworkUsers(new NetworkHelper.NetworkListener() {
            @Override
            public void onFinish() {
                Log.d(TAG, "updateNetworkDb: onFinish: updated");
            }
        });
    }

    private void updateActivitiesDb(Context context) {
        NetworkHelper networkHelper = new NetworkHelper(context);
        networkHelper.getDashBoardDetails(new NetworkHelper.NetworkListener() {
            @Override
            public void onFinish() {
                Log.d(TAG, "updateActivitiesDb: onFinish: updated");
            }
        });
    }
    private void updateSectionsDb(Context context) {
        NetworkHelper networkHelper = new NetworkHelper(context);
        networkHelper.getUserSections(new NetworkHelper.NetworkListener() {
            @Override
            public void onFinish() {
                Log.d(TAG, "updateSectionsDb: onFinish: updated");
            }
        });
    }
}

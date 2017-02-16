package com.wowconnect.ui.helpers;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;

/**
 * Created by Ajit on 25-01-2017.
 */

public abstract class S2MDialogFragment extends DialogFragment {
    private FragmentActivity activity;
    private CharSequence alertMessage;

    public void show() {
        this.show(activity.getSupportFragmentManager(), null);
    }

    public void setAlertMessage(CharSequence message) {
        this.alertMessage = message;
    }
    protected CharSequence getAlertMessage() {
        return alertMessage;
    }

    protected void setAlertActivity(FragmentActivity activity) {
        this.activity = activity;
    }

    protected FragmentManager getSupportFragmentManager() {
        return this.activity.getSupportFragmentManager();
    }
}


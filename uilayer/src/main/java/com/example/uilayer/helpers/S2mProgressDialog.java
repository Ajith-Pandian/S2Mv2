package com.example.uilayer.helpers;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.example.uilayer.R;

/**
 * Created by Ajit on 25-01-2017.
 */

public class S2mProgressDialog extends S2MDialogFragment {
    private static boolean isCancelableDialog;
    public static S2mProgressDialog build(FragmentActivity activity,
                                          String message, boolean isCancelable) {
        S2mProgressDialog dialogProgress = new S2mProgressDialog();
        dialogProgress.setAlertMessage("<font color=\'" + activity
                .getResources().getColor(R.color.colorPrimaryDark)
                + "\'>" + message + "</font>");
        dialogProgress.setAlertActivity(activity);
        isCancelableDialog = isCancelable;
        return dialogProgress;
    }

    // S2mProgressDialog.build(activity).show();
    public static S2mProgressDialog build(FragmentActivity activity, boolean isCancelable) {
        return build(activity, "Please wait...", isCancelable);
    }
    public static S2mProgressDialog build(FragmentActivity activity) {
        return build(activity, false);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getAlertMessage());
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(isCancelableDialog);
        //if(!isCancelableDialog)
        //disableBackButton(dialog);
        return dialog;
    }

    // Disable the back button
    private void disableBackButton(ProgressDialog dialog) {
        DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }

        };
        dialog.setOnKeyListener(keyListener);
    }
}
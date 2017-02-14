package com.wowconnect.ui.helpers;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.Html;

import com.wowconnect.R;

/**
 * Created by Ajit on 25-01-2017.
 */

public class S2mAlertDialog extends S2MDialogFragment {
    AlertDialogListener listener;
    String positiveText, negativeText;
    int alertIcon;
    private static boolean isCancelableDialog;

    // S2mAlertDialog.build(activity, "Do you want to delete that?",
    //  "OK", "Cancel", ...).show();
    public static S2mAlertDialog build(FragmentActivity activity, String alertMessage,
                                       String positiveText, String negativeText,
                                       boolean isCancelable,
                                       AlertDialogListener listener) {
        S2mAlertDialog dialogAlert = new S2mAlertDialog();
        dialogAlert.setAlertMessage(
                Html.fromHtml(
                        "<font color=\'" + activity
                                .getResources()
                                .getColor(R.color.colorPrimaryDark)
                                + "\'>" + alertMessage + "</font>"));
        dialogAlert.setAlertIcon(R.mipmap.ic_launcher);
        dialogAlert.setAlertListener(listener);
        dialogAlert.setAlertActivity(activity);
        dialogAlert.setAlertButtonText(positiveText, negativeText);
        dialogAlert.setCancelable(isCancelable);
        return dialogAlert;
    }

    // S2mAlertDialog.build(manager, "Do you want to delete that?");
    public static S2mAlertDialog build(FragmentActivity activity, String body,
                                       AlertDialogListener listener) {
        return build(activity, body, "OK", "Cancel", false, listener);
    }

    public static S2mAlertDialog build(FragmentActivity activity, String body, boolean isCancelable,
                                       AlertDialogListener listener) {
        return build(activity, body, "OK", "Cancel", isCancelable, listener);
    }

    public void setAlertIcon(int drawable) {
        this.alertIcon = drawable;
    }

    public void setAlertButtonText(String positiveText, String negativeText) {
        this.positiveText = positiveText;
        this.negativeText = negativeText;
    }

    protected void setAlertListener(AlertDialogListener listener) {
        this.listener = listener;
    }

    void removeAlertListener() {
        if (listener != null)
            listener = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity())
                        .setIcon(alertIcon)
                        .setTitle(getAlertMessage());
        if (positiveText != null) {
            builder.setPositiveButton(positiveText,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            listener.onPositive();
                        }
                    });
        }

        if (negativeText != null) {
            builder.setNegativeButton(negativeText,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            listener.onNegative();
                        }
                    });
        }
        builder.setCancelable(isCancelableDialog);

        return builder.create();
    }
}

package com.wowconnect.ui.helpers;

import android.support.v4.app.FragmentActivity;

/**
 * Created by Ajit on 25-01-2017.
 */

public class DialogHelper {
    static S2mAlertDialog currentDialog;

    public static void createAlertDialog(FragmentActivity context,
                                         String message,
                                         String positiveBtnTxt,
                                         String negativeeBtnTxt,
                                         AlertDialogListener alertListener) {
        currentDialog = S2mAlertDialog
                .build(context, message, positiveBtnTxt, negativeeBtnTxt, false, alertListener);
        currentDialog.show();
    }

    public static void createCancelableAlertDialog(FragmentActivity context,
                                                   String message,
                                                   AlertDialogListener alertListener) {
        currentDialog = S2mAlertDialog
                .build(context, message, true, alertListener);
        currentDialog.show();
    }

    public static void createProgressDialog(FragmentActivity context) {
        S2mProgressDialog
                .build(context).show();

    }

    public static void setCurrentDialog(S2mAlertDialog currentDialog) {
        DialogHelper.currentDialog = currentDialog;
    }

    public static S2mAlertDialog getCurrentDialog() {
        return currentDialog;
    }

    public static void removeAlertListener() {
        if (currentDialog != null) {
            currentDialog.removeAlertListener();
        }
    }

}

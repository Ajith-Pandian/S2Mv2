package com.wowconnect.ui.helpers;

import android.support.v4.app.FragmentActivity;

/**
 * Created by Ajit on 25-01-2017.
 */

public class DialogHelper {
    static S2MDialogFragment currentDialog;

    public static void createAlertDialog(FragmentActivity context,
                                         String message,
                                         String positiveBtnTxt,
                                         String negativeeBtnTxt,
                                         S2mAlertDialog.AlertListener alertListener) {
        currentDialog = S2mAlertDialog
                .build(context, message, positiveBtnTxt, negativeeBtnTxt, false, alertListener);
        currentDialog.show();
    }

    public static void createCancelableAlertDialog(FragmentActivity context,
                                                   String message,
                                                   S2mAlertDialog.AlertListener alertListener) {
        currentDialog = S2mAlertDialog
                .build(context, message, true, alertListener);
        currentDialog.show();
    }

    public static void createProgressDialog(FragmentActivity context) {
        currentDialog = S2mProgressDialog
                .build(context);
        currentDialog.show();

    }

    public static void setCurrentDialog(S2MDialogFragment currentDialog) {
        DialogHelper.currentDialog = currentDialog;
    }

    public static S2MDialogFragment getCurrentDialog() {
        return currentDialog;
    }

}

package com.example.wowconnect.ui.helpers;

import android.app.Dialog;
import android.support.v4.app.FragmentActivity;

/**
 *  Created by Ajit on 25-01-2017.
 */

public class DialogHelper {
    static Dialog currentDialog;

    public static void createAlertDialog(FragmentActivity context,
                                         String message,
                                         String positiveBtnTxt,
                                         String negativeeBtnTxt,
                                         S2mAlertDialog.AlertListener alertListener) {
        S2mAlertDialog
                .build(context, message,positiveBtnTxt,negativeeBtnTxt,false,alertListener)
                .show();
    }

    public static void createCancelableAlertDialog(FragmentActivity context,
                                                   String message,
                                                   S2mAlertDialog.AlertListener alertListener) {
        S2mAlertDialog
                .build(context, message, true, alertListener)
                .show();
    }

    public static void createProgressDialog(FragmentActivity context) {
        S2mProgressDialog
                .build(context)
                .show();
    }

    public static void setCurrentDialog(Dialog currentDialog) {
        DialogHelper.currentDialog = currentDialog;
    }

    public static Dialog getCurrentDialog() {
        return currentDialog;
    }
}

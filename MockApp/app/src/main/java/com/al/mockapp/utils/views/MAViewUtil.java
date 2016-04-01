package com.al.mockapp.utils.views;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.al.mockapp.R;
import com.al.mockapp.controllers.MAAppController;

/**
 * Created by vineeth on 31/03/16
 */
public class MAViewUtil {
    private static ProgressDialog sProgressDialog = null;

    private static Snackbar sSnackbar = null;

    public static void showProgressDialog(final Context context,
                                          final String title, final String message,
                                          final DialogInterface.OnCancelListener listener) {
        dismissProgressDialog();

        sProgressDialog = new ProgressDialog(context);

        sProgressDialog.setTitle(title);
        sProgressDialog.setMessage(message);
        sProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        sProgressDialog.setCanceledOnTouchOutside(false);
        if (listener == null) {
            sProgressDialog.setCancelable(false);
        } else {
            sProgressDialog.setCancelable(true);
            sProgressDialog.setOnCancelListener(listener);
        }
        if (!sProgressDialog.isShowing()) {
            sProgressDialog.show();
        }
    }

    public static boolean isProgressDialogShowing() {
        return sProgressDialog != null && sProgressDialog.isShowing();
    }

    public static void dismissProgressDialog() {
        try {
            if (sProgressDialog != null) {
                if (sProgressDialog.isShowing()) sProgressDialog.dismiss();

                sProgressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showToastMessage(Context context, String message,
                                        int duration) {
        Toast.makeText(context, message, duration).show();
    }

    public static void showSnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE).show();
    }

    public static void showShortSnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).setActionTextColor(Color.WHITE).show();
    }

    public static void showIndefiniteSnackBar(View view, String message, View.OnClickListener onClickListener) {
        showIndefiniteSnackBar(view, message,
                MAAppController.getContext().getString(R.string.retry), onClickListener);
    }

    public static void showIndefiniteSnackBar(View view, String message, String actionText, View.OnClickListener onClickListener) {
        sSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
        sSnackbar.setActionTextColor(Color.WHITE)
                .setAction(actionText, onClickListener).show();
    }

    public static void showAlertDialog(Context context, String title, String message,
                                       String buttonPostiveText, DialogInterface.OnClickListener buttonPostiveClickListener,
                                       String buttonNegativeText, DialogInterface.OnClickListener buttonNegativeClickListener) {

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        if (title == null) {
            try {
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        if (buttonPostiveText == null && buttonPostiveClickListener == null) {
            alertDialog.setCancelable(true);
            alertDialog.setCanceledOnTouchOutside(true);
        } else {
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, buttonPostiveText, buttonPostiveClickListener);
            if (buttonNegativeText != null || buttonNegativeClickListener != null) {
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, buttonNegativeText, buttonNegativeClickListener);
            }
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
        }
        alertDialog.setIcon(null);
        alertDialog.show();
    }

    public static void dismissSnackBar() {
        if (sSnackbar != null) {
            sSnackbar.dismiss();
        }
    }
}

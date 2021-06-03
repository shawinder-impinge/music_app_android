package com.musicapp.util.sweetdialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.musicapp.R;
import com.musicapp.util.SharedPreference;

public class Alert {

    static Alert utilityClass;
    private static OnOKClickListner onOKClickListnerr;

    public static void setOnOKClickListner(OnOKClickListner onOKClickListner) {
        onOKClickListnerr = onOKClickListner;
    }

    public Alert() {
    }

    public Alert getInstance() {
        if (utilityClass == null) {
            utilityClass = new Alert();
        }
        return utilityClass;
    }

    public static void showLog(String title, String msg) {
        Log.e(title, msg);
    }

    public static void snackBar(View view, String msg) {
        try {
            Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Dialog startLoader(Context mcontext)
    {
        Dialog mprogressDialog = new Dialog(mcontext);
       // mprogressDialog.setContentView(R.layout.gif_layout);
        mprogressDialog.setCancelable(false);
        mprogressDialog.setCanceledOnTouchOutside(false);
        mprogressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return mprogressDialog;
    }


    public static void showSimpleDialog(final Context context, String msg)// Simple Alert
    {
        TextView ttl, txt_msg, ok;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.custom_alert_dialog);
        LayoutInflater inflater = ((AppCompatActivity) context).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_message, null);
        ttl = (TextView) dialogView.findViewById(R.id.title);
        txt_msg = (TextView) dialogView.findViewById(R.id.message);
        txt_msg.setText(msg);
        ttl.setText(context.getString(R.string.app_name));
        ok = (TextView) dialogView.findViewById(R.id.submit);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialog.getWindow().setLayout(width, height);
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public static void showDialog(final Context mContext, String msg, final Class cs)// For Questionable Condition
    {
        TextView ttl, txt_msg, submit;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext, R.style.custom_alert_dialog);
        LayoutInflater inflater = ((AppCompatActivity) mContext).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_message, null);
        ttl = (TextView) dialogView.findViewById(R.id.title);
        txt_msg = (TextView) dialogView.findViewById(R.id.message);
        ttl.setText(mContext.getString(R.string.app_name));
        txt_msg.setText(msg);
        submit = (TextView) dialogView.findViewById(R.id.submit);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        int width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialog.getWindow().setLayout(width, height);
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) mContext;
                activity.finish();
                Intent intent = new Intent(mContext, cs);
                mContext.startActivity(intent);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public static void showDialog(final Context mContext, String msg, final Class cs, String title)// For Questionable Condition
    {
        TextView ttl, txt_msg, submit;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext, R.style.custom_alert_dialog);
        LayoutInflater inflater = ((AppCompatActivity) mContext).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_message, null);
        ttl = (TextView) dialogView.findViewById(R.id.title);
        txt_msg = (TextView) dialogView.findViewById(R.id.message);
        ttl.setText(mContext.getString(R.string.app_name));
        txt_msg.setText(msg);
        submit = (TextView) dialogView.findViewById(R.id.submit);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        int width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialog.getWindow().setLayout(width, height);
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) mContext;
                activity.finish();
                Intent intent = new Intent(mContext, cs).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public static void showDialog(final Context mContext, String msg)// For Questionable Condition
    {
        TextView ttl, txt_msg, submit;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext, R.style.custom_alert_dialog);
        LayoutInflater inflater = ((AppCompatActivity) mContext).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_message, null);
        ttl = (TextView) dialogView.findViewById(R.id.title);
        txt_msg = (TextView) dialogView.findViewById(R.id.message);
        ttl.setText(mContext.getString(R.string.app_name));
        txt_msg.setText(msg);
        submit = (TextView) dialogView.findViewById(R.id.submit);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        int width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialog.getWindow().setLayout(width, height);
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) mContext;
                activity.finish();
            }
        });
        alertDialog.show();
    }

    public static Toast showToast(final Context context, String msg) {
        Toast toast = null;
        try {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            ((TextView) ((LinearLayout) toast.getView()).getChildAt(0)).setGravity(Gravity.CENTER_HORIZONTAL);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toast;
    }


    public static void showErrorAlert(final Context context, String msg)// Simple Alert
    {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(msg)
                .setConfirmText(context.getString(R.string.ok))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                    }
                })
                .show();
    }


    public static void showWarningAlert(final Context context, String message) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(message)
                .setConfirmText(context.getString(R.string.ok))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                    }
                }).show();
    }

    public static void showSuccessAlert(final Context context, String message) {
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(message)
                .setConfirmText(context.getString(R.string.ok))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                    }
                }).show();
    }

    public static void showSuccessAlertWithResponse(final Context context, String message) {
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(message)
                .setConfirmText(context.getString(R.string.ok))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                        if (onOKClickListnerr != null) {
                            onOKClickListnerr.onOK(true);
                        }
                    }
                }).show();
    }

    public static void showWarningAlertWithResponse(final Context context, String message) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(message)
                .setConfirmText(context.getString(R.string.ok))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                        if (onOKClickListnerr != null) {
                            onOKClickListnerr.onOK(true);
                        }
                    }
                }).show();
    }


    public static void showQuestionAlert(final Context context, String message) {
        new SweetAlertDialog(context, SweetAlertDialog.QUESTION_TYPE)
                .setTitleText(message)
                .setConfirmText(context.getString(R.string.yes))
                .setCancelText(context.getString(R.string.no))
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        onOKClickListnerr.onOK(false);
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        if (onOKClickListnerr != null) {
                            onOKClickListnerr.onOK(true);
                        }
                    }
                }).show();
    }


    public static void showLogoutAlert(final Context context, final Activity activity) {
        new SweetAlertDialog(context, SweetAlertDialog.QUESTION_TYPE)
                .setTitleText(context.getString(R.string.you_want_logout))
                .setConfirmText(context.getString(R.string.yes))
                .setCancelText(context.getString(R.string.no))
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        SharedPreference.removeAll(context);
                       /* context.startActivity(new Intent(context, LoginPage.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK));
                        activity.finish();*/
                    }
                }).show();
    }

    public interface OnOKClickListner {
        public void onOK(boolean yes);
    }

}

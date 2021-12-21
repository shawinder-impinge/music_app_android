package com.impinge.soul.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;

import com.impinge.soul.R;

import org.json.JSONObject;

import retrofit2.Response;

public class Utility {

    private static Utility utility;
    private static Context ctx;
    private ProgressDialog mProgressDialog;

    public static Utility getInstance(Context context){

        if(utility == null){
            utility = new Utility();

        }

        ctx = context;
        return utility;

    }

    public static class ErrorResponseHandler {

        public static String parseError(Response<?> response){
            String errorMsg = null;
            try {
                JSONObject jObjError = new JSONObject(response.errorBody().string());
                errorMsg = jObjError.getString("message");
                return errorMsg ;
            } catch (Exception e) {
            }
            return errorMsg;
        }
    }

    public  void showLoading(String message) {
        mProgressDialog = new ProgressDialog(ctx);
        mProgressDialog.setMessage(message);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    public  void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    public void fullWindow(Activity context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            final WindowInsetsController insetsController = context.getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.statusBars());
            }
        } else {
            context.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }
    }

    public static int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        //return useWhiteIcon ? R.mipmap.ic_stat_call_white : R.mipmap.ic_launcher;
        return useWhiteIcon ? R.mipmap.ic_launcher : R.mipmap.ic_launcher;
    }
}

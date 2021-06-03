package com.musicapp.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;
import com.musicapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Constants {


    public static final String SONG_NAME = "SONG_NAME";
    public static final String SONG_ID = "SONG_ID";
    public static final String SONG_COVER_IMAGE = "SONG_IMAGE";
    public static final String SONG_AUDIO_URL = "SONG_AUDIO_URL";
    public static final String SONG_AUTHOR_NAME = "SONG_AUTHOR_NAME";
    public static final String SONG_AUTHOR_IMAGE = "SONG_AUTHOR_IMAGE";
    public static final String SONG_DESCRIPTION = "SONG_DESCRIPTION";
    public static final String SONG_DURATION = "SONG_DURATION";
    public static final String ALBUM_DESCRIPTION = "ALBUM_DESCRIPTION";
    public static final String ALBUM_COVER_IMAGE = "ALBUM_COVER_IMAGE";
    public static final String ALBUM_AUTHOR_NAME = "ALBUM_AUTHOR_NAME";
    public static final String ALBUM_AUTHOR_IMAGE = "ALBUM_AUTHOR_IMAGE";
    public static final String ALBUM_ID = "ALBUM_ID";
    public static final String ALBUM_TITILE = "ALBUM_TITILE";
    static Date date1;
    public static final String BASE_URL = "";
    public static final String Image_url = "";
    public static final String Flag_url = "";


    public static final int SECOND_MILLIS = 1000;
    public static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    public static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    public static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    public static final String Countries = "countries";
    public static final String Signup = "signup";
    public static final String login = "login";
    public static final String contact_us = "contact_us";
    public static final String about_me = "about_me";
    public static final String forgot_pass = "forgot_password";
    public static final String Reset_pass = "reset_password";
    public static final String get_about_me = "get_about_me";
    public static final String fb_login = "fb_login";
    public static final String updateProfile = "updateProfile";
    public static final String updatebasic_info = "updateBasicInfo";
    public static final String update_appearnce = "updateAppearance";
    public static final String status = "updateStatusLatLong";
    public static final String user_list = "get_users_details";
    public static final String Viplist = "get_vip_users_details";
    public static final String Block_Unblock_user = "block_user";
    public static final String BlockUser_list = "block_user_list";
    public static final String LikeUnlike_user = "like_user";
    public static final String likeuser_list = "liked_user_list";
    public static final String favourite_unfavourite_user = "favourite_user";
    public static final String favourite_user_list = "favourite_user_list";
    public static final String gold_membership = "get_gold_plan";
    public static final String vip_membership = "get_vip_plan";
    public static final String View_profileService = "get_other_user_detail";
    public static final String add_device = "save_device";
    public static final String logout = "logout";
    public static final String discount_code = "calculate_discount";
    public static final String my_fitoor = "requestForGateway";
    public static final String pay_pal = "paypal/payment";
    public static final String residence_service = "residence";
    public static final String search = "search";
    public static final String free_trial = "free_trial";
    public static final String editname = "updateProfile";
    public static final String RemoveProfilePicMain = "remove_profile_pic/";
    public static final String RemoveOldPics = "remove_old_pics/";
    public static final String getUserDetail_Fitoora = "get_user_detail/";
    public static final String chat_service = "get_chat";
    public static final String inbox = "get_all_chats";
    public static final String deletchat = "delete_chat";
    public static final String set_message_seen = "set_message_seen";
    public static final String aboutus_new = "get_about_us";
    public static final String usage_policy = "get_privacy_policy";
    public static final String likechatcount = "get_chat_like_count/";
    public static final String like_seen = "set_like_seen/";
    public static final String admincontact_us = "contact_admin";
    public static final String adminchat = "get_admin_chat";
    public static final String report = "report_user";













    /*.........................todo keys for sharedprefrence....................*/

    public static String Id = "id";
    public static String Name = "name";
    public static String User_type = "user_type";
    public static String Email = "email";
    public static String Image = "image";
    public static String Country_id = "country_id";
    public static String Age = "age";
    public static String Gender = "gender";
    public static String smoking = "smoking";
    public static String residence = "residence";
    public static String region = "region";
    public static String social_status = "social_status";
    public static String marriage_type = "marriage_type";
    public static String children = "children";
    public static String Height = "Height";
    public static String weight = "weight";
    public static String skin_color = "skin_color";
    public static String hair_color = "hair_color";
    public static String body_type = "body_type";
    public static String Introduction = "introduction";
    public static String Looking_for = "looking_for";
    public static String LAT = "latitude";
    public static String LONG = "longitude";
    public static String Device_id = "device_id";
    public static String Device_token = "token";
    public static String country_name = "c_name";
    public static String flag = "flag";
    public static String search_parallelity = "search_parallelity";
    public static String Facebuk = "facebook_id";
    public static String facebook_status = "fb_status";
    public static String free_trialss = "free_trial";
    public static String free_trial_status = "free_trial_status";
    public static String Pasword = "decoded_password";


    public static String getFormattedDate(long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);
        return DateFormat.format("yyyy-MM-dd", smsTime).toString();
    }

    /* GET CURRENT TIME */
    public static long currentTime() {
        Date mDate;
        long milliseconds = 0;
        String givenDateString = String.valueOf(Calendar.getInstance().getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        try {
            mDate = sdf.parse(givenDateString);
            milliseconds = mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return milliseconds;
    }

    /*Use to store data in SharedPreferences*/
    public static String KEY = "mmp";
    public static String ID = "ID";

    /*To hide keyboard*/
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    /*To show keyboard*/
    public static void showSoftKeyboard(Activity activity, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }


    /*public static String getTimeAgo(long time, Context context) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }
        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;

        }
        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return context.getString(R.string.justnoww);
        } else if (diff < 2 * MINUTE_MILLIS) {
            return context.getString(R.string.aminuteagoo);
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + context.getString(R.string.minuteagoo);
        } else if (diff < 90 * MINUTE_MILLIS) {
            return context.getString(R.string.anhouragoo);
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + context.getString(R.string.houragoo);
        } else if (diff < 48 * HOUR_MILLIS) {
            return context.getString(R.string.yesterdayy);
        } else {
            return diff / DAY_MILLIS + context.getString(R.string.dayaagoo);
        }

    }*/


    public static File makeFile(Bitmap bitmap, String name) {

        File file;
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        file = new File(dir, name);
        if (file.exists()) {
            file.delete();
        }
        file = new File(dir, name);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file.getPath());
            bitmap.compress(Bitmap.CompressFormat.PNG, 00, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return file;
    }

    public static Bitmap shrinkBitmap(String file, int width, int height) {

        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                bmpFactoryOptions.inSampleSize = heightRatio;
            } else {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bitmap;
    }

    public static File compressFile(File file, String name) {

        Bitmap bitmap = shrinkBitmap(file.getPath(), 400, 400);
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        file = new File(dir, name);
        if (file.exists()) {
            file.delete();
        }
        file = new File(dir, name);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file.getPath());
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return file;
    }

    /*public static String getFcmDeviceID(Context mContext) {
        String refreshedToken = "";
        if (SharedPreference.retrieveFcmDeviceId(mContext) == null) {
            refreshedToken = FirebaseInstanceId.getInstance().getToken();
            SharedPreference.storeFcmDeviceId(mContext, refreshedToken);
            return refreshedToken;
        } else {
            refreshedToken = SharedPreference.retrieveFcmDeviceId(mContext);
        }
        return refreshedToken;
    }*/

    public static String getUniqueId(Context mContext) {
        String android_id = "";
        android_id = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        return android_id;
    }


    public static String parseDateToddMMyyyyTime(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd-MMM-yyyy h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        String timeee[] = time.split("T");

        Log.d("nhdd", timeee[0]);
        return timeee[0];
    }


    public static String dateTimeNew(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {


            date1 = sdf.parse(date);


        } catch (ParseException e) {
            e.printStackTrace();
        }
        // output format: hour:minute AM/PM
        SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        // assuming a timezone in India
        //outputFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        System.out.println(outputFormat.format(date1));
        String out = outputFormat.format(date1);

        //return String.valueOf(date1);
        return out;
    }

    public static Snackbar showActionSnackBar(View parentView, String msg, String actionText, final View.OnClickListener actionListener) {
        try {
            final Snackbar snackBar = Snackbar.make(parentView, msg, Snackbar.LENGTH_INDEFINITE);
            snackBar.setActionTextColor(Color.WHITE);
            View view = snackBar.getView();
            TextView tv = view.findViewById(com.google.android.material.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            tv.setMaxLines(5);
            snackBar.setAction(actionText, v -> {
                snackBar.dismiss();
                actionListener.onClick(v);
            });
            snackBar.show();
            return snackBar;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static Snackbar showSnackBar(View anyView, String msg) {
        final Snackbar snackBar = Snackbar.make(anyView, msg, Snackbar.LENGTH_LONG);
        snackBar.setActionTextColor(Color.WHITE);
        if (anyView instanceof CoordinatorLayout) {
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)
                    snackBar.getView().getLayoutParams();
            params.setMargins(0, 0, 0, 130);
            snackBar.getView().setLayoutParams(params);
        }
        View view = snackBar.getView();
        TextView tv = view.findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        tv.setMaxLines(5);
        snackBar.show();
        return snackBar;
    }


    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifi != null && wifi.isConnected()) {
            return true;
        }

        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobile != null && mobile.isConnected()) {
            return true;
        }

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }


        return false;
    }

    public static String getTimeAgo(long time, Context context) {
        final int SECOND_MILLIS = 1000;
        final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        final int DAY_MILLIS = 24 * HOUR_MILLIS;
        if (time < 1000000000000L) {
            time *= 1000;
        }
        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return context.getString(R.string.time_just_now);
        }
        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return context.getString(R.string.time_just_now);
        } else if (diff < 2 * MINUTE_MILLIS) {
            return context.getString(R.string.time_minute_ago);
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " " + context.getString(R.string.time_min_ago);
        } else if (diff < 90 * MINUTE_MILLIS) {
            return context.getString(R.string.time_an_hr_ago);
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " " + context.getString(R.string.time_hr_ago);
        } else if (diff < 48 * HOUR_MILLIS) {
            return context.getString(R.string.time_yesterday);
        } else {
            return diff / DAY_MILLIS + " " + context.getString(R.string.time_day_ago);
        }
    }

}

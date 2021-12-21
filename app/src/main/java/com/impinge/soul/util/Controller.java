package com.impinge.soul.util;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.io.File;

public class Controller extends Application {
    public static String IMAGE_PATH = null;
    private static final String FORMAT = "hh:mm a";
    public static final String APP_NAME = "SOUL";


    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        makeDirectory();
    }



    public static String getImagePath() {
        return IMAGE_PATH;
    }

    private void makeDirectory() {
        File directory = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            directory = new File(Environment.getExternalStorageDirectory() + File.separator + APP_NAME);
            if (!directory.exists())
                directory.mkdirs();
        } else {
            directory = getApplicationContext().getDir(APP_NAME, Context.MODE_PRIVATE);
            if (!directory.exists())
                directory.mkdirs();
        }

        if (directory != null) {
            File _image = new File(directory + File.separator + "Images");

            if (!_image.exists())
                _image.mkdirs();


            IMAGE_PATH = directory + File.separator + "Images";
        }
    }
}

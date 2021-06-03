package com.musicapp.util.cropping.latest_cropper.util;

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.core.content.ContextCompat;


public class ResUtil {

    private Context context;

    public ResUtil(Context context) {
        this.context = context;
    }

    @ColorInt
    public int color(@ColorRes int colorRes) {
        return ContextCompat.getColor(context, colorRes);
    }

    public int dimen(@DimenRes int dimRes) {
        return Math.round(context.getResources().getDimension(dimRes));
    }
}

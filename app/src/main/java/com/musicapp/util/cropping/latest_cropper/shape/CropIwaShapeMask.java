package com.musicapp.util.cropping.latest_cropper.shape;

import android.graphics.Bitmap;

import java.io.Serializable;

public interface CropIwaShapeMask extends Serializable {
    Bitmap applyMaskTo(Bitmap croppedRegion);
}

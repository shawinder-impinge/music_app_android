package com.impinge.soul.util.cropping.latest_cropper;


import androidx.annotation.IntRange;

public class AspectRatio {

    @SuppressWarnings("Range")
    public static final AspectRatio IMG_SRC = new AspectRatio(-1, -1);

    private final int width;
    private final int height;

    public AspectRatio(@IntRange(from = 1) int w, @IntRange(from = 1) int h) {
        this.width = w;
        this.height = h;
    }

    public int getWidth() {
        return width;
    }

    public boolean isSquare() {
        return width == height;
    }

    public int getHeight() {
        return height;
    }

    public float getRatio() {
        return ((float) width) / height;
    }
}

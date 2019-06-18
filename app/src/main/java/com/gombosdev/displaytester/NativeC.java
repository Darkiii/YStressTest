package com.gombosdev.displaytester;

import android.graphics.Bitmap;

public class NativeC {

    private static NativeC instance = null;

    private NativeC() {

    }

    public static synchronized NativeC getInstance() {
        if (instance == null) {
            instance = new NativeC();
        }

        return instance;
    }

    static {
        System.loadLibrary("noise_generator");
    }

    public native void generateFourColorGradient(Bitmap paramBitmap, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
    public native void generateNoise(Bitmap paramBitmap, int paramInt);
}

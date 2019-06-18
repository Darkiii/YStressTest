package com.example.ystresstest.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.gombosdev.displaytester.NativeC;

public class NoiseViewImpl extends NoiseView {

    private Bitmap bitmap;
    private NativeC nativeC;

    public NoiseViewImpl(Context context) {
        super(context);
        bitmap = Bitmap.createBitmap(1024, 600, Bitmap.Config.ARGB_8888);
        nativeC = NativeC.getInstance();
    }

    public void generateBitmap() {
        nativeC.generateNoise(bitmap, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (canvas == null) {
            return;
        }
        super.onDraw(canvas);
        generateBitmap();
        canvas.drawBitmap(bitmap, 1024, 600, null);
    }
}

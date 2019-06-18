package com.example.ystresstest.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.ystresstest.R;
import com.gombosdev.displaytester.NativeC;

import java.util.jar.Attributes;

public class NoiseView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "NoiseView";

    private SurfaceHolder mHolder;
    private Bitmap mNoiseBitmap = null;
    private NativeC generator;
    private Paint mPaint;
    private int mViewWidth  = 1024;
    private int mViewHeight = 600;
    private boolean generatorFlag = false;

    public NoiseView(Context context) {
        super(context, null);
        Log.d(TAG, "NoiseView0");
    }

    public NoiseView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        Log.d(TAG, "NoiseView1");

        initHolder();
//        setZOrderOnTop(true);

        generator = NativeC.getInstance();
        mPaint = new Paint();
        mNoiseBitmap = Bitmap.createBitmap(1024, 600, Bitmap.Config.ARGB_8888);

    }

    public NoiseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        Log.d(TAG, "NoiseView2");

//        initHolder();
//        setZOrderOnTop(true);
//
//        generator = NativeC.getInstance();
//        mPaint = new Paint();
//        mNoiseBitmap = Bitmap.createBitmap(1024, 600, Bitmap.Config.ARGB_8888);
//
//        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.NoiseView, defStyleAttr, 0);
//        array.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //--- measure the view's width
        int widthMode  = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            mViewWidth = (getPaddingStart() + mNoiseBitmap.getWidth() + getPaddingEnd());
        }

        //--- measure the view's height
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY) {
            mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            mViewHeight = (getPaddingTop() + mNoiseBitmap.getHeight() + getPaddingBottom());
        }

        Log.d(TAG, "width= " + mViewWidth + " , height= " + mViewHeight);

        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    private void initHolder() {
        mHolder = this.getHolder();
//        mHolder.setFormat(PixelFormat.TRANSLUCENT);
        mHolder.addCallback(this);
    }

    private void drawView() {
        Rect rect = new Rect();

        if (mHolder == null) {
            return;
        }
        Canvas canvas = mHolder.lockCanvas();
        if (canvas == null) {
            return;
        }

        rect.left = 0;
        rect.top = 0;
        rect.right = mViewWidth - 1;
        rect.bottom = mViewHeight - 1;
//        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//        generator.generateNoise(mNoiseBitmap, 0);
//        Log.d(TAG, "Pixel: " + mNoiseBitmap.getPixel(200, 110));
//        canvas.drawBitmap(mNoiseBitmap, 1024, 600, mPaint);

//        canvas.drawColor(Color.WHITE);
//        mPaint.setColor(Color.BLACK);
        generator.generateNoise(mNoiseBitmap, 0);
//        mNoiseBitmap = generateNewBitmap(mNoiseBitmap);
        canvas.drawBitmap(mNoiseBitmap, null, rect, mPaint);
//        canvas.drawRect(0, 0, 100, 100, mPaint);
        mHolder.unlockCanvasAndPost(canvas);
    }

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (generatorFlag) {
                    drawView();

//                    try {
//                        Thread.sleep(5);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        }).start();
    }

    private Bitmap generateNewBitmap(Bitmap oldBitmap) {
//        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Bitmap mBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true);
        int bitmapWidth = mBitmap.getWidth();
        int bitmapHeight = mBitmap.getHeight();
        int[] palette = {Color.WHITE, Color.BLACK};
        for (int i = 0; i < bitmapHeight; i++) {
            for (int j = 0; j < bitmapWidth; j++) {
//                int color = mBitmap.getPixel(j, i);
                mBitmap.setPixel(j, i, palette[(int)(0 + Math.random()*(1-0+1))]);
            }
        }
        return mBitmap;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        generatorFlag = true;
        start();
        Log.e(TAG, "surfaceCreated");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        generatorFlag = false;
        Log.e(TAG, "surfaceDestroyed");
    }
}

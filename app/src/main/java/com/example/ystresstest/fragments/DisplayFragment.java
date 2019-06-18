package com.example.ystresstest.fragments;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.ystresstest.R;
import com.example.ystresstest.view.NoiseView;
import com.example.ystresstest.view.NoiseViewImpl;
import com.gombosdev.displaytester.NativeC;

import javax.net.ssl.SNIHostName;

public class DisplayFragment extends Fragment implements SurfaceHolder.Callback {

    private static String TAG = "DisplayFragment";
    private RelativeLayout       mLayout;
    private NoiseView noiseView;
    private SurfaceHolder holder;
    private ImageView imageView;
    private NativeC generator = null;
    private int screenWidth, screenHeight;
    private boolean generatorFlag = true;
    private boolean handled = true;
    private DrawingThread mThread;

    private Paint paint;
    private Bitmap bitmap;
    private Canvas canvas;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                default:
                    generator.generateNoise(bitmap, 0);
                    canvas.drawBitmap(bitmap, screenWidth, screenHeight, paint);
                    mHandler.sendEmptyMessage(0);
                    imageView.setImageBitmap(bitmap);
            }
            mHandler.sendEmptyMessage(0);
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState) {
        super.onCreateView(inflater, container, savedInstaceState);

        mLayout = (RelativeLayout) inflater.inflate(R.layout.display_fragment, container, false);
//        noiseView = (SurfaceView)mLayout.findViewById(R.id.surface);

//        imageView = mLayout.findViewById(R.id.image);
        noiseView = mLayout.findViewById(R.id.noise_view);
//        holder = noiseView.getHolder();
//        holder.addCallback(this);

//        generator = NativeC.getInstance();
//
//        Point outSize = new Point();
//        getActivity().getWindowManager().getDefaultDisplay().getRealSize(outSize);
//        screenWidth = outSize.x;
//        screenHeight = outSize.y;
//        Log.w(TAG, "screenWidth = " + screenWidth + ",screenHeight = " + screenHeight);
//
//        paint = new Paint();
////        paint.setColor(Color.BLACK);
//
//        if (bitmap == null) {
//            bitmap = Bitmap.createBitmap(1024, 600, Bitmap.Config.ARGB_8888);
//            canvas = new Canvas(bitmap);
////            canvas.drawColor(Color.WHITE);
//        }
//        generatorFlag = true;
//
//        generator.generateNoise(bitmap, 0);
//
//        canvas.drawBitmap(bitmap, 1024, 600, paint);
//
//        noiseView.draw(canvas);
//        holder.unlockCanvasAndPost(canvas);

//        mHandler.sendEmptyMessage(0);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (generatorFlag) {
////                    mHandler.post(new Runnable() {
////                        @Override
////                        public void run() {
//
//                        generator.generateNoise(bitmap, 0);
//                        canvas.drawBitmap(bitmap, screenWidth, screenHeight, paint);
//                        mHandler.sendEmptyMessage(0);
//                        sleepMs();
////                        }
////                    });
//                }
//            }
//        });

        return mLayout;
    }

    private boolean sleepMs() {
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private synchronized void flashFlag(boolean flag) {
        handled = flag;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        generatorFlag = false;
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
        return bitmap;
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "<<<<<<<<<<<<<");
//        mThread = new DrawingThread(holder, bitmap);
//        mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
/*        generator.generateNoise(bitmap, 0);
        holder.lockCanvas().drawBitmap(bitmap, screenWidth, screenHeight, paint);*/
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private static class DrawingThread extends HandlerThread implements Handler.Callback {
        private static final int MSG_MOVE = 101;

        private SurfaceHolder mDrawingHolder;
        private Bitmap pictures;
        private Paint mPaint;
        private Handler handler;
        private NativeC nativeC;
        private boolean mRunning;

        public DrawingThread(SurfaceHolder holder, Bitmap bitmap) {
            super("DrawingThread");
            mDrawingHolder = holder;
            pictures = bitmap;
            mPaint = new Paint();
        }

        @Override
        protected void onLooperPrepared() {
            handler = new Handler(getLooper(), this);
            nativeC = NativeC.getInstance();
            mRunning = true;
            handler.sendEmptyMessage(MSG_MOVE);

        }

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_MOVE:
                    if (!mRunning) {
                        return true;
                    }
                    Log.e(TAG, "<<<<<<<<<<<<<<<<<<<<<<<<<");
                    nativeC.generateNoise(pictures, 0);
                    Canvas canvas = mDrawingHolder.lockCanvas();
                    canvas.drawBitmap(pictures, 1024, 600, mPaint);
                    mDrawingHolder.unlockCanvasAndPost(canvas);
                    break;
            }
            if (mRunning) {
                handler.sendEmptyMessage(MSG_MOVE);
            }
            return false;
        }
    }
}

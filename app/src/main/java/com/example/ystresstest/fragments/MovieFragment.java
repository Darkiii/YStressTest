package com.example.ystresstest.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.ystresstest.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MovieFragment extends Fragment {
    private static final String TAG = "MovieFragment";

    private RelativeLayout       mLayout;
    private VideoView movieSurface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState) {
        super.onCreateView(inflater, container, savedInstaceState);

        mLayout = (RelativeLayout) inflater.inflate(R.layout.movie_fragment, container, false);

        movieSurface = mLayout.findViewById(R.id.scene);

//        String videoPath = "/sdcard/demovideo.mov";
//            movieSurface.setVideoPath("file:///android_asset/test.mp4");
//            movieSurface.setVideoURI(Uri.parse("android.resource://com.example.ystresstest/raw/test"));
        copyAssetAndWrite("test.mp4");
        File videoFile = new File(getContext().getCacheDir(), "test.mp4");
        if (!videoFile.exists()) {
            Toast.makeText(getContext(), "没有找到视频文件", Toast.LENGTH_SHORT).show();
        } else {
            final String videoPath = videoFile.getAbsolutePath();
            Log.d(TAG, "filepath:" + videoFile.getAbsolutePath());
            movieSurface.setVideoPath(videoPath);
            movieSurface.start();
            movieSurface.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    mp.setLooping(true);
                }
            });
            movieSurface.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    movieSurface.setVideoPath(videoPath);
                    movieSurface.start();
                }
            });
        }

        return mLayout;
    }

    private boolean copyAssetAndWrite(String fileName){
        try {
            File cacheDir = getContext().getCacheDir();
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            File outFile = new File(cacheDir,fileName);
            if (!outFile.exists()) {
                boolean res = outFile.createNewFile();
                if (!res) {
                    return false;
                }
            } else {
                if (outFile.length() > 10) {  //表示已经写入一次
                    return true;
                }
            }
            InputStream is = getContext().getAssets().open(fileName);
            FileOutputStream fos = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int byteCount;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
            is.close();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

}

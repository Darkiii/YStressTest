package com.example.ystresstest.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.ystresstest.R;

import org.rajawali3d.renderer.ISurfaceRenderer;
import org.rajawali3d.renderer.Renderer;
import org.rajawali3d.view.IDisplay;
import org.rajawali3d.view.ISurface;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public abstract class BaseFragment extends Fragment implements IDisplay {

    protected FrameLayout       mLayout;
    protected ISurface          mRajawaliSurface;
    protected ISurfaceRenderer  mRenderer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState) {
        super.onCreateView(inflater, container, savedInstaceState);

        mLayout = (FrameLayout) inflater.inflate(getLayoutID(), container, false);

        mRajawaliSurface = mLayout.findViewById(R.id.rajawali_surface);

        mRenderer = createRenderer();
        onBeforeApplyRenderer();
        applyRenderer();

        return mLayout;
    }

    protected  void onBeforeApplyRenderer() {
    }

    protected void applyRenderer() {
        mRajawaliSurface.setSurfaceRenderer(mRenderer);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mLayout != null) {
            mLayout.removeView((View) mRajawaliSurface);
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.rajawali_textureview_fragment;
    }

    protected abstract class AExampleRenderer extends Renderer {
        final BaseFragment exampleFragment;

        public AExampleRenderer(Context context, @Nullable BaseFragment fragment) {
            super(context);
            exampleFragment = fragment;
        }

        @Override
        public void onOffsetsChanged(float v, float v2, float v3, float v4, int i, int i2) {

        }

        @Override
        public void onTouchEvent(MotionEvent event) {

        }

        @Override
        public void onRenderSurfaceCreated(EGLConfig config, GL10 gl, int width, int height) {
//            showLoader();
            super.onRenderSurfaceCreated(config, gl, width, height);
//            hideLoader();
        }

        @Override
        protected void onRender(long ellapsedRealtime, double deltaTime) {
            super.onRender(ellapsedRealtime, deltaTime);
        }
    }
}

package com.vinaysshenoy.android3d;

import android.animation.ValueAnimator;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.vinaysshenoy.core3d.Core3d;

import java.util.concurrent.TimeUnit;

/**
 * Created by vinaysshenoy on 21/1/17.
 */

public class CubeAnimationFragment extends AndroidFragmentApplication {

    private static final String TAG = "CubeAnimationFragment";

    private AndroidApplicationConfiguration androidApplicationConfiguration;
    private Core3d core3d;
    private ValueAnimator cubeRotationAnimator;
    private Handler handler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new Handler(Looper.getMainLooper());

        androidApplicationConfiguration = new AndroidApplicationConfiguration();
        androidApplicationConfiguration.numSamples = 2;
        androidApplicationConfiguration.disableAudio = true;
        androidApplicationConfiguration.useAccelerometer = false;
        androidApplicationConfiguration.useCompass = false;

        //Allow full transparency
        androidApplicationConfiguration.r = 8;
        androidApplicationConfiguration.g = 8;
        androidApplicationConfiguration.b = 8;
        androidApplicationConfiguration.a = 8;

        cubeRotationAnimator = ValueAnimator.ofFloat(0.0F, 360F);
        cubeRotationAnimator.setDuration(TimeUnit.MILLISECONDS.convert(12L, TimeUnit.SECONDS));
        cubeRotationAnimator.setRepeatCount(ValueAnimator.INFINITE);
        cubeRotationAnimator.setRepeatMode(ValueAnimator.RESTART);
        cubeRotationAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        cubeRotationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                final float value = (float) animation.getAnimatedValue();
                core3d.setModelRotation(value, value, value);
            }
        });

        core3d = new Core3d(new Core3d.Core3DCallbacks() {
            @Override
            public void onCreate(final Core3d core3d) {
                core3d.setBackgroundColor(0.46F, 0.46F, 0.46F, 0.65F);
                startAnimator();
            }

            @Override
            public void onResize(Core3d core3d, int width, int height) {

            }

            @Override
            public void onRender(Core3d core3d) {

            }

            @Override
            public void onPause(Core3d core3d) {
                setAnimatorPaused(true);
            }

            @Override
            public void onResume(Core3d core3d) {

                setAnimatorPaused(false);
            }

            @Override
            public void onDispose(Core3d core3d) {
                stopAnimator();
            }
        });
    }

    private void startAnimator() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                cubeRotationAnimator.start();
            }
        });
    }

    private void stopAnimator() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                cubeRotationAnimator.end();
                cubeRotationAnimator.removeAllUpdateListeners();
            }
        });
    }

    private void setAnimatorPaused(final boolean paused) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (paused) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        cubeRotationAnimator.pause();
                    } else {
                        cubeRotationAnimator.end();
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        cubeRotationAnimator.resume();
                    } else {
                        cubeRotationAnimator.start();
                    }
                }
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = initializeForView(core3d, androidApplicationConfiguration);
        //This HAS to be called here before the View is attached to the window
        setTranslucencyEnabled(view);
        return view;
    }

    /**
     * This can be used to make the background of the 3D view transparent/translucent
     * in combination with {@link Core3d#setBackgroundColor(float, float, float, float)}.
     *
     * If this isn't called, the alpha component of the background color has no effect.
     *
     * Note: If you use this, you cannot draw any Android Views on top of this 3D view
     * since setting this forces the Surface view to render on top of everything else
     * */
    private void setTranslucencyEnabled(View view) {
        if (view instanceof SurfaceView) {
            final SurfaceView surfaceView = (SurfaceView) view;
            surfaceView.setZOrderOnTop(true);
            surfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        }
    }
}

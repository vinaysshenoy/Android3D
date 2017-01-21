package com.vinaysshenoy.android3d;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

public class MainActivity extends AppCompatActivity implements AndroidFragmentApplication.Callbacks {

    private static final String TAG = "Exit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_overlay, new CubeAnimationFragment())
                .commit();
    }

    @Override
    public void exit() {
        Log.d(TAG, "Exit!");
    }
}

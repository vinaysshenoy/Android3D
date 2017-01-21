package com.vinaysshenoy.android3d.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.vinaysshenoy.core3d.Core3d;

public class DesktopApplication {

    private final LwjglApplicationConfiguration glAppConfig;
    private final Core3d core3d;
    private final LwjglApplication application;
    private ModelAnimatorThread modelAnimatorThread;

    private DesktopApplication() {

        LwjglApplicationConfiguration.disableAudio = true;

        glAppConfig = new LwjglApplicationConfiguration();

        glAppConfig.title = "Android 3D";
        glAppConfig.width = 1280;
        glAppConfig.height = 720;
        glAppConfig.vSyncEnabled = true;
        glAppConfig.samples = 2;

        core3d = new Core3d(new Core3d.Core3DCallbacks() {
            @Override
            public void onCreate(Core3d core3d) {
                core3d.setBackgroundColor(0.46F, 0.46F, 0.46F, 1.0F);
                modelAnimatorThread = new ModelAnimatorThread(core3d, 30.0F, 30);
                modelAnimatorThread.start();
            }

            @Override
            public void onResize(Core3d core3d, int width, int height) {
                System.out.println("On Resize: " + width + "x" + height);
            }

            @Override
            public void onRender(Core3d core3d) {
                //No log here because this gets called every frame
            }

            @Override
            public void onPause(Core3d core3d) {
                modelAnimatorThread.setPaused(true);
            }

            @Override
            public void onResume(Core3d core3d) {
                modelAnimatorThread.setPaused(false);
            }

            @Override
            public void onDispose(Core3d core3d) {
                modelAnimatorThread.quit();
            }
        });

        application = new LwjglApplication(core3d, glAppConfig);
    }

    public static void main(String[] args) {

        new DesktopApplication();
    }
}

package com.vinaysshenoy.core3d;

import java.util.concurrent.TimeUnit;

/**
 * Created by vinaysshenoy on 21/1/17.
 */

public class ModelAnimatorThread extends Thread {


    private final Core3d core3d;
    private final long sleepInterval;
    private final float degreeChangePerUpdate;
    private boolean paused;
    private boolean quit;
    private float xRotationDegrees = 0F;
    private float yRotationDegrees = 0F;
    private float zRotationDegrees = 0F;

    public ModelAnimatorThread(Core3d core3d, float degreeChangePerSecond, int updatesPersecond) {

        paused = false;
        quit = false;
        this.core3d = core3d;
        this.degreeChangePerUpdate = degreeChangePerSecond / updatesPersecond;
        this.sleepInterval = TimeUnit.MILLISECONDS.convert(1L, TimeUnit.SECONDS) / updatesPersecond;
    }

    @Override
    public void run() {
        super.run();
        while (!quit) {

            //If its been paused, the thread should run, but should not update
            if (!paused) {
                xRotationDegrees = (xRotationDegrees + degreeChangePerUpdate) % 360F;
                yRotationDegrees = (yRotationDegrees + degreeChangePerUpdate) % 360F;
                zRotationDegrees = (zRotationDegrees + degreeChangePerUpdate) % 360F;
                core3d.setModelRotation(xRotationDegrees, yRotationDegrees, zRotationDegrees);
            }

            try {
                sleep(sleepInterval);
            } catch (InterruptedException e) {
                System.out.println("Interrupted; quit: " + quit);
            }

        }
    }


    public void quit() {
        this.quit = true;
        interrupt();
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}

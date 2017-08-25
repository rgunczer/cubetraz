package com.almagems.cubetraz;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.os.SystemClock;
import android.widget.Toast;

import com.almagems.cubetraz.graphics.Graphics;


public final class MainRenderer implements Renderer {

	private long frameStartTimeMS;
    public final Context context;

	MainRenderer(Context context) {
        System.out.println("MainRenderer.ctor");
		this.context = context;
        Game.setRenderer(this);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        System.out.println("MainRenderer.onSurfaceCreated");
        frameStartTimeMS = SystemClock.elapsedRealtime();

        createAndInitGraphicsObject(gl);

        try {
            Graphics.loadStartupAssets();
        } catch (final Exception ex) {
            Activity activity = (Activity) context;
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(context, "Error loading assets. " + ex.toString(), Toast.LENGTH_LONG).show();
                }
            });
            return;
        }

        GameOptions.load();
        GameProgress.load();
        Game.init();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Graphics.onSurfaceChanged(width, height);
        Game.onSurfaceChanged();
    }

	@Override
	public void onDrawFrame(GL10 gl) {
		Game.update();
        Graphics.prepareFrame();
        Game.draw();

        // limit frame rate
        final int framesPerSecond = 30;
        long elapsedFrameTimeMS = SystemClock.elapsedRealtime() - frameStartTimeMS;
        long expectedFrameTimeMS = 1000 / framesPerSecond;
        long timeToSleepMS = expectedFrameTimeMS - elapsedFrameTimeMS;

        if (timeToSleepMS > 0) {
            SystemClock.sleep(timeToSleepMS);
        }
        frameStartTimeMS = SystemClock.elapsedRealtime();
    }

    void handleTouchPress(float normalizedX, float normalizedY, int fingerCount) {
        Game.handleTouchPress(normalizedX, normalizedY, fingerCount);
    }
	
	void handleTouchDrag(float normalizedX, float normalizedY, int fingerCount) {
        Game.handleTouchDrag(normalizedX, normalizedY, fingerCount);
    }
	
	void handleTouchRelease(float normalizedX, float normalizedY) {
        Game.handleTouchRelease(normalizedX, normalizedY);
    }

	void pause() {
        Audio.release();
        Game.fboLost = true;
    }

    void resume() {
        Audio.reInit();
    }

    void incrementSolverCount() {
        int currentSolverCount = GameOptions.getSolverCount();
        GameOptions.setSolverCount(currentSolverCount + 1);
        Game.updateDisplayedSolvers();
    }

    private void createAndInitGraphicsObject(GL10 gl) {
        Graphics.gl = gl;
        Graphics.initialSetup();
    }
}
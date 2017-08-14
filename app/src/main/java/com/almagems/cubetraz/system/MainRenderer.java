package com.almagems.cubetraz.system;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.os.SystemClock;
import android.widget.Toast;

import com.almagems.cubetraz.game.Engine;


public final class MainRenderer implements Renderer {

	private long frameStartTimeMS;
    public final Context context;
    public String sceneName;

	MainRenderer(Context context) {
        System.out.println("MainRenderer.ctor");
		this.context = context;
        Engine engine = Engine.getInstance();
        engine.setRenderer(this);


	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        System.out.println("MainRenderer.onSurfaceCreated");
        frameStartTimeMS = SystemClock.elapsedRealtime();

        Engine.createAndInitGraphicsObject(gl);

        try {
            Engine.graphics.loadStartupAssets();
        } catch (final Exception ex) {
            Activity activity = (Activity) context;
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(context, "Error loading assets. " + ex.toString(), Toast.LENGTH_LONG).show();
                }
            });
            return;
        }

        Engine.createGameObject();
        Engine.initGameObject();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Engine.onSurfaceChanged(width, height);
    }

	@Override
	public void onDrawFrame(GL10 gl) {
		Engine.update();
		Engine.draw();

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
		Engine.handleTouchPress(normalizedX, normalizedY, fingerCount);
	}
	
	void handleTouchDrag(float normalizedX, float normalizedY, int fingerCount) {
		Engine.handleTouchDrag(normalizedX, normalizedY, fingerCount);
	}
	
	void handleTouchRelease(float normalizedX, float normalizedY) {
		Engine.handleTouchRelease(normalizedX, normalizedY);
	}

}
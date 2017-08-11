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

    private boolean surfaceCreated;
    private int width;
    private int height;

	private long frameStartTimeMS;
	public final Context context;

	// ctor
	public MainRenderer(Context context) {
		this.context = context;
        surfaceCreated = false;
		Engine.activity = (MainActivity)context;
		Engine.renderer = this;
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //System.out.println("onSurfaceCreated...");
        surfaceCreated = true;
        width = -1;
        height = -1;
        frameStartTimeMS = SystemClock.elapsedRealtime();
    }

    private void onCreate(GL10 gl, int width, int height, boolean contextLost) {
        Engine.createGraphicsObject(gl);
        Engine.initGraphicsObject(width, height);

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
        if (!surfaceCreated && width == this.width && height == this.height) {
            return;
        }

        String msg = "Surface changed width: " + width + ", height: " + height;

        if (surfaceCreated) {
            msg += " (context lost)";
        } else {
            msg += ".";
        }

        //System.out.println(msg);

        this.width = width;
        this.height = height;

        onCreate(gl, width, height, surfaceCreated);
		Engine.onSurfaceChanged(width, height);
        surfaceCreated = false;
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

	public void handleTouchPress(float normalizedX, float normalizedY) {
		Engine.handleTouchPress(normalizedX, normalizedY);
	}
	
	public void handleTouchDrag(float normalizedX, float normalizedY) {
		Engine.handleTouchDrag(normalizedX, normalizedY);
	}
	
	public void handleTouchRelease(float normalizedX, float normalizedY) {
		Engine.handleTouchRelease(normalizedX, normalizedY);
	}

    public void handleBackButtonPress() {
        Engine.game.handleButton();
    }

    public void handleMenuButtonPress() {
        Engine.game.showMenu();
    }
}
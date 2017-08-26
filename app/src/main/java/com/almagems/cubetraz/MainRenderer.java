package com.almagems.cubetraz;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.os.SystemClock;
import android.widget.Toast;
import com.almagems.cubetraz.graphics.Graphics;


final class MainRenderer implements Renderer {

	private long frameStartTimeMS;
    final Context context;

	MainRenderer(Context context) {
        System.out.println("MainRenderer.ctor");
		this.context = context;
        Game.setRenderer(this);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        System.out.println("MainRenderer.onSurfaceCreated");
        frameStartTimeMS = SystemClock.elapsedRealtime();

        Graphics.gl = gl;
        Graphics.createBuffers();

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
}
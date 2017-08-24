package com.almagems.cubetraz;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.os.SystemClock;
import android.widget.Toast;

import com.almagems.cubetraz.Audio;
import com.almagems.cubetraz.Game;
import com.almagems.cubetraz.GameOptions;
import com.almagems.cubetraz.graphics.Graphics;


public final class MainRenderer implements Renderer {

	private long frameStartTimeMS;
    public final Context context;

    public static Audio audio;
    public static Graphics graphics;
    public static Game game;

	MainRenderer(Context context) {
        System.out.println("MainRenderer.ctor");
		this.context = context;

        if (game == null) {
            game = new Game();
            Game game = Game.getInstance();
            game.setRenderer(this);
        }
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        System.out.println("MainRenderer.onSurfaceCreated");
        frameStartTimeMS = SystemClock.elapsedRealtime();

        createAndInitGraphicsObject(gl);

        try {
            graphics.loadStartupAssets();
        } catch (final Exception ex) {
            Activity activity = (Activity) context;
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(context, "Error loading assets. " + ex.toString(), Toast.LENGTH_LONG).show();
                }
            });
            return;
        }

        game.init();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        graphics.onSurfaceChanged(width, height);
        game.onSurfaceChanged();
    }

	@Override
	public void onDrawFrame(GL10 gl) {
		game.update();
        graphics.prepareFrame();
        game.draw();

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
        game.handleTouchPress(normalizedX, normalizedY, fingerCount);
    }
	
	void handleTouchDrag(float normalizedX, float normalizedY, int fingerCount) {
        game.handleTouchDrag(normalizedX, normalizedY, fingerCount);
    }
	
	void handleTouchRelease(float normalizedX, float normalizedY) {
        game.handleTouchRelease(normalizedX, normalizedY);
    }

	void pause() {
        if (audio != null) {
            audio.release();
        }
        Game.fboLost = true;
    }

    void resume() {
        if (audio != null) {
            audio.reInit();
        }
    }

    void incrementSolverCount() {
        GameOptions options = Game.options;
        if (options != null) {
            int currentSolverCount = options.getSolverCount();
            options.setSolverCount(currentSolverCount + 1);
        }
        Game.updateDisplayedSolvers();
    }

    private void createAndInitGraphicsObject(GL10 gl) {
        if (graphics == null) {
            graphics = new Graphics(gl);
            graphics.initialSetup();
            Game.graphics = graphics;
        }
    }
}
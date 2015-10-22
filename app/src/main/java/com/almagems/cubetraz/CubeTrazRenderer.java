package com.almagems.cubetraz;

import static android.opengl.GLES10.*;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.os.SystemClock;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


public final class CubeTrazRenderer implements Renderer {

    private boolean surfaceCreated;
    private int width;
    private int height;

	private long frameStartTimeMS;
	public final Context context;

    private FloatBuffer floatBuffer;

	// ctor
	public CubeTrazRenderer(Context context) {
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

        float d = 0.5f;

        float[] vertices = {
             0f,   d, 0f,
             -d,  -d, 0f,
              d,  -d, 0f
        };

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());

        floatBuffer = vbb.asFloatBuffer();
        floatBuffer.put(vertices);
        floatBuffer.position(0);

        gl.glDisable(GL10.GL_LIGHTING);
        gl.glDisable(GL10.GL_CULL_FACE);
        gl.glDisable(GL10.GL_DEPTH_BUFFER_BIT);
        gl.glDisable(GL10.GL_DEPTH_TEST);
        gl.glShadeModel(GL10.GL_SMOOTH);
    }

    private void onCreate(GL10 gl, int width, int height, boolean contextLost) {
        Engine.createGraphicsObject(gl);
        Engine.initGraphicsObject(width, height);

        //gl.glViewport(0, 0, width, height);
        //gl.glClearColor(0f, 0f, 1f, 1f);

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

//        gl.glMatrixMode(GL10.GL_PROJECTION);
//        gl.glLoadIdentity();
//        gl.glOrthof(-1f, 1f, -1f, 1f, -1f, 1f);
//
//        gl.glMatrixMode(GL10.GL_MODELVIEW);
//        gl.glLoadIdentity();
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

//        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//
//        glPushMatrix();
//            glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
//            glEnableClientState(GL_VERTEX_ARRAY);
//            glVertexPointer(3, GL_FLOAT, 0, floatBuffer);
//            glDrawArrays(GL_TRIANGLES, 0, 3);
//            glDisableClientState(GL_VERTEX_ARRAY);
//        glPopMatrix();
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
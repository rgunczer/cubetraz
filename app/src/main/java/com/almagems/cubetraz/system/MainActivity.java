package com.almagems.cubetraz.system;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.almagems.cubetraz.game.Engine;


public final class MainActivity extends Activity {

    private GLSurfaceView glSurfaceView;
    private MainRenderer renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("MainActivity.onCreate");
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(1);
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 24, 8);

        setContentView(glSurfaceView);

        renderer = new MainRenderer(this);
        glSurfaceView.setRenderer(renderer);

        glSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, final MotionEvent event) {
                if (event != null) {
                    //System.out.println("Touch Event: " + event.getX() + ", " + event.getY() );
                    // convert touch coordinates into normalized device
                    // coordinates, keeping in mind that Android's Y coordinates are inverted
//                    final float normalizedX = (event.getX() / (float) v.getWidth()) * 2 - 1;
//                    final float normalizedY = -((event.getY() / (float) v.getHeight()) * 2 - 1);
//                    Engine.rawX = event.getX();
//                    Engine.rawY = event.getY();

                    final int action = event.getAction();
                    final float x = event.getX();
                    final float y = event.getY();
                    final int fingerCount = event.getPointerCount();

                    if (action == MotionEvent.ACTION_DOWN) {
                        glSurfaceView.queueEvent(new Runnable() {

                            @Override
                            public void run() {
                                renderer.handleTouchPress(x, y, fingerCount);
                            }
                        });
                    } else if (action == MotionEvent.ACTION_MOVE) {
                        glSurfaceView.queueEvent(new Runnable() {

                            @Override
                            public void run() {
                                renderer.handleTouchDrag(x, y, fingerCount);
                            }
                        });
                    } else if (action == MotionEvent.ACTION_UP) {
                        glSurfaceView.queueEvent(new Runnable() {

                            @Override
                            public void run() {
                                renderer.handleTouchRelease(x, y);
                            }
                        });
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onPause() {
        System.out.println("MainActivity.onPause");
        super.onPause();

        if (renderer != null) {
            glSurfaceView.onPause();
        }

        Engine.pause();
    }

    @Override
    protected void onResume() {
        System.out.println("MainActivity.onResume");
        super.onResume();

        if (renderer != null) {
            glSurfaceView.onResume();
        }

        Engine.resume();

    }

}

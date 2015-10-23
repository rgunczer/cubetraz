package com.almagems.cubetraz;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


public final class MainActivity extends Activity {

    private GLSurfaceView glSurfaceView;
    private CubeTrazRenderer renderer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        glSurfaceView = new GLSurfaceView(this);
        setContentView(glSurfaceView);

        renderer = new CubeTrazRenderer(this);
        glSurfaceView.setEGLContextClientVersion(1);
        glSurfaceView.setRenderer(renderer);

        glSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, final MotionEvent event) {
                if (event != null) {
                    //System.out.println("Touch Event: " + event.getX() + ", " + event.getY() );
                    // convert touch coordinates into normalized device
                    // coordinates, keeping in mind that Android's Y coordinates are inverted
                    final float normalizedX = (event.getX() / (float) v.getWidth()) * 2 - 1;
                    final float normalizedY = -((event.getY() / (float) v.getHeight()) * 2 - 1);
                    Engine.rawX = event.getX();
                    Engine.rawY = event.getY();

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        glSurfaceView.queueEvent(new Runnable() {

                            @Override
                            public void run() {
                                //renderer.handleTouchPress(normalizedX, normalizedY);
                                renderer.handleTouchPress(Engine.rawX, Engine.rawY);
                            }
                        });
                    } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        glSurfaceView.queueEvent(new Runnable() {

                            @Override
                            public void run() {
                                //renderer.handleTouchDrag(normalizedX, normalizedY);
                                renderer.handleTouchDrag(Engine.rawX, Engine.rawY);
                            }
                        });
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        glSurfaceView.queueEvent(new Runnable() {

                            @Override
                            public void run() {
                                //renderer.handleTouchRelease(normalizedX, normalizedY);
                                renderer.handleTouchRelease(Engine.rawX, Engine.rawY);
                            }
                        });
                    }
                    return true;
                }
                return false;
            }
        });
    }
}

package com.almagems.cubetraz;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;


public final class MainActivity extends Activity {
    private GLSurfaceView glSurfaceView;
    private MainRenderer renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("MainActivity.onCreate");
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.layout);

        glSurfaceView = (GLSurfaceView)findViewById(R.id.surfaceView);

        //glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(1);
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 24, 8);
        //setContentView(glSurfaceView);

        renderer = new MainRenderer(this);
        glSurfaceView.setRenderer(renderer);

        glSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, final MotionEvent event) {
                if (event != null) {
                    final int action = event.getAction();
                    final float x = event.getX();
                    final float y = event.getY();
                    final int fingerCount = event.getPointerCount();

                    if (action == MotionEvent.ACTION_DOWN) {
                        glSurfaceView.queueEvent(new Runnable() {

                            @Override
                            public void run() {
                                Game.handleTouchPress(x, y, fingerCount);
                            }
                        });
                    } else if (action == MotionEvent.ACTION_MOVE) {
                        glSurfaceView.queueEvent(new Runnable() {

                            @Override
                            public void run() {
                                Game.handleTouchDrag(x, y, fingerCount);
                            }
                        });
                    } else if (action == MotionEvent.ACTION_UP) {
                        glSurfaceView.queueEvent(new Runnable() {

                            @Override
                            public void run() {
                                Game.handleTouchRelease(x, y);
                            }
                        });
                    }
                    return true;
                }
                return false;
            }
        });
        loadRewardedVideoAd();
    }

    @Override
    protected void onPause() {
        System.out.println("MainActivity.onPause");
        super.onPause();

        if (renderer != null) {
            glSurfaceView.onPause();
            Audio.release();
            Game.fboLost = true;
        }
    }

    @Override
    protected void onResume() {
        System.out.println("MainActivity.onResume");
        super.onResume();

        if (renderer != null) {
            glSurfaceView.onResume();
            Audio.reInit();
        }
    }

    public void showAd() {
        // do nothing
    }

    public void showAdNotReady() {
        // do nothing
    }

    public void showText(final String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void loadRewardedVideoAd() {
        // do nothing
    }

}

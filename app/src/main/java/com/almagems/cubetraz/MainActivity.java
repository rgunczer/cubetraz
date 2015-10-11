package com.almagems.cubetraz;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public final class MainActivity extends Activity { //AppCompatActivity {

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
    }
}

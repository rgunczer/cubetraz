package com.almagems.cubetraz.scenes;

import com.almagems.cubetraz.graphics.Camera;
import com.almagems.cubetraz.math.Vector;
import com.almagems.cubetraz.math.Vector2;

import static com.almagems.cubetraz.Game.*;

public abstract class Scene {

    int mTick = 0;
    protected float mElapsed;

    protected Camera mCameraCurrent = new Camera();

    protected Vector mPosLight = new Vector();
    protected Vector mPosLightCurrent = new Vector();

    protected Vector2 mPosDown = new Vector2();
    protected Vector2 mPosMove = new Vector2();
    protected Vector2 mPosUp = new Vector2();

    protected boolean mIsFingerDown;
    protected boolean mIsSwipe;


    public abstract void init();
    public abstract void update();
    public abstract void render();
    public void renderToFBO() {}

    // input
    public void onFingerDown(float x, float y, int fingerCount) {}
    public void onFingerUp(float x, float y, int fingerCount) {}
    public void onFingerMove(float prevX, float prevY, float curX, float curY, int fingerCount) {}

    public Camera getCameraCurrent() { return mCameraCurrent; }
    public Vector getLightPositionCurrent() { return mPosLightCurrent; }

}

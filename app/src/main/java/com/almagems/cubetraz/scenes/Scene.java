package com.almagems.cubetraz.scenes;

import com.almagems.cubetraz.graphics.Camera;
import com.almagems.cubetraz.graphics.Graphics;
import com.almagems.cubetraz.math.Vector2;

import static com.almagems.cubetraz.game.Constants.*;


public abstract class Scene {

    protected int tick = 0;

    public static Graphics graphics;

    protected Camera mCameraCurrent = new Camera();

    protected Vector2 mPosDown = new Vector2();
    protected Vector2 mPosMove = new Vector2();
    protected Vector2 mPosUp = new Vector2();

    protected boolean mIsFingerDown;
    protected boolean mIsSwipe;

    public abstract void init();
    public abstract void update();
    public abstract void render();
    public void renderForPicking() {}
    public void renderToFBO() {}

    // input
    public void onFingerDown(float x, float y, int finger_count) {}
    public void onFingerUp(float x, float y, int finger_count) {}
    public void onFingerMove(float prev_x, float prev_y, float cur_x, float cur_y, int finger_count) {}

    public void onSwipe(SwipeDirEnums swipeDir) {}

    public void EnteredBackground() {}
    public void EnteredForeground() {}
    public void SetupCameras() {}


    public Camera getCameraCurrent() { return mCameraCurrent; }

}

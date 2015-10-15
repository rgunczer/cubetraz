package com.almagems.cubetraz;


public abstract class Scene {

    protected Vector2 mPosDown;
    protected Vector2 mPosMove;
    protected Vector2 mPosUp;

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

    public void onSwipe() {}

    public void EnteredBackground() {}
    public void EnteredForeground() {}
    public void SetupCameras() {}

}

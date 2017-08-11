package com.almagems.cubetraz.game;

import com.almagems.cubetraz.graphics.Graphics;
import com.almagems.cubetraz.math.Vector2;
import com.almagems.cubetraz.system.MainRenderer;
import com.almagems.cubetraz.system.MainActivity;
import com.almagems.cubetraz.utils.SwipeInfo;

import static com.almagems.cubetraz.game.Constants.*;
import javax.microedition.khronos.opengles.GL10;


public final class Engine {

    public static float rawX;
    public static float rawY;

    public static SwipeInfo getSwipeDirAndLength(Vector2 pos_down, Vector2 pos_up) {
        SwipeInfo swipeInfo = new SwipeInfo();

        swipeInfo.swipeDir = SwipeDirEnums.SwipeNone;

        float up_x = pos_up.x;
        float up_y = pos_up.y;

        float down_x = pos_down.x;
        float down_y = pos_down.y;

        up_y = -up_y;
        down_y = -down_y;

        Vector2 dir = new Vector2();
        dir.x = up_x - down_x;
        dir.y = up_y - down_y;

        swipeInfo.length = dir.len();

        float diff_x = pos_up.x - pos_down.x;
        float diff_y = pos_up.y - pos_down.y;

//    printf("\ndiff x:%.2f, y:%.2f", diff_x, diff_y);

        if ( Math.abs(diff_x) > Math.abs(diff_y) ) {
            if (diff_x < 0.0f) {
//            printf("\nSwipe Down");
                swipeInfo.swipeDir = SwipeDirEnums.SwipeLeft; // SwipeDown;
            }

            if (diff_x > 0.0f) {
//            printf("\nSwipe Up");
                swipeInfo.swipeDir = SwipeDirEnums.SwipeRight; // SwipeUp;
            }
        } else {
            if (diff_y < 0.0f) {
//            printf("\nSwipe Left");
                swipeInfo.swipeDir = SwipeDirEnums.SwipeUp;
            }

            if (diff_y > 0.0f) {
//            printf("\nSwipe Right");
                swipeInfo.swipeDir = SwipeDirEnums.SwipeDown;
            }
        }
        return swipeInfo;
    }

    // android
    public static MainRenderer renderer;
    public static MainActivity activity;

    // basic
    public static Graphics graphics;
    //public static Audio audio;
    public static Game game;




    // ctor
	private Engine() {
        //System.out.println("Engine ctor...");
    }

    public static void createGraphicsObject(GL10 gl) {
        //System.out.println("Engine createGraphicsObject...");
        graphics = new Graphics(activity, gl);
    }

    public static void initGraphicsObject(int width, int height) {
        //System.out.println("Engine initGraphicsObject...");
        graphics.initialSetup(width, height);
    }

    public static void createGameObject() {        
        //System.out.println("Engine createGameObject...");
        game = new Game();
    }

    public static void initGameObject() {
       game.init();
    }

    public static void pause() {
//        if (audio != null) {
//            audio.pause();
//        }
    }

    public static void resume() {
//        if (audio != null) {
//            audio.resume();
//        }
    }

    public static void releaseAudio() {
//        if (audio != null) {
//            audio.release();
//        }
    }

    public static void createAudio() {
//        if (audio != null) {
//            audio.create();
//        }
    }

    public static void update() {
        game.update();
    }

    public static void draw() {
        graphics.prepareFrame();
        game.draw();
    }

    public static void onSurfaceChanged(int width, int height) {
        graphics.onSurfaceChanged(width, height);
        game.onSurfaceChanged(width, height);
    }

    public static void showInterstitialAd() {
        //activity.requestNewInterstitial();
    }

    // input
    public static void handleTouchPress(float normalizedX, float normalizedY) {
        game.handleTouchPress(normalizedX, normalizedY);
    }

    public static void handleTouchDrag(float normalizedX, float normalizedY) {
        game.handleTouchDrag(normalizedX, normalizedY);
    }

    public static void handleTouchRelease(float normalizedX, float normalizedY) {
        game.handleTouchRelease(normalizedX, normalizedY);
    }

}

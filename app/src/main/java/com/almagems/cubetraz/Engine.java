package com.almagems.cubetraz;

import static com.almagems.cubetraz.Constants.*;
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
    public static CubeTrazRenderer renderer;
    public static MainActivity activity;

    // basic
    public static Graphics graphics;
    //public static Audio audio;
    public static Game game;


    private static float musicVolume = 0.5f;
    private static float soundVolume = 0.5f;

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

    private static SecurePreferences CreateSecurePreferencesObj() {
        String name = "ctraz0";
        String key = "jKDz2fJhKE33cBQlDAkRM9axAnrBmUxs9dp11AqoogfulCL2DvNsexBuL2qeEstAIc27bqiMZbOOxZbZ";
        key = key.substring(10, 20) + key.substring(21, 26);

        return new SecurePreferences(renderer.context, name, key, true);
    }

    public static void savePreferences() {
        SecurePreferences preferences = CreateSecurePreferencesObj();

        // Put (all puts are automatically committed)
        //preferences.put("userId", "User1234");

        // save music and sound volume
        preferences.put("MUSIC", "" + musicVolume);
        preferences.put("SOUND", "" + soundVolume);
    }

    private static int getPref(SecurePreferences preferences, String key, int defaultValue) {
        String valueString = preferences.getString(key);
        if (valueString != null) {
            return Integer.parseInt(valueString);
        }
        return defaultValue;
    }

    private static float getPref(SecurePreferences preferences, String key, float defaultValue) {
        String valueString = preferences.getString(key);
        if (valueString != null) {
            return Float.parseFloat(valueString);
        }
        return defaultValue;
    }

    public static void loadPreferences() {
        SecurePreferences preferences = CreateSecurePreferencesObj();

        musicVolume = getPref(preferences, "MUSIC", 0.5f);
        soundVolume = getPref(preferences, "SOUND", 0.5f);
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

    public static float getMusicVolume() {
        return musicVolume;
    }

    public static float getSoundVolume() {
        return soundVolume;
    }

    public static void setSoundVolume(float volume) {
        soundVolume = volume;
    }

    public static void setMusicVolume(float volume) {
        musicVolume = volume;
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

package com.almagems.cubetraz;

import com.almagems.cubetraz.external.SecurePreferences;

public class GameOptions {

    private static String keyMusic = "MUSIC";
    private static String keySound = "SOUND";
    private static String keyCanSkipIntro = "INTRO";
    private static String keySolverCount = "SOLVER";

    private static float mMusicVolume = 0.5f;
    private static float mSoundVolume = 0.5f;
    private static boolean mCanSkipIntro = false;
    private static int mSolverCount = 6;

    public static float getMusicVolume() {
        return mMusicVolume;
    }
    public static float getSoundVolume() {
        return mSoundVolume;
    }

    public static void setSoundVolume(float volume) {
        mSoundVolume = volume;
        save();
    }
    public static void setMusicVolume(float volume) {
        mMusicVolume = volume;
        save();
    }

    public static boolean getCanSkipIntro() { return mCanSkipIntro; }
    //public static boolean getCanSkipIntro() { return true; } // TODO: remove this in final version
    public static void setCanSkipIntro(boolean value) {
        mCanSkipIntro = value;
        save();
    }

    public static void setSolverCount(int solverCount) {
        mSolverCount = solverCount;
        save();
    }

    public static int getSolverCount() {
        //return 180; // TODO: remove
        return mSolverCount;
    }

    private GameOptions() {
    }

    private static SecurePreferences createSecurePreferencesObj() {
        String name = "ctraz0";
        String key = "jKDz2fJhKE33cBQlDAkRM9axAnrBmUxs9dp11AqoogfulCL2DvNsexBuL2qeEstAIc27bqiMZbOOxZbZ";
        key = key.substring(10, 20) + key.substring(21, 26);

        return new SecurePreferences(Game.getContext(), name, key, true);
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

    public static void save() {
        SecurePreferences preferences = createSecurePreferencesObj();

        // Put (all puts are automatically committed)
        //preferences.put("userId", "User1234");

        preferences.put(keyMusic, "" + mMusicVolume);
        preferences.put(keySound, "" + mSoundVolume);
        preferences.put(keyCanSkipIntro, "" + (mCanSkipIntro ? 1 : 0));
        preferences.put(keySolverCount, "" + mSolverCount);
    }

    public static void load() {
        SecurePreferences preferences = createSecurePreferencesObj();

        mMusicVolume = getPref(preferences, keyMusic, 0.5f);
        mSoundVolume = getPref(preferences, keySound, 0.5f);

        int value = getPref(preferences, keyCanSkipIntro, 0);
        mCanSkipIntro = value != 0;

        value = getPref(preferences, keySolverCount, 6);
        mSolverCount = value;
    }

}
package com.almagems.cubetraz;

import com.almagems.cubetraz.external.SecurePreferences;

public class GameOptions {

    private static String keyMusic = "MUSIC";
    private static String keySound = "SOUND";
    private static String keyCanSkipIntro = "INTRO";
    private static String keySolverCount = "SOLVER";

    private float mMusicVolume = 0.5f;
    private float mSoundVolume = 0.5f;
    private boolean mCanSkipIntro = false;
    private int mSolverCount = 9;

    public float getMusicVolume() {
        return mMusicVolume;
    }
    public float getSoundVolume() {
        return mSoundVolume;
    }

    public void setSoundVolume(float volume) {
        mSoundVolume = volume;
        save();
    }
    public void setMusicVolume(float volume) {
        mMusicVolume = volume;
        save();
    }

    public boolean getCanSkipIntro() { return mCanSkipIntro; }
    //public boolean getCanSkipIntro() { return true; } // TODO: remove this in final version
    public void setCanSkipIntro(boolean value) {
        mCanSkipIntro = value;
        save();
    }

    public void setSolverCount(int solverCount) {
        mSolverCount = solverCount;
        save();
    }

    public int getSolverCount() {
        return 180; // TODO: remove
        //return mSolverCount;
    }

    public GameOptions() {
    }

    private static SecurePreferences CreateSecurePreferencesObj() {
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

    public void save() {
        SecurePreferences preferences = CreateSecurePreferencesObj();

        // Put (all puts are automatically committed)
        //preferences.put("userId", "User1234");

        preferences.put(keyMusic, "" + mMusicVolume);
        preferences.put(keySound, "" + mSoundVolume);
        preferences.put(keyCanSkipIntro, "" + (mCanSkipIntro ? 1 : 0));
        preferences.put(keySolverCount, "" + mSolverCount);
    }

    public void load() {
        SecurePreferences preferences = CreateSecurePreferencesObj();

        mMusicVolume = getPref(preferences, keyMusic, 0.5f);
        mSoundVolume = getPref(preferences, keySound, 0.5f);

        int value = getPref(preferences, keyCanSkipIntro, 0);
        mCanSkipIntro = value != 0;

        value = getPref(preferences, keySolverCount, 9);
        mSolverCount = value;
    }

}

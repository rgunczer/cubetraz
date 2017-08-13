package com.almagems.cubetraz.game;

import com.almagems.cubetraz.external.SecurePreferences;

public class GameOptions {

    //private static final String SAVE_OPTIONS_FILE = "options.dat";

    private static String keyMusic = "MUSIC";
    private static String keySound = "SOUND";
    private static String keyCanSkipIntro = "INTRO";

    private float mMusicVolume = 0.5f;
    private float mSoundVolume = 0.5f;
    private boolean mCanSkipIntro = false;

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

    //public boolean getCanSkipIntro() { return mCanSkipIntro; }
    public boolean getCanSkipIntro() { return true; } // TODO: remove this in final version
    public void setCanSkipIntro(boolean value) {
        mCanSkipIntro = value;
        save();
    }

    public GameOptions() {
    }

    private static SecurePreferences CreateSecurePreferencesObj() {
        String name = "ctraz0";
        String key = "jKDz2fJhKE33cBQlDAkRM9axAnrBmUxs9dp11AqoogfulCL2DvNsexBuL2qeEstAIc27bqiMZbOOxZbZ";
        key = key.substring(10, 20) + key.substring(21, 26);

        return new SecurePreferences(Engine.getContext(), name, key, true);
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
    }

    public void load() {
        SecurePreferences preferences = CreateSecurePreferencesObj();

        mMusicVolume = getPref(preferences, keyMusic, 0.5f);
        mSoundVolume = getPref(preferences, keySound, 0.5f);
        int value = getPref(preferences, keyCanSkipIntro, 0);
        mCanSkipIntro = value != 0;
    }

}

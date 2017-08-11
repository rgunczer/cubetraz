package com.almagems.cubetraz.game;


import android.content.Context;

import com.almagems.cubetraz.external.SecurePreferences;

public class GameOptions {

    private Context _context;

    //private static final String SAVE_OPTIONS_FILE = "options.dat";

    private static String keyMusic = "MUSIC";
    private static String keySound = "SOUND";
    private static String keyCanSkipIntro = "INTRO";

    private float _musicVolume = 0.5f;
    private float _soundVolume = 0.5f;
    private boolean _canSkipIntro = false;

    public float getMusicVolume() {
        return _musicVolume;
    }
    public float getSoundVolume() {
        return _soundVolume;
    }

    public void setSoundVolume(float volume) {
        _soundVolume = volume;
        save();
    }
    public void setMusicVolume(float volume) {
        _musicVolume = volume;
        save();
    }

    public boolean getCanSkipIntro() { return _canSkipIntro; }
    public void setCanSkipIntro(boolean value) {
        _canSkipIntro = value;
        save();
    }

    public GameOptions(Context context) {
        _context = context;
    }

    private static SecurePreferences CreateSecurePreferencesObj(Context context) {
        String name = "ctraz0";
        String key = "jKDz2fJhKE33cBQlDAkRM9axAnrBmUxs9dp11AqoogfulCL2DvNsexBuL2qeEstAIc27bqiMZbOOxZbZ";
        key = key.substring(10, 20) + key.substring(21, 26);

        return new SecurePreferences(context, name, key, true);
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
        SecurePreferences preferences = CreateSecurePreferencesObj(_context);

        // Put (all puts are automatically committed)
        //preferences.put("userId", "User1234");

        preferences.put(keyMusic, "" + _musicVolume);
        preferences.put(keySound, "" + _soundVolume);
        preferences.put(keyCanSkipIntro, "" + (_canSkipIntro ? 1 : 0));
    }

    public void load() {
        SecurePreferences preferences = CreateSecurePreferencesObj(_context);

        _musicVolume = getPref(preferences, keyMusic, 0.5f);
        _soundVolume = getPref(preferences, keySound, 0.5f);
        int value = getPref(preferences, keyCanSkipIntro, 0);
        _canSkipIntro = value != 0;
    }

}

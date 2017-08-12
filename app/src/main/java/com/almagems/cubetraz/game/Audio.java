package com.almagems.cubetraz.game;

import com.almagems.cubetraz.R;

import java.util.HashMap;
import java.util.Map;

import static com.almagems.cubetraz.game.Constants.*;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;



public final class Audio {

    private Context mContext;

    // music
    public static final String MUSIC_CPU = "POL-a-cpu-life-short";
    public static final String MUSIC_BREEZE = "POL-breeze-short";
    public static final String MUSIC_DRONES = "POL-positive-drones-short";
    public static final String MUSIC_VECTORS = "POL-vectors-short";
    public static final String MUSIC_WAVES = "POL-warm-waves-short";

    // sound
    public static final String SOUND_LEVEL_COMPLETED = "keys";
    public static final String SOUND_VOLUME_UP = "NFF-switch-on";
    public static final String SOUND_VOLUME_DOWN = "NFF-switch-off";
    public static final String SOUND_CUBE_HIT = "cube_to_cube";
    public static final String SOUND_TAP_ON_LEVEL_CUBE = "open";
    public static final String SOUND_TAP_ON_LOCKED_LEVEL_CUBE = "locked";

    private Map<String, String> mResources = new HashMap<>();
    private Map<String, Integer> mSounds = new HashMap<>();

    public float musicVolume = 0.5f;
    public float soundVolume = 0.5f;

    private MediaPlayer mediaPlayer;
    private SoundPool soundPool;

    private int soundStreamId;
    private int musicTrackPos = 0;


    // ctor
    public Audio() {
        mResources.clear();

        // add music
        mResources.put(MUSIC_CPU, "" + R.raw.pol_a_cpu_life_short);
        mResources.put(MUSIC_BREEZE, "" + R.raw.pol_breeze_short);
        mResources.put(MUSIC_DRONES, "" + R.raw.pol_positive_drones_short);
        mResources.put(MUSIC_VECTORS, "" + R.raw.pol_vectors_short);
        mResources.put(MUSIC_WAVES, "" + R.raw.pol_warm_waves_short);

        // add sound
        mResources.put(SOUND_LEVEL_COMPLETED, "" + R.raw.keys); // "keys"
        mResources.put(SOUND_VOLUME_DOWN, "" + R.raw.nff_switch_off); // "NFF-switch-off"
        mResources.put(SOUND_VOLUME_UP, "" + R.raw.nff_switch_on);
        mResources.put(SOUND_CUBE_HIT, "" + R.raw.cube_to_cube); // "cube_to_cube"
        mResources.put(SOUND_TAP_ON_LEVEL_CUBE, "" + R.raw.open); // "open"
        mResources.put(SOUND_TAP_ON_LOCKED_LEVEL_CUBE, "" + R.raw.locked); // "locked"

        mSounds.clear();
    }

    public void setMusicVolume(float value) {
        if (value != musicVolume) {
            musicVolume = value;
            playMusic(null);
        }
    }

    public void setSoundVolume(float value) {
        if (value != soundVolume) {
            soundVolume = value;
            soundPool.setVolume(soundStreamId, soundVolume, soundVolume);
            playSound(null);
        }
    }

    private int getResourceIdFromKey(String key) {
        String stringValue = mResources.get(key);
        int resourceId = Integer.parseInt(stringValue);
        return resourceId;
    }

    private void createMediaPlayer(String key) {
        int resourceId = getResourceIdFromKey(key);
        mediaPlayer = MediaPlayer.create(mContext, resourceId);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    private void destroyMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void loadToSoundPool(String key) {
        int resourceId = Integer.parseInt(mResources.get(key));
        int id = soundPool.load(mContext, resourceId, 1);
        mSounds.put(key, id);
    }

    private void createSoundPool() {
        soundPool = new SoundPool(12, AudioManager.STREAM_MUSIC, 0);

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                System.out.println("loadComplete: " + sampleId);
            }
        });

        loadToSoundPool(SOUND_CUBE_HIT);
        loadToSoundPool(SOUND_LEVEL_COMPLETED);
        loadToSoundPool(SOUND_TAP_ON_LEVEL_CUBE);
        loadToSoundPool(SOUND_TAP_ON_LOCKED_LEVEL_CUBE);
        loadToSoundPool(SOUND_VOLUME_DOWN);
        loadToSoundPool(SOUND_VOLUME_UP);
    }

    public void init(Context context, float musicVolume, float soundVolume) {
        mContext = context;
        this.musicVolume = musicVolume;
        this.soundVolume = soundVolume;

        createSoundPool();
    }

    public void playMusic(String key) {
        if (key != null) {
            createMediaPlayer(key);
        }

        if (mediaPlayer != null) {

            if (mediaPlayer.isPlaying()) {
                mediaPlayer.setVolume(musicVolume, musicVolume);
            } else {
                mediaPlayer.setLooping(true);
                mediaPlayer.setVolume(musicVolume, musicVolume);
                mediaPlayer.seekTo(musicTrackPos);
                mediaPlayer.start();
            }

        }
    }

    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            destroyMediaPlayer();
        }
    }

    public void playSound(String key) {
        int loopMode = 0; // (0 = no loop, -1 = loop forever)

        int soundPoolId = mSounds.get(key);
        soundStreamId = soundPool.play(soundPoolId, soundVolume, soundVolume, 1, loopMode, 1.0f);
    }

    public void stopSound(){
        soundPool.stop(soundStreamId);
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void resume() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    public void release() {
        if (soundPool != null ) {
            soundPool.release();
            soundPool = null;
        }

        if (mediaPlayer != null) {
            musicTrackPos = mediaPlayer.getCurrentPosition();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}

package com.almagems.cubetraz;

import java.util.HashMap;
import java.util.Map;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public final class Audio {

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

    private static Map<String, String> resources = new HashMap<>();
    private static Map<String, Integer> sounds = new HashMap<>();

    private static float musicVolume = 0.5f;
    private static float soundVolume = 0.5f;

    private static MediaPlayer mediaPlayer;
    private static SoundPool soundPool;

    private static int soundStreamId;
    private static int musicTrackPos = 0;
    private static String currentMusicKey;

    static {
        resources.clear();

        // add music
        resources.put(MUSIC_CPU, "" + R.raw.pol_a_cpu_life_short);
        resources.put(MUSIC_BREEZE, "" + R.raw.pol_breeze_short);
        resources.put(MUSIC_DRONES, "" + R.raw.pol_positive_drones_short);
        resources.put(MUSIC_VECTORS, "" + R.raw.pol_vectors_short);
        resources.put(MUSIC_WAVES, "" + R.raw.pol_warm_waves_short);

        // add sound
        resources.put(SOUND_LEVEL_COMPLETED, "" + R.raw.keys); // "keys"
        resources.put(SOUND_VOLUME_DOWN, "" + R.raw.nff_switch_off); // "NFF-switch-off"
        resources.put(SOUND_VOLUME_UP, "" + R.raw.nff_switch_on);
        resources.put(SOUND_CUBE_HIT, "" + R.raw.cube_to_cube); // "cube_to_cube"
        resources.put(SOUND_TAP_ON_LEVEL_CUBE, "" + R.raw.open); // "open"
        resources.put(SOUND_TAP_ON_LOCKED_LEVEL_CUBE, "" + R.raw.locked); // "locked"

        sounds.clear();
    }

    private Audio() {
    }

    public static void setMusicVolume(float value) {
        if (value != musicVolume) {
            musicVolume = value;
            playMusic(null);
        }
    }

    public static void setSoundVolume(float value) {
        if (value != soundVolume) {
            soundVolume = value;
            soundPool.setVolume(soundStreamId, soundVolume, soundVolume);
            playSound(SOUND_VOLUME_DOWN);
        }
    }

    private static int getResourceIdFromKey(String key) {
        String stringValue = resources.get(key);
        int resourceId = Integer.parseInt(stringValue);
        return resourceId;
    }

    private static void createMediaPlayer(String key) {
        int resourceId = getResourceIdFromKey(key);
        destroyMediaPlayer();
        mediaPlayer = MediaPlayer.create(Game.getContext(), resourceId);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    private static void destroyMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private static void loadToSoundPool(String key) {
        int resourceId = Integer.parseInt(resources.get(key));
        int id = soundPool.load(Game.getContext(), resourceId, 1);
        sounds.put(key, id);
    }

    private static void createSoundPool() {
        soundPool = new SoundPool(12, AudioManager.STREAM_MUSIC, 0);

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                //System.out.println("soundPool loadComplete: " + sampleId);
            }
        });

        loadToSoundPool(SOUND_CUBE_HIT);
        loadToSoundPool(SOUND_LEVEL_COMPLETED);
        loadToSoundPool(SOUND_TAP_ON_LEVEL_CUBE);
        loadToSoundPool(SOUND_TAP_ON_LOCKED_LEVEL_CUBE);
        loadToSoundPool(SOUND_VOLUME_DOWN);
        loadToSoundPool(SOUND_VOLUME_UP);
    }

    public static void init(float musicVolume, float soundVolume) {
        Audio.musicVolume = musicVolume;
        Audio.soundVolume = soundVolume;

        createSoundPool();
    }

     static void reInit() {
        init(musicVolume, soundVolume);
        if (currentMusicKey != null) {
            createMediaPlayer(currentMusicKey);
            mediaPlayer.setVolume(musicVolume, musicVolume);
            mediaPlayer.setLooping(true);
            mediaPlayer.seekTo(musicTrackPos);
            mediaPlayer.start();
        }
    }

    public static void playMusic(String key) {
        if (key != null) {
            currentMusicKey = key;
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

    public static void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            destroyMediaPlayer();
        }
    }

    public static void playSound(String key) {
        int loopMode = 0; // (0 = no loop, -1 = loop forever)

        int soundPoolId = sounds.get(key);
        soundStreamId = soundPool.play(soundPoolId, soundVolume, soundVolume, 1, loopMode, 1.0f);
    }

    public static void stopSound(){
        soundPool.stop(soundStreamId);
    }

    public static void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public static void resume() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    public static void release() {
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

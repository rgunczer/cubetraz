package com.almagems.cubetraz;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public final class GameProgress {

    private static final int MAX_LEVELS = 61;

    private static final String SAVE_GAME_FILE_EASY = "easy_game.dat";
    private static final String SAVE_GAME_FILE_NORMAL = "normal_game.dat";
    private static final String SAVE_GAME_FILE_HARD = "hard_game.dat";

    private final LevelData[] mLevelsEasy = new LevelData[MAX_LEVELS];
    private final LevelData[] mLevelsNormal = new LevelData[MAX_LEVELS];
    private final LevelData[] mLevelsHard = new LevelData[MAX_LEVELS];

    public int getStarsEasy(final int levelNumber) { return mLevelsEasy[levelNumber - 1].stars; }
    public int getStarsNormal(final int levelNumber) { return mLevelsNormal[levelNumber - 1].stars; }
    public int getStarsHard(final int levelNumber) { return mLevelsHard[levelNumber - 1].stars; }

    public boolean isSolvedEasy(final int levelNumber) { return mLevelsEasy[levelNumber - 1].solved; }
    public boolean isSolvedNormal(final int levelNumber) { return mLevelsNormal[levelNumber - 1].solved; }
    public boolean isSolvedHard(final int levelNumber) { return mLevelsHard[levelNumber - 1].solved; }

    public GameProgress() {
    }

    public void setStarsEasy(final int levelNumber, final int stars) {
        int index = levelNumber - 1;

        if (stars > mLevelsEasy[index].stars) {
            mLevelsEasy[index].stars = stars;
        }
    }

    public void setMovesEasy(final int levelNumber, final int moves) { mLevelsEasy[levelNumber - 1].moves = moves; }
    public void setSolvedEasy(final int levelNumber, final boolean solved) { mLevelsEasy[levelNumber - 1].solved = solved; }

    public void setStarsNormal(final int levelNumber, final int stars) {
        int index = levelNumber - 1;

        if (stars > mLevelsNormal[index].stars) {
            mLevelsNormal[index].stars = stars;
        }
    }

    public void setMovesNormal(final int levelNumber, final int moves) { mLevelsNormal[levelNumber - 1].moves = moves; }
    public void setSolvedNormal(final int levelNumber, final boolean solved) { mLevelsNormal[levelNumber - 1].solved = solved; }

    public void setStarsHard(final int levelNumber, final int stars) {
        int index = levelNumber - 1;

        if (stars > mLevelsHard[index].stars) {
            mLevelsHard[index].stars = stars;
        }
    }

    public void setMovesHard(final int levelNumber, final int moves) { mLevelsHard[levelNumber - 1].moves = moves; }
    public void setSolvedHard(final int levelNumber, final boolean solved) { mLevelsHard[levelNumber - 1].solved = solved; }

    private void saveArray(String fileName, LevelData[] array) {
        try {
            FileOutputStream fos = Game.getContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(array);

            oos.flush();
            fos.getFD().sync();
            fos.close();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public void save() {
        saveArray(SAVE_GAME_FILE_EASY, mLevelsEasy);
        saveArray(SAVE_GAME_FILE_NORMAL, mLevelsNormal);
        saveArray(SAVE_GAME_FILE_HARD, mLevelsHard);
    }

    private LevelData[] loadArray(String fileName) {
        LevelData[] ar = new LevelData[MAX_LEVELS];

        for (int i = 0; i < MAX_LEVELS; ++i) {
            ar[i] = new LevelData();
            ar[i].reset();
        }

        if (fileName.equals(SAVE_GAME_FILE_EASY)) {
            ar[0].stars = Game.LEVEL_UNLOCKED; // unlock first level on easy face
        }

        try {
            FileInputStream fis = Game.getContext().openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object obj = ois.readObject();
            ar = (LevelData[])(obj);
            fis.close();
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
        return ar;
    }

    private void copyArray(LevelData[] from, LevelData[] to) {
        //System.arraycopy(from, 0, to, 0, from.length);
        for(int i = 0; i < from.length; ++i) {
            to[i] = from[i];
        }
    }

    public void load() {
        LevelData[] temp;

        temp = loadArray(SAVE_GAME_FILE_EASY);
        copyArray(temp, mLevelsEasy);

        temp = loadArray(SAVE_GAME_FILE_NORMAL);
        copyArray(temp, mLevelsNormal);

        temp = loadArray(SAVE_GAME_FILE_HARD);
        copyArray(temp, mLevelsHard);
    }

    public int getStarCount() {
        int stars = 0;
        for (int i = 0; i < MAX_LEVELS - 1; ++i) {
            if (mLevelsEasy[i].stars > 0) {
                stars += mLevelsEasy[i].stars;
            }

            if (mLevelsNormal[i].stars > 0) {
                stars += mLevelsNormal[i].stars;
            }

            if (mLevelsHard[i].stars > 0) {
                stars += mLevelsHard[i].stars;
            }
        }
        return stars;
    }

    public int getSolvedLevelCount() {
        int solved = 0;
        for(int i = 0; i < MAX_LEVELS - 1; ++i) {
            if (mLevelsEasy[i].stars > 0) {
                ++solved;
            }
            if (mLevelsNormal[i].stars > 0) {
                ++solved;
            }
            if (mLevelsHard[i].stars > 0) {
                ++solved;
            }
        }
        return solved;
    }

}

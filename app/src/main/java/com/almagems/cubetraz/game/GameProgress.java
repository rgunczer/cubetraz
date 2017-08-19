package com.almagems.cubetraz.game;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static com.almagems.cubetraz.game.Game.*;


public final class GameProgress {

    private static final int MAX_LEVELS = 61;

    private static final String SAVE_GAME_FILE_EASY = "easy_game.dat";
    private static final String SAVE_GAME_FILE_NORMAL = "normal_game.dat";
    private static final String SAVE_GAME_FILE_HARD = "hard_game.dat";

    private final LevelData[] ar_levels_easy = new LevelData[MAX_LEVELS];
    private final LevelData[] ar_levels_normal = new LevelData[MAX_LEVELS];
    private final LevelData[] ar_levels_hard = new LevelData[MAX_LEVELS];

    public int getStarsEasy(final int level_number)     { return ar_levels_easy[level_number-1].stars;      }
    public int getStarsNormal(final int level_number)   { return ar_levels_normal[level_number-1].stars;    }
    public int getStarsHard(final int level_number)     { return ar_levels_hard[level_number-1].stars;      }

    public boolean getSolvedEasy(int level_number)      { return ar_levels_easy[level_number-1].solved;     }
    public boolean getSolvedNormal(int level_number)    { return ar_levels_normal[level_number-1].solved;   }
    public boolean getSolvedHard(int level_number)      { return ar_levels_hard[level_number-1].solved;     }

    public GameProgress() {
    }

    public void setStarsEasy(final int level_number, final int stars) {
        int index = level_number - 1;

        if (stars > ar_levels_easy[index].stars) {
            ar_levels_easy[index].stars = stars;
        }
    }

    public void setMovesEasy(final int level_number, final int moves)    { ar_levels_easy[level_number-1].moves = moves;     }
    public void setSolvedEasy(final int level_number, final boolean solved)  { ar_levels_easy[level_number-1].solved = solved;   }

    public void setStarsNormal(final int level_number, final int stars) {
        int index = level_number - 1;

        if (stars > ar_levels_normal[index].stars) {
            ar_levels_normal[index].stars = stars;
        }
    }

    public void setMovesNormal(final int level_number, final int moves)     { ar_levels_normal[level_number-1].moves = moves;   }
    public void setSolvedNormal(final int level_number, final boolean solved)  { ar_levels_normal[level_number-1].solved = solved; }

    public void setStarsHard(final int level_number, final int stars) {
        int index = level_number - 1;

        if (stars > ar_levels_hard[index].stars) {
            ar_levels_hard[index].stars = stars;
        }
    }

    public void setMovesHard(final int level_number, final int moves)       { ar_levels_hard[level_number-1].moves = moves;     }
    public void setSolvedHard(final int level_number, final boolean solved)    { ar_levels_hard[level_number-1].solved = solved;   }

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
        saveArray(SAVE_GAME_FILE_EASY, ar_levels_easy);
        saveArray(SAVE_GAME_FILE_NORMAL, ar_levels_normal);
        saveArray(SAVE_GAME_FILE_HARD, ar_levels_hard);
    }

    private LevelData[] loadArray(String fileName) {
        LevelData[] ar = new LevelData[MAX_LEVELS];

        for (int i = 0; i < MAX_LEVELS; ++i) {
            ar[i] = new LevelData();
            ar[i].reset();
        }

        ar[0].stars = LEVEL_UNLOCKED; // unlock first level

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
        copyArray(temp, ar_levels_easy);

        temp = loadArray(SAVE_GAME_FILE_NORMAL);
        copyArray(temp, ar_levels_normal);

        temp = loadArray(SAVE_GAME_FILE_HARD);
        copyArray(temp, ar_levels_hard);
    }

    public int getStarCount() {
        int stars = 0;
        for (int i = 0; i < MAX_LEVELS - 1; ++i) {
            if (ar_levels_easy[i].stars > 0) {
                stars += ar_levels_easy[i].stars;
            }

            if (ar_levels_normal[i].stars > 0) {
                stars += ar_levels_normal[i].stars;
            }

            if (ar_levels_hard[i].stars > 0) {
                stars += ar_levels_hard[i].stars;
            }
        }
        return stars;
    }

    public int getSolvedLevelCount() {
        int solved = 0;
        for(int i = 0; i < MAX_LEVELS - 1; ++i) {
            if (ar_levels_easy[i].stars > 0) {
                ++solved;
            }
            if (ar_levels_normal[i].stars > 0) {
                ++solved;
            }
            if (ar_levels_hard[i].stars > 0) {
                ++solved;
            }
        }
        return solved;
    }

}

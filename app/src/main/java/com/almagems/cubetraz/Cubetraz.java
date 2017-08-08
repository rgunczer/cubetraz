package com.almagems.cubetraz;

import android.content.Context;
import android.provider.Settings;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import static com.almagems.cubetraz.Constants.*;


public final class Cubetraz {

    private static final LevelData[] ar_levels_easy = new LevelData[MAX_LEVELS];
    private static final LevelData[] ar_levels_normal = new LevelData[MAX_LEVELS];
    private static final LevelData[] ar_levels_hard = new LevelData[MAX_LEVELS];

    public static int getStarsEasy(final int level_number)     { return ar_levels_easy[level_number-1].stars;      }
    public static int getStarsNormal(final int level_number)   { return ar_levels_normal[level_number-1].stars;    }
    public static int getStarsHard(final int level_number)     { return ar_levels_hard[level_number-1].stars;      }

    public static boolean getSolvedEasy(int level_number)   { return ar_levels_easy[level_number-1].solved;     }
    public static boolean getSolvedNormal(int level_number) { return ar_levels_normal[level_number-1].solved;   }
    public static boolean getSolvedHard(int level_number)   { return ar_levels_hard[level_number-1].solved;     }

    public static void setStarsEasy(final int level_number, final int stars) {
        int index = level_number - 1;

        if (stars > ar_levels_easy[index].stars) {
            ar_levels_easy[index].stars = stars;
        }
    }

    public static void setMovesEasy(final int level_number, final int moves)    { ar_levels_easy[level_number-1].moves = moves;     }
    public static void setSolvedEasy(final int level_number, final boolean solved)  { ar_levels_easy[level_number-1].solved = solved;   }

    public static void setStarsNormal(final int level_number, final int stars) {
        int index = level_number - 1;

        if (stars > ar_levels_normal[index].stars) {
            ar_levels_normal[index].stars = stars;
        }
    }

    public static void setMovesNormal(final int level_number, final int moves)     { ar_levels_normal[level_number-1].moves = moves;   }
    public static void setSolvedNormal(final int level_number, final boolean solved)  { ar_levels_normal[level_number-1].solved = solved; }

    public static void setStarsHard(final int level_number, final int stars) {
        int index = level_number - 1;

        if (stars > ar_levels_hard[index].stars) {
            ar_levels_hard[index].stars = stars;
        }
    }

    public static void setMovesHard(final int level_number, final int moves)       { ar_levels_hard[level_number-1].moves = moves;     }
    public static void setSolvedHard(final int level_number, final boolean solved)    { ar_levels_hard[level_number-1].solved = solved;   }


    public static void init() {
//        for (int i = 0; i < MAX_LEVELS; ++i) {
//            ar_levels_easy[i] = new LevelData();
//            ar_levels_normal[i] = new LevelData();
//            ar_levels_hard[i] = new LevelData();
//
//            ar_levels_easy[i].reset();
//            ar_levels_normal[i].reset();
//            ar_levels_hard[i].reset();
//        }
    }

    private static void saveArray(String fileName, LevelData[] array) {
        try {
            Context context = Engine.activity;
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(array);

            oos.flush();
            fos.getFD().sync();
            fos.close();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public static void save() {
        saveArray("easy", ar_levels_easy);
        saveArray("normal", ar_levels_normal);
        saveArray("hard", ar_levels_hard);
    }

    private static LevelData[] loadArray(String fileName) {
        LevelData[] ar = new LevelData[MAX_LEVELS];

        for (int i = 0; i < MAX_LEVELS; ++i) {
            ar[i] = new LevelData();
            ar[i].reset();
        }

        try {
            Context context = Engine.activity;
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object obj = ois.readObject();
            ar = (LevelData[])(obj);
            fis.close();
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
        return ar;
    }

    private static void copyArray(LevelData[] from, LevelData[] to) {
        //System.arraycopy(from, 0, to, 0, from.length);
        for(int i = 0; i < from.length; ++i) {
            to[i] = from[i];
        }
    }

    public static void load() {
        LevelData[] temp;

        temp = loadArray("easy");
        copyArray(temp, ar_levels_easy);

        temp = loadArray("normal");
        copyArray(temp, ar_levels_normal);

        temp = loadArray("hard");
        copyArray(temp, ar_levels_hard);
    }

    public static int getStarCount() {
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

}

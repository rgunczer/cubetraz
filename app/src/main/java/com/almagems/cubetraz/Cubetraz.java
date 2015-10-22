package com.almagems.cubetraz;

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
        for (int i = 0; i < MAX_LEVELS; ++i) {
            ar_levels_easy[i] = new LevelData();
            ar_levels_normal[i] = new LevelData();
            ar_levels_hard[i] = new LevelData();

            ar_levels_easy[i].reset();
            ar_levels_normal[i].reset();
            ar_levels_hard[i].reset();
        }
    }

    public static void save() {
        //fwrite(ar_levels_easy, 1, sizeof(ar_levels_easy), fp);
        //fwrite(ar_levels_normal, 1, sizeof(ar_levels_normal), fp);
        //fwrite(ar_levels_hard, 1, sizeof(ar_levels_hard), fp);
    }

    public static void load() {
        //fread(ar_levels_easy, 1, sizeof(ar_levels_easy), fp);
        //fread(ar_levels_normal, 1, sizeof(ar_levels_normal), fp);
        //fread(ar_levels_hard, 1, sizeof(ar_levels_hard), fp);
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

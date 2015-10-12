package com.almagems.cubetraz;


public final class Constants {

    public static final int BYTES_PER_SHORT = 2;
	public static final int BYTES_PER_FLOAT = 4;
	public static final float EPSILON = 0.00000001f;

    public static final boolean FINAL = false;

    public static final float CUBE_SIZE = 1.1f;
    public static final float HALF_CUBE_SIZE = CUBE_SIZE / 2.0f;

    public static final int MAX_CUBE_COUNT = 9;
    public static final int MAX_STARS = 1024;

    public static final String MUSIC_CPU = "POL-a-cpu-life-short";
    public static final String MUSIC_BREEZE = "POL-breeze-short";
    public static final String MUSIC_DRONES = "POL-positive-drones-short";
    public static final String MUSIC_VECTORS = "POL-vectors-short";
    public static final String MUSIC_WAVES = "POL-warm-waves-short";

    public static final String SOUND_LEVEL_COMPLETED = "keys";
    public static final String SOUND_VOLUME_UP = "NFF-switch-on";
    public static final String SOUND_VOLUME_DOWN = "NFF-switch-off";
    public static final String SOUND_CUBE_HIT = "cube_to_cube";
    public static final String SOUND_TAP_ON_LEVEL_CUBE = "open";
    public static final String SOUND_TAP_ON_LOCKED_LEVEL_CUBE = "locked";

    public static final int DIRTY_ALPHA = 60;

    public static final int MAX_FACE_TRANSFORM_COUNT = 8;


    public static final int Face_X_Plus = 0;
    public static final int Face_X_Minus = 1;
    public static final int Face_Y_Plus = 2;
    public static final int Face_Y_Minus = 3;
    public static final int Face_Z_Plus = 4;
    public static final int Face_Z_Minus = 5;

    public static final int AxisMovement_Y_Plus = 1;
    public static final int AxisMovement_Y_Minus = 2;
    public static final int AxisMovement_X_Plus = 3;
    public static final int AxisMovement_X_Minus = 4;
    public static final int AxisMovement_Z_Plus = 5;
    public static final int AxisMovement_Z_Minus = 6;
    public static final int AxisMovement_No_Move = 7;

    public static final int Scene_Intro = 0;
    public static final int Scene_Menu = 1;
    public static final int Scene_Anim = 2;
    public static final int Scene_Level = 3;
    public static final int Scene_Stat = 4;
    public static final int Scene_Solvers = 5;
    public static final int Scene_Outro = 6;

    public static final int SymbolInfo = 0;
    public static final int SymbolLock = 1;
    public static final int SymbolPlus = 2;
    public static final int SymbolMinus = 3;
    public static final int SymbolQuestionmark = 4;
    public static final int SymbolGoLeft = 5;
    public static final int SymbolGoRight = 6;
    public static final int SymbolGoUp = 7;
    public static final int SymbolGoDown = 8;
    public static final int SymbolUndo = 9;
    public static final int SymbolSolver = 10;
    public static final int SymbolPause = 11;
    public static final int Symbol3Star = 12;
    public static final int Symbol2Star = 13;
    public static final int Symbol1Star = 14;
    public static final int SymbolStar = 15;
    public static final int SymbolTriangleUp = 16;
    public static final int SymbolTriangleDown = 17;
    public static final int SymbolHilite = 18;
    public static final int SymbolSolved = 19;
    public static final int SymbolDeath = 20;
    public static final int SymbolLightning = 21;
    public static final int SymbolCracks = 22;
    public static final int SymbolTriangleLeft = 23;
    public static final int SymbolTriangleRight = 24;
    public static final int SymbolEmpty = 25;


}

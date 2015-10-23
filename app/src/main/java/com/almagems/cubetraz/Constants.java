package com.almagems.cubetraz;


public final class Constants {

    public static final int MAX_TUTOR_COUNT = 11;

    public static final float EPSILON_SMALL = 0.001f;

    public static final int MAX_SOLUTION_MOVES = 16;
    public static final int MAX_HINT_CUBES = 48;
    public static final float UNDO_TIMEOUT = 0.2f;

    public static final int WARM_FACTOR = 5;

    public static final int KILOBYTE = 1024;
    public static final int BUF_SIZE = 64;

    public static final int BYTES_PER_SHORT = 2;
	public static final int BYTES_PER_FLOAT = 4;
	public static final float EPSILON = 0.01f;

    public static final boolean FINAL = false;

    public static final float CUBE_SIZE = 1.1f;
    public static final float HALF_CUBE_SIZE = CUBE_SIZE / 2.0f;

    public static final int MAX_CUBE_COUNT = 9;
    public static final int MAX_STARS = 1024;

    public static final int MAX_LEVELS = 61;

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

    public static final float DIRTY_ALPHA = 60f;

    public static final int MAX_FACE_TRANSFORM_COUNT = 8;

    public static final float FONT_OVERLAY_OFFSET = 0.01f;
    public static final String SAVE_GAME_FILE = "game.dat";
    public static final String SAVE_GAME_FILE_EASY = "easy_game.dat";
    public static final String SAVE_GAME_FILE_NORMAL = "normal_game.dat";
    public static final String SAVE_GAME_FILE_HARD = "hard_game.dat";
    public static final String SAVE_OPTIONS_FILE = "options.dat";
    public static final int LEVEL_LOCKED = -1;
    public static final int LEVEL_UNLOCKED = 0;


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


    public static final int Tutor_Dead = 0;
    public static final int Tutor_Swipe = 1;
    public static final int Tutor_Goal = 2;
    public static final int Tutor_Moving = 3;
    public static final int Tutor_Mover = 4;
    public static final int Tutor_Drag = 5;
    public static final int Tutor_Plain = 6;
    public static final int Tutor_MenuPause = 7;
    public static final int Tutor_MenuUndo = 8;
    public static final int Tutor_MenuHint = 9;
    public static final int Tutor_MenuSolvers = 10;

    public static final int FACE_SIZE = MAX_CUBE_COUNT * MAX_CUBE_COUNT;

    public static final int MAX_TEXT_LINES = 4;

    public static final int MoveDir_MoveNone = 0;
    public static final int MoveDir_MoveY = 1;
    public static final int MoveDir_MoveX = 2;
    public static final int MoveDir_MoveZ = 3;




    public enum TextAlignEnum {
        LeftAlign,
        CenterAlign,
        RightAlign
    }

    public enum TutorStateEnum {
        TutorAppear,
        TutorDisappear,
        TutorDone
    }

    public enum HUDStateEnum {
        AppearHUD,
        DisappearHUD,
        DoneHUD,
    }

    public enum LevelInitActionEnum {
        FullInit,
        JustContinue,
        ShowSolution
    }

    public enum DifficultyEnum {
        Easy,
        Normal,
        Hard
    }

    public enum AnimTypeEnum {
        AnimToLevel,
        AnimToMenuFromPaused,
        AnimToMenuFromCompleted
    }

    public enum LevelCubeDecalTypeEnum {
        LevelCubeDecalNumber,
        LevelCubeDecalStars,
        LevelCubeDecalSolver
    }

    public enum SwipeDirEnums {
        SwipeNone,
        SwipeUp,
        SwipeDown,
        SwipeUpLeft,
        SwipeUpRight,
        SwipeDownLeft,
        SwipeDownRight,
        SwipeLeft,
        SwipeRight,
    }

    public enum CubeTypeEnum {
        CubeIsNotSet,

        CubeIsInvisible,
        CubeIsInvisibleAndObstacle,

        CubeIsVisibleAndObstacle,
        CubeIsVisibleAndObstacleAndLevel
    }

    public enum CubeFaceNamesEnum {
        Face_Empty,

        Face_Tutorial,

        Face_Menu,
        Face_Options,
        Face_Store,

        Face_Easy01,
        Face_Easy02,
        Face_Easy03,
        Face_Easy04,

        Face_Normal01,
        Face_Normal02,
        Face_Normal03,
        Face_Normal04,

        Face_Hard01,
        Face_Hard02,
        Face_Hard03,
        Face_Hard04
    }

    public enum PickRenderTypeEnum {
        RenderAll,
        RenderOnlyMovingCubes,
        RenderOnlyLevelCubes,
        RenderOnlyMovingCubePlay,
        RenderOnlyMovingCubeOptions,
        RenderOnlyMovingCubeStore,
        RenderOnlyOptions,
        RenderOnlyCubeCredits,
        RenderOnlyHUD
    }

    public enum AxisEnum {
        X_Axis,
        Y_Axis,
        Z_Axis
    }

    public enum CompletedFaceNextActionEnum {
        //Unlock,
        Finish,
        Next,
        Buy_Full_Version,
    }

    public enum FaceTransformsEnum {
        NoTransform,
        MirrorHoriz,
        MirrorVert,
        RotateCW90,
        RotateCCW90
    }

    public enum CubeFaceNavigationEnum {
        NoNavigation,

        Tutorial_To_Menu,

        Menu_To_Options,
        Options_To_Menu,

        Menu_To_Store,
        Store_To_Menu,

        Menu_To_Easy1,
        Easy1_To_Menu,

        Easy1_To_Easy2,
        Easy2_To_Easy1,

        Easy2_To_Easy3,
        Easy3_To_Easy2,

        Easy3_To_Easy4,
        Easy4_To_Easy3,

        Easy4_To_Easy1,
        Easy1_To_Easy4,

        Easy1_To_Normal1,
        Normal1_To_Easy1,

        Normal1_To_Normal2,
        Normal2_To_Normal1,

        Normal2_To_Normal3,
        Normal3_To_Normal2,

        Normal3_To_Normal4,
        Normal4_To_Normal3,

        Normal4_To_Normal1,
        Normal1_To_Normal4,

        Normal1_To_Hard1,
        Hard1_To_Normal1,

        Hard1_To_Hard2,
        Hard2_To_Hard1,

        Hard2_To_Hard3,
        Hard3_To_Hard2,

        Hard3_To_Hard4,
        Hard4_To_Hard3,

        Hard4_To_Hard1,
        Hard1_To_Hard4,

        Hard1_To_Menu,
    }


}

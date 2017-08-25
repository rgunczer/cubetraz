package com.almagems.cubetraz;

import android.content.Context;

import com.almagems.cubetraz.cubes.CubeLocation;
import com.almagems.cubetraz.cubes.MenuCube;
import com.almagems.cubetraz.graphics.Texture;
import com.almagems.cubetraz.math.Vector;
import com.almagems.cubetraz.math.Vector2;
import com.almagems.cubetraz.scenes.level.LevelBuilder;
import com.almagems.cubetraz.cubes.Cube;
import com.almagems.cubetraz.cubes.CubeFaceData;
import com.almagems.cubetraz.graphics.Color;
import com.almagems.cubetraz.graphics.FontStruct;
import com.almagems.cubetraz.graphics.Graphics;
import com.almagems.cubetraz.graphics.Rectangle;
import com.almagems.cubetraz.graphics.TexturedQuad;
import com.almagems.cubetraz.scenes.anim.AnimInitData;
import com.almagems.cubetraz.scenes.anim.Animator;
import com.almagems.cubetraz.scenes.Intro;
import com.almagems.cubetraz.scenes.Creator;
import com.almagems.cubetraz.scenes.level.Level;
import com.almagems.cubetraz.scenes.level.Solvers;
import com.almagems.cubetraz.scenes.menu.Menu;
import com.almagems.cubetraz.scenes.Outro;
import com.almagems.cubetraz.scenes.Scene;
import com.almagems.cubetraz.scenes.stat.StatInitData;
import com.almagems.cubetraz.scenes.stat.Statistics;
import com.almagems.cubetraz.scenes.level.LevelInitData;
import com.almagems.cubetraz.scenes.menu.MenuInitData;
import com.almagems.cubetraz.utils.SwipeInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static android.opengl.GLES10.*;

public final class Game {

    public static final Vector vectorXaxis = new Vector(1f, 0f, 0f);
    public static final Vector vectorYaxis = new Vector(0f, 1f, 0f);
    public static final Vector vectorZaxis = new Vector(0f, 0f, 1f);

    private static float hiliteTimeout = 0f;

    public static final int MAX_TUTOR_COUNT = 11;

    public static final float EPSILON_SMALL = 0.001f;

    public static final int MAX_HINT_CUBES = 48;
    public static final float UNDO_TIMEOUT = 0.2f;

    public static final byte WARM_FACTOR = (byte)5;

    public static final int KILOBYTE = 1024;
    public static final int BUF_SIZE = 64;

    public static final int BYTES_PER_SHORT = 2;
    public static final int BYTES_PER_FLOAT = 4;
    public static final float EPSILON = 0.01f;

    public static final float CUBE_SIZE = 1.1f;
    public static final float HALF_CUBE_SIZE = CUBE_SIZE / 2.0f;

    public static final int MAX_CUBE_COUNT = 9;
    public static final int MAX_STARS = 1024;

    public static final float DIRTY_ALPHA = 60f;

    public static final int MAX_FACE_TRANSFORM_COUNT = 8;

    public static final float FONT_OVERLAY_OFFSET = 0.02f;
    public static final int LEVEL_LOCKED = -1;
    public static final int LEVEL_UNLOCKED = 0;

    public static final int X_Plus = 3;
    public static final int X_Minus = 4;
    public static final int Y_Plus = 1;
    public static final int Y_Minus = 2;
    public static final int Z_Plus = 5;
    public static final int Z_Minus = 6;

    public static final int Scene_Intro = 0;
    public static final int Scene_Menu = 1;
    public static final int Scene_Anim = 2;
    public static final int Scene_Level = 3;
    public static final int Scene_Stat = 4;
    public static final int Scene_Solvers = 5;
    public static final int Scene_Outro = 6;

    public static final int Symbol_Info = 0;
    public static final int Symbol_Lock = 1;
    public static final int Symbol_Plus = 2;
    public static final int Symbol_Minus = 3;
    public static final int Symbol_Questionmark = 4;
    public static final int Symbol_GoLeft = 5;
    public static final int Symbol_GoRight = 6;
    public static final int Symbol_GoUp = 7;
    public static final int Symbol_GoDown = 8;
    public static final int Symbol_Undo = 9;
    public static final int Symbol_Solver = 10;
    public static final int Symbol_Pause = 11;
    public static final int Symbol_3Star = 12;
    public static final int Symbol_2Star = 13;
    public static final int Symbol_1Star = 14;
    public static final int Symbol_Star = 15;
    public static final int Symbol_TriangleUp = 16;
    public static final int Symbol_TriangleDown = 17;
    public static final int Symbol_Hilite = 18;
    public static final int Symbol_Solved = 19;
    public static final int Symbol_Death = 20;
    public static final int Symbol_Lightning = 21;
    public static final int Symbol_Cracks = 22;
    public static final int Symbol_TriangleLeft = 23;
    public static final int Symbol_TriangleRight = 24;
    public static final int Symbol_Empty = 25;

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

    public static final int MAX_TEXT_LINES = 4;


    public enum TextAlignEnum {
        LeftAlign,
        CenterAlign,
        RightAlign
    }

    public enum TutorStateEnum {
        Appear,
        Disappear,
        Done
    }

    public enum HUDStateEnum {
        Appear,
        Disappear,
        Done,
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
        AnimToMenu,
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
        CubeIsInvisible,
        CubeIsInvisibleAndObstacle,

        CubeIsVisibleAndObstacle,
        CubeIsVisibleAndObstacleAndLevel
    }

    public enum CubeFaceNames {
        Face_Empty,

        Face_Tutorial,

        Face_Menu,
        Face_Options,
        Face_Score,

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
        Finish,
        Next
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

    private static Game instance;
    private static MainRenderer renderer;

    static void setRenderer(MainRenderer renderer) {
        Game.renderer = renderer;
    }

    public static Context getContext() {
        return Game.renderer.context;
    }

    public static Cube[][][] cubes = new Cube[MAX_CUBE_COUNT][MAX_CUBE_COUNT][MAX_CUBE_COUNT];

    public static boolean fboLost = false;

    private static boolean initialized;

    private static Scene currentScene;
    private static Intro intro;
    public static Menu menu;
    private static Animator animator;
    public static Level level;
    private static Solvers solvers;
    private static Statistics statistics;
    private static Outro outro;

    public static float dirtyAlpha;
    public static float minSwipeLength;

    public static final StatInitData statInitData = new StatInitData();
    public static final MenuInitData menuInitData = new MenuInitData();
    public static final AnimInitData animInitData = new AnimInitData();
    public static final LevelInitData levelInitData = new LevelInitData();

    public static final Vector cubeOffset = new Vector();
    public static CubeFaceData[] cubeFacesData = new CubeFaceData[7];

    public static boolean canPlayLockedLevels = true; // TODO: modify this in final version

    public static Color faceColor = new Color(191, 204, 191, 255);
    public static Color baseColor = new Color(230, 230, 230, 255);
    public static Color titleColor = new Color(100, 0, 0, 200);
    public static Color textColor = new Color(80, 0, 0, 130);
    public static Color fontColorOnMenuCube = new Color(80, 0, 0, 230);
    public static Color symbolColor = new Color(23, 23, 23, 150);
    public static Color textColorOnCubeFace = new Color(76, 0, 0, 153);
    public static Color levelNumberColor = new Color(0, 0, 0, 150);
    public static Color cubeHiLiteColor = new Color(160, 160, 160, 255);

    static {
        for(int i = 0; i < MAX_CUBE_COUNT; ++i) {
            for(int j = 0; j < MAX_CUBE_COUNT; ++j) {
                for(int k = 0; k < MAX_CUBE_COUNT; ++k)
                    cubes[i][j][k] = new Cube();
            }
        }
        for (int i = 0; i < 7; ++i) {
            cubeFacesData[i] = new CubeFaceData();
        }
    }

    private Game() {
    }

    public static void init() {
        //System.out.println("Game.init");

        if (initialized) {
            return;
        }

        initialized = true;



        Audio.init(GameOptions.getMusicVolume(), GameOptions.getSoundVolume());

        intro = null;
        menu = null;
        animator = null;
        level = null;
        statistics = null;
        outro = null;

        currentScene = null;

        dirtyAlpha = 0f;

        initFonts();
        initFontsBig();
        initNumberFonts();
        initSymbols();

        float size = (MAX_CUBE_COUNT * CUBE_SIZE) - CUBE_SIZE;
        cubeOffset.x = cubeOffset.y = cubeOffset.z = size / -2.0f;

        Creator.createCubes();

        level = new Level();
        menu = new Menu();
        animator = new Animator();
        statistics = new Statistics();

        LevelBuilder.level = level;
    }

    static void onSurfaceChanged() {
        minSwipeLength = Graphics.height / 10;

        if (currentScene == null) {
            showScene(Scene_Intro);
            //showScene(Scene_Menu);
            //showScene(Scene_Anim);
            //showScene(Scene_Level);
            //showScene(Scene_Stat);
            //showScene(Scene_Outro);
        }
    }

    public static void resetCubesColors() {
        for(int x = 0; x < MAX_CUBE_COUNT; ++x) {
            for(int y = 0; y < MAX_CUBE_COUNT; ++y) {
                for(int z = 0; z < MAX_CUBE_COUNT; ++z) {
                    cubes[x][y][z].resetColor();
                }
            }
        }
    }

    public static String getLevelTypeAndNumberString(DifficultyEnum difficulty, int levelNumber) {
        String str = "";
        switch (difficulty) {
            case Easy: str = "EASY-" + levelNumber + " " + (GameProgress.isSolvedEasy(levelNumber) ? "\nSOLVED" : ""); break;
            case Normal: str = "NORMAL-" + levelNumber + " " + (GameProgress.isSolvedNormal(levelNumber) ? "\nSOLVED" : ""); break;
            case Hard: str = "HARD-" + levelNumber + " " + (GameProgress.isSolvedHard(levelNumber) ? "\nSOLVED" : ""); break;
        }
        return str.trim();
    }

    public static boolean isObstacle(CubeLocation cube_pos) {
        Cube cube = cubes[cube_pos.x][cube_pos.y][cube_pos.z];

        if (cube.type == CubeTypeEnum.CubeIsVisibleAndObstacle ||
            cube.type == CubeTypeEnum.CubeIsInvisibleAndObstacle ||
            cube.type == CubeTypeEnum.CubeIsVisibleAndObstacleAndLevel) {
            return true;
        } else {
            return false;
        }
    }

    public static TexturedQuad getNumberFont(int number) {
        return m_numbers.get(number);
    }
    public static TexturedQuad getSymbol(int key) {
        return m_symbols.get(key);
    }

    public static Map<String, TexturedQuad> m_fonts = new HashMap<>();
    public static Map<String, TexturedQuad> m_fonts_big = new HashMap<>();
    public static Map<Integer, TexturedQuad> m_numbers = new HashMap<>();
    public static Map<Integer, TexturedQuad> m_symbols = new HashMap<>();

    public static TexturedQuad getFont(char ch) {
        //printf("\nNumber of Fonts:%lu", m_fonts.size());
        return m_fonts.get("" + ch);
    }

    public static boolean isOnAList(Cube theCube, ArrayList<Cube> lst) {
        return  lst.contains(theCube);
    }

    public static TexturedQuad getFontBig(String ch) {
        return m_fonts_big.get(ch);
    }


    public static ArrayList<Cube> createBaseCubesList() {
        ArrayList<Cube> lst = new ArrayList<>();
        Cube cube;

        for(int x = 0; x < MAX_CUBE_COUNT; ++x) {
            for(int y = 0; y < MAX_CUBE_COUNT; ++y) {
                for(int z = 0; z < MAX_CUBE_COUNT; ++z) {
                    cube = cubes[x][y][z];

                    if ( x > 1 && x < MAX_CUBE_COUNT - 2 && y > 1 && y < MAX_CUBE_COUNT - 2 && z > 1 && z < MAX_CUBE_COUNT - 2 ) {

                    } else {
                        if (x == 0 || x == MAX_CUBE_COUNT - 1 || y == 0 || y == MAX_CUBE_COUNT - 1 || z == 0 || z == MAX_CUBE_COUNT - 1) {

                        } else {
                            lst.add(cube);
                        }
                    }
                }
            }
        }
        return lst;
    }

    public static void resetCubesFonts() {
        Cube cube;
        for(int x = 0; x < MAX_CUBE_COUNT; ++x) {
            for(int y = 0; y < MAX_CUBE_COUNT; ++y) {
                for(int z = 0; z < MAX_CUBE_COUNT; ++z) {
                    cube = cubes[x][y][z];

                    cube.removeFonts();
                    cube.removeSymbols();
                }
            }
        }
    }

    public static  void clearCubeFaceData() {
        for (int i = 0; i < 7; ++i) {
            cubeFacesData[i].clear();
        }
        resetCubesFonts();
    }

    public static void showScene(int sceneName) {
        Scene scene = null;

        switch (sceneName) {
            case Scene_Intro:
                intro = new Intro();
                intro.init();
                scene = intro;
                break;

            case Scene_Menu:
                intro = null;

                if (outro != null) {
                    outro = null;
                    menuInitData.reappear = false;
                }
                menu.init();
                scene = menu;
                break;

            case Scene_Anim:
                animator.init();
                scene = animator;
                break;

            case Scene_Level:
                level.init();
                scene = level;
                break;

            case Scene_Solvers:
                if (solvers == null) {
                    solvers = new Solvers();
                }
                solvers.init();
                scene = solvers;
                break;

            case Scene_Stat:
                statistics.init();
                scene = statistics;
                break;

            case Scene_Outro:
                if (outro == null) {
                    outro = new Outro();
                }
                outro.init();
                scene = outro;
                break;

            default:
                break;
        }
        currentScene = scene;
        currentScene.update();
    }

    public static Vector2 rotate(Vector2 a, float degree) {
        float angle = (float)Math.toRadians(degree);
        float c = (float)Math.cos(angle);
        float s = (float)Math.sin(angle);
        return new Vector2(c*a.x - s*a.y, s*a.x + c*a.y);
    }

    public static void resetCubes() {
        for(int x = 0; x < MAX_CUBE_COUNT; ++x) {
            for(int y = 0; y < MAX_CUBE_COUNT; ++y) {
                for(int z = 0; z < MAX_CUBE_COUNT; ++z) {
                    cubes[x][y][z].reset();
                }
            }
        }
    }

    public static void buildVisibleCubesList(ArrayList<Cube> lst) {
        lst.clear();
        CubeTypeEnum type;
        for (int x = 0; x < MAX_CUBE_COUNT; ++x) {
            for (int y = 0; y < MAX_CUBE_COUNT; ++y) {
                for (int z = 0; z < MAX_CUBE_COUNT; ++z) {
                    type = cubes[x][y][z].type;
                    if (type == CubeTypeEnum.CubeIsVisibleAndObstacle || type == CubeTypeEnum.CubeIsVisibleAndObstacleAndLevel) {
                        lst.add(cubes[x][y][z]);
                    }
                }
            }
        }
    }

    public static void buildVisibleCubesListOnlyOnFaces(ArrayList<Cube> lst) {
        lst.clear();
        CubeTypeEnum type;
        for (int x = 0; x < MAX_CUBE_COUNT; ++x) {
            for (int y = 0; y < MAX_CUBE_COUNT; ++y) {
                for (int z = 0; z < MAX_CUBE_COUNT; ++z) {
                    type = cubes[x][y][z].type;
                    if (type == CubeTypeEnum.CubeIsVisibleAndObstacle || type == CubeTypeEnum.CubeIsVisibleAndObstacleAndLevel) {
                        if (x == 0 || x == MAX_CUBE_COUNT - 1 ||
                            y == 0 || y == MAX_CUBE_COUNT - 1 ||
                            z == 0 || z == MAX_CUBE_COUNT - 1) {
                                lst.add(cubes[x][y][z]);
                                cubes[x][y][z].setColor( faceColor );
                        }
                    }
                }
            }
        }
    }

    public static Vector getCubePosition(CubeLocation pos) {
        return getCubePosition(pos.x, pos.y, pos.z);
    }

    public static Vector getCubePosition(int x, int y, int z) {
        Vector pos = new Vector();

        pos.x = cubes[x][y][z].tx;
        pos.y = cubes[x][y][z].ty;
        pos.z = cubes[x][y][z].tz;

        return pos;
    }

    public static void renderToFBO(Scene scene) {
        fboLost = false;

        Graphics.saveOriginalFBO();

        Graphics.fbo.bind();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        scene.renderToFBO();

        Graphics.restoreOriginalFBO();
    }

	public static void update() {
        currentScene.update();
    }

	public static void draw() {
        currentScene.render();
	}

	public static void handleTouchPress(float normalizedX, float normalizedY, int fingerCount) {
        currentScene.onFingerDown(normalizedX, normalizedY, fingerCount);
    }

	public static void handleTouchDrag(float normalizedX, float normalizedY, int fingerCount) {
        currentScene.onFingerMove(0f, 0f, normalizedX, normalizedY, fingerCount);
    }

	public static void handleTouchRelease(float normalizedX, float normalizedY) {
        currentScene.onFingerUp(normalizedX, normalizedY, 1);
    }

    public static void showAd() {
        MainActivity act = (MainActivity)getContext();
        act.showAd();
    }

    public static SwipeInfo getSwipeDirAndLength(Vector2 pos_down, Vector2 pos_up) {
        SwipeInfo swipeInfo = new SwipeInfo();

        swipeInfo.swipeDir = SwipeDirEnums.SwipeNone;

        float up_x = pos_up.x;
        float up_y = pos_up.y;

        float down_x = pos_down.x;
        float down_y = pos_down.y;

        up_y = -up_y;
        down_y = -down_y;

        Vector2 dir = new Vector2();
        dir.x = up_x - down_x;
        dir.y = up_y - down_y;

        swipeInfo.length = dir.len();

        float diff_x = pos_up.x - pos_down.x;
        float diff_y = pos_up.y - pos_down.y;

//    printf("\ndiff x:%.2f, y:%.2f", diff_x, diff_y);

        if ( Math.abs(diff_x) > Math.abs(diff_y) ) {
            if (diff_x < 0.0f) {
//            printf("\nSwipe Down");
                swipeInfo.swipeDir = SwipeDirEnums.SwipeLeft; // SwipeDown;
            }

            if (diff_x > 0.0f) {
//            printf("\nSwipe Up");
                swipeInfo.swipeDir = SwipeDirEnums.SwipeRight; // SwipeUp;
            }
        } else {
            if (diff_y < 0.0f) {
//            printf("\nSwipe Left");
                swipeInfo.swipeDir = SwipeDirEnums.SwipeUp;
            }

            if (diff_y > 0.0f) {
//            printf("\nSwipe Right");
                swipeInfo.swipeDir = SwipeDirEnums.SwipeDown;
            }
        }
        return swipeInfo;
    }

    public static void setupHollowCube() {
        int number_of_visible_cubes = 0;
        int number_of_invisible_cubes = 0;

        for(int x = 0; x < MAX_CUBE_COUNT; ++x) {
            for(int y = 0; y < MAX_CUBE_COUNT; ++y) {
                for(int z = 0; z < MAX_CUBE_COUNT; ++z) {
                    if ( x > 1 && x < MAX_CUBE_COUNT - 2 && y > 1 && y < MAX_CUBE_COUNT - 2 && z > 1 && z < MAX_CUBE_COUNT - 2 ) {
                        ++number_of_invisible_cubes;
                        cubes[x][y][z].type = CubeTypeEnum.CubeIsInvisible;
                    } else {
                        if (x == 0 || x == MAX_CUBE_COUNT - 1 || y == 0 || y == MAX_CUBE_COUNT - 1 || z == 0 || z == MAX_CUBE_COUNT - 1) {
                            ++number_of_invisible_cubes;
                            cubes[x][y][z].type = CubeTypeEnum.CubeIsInvisible;
                        } else {
                            ++number_of_visible_cubes;
                            cubes[x][y][z].type = CubeTypeEnum.CubeIsVisibleAndObstacle;
                            cubes[x][y][z].setColor(baseColor);
                        }
                    }
                }
            }
        }

        System.out.println("Number of Visible Cubes: " + number_of_visible_cubes);
        System.out.println("Number of Invisible Cubes: " + number_of_invisible_cubes);
    }

    public static void setCubeTypeOnFace(Cube cube, char ch, int faceType, CubeFaceNames faceName) {
        int levelNumber = -1;

        switch (faceName) {
            case Face_Easy01:
            case Face_Normal01:
            case Face_Hard01:
                levelNumber = 0;
                break;

            case Face_Easy02:
            case Face_Normal02:
            case Face_Hard02:
                levelNumber = 15;
                break;

            case Face_Easy03:
            case Face_Normal03:
            case Face_Hard03:
                levelNumber = 30;
                break;

            case Face_Easy04:
            case Face_Normal04:
            case Face_Hard04:
                levelNumber = 45;
                break;

            default:
                break;
        }

        switch (ch) {
            case ' ': cube.type = CubeTypeEnum.CubeIsInvisible; return;
            case 'x': cube.type = CubeTypeEnum.CubeIsVisibleAndObstacle; return;
            case '1': levelNumber+=1; break;
            case '2': levelNumber+=2; break;
            case '3': levelNumber+=3; break;
            case '4': levelNumber+=4; break;
            case '5': levelNumber+=5; break;
            case '6': levelNumber+=6; break;
            case '7': levelNumber+=7; break;
            case '8': levelNumber+=8; break;
            case '9': levelNumber+=9; break;
            case 'A': levelNumber+=10; break;
            case 'B': levelNumber+=11; break;
            case 'C': levelNumber+=12; break;
            case 'D': levelNumber+=13; break;
            case 'E': levelNumber+=14; break;
            case 'F': levelNumber+=15; break;
            default:
                System.out.println("Unknown cube type on face.");
                return;
        } // switch

        cube.type = CubeTypeEnum.CubeIsInvisibleAndObstacle;

        Creator.addLevelCube(levelNumber, faceType, faceName, cube.x, cube.y, cube.z);
    }

    public static void setCubeTypeInvisible(CubeLocation cube_pos) {
        //printf("\nSet Cube Type Invisible (%d, %d, %d)", location.x, location.y, location.z);
        cubes[cube_pos.x][cube_pos.y][cube_pos.z].type = CubeTypeEnum.CubeIsInvisible;
    }

    public static boolean isPlayerCube(CubeLocation cube_pos) {
        CubeLocation cp = level.mPlayerCube.getLocation();
        if (cp.x == cube_pos.x && cp.y == cube_pos.y && cp.z == cube_pos.z) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isKeyCube(CubeLocation cube_pos) {
        CubeLocation cp = level.getKeyPos();
        if (cp.x == cube_pos.x && cp.y == cube_pos.y && cp.z == cube_pos.z) {
            return true;
        } else {
            return false;
        }
    }

    public static void bindCubeGLData() {

        final float verts[] = {
                // x-plus
                HALF_CUBE_SIZE, HALF_CUBE_SIZE, -HALF_CUBE_SIZE,
                HALF_CUBE_SIZE, HALF_CUBE_SIZE, HALF_CUBE_SIZE,
                HALF_CUBE_SIZE, -HALF_CUBE_SIZE, HALF_CUBE_SIZE,

                HALF_CUBE_SIZE, -HALF_CUBE_SIZE, HALF_CUBE_SIZE,
                HALF_CUBE_SIZE, -HALF_CUBE_SIZE, -HALF_CUBE_SIZE,
                HALF_CUBE_SIZE, HALF_CUBE_SIZE, -HALF_CUBE_SIZE,

                // x-minus
                -HALF_CUBE_SIZE, -HALF_CUBE_SIZE, -HALF_CUBE_SIZE,
                -HALF_CUBE_SIZE, -HALF_CUBE_SIZE, HALF_CUBE_SIZE,
                -HALF_CUBE_SIZE, HALF_CUBE_SIZE, HALF_CUBE_SIZE,

                -HALF_CUBE_SIZE, HALF_CUBE_SIZE, HALF_CUBE_SIZE,
                -HALF_CUBE_SIZE, HALF_CUBE_SIZE, -HALF_CUBE_SIZE,
                -HALF_CUBE_SIZE, -HALF_CUBE_SIZE, -HALF_CUBE_SIZE,


                // y-plus
                HALF_CUBE_SIZE, HALF_CUBE_SIZE, HALF_CUBE_SIZE,
                HALF_CUBE_SIZE, HALF_CUBE_SIZE, -HALF_CUBE_SIZE,
                -HALF_CUBE_SIZE, HALF_CUBE_SIZE, -HALF_CUBE_SIZE,

                -HALF_CUBE_SIZE, HALF_CUBE_SIZE, -HALF_CUBE_SIZE,
                -HALF_CUBE_SIZE, HALF_CUBE_SIZE, HALF_CUBE_SIZE,
                HALF_CUBE_SIZE, HALF_CUBE_SIZE, HALF_CUBE_SIZE,

                // y-minus
                HALF_CUBE_SIZE, -HALF_CUBE_SIZE, -HALF_CUBE_SIZE,
                HALF_CUBE_SIZE, -HALF_CUBE_SIZE, HALF_CUBE_SIZE,
                -HALF_CUBE_SIZE, -HALF_CUBE_SIZE, HALF_CUBE_SIZE,

                -HALF_CUBE_SIZE, -HALF_CUBE_SIZE, HALF_CUBE_SIZE,
                -HALF_CUBE_SIZE, -HALF_CUBE_SIZE, -HALF_CUBE_SIZE,
                HALF_CUBE_SIZE, -HALF_CUBE_SIZE, -HALF_CUBE_SIZE,


                // z-plus
                HALF_CUBE_SIZE, HALF_CUBE_SIZE, HALF_CUBE_SIZE,
                -HALF_CUBE_SIZE, HALF_CUBE_SIZE, HALF_CUBE_SIZE,
                -HALF_CUBE_SIZE, -HALF_CUBE_SIZE, HALF_CUBE_SIZE,

                -HALF_CUBE_SIZE, -HALF_CUBE_SIZE, HALF_CUBE_SIZE,
                HALF_CUBE_SIZE, -HALF_CUBE_SIZE, HALF_CUBE_SIZE,
                HALF_CUBE_SIZE, HALF_CUBE_SIZE, HALF_CUBE_SIZE,

                // z-minus
                HALF_CUBE_SIZE, HALF_CUBE_SIZE, -HALF_CUBE_SIZE,
                HALF_CUBE_SIZE, -HALF_CUBE_SIZE, -HALF_CUBE_SIZE,
                -HALF_CUBE_SIZE, -HALF_CUBE_SIZE, -HALF_CUBE_SIZE,

                -HALF_CUBE_SIZE, -HALF_CUBE_SIZE, -HALF_CUBE_SIZE,
                -HALF_CUBE_SIZE, HALF_CUBE_SIZE, -HALF_CUBE_SIZE,
                HALF_CUBE_SIZE, HALF_CUBE_SIZE, -HALF_CUBE_SIZE,
        };

        float norms[] = {
                // x-plus
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,

                // x-minus
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,

                // y-plus
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,

                // y-minus
                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,


                // z-plus
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,

                // z-minus
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,
        };


        float coords[] = {
                // x-plus
                1f, 0f, // 0
                0f, 0f, // 1
                0f, 1f, // 2

                0f, 1f, // 2
                1f, 1f, // 3
                1f, 0f, // 0

                // x-minus
                1f, 1f, // 0
                0f, 1f, // 1
                0f, 0f, // 2

                0f, 0f, // 2
                1f, 0f, // 3
                1f, 1f, // 0

                // y-plus
                1f, 0f, // 0
                0f, 0f, // 1
                0f, 1f, // 2

                0f, 1f, // 2
                1f, 1f, // 3
                1f, 0f, // 0

                // y-minus
                1f, 1f, // 0
                0f, 1f, // 1
                0f, 0f, // 2

                0f, 0f, // 2
                1f, 0f, // 3
                1f, 1f, // 0

                // z-plus
                1f, 1f, // 0
                0f, 1f, // 1
                0f, 0f, // 2

                0f, 0f, // 2
                1f, 0f, // 3
                1f, 1f, // 0

                // z-minus
                1f, 0f, // 0
                1f, 1f, // 1
                0f, 1f, // 2

                0f, 1f, // 2
                0f, 0f, // 3
                1f, 0f, // 0
        };

        final byte R = (byte)255;
        final byte G = (byte)255;
        final byte B = (byte)255;
        final byte A = (byte)255;

        final byte colors[] = {
                // x-plus
                R, G, B, A,
                R, G, B, A,
                R, G, B, A,
                R, G, B, A,
                R, G, B, A,
                R, G, B, A,

                // x-minus
                R, G, B, A,
                R, G, B, A,
                R, G, B, A,
                R, G, B, A,
                R, G, B, A,
                R, G, B, A,

                // y-plus
                R, G, B, A,
                R, G, B, A,
                R, G, B, A,
                R, G, B, A,
                R, G, B, A,
                R, G, B, A,

                // y-minus
                R, G, B, A,
                R, G, B, A,
                R, G, B, A,
                R, G, B, A,
                R, G, B, A,
                R, G, B, A,

                // z-plus
                R, G, B, A,
                R, G, B, A,
                R, G, B, A,
                R, G, B, A,
                R, G, B, A,
                R, G, B, A,

                // z-minus
                R, G, B, A,
                R, G, B, A,
                R, G, B, A,
                R, G, B, A,
                R, G, B, A,
                R, G, B, A,
        };

        Graphics.addVerticesCoordsNormalsColors(verts, coords, norms, colors);
    }

    // Scales a vector by a scalar
    public static void scaleVector(float[] vector, final float scale) {
        vector[0] *= scale;
        vector[1] *= scale;
        vector[2] *= scale;
    }

    // Gets the length of a vector squared
    public static float getVectorLengthSqrd(final float[] vector) {
        return (vector[0]*vector[0]) + (vector[1]*vector[1]) + (vector[2]*vector[2]);
    }

    // Gets the length of a vector
    public static float getVectorLength(final float[] vVector) {
        return (float)Math.sqrt(getVectorLengthSqrd(vVector));
    }

    // Calculate the cross product of two vectors
    public static void calcVectorCrossProduct(final float[] vU, final float[] vV, float[] vResult) {
        vResult[0] =  vU[1] * vV[2] - vV[1] * vU[2];
        vResult[1] = -vU[0] * vV[2] + vV[0] * vU[2];
        vResult[2] =  vU[0] * vV[1] - vV[0] * vU[1];
    }

    public static void normalizeVector(float[] vNormal) {
        float fLength = 1.0f / getVectorLength(vNormal);
        scaleVector(vNormal, fLength);
    }

    // Subtract one vector from another
    public static void subtractVectors(final float[] vFirst, final float[] vSecond, float[] vResult) {
        vResult[0] = vFirst[0] - vSecond[0];
        vResult[1] = vFirst[1] - vSecond[1];
        vResult[2] = vFirst[2] - vSecond[2];
    }

    public static void getNormalVector(final float[] vP1, final float[] vP2, final float[] vP3, float[] vNormal) {
        float[] vV1 = new float[3];
        float[] vV2 = new float[3];

        subtractVectors(vP2, vP1, vV1);
        subtractVectors(vP3, vP1, vV2);

        calcVectorCrossProduct(vV1, vV2, vNormal);
        normalizeVector(vNormal);
    }

    // Gets the three coefficients of a plane equation given three points on the plane.
    static void getPlaneEquation(float[] vPoint1, float[] vPoint2, float[] vPoint3, float[] vPlane) {
        // Get normal vector from three points. The normal vector is the first three coefficients
        // to the plane equation...
        getNormalVector(vPoint1, vPoint2, vPoint3, vPlane);

        // Final coefficient found by back substitution
        vPlane[3] = -(vPlane[0] * vPoint3[0] + vPlane[1] * vPoint3[1] + vPlane[2] * vPoint3[2]);
    }

    public static void makeShadowMatrix(float[] vPointOnPlane0, float[] vPointOnPlane1, float[] vPointOnPlane2, float[] vLightPos, float[] destMat) {
        float[] vPlaneEquation = new float[4];
        float dot;

        getPlaneEquation(vPointOnPlane0, vPointOnPlane1, vPointOnPlane2, vPlaneEquation);

        // Dot product of plane and light position
        dot = vPlaneEquation[0]*vLightPos[0] +
              vPlaneEquation[1]*vLightPos[1] +
              vPlaneEquation[2]*vLightPos[2] +
              vPlaneEquation[3]*vLightPos[3];

        // Now do the projection
        // First column
        destMat[0] =   dot - vLightPos[0] * vPlaneEquation[0];
        destMat[4] =  0.0f - vLightPos[0] * vPlaneEquation[1];
        destMat[8] =  0.0f - vLightPos[0] * vPlaneEquation[2];
        destMat[12] = 0.0f - vLightPos[0] * vPlaneEquation[3];

        // Second column
        destMat[1] =  0.0f - vLightPos[1] * vPlaneEquation[0];
        destMat[5] =   dot - vLightPos[1] * vPlaneEquation[1];
        destMat[9] =  0.0f - vLightPos[1] * vPlaneEquation[2];
        destMat[13] = 0.0f - vLightPos[1] * vPlaneEquation[3];

        // Third Column
        destMat[2] =  0.0f - vLightPos[2] * vPlaneEquation[0];
        destMat[6] =  0.0f - vLightPos[2] * vPlaneEquation[1];
        destMat[10] =  dot - vLightPos[2] * vPlaneEquation[2];
        destMat[14] = 0.0f - vLightPos[2] * vPlaneEquation[3];

        // Fourth Column
        destMat[3] =  0.0f - vLightPos[3] * vPlaneEquation[0];
        destMat[7] =  0.0f - vLightPos[3] * vPlaneEquation[1];
        destMat[11] = 0.0f - vLightPos[3] * vPlaneEquation[2];
        destMat[15] =  dot - vLightPos[3] * vPlaneEquation[3];
    }

    public static Texture loadTutorTexture(final int tutorId) {
        if (Graphics.textureTutor != null) {
            Graphics.textureTutor.release();
            Graphics.textureTutor = null;
        }

        int resourceId = -1;
        switch (tutorId) {
            case Tutor_Swipe: resourceId = R.drawable.tutor_swipe; break;
            case Tutor_Goal: resourceId = R.drawable.tutor_goal; break;
            case Tutor_Drag: resourceId = R.drawable.tutor_drag; break;
            case Tutor_Dead: resourceId = R.drawable.tutor_dead; break;
            case Tutor_Moving: resourceId = R.drawable.tutor_moving; break;
            case Tutor_Mover: resourceId = R.drawable.tutor_pusher; break;
            case Tutor_Plain: resourceId = R.drawable.tutor_plain; break;
            case Tutor_MenuPause: resourceId = R.drawable.tutor_menu_pause; break;
            case Tutor_MenuUndo: resourceId = R.drawable.tutor_menu_undo; break;
            case Tutor_MenuHint: resourceId = R.drawable.tutor_menu_hint; break;
            case Tutor_MenuSolvers: resourceId = R.drawable.tutor_menu_solver; break;
        }
        if (resourceId != -1) {
            Graphics.textureTutor = Graphics.loadTexture(resourceId);
        }
        return Graphics.textureTutor;
    }

    private static void initFontsBig() {
        FontStruct[] a = {
            new FontStruct(' ', new Rectangle(1,      0, 30,  82) ),
            new FontStruct('!', new Rectangle(31,     0, 49,  82) ),
            new FontStruct('"', new Rectangle(80,     0, 61,  82) ),
            new FontStruct('#', new Rectangle(141,    0, 76,  82) ),
            new FontStruct('$', new Rectangle(217,    0, 72,  82) ),
            new FontStruct('%', new Rectangle(289,    0, 84,  82) ),
            new FontStruct('&', new Rectangle(373,    0, 76,  82) ),
            new FontStruct('\'', new Rectangle(449,    0, 42,  82) ),
            new FontStruct('(', new Rectangle(491,    0, 46,  82) ),
            new FontStruct(')', new Rectangle(537,    0, 46,  82) ),
            new FontStruct('*', new Rectangle(583,    0, 58,  82) ),
            new FontStruct('+', new Rectangle(641,    0, 76,  82) ),
            new FontStruct(',', new Rectangle(717,    0, 44,  82) ),
            new FontStruct('-', new Rectangle(761,    0, 38,  82) ),
            new FontStruct('.', new Rectangle(799,    0, 44,  82) ),
            new FontStruct('/', new Rectangle(843,    0, 75,  82) ),
            new FontStruct('0', new Rectangle(918,    0, 72,  82) ),
            new FontStruct('1', new Rectangle(0,     83, 72,  82) ),
            new FontStruct('2', new Rectangle(72,    83, 72,  82) ),
            new FontStruct('3', new Rectangle(144,   83, 72,  82) ),
            new FontStruct('4', new Rectangle(216,   83, 72,  82) ),
            new FontStruct('5', new Rectangle(288,   83, 72,  82) ),
            new FontStruct('6', new Rectangle(360,   83, 72,  82) ),
            new FontStruct('7', new Rectangle(432,   83, 72,  82) ),
            new FontStruct('8', new Rectangle(504,   83, 72,  82) ),
            new FontStruct('9', new Rectangle(576,   83, 72,  82) ),
            new FontStruct(':', new Rectangle(648,   83, 44,  82) ),
            new FontStruct(';', new Rectangle(692,   83, 44,  82) ),
            new FontStruct('<', new Rectangle(736,   83, 57,  82) ),
            new FontStruct('=', new Rectangle(793,   83, 76,  82) ),
            new FontStruct('>', new Rectangle(869,   83, 57,  82) ),
            new FontStruct('?', new Rectangle(926,   83, 57,  82) ),
            new FontStruct('@', new Rectangle(0,    166, 79,  82) ),
            new FontStruct('A', new Rectangle(79,   166, 79,  82) ),
            new FontStruct('B', new Rectangle(158,  166, 78,  82) ),
            new FontStruct('C', new Rectangle(236,  166, 81,  82) ),
            new FontStruct('D', new Rectangle(317,  166, 81,  82) ),
            new FontStruct('E', new Rectangle(398,  166, 75,  82) ),
            new FontStruct('F', new Rectangle(473,  166, 73,  82) ),
            new FontStruct('G', new Rectangle(546,  166, 82,  82) ),
            new FontStruct('H', new Rectangle(628,  166, 82,  82) ),
            new FontStruct('I', new Rectangle(710,  166, 46,  82) ),
            new FontStruct('J', new Rectangle(756,  166, 65,  82) ),
            new FontStruct('K', new Rectangle(821,  166, 78,  82) ),
            new FontStruct('L', new Rectangle(899,  166, 72,  82) ),
            new FontStruct('M', new Rectangle(0,    249, 89,  82) ),
            new FontStruct('N', new Rectangle(89,   249, 83,  82) ),
            new FontStruct('O', new Rectangle(172,  249, 82,  82) ),
            new FontStruct('P', new Rectangle(254,  249, 76,  82) ),
            new FontStruct('Q', new Rectangle(330,  249, 82,  82) ),
            new FontStruct('R', new Rectangle(412,  249, 79,  82) ),
            new FontStruct('S', new Rectangle(491,  249, 73,  82) ),
            new FontStruct('T', new Rectangle(564,  249, 69,  82) ),
            new FontStruct('U', new Rectangle(633,  249, 79,  82) ),
            new FontStruct('V', new Rectangle(712,  249, 76,  82) ),
            new FontStruct('W', new Rectangle(788,  249, 101, 82) ),
            new FontStruct('X', new Rectangle(889,  249, 75,  82) ),
            new FontStruct('Y', new Rectangle(0,    332, 72,  82) ),
            new FontStruct('Z', new Rectangle(72,   332, 72,  82) ),
            new FontStruct('[', new Rectangle(144,  332, 46,  82) ),
            new FontStruct('\\', new Rectangle(190, 332, 75,  82) ),
            new FontStruct(']', new Rectangle(265,  332, 46,  82) ),
            new FontStruct('^', new Rectangle(311,  332, 57,  82) ),
            new FontStruct('_', new Rectangle(368,  332, 57,  82) ),
            new FontStruct('`', new Rectangle(425,  332, 57,  82) )
        };

        final float tw = 1024.0f;   // texture width
        final float th = 1024.0f;   // texture height

        TexturedQuad pFont;

        float x, y, w, h;

        for (int i = 0; i < a.length; ++i) {
            x  = a[i].rc.x;
            y  = a[i].rc.y;
            w  = a[i].rc.w;
            h  = a[i].rc.h;

            pFont = new TexturedQuad();
            pFont.ch = a[i].ch;
            pFont.w = w;
            pFont.h = h;

            // x								// y
            pFont.tx_lo_left.x  =     x / tw;	pFont.tx_lo_left.y  = (y+h) / th;	// 0
            pFont.tx_lo_right.x = (x+w) / tw;   pFont.tx_lo_right.y = (y+h) / th;	// 1
            pFont.tx_up_right.x = (x+w) / tw;   pFont.tx_up_right.y =     y / th;	// 2
            pFont.tx_up_left.x  =     x / tw;	pFont.tx_up_left.y  =     y / th;	// 3

            m_fonts_big.put(a[i].ch + "", pFont);
        }
    }

    public static void initFonts() {
        final int arr_size = 65;

        FontStruct[] a = {
            new FontStruct(' ', new Rectangle(1, 0, 20, 41)),
            new FontStruct('!', new Rectangle(21, 0, 24, 41)),
            new FontStruct('"', new  Rectangle(45,    0, 30, 41)),
            new FontStruct( '#', new  Rectangle(75,    0, 38, 41)),
            new FontStruct('$', new  Rectangle(113,   0, 36, 41)),
            new FontStruct('%', new  Rectangle(149,   0, 42, 41)),
            new FontStruct('&', new  Rectangle(191,   0, 38, 41)),
            new FontStruct( '\'', new Rectangle(229,   0, 21, 41)),
            new FontStruct('(', new  Rectangle(250,   0, 23, 41)),
            new FontStruct(')', new  Rectangle(273,   0, 23, 41)),
            new FontStruct('*', new  Rectangle(296,   0, 29, 41)),
            new FontStruct('+', new  Rectangle(325,   0, 38, 41)),
            new FontStruct( ',', new  Rectangle(363,   0, 22, 41)),
            new FontStruct( '-', new  Rectangle(385,   0, 19, 41)),
            new FontStruct( '.', new  Rectangle(404,   0, 22, 41)),
            new FontStruct( '/', new  Rectangle(426,   0, 37, 41)),
            new FontStruct( '0', new  Rectangle(463,   0, 36, 41)),
            new FontStruct( '1', new  Rectangle(0,    42, 36, 41)),
            new FontStruct( '2', new  Rectangle(36,   42, 36, 41)),
            new FontStruct( '3', new  Rectangle(72,   42, 36, 41)),
            new FontStruct( '4', new  Rectangle(108,  42, 36, 41)),
            new FontStruct( '5', new  Rectangle(144,  42, 36, 41)),
            new FontStruct( '6', new  Rectangle(180,  42, 36, 41)),
            new FontStruct( '7', new  Rectangle(216,  42, 36, 41)),
            new FontStruct( '8', new  Rectangle(252,  42, 36, 41)),
            new FontStruct( '9', new  Rectangle(288,  42, 36, 41)),
            new FontStruct( ':', new  Rectangle(324,  42, 22, 41)),
            new FontStruct( ';', new  Rectangle(346,  42, 22, 41)),
            new FontStruct( '<', new  Rectangle(368,  42, 28, 41)),
            new FontStruct( '=', new  Rectangle(396,  42, 38, 41)),
            new FontStruct( '>', new  Rectangle(434,  42, 28, 41)),
            new FontStruct( '?', new  Rectangle(462,  42, 28, 41)),
            new FontStruct( '@', new  Rectangle(0,    84, 39, 41)),
            new FontStruct( 'A', new  Rectangle(39,   84, 39, 41)),
            new FontStruct( 'B', new  Rectangle(78,   84, 39, 41)),
            new FontStruct( 'C', new  Rectangle(117,  84, 40, 41)),
            new FontStruct( 'D', new  Rectangle(157,  84, 40, 41)),
            new FontStruct( 'E', new  Rectangle(197,  84, 37, 41)),
            new FontStruct( 'F', new  Rectangle(234,  84, 36, 41)),
            new FontStruct( 'G', new  Rectangle(270,  84, 41, 41)),
            new FontStruct( 'H', new  Rectangle(311,  84, 41, 41)),
            new FontStruct( 'I', new  Rectangle(348,  84, 30, 41)),
            new FontStruct( 'J', new  Rectangle(375,  84, 32, 41)),
            new FontStruct( 'K', new  Rectangle(407,  84, 39, 41)),
            new FontStruct( 'L', new  Rectangle(446,  84, 36, 41)),
            new FontStruct( 'M', new  Rectangle(0,   126, 44, 41)),
            new FontStruct( 'N', new  Rectangle(44,  126, 41, 41)),
            new FontStruct( 'O', new  Rectangle(85,  126, 41, 41)),
            new FontStruct( 'P', new  Rectangle(126, 126, 38, 41)),
            new FontStruct( 'Q', new  Rectangle(164, 126, 41, 41)),
            new FontStruct( 'R', new  Rectangle(205, 126, 39, 41)),
            new FontStruct( 'S', new  Rectangle(244, 126, 36, 41)),
            new FontStruct( 'T', new  Rectangle(280, 126, 34, 41)),
            new FontStruct( 'U', new  Rectangle(314, 126, 39, 41)),
            new FontStruct( 'V', new  Rectangle(353, 126, 38, 41)),
            new FontStruct( 'W', new  Rectangle(391, 126, 50, 41)),
            new FontStruct( 'X', new  Rectangle(441, 126, 37, 41)),
            new FontStruct( 'Y', new  Rectangle(0,   168, 36, 41)),
            new FontStruct( 'Z', new  Rectangle(36,  168, 36, 41)),
            new FontStruct( '[', new  Rectangle(72,  168, 23, 41)),
            new FontStruct( '\\', new Rectangle(95,  168, 37, 41)),
            new FontStruct( ']', new  Rectangle(132, 168, 23, 41)),
            new FontStruct( '^', new  Rectangle(155, 168, 28, 41)),
            new FontStruct( '_', new  Rectangle(183, 168, 28, 41)),
            new FontStruct( '`', new  Rectangle(211, 168, 28, 41))
        };

        TexturedQuad pFont;
        float x, y, w, h;
        final float tw = 512.0f; // texture width
        final float th = 512.0f; // texture height

        for (int i = 0; i < arr_size; ++i) {
            x  = a[i].rc.x;
            y  = a[i].rc.y;
            w  = a[i].rc.w;
            h  = a[i].rc.h;

            pFont = new TexturedQuad();
            pFont.ch = a[i].ch;
            pFont.w = w;
            pFont.h = h;

            // x								// y
            pFont.tx_lo_left.x  =     x / tw;	pFont.tx_lo_left.y  = (y+h) / th;	// 0
            pFont.tx_lo_right.x = (x+w) / tw;   pFont.tx_lo_right.y = (y+h) / th;	// 1
            pFont.tx_up_right.x = (x+w) / tw;   pFont.tx_up_right.y =     y / th;	// 2
            pFont.tx_up_left.x  =     x / tw;	pFont.tx_up_left.y  =     y / th;	// 3

            m_fonts.put( a[i].ch + "", pFont );
        }
    }

    private static void initNumberFonts() {
        final int[] a = {
//          x      y    w    h
            0,     0, 100, 100,
            100,   0, 100, 100,
            200,   0, 100, 100,
            300,   0, 100, 100,
            400,   0, 100, 100,
            500,   0, 100, 100,
            600,   0, 100, 100,
            700,   0, 100, 100,
            800,   0, 100, 100,
            900,   0, 100, 100,

            0,   100, 100, 100,
            100, 100, 100, 100,
            200, 100, 100, 100,
            300, 100, 100, 100,
            400, 100, 100, 100,
            500, 100, 100, 100,
            600, 100, 100, 100,
            700, 100, 100, 100,
            800, 100, 100, 100,
            900, 100, 100, 100,

            0,   200, 100, 100,
            100, 200, 100, 100,
            200, 200, 100, 100,
            300, 200, 100, 100,
            400, 200, 100, 100,
            500, 200, 100, 100,
            600, 200, 100, 100,
            700, 200, 100, 100,
            800, 200, 100, 100,
            900, 200, 100, 100,

            0,   300, 100, 100,
            100, 300, 100, 100,
            200, 300, 100, 100,
            300, 300, 100, 100,
            400, 300, 100, 100,
            500, 300, 100, 100,
            600, 300, 100, 100,
            700, 300, 100, 100,
            800, 300, 100, 100,
            900, 300, 100, 100,

            0,   400, 100, 100,
            100, 400, 100, 100,
            200, 400, 100, 100,
            300, 400, 100, 100,
            400, 400, 100, 100,
            500, 400, 100, 100,
            600, 400, 100, 100,
            700, 400, 100, 100,
            800, 400, 100, 100,
            900, 400, 100, 100,

            0,   500, 100, 100,
            100, 500, 100, 100,
            200, 500, 100, 100,
            300, 500, 100, 100,
            400, 500, 100, 100,
            500, 500, 100, 100,
            600, 500, 100, 100,
            700, 500, 100, 100,
            800, 500, 100, 100,
            900, 500, 100, 100,
            0,   600, 100, 100
        };

        int index = 1;
        float x, y, w, h;

        final float tw = 1024.0f;    // texture width
        final float th = 1024.0f;    // texture height

        for (int i = 4; i < a.length; i+=4, ++index) {
            x  = a[i];
            y  = a[i+1];
            w  = a[i+2];
            h  = a[i+3];

            TexturedQuad pFont = new TexturedQuad();
            pFont.number = index;

            // x                                y
            pFont.tx_lo_left.x  = x / tw;		pFont.tx_lo_left.y  = (y+h) / th;   // 0 lower left
            pFont.tx_lo_right.x = (x+w) / tw;	pFont.tx_lo_right.y = (y+h) / th;   // 1 lower right
            pFont.tx_up_right.x = (x+w) / tw;	pFont.tx_up_right.y = (y) / th;     // 2 upper righ
            pFont.tx_up_left.x  = x / tw;		pFont.tx_up_left.y  = (y) / th;		// 3 upper left

            m_numbers.put(index, pFont);
        }
    }

    public static void initSymbols() {
        final int max_symbols = 25;

        Rectangle[] a = new Rectangle[max_symbols];

        a[Symbol_Minus]          = new Rectangle(  0,   0, 170, 170);
        a[Symbol_Plus]           = new Rectangle(170,   0, 170, 170);
        a[Symbol_Info]           = new Rectangle(340,   0, 170, 170);
        a[Symbol_Lock]           = new Rectangle(  0, 170, 170, 170);
        a[Symbol_Questionmark]   = new Rectangle(170, 170, 170, 170);
        a[Symbol_GoLeft]         = new Rectangle(340, 340, 170, 170);
        a[Symbol_GoRight]        = new Rectangle(340, 170, 170, 170);
        a[Symbol_GoUp]           = new Rectangle(  0, 340, 170, 170);
        a[Symbol_GoDown]         = new Rectangle(170, 340, 170, 170);
        a[Symbol_Undo]           = new Rectangle(510,   0, 170, 170);
        a[Symbol_Solver]         = new Rectangle(510, 170, 170, 170);
        a[Symbol_Pause]          = new Rectangle(510, 340, 170, 170);
        a[Symbol_1Star]          = new Rectangle(680, 340, 170, 170);
        a[Symbol_2Star]          = new Rectangle(680, 170, 170, 170);
        a[Symbol_3Star]          = new Rectangle(680,   0, 170, 170);
        a[Symbol_Star]           = new Rectangle(850,   0, 170, 170);
        a[Symbol_TriangleUp]     = new Rectangle(  0, 510, 170, 170);
        a[Symbol_TriangleDown]   = new Rectangle(170, 510, 170, 170);
        a[Symbol_Hilite]         = new Rectangle(510, 510, 170, 170);
        a[Symbol_Death]          = new Rectangle(680, 510, 170, 170);
        a[Symbol_Lightning]      = new Rectangle(850, 510, 170, 170);
        a[Symbol_Cracks]         = new Rectangle(  0, 680, 170, 170);
        a[Symbol_TriangleLeft]   = new Rectangle(170, 680, 170, 170);
        a[Symbol_TriangleRight]  = new Rectangle(340, 680, 170, 170);
        a[Symbol_Solved]         = new Rectangle(850, 170, 170, 170);

        final float tw = 1024.0f;
        final float th = 1024.0f;

        float x, y, w, h;

        for (int i = 0; i < max_symbols; ++i) {
            x  = a[i].x;
            y  = a[i].y;
            w  = a[i].w;
            h  = a[i].h;

            TexturedQuad pFont = new TexturedQuad();
            pFont.number = i;
            pFont.w = w;
            pFont.h = h;

            // x								y
            pFont.tx_lo_left.x  = x     / tw;	pFont.tx_lo_left.y  = (y+h) / th;
            pFont.tx_lo_right.x = (x+w) / tw;  pFont.tx_lo_right.y = (y+h) / th;
            pFont.tx_up_right.x = (x+w) / tw;  pFont.tx_up_right.y = y     / th;
            pFont.tx_up_left.x  = x     / tw;	pFont.tx_up_left.y  = y     / th;

            m_symbols.put(i, pFont);
        }
    }

    public static void updateDisplayedSolvers() {
        if (solvers != null) {
            int solver = GameOptions.getSolverCount();
            solvers.updateSolversCount(solver);
        }

        if (level != null) {
            level.setSolversCount();
        }
    }

    public static void musicVolumeUp() {
        float volume = GameOptions.getMusicVolume();

        volume += 0.1f;
        if (volume > 1.0f) {
            volume = 1.0f;
        }

        GameOptions.setMusicVolume(volume);
        Audio.setMusicVolume(volume);
    }

    public static void musicVolumeDown() {
        float volume = GameOptions.getMusicVolume();

        volume -= 0.1f;
        if (volume < 0.0f) {
            volume = 0.0f;
        }

        GameOptions.setMusicVolume(volume);
        Audio.setMusicVolume(volume);
    }

    public static void soundVolumeUp() {
        float volume = GameOptions.getSoundVolume();

        volume += 0.1f;
        if (volume > 1.0f) {
            volume = 1.0f;
        }

        GameOptions.setSoundVolume(volume);
        Audio.setSoundVolume(volume);
    }

    public static void soundVolumeDown() {
        float volume = GameOptions.getSoundVolume();

        volume -= 0.1f;
        if (volume < 0.0f) {
            volume = 0.0f;
        }

        GameOptions.setSoundVolume(volume);
        Audio.setSoundVolume(volume);
    }

    public static void drawCubeNoFace(  boolean x_plus,
                                        boolean x_minus,
                                        boolean y_plus,
                                        boolean y_minus,
                                        boolean z_plus,
                                        boolean z_minus) {
        if (z_minus) {
            Graphics.drawCubeFaceZ_Minus();
        }

        if (z_plus) {
            Graphics.drawCubeFaceZ_Plus();
        }

        if (x_plus) {
            Graphics.drawCubeFaceX_Plus();
        }

        if (y_minus) {
            Graphics.drawCubeFaceY_Minus();
        }

        if (x_minus) {
            Graphics.drawCubeFaceX_Minus();
        }

        if (y_plus){
            Graphics.drawCubeFaceY_Plus();
        }
    }

    public static void setAnimFaces(DifficultyEnum difficulty, int levelNumber) {
        AnimInitData animInitData = Game.animInitData;
        switch (difficulty) {
            case Easy:
                if (levelNumber >= 1 && levelNumber <= 15) {
                    animInitData.setFaces(CubeFaceNames.Face_Easy01, CubeFaceNames.Face_Easy04, CubeFaceNames.Face_Menu);
                }

                if (levelNumber >= 16 && levelNumber <= 30) {
                    animInitData.setFaces(CubeFaceNames.Face_Easy02, CubeFaceNames.Face_Easy01, CubeFaceNames.Face_Empty);
                }

                if (levelNumber >= 31 && levelNumber <= 45) {
                    animInitData.setFaces(CubeFaceNames.Face_Easy03, CubeFaceNames.Face_Easy02, CubeFaceNames.Face_Empty);
                }

                if (levelNumber >= 46 && levelNumber <= 60) {
                    animInitData.setFaces(CubeFaceNames.Face_Easy04, CubeFaceNames.Face_Easy03, CubeFaceNames.Face_Empty);
                }
                break;

            case Normal:
                if (levelNumber >= 1 && levelNumber <= 15) {
                    animInitData.setFaces(CubeFaceNames.Face_Normal01, CubeFaceNames.Face_Normal04, CubeFaceNames.Face_Easy01);
                }

                if (levelNumber >= 16 && levelNumber <= 30) {
                    animInitData.setFaces(CubeFaceNames.Face_Normal02, CubeFaceNames.Face_Normal01, CubeFaceNames.Face_Empty);
                }

                if (levelNumber >= 31 && levelNumber <= 45) {
                    animInitData.setFaces(CubeFaceNames.Face_Normal03, CubeFaceNames.Face_Normal02, CubeFaceNames.Face_Empty);
                }

                if (levelNumber >= 46 && levelNumber <= 60) {
                    animInitData.setFaces(CubeFaceNames.Face_Normal04, CubeFaceNames.Face_Normal03, CubeFaceNames.Face_Empty);
                }
                break;

            case Hard:
                if (levelNumber >= 1 && levelNumber <= 15) {
                    animInitData.setFaces(CubeFaceNames.Face_Hard01, CubeFaceNames.Face_Hard04, CubeFaceNames.Face_Normal01);
                }

                if (levelNumber >= 16 && levelNumber <= 30) {
                    animInitData.setFaces(CubeFaceNames.Face_Hard02, CubeFaceNames.Face_Hard01, CubeFaceNames.Face_Empty);
                }

                if (levelNumber >= 31 && levelNumber <= 45) {
                    animInitData.setFaces(CubeFaceNames.Face_Hard03, CubeFaceNames.Face_Hard02, CubeFaceNames.Face_Empty);
                }

                if (levelNumber >= 46 && levelNumber <= 60) {
                    animInitData.setFaces(CubeFaceNames.Face_Hard04, CubeFaceNames.Face_Hard03, CubeFaceNames.Face_Empty);
                }
                break;
        }

    }

    public static void updateHiLitedCubes(ArrayList<MenuCube> menuCubes) {
        hiliteTimeout -= 0.01f;

        if (hiliteTimeout < 0.0f) {
            hiliteTimeout = 0.02f;

            MenuCube menuCube;
            int size = menuCubes.size();
            for(int i = 0; i < size; ++i) {
                menuCube = menuCubes.get(i);
                if (!menuCube.cubesToHilite.isEmpty()) {
                    Cube cube = menuCube.cubesToHilite.get(0);
                    menuCube.cubesToHilite.remove(cube);
                    cube.colorCurrent.init(Game.cubeHiLiteColor);
                }
            }
        }
    }
}

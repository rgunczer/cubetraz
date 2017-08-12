package com.almagems.cubetraz.game;

import com.almagems.cubetraz.R;
import com.almagems.cubetraz.utils.Starfield;
import com.almagems.cubetraz.math.Vector;
import com.almagems.cubetraz.math.Vector2;
import com.almagems.cubetraz.builder.LevelBuilder;
import com.almagems.cubetraz.cubes.Cube;
import com.almagems.cubetraz.cubes.CubeFaceData;
import com.almagems.cubetraz.cubes.CubePos;
import com.almagems.cubetraz.cubes.DeadCube;
import com.almagems.cubetraz.cubes.MoverCube;
import com.almagems.cubetraz.cubes.MovingCube;
import com.almagems.cubetraz.graphics.Color;
import com.almagems.cubetraz.graphics.FontStruct;
import com.almagems.cubetraz.graphics.Graphics;
import com.almagems.cubetraz.graphics.Rectangle;
import com.almagems.cubetraz.graphics.Text;
import com.almagems.cubetraz.graphics.TexturedQuad;
import com.almagems.cubetraz.scenes.anim.AnimInitData;
import com.almagems.cubetraz.scenes.anim.Animator;
import com.almagems.cubetraz.scenes.Intro;
import com.almagems.cubetraz.scenes.level.Creator;
import com.almagems.cubetraz.scenes.level.HUD;
import com.almagems.cubetraz.scenes.level.Level;
import com.almagems.cubetraz.scenes.menu.Menu;
import com.almagems.cubetraz.scenes.Outro;
import com.almagems.cubetraz.scenes.Scene;
import com.almagems.cubetraz.scenes.stat.StatInitData;
import com.almagems.cubetraz.scenes.stat.Statistics;
import com.almagems.cubetraz.scenes.level.LevelInitData;
import com.almagems.cubetraz.scenes.menu.MenuInitData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static android.opengl.GLES10.*;
import static com.almagems.cubetraz.game.Constants.*;


public final class Game {

    public static Graphics graphics;
    public static Cube[][][] cubes = new Cube[MAX_CUBE_COUNT][MAX_CUBE_COUNT][MAX_CUBE_COUNT];

    public static GameOptions options;
    public static GameProgress progress;

    // audio
    public static Audio audio;

    // scenes
    public static Scene currentScene;
    public static Intro intro;
    public static Menu menu;
    public static Animator animator;
    public static Level level;
    public static Statistics statistics;
    public static Outro outro;

    public static float dirtyAlpha;
    public static float minSwipeLength;
    public static TexturedQuad m_newlinefont = new TexturedQuad();

    public static StatInitData stat_init_data = new StatInitData();

    public static final MenuInitData menu_init_data = new MenuInitData();
    public static final AnimInitData anim_init_data = new AnimInitData();
    public static final LevelInitData level_init_data = new LevelInitData();

    public static final Vector cube_offset = new Vector();
    public static CubeFaceData[] ar_cubefacedata = new CubeFaceData[6];

    public static boolean canPlayLockedLevels = true;

    static {
        for(int i = 0; i < MAX_CUBE_COUNT; ++i) {
            for(int j = 0; j < MAX_CUBE_COUNT; ++j) {
                for(int k = 0; k < MAX_CUBE_COUNT; ++k)
                    cubes[i][j][k] = new Cube();
            }
        }
        for (int i = 0; i < 6; ++i) {
            ar_cubefacedata[i] = new CubeFaceData();
        }

        options = new GameOptions(Engine.activity);
        options.load();

        progress = new GameProgress(Engine.activity);
        progress.load();

        audio = new Audio();
        audio.init(Engine.activity, options.getMusicVolume(), options.getSoundVolume());
    }

    // ctor
    public Game() {

    }

    public void init() {
        graphics = Engine.graphics;

        Text.graphics = graphics;
        HUD.graphics = graphics;
        Scene.graphics = graphics;
        MovingCube.graphics = graphics;
        MoverCube.graphics = graphics;
        DeadCube.graphics = graphics;
        Starfield.graphics = graphics;

        minSwipeLength = Graphics.height / 10;

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
        cube_offset.x = cube_offset.y = cube_offset.z = size / -2.0f;

        Creator.createCubes();

        level = new Level();
        menu = new Menu();

        LevelBuilder.level = level;

        showScene(Scene_Intro);
        //showScene(Scene_Menu);
        //showScene(Scene_Anim);
        //showScene(Scene_Level);
        //showScene(Scene_Stat);
        //showScene(Scene_Outro);

        graphics.warmCache();
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

    public static String getLevelTypeAndNumberString(DifficultyEnum difficulty, int level_number) {
        String str = "";
        switch (difficulty) {
            case Easy: str = "EASY-" + level_number + " " + (progress.getSolvedEasy(level_number) ? "\nSOLVED" : ""); break;
            case Normal: str = "NORMAL-" + level_number + " " + (progress.getSolvedNormal(level_number) ? "\nSOLVED" : ""); break;
            case Hard: str = "HARD-" + level_number + " " + (progress.getSolvedHard(level_number) ? "\nSOLVED" : ""); break;
        }
        return str;
    }

    public static void levelComplete() {
        if (level != null) {
            level.eventLevelComplete();
        }
    }

    public static int getSolverCount() {
        return 12;
    }

    public static boolean isObstacle(CubePos cube_pos) {
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

    public static Map<String, TexturedQuad> m_fonts = new HashMap<String, TexturedQuad>();
    public static Map<String, TexturedQuad> m_fonts_big = new HashMap<String, TexturedQuad>();
    public static Map<Integer, TexturedQuad> m_numbers = new HashMap<Integer, TexturedQuad>();
    public static Map<Integer, TexturedQuad> m_symbols = new HashMap<Integer, TexturedQuad>();

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

    public boolean isPlaying() {
        return currentScene == level;
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

                    cube.resetFonts();
                    cube.resetSymbols();
                }
            }
        }
    }

    public static  void clearCubeFaceData() {
        for (int i = 0; i < 6; ++i) {
            ar_cubefacedata[i].clear();
        }
        resetCubesFonts();
    }

    public static Color getFaceColor(float alpha) { return new Color( (int)(0.75f * 255), (int)(0.8f * 255), (int)(0.75f * 255), (int)(alpha * 255)); }
    public static Color getBaseColor() { return new Color(255, 255, 255, 255); }
    public static Color getTitleColor() { return new Color(139, 0, 0, 200); }
    public static Color getTextColor() { return new Color(80, 0, 0, 130);  }
    public static Color getSymbolColor() { return new Color(76, 0, 0, 150);  }
    public static Color getTextColorOnCubeFace() { return new Color(76, 0, 0, 153); }
    public static Color getLockedLevelNumberColor() { return new Color(0, 0, 0, 60); }
    public static Color getLevelNumberColor() { return new  Color(0, 0, 0, 150); }

    public static void showScene(int scene_id) {
        Scene scene = null;

        switch (scene_id) {
            case Scene_Intro:
                intro = new Intro();
                intro.init();
                scene = intro;
                break;

            case Scene_Menu:
                //showFullScreenAd();
                intro = null;

                if (outro != null) {
                    outro = null;
                    menu_init_data.reappear = false;
                }
                menu.init();
                scene = menu;
                break;

            case Scene_Anim:
                if (animator == null) {
                    animator = new Animator();
                }
                animator.init();
                scene = animator;
                break;

            case Scene_Level:
                level.init();
                scene = level;
                break;

            case Scene_Stat:
                if (statistics == null) {
                    statistics = new Statistics();
                }
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
        }
        currentScene = scene;
    }

    public static Vector2 rotate(Vector2 a, float degree) {
        float angle = (float)Math.toRadians(degree);
        float c = (float)Math.cos(angle);
        float s = (float)Math.sin(angle);
        return new Vector2(c*a.x - s*a.y, s*a.x + c*a.y);
    }

    public static TexturedQuad getNewLineFont() {
        return m_newlinefont;
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
        //printf("\nNum of Visible Cubes: %lu", m_list_cubes_to_draw.size());
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
                                cubes[x][y][z].setColor( getFaceColor(1f) );
                        }
                    }
                }
            }
        }
    }

    public static Vector getCubePosAt(CubePos cube_pos) {
        Vector pos = new Vector();

        pos.x = cubes[cube_pos.x][cube_pos.y][cube_pos.z].tx;
        pos.y = cubes[cube_pos.x][cube_pos.y][cube_pos.z].ty;
        pos.z = cubes[cube_pos.x][cube_pos.y][cube_pos.z].tz;

        return pos;
    }

    public static void renderToFBO(Scene scene) {
        Graphics.saveOriginalFBO();

        Graphics.fbo.bind();
        //glClearColor(1f, 0f, 0f, 0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        scene.renderToFBO();
        //glClearColor(0f, 0f, 0f, 0f);
        Graphics.restoreOriginalFBO();
    }

	public void onSurfaceChanged(int width, int height) {
        //loading.init();
    }

    public void handleButton() {

    }

    public void showMenu() {

    }

	public void update() {
        currentScene.update();
    }

	public void draw() {
        currentScene.render();
	}

	public void handleTouchPress(float normalizedX, float normalizedY, int fingerCount) {
        currentScene.onFingerDown(normalizedX, normalizedY, fingerCount);
    }

	public void handleTouchDrag(float normalizedX, float normalizedY, int fingerCount) {
        currentScene.onFingerMove(0f, 0f, normalizedX, normalizedY, fingerCount);
    }

	public void handleTouchRelease(float normalizedX, float normalizedY) {
        currentScene.onFingerUp(normalizedX, normalizedY, 1);
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
                            cubes[x][y][z].setColor(Color.WHITE);
                        }
                    }
                }
            }
        }

        System.out.println("Number of Visible Cubes: " + number_of_visible_cubes);
        System.out.println("Number of Invisible Cubes: " + number_of_invisible_cubes);
    }

    public static void setCubeTypeOnFace(Cube cube, char ch, int face_type, CubeFaceNamesEnum face_id) {
        int level_number = -1;

        switch (face_id) {
            case Face_Easy01:
            case Face_Normal01:
            case Face_Hard01:
                level_number = 0;
                break;

            case Face_Easy02:
            case Face_Normal02:
            case Face_Hard02:
                level_number = 15;
                break;

            case Face_Easy03:
            case Face_Normal03:
            case Face_Hard03:
                level_number = 30;
                break;

            case Face_Easy04:
            case Face_Normal04:
            case Face_Hard04:
                level_number = 45;
                break;

            default:
                break;
        }

        switch (ch) {
            case ' ': cube.type = CubeTypeEnum.CubeIsInvisible; return;
            case 'x': cube.type = CubeTypeEnum.CubeIsVisibleAndObstacle; return;
            case '1': level_number+=1; break;
            case '2': level_number+=2; break;
            case '3': level_number+=3; break;
            case '4': level_number+=4; break;
            case '5': level_number+=5; break;
            case '6': level_number+=6; break;
            case '7': level_number+=7; break;
            case '8': level_number+=8; break;
            case '9': level_number+=9; break;
            case 'A': level_number+=10; break;
            case 'B': level_number+=11; break;
            case 'C': level_number+=12; break;
            case 'D': level_number+=13; break;
            case 'E': level_number+=14; break;
            case 'F': level_number+=15; break;
            default:
                //printf("\nERROR: Unknown cube type on face.");
                return;
        } // switch

        cube.type = CubeTypeEnum.CubeIsInvisibleAndObstacle;
        //printf("\nLevel Cube at: (%d,%d,%d)", pCube.x, pCube.y, pCube.z);
        Creator.addLevelCube(level_number, face_type, face_id, cube.x, cube.y, cube.z);
    }

    public static void setCubeTypeInvisible(CubePos cube_pos) {
        //printf("\nSet Cube Type Invisible (%d, %d, %d)", cube_pos.x, cube_pos.y, cube_pos.z);
        cubes[cube_pos.x][cube_pos.y][cube_pos.z].type = CubeTypeEnum.CubeIsInvisible;
    }

    public static boolean isPlayerCube(CubePos cube_pos) {
        CubePos cp = level.mPlayerCube.getCubePos();
        if (cp.x == cube_pos.x && cp.y == cube_pos.y && cp.z == cube_pos.z) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isKeyCube(CubePos cube_pos) {
        CubePos cp = level.getKeyPos();
        if (cp.x == cube_pos.x && cp.y == cube_pos.y && cp.z == cube_pos.z) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isCubeOnAList(Cube cube, ArrayList<Cube> lst) {
        return lst.contains(cube);
    }

    public static int countOnAList(Cube theCube, ArrayList<Cube> lst) {
        int count = 0;
        int size = lst.size();
        Cube cube;

        for (int i = 0; i < size; ++i) {
            cube = lst.get(i);

            if (cube == theCube) {
                ++count;
            }
        }
        return count;
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

        graphics.addVerticesCoordsNormalsColors(verts, coords, norms, colors);
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

    public static void showFullScreenAd() {
        //m_resourceManager.ShowFullScreenAd();
    }

    public static boolean getAdsFlag() {
        return false;
    }

    public static int loadTutorTexture(final String name) {
        if (Graphics.texture_id_tutor != 0) {
            int[] arr = {Graphics.texture_id_tutor};
            glDeleteTextures(arr.length, arr, 0);
        }

        if (name.equals("tutor_swipe")) {
            Graphics.texture_id_tutor = graphics.loadTexture(R.drawable.tutor_swipe);
        } else if (name.equals("tutor_goal")) {
            Graphics.texture_id_tutor = graphics.loadTexture(R.drawable.tutor_goal);
        } else if (name.equals("tutor_drag")) {
            Graphics.texture_id_tutor = graphics.loadTexture(R.drawable.tutor_drag);
        } else if (name.equals("tutor_dead")) {
            Graphics.texture_id_tutor = graphics.loadTexture(R.drawable.tutor_dead);
        } else if (name.equals("tutor_moving")) {
            Graphics.texture_id_tutor = graphics.loadTexture(R.drawable.tutor_moving);
        } else if (name.equals("tutor_pusher")) {
            Graphics.texture_id_tutor = graphics.loadTexture(R.drawable.tutor_pusher);
        } else if (name.equals("tutor_plain")) {
            Graphics.texture_id_tutor = graphics.loadTexture(R.drawable.tutor_plain);
        } else if (name.equals("tutor_menu_pause")) {
            Graphics.texture_id_tutor = graphics.loadTexture(R.drawable.tutor_menu_pause);
        } else if (name.equals("tutor_menu_undo")) {
            Graphics.texture_id_tutor = graphics.loadTexture(R.drawable.tutor_menu_undo);
        } else if (name.equals("tutor_menu_hint")) {
            Graphics.texture_id_tutor = graphics.loadTexture(R.drawable.tutor_menu_hint);
        } else if (name.equals("tutor_menu_solver")) {
            Graphics.texture_id_tutor = graphics.loadTexture(R.drawable.tutor_menu_solver);
        } else {
            // bad!
        }
        return Graphics.texture_id_tutor;
    }

    public static void initFontsBig() {
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

        a[SymbolMinus]          = new Rectangle(  0,   0, 170, 170);
        a[SymbolPlus]           = new Rectangle(170,   0, 170, 170);
        a[SymbolInfo]           = new Rectangle(340,   0, 170, 170);
        a[SymbolLock]           = new Rectangle(  0, 170, 170, 170);
        a[SymbolQuestionmark]   = new Rectangle(170, 170, 170, 170);
        a[SymbolGoLeft]         = new Rectangle(340, 340, 170, 170);
        a[SymbolGoRight]        = new Rectangle(340, 170, 170, 170);
        a[SymbolGoUp]           = new Rectangle(  0, 340, 170, 170);
        a[SymbolGoDown]         = new Rectangle(170, 340, 170, 170);
        a[SymbolUndo]           = new Rectangle(510,   0, 170, 170);
        a[SymbolSolver]         = new Rectangle(510, 170, 170, 170);
        a[SymbolPause]          = new Rectangle(510, 340, 170, 170);
        a[Symbol1Star]          = new Rectangle(680, 340, 170, 170);
        a[Symbol2Star]          = new Rectangle(680, 170, 170, 170);
        a[Symbol3Star]          = new Rectangle(680,   0, 170, 170);
        a[SymbolStar]           = new Rectangle(850,   0, 170, 170);
        a[SymbolTriangleUp]     = new Rectangle(  0, 510, 170, 170);
        a[SymbolTriangleDown]   = new Rectangle(170, 510, 170, 170);
        a[SymbolHilite]         = new Rectangle(510, 510, 170, 170);
        a[SymbolDeath]          = new Rectangle(680, 510, 170, 170);
        a[SymbolLightning]      = new Rectangle(850, 510, 170, 170);
        a[SymbolCracks]         = new Rectangle(  0, 680, 170, 170);
        a[SymbolTriangleLeft]   = new Rectangle(170, 680, 170, 170);
        a[SymbolTriangleRight]  = new Rectangle(340, 680, 170, 170);
        a[SymbolSolved]         = new Rectangle(850, 170, 170, 170);

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
//        if (null != m_solvers) {
//            //m_solvers.UpdateSolversCount();
//        }
//
//        if (null != m_level) {
//            level.setSolversCount();
//        }
    }

    public static void setTextureID(int id) {
        //m_texture_id = id;
    }

    public static void setSolverCount(int count) {
        //return m_resourceManager.SetSolvers(count);
    }

    public static void decSolverCount() {
        //m_resourceManager.SetSolvers(m_resourceManager.GetSolvers() - 1);
    }

    public static void musicVolumeUp() {
        float volume = options.getMusicVolume();

        volume += 0.1f;
        if (volume > 1.0f) {
            volume = 1.0f;
        }

        options.setMusicVolume(volume);
        audio.setMusicVolume(volume);
    }

    public static void musicVolumeDown() {
        float volume = options.getMusicVolume();

        volume -= 0.1f;
        if (volume < 0.0f) {
            volume = 0.0f;
        }

        options.setMusicVolume(volume);
        audio.setMusicVolume(volume);
    }

    public static void soundVolumeUp() {
        float volume = options.getSoundVolume();

        volume += 0.1f;
        if (volume > 1.0f) {
            volume = 1.0f;
        }

        options.setSoundVolume(volume);
    }

    public static void soundVolumeDown() {
        float volume = options.getSoundVolume();

        volume -= 0.1f;
        if (volume < 0.0f) {
            volume = 0.0f;
        }

        options.setSoundVolume(volume);
    }

    public static void enteredBackground() {
        if (currentScene != null) {
            //currentScene.enteredBackground();
        }
    }

    public static void enteredForeground() {
        if (currentScene != null) {
            //m_scene.enteredForeground();
        }
    }


    public static void drawCubeNoFace(  boolean x_plus,
                                        boolean x_minus,
                                        boolean y_plus,
                                        boolean y_minus,
                                        boolean z_plus,
                                        boolean z_minus) {
        if (z_minus) {
            graphics.drawCubeFaceZ_Minus();
        }

        if (z_plus) {
            graphics.drawCubeFaceZ_Plus();
        }

        if (x_plus) {
            graphics.drawCubeFaceX_Plus();
        }

        if (y_minus) {
            graphics.drawCubeFaceY_Minus();
        }

        if (x_minus) {
            graphics.drawCubeFaceX_Minus();
        }

        if (y_plus){
            graphics.drawCubeFaceY_Plus();
        }
    }




    public static void resetHelp() {
        //printf("\nReset Help Here...\n");
    }

    public static float getTextWidth(final String text, float scale) {
        float width = 0.0f;

        int len = text.length();
        TexturedQuad pFont;

        for (int i = 0; i < len; ++i) {
            pFont = getFont(text.charAt(i));
            width += (pFont.w * scale);
        }

        return width;
    }

    public static void drawFullScreenQuad(Color color) {
        final float[] verts = {
            0.0f,              graphics.height,
            0.0f,              0.0f,
            graphics.width,    0.0f,
            graphics.width,    graphics.height
        };

        final float[] colors = {
            color.r, color.g, color.b, color.a,
            color.r, color.g, color.b, color.a,
            color.r, color.g, color.b, color.a,
            color.r, color.g, color.b, color.a
        };

        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);

        // glVertexPointer(2, GL_FLOAT, 0, verts);
        // glColorPointer(4, GL_UNSIGNED_BYTE, 0, colors);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
    }




}

package com.almagems.cubetraz;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static android.opengl.GLES10.*;
import static com.almagems.cubetraz.Constants.*;


public final class Game {


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
            case Easy: str = "EASY-" + level_number + " " + (Cubetraz.getSolvedEasy(level_number) ? "\nSOLVED" : ""); break;
            case Normal: str = "NORMAL-" + level_number + " " + (Cubetraz.getSolvedNormal(level_number) ? "\nSOLVED" : ""); break;
            case Hard: str = "HARD-" + level_number + " " + (Cubetraz.getSolvedHard(level_number) ? "\nSOLVED" : ""); break;
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

        if (cube.type == CubeTypeEnum.CubeIsVisibleAndObstacle || cube.type == CubeTypeEnum.CubeIsInvisibleAndObstacle || cube.type == CubeTypeEnum.CubeIsVisibleAndObstacleAndLevel) {
            return true;
        } else {
            return false;
        }
    }

    public static void playMusic(String string) {

    }

    public static StatInitData stat_init_data = new StatInitData();

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


    public static Menu menu;

    public static boolean isOnAList(Cube theCube, ArrayList<Cube> lst) {
        int size = lst.size();
        Cube cube;
        for (int i = 0; i < size; ++i) {
            cube = lst.get(i);
            if ( cube == theCube ) {
                return true;
            }
        }
        return false;
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


    public static void musicVolumeUp() {

    }

    public static void musicVolumeDown() {

    }

    public static void soundVolumeUp() {

    }

    public static void soundVolumeDown() {

    }

    public static Color getFaceColor(float alpha) { return new Color(0.75f * 255, 0.8f * 255, 0.75f * 255, alpha * 255); }
    public static Color getBaseColor() { return new Color(255, 255, 255, 255); }
    public static Color getTitleColor() { return new Color(139, 0, 0, 200); }
    public static Color getTextColor() { return new Color(80, 0, 0, 130);  }
    public static Color getSymbolColor() { return new Color(76, 0, 0, 150);  }
    public static Color getTextColorOnCubeFace() { return new Color(76, 0, 0, 153); }
    public static Color getLockedLevelNumberColor() { return new Color(0, 0, 0, 60); }
    public static Color getLevelNumberColor() { return new  Color(0, 0, 0, 150); }



    public static void showScene(int scene_id) {
        Scene scene = null;

//        switch (scene_id) {
//            case Scene_Intro:
//                m_intro = new cIntro();
//                m_intro.init();
//
//                scene = m_intro;
//                break;
//
//            case Scene_Menu:
//                //showFullScreenAd();
//
//                if (m_intro) {
//                    delete m_intro;
//                    m_intro = NULL;
//                }
//
//                if (m_outro) {
//                    delete m_outro;
//                    m_outro = NULL;
//                    engine->menu_init_data.reappear = false;
//                }
//
//                if (NULL == m_menu)
//                    m_menu = new cMenu();
//
//                m_menu->Init();
//
//                scene = m_menu;
//                break;
//
//            case Scene_Anim:
//
//                if (NULL == m_animator)
//                    m_animator = new cAnimator();
//
//                m_animator->Init();
//
//                scene = m_animator;
//                break;
//
//            case Scene_Level:
//
//                m_level->Init(&level_init_data);
//
//                scene = m_level;
//                break;
//
//            case Scene_Stat:
//
//                if (NULL == m_statistics)
//                    m_statistics = new cStatistics();
//
//                m_statistics->Init();
//
//                scene = m_statistics;
//                break;
//
//            case Scene_Solvers:
//
//                m_solvers->Init();
//
//                scene = m_solvers;
//                break;
//
//            case Scene_Outro:
//
//                if (NULL == m_outro)
//                    m_outro = new cOutro();
//
//                m_outro->Init();
//
//                scene = m_outro;
//                break;
//        }
//
//        m_scene = scene;
    }


    public static final Level level = new Level();

    public static final LevelInitData level_init_data = new LevelInitData();
    public static final AnimInitData anim_init_data = new AnimInitData();

    public static void stopMusic() {

    }

    public static void playSound(String soundName) {

    }

    public static boolean getCanPlayLockedLevels() {
        return true;
    }

    public static void hideProgressIndicator() {

    }

    public static float dirty_alpha;

    public static final Vector cube_offset = new Vector();

    public static float getMusicVolume() {
        return 0.5f;
    }

    public static float getSoundVolume() {
        return 0.5f;
    }

    public static Vector getCubePosAt(int x, int y, int z) {
        Vector pos = new Vector();
        pos.x = cubes[x][y][z].tx;
        pos.y = cubes[x][y][z].ty;
        pos.z = cubes[x][y][z].tz;
        return pos;
    }


    public static CubeFaceData[] ar_cubefacedata = new CubeFaceData[6];
    public static MenuInitData menu_init_data = new MenuInitData();

    public enum GameState {
        Loading,
        Menu,
        Stats,
        Playing,
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
                                cubes[x][y][z].setColor( getFaceColor() );
                        }
                    }
                }
            }
        }
    }




    public GameState gameState;

    public static Cube[][][] cubes = new Cube[MAX_CUBE_COUNT][MAX_CUBE_COUNT][MAX_CUBE_COUNT];

    private boolean initialized;


    private Graphics graphics;


	private float elapsedTimeSelMarkerAnim;

	private boolean editorEnabled;

	private final PositionInfo pos;

    static {
        for(int i = 0; i < MAX_CUBE_COUNT; ++i) {
            for(int j = 0; j < MAX_CUBE_COUNT; ++j) {
                for(int k = 0; k < MAX_CUBE_COUNT; ++k)
                    cubes[i][j][k] = new Cube();
            }
        }
    }

    // ctor
	public Game() {
        initialized = false;
        gameState = GameState.Loading;
        elapsedTimeSelMarkerAnim = 0f;
        editorEnabled = false;
        pos = new PositionInfo();
    }

    public void init() {
        graphics = Engine.graphics;

//        StatSectionBase.graphics = graphics;
//        SingleColoredQuad.graphics = graphics;
//        ColoredQuad.graphics = graphics;
//
//
//        background = new Quad();
//        loading = new Loading();
    }

    public void createObjects() {
        if (initialized) {
            return;
        }

        initialized = true;

//        scoreCounter = new ScoreCounter();
//		animManager = new AnimationManager();





//        menu = new Menu();
//        stats = new Stats();
//
//        hud = new HUD();
//        hud.init();
//        hud.reset();
//        hud.updateScore(scoreCounter.score);


//        loading.init();
//        menu.init();
	}

    public static void renderToFBO(Scene scene) {
        //graphics.fboBackground.bind();
        //glViewport(0, 0, graphics.fboBackground.getWidth(), graphics.fboBackground.getHeight());

        // regular render
        glClearColor(0f, 0f, 0f, 0f);
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);

        if (false) { // render test
            glDisable(GL_DEPTH_TEST);
            glDisable(GL_BLEND);

            //graphics.setProjectionMatrix2D();
            //graphics.updateViewProjMatrix();

            // custom drawing
            //graphics.bindNoTexture();
            glDisable(GL_TEXTURE_2D);

            //Color color = new Color(0f, 0f, 0f);
            //EdgeDrawer edgeDrawer = new EdgeDrawer(10);
            //edgeDrawer.begin();

            //System.out.println("box2d " + edge.m_vertex1.x + ", " + edge.m_vertex1.y + ", " + edge.m_vertex2.x + ", " + edge.m_vertex2.y);

//            edgeDrawer.addLine(-0.5f, 0.5f, 0f, 0.5f, 0.0f, 0f);
//            edgeDrawer.addLine(0.5f, 0f, 0f, -0.5f, -0.5f, 0f);
//            edgeDrawer.addLine(-0.5f, -0.5f, 0f, -0.5f, 0.5f, 0f);

//            setIdentityM(graphics.modelMatrix, 0);
//            multiplyMM(graphics.mvpMatrix, 0, graphics.viewProjectionMatrix, 0, graphics.modelMatrix, 0);
//
//            graphics.singleColorShader.useProgram();
//            graphics.singleColorShader.setUniforms(graphics.mvpMatrix, color);
//            edgeDrawer.bindData(graphics.singleColorShader);
//            edgeDrawer.draw();
        }

        if (true) {
            //graphics.setProjectionMatrix3D();
            //graphics.updateViewProjMatrix();

            glDisable(GL_BLEND);
            glEnable(GL_DEPTH_TEST);

            //drawBackgroundAndDecoration();
        }

//        graphics.fboBackground.unbind();
//        background.initWithFBOTexture(graphics.fboBackground.getTextureId());

        glViewport(0, 0, (int) Graphics.screenWidth, (int) Graphics.screenHeight);
        glClearColor(0f, 0f, 0f, 1f);
    }

	public void onSurfaceChanged(int width, int height) {
        //loading.init();
    }

    public void handleButton() {
        if (gameState == GameState.Menu) {
            gameState = GameState.Playing;
        }
    }

    public void showMenu() {
        if (gameState != GameState.Menu) {
            //menu.reset();
            gameState = GameState.Menu;
        }
    }

	public void update() {
        graphics.updateViewProjMatrix();

        if (gameState == GameState.Loading) {
//            loading.update();
//            if (loading.done) {
//                showMenu();
//                update();
//            }
        } else {
//            hud.update();
//            hud.updateScore(scoreCounter.score);

//            switch (gameState) {
//                case Menu:
//                    menu.update();
//                    switch (menu.getSelectedMenuOption()) {
//                        case Play:
//                            gameState = GameState.Playing;
//                            break;
//
//                        case Stats:
//                            gameState = GameState.Stats;
//                            menu.resetSelectedMenuOption();
//                            stats.init();
//                            stats.update();
//                            break;
//                    }
//                    break;
//
//                case Stats:
//                    stats.update();
//                    if (stats.done) {
//                        menu.resetBackground();
//                        gameState = GameState.Menu;
//                    }
//                    break;
//
//                case Playing:
//                    updateInPlaying();
//            }
        }
    }

    private void updateInPlaying() {

    }

	public void draw() {
        if (gameState == GameState.Loading) {
            //loading.draw();
        } else {
            // 2d drawing
            graphics.setProjectionMatrix2D();
            graphics.updateViewProjMatrix();

            glDisable(GL_BLEND);
            glDepthMask(false);
            //background.draw();
            glDepthMask(true);

            // 3d drawing
            graphics.setProjectionMatrix3D();
            graphics.updateViewProjMatrix();

            glEnable(GL_DEPTH_TEST);



            // particle system
            glEnable(GL_BLEND);
            glDisable(GL_DEPTH_TEST);
            glBlendFunc(GL_ONE, GL_ONE);
            //graphics.particleManager.draw();

            // 2d drawing (HUD and Menu)
            graphics.setProjectionMatrix2D();
            graphics.updateViewProjMatrix();
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            //hud.draw();

            if (gameState == GameState.Menu) {
                //menu.draw();
            } else if (gameState == GameState.Stats) {
                //stats.draw();
            } else {
                graphics.setProjectionMatrix3D();
                graphics.updateViewProjMatrix();
            }
        }
	}

	public void handleTouchPress(float normalizedX, float normalizedY) {
        switch (gameState) {
            case Playing:
                handleTouchPressOnPlaying(normalizedX, normalizedY);
                break;

            case Menu:
                //menu.handleTouchPress(normalizedX, normalizedY);
                break;

            case Stats:
                //stats.handleTouchPress(normalizedX, normalizedY);
                break;

            case Loading:
                // do nothing
                break;
        }
    }

    private void handleTouchPressOnPlaying(float normalizedX, float normalizedY) {
        if (normalizedY < -0.86f ) {
            if (normalizedX > 0.86f) {
                gameState = GameState.Menu;
                //menu.reset();
                Engine.showInterstitialAd();
            } else if (normalizedX < -0.65f) {
                //gameState = GameState.Stats;
                //menu.reset();
            }
        } else {
            //touchDownX = normalizedX;
            //touchDownY = normalizedY;

//            if (!match3.isAnimating) {
//                Ray ray = Geometry.convertNormalized2DPointToRay(touchDownX, touchDownY, graphics.invertedViewProjectionMatrix);
//                GemPosition selectedGem = getSelectedGemFromRay(ray);
//
//                if (selectedGem != null) {
//                    if (match3.firstSelected == null) {
//                        match3.firstSelected = selectedGem;
//                    } else {
//                        match3.secondSelected = selectedGem;
//                        if (match3.firstSelected == match3.secondSelected) { // Same Gems are selected
//                            match3.firstSelected = null;
//                            match3.secondSelected = null;
//                        } else {
//                            match3.handle();
//                        }
//                    }
//                }
//            }
        }
	}

	public void handleTouchDrag(float normalizedX, float normalizedY) {
        switch (gameState) {
            case Menu:
                handleTouchDragOnMenu(normalizedX, normalizedY);
                break;

            case Playing:
                handleTouchDragOnPlaying(normalizedX, normalizedY);
                break;

            case Stats:
                //stats.handleTouchDrag(normalizedX, normalizedY);
                break;
        }
    }

    private void handleTouchDragOnMenu(float normalizedX, float normalizedY) {
        //menu.handleTouchDrag(normalizedX, normalizedY);
    }

    private void handleTouchDragOnPlaying(float normalizedX, float normalizedY) {
//		if (match3.firstSelected != null) {
//			final float minDiff = 0.15f;
//
//			float diffX = Math.abs(touchDownX - normalizedX);
//			float diffY = Math.abs(touchDownY - normalizedY);
//
//			//System.out.println("DiffX: " + diffX);
//			//System.out.println("DiffY: " + diffY);
//
//			if (diffX > diffY) { // move on X axis
//				if (diffX > minDiff) {
//					if (touchDownX > normalizedX) {
//						//System.out.println("Swipe left");
//						swipeDir = SwipeDir.SwipeLeft;
//					} else {
//						//System.out.println("Swipe right");
//						swipeDir = SwipeDir.SwipeRight;
//					}
//				}
//			} else { // move on Y axis
//				if (diffY > minDiff) {
//					if (touchDownY > normalizedY) {
//						//System.out.println("Swipe down");
//						swipeDir = SwipeDir.SwipeDown;
//					} else {
//						//System.out.println("Swipe up");
//						swipeDir = SwipeDir.SwipeUp;
//					}
//				}
//			}
//		}
	}

	public void handleTouchRelease(float normalizedX, float normalizedY) {
        switch (gameState) {
            case Playing:
                handleTouchReleseOnPlaying(normalizedX, normalizedY);
                break;

            case Stats:
//                stats.handleTouchRelease(normalizedX, normalizedY);
                break;
        }
    }

    private void handleTouchReleseOnPlaying(float normalizedX, float normalizedY) {
//		if (!match3.isAnimating) {
//			if (match3.firstSelected != null && swipeDir != SwipeDir.SwipeNone) {
//				int x = match3.firstSelected.boardX;
//				int y = match3.firstSelected.boardY;
//
//				switch(swipeDir) {
//				case SwipeDown:
//					if ( (y - 1) >= 0 ) {
//						match3.secondSelected = match3.board[x][y-1];
//					}
//					break;
//
//				case SwipeUp:
//					if ( (y + 1) < match3.boardSize) {
//						match3.secondSelected = match3.board[x][y+1];
//					}
//					break;
//
//				case SwipeLeft:
//					if ( (x - 1) >= 0 ) {
//						match3.secondSelected = match3.board[x-1][y];
//					}
//					break;
//
//				case SwipeRight:
//					if ( (x + 1) < match3.boardSize) {
//						match3.secondSelected = match3.board[x+1][y];
//					}
//					break;
//
//				default:
//					//System.out.println("No swipe!");
//					break;
//				}
//
//				if (match3.secondSelected != null) {
//					match3.handle();
//				}
//			}
//		}
	}

//	private GemPosition getSelectedGemFromRay(Ray ray) {
//		for(int y = 0; y < match3.boardSize; ++y) {
//			for (int x = 0; x < match3.boardSize; ++x) {
//				GemPosition gp = match3.board[x][y];
//				if ( Geometry.intersects(gp.boundingSphere, ray) ) {
//					return gp;
//				}
//			}
//		}
//		return null;
//	}

    public static void setupHollowCube() {
        int number_of_visible_cubes = 0;
        int number_of_invisible_cubes = 0;

        for(int x = 0; x < MAX_CUBE_COUNT; ++x) {
            for(int y = 0; y < MAX_CUBE_COUNT; ++y) {
                for(int z = 0; z < MAX_CUBE_COUNT; ++z) {
                    if ( x > 1 && x < MAX_CUBE_COUNT - 2 && y > 1 && y < MAX_CUBE_COUNT - 2 && z > 1 && z < MAX_CUBE_COUNT - 2 ) {
                        ++number_of_invisible_cubes;
                        cubes[x][y][z].type = Cube.CubeTypeEnum.CubeIsInvisible;
                    } else {
                        if (x == 0 || x == MAX_CUBE_COUNT - 1 || y == 0 || y == MAX_CUBE_COUNT - 1 || z == 0 || z == MAX_CUBE_COUNT - 1) {
                            ++number_of_invisible_cubes;
                            cubes[x][y][z].type = Cube.CubeTypeEnum.CubeIsInvisible;
                        } else {
                            ++number_of_visible_cubes;
                            cubes[x][y][z].type = Cube.CubeTypeEnum.CubeIsVisibleAndObstacle;
                            cubes[x][y][z].SetColor(Color.WHITE);
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
        //printf("\nLevel Cube at: (%d,%d,%d)", pCube->x, pCube->y, pCube->z);
        Creator.addLevelCube(level_number,  face_type, face_id, cube.x, cube.y, cube.z);
    }















    #define HALF_CUBE_SIZE CUBE_SIZE / 2.0f
//#define FONT_OVERLAY_OFFSET ((CUBE_SIZE / 2.0f) + 0.001f)
            #define FONT_OVERLAY_OFFSET 0.01f
            #define SAVE_GAME_FILE "game.dat"
            #define SAVE_GAME_FILE_EASY "easy_game.dat"
            #define SAVE_GAME_FILE_NORMAL "normal_game.dat"
            #define SAVE_GAME_FILE_HARD "hard_game.dat"
            #define SAVE_OPTIONS_FILE "options.dat"
            #define LEVEL_LOCKED -1
            #define LEVEL_UNLOCKED 0



    struct CubeRotation
    {
        float degree;
        vec3 axis;
    };




// Scenes


enum MoveDir
{
    MoveNone = 0,
    MoveY = 1,
    MoveX = 2,
    MoveZ = 3
};


struct CellInitData
        {
        vec3 offset;
        int level_index;

        };

        struct Rectangle
        {
        Rectangle()
        {
        x = y = w = h = 0.0f;
        }

        Rectangle(int x, int y, int w, int h)
        {
        this->x = x;
        this->y = y;
        this->w = w;
        this->h = h;
        }

        float x;
        float y;
        float w;
        float h;
        };



class cEngine : public IEngine
        {

public:

static GLfloat vertices[];
static GLfloat normals[];
static GLshort texture_coordinates[];
static GLubyte colors[];


        cCube cubes[MAX_CUBE_COUNT][MAX_CUBE_COUNT][MAX_CUBE_COUNT];

        float m_banner_height;
        float m_aspectRatio;

        TexturedQuad* m_newlinefont;


        DeviceTypeEnum device_type;








// scenes
        cIntro* m_intro;

        cAnimator* m_animator;

        cStatistics* m_statistics;
        cOutro* m_outro;
        cSolvers* m_solvers;

        cEngine();
        ~cEngine();

        void ResizeGLView(int width, int height);
        void Init(int width, int height, float scaleFactor, DeviceTypeEnum device_type, float banner_height);
        void Update(float dt);
        void Render();


        void EnteredBackground();
        void EnteredForeground();


        void PlayGame(DifficultyEnum difficulty, int level_number);

        GLuint LoadTexture(const char* name);

// input
        void OnFingerDown(float x, float y, int finger_count);
        void OnFingerUp(float x, float y, int finger_count);
        void OnFingerMove(float prev_x, float prev_y, float cur_x, float cur_y, int finger_count);

// Save & Load Options
        void SaveOptions();
        void LoadOptions();

        void OnSwipe(SwipeDirEnums type);


        inline float quadraticIn(float time)
        {
        return time * time;
        }

        inline float quadraticOut(float time)
        {
        return time * (2 - time);
        }

        void ReportAchievement(const char* identifier, float percent);


        void ShowFullScreenAd();
        bool GetAdsFlag();

// volumes
        void MusicVolumeUp();
        void MusicVolumeDown();

        void SoundVolumeUp();
        void SoundVolumeDown();
        float GetMusicVolume();
        float GetSoundVolume();

        void SetMusicVolume(float volume);
        void SetSoundVolume(float volume);

        void PlaySound(const char* key);
        void PlayMusic(const char* key);

        void PrepareMusicToPlay(const char* key);
        void PlayPreparedMusic();

        void StopMusic();

        inline bool IsPlaying()
        {
        return m_scene == m_level ? true : false;
        }

//	inline void GetTimeString(char* buffer, double elapsed)
//    {
//        sprintf(buffer, "%02.0f:%02.0f:%02.0f", floor(elapsed/3600.0), floor(fmod(elapsed,3600.0)/60.0), fmod(elapsed, 60.0));
//    }

        void SetResourceManager(void* pResMgr)
        {
        m_resourceManager = (IResourceManager*)pResMgr;
        }

        void SaveGLBuffer()
        {
        m_resourceManager->SaveGLBuffer();
        }

        inline void IncT(float& t)
        {
        t += 0.04f;

        if (t > 1.0f)
        t = 1.0f;
        }

        inline void DecT(float& t)
        {
        t -= 0.04f;

        if (t < 0.0f)
        t = .0f;
        }

        bool GetCanSkipIntro();
        void SetCanSkipIntro();


        float m_scaleFactor;

        IResourceManager* m_resourceManager;

        cFBO m_fbo;

        void MakeShadowMatrix(float vPointOnPlane0[3], float vPointOnPlane1[3], float vPointOnPlane2[3], float vLightPos[4], float destMat[16]);

        inline void DrawCube()
        {
        glDrawArrays(GL_TRIANGLES, 0, 36);
        }

// draw cube face
        inline void DrawCubeFaceY_Plus()
        {
        glDrawArrays(GL_TRIANGLES, 30, 6);
        }
        inline void DrawCubeFaceY_Minus()
        {
        glDrawArrays(GL_TRIANGLES, 18, 6);
        }
        inline void DrawCubeFaceX_Plus()
        {
        glDrawArrays(GL_TRIANGLES, 12, 6);
        }
        inline void DrawCubeFaceX_Minus()
        {
        glDrawArrays(GL_TRIANGLES, 24, 6);
        }
        inline void DrawCubeFaceZ_Plus()
        {
        glDrawArrays(GL_TRIANGLES, 6, 6);
        }
        inline void DrawCubeFaceZ_Minus()
        {
        glDrawArrays(GL_TRIANGLES, 0, 6);
        }

// draw cube no face
        void DrawCubeNoFace(bool x_plus,
        bool x_minus,
        bool y_plus,
        bool y_minus,
        bool z_plus,
        bool z_minus);

        void DrawQuad();

        void DrawCircleAt(float x, float y, float radius, Color& color);


        inline TexturedQuad* GetFontBig(char ch)
        {
        //printf("\nNumber of Fonts:%lu", m_fonts.size());
        return m_fonts_big[ch];
        }





        inline TexturedQuad* GetNewLineFont()
        {
        return m_newlinefont;
        }

        float GetTextWidth(const char* text, float scale);

        vec2 Rotate(vec2& a, float degree)
        {
        float angle = TO_RAD(degree);

        float c = cosf(angle);
        float s = sinf(angle);
        return vec2(
        c*a.x - s*a.y,
        s*a.x + c*a.y
        );
        }

// SetProjection


// IAP
        void PurchaseRestore();
        void PurchaseSolvers();
        void PurchaseRemoveAds();

        void HideProgressIndicator();
        void ShowAlreadyPurchased();

// Game Center
        void SubmitScore(int score);
        void ShowLeaderboard();
        void ShowLocalScore();

        void ShowSocialShare();


        void HideGameCenterInfo();
        void ShowGameCenterInfo();

        GLuint m_texture_id;

        void SetTextureID(GLuint id);

        void SetupHollowCube();





        inline vec3 GetCubePosAt(CubePos cube_pos)
        {
        vec3 pos;

        pos.x = cubes[cube_pos.x][cube_pos.y][cube_pos.z].tx;
        pos.y = cubes[cube_pos.x][cube_pos.y][cube_pos.z].ty;
        pos.z = cubes[cube_pos.x][cube_pos.y][cube_pos.z].tz;

        return pos;
        }

        inline void SetCubeTypeOnFace(cCube* pCube, char ch, CubeFaceTypesEnum face_type, CubeFaceNamesEnum face_id)
        {
        int level_number = -1;

        switch (face_id)
        {
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

        switch (ch)
        {
        case ' ':
        pCube->type = CubeIsInvisible;
        return;

        case 'x':
        pCube->type = CubeIsVisibleAndObstacle;
        return;

        case '1':
        level_number+=1;
        break;

        case '2':
        level_number+=2;
        break;

        case '3':
        level_number+=3;
        break;

        case '4':
        level_number+=4;
        break;

        case '5':
        level_number+=5;
        break;

        case '6':
        level_number+=6;
        break;

        case '7':
        level_number+=7;
        break;

        case '8':
        level_number+=8;
        break;

        case '9':
        level_number+=9;
        break;

        case 'A':
        level_number+=10;
        break;

        case 'B':
        level_number+=11;
        break;

        case 'C':
        level_number+=12;
        break;

        case 'D':
        level_number+=13;
        break;

        case 'E':
        level_number+=14;
        break;

        case 'F':
        level_number+=15;
        break;

default:
        //printf("\nERROR: Unknown cube type on face.");
        return;
        } // switch

        pCube->type = CubeIsInvisibleAndObstacle;
        //printf("\nLevel Cube at: (%d,%d,%d)", pCube->x, pCube->y, pCube->z);
        cCreator::AddLevelCube(level_number,  face_type, face_id, pCube->x, pCube->y, pCube->z);
        }









        void WarmCache();
        void ResetHelp();


        inline void SetCubeTypeInvisible(CubePos cube_pos)
        {
        //printf("\nSet Cube Type Invisible (%d, %d, %d)", cube_pos.x, cube_pos.y, cube_pos.z);
        cubes[cube_pos.x][cube_pos.y][cube_pos.z].type = CubeIsInvisible;
        }

        inline bool IsPlayerCube(CubePos& cube_pos)
        {
        CubePos cp = m_level->m_player_cube->GetCubePos();

        if (cp.x == cube_pos.x && cp.y == cube_pos.y && cp.z == cube_pos.z)
        return true;
        else
        return false;
        }

        inline bool IsKeyCube(CubePos& cube_pos)
        {
        CubePos cp = m_level->GetKeyPos();

        if (cp.x == cube_pos.x && cp.y == cube_pos.y && cp.z == cube_pos.z)
        return true;
        else
        return false;
        }

        inline Color GetRandomColor()
        {
        return Color( rand()%255, rand()%255, rand()%255, rand()%255 );
        }

        #ifdef LITE_VERSION
        void AskToBuyFullVersion();
        #endif

        void RenderToFBO(cScene* scene);

        void BuyPackOfSolvers(int number_of_solvers);
        void DrawFullScreenQuad(Color& color);

        void UpdateDisplayedSolvers();


        void SetSolverCount(int count);

        void DecSolverCount();

        bool IsCubeOnAList(cCube* pCube, list<cCube*>& lst)
        {
        assert(NULL != pCube);

        cCube* p;
        list<cCube*>::iterator it;
        for (it = lst.begin(); it != lst.end(); ++it)
        {
        p = *it;

        if (p == pCube)
        return true;
        }

        return false;
        }

        int CountOnAList(cCube* pCube, list<cCube*>& lst)
        {
        assert(NULL != pCube);

        int count = 0;

        cCube* p;
        list<cCube*>::iterator it;
        for (it = lst.begin(); it != lst.end(); ++it)
        {
        p = *it;

        if (p == pCube)
        ++count;
        }

        return count;
        }



private:



        void InitTextures();
        void InitFontsBig();
        void InitFonts();
        void InitNumberFonts();
        void InitSymbols();

        void ScaleVector(float vVector[3], const GLfloat fScale);
        GLfloat GetVectorLengthSqrd(const float vVector[3]);
        GLfloat GetVectorLength(const float vVector[3]);
        void CalcVectorCrossProduct(const float vU[3], const float vV[3], float vResult[3]);
        void NormalizeVector(float vNormal[3]);
        void SubtractVectors(const float vFirst[3], const float vSecond[3], float vResult[3]);
        void GetNormalVector(const float vP1[3], const float vP2[3], const float vP3[3], float vNormal[3]);
        void GetPlaneEquation(float vPoint1[3], float vPoint2[3], float vPoint3[3], float vPlane[3]);

        GLuint m_framebuffer;
        GLuint m_colorbuffer;
        GLuint m_depthstencilbuffer;


        cScene* m_scene;
        };

        extern cEngine* engine;












    cEngine* engine = NULL;


    #pragma mark - ctor

    IEngine* CreateEngine()
    {
        return new cEngine();
    }


    GLfloat cEngine::vertices[] =
    {
        // x-plus
        HALF_CUBE_SIZE,  HALF_CUBE_SIZE, -HALF_CUBE_SIZE,
                HALF_CUBE_SIZE,  HALF_CUBE_SIZE,  HALF_CUBE_SIZE,
                HALF_CUBE_SIZE, -HALF_CUBE_SIZE,  HALF_CUBE_SIZE,

                HALF_CUBE_SIZE, -HALF_CUBE_SIZE,  HALF_CUBE_SIZE,
                HALF_CUBE_SIZE, -HALF_CUBE_SIZE, -HALF_CUBE_SIZE,
                HALF_CUBE_SIZE,  HALF_CUBE_SIZE, -HALF_CUBE_SIZE,

                // x-minus
                -HALF_CUBE_SIZE, -HALF_CUBE_SIZE, -HALF_CUBE_SIZE,
                -HALF_CUBE_SIZE, -HALF_CUBE_SIZE,  HALF_CUBE_SIZE,
                -HALF_CUBE_SIZE,  HALF_CUBE_SIZE,  HALF_CUBE_SIZE,

                -HALF_CUBE_SIZE,  HALF_CUBE_SIZE,  HALF_CUBE_SIZE,
                -HALF_CUBE_SIZE,  HALF_CUBE_SIZE, -HALF_CUBE_SIZE,
                -HALF_CUBE_SIZE, -HALF_CUBE_SIZE, -HALF_CUBE_SIZE,


                // y-plus
                HALF_CUBE_SIZE,  HALF_CUBE_SIZE,  HALF_CUBE_SIZE,
                HALF_CUBE_SIZE,  HALF_CUBE_SIZE, -HALF_CUBE_SIZE,
                -HALF_CUBE_SIZE,  HALF_CUBE_SIZE, -HALF_CUBE_SIZE,

                -HALF_CUBE_SIZE,  HALF_CUBE_SIZE, -HALF_CUBE_SIZE,
                -HALF_CUBE_SIZE,  HALF_CUBE_SIZE,  HALF_CUBE_SIZE,
                HALF_CUBE_SIZE,  HALF_CUBE_SIZE,  HALF_CUBE_SIZE,

                // y-minus
                HALF_CUBE_SIZE, -HALF_CUBE_SIZE, -HALF_CUBE_SIZE,
                HALF_CUBE_SIZE, -HALF_CUBE_SIZE,  HALF_CUBE_SIZE,
                -HALF_CUBE_SIZE, -HALF_CUBE_SIZE,  HALF_CUBE_SIZE,

                -HALF_CUBE_SIZE, -HALF_CUBE_SIZE,  HALF_CUBE_SIZE,
                -HALF_CUBE_SIZE, -HALF_CUBE_SIZE, -HALF_CUBE_SIZE,
                HALF_CUBE_SIZE, -HALF_CUBE_SIZE, -HALF_CUBE_SIZE,


                // z-plus
                HALF_CUBE_SIZE,  HALF_CUBE_SIZE,  HALF_CUBE_SIZE,
                -HALF_CUBE_SIZE,  HALF_CUBE_SIZE,  HALF_CUBE_SIZE,
                -HALF_CUBE_SIZE, -HALF_CUBE_SIZE,  HALF_CUBE_SIZE,

                -HALF_CUBE_SIZE, -HALF_CUBE_SIZE,  HALF_CUBE_SIZE,
                HALF_CUBE_SIZE, -HALF_CUBE_SIZE,  HALF_CUBE_SIZE,
                HALF_CUBE_SIZE,  HALF_CUBE_SIZE,  HALF_CUBE_SIZE,

                // z-minus
                HALF_CUBE_SIZE,  HALF_CUBE_SIZE, -HALF_CUBE_SIZE,
                HALF_CUBE_SIZE, -HALF_CUBE_SIZE, -HALF_CUBE_SIZE,
                -HALF_CUBE_SIZE, -HALF_CUBE_SIZE, -HALF_CUBE_SIZE,

                -HALF_CUBE_SIZE, -HALF_CUBE_SIZE, -HALF_CUBE_SIZE,
                -HALF_CUBE_SIZE,  HALF_CUBE_SIZE, -HALF_CUBE_SIZE,
                HALF_CUBE_SIZE,  HALF_CUBE_SIZE, -HALF_CUBE_SIZE,
    };

    GLfloat cEngine::normals[] =
    {
        // x-plus
        1.0f,  0.0f,  0.0f,
                1.0f,  0.0f,  0.0f,
                1.0f,  0.0f,  0.0f,
                1.0f,  0.0f,  0.0f,
                1.0f,  0.0f,  0.0f,
                1.0f,  0.0f,  0.0f,

                // x-minus
                -1.0f,  0.0f,  0.0f,
                -1.0f,  0.0f,  0.0f,
                -1.0f,  0.0f,  0.0f,
                -1.0f,  0.0f,  0.0f,
                -1.0f,  0.0f,  0.0f,
                -1.0f,  0.0f,  0.0f,

                // y-plus
                0.0f,  1.0f,  0.0f,
                0.0f,  1.0f,  0.0f,
                0.0f,  1.0f,  0.0f,
                0.0f,  1.0f,  0.0f,
                0.0f,  1.0f,  0.0f,
                0.0f,  1.0f,  0.0f,

                // y-minus
                0.0f, -1.0f,  0.0f,
                0.0f, -1.0f,  0.0f,
                0.0f, -1.0f,  0.0f,
                0.0f, -1.0f,  0.0f,
                0.0f, -1.0f,  0.0f,
                0.0f, -1.0f,  0.0f,


                // z-plus
                0.0f,  0.0f,  1.0f,
                0.0f,  0.0f,  1.0f,
                0.0f,  0.0f,  1.0f,
                0.0f,  0.0f,  1.0f,
                0.0f,  0.0f,  1.0f,
                0.0f,  0.0f,  1.0f,

                // z-minus
                0.0f,  0.0f, -1.0f,
                0.0f,  0.0f, -1.0f,
                0.0f,  0.0f, -1.0f,
                0.0f,  0.0f, -1.0f,
                0.0f,  0.0f, -1.0f,
                0.0f,  0.0f, -1.0f,
    };


    GLshort cEngine::texture_coordinates[] =
    {
        // x-plus
        1, 0, // 0
                0, 0, // 1
                0, 1, // 2

                0, 1, // 2
                1, 1, // 3
                1, 0, // 0

                // x-minus
                1, 1, // 0
                0, 1, // 1
                0, 0, // 2

                0, 0, // 2
                1, 0, // 3
                1, 1, // 0

                // y-plus
                1, 0, // 0
                0, 0, // 1
                0, 1, // 2

                0, 1, // 2
                1, 1, // 3
                1, 0, // 0

                // y-minus
                1, 1, // 0
                0, 1, // 1
                0, 0, // 2

                0, 0, // 2
                1, 0, // 3
                1, 1, // 0

                // z-plus
                1, 1, // 0
                0, 1, // 1
                0, 0, // 2

                0, 0, // 2
                1, 0, // 3
                1, 1, // 0

                // z-minus
                1, 0, // 0
                1, 1, // 1
                0, 1, // 2

                0, 1, // 2
                0, 0, // 3
                1, 0, // 0
    };

    #define R 255
            #define G 255
            #define B 255
            #define A 255

    GLubyte cEngine::colors[] =
    {
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

    #undef R
    #undef G
    #undef B
    #undef A


//-------------------------------------------------------------------------------------
    cEngine::cEngine()
    {
        #ifdef __APPLE__
        //printf("\ncEngine is on Apple...\n");
        #endif

        glGenRenderbuffersOES(1, &m_colorbuffer);
        glBindRenderbufferOES(GL_RENDERBUFFER_OES, m_colorbuffer);

// scenes
        m_intro = NULL;
        m_menu = NULL;
        m_animator = NULL;
        m_level = NULL;
        m_statistics = NULL;
        m_outro = NULL;

        m_scene = NULL;

        dirty_alpha = DIRTY_ALPHA;
    }

    cEngine::~cEngine()
    {
        map<char, TexturedQuad*>::iterator it;

        for( it = m_fonts.begin(); it != m_fonts.end(); ++it)
            delete (*it).second;

        m_fonts.clear();
    }

    // Scales a vector by a scalar
    void cEngine::ScaleVector(float vVector[3], const GLfloat fScale)
    {
        vVector[0] *= fScale;
        vVector[1] *= fScale;
        vVector[2] *= fScale;
    }

    // Gets the length of a vector squared
    GLfloat cEngine::GetVectorLengthSqrd(const float vVector[3])
    {
        return (vVector[0]*vVector[0]) + (vVector[1]*vVector[1]) + (vVector[2]*vVector[2]);
    }

    // Gets the length of a vector
    GLfloat cEngine::GetVectorLength(const float vVector[3])
    {
        return (GLfloat)sqrt(GetVectorLengthSqrd(vVector));
    }

    // Calculate the cross product of two vectors
    void cEngine::CalcVectorCrossProduct(const float vU[3], const float vV[3], float vResult[3])
    {
        vResult[0] =  vU[1] * vV[2] - vV[1] * vU[2];
        vResult[1] = -vU[0] * vV[2] + vV[0] * vU[2];
        vResult[2] =  vU[0] * vV[1] - vV[0] * vU[1];
    }

    void cEngine::NormalizeVector(float vNormal[3])
    {
        GLfloat fLength = 1.0f / GetVectorLength(vNormal);
        ScaleVector(vNormal, fLength);
    }

    // Subtract one vector from another
    void cEngine::SubtractVectors(const float vFirst[3], const float vSecond[3], float vResult[3])
    {
        vResult[0] = vFirst[0] - vSecond[0];
        vResult[1] = vFirst[1] - vSecond[1];
        vResult[2] = vFirst[2] - vSecond[2];
    }

    void cEngine::GetNormalVector(const float vP1[3], const float vP2[3], const float vP3[3], float vNormal[3])
    {
        float vV1[3];
        float vV2[3];

        SubtractVectors(vP2, vP1, vV1);
        SubtractVectors(vP3, vP1, vV2);

        CalcVectorCrossProduct(vV1, vV2, vNormal);
        NormalizeVector(vNormal);
    }

    // Gets the three coefficients of a plane equation given three points on the plane.
    void cEngine::GetPlaneEquation(float vPoint1[3], float vPoint2[3], float vPoint3[3], float vPlane[3])
    {
        // Get normal vector from three points. The normal vector is the first three coefficients
        // to the plane equation...
        GetNormalVector(vPoint1, vPoint2, vPoint3, vPlane);

        // Final coefficient found by back substitution
        vPlane[3] = -(vPlane[0] * vPoint3[0] + vPlane[1] * vPoint3[1] + vPlane[2] * vPoint3[2]);
    }

    void cEngine::MakeShadowMatrix(float vPointOnPlane0[3], float vPointOnPlane1[3], float vPointOnPlane2[3], float vLightPos[4], float destMat[16])
    {
        float vPlaneEquation[4];
        GLfloat dot;

        GetPlaneEquation(vPointOnPlane0, vPointOnPlane1, vPointOnPlane2, vPlaneEquation);

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

    void cEngine::ShowFullScreenAd()
    {
        m_resourceManager->ShowFullScreenAd();
    }

    bool cEngine::GetAdsFlag()
    {
        return m_resourceManager->GetAdsFlag();
    }

    #pragma mark - resize

    void cEngine::ResizeGLView(int width, int height)
    {
        m_width = width;
        m_height = height;

        m_half_width = width / 2;
        m_half_height = height / 2;

        m_aspectRatio = (float)m_width / (float)m_height;
        glViewport(0, 0, width, height);

        m_menu->SetupCameras();
    }


    #pragma mark - Init

    void cEngine::Init(int width, int height, float scaleFactor, DeviceTypeEnum device_type, float banner_height)
    {
        srand(time(NULL));

        m_banner_height = banner_height;
        m_scaleFactor = scaleFactor;
        this->device_type = device_type;

        // create packed depth & stencil buffer with same size as the color buffer
        glGenRenderbuffersOES(1, &m_depthstencilbuffer);
        glBindRenderbufferOES(GL_RENDERBUFFER_OES, m_depthstencilbuffer);
        glRenderbufferStorageOES(GL_RENDERBUFFER_OES, GL_DEPTH24_STENCIL8_OES, width, height);

        // Create the framebuffer object and attach
        // - the color buffer
        // - the packed depth & stencil buffer
        glGenFramebuffersOES(1, &m_framebuffer);
        glBindFramebufferOES(GL_FRAMEBUFFER_OES, m_framebuffer);

        glFramebufferRenderbufferOES(GL_FRAMEBUFFER_OES, GL_COLOR_ATTACHMENT0_OES, GL_RENDERBUFFER_OES, m_colorbuffer);         // color
        glFramebufferRenderbufferOES(GL_FRAMEBUFFER_OES, GL_DEPTH_ATTACHMENT_OES, GL_RENDERBUFFER_OES, m_depthstencilbuffer);   // depth
        glFramebufferRenderbufferOES(GL_FRAMEBUFFER_OES, GL_STENCIL_ATTACHMENT_OES, GL_RENDERBUFFER_OES, m_depthstencilbuffer); // stencil

        glBindRenderbufferOES(GL_RENDERBUFFER_OES, m_colorbuffer);

        GLenum status = glCheckFramebufferStatusOES(GL_FRAMEBUFFER_OES);

        if (GL_FRAMEBUFFER_COMPLETE_OES != status)
        {
//        printf("\nFailure with framebuffer generation: %d", glCheckFramebufferStatusOES(GL_FRAMEBUFFER_OES));

            switch (status)
            {
                case GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_OES:
//                printf("\nIncomplete!!!");
                    break;

                case GL_FRAMEBUFFER_UNSUPPORTED_OES:
//                printf("\nUnsupported!!!");
                    break;
            }
        }

        m_width = width;
        m_height = height;

        m_half_width = m_width / 2;
        m_half_height = m_height / 2;

        int h = m_height - banner_height;
//	m_aspectRatio = (float)m_width / (float)m_height;
        m_aspectRatio = (float)m_width / (float)h;

        m_fbo.CreateWithColorAndDepthStencilBuffer(m_width, m_height);

//    glViewport(0, 0, width, height);
        glViewport(0, 0, width, h);

        // make the OpenGL ModelView matrix the default
        glMatrixMode(GL_MODELVIEW);

//  glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
//  glClearColor(0.4f, 0.4f, 0.4f, 0.0f);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // setup material properties
        vec4 specular(1.0f, 1.0f, 1.0f, 1.0f);
        glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, specular.Pointer());
        glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, 35.0f);

        glEnable(GL_COLOR_MATERIAL);

        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);

        // create frame buffer object
        glBindFramebufferOES(GL_FRAMEBUFFER_OES, m_framebuffer);
        glBindRenderbufferOES(GL_RENDERBUFFER_OES, m_colorbuffer);

        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);

        switch (device_type)
        {
            case Device_iPad:
                device_scale = 2.0f * m_scaleFactor;
                break;

            default:
                device_scale = 1.0f * m_scaleFactor;
                break;
        }

        InitTextures();
        InitFonts();
        InitFontsBig();
        InitNumberFonts();
        InitSymbols();

        engine = this;

        LoadOptions();

        float size = (MAX_CUBE_COUNT * CUBE_SIZE) - CUBE_SIZE;
        cube_offset.x = cube_offset.y = cube_offset.z = size / -2.0f;

        cCreator::CreateCubes();

        m_level = new cLevel();
        m_menu = new cMenu();
        m_solvers = new cSolvers();

        cLevelBuilder::level = m_level;

//	printf("\nsize of level:%lu", sizeof(cEngine));
//	printf("\nsize of menu:%lu", sizeof(cMenu));
//	printf("\nsize of level:%lu", sizeof(cLevel));

        ShowScene(Scene_Intro);
        //ShowScene(Scene_Menu);
        //ShowScene(Scene_Anim);
        //ShowScene(Scene_Level);
        //ShowScene(Scene_Solvers);
        //ShowScene(Scene_Stat);
        //ShowScene(Scene_Outro);

        WarmCache();

        cCubetraz::Init();
        //cCubetraz::CreateOldSaveGameFile();
        cCubetraz::Convert();
        cCubetraz::Load();
    }

    GLuint cEngine::LoadTexture(const char* name)
    {
        if (0 != texture_id_tutor)
        {
            glDeleteTextures(1, &texture_id_tutor);
            texture_id_tutor = 0;
        }

        m_resourceManager->CreateTexture(name, texture_id_tutor);
        return texture_id_tutor;
    }

    void cEngine::InitTextures()
    {
        m_resourceManager->CreateTexture("grey_concrete128_stroke.png", texture_id_gray_concrete);
        m_resourceManager->CreateTexture("key.png", texture_id_key);
        m_resourceManager->CreateTexture("player.png", texture_id_player);
        m_resourceManager->CreateTexture("level_cube.png", texture_id_level_cubes);
        m_resourceManager->CreateTexture("level_cube_locked.png", texture_id_level_cubes_locked);
        m_resourceManager->CreateTexture("fonts.png", texture_id_fonts);
        m_resourceManager->CreateTexture("fonts_clear.png", texture_id_fonts_clear);
        m_resourceManager->CreateTexture("level_numbers.png", texture_id_numbers);
        m_resourceManager->CreateTexture("star.png", texture_id_star);
        m_resourceManager->CreateTexture("symbols.png", texture_id_symbols);
        m_resourceManager->CreateTexture("stat_background.png", texture_id_stat_background);
        m_resourceManager->CreateTexture("credits.png", texture_id_credits);
        m_resourceManager->CreateTexture("fonts_big.png", texture_id_fonts_big);
        m_resourceManager->CreateTexture("dirty.png", texture_id_dirty);
        m_resourceManager->CreateTexture("tutor_swipe", texture_id_tutor);
    }

    void cEngine::InitFontsBig()
    {
        const int arr_size = 65;

        struct FontStruct
        {
            char ch;
            Rectangle rc;
        };

        FontStruct a[arr_size] =
        {
            { ' ', Rectangle(1,      0, 30,  82) },
            { '!', Rectangle(31,     0, 49,  82) },
            { '"', Rectangle(80,     0, 61,  82) },
            { '#', Rectangle(141,    0, 76,  82) },
            { '$', Rectangle(217,    0, 72,  82) },
            { '%', Rectangle(289,    0, 84,  82) },
            { '&', Rectangle(373,    0, 76,  82) },
            { '\'',Rectangle(449,    0, 42,  82) },
            { '(', Rectangle(491,    0, 46,  82) },
            { ')', Rectangle(537,    0, 46,  82) },
            { '*', Rectangle(583,    0, 58,  82) },
            { '+', Rectangle(641,    0, 76,  82) },
            { ',', Rectangle(717,    0, 44,  82) },
            { '-', Rectangle(761,    0, 38,  82) },
            { '.', Rectangle(799,    0, 44,  82) },
            { '/', Rectangle(843,    0, 75,  82) },
            { '0', Rectangle(918,    0, 72,  82) },
            { '1', Rectangle(0,     83, 72,  82) },
            { '2', Rectangle(72,    83, 72,  82) },
            { '3', Rectangle(144,   83, 72,  82) },
            { '4', Rectangle(216,   83, 72,  82) },
            { '5', Rectangle(288,   83, 72,  82) },
            { '6', Rectangle(360,   83, 72,  82) },
            { '7', Rectangle(432,   83, 72,  82) },
            { '8', Rectangle(504,   83, 72,  82) },
            { '9', Rectangle(576,   83, 72,  82) },
            { ':', Rectangle(648,   83, 44,  82) },
            { ';', Rectangle(692,   83, 44,  82) },
            { '<', Rectangle(736,   83, 57,  82) },
            { '=', Rectangle(793,   83, 76,  82) },
            { '>', Rectangle(869,   83, 57,  82) },
            { '?', Rectangle(926,   83, 57,  82) },
            { '@', Rectangle(0,    166, 79,  82) },
            { 'A', Rectangle(79,   166, 79,  82) },
            { 'B', Rectangle(158,  166, 78,  82) },
            { 'C', Rectangle(236,  166, 81,  82) },
            { 'D', Rectangle(317,  166, 81,  82) },
            { 'E', Rectangle(398,  166, 75,  82) },
            { 'F', Rectangle(473,  166, 73,  82) },
            { 'G', Rectangle(546,  166, 82,  82) },
            { 'H', Rectangle(628,  166, 82,  82) },
            { 'I', Rectangle(710,  166, 46,  82) },
            { 'J', Rectangle(756,  166, 65,  82) },
            { 'K', Rectangle(821,  166, 78,  82) },
            { 'L', Rectangle(899,  166, 72,  82) },
            { 'M', Rectangle(0,    249, 89,  82) },
            { 'N', Rectangle(89,   249, 83,  82) },
            { 'O', Rectangle(172,  249, 82,  82) },
            { 'P', Rectangle(254,  249, 76,  82) },
            { 'Q', Rectangle(330,  249, 82,  82) },
            { 'R', Rectangle(412,  249, 79,  82) },
            { 'S', Rectangle(491,  249, 73,  82) },
            { 'T', Rectangle(564,  249, 69,  82) },
            { 'U', Rectangle(633,  249, 79,  82) },
            { 'V', Rectangle(712,  249, 76,  82) },
            { 'W', Rectangle(788,  249, 101, 82) },
            { 'X', Rectangle(889,  249, 75,  82) },
            { 'Y', Rectangle(0,    332, 72,  82) },
            { 'Z', Rectangle(72,   332, 72,  82) },
            { '[', Rectangle(144,  332, 46,  82) },
            { '\\', Rectangle(190, 332, 75,  82) },
            { ']', Rectangle(265,  332, 46,  82) },
            { '^', Rectangle(311,  332, 57,  82) },
            { '_', Rectangle(368,  332, 57,  82) },
            { '`', Rectangle(425,  332, 57,  82) }
        };

        const float tw = 1024.0f;   // texture width
        const float th = 1024.0f;   // texture height

        TexturedQuad* pFont;

        float x, y, w, h;

        for (int i = 0; i < arr_size; ++i)
        {
            x  = a[i].rc.x;
            y  = a[i].rc.y;
            w  = a[i].rc.w;
            h  = a[i].rc.h;

            pFont = new TexturedQuad();
            pFont->ch = a[i].ch;
            pFont->w = w;
            pFont->h = h;

            // x								// y
            pFont->tx_lo_left.x  =     x / tw;	pFont->tx_lo_left.y  = (y+h) / th;	// 0
            pFont->tx_lo_right.x = (x+w) / tw;  pFont->tx_lo_right.y = (y+h) / th;	// 1
            pFont->tx_up_right.x = (x+w) / tw;  pFont->tx_up_right.y =     y / th;	// 2
            pFont->tx_up_left.x  =     x / tw;	pFont->tx_up_left.y  =     y / th;	// 3

            m_fonts_big[ a[i].ch ] = pFont;
        }
    }

    void cEngine::InitFonts()
    {
        const int arr_size = 65;

        struct FontStruct
        {
            char ch;
            Rectangle rc;
        };

        FontStruct a[arr_size] =
        {
            { ' ',  Rectangle(1,     0, 20, 41) },
            { '!',  Rectangle(21,    0, 24, 41) },
            { '"',  Rectangle(45,    0, 30, 41) },
            { '#',  Rectangle(75,    0, 38, 41) },
            { '$',  Rectangle(113,   0, 36, 41) },
            { '%',  Rectangle(149,   0, 42, 41) },
            { '&',  Rectangle(191,   0, 38, 41) },
            { '\'', Rectangle(229,   0, 21, 41) },
            { '(',  Rectangle(250,   0, 23, 41) },
            { ')',  Rectangle(273,   0, 23, 41) },
            { '*',  Rectangle(296,   0, 29, 41) },
            { '+',  Rectangle(325,   0, 38, 41) },
            { ',',  Rectangle(363,   0, 22, 41) },
            { '-',  Rectangle(385,   0, 19, 41) },
            { '.',  Rectangle(404,   0, 22, 41) },
            { '/',  Rectangle(426,   0, 37, 41) },
            { '0',  Rectangle(463,   0, 36, 41) },
            { '1',  Rectangle(0,    42, 36, 41) },
            { '2',  Rectangle(36,   42, 36, 41) },
            { '3',  Rectangle(72,   42, 36, 41) },
            { '4',  Rectangle(108,  42, 36, 41) },
            { '5',  Rectangle(144,  42, 36, 41) },
            { '6',  Rectangle(180,  42, 36, 41) },
            { '7',  Rectangle(216,  42, 36, 41) },
            { '8',  Rectangle(252,  42, 36, 41) },
            { '9',  Rectangle(288,  42, 36, 41) },
            { ':',  Rectangle(324,  42, 22, 41) },
            { ';',  Rectangle(346,  42, 22, 41) },
            { '<',  Rectangle(368,  42, 28, 41) },
            { '=',  Rectangle(396,  42, 38, 41) },
            { '>',  Rectangle(434,  42, 28, 41) },
            { '?',  Rectangle(462,  42, 28, 41) },
            { '@',  Rectangle(0,    84, 39, 41) },
            { 'A',  Rectangle(39,   84, 39, 41) },
            { 'B',  Rectangle(78,   84, 39, 41) },
            { 'C',  Rectangle(117,  84, 40, 41) },
            { 'D',  Rectangle(157,  84, 40, 41) },
            { 'E',  Rectangle(197,  84, 37, 41) },
            { 'F',  Rectangle(234,  84, 36, 41) },
            { 'G',  Rectangle(270,  84, 41, 41) },
            { 'H',  Rectangle(311,  84, 41, 41) },
            { 'I',  Rectangle(348,  84, 30, 41) },
            { 'J',  Rectangle(375,  84, 32, 41) },
            { 'K',  Rectangle(407,  84, 39, 41) },
            { 'L',  Rectangle(446,  84, 36, 41) },
            { 'M',  Rectangle(0,   126, 44, 41) },
            { 'N',  Rectangle(44,  126, 41, 41) },
            { 'O',  Rectangle(85,  126, 41, 41) },
            { 'P',  Rectangle(126, 126, 38, 41) },
            { 'Q',  Rectangle(164, 126, 41, 41) },
            { 'R',  Rectangle(205, 126, 39, 41) },
            { 'S',  Rectangle(244, 126, 36, 41) },
            { 'T',  Rectangle(280, 126, 34, 41) },
            { 'U',  Rectangle(314, 126, 39, 41) },
            { 'V',  Rectangle(353, 126, 38, 41) },
            { 'W',  Rectangle(391, 126, 50, 41) },
            { 'X',  Rectangle(441, 126, 37, 41) },
            { 'Y',  Rectangle(0,   168, 36, 41) },
            { 'Z',  Rectangle(36,  168, 36, 41) },
            { '[',  Rectangle(72,  168, 23, 41) },
            { '\\', Rectangle(95,  168, 37, 41) },
            { ']',  Rectangle(132, 168, 23, 41) },
            { '^',  Rectangle(155, 168, 28, 41) },
            { '_',  Rectangle(183, 168, 28, 41) },
            { '`',  Rectangle(211, 168, 28, 41) }
        };

        TexturedQuad* pFont;

        float x, y, w, h;

        const float tw = 512.0f;    // texture width
        const float th = 512.0f;    // texture height

        for (int i = 0; i < arr_size; ++i)
        {
            x  = a[i].rc.x;
            y  = a[i].rc.y;
            w  = a[i].rc.w;
            h  = a[i].rc.h;

            pFont = new TexturedQuad();
            pFont->ch = a[i].ch;
            pFont->w = w;
            pFont->h = h;

            // x								// y
            pFont->tx_lo_left.x  =     x / tw;	pFont->tx_lo_left.y  = (y+h) / th;	// 0
            pFont->tx_lo_right.x = (x+w) / tw;  pFont->tx_lo_right.y = (y+h) / th;	// 1
            pFont->tx_up_right.x = (x+w) / tw;  pFont->tx_up_right.y =     y / th;	// 2
            pFont->tx_up_left.x  =     x / tw;	pFont->tx_up_left.y  =     y / th;	// 3

            m_fonts[ a[i].ch ] = pFont;
        }
    }

    void cEngine::InitNumberFonts()
    {
        int a[] =
                {
//      x      y    w    h
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

        const float tw = 1024.0f;    // texture width
        const float th = 1024.0f;    // texture height

        int size = (sizeof(a) / sizeof(int));

        for (int i = 4; i < size; i+=4, ++index)
        {
            x  = a[i];
            y  = a[i+1];
            w  = a[i+2];
            h  = a[i+3];

            TexturedQuad* pFont = new TexturedQuad();
            pFont->number = index;

            // x                                y
            pFont->tx_lo_left.x  = x / tw;		pFont->tx_lo_left.y  = (y+h) / th;      // 0 lower left
            pFont->tx_lo_right.x = (x+w) / tw;	pFont->tx_lo_right.y = (y+h) / th;      // 1 lower right
            pFont->tx_up_right.x = (x+w) / tw;	pFont->tx_up_right.y = (y) / th;        // 2 upper righ
            pFont->tx_up_left.x  = x / tw;		pFont->tx_up_left.y  = (y) / th;		// 3 upper left

            m_numbers[index] = pFont;
        }
    }

    void cEngine::InitSymbols()
    {
        const int max_symbols = 25;

        Rectangle a[max_symbols];

        a[SymbolMinus]          = Rectangle(  0,   0, 170, 170);
        a[SymbolPlus]           = Rectangle(170,   0, 170, 170);
        a[SymbolInfo]           = Rectangle(340,   0, 170, 170);
        a[SymbolLock]           = Rectangle(  0, 170, 170, 170);
        a[SymbolQuestionmark]   = Rectangle(170, 170, 170, 170);
        a[SymbolGoLeft]         = Rectangle(340, 340, 170, 170);
        a[SymbolGoRight]        = Rectangle(340, 170, 170, 170);
        a[SymbolGoUp]           = Rectangle(  0, 340, 170, 170);
        a[SymbolGoDown]         = Rectangle(170, 340, 170, 170);
        a[SymbolUndo]           = Rectangle(510,   0, 170, 170);
        a[SymbolSolver]         = Rectangle(510, 170, 170, 170);
        a[SymbolPause]          = Rectangle(510, 340, 170, 170);
        a[Symbol1Star]          = Rectangle(680, 340, 170, 170);
        a[Symbol2Star]          = Rectangle(680, 170, 170, 170);
        a[Symbol3Star]          = Rectangle(680,   0, 170, 170);
        a[SymbolStar]           = Rectangle(850,   0, 170, 170);
        a[SymbolTriangleUp]     = Rectangle(  0, 510, 170, 170);
        a[SymbolTriangleDown]   = Rectangle(170, 510, 170, 170);
        a[SymbolHilite]         = Rectangle(510, 510, 170, 170);
        a[SymbolDeath]          = Rectangle(680, 510, 170, 170);
        a[SymbolLightning]      = Rectangle(850, 510, 170, 170);
        a[SymbolCracks]         = Rectangle(  0, 680, 170, 170);
        a[SymbolTriangleLeft]   = Rectangle(170, 680, 170, 170);
        a[SymbolTriangleRight]  = Rectangle(340, 680, 170, 170);
        a[SymbolSolved]         = Rectangle(850, 170, 170, 170);

        const float tw = 1024.0f;
        const float th = 1024.0f;

        float x, y, w, h;

        for (int i = 0; i < max_symbols; ++i)
        {
            x  = a[i].x;
            y  = a[i].y;
            w  = a[i].w;
            h  = a[i].h;

            TexturedQuad* pFont = new TexturedQuad();
            pFont->number = i;
            pFont->w = w;
            pFont->h = h;

            // x								y
            pFont->tx_lo_left.x  = x     / tw;	pFont->tx_lo_left.y  = (y+h) / th;
            pFont->tx_lo_right.x = (x+w) / tw;  pFont->tx_lo_right.y = (y+h) / th;
            pFont->tx_up_right.x = (x+w) / tw;  pFont->tx_up_right.y = y     / th;
            pFont->tx_up_left.x  = x     / tw;	pFont->tx_up_left.y  = y     / th;

            m_symbols[i] = pFont;
        }
    }

    #pragma mark - SetProjection




    #pragma mark - IAP

    void cEngine::PurchaseRestore()
    {
        m_resourceManager->PurchaseRestore();
    }

    void cEngine::PurchaseSolvers()
    {
        m_resourceManager->PurchaseSolvers();
    }

    void cEngine::PurchaseRemoveAds()
    {
        m_resourceManager->PurchaseRemoveAds();
    }

    void cEngine::HideProgressIndicator()
    {
        m_resourceManager->HideProgressIndicator();
    }

    void cEngine::UpdateDisplayedSolvers()
    {
        if (NULL != m_solvers)
            m_solvers->UpdateSolversCount();

        if (NULL != m_level)
            m_level->SetSolversCount();
    }

    void cEngine::SetTextureID(GLuint id)
    {
        m_texture_id = id;
    }


    void cEngine::SetSolverCount(int count)
    {
        return m_resourceManager->SetSolvers(count);
    }

    void cEngine::DecSolverCount()
    {
        m_resourceManager->SetSolvers(m_resourceManager->GetSolvers() - 1);
    }

    #pragma mark - GameCenter

    void cEngine::HideGameCenterInfo()
    {
        m_resourceManager->HideGameCenterInfo();
    }

    void cEngine::ShowGameCenterInfo()
    {
        m_resourceManager->ShowGameCenterInfo();
    }

    void cEngine::SubmitScore(int score)
    {
        m_resourceManager->ReportScore(score);
    }

    void cEngine::ReportAchievement(const char* identifier, float percent)
    {
        m_resourceManager->ReportAchievement(identifier, percent);
    }

    void cEngine::ShowLeaderboard()
    {
        m_resourceManager->ShowLeaderboard();
    }

    void cEngine::ShowLocalScore()
    {
        m_resourceManager->ShowLocalScore();
    }

    void cEngine::ShowSocialShare()
    {
        m_resourceManager->ShowSocialShare();
    }

    #pragma mark - Volume

    void cEngine::MusicVolumeUp()
    {
        float volume = m_resourceManager->GetMusicVolume();

        volume += 0.1f;

        if (volume > 1.0f)
            volume = 1.0f;

        m_resourceManager->SetMusicVolume(volume);
    }

    void cEngine::MusicVolumeDown()
    {
        float volume = m_resourceManager->GetMusicVolume();

        volume -= 0.1f;

        if (volume < 0.0f)
            volume = 0.0f;

        m_resourceManager->SetMusicVolume(volume);
    }

    void cEngine::SoundVolumeUp()
    {
        float volume = m_resourceManager->GetSoundVolume();

        volume += 0.1f;

        if (volume > 1.0f)
            volume = 1.0f;

        m_resourceManager->SetSoundFXVolume(volume);
    }

    void cEngine::SoundVolumeDown()
    {
        float volume = m_resourceManager->GetSoundVolume();

        volume -= 0.1f;

        if (volume < 0.0f)
            volume = 0.0f;

        m_resourceManager->SetSoundFXVolume(volume);
    }

    float cEngine::GetMusicVolume()
    {
        return m_resourceManager->GetMusicVolume();
    }

    float cEngine::GetSoundVolume()
    {
        return m_resourceManager->GetSoundVolume();
    }

    void cEngine::SetMusicVolume(float volume)
    {
        m_resourceManager->SetMusicVolume(volume);
    }

    void cEngine::SetSoundVolume(float volume)
    {
        m_resourceManager->SetSoundFXVolume(volume);
    }


    #pragma mark - Sound & Music Playback


    void cEngine::PlaySound(const char* key)
    {
        m_resourceManager->PlaySound(key);
    }

    void cEngine::PlayMusic(const char* key)
    {
        m_resourceManager->PlayMusic(key);
    }

    void cEngine::PrepareMusicToPlay(const char* key)
    {
        m_resourceManager->PrepareMusicToPlay(key);
    }

    void cEngine::PlayPreparedMusic()
    {
        m_resourceManager->PlayPreparedMusic();
    }

    void cEngine::StopMusic()
    {
        m_resourceManager->StopMusic();
    }

    #pragma mark - Entered

    void cEngine::EnteredBackground()
    {
        if (m_scene)
            m_scene->EnteredBackground();
    }

    void cEngine::EnteredForeground()
    {
        if (m_scene)
            m_scene->EnteredForeground();
    }

    #pragma mark - Draw

    void cEngine::DrawCircleAt(float x, float y, float radius, Color& color)
    {
        GLfloat vertices[80];
        GLubyte colors[150];
        int v_index = -1;
        int color_index = -1;
        float radian;
        vec2 pos;
        vec2 pt;

        pos.x = x;
        pos.y = y;

        vertices[++v_index] = pos.x;
        vertices[++v_index] = pos.y;

        colors[++color_index] = color.r;
        colors[++color_index] = color.g;
        colors[++color_index] = color.b;
        colors[++color_index] = color.a;

        for (float degree = 0.0f; degree <= 360.0f; degree += 36.0f)
        {
            radian = TO_RAD(degree);

            pt.x = pos.x + sin(radian) * radius;
            pt.y = pos.y + cos(radian) * radius;

            vertices[++v_index] = pt.x;
            vertices[++v_index] = pt.y;

            colors[++color_index] = color.r;
            colors[++color_index] = color.g;
            colors[++color_index] = color.b;
            colors[++color_index] = color.a;
        }

        glVertexPointer(2, GL_FLOAT, 0, vertices);
        glColorPointer(4, GL_UNSIGNED_BYTE, 0, colors);

        glDisableClientState(GL_NORMAL_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);

        glDrawArrays(GL_TRIANGLE_FAN, 0, v_index/2+1);
    }


    #pragma mark - DrawCubeFace

    void cEngine::DrawCubeNoFace(bool x_plus,
                                 bool x_minus,
                                 bool y_plus,
                                 bool y_minus,
                                 bool z_plus,
                                 bool z_minus)
    {
        if (z_minus)
            DrawCubeFaceZ_Minus();

        if (z_plus)
            DrawCubeFaceZ_Plus();

        if (x_plus)
            DrawCubeFaceX_Plus();

        if (y_minus)
            DrawCubeFaceY_Minus();

        if (x_minus)
            DrawCubeFaceX_Minus();

        if (y_plus)
            DrawCubeFaceY_Plus();
    }

    void cEngine::DrawQuad()
    {
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
    }

    #pragma mark - Render

    void cEngine::WarmCache()
    {
        glEnable(GL_TEXTURE_2D);

        cRenderer::Prepare();
        cRenderer::SetStreamSource();
        cRenderer::AddCube(0.0f, 0.0f, 0.0f);
        glBindTexture(GL_TEXTURE_2D, engine->texture_id_player);
        glDrawArrays(GL_TRIANGLES, 0, 36);

        glBindTexture(GL_TEXTURE_2D, engine->texture_id_gray_concrete);
        glDrawArrays(GL_TRIANGLES, 0, 36);

        glDisable(GL_TEXTURE_2D);
    }

    void cEngine::Render()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        m_scene->Render();
    }

    #pragma mark - Update

    void cEngine::Update(float dt)
    {
        m_scene->Update(dt);
    }

    #pragma mark - Input

    void cEngine::OnFingerDown(float x, float y, int finger_count)
    {
        m_scene->OnFingerDown(x * m_scaleFactor, y * m_scaleFactor, finger_count);
    }

    void cEngine::OnFingerUp(float x, float y, int finger_count)
    {
        m_scene->OnFingerUp(x * m_scaleFactor, y * m_scaleFactor, finger_count);
    }

    void cEngine::OnFingerMove(float prev_x, float prev_y, float cur_x, float cur_y, int finger_count)
    {
        m_scene->OnFingerMove(prev_x * m_scaleFactor, prev_y * m_scaleFactor, cur_x * m_scaleFactor, cur_y * m_scaleFactor, finger_count);
    }

    void cEngine::OnSwipe(SwipeDirEnums type)
    {
        m_scene->OnSwipe(type);
    }

    #pragma mark - Save / Load Options

    void cEngine::SaveOptions()
    {
        FILE* fp = engine->m_resourceManager->GetFilePointerForWrite(SAVE_OPTIONS_FILE);

        if (fp)
        {
            float mv = engine->GetMusicVolume();
            float sv = engine->GetSoundVolume();

            fwrite(&mv, 1, sizeof(float), fp);
            fwrite(&sv, 1, sizeof(float), fp);

            fclose(fp);
        }
    }

    void cEngine::LoadOptions()
    {
        float mv = 0.5f;
        float sv = 0.5f;

        FILE* fp = engine->m_resourceManager->GetFilePointerForRead(SAVE_OPTIONS_FILE);

        if (fp)
        {
            fread(&mv, sizeof(float), 1, fp);
            fread(&sv, sizeof(float), 1, fp);

            fclose(fp);
        }

        SetMusicVolume(mv);
        SetSoundVolume(sv);
    }

    #pragma mark - misc

    void cEngine::ResetHelp()
    {
        //printf("\nReset Help Here...\n");
    }

    void cEngine::SetupHollowCube()
    {
        int number_of_visible_cubes = 0;
        int number_of_invisible_cubes = 0;

        for(int x = 0; x < MAX_CUBE_COUNT; ++x)
        {
            for(int y = 0; y < MAX_CUBE_COUNT; ++y)
            {
                for(int z = 0; z < MAX_CUBE_COUNT; ++z)
                {
                    if ( x > 1 && x < MAX_CUBE_COUNT - 2 && y > 1 && y < MAX_CUBE_COUNT - 2 && z > 1 && z < MAX_CUBE_COUNT - 2 )
                    {
                        ++number_of_invisible_cubes;
                        cubes[x][y][z].type = CubeIsInvisible;
                    }
                    else
                    {
                        if (x == 0 || x == MAX_CUBE_COUNT - 1 || y == 0 || y == MAX_CUBE_COUNT - 1 || z == 0 || z == MAX_CUBE_COUNT - 1)
                        {
                            ++number_of_invisible_cubes;
                            cubes[x][y][z].type = CubeIsInvisible;
                        }
                        else
                        {
                            ++number_of_visible_cubes;
                            cubes[x][y][z].type = CubeIsVisibleAndObstacle;
                            cubes[x][y][z].SetColor( GetBaseColor() );
                        }
                    }
                }
            }
        }

        //printf("\nNumber of Visible Cubes: %d", number_of_visible_cubes);
        //printf("\nNumber of Invisible Cubes: %d", number_of_invisible_cubes);
    }


    float cEngine::GetTextWidth(const char* text, float scale)
    {
        float width = 0.0f;

        int len = (int)strlen(text);
        TexturedQuad* pFont;

        for (int i = 0; i < len; ++i)
        {
            pFont = GetFont(text[i]);
            width += (pFont->w * scale);
        }

        return width;
    }


    bool cEngine::GetCanSkipIntro()
    {
        return m_resourceManager->GetCanSkipIntro();
    }

    void cEngine::SetCanSkipIntro()
    {
        m_resourceManager->SetCanSkipIntro();
    }




    void cEngine::BuyPackOfSolvers(int number_of_solvers)
    {
        #ifndef LITE_VERSION
        if (5 == number_of_solvers)
            m_resourceManager->Purchase5Solvers();

        if (15 == number_of_solvers)
            m_resourceManager->Purchase15Solvers();
        #endif
    }

    #ifdef LITE_VERSION
    void cEngine::BuyFullVersion()
    {
        m_resourceManager->BuyFullVersion();
    }
    #endif

    void cEngine::RenderToFBO(cScene* scene)
    {
        GLint defaultFBO = 0;
        glGetIntegerv(GL_FRAMEBUFFER_BINDING_OES, &defaultFBO);

        glBindFramebufferOES(GL_FRAMEBUFFER_OES, m_fbo.m_FrameBuffer);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        scene->RenderToFBO();

        glBindFramebufferOES(GL_FRAMEBUFFER_OES, defaultFBO);
    }


    void cEngine::DrawFullScreenQuad(Color& color)
    {
        const GLfloat verts[] =
            {
                    0.0f,               engine->m_height,
                    0.0f,               0.0f,
                    engine->m_width,    0.0f,
                    engine->m_width,    engine->m_height
            };

        const GLubyte colors[] =
            {
                    color.r, color.g, color.b, color.a,
                    color.r, color.g, color.b, color.a,
                    color.r, color.g, color.b, color.a,
                    color.r, color.g, color.b, color.a
            };

        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);

        glVertexPointer(2, GL_FLOAT, 0, verts);
        glColorPointer(4, GL_UNSIGNED_BYTE, 0, colors);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
    }




}

package com.almagems.cubetraz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static android.opengl.GLES10.*;
import static com.almagems.cubetraz.Constants.*;


public final class Game {

    public static Graphics graphics;

    public static Vector getCubePosAt(CubePos cube_pos) {
        Vector pos = new Vector();

        pos.x = cubes[cube_pos.x][cube_pos.y][cube_pos.z].tx;
        pos.y = cubes[cube_pos.x][cube_pos.y][cube_pos.z].ty;
        pos.z = cubes[cube_pos.x][cube_pos.y][cube_pos.z].tz;

        return pos;
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
    public static TexturedQuad m_newlinefont;

    public static TexturedQuad getFontBig(String ch) {
        //printf("\nNumber of Fonts:%lu", m_fonts.size());
        return m_fonts_big.get(ch);
    }

    public static FBO m_fbo;

    public boolean isPlaying() {
        return m_scene == level;
    }

    public static Scene m_scene;
    public static Intro intro;
    public static Animator animator;
    public static Statistics statistics;
    public static Outro outro;

    private static int m_framebuffer;
    private static int m_colorbuffer;
    private static int m_depthstencilbuffer;





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

        switch (scene_id) {
            case Scene_Intro:
                intro = new Intro();
                intro.init();

                scene = intro;
                break;

            case Scene_Menu:
//                //showFullScreenAd();
//
//                if (m_intro) {
//                    delete m_intro;
//                    m_intro = null;
//                }
//
//                if (m_outro) {
//                    delete m_outro;
//                    m_outro = null;
//                    engine.menu_init_data.reappear = false;
//                }
//
//                if (null == m_menu)
//                    m_menu = new cMenu();
//
//                m_menu.Init();
//
//                scene = m_menu;
                break;

            case Scene_Anim:
//
//                if (null == m_animator)
//                    m_animator = new cAnimator();
//
//                m_animator.Init();
//
//                scene = m_animator;
                break;

            case Scene_Level:
//
//                m_level.Init(&level_init_data);
//
//                scene = m_level;
                break;

            case Scene_Stat:

//                if (null == m_statistics)
//                    m_statistics = new cStatistics();
//
//                m_statistics.Init();
//
//                scene = m_statistics;
                break;

            case Scene_Solvers:

//                m_solvers.Init();
//
//                scene = m_solvers;
                break;

            case Scene_Outro:

//                if (null == m_outro)
//                    m_outro = new cOutro();
//
//                m_outro.Init();
//
//                scene = m_outro;
                break;
        }

        m_scene = scene;
    }


    public static Level level;

    public static final LevelInitData level_init_data = new LevelInitData();
    public static final AnimInitData anim_init_data = new AnimInitData();


    public static boolean getCanPlayLockedLevels() {
        return true;
    }


    public static float dirty_alpha;

    public static final Vector cube_offset = new Vector();

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
                                cubes[x][y][z].setColor( getFaceColor(1f) );
                        }
                    }
                }
            }
        }
    }




    public GameState gameState;

    public static Cube[][][] cubes = new Cube[MAX_CUBE_COUNT][MAX_CUBE_COUNT][MAX_CUBE_COUNT];

    private boolean initialized;





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

        HUD.graphics = graphics;
        Scene.graphics = graphics;
        MovingCube.graphics = graphics;
        MoverCube.graphics = graphics;
        DeadCube.graphics = graphics;
        Starfield.graphics = graphics;

        intro = null;
        menu = null;
        animator = null;
        level = null;
        statistics = null;
        outro = null;

        m_scene = null;

        dirty_alpha = DIRTY_ALPHA;


//        loading = new Loading();

        initTextures();
        initFonts();
        initFontsBig();
        initNumberFonts();
        initSymbols();

        loadOptions();

        float size = (MAX_CUBE_COUNT * CUBE_SIZE) - CUBE_SIZE;
        cube_offset.x = cube_offset.y = cube_offset.z = size / -2.0f;

        Creator.createCubes();

        level = new Level();
        menu = new Menu();

        LevelBuilder.level = level;

//	printf("\nsize of level:%lu", sizeof(cEngine));
//	printf("\nsize of menu:%lu", sizeof(cMenu));
//	printf("\nsize of level:%lu", sizeof(cLevel));

        showScene(Scene_Intro);
        //ShowScene(Scene_Menu);
        //ShowScene(Scene_Anim);
        //ShowScene(Scene_Level);
        //ShowScene(Scene_Solvers);
        //ShowScene(Scene_Stat);
        //ShowScene(Scene_Outro);

        graphics.warmCache();

        Cubetraz.init();
        Cubetraz.load();
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


//        int defaultFBO = 0;
//        glGetIntegerv(GL_FRAMEBUFFER_BINDING_OES, &defaultFBO);
//
//        glBindFramebufferOES(GL_FRAMEBUFFER_OES, m_fbo.m_FrameBuffer);
//
//        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//
//        m_scene.renderToFBO();
//
//        glBindFramebufferOES(GL_FRAMEBUFFER_OES, defaultFBO);



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

        glViewport(0, 0, (int) graphics.screenWidth, (int) graphics.screenHeight);
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
        m_scene.update();


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

	public void draw() {
        m_scene.render();
//        if (gameState == GameState.Loading) {
//            //loading.draw();
//        } else {
//            // 2d drawing
//            graphics.setProjectionMatrix2D();
//            graphics.updateViewProjMatrix();
//
//            glDisable(GL_BLEND);
//            glDepthMask(false);
//            //background.draw();
//            glDepthMask(true);
//
//            // 3d drawing
//            graphics.setProjectionMatrix3D();
//            graphics.updateViewProjMatrix();
//
//            glEnable(GL_DEPTH_TEST);
//
//
//
//            // particle system
//            glEnable(GL_BLEND);
//            glDisable(GL_DEPTH_TEST);
//            glBlendFunc(GL_ONE, GL_ONE);
//            //graphics.particleManager.draw();
//
//            // 2d drawing (HUD and Menu)
//            graphics.setProjectionMatrix2D();
//            graphics.updateViewProjMatrix();
//            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
//            //hud.draw();
//
//            if (gameState == GameState.Menu) {
//                //menu.draw();
//            } else if (gameState == GameState.Stats) {
//                //stats.draw();
//            } else {
//                graphics.setProjectionMatrix3D();
//                graphics.updateViewProjMatrix();
//            }
//        }
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
        CubePos cp = level.m_player_cube.getCubePos();
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

    public static Color getRandomColor() {
        return new Color( Utils.rand.nextInt(255), Utils.rand.nextInt(255), Utils.rand.nextInt(255), Utils.rand.nextInt(255) );
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

    public static void cubeGLData() {

        float vertices[] = {
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

        float normals[] = {
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


        short texture_coordinates[] = {
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

        final int R = 255;
        final int G = 255;
        final int B = 255;
        final int A = 255;

        int colors[] = {
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
    void calcVectorCrossProduct(final float[] vU, final float[] vV, float[] vResult) {
        vResult[0] =  vU[1] * vV[2] - vV[1] * vU[2];
        vResult[1] = -vU[0] * vV[2] + vV[0] * vU[2];
        vResult[2] =  vU[0] * vV[1] - vV[0] * vU[1];
    }

    void normalizeVector(float[] vNormal) {
        float fLength = 1.0f / getVectorLength(vNormal);
        scaleVector(vNormal, fLength);
    }

    // Subtract one vector from another
    void subtractVectors(final float[] vFirst, final float[] vSecond, float[] vResult) {
        vResult[0] = vFirst[0] - vSecond[0];
        vResult[1] = vFirst[1] - vSecond[1];
        vResult[2] = vFirst[2] - vSecond[2];
    }

    void getNormalVector(final float[] vP1, final float[] vP2, final float[] vP3, float[] vNormal) {
        float[] vV1 = new float[3];
        float[] vV2 = new float[3];

        subtractVectors(vP2, vP1, vV1);
        subtractVectors(vP3, vP1, vV2);

        calcVectorCrossProduct(vV1, vV2, vNormal);
        normalizeVector(vNormal);
    }

    // Gets the three coefficients of a plane equation given three points on the plane.
    void getPlaneEquation(float[] vPoint1, float[] vPoint2, float[] vPoint3, float[] vPlane) {
        // Get normal vector from three points. The normal vector is the first three coefficients
        // to the plane equation...
        getNormalVector(vPoint1, vPoint2, vPoint3, vPlane);

        // Final coefficient found by back substitution
        vPlane[3] = -(vPlane[0] * vPoint3[0] + vPlane[1] * vPoint3[1] + vPlane[2] * vPoint3[2]);
    }

    void makeShadowMatrix(float[] vPointOnPlane0, float[] vPointOnPlane1, float[] vPointOnPlane2, float[] vLightPos, float[] destMat) {
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

    public static int loadTexture(final String name) {
//        if (0 != texture_id_tutor) {
//            glDeleteTextures(1, &texture_id_tutor);
//            texture_id_tutor = 0;
//        }
//
//        m_resourceManager.CreateTexture(name, texture_id_tutor);
//        return texture_id_tutor;
        return 0;
    }

    public static void initTextures() {
//        m_resourceManager.CreateTexture("grey_concrete128_stroke.png", texture_id_gray_concrete);
//        m_resourceManager.CreateTexture("key.png", texture_id_key);
//        m_resourceManager.CreateTexture("player.png", texture_id_player);
//        m_resourceManager.CreateTexture("level_cube.png", texture_id_level_cubes);
//        m_resourceManager.CreateTexture("level_cube_locked.png", texture_id_level_cubes_locked);
//        m_resourceManager.CreateTexture("fonts.png", texture_id_fonts);
//        m_resourceManager.CreateTexture("fonts_clear.png", texture_id_fonts_clear);
//        m_resourceManager.CreateTexture("level_numbers.png", texture_id_numbers);
//        m_resourceManager.CreateTexture("star.png", texture_id_star);
//        m_resourceManager.CreateTexture("symbols.png", texture_id_symbols);
//        m_resourceManager.CreateTexture("stat_background.png", texture_id_stat_background);
//        m_resourceManager.CreateTexture("credits.png", texture_id_credits);
//        m_resourceManager.CreateTexture("fonts_big.png", texture_id_fonts_big);
//        m_resourceManager.CreateTexture("dirty.png", texture_id_dirty);
//        m_resourceManager.CreateTexture("tutor_swipe", texture_id_tutor);
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

            m_fonts_big.put("" + a[i].ch, pFont);
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

    public static void hideProgressIndicator() {
        //m_resourceManager.HideProgressIndicator();
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
//        float volume = Engine.getMusicVolume();
//
//        volume += 0.1f;
//
//        if (volume > 1.0f) {
//            volume = 1.0f;
//        }
//
//        Engine.setMusicVolume(volume);
    }

    public static void musicVolumeDown() {
//        float volume = Engine.getMusicVolume();
//
//        volume -= 0.1f;
//
//        if (volume < 0.0f) {
//            volume = 0.0f;
//        }
//
//        Engine.setMusicVolume(volume);
    }

    public static void soundVolumeUp() {
//        float volume = Engine.getSoundVolume();
//
//        volume += 0.1f;
//
//        if (volume > 1.0f) {
//            volume = 1.0f;
//        }
//
//        Engine.setSoundFXVolume(volume);
    }

    public static void soundVolumeDown() {
//        float volume = Engine.getSoundVolume();
//
//        volume -= 0.1f;
//
//        if (volume < 0.0f) {
//            volume = 0.0f;
//        }
//
//        Engine.setSoundFXVolume(volume);
    }

    public static float getMusicVolume() {
        return 0.5f; // Engine.getMusicVolume();
    }

    public static float getSoundVolume() {
        return 0.5f; // Engine.getSoundVolume();
    }

    public static void setMusicVolume(float volume) {
        //Engine.setMusicVolume(volume);
    }

    public static void setSoundVolume(float volume) {
        //Engine.SetSoundFXVolume(volume);
    }

    public static void playSound(final String key) {
        //Engine.playSound(key);
    }

    public static void playMusic(final String key) {
        //Engine.playMusic(key);
    }

    public static void PrepareMusicToPlay(final String key) {
        //Engine.prepareMusicToPlay(key);
    }

    public static void playPreparedMusic() {
        //Engine.playPreparedMusic();
    }

    public static void stopMusic() {
        //Engine.stopMusic();
    }

    public static void enteredBackground() {
        if (m_scene != null) {
            //m_scene.enteredBackground();
        }
    }

    public static void enteredForeground() {
        if (m_scene != null) {
            //m_scene.enteredForeground();
        }
    }

    public static void drawCircleAt(float x, float y, float radius, Color color) {
        float[] vertices = new float[80];
        float[] colors = new float[150];
        int v_index = -1;
        int color_index = -1;
        float radian;
        Vector2 pos = new Vector2();
        Vector2 pt = new Vector2();

        pos.x = x;
        pos.y = y;

        vertices[++v_index] = pos.x;
        vertices[++v_index] = pos.y;

        colors[++color_index] = color.r;
        colors[++color_index] = color.g;
        colors[++color_index] = color.b;
        colors[++color_index] = color.a;

        for (float degree = 0.0f; degree <= 360.0f; degree += 36.0f) {
            radian = (float)Math.toRadians(degree);

            pt.x = pos.x + (float)Math.sin(radian) * radius;
            pt.y = pos.y + (float)Math.cos(radian) * radius;

            vertices[++v_index] = pt.x;
            vertices[++v_index] = pt.y;

            colors[++color_index] = color.r;
            colors[++color_index] = color.g;
            colors[++color_index] = color.b;
            colors[++color_index] = color.a;
        }

//        glVertexPointer(2, GL_FLOAT, 0, vertices);
//        glColorPointer(4, GL_UNSIGNED_BYTE, 0, colors);

        glDisableClientState(GL_NORMAL_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);

        glDrawArrays(GL_TRIANGLE_FAN, 0, v_index/2+1);
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

    public static void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        m_scene.render();
    }

    public static void onFingerDown(float x, float y, int finger_count) {
        m_scene.onFingerDown(x * graphics.scaleFactor, y * graphics.scaleFactor, finger_count);
    }

    public static void onFingerUp(float x, float y, int finger_count) {
        m_scene.onFingerUp(x * graphics.scaleFactor, y * graphics.scaleFactor, finger_count);
    }

    public static void onFingerMove(float prev_x, float prev_y, float cur_x, float cur_y, int finger_count) {
        m_scene.onFingerMove(prev_x * graphics.scaleFactor, prev_y * graphics.scaleFactor, cur_x * graphics.scaleFactor, cur_y * graphics.scaleFactor, finger_count);
    }

    public static void onSwipe(SwipeDirEnums type) {
        m_scene.onSwipe(type);
    }

    public static void saveOptions() {
        float mv = 0.5f; //Engine.getMusicVolume();
        float sv = 0.5f; //Engine.getSoundVolume();

        // TODO save code goes here
    
    }

    public static void loadOptions() {
        float mv = 0.5f;
        float sv = 0.5f;

        
        // TODO load code goes here
        
        
        setMusicVolume(mv);
        setSoundVolume(sv);
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

    public static boolean getCanSkipIntro() {
        //return m_resourceManager.getCanSkipIntro();
        return true;
    }

    public static void setCanSkipIntro() {
        //m_resourceManager.SetCanSkipIntro();
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

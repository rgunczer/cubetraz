package com.almagems.cubetraz;

// opengl
import static android.opengl.GLES10.*;

// java
import java.util.ArrayList;


// mine
import static com.almagems.cubetraz.Constants.*;


public final class Game {

    public enum GameState {
        Loading,
        Menu,
        Stats,
        Playing,
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

    public void renderToFBO() {
        //graphics.fboBackground.bind();
        //glViewport(0, 0, graphics.fboBackground.getWidth(), graphics.fboBackground.getHeight());

        // regular render
        glClearColor(0f, 0f, 0f, 0f);
        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);

        if (false) { // render test
            glDisable(GL_DEPTH_TEST);
            glDisable(GL_BLEND);

            graphics.setProjectionMatrix2D();
            graphics.updateViewProjMatrix();

            // custom drawing
            graphics.bindNoTexture();
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
            graphics.setProjectionMatrix3D();
            graphics.updateViewProjMatrix();

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


}

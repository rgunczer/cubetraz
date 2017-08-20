package com.almagems.cubetraz.scenes.level;

import com.almagems.cubetraz.cubes.CubeLocation;
import com.almagems.cubetraz.graphics.Graphics;
import com.almagems.cubetraz.scenes.Creator;
import com.almagems.cubetraz.graphics.Camera;
import com.almagems.cubetraz.graphics.Color;
import com.almagems.cubetraz.cubes.Cube;
import com.almagems.cubetraz.cubes.CubeFont;
import com.almagems.cubetraz.utils.CubeRotation;
import com.almagems.cubetraz.cubes.DeadCube;
import com.almagems.cubetraz.utils.EaseOutDivideInterpolation;
import com.almagems.cubetraz.game.Game;
import com.almagems.cubetraz.scenes.menu.MenuCube;
import com.almagems.cubetraz.cubes.MoverCube;
import com.almagems.cubetraz.cubes.MovingCube;
import com.almagems.cubetraz.cubes.PlayerCube;
import com.almagems.cubetraz.scenes.stat.StatInitData;
import com.almagems.cubetraz.utils.SwipeInfo;
import com.almagems.cubetraz.graphics.TexCoordsQuad;
import com.almagems.cubetraz.graphics.TexturedQuad;
import com.almagems.cubetraz.utils.UserRotation;
import com.almagems.cubetraz.math.Utils;
import com.almagems.cubetraz.math.Vector;
import com.almagems.cubetraz.math.Vector2;
import com.almagems.cubetraz.scenes.Scene;

import java.util.ArrayList;
import static android.opengl.GLES10.*;
import static com.almagems.cubetraz.game.Audio.*;

import static com.almagems.cubetraz.game.Game.*;

public final class Level extends Scene {

    enum LevelStateEnum {
        Playing,
        PrepareSolving,
        Solving,
        MovingCube,
        MovingPlayer,
        Completed,
        Paused,
        Appear,
        Undo,
        Tutorial,

        AnimFromCompleted,
        AnimFromPaused,

        AnimToCompleted,
        AnimToPaused,

        SetupAnimToCompleted,
        SetupAnimToPaused,

        SetupAnimFromCompleted,
        SetupAnimFromPaused,
        DeadAnim,
    }

    private enum LevelNextActionEnum {
        NoNextAction,
        ShowSceneSolvers,
    }

    private enum SubAppearStateEnum {
        SubAppearWait,
        SubAppearKeyAndPlayer,
        SubAppearLevel,
        SubAppearWaitAgain,
    }

    private CompletedFaceNextActionEnum m_completed_face_next_action;
    private LevelNextActionEnum mNextAction;
    private float mTimeoutUndo;
    private final ArrayList<UndoData> mUndoList = new ArrayList<>();

    public LevelStateEnum mState;
    private LevelStateEnum mStateToRestore;

    private SubAppearStateEnum m_appear_state;

    private DeadAnim mDeadAnim = new DeadAnim();
    private HUD mHud = new HUD(mDeadAnim);

    AppearDisappear appearDisappear = new AppearDisappear();

    private UserRotation mUserRotation = new UserRotation();

    private MenuCube m_menu_cube_hilite;
    private CubeFont m_font_hilite = new CubeFont();

    private float m_hilite_alpha;
    private float m_hilite_timeout;

    public ArrayList<Cube> m_list_cubes_level = new ArrayList<>();
    public ArrayList<Cube> m_list_cubes_wall_y_minus = new ArrayList<>();
    public ArrayList<Cube> m_list_cubes_wall_x_minus = new ArrayList<>();
    public ArrayList<Cube> m_list_cubes_wall_z_minus = new ArrayList<>();
    public ArrayList<Cube> m_list_cubes_edges = new ArrayList<>();
    public ArrayList<Cube> m_list_cubes_base = new ArrayList<>();
    public ArrayList<Cube> m_list_cubes_face = new ArrayList<>();
    public ArrayList<Cube> hintCubes = new ArrayList<>();

    private Cube[] mHintCubes = new Cube[MAX_HINT_CUBES];
    private boolean m_show_hint_2nd;
    private int m_hint_index;
    private float m_hint_timeout;

    public final Camera mCameraLevel = new Camera();
    private final Camera mCameraLevelCompleted = new Camera();
    private final Camera mCameraLevelPaused = new Camera();

    private float m_target_rotation_degree;

    private final EaseOutDivideInterpolation mInterpolator = new EaseOutDivideInterpolation();

    private boolean m_alter_view;
    private boolean m_reposition_view;
    private boolean m_draw_texts;

    private DifficultyEnum mDifficulty;
    private int mLevelNumber;

    Solution solution = new Solution();

    private int mMovesCounter;
    private float mTimeout;

    private CubeLocation mLocationKeyCube = new CubeLocation();

// CubeFonts
    public CubeFont mCubefontUp = new CubeFont();
    public CubeFont mCubefontMid = new CubeFont();
    public CubeFont mCubefontLow = new CubeFont();

    public final ArrayList<CubeFont> m_list_fonts = new ArrayList<>();

// spec cubes
    private final ArrayList<MovingCube> movingCubes = new ArrayList<>();
    private final ArrayList<MoverCube> moverCubes = new ArrayList<>();
    private final ArrayList<DeadCube> deadCubes = new ArrayList<>();

    private float m_fade_value;

    private final CubeRotation mCubeRotation = new CubeRotation();

    private MovingCube mMovingCube;
    private MoverCube mMoverCube;
    private DeadCube mDeadCube;

    private LevelMenu mLevelMenu = new LevelMenu();

    private boolean mDrawMenuCubes;

    private float m_t;

    public PlayerCube mPlayerCube;


    public Level() {
        mLevelNumber = 1;
        
        mPosLightCurrent = new Vector(3.0f, 5.0f, 20.0f);
        
        mCameraLevel.eye = new Vector(0.0f, 20.0f, 31.0f);
        mCameraLevel.target = new Vector(0.0f, -1.6f, 0.0f);

        mCameraLevelCompleted.eye = new Vector(0.0f, 5.0f, 34.0f);
        mCameraLevelCompleted.target = new Vector(0.0f, -1.7f, 0.0f);

        mCameraLevelPaused.eye = new Vector(0.0f, 5.0f, 34.0f);
        mCameraLevelPaused.target = new Vector(0.0f, -1.7f, 0.0f);

        float d = 1.2f;

        mCameraLevel.eye.scaled(d);

        mCameraLevelCompleted.eye.scaled(d);
        mCameraLevelCompleted.target.scaled(d);

        mCameraLevelPaused.eye.scaled(d);
        mCameraLevelPaused.target.scaled(d);

        mPlayerCube = new PlayerCube();
        mPlayerCube.init(new CubeLocation(4, 4, 4));

        mLevelMenu.init();

        m_reposition_view = false;
        m_show_hint_2nd = false;
    }

    public CubeLocation getKeyPos() {
        return mLocationKeyCube;
    }
    public int getLevelNumber() {
        return mLevelNumber;
    }
    public DifficultyEnum getDifficulty() {
        return mDifficulty;
    }

    @Override
    public void init() {
        Game.dirtyAlpha = 60f;

        mMovingCube = null;
        mMoverCube = null;
        mDeadCube = null;

        LevelInitData levelInitData = Game.levelInitData;

        // uncomment to load alternative level
        Game.levelInitData.difficulty = DifficultyEnum.Hard;
        Game.levelInitData.levelNumber = 49;

        mNextAction = LevelNextActionEnum.NoNextAction;
        
        switch (levelInitData.initAction) {
            case FullInit: {
                m_menu_cube_hilite = null;
                m_show_hint_2nd = false;

                m_hilite_timeout = 0.0f;

                mDifficulty = levelInitData.difficulty;
                mLevelNumber = levelInitData.levelNumber;

                setupMusic();

                CubeLocation zero = new CubeLocation(0, 0, 0);
                mPlayerCube.init(zero);
                mLocationKeyCube = zero;

                movingCubes.clear();
                moverCubes.clear();
                deadCubes.clear();

                mMovesCounter = 0;
                mUndoList.clear();

                mHud.set1stHint();
                mHud.setTextMoves(mMovesCounter);
                mHud.setTextUndo(mUndoList.size());

                mIsFingerDown = false;
                mIsSwipe = false;

                mCubeRotation.degree = -45.0f;
                mCubeRotation.axis = new Vector(0.0f, 1.0f, 0.0f);

                mUserRotation.reset();

                mCameraCurrent.init(mCameraLevel);

                m_list_cubes_level.clear();
                m_list_cubes_wall_y_minus.clear();
                m_list_cubes_wall_x_minus.clear();
                m_list_cubes_wall_z_minus.clear();
                m_list_cubes_edges.clear();
                m_list_cubes_base.clear();
                m_list_cubes_face.clear();
                hintCubes.clear();

                mHud.init();

                setupCubesForLevel();
                setupAppear();
            }
            break;

            case JustContinue: // take no action ?
                if (LevelStateEnum.SetupAnimToCompleted == mState) {
                
                } else {
                    mHud.setupAppear();
                }
                break;

            case ShowSolution:
                reset();
                mState = LevelStateEnum.PrepareSolving;
                mTimeout = 3.5f;
                break;
        }

        Graphics graphics = Game.graphics;
        graphics.setProjection3D();
        graphics.setModelViewMatrix3D(mCameraCurrent);

        graphics.setLightPosition(mPosLightCurrent);
    }

    private void getRatings() {
        String sign = "-";
        int minSolutionSteps = solution.getStepsCount();
        StatInitData statInitData = Game.statInitData;

        if (mMovesCounter < minSolutionSteps) {
            sign = "<";
            statInitData.title = "EXPERT";
            statInitData.stars = 3;
        }

        if (mMovesCounter == minSolutionSteps) {
            sign = "=";
            statInitData.title = "PERFECT";
            statInitData.stars = 3;
        }

        if (mMovesCounter > minSolutionSteps && mMovesCounter <= minSolutionSteps + 1) {
            sign = ">";
            statInitData.title = "EXCELLENT";
            statInitData.stars = 3;
        }

        if (mMovesCounter > minSolutionSteps + 1 && mMovesCounter <= minSolutionSteps + 4) {
            sign = ">";
            statInitData.title = "GREAT";
            statInitData.stars = 2;
        }

        if (mMovesCounter > minSolutionSteps + 4) {
            sign = ">";
            statInitData.title = "GOOD";
            statInitData.stars = 1;
        }

        statInitData.moves = "PLAYER:" + mMovesCounter + " " + sign + " BEST:" + minSolutionSteps;
    }
    
    private void showTutor(int index) {
        mState = LevelStateEnum.Tutorial;
        mHud.clearTutors();

        switch (index) {
            case Tutor_Swipe: mHud.showTutorSwipeAndGoal(); break;
            case Tutor_Drag: mHud.showTutorDrag(); break;
            case Tutor_Moving: mHud.showTutorMoving(); break;
            case Tutor_Mover: mHud.showTutorMover(); break;
            case Tutor_Dead: mHud.showTutorDead(); break;
            case Tutor_Plain: mHud.showTutorPlain(); break;
            case Tutor_MenuPause: mHud.showTutorMenuPause(); break;
            case Tutor_MenuUndo: mHud.showTutorMenuUndo(); break;
            case Tutor_MenuHint: mHud.showTutorMenuHint(); break;
            case Tutor_MenuSolvers: mHud.showTutorMenuSolvers(); break;
            default:
                break;
        }
    }
    
    public boolean isMovingCube(CubeLocation cube_pos, boolean set) {
        MovingCube movingCube;
        int size = movingCubes.size();
        for (int i = 0; i < size; ++i) {
            movingCube = movingCubes.get(i);
            CubeLocation cp = movingCube.getCubePos();

            if (cp.x == cube_pos.x && cp.y == cube_pos.y && cp.z == cube_pos.z) {
                if (set) {
                    mPlayerCube.setMovingCube(movingCube);
                }
                return true;
            }
        }
        return false;
    }

    public boolean isMoverCube(CubeLocation cube_pos, boolean set) {
        MoverCube moverCube;
        int size = moverCubes.size();
        for(int i = 0; i < size; ++i) {
            moverCube = moverCubes.get(i);
            CubeLocation cp = moverCube.getCubePos();

            if (cp.x == cube_pos.x && cp.y == cube_pos.y && cp.z == cube_pos.z) {
                if (set) {
                    if (mMoverCube != moverCube) { // bugfix! when locationPlayer hits the mover cube from behind!
                        mPlayerCube.setMoverCube(moverCube);
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean isDeadCube(CubeLocation cubeLocation, boolean set) {
        DeadCube cube;
        CubeLocation location;
        int len = deadCubes.size();
        for(int i = 0; i < len; ++i) {
            cube = deadCubes.get(i);
            location = cube.getLocation();

            if (location.x == cubeLocation.x && location.y == cubeLocation.y && location.z == cubeLocation.z) {
                if (set) {
                    mPlayerCube.setDeadCube(cube);
                }
                return true;
            }
        }
        return false;
    }

    public boolean isSpecCubeObstacle(CubeLocation cube_pos,
                                      MovingCube ignore_moving_cube,
                                      MoverCube ignore_mover_cube,
                                      DeadCube ignore_dead_cube) {
        CubeLocation cp;
        int size;
        if (!deadCubes.isEmpty()) {
            size = deadCubes.size();
            DeadCube deadCube;
            for(int i = 0; i < size; ++i) {
                deadCube = deadCubes.get(i);
                if (deadCube != ignore_dead_cube) {
                    cp = deadCube.getLocation();
                    if (cp.x == cube_pos.x && cp.y == cube_pos.y && cp.z == cube_pos.z) {
                        return true;
                    }
                }
            }
        }

        if (!moverCubes.isEmpty()) {
            size = moverCubes.size();
            MoverCube moverCube;
            for(int i = 0; i < size; ++i) {
                moverCube = moverCubes.get(i);
                if (moverCube != ignore_mover_cube) {
                    cp = moverCube.getCubePos();
                    if (cp.x == cube_pos.x && cp.y == cube_pos.y && cp.z == cube_pos.z) {
                        return true;
                    }
                }
            }
        }

        if (!movingCubes.isEmpty()) {
            size = movingCubes.size();
            MovingCube movingCube;
            for(int i = 0; i < size; ++i) {
                movingCube = movingCubes.get(i);
                if (movingCube != ignore_moving_cube) {
                    cp = movingCube.getCubePos();
                    if (cp.x == cube_pos.x && cp.y == cube_pos.y && cp.z == cube_pos.z) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void setupMusic() {
        switch (mDifficulty) {
            case Easy:
                if (mLevelNumber >= 1 && mLevelNumber <= 15) {
                    Game.audio.playMusic(MUSIC_BREEZE);
                }

                if (mLevelNumber >= 16 && mLevelNumber <= 30) {
                    Game.audio.playMusic(MUSIC_DRONES);
                }

                if (mLevelNumber >= 31 && mLevelNumber <= 45) {
                    Game.audio.playMusic(MUSIC_WAVES);
                }

                if (mLevelNumber >= 46 && mLevelNumber <= 60) {
                    Game.audio.playMusic(MUSIC_BREEZE);
                }
                break;

            case Normal:
                if (mLevelNumber >= 1 && mLevelNumber <= 15) {
                    Game.audio.playMusic(MUSIC_DRONES);
                }

                if (mLevelNumber >= 16 && mLevelNumber <= 30) {
                    Game.audio.playMusic(MUSIC_WAVES);
                }

                if (mLevelNumber >= 31 && mLevelNumber <= 45) {
                    Game.audio.playMusic(MUSIC_BREEZE);
                }

                if (mLevelNumber >= 46 && mLevelNumber <= 60) {
                    Game.audio.playMusic(MUSIC_DRONES);
                }
                break;

            case Hard:
                if (mLevelNumber >= 1 && mLevelNumber <= 15) {
                    Game.audio.playMusic(MUSIC_WAVES);
                }

                if (mLevelNumber >= 16 && mLevelNumber <= 30) {
                    Game.audio.playMusic(MUSIC_BREEZE);
                }

                if (mLevelNumber >= 31 && mLevelNumber <= 45) {
                    Game.audio.playMusic(MUSIC_DRONES);
                }                    

                if (mLevelNumber >= 46 && mLevelNumber <= 60) {
                    Game.audio.playMusic(MUSIC_WAVES);
                }
                break;
        }
    }

    private void setupAppear() {
        mState = LevelStateEnum.Appear;
        m_appear_state = SubAppearStateEnum.SubAppearWait;
        mDrawMenuCubes = false;
        m_draw_texts = false;
        mTimeout = 0.0f;
        m_alter_view = false;

        if (Utils.rand.nextBoolean()) {
            appearDisappear.level.setLevelAndDirection(0, 1);
        } else {
            appearDisappear.level.setLevelAndDirection(MAX_CUBE_COUNT - 1, -1);
        }
        mLocationKeyCube.reset();
    }

    private void setupCubesForLevel() {
        m_list_cubes_wall_y_minus.clear();
        m_list_cubes_wall_x_minus.clear();
        m_list_cubes_wall_z_minus.clear();
        m_list_cubes_edges.clear();

        Game.resetCubes();

        // ceiling
        for (int z = 0; z < MAX_CUBE_COUNT; ++z) {
            for (int x = 0; x < MAX_CUBE_COUNT; ++x) {
                Game.cubes[x][7][z].type = CubeTypeEnum.CubeIsInvisibleAndObstacle;
                Game.cubes[x][1][z].type = CubeTypeEnum.CubeIsInvisibleAndObstacle;

                Game.cubes[x][7][z].setColor( Game.baseColor );
                Game.cubes[x][1][z].setColor( Game.baseColor );
            }
        }

        // wall x minus
        for (int z = 0; z < MAX_CUBE_COUNT; ++z) {
            for (int y = 0; y < MAX_CUBE_COUNT; ++y) {
                Game.cubes[1][y][z].type = CubeTypeEnum.CubeIsInvisibleAndObstacle;
                Game.cubes[1][y][z].setColor( Game.baseColor );
            }
        }

        // wall z minus
        for (int x = 0; x < MAX_CUBE_COUNT; ++x) {
            for (int y = 0; y < MAX_CUBE_COUNT; ++y) {
                Game.cubes[x][y][1].type = CubeTypeEnum.CubeIsInvisibleAndObstacle;
                Game.cubes[x][y][1].setColor( Game.baseColor );
            }
        }


        // wall y minus
        for (int z = 2; z < MAX_CUBE_COUNT; ++z) {
            for (int x = 2; x < MAX_CUBE_COUNT; ++x) {
                Game.cubes[x][1][z].type = CubeTypeEnum.CubeIsVisibleAndObstacle;
                Game.cubes[x][1][z].setColor( Game.baseColor );

                m_list_cubes_wall_y_minus.add(Game.cubes[x][1][z]);
            }
        }

        // walls
        for (int y = 2; y < 7; ++y) {
            for (int x = 2; x < MAX_CUBE_COUNT; ++x) {
                Game.cubes[x][y][1].type = CubeTypeEnum.CubeIsVisibleAndObstacle;
                Game.cubes[x][y][1].setColor( Game.baseColor );

                if (y > 0) {
                    m_list_cubes_wall_z_minus.add(Game.cubes[x][y][1]);
                }
            }

            for (int z = 2; z < MAX_CUBE_COUNT; ++z) {
                Game.cubes[1][y][z].type = CubeTypeEnum.CubeIsVisibleAndObstacle;
                Game.cubes[1][y][z].setColor( Game.baseColor );

                if (y > 0) {
                    m_list_cubes_wall_x_minus.add(Game.cubes[1][y][z]);
                }
            }
        }

        // edges
        for (int i = 1; i < MAX_CUBE_COUNT; ++i) {
            m_list_cubes_edges.add(Game.cubes[i][1][1]);
            Game.cubes[i][1][1].setColor(Game.baseColor);
        }

        for (int i = 1; i < MAX_CUBE_COUNT; ++i) {
            m_list_cubes_edges.add(Game.cubes[1][1][i]);
            Game.cubes[1][1][i].setColor(Game.baseColor);
        }

        for (int i = 1; i < 7; ++i) {
            m_list_cubes_edges.add(Game.cubes[1][i][1]);
            Game.cubes[1][i][1].setColor(Game.baseColor);
        }
    }

    private void setupDeadCube(DeadCube deadCube) {
        mState = LevelStateEnum.DeadAnim;
        mTimeout = 1.5f;

        mDeadAnim.init();

        mDeadCube = deadCube;
        mDeadCube.hiLite();
    }

    private void setupMoveCube(MovingCube movingCube) {
        mStateToRestore = mState;
        mState = LevelStateEnum.MovingCube;
        mTimeout = 0.1f;

        mMovingCube = movingCube;
        mMovingCube.move();
    }

    private void setupMoverCube(MoverCube moverCube) {
        int movement = moverCube.getMoveDir();
        mStateToRestore = mState;
        mState = LevelStateEnum.MovingPlayer;
        mTimeout = 0.15f;

        mMoverCube = moverCube;
        mMoverCube.hiLite();

        mPlayerCube.moveOnAxis(movement);

        if (mUndoList.size() > 0) {
            UndoData ud = mUndoList.get(mUndoList.size() - 1);
            ud.movingCube = mPlayerCube.movingCube; //movingCubes.front();

            if (ud.movingCube != null) {
                ud.movingCubeMoveDir = ud.movingCube.getMovement();
                ud.movingCubeLocation = ud.movingCube.getCubePos();
            }
        }
    }
    
    public void setSolversCount() {
        mHud.setTextSolver(Game.options.getSolverCount());
    }
    
    private void setAnimToCompleted() {
        mState = LevelStateEnum.AnimToCompleted;

        mDrawMenuCubes = true;
        m_draw_texts = true;

        m_list_cubes_base.clear();
        m_list_cubes_face.clear();

        appearDisappear.face.clear();
        appearDisappear.base.clear();

        Cube cube;

        // way for menu cubes carved out
        for (int i = 0; i < MAX_CUBE_COUNT; ++i) {
            cube = Game.cubes[i][1][0];
            cube.type = CubeTypeEnum.CubeIsInvisible;

            cube = Game.cubes[i][3][0];
            cube.type = CubeTypeEnum.CubeIsInvisible;

            cube = Game.cubes[i][5][0];
            cube.type = CubeTypeEnum.CubeIsInvisible;
        }

        for (int i = 0; i < 7; ++i) {
            cube = Game.cubes[8][i][0];
            cube.type = CubeTypeEnum.CubeIsVisibleAndObstacle;
            cube.setColor( Game.baseColor );
            appearDisappear.base.addAppear(cube);
        }

        for (int i = 0; i < 7; ++i) {
            cube = Game.cubes[0][i][1];
            cube.type = CubeTypeEnum.CubeIsVisibleAndObstacle;
            cube.setColor( Game.baseColor );
            appearDisappear.base.addAppear(cube);
        }

        for (int i = 0; i < MAX_CUBE_COUNT; ++i) {
            cube = Game.cubes[i][0][1];
            cube.type = CubeTypeEnum.CubeIsVisibleAndObstacle;
            cube.setColor( Game.baseColor );
            appearDisappear.base.addAppear(cube);
        }

        cube = Game.cubes[7][0][0];
        cube.type = CubeTypeEnum.CubeIsVisibleAndObstacle;
        cube.setColor(Game.faceColor);
        appearDisappear.face.addAppear(cube);

        cube = Game.cubes[7][2][0];
        cube.type = CubeTypeEnum.CubeIsVisibleAndObstacle;
        cube.setColor(Game.faceColor);
        appearDisappear.face.addAppear(cube);

        cube = Game.cubes[7][4][0];
        cube.type = CubeTypeEnum.CubeIsVisibleAndObstacle;
        cube.setColor(Game.faceColor);
        appearDisappear.face.addAppear(cube);

        cube = Game.cubes[7][6][0];
        cube.type = CubeTypeEnum.CubeIsVisibleAndObstacle;
        cube.setColor( Game.faceColor );
        appearDisappear.face.addAppear(cube);

        appearDisappear.face.setLevelAndDirection(0, 1);
        appearDisappear.base.setLevelAndDirection(0, 1);

        m_t = 0.0f;

        if (DifficultyEnum.Hard == mDifficulty && 60 == mLevelNumber) {
            m_completed_face_next_action = CompletedFaceNextActionEnum.Finish;
        } else {
            m_completed_face_next_action = CompletedFaceNextActionEnum.Next;
        }

        Creator.createTextsLevelCompletedFace(this, m_completed_face_next_action);

        CubeFont cubeFont;
        int size = m_list_fonts.size();
        for(int i = 0; i < size; ++i) {
            cubeFont = m_list_fonts.get(i);
            cubeFont.setColor(Game.textColorOnCubeFace);
        }

        Color color = new Color(20, 0, 0, 255);
        mCubefontUp.setColor(color);
        mCubefontMid.setColor(color);
        mCubefontLow.setColor(color);

        m_target_rotation_degree = mCubeRotation.degree - 90.0f - 45.0f;

        mInterpolator.setup(mCubeRotation.degree, m_target_rotation_degree, 5.0f);

        mLevelMenu.setupForAnimCompleted();
    }

    private void updateAnimToCompleted() {
        mLevelMenu.update();

        Cube cube;

        cube = appearDisappear.base.getCubeFromAppearList();
        if (cube != null) {
            m_list_cubes_base.add(cube);
        }

        cube = appearDisappear.face.getCubeFromAppearList();
        if (cube != null) {
            m_list_cubes_face.add(cube);
        }

        m_t += 0.04f;
        if (m_t > 1f) {
            m_t = 1f;
        }

        Utils.lerpCamera(mCameraLevel, mCameraLevelCompleted, m_t, mCameraCurrent);

        mInterpolator.interpolate();
        mCubeRotation.degree = mInterpolator.getValue();

        float diff = Math.abs(m_target_rotation_degree) - Math.abs(mInterpolator.getValue());

        if (diff < 0.1f && appearDisappear.base.lst_appear.isEmpty() && appearDisappear.face.lst_appear.isEmpty() && (Math.abs(1.0f - m_t) < EPSILON)) {
            mCubeRotation.degree = m_target_rotation_degree;
            mState = LevelStateEnum.Completed;

            Color color = new Color(100, 0, 0, 230);

            mCubefontUp.setColor(color);
            mCubefontMid.setColor(color);
            mCubefontLow.setColor(color);

            m_show_hint_2nd = false;

            for (int i = 0; i < MAX_HINT_CUBES; ++i) {
                mHintCubes[i] = null;
            }
        }
    }

    private void updateCompleted() {
        mLevelMenu.update();

        mCubefontUp.warmByFactor(60);
        mCubefontMid.warmByFactor(60);
        mCubefontLow.warmByFactor(60);

        if (mLevelMenu.cubeUp.isDone()) { // next
            if (0 == mLevelMenu.cubeUp.cubeLocation.x) {
                switch (m_completed_face_next_action) {
                    case Next:
                        ++mLevelNumber;
                        if (mLevelNumber > 60) {
                            switch (mDifficulty) {
                                case Easy:
                                    mDifficulty = DifficultyEnum.Normal;
                                    mLevelNumber = 1;
                                    Game.audio.stopMusic();
                                    setupMusic();
                                    break;

                                case Normal:
                                    mDifficulty = DifficultyEnum.Hard;
                                    mLevelNumber = 1;
                                    Game.audio.stopMusic();
                                    setupMusic();
                                    break;

                                case Hard: // win!
                                    break;
                            }
                        } else {
                            if (mLevelNumber == 16 || mLevelNumber == 31 || mLevelNumber == 46) {
                                Game.audio.stopMusic();
                                setupMusic();
                            }
                        }
                        setAnimFromCompleted();
                        break;

                    case Finish:
                        ++mLevelNumber;
                        setAnimFromCompleted();
                        break;
                }
            }
        }

        if (mLevelMenu.cubeMid.isDone()) { // replay
            if (mLevelMenu.cubeMid.cubeLocation.x == 0) {
                reset();
                setAnimFromCompleted();
            }
        }

        if (mLevelMenu.cubeLow.isDone()) { // quit
            if (mLevelMenu.cubeLow.cubeLocation.x == 0) {
                eventQuit();
            }
        }
    }

    private void setAnimFromCompleted() {
        mState = LevelStateEnum.AnimFromCompleted;
        m_t = 0.0f;
        m_target_rotation_degree = -45.0f;

        mInterpolator.setup(mCubeRotation.degree, m_target_rotation_degree, 6.0f);

        appearDisappear.base.setLevelAndDirection(MAX_CUBE_COUNT - 1, -1);
        appearDisappear.face.setLevelAndDirection(MAX_CUBE_COUNT - 1, -1);
    }

    private void updateAnimFromCompleted() {
        m_t += 0.04f;
        if (m_t > 1f) {
            m_t = 1f;
        }

        Utils.lerpCamera(mCameraLevelCompleted, mCameraLevel, m_t, mCameraCurrent);

        Cube cube;

        cube = appearDisappear.base.getCubeFromDisappearList();
        if (cube != null) {
            m_list_cubes_base.remove(cube);
        }

        cube = appearDisappear.face.getCubeFromDisappearList();
        if (cube != null) {
            m_list_cubes_face.remove(cube);
        }

        mInterpolator.interpolate();
        mCubeRotation.degree = mInterpolator.getValue();

        float diff = Math.abs(mCubeRotation.degree) - Math.abs(m_target_rotation_degree);

        if (diff < 0.1f && ((1.0f - m_t) < EPSILON) && appearDisappear.face.lst_disappear.isEmpty() && appearDisappear.base.lst_disappear.isEmpty()) {
            mCubeRotation.degree = m_target_rotation_degree;

            // TODO: later
            if (DifficultyEnum.Hard == mDifficulty && 61 == mLevelNumber) {
                Game.showScene(Scene_Outro);
            } else {
                setupAppear();
            }
//            Game.showScene(Scene_Outro);
        }
    }
    
    private void setAnimToPaused() {
        mState = LevelStateEnum.AnimToPaused;

        mDrawMenuCubes = true;
        m_draw_texts = true;

        m_target_rotation_degree = mCubeRotation.degree + 90.0f + 45.0f;
        mInterpolator.setup(mCubeRotation.degree, m_target_rotation_degree, 5.0f);
        m_t = 0.0f;

        Creator.createTextsLevelPausedFace(this);

        CubeFont cubeFont;
        int size = m_list_fonts.size();
        for(int i = 0; i < size; ++i) {
            cubeFont = m_list_fonts.get(i);
            cubeFont.setColor(Game.textColorOnCubeFace);
        }

        Color color = new Color(20, 0, 0, 20);
        mCubefontUp.setColor(color);
        mCubefontMid.setColor(color);
        mCubefontLow.setColor(color);

        m_list_cubes_base.clear();
        m_list_cubes_face.clear();

        appearDisappear.face.clear();
        appearDisappear.base.clear();

        Cube cube;

        for (int i = 1; i < MAX_CUBE_COUNT; ++i) {
            cube = Game.cubes[0][5][i];
            cube.type = CubeTypeEnum.CubeIsInvisible;

            cube = Game.cubes[0][3][i];
            cube.type = CubeTypeEnum.CubeIsInvisible;

            cube = Game.cubes[0][1][i];
            cube.type = CubeTypeEnum.CubeIsInvisible;
        }

        for (int i = 0; i < 7; ++i) {
            cube = Game.cubes[0][i][0];
            cube.type = CubeTypeEnum.CubeIsVisibleAndObstacle;
            cube.setColor(Game.faceColor);
            appearDisappear.face.addAppear(cube);

            cube = Game.cubes[1][i][0];
            cube.type = CubeTypeEnum.CubeIsVisibleAndObstacle;
            cube.setColor( Game.baseColor );
            appearDisappear.base.addAppear(cube);
        }

        for (int i = 0; i < MAX_CUBE_COUNT; ++i) {
            cube = Game.cubes[1][0][i];
            cube.type = CubeTypeEnum.CubeIsVisibleAndObstacle;
            cube.setColor( Game.baseColor );
            appearDisappear.base.addAppear(cube);
        }

        cube = Game.cubes[0][0][1];
        cube.type = CubeTypeEnum.CubeIsVisibleAndObstacle;
        cube.setColor(Game.faceColor);
        appearDisappear.face.addAppear(cube);

        cube = Game.cubes[0][2][1];
        cube.type = CubeTypeEnum.CubeIsVisibleAndObstacle;
        cube.setColor(Game.faceColor);
        appearDisappear.face.addAppear(cube);

        cube = Game.cubes[0][4][1];
        cube.type = CubeTypeEnum.CubeIsVisibleAndObstacle;
        cube.setColor(Game.faceColor);
        appearDisappear.face.addAppear(cube);

        cube = Game.cubes[0][6][1];
        cube.type = CubeTypeEnum.CubeIsVisibleAndObstacle;
        cube.setColor(Game.faceColor);
        appearDisappear.face.addAppear(cube);

        appearDisappear.face.setLevelAndDirection(0, 1);
        appearDisappear.base.setLevelAndDirection(0, 1);

        mLevelMenu.setupForAnimPaused();
    }

    private void updateAnimToPaused() {
        mLevelMenu.update();

        Cube cube;

        cube = appearDisappear.base.getCubeFromAppearList();
        if (cube != null) {
            m_list_cubes_base.add(cube);
        }

        cube = appearDisappear.face.getCubeFromAppearList();
        if (cube != null) {
            m_list_cubes_face.add(cube);
        }

        m_t += 0.04f;
        if (m_t > 1f) {
            m_t = 1f;
        }

        Utils.lerpCamera(mCameraLevel, mCameraLevelPaused, m_t, mCameraCurrent);

        mInterpolator.interpolate();
        mCubeRotation.degree = mInterpolator.getValue();

        float diff = Math.abs(mInterpolator.getValue()) - Math.abs(m_target_rotation_degree);
        diff = Math.abs(diff);

        if (diff < 0.1f && ((1.0f - m_t) < EPSILON_SMALL) && appearDisappear.face.lst_appear.isEmpty() && appearDisappear.base.lst_appear.isEmpty() ) {
            mCubeRotation.degree = m_target_rotation_degree;
            mState = LevelStateEnum.Paused;

            Color color = new Color(100, 0, 0, 230);
            mCubefontUp.setColor(color);
            mCubefontMid.setColor(color);
            mCubefontLow.setColor(color);

            m_show_hint_2nd = false;

            for (int i = 0; i < MAX_HINT_CUBES; ++i) {
                mHintCubes[i] = null;
            }
        }
    }

    private void updatePaused() {
        mLevelMenu.update();

        mCubefontUp.warmByFactor(60);
        mCubefontMid.warmByFactor(60);
        mCubefontLow.warmByFactor(60);

        if (mLevelMenu.cubeUp.isDone()) { // back
            if (mLevelMenu.cubeUp.cubeLocation.z == 8) {
                setAnimFromPaused();
            }
        }

        if (mLevelMenu.cubeMid.isDone()) { // reset
            if (mLevelMenu.cubeMid.cubeLocation.z == 8) {
                reset();
                setAnimFromPaused();
            }
        }

        if (mLevelMenu.cubeLow.isDone()) { // quit
            if (mLevelMenu.cubeLow.cubeLocation.z == 8) {
                mLevelMenu.cubeLow.lst_cubes_to_hilite.clear();
                eventQuit();
            }
        }
    }

    private void setAnimFromPaused() {
        mHud.setupAppear();
        m_target_rotation_degree = mCubeRotation.degree - 90.0f - 45.0f;
        mInterpolator.setup(mCubeRotation.degree, m_target_rotation_degree, 6.0f);
        m_t = 0.0f;
        mState = LevelStateEnum.AnimFromPaused;

        appearDisappear.base.setLevelAndDirection(MAX_CUBE_COUNT - 1, -1);
        appearDisappear.face.setLevelAndDirection(MAX_CUBE_COUNT - 1, -1);
    }

    private void updateAnimFromPaused() {
        m_t += 0.04f;
        if (m_t > 1f) {
            m_t = 1f;
        }

        Utils.lerpCamera(mCameraLevelPaused, mCameraLevel, m_t, mCameraCurrent);

        Cube cube;

        cube = appearDisappear.base.getCubeFromDisappearList();
        if (cube != null) {
            m_list_cubes_base.remove(cube);
        }

        cube = appearDisappear.face.getCubeFromDisappearList();
        if (cube != null) {
            m_list_cubes_face.remove(cube);
        }

        mInterpolator.interpolate();
        mCubeRotation.degree = mInterpolator.getValue();

        // 90 <. - 45
        float diff = Math.abs( Math.abs(m_target_rotation_degree) - Math.abs(mInterpolator.getValue()) );

        if (diff < 0.1f && ((1.0f - m_t) < EPSILON) && appearDisappear.face.lst_disappear.isEmpty() && appearDisappear.base.lst_disappear.isEmpty() ) {
            mCubeRotation.degree = m_target_rotation_degree;
            mState = LevelStateEnum.Playing;
            mDrawMenuCubes = false;
            m_draw_texts = false;
        }
    }

    @Override
    public void update() {
        switch (mState) {
            case Playing:                   updatePlaying();            break;
            case Undo:                      updateUndo();               break;
            case PrepareSolving:            updatePrepareSolving();     break;
            case Solving:                   updateSolving();            break;
            case MovingCube:                updateMovingCube();         break;
            case MovingPlayer:              updateMovingPlayer();       break;
            case Completed:                 updateCompleted();          break;
            case Paused:                    updatePaused();             break;
            case Appear:                    updateAppear();             break;
            case AnimFromCompleted:         updateAnimFromCompleted();  break;
            case AnimFromPaused:            updateAnimFromPaused();     break;
            case AnimToCompleted:           updateAnimToCompleted();    break;
            case AnimToPaused:              updateAnimToPaused();       break;
            case SetupAnimToCompleted:      setAnimToCompleted();       break;
            case SetupAnimToPaused:         setAnimToPaused();          break;
            case SetupAnimFromCompleted:    setAnimFromCompleted();     break;
            case SetupAnimFromPaused:       setAnimFromPaused();        break;
            case DeadAnim:                  updateDeadAnim();           break;
            case Tutorial:                  updateTutorial();           break;
            default:                
                break;
        }

        warmCubes();

        if (m_reposition_view) {
            m_t += 0.1f;

            if (m_t >= 1.0f) {
                m_t = 1.0f;
                m_reposition_view = false;
                mUserRotation.reset();
            } else {
                mUserRotation.current.x = Utils.lerp(mUserRotation.from.x, 0.0f, m_t);
                mUserRotation.current.y = Utils.lerp(mUserRotation.from.y, 0.0f, m_t);
            }
        }

        mHud.update();

        if (LevelNextActionEnum.NoNextAction != mNextAction) {
            if (HUDStateEnum.DoneHUD == mHud.getState() ) {
                switch (mNextAction) {
                    case ShowSceneSolvers:
                        Game.renderToFBO(this);
                        Game.showScene(Scene_Solvers);
                        mNextAction = LevelNextActionEnum.NoNextAction;
                        break;

                    default:
                        break;
                }
            }
        }

        if (m_menu_cube_hilite != null) {
            m_hilite_alpha += 0.05f;

            if (m_hilite_alpha > 0.2f) {
                m_hilite_alpha = 0.2f;
            }
        }

        if (m_show_hint_2nd) {
            m_hint_timeout -= 0.05f;

            if (m_hint_timeout < 0.0f) {
                m_hint_timeout = 0.2f;
                Cube cube = mHintCubes[m_hint_index];
                if (cube != null) {
                    Color col = new Color(255, 0, 0, 255);

                    cube.colorCurrent.init(col);
                    ++m_hint_index;

                    // locate cube among moving cubes
                    MovingCube movingCube;
                    int size = movingCubes.size();
                    for(int i = 0; i < size; ++i) {
                        movingCube = movingCubes.get(i);
                        CubeLocation cube_pos = new CubeLocation(cube.x, cube.y, cube.z);
                        CubeLocation moving_cube_pos = movingCube.getCubePos();

                        if (moving_cube_pos.x == cube_pos.x && moving_cube_pos.y == cube_pos.y && moving_cube_pos.z == cube_pos.z) {
                            movingCube.setCurrentColor(col);
                        }
                    }
                } else {
                    m_show_hint_2nd = false;
                }
            }
        }
    }

    private void warmCubes() {
        m_hilite_timeout -= 0.01f;

        if (m_hilite_timeout < 0.0f) {
            m_hilite_timeout = 0.05f;
            Color color = new Color(99, 99, 99, 255);

            if (!mLevelMenu.cubeUp.lst_cubes_to_hilite.isEmpty()) {
                Cube p = mLevelMenu.cubeUp.lst_cubes_to_hilite.get(0);
                mLevelMenu.cubeUp.lst_cubes_to_hilite.remove(p);

                p.colorCurrent.init(color);
            }

            if (!mLevelMenu.cubeMid.lst_cubes_to_hilite.isEmpty()) {
                Cube p = mLevelMenu.cubeMid.lst_cubes_to_hilite.get(0);
                mLevelMenu.cubeMid.lst_cubes_to_hilite.remove(p);

                p.colorCurrent.init(color);
            }

            if (!mLevelMenu.cubeLow.lst_cubes_to_hilite.isEmpty()) {
                Cube p = mLevelMenu.cubeLow.lst_cubes_to_hilite.get(0);
                mLevelMenu.cubeLow.lst_cubes_to_hilite.remove(p);

                p.colorCurrent.init(color);
            }
        }

        int size;
        Cube cube;

        size = m_list_cubes_wall_x_minus.size();
        for (int i = 0; i < size; ++i) {
            cube = m_list_cubes_wall_x_minus.get(i);
            cube.warmByFactor(WARM_FACTOR);
        }

        size = m_list_cubes_edges.size();
        for (int i = 0; i < size; ++i) {
            cube = m_list_cubes_edges.get(i);
            cube.warmByFactor(WARM_FACTOR);
        }

        size = m_list_cubes_wall_y_minus.size();
        for (int i = 0; i < size; ++i) {
            cube = m_list_cubes_wall_y_minus.get(i);
            cube.warmByFactor(WARM_FACTOR);
        }

        size = m_list_cubes_wall_z_minus.size();
        for (int i = 0; i < size; ++i) {
            cube = m_list_cubes_wall_z_minus.get(i);
            cube.warmByFactor(WARM_FACTOR);
        }

        size = m_list_cubes_base.size();
        for (int i = 0; i < size; ++i) {
            cube = m_list_cubes_base.get(i);
            cube.warmByFactor(WARM_FACTOR);
        }

        size = m_list_cubes_level.size();
        for (int i = 0; i < size; ++i) {
            cube = m_list_cubes_level.get(i);
            cube.warmByFactor(WARM_FACTOR);
        }

        size = movingCubes.size();
        MovingCube movingCube;
        for (int i = 0; i < size; ++i) {
            movingCube = movingCubes.get(i);
            movingCube.warmByFactor(WARM_FACTOR);
        }
    }

    private void updateDeadAnim() {
        mTimeout -= 0.01f;
        mDeadAnim.update();

        if (mDeadAnim.isDone()) {
            reset();
            mState = LevelStateEnum.Playing;
            mDeadCube = null;
        }
    }

    private void updateMovingCube() {
        mTimeout -= 0.01f;

        if (mTimeout < 0.0f) {
            mMovingCube.update();

            if (mMovingCube.isDone()) {
                mState = mStateToRestore;
                mMovingCube = null;

                if (LevelStateEnum.Solving == mStateToRestore) {
                    mTimeout = 1.0f;
                }
            }
        }
    }

    private void updateMovingPlayer() {
        mTimeout -= 0.01f;

        if (mTimeout < 0.0f) {
            mPlayerCube.update();

            if (mPlayerCube.isDone()) {
                if (mMoverCube != null) {
                    mMoverCube.noHiLite();
                    mMoverCube = null;
                }

                if (mMovingCube != null) {
                    mMovingCube.noHiLite();
                    mMovingCube = null;
                }

                if (mDeadCube != null) {
                    mDeadCube.noHilite();
                    mDeadCube = null;
                }

                Game.audio.playSound(SOUND_CUBE_HIT);

                mState = mStateToRestore;

                if (LevelStateEnum.Solving == mStateToRestore) {
                    mTimeout = 1.0f;
                }

                CubeLocation player = mPlayerCube.getLocation();
                CubeLocation key = mLocationKeyCube;

                if (player.x == key.x && player.y == key.y && player.z == key.z) {
                    Game.levelComplete();
                } else {
                    if (mPlayerCube.deadCube != null) {
                        setupDeadCube(mPlayerCube.deadCube);
                    } else if (mPlayerCube.movingCube != null) {
                        setupMoveCube(mPlayerCube.movingCube);
                    } else if (mPlayerCube.moverCube != null) {
                        setupMoverCube(mPlayerCube.moverCube);
                    }
                }
            }
        }
    }

    private void updateSolving() {
        mTimeout -= 0.02f;

        if (mTimeout <= 0.0f) {
            mTimeout = 1.0f;

            if (mPlayerCube.isDone()) {
                int dir = solution.getStep(); //m_ar_solution[m_solution_pointer++];
                boolean success = mPlayerCube.moveOnAxis(dir);

                if (success) {
                    mStateToRestore = mState;
                    mState = LevelStateEnum.MovingPlayer;
                    mTimeout = 0.0f;

                    ++mMovesCounter;
                    mHud.setTextMoves(mMovesCounter);
                }
            }
        }

        mPlayerCube.update();
    }

    private void updatePrepareSolving() {
        mHud.update();

        mTimeout -= 0.03f;

        if (mTimeout < 1.5f) {
            mHud.showPrepareSolving(true, solution.getStepsCount());
        }

        if (mTimeout <= 0.0f) {
            mHud.showPrepareSolving(false, solution.getStepsCount());

            mState = LevelStateEnum.Solving;
            solution.index = 0;
            m_fade_value = 1.0f;
            mTimeout = 1.0f;
            mHud.setupAppear();
        }
    }

    private void updateAppear() {
        mTimeout += 0.01f;

        switch (m_appear_state) {
            case SubAppearWait: 
                if (mTimeout > 0.2f) {
                    Cube cube;

                    cube = appearDisappear.level.getCubeFromDisappearList();
                    if (cube != null) {
                        cube.type = CubeTypeEnum.CubeIsInvisible;
                        m_list_cubes_level.remove(cube);
                    }

                    cube = appearDisappear.level.getCubeFromDisappearList();
                    if (cube != null) {
                        cube.type = CubeTypeEnum.CubeIsInvisible;
                        m_list_cubes_level.remove(cube);
                    }

                    if (!movingCubes.isEmpty()) {
                        MovingCube movingCube = movingCubes.get(0);
                        movingCubes.remove(movingCube);
                        LevelBuilder.movingCubes.add(movingCube);
                    }

                    if (!moverCubes.isEmpty()) {
                        MoverCube moverCube = moverCubes.get(0);
                        moverCubes.remove(moverCube);
                        LevelBuilder.moverCubes.add(moverCube);
                    }

                    if (!deadCubes.isEmpty()) {
                        DeadCube deadCube = deadCubes.get(0);
                        deadCubes.remove(deadCube);
                        LevelBuilder.deadCubes.add(deadCube);
                    }

                    if (m_list_cubes_level.isEmpty() &&
                            movingCubes.isEmpty() &&
                            moverCubes.isEmpty() &&
                            deadCubes.isEmpty()) {
                        mTimeout = 0.0f;
                        m_appear_state = SubAppearStateEnum.SubAppearKeyAndPlayer;
                        m_t = 0.0f;
                        m_fade_value = 0.0f;

                        eventBuildLevel();
                        reset();
                    }
                }
                break;

            case SubAppearKeyAndPlayer:
                if (mTimeout > 0.1f) {
                    m_t += 0.1;

                    if (m_t > 1.0f) {
                        mTimeout = 0.0f;
                        m_t = 1.0f;
                        m_appear_state = SubAppearStateEnum.SubAppearWaitAgain;
                    }

                    m_fade_value = Utils.lerp(0.0f, 1.0f, m_t);
                }
                break;

            case SubAppearWaitAgain:
                if (mTimeout > 0.2) {
                    m_appear_state = SubAppearStateEnum.SubAppearLevel;
                }
                break;

            case SubAppearLevel:
                if (appearDisappear.level.lst_appear.isEmpty() &&
                    LevelBuilder.movingCubes.isEmpty() &&
                    LevelBuilder.moverCubes.isEmpty() &&
                    LevelBuilder.deadCubes.isEmpty()) {
                    mState = LevelStateEnum.Playing;

                if (DifficultyEnum.Easy == mDifficulty) {
                    if (1 == mLevelNumber) {
                        showTutor(Tutor_Swipe);
                    }

                    if (2 == mLevelNumber) {
                        showTutor(Tutor_MenuPause);
                    }

                    if (3 == mLevelNumber) {
                        showTutor(Tutor_MenuHint);
                    }

                    if (4 == mLevelNumber) {
                        showTutor(Tutor_Drag);
                    }

                    if (5 == mLevelNumber) {
                        showTutor(Tutor_Plain);
                    }

                    if (10 == mLevelNumber) {
                        showTutor(Tutor_Moving);
                    }

                    if (12 == mLevelNumber) {
                        showTutor(Tutor_Mover);
                    }

                    if (19 == mLevelNumber) {
                        showTutor(Tutor_Dead);
                    }
                }                
            } else {
                if (mTimeout > 0.02f) {
                    Cube cube = appearDisappear.level.getCubeFromAppearList();

                    if (cube != null) {
                        m_list_cubes_level.add(cube);
                    }

                    if (!LevelBuilder.movingCubes.isEmpty()) {
                        MovingCube movingCube = LevelBuilder.movingCubes.get(0);
                        LevelBuilder.movingCubes.remove(movingCube);
                        movingCubes.add(movingCube);
                    }

                    if (!LevelBuilder.moverCubes.isEmpty()) {
                        MoverCube moverCube = LevelBuilder.moverCubes.get(0);
                        LevelBuilder.moverCubes.remove(moverCube);
                        moverCubes.add(moverCube);
                    }

                    if (!LevelBuilder.deadCubes.isEmpty()) {
                        DeadCube deadCube = LevelBuilder.deadCubes.get(0);
                        LevelBuilder.deadCubes.remove(deadCube);
                        deadCubes.add(deadCube);
                    }

                    mTimeout = 0.0f;
                }
            }
            break;

            default:
                break;
        } // switch
    }

    private void updateTutorial() {
        if (!mHud.isTutorDisplaying()) {
            mState = LevelStateEnum.Playing;
        }
    }

    private void updatePlaying() {
        mPlayerCube.update();
    }

    private void updateUndo() {
        mTimeoutUndo -= 0.01f;

        if (mTimeoutUndo <= 0.0f) {
            if (mPlayerCube.isDone()) {
                if (!mUndoList.isEmpty()) {
                    UndoData ud = mUndoList.get( mUndoList.size() - 1 );

                    if (ud.moverCube != null) {
                        eventUndo();
                        return;
                    }
                }
                mState = LevelStateEnum.Playing;
            }
            mTimeoutUndo = UNDO_TIMEOUT;
        }
    }

    private boolean isPlayerAndKeyInSamePosition() {
        if (mLocationKeyCube.x == mPlayerCube.location.x &&
            mLocationKeyCube.y == mPlayerCube.location.y &&
            mLocationKeyCube.z == mPlayerCube.location.z) {
            return true;
        } else{
            return false;
        }
    }

    private void drawTheCube() {
        Graphics graphics = Game.graphics;

        glClear(GL_STENCIL_BUFFER_BIT);

        Cube cube;

        glEnable(GL_LIGHTING);

        glEnable(GL_TEXTURE_2D);
        graphics.textureGrayConcrete.bind();

        Vector light = Utils.rotate3D_AroundYAxis(mPosLightCurrent.x, mPosLightCurrent.y, mPosLightCurrent.z, -mCubeRotation.degree - mUserRotation.current.y);
        Vector light_tmp = Utils.rotate3D_AroundXAxis(light.x, light.y, light.z, -mUserRotation.current.x);
        light.init(light_tmp);

        glEnable(GL_STENCIL_TEST);
        glDisable(GL_CULL_FACE);

        graphics.bindStreamSources3d();

        int size;
        float[] shadowMat = new float[16];
        
        for (int j = 0; j < 3; ++j) { // 3 times (1 floor + 2 walls)
            graphics.resetBufferIndices();

            switch (j) {
                case 0: // floor
                    Utils.calcShadowMatrixFloor(light, shadowMat, 0.0f);
                    size = m_list_cubes_wall_y_minus.size();
                    for(int i = 0; i < size; ++i) {
                        cube = m_list_cubes_wall_y_minus.get(i);
                        graphics.addCubeFace_Y_Plus(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
                    }
                    break;

                case 1: // wall x
                    Utils.calcShadowMatrixWallX(light, shadowMat, 0.0f);
                    size = m_list_cubes_wall_x_minus.size();
                    for(int i = 0; i < size; ++i) {
                        cube = m_list_cubes_wall_x_minus.get(i);
                        graphics.addCubeFace_X_Plus(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
                    }
                    break;

                case 2: // wall z
                    Utils.calcShadowMatrixWallZ(light, shadowMat, 0.0f);
                    size = m_list_cubes_wall_z_minus.size();
                    for(int i = 0; i < size; ++i) {
                        cube = m_list_cubes_wall_z_minus.get(i);
                        graphics.addCubeFace_Z_Plus(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
                    }
                    break;
            } // switch

            glClear(GL_STENCIL_BUFFER_BIT);
            glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
            glStencilFunc(GL_ALWAYS, 0xff, 0xff);

            glDisable(GL_BLEND);
            glEnable(GL_LIGHTING);
            glEnable(GL_DEPTH_TEST);
            glEnable(GL_TEXTURE_2D);

            // draw shadow receivers (floor and two walls)
            graphics.updateBuffers();
            graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);

            //----------------------------------------
            // cast shadow on a receiver
            Color shadow_color = new Color(0, 0, 0, (int)((m_fade_value / 2.0f) * 255));

            graphics.resetBufferIndices();

            // with level cubes
            size = m_list_cubes_level.size();
            for(int i = 0; i < size; ++i) {
                cube = m_list_cubes_level.get(i);
                graphics.addCubeSize(cube.tx + Game.cube_offset.x, cube.ty + Game.cube_offset.y, cube.tz + Game.cube_offset.z, HALF_CUBE_SIZE, shadow_color);
            }

            // moving cubes
            if (!movingCubes.isEmpty()) {
                MovingCube movingCube;
                size = movingCubes.size();
                for(int i = 0; i < size; ++i) {
                    movingCube = movingCubes.get(i);
                    graphics.addCubeSize(movingCube.pos.x + Game.cube_offset.x, movingCube.pos.y + Game.cube_offset.y, movingCube.pos.z + Game.cube_offset.z, HALF_CUBE_SIZE, shadow_color);
                }
            }

            // mover cubes
            if (!moverCubes.isEmpty()) {
                MoverCube moverCube;
                size = moverCubes.size();
                for(int i = 0; i < size; ++i) {
                    moverCube = moverCubes.get(i);
                    graphics.addCubeSize(moverCube.pos.x + Game.cube_offset.x, moverCube.pos.y + Game.cube_offset.y, moverCube.pos.z + Game.cube_offset.z, HALF_CUBE_SIZE, shadow_color);
                }
            }

            // dead cubes
            if (!deadCubes.isEmpty()) {
                DeadCube deadCube;
                size = deadCubes.size();
                for(int i = 0; i < size; ++i) {
                    deadCube = deadCubes.get(i);
                    graphics.addCubeSize(deadCube.pos.x + Game.cube_offset.x, deadCube.pos.y + Game.cube_offset.y, deadCube.pos.z + Game.cube_offset.z, HALF_CUBE_SIZE, shadow_color);
                }
            }

            // with locationPlayer cube
            if (mPlayerCube.location.x != 0) {
                graphics.addCubeSize(mPlayerCube.pos.x + Game.cube_offset.x, mPlayerCube.pos.y + Game.cube_offset.y, mPlayerCube.pos.z + Game.cube_offset.z, HALF_CUBE_SIZE, shadow_color);
            }

            // with locationKey cube
            if (mLocationKeyCube.x != 0 && !isPlayerAndKeyInSamePosition())
            {
                graphics.addCubeSize(Game.cubes[mLocationKeyCube.x][mLocationKeyCube.y][mLocationKeyCube.z].tx + Game.cube_offset.x,
                                     Game.cubes[mLocationKeyCube.x][mLocationKeyCube.y][mLocationKeyCube.z].ty + Game.cube_offset.y,
                                     Game.cubes[mLocationKeyCube.x][mLocationKeyCube.y][mLocationKeyCube.z].tz + Game.cube_offset.z, HALF_CUBE_SIZE * 0.95f, shadow_color);
            }

            glEnable(GL_BLEND);
            glDisable(GL_LIGHTING);
            glDisable(GL_TEXTURE_2D);
            glDisable(GL_DEPTH_TEST);

            glStencilOp(GL_KEEP, GL_KEEP, GL_ZERO);
            glStencilFunc(GL_EQUAL, 0xff, 0xff);

            glPushMatrix();
            glMultMatrixf(shadowMat, 0);
            graphics.updateBuffers();
            graphics.renderTriangles();
            glPopMatrix();
            //----------------------------------------
        } // for

        glPushMatrix();

        glDisable(GL_STENCIL_TEST);
        glEnable(GL_CULL_FACE);
        glDisable(GL_BLEND);
        glEnable(GL_LIGHTING);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);

        // draw rest of the floor and walls (not shadow receivers)
        graphics.resetBufferIndices();

        size = m_list_cubes_wall_y_minus.size();
        for (int i = 0; i < size; ++i) {
            cube = m_list_cubes_wall_y_minus.get(i);
            graphics.addCubeFace_X_Plus(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
            graphics.addCubeFace_X_Minus(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
            graphics.addCubeFace_Z_Plus(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
            graphics.addCubeFace_Z_Minus(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
            graphics.addCubeFace_Y_Minus(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
        }

        size = m_list_cubes_wall_x_minus.size();
        for (int i = 0; i < size; ++i) {
            cube = m_list_cubes_wall_x_minus.get(i);
            graphics.addCubeFace_X_Minus(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
            graphics.addCubeFace_Y_Plus(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
            graphics.addCubeFace_Z_Plus(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
            graphics.addCubeFace_Z_Minus(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
        }

        size = m_list_cubes_wall_z_minus.size();
        for (int i = 0; i < size; ++i) {
            cube = m_list_cubes_wall_z_minus.get(i);
            graphics.addCubeFace_X_Plus(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
            graphics.addCubeFace_X_Minus(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
            graphics.addCubeFace_Y_Plus(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
            graphics.addCubeFace_Z_Minus(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
        }

        // draw edges
        size = m_list_cubes_edges.size();
        for(int i = 0; i < size; ++i) {
            cube = m_list_cubes_edges.get(i);
            graphics.addCubeSize(cube.tx, cube.ty, cube.tz, HALF_CUBE_SIZE, cube.colorCurrent);
        }

        // level cubes
        size = m_list_cubes_level.size();
        for(int i = 0; i < size; ++i) {
            cube = m_list_cubes_level.get(i);
            graphics.addCubeSize(cube.tx, cube.ty, cube.tz, HALF_CUBE_SIZE, cube.colorCurrent);
        }

        MovingCube movingCube;
        size = movingCubes.size();
        for(int i = 0; i < size; ++i) {
            movingCube = movingCubes.get(i);
            movingCube.renderCube();
        }

        MoverCube moverCube;
        size = moverCubes.size();
        for(int i = 0; i < size; ++i) {
            moverCube = moverCubes.get(i);
            moverCube.renderCube();
        }

        DeadCube deadCube;
        size = deadCubes.size();
        for(int i = 0; i < size; ++i) {
            deadCube = deadCubes.get(i);
            deadCube.renderCube();
        }

        if (LevelStateEnum.Playing != mState) {
            size = m_list_cubes_face.size();
            for(int i = 0; i < size; ++i) {
                cube = m_list_cubes_face.get(i);
                graphics.addCubeSize(cube.tx, cube.ty, cube.tz, HALF_CUBE_SIZE, cube.colorCurrent);
            }

            size = m_list_cubes_base.size();
            for(int i = 0; i < size; ++i) {
                cube = m_list_cubes_base.get(i);
                graphics.addCubeSize(cube.tx, cube.ty, cube.tz, HALF_CUBE_SIZE, cube.colorCurrent);
            }
        }

        graphics.updateBuffers();
        graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);

        glPopMatrix();
    }

    private void drawTextsCompleted() {
        Graphics graphics = Game.graphics;

        CubeFont cubeFont;
        TexCoordsQuad coords = new TexCoordsQuad();
        TexturedQuad pFont;
        int size;
        graphics.bindStreamSources3d();
        graphics.resetBufferIndices();

        size = m_list_fonts.size();
        for(int i = 0; i < size; ++i) {
            cubeFont = m_list_fonts.get(i);
            pFont = cubeFont.getFont();

            coords.tx0 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);
            coords.tx1 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
            coords.tx2 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
            coords.tx3 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);

            graphics.addCubeFace_Z_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        if (mLevelMenu.cubeUp.isDone() && mLevelMenu.cubeUp.cubeLocation.x == 7) {
            cubeFont = mCubefontUp;
            pFont = cubeFont.getFont();

            coords.tx0 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);
            coords.tx1 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
            coords.tx2 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
            coords.tx3 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);

            graphics.addCubeFace_Z_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        if (mLevelMenu.cubeMid.isDone() && mLevelMenu.cubeMid.cubeLocation.x == 7) {
            cubeFont = mCubefontMid;
            pFont = cubeFont.getFont();

            coords.tx0 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);
            coords.tx1 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
            coords.tx2 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
            coords.tx3 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);

            graphics.addCubeFace_Z_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        if (mLevelMenu.cubeLow.isDone() && mLevelMenu.cubeLow.cubeLocation.x == 7) {
            cubeFont = mCubefontLow;
            pFont = cubeFont.getFont();

            coords.tx0 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);
            coords.tx1 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
            coords.tx2 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
            coords.tx3 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);

            graphics.addCubeFace_Z_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        graphics.updateBuffers();
        graphics.renderTriangles();
    }

    private void drawTextsPaused() {
        Graphics graphics = Game.graphics;

        CubeFont cubeFont;
        TexCoordsQuad coords = new TexCoordsQuad();
        TexturedQuad pFont;
        int size;
        graphics.bindStreamSources3d();
        graphics.resetBufferIndices();

        size = m_list_fonts.size();
        for(int i = 0; i < size; ++i) {
            cubeFont = m_list_fonts.get(i);
            pFont = cubeFont.getFont();

            coords.tx0 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
            coords.tx1 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
            coords.tx2 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);
            coords.tx3 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);

            graphics.addCubeFace_X_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        if (mLevelMenu.cubeUp.isDone() && mLevelMenu.cubeUp.cubeLocation.z == 1) {
            cubeFont = mCubefontUp;
            pFont = cubeFont.getFont();

            coords.tx0 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
            coords.tx1 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
            coords.tx2 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);
            coords.tx3 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);

            graphics.addCubeFace_X_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        if (mLevelMenu.cubeMid.isDone() && mLevelMenu.cubeMid.cubeLocation.z == 1) {
            cubeFont = mCubefontMid;
            pFont = cubeFont.getFont();

            coords.tx0 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
            coords.tx1 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
            coords.tx2 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);
            coords.tx3 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);

            graphics.addCubeFace_X_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        if (mLevelMenu.cubeLow.isDone() && mLevelMenu.cubeLow.cubeLocation.z == 1) {
            cubeFont = mCubefontLow;
            pFont = cubeFont.getFont();

            coords.tx0 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
            coords.tx1 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
            coords.tx2 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);
            coords.tx3 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);

            graphics.addCubeFace_X_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }
        graphics.updateBuffers();
        graphics.renderTriangles();
    }

    private void draw() {
        Graphics graphics = Game.graphics;
        Color color = new Color(255, 255, 255, (int)(m_fade_value * 255));
        graphics.bindStreamSources3d();

        if (m_fade_value < 1.0f) {
            glEnable(GL_BLEND);
            glDisable(GL_DEPTH_TEST);
        }

        glEnable(GL_LIGHTING);

        if (mDrawMenuCubes || mPlayerCube.location.x != 0) {
            Game.graphics.texturePlayer.bind();

            graphics.resetBufferIndices();

            if (0 != mPlayerCube.location.x) {
                graphics.addCubeSize(mPlayerCube.pos.x, mPlayerCube.pos.y, mPlayerCube.pos.z, HALF_CUBE_SIZE, color);
            }

            if (mDrawMenuCubes) {
                mLevelMenu.draw();
            }
            graphics.updateBuffers();
            graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);
        }

        if (0 != mLocationKeyCube.x && !isPlayerAndKeyInSamePosition()) {
            Game.graphics.textureKey.bind();

            Cube pCube = Game.cubes[mLocationKeyCube.x][mLocationKeyCube.y][mLocationKeyCube.z];

            graphics.resetBufferIndices();
            graphics.addCubeSize(pCube.tx, pCube.ty, pCube.tz, HALF_CUBE_SIZE * 0.95f, color);
            graphics.updateBuffers();
            graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);
        }

        if (m_fade_value < 1.0f) {
            glDisable(GL_BLEND);
            glEnable(GL_DEPTH_TEST);
        }

        if (!movingCubes.isEmpty() || !moverCubes.isEmpty() || !deadCubes.isEmpty()) {
            int size;
            MovingCube movingCube;
            MoverCube moverCube;
            DeadCube deadCube;

            graphics.resetBufferIndices();

            size = movingCubes.size();
            for(int i = 0; i < size; ++i) {
                movingCube = movingCubes.get(i);
                movingCube.renderSymbols();
            }

            size = moverCubes.size();
            for(int i = 0; i < size; ++i) {
                moverCube = moverCubes.get(i);
                moverCube.renderSymbols();
            }

            size = deadCubes.size();
            for(int i = 0; i < size; ++i) {
                deadCube = deadCubes.get(i);
                deadCube.renderSymbols();
            }

            glDisable(GL_LIGHTING);
            glEnable(GL_BLEND);
            graphics.bindStreamSources3d();
            Game.graphics.textureSymbols.bind();
            graphics.updateBuffers();
            graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);
        }

        if (m_menu_cube_hilite != null) {
            Game.graphics.textureSymbols.bind();
            color.init( new Color(color.r, color.g, color.b, (int)(m_hilite_alpha * 255)));
            glDisable(GL_DEPTH_TEST);
            glDisable(GL_CULL_FACE);
            glDisable(GL_LIGHTING);

            TexCoordsQuad coords = new TexCoordsQuad();

            TexturedQuad p = m_font_hilite.getFont();

            coords.tx0 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
            coords.tx1 = new Vector2(p.tx_up_left.x,  p.tx_up_left.y);
            coords.tx2 = new Vector2(p.tx_lo_left.x,  p.tx_lo_left.y);
            coords.tx3 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);

            graphics.resetBufferIndices();
            graphics.bindStreamSources3d();

            switch (mState) {
                case Paused: //Face_X_Minus:
                    graphics.addCubeFace_X_Minus(m_font_hilite.pos.x, m_font_hilite.pos.y, m_font_hilite.pos.z, coords, color);
                    break;

                case Completed: //Face_Z_Minus:
                    graphics.addCubeFace_Z_Minus(m_font_hilite.pos.x, m_font_hilite.pos.y, m_font_hilite.pos.z, coords, color);
                    break;

                default:
                    break;
            }
            glEnable(GL_BLEND);
            graphics.updateBuffers();
            graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);

            glEnable(GL_LIGHTING);
            glEnable(GL_DEPTH_TEST);
            glEnable(GL_CULL_FACE);
        }

        if (m_draw_texts) {
            glPushMatrix();
            glTranslatef(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);

            glEnable(GL_TEXTURE_2D);
            Game.graphics.textureFonts.bind();

            glEnable(GL_BLEND);
            glDisable(GL_LIGHTING);
            glDisableClientState(GL_NORMAL_ARRAY);

            if (mState == LevelStateEnum.AnimToPaused || mState == LevelStateEnum.AnimFromPaused || mState == LevelStateEnum.Paused) {
                drawTextsPaused();
            } else {
                drawTextsCompleted();
            }
            glPopMatrix();
        }
    }

    @Override
    public void render() {
        Graphics graphics = Game.graphics;
//        if (mState == LevelStateEnum.Completed) {
//            renderForPicking(PickRenderTypeEnum.RenderOnlyMovingCubes);
//            if (true) {
//                return;
//            }
//        }

        graphics.setProjection2D();
        graphics.setModelViewMatrix2D();
        graphics.bindStreamSources2d();

        glEnable(GL_LIGHT0);
        glEnable(GL_BLEND);
        glDisable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);
        glDepthMask(false); //GL_FALSE);

        Color color = new Color(255, 255, 255, (int)Game.dirtyAlpha);
        graphics.drawFullScreenTexture(graphics.textureDirty, color);

        glDepthMask(true); //GL_TRUE);

        graphics.setProjection3D();
        graphics.setModelViewMatrix3D(mCameraCurrent);
        graphics.bindStreamSources3d();

        // draw axes global
//        glDisable(GL_TEXTURE_2D);
//        graphics.drawAxes();
//        glEnable(GL_TEXTURE_2D);

        graphics.setLightPosition(mPosLightCurrent);
        glEnable(GL_LIGHTING);

        glPushMatrix();
        glRotatef(mUserRotation.current.x, 1.0f, 0.0f, 0.0f);
        glRotatef(mUserRotation.current.y, 0.0f, 1.0f, 0.0f);

        glPushMatrix();
        glRotatef(mCubeRotation.degree, mCubeRotation.axis.x, mCubeRotation.axis.y, mCubeRotation.axis.z);

        drawTheCube();

        // draw axes cube
//        glDisable(GL_TEXTURE_2D);
//        glDisable(GL_LIGHTING);
//        graphics.drawAxes();
//        glEnable(GL_LIGHTING);
//        glEnable(GL_TEXTURE_2D);

        draw();

        glPopMatrix();

        glPopMatrix();

        mHud.render();
    }

    private void renderForPicking(PickRenderTypeEnum type) {
        Graphics graphics = Game.graphics;
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glDisable(GL_LIGHTING);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);

        glDisable(GL_TEXTURE_2D);

        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);

        graphics.setProjection3D();
        graphics.setModelViewMatrix3D(mCameraCurrent);

        glPushMatrix();

        switch (type) {
            case RenderOnlyHUD:
                mHud.renderForPicking();
                break;

            case RenderOnlyMovingCubes:
                graphics.resetBufferIndices();
                graphics.bindStreamSources3d();

                glRotatef(mCubeRotation.degree, mCubeRotation.axis.x, mCubeRotation.axis.y, mCubeRotation.axis.z);

                mLevelMenu.drawForPicking();

                graphics.updateBuffers();
                graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);
                break;

            default:
                break;
        } // switch

        glPopMatrix();
    }

    @Override
    public void renderToFBO() {
        Graphics graphics = Game.graphics;
        graphics.setProjection2D();
        graphics.setModelViewMatrix2D();

        glEnable(GL_BLEND);
        glDisable(GL_LIGHTING);
        glDepthMask(false);
        glEnable(GL_TEXTURE_2D);

        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);

        Color color_dirty = new Color(255, 255, 255, (int)Game.dirtyAlpha);
        graphics.drawFullScreenTexture(graphics.textureDirty, color_dirty);

        glDepthMask(true);

        graphics.setProjection3D();
        graphics.setModelViewMatrix3D(mCameraCurrent);

        glEnable(GL_LIGHTING);

        graphics.setLightPosition(mPosLightCurrent);

        glRotatef(mCubeRotation.degree, mCubeRotation.axis.x, mCubeRotation.axis.y, mCubeRotation.axis.z);
        drawTheCube();

        glEnable(GL_BLEND);

        graphics.bindStreamSources3d();
        Game.graphics.textureKey.bind();

        if (0 != mLocationKeyCube.x) {
            glEnable(GL_LIGHTING);
            Game.graphics.textureKey.bind();

            Cube cube = Game.cubes[mLocationKeyCube.x][mLocationKeyCube.y][mLocationKeyCube.z];

            graphics.resetBufferIndices();
            graphics.addCubeSize(cube.tx, cube.ty, cube.tz, HALF_CUBE_SIZE * 0.95f, Color.WHITE);
            graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);

            glDisable(GL_LIGHTING);
        }

        glDisable(GL_BLEND);

        // draw locationKey cube
        graphics.resetBufferIndices();

        glEnable(GL_LIGHTING);
        Game.graphics.textureKey.bind();

        CubeLocation pos = mLocationKeyCube;
        Cube keyCube = Game.cubes[pos.x][pos.y][pos.z];

        graphics.addCubeSize(keyCube.tx, keyCube.ty, keyCube.tz, HALF_CUBE_SIZE * 0.95f, Color.WHITE);
        graphics.updateBuffers();
        graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);

        // draw locationPlayer cube
        graphics.resetBufferIndices();

        glEnable(GL_LIGHTING);
        Game.graphics.texturePlayer.bind();
        graphics.addCubeSize(mPlayerCube.pos.x, mPlayerCube.pos.y, mPlayerCube.pos.z, HALF_CUBE_SIZE, Color.WHITE);
        graphics.updateBuffers();
        graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);

        glDisable(GL_LIGHTING);

        if (!movingCubes.isEmpty() || !moverCubes.isEmpty() || !deadCubes.isEmpty()) {
            int size;
            MovingCube movingCube;
            MoverCube moverCube;
            DeadCube deadCube;

            graphics.resetBufferIndices();

            size = movingCubes.size();
            for(int i = 0; i < size; ++i) {
                movingCube = movingCubes.get(i);
                movingCube.renderSymbols();
            }

            size = moverCubes.size();
            for(int i = 0; i < size; ++i) {
                moverCube = moverCubes.get(i);
                moverCube.renderSymbols();
            }

            size = deadCubes.size();
            for(int i = 0; i < size; ++i) {
                deadCube = deadCubes.get(i);
                deadCube.renderSymbols();
            }

            glDisable(GL_LIGHTING);
            glEnable(GL_BLEND);

            graphics.bindStreamSources3d();
            Game.graphics.textureSymbols.bind();
            graphics.updateBuffers();
            graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);
        }
    }

    private void eventBuildLevel() {
        switch (mDifficulty) {
            case Easy: LevelBuilderEasy.build(mLevelNumber); break;
            case Normal: LevelBuilderNormal.build(mLevelNumber); break;
            case Hard: LevelBuilderHard.build(mLevelNumber); break;
        }

        mLocationKeyCube = LevelBuilder.locationKey;

        mPlayerCube.setLocation(LevelBuilder.locationPlayer);
        mPlayerCube.setKeyCubePos(mLocationKeyCube);

        if (Utils.rand.nextBoolean()) {
            appearDisappear.level.setLevelAndDirection(0, 1);
        } else {
            appearDisappear.level.setLevelAndDirection(MAX_CUBE_COUNT - 1, -1);
        }

        String str;

        mHud.set1stHint();

        // level
        str = Game.getLevelTypeAndNumberString(mDifficulty, mLevelNumber);
        mHud.setTextLevel(str);

        // stars
        mHud.setTextStars(Game.progress.getStarCount());

        // moves
        mHud.setTextMoves(0);

        // motto
        str = Creator.getLevelMotto(mDifficulty, mLevelNumber);
        mHud.setTextMotto(str);

        mHud.setupAppear();
    }

    private void eventShowHint() {
        if (0 == mMovesCounter) {
            mHud.showHintToBegin( solution.getFirstStep() );
        } else {
            m_show_hint_2nd = true;
            m_hint_index = 0;
            m_hint_timeout = 0.0f;
            
            for (int i = 0; i < MAX_HINT_CUBES; ++i) {
                mHintCubes[i] = null;
            }

            Cube cube;
            int size = hintCubes.size();
            for(int i = 0; i < size; ++i) {
                cube = hintCubes.get(i);
                mHintCubes[i] = cube;
            }
        }
    }

    private void eventSolver() {
        if (LevelNextActionEnum.ShowSceneSolvers == mNextAction || LevelStateEnum.PrepareSolving == mState) {
            return;
        }

        boolean solved = false;
        switch (mDifficulty) {
            case Easy: solved = Game.progress.getSolvedEasy(mLevelNumber); break;
            case Normal: solved = Game.progress.getSolvedNormal(mLevelNumber); break;
            case Hard: solved = Game.progress.getSolvedHard(mLevelNumber); break;
        }

        if (solved) {
            reset();
            mState = LevelStateEnum.PrepareSolving;
            mTimeout = 2.0f;
        } else {
            mNextAction = LevelNextActionEnum.ShowSceneSolvers;
        }

        mHud.setupDisappear();
    }

    private void eventQuit() {
        Game.anim_init_data.list_cubes_base.clear();
        int size;
        Cube cube;

        size = m_list_cubes_base.size();
        for(int i = 0; i < size; ++i) {
            cube = m_list_cubes_base.get(i);
            Game.anim_init_data.list_cubes_base.add(cube);
        }

        size = m_list_cubes_edges.size();
        for(int i = 0; i < size; ++i) {
            cube = m_list_cubes_edges.get(i);
            Game.anim_init_data.list_cubes_base.add(cube);
        }

        Game.anim_init_data.cube_rotation_degree = mCubeRotation.degree;

        if (LevelStateEnum.Paused == mState) {
            Game.anim_init_data.type = AnimTypeEnum.AnimToMenuFromPaused;
        } else {
            Game.anim_init_data.type = AnimTypeEnum.AnimToMenuFromCompleted;
        }

        Game.showScene(Scene_Anim);
    }

    private void eventLevelPause() {
        mHud.setupDisappear();
        setAnimToPaused();
    }

    public void eventLevelComplete() {
        Game.audio.playSound(SOUND_LEVEL_COMPLETED);

        Game.renderToFBO(this);
        mHud.setupDisappear();

        StatInitData sid = Game.statInitData;
        getRatings();

        switch (mDifficulty) {
            case Easy:
                Game.progress.setStarsEasy(mLevelNumber, sid.stars);
                Game.progress.setMovesEasy(mLevelNumber, mMovesCounter);

                if (mLevelNumber < 60) {
                    if (LEVEL_LOCKED == Game.progress.getStarsEasy(mLevelNumber + 1))
                    Game.progress.setStarsEasy(mLevelNumber + 1, LEVEL_UNLOCKED);
                }

                //reportAchievementEasy();
                break;

            case Normal:
                Game.progress.setStarsNormal(mLevelNumber, sid.stars);
                Game.progress.setMovesNormal(mLevelNumber, mMovesCounter);

                if (mLevelNumber < 60) {
                    if (LEVEL_LOCKED == Game.progress.getStarsNormal(mLevelNumber + 1)) {
                        Game.progress.setStarsNormal(mLevelNumber + 1, LEVEL_UNLOCKED);
                    }
                }

                //reportAchievementNormal();
                break;

            case Hard:
                Game.progress.setStarsHard(mLevelNumber, sid.stars);
                Game.progress.setMovesHard(mLevelNumber, mMovesCounter);

                if (mLevelNumber < 60) {
                    if (LEVEL_LOCKED == Game.progress.getStarsHard(mLevelNumber + 1)) {
                        Game.progress.setStarsHard(mLevelNumber + 1, LEVEL_UNLOCKED);
                    }
                }

                //reportAchievementHard();
                break;
        } // switch

        Game.progress.save();
        //Game.submitScore(cCubetraz::GetStarCount());
        mState = LevelStateEnum.SetupAnimToCompleted;
        Game.showScene(Scene_Stat);
    }

    public void reset() {
        int size;
        mMovesCounter = 0;
        mUndoList.clear();

        mHud.set1stHint();
        mHud.setTextMoves(mMovesCounter);
        mHud.setTextUndo( mUndoList.size() );

        mPlayerCube.setLocation(LevelBuilder.locationPlayer);

        Game.resetCubesColors();

        if (!movingCubes.isEmpty()) {
            MovingCube movingCube;
            size = movingCubes.size();
            for (int i = 0; i < size; ++i) {
                movingCube = movingCubes.get(i);
                movingCube.reposition();
            }
        }

        if (!moverCubes.isEmpty()) {
            MoverCube moverCube;
            size = movingCubes.size();
            for (int i = 0; i < size; ++i) {
                moverCube = moverCubes.get(i);
                moverCube.reposition();
            }
        }

        if (!deadCubes.isEmpty()) {
            DeadCube deadCube;
            size = deadCubes.size();
            for (int i = 0; i < size; ++i) {
                deadCube = deadCubes.get(i);
                deadCube.reposition();
            }
        }
    }

    private void eventUndo() {
        mTimeoutUndo = UNDO_TIMEOUT;

        if (mMovesCounter > 0) {
            --mMovesCounter;
            mHud.setTextMoves(mMovesCounter);

            UndoData ud = mUndoList.get(mUndoList.size() - 1);
            mUndoList.remove(ud);

            mPlayerCube.setLocation(ud.playerLocation);

            mHud.setTextUndo(mUndoList.size());

            if (null != ud.movingCube) {
                ud.movingCube.init(ud.movingCubeLocation, ud.movingCubeMoveDir);
            }

            if (0 == mMovesCounter) {
                mHud.set1stHint();
            }

            mState = LevelStateEnum.Undo;
        }
    }

    @Override
    public void onFingerDown(float x, float y, int fingerCount) {
        mIsFingerDown = true;

        mPosDown.x = x;
        mPosDown.y = y;

        if (fingerCount == 1) {
            Color downColor;

            switch (mState) {
                case Playing:
                    if (HUDStateEnum.DoneHUD == mHud.getState() ) {
                        renderForPicking(PickRenderTypeEnum.RenderOnlyHUD);

                        downColor = Game.graphics.getColorFromScreen(mPosDown);
                        switch (downColor.b) {
                            case 200: mHud.setHilitePause(true); break;
                            case 150: mHud.setHiliteUndo(true); break;
                            case 100: mHud.setHiliteHint(true); break;
                            case 50: mHud.setHiliteSolver(true); break;
                            default:
                                break;
                        }
                    }
                    break;

                case Paused:
                case Completed: {
                    renderForPicking(PickRenderTypeEnum.RenderOnlyMovingCubes);

                    downColor = Game.graphics.getColorFromScreen(mPosDown);
                    MenuCube menuCube = mLevelMenu.getMenuCubeFromColor(downColor.b);
                    if (menuCube != null) {
                        m_hilite_alpha = 0.0f;
                        m_menu_cube_hilite = menuCube;
                        CubeLocation cp = new CubeLocation();
                        cp.init(m_menu_cube_hilite.cubeLocation);
                        m_font_hilite.init(SymbolHilite, cp);
                    }
                }
                break;

                default:
                    break;
            } // switch
        }
    }

    @Override
    public void onFingerUp(float x, float y, int finger_count) {
/*
    int ln = mLevelNumber + 1;
    DifficultyEnum diff = mDifficulty;

    if (ln > 60)
    {
        ln = 1;
        if (Easy == mDifficulty)
            diff = Normal;

        if (Normal == mDifficulty)
            diff = Hard;

        if (Hard == mDifficulty)
            diff = Easy;
    }

    engine.levelInitData.difficulty = diff;
    engine.levelInitData.levelNumber = ln;
    engine.levelInitData.initAction = FullInit;

    engine.ShowScene(Scene_Level);
    return;
*/
        mPosUp.x = x;
        mPosUp.y = y;

        switch (mState) {
            case Playing:
                fingerUpPlaying(x, y, finger_count);
                mHud.setHilitePause(false);
                mHud.setHiliteUndo(false);
                mHud.setHiliteHint(false);
                mHud.setHiliteSolver(false);
                break;

            case Completed:
                m_menu_cube_hilite = null;
                fingerUpCompleted(x, y);
                break;

            case Paused:
                m_menu_cube_hilite = null;
                fingerUpPaused(x, y);
                break;

            case Tutorial:
                mHud.hideTutor();
                break;

            default:
                break;
        }
    }

    @Override
    public void onFingerMove(float prev_x, float prev_y, float cur_x, float cur_y, int finger_count) {
        mPosMove.x = cur_x;
        mPosMove.y = cur_y;

        float dist = Utils.getDistance2D(mPosDown, mPosMove);
        //printf("\nOnFingerMove: %.2f", dist);

        if (dist > Game.minSwipeLength) {
            mIsSwipe = true;
        }

        if (2 == finger_count) {
            if (mState == LevelStateEnum.Playing) {
                Vector2 dir = new Vector2();
                dir.x = (mPosDown.x - cur_x) / Game.graphics.deviceScale;
                dir.y = (mPosDown.y - cur_y) / Game.graphics.deviceScale;

                m_alter_view = true;

                mUserRotation.current.x = -dir.y * 0.3f;
                mUserRotation.current.y = -dir.x * 0.3f;
                mUserRotation.clamp();

                mIsSwipe = false;
            }
        }
    }

    private void fingerUpPaused(float x, float y) {
        if (mIsSwipe) {
            mIsSwipe = false;
            renderForPicking(PickRenderTypeEnum.RenderOnlyMovingCubes);
            Color downColor = Game.graphics.getColorFromScreen(mPosDown);
            SwipeInfo swipeInfo = Game.getSwipeDirAndLength(mPosDown, mPosUp);

            if (swipeInfo.length > 30.0f * Game.graphics.scaleFactor) {
                MenuCube menuCube = mLevelMenu.getMenuCubeFromColor(downColor.b);
                if (menuCube != null) {
                    switch (swipeInfo.swipeDir) {
                        case SwipeRight: menuCube.moveOnAxis(AxisMovement_Z_Plus); break;
                        case SwipeLeft:  menuCube.moveOnAxis(AxisMovement_Z_Minus); break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    private void fingerUpCompleted(float x, float y) {
        if (mIsSwipe) {
            mIsSwipe = false;

            renderForPicking(PickRenderTypeEnum.RenderOnlyMovingCubes);
            Color downColor = Game.graphics.getColorFromScreen(mPosDown);
            SwipeInfo swipeInfo = Game.getSwipeDirAndLength(mPosDown, mPosUp);

            if (swipeInfo.length > 30.0f * Game.graphics.scaleFactor) {
                MenuCube menuCube = mLevelMenu.getMenuCubeFromColor(downColor.b);
                if (menuCube != null) {
                    switch (swipeInfo.swipeDir) {
                        case SwipeLeft: menuCube.moveOnAxis(AxisMovement_X_Plus); break;
                        case SwipeRight: menuCube.moveOnAxis(AxisMovement_X_Minus); break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    private void fingerUpPlaying(float x, float y, int fingerCount) {
        if (mHud.getShowHint()) {
            return;
        }

        if (!mPlayerCube.isDone()) {
            mIsSwipe = false;
            return;
        }

        if (m_alter_view) {
            if ( Math.abs(mUserRotation.current.x) > EPSILON || Math.abs(mUserRotation.current.y) > EPSILON ) {
                m_reposition_view = true;
                m_alter_view = false;
                m_t = 0.0f;
                mUserRotation.from = mUserRotation.current;
            }
            return;
        }

        if (!mIsSwipe) {
            if (mState == LevelStateEnum.Playing) {
                renderForPicking(PickRenderTypeEnum.RenderOnlyHUD);
                Vector2 pos = new Vector2(x,y);
                Color color = Game.graphics.getColorFromScreen(pos);
                switch (color.b) {
                    case 200: eventLevelPause(); break;
                    case 150: eventUndo(); break;
                    case 100: eventShowHint(); break;
                    case  50: eventSolver(); break;
                    default:
                        break;
                }
            }
        } else { // swipe        
            mIsSwipe = false;

            if (mHud.isAnythingHilited()) {
                return;
            }

            float fingerdown_x = mPosDown.x;
            float fingerdown_y = -mPosDown.y;

            float fingerup_x = x;
            float fingerup_y = -y;

            Vector2 dir = new Vector2();
            dir.x = fingerup_x - fingerdown_x;
            dir.y = fingerup_y - fingerdown_y;

            float len = dir.len(); // swipe length
            if (len < Game.minSwipeLength){
                return;
            }

            dir.nor();

            float degree = (float)Math.toDegrees( Math.atan2(dir.y, dir.x) );
            boolean success = false;
            float center;
            final float space = 30.0f;

            center = 90.0f; // Y plus
            if (degree > (center - space) && degree < (center + space)) {
                success = mPlayerCube.moveOnAxis(AxisMovement_Y_Plus);
            }

            center = -90.0f; // Y minus
            if (degree > (center - space) && degree < (center + space)) {
                success = mPlayerCube.moveOnAxis(AxisMovement_Y_Minus);
            }

            center = 30.0f; // Z plus
            if (degree > (center - space) && degree < (center + space)) {
                success = mPlayerCube.moveOnAxis(AxisMovement_Z_Minus);
            }

            center = -150.0f; // Z minus
            if (degree > (center - space) && degree < (center + space)) {
                success = mPlayerCube.moveOnAxis(AxisMovement_Z_Plus);
            }

            center = -30.0f; // X minus
            if (degree > (center - space) && degree < (center + space)) {
                success = mPlayerCube.moveOnAxis(AxisMovement_X_Plus);
            }

            center = 150.0f; // X plus
            if (degree > (center - space) && degree < (center + space)) {
                success = mPlayerCube.moveOnAxis(AxisMovement_X_Minus);
            }

            if (success) {
                addMove();
            }
        }
    }

    private void addMove() {
        ++mMovesCounter;
        mHud.setTextMoves(mMovesCounter);

        if (mMovesCounter == 1) {
            mHud.set2ndHint();
        }

        UndoData undoData = new UndoData(mPlayerCube.getLocation());

        if (mPlayerCube.movingCube != null) {
            undoData.movingCube = mPlayerCube.movingCube;
            undoData.movingCube.init(mPlayerCube.movingCube);

            undoData.movingCubeLocation.init(mPlayerCube.movingCube.getCubePos());
            undoData.movingCubeMoveDir = mPlayerCube.movingCube.getMovement();
        }

        if (mPlayerCube.moverCube != null) {
            CubeLocation cp = mPlayerCube.destination;
            CubeLocation cp_new = new CubeLocation();
            cp_new.init(cp);

            mPlayerCube.calcMovement(cp_new, mPlayerCube.moverCube.getMoveDir(), false);

            if (cp.x != cp_new.x || cp.y != cp_new.y || cp.z != cp_new.z) {
                undoData.moverCube = mPlayerCube.moverCube;
            }
        }

        mUndoList.add(undoData);

        mHud.setTextUndo(mUndoList.size());

        mStateToRestore = mState;
        mState = LevelStateEnum.MovingPlayer;
        mTimeout = 0.0f; // move immediately
    }

}

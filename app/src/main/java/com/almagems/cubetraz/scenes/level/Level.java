package com.almagems.cubetraz.scenes.level;

import com.almagems.cubetraz.Audio;
import com.almagems.cubetraz.GameOptions;
import com.almagems.cubetraz.GameProgress;
import com.almagems.cubetraz.cubes.CubeLocation;
import com.almagems.cubetraz.graphics.Graphics;
import com.almagems.cubetraz.scenes.Creator;
import com.almagems.cubetraz.graphics.Camera;
import com.almagems.cubetraz.graphics.Color;
import com.almagems.cubetraz.cubes.Cube;
import com.almagems.cubetraz.cubes.CubeFont;
import com.almagems.cubetraz.scenes.menu.Menu;
import com.almagems.cubetraz.utils.CubeRotation;
import com.almagems.cubetraz.cubes.DeadCube;
import com.almagems.cubetraz.utils.EaseOutDivideInterpolation;
import com.almagems.cubetraz.Game;
import com.almagems.cubetraz.cubes.MenuCube;
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
import static com.almagems.cubetraz.Audio.*;

import static com.almagems.cubetraz.Game.*;

public final class Level extends Scene {

    enum State {
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

    State mState;
    private State mStateToRestore;

    private SubAppearStateEnum mAppearState;

    private DeadAnim mDeadAnim = new DeadAnim();
    private HUD mHud = new HUD(mDeadAnim);

    AppearDisappear appearDisappear = new AppearDisappear();

    private UserRotation mUserRotation = new UserRotation();

    private MenuCube mMenuCubeHilite;
    private CubeFont mFontHilite = new CubeFont();

    private float mHiliteAlpha;
    private float mHiliteTimeout;

    public ArrayList<Cube> cubesLevel = new ArrayList<>();
    public ArrayList<Cube> cubesWallYminus = new ArrayList<>();
    public ArrayList<Cube> cubesWallXminus = new ArrayList<>();
    public ArrayList<Cube> cubesWallZminus = new ArrayList<>();
    public ArrayList<Cube> cubesEdge = new ArrayList<>();
    public ArrayList<Cube> cubesBase = new ArrayList<>();
    public ArrayList<Cube> cubesFace = new ArrayList<>();
    public ArrayList<Cube> cubesHint = new ArrayList<>();

    private Cube[] mHintCubes = new Cube[MAX_HINT_CUBES];
    private boolean mShowHint2nd;
    private int mHintIndex;
    private float mHintTimeout;

    public final Camera mCameraLevel = new Camera();
    private final Camera mCameraLevelCompleted = new Camera();
    private final Camera mCameraLevelPaused = new Camera();

    private float mTargetRotationDegree;

    private final EaseOutDivideInterpolation mInterpolator = new EaseOutDivideInterpolation();

    private boolean mAlterView;
    private boolean mRepositionView;
    private boolean mDrawTexts;

    private DifficultyEnum mDifficulty;
    private int mLevelNumber;

    Solution solution = new Solution();

    private int mMovesCounter;
    private float mTimeout;

    private CubeLocation mLocationKeyCube = new CubeLocation();

    public final ArrayList<CubeFont> fonts = new ArrayList<>();

// spec cubes
    private final ArrayList<MovingCube> movingCubes = new ArrayList<>();
    private final ArrayList<MoverCube> moverCubes = new ArrayList<>();
    private final ArrayList<DeadCube> deadCubes = new ArrayList<>();

    private float mFadeValue;

    private final CubeRotation mCubeRotation = new CubeRotation();

    private MovingCube mMovingCube;
    private MoverCube mMoverCube;
    private DeadCube mDeadCube;

    public LevelMenu mLevelMenu = new LevelMenu();

    private boolean mDrawMenuCubes;

    private float mElapsed;

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

        mRepositionView = false;
        mShowHint2nd = false;
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
        //Game.levelInitData.difficulty = DifficultyEnum.Easy;
        //Game.levelInitData.levelNumber = 49;

        mNextAction = LevelNextActionEnum.NoNextAction;
        
        switch (levelInitData.initAction) {
            case FullInit: {
                mMenuCubeHilite = null;
                mShowHint2nd = false;

                mHiliteTimeout = 0.0f;

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

                cubesLevel.clear();
                cubesWallYminus.clear();
                cubesWallXminus.clear();
                cubesWallZminus.clear();
                cubesEdge.clear();
                cubesBase.clear();
                cubesFace.clear();
                cubesHint.clear();

                mHud.init();

                setupCubesForLevel();
                setupAppear();
            }
            break;

            case JustContinue: // take no action ?
                if (State.SetupAnimToCompleted == mState) {
                
                } else {
                    mHud.setupAppear();
                }
                break;

            case ShowSolution:
                reset();
                mState = State.PrepareSolving;
                mTimeout = 3.5f;
                break;
        }


        Graphics.setProjection3D();
        Graphics.setModelViewMatrix3D(mCameraCurrent);

        Graphics.setLightPosition(mPosLightCurrent);
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
                    Audio.playMusic(MUSIC_BREEZE);
                }

                if (mLevelNumber >= 16 && mLevelNumber <= 30) {
                    Audio.playMusic(MUSIC_DRONES);
                }

                if (mLevelNumber >= 31 && mLevelNumber <= 45) {
                    Audio.playMusic(MUSIC_WAVES);
                }

                if (mLevelNumber >= 46 && mLevelNumber <= 60) {
                    Audio.playMusic(MUSIC_BREEZE);
                }
                break;

            case Normal:
                if (mLevelNumber >= 1 && mLevelNumber <= 15) {
                    Audio.playMusic(MUSIC_DRONES);
                }

                if (mLevelNumber >= 16 && mLevelNumber <= 30) {
                    Audio.playMusic(MUSIC_WAVES);
                }

                if (mLevelNumber >= 31 && mLevelNumber <= 45) {
                    Audio.playMusic(MUSIC_BREEZE);
                }

                if (mLevelNumber >= 46 && mLevelNumber <= 60) {
                    Audio.playMusic(MUSIC_DRONES);
                }
                break;

            case Hard:
                if (mLevelNumber >= 1 && mLevelNumber <= 15) {
                    Audio.playMusic(MUSIC_WAVES);
                }

                if (mLevelNumber >= 16 && mLevelNumber <= 30) {
                    Audio.playMusic(MUSIC_BREEZE);
                }

                if (mLevelNumber >= 31 && mLevelNumber <= 45) {
                    Audio.playMusic(MUSIC_DRONES);
                }                    

                if (mLevelNumber >= 46 && mLevelNumber <= 60) {
                    Audio.playMusic(MUSIC_WAVES);
                }
                break;
        }
    }

    private void setupAppear() {
        mState = State.Appear;
        mAppearState = SubAppearStateEnum.SubAppearWait;
        mDrawMenuCubes = false;
        mDrawTexts = false;
        mTimeout = 0.0f;
        mAlterView = false;

        if (Utils.rand.nextBoolean()) {
            appearDisappear.level.setLevelAndDirection(0, 1);
        } else {
            appearDisappear.level.setLevelAndDirection(MAX_CUBE_COUNT - 1, -1);
        }
        mLocationKeyCube.reset();
    }

    private void setupCubesForLevel() {
        cubesWallYminus.clear();
        cubesWallXminus.clear();
        cubesWallZminus.clear();
        cubesEdge.clear();

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

                cubesWallYminus.add(Game.cubes[x][1][z]);
            }
        }

        // walls
        for (int y = 2; y < 7; ++y) {
            for (int x = 2; x < MAX_CUBE_COUNT; ++x) {
                Game.cubes[x][y][1].type = CubeTypeEnum.CubeIsVisibleAndObstacle;
                Game.cubes[x][y][1].setColor( Game.baseColor );

                if (y > 0) {
                    cubesWallZminus.add(Game.cubes[x][y][1]);
                }
            }

            for (int z = 2; z < MAX_CUBE_COUNT; ++z) {
                Game.cubes[1][y][z].type = CubeTypeEnum.CubeIsVisibleAndObstacle;
                Game.cubes[1][y][z].setColor( Game.baseColor );

                if (y > 0) {
                    cubesWallXminus.add(Game.cubes[1][y][z]);
                }
            }
        }

        // edges
        for (int i = 1; i < MAX_CUBE_COUNT; ++i) {
            cubesEdge.add(Game.cubes[i][1][1]);
            Game.cubes[i][1][1].setColor(Game.baseColor);
        }

        for (int i = 1; i < MAX_CUBE_COUNT; ++i) {
            cubesEdge.add(Game.cubes[1][1][i]);
            Game.cubes[1][1][i].setColor(Game.baseColor);
        }

        for (int i = 1; i < 7; ++i) {
            cubesEdge.add(Game.cubes[1][i][1]);
            Game.cubes[1][i][1].setColor(Game.baseColor);
        }
    }

    private void setupDeadCube(DeadCube deadCube) {
        mState = State.DeadAnim;
        mTimeout = 1.5f;

        mDeadAnim.init();

        mDeadCube = deadCube;
        mDeadCube.hiLite();
    }

    private void setupMoveCube(MovingCube movingCube) {
        mStateToRestore = mState;
        mState = State.MovingCube;
        mTimeout = 0.1f;

        mMovingCube = movingCube;
        mMovingCube.move();
    }

    private void setupMoverCube(MoverCube moverCube) {
        int movement = moverCube.getMoveDir();
        mStateToRestore = mState;
        mState = State.MovingPlayer;
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
        mHud.setTextSolver(GameOptions.getSolverCount());
    }
    
    private void setAnimToCompleted() {
        mState = State.AnimToCompleted;

        mDrawMenuCubes = true;
        mDrawTexts = true;

        cubesBase.clear();
        cubesFace.clear();

        appearDisappear.face.clear();
        appearDisappear.base.clear();

        mLevelMenu.setFontColor(20, 0, 0, 20);

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

        mElapsed = 0.0f;

        if (DifficultyEnum.Hard == mDifficulty && 60 == mLevelNumber) {
            m_completed_face_next_action = CompletedFaceNextActionEnum.Finish;
        } else {
            m_completed_face_next_action = CompletedFaceNextActionEnum.Next;
        }

        Creator.createTextsLevelCompletedFace(this, m_completed_face_next_action);

        CubeFont cubeFont;
        int size = fonts.size();
        for(int i = 0; i < size; ++i) {
            cubeFont = fonts.get(i);
            cubeFont.setColor(Game.textColorOnCubeFace);
        }

        mTargetRotationDegree = mCubeRotation.degree - 90.0f - 45.0f;

        mInterpolator.setup(mCubeRotation.degree, mTargetRotationDegree, 5.0f);

        mLevelMenu.setupForAnimCompleted();
    }

    private void updateAnimToCompleted() {
        mLevelMenu.update();

        Cube cube;

        cube = appearDisappear.base.getCubeFromAppearList();
        if (cube != null) {
            cubesBase.add(cube);
        }

        cube = appearDisappear.face.getCubeFromAppearList();
        if (cube != null) {
            cubesFace.add(cube);
        }

        mElapsed += 0.04f;
        if (mElapsed > 1f) {
            mElapsed = 1f;
        }

        Utils.lerpCamera(mCameraLevel, mCameraLevelCompleted, mElapsed, mCameraCurrent);

        mInterpolator.interpolate();
        mCubeRotation.degree = mInterpolator.getValue();

        float diff = Math.abs(mTargetRotationDegree) - Math.abs(mInterpolator.getValue());

        if (diff < 0.1f && appearDisappear.base.appear.isEmpty() && appearDisappear.face.appear.isEmpty() && (Math.abs(1.0f - mElapsed) < EPSILON)) {
            mCubeRotation.degree = mTargetRotationDegree;
            mState = State.Completed;

            mLevelMenu.setFontColor(Game.fontColorOnMenuCube);

            mShowHint2nd = false;

            for (int i = 0; i < MAX_HINT_CUBES; ++i) {
                mHintCubes[i] = null;
            }
        }
    }

    private void updateCompleted() {
        mLevelMenu.update();

        if (mLevelMenu.cubeUp.isDone()) { // next
            if (0 == mLevelMenu.cubeUp.location.x) {
                switch (m_completed_face_next_action) {
                    case Next:
                        ++mLevelNumber;
                        if (mLevelNumber > 60) {
                            switch (mDifficulty) {
                                case Easy:
                                    mDifficulty = DifficultyEnum.Normal;
                                    mLevelNumber = 1;
                                    Audio.stopMusic();
                                    setupMusic();
                                    break;

                                case Normal:
                                    mDifficulty = DifficultyEnum.Hard;
                                    mLevelNumber = 1;
                                    Audio.stopMusic();
                                    setupMusic();
                                    break;

                                case Hard: // win!
                                    break;
                            }
                        } else {
                            if (mLevelNumber == 16 || mLevelNumber == 31 || mLevelNumber == 46) {
                                Audio.stopMusic();
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
            if (mLevelMenu.cubeMid.location.x == 0) {
                reset();
                setAnimFromCompleted();
            }
        }

        if (mLevelMenu.cubeLow.isDone()) { // quit
            if (mLevelMenu.cubeLow.location.x == 0) {
                eventQuit();
            }
        }
    }

    private void setAnimFromCompleted() {
        mState = State.AnimFromCompleted;
        mElapsed = 0.0f;
        mTargetRotationDegree = -45.0f;

        mInterpolator.setup(mCubeRotation.degree, mTargetRotationDegree, 6.0f);

        appearDisappear.base.setLevelAndDirection(MAX_CUBE_COUNT - 1, -1);
        appearDisappear.face.setLevelAndDirection(MAX_CUBE_COUNT - 1, -1);
    }

    private void updateAnimFromCompleted() {
        mElapsed += 0.04f;
        if (mElapsed > 1f) {
            mElapsed = 1f;
        }

        Utils.lerpCamera(mCameraLevelCompleted, mCameraLevel, mElapsed, mCameraCurrent);

        Cube cube;

        cube = appearDisappear.base.getCubeFromDisappearList();
        if (cube != null) {
            cubesBase.remove(cube);
        }

        cube = appearDisappear.face.getCubeFromDisappearList();
        if (cube != null) {
            cubesFace.remove(cube);
        }

        mInterpolator.interpolate();
        mCubeRotation.degree = mInterpolator.getValue();

        float diff = Math.abs(mCubeRotation.degree) - Math.abs(mTargetRotationDegree);

        if (diff < 0.1f && ((1.0f - mElapsed) < EPSILON) && appearDisappear.face.disappear.isEmpty() && appearDisappear.base.disappear.isEmpty()) {
            mCubeRotation.degree = mTargetRotationDegree;

            if (DifficultyEnum.Hard == mDifficulty && 61 == mLevelNumber) {
                Game.showScene(Scene_Outro);
            } else {
                setupAppear();
            }
        }
    }
    
    private void setAnimToPaused() {
        mState = State.AnimToPaused;

        mDrawMenuCubes = true;
        mDrawTexts = true;

        mTargetRotationDegree = mCubeRotation.degree + 90.0f + 45.0f;
        mInterpolator.setup(mCubeRotation.degree, mTargetRotationDegree, 5.0f);
        mElapsed = 0.0f;

        Creator.createTextsLevelPausedFace(this);

        CubeFont cubeFont;
        int size = fonts.size();
        for(int i = 0; i < size; ++i) {
            cubeFont = fonts.get(i);
            cubeFont.setColor(Game.textColorOnCubeFace);
        }

        mLevelMenu.setFontColor(20, 0, 0, 20);

        cubesBase.clear();
        cubesFace.clear();

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
            cubesBase.add(cube);
        }

        cube = appearDisappear.face.getCubeFromAppearList();
        if (cube != null) {
            cubesFace.add(cube);
        }

        mElapsed += 0.04f;
        if (mElapsed > 1f) {
            mElapsed = 1f;
        }

        Utils.lerpCamera(mCameraLevel, mCameraLevelPaused, mElapsed, mCameraCurrent);

        mInterpolator.interpolate();
        mCubeRotation.degree = mInterpolator.getValue();

        float diff = Math.abs(mInterpolator.getValue()) - Math.abs(mTargetRotationDegree);
        diff = Math.abs(diff);

        if (diff < 0.1f && ((1.0f - mElapsed) < EPSILON_SMALL) && appearDisappear.face.appear.isEmpty() && appearDisappear.base.appear.isEmpty() ) {
            mCubeRotation.degree = mTargetRotationDegree;
            mState = State.Paused;

            mLevelMenu.setFontColor(Game.fontColorOnMenuCube);

            mShowHint2nd = false;

            for (int i = 0; i < MAX_HINT_CUBES; ++i) {
                mHintCubes[i] = null;
            }
        }
    }

    private void updatePaused() {
        mLevelMenu.update();

        if (mLevelMenu.cubeUp.isDone()) { // back
            if (mLevelMenu.cubeUp.location.z == 8) {
                setAnimFromPaused();
            }
        }

        if (mLevelMenu.cubeMid.isDone()) { // reset
            if (mLevelMenu.cubeMid.location.z == 8) {
                reset();
                setAnimFromPaused();
            }
        }

        if (mLevelMenu.cubeLow.isDone()) { // quit
            if (mLevelMenu.cubeLow.location.z == 8) {
                mLevelMenu.done();
                eventQuit();
            }
        }
    }

    private void setAnimFromPaused() {
        mHud.setupAppear();
        mTargetRotationDegree = mCubeRotation.degree - 90.0f - 45.0f;
        mInterpolator.setup(mCubeRotation.degree, mTargetRotationDegree, 6.0f);
        mElapsed = 0.0f;
        mState = State.AnimFromPaused;

        appearDisappear.base.setLevelAndDirection(MAX_CUBE_COUNT - 1, -1);
        appearDisappear.face.setLevelAndDirection(MAX_CUBE_COUNT - 1, -1);
    }

    private void updateAnimFromPaused() {
        mElapsed += 0.04f;
        if (mElapsed > 1f) {
            mElapsed = 1f;
        }

        Utils.lerpCamera(mCameraLevelPaused, mCameraLevel, mElapsed, mCameraCurrent);

        Cube cube;

        cube = appearDisappear.base.getCubeFromDisappearList();
        if (cube != null) {
            cubesBase.remove(cube);
        }

        cube = appearDisappear.face.getCubeFromDisappearList();
        if (cube != null) {
            cubesFace.remove(cube);
        }

        mInterpolator.interpolate();
        mCubeRotation.degree = mInterpolator.getValue();

        // 90 <. - 45
        float diff = Math.abs( Math.abs(mTargetRotationDegree) - Math.abs(mInterpolator.getValue()) );

        if (diff < 0.1f && ((1.0f - mElapsed) < EPSILON) && appearDisappear.face.disappear.isEmpty() && appearDisappear.base.disappear.isEmpty() ) {
            mCubeRotation.degree = mTargetRotationDegree;
            mState = State.Playing;
            mDrawMenuCubes = false;
            mDrawTexts = false;
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

        if (mRepositionView) {
            mElapsed += 0.1f;

            if (mElapsed >= 1.0f) {
                mElapsed = 1.0f;
                mRepositionView = false;
                mUserRotation.reset();
            } else {
                mUserRotation.current.x = Utils.lerp(mUserRotation.from.x, 0.0f, mElapsed);
                mUserRotation.current.y = Utils.lerp(mUserRotation.from.y, 0.0f, mElapsed);
            }
        }

        mHud.update();

        if (LevelNextActionEnum.NoNextAction != mNextAction) {
            if (HUDStateEnum.Done == mHud.getState() ) {
                if (mNextAction == LevelNextActionEnum.ShowSceneSolvers) {
                    Game.renderToFBO(this);
                    Game.showScene(Scene_Solvers);
                    mNextAction = LevelNextActionEnum.NoNextAction;
                }
            }
        }

        if (mMenuCubeHilite != null) {
            mHiliteAlpha += 0.05f;

            if (mHiliteAlpha > 0.2f) {
                mHiliteAlpha = 0.2f;
            }
        }

        if (mShowHint2nd) {
            mHintTimeout -= 0.05f;

            if (mHintTimeout < 0.0f) {
                mHintTimeout = 0.2f;
                Cube cube = mHintCubes[mHintIndex];
                if (cube != null) {
                    Color col = new Color(255, 0, 0, 255);

                    cube.colorCurrent.init(col);
                    ++mHintIndex;

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
                    mShowHint2nd = false;
                }
            }
        }
    }

    private void warmCubes() {
        Game.updateHiLitedCubes( mLevelMenu.menuCubes );

        int size;
        Cube cube;

        size = cubesWallXminus.size();
        for (int i = 0; i < size; ++i) {
            cube = cubesWallXminus.get(i);
            cube.warmByFactor(WARM_FACTOR);
        }

        size = cubesEdge.size();
        for (int i = 0; i < size; ++i) {
            cube = cubesEdge.get(i);
            cube.warmByFactor(WARM_FACTOR);
        }

        size = cubesWallYminus.size();
        for (int i = 0; i < size; ++i) {
            cube = cubesWallYminus.get(i);
            cube.warmByFactor(WARM_FACTOR);
        }

        size = cubesWallZminus.size();
        for (int i = 0; i < size; ++i) {
            cube = cubesWallZminus.get(i);
            cube.warmByFactor(WARM_FACTOR);
        }

        size = cubesBase.size();
        for (int i = 0; i < size; ++i) {
            cube = cubesBase.get(i);
            cube.warmByFactor(WARM_FACTOR);
        }

        size = cubesLevel.size();
        for (int i = 0; i < size; ++i) {
            cube = cubesLevel.get(i);
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
            mState = State.Playing;
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

                if (State.Solving == mStateToRestore) {
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

                Audio.playSound(SOUND_CUBE_HIT);

                mState = mStateToRestore;

                if (State.Solving == mStateToRestore) {
                    mTimeout = 1.0f;
                }

                CubeLocation player = mPlayerCube.getLocation();
                CubeLocation key = mLocationKeyCube;

                if (player.equals(key)) {
                    eventLevelComplete();
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
                int dir = solution.getStep();
                boolean success = mPlayerCube.moveOnAxis(dir);

                if (success) {
                    mStateToRestore = mState;
                    mState = State.MovingPlayer;
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

        if (mTimeout < 1.6f) {
            mHud.showPrepareSolving(true, solution.getStepsCount());
        }

        if (mTimeout <= 0.0f) {
            mHud.showPrepareSolving(false, solution.getStepsCount());

            mState = State.Solving;
            solution.index = 0;
            mFadeValue = 1.0f;
            mTimeout = 1.0f;
            mHud.setupAppear();
        }
    }

    private void showTutor() {
        if (DifficultyEnum.Easy == mDifficulty) {
            int tutorId = -1;
            if (mLevelNumber == 1) {
                tutorId = Tutor_Swipe;
            } else if (mLevelNumber == 2) {
                tutorId = Tutor_MenuPause;
            } else if (mLevelNumber == 3) {
                tutorId = Tutor_MenuHint;
            } else if (mLevelNumber == 4) {
                tutorId = Tutor_Drag;
            } else if (mLevelNumber == 5) {
                tutorId = Tutor_Plain;
            } else if (mLevelNumber == 10) {
                tutorId = Tutor_Moving;
            } else if (mLevelNumber == 12) {
                tutorId = Tutor_Mover;
            } else if (mLevelNumber == 19) {
                tutorId = Tutor_Dead;
            }

            if (tutorId != -1) {
                mState = State.Tutorial;
                mHud.clearTutors();
                mHud.showTutor(tutorId);
            }
        }
    }

    private void updateAppear() {
        mTimeout += 0.01f;

        switch (mAppearState) {
            case SubAppearWait: 
                if (mTimeout > 0.2f) {
                    Cube cube;

                    cube = appearDisappear.level.getCubeFromDisappearList();
                    if (cube != null) {
                        cube.type = CubeTypeEnum.CubeIsInvisible;
                        cubesLevel.remove(cube);
                    }

                    cube = appearDisappear.level.getCubeFromDisappearList();
                    if (cube != null) {
                        cube.type = CubeTypeEnum.CubeIsInvisible;
                        cubesLevel.remove(cube);
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

                    if (cubesLevel.isEmpty() &&
                            movingCubes.isEmpty() &&
                            moverCubes.isEmpty() &&
                            deadCubes.isEmpty()) {
                        mTimeout = 0.0f;
                        mAppearState = SubAppearStateEnum.SubAppearKeyAndPlayer;
                        mElapsed = 0.0f;
                        mFadeValue = 0.0f;

                        eventBuildLevel();
                        reset();
                    }
                }
                break;

            case SubAppearKeyAndPlayer:
                if (mTimeout > 0.1f) {
                    mElapsed += 0.1;

                    if (mElapsed > 1.0f) {
                        mTimeout = 0.0f;
                        mElapsed = 1.0f;
                        mAppearState = SubAppearStateEnum.SubAppearWaitAgain;
                    }

                    mFadeValue = Utils.lerp(0.0f, 1.0f, mElapsed);
                }
                break;

            case SubAppearWaitAgain:
                if (mTimeout > 0.2) {
                    mAppearState = SubAppearStateEnum.SubAppearLevel;
                }
                break;

            case SubAppearLevel:
                if (appearDisappear.level.appear.isEmpty() &&
                    LevelBuilder.movingCubes.isEmpty() &&
                    LevelBuilder.moverCubes.isEmpty() &&
                    LevelBuilder.deadCubes.isEmpty()) {
                    mState = State.Playing;
                    showTutor();
            } else {
                if (mTimeout > 0.02f) {
                    Cube cube = appearDisappear.level.getCubeFromAppearList();

                    if (cube != null) {
                        cubesLevel.add(cube);
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
            mState = State.Playing;
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
                mState = State.Playing;
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

        Vector cubeOffset = Game.cubeOffset;
        glClear(GL_STENCIL_BUFFER_BIT);

        Cube cube;

        glEnable(GL_LIGHTING);

        glEnable(GL_TEXTURE_2D);
        Graphics.textureGrayConcrete.bind();

        Vector light = Utils.rotate3D_AroundYAxis(mPosLightCurrent.x, mPosLightCurrent.y, mPosLightCurrent.z, -mCubeRotation.degree - mUserRotation.current.y);
        Vector light_tmp = Utils.rotate3D_AroundXAxis(light.x, light.y, light.z, -mUserRotation.current.x);
        light.init(light_tmp);

        glEnable(GL_STENCIL_TEST);
        glDisable(GL_CULL_FACE);

        Graphics.bindStreamSources3d();

        int size;
        float[] shadowMat = new float[16];
        
        for (int j = 0; j < 3; ++j) { // 3 times (1 floor + 2 walls)
            Graphics.resetBufferIndices();

            switch (j) {
                case 0: // floor
                    Utils.calcShadowMatrixFloor(light, shadowMat, 0.0f);
                    size = cubesWallYminus.size();
                    for(int i = 0; i < size; ++i) {
                        cube = cubesWallYminus.get(i);
                        Graphics.addCubeFace_Y_Plus(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
                    }
                    break;

                case 1: // wall x
                    Utils.calcShadowMatrixWallX(light, shadowMat, 0.0f);
                    size = cubesWallXminus.size();
                    for(int i = 0; i < size; ++i) {
                        cube = cubesWallXminus.get(i);
                        Graphics.addCubeFace_X_Plus(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
                    }
                    break;

                case 2: // wall z
                    Utils.calcShadowMatrixWallZ(light, shadowMat, 0.0f);
                    size = cubesWallZminus.size();
                    for(int i = 0; i < size; ++i) {
                        cube = cubesWallZminus.get(i);
                        Graphics.addCubeFace_Z_Plus(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
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
            Graphics.updateBuffers();
            Graphics.renderTriangles(cubeOffset.x, cubeOffset.y, cubeOffset.z);

            //----------------------------------------
            // cast shadow on a receiver
            Color shadowColor = new Color(0, 0, 0, (int)((mFadeValue / 2.0f) * 255));

            Graphics.resetBufferIndices();

            // with level cubes
            size = cubesLevel.size();
            for(int i = 0; i < size; ++i) {
                cube = cubesLevel.get(i);
                Graphics.addCubeSize(cube.tx + cubeOffset.x, cube.ty + cubeOffset.y, cube.tz + cubeOffset.z, HALF_CUBE_SIZE, shadowColor);
            }

            // moving cubes
            if (!movingCubes.isEmpty()) {
                MovingCube movingCube;
                size = movingCubes.size();
                for(int i = 0; i < size; ++i) {
                    movingCube = movingCubes.get(i);
                    Graphics.addCubeSize(movingCube.pos.x + cubeOffset.x, movingCube.pos.y + cubeOffset.y, movingCube.pos.z + cubeOffset.z, HALF_CUBE_SIZE, shadowColor);
                }
            }

            // mover cubes
            if (!moverCubes.isEmpty()) {
                MoverCube moverCube;
                size = moverCubes.size();
                for(int i = 0; i < size; ++i) {
                    moverCube = moverCubes.get(i);
                    Graphics.addCubeSize(moverCube.pos.x + cubeOffset.x, moverCube.pos.y + cubeOffset.y, moverCube.pos.z + cubeOffset.z, HALF_CUBE_SIZE, shadowColor);
                }
            }

            // dead cubes
            if (!deadCubes.isEmpty()) {
                DeadCube deadCube;
                size = deadCubes.size();
                for(int i = 0; i < size; ++i) {
                    deadCube = deadCubes.get(i);
                    Graphics.addCubeSize(deadCube.pos.x + cubeOffset.x, deadCube.pos.y + cubeOffset.y, deadCube.pos.z + cubeOffset.z, HALF_CUBE_SIZE, shadowColor);
                }
            }

            // with locationPlayer cube
            if (mPlayerCube.location.x != 0) {
                Graphics.addCubeSize(mPlayerCube.pos.x + cubeOffset.x, mPlayerCube.pos.y + cubeOffset.y, mPlayerCube.pos.z + cubeOffset.z, HALF_CUBE_SIZE, shadowColor);
            }

            // with locationKey cube
            if (mLocationKeyCube.x != 0 && !isPlayerAndKeyInSamePosition())
            {
                Graphics.addCubeSize(Game.cubes[mLocationKeyCube.x][mLocationKeyCube.y][mLocationKeyCube.z].tx + cubeOffset.x,
                                     Game.cubes[mLocationKeyCube.x][mLocationKeyCube.y][mLocationKeyCube.z].ty + cubeOffset.y,
                                     Game.cubes[mLocationKeyCube.x][mLocationKeyCube.y][mLocationKeyCube.z].tz + cubeOffset.z, HALF_CUBE_SIZE * 0.95f, shadowColor);
            }

            glEnable(GL_BLEND);
            glDisable(GL_LIGHTING);
            glDisable(GL_TEXTURE_2D);
            glDisable(GL_DEPTH_TEST);

            glStencilOp(GL_KEEP, GL_KEEP, GL_ZERO);
            glStencilFunc(GL_EQUAL, 0xff, 0xff);

            glPushMatrix();
            glMultMatrixf(shadowMat, 0);
            Graphics.updateBuffers();
            Graphics.renderTriangles();
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
        Graphics.resetBufferIndices();

        size = cubesWallYminus.size();
        for (int i = 0; i < size; ++i) {
            cube = cubesWallYminus.get(i);
            Graphics.addCubeFace_X_Plus(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
            Graphics.addCubeFace_X_Minus(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
            Graphics.addCubeFace_Z_Plus(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
            Graphics.addCubeFace_Z_Minus(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
            Graphics.addCubeFace_Y_Minus(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
        }

        size = cubesWallXminus.size();
        for (int i = 0; i < size; ++i) {
            cube = cubesWallXminus.get(i);
            Graphics.addCubeFace_X_Minus(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
            Graphics.addCubeFace_Y_Plus(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
            Graphics.addCubeFace_Z_Plus(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
            Graphics.addCubeFace_Z_Minus(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
        }

        size = cubesWallZminus.size();
        for (int i = 0; i < size; ++i) {
            cube = cubesWallZminus.get(i);
            Graphics.addCubeFace_X_Plus(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
            Graphics.addCubeFace_X_Minus(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
            Graphics.addCubeFace_Y_Plus(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
            Graphics.addCubeFace_Z_Minus(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
        }

        // draw edges
        size = cubesEdge.size();
        for(int i = 0; i < size; ++i) {
            cube = cubesEdge.get(i);
            Graphics.addCubeSize(cube.tx, cube.ty, cube.tz, HALF_CUBE_SIZE, cube.colorCurrent);
        }

        // level cubes
        size = cubesLevel.size();
        for(int i = 0; i < size; ++i) {
            cube = cubesLevel.get(i);
            Graphics.addCubeSize(cube.tx, cube.ty, cube.tz, HALF_CUBE_SIZE, cube.colorCurrent);
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

        if (State.Playing != mState) {
            size = cubesFace.size();
            for(int i = 0; i < size; ++i) {
                cube = cubesFace.get(i);
                Graphics.addCubeSize(cube.tx, cube.ty, cube.tz, HALF_CUBE_SIZE, cube.colorCurrent);
            }

            size = cubesBase.size();
            for(int i = 0; i < size; ++i) {
                cube = cubesBase.get(i);
                Graphics.addCubeSize(cube.tx, cube.ty, cube.tz, HALF_CUBE_SIZE, cube.colorCurrent);
            }
        }

        Graphics.updateBuffers();
        Graphics.renderTriangles(cubeOffset.x, cubeOffset.y, cubeOffset.z);

        glPopMatrix();
    }

    private void drawTextsCompleted() {


        CubeFont cubeFont;
        TexCoordsQuad coords = new TexCoordsQuad();
        TexturedQuad pFont;
        int size;
        Graphics.bindStreamSources3d();
        Graphics.resetBufferIndices();

        size = fonts.size();
        for(int i = 0; i < size; ++i) {
            cubeFont = fonts.get(i);
            pFont = cubeFont.texturedQuad;

            coords.tx0 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);
            coords.tx1 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
            coords.tx2 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
            coords.tx3 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);

            Graphics.addCubeFace_Z_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        if (mLevelMenu.cubeUp.isDone() && mLevelMenu.cubeUp.location.x == 7) {
            cubeFont = mLevelMenu.fontUp;
            pFont = cubeFont.texturedQuad;

            coords.tx0 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);
            coords.tx1 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
            coords.tx2 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
            coords.tx3 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);

            Graphics.addCubeFace_Z_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        if (mLevelMenu.cubeMid.isDone() && mLevelMenu.cubeMid.location.x == 7) {
            cubeFont = mLevelMenu.fontMid;
            pFont = cubeFont.texturedQuad;

            coords.tx0 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);
            coords.tx1 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
            coords.tx2 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
            coords.tx3 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);

            Graphics.addCubeFace_Z_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        if (mLevelMenu.cubeLow.isDone() && mLevelMenu.cubeLow.location.x == 7) {
            cubeFont = mLevelMenu.fontLow;
            pFont = cubeFont.texturedQuad;

            coords.tx0 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);
            coords.tx1 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
            coords.tx2 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
            coords.tx3 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);

            Graphics.addCubeFace_Z_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        Graphics.updateBuffers();
        Graphics.renderTriangles();
    }

    private void drawTextsPaused() {


        CubeFont cubeFont;
        TexCoordsQuad coords = new TexCoordsQuad();
        TexturedQuad pFont;
        int size;
        Graphics.bindStreamSources3d();
        Graphics.resetBufferIndices();

        size = fonts.size();
        for(int i = 0; i < size; ++i) {
            cubeFont = fonts.get(i);
            pFont = cubeFont.texturedQuad;

            coords.tx0 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
            coords.tx1 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
            coords.tx2 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);
            coords.tx3 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);

            Graphics.addCubeFace_X_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        if (mLevelMenu.cubeUp.isDone() && mLevelMenu.cubeUp.location.z == 1) {
            cubeFont = mLevelMenu.fontUp;
            pFont = cubeFont.texturedQuad;

            coords.tx0 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
            coords.tx1 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
            coords.tx2 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);
            coords.tx3 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);

            Graphics.addCubeFace_X_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        if (mLevelMenu.cubeMid.isDone() && mLevelMenu.cubeMid.location.z == 1) {
            cubeFont = mLevelMenu.fontMid;
            pFont = cubeFont.texturedQuad;

            coords.tx0 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
            coords.tx1 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
            coords.tx2 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);
            coords.tx3 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);

            Graphics.addCubeFace_X_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        if (mLevelMenu.cubeLow.isDone() && mLevelMenu.cubeLow.location.z == 1) {
            cubeFont = mLevelMenu.fontLow;
            pFont = cubeFont.texturedQuad;

            coords.tx0 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
            coords.tx1 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
            coords.tx2 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);
            coords.tx3 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);

            Graphics.addCubeFace_X_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }
        Graphics.updateBuffers();
        Graphics.renderTriangles();
    }

    private void draw() {

        Color color = new Color(255, 255, 255, (int)(mFadeValue * 255));
        Graphics.bindStreamSources3d();

        if (mFadeValue < 1.0f) {
            glEnable(GL_BLEND);
            glDisable(GL_DEPTH_TEST);
        }

        glEnable(GL_LIGHTING);

        if (mDrawMenuCubes || mPlayerCube.location.x != 0) {
            Graphics.texturePlayer.bind();

            Graphics.resetBufferIndices();

            if (0 != mPlayerCube.location.x) {
                Graphics.addCubeSize(mPlayerCube.pos.x, mPlayerCube.pos.y, mPlayerCube.pos.z, HALF_CUBE_SIZE, color);
            }

            if (mDrawMenuCubes) {
                mLevelMenu.draw();
            }
            Graphics.updateBuffers();
            Graphics.renderTriangles(Game.cubeOffset.x, Game.cubeOffset.y, Game.cubeOffset.z);
        }

        if (0 != mLocationKeyCube.x && !isPlayerAndKeyInSamePosition()) {
            Graphics.textureKey.bind();

            Cube pCube = Game.cubes[mLocationKeyCube.x][mLocationKeyCube.y][mLocationKeyCube.z];

            Graphics.resetBufferIndices();
            Graphics.addCubeSize(pCube.tx, pCube.ty, pCube.tz, HALF_CUBE_SIZE * 0.95f, color);
            Graphics.updateBuffers();
            Graphics.renderTriangles(Game.cubeOffset.x, Game.cubeOffset.y, Game.cubeOffset.z);
        }

        if (mFadeValue < 1.0f) {
            glDisable(GL_BLEND);
            glEnable(GL_DEPTH_TEST);
        }

        if (!movingCubes.isEmpty() || !moverCubes.isEmpty() || !deadCubes.isEmpty()) {
            int size;
            MovingCube movingCube;
            MoverCube moverCube;
            DeadCube deadCube;

            Graphics.resetBufferIndices();

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
            Graphics.bindStreamSources3d();
            Graphics.textureSymbols.bind();
            Graphics.updateBuffers();
            Graphics.renderTriangles(Game.cubeOffset.x, Game.cubeOffset.y, Game.cubeOffset.z);
        }

        if (mMenuCubeHilite != null) {
            Graphics.textureSymbols.bind();
            color.init( new Color(color.r, color.g, color.b, (int)(mHiliteAlpha * 255)));
            glDisable(GL_DEPTH_TEST);
            glDisable(GL_CULL_FACE);
            glDisable(GL_LIGHTING);

            TexCoordsQuad coords = new TexCoordsQuad();

            TexturedQuad p = mFontHilite.texturedQuad;

            coords.tx0 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
            coords.tx1 = new Vector2(p.tx_up_left.x,  p.tx_up_left.y);
            coords.tx2 = new Vector2(p.tx_lo_left.x,  p.tx_lo_left.y);
            coords.tx3 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);

            Graphics.resetBufferIndices();
            Graphics.bindStreamSources3d();

            switch (mState) {
                case Paused: //Face_X_Minus:
                    Graphics.addCubeFace_X_Minus(mFontHilite.pos.x, mFontHilite.pos.y, mFontHilite.pos.z, coords, color);
                    break;

                case Completed: //Face_Z_Minus:
                    Graphics.addCubeFace_Z_Minus(mFontHilite.pos.x, mFontHilite.pos.y, mFontHilite.pos.z, coords, color);
                    break;

                default:
                    break;
            }
            glEnable(GL_BLEND);
            Graphics.updateBuffers();
            Graphics.renderTriangles(Game.cubeOffset.x, Game.cubeOffset.y, Game.cubeOffset.z);

            glEnable(GL_LIGHTING);
            glEnable(GL_DEPTH_TEST);
            glEnable(GL_CULL_FACE);
        }

        if (mDrawTexts) {
            glPushMatrix();
            glTranslatef(Game.cubeOffset.x, Game.cubeOffset.y, Game.cubeOffset.z);

            glEnable(GL_TEXTURE_2D);
            Graphics.textureFonts.bind();

            glEnable(GL_BLEND);
            glDisable(GL_LIGHTING);
            glDisableClientState(GL_NORMAL_ARRAY);

            if (mState == State.AnimToPaused || mState == State.AnimFromPaused || mState == State.Paused) {
                drawTextsPaused();
            } else {
                drawTextsCompleted();
            }
            glPopMatrix();
        }
    }

    @Override
    public void render() {

//        if (mState == State.Completed) {
//            renderForPicking(PickRenderTypeEnum.RenderOnlyMovingCubes);
//            if (true) {
//                return;
//            }
//        }

        Graphics.setProjection2D();
        Graphics.setModelViewMatrix2D();
        Graphics.bindStreamSources2d();

        glEnable(GL_LIGHT0);
        glEnable(GL_BLEND);
        glDisable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);
        glDepthMask(false); //GL_FALSE);

        Color color = new Color(255, 255, 255, (int)Game.dirtyAlpha);
        Graphics.drawFullScreenTexture(Graphics.textureDirty, color);

        glDepthMask(true); //GL_TRUE);

        Graphics.setProjection3D();
        Graphics.setModelViewMatrix3D(mCameraCurrent);
        Graphics.bindStreamSources3d();

        // draw axes global
//        glDisable(GL_TEXTURE_2D);
//        Graphics.drawAxes();
//        glEnable(GL_TEXTURE_2D);

        Graphics.setLightPosition(mPosLightCurrent);
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
//        Graphics.drawAxes();
//        glEnable(GL_LIGHTING);
//        glEnable(GL_TEXTURE_2D);

        draw();

        glPopMatrix();

        glPopMatrix();

        mHud.render();
    }

    private void renderForPicking(PickRenderTypeEnum type) {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glDisable(GL_LIGHTING);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);

        glDisable(GL_TEXTURE_2D);

        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);

        Graphics.setProjection3D();
        Graphics.setModelViewMatrix3D(mCameraCurrent);

        glPushMatrix();

        switch (type) {
            case RenderOnlyHUD:
                mHud.renderForPicking();
                break;

            case RenderOnlyMovingCubes:
                Graphics.resetBufferIndices();
                Graphics.bindStreamSources3d();

                glRotatef(mCubeRotation.degree, mCubeRotation.axis.x, mCubeRotation.axis.y, mCubeRotation.axis.z);

                mLevelMenu.drawForPicking();

                Graphics.updateBuffers();
                Graphics.renderTriangles(Game.cubeOffset.x, Game.cubeOffset.y, Game.cubeOffset.z);
                break;

            default:
                break;
        } // switch

        glPopMatrix();
    }

    @Override
    public void renderToFBO() {

        Graphics.setProjection2D();
        Graphics.setModelViewMatrix2D();

        glEnable(GL_BLEND);
        glDisable(GL_LIGHTING);
        glDepthMask(false);
        glEnable(GL_TEXTURE_2D);

        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);

        Color color_dirty = new Color(255, 255, 255, (int)Game.dirtyAlpha);
        Graphics.drawFullScreenTexture(Graphics.textureDirty, color_dirty);

        glDepthMask(true);

        Graphics.setProjection3D();
        Graphics.setModelViewMatrix3D(mCameraCurrent);

        glEnable(GL_LIGHTING);

        Graphics.setLightPosition(mPosLightCurrent);

        glRotatef(mCubeRotation.degree, mCubeRotation.axis.x, mCubeRotation.axis.y, mCubeRotation.axis.z);
        drawTheCube();

        glEnable(GL_BLEND);

        Graphics.bindStreamSources3d();
        Graphics.textureKey.bind();

        if (0 != mLocationKeyCube.x) {
            glEnable(GL_LIGHTING);
            Graphics.textureKey.bind();

            Cube cube = Game.cubes[mLocationKeyCube.x][mLocationKeyCube.y][mLocationKeyCube.z];

            Graphics.resetBufferIndices();
            Graphics.addCubeSize(cube.tx, cube.ty, cube.tz, HALF_CUBE_SIZE * 0.95f, Color.WHITE);
            Graphics.renderTriangles(Game.cubeOffset.x, Game.cubeOffset.y, Game.cubeOffset.z);

            glDisable(GL_LIGHTING);
        }

        glDisable(GL_BLEND);

        // draw locationKey cube
        Graphics.resetBufferIndices();

        glEnable(GL_LIGHTING);
        Graphics.textureKey.bind();

        CubeLocation pos = mLocationKeyCube;
        Cube keyCube = Game.cubes[pos.x][pos.y][pos.z];

        Graphics.addCubeSize(keyCube.tx, keyCube.ty, keyCube.tz, HALF_CUBE_SIZE * 0.95f, Color.WHITE);
        Graphics.updateBuffers();
        Graphics.renderTriangles(Game.cubeOffset.x, Game.cubeOffset.y, Game.cubeOffset.z);

        // draw locationPlayer cube
        Graphics.resetBufferIndices();

        glEnable(GL_LIGHTING);
        Graphics.texturePlayer.bind();
        Graphics.addCubeSize(mPlayerCube.pos.x, mPlayerCube.pos.y, mPlayerCube.pos.z, HALF_CUBE_SIZE, Color.WHITE);
        Graphics.updateBuffers();
        Graphics.renderTriangles(Game.cubeOffset.x, Game.cubeOffset.y, Game.cubeOffset.z);

        glDisable(GL_LIGHTING);

        if (!movingCubes.isEmpty() || !moverCubes.isEmpty() || !deadCubes.isEmpty()) {
            int size;
            MovingCube movingCube;
            MoverCube moverCube;
            DeadCube deadCube;

            Graphics.resetBufferIndices();

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

            Graphics.bindStreamSources3d();
            Graphics.textureSymbols.bind();
            Graphics.updateBuffers();
            Graphics.renderTriangles(Game.cubeOffset.x, Game.cubeOffset.y, Game.cubeOffset.z);
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
        mHud.setTextStars(GameProgress.getStarCount());

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
            mShowHint2nd = true;
            mHintIndex = 0;
            mHintTimeout = 0.0f;
            
            for (int i = 0; i < MAX_HINT_CUBES; ++i) {
                mHintCubes[i] = null;
            }

            Cube cube;
            int size = cubesHint.size();
            for(int i = 0; i < size; ++i) {
                cube = cubesHint.get(i);
                mHintCubes[i] = cube;
            }
        }
    }

    private void eventSolver() {
        if (LevelNextActionEnum.ShowSceneSolvers == mNextAction || State.PrepareSolving == mState) {
            return;
        }

        boolean solved = false;
        switch (mDifficulty) {
            case Easy: solved = GameProgress.isSolvedEasy(mLevelNumber); break;
            case Normal: solved = GameProgress.isSolvedNormal(mLevelNumber); break;
            case Hard: solved = GameProgress.isSolvedHard(mLevelNumber); break;
        }

        if (solved) {
            reset();
            mState = State.PrepareSolving;
            mTimeout = 2.0f;
        } else {
            mNextAction = LevelNextActionEnum.ShowSceneSolvers;
        }

        mHud.setupDisappear();
    }

    private void eventQuit() {
        // setup menu obj to point to a cube face where current level is located
        CubeFaceNames faceName = Menu.getCubeFaceName(mDifficulty, mLevelNumber);
        Game.menuInitData.cubeFaceName = faceName;
        Game.menuInitData.reappear = true;
        Game.menu.init();

        Game.setAnimFaces(mDifficulty, mLevelNumber);

        Game.animInitData.cubesBase.clear();
        int size;
        Cube cube;

        size = cubesBase.size();
        for(int i = 0; i < size; ++i) {
            cube = cubesBase.get(i);
            Game.animInitData.cubesBase.add(cube);
        }

        size = cubesEdge.size();
        for(int i = 0; i < size; ++i) {
            cube = cubesEdge.get(i);
            Game.animInitData.cubesBase.add(cube);
        }

        Game.animInitData.cubeRotationDegree = mCubeRotation.degree;
        Game.animInitData.type = AnimTypeEnum.AnimToMenu;

        Game.showScene(Scene_Anim);
    }

    private void eventLevelPause() {
        mHud.setupDisappear();
        setAnimToPaused();
    }

    public void eventLevelComplete() {
        Audio.playSound(SOUND_LEVEL_COMPLETED);

        Game.renderToFBO(this);
        mHud.setupDisappear();

        StatInitData sid = Game.statInitData;
        getRatings();

        switch (mDifficulty) {
            case Easy:
                GameProgress.setStarsEasy(mLevelNumber, sid.stars);
                GameProgress.setMovesEasy(mLevelNumber, mMovesCounter);

                if (mLevelNumber < 60) {
                    if (LEVEL_LOCKED == GameProgress.getStarsEasy(mLevelNumber + 1))
                    GameProgress.setStarsEasy(mLevelNumber + 1, LEVEL_UNLOCKED);
                }
                break;

            case Normal:
                GameProgress.setStarsNormal(mLevelNumber, sid.stars);
                GameProgress.setMovesNormal(mLevelNumber, mMovesCounter);

                if (mLevelNumber < 60) {
                    if (LEVEL_LOCKED == GameProgress.getStarsNormal(mLevelNumber + 1)) {
                        GameProgress.setStarsNormal(mLevelNumber + 1, LEVEL_UNLOCKED);
                    }
                }
                break;

            case Hard:
                GameProgress.setStarsHard(mLevelNumber, sid.stars);
                GameProgress.setMovesHard(mLevelNumber, mMovesCounter);

                if (mLevelNumber < 60) {
                    if (LEVEL_LOCKED == GameProgress.getStarsHard(mLevelNumber + 1)) {
                        GameProgress.setStarsHard(mLevelNumber + 1, LEVEL_UNLOCKED);
                    }
                }
                break;
        }

        GameProgress.save();
        mState = State.SetupAnimToCompleted;
        Game.showScene(Scene_Stat);
    }

    public void reset() {
        int size;
        mMovesCounter = 0;
        mUndoList.clear();

        mHud.set1stHint();
        mHud.setTextMoves(mMovesCounter);
        mHud.setTextUndo( mUndoList.size() );
        mHud.flashMotto(true);

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
            size = moverCubes.size();
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

            mState = State.Undo;
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
                    if (HUDStateEnum.Done == mHud.getState() ) {
                        renderForPicking(PickRenderTypeEnum.RenderOnlyHUD);
                        mHud.flashMotto(false);
                        downColor = Graphics.getColorFromScreen(mPosDown);
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

                    downColor = Graphics.getColorFromScreen(mPosDown);
                    MenuCube menuCube = mLevelMenu.getMenuCubeFromColor(downColor.b);
                    if (menuCube != null) {
                        mHiliteAlpha = 0.0f;
                        mMenuCubeHilite = menuCube;
                        CubeLocation cp = new CubeLocation();
                        cp.init(mMenuCubeHilite.location);
                        mFontHilite.init(Symbol_Hilite, cp);
                    }
                }
                break;

                default:
                    break;
            } // switch
        }
    }

    @Override
    public void onFingerUp(float x, float y, int fingerCount) {

        // TODO: comment this out in final release
        if (x > Graphics.width - 100 && y > Graphics.height - 100) {
            int ln = mLevelNumber + 1;
            DifficultyEnum diff = mDifficulty;

            if (ln > 60)
            {
                ln = 1;
                if (DifficultyEnum.Easy == mDifficulty)
                    diff = DifficultyEnum.Normal;

                if (DifficultyEnum.Normal == mDifficulty)
                    diff = DifficultyEnum.Hard;

                if (DifficultyEnum.Hard == mDifficulty)
                    diff = DifficultyEnum.Easy;
            }

            Game.levelInitData.difficulty = diff;
            Game.levelInitData.levelNumber = ln;
            Game.levelInitData.initAction = LevelInitActionEnum.FullInit;

            Game.showScene(Scene_Level);

            return;
        }

        mPosUp.x = x;
        mPosUp.y = y;

        switch (mState) {
            case Playing:
                fingerUpPlaying(x, y, fingerCount);
                mHud.setHilitePause(false);
                mHud.setHiliteUndo(false);
                mHud.setHiliteHint(false);
                mHud.setHiliteSolver(false);
                break;

            case Completed:
                mMenuCubeHilite = null;
                fingerUpCompleted(x, y);
                break;

            case Paused:
                mMenuCubeHilite = null;
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
    public void onFingerMove(float prevX, float prevY, float curX, float curY, int fingerCount) {
        mPosMove.x = curX;
        mPosMove.y = curY;

        float dist = Utils.getDistance2D(mPosDown, mPosMove);
        //printf("\nOnFingerMove: %.2f", dist);

        if (dist > Game.minSwipeLength) {
            mIsSwipe = true;
        }

        if (2 == fingerCount) {
            if (mState == State.Playing) {
                Vector2 dir = new Vector2();
                dir.x = (mPosDown.x - curX) / Graphics.deviceScale;
                dir.y = (mPosDown.y - curY) / Graphics.deviceScale;

                mAlterView = true;

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
            Color downColor = Graphics.getColorFromScreen(mPosDown);
            SwipeInfo swipeInfo = Game.getSwipeDirAndLength(mPosDown, mPosUp);

            if (swipeInfo.length > 30.0f * Graphics.scaleFactor) {
                MenuCube menuCube = mLevelMenu.getMenuCubeFromColor(downColor.b);
                if (menuCube != null) {
                    switch (swipeInfo.swipeDir) {
                        case SwipeRight: menuCube.moveOnAxis(Z_Plus); break;
                        case SwipeLeft:  menuCube.moveOnAxis(Z_Minus); break;
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
            Color downColor = Graphics.getColorFromScreen(mPosDown);
            SwipeInfo swipeInfo = Game.getSwipeDirAndLength(mPosDown, mPosUp);

            if (swipeInfo.length > 30.0f * Graphics.scaleFactor) {
                MenuCube menuCube = mLevelMenu.getMenuCubeFromColor(downColor.b);
                if (menuCube != null) {
                    switch (swipeInfo.swipeDir) {
                        case SwipeLeft: menuCube.moveOnAxis(X_Plus); break;
                        case SwipeRight: menuCube.moveOnAxis(X_Minus); break;
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

        if (mAlterView) {
            if ( Math.abs(mUserRotation.current.x) > EPSILON || Math.abs(mUserRotation.current.y) > EPSILON ) {
                mRepositionView = true;
                mAlterView = false;
                mElapsed = 0.0f;
                mUserRotation.from = mUserRotation.current;
            }
            return;
        }

        if (!mIsSwipe) {
            if (mState == State.Playing) {
                renderForPicking(PickRenderTypeEnum.RenderOnlyHUD);
                Vector2 pos = new Vector2(x,y);
                Color color = Graphics.getColorFromScreen(pos);
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
                success = mPlayerCube.moveOnAxis(Y_Plus);
            }

            center = -90.0f; // Y minus
            if (degree > (center - space) && degree < (center + space)) {
                success = mPlayerCube.moveOnAxis(Y_Minus);
            }

            center = 30.0f; // Z plus
            if (degree > (center - space) && degree < (center + space)) {
                success = mPlayerCube.moveOnAxis(Z_Minus);
            }

            center = -150.0f; // Z minus
            if (degree > (center - space) && degree < (center + space)) {
                success = mPlayerCube.moveOnAxis(Z_Plus);
            }

            center = -30.0f; // X minus
            if (degree > (center - space) && degree < (center + space)) {
                success = mPlayerCube.moveOnAxis(X_Plus);
            }

            center = 150.0f; // X plus
            if (degree > (center - space) && degree < (center + space)) {
                success = mPlayerCube.moveOnAxis(X_Minus);
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
        mState = State.MovingPlayer;
        mTimeout = 0.0f; // move immediately
    }

}

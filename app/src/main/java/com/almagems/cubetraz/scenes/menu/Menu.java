package com.almagems.cubetraz.scenes.menu;

import com.almagems.cubetraz.Audio;
import com.almagems.cubetraz.GameOptions;
import com.almagems.cubetraz.GameProgress;
import com.almagems.cubetraz.cubes.CubeLocation;
import com.almagems.cubetraz.cubes.MenuCube;
import com.almagems.cubetraz.graphics.Camera;
import com.almagems.cubetraz.graphics.Color;
import com.almagems.cubetraz.graphics.Graphics;
import com.almagems.cubetraz.graphics.TexCoordsQuad;
import com.almagems.cubetraz.graphics.TexturedQuad;
import com.almagems.cubetraz.scenes.Creator;
import com.almagems.cubetraz.cubes.Cube;
import com.almagems.cubetraz.cubes.CubeFont;
import com.almagems.cubetraz.utils.EaseOutDivideInterpolation;
import com.almagems.cubetraz.Game;
import com.almagems.cubetraz.cubes.LevelCube;
import com.almagems.cubetraz.utils.SwipeInfo;
import com.almagems.cubetraz.math.Utils;
import com.almagems.cubetraz.math.Vector;
import com.almagems.cubetraz.math.Vector2;
import com.almagems.cubetraz.scenes.Scene;

import java.util.ArrayList;
import static android.opengl.GLES10.*;
import static com.almagems.cubetraz.Audio.*;
import static com.almagems.cubetraz.Game.*;

public final class Menu extends Scene {

    private enum StateEnum {
        InMenu,
        InCredits,
        AnimToCredits,
        AnimFromCredits
    }

    private final ArrayList<ArrayList<CubeFont>> mTitles = new ArrayList<>(7);
    private final ArrayList<ArrayList<CubeFont>> mTexts = new ArrayList<>(7);
    private final ArrayList<ArrayList<CubeFont>> mSymbols = new ArrayList<>(7);

    private final ArrayList<MenuCube> mMenuCubes = new ArrayList<>(3);

    private StateEnum mState;

    private float m_t;

    private MenuNavigator mNavigator = new MenuNavigator();

    private boolean m_can_alter_text;

    private float m_credits_offset;

    private Color mColorDown;
    private Color mColorUp;

    private boolean mShowingHelp;
    private float mShowHelpTimeout;

    public Camera mCameraMenu = new Camera();
    private Camera mCameraCredits = new Camera();

    private CubeFaceNames mPrevFace;

    public MenuCube mMenuCubePlay;
    public MenuCube mMenuCubeOptions;
    public MenuCube mMenuCubeStore;

    public final MenuCube[] optionCubes = new MenuCube[4];

    public MenuCube cubeCredits;

    private MenuCube mMenuCubeHilite;
    private CubeFont m_font_hilite = new CubeFont();
    private float m_hilite_alpha;

    // fonts on red cubes
    public CubeFont m_cubefont_play = new CubeFont();
    public CubeFont m_cubefont_options = new CubeFont();
    public CubeFont m_cubefont_store = new CubeFont();
    public CubeFont m_cubefont_noads = new CubeFont();
    public CubeFont m_cubefont_solvers = new CubeFont();
    public CubeFont m_cubefont_restore = new CubeFont();

    private final ArrayList<Cube> mCubesBase = new ArrayList<>();
    public final ArrayList<Cube> mCubesFace = new ArrayList<>();

    private final EaseOutDivideInterpolation[] mInterpolators = new EaseOutDivideInterpolation[6];

    private CubeFaceNames mCurrentCubeFaceName;
    private int mCurrentCubeFaceType;

    private MenuCube getMenuCubeFromColor(int color) {
        switch (color) {
            case 255: return mMenuCubePlay;
            case 200: return mMenuCubeOptions;
            case 100: return mMenuCubeStore;
            case 1:   return cubeCredits;
        }
        return null;
    }

    public void dontHiliteMenuCube() {
        mMenuCubeHilite = null;
    }
    public Camera getCamera() {
        return mCameraMenu;
    }
    public Vector getLightPositon() {
        return mPosLight;
    }

    public Menu() {
        m_can_alter_text = false;

        mPosLight = new Vector(-10.0f, 3.0f, 12.0f);

        m_cubefont_play = new CubeFont();
        m_cubefont_options = new CubeFont();
        m_cubefont_store = new CubeFont();
        m_cubefont_noads = new CubeFont();
        m_cubefont_solvers = new CubeFont();
        m_cubefont_restore = new CubeFont();

        for (int i = 0; i < 6; ++i) {
            mInterpolators[i] = new EaseOutDivideInterpolation();
        }

        ArrayList<CubeFont> lst;
        for (int i = 0; i < 7; ++i) {
            mTitles.add(new ArrayList<CubeFont>());
            mTexts.add(new ArrayList<CubeFont>());
            mSymbols.add(new ArrayList<CubeFont>());

            lst = mTitles.get(i);
            for (int j = 0; j < 7; j++) {
                lst.add(new CubeFont());
            }

            lst = mTexts.get(i);
            for (int j = 0; j < 7; j++) {
                lst.add(new CubeFont());
            }

            lst = mSymbols.get(i);
            for (int j = 0; j < 7; j++) {
                lst.add(new CubeFont());
            }
        }
    }

    private void setupCameras() {
        mCameraCredits.eye = new Vector(18.0f / 1.5f, 30.0f / 1.5f, 45.0f / 1.5f);
        mCameraCredits.target = new Vector(-2.0f / 1.5f, 0.0f, 5.0f / 1.5f);

        // DRAW_AXES_CUBE || DRAW_AXES_GLOBAL
        //mCameraMenu.eye = new Vector(6.0f , 3.0f, 26.0f);

        mCameraMenu.eye = new Vector(0.0f, 0.0f, 35.0f / 1.5f);
        mCameraMenu.target.init(new Vector(0.0f, 0.0f, 0.0f));

        mCameraMenu.eye.scaled(Graphics.aspectRatio);
        mCameraCredits.eye.scaled(Graphics.aspectRatio);

        mPosLightCurrent.init(mPosLight);
        mCameraCurrent.init(mCameraMenu);
    }

    private void setCubeFaceTypeFromCubeFaceName(CubeFaceNames faceName) {
        switch (faceName) {
            case Face_Easy01: mCurrentCubeFaceType = X_Plus; break;
            case Face_Easy02: mCurrentCubeFaceType = Y_Minus; break;
            case Face_Easy03: mCurrentCubeFaceType = X_Minus; break;
            case Face_Easy04: mCurrentCubeFaceType = Y_Plus; break;

            case Face_Normal01: mCurrentCubeFaceType = Z_Minus; break;
            case Face_Normal02: mCurrentCubeFaceType = Y_Minus; break;
            case Face_Normal03: mCurrentCubeFaceType = Z_Plus; break;
            case Face_Normal04: mCurrentCubeFaceType = Y_Plus; break;

            case Face_Hard01: mCurrentCubeFaceType = X_Minus; break;
            case Face_Hard02: mCurrentCubeFaceType = Y_Minus; break;
            case Face_Hard03: mCurrentCubeFaceType = X_Plus; break;
            case Face_Hard04: mCurrentCubeFaceType = Y_Plus; break;
        }
    }

    private void setPlayCubeLocationAndMenuRotation() {
        switch (mCurrentCubeFaceName) {
            case Face_Easy01:
                if (mMenuCubePlay.location.x != 8) {
                    mMenuCubePlay.setCubeLocation(8, 5, 7);
                }
                mNavigator.primaryRotation.init(-90.0f, Game.vectorYaxis);
                mNavigator.secondaryRotation.init(0.0f, Game.vectorZaxis);
                break;

            case Face_Easy02:
                if (mMenuCubePlay.location.y != 0) {
                    mMenuCubePlay.setCubeLocation(2, 0, 6);
                }
                mNavigator.primaryRotation.init(-90.0f, Game.vectorYaxis);
                mNavigator.secondaryRotation.init(90.0f, Game.vectorZaxis);
                break;

            case Face_Easy03:
                if (mMenuCubePlay.location.x != 0) {
                    mMenuCubePlay.setCubeLocation(0, 2, 1);
                }
                mNavigator.primaryRotation.init(-90.0f, Game.vectorYaxis);
                mNavigator.secondaryRotation.init(180.0f, Game.vectorZaxis);
                break;

            case Face_Easy04:
                if (mMenuCubePlay.location.y != 8) {
                    mMenuCubePlay.setCubeLocation(2, 8, 6);
                }
                mNavigator.primaryRotation.init(-90.0f, Game.vectorYaxis);
                mNavigator.secondaryRotation.init(270.0f, Game.vectorZaxis);
                break;


            case Face_Normal01:
                if (mMenuCubePlay.location.z != 0) {
                    mMenuCubePlay.setCubeLocation(4, 6, 0);
                }
                mNavigator.primaryRotation.init(-180.0f, Game.vectorYaxis);
                mNavigator.secondaryRotation.init(0.0f, Game.vectorZaxis);
                break;

            case Face_Normal02:
                if (mMenuCubePlay.location.y != 0) {
                    mMenuCubePlay.setCubeLocation(6, 0, 4);
                }
                mNavigator.primaryRotation.init(-180.0f, Game.vectorYaxis);
                mNavigator.secondaryRotation.init(90.0f, Game.vectorXaxis);
                break;

            case Face_Normal03:
                if (mMenuCubePlay.location.z != 8) {
                    mMenuCubePlay.setCubeLocation(7, 2, 8);
                }
                mNavigator.primaryRotation.init(-180.0f, Game.vectorYaxis);
                mNavigator.secondaryRotation.init(180.0f, Game.vectorXaxis);
                break;

            case Face_Normal04:
                if (mMenuCubePlay.location.y != 8) {
                    mMenuCubePlay.setCubeLocation(6, 8, 4);
                }
                mNavigator.primaryRotation.init(-180.0f, Game.vectorYaxis);
                mNavigator.secondaryRotation.init(270.0f, Game.vectorXaxis);
                break;


            case Face_Hard01:
                if (mMenuCubePlay.location.x != 0) {
                    mMenuCubePlay.setCubeLocation(0, 2, 2);
                }
                mNavigator.primaryRotation.init(-270.0f, Game.vectorYaxis);
                mNavigator.secondaryRotation.init(0.0f, Game.vectorXaxis);
                break;

            case Face_Hard02:
                if (mMenuCubePlay.location.y != 0) {
                    mMenuCubePlay.setCubeLocation(2, 0, 6);
                }
                mNavigator.primaryRotation.init(-270.0f, Game.vectorYaxis);
                mNavigator.secondaryRotation.init(-90.0f, Game.vectorZaxis);
                break;

            case Face_Hard03:
                if (mMenuCubePlay.location.x != 8) {
                    mMenuCubePlay.setCubeLocation(8, 2, 7);
                }
                mNavigator.primaryRotation.init(-270.0f, Game.vectorYaxis);
                mNavigator.secondaryRotation.init(-180.0f, Game.vectorZaxis);
                break;

            case Face_Hard04:
                if (mMenuCubePlay.location.y != 8) {
                    mMenuCubePlay.setCubeLocation(6, 8, 6);
                }
                mNavigator.primaryRotation.init(-270.0f, Game.vectorYaxis);
                mNavigator.secondaryRotation.init(-270.0f, Game.vectorZaxis);
                break;
        }
        setCubeFaceTypeFromCubeFaceName(mCurrentCubeFaceName);
    }

    public static CubeFaceNames getCubeFaceName(DifficultyEnum difficulty, int levelNumber) {
        if (levelNumber > 0 && levelNumber <= 15) {
            switch (difficulty) {
                case Easy: return CubeFaceNames.Face_Easy01;
                case Normal: return CubeFaceNames.Face_Normal01;
                case Hard: return CubeFaceNames.Face_Hard01;
            }
        } else if (levelNumber > 15 && levelNumber <= 30) {
            switch (difficulty) {
                case Easy: return CubeFaceNames.Face_Easy02;
                case Normal: return CubeFaceNames.Face_Normal02;
                case Hard: return CubeFaceNames.Face_Hard02;
            }
        } else if (levelNumber > 30 && levelNumber <= 45) {
            switch (difficulty) {
                case Easy: return CubeFaceNames.Face_Easy03;
                case Normal: return CubeFaceNames.Face_Normal03;
                case Hard: return CubeFaceNames.Face_Hard03;
            }
        } else if (levelNumber > 45 && levelNumber <= 60) {
            switch (difficulty) {
                case Easy: return CubeFaceNames.Face_Easy04;
                case Normal: return CubeFaceNames.Face_Normal04;
                case Hard: return CubeFaceNames.Face_Hard04;
            }
        }

        return CubeFaceNames.Face_Easy01;
    }

    @Override
    public void init() {
        setupCameras();
        Game.dirtyAlpha = DIRTY_ALPHA;
        if (Game.menuInitData.playMusic) {
            Audio.playMusic(MUSIC_CPU);
        }

        mMenuCubeHilite = null;
        mShowingHelp = false;
        mPrevFace = CubeFaceNames.Face_Empty;
        mIsFingerDown = false;
        mIsSwipe = false;
        mPosLightCurrent.init(mPosLight);
        mCameraCurrent.init(mCameraMenu);

        Game.resetCubes();
        Game.setupHollowCube();
        Game.buildVisibleCubesList(mCubesBase);

        if (Game.menuInitData.cubeFaceName != CubeFaceNames.Face_Menu) {
            mCurrentCubeFaceName = Game.menuInitData.cubeFaceName;
        }

        if (Game.menuInitData.reappear) {
            switch (mCurrentCubeFaceName) {
                case Face_Easy01:
                case Face_Easy02:
                case Face_Easy03:
                case Face_Easy04:
                    mNavigator.createEasyFaces();

                    if (CubeFaceNames.Face_Easy02 == mCurrentCubeFaceName || CubeFaceNames.Face_Easy03 == mCurrentCubeFaceName || CubeFaceNames.Face_Easy04 == mCurrentCubeFaceName) {
                        MenuFaceBuilder.build(CubeFaceNames.Face_Empty, Z_Plus);
                        MenuFaceBuilder.build(CubeFaceNames.Face_Empty, Z_Minus);
                    }
                    break;

                case Face_Normal01:
                case Face_Normal02:
                case Face_Normal03:
                case Face_Normal04:
                    mNavigator.createNormalFaces();

                    if (CubeFaceNames.Face_Normal02 == mCurrentCubeFaceName || CubeFaceNames.Face_Normal03 == mCurrentCubeFaceName || CubeFaceNames.Face_Normal04 == mCurrentCubeFaceName) {
                        MenuFaceBuilder.build(CubeFaceNames.Face_Empty, X_Plus);
                        MenuFaceBuilder.build(CubeFaceNames.Face_Empty, X_Minus);
                    }
                    break;

                case Face_Hard01:
                case Face_Hard02:
                case Face_Hard03:
                case Face_Hard04:
                    mNavigator.createHardFaces();

                    if (CubeFaceNames.Face_Hard02 == mCurrentCubeFaceName || CubeFaceNames.Face_Hard03 == mCurrentCubeFaceName || CubeFaceNames.Face_Hard04 == mCurrentCubeFaceName) {
                        MenuFaceBuilder.build(CubeFaceNames.Face_Empty, Z_Plus);
                        MenuFaceBuilder.build(CubeFaceNames.Face_Empty, Z_Minus);
                    }
                    break;

                default:
                    break;
            }
            setPlayCubeLocationAndMenuRotation();
            mNavigator.setup(CubeFaceNavigationEnum.NoNavigation);
        } else {
            Creator.createMovingCubesForMenu();
            mMenuCubes.clear();
            mMenuCubes.add(mMenuCubePlay);
            mMenuCubes.add(mMenuCubeOptions);
            mMenuCubes.add(mMenuCubeStore);

            mNavigator.init();
            mCurrentCubeFaceName = CubeFaceNames.Face_Tutorial;

            mNavigator.createMenuFaces(true);

            mMenuCubePlay.setCubeLocation(0, 5, 4);
            mMenuCubeOptions.setCubeLocation(1, 3, 8);
            mMenuCubeStore.setCubeLocation(1, 1, 8);
        }

        mState = StateEnum.InMenu;
        Game.buildVisibleCubesListOnlyOnFaces(mCubesFace);
    }

    private void releaseCubeTextsOnFace(int faceType) {
        int size = mCubesBase.size();
        Cube cube;
        for (int i = 0; i < size; ++i) {
            cube = mCubesBase.get(i);
            if (null != cube.fonts[faceType]) {
                Creator.cubeFontReleased(cube.fonts[faceType]);
                cube.fonts[faceType] = null;
            }
        }
    }

    private void drawMenuCubes() {
        

        Graphics.resetBufferIndices();
        Graphics.bindStreamSources3d();

        Color color = new Color(255, 255, 255, 255);
        MenuCube p = mMenuCubePlay;

        if (p.visible) {
            Graphics.addCubeSize(p.pos.x, p.pos.y, p.pos.z, HALF_CUBE_SIZE, color);
        }

        if (mCurrentCubeFaceName == CubeFaceNames.Face_Menu ||
                mCurrentCubeFaceName == CubeFaceNames.Face_Options ||
                mNavigator.isCurrentNavigation(CubeFaceNavigationEnum.Menu_To_Options) ||
                mNavigator.isCurrentNavigation(CubeFaceNavigationEnum.Menu_To_Easy1)) {
            p = mMenuCubeOptions;
            Graphics.addCubeSize(p.pos.x, p.pos.y, p.pos.z, HALF_CUBE_SIZE, color);
        }

        if (mCurrentCubeFaceName == CubeFaceNames.Face_Menu ||
                mCurrentCubeFaceName == CubeFaceNames.Face_Score ||
                mNavigator.isCurrentNavigation(CubeFaceNavigationEnum.Menu_To_Store) ||
                mNavigator.isCurrentNavigation(CubeFaceNavigationEnum.Menu_To_Easy1)) {
            p = mMenuCubeStore;
            Graphics.addCubeSize(p.pos.x, p.pos.y, p.pos.z, HALF_CUBE_SIZE, color);
     }

        Graphics.updateBuffers();
        Graphics.renderTriangles();
    }

    private void drawLevelCubes() {
        Graphics.resetBufferIndices();
        Graphics.bindStreamSources3d();

        int size;
        LevelCube levelCube;
        for (int i = 0; i < 7; ++i) {
            size = Game.cubeFacesData[i].levelCubes.size();
            for (int j = 0; j < size; ++j) {
                levelCube = Game.cubeFacesData[i].levelCubes.get(j);
                Graphics.addCube(levelCube.pos.x, levelCube.pos.y, levelCube.pos.z);
            }
        }
        Graphics.updateBuffers();
        Graphics.renderTriangles();
    }

    private void drawLevelCubeDecalsEasy(ArrayList<LevelCube> lst_level_cubes_x_plus,
                                         ArrayList<LevelCube> lst_level_cubes_x_minus,
                                         ArrayList<LevelCube> lst_level_cubes_y_plus,
                                         ArrayList<LevelCube> lst_level_cubes_y_minus,
                                         LevelCubeDecalTypeEnum decal_type) {
        
        LevelCube levelCube;
        TexCoordsQuad coords = new TexCoordsQuad();
        TexturedQuad p;
        Color color = new Color();
        int size;

        size = lst_level_cubes_x_plus.size();
        for (int i = 0; i < size; ++i) {
            levelCube = lst_level_cubes_x_plus.get(i);
            p = null;

            switch (decal_type) {
                case LevelCubeDecalNumber:
                    p = levelCube.pNumber;
                    color.init(levelCube.colorNumber);
                    break;

                case LevelCubeDecalStars:
                    p = levelCube.pStars;
                    color.init(levelCube.colorStarsAndSolver);
                    break;

                case LevelCubeDecalSolver:
                    p = levelCube.pSolver;
                    color.init(levelCube.colorStarsAndSolver);
                    break;
            }

            if (p != null) {
                coords.tx0 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                coords.tx1 = new Vector2(p.tx_up_left.x, p.tx_up_left.y);
                coords.tx2 = new Vector2(p.tx_lo_left.x, p.tx_lo_left.y);
                coords.tx3 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);

                Graphics.addCubeFace_X_Plus(levelCube.fontPos.x, levelCube.fontPos.y, levelCube.fontPos.z, coords, color);
            }
        }

        size = lst_level_cubes_x_minus.size();
        for (int i = 0; i < size; ++i) {
            levelCube = lst_level_cubes_x_minus.get(i);
            p = null;

            switch (decal_type) {
                case LevelCubeDecalNumber:
                    p = levelCube.pNumber;
                    color.init(levelCube.colorNumber);
                    break;

                case LevelCubeDecalStars:
                    p = levelCube.pStars;
                    color.init(levelCube.colorStarsAndSolver);
                    break;

                case LevelCubeDecalSolver:
                    p = levelCube.pSolver;
                    color.init(levelCube.colorStarsAndSolver);
                    break;
            }

            if (p != null) {
                coords.tx0 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                coords.tx1 = new Vector2(p.tx_up_left.x, p.tx_up_left.y);
                coords.tx2 = new Vector2(p.tx_lo_left.x, p.tx_lo_left.y);
                coords.tx3 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);

                Graphics.addCubeFace_X_Minus(levelCube.fontPos.x, levelCube.fontPos.y, levelCube.fontPos.z, coords, color);
            }
        }

        size = lst_level_cubes_y_plus.size();
        for (int i = 0; i < size; ++i) {
            levelCube = lst_level_cubes_y_plus.get(i);
            p = null;

            switch (decal_type) {
                case LevelCubeDecalNumber:
                    p = levelCube.pNumber;
                    color.init(levelCube.colorNumber);
                    break;

                case LevelCubeDecalStars:
                    p = levelCube.pStars;
                    color.init(levelCube.colorStarsAndSolver);
                    break;

                case LevelCubeDecalSolver:
                    p = levelCube.pSolver;
                    color.init(levelCube.colorStarsAndSolver);
                    break;
            }

            if (p != null) {
                coords.tx0 = new Vector2(p.tx_lo_left.x, p.tx_lo_left.y);
                coords.tx1 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);
                coords.tx2 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                coords.tx3 = new Vector2(p.tx_up_left.x, p.tx_up_left.y);

                Graphics.addCubeFace_Y_Plus(levelCube.fontPos.x, levelCube.fontPos.y, levelCube.fontPos.z, coords, color);
            }
        }

        size = lst_level_cubes_y_minus.size();
        for (int i = 0; i < size; ++i) {
            levelCube = lst_level_cubes_y_minus.get(i);
            p = null;

            switch (decal_type) {
                case LevelCubeDecalNumber:
                    p = levelCube.pNumber;
                    color.init(levelCube.colorNumber);
                    break;

                case LevelCubeDecalStars:
                    p = levelCube.pStars;
                    color.init(levelCube.colorStarsAndSolver);
                    break;

                case LevelCubeDecalSolver:
                    p = levelCube.pSolver;
                    color.init(levelCube.colorStarsAndSolver);
                    break;
            }

            if (p != null) {
                coords.tx0 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                coords.tx1 = new Vector2(p.tx_up_left.x, p.tx_up_left.y);
                coords.tx2 = new Vector2(p.tx_lo_left.x, p.tx_lo_left.y);
                coords.tx3 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);

                Graphics.addCubeFace_Y_Minus(levelCube.fontPos.x, levelCube.fontPos.y, levelCube.fontPos.z, coords, color);
            }
        }
    }

    private void drawLevelCubeDecalsNormal(ArrayList<LevelCube> lst_level_cubes_z_plus,
                                           ArrayList<LevelCube> lst_level_cubes_z_minus,
                                           ArrayList<LevelCube> lst_level_cubes_y_plus,
                                           ArrayList<LevelCube> lst_level_cubes_y_minus,
                                           LevelCubeDecalTypeEnum decal_type) {
        
        LevelCube levelCube;
        TexCoordsQuad coords = new TexCoordsQuad();
        TexturedQuad p;
        Color color = new Color();
        int size;

        size = lst_level_cubes_z_plus.size();
        for (int i = 0; i < size; ++i) {
            levelCube = lst_level_cubes_z_plus.get(i);
            p = null;

            switch (decal_type) {
                case LevelCubeDecalNumber:
                    p = levelCube.pNumber;
                    color.init(levelCube.colorNumber);
                    break;

                case LevelCubeDecalStars:
                    p = levelCube.pStars;
                    color.init(levelCube.colorStarsAndSolver);
                    break;

                case LevelCubeDecalSolver:
                    p = levelCube.pSolver;
                    color.init(levelCube.colorStarsAndSolver);
                    break;
            }

            if (p != null) {
                coords.tx0 = new Vector2(p.tx_lo_left.x, p.tx_lo_left.y);
                coords.tx1 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);
                coords.tx2 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                coords.tx3 = new Vector2(p.tx_up_left.x, p.tx_up_left.y);

                Graphics.addCubeFace_Z_Plus(levelCube.fontPos.x, levelCube.fontPos.y, levelCube.fontPos.z, coords, color);
            }
        }

        size = lst_level_cubes_z_minus.size();
        for (int i = 0; i < size; ++i) {
            levelCube = lst_level_cubes_z_minus.get(i);
            p = null;

            switch (decal_type) {
                case LevelCubeDecalNumber:
                    p = levelCube.pNumber;
                    color.init(levelCube.colorNumber);
                    break;

                case LevelCubeDecalStars:
                    p = levelCube.pStars;
                    color.init(levelCube.colorStarsAndSolver);
                    break;

                case LevelCubeDecalSolver:
                    p = levelCube.pSolver;
                    color.init(levelCube.colorStarsAndSolver);
                    break;
            }

            if (p != null) {
                coords.tx0 = new Vector2(p.tx_up_left.x, p.tx_up_left.y);
                coords.tx1 = new Vector2(p.tx_lo_left.x, p.tx_lo_left.y);
                coords.tx2 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);
                coords.tx3 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);

                Graphics.addCubeFace_Z_Minus(levelCube.fontPos.x, levelCube.fontPos.y, levelCube.fontPos.z, coords, color);
            }
        }

        size = lst_level_cubes_y_plus.size();
        for (int i = 0; i < size; ++i) {
            levelCube = lst_level_cubes_y_plus.get(i);
            p = null;

            switch (decal_type) {
                case LevelCubeDecalNumber:
                    p = levelCube.pNumber;
                    color.init(levelCube.colorNumber);
                    break;

                case LevelCubeDecalStars:
                    p = levelCube.pStars;
                    color.init(levelCube.colorStarsAndSolver);
                    break;

                case LevelCubeDecalSolver:
                    p = levelCube.pSolver;
                    color.init(levelCube.colorStarsAndSolver);
                    break;
            }

            if (p != null) {
                coords.tx0 = new Vector2(p.tx_up_left.x, p.tx_up_left.y);
                coords.tx1 = new Vector2(p.tx_lo_left.x, p.tx_lo_left.y);
                coords.tx2 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);
                coords.tx3 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);

                Graphics.addCubeFace_Y_Plus(levelCube.fontPos.x, levelCube.fontPos.y, levelCube.fontPos.z, coords, color);
            }
        }

        size = lst_level_cubes_y_minus.size();
        for (int i = 0; i < size; ++i) {
            levelCube = lst_level_cubes_y_minus.get(i);
            p = null;

            switch (decal_type) {
                case LevelCubeDecalNumber:
                    p = levelCube.pNumber;
                    color.init(levelCube.colorNumber);
                    break;

                case LevelCubeDecalStars:
                    p = levelCube.pStars;
                    color.init(levelCube.colorStarsAndSolver);
                    break;

                case LevelCubeDecalSolver:
                    p = levelCube.pSolver;
                    color.init(levelCube.colorStarsAndSolver);
                    break;
            }

            if (p != null) {
                coords.tx0 = new Vector2(p.tx_up_left.x, p.tx_up_left.y);
                coords.tx1 = new Vector2(p.tx_lo_left.x, p.tx_lo_left.y);
                coords.tx2 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);
                coords.tx3 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);

                Graphics.addCubeFace_Y_Minus(levelCube.fontPos.x, levelCube.fontPos.y, levelCube.fontPos.z, coords, color);
            }
        }
    }

    private void drawLevelCubeDecalsHard(ArrayList<LevelCube> lst_level_cubes_x_plus,
                                         ArrayList<LevelCube> lst_level_cubes_x_minus,
                                         ArrayList<LevelCube> lst_level_cubes_y_plus,
                                         ArrayList<LevelCube> lst_level_cubes_y_minus,
                                         LevelCubeDecalTypeEnum decal_type) {
        
        LevelCube levelCube;
        TexCoordsQuad coords = new TexCoordsQuad();
        TexturedQuad p;
        Color color = new Color();
        int size;

        size = lst_level_cubes_x_plus.size();
        for (int i = 0; i < size; ++i) {
            levelCube = lst_level_cubes_x_plus.get(i);
            p = null;

            switch (decal_type) {
                case LevelCubeDecalNumber:
                    p = levelCube.pNumber;
                    color.init(levelCube.colorNumber);
                    break;

                case LevelCubeDecalStars:
                    p = levelCube.pStars;
                    color.init(levelCube.colorStarsAndSolver);
                    break;

                case LevelCubeDecalSolver:
                    p = levelCube.pSolver;
                    color.init(levelCube.colorStarsAndSolver);
                    break;
            }

            if (p != null) {
                coords.tx0 = new Vector2(p.tx_lo_left.x, p.tx_lo_left.y);
                coords.tx1 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);
                coords.tx2 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                coords.tx3 = new Vector2(p.tx_up_left.x, p.tx_up_left.y);

                Graphics.addCubeFace_X_Plus(levelCube.fontPos.x, levelCube.fontPos.y, levelCube.fontPos.z, coords, color);
            }
        }

        size = lst_level_cubes_x_minus.size();
        for (int i = 0; i < size; ++i) {
            levelCube = lst_level_cubes_x_minus.get(i);
            p = null;

            switch (decal_type) {
                case LevelCubeDecalNumber:
                    p = levelCube.pNumber;
                    color.init(levelCube.colorNumber);
                    break;

                case LevelCubeDecalStars:
                    p = levelCube.pStars;
                    color.init(levelCube.colorStarsAndSolver);
                    break;

                case LevelCubeDecalSolver:
                    p = levelCube.pSolver;
                    color.init(levelCube.colorStarsAndSolver);
                    break;
            }

            if (p != null) {
                coords.tx0 = new Vector2(p.tx_lo_left.x, p.tx_lo_left.y);
                coords.tx1 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);
                coords.tx2 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                coords.tx3 = new Vector2(p.tx_up_left.x, p.tx_up_left.y);

                Graphics.addCubeFace_X_Minus(levelCube.fontPos.x, levelCube.fontPos.y, levelCube.fontPos.z, coords, color);
            }
        }

        size = lst_level_cubes_y_plus.size();
        for (int i = 0; i < size; ++i) {
            levelCube = lst_level_cubes_y_plus.get(i);
            p = null;

            switch (decal_type) {
                case LevelCubeDecalNumber:
                    p = levelCube.pNumber;
                    color.init(levelCube.colorNumber);
                    break;

                case LevelCubeDecalStars:
                    p = levelCube.pStars;
                    color.init(levelCube.colorStarsAndSolver);
                    break;

                case LevelCubeDecalSolver:
                    p = levelCube.pSolver;
                    color.init(levelCube.colorStarsAndSolver);
                    break;
            }

            if (p != null) {
                coords.tx0 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                coords.tx1 = new Vector2(p.tx_up_left.x, p.tx_up_left.y);
                coords.tx2 = new Vector2(p.tx_lo_left.x, p.tx_lo_left.y);
                coords.tx3 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);

                Graphics.addCubeFace_Y_Plus(levelCube.fontPos.x, levelCube.fontPos.y, levelCube.fontPos.z, coords, color);
            }
        }

        size = lst_level_cubes_y_minus.size();
        for (int i = 0; i < size; ++i) {
            levelCube = lst_level_cubes_y_minus.get(i);
            p = null;

            switch (decal_type) {
                case LevelCubeDecalNumber:
                    p = levelCube.pNumber;
                    color.init(levelCube.colorNumber);
                    break;

                case LevelCubeDecalStars:
                    p = levelCube.pStars;
                    color.init(levelCube.colorStarsAndSolver);
                    break;

                case LevelCubeDecalSolver:
                    p = levelCube.pSolver;
                    color.init(levelCube.colorStarsAndSolver);
                    break;
            }

            if (p != null) {
                coords.tx0 = new Vector2(p.tx_lo_left.x, p.tx_lo_left.y);
                coords.tx1 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);
                coords.tx2 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                coords.tx3 = new Vector2(p.tx_up_left.x, p.tx_up_left.y);

                Graphics.addCubeFace_Y_Minus(levelCube.fontPos.x, levelCube.fontPos.y, levelCube.fontPos.z, coords, color);
            }
        }
    }

    private void drawLevelNumbers() {
        
        Graphics.resetBufferIndices();
        Graphics.bindStreamSources3d();

        switch (mCurrentCubeFaceName) {
            case Face_Easy01:
            case Face_Easy02:
            case Face_Easy03:
            case Face_Easy04:
                drawLevelCubeDecalsEasy(Game.cubeFacesData[X_Plus].levelCubes,
                        Game.cubeFacesData[X_Minus].levelCubes,
                        Game.cubeFacesData[Y_Plus].levelCubes,
                        Game.cubeFacesData[Y_Minus].levelCubes,
                        LevelCubeDecalTypeEnum.LevelCubeDecalNumber);
                break;

            case Face_Normal01:
            case Face_Normal02:
            case Face_Normal03:
            case Face_Normal04:
                drawLevelCubeDecalsNormal(Game.cubeFacesData[Z_Plus].levelCubes,
                        Game.cubeFacesData[Z_Minus].levelCubes,
                        Game.cubeFacesData[Y_Plus].levelCubes,
                        Game.cubeFacesData[Y_Minus].levelCubes,
                        LevelCubeDecalTypeEnum.LevelCubeDecalNumber);
                break;

            case Face_Hard01:
            case Face_Hard02:
            case Face_Hard03:
            case Face_Hard04:
                drawLevelCubeDecalsHard(Game.cubeFacesData[X_Plus].levelCubes,
                        Game.cubeFacesData[X_Minus].levelCubes,
                        Game.cubeFacesData[Y_Plus].levelCubes,
                        Game.cubeFacesData[Y_Minus].levelCubes,
                        LevelCubeDecalTypeEnum.LevelCubeDecalNumber);
                break;

            default:
                break;
        }
        Graphics.updateBuffers();
        Graphics.renderTriangles();
    }

    private void drawTexts() {
        
        Graphics.resetBufferIndices();

        if (Math.abs(mNavigator.secondaryRotation.degree) < EPSILON) {
            drawTextsDefaultOrientation(mTexts.get(X_Plus),
                    mTexts.get(X_Minus),
                    mTexts.get(Y_Plus),
                    mTexts.get(Y_Minus),
                    mTexts.get(Z_Plus),
                    mTexts.get(Z_Minus));
        } else {
            switch (mCurrentCubeFaceName) {
                case Face_Easy01:
                case Face_Easy02:
                case Face_Easy03:
                case Face_Easy04:
                    drawEasyTitles(mTexts.get(X_Plus), mTexts.get(X_Minus),
                            mTexts.get(Y_Plus), mTexts.get(Y_Minus),
                            mTexts.get(Z_Plus), mTexts.get(Z_Minus));
                    break;

                case Face_Normal01:
                case Face_Normal02:
                case Face_Normal03:
                case Face_Normal04:
                    drawNormalTitles(mTexts.get(X_Plus), mTexts.get(X_Minus),
                            mTexts.get(Y_Plus), mTexts.get(Y_Minus),
                            mTexts.get(Z_Plus), mTexts.get(Z_Minus));
                    break;

                case Face_Hard01:
                case Face_Hard02:
                case Face_Hard03:
                case Face_Hard04:
                    drawHardTitles(mTexts.get(X_Plus), mTexts.get(X_Minus),
                            mTexts.get(Y_Plus), mTexts.get(Y_Minus),
                            mTexts.get(Z_Plus), mTexts.get(Z_Minus));
                    break;

                default:
                    break;
            }
        }

        CubeFont cubeFont;
        TexturedQuad font;
        TexCoordsQuad coords = new TexCoordsQuad();

        // Draw CubeFonts on Red Cubes [P]lay [O]ptions [S]tore [U]nlock [R]estore
        if (mNavigator.isCurrentNavigation(CubeFaceNavigationEnum.Menu_To_Easy1) || mCurrentCubeFaceName == CubeFaceNames.Face_Menu || mCurrentCubeFaceName == CubeFaceNames.Face_Options || mCurrentCubeFaceName == CubeFaceNames.Face_Score) {

            Color color = new Color(Game.fontColorOnMenuCube);

            if (mMenuCubePlay.isDone() && mMenuCubePlay.location.x == 1) {
                cubeFont = m_cubefont_play;
                font = cubeFont.texturedQuad;

                if (font != null) {
                    coords.tx0 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
                    coords.tx1 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
                    coords.tx2 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
                    coords.tx3 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);

                    Graphics.addCubeFace_Z_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
                }
            }

            if (mMenuCubeOptions.isDone() && mMenuCubeOptions.location.x == 1) {
                cubeFont = m_cubefont_options;
                font = cubeFont.texturedQuad;

                if (font != null) {
                    coords.tx0 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
                    coords.tx1 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
                    coords.tx2 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
                    coords.tx3 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);

                    Graphics.addCubeFace_Z_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
                }
            }

            if (mMenuCubeStore.isDone() && mMenuCubeStore.location.x == 1) {
                cubeFont = m_cubefont_store;
                font = cubeFont.texturedQuad;

                if (font != null) {
                    coords.tx0 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
                    coords.tx1 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
                    coords.tx2 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
                    coords.tx3 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);

                    Graphics.addCubeFace_Z_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
                }
            }
        }

        Graphics.updateBuffers();
        Graphics.renderTriangles();
    }

    private void drawTextsTitles() {
        
        Graphics.resetBufferIndices();

        if (Math.abs(mNavigator.secondaryRotation.degree) < EPSILON) {
            drawTextsDefaultOrientation(mTitles.get(X_Plus), mTitles.get(X_Minus),
                    mTitles.get(Y_Plus), mTitles.get(Y_Minus),
                    mTitles.get(Z_Plus), mTitles.get(Z_Minus));
        } else {
            switch (mCurrentCubeFaceName) {
                case Face_Easy01:
                case Face_Easy02:
                case Face_Easy03:
                case Face_Easy04:
                    drawEasyTitles(mTitles.get(X_Plus), mTitles.get(X_Minus),
                            mTitles.get(Y_Plus), mTitles.get(Y_Minus),
                            mTitles.get(Z_Plus), mTitles.get(Z_Minus));
                    break;

                case Face_Normal01:
                case Face_Normal02:
                case Face_Normal03:
                case Face_Normal04:
                    drawNormalTitles(mTitles.get(X_Plus), mTitles.get(X_Minus),
                            mTitles.get(Y_Plus), mTitles.get(Y_Minus),
                            mTitles.get(Z_Plus), mTitles.get(Z_Minus));
                    break;

                case Face_Hard01:
                case Face_Hard02:
                case Face_Hard03:
                case Face_Hard04:
                    drawHardTitles(mTitles.get(X_Plus), mTitles.get(X_Minus),
                            mTitles.get(Y_Plus), mTitles.get(Y_Minus),
                            mTitles.get(Z_Plus), mTitles.get(Z_Minus));
                    break;

                default:
                    break;
            }
        }
        Graphics.updateBuffers();
        Graphics.renderTriangles();
    }

    private void drawTextsDefaultOrientation(ArrayList<CubeFont> lst_x_plus,
                                             ArrayList<CubeFont> lst_x_minus,
                                             ArrayList<CubeFont> lst_y_plus,
                                             ArrayList<CubeFont> lst_y_minus,
                                             ArrayList<CubeFont> lst_z_plus,
                                             ArrayList<CubeFont> lst_z_minus) {
        
        CubeFont cubeFont;
        TexturedQuad font;
        TexCoordsQuad coords = new TexCoordsQuad();
        int size;

        size = lst_z_plus.size();
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_z_plus.get(i);
            font = cubeFont.texturedQuad;

            coords.tx0 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx1 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx2 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx3 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);

            Graphics.addCubeFace_Z_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        size = lst_x_plus.size();
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_x_plus.get(i);
            font = cubeFont.texturedQuad;

            coords.tx0 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx1 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx2 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx3 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);

            Graphics.addCubeFace_X_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        size = lst_z_minus.size();
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_z_minus.get(i);
            font = cubeFont.texturedQuad;

            coords.tx0 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx1 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx2 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx3 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);

            Graphics.addCubeFace_Z_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        size = lst_x_minus.size();
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_x_minus.get(i);
            font = cubeFont.texturedQuad;

            coords.tx0 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx1 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx2 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx3 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);

            Graphics.addCubeFace_X_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        size = lst_y_plus.size();
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_y_plus.get(i);
            font = cubeFont.texturedQuad;

            coords.tx0 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx1 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx2 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx3 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);

            Graphics.addCubeFace_Y_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        size = lst_y_minus.size();
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_y_minus.get(i);
            font = cubeFont.texturedQuad;

            coords.tx0 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx1 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx2 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx3 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);

            Graphics.addCubeFace_Y_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }
    }

    private void drawEasyTitles(ArrayList<CubeFont> lst_x_plus,
                                ArrayList<CubeFont> lst_x_minus,
                                ArrayList<CubeFont> lst_y_plus,
                                ArrayList<CubeFont> lst_y_minus,
                                ArrayList<CubeFont> lst_z_plus,
                                ArrayList<CubeFont> lst_z_minus) {
        
        CubeFont cubeFont;
        TexturedQuad font;
        TexCoordsQuad coords = new TexCoordsQuad();
        int size;

        size = lst_x_plus.size();
        // Face_X_Plus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_x_plus.get(i);
            font = cubeFont.texturedQuad;

            coords.tx0 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx1 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx2 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx3 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);

            Graphics.addCubeFace_X_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        size = lst_y_minus.size();
        // Face_Y_Minus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_y_minus.get(i);
            font = cubeFont.texturedQuad;

            coords.tx0 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx1 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx2 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx3 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);

            Graphics.addCubeFace_Y_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        size = lst_x_minus.size();
        // Face_X_Minus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_x_minus.get(i);
            font = cubeFont.texturedQuad;

            coords.tx0 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx1 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx2 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx3 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);

            Graphics.addCubeFace_X_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        size = lst_y_plus.size();
        // Face_Y_Plus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_y_plus.get(i);
            font = cubeFont.texturedQuad;

            coords.tx0 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx1 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx2 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx3 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);

            Graphics.addCubeFace_Y_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }
    }

    private void drawNormalTitles(ArrayList<CubeFont> lst_x_plus,
                                 ArrayList<CubeFont> lst_x_minus,
                                 ArrayList<CubeFont> lst_y_plus,
                                 ArrayList<CubeFont> lst_y_minus,
                                 ArrayList<CubeFont> lst_z_plus,
                                 ArrayList<CubeFont> lst_z_minus) {
        
        CubeFont cubeFont;
        TexturedQuad font;
        TexCoordsQuad coords = new TexCoordsQuad();
        int size;

        size = lst_z_minus.size();
        // Face_Z_Minus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_z_minus.get(i);
            font = cubeFont.texturedQuad;

            coords.tx0 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx1 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx2 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx3 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);

            Graphics.addCubeFace_Z_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        size = lst_y_minus.size();
        // Face_Y_Minus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_y_minus.get(i);
            font = cubeFont.texturedQuad;

            coords.tx0 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx1 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx2 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx3 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);

            Graphics.addCubeFace_Y_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        size = lst_z_plus.size();
        // Face_Z_Plus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_z_plus.get(i);
            font = cubeFont.texturedQuad;

            coords.tx0 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx1 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx2 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx3 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);

            Graphics.addCubeFace_Z_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        size = lst_y_plus.size();
        // Face_Y_Plus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_y_plus.get(i);
            font = cubeFont.texturedQuad;

            coords.tx0 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx1 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx2 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx3 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);

            Graphics.addCubeFace_Y_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }
    }

    private void drawHardTitles(ArrayList<CubeFont> lst_x_plus,
                               ArrayList<CubeFont> lst_x_minus,
                               ArrayList<CubeFont> lst_y_plus,
                               ArrayList<CubeFont> lst_y_minus,
                               ArrayList<CubeFont> lst_z_plus,
                               ArrayList<CubeFont> lst_z_minus) {
        
        CubeFont cubeFont;
        TexturedQuad font;
        TexCoordsQuad coords = new TexCoordsQuad();
        int size;

        size = lst_x_minus.size();
        // Face_X_Minus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_x_minus.get(i);
            font = cubeFont.texturedQuad;

            coords.tx0 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx1 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx2 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx3 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);

            Graphics.addCubeFace_X_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        size = lst_y_minus.size();
        // Face_Y_Minus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_y_minus.get(i);
            font = cubeFont.texturedQuad;

            coords.tx0 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx1 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx2 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx3 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);

            Graphics.addCubeFace_Y_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        size = lst_x_plus.size();
        // Face_X_Plus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_x_plus.get(i);
            font = cubeFont.texturedQuad;

            coords.tx0 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx1 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx2 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx3 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);

            Graphics.addCubeFace_X_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        size = lst_y_plus.size();
        // Face_Y_Plus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_y_plus.get(i);
            font = cubeFont.texturedQuad;

            coords.tx0 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx1 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx2 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx3 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);

            Graphics.addCubeFace_Y_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }
    }

    private void drawSymbols() {
        
        Graphics.resetBufferIndices();
        Graphics.bindStreamSources3d();

        drawTextsDefaultOrientation(mSymbols.get(X_Plus),
                mSymbols.get(X_Minus),
                mSymbols.get(Y_Plus),
                mSymbols.get(Y_Minus),
                mSymbols.get(Z_Plus),
                mSymbols.get(Z_Minus));

        Graphics.updateBuffers();
        Graphics.renderTriangles();
    }

    private void drawCubeFaceOptions() {
        
        Cube p = Game.cubes[2][7][3];

        float x = p.tx - HALF_CUBE_SIZE;
        float y = p.ty + HALF_CUBE_SIZE + 0.02f;
        float z = p.tz - HALF_CUBE_SIZE;
        float w = CUBE_SIZE * 5.0f * GameOptions.getMusicVolume();
        float ws = CUBE_SIZE * 5.0f * GameOptions.getSoundVolume();
        float zs = Game.cubes[2][7][6].tz - HALF_CUBE_SIZE;

        final float verts[] = {
            // music
            x, y, z + CUBE_SIZE,
            x + w, y, z + CUBE_SIZE,
            x + w, y, z,
            x, y, z,

            // sound
            x, y, zs + CUBE_SIZE,
            x + ws, y, zs + CUBE_SIZE,
            x + ws, y, zs,
            x, y, zs
        };

        final float norms[] = {
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,

            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f
        };

        byte c76 = (byte)76;
        byte c140 = (byte)140;
        byte c220 = (byte)220;

        final byte colors[] = {
            c76, 0, 0, c140,
            c76, 0, 0, c220,
            c76, 0, 0, c220,
            c76, 0, 0, c140,

            c76, 0, 0, c140,
            c76, 0, 0, c220,
            c76, 0, 0, c220,
            c76, 0, 0, c140,
        };

        final float coords[] = {};

        Graphics.bindStreamSources3dNoTexture();
        Graphics.resetBufferIndices();
        Graphics.addVerticesCoordsNormalsColors(verts, coords, norms, colors);

        glDisable(GL_TEXTURE_2D);

        glDrawArrays(GL_TRIANGLE_FAN, 0, 4); // progress bar music
        glDrawArrays(GL_TRIANGLE_FAN, 4, 4); // progress bar soundfx

        glEnable(GL_TEXTURE_2D);
    }

    private void drawLevelCubeSymbols() {
        Graphics.resetBufferIndices();
        Graphics.bindStreamSources3d();

        switch (mCurrentCubeFaceName) {
            case Face_Easy01:
            case Face_Easy02:
            case Face_Easy03:
            case Face_Easy04:
                drawLevelCubeDecalsEasy(Game.cubeFacesData[X_Plus].levelCubes,
                        Game.cubeFacesData[X_Minus].levelCubes,
                        Game.cubeFacesData[Y_Plus].levelCubes,
                        Game.cubeFacesData[Y_Minus].levelCubes,
                        LevelCubeDecalTypeEnum.LevelCubeDecalStars);

                drawLevelCubeDecalsEasy(Game.cubeFacesData[X_Plus].levelCubes,
                        Game.cubeFacesData[X_Minus].levelCubes,
                        Game.cubeFacesData[Y_Plus].levelCubes,
                        Game.cubeFacesData[Y_Minus].levelCubes,
                        LevelCubeDecalTypeEnum.LevelCubeDecalSolver);
                break;

            case Face_Normal01:
            case Face_Normal02:
            case Face_Normal03:
            case Face_Normal04:
                drawLevelCubeDecalsNormal(Game.cubeFacesData[Z_Plus].levelCubes,
                        Game.cubeFacesData[Z_Minus].levelCubes,
                        Game.cubeFacesData[Y_Plus].levelCubes,
                        Game.cubeFacesData[Y_Minus].levelCubes,
                        LevelCubeDecalTypeEnum.LevelCubeDecalStars);

                drawLevelCubeDecalsNormal(Game.cubeFacesData[Z_Plus].levelCubes,
                        Game.cubeFacesData[Z_Minus].levelCubes,
                        Game.cubeFacesData[Y_Plus].levelCubes,
                        Game.cubeFacesData[Y_Minus].levelCubes,
                        LevelCubeDecalTypeEnum.LevelCubeDecalSolver);
                break;

            case Face_Hard01:
            case Face_Hard02:
            case Face_Hard03:
            case Face_Hard04:
                drawLevelCubeDecalsHard(Game.cubeFacesData[X_Plus].levelCubes,
                        Game.cubeFacesData[X_Minus].levelCubes,
                        Game.cubeFacesData[Y_Plus].levelCubes,
                        Game.cubeFacesData[Y_Minus].levelCubes,
                        LevelCubeDecalTypeEnum.LevelCubeDecalStars);

                drawLevelCubeDecalsHard(Game.cubeFacesData[X_Plus].levelCubes,
                        Game.cubeFacesData[X_Minus].levelCubes,
                        Game.cubeFacesData[Y_Plus].levelCubes,
                        Game.cubeFacesData[Y_Minus].levelCubes,
                        LevelCubeDecalTypeEnum.LevelCubeDecalSolver);
                break;

            default:
                break;
        }
        Graphics.updateBuffers();
        Graphics.renderTriangles();
    }

    private void drawCredits() {
        
        final float verts[] = {
            -0.5f, -0.5f, 0.0f,
             0.5f, -0.5f, 0.0f,
             0.5f,  0.5f, 0.0f,
            -0.5f,  0.5f, 0.0f
        };

        final float coords[] = {
            0f,   1f,
            1f,   1f,
            1f,   0f,
            0f,   0f
        };

        final float norms[] = {
            0f, 0f, 1f,
            0f, 0f, 1f,
            0f, 0f, 1f,
            0f, 0f, 1f
        };

        final byte maxColor = (byte)255;

        final byte colors_white[] = {
            maxColor, maxColor, maxColor, maxColor,
            maxColor, maxColor, maxColor, maxColor,
            maxColor, maxColor, maxColor, maxColor,
            maxColor, maxColor, maxColor, maxColor,
        };

        final byte colors_black[] = {
            0, 0, 0, maxColor,
            0, 0, 0, maxColor,
            0, 0, 0, maxColor,
            0, 0, 0, maxColor,
        };

        Graphics.addVerticesCoordsNormalsColors(verts, coords, norms, colors_white);
        glPushMatrix();
            glTranslatef(0.0f, -5.7f, m_credits_offset);
            glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
            glScalef(11.0f, 11.0f, 1.0f);
            glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
        glPopMatrix();

        Graphics.addVerticesCoordsNormalsColors(verts, coords, norms, colors_black);
        glPushMatrix();
            glTranslatef(0.0f, -5.6f, -10.0f);
            glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
            glScalef(11.0f, 11.0f, 1.0f);
            glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
        glPopMatrix();
    }

    private void drawTheCube() {
        
        int size;
        Cube cube;

        Graphics.resetBufferIndices();
        Graphics.zeroBufferPositions();
        Graphics.bindStreamSources3d();
        size = mCubesBase.size();
        for (int i = 0; i < size; ++i) {
            cube = mCubesBase.get(i);
            Graphics.addCubeWithColor(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
        }
        Graphics.updateBuffers();
        Graphics.renderTriangles(Game.cubeOffset.x, Game.cubeOffset.y, Game.cubeOffset.z);

        Graphics.resetBufferIndices();
        size = mCubesFace.size();
        for (int i = 0; i < size; ++i) {
            cube = mCubesFace.get(i);
            Graphics.addCubeWithColor(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
        }
        Graphics.updateBuffers();
        Graphics.renderTriangles(Game.cubeOffset.x, Game.cubeOffset.y, Game.cubeOffset.z);
    }

    private void drawCubeHiLite(Color color) {
        
        TexCoordsQuad coords = new TexCoordsQuad();

        TexturedQuad p = m_font_hilite.texturedQuad;
        coords.tx0 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
        coords.tx1 = new Vector2(p.tx_up_left.x, p.tx_up_left.y);
        coords.tx2 = new Vector2(p.tx_lo_left.x, p.tx_lo_left.y);
        coords.tx3 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);

        Graphics.resetBufferIndices();
        Graphics.bindStreamSources3d();

        switch (mCurrentCubeFaceType) {
            case X_Plus:
                Graphics.addCubeFace_X_Plus(m_font_hilite.pos.x, m_font_hilite.pos.y, m_font_hilite.pos.z, coords, color);
                break;

            case X_Minus:
                Graphics.addCubeFace_X_Minus(m_font_hilite.pos.x, m_font_hilite.pos.y, m_font_hilite.pos.z, coords, color);
                break;

            case Y_Plus:
                Graphics.addCubeFace_Y_Plus(m_font_hilite.pos.x, m_font_hilite.pos.y, m_font_hilite.pos.z, coords, color);
                break;

            case Y_Minus:
                Graphics.addCubeFace_Y_Minus(m_font_hilite.pos.x, m_font_hilite.pos.y, m_font_hilite.pos.z, coords, color);
                break;

            case Z_Plus:
                Graphics.addCubeFace_Z_Plus(m_font_hilite.pos.x, m_font_hilite.pos.y, m_font_hilite.pos.z, coords, color);
                break;

            case Z_Minus:
                Graphics.addCubeFace_Z_Minus(m_font_hilite.pos.x, m_font_hilite.pos.y, m_font_hilite.pos.z, coords, color);
                break;

            default:
                break;
        }
        Graphics.updateBuffers();
        Graphics.renderTriangles();
    }

    private void updateAnimToCredits() {
        if (m_t >= 1.0f) {
            mState = StateEnum.InCredits;
            return;
        }

        m_t += 0.05f;
        if (m_t > 1.0f) {
            m_t = 1.0f;
        }

        Utils.lerpCamera(mCameraMenu, mCameraCredits, m_t, mCameraCurrent);
        Game.dirtyAlpha = Utils.lerp(DIRTY_ALPHA, 0f, m_t);

        mMenuCubeStore.update();
    }

    private void updateAnimFromCredits() {
        if (m_t >= 1.0f) {
            mState = StateEnum.InMenu;
            return;
        }

        m_t += 0.05f;
        if (m_t > 1.0f) {
            m_t = 1.0f;
        }

        Utils.lerpCamera(mCameraCredits, mCameraMenu, m_t, mCameraCurrent);
        Game.dirtyAlpha = Utils.lerp(0f, DIRTY_ALPHA, m_t);
    }

    private void updatePlayCube() {
        if (mMenuCubePlay.isDone()) {
            switch (mCurrentCubeFaceName) {
                case Face_Tutorial:
                    if (mMenuCubePlay.location.z == 8) {
                        mCurrentCubeFaceName = CubeFaceNames.Face_Menu;
                        mMenuCubePlay.moveOnAxis(X_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Tutorial_To_Menu);
                    }

                    if (mMenuCubePlay.location.y == 7 && m_can_alter_text) {
                        m_can_alter_text = false;
                        MenuFaceBuilder.resetTransforms();
                        MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
                        releaseCubeTextsOnFace(X_Minus);
                        MenuFaceBuilder.buildTexts(CubeFaceNames.Face_Tutorial, X_Minus, true);
                    }

                    if (mMenuCubePlay.location.y == 1 && m_can_alter_text) {
                        m_can_alter_text = false;
                        MenuFaceBuilder.resetTransforms();
                        MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
                        releaseCubeTextsOnFace(X_Minus);
                        MenuFaceBuilder.buildTexts(CubeFaceNames.Face_Tutorial, X_Minus, false);
                    }
                    break;

                case Face_Menu:
                    if (mMenuCubePlay.location.x == 2 && mMenuCubePlay.location.y == 7 && mMenuCubePlay.location.z == 8) {
                        mMenuCubePlay.moveOnAxis(Y_Minus);
                    }

                    if (mMenuCubePlay.location.x == 2 && mMenuCubePlay.location.y == 5 && mMenuCubePlay.location.z == 8) {
                        mMenuCubePlay.moveOnAxis(X_Minus);
                    }

                    if (mMenuCubePlay.location.x == 8) {
                        mCurrentCubeFaceName = CubeFaceNames.Face_Easy01;
                        mMenuCubePlay.moveOnAxis(Z_Minus);
                        mNavigator.setup(CubeFaceNavigationEnum.Menu_To_Easy1);
                    }
                    break;

                case Face_Easy01:
                    if (mMenuCubePlay.location.z == 0) {
                        mCurrentCubeFaceName = CubeFaceNames.Face_Normal01;
                        mMenuCubePlay.moveOnAxis(X_Minus);
                        mNavigator.setup(CubeFaceNavigationEnum.Easy1_To_Normal1);
                    }

                    if (mMenuCubePlay.location.z == 8) {
                        mCurrentCubeFaceName = CubeFaceNames.Face_Menu;
                        mMenuCubePlay.moveOnAxis(X_Minus);
                        mNavigator.setup(CubeFaceNavigationEnum.Easy1_To_Menu);
                    }

                    if (mMenuCubePlay.location.y == 0) {
                        mCurrentCubeFaceName = CubeFaceNames.Face_Easy02;
                        mMenuCubePlay.moveOnAxis(X_Minus);
                        mNavigator.setup(CubeFaceNavigationEnum.Easy1_To_Easy2);
                    }

                    if (mMenuCubePlay.location.y == 8) {
                        mCurrentCubeFaceName = CubeFaceNames.Face_Easy04;
                        mMenuCubePlay.moveOnAxis(X_Minus);
                        mNavigator.setup(CubeFaceNavigationEnum.Easy1_To_Easy4);
                    }
                    break;

                case Face_Easy02:
                    if (mMenuCubePlay.location.x == 8) {
                        mCurrentCubeFaceName = CubeFaceNames.Face_Easy01;
                        mMenuCubePlay.moveOnAxis(Y_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Easy2_To_Easy1);
                    }

                    if (mMenuCubePlay.location.x == 0) {
                        mCurrentCubeFaceName = CubeFaceNames.Face_Easy03;
                        mMenuCubePlay.moveOnAxis(Y_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Easy2_To_Easy3);
                    }
                    break;

                case Face_Easy03:
                    if (mMenuCubePlay.location.y == 0) {
                        mCurrentCubeFaceName = CubeFaceNames.Face_Easy02;
                        mMenuCubePlay.moveOnAxis(X_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Easy3_To_Easy2);
                    }

                    if (mMenuCubePlay.location.y == 8) {
                        mCurrentCubeFaceName = CubeFaceNames.Face_Easy04;
                        mMenuCubePlay.moveOnAxis(X_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Easy3_To_Easy4);
                    }
                    break;

                case Face_Easy04:
                    if (mMenuCubePlay.location.x == 8) {
                        mCurrentCubeFaceName = CubeFaceNames.Face_Easy01;
                        mMenuCubePlay.moveOnAxis(Y_Minus);
                        mNavigator.setup(CubeFaceNavigationEnum.Easy4_To_Easy1);
                    }

                    if (mMenuCubePlay.location.x == 0) {
                        mCurrentCubeFaceName = CubeFaceNames.Face_Easy03;
                        mMenuCubePlay.moveOnAxis(Y_Minus);
                        mNavigator.setup(CubeFaceNavigationEnum.Easy4_To_Easy3);
                    }
                    break;

                case Face_Normal01:
                    if (mMenuCubePlay.location.x == 8) {
                        mCurrentCubeFaceName = CubeFaceNames.Face_Easy01;
                        mMenuCubePlay.moveOnAxis(Z_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Normal1_To_Easy1);
                    }

                    if (mMenuCubePlay.location.x == 0) {
                        mCurrentCubeFaceName = CubeFaceNames.Face_Hard01;
                        mMenuCubePlay.moveOnAxis(Z_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Normal1_To_Hard1);
                    }

                    if (mMenuCubePlay.location.y == 0) {
                        mCurrentCubeFaceName = CubeFaceNames.Face_Normal02;
                        mMenuCubePlay.moveOnAxis(Z_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Normal1_To_Normal2);
                    }

                    if (mMenuCubePlay.location.y == 8) {
                        mCurrentCubeFaceName = CubeFaceNames.Face_Normal04;
                        mMenuCubePlay.moveOnAxis(Z_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Normal1_To_Normal4);
                    }
                    break;

                case Face_Normal02:
                    if (mMenuCubePlay.location.z == 0) {
                        mCurrentCubeFaceName = CubeFaceNames.Face_Normal01;
                        mMenuCubePlay.moveOnAxis(Y_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Normal2_To_Normal1);
                    }

                    if (mMenuCubePlay.location.z == 8) {
                        mCurrentCubeFaceName = CubeFaceNames.Face_Normal03;
                        mMenuCubePlay.moveOnAxis(Y_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Normal2_To_Normal3);
                    }
                    break;

                case Face_Normal03:
                    if (mMenuCubePlay.location.y == 0) {
                        mCurrentCubeFaceName = CubeFaceNames.Face_Normal02;
                        mMenuCubePlay.moveOnAxis(Z_Minus);
                        mNavigator.setup(CubeFaceNavigationEnum.Normal3_To_Normal2);
                    }

                    if (mMenuCubePlay.location.y == 8) {
                        mCurrentCubeFaceName = CubeFaceNames.Face_Normal04;
                        mMenuCubePlay.moveOnAxis(Z_Minus);
                        mNavigator.setup(CubeFaceNavigationEnum.Normal3_To_Normal4);
                    }
                    break;

                case Face_Normal04:
                    if (mMenuCubePlay.location.z == 8) {
                        mCurrentCubeFaceName = CubeFaceNames.Face_Normal03;
                        mMenuCubePlay.moveOnAxis(Y_Minus);
                        mNavigator.setup(CubeFaceNavigationEnum.Normal4_To_Normal3);
                    }

                    if (mMenuCubePlay.location.z == 0) {
                        mCurrentCubeFaceName = CubeFaceNames.Face_Normal01;
                        mMenuCubePlay.moveOnAxis(Y_Minus);
                        mNavigator.setup(CubeFaceNavigationEnum.Normal4_To_Normal1);
                    }
                    break;

                case Face_Hard01:
                    if (mMenuCubePlay.location.z == 0) {
                        mCurrentCubeFaceName = CubeFaceNames.Face_Normal01;
                        mMenuCubePlay.moveOnAxis(X_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Hard1_To_Normal1);
                    }

                    if (mMenuCubePlay.location.z == 8) {
                        mCurrentCubeFaceName = CubeFaceNames.Face_Menu;
                        mMenuCubePlay.moveOnAxis(X_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Hard1_To_Menu);
                    }

                    if (mMenuCubePlay.location.y == 0) {
                        mCurrentCubeFaceName = CubeFaceNames.Face_Hard02;
                        mMenuCubePlay.moveOnAxis(X_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Hard1_To_Hard2);
                    }

                    if (mMenuCubePlay.location.y == 8) {
                        mCurrentCubeFaceName = CubeFaceNames.Face_Hard04;
                        mMenuCubePlay.moveOnAxis(X_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Hard1_To_Hard4);
                    }
                    break;

                case Face_Hard02:
                    if (mMenuCubePlay.location.x == 0) {
                        mCurrentCubeFaceName = CubeFaceNames.Face_Hard01;
                        mMenuCubePlay.moveOnAxis(Y_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Hard2_To_Hard1);
                    }

                    if (mMenuCubePlay.location.x == 8) {
                        mCurrentCubeFaceName = CubeFaceNames.Face_Hard03;
                        mMenuCubePlay.moveOnAxis(Y_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Hard2_To_Hard3);
                    }
                    break;

                case Face_Hard03:
                    if (mMenuCubePlay.location.y == 8) {
                        mCurrentCubeFaceName = CubeFaceNames.Face_Hard04;
                        mMenuCubePlay.moveOnAxis(X_Minus);
                        mNavigator.setup(CubeFaceNavigationEnum.Hard3_To_Hard4);
                    }

                    if (mMenuCubePlay.location.y == 0) {
                        mCurrentCubeFaceName = CubeFaceNames.Face_Hard02;
                        mMenuCubePlay.moveOnAxis(X_Minus);
                        mNavigator.setup(CubeFaceNavigationEnum.Hard3_To_Hard2);
                    }
                    break;

                case Face_Hard04:
                    if (mMenuCubePlay.location.x == 8) {
                        mCurrentCubeFaceName = CubeFaceNames.Face_Hard03;
                        mMenuCubePlay.moveOnAxis(Y_Minus);
                        mNavigator.setup(CubeFaceNavigationEnum.Hard4_To_Hard3);
                    }

                    if (mMenuCubePlay.location.x == 0) {
                        mCurrentCubeFaceName = CubeFaceNames.Face_Hard01;
                        mMenuCubePlay.moveOnAxis(Y_Minus);
                        mNavigator.setup(CubeFaceNavigationEnum.Hard4_To_Hard1);
                    }
                    break;

                default:
                    break;
            }
            setCubeFaceTypeFromCubeFaceName(mCurrentCubeFaceName);
        } else {
            mMenuCubePlay.update();
            m_can_alter_text = true;
        }
    }

    private void updateOptionsCube() {
        if (mMenuCubeOptions.isDone()) {
            switch (mCurrentCubeFaceName) {
                case Face_Menu:
                    if (mMenuCubeOptions.location.y == 8) {
                        mPrevFace = CubeFaceNames.Face_Menu;
                        mCurrentCubeFaceName = CubeFaceNames.Face_Options;
                        mMenuCubeOptions.moveOnAxis(Z_Minus);

                        mNavigator.setup(CubeFaceNavigationEnum.Menu_To_Options);
                    }

                    if (mMenuCubeOptions.location.x == 7) {
                        if (CubeFaceNames.Face_Options == mPrevFace) {
                            mPrevFace = CubeFaceNames.Face_Empty;
                            mMenuCubeOptions.moveOnAxis(X_Minus);
                        }
                    }
                    break;

                case Face_Options:
                    if (mMenuCubeOptions.location.z == 8) {
                        mPrevFace = CubeFaceNames.Face_Options;
                        mCurrentCubeFaceName = CubeFaceNames.Face_Menu;
                        mMenuCubeOptions.moveOnAxis(Y_Minus);

                        mNavigator.setup(CubeFaceNavigationEnum.Options_To_Menu);
                    }
                    break;

                default:
                    break;
            } // switch
        } else {
            mMenuCubeOptions.update();
        }
    }

    private void updateStoreCube() {
        if (mMenuCubeStore.isDone()) {
            switch (mCurrentCubeFaceName) {
                case Face_Menu:
                    if (mMenuCubeStore.location.y == 0) {
                        mPrevFace = CubeFaceNames.Face_Menu;
                        mCurrentCubeFaceName = CubeFaceNames.Face_Score;
                        mMenuCubeStore.moveOnAxis(Z_Minus);

                        mNavigator.setup(CubeFaceNavigationEnum.Menu_To_Store);
                    }

                    if (mMenuCubeStore.location.x == 7) {
                        if (!mMenuCubeOptions.isDone() || !mMenuCubePlay.isDone() || mPrevFace == CubeFaceNames.Face_Score) {
                            if (mPrevFace == CubeFaceNames.Face_Score) {
                                mPrevFace = CubeFaceNames.Face_Empty;
                            }

                            mMenuCubeStore.moveOnAxis(X_Minus);
                        }
                    }
                    break;

                case Face_Score:
                    if (mMenuCubeStore.location.z == 8) {
                        mPrevFace = CubeFaceNames.Face_Score;
                        mCurrentCubeFaceName = CubeFaceNames.Face_Menu;
                        mMenuCubeStore.moveOnAxis(Y_Plus);

                        mNavigator.setup(CubeFaceNavigationEnum.Store_To_Menu);
                    }
                    break;

                default:
                    break;
            }
        } else {
            mMenuCubeStore.update();
        }
    }

    private void updateCubes() {
        for (int i = 0; i < 7; ++i) {
            mTitles.get(i).clear();
            mTexts.get(i).clear();
            mSymbols.get(i).clear();
        }

        int face_type;
        int size;
        Cube cube;

        size = mCubesBase.size();
        for (int i = 0; i < size; ++i) {
            cube = mCubesBase.get(i);

            face_type = X_Plus;
            if (null != cube.fonts[face_type]) {
                mTexts.get(face_type).add(cube.fonts[face_type]);
            }

            if (null != cube.symbols[face_type]) {
                mSymbols.get(face_type).add(cube.symbols[face_type]);
            }


            face_type = X_Minus;
            if (null != cube.fonts[face_type]) {
                mTexts.get(face_type).add(cube.fonts[face_type]);
            }

            if (null != cube.symbols[face_type]) {
                mSymbols.get(face_type).add(cube.symbols[face_type]);
            }


            face_type = Y_Plus;
            if (null != cube.fonts[face_type]) {
                mTexts.get(face_type).add(cube.fonts[face_type]);
            }

            if (null != cube.symbols[face_type]) {
                mSymbols.get(face_type).add(cube.symbols[face_type]);
            }


            face_type = Y_Minus;
            if (null != cube.fonts[face_type]) {
                mTexts.get(face_type).add(cube.fonts[face_type]);
            }

            if (null != cube.symbols[face_type]) {
                mSymbols.get(face_type).add(cube.symbols[face_type]);
            }


            face_type = Z_Plus;
            if (null != cube.fonts[face_type]) {
                mTexts.get(face_type).add(cube.fonts[face_type]);
            }

            if (null != cube.symbols[face_type]) {
                mSymbols.get(face_type).add(cube.symbols[face_type]);
            }


            face_type = Z_Minus;
            if (null != cube.fonts[face_type]) {
                mTexts.get(face_type).add(cube.fonts[face_type]);
            }

            if (null != cube.symbols[face_type]) {
                mSymbols.get(face_type).add(cube.symbols[face_type]);
            }
        }

        size = mCubesFace.size();
        for (int i = 0; i < size; ++i) {
            cube = mCubesFace.get(i);

            face_type = X_Plus;
            if (null != cube.fonts[face_type]) {
                mTitles.get(face_type).add(cube.fonts[face_type]);
            }

            if (null != cube.symbols[face_type]) {
                mSymbols.get(face_type).add(cube.symbols[face_type]);
            }


            face_type = X_Minus;
            if (null != cube.fonts[face_type]) {
                mTitles.get(face_type).add(cube.fonts[face_type]);
            }

            if (null != cube.symbols[face_type]) {
                mSymbols.get(face_type).add(cube.symbols[face_type]);
            }


            face_type = Y_Plus;
            if (null != cube.fonts[face_type]) {
                mTitles.get(face_type).add(cube.fonts[face_type]);
            }

            if (null != cube.symbols[face_type]) {
                mSymbols.get(face_type).add(cube.symbols[face_type]);
            }


            face_type = Y_Minus;
            if (null != cube.fonts[face_type]) {
                mTitles.get(face_type).add(cube.fonts[face_type]);
            }

            if (null != cube.symbols[face_type]) {
                mSymbols.get(face_type).add(cube.symbols[face_type]);
            }


            face_type = Z_Plus;
            if (null != cube.fonts[face_type]) {
                mTitles.get(face_type).add(cube.fonts[face_type]);
            }

            if (null != cube.symbols[face_type]) {
                mSymbols.get(face_type).add(cube.symbols[face_type]);
            }


            face_type = Z_Minus;
            if (null != cube.fonts[face_type]) {
                mTitles.get(face_type).add(cube.fonts[face_type]);
            }

            if (null != cube.symbols[face_type]) {
                mSymbols.get(face_type).add(cube.symbols[face_type]);
            }
        }

        if (mMenuCubeHilite != null) {
            m_hilite_alpha += 0.05f;

            if (m_hilite_alpha > 0.2f) {
                m_hilite_alpha = 0.2f;
            }
        }

        size = mCubesBase.size();
        for (int i = 0; i < size; ++i) {
            cube = mCubesBase.get(i);
            cube.warmByFactor(WARM_FACTOR);
        }
    }

    private void updateInCredits() {
        m_credits_offset -= 0.05f;

        if (m_credits_offset < -5.0f) {
            m_credits_offset = 20.0f;
        }
    }

    private void animFontAppear() {
        int i;
        int size;
        Cube cube;

        size = mCubesBase.size();
        for(i = 0; i < size; ++i) {
            cube = mCubesBase.get(i);
            cube.warmFonts();
            cube.warmSymbols();
        }

        size = mCubesFace.size();
        for(i = 0; i < size; ++i) {
            cube = mCubesFace.get(i);
            cube.warmFonts();
            cube.warmSymbols();
        }
    }

    private void updateInMenu() {
        switch (mCurrentCubeFaceName) {
            case Face_Tutorial:
                animFontAppear();
                break;

            case Face_Score:
                break;

            default:
                break;
        }

        updatePlayCube();
        updateOptionsCube();
        updateStoreCube();
    }

    public void update() {
        if (mShowingHelp) {
            mShowHelpTimeout -= 0.01f;

            if (mShowHelpTimeout < 0.0f) {
                mShowingHelp = false;

                releaseCubeTextsOnFace(Z_Plus);
                MenuFaceBuilder.resetTransforms();
                MenuFaceBuilder.buildTexts(CubeFaceNames.Face_Menu, Z_Plus, false);
            }
        }

        if (!mNavigator.isCurrentNavigation(CubeFaceNavigationEnum.NoNavigation)) {
            mNavigator.update();
        } else {
            switch (mState) {
                case InMenu: updateInMenu(); break;
                case InCredits: updateInCredits(); break;
                case AnimToCredits: updateAnimToCredits(); break;
                case AnimFromCredits: updateAnimFromCredits(); break;
            }
            Game.updateHiLitedCubes(mMenuCubes);
        }
        updateCubes();
    }

    private void eventPlayLevel(DifficultyEnum difficulty, int levelNumber) {
        if (!Game.canPlayLockedLevels) {
            switch (difficulty) {
                case Easy:
                    if (LEVEL_LOCKED == GameProgress.getStarsEasy(levelNumber)) {
                        Audio.playSound(SOUND_TAP_ON_LOCKED_LEVEL_CUBE);
                        return;
                    }
                    break;

                case Normal:
                    if (LEVEL_LOCKED == GameProgress.getStarsNormal(levelNumber)) {
                        Audio.playSound(SOUND_TAP_ON_LOCKED_LEVEL_CUBE);
                        return;
                    }
                    break;

                case Hard:
                    if (LEVEL_LOCKED == GameProgress.getStarsHard(levelNumber)) {
                        Audio.playSound(SOUND_TAP_ON_LOCKED_LEVEL_CUBE);
                        return;
                    }
                    break;
            }
        }

        Audio.playSound(SOUND_TAP_ON_LEVEL_CUBE);

        Audio.stopMusic();

        Game.setAnimFaces(difficulty, levelNumber);

        Game.levelInitData.difficulty = difficulty;
        Game.levelInitData.levelNumber = levelNumber;
        Game.levelInitData.initAction = LevelInitActionEnum.FullInit;

        Game.animInitData.type = AnimTypeEnum.AnimToLevel;

        Game.animInitData.cubesBase.clear();

        int size = mCubesBase.size();
        Cube cube;
        for (int i = 0; i < size; ++i) {
            cube = mCubesBase.get(i);
            Game.animInitData.cubesBase.add(cube);
        }

        Game.animInitData.cameraFrom.init(mCameraCurrent);
        Game.animInitData.cameraTo.init(Game.level.mCameraLevel);

        Game.animInitData.posLightFrom.init(mPosLightCurrent);
        Game.animInitData.posLightTo.init(Game.level.getLightPositionCurrent());

        Game.showScene(Scene_Anim);
    }

    private void eventShowCredits() {
        m_credits_offset = 20.0f;

        m_t = 0.0f;
        mState = StateEnum.AnimToCredits;

        float divisor = 20.0f;

        mInterpolators[0].setup(mCameraMenu.eye.x, mCameraCredits.eye.x, divisor);
        mInterpolators[1].setup(mCameraMenu.eye.y, mCameraCredits.eye.y, divisor);
        mInterpolators[2].setup(mCameraMenu.eye.z, mCameraCredits.eye.z, divisor);

        mInterpolators[3].setup(mCameraMenu.target.x, mCameraCredits.target.x, divisor);
        mInterpolators[4].setup(mCameraMenu.target.y, mCameraCredits.target.y, divisor);
        mInterpolators[5].setup(mCameraMenu.target.z, mCameraCredits.target.z, divisor);
    }

    private void renderForPicking(PickRenderTypeEnum type) {
        
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glDisable(GL_LIGHTING);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);

        glDisable(GL_TEXTURE_2D);

        Graphics.setProjection3D();
        Graphics.setModelViewMatrix3D(mCameraCurrent);

        mNavigator.applyRotations();

        glPushMatrix();
        glTranslatef(Game.cubeOffset.x, Game.cubeOffset.y, Game.cubeOffset.z);

        Graphics.resetBufferIndices();

        switch (type) {
            case RenderOnlyOptions:
                Graphics.addCubeSize(optionCubes[0].pos.x, optionCubes[0].pos.y, optionCubes[0].pos.z, HALF_CUBE_SIZE * 1.5f, optionCubes[0].color);
                Graphics.addCubeSize(optionCubes[1].pos.x, optionCubes[1].pos.y, optionCubes[1].pos.z, HALF_CUBE_SIZE * 1.5f, optionCubes[1].color);
                Graphics.addCubeSize(optionCubes[2].pos.x, optionCubes[2].pos.y, optionCubes[2].pos.z, HALF_CUBE_SIZE * 1.5f, optionCubes[2].color);
                Graphics.addCubeSize(optionCubes[3].pos.x, optionCubes[3].pos.y, optionCubes[3].pos.z, HALF_CUBE_SIZE * 1.5f, optionCubes[3].color);
                break;

            case RenderOnlyMovingCubePlay:
                Graphics.addCubeSize(mMenuCubePlay.pos.x, mMenuCubePlay.pos.y, mMenuCubePlay.pos.z, HALF_CUBE_SIZE * 1.5f, mMenuCubePlay.color);
                break;

            case RenderOnlyMovingCubeStore:
                Graphics.addCubeSize(mMenuCubeStore.pos.x, mMenuCubeStore.pos.y, mMenuCubeStore.pos.z, HALF_CUBE_SIZE * 1.5f, mMenuCubeStore.color);
                break;

            case RenderOnlyMovingCubeOptions:
                Graphics.addCubeSize(mMenuCubeOptions.pos.x, mMenuCubeOptions.pos.y, mMenuCubeOptions.pos.z, HALF_CUBE_SIZE * 1.5f, mMenuCubeOptions.color);
                break;

            case RenderOnlyCubeCredits:
                Graphics.addCubeSize(cubeCredits.pos.x, cubeCredits.pos.y, cubeCredits.pos.z, HALF_CUBE_SIZE * 1.5f, cubeCredits.color);
                break;

            case RenderOnlyMovingCubes:
                switch (mCurrentCubeFaceName) {
                    case Face_Options:
                        Graphics.addCubeSize(mMenuCubeOptions.pos.x, mMenuCubeOptions.pos.y, mMenuCubeOptions.pos.z, HALF_CUBE_SIZE * 1.5f, mMenuCubeOptions.color);
                        break;

                    case Face_Score:
                        Graphics.addCubeSize(mMenuCubeStore.pos.x, mMenuCubeStore.pos.y, mMenuCubeStore.pos.z, HALF_CUBE_SIZE * 1.5f, mMenuCubeStore.color);
                        break;

                    case Face_Menu:
                        Graphics.addCubeSize(mMenuCubePlay.pos.x, mMenuCubePlay.pos.y, mMenuCubePlay.pos.z, HALF_CUBE_SIZE * 1.5f, mMenuCubePlay.color);
                        Graphics.addCubeSize(mMenuCubeOptions.pos.x, mMenuCubeOptions.pos.y, mMenuCubeOptions.pos.z, HALF_CUBE_SIZE * 1.5f, mMenuCubeOptions.color);
                        Graphics.addCubeSize(mMenuCubeStore.pos.x, mMenuCubeStore.pos.y, mMenuCubeStore.pos.z, HALF_CUBE_SIZE * 1.5f, mMenuCubeStore.color);
                        Graphics.addCubeSize(cubeCredits.pos.x, cubeCredits.pos.y, cubeCredits.pos.z, HALF_CUBE_SIZE * 1.5f, cubeCredits.color);
                        break;

                    default:
                        Graphics.addCubeSize(mMenuCubePlay.pos.x, mMenuCubePlay.pos.y, mMenuCubePlay.pos.z, HALF_CUBE_SIZE * 1.5f, mMenuCubePlay.color);
                        break;
                }
                break;

            case RenderOnlyLevelCubes: {
                int size = Game.cubeFacesData[mCurrentCubeFaceType].levelCubes.size();
                LevelCube levelCube;

                for (int i = 0; i < size; ++i) {
                    levelCube = Game.cubeFacesData[mCurrentCubeFaceType].levelCubes.get(i);
                    if (mCurrentCubeFaceName == levelCube.faceName) {
                        Graphics.addCubeSize(levelCube.pos.x, levelCube.pos.y, levelCube.pos.z, HALF_CUBE_SIZE * 1.5f, levelCube.color);
                    }
                }
            }
            break;

            default:
                break;
        }

        Graphics.addCubeSize(mMenuCubePlay.pos.x, mMenuCubePlay.pos.y, mMenuCubePlay.pos.z, HALF_CUBE_SIZE * 1.5f, mMenuCubePlay.color);

        glDisable(GL_TEXTURE_2D);
        Graphics.bindStreamSources3dNoTexture();
        Graphics.updateBuffers();
        Graphics.renderTriangles();
        glEnable(GL_TEXTURE_2D);
        glPopMatrix();
    }

    @Override
    public void render() {
//        renderForPicking(PickRenderTypeEnum.RenderOnlyLevelCubes);
//        if (true) {
//            return;
//        }

        glEnable(GL_LIGHT0);

        Graphics.setProjection2D();
        Graphics.setModelViewMatrix2D();
        Graphics.bindStreamSources2d();

        glEnable(GL_BLEND);
        glDisable(GL_LIGHTING);
        glDepthMask(false);
        glEnable(GL_TEXTURE_2D);

        Color color = new Color(255, 255, 255, (int) Game.dirtyAlpha);
        Graphics.drawFullScreenTexture(Graphics.textureDirty, color);

        glDepthMask(true);

        Graphics.setProjection3D();
        Graphics.setModelViewMatrix3D(mCameraCurrent);
        Graphics.bindStreamSources3d();

        Graphics.setLightPosition(mPosLightCurrent);

        glEnable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);


        if (StateEnum.InCredits == mState) {
            Graphics.textureCredits.bind();
            drawCredits();
            //return;
        }

//        if (true) {
//            glDisable(GL_TEXTURE_2D);
//            glDisable(GL_LIGHTING);
//            Graphics.drawAxes();
//            glEnable(GL_LIGHTING);
//            glEnable(GL_TEXTURE_2D);
//        }

        mNavigator.applyRotations();

        glEnable(GL_DEPTH_TEST);
        Graphics.textureGrayConcrete.bind();
        drawTheCube();

//        if (true) {
//            glDisable(GL_TEXTURE_2D);
//            glDisable(GL_LIGHTING);
//            Graphics.drawAxes();
//            glEnable(GL_LIGHTING);
//            glEnable(GL_TEXTURE_2D);
//        }

        glPushMatrix();
        glTranslatef(Game.cubeOffset.x, Game.cubeOffset.y, Game.cubeOffset.z);

        Graphics.textureLevelCubes.bind();
        drawLevelCubes();

        Graphics.texturePlayer.bind();
        drawMenuCubes();

        glPopMatrix();

        glDisable(GL_LIGHTING);

        Graphics.resetBufferIndices();
        glEnable(GL_BLEND);
        Graphics.bindStreamSources3d();

        glPushMatrix();
            glTranslatef(Game.cubeOffset.x, Game.cubeOffset.y, Game.cubeOffset.z);

            Graphics.textureFonts.bind();

            drawTexts();
            drawTextsTitles();

            Graphics.textureNumbers.bind();
            drawLevelNumbers();

            Graphics.textureSymbols.bind();
            drawLevelCubeSymbols();

            drawSymbols();

            if (mMenuCubeHilite != null) {
                color = new Color(255, 255, 255, (int)(m_hilite_alpha * 255));
                glDisable(GL_DEPTH_TEST);
                drawCubeHiLite(color);
                glEnable(GL_DEPTH_TEST);
            }

            if (CubeFaceNames.Face_Options == mCurrentCubeFaceName ||
                mNavigator.isCurrentNavigation(CubeFaceNavigationEnum.Menu_To_Options) ||
                mNavigator.isCurrentNavigation(CubeFaceNavigationEnum.Options_To_Menu)) {
                drawCubeFaceOptions();
            }
        glPopMatrix();
    }

    private String getCubeFaceName(CubeFaceNames cubeFaceName) {
        String face = "-";
        switch (cubeFaceName) {
            case Face_Easy01:
                face = "Face_Easy01";
                break;

            case Face_Easy02:
                face = "Face_Easy02";
                break;

            case Face_Easy03:
                face = "Face_Easy03";
                break;

            case Face_Easy04:
                face = "Face_Easy04";
                break;

            case Face_Normal01:
                face = "Face_Normal01";
                break;

            case Face_Normal02:
                face = "Face_Normal02";
                break;

            case Face_Normal03:
                face = "Face_Normal03";
                break;

            case Face_Normal04:
                face = "Face_Normal04";
                break;

            case Face_Hard01:
                face = "Face_Hard01";
                break;

            case Face_Hard02:
                face = "Face_Hard02";
                break;

            case Face_Hard03:
                face = "Face_Hard03";
                break;

            case Face_Hard04:
                face = "Face_Hard04";
                break;
        }
        return face;
    }

    @Override
    public void onFingerDown(float x, float y, int fingerCount) {
        //System.out.println("Menu::OnFingerDown " + x + ", y:" + y);

        if (mNavigator.isCurrentNavigation(
                CubeFaceNavigationEnum.NoNavigation) &&
                mMenuCubePlay.isDone() &&
                mMenuCubeOptions.isDone() &&
                mMenuCubeStore.isDone()) {
            mIsFingerDown = true;

            mPosDown.x = x;
            mPosDown.y = y;

            renderForPicking(PickRenderTypeEnum.RenderOnlyMovingCubes);

            mColorDown = Graphics.getColorFromScreen(mPosDown);
            MenuCube menuCube = getMenuCubeFromColor(mColorDown.r);

            if (menuCube != null) {

//                // TODO: remove this later
//                if (false) {
//                    System.out.println( "// " + getCubeFaceName(mCurrentCubeFaceName) );
//                    System.out.println("mMenuCubePlay.setCubeLocation(" + menuCube.location.x + ", " + menuCube.location.y +", " + menuCube.location.z + ");");
//                    mNavigator.dump();
//                }

                m_hilite_alpha = 0.0f;
                mMenuCubeHilite = menuCube;
                CubeLocation cp = mMenuCubeHilite.location;
                m_font_hilite.init(Symbol_Hilite, cp);
            }
        }
    }

    @Override
    public void onFingerMove(float prevX, float prevY, float curX, float curY, int fingerCount) {
        if (mIsFingerDown) {
            //System.out.println("Menu::OnFingerMove " + cur_x + ", " + cur_y);
            mPosMove.x = curX;
            mPosMove.y = curY;

            float dist = Utils.getDistance2D(mPosDown, mPosMove);
            //System.out.println("OnFingerMove: distance " + dist);

            if (dist > 20.0f * Graphics.deviceScale) {
                mIsSwipe = true;
            }
        }
    }

    @Override
    public void onFingerUp(float x, float y, int fingerCount) {
        mIsFingerDown = false;

        if (!mMenuCubePlay.isDone() || !mMenuCubeOptions.isDone() || !mMenuCubeStore.isDone()) {
            return;
        }

        mMenuCubeHilite = null;

        mPosUp.x = x;
        mPosUp.y = y;

        if (StateEnum.InCredits == mState || StateEnum.AnimToCredits == mState || StateEnum.AnimFromCredits == mState) {
            if (StateEnum.InCredits == mState) {
                m_t = 0.0f;
                mState = StateEnum.AnimFromCredits;
            }
            return;
        }

        if (mIsSwipe) {
            mIsSwipe = false;
            handleSwipe();
        } else { // single tap        
            switch (mCurrentCubeFaceName) {
                case Face_Menu:
                    fingerUpOnFaceMenu();
                    break;

                case Face_Options:
                    fingerUpOnFaceOptions();
                    break;

                case Face_Easy01:
                case Face_Easy02:
                case Face_Easy03:
                case Face_Easy04:
                    fingerUpOnFacesEasy();
                    break;

                case Face_Normal01:
                case Face_Normal02:
                case Face_Normal03:
                case Face_Normal04:
                    fingerUpOnFacesNormal();
                    break;

                case Face_Hard01:
                case Face_Hard02:
                case Face_Hard03:
                case Face_Hard04:
                    fingerUpOnFacesHard();
                    break;

                default:
                    break;
            }
        }
    }

    private void fingerUpOnFaceOptions() {
        renderForPicking(PickRenderTypeEnum.RenderOnlyOptions);
        mColorDown = Graphics.getColorFromScreen(mPosDown);
        mColorUp = Graphics.getColorFromScreen(mPosUp);

        if (mColorDown.r == mColorUp.r) {
            switch (mColorDown.r) {
                case 255:
                    Game.musicVolumeUp();
                    Audio.playSound(SOUND_VOLUME_UP);
                    break;

                case 254:
                    Game.musicVolumeDown();
                    Audio.playSound(SOUND_VOLUME_DOWN);
                    break;

                case 253:
                    Game.soundVolumeUp();
                    //Audio.playSound(SOUND_VOLUME_UP);
                    break;

                case 252:
                    Game.soundVolumeDown();
                    //Audio.playSound(SOUND_VOLUME_DOWN);
                    break;

                default:
                    break;
            }
        }
    }

    private void fingerUpOnFacesEasy() {
        renderForPicking(PickRenderTypeEnum.RenderOnlyLevelCubes);
        mColorDown = Graphics.getColorFromScreen(mPosDown);
        mColorUp = Graphics.getColorFromScreen(mPosUp);

        if (mColorDown.r == mColorUp.r) {
            int level_number = 255 - mColorUp.r;

            if (level_number >= 1 && level_number <= 60) {
                eventPlayLevel(DifficultyEnum.Easy, level_number);
            }
        }
    }

    private void fingerUpOnFacesNormal() {
        renderForPicking(PickRenderTypeEnum.RenderOnlyLevelCubes);
        mColorDown = Graphics.getColorFromScreen(mPosDown);
        mColorUp = Graphics.getColorFromScreen(mPosUp);

        if (mColorDown.r == mColorUp.r) {
            int level_number = 255 - mColorUp.r;

            if (level_number >= 1 && level_number <= 60) {
                eventPlayLevel(DifficultyEnum.Normal, level_number);
            }
        }
    }

    private void fingerUpOnFacesHard() {
        renderForPicking(PickRenderTypeEnum.RenderOnlyLevelCubes);
        mColorDown = Graphics.getColorFromScreen(mPosDown);
        mColorUp = Graphics.getColorFromScreen(mPosUp);

        if (mColorDown.r == mColorUp.r) {
            int level_number = 255 - mColorUp.r;

            if (level_number >= 1 && level_number <= 60) {
                eventPlayLevel(DifficultyEnum.Hard, level_number);
            }
        }
    }

    private void fingerUpOnFaceMenu() {
        renderForPicking(PickRenderTypeEnum.RenderOnlyMovingCubes);
        mColorDown = Graphics.getColorFromScreen(mPosDown);
        mColorUp = Graphics.getColorFromScreen(mPosUp);

        if (mColorDown.r == mColorUp.r) {
            MenuCube menuCube = getMenuCubeFromColor(mColorUp.r);
            if (menuCube == cubeCredits) {
                eventShowCredits();
                return;
            }

            if (!mShowingHelp) {
                if (255 == mColorDown.r || 200 == mColorDown.r || 100 == mColorDown.r) {
                    mShowingHelp = true;
                    mShowHelpTimeout = 3.0f;
                    releaseCubeTextsOnFace(Z_Plus);
                    MenuFaceBuilder.resetTransforms();
                    MenuFaceBuilder.buildTexts(CubeFaceNames.Face_Menu, Z_Plus, true);
                }
            }
        }
    }

    private void handleSwipe() {
        
        SwipeInfo swipeInfo = Game.getSwipeDirAndLength(mPosDown, mPosUp);

        if (swipeInfo.length > (30.0f * Graphics.scaleFactor)) {
            renderForPicking(PickRenderTypeEnum.RenderOnlyMovingCubes);
            Color down_color = Graphics.getColorFromScreen(mPosDown);
            //printf("\nOnFingerUp [SWIPE] color is: %d, %d, %d, %d", m_down_color.r, m_down_color.g, m_down_color.b, m_down_color.a);

            MenuCube menuCube = getMenuCubeFromColor(down_color.r);

            if (menuCube != null) {
                switch (swipeInfo.swipeDir) {
                    case SwipeLeft:
                        switch (mCurrentCubeFaceName) {
                            case Face_Menu:
                            case Face_Options:
                            case Face_Score:
                                menuCube.moveOnAxis(X_Minus);
                                break;

                            case Face_Normal01:
                            case Face_Normal02:
                            case Face_Normal03:
                            case Face_Normal04:
                                menuCube.moveOnAxis(X_Plus);
                                break;

                            case Face_Hard01:
                            case Face_Hard02:
                            case Face_Hard03:
                            case Face_Hard04:
                            case Face_Tutorial:
                                menuCube.moveOnAxis(Z_Minus);
                                break;

                            case Face_Easy01:
                            case Face_Easy02:
                            case Face_Easy03:
                            case Face_Easy04:
                                menuCube.moveOnAxis(Z_Plus);
                                break;

                            default:
                                break;
                        }
                        break;

                    case SwipeRight:
                        switch (mCurrentCubeFaceName) {
                            case Face_Menu:
                            case Face_Options:
                            case Face_Score:
                                menuCube.moveOnAxis(X_Plus);
                                if (CubeFaceNames.Face_Menu == mCurrentCubeFaceName) {
                                    if (menuCube != mMenuCubeOptions) {
                                        if (7 == mMenuCubeOptions.location.x) {
                                            mMenuCubeOptions.moveOnAxis(X_Minus);
                                        }
                                    }
                                }
                                break;

                            case Face_Normal01:
                            case Face_Normal02:
                            case Face_Normal03:
                            case Face_Normal04:
                                menuCube.moveOnAxis(X_Minus);
                                break;

                            case Face_Hard01:
                            case Face_Hard02:
                            case Face_Hard03:
                            case Face_Hard04:
                            case Face_Tutorial:
                                menuCube.moveOnAxis(Z_Plus);
                                break;

                            case Face_Easy01:
                            case Face_Easy02:
                            case Face_Easy03:
                            case Face_Easy04:
                                menuCube.moveOnAxis(Z_Minus);
                                break;

                            default:
                                break;
                        }
                        break;

                    case SwipeUp:
                        switch (mCurrentCubeFaceName) {
                            case Face_Score: menuCube.moveOnAxis(Z_Plus); break;
                            case Face_Options: menuCube.moveOnAxis(Z_Minus); break;
                            case Face_Easy02: menuCube.moveOnAxis(X_Plus); break;
                            case Face_Easy03: menuCube.moveOnAxis(Y_Minus); break;
                            case Face_Easy04: menuCube.moveOnAxis(X_Minus); break;
                            case Face_Normal02: menuCube.moveOnAxis(Z_Minus); break;
                            case Face_Normal03: menuCube.moveOnAxis(Y_Minus); break;
                            case Face_Normal04: menuCube.moveOnAxis(Z_Plus); break;
                            case Face_Hard02: menuCube.moveOnAxis(X_Minus); break;
                            case Face_Hard03: menuCube.moveOnAxis(Y_Minus); break;
                            case Face_Hard04: menuCube.moveOnAxis(X_Plus); break;
                            default: menuCube.moveOnAxis(Y_Plus); break;
                        }
                        break;

                    case SwipeDown:
                        switch (mCurrentCubeFaceName) {
                            case Face_Score: menuCube.moveOnAxis(Z_Minus); break;
                            case Face_Options: menuCube.moveOnAxis(Z_Plus); break;
                            case Face_Easy02: menuCube.moveOnAxis(X_Minus); break;
                            case Face_Easy03: menuCube.moveOnAxis(Y_Plus); break;
                            case Face_Easy04: menuCube.moveOnAxis(X_Plus); break;
                            case Face_Normal02: menuCube.moveOnAxis(Z_Plus); break;
                            case Face_Normal03: menuCube.moveOnAxis(Y_Plus); break;
                            case Face_Normal04: menuCube.moveOnAxis(Z_Minus); break;
                            case Face_Hard02: menuCube.moveOnAxis(X_Plus); break;
                            case Face_Hard03: menuCube.moveOnAxis(Y_Plus); break;
                            case Face_Hard04: menuCube.moveOnAxis(X_Minus); break;
                            default: menuCube.moveOnAxis(Y_Minus); break;
                        }
                        break;

                    default:
                        break;
                }
            }
        }
    }
}

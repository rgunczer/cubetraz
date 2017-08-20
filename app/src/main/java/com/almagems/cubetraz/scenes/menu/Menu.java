package com.almagems.cubetraz.scenes.menu;

import com.almagems.cubetraz.cubes.CubeLocation;
import com.almagems.cubetraz.graphics.Camera;
import com.almagems.cubetraz.graphics.Color;
import com.almagems.cubetraz.graphics.Graphics;
import com.almagems.cubetraz.scenes.Creator;
import com.almagems.cubetraz.cubes.Cube;
import com.almagems.cubetraz.cubes.CubeFont;
import com.almagems.cubetraz.utils.EaseOutDivideInterpolation;
import com.almagems.cubetraz.game.Game;
import com.almagems.cubetraz.cubes.LevelCube;
import com.almagems.cubetraz.utils.SwipeInfo;
import com.almagems.cubetraz.graphics.TexCoordsQuad;
import com.almagems.cubetraz.graphics.TexturedQuad;
import com.almagems.cubetraz.math.Utils;
import com.almagems.cubetraz.math.Vector;
import com.almagems.cubetraz.math.Vector2;
import com.almagems.cubetraz.scenes.Scene;

import java.util.ArrayList;
import static android.opengl.GLES10.*;
import static com.almagems.cubetraz.game.Audio.*;
import static com.almagems.cubetraz.game.Game.*;

public final class Menu extends Scene {

    private enum StateEnum {
        InMenu,
        InCredits,
        AnimToCredits,
        AnimFromCredits
    }

    private final ArrayList<ArrayList<CubeFont>> mTitles = new ArrayList<>(6);
    private final ArrayList<ArrayList<CubeFont>> mTexts = new ArrayList<>(6);
    private final ArrayList<ArrayList<CubeFont>> mSymbols = new ArrayList<>(6);

    private StateEnum mState;

    private float m_t;

    private float m_hilite_timeout;

    private MenuNavigator mNavigator = new MenuNavigator();

    private boolean m_can_alter_text;

    private float m_credits_offset;

    private Color m_color_down;
    private Color m_color_up;

    private boolean m_showing_help;
    private float m_show_help_timeout;

    public Camera mCameraMenu = new Camera();
    private Camera mCameraCredits = new Camera();

    private CubeFaceNamesEnum m_prev_face;

    public MenuCube mMenuCubePlay;
    public MenuCube mMenuCubeOptions;
    public MenuCube mMenuCubeStore;

    public final MenuCube[] m_arOptionsCubes = new MenuCube[4];

    public MenuCube m_cubeCredits;

    private MenuCube m_menu_cube_hilite;
    private CubeFont m_font_hilite = new CubeFont();
    private float m_hilite_alpha;

    // fonts on red cubes
    public CubeFont m_cubefont_play = new CubeFont();
    public CubeFont m_cubefont_options = new CubeFont();
    public CubeFont m_cubefont_store = new CubeFont();
    public CubeFont m_cubefont_noads = new CubeFont();
    public CubeFont m_cubefont_solvers = new CubeFont();
    public CubeFont m_cubefont_restore = new CubeFont();

    // Cubes
    private final ArrayList<Cube> mCubesBase = new ArrayList<>();
    public final ArrayList<Cube> mCubesFace = new ArrayList<>();

    private final EaseOutDivideInterpolation[] mInterpolators = new EaseOutDivideInterpolation[6];

    private CubeFaceNamesEnum m_current_cube_face;
    private int m_current_cube_face_type;

    private MenuCube getMovingCubeFromColor(int color) {
        switch (color) {
            case 255: return mMenuCubePlay;
            case 200: return mMenuCubeOptions;
            case 100: return mMenuCubeStore;
            case 1:   return m_cubeCredits;
        }
        return null;
    }

    public void dontHiliteMenuCube() {
        m_menu_cube_hilite = null;
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
        for (int i = 0; i < 6; ++i) {
            mTitles.add(new ArrayList<CubeFont>());
            mTexts.add(new ArrayList<CubeFont>());
            mSymbols.add(new ArrayList<CubeFont>());

            lst = mTitles.get(i);
            for (int j = 0; j < 6; j++) {
                lst.add(new CubeFont());
            }

            lst = mTexts.get(i);
            for (int j = 0; j < 6; j++) {
                lst.add(new CubeFont());
            }

            lst = mSymbols.get(i);
            for (int j = 0; j < 6; j++) {
                lst.add(new CubeFont());
            }
        }
    }

    private void setupCameras() {
        mCameraCredits.eye = new Vector(18.0f / 1.5f, 30.0f / 1.5f, 45.0f / 1.5f);
        mCameraCredits.target = new Vector(-2.0f / 1.5f, 0.0f, 5.0f / 1.5f);

        //#if defined(DRAW_AXES_CUBE) || defined(DRAW_AXES_GLOBAL)
        //mCameraMenu.eye = Vector(1.0f / 1.5f, -1.0f / 1.5f, 34.0f / 1.5f);
        //#else
        mCameraMenu.eye = new Vector(0.0f, 0.0f, 35.0f / 1.5f);
        //#endif

        mCameraMenu.target = new Vector(0.0f, 0.0f, 0.0f);

        mCameraMenu.eye = mCameraMenu.eye.scale(Game.graphics.aspectRatio);
        mCameraCredits.eye = mCameraCredits.eye.scale(Game.graphics.aspectRatio);

        mPosLightCurrent = mPosLight;
        mCameraCurrent.init(mCameraMenu);
    }

    @Override
    public void init() {
        setupCameras();
        Game.dirtyAlpha = DIRTY_ALPHA;
        Game.audio.playMusic(MUSIC_CPU);

        m_hilite_timeout = 0.0f;
        m_menu_cube_hilite = null;
        m_showing_help = false;
        mNavigator.m_menu = this;
        m_prev_face = CubeFaceNamesEnum.Face_Empty;
        mIsFingerDown = false;
        mIsSwipe = false;
        mPosLightCurrent.init(mPosLight);
        mCameraCurrent.init(mCameraMenu);

        Game.resetCubes();
        Game.setupHollowCube();
        Game.buildVisibleCubesList(mCubesBase);

        if (Game.menu_init_data.reappear) {
            switch (m_current_cube_face) {
                case Face_Easy01:
                case Face_Easy02:
                case Face_Easy03:
                case Face_Easy04:
                    mNavigator.createEasyFaces();

                    if (CubeFaceNamesEnum.Face_Easy02 == m_current_cube_face || CubeFaceNamesEnum.Face_Easy03 == m_current_cube_face || CubeFaceNamesEnum.Face_Easy04 == m_current_cube_face) {
                        MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Empty, Face_Z_Plus);
                        MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Empty, Face_Z_Minus);
                    }
                    break;

                case Face_Normal01:
                case Face_Normal02:
                case Face_Normal03:
                case Face_Normal04:
                    mNavigator.createNormalFaces();

                    if (CubeFaceNamesEnum.Face_Normal02 == m_current_cube_face || CubeFaceNamesEnum.Face_Normal03 == m_current_cube_face || CubeFaceNamesEnum.Face_Normal04 == m_current_cube_face) {
                        MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Empty, Face_X_Plus);
                        MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Empty, Face_X_Minus);
                    }
                    break;

                case Face_Hard01:
                case Face_Hard02:
                case Face_Hard03:
                case Face_Hard04:
                    mNavigator.createHardFaces();

                    if (CubeFaceNamesEnum.Face_Hard02 == m_current_cube_face || CubeFaceNamesEnum.Face_Hard03 == m_current_cube_face || CubeFaceNamesEnum.Face_Hard04 == m_current_cube_face) {
                        MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Empty, Face_Z_Plus);
                        MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Empty, Face_Z_Minus);
                    }
                    break;

                default:
                    break;
            }
        } else {
            Creator.createMovingCubesForMenu();

            mNavigator.init(this);
            m_current_cube_face = CubeFaceNamesEnum.Face_Tutorial;
            setCurrentCubeFaceType(Face_X_Minus);

            mNavigator.createMenuFaces(true);

            mMenuCubePlay.setCubePos(0, 5, 4);
            mMenuCubeOptions.setCubePos(1, 3, 8);
            mMenuCubeStore.setCubePos(1, 1, 8);
        }

        mState = StateEnum.InMenu;
        Game.buildVisibleCubesListOnlyOnFaces(mCubesFace);
        update();


        Graphics graphics = Game.graphics;
        graphics.setProjection3D();
        graphics.setModelViewMatrix3D(mCameraCurrent);

        graphics.setLightPosition(mPosLightCurrent);
    }

    private void setCurrentCubeFaceType(int face_type) {
        CubeLocation offset = new CubeLocation(0, 0, 0);

        m_current_cube_face_type = face_type;

        switch (face_type) {
            case Face_X_Plus: offset = new CubeLocation(-1, 0, 0); break;
            case Face_X_Minus: offset = new CubeLocation(1, 0, 0); break;
            case Face_Y_Plus: offset = new CubeLocation(0, -1, 0); break;
            case Face_Y_Minus: offset = new CubeLocation(0, 1, 0); break;
            case Face_Z_Plus: offset = new CubeLocation(0, 0, -1); break;
            case Face_Z_Minus: offset = new CubeLocation(0, 0, 1); break;
            default:
                break;
        }

        mMenuCubePlay.setHiliteOffset(offset);
        mMenuCubeOptions.setHiliteOffset(offset);
        mMenuCubeStore.setHiliteOffset(offset);
    }

    private void releaseCubeTextsOnFace(int face_type) {
        int size = mCubesBase.size();
        Cube cube;
        for (int i = 0; i < size; ++i) {
            cube = mCubesBase.get(i);
            if (null != cube.fonts[face_type]) {
                Creator.cubeFontReleased(cube.fonts[face_type]);
                cube.fonts[face_type] = null;
            }
        }
    }

    private void drawMenuCubes() {
        Graphics graphics = Game.graphics;

        graphics.resetBufferIndices();
        graphics.bindStreamSources3d();

        Color color = new Color(255, 255, 255, 255);
        MenuCube p = mMenuCubePlay;

        if (p.visible) {
            graphics.addCubeSize(p.pos.x, p.pos.y, p.pos.z, HALF_CUBE_SIZE, color);
        }

        if (m_current_cube_face == CubeFaceNamesEnum.Face_Menu ||
                m_current_cube_face == CubeFaceNamesEnum.Face_Options ||
                mNavigator.isCurrentNavigation(CubeFaceNavigationEnum.Menu_To_Options) ||
                mNavigator.isCurrentNavigation(CubeFaceNavigationEnum.Menu_To_Easy1)) {
            p = mMenuCubeOptions;
            graphics.addCubeSize(p.pos.x, p.pos.y, p.pos.z, HALF_CUBE_SIZE, color);
        }

        if (m_current_cube_face == CubeFaceNamesEnum.Face_Menu ||
                m_current_cube_face == CubeFaceNamesEnum.Face_Score ||
                mNavigator.isCurrentNavigation(CubeFaceNavigationEnum.Menu_To_Store) ||
                mNavigator.isCurrentNavigation(CubeFaceNavigationEnum.Menu_To_Easy1)) {
            p = mMenuCubeStore;
            graphics.addCubeSize(p.pos.x, p.pos.y, p.pos.z, HALF_CUBE_SIZE, color);
     }

        graphics.updateBuffers();
        graphics.renderTriangles();
    }

    private void drawLevelCubes() {
        Graphics graphics = Game.graphics;

        graphics.resetBufferIndices();
        graphics.bindStreamSources3d();

        int size;
        LevelCube levelCube;
        for (int i = 0; i < 6; ++i) {
            size = Game.ar_cubefacedata[i].lst_level_cubes.size();
            for (int j = 0; j < size; ++j) {
                levelCube = Game.ar_cubefacedata[i].lst_level_cubes.get(j);
                graphics.addCube(levelCube.pos.x, levelCube.pos.y, levelCube.pos.z);
            }
        }
        graphics.updateBuffers();
        graphics.renderTriangles();
    }

    private void drawLevelCubeDecalsEasy(ArrayList<LevelCube> lst_level_cubes_x_plus,
                                         ArrayList<LevelCube> lst_level_cubes_x_minus,
                                         ArrayList<LevelCube> lst_level_cubes_y_plus,
                                         ArrayList<LevelCube> lst_level_cubes_y_minus,
                                         LevelCubeDecalTypeEnum decal_type) {
        Graphics graphics = Game.graphics;
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
                    color.init(levelCube.color_number);
                    break;

                case LevelCubeDecalStars:
                    p = levelCube.pStars;
                    color.init(levelCube.color_stars_and_solver);
                    break;

                case LevelCubeDecalSolver:
                    p = levelCube.pSolver;
                    color.init(levelCube.color_stars_and_solver);
                    break;
            }

            if (p != null) {
                coords.tx0 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                coords.tx1 = new Vector2(p.tx_up_left.x, p.tx_up_left.y);
                coords.tx2 = new Vector2(p.tx_lo_left.x, p.tx_lo_left.y);
                coords.tx3 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);

                graphics.addCubeFace_X_Plus(levelCube.font_pos.x, levelCube.font_pos.y, levelCube.font_pos.z, coords, color);
            }
        }

        size = lst_level_cubes_x_minus.size();
        for (int i = 0; i < size; ++i) {
            levelCube = lst_level_cubes_x_minus.get(i);
            p = null;

            switch (decal_type) {
                case LevelCubeDecalNumber:
                    p = levelCube.pNumber;
                    color.init(levelCube.color_number);
                    break;

                case LevelCubeDecalStars:
                    p = levelCube.pStars;
                    color.init(levelCube.color_stars_and_solver);
                    break;

                case LevelCubeDecalSolver:
                    p = levelCube.pSolver;
                    color.init(levelCube.color_stars_and_solver);
                    break;
            }

            if (p != null) {
                coords.tx0 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                coords.tx1 = new Vector2(p.tx_up_left.x, p.tx_up_left.y);
                coords.tx2 = new Vector2(p.tx_lo_left.x, p.tx_lo_left.y);
                coords.tx3 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);

                graphics.addCubeFace_X_Minus(levelCube.font_pos.x, levelCube.font_pos.y, levelCube.font_pos.z, coords, color);
            }
        }

        size = lst_level_cubes_y_plus.size();
        for (int i = 0; i < size; ++i) {
            levelCube = lst_level_cubes_y_plus.get(i);
            p = null;

            switch (decal_type) {
                case LevelCubeDecalNumber:
                    p = levelCube.pNumber;
                    color.init(levelCube.color_number);
                    break;

                case LevelCubeDecalStars:
                    p = levelCube.pStars;
                    color.init(levelCube.color_stars_and_solver);
                    break;

                case LevelCubeDecalSolver:
                    p = levelCube.pSolver;
                    color.init(levelCube.color_stars_and_solver);
                    break;
            }

            if (p != null) {
                coords.tx0 = new Vector2(p.tx_lo_left.x, p.tx_lo_left.y);
                coords.tx1 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);
                coords.tx2 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                coords.tx3 = new Vector2(p.tx_up_left.x, p.tx_up_left.y);

                graphics.addCubeFace_Y_Plus(levelCube.font_pos.x, levelCube.font_pos.y, levelCube.font_pos.z, coords, color);
            }
        }

        size = lst_level_cubes_y_minus.size();
        for (int i = 0; i < size; ++i) {
            levelCube = lst_level_cubes_y_minus.get(i);
            p = null;

            switch (decal_type) {
                case LevelCubeDecalNumber:
                    p = levelCube.pNumber;
                    color.init(levelCube.color_number);
                    break;

                case LevelCubeDecalStars:
                    p = levelCube.pStars;
                    color.init(levelCube.color_stars_and_solver);
                    break;

                case LevelCubeDecalSolver:
                    p = levelCube.pSolver;
                    color.init(levelCube.color_stars_and_solver);
                    break;
            }

            if (p != null) {
                coords.tx0 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                coords.tx1 = new Vector2(p.tx_up_left.x, p.tx_up_left.y);
                coords.tx2 = new Vector2(p.tx_lo_left.x, p.tx_lo_left.y);
                coords.tx3 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);

                graphics.addCubeFace_Y_Minus(levelCube.font_pos.x, levelCube.font_pos.y, levelCube.font_pos.z, coords, color);
            }
        }
    }

    private void drawLevelCubeDecalsNormal(ArrayList<LevelCube> lst_level_cubes_z_plus,
                                           ArrayList<LevelCube> lst_level_cubes_z_minus,
                                           ArrayList<LevelCube> lst_level_cubes_y_plus,
                                           ArrayList<LevelCube> lst_level_cubes_y_minus,
                                           LevelCubeDecalTypeEnum decal_type) {
        Graphics graphics = Game.graphics;
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
                    color.init(levelCube.color_number);
                    break;

                case LevelCubeDecalStars:
                    p = levelCube.pStars;
                    color.init(levelCube.color_stars_and_solver);
                    break;

                case LevelCubeDecalSolver:
                    p = levelCube.pSolver;
                    color.init(levelCube.color_stars_and_solver);
                    break;
            }

            if (p != null) {
                coords.tx0 = new Vector2(p.tx_lo_left.x, p.tx_lo_left.y);
                coords.tx1 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);
                coords.tx2 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                coords.tx3 = new Vector2(p.tx_up_left.x, p.tx_up_left.y);

                graphics.addCubeFace_Z_Plus(levelCube.font_pos.x, levelCube.font_pos.y, levelCube.font_pos.z, coords, color);
            }
        }

        size = lst_level_cubes_z_minus.size();
        for (int i = 0; i < size; ++i) {
            levelCube = lst_level_cubes_z_minus.get(i);
            p = null;

            switch (decal_type) {
                case LevelCubeDecalNumber:
                    p = levelCube.pNumber;
                    color.init(levelCube.color_number);
                    break;

                case LevelCubeDecalStars:
                    p = levelCube.pStars;
                    color.init(levelCube.color_stars_and_solver);
                    break;

                case LevelCubeDecalSolver:
                    p = levelCube.pSolver;
                    color.init(levelCube.color_stars_and_solver);
                    break;
            }

            if (p != null) {
                coords.tx0 = new Vector2(p.tx_up_left.x, p.tx_up_left.y);
                coords.tx1 = new Vector2(p.tx_lo_left.x, p.tx_lo_left.y);
                coords.tx2 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);
                coords.tx3 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);

                graphics.addCubeFace_Z_Minus(levelCube.font_pos.x, levelCube.font_pos.y, levelCube.font_pos.z, coords, color);
            }
        }

        size = lst_level_cubes_y_plus.size();
        for (int i = 0; i < size; ++i) {
            levelCube = lst_level_cubes_y_plus.get(i);
            p = null;

            switch (decal_type) {
                case LevelCubeDecalNumber:
                    p = levelCube.pNumber;
                    color.init(levelCube.color_number);
                    break;

                case LevelCubeDecalStars:
                    p = levelCube.pStars;
                    color.init(levelCube.color_stars_and_solver);
                    break;

                case LevelCubeDecalSolver:
                    p = levelCube.pSolver;
                    color.init(levelCube.color_stars_and_solver);
                    break;
            }

            if (p != null) {
                coords.tx0 = new Vector2(p.tx_up_left.x, p.tx_up_left.y);
                coords.tx1 = new Vector2(p.tx_lo_left.x, p.tx_lo_left.y);
                coords.tx2 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);
                coords.tx3 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);

                graphics.addCubeFace_Y_Plus(levelCube.font_pos.x, levelCube.font_pos.y, levelCube.font_pos.z, coords, color);
            }
        }

        size = lst_level_cubes_y_minus.size();
        for (int i = 0; i < size; ++i) {
            levelCube = lst_level_cubes_y_minus.get(i);
            p = null;

            switch (decal_type) {
                case LevelCubeDecalNumber:
                    p = levelCube.pNumber;
                    color.init(levelCube.color_number);
                    break;

                case LevelCubeDecalStars:
                    p = levelCube.pStars;
                    color.init(levelCube.color_stars_and_solver);
                    break;

                case LevelCubeDecalSolver:
                    p = levelCube.pSolver;
                    color.init(levelCube.color_stars_and_solver);
                    break;
            }

            if (p != null) {
                coords.tx0 = new Vector2(p.tx_up_left.x, p.tx_up_left.y);
                coords.tx1 = new Vector2(p.tx_lo_left.x, p.tx_lo_left.y);
                coords.tx2 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);
                coords.tx3 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);

                graphics.addCubeFace_Y_Minus(levelCube.font_pos.x, levelCube.font_pos.y, levelCube.font_pos.z, coords, color);
            }
        }
    }

    private void drawLevelCubeDecalsHard(ArrayList<LevelCube> lst_level_cubes_x_plus,
                                         ArrayList<LevelCube> lst_level_cubes_x_minus,
                                         ArrayList<LevelCube> lst_level_cubes_y_plus,
                                         ArrayList<LevelCube> lst_level_cubes_y_minus,
                                         LevelCubeDecalTypeEnum decal_type) {
        Graphics graphics = Game.graphics;
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
                    color.init(levelCube.color_number);
                    break;

                case LevelCubeDecalStars:
                    p = levelCube.pStars;
                    color.init(levelCube.color_stars_and_solver);
                    break;

                case LevelCubeDecalSolver:
                    p = levelCube.pSolver;
                    color.init(levelCube.color_stars_and_solver);
                    break;
            }

            if (p != null) {
                coords.tx0 = new Vector2(p.tx_lo_left.x, p.tx_lo_left.y);
                coords.tx1 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);
                coords.tx2 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                coords.tx3 = new Vector2(p.tx_up_left.x, p.tx_up_left.y);

                graphics.addCubeFace_X_Plus(levelCube.font_pos.x, levelCube.font_pos.y, levelCube.font_pos.z, coords, color);
            }
        }

        size = lst_level_cubes_x_minus.size();
        for (int i = 0; i < size; ++i) {
            levelCube = lst_level_cubes_x_minus.get(i);
            p = null;

            switch (decal_type) {
                case LevelCubeDecalNumber:
                    p = levelCube.pNumber;
                    color.init(levelCube.color_number);
                    break;

                case LevelCubeDecalStars:
                    p = levelCube.pStars;
                    color.init(levelCube.color_stars_and_solver);
                    break;

                case LevelCubeDecalSolver:
                    p = levelCube.pSolver;
                    color.init(levelCube.color_stars_and_solver);
                    break;
            }

            if (p != null) {
                coords.tx0 = new Vector2(p.tx_lo_left.x, p.tx_lo_left.y);
                coords.tx1 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);
                coords.tx2 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                coords.tx3 = new Vector2(p.tx_up_left.x, p.tx_up_left.y);

                graphics.addCubeFace_X_Minus(levelCube.font_pos.x, levelCube.font_pos.y, levelCube.font_pos.z, coords, color);
            }
        }

        size = lst_level_cubes_y_plus.size();
        for (int i = 0; i < size; ++i) {
            levelCube = lst_level_cubes_y_plus.get(i);
            p = null;

            switch (decal_type) {
                case LevelCubeDecalNumber:
                    p = levelCube.pNumber;
                    color.init(levelCube.color_number);
                    break;

                case LevelCubeDecalStars:
                    p = levelCube.pStars;
                    color.init(levelCube.color_stars_and_solver);
                    break;

                case LevelCubeDecalSolver:
                    p = levelCube.pSolver;
                    color.init(levelCube.color_stars_and_solver);
                    break;
            }

            if (p != null) {
                coords.tx0 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                coords.tx1 = new Vector2(p.tx_up_left.x, p.tx_up_left.y);
                coords.tx2 = new Vector2(p.tx_lo_left.x, p.tx_lo_left.y);
                coords.tx3 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);

                graphics.addCubeFace_Y_Plus(levelCube.font_pos.x, levelCube.font_pos.y, levelCube.font_pos.z, coords, color);
            }
        }

        size = lst_level_cubes_y_minus.size();
        for (int i = 0; i < size; ++i) {
            levelCube = lst_level_cubes_y_minus.get(i);
            p = null;

            switch (decal_type) {
                case LevelCubeDecalNumber:
                    p = levelCube.pNumber;
                    color.init(levelCube.color_number);
                    break;

                case LevelCubeDecalStars:
                    p = levelCube.pStars;
                    color.init(levelCube.color_stars_and_solver);
                    break;

                case LevelCubeDecalSolver:
                    p = levelCube.pSolver;
                    color.init(levelCube.color_stars_and_solver);
                    break;
            }

            if (p != null) {
                coords.tx0 = new Vector2(p.tx_lo_left.x, p.tx_lo_left.y);
                coords.tx1 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);
                coords.tx2 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                coords.tx3 = new Vector2(p.tx_up_left.x, p.tx_up_left.y);

                graphics.addCubeFace_Y_Minus(levelCube.font_pos.x, levelCube.font_pos.y, levelCube.font_pos.z, coords, color);
            }
        }
    }

    private void drawLevelNumbers() {
        Graphics graphics = Game.graphics;
        graphics.resetBufferIndices();
        graphics.bindStreamSources3d();

        switch (m_current_cube_face) {
            case Face_Easy01:
            case Face_Easy02:
            case Face_Easy03:
            case Face_Easy04:
                drawLevelCubeDecalsEasy(Game.ar_cubefacedata[Face_X_Plus].lst_level_cubes,
                        Game.ar_cubefacedata[Face_X_Minus].lst_level_cubes,
                        Game.ar_cubefacedata[Face_Y_Plus].lst_level_cubes,
                        Game.ar_cubefacedata[Face_Y_Minus].lst_level_cubes,
                        LevelCubeDecalTypeEnum.LevelCubeDecalNumber);
                break;

            case Face_Normal01:
            case Face_Normal02:
            case Face_Normal03:
            case Face_Normal04:
                drawLevelCubeDecalsNormal(Game.ar_cubefacedata[Face_Z_Plus].lst_level_cubes,
                        Game.ar_cubefacedata[Face_Z_Minus].lst_level_cubes,
                        Game.ar_cubefacedata[Face_Y_Plus].lst_level_cubes,
                        Game.ar_cubefacedata[Face_Y_Minus].lst_level_cubes,
                        LevelCubeDecalTypeEnum.LevelCubeDecalNumber);
                break;

            case Face_Hard01:
            case Face_Hard02:
            case Face_Hard03:
            case Face_Hard04:
                drawLevelCubeDecalsHard(Game.ar_cubefacedata[Face_X_Plus].lst_level_cubes,
                        Game.ar_cubefacedata[Face_X_Minus].lst_level_cubes,
                        Game.ar_cubefacedata[Face_Y_Plus].lst_level_cubes,
                        Game.ar_cubefacedata[Face_Y_Minus].lst_level_cubes,
                        LevelCubeDecalTypeEnum.LevelCubeDecalNumber);
                break;

            default:
                break;
        }
        graphics.updateBuffers();
        graphics.renderTriangles();
    }

    private void drawTexts() {
        Graphics graphics = Game.graphics;
        graphics.resetBufferIndices();

        if (Math.abs(mNavigator.m_cube_rotation_secondary.degree) < EPSILON) {
            drawTextsDefaultOrientation(mTexts.get(Face_X_Plus),
                    mTexts.get(Face_X_Minus),
                    mTexts.get(Face_Y_Plus),
                    mTexts.get(Face_Y_Minus),
                    mTexts.get(Face_Z_Plus),
                    mTexts.get(Face_Z_Minus));
        } else {
            switch (m_current_cube_face) {
                case Face_Easy01:
                case Face_Easy02:
                case Face_Easy03:
                case Face_Easy04:
                    drawEasyTitles(mTexts.get(Face_X_Plus), mTexts.get(Face_X_Minus),
                            mTexts.get(Face_Y_Plus), mTexts.get(Face_Y_Minus),
                            mTexts.get(Face_Z_Plus), mTexts.get(Face_Z_Minus));
                    break;

                case Face_Normal01:
                case Face_Normal02:
                case Face_Normal03:
                case Face_Normal04:
                    drawNormalTitles(mTexts.get(Face_X_Plus), mTexts.get(Face_X_Minus),
                            mTexts.get(Face_Y_Plus), mTexts.get(Face_Y_Minus),
                            mTexts.get(Face_Z_Plus), mTexts.get(Face_Z_Minus));
                    break;

                case Face_Hard01:
                case Face_Hard02:
                case Face_Hard03:
                case Face_Hard04:
                    drawHardTitles(mTexts.get(Face_X_Plus), mTexts.get(Face_X_Minus),
                            mTexts.get(Face_Y_Plus), mTexts.get(Face_Y_Minus),
                            mTexts.get(Face_Z_Plus), mTexts.get(Face_Z_Minus));
                    break;

                default:
                    break;
            }
        }

        CubeFont cubeFont;
        TexturedQuad font;
        TexCoordsQuad coords = new TexCoordsQuad();

        // Draw CubeFonts on Red Cubes [P]lay [O]ptions [S]tore [U]nlock [R]estore
        if (mNavigator.isCurrentNavigation(CubeFaceNavigationEnum.Menu_To_Easy1) || m_current_cube_face == CubeFaceNamesEnum.Face_Menu || m_current_cube_face == CubeFaceNamesEnum.Face_Options || m_current_cube_face == CubeFaceNamesEnum.Face_Score) {

            Color color = new Color(Game.fontColorOnMenuCube);

            if (mMenuCubePlay.isDone() && mMenuCubePlay.cubeLocation.x == 1) {
                cubeFont = m_cubefont_play;
                font = cubeFont.getFont();

                if (font != null) {
                    coords.tx0 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
                    coords.tx1 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
                    coords.tx2 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
                    coords.tx3 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);

                    graphics.addCubeFace_Z_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
                }
            }

            if (mMenuCubeOptions.isDone() && mMenuCubeOptions.cubeLocation.x == 1) {
                cubeFont = m_cubefont_options;
                font = cubeFont.getFont();

                if (font != null) {
                    coords.tx0 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
                    coords.tx1 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
                    coords.tx2 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
                    coords.tx3 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);

                    graphics.addCubeFace_Z_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
                }
            }

            if (mMenuCubeStore.isDone() && mMenuCubeStore.cubeLocation.x == 1) {
                cubeFont = m_cubefont_store;
                font = cubeFont.getFont();

                if (font != null) {
                    coords.tx0 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
                    coords.tx1 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
                    coords.tx2 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
                    coords.tx3 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);

                    graphics.addCubeFace_Z_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
                }
            }

            // TODO: here
//            if (m_pStoreCubeRestore.isDone() && m_pStoreCubeRestore.cubeLocation.x == 1) {
//                cubeFont = m_cubefont_restore;
//                font = cubeFont.getFont();
//
//                if (font != null) {
//                    coords.tx0 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
//                    coords.tx1 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
//                    coords.tx2 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
//                    coords.tx3 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
//
//                    graphics.addCubeFace_Y_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
//                }
//            }
        }

        graphics.updateBuffers();
        graphics.renderTriangles();
    }

    private void drawTextsTitles() {
        Graphics graphics = Game.graphics;
        graphics.resetBufferIndices();

        if (Math.abs(mNavigator.m_cube_rotation_secondary.degree) < EPSILON) {
            drawTextsDefaultOrientation(mTitles.get(Face_X_Plus), mTitles.get(Face_X_Minus),
                    mTitles.get(Face_Y_Plus), mTitles.get(Face_Y_Minus),
                    mTitles.get(Face_Z_Plus), mTitles.get(Face_Z_Minus));
        } else {
            switch (m_current_cube_face) {
                case Face_Easy01:
                case Face_Easy02:
                case Face_Easy03:
                case Face_Easy04:
                    drawEasyTitles(mTitles.get(Face_X_Plus), mTitles.get(Face_X_Minus),
                            mTitles.get(Face_Y_Plus), mTitles.get(Face_Y_Minus),
                            mTitles.get(Face_Z_Plus), mTitles.get(Face_Z_Minus));
                    break;

                case Face_Normal01:
                case Face_Normal02:
                case Face_Normal03:
                case Face_Normal04:
                    drawNormalTitles(mTitles.get(Face_X_Plus), mTitles.get(Face_X_Minus),
                            mTitles.get(Face_Y_Plus), mTitles.get(Face_Y_Minus),
                            mTitles.get(Face_Z_Plus), mTitles.get(Face_Z_Minus));
                    break;

                case Face_Hard01:
                case Face_Hard02:
                case Face_Hard03:
                case Face_Hard04:
                    drawHardTitles(mTitles.get(Face_X_Plus), mTitles.get(Face_X_Minus),
                            mTitles.get(Face_Y_Plus), mTitles.get(Face_Y_Minus),
                            mTitles.get(Face_Z_Plus), mTitles.get(Face_Z_Minus));
                    break;

                default:
                    break;
            }
        }
        graphics.updateBuffers();
        graphics.renderTriangles();
    }

    private void drawTextsDefaultOrientation(ArrayList<CubeFont> lst_x_plus,
                                            ArrayList<CubeFont> lst_x_minus,
                                            ArrayList<CubeFont> lst_y_plus,
                                            ArrayList<CubeFont> lst_y_minus,
                                            ArrayList<CubeFont> lst_z_plus,
                                            ArrayList<CubeFont> lst_z_minus) {
        Graphics graphics = Game.graphics;
        CubeFont cubeFont;
        TexturedQuad font;
        TexCoordsQuad coords = new TexCoordsQuad();
        int size;

        size = lst_z_plus.size();
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_z_plus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx1 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx2 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx3 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);

            graphics.addCubeFace_Z_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        size = lst_x_plus.size();
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_x_plus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx1 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx2 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx3 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);

            graphics.addCubeFace_X_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        size = lst_z_minus.size();
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_z_minus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx1 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx2 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx3 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);

            graphics.addCubeFace_Z_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        size = lst_x_minus.size();
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_x_minus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx1 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx2 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx3 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);

            graphics.addCubeFace_X_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        size = lst_y_plus.size();
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_y_plus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx1 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx2 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx3 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);

            graphics.addCubeFace_Y_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        size = lst_y_minus.size();
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_y_minus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx1 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx2 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx3 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);

            graphics.addCubeFace_Y_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }
    }

    private void drawEasyTitles(ArrayList<CubeFont> lst_x_plus,
                               ArrayList<CubeFont> lst_x_minus,
                               ArrayList<CubeFont> lst_y_plus,
                               ArrayList<CubeFont> lst_y_minus,
                               ArrayList<CubeFont> lst_z_plus,
                               ArrayList<CubeFont> lst_z_minus) {
        Graphics graphics = Game.graphics;
        CubeFont cubeFont;
        TexturedQuad font;
        TexCoordsQuad coords = new TexCoordsQuad();
        int size;

        size = lst_x_plus.size();
        // Face_X_Plus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_x_plus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx1 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx2 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx3 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);

            graphics.addCubeFace_X_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        size = lst_y_minus.size();
        // Face_Y_Minus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_y_minus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx1 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx2 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx3 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);

            graphics.addCubeFace_Y_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        size = lst_x_minus.size();
        // Face_X_Minus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_x_minus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx1 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx2 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx3 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);

            graphics.addCubeFace_X_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        size = lst_y_plus.size();
        // Face_Y_Plus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_y_plus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx1 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx2 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx3 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);

            graphics.addCubeFace_Y_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }
    }

    private void drawNormalTitles(ArrayList<CubeFont> lst_x_plus,
                                 ArrayList<CubeFont> lst_x_minus,
                                 ArrayList<CubeFont> lst_y_plus,
                                 ArrayList<CubeFont> lst_y_minus,
                                 ArrayList<CubeFont> lst_z_plus,
                                 ArrayList<CubeFont> lst_z_minus) {
        Graphics graphics = Game.graphics;
        CubeFont cubeFont;
        TexturedQuad font;
        TexCoordsQuad coords = new TexCoordsQuad();
        int size;

        size = lst_z_minus.size();
        // Face_Z_Minus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_z_minus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx1 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx2 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx3 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);

            graphics.addCubeFace_Z_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        size = lst_y_minus.size();
        // Face_Y_Minus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_y_minus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx1 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx2 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx3 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);

            graphics.addCubeFace_Y_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        size = lst_z_plus.size();
        // Face_Z_Plus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_z_plus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx1 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx2 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx3 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);

            graphics.addCubeFace_Z_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        size = lst_y_plus.size();
        // Face_Y_Plus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_y_plus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx1 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx2 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx3 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);

            graphics.addCubeFace_Y_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }
    }

    private void drawHardTitles(ArrayList<CubeFont> lst_x_plus,
                               ArrayList<CubeFont> lst_x_minus,
                               ArrayList<CubeFont> lst_y_plus,
                               ArrayList<CubeFont> lst_y_minus,
                               ArrayList<CubeFont> lst_z_plus,
                               ArrayList<CubeFont> lst_z_minus) {
        Graphics graphics = Game.graphics;
        CubeFont cubeFont;
        TexturedQuad font;
        TexCoordsQuad coords = new TexCoordsQuad();
        int size;

        size = lst_x_minus.size();
        // Face_X_Minus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_x_minus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx1 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx2 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx3 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);

            graphics.addCubeFace_X_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        size = lst_y_minus.size();
        // Face_Y_Minus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_y_minus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx1 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx2 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx3 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);

            graphics.addCubeFace_Y_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        size = lst_x_plus.size();
        // Face_X_Plus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_x_plus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx1 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx2 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx3 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);

            graphics.addCubeFace_X_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }

        size = lst_y_plus.size();
        // Face_Y_Plus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_y_plus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx1 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx2 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx3 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);

            graphics.addCubeFace_Y_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.colorCurrent);
        }
    }

    private void drawSymbols() {
        Graphics graphics = Game.graphics;
        graphics.resetBufferIndices();
        graphics.bindStreamSources3d();

        drawTextsDefaultOrientation(mSymbols.get(Face_X_Plus),
                mSymbols.get(Face_X_Minus),
                mSymbols.get(Face_Y_Plus),
                mSymbols.get(Face_Y_Minus),
                mSymbols.get(Face_Z_Plus),
                mSymbols.get(Face_Z_Minus));

        graphics.updateBuffers();
        graphics.renderTriangles();
    }

    private void drawCubeFaceOptions() {
        Graphics graphics = Game.graphics;
        Cube p = Game.cubes[2][7][3];

        float x = p.tx - HALF_CUBE_SIZE;
        float y = p.ty + HALF_CUBE_SIZE + 0.02f;
        float z = p.tz - HALF_CUBE_SIZE;
        float w = CUBE_SIZE * 5.0f * Game.options.getMusicVolume();
        float ws = CUBE_SIZE * 5.0f * Game.options.getSoundVolume();
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

        graphics.bindStreamSources3dNoTexture();
        graphics.resetBufferIndices();
        graphics.addVerticesCoordsNormalsColors(verts, coords, norms, colors);

        glDisable(GL_TEXTURE_2D);

        glDrawArrays(GL_TRIANGLE_FAN, 0, 4); // progress bar music
        glDrawArrays(GL_TRIANGLE_FAN, 4, 4); // progress bar soundfx

        glEnable(GL_TEXTURE_2D);
    }

    private void drawLevelCubeSymbols() {
        Graphics graphics = Game.graphics;
        graphics.resetBufferIndices();
        graphics.bindStreamSources3d();

        switch (m_current_cube_face) {
            case Face_Easy01:
            case Face_Easy02:
            case Face_Easy03:
            case Face_Easy04:
                drawLevelCubeDecalsEasy(Game.ar_cubefacedata[Face_X_Plus].lst_level_cubes,
                        Game.ar_cubefacedata[Face_X_Minus].lst_level_cubes,
                        Game.ar_cubefacedata[Face_Y_Plus].lst_level_cubes,
                        Game.ar_cubefacedata[Face_Y_Minus].lst_level_cubes,
                        LevelCubeDecalTypeEnum.LevelCubeDecalStars);

                drawLevelCubeDecalsEasy(Game.ar_cubefacedata[Face_X_Plus].lst_level_cubes,
                        Game.ar_cubefacedata[Face_X_Minus].lst_level_cubes,
                        Game.ar_cubefacedata[Face_Y_Plus].lst_level_cubes,
                        Game.ar_cubefacedata[Face_Y_Minus].lst_level_cubes,
                        LevelCubeDecalTypeEnum.LevelCubeDecalSolver);
                break;

            case Face_Normal01:
            case Face_Normal02:
            case Face_Normal03:
            case Face_Normal04:
                drawLevelCubeDecalsNormal(Game.ar_cubefacedata[Face_Z_Plus].lst_level_cubes,
                        Game.ar_cubefacedata[Face_Z_Minus].lst_level_cubes,
                        Game.ar_cubefacedata[Face_Y_Plus].lst_level_cubes,
                        Game.ar_cubefacedata[Face_Y_Minus].lst_level_cubes,
                        LevelCubeDecalTypeEnum.LevelCubeDecalStars);

                drawLevelCubeDecalsNormal(Game.ar_cubefacedata[Face_Z_Plus].lst_level_cubes,
                        Game.ar_cubefacedata[Face_Z_Minus].lst_level_cubes,
                        Game.ar_cubefacedata[Face_Y_Plus].lst_level_cubes,
                        Game.ar_cubefacedata[Face_Y_Minus].lst_level_cubes,
                        LevelCubeDecalTypeEnum.LevelCubeDecalSolver);
                break;

            case Face_Hard01:
            case Face_Hard02:
            case Face_Hard03:
            case Face_Hard04:
                drawLevelCubeDecalsHard(Game.ar_cubefacedata[Face_X_Plus].lst_level_cubes,
                        Game.ar_cubefacedata[Face_X_Minus].lst_level_cubes,
                        Game.ar_cubefacedata[Face_Y_Plus].lst_level_cubes,
                        Game.ar_cubefacedata[Face_Y_Minus].lst_level_cubes,
                        LevelCubeDecalTypeEnum.LevelCubeDecalStars);

                drawLevelCubeDecalsHard(Game.ar_cubefacedata[Face_X_Plus].lst_level_cubes,
                        Game.ar_cubefacedata[Face_X_Minus].lst_level_cubes,
                        Game.ar_cubefacedata[Face_Y_Plus].lst_level_cubes,
                        Game.ar_cubefacedata[Face_Y_Minus].lst_level_cubes,
                        LevelCubeDecalTypeEnum.LevelCubeDecalSolver);
                break;

            default:
                break;
        }
        graphics.updateBuffers();
        graphics.renderTriangles();
    }

    private void drawCredits() {
        Graphics graphics = Game.graphics;
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

        graphics.addVerticesCoordsNormalsColors(verts, coords, norms, colors_white);
        glPushMatrix();
            glTranslatef(0.0f, -5.7f, m_credits_offset);
            glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
            glScalef(11.0f, 11.0f, 1.0f);
            glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
        glPopMatrix();

        graphics.addVerticesCoordsNormalsColors(verts, coords, norms, colors_black);
        glPushMatrix();
            glTranslatef(0.0f, -5.6f, -10.0f);
            glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
            glScalef(11.0f, 11.0f, 1.0f);
            glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
        glPopMatrix();
    }

    private void drawTheCube() {
        Graphics graphics = Game.graphics;
        int size;
        Cube cube;

        graphics.resetBufferIndices();
        graphics.zeroBufferPositions();
        graphics.bindStreamSources3d();
        size = mCubesBase.size();
        for (int i = 0; i < size; ++i) {
            cube = mCubesBase.get(i);
            graphics.addCubeWithColor(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
        }
        graphics.updateBuffers();
        graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);

        graphics.resetBufferIndices();
        size = mCubesFace.size();
        for (int i = 0; i < size; ++i) {
            cube = mCubesFace.get(i);
            graphics.addCubeWithColor(cube.tx, cube.ty, cube.tz, cube.colorCurrent);
        }
        graphics.updateBuffers();
        graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);
    }

    private void drawCubeHiLite(Color color) {
        Graphics graphics = Game.graphics;
        TexCoordsQuad coords = new TexCoordsQuad();

        TexturedQuad p = m_font_hilite.getFont();
        coords.tx0 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
        coords.tx1 = new Vector2(p.tx_up_left.x, p.tx_up_left.y);
        coords.tx2 = new Vector2(p.tx_lo_left.x, p.tx_lo_left.y);
        coords.tx3 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);

        graphics.resetBufferIndices();
        graphics.bindStreamSources3d();

        switch (m_current_cube_face_type) {
            case Face_X_Plus:
                graphics.addCubeFace_X_Plus(m_font_hilite.pos.x, m_font_hilite.pos.y, m_font_hilite.pos.z, coords, color);
                break;

            case Face_X_Minus:
                graphics.addCubeFace_X_Minus(m_font_hilite.pos.x, m_font_hilite.pos.y, m_font_hilite.pos.z, coords, color);
                break;

            case Face_Y_Plus:
                graphics.addCubeFace_Y_Plus(m_font_hilite.pos.x, m_font_hilite.pos.y, m_font_hilite.pos.z, coords, color);
                break;

            case Face_Y_Minus:
                graphics.addCubeFace_Y_Minus(m_font_hilite.pos.x, m_font_hilite.pos.y, m_font_hilite.pos.z, coords, color);
                break;

            case Face_Z_Plus:
                graphics.addCubeFace_Z_Plus(m_font_hilite.pos.x, m_font_hilite.pos.y, m_font_hilite.pos.z, coords, color);
                break;

            case Face_Z_Minus:
                graphics.addCubeFace_Z_Minus(m_font_hilite.pos.x, m_font_hilite.pos.y, m_font_hilite.pos.z, coords, color);
                break;

            default:
                break;
        }
        graphics.updateBuffers();
        graphics.renderTriangles();
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
            switch (m_current_cube_face) {
                case Face_Tutorial:
                    if (mMenuCubePlay.cubeLocation.z == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Menu;
                        setCurrentCubeFaceType(Face_Z_Plus);
                        mMenuCubePlay.moveOnAxis(AxisMovement_X_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Tutorial_To_Menu);
                    }

                    if (mMenuCubePlay.cubeLocation.y == 7 && m_can_alter_text) {
                        m_can_alter_text = false;
                        MenuFaceBuilder.resetTransforms();
                        MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
                        releaseCubeTextsOnFace(Face_X_Minus);
                        MenuFaceBuilder.buildTexts(CubeFaceNamesEnum.Face_Tutorial, Face_X_Minus, true);
                    }

                    if (mMenuCubePlay.cubeLocation.y == 1 && m_can_alter_text) {
                        m_can_alter_text = false;
                        MenuFaceBuilder.resetTransforms();
                        MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
                        releaseCubeTextsOnFace(Face_X_Minus);
                        MenuFaceBuilder.buildTexts(CubeFaceNamesEnum.Face_Tutorial, Face_X_Minus, false);
                    }
                    break;

                case Face_Menu:
                    if (mMenuCubePlay.cubeLocation.x == 2 && mMenuCubePlay.cubeLocation.y == 7 && mMenuCubePlay.cubeLocation.z == 8) {
                        mMenuCubePlay.moveOnAxis(AxisMovement_Y_Minus);
                    }

                    if (mMenuCubePlay.cubeLocation.x == 2 && mMenuCubePlay.cubeLocation.y == 5 && mMenuCubePlay.cubeLocation.z == 8) {
                        mMenuCubePlay.moveOnAxis(AxisMovement_X_Minus);
                    }

                    if (mMenuCubePlay.cubeLocation.x == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Easy01;
                        setCurrentCubeFaceType(Face_X_Plus);
                        mMenuCubePlay.moveOnAxis(AxisMovement_Z_Minus);
                        mNavigator.setup(CubeFaceNavigationEnum.Menu_To_Easy1);
                    }
                    break;

                case Face_Easy01:
                    if (mMenuCubePlay.cubeLocation.z == 0) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Normal01;
                        setCurrentCubeFaceType(Face_Z_Minus);
                        mMenuCubePlay.moveOnAxis(AxisMovement_X_Minus);
                        mNavigator.setup(CubeFaceNavigationEnum.Easy1_To_Normal1);
                    }

                    if (mMenuCubePlay.cubeLocation.z == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Menu;
                        setCurrentCubeFaceType(Face_Z_Plus);
                        mMenuCubePlay.moveOnAxis(AxisMovement_X_Minus);
                        mNavigator.setup(CubeFaceNavigationEnum.Easy1_To_Menu);
                    }

                    if (mMenuCubePlay.cubeLocation.y == 0) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Easy02;
                        setCurrentCubeFaceType(Face_Y_Minus);
                        mMenuCubePlay.moveOnAxis(AxisMovement_X_Minus);
                        mNavigator.setup(CubeFaceNavigationEnum.Easy1_To_Easy2);
                    }

                    if (mMenuCubePlay.cubeLocation.y == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Easy04;
                        setCurrentCubeFaceType(Face_Y_Plus);
                        mMenuCubePlay.moveOnAxis(AxisMovement_X_Minus);
                        mNavigator.setup(CubeFaceNavigationEnum.Easy1_To_Easy4);
                    }
                    break;

                case Face_Easy02:
                    if (mMenuCubePlay.cubeLocation.x == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Easy01;
                        setCurrentCubeFaceType(Face_X_Plus);
                        mMenuCubePlay.moveOnAxis(AxisMovement_Y_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Easy2_To_Easy1);
                    }

                    if (mMenuCubePlay.cubeLocation.x == 0) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Easy03;
                        setCurrentCubeFaceType(Face_X_Minus);
                        mMenuCubePlay.moveOnAxis(AxisMovement_Y_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Easy2_To_Easy3);
                    }
                    break;

                case Face_Easy03:
                    if (mMenuCubePlay.cubeLocation.y == 0) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Easy02;
                        setCurrentCubeFaceType(Face_Y_Minus);
                        mMenuCubePlay.moveOnAxis(AxisMovement_X_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Easy3_To_Easy2);
                    }

                    if (mMenuCubePlay.cubeLocation.y == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Easy04;
                        setCurrentCubeFaceType(Face_Y_Plus);
                        mMenuCubePlay.moveOnAxis(AxisMovement_X_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Easy3_To_Easy4);
                    }
                    break;

                case Face_Easy04:
                    if (mMenuCubePlay.cubeLocation.x == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Easy01;
                        setCurrentCubeFaceType(Face_X_Plus);
                        mMenuCubePlay.moveOnAxis(AxisMovement_Y_Minus);
                        mNavigator.setup(CubeFaceNavigationEnum.Easy4_To_Easy1);
                    }

                    if (mMenuCubePlay.cubeLocation.x == 0) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Easy03;
                        setCurrentCubeFaceType(Face_X_Minus);
                        mMenuCubePlay.moveOnAxis(AxisMovement_Y_Minus);
                        mNavigator.setup(CubeFaceNavigationEnum.Easy4_To_Easy3);
                    }
                    break;

                case Face_Normal01:
                    if (mMenuCubePlay.cubeLocation.x == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Easy01;
                        setCurrentCubeFaceType(Face_X_Plus);
                        mMenuCubePlay.moveOnAxis(AxisMovement_Z_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Normal1_To_Easy1);
                    }

                    if (mMenuCubePlay.cubeLocation.x == 0) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Hard01;
                        setCurrentCubeFaceType(Face_X_Minus);
                        mMenuCubePlay.moveOnAxis(AxisMovement_Z_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Normal1_To_Hard1);
                    }

                    if (mMenuCubePlay.cubeLocation.y == 0) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Normal02;
                        setCurrentCubeFaceType(Face_Y_Minus);
                        mMenuCubePlay.moveOnAxis(AxisMovement_Z_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Normal1_To_Normal2);
                    }

                    if (mMenuCubePlay.cubeLocation.y == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Normal04;
                        setCurrentCubeFaceType(Face_Y_Plus);
                        mMenuCubePlay.moveOnAxis(AxisMovement_Z_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Normal1_To_Normal4);
                    }
                    break;

                case Face_Normal02:
                    if (mMenuCubePlay.cubeLocation.z == 0) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Normal01;
                        setCurrentCubeFaceType(Face_Z_Minus);
                        mMenuCubePlay.moveOnAxis(AxisMovement_Y_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Normal2_To_Normal1);
                    }

                    if (mMenuCubePlay.cubeLocation.z == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Normal03;
                        setCurrentCubeFaceType(Face_Z_Plus);
                        mMenuCubePlay.moveOnAxis(AxisMovement_Y_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Normal2_To_Normal3);
                    }
                    break;

                case Face_Normal03:
                    if (mMenuCubePlay.cubeLocation.y == 0) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Normal02;
                        setCurrentCubeFaceType(Face_Y_Minus);
                        mMenuCubePlay.moveOnAxis(AxisMovement_Z_Minus);
                        mNavigator.setup(CubeFaceNavigationEnum.Normal3_To_Normal2);
                    }

                    if (mMenuCubePlay.cubeLocation.y == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Normal04;
                        setCurrentCubeFaceType(Face_Y_Plus);
                        mMenuCubePlay.moveOnAxis(AxisMovement_Z_Minus);
                        mNavigator.setup(CubeFaceNavigationEnum.Normal3_To_Normal4);
                    }
                    break;

                case Face_Normal04:
                    if (mMenuCubePlay.cubeLocation.z == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Normal03;
                        setCurrentCubeFaceType(Face_Z_Plus);
                        mMenuCubePlay.moveOnAxis(AxisMovement_Y_Minus);
                        mNavigator.setup(CubeFaceNavigationEnum.Normal4_To_Normal3);
                    }

                    if (mMenuCubePlay.cubeLocation.z == 0) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Normal01;
                        setCurrentCubeFaceType(Face_Z_Minus);
                        mMenuCubePlay.moveOnAxis(AxisMovement_Y_Minus);
                        mNavigator.setup(CubeFaceNavigationEnum.Normal4_To_Normal1);
                    }
                    break;

                case Face_Hard01:
                    if (mMenuCubePlay.cubeLocation.z == 0) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Normal01;
                        setCurrentCubeFaceType(Face_Z_Minus);
                        mMenuCubePlay.moveOnAxis(AxisMovement_X_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Hard1_To_Normal1);
                    }

                    if (mMenuCubePlay.cubeLocation.z == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Menu;
                        setCurrentCubeFaceType(Face_Z_Plus);
                        mMenuCubePlay.moveOnAxis(AxisMovement_X_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Hard1_To_Menu);
                    }

                    if (mMenuCubePlay.cubeLocation.y == 0) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Hard02;
                        setCurrentCubeFaceType(Face_Y_Minus);
                        mMenuCubePlay.moveOnAxis(AxisMovement_X_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Hard1_To_Hard2);
                    }

                    if (mMenuCubePlay.cubeLocation.y == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Hard04;
                        setCurrentCubeFaceType(Face_Y_Plus);
                        mMenuCubePlay.moveOnAxis(AxisMovement_X_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Hard1_To_Hard4);
                    }
                    break;

                case Face_Hard02:
                    if (mMenuCubePlay.cubeLocation.x == 0) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Hard01;
                        setCurrentCubeFaceType(Face_X_Minus);
                        mMenuCubePlay.moveOnAxis(AxisMovement_Y_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Hard2_To_Hard1);
                    }

                    if (mMenuCubePlay.cubeLocation.x == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Hard03;
                        setCurrentCubeFaceType(Face_X_Plus);
                        mMenuCubePlay.moveOnAxis(AxisMovement_Y_Plus);
                        mNavigator.setup(CubeFaceNavigationEnum.Hard2_To_Hard3);
                    }
                    break;

                case Face_Hard03:
                    if (mMenuCubePlay.cubeLocation.y == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Hard04;
                        setCurrentCubeFaceType(Face_Y_Plus);
                        mMenuCubePlay.moveOnAxis(AxisMovement_X_Minus);
                        mNavigator.setup(CubeFaceNavigationEnum.Hard3_To_Hard4);
                    }

                    if (mMenuCubePlay.cubeLocation.y == 0) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Hard02;
                        setCurrentCubeFaceType(Face_Y_Minus);
                        mMenuCubePlay.moveOnAxis(AxisMovement_X_Minus);
                        mNavigator.setup(CubeFaceNavigationEnum.Hard3_To_Hard2);
                    }
                    break;

                case Face_Hard04:
                    if (mMenuCubePlay.cubeLocation.x == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Hard03;
                        setCurrentCubeFaceType(Face_X_Plus);
                        mMenuCubePlay.moveOnAxis(AxisMovement_Y_Minus);
                        mNavigator.setup(CubeFaceNavigationEnum.Hard4_To_Hard3);
                    }

                    if (mMenuCubePlay.cubeLocation.x == 0) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Hard01;
                        setCurrentCubeFaceType(Face_X_Minus);
                        mMenuCubePlay.moveOnAxis(AxisMovement_Y_Minus);
                        mNavigator.setup(CubeFaceNavigationEnum.Hard4_To_Hard1);
                    }
                    break;

                default:
                    break;
            }
        } else {
            mMenuCubePlay.update();
            m_can_alter_text = true;
        }
    }

    private void updateOptionsCube() {
        if (mMenuCubeOptions.isDone()) {
            switch (m_current_cube_face) {
                case Face_Menu:
                    if (mMenuCubeOptions.cubeLocation.y == 8) {
                        m_prev_face = CubeFaceNamesEnum.Face_Menu;
                        m_current_cube_face = CubeFaceNamesEnum.Face_Options;
                        setCurrentCubeFaceType(Face_Y_Plus);
                        mMenuCubeOptions.moveOnAxis(AxisMovement_Z_Minus);

                        mNavigator.setup(CubeFaceNavigationEnum.Menu_To_Options);
                    }

                    if (mMenuCubeOptions.cubeLocation.x == 7) {
                        if (CubeFaceNamesEnum.Face_Options == m_prev_face) {
                            m_prev_face = CubeFaceNamesEnum.Face_Empty;
                            mMenuCubeOptions.moveOnAxis(AxisMovement_X_Minus);
                        }
                    }
                    break;

                case Face_Options:
                    if (mMenuCubeOptions.cubeLocation.z == 8) {
                        m_prev_face = CubeFaceNamesEnum.Face_Options;
                        m_current_cube_face = CubeFaceNamesEnum.Face_Menu;
                        setCurrentCubeFaceType(Face_Z_Plus);
                        mMenuCubeOptions.moveOnAxis(AxisMovement_Y_Minus);

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
            switch (m_current_cube_face) {
                case Face_Menu:
                    if (mMenuCubeStore.cubeLocation.y == 0) {
                        m_prev_face = CubeFaceNamesEnum.Face_Menu;
                        m_current_cube_face = CubeFaceNamesEnum.Face_Score;
                        setCurrentCubeFaceType(Face_Y_Minus);
                        mMenuCubeStore.moveOnAxis(AxisMovement_Z_Minus);

                        mNavigator.setup(CubeFaceNavigationEnum.Menu_To_Store);
                    }

                    if (mMenuCubeStore.cubeLocation.x == 7) {
                        if (!mMenuCubeOptions.isDone() || !mMenuCubePlay.isDone() || m_prev_face == CubeFaceNamesEnum.Face_Score) {
                            if (m_prev_face == CubeFaceNamesEnum.Face_Score) {
                                m_prev_face = CubeFaceNamesEnum.Face_Empty;
                            }

                            mMenuCubeStore.moveOnAxis(AxisMovement_X_Minus);
                        }
                    }
                    break;

                case Face_Score:
                    if (mMenuCubeStore.cubeLocation.z == 8) {
                        m_prev_face = CubeFaceNamesEnum.Face_Score;
                        m_current_cube_face = CubeFaceNamesEnum.Face_Menu;
                        setCurrentCubeFaceType(Face_Z_Plus);
                        mMenuCubeStore.moveOnAxis(AxisMovement_Y_Plus);

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
        for (int i = 0; i < 6; ++i) {
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

            face_type = Face_X_Plus;
            if (null != cube.fonts[face_type]) {
                mTexts.get(face_type).add(cube.fonts[face_type]);
            }

            if (null != cube.symbols[face_type]) {
                mSymbols.get(face_type).add(cube.symbols[face_type]);
            }


            face_type = Face_X_Minus;
            if (null != cube.fonts[face_type]) {
                mTexts.get(face_type).add(cube.fonts[face_type]);
            }

            if (null != cube.symbols[face_type]) {
                mSymbols.get(face_type).add(cube.symbols[face_type]);
            }


            face_type = Face_Y_Plus;
            if (null != cube.fonts[face_type]) {
                mTexts.get(face_type).add(cube.fonts[face_type]);
            }

            if (null != cube.symbols[face_type]) {
                mSymbols.get(face_type).add(cube.symbols[face_type]);
            }


            face_type = Face_Y_Minus;
            if (null != cube.fonts[face_type]) {
                mTexts.get(face_type).add(cube.fonts[face_type]);
            }

            if (null != cube.symbols[face_type]) {
                mSymbols.get(face_type).add(cube.symbols[face_type]);
            }


            face_type = Face_Z_Plus;
            if (null != cube.fonts[face_type]) {
                mTexts.get(face_type).add(cube.fonts[face_type]);
            }

            if (null != cube.symbols[face_type]) {
                mSymbols.get(face_type).add(cube.symbols[face_type]);
            }


            face_type = Face_Z_Minus;
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

            face_type = Face_X_Plus;
            if (null != cube.fonts[face_type]) {
                mTitles.get(face_type).add(cube.fonts[face_type]);
            }

            if (null != cube.symbols[face_type]) {
                mSymbols.get(face_type).add(cube.symbols[face_type]);
            }


            face_type = Face_X_Minus;
            if (null != cube.fonts[face_type]) {
                mTitles.get(face_type).add(cube.fonts[face_type]);
            }

            if (null != cube.symbols[face_type]) {
                mSymbols.get(face_type).add(cube.symbols[face_type]);
            }


            face_type = Face_Y_Plus;
            if (null != cube.fonts[face_type]) {
                mTitles.get(face_type).add(cube.fonts[face_type]);
            }

            if (null != cube.symbols[face_type]) {
                mSymbols.get(face_type).add(cube.symbols[face_type]);
            }


            face_type = Face_Y_Minus;
            if (null != cube.fonts[face_type]) {
                mTitles.get(face_type).add(cube.fonts[face_type]);
            }

            if (null != cube.symbols[face_type]) {
                mSymbols.get(face_type).add(cube.symbols[face_type]);
            }


            face_type = Face_Z_Plus;
            if (null != cube.fonts[face_type]) {
                mTitles.get(face_type).add(cube.fonts[face_type]);
            }

            if (null != cube.symbols[face_type]) {
                mSymbols.get(face_type).add(cube.symbols[face_type]);
            }


            face_type = Face_Z_Minus;
            if (null != cube.fonts[face_type]) {
                mTitles.get(face_type).add(cube.fonts[face_type]);
            }

            if (null != cube.symbols[face_type]) {
                mSymbols.get(face_type).add(cube.symbols[face_type]);
            }
        }

        if (m_menu_cube_hilite != null) {
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
        switch (m_current_cube_face) {
            case Face_Tutorial:
                animFontAppear();
                break;

            case Face_Score: // TODO: rename
                break;

            default:
                break;
        }

        updatePlayCube();
        updateOptionsCube();
        updateStoreCube();
    }

    private void updateHilite() {
        m_hilite_timeout -= 0.01f;

        if (m_hilite_timeout < 0.0f) {
            m_hilite_timeout = 0.05f;

            Color color = new Color(160, 160, 160, 255);

            if (!mMenuCubePlay.lst_cubes_to_hilite.isEmpty()) {
                Cube p = mMenuCubePlay.lst_cubes_to_hilite.get(0);
                mMenuCubePlay.lst_cubes_to_hilite.remove(p);

                p.colorCurrent = color;
            }

            if (!mMenuCubeOptions.lst_cubes_to_hilite.isEmpty()) {
                Cube p = mMenuCubeOptions.lst_cubes_to_hilite.get(0);
                mMenuCubeOptions.lst_cubes_to_hilite.remove(p);

                p.colorCurrent = color;
            }

            if (!mMenuCubeStore.lst_cubes_to_hilite.isEmpty()) {
                Cube p = mMenuCubeStore.lst_cubes_to_hilite.get(0);
                mMenuCubeStore.lst_cubes_to_hilite.remove(p);

                p.colorCurrent = color;
            }
        }
    }

    public void update() {
        if (m_showing_help) {
            m_show_help_timeout -= 0.01f;

            if (m_show_help_timeout < 0.0f) {
                m_showing_help = false;

                releaseCubeTextsOnFace(Face_Z_Plus);
                MenuFaceBuilder.resetTransforms();
                MenuFaceBuilder.buildTexts(CubeFaceNamesEnum.Face_Menu, Face_Z_Plus, false);
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
            updateHilite();
        }
        updateCubes();
    }

    private void eventPlayLevel(DifficultyEnum difficulty, int level_number) {
        if (!Game.canPlayLockedLevels) {
            switch (difficulty) {
                case Easy:
                    if (LEVEL_LOCKED == Game.progress.getStarsEasy(level_number)) {
                        Game.audio.playSound(SOUND_TAP_ON_LOCKED_LEVEL_CUBE);
                        return;
                    }
                    break;

                case Normal:
                    if (LEVEL_LOCKED == Game.progress.getStarsNormal(level_number)) {
                        Game.audio.playSound(SOUND_TAP_ON_LOCKED_LEVEL_CUBE);
                        return;
                    }
                    break;

                case Hard:
                    if (LEVEL_LOCKED == Game.progress.getStarsHard(level_number)) {
                        Game.audio.playSound(SOUND_TAP_ON_LOCKED_LEVEL_CUBE);
                        return;
                    }
                    break;
            }
        }

        Game.audio.playSound(SOUND_TAP_ON_LEVEL_CUBE);

        Game.audio.stopMusic();
        Game.anim_init_data.clearTransforms();

        switch (difficulty) {
            case Easy:
                if (level_number >= 1 && level_number <= 15) {
                    Game.anim_init_data.setFaces(CubeFaceNamesEnum.Face_Easy01, CubeFaceNamesEnum.Face_Easy04, CubeFaceNamesEnum.Face_Menu);
                }

                if (level_number >= 16 && level_number <= 30) {
                    Game.anim_init_data.setFaces(CubeFaceNamesEnum.Face_Easy02, CubeFaceNamesEnum.Face_Easy01, CubeFaceNamesEnum.Face_Empty);
                }

                if (level_number >= 31 && level_number <= 45) {
                    Game.anim_init_data.setFaces(CubeFaceNamesEnum.Face_Easy03, CubeFaceNamesEnum.Face_Easy02, CubeFaceNamesEnum.Face_Empty);
                }

                if (level_number >= 46 && level_number <= 60) {
                    Game.anim_init_data.setFaces(CubeFaceNamesEnum.Face_Easy04, CubeFaceNamesEnum.Face_Easy03, CubeFaceNamesEnum.Face_Empty);
                }
                break;

            case Normal:
                if (level_number >= 1 && level_number <= 15) {
                    Game.anim_init_data.setFaces(CubeFaceNamesEnum.Face_Normal01, CubeFaceNamesEnum.Face_Normal04, CubeFaceNamesEnum.Face_Easy01);
                }

                if (level_number >= 16 && level_number <= 30) {
                    Game.anim_init_data.setFaces(CubeFaceNamesEnum.Face_Normal02, CubeFaceNamesEnum.Face_Normal01, CubeFaceNamesEnum.Face_Empty);
                }

                if (level_number >= 31 && level_number <= 45) {
                    Game.anim_init_data.setFaces(CubeFaceNamesEnum.Face_Normal03, CubeFaceNamesEnum.Face_Normal02, CubeFaceNamesEnum.Face_Empty);
                }

                if (level_number >= 46 && level_number <= 60) {
                    Game.anim_init_data.setFaces(CubeFaceNamesEnum.Face_Normal04, CubeFaceNamesEnum.Face_Normal03, CubeFaceNamesEnum.Face_Empty);
                }
                break;

            case Hard:
                if (level_number >= 1 && level_number <= 15) {
                    Game.anim_init_data.setFaces(CubeFaceNamesEnum.Face_Hard01, CubeFaceNamesEnum.Face_Hard04, CubeFaceNamesEnum.Face_Normal01);
                }

                if (level_number >= 16 && level_number <= 30) {
                    Game.anim_init_data.setFaces(CubeFaceNamesEnum.Face_Hard02, CubeFaceNamesEnum.Face_Hard01, CubeFaceNamesEnum.Face_Empty);
                }

                if (level_number >= 31 && level_number <= 45) {
                    Game.anim_init_data.setFaces(CubeFaceNamesEnum.Face_Hard03, CubeFaceNamesEnum.Face_Hard02, CubeFaceNamesEnum.Face_Empty);
                }

                if (level_number >= 46 && level_number <= 60) {
                    Game.anim_init_data.setFaces(CubeFaceNamesEnum.Face_Hard04, CubeFaceNamesEnum.Face_Hard03, CubeFaceNamesEnum.Face_Empty);
                }
                break;
        }

        Game.levelInitData.difficulty = difficulty;
        Game.levelInitData.levelNumber = level_number;
        Game.levelInitData.initAction = LevelInitActionEnum.FullInit;

        Game.anim_init_data.type = AnimTypeEnum.AnimToLevel;

        Game.anim_init_data.list_cubes_base.clear();

        int size = mCubesBase.size();
        Cube cube;
        for (int i = 0; i < size; ++i) {
            cube = mCubesBase.get(i);
            Game.anim_init_data.list_cubes_base.add(cube);
        }

        Game.anim_init_data.camera_from.init(mCameraCurrent);
        Game.anim_init_data.camera_to.init(Game.level.mCameraLevel);

        Game.anim_init_data.pos_light_from.init(mPosLightCurrent);
        Game.anim_init_data.pos_light_to.init(Game.level.getLightPositionCurrent());

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

        //Game.hideGameCenterInfo();
    }

    private void renderForPicking(PickRenderTypeEnum type) {
        Graphics graphics = Game.graphics;
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glDisable(GL_LIGHTING);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);

        glDisable(GL_TEXTURE_2D);

        graphics.setProjection3D();
        graphics.setModelViewMatrix3D(mCameraCurrent);

        mNavigator.applyRotations();

        glPushMatrix();
        glTranslatef(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);

        graphics.resetBufferIndices();

        switch (type) {
            case RenderOnlyOptions:
                graphics.addCubeSize(m_arOptionsCubes[0].pos.x, m_arOptionsCubes[0].pos.y, m_arOptionsCubes[0].pos.z, HALF_CUBE_SIZE * 1.5f, m_arOptionsCubes[0].color);
                graphics.addCubeSize(m_arOptionsCubes[1].pos.x, m_arOptionsCubes[1].pos.y, m_arOptionsCubes[1].pos.z, HALF_CUBE_SIZE * 1.5f, m_arOptionsCubes[1].color);
                graphics.addCubeSize(m_arOptionsCubes[2].pos.x, m_arOptionsCubes[2].pos.y, m_arOptionsCubes[2].pos.z, HALF_CUBE_SIZE * 1.5f, m_arOptionsCubes[2].color);
                graphics.addCubeSize(m_arOptionsCubes[3].pos.x, m_arOptionsCubes[3].pos.y, m_arOptionsCubes[3].pos.z, HALF_CUBE_SIZE * 1.5f, m_arOptionsCubes[3].color);
                break;

            case RenderOnlyMovingCubePlay:
                graphics.addCubeSize(mMenuCubePlay.pos.x, mMenuCubePlay.pos.y, mMenuCubePlay.pos.z, HALF_CUBE_SIZE * 1.5f, mMenuCubePlay.color);
                break;

            case RenderOnlyMovingCubeStore:
                graphics.addCubeSize(mMenuCubeStore.pos.x, mMenuCubeStore.pos.y, mMenuCubeStore.pos.z, HALF_CUBE_SIZE * 1.5f, mMenuCubeStore.color);
                break;

            case RenderOnlyMovingCubeOptions:
                graphics.addCubeSize(mMenuCubeOptions.pos.x, mMenuCubeOptions.pos.y, mMenuCubeOptions.pos.z, HALF_CUBE_SIZE * 1.5f, mMenuCubeOptions.color);
                break;

            case RenderOnlyCubeCredits:
                graphics.addCubeSize(m_cubeCredits.pos.x, m_cubeCredits.pos.y, m_cubeCredits.pos.z, HALF_CUBE_SIZE * 1.5f, m_cubeCredits.color);
                break;

            case RenderOnlyMovingCubes:
                switch (m_current_cube_face) {
                    case Face_Options:
                        graphics.addCubeSize(mMenuCubeOptions.pos.x, mMenuCubeOptions.pos.y, mMenuCubeOptions.pos.z, HALF_CUBE_SIZE * 1.5f, mMenuCubeOptions.color);
                        break;

                    case Face_Score:
                        graphics.addCubeSize(mMenuCubeStore.pos.x, mMenuCubeStore.pos.y, mMenuCubeStore.pos.z, HALF_CUBE_SIZE * 1.5f, mMenuCubeStore.color);
                        break;

                    case Face_Menu:
                        graphics.addCubeSize(mMenuCubePlay.pos.x, mMenuCubePlay.pos.y, mMenuCubePlay.pos.z, HALF_CUBE_SIZE * 1.5f, mMenuCubePlay.color);
                        graphics.addCubeSize(mMenuCubeOptions.pos.x, mMenuCubeOptions.pos.y, mMenuCubeOptions.pos.z, HALF_CUBE_SIZE * 1.5f, mMenuCubeOptions.color);
                        graphics.addCubeSize(mMenuCubeStore.pos.x, mMenuCubeStore.pos.y, mMenuCubeStore.pos.z, HALF_CUBE_SIZE * 1.5f, mMenuCubeStore.color);
                        graphics.addCubeSize(m_cubeCredits.pos.x, m_cubeCredits.pos.y, m_cubeCredits.pos.z, HALF_CUBE_SIZE * 1.5f, m_cubeCredits.color);
                        break;

                    default:
                        graphics.addCubeSize(mMenuCubePlay.pos.x, mMenuCubePlay.pos.y, mMenuCubePlay.pos.z, HALF_CUBE_SIZE * 1.5f, mMenuCubePlay.color);
                        break;
                }
                break;

            case RenderOnlyLevelCubes: {
                int size = Game.ar_cubefacedata[m_current_cube_face_type].lst_level_cubes.size();
                LevelCube p;

                for (int i = 0; i < size; ++i) {
                    p = Game.ar_cubefacedata[m_current_cube_face_type].lst_level_cubes.get(i);
                    if (m_current_cube_face == p.face_id) {
                        graphics.addCubeSize(p.pos.x, p.pos.y, p.pos.z, HALF_CUBE_SIZE * 1.5f, p.color);
                    }
                }
            }
            break;

            default:
                break;
        }

        glDisable(GL_TEXTURE_2D);
        graphics.bindStreamSources3dNoTexture();
        graphics.updateBuffers();
        graphics.renderTriangles();
        glEnable(GL_TEXTURE_2D);
        glPopMatrix();
    }

    @Override
    public void render() {
        Graphics graphics = Game.graphics;
//        renderForPicking(PickRenderTypeEnum.RenderOnlyMovingCubePlay);
//        if (true) {
//            return;
    //        }

        glEnable(GL_LIGHT0);

        graphics.setProjection2D();
        graphics.setModelViewMatrix2D();
        graphics.bindStreamSources2d();

        glEnable(GL_BLEND);
        glDisable(GL_LIGHTING);
        glDepthMask(false); //GL_FALSE);
        glEnable(GL_TEXTURE_2D);

        Color color = new Color(255, 255, 255, (int) Game.dirtyAlpha);
        graphics.drawFullScreenTexture(Game.graphics.textureDirty, color);

        glDepthMask(true);

        graphics.setProjection3D();
        graphics.setModelViewMatrix3D(mCameraCurrent);
        graphics.bindStreamSources3d();

        graphics.setLightPosition(mPosLightCurrent);

        glEnable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);

        //glEnableClientState(GL_NORMAL_ARRAY);
        //glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        if (StateEnum.InCredits == mState) {
            graphics.textureCredits.bind();
            drawCredits();
            //return;
        }

//        if (true) {
//            glDisable(GL_TEXTURE_2D);
//            glDisable(GL_LIGHTING);
//            graphics.drawAxes();
//            glEnable(GL_LIGHTING);
//            glEnable(GL_TEXTURE_2D);
//        }

        mNavigator.applyRotations();

        glEnable(GL_DEPTH_TEST);
        graphics.textureGrayConcrete.bind();
        drawTheCube();

//        if (true) {
//            glDisable(GL_TEXTURE_2D);
//            glDisable(GL_LIGHTING);
//            graphics.drawAxes();
//            glEnable(GL_LIGHTING);
//            glEnable(GL_TEXTURE_2D);
//        }

        glPushMatrix();
        glTranslatef(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);

        graphics.textureLevelCubes.bind();
        drawLevelCubes();

        graphics.texturePlayer.bind();
        drawMenuCubes();

        glPopMatrix();

        glDisable(GL_LIGHTING);

        graphics.resetBufferIndices();
        glEnable(GL_BLEND);
        graphics.bindStreamSources3d();

        glPushMatrix();
            glTranslatef(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);

            graphics.textureFonts.bind();

            drawTexts();
            drawTextsTitles();

            graphics.textureNumbers.bind();
            drawLevelNumbers();

            graphics.textureSymbols.bind();
            drawLevelCubeSymbols();

            drawSymbols();

            if (m_menu_cube_hilite != null) {
                color = new Color(255, 255, 255, (int)(m_hilite_alpha * 255));
                glDisable(GL_DEPTH_TEST);
                drawCubeHiLite(color);
                glEnable(GL_DEPTH_TEST);
            }

            if (CubeFaceNamesEnum.Face_Options == m_current_cube_face ||
                mNavigator.isCurrentNavigation(CubeFaceNavigationEnum.Menu_To_Options) ||
                mNavigator.isCurrentNavigation(CubeFaceNavigationEnum.Options_To_Menu)) {
                drawCubeFaceOptions();
            }
        glPopMatrix();
    }

    @Override
    public void onFingerDown(float x, float y, int finger_count) {
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

            m_color_down = Game.graphics.getColorFromScreen(mPosDown);
            MenuCube menuCube = getMovingCubeFromColor(m_color_down.r);

            if (menuCube != null) {
                m_hilite_alpha = 0.0f;
                m_menu_cube_hilite = menuCube;
                CubeLocation cp = m_menu_cube_hilite.cubeLocation;
                m_font_hilite.init(SymbolHilite, cp);
            }
        }
    }

    @Override
    public void onFingerMove(float prev_x, float prev_y, float cur_x, float cur_y, int finger_count) {
        if (mIsFingerDown) {
            //System.out.println("Menu::OnFingerMove " + cur_x + ", " + cur_y);
            mPosMove.x = cur_x;
            mPosMove.y = cur_y;

            float dist = Utils.getDistance2D(mPosDown, mPosMove);
            //System.out.println("OnFingerMove: distance " + dist);

            if (dist > 20.0f * Game.graphics.deviceScale) {
                mIsSwipe = true;
            }
        }
    }

    @Override
    public void onFingerUp(float x, float y, int finger_count) {
        mIsFingerDown = false;

        if (!mMenuCubePlay.isDone() || !mMenuCubeOptions.isDone() || !mMenuCubeStore.isDone()) {
            return;
        }

        m_menu_cube_hilite = null;

        mPosUp.x = x;
        mPosUp.y = y;

        if (StateEnum.InCredits == mState || StateEnum.AnimToCredits == mState || StateEnum.AnimFromCredits == mState) {
            if (StateEnum.InCredits == mState) {
                m_t = 0.0f;
                mState = StateEnum.AnimFromCredits;
                //Game.showGameCenterInfo();
            }
            return;
        }

        if (mIsSwipe) {
            mIsSwipe = false;
            handleSwipe();
        } else { // single tap        
            switch (m_current_cube_face) {
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
        m_color_down = Game.graphics.getColorFromScreen(mPosDown);
        m_color_up = Game.graphics.getColorFromScreen(mPosUp);

        int downR = m_color_down.r;
        int upR = m_color_up.r;

        if (downR == upR) {
            switch (downR) {
                case 255:
                    Game.musicVolumeUp();
                    Game.audio.playSound(SOUND_VOLUME_UP);
                    break;

                case 254:
                    Game.musicVolumeDown();
                    Game.audio.playSound(SOUND_VOLUME_DOWN);
                    break;

                case 253:
                    Game.soundVolumeUp();
                    Game.audio.playSound(SOUND_VOLUME_UP);
                    break;

                case 252:
                    Game.soundVolumeDown();
                    Game.audio.playSound(SOUND_VOLUME_DOWN);
                    break;

                default:
                    break;
            }
        }
    }

    private void fingerUpOnFacesEasy() {
        renderForPicking(PickRenderTypeEnum.RenderOnlyLevelCubes);
        m_color_down = Game.graphics.getColorFromScreen(mPosDown);
        m_color_up = Game.graphics.getColorFromScreen(mPosUp);

        if (m_color_down.r == m_color_up.r) {
            int level_number = 255 - m_color_up.r;

            if (level_number >= 1 && level_number <= 60) {
                eventPlayLevel(DifficultyEnum.Easy, level_number);
            }
        }
    }

    private void fingerUpOnFacesNormal() {
        renderForPicking(PickRenderTypeEnum.RenderOnlyLevelCubes);
        m_color_down = Game.graphics.getColorFromScreen(mPosDown);
        m_color_up = Game.graphics.getColorFromScreen(mPosUp);

        if (m_color_down.r == m_color_up.r) {
            int level_number = 255 - m_color_up.r;

            if (level_number >= 1 && level_number <= 60) {
                eventPlayLevel(DifficultyEnum.Normal, level_number);
            }
        }
    }

    private void fingerUpOnFacesHard() {
        renderForPicking(PickRenderTypeEnum.RenderOnlyLevelCubes);
        m_color_down = Game.graphics.getColorFromScreen(mPosDown);
        m_color_up = Game.graphics.getColorFromScreen(mPosUp);

        if (m_color_down.r == m_color_up.r) {
            int level_number = 255 - m_color_up.r;

            if (level_number >= 1 && level_number <= 60) {
                eventPlayLevel(DifficultyEnum.Hard, level_number);
            }
        }
    }

    private void fingerUpOnFaceMenu() {
        renderForPicking(PickRenderTypeEnum.RenderOnlyMovingCubes);
        m_color_down = Game.graphics.getColorFromScreen(mPosDown);
        m_color_up = Game.graphics.getColorFromScreen(mPosUp);

        if (m_color_down.r == m_color_up.r) {
            MenuCube menuCube = getMovingCubeFromColor(m_color_up.r);
            if (menuCube == m_cubeCredits) {
                eventShowCredits();
                return;
            }

            if (!m_showing_help) {
                if (255 == m_color_down.r || 200 == m_color_down.r || 100 == m_color_down.r) {
                    m_showing_help = true;
                    m_show_help_timeout = 3.0f;
                    releaseCubeTextsOnFace(Face_Z_Plus);
                    //cCreator::AlterRedCubeFontsOnFaceMain(true);
                    MenuFaceBuilder.resetTransforms();
                    MenuFaceBuilder.buildTexts(CubeFaceNamesEnum.Face_Menu, Face_Z_Plus, true);
                }
            }
        }
    }

    private void handleSwipe() {
        Graphics graphics = Game.graphics;
        SwipeInfo swipeInfo = Game.getSwipeDirAndLength(mPosDown, mPosUp);

        if (swipeInfo.length > (30.0f * graphics.scaleFactor)) {
            renderForPicking(PickRenderTypeEnum.RenderOnlyMovingCubes);
            Color down_color = Game.graphics.getColorFromScreen(mPosDown);
            //printf("\nOnFingerUp [SWIPE] color is: %d, %d, %d, %d", m_down_color.r, m_down_color.g, m_down_color.b, m_down_color.a);

            MenuCube menuCube = getMovingCubeFromColor(down_color.r);

            if (menuCube != null) {
                switch (swipeInfo.swipeDir) {
                    case SwipeLeft:
                        switch (m_current_cube_face) {
                            case Face_Menu:
                            case Face_Options:
                            case Face_Score:
                                menuCube.moveOnAxis(AxisMovement_X_Minus);
                                break;

                            case Face_Normal01:
                            case Face_Normal02:
                            case Face_Normal03:
                            case Face_Normal04:
                                menuCube.moveOnAxis(AxisMovement_X_Plus);
                                break;

                            case Face_Hard01:
                            case Face_Hard02:
                            case Face_Hard03:
                            case Face_Hard04:
                            case Face_Tutorial:
                                menuCube.moveOnAxis(AxisMovement_Z_Minus);
                                break;

                            case Face_Easy01:
                            case Face_Easy02:
                            case Face_Easy03:
                            case Face_Easy04:
                                menuCube.moveOnAxis(AxisMovement_Z_Plus);
                                break;

                            default:
                                break;
                        }
                        break;

                    case SwipeRight:
                        switch (m_current_cube_face) {
                            case Face_Menu:
                            case Face_Options:
                            case Face_Score:
                                menuCube.moveOnAxis(AxisMovement_X_Plus);
                                if (CubeFaceNamesEnum.Face_Menu == m_current_cube_face) {
                                    if (menuCube != mMenuCubeOptions) {
                                        if (7 == mMenuCubeOptions.cubeLocation.x) {
                                            mMenuCubeOptions.moveOnAxis(AxisMovement_X_Minus);
                                        }
                                    }
                                }
                                break;

                            case Face_Normal01:
                            case Face_Normal02:
                            case Face_Normal03:
                            case Face_Normal04:
                                menuCube.moveOnAxis(AxisMovement_X_Minus);
                                break;

                            case Face_Hard01:
                            case Face_Hard02:
                            case Face_Hard03:
                            case Face_Hard04:
                            case Face_Tutorial:
                                menuCube.moveOnAxis(AxisMovement_Z_Plus);
                                break;

                            case Face_Easy01:
                            case Face_Easy02:
                            case Face_Easy03:
                            case Face_Easy04:
                                menuCube.moveOnAxis(AxisMovement_Z_Minus);
                                break;

                            default:
                                break;
                        }
                        break;

                    case SwipeUp:
                        switch (m_current_cube_face) {
                            case Face_Score: menuCube.moveOnAxis(AxisMovement_Z_Plus); break;
                            case Face_Options: menuCube.moveOnAxis(AxisMovement_Z_Minus); break;
                            case Face_Easy02: menuCube.moveOnAxis(AxisMovement_X_Plus); break;
                            case Face_Easy03: menuCube.moveOnAxis(AxisMovement_Y_Minus); break;
                            case Face_Easy04: menuCube.moveOnAxis(AxisMovement_X_Minus); break;
                            case Face_Normal02: menuCube.moveOnAxis(AxisMovement_Z_Minus); break;
                            case Face_Normal03: menuCube.moveOnAxis(AxisMovement_Y_Minus); break;
                            case Face_Normal04: menuCube.moveOnAxis(AxisMovement_Z_Plus); break;
                            case Face_Hard02: menuCube.moveOnAxis(AxisMovement_X_Minus); break;
                            case Face_Hard03: menuCube.moveOnAxis(AxisMovement_Y_Minus); break;
                            case Face_Hard04: menuCube.moveOnAxis(AxisMovement_X_Plus); break;
                            default: menuCube.moveOnAxis(AxisMovement_Y_Plus); break;
                        }
                        break;

                    case SwipeDown:
                        switch (m_current_cube_face) {
                            case Face_Score: menuCube.moveOnAxis(AxisMovement_Z_Minus); break;
                            case Face_Options: menuCube.moveOnAxis(AxisMovement_Z_Plus); break;
                            case Face_Easy02: menuCube.moveOnAxis(AxisMovement_X_Minus); break;
                            case Face_Easy03: menuCube.moveOnAxis(AxisMovement_Y_Plus); break;
                            case Face_Easy04: menuCube.moveOnAxis(AxisMovement_X_Plus); break;
                            case Face_Normal02: menuCube.moveOnAxis(AxisMovement_Z_Plus); break;
                            case Face_Normal03: menuCube.moveOnAxis(AxisMovement_Y_Plus); break;
                            case Face_Normal04: menuCube.moveOnAxis(AxisMovement_Z_Minus); break;
                            case Face_Hard02: menuCube.moveOnAxis(AxisMovement_X_Plus); break;
                            case Face_Hard03: menuCube.moveOnAxis(AxisMovement_Y_Plus); break;
                            case Face_Hard04: menuCube.moveOnAxis(AxisMovement_X_Minus); break;
                            default: menuCube.moveOnAxis(AxisMovement_Y_Minus); break;
                        }
                        break;

                    default:
                        break;
                }
            }
        }
    }
}

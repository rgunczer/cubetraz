package com.almagems.cubetraz;

import java.util.ArrayList;
import static android.opengl.GLES10.*;
import static com.almagems.cubetraz.Constants.*;


public final class Menu extends Scene {

    public enum MenuStateEnum {
        InMenu,
        InCredits,
        AnimToCredits,
        AnimFromCredits
    }

    private final ArrayList<ArrayList<CubeFont>> m_lst_titles = new ArrayList<ArrayList<CubeFont>>(6);
    private final ArrayList<ArrayList<CubeFont>> m_lst_texts = new ArrayList<ArrayList<CubeFont>>(6);
    private final ArrayList<ArrayList<CubeFont>> m_lst_symbols = new ArrayList<ArrayList<CubeFont>>(6);

    private MenuStateEnum m_state;

    private float m_t;
    private float zoom;

    private float m_hilite_timeout;

    private MenuNavigator m_navigator = new MenuNavigator();

    private boolean m_can_alter_text;

    private float m_credits_offset;

    private Color m_color_down;
    private Color m_color_up;

    private boolean m_showing_help;
    private float m_show_help_timeout;

    public Camera m_camera_menu = new Camera();
    private Camera m_camera_credits = new Camera();
    private Camera m_camera_current = new Camera();

    private CubeFaceNamesEnum m_prev_face;
    private float m_menu_rotation;

    private Vector m_pos_light_menu = new Vector();
    public Vector m_pos_light_current = new Vector();

    public MenuCube m_pMenuCubePlay;
    public MenuCube m_pMenuCubeOptions;
    public MenuCube m_pMenuCubeStore;

    public final MenuCube[] m_arOptionsCubes = new MenuCube[4];

    // store
    public MenuCube m_pStoreCubeNoAds;
    public MenuCube m_pStoreCubeSolvers;
    public MenuCube m_pStoreCubeRestore;

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
    public final ArrayList<Cube> m_list_cubes_base = new ArrayList<Cube>();
    public final ArrayList<Cube> m_list_cubes_face = new ArrayList<Cube>();

    private float m_target_rotation_degree;

    private final EaseOutDivideInterpolation[] m_interpolators = new EaseOutDivideInterpolation[6];

    private CubeFaceNamesEnum m_current_cube_face;
    private int m_current_cube_face_type;

    private MenuCube getMovingCubeFromColor(float color) {
        int realColor = (int)(color * 255f);
        switch (realColor) {
            case 255: return m_pMenuCubePlay;
            case 200: return m_pMenuCubeOptions;
            case 100: return m_pMenuCubeStore;
            case 40:  return m_pStoreCubeNoAds;
            case 50:  return m_pStoreCubeSolvers;
            case 60:  return m_pStoreCubeRestore;
            case 1:   return m_cubeCredits;
        }
        return null;
    }

    public void dontHiliteMenuCube() {
        m_menu_cube_hilite = null;
    }
    public Camera getCamera() {
        return m_camera_menu;
    }
    public Vector getLightPositon() {
        return m_pos_light_menu;
    }

    // ctor
    public Menu() {
        m_can_alter_text = false;

        m_pos_light_menu = new Vector(-10.0f, 3.0f, 12.0f);

        m_cubefont_play = new CubeFont();
        m_cubefont_options = new CubeFont();
        m_cubefont_store = new CubeFont();
        m_cubefont_noads = new CubeFont();
        m_cubefont_solvers = new CubeFont();
        m_cubefont_restore = new CubeFont();

        for (int i = 0; i < 6; ++i) {
            m_interpolators[i] = new EaseOutDivideInterpolation();
        }

        ArrayList<CubeFont> lst;
        for (int i = 0; i < 6; ++i) {
            m_lst_titles.add(new ArrayList<CubeFont>());
            m_lst_texts.add(new ArrayList<CubeFont>());
            m_lst_symbols.add(new ArrayList<CubeFont>());

            lst = m_lst_titles.get(i);
            for (int j = 0; j < 6; j++) {
                lst.add(new CubeFont());
            }

            lst = m_lst_texts.get(i);
            for (int j = 0; j < 6; j++) {
                lst.add(new CubeFont());
            }

            lst = m_lst_symbols.get(i);
            for (int j = 0; j < 6; j++) {
                lst.add(new CubeFont());
            }
        }

//            ArrayList<CubeFont> lst = m_lst_titles.get(0);
//            lst.add(new CubeFont());

//        for (int i = 0; i < 6; ++i) {
//            m_lst_titles.get(i)
//            //m_lst_texts.get(i).clear();
//            //m_lst_symbols.get(i).clear();
//        }
    }

    public void setupCameras() {
        m_camera_credits.eye = new Vector(18.0f / 1.5f, 30.0f / 1.5f, 45.0f / 1.5f);
        m_camera_credits.target = new Vector(-2.0f / 1.5f, 0.0f, 5.0f / 1.5f);

        //#if defined(DRAW_AXES_CUBE) || defined(DRAW_AXES_GLOBAL)
        //m_camera_menu.eye = Vector(1.0f / 1.5f, -1.0f / 1.5f, 34.0f / 1.5f);
        //#else
        m_camera_menu.eye = new Vector(0.0f, 0.0f, 35.0f / 1.5f);
        //#endif

        m_camera_menu.target = new Vector(0.0f, 0.0f, 0.0f);

        m_camera_menu.eye = m_camera_menu.eye.scale(graphics.aspectRatio);
        m_camera_credits.eye = m_camera_credits.eye.scale(graphics.aspectRatio);

        m_pos_light_current = m_pos_light_menu;
        m_camera_current.init(m_camera_menu);
    }

    @Override
    public void init() {
        setupCameras();
        //printf("\ncCube size: %lu byte, all cubes size: %lu kbyte\n", sizeof(cCube), (sizeof(cCube)*9*9*9) / 1024);
        //cMenuFaceBuilder::Custom();
        Game.dirty_alpha = DIRTY_ALPHA;
        //Game.playMusic(MUSIC_CPU);

        m_hilite_timeout = 0.0f;
        m_menu_cube_hilite = null;
        m_showing_help = false;
        m_navigator.m_menu = this;
        m_prev_face = CubeFaceNamesEnum.Face_Empty;
        mIsFingerDown = false;
        mIsSwipe = false;
        m_pos_light_current.init(m_pos_light_menu);
        m_camera_current.init(m_camera_menu);

        Game.resetCubes();
        Game.setupHollowCube();
        Game.buildVisibleCubesList(m_list_cubes_base);

        if (Game.menu_init_data.reappear) {
            switch (m_current_cube_face) {
                case Face_Easy01:
                case Face_Easy02:
                case Face_Easy03:
                case Face_Easy04:
                    m_navigator.createEasyFaces();

                    if (CubeFaceNamesEnum.Face_Easy02 == m_current_cube_face || CubeFaceNamesEnum.Face_Easy03 == m_current_cube_face || CubeFaceNamesEnum.Face_Easy04 == m_current_cube_face) {
                        MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Empty, Face_Z_Plus);
                        MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Empty, Face_Z_Minus);
                    }
                    break;

                case Face_Normal01:
                case Face_Normal02:
                case Face_Normal03:
                case Face_Normal04:
                    m_navigator.createNormalFaces();

                    if (CubeFaceNamesEnum.Face_Normal02 == m_current_cube_face || CubeFaceNamesEnum.Face_Normal03 == m_current_cube_face || CubeFaceNamesEnum.Face_Normal04 == m_current_cube_face) {
                        MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Empty, Face_X_Plus);
                        MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Empty, Face_X_Minus);
                    }
                    break;

                case Face_Hard01:
                case Face_Hard02:
                case Face_Hard03:
                case Face_Hard04:
                    m_navigator.createHardFaces();

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
            //Creator.createStaticTexts();

            m_navigator.init(this);
            m_current_cube_face = CubeFaceNamesEnum.Face_Tutorial;
            setCurrentCubeFaceType(Face_X_Minus);

            m_navigator.createMenuFaces(true);

            m_pMenuCubePlay.setCubePos(new CubePos(0, 5, 4));
            m_pMenuCubeOptions.setCubePos(new CubePos(1, 3, 8));
            m_pMenuCubeStore.setCubePos(new CubePos(1, 1, 8));

            //m_cubeCredits.SetCubePos(CubePos(1, 1, 8));
        }

        m_state = MenuStateEnum.InMenu;
        Game.buildVisibleCubesListOnlyOnFaces(m_list_cubes_face);
        update();

        glEnable(GL_LIGHT0);
    }

    public void setCurrentCubeFaceType(int face_type) {
        CubePos offset = new CubePos(0, 0, 0);

        m_current_cube_face_type = face_type;

        switch (face_type) {
            case Face_X_Plus: offset = new CubePos(-1, 0, 0); break;
            case Face_X_Minus: offset = new CubePos(1, 0, 0); break;
            case Face_Y_Plus: offset = new CubePos(0, -1, 0); break;
            case Face_Y_Minus: offset = new CubePos(0, 1, 0); break;
            case Face_Z_Plus: offset = new CubePos(0, 0, -1); break;
            case Face_Z_Minus: offset = new CubePos(0, 0, 1); break;
            default:
                break;
        }

        m_pMenuCubePlay.setHiliteOffset(offset);
        m_pMenuCubeOptions.setHiliteOffset(offset);
        m_pMenuCubeStore.setHiliteOffset(offset);
    }

    public void resetStoreCubes() {
        m_pStoreCubeNoAds.setCubePos(new CubePos(1, 0, 6));
        m_pStoreCubeSolvers.setCubePos(new CubePos(1, 0, 4));
        m_pStoreCubeRestore.setCubePos(new CubePos(1, 0, 2));
    }

    public void releaseCubeTextsOnFace(int face_type) {
        int size = m_list_cubes_base.size();
        Cube cube;
        for (int i = 0; i < size; ++i) {
            cube = m_list_cubes_base.get(i);
            if (null != cube.ar_fonts[face_type]) {
                Creator.cubeFontReleased(cube.ar_fonts[face_type]);
                cube.ar_fonts[face_type] = null;
            }
        }
    }

//public void DisplayCurrentCubeFaceName()
//{
//    switch (m_current_cube_face)
//    {
//        case Face_Menu:     printf("\nFace_Menu");      break;
//        case Face_Options:  printf("\nFace_Options");   break;
//        case Face_Store:    printf("\nFace_Store");     break;
//
//        case Face_Easy01:    printf("\nFace_Easy1");     break;
//        case Face_Easy02:    printf("\nFace_Easy2");     break;
//        case Face_Easy03:    printf("\nFace_Easy3");     break;
//        case Face_Easy04:    printf("\nFace_Easy4");     break;
//
//        case Face_Normal01:  printf("\nFace_Normal1");   break;
//        case Face_Normal02:  printf("\nFace_Normal2");   break;
//        case Face_Normal03:  printf("\nFace_Normal3");   break;
//        case Face_Normal04:  printf("\nFace_Normal4");   break;
//
//        case Face_Hard01:    printf("\nFace_Hard1");     break;
//        case Face_Hard02:    printf("\nFace_Hard2");     break;
//        case Face_Hard03:    printf("\nFace_Hard3");     break;
//        case Face_Hard04:    printf("\nFace_Hard4");     break;
//
//        default:
//            break;
//    } // switch
//}

//public void DisplayMenuCubePlayCoordinates()
//{
//    printf("\nPlayCube x:%d, y:%d z:%d", m_pMenuCubePlay.m_cube_pos.x, m_pMenuCubePlay.m_cube_pos.y, m_pMenuCubePlay.m_cube_pos.z);
//}

    public void drawMenuCubes() {
        graphics.resetBufferIndices();
        graphics.bindStreamSources3d();

        Color color = new Color(255, 255, 255, 255);
        MenuCube p = m_pMenuCubePlay;

        if (p.visible) {
            graphics.addCubeSize(p.pos.x, p.pos.y, p.pos.z, HALF_CUBE_SIZE, color);
        }

        if (m_current_cube_face == CubeFaceNamesEnum.Face_Menu ||
                m_current_cube_face == CubeFaceNamesEnum.Face_Options ||
                m_navigator.isCurrentNavigation(CubeFaceNavigationEnum.Menu_To_Options) ||
                m_navigator.isCurrentNavigation(CubeFaceNavigationEnum.Menu_To_Easy1)) {
            p = m_pMenuCubeOptions;
            graphics.addCubeSize(p.pos.x, p.pos.y, p.pos.z, HALF_CUBE_SIZE, color);
        }

        if (m_current_cube_face == CubeFaceNamesEnum.Face_Menu ||
                m_current_cube_face == CubeFaceNamesEnum.Face_Store ||
                m_navigator.isCurrentNavigation(CubeFaceNavigationEnum.Menu_To_Store) ||
                m_navigator.isCurrentNavigation(CubeFaceNavigationEnum.Menu_To_Easy1)) {
            p = m_pMenuCubeStore;
            graphics.addCubeSize(p.pos.x, p.pos.y, p.pos.z, HALF_CUBE_SIZE, color);

            p = m_pStoreCubeNoAds;
            graphics.addCubeSize(p.pos.x, p.pos.y, p.pos.z, HALF_CUBE_SIZE, color);

            p = m_pStoreCubeSolvers;
            graphics.addCubeSize(p.pos.x, p.pos.y, p.pos.z, HALF_CUBE_SIZE, color);

            p = m_pStoreCubeRestore;
            graphics.addCubeSize(p.pos.x, p.pos.y, p.pos.z, HALF_CUBE_SIZE, color);
        }

        graphics.updateBuffers();
        graphics.renderTriangles();
    }

    public void drawLevelCubes() {
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

    public void drawLevelCubesLocked() {
        graphics.resetBufferIndices();
        graphics.bindStreamSources3d();

        int size;
        LevelCube levelCube;
        for (int i = 0; i < 6; ++i) {
            size = Game.ar_cubefacedata[i].lst_level_cubes_locked.size();
            for (int j = 0; j < size; ++j) {
                levelCube = Game.ar_cubefacedata[i].lst_level_cubes_locked.get(j);
                graphics.addCube(levelCube.pos.x, levelCube.pos.y, levelCube.pos.z);
            }
        }
        graphics.renderTriangles();
    }

    public void drawLevelCubeDecalsEasy(ArrayList<LevelCube> lst_level_cubes_x_plus,
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

    public void drawLevelCubeDecalsNormal(ArrayList<LevelCube> lst_level_cubes_z_plus,
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

    public void drawLevelCubeDecalsHard(ArrayList<LevelCube> lst_level_cubes_x_plus,
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

    public void drawLevelNumbersOnLocked() {
        graphics.resetBufferIndices();
        graphics.bindStreamSources3d();

        switch (m_current_cube_face) {
            case Face_Easy01:
            case Face_Easy02:
            case Face_Easy03:
            case Face_Easy04:
                drawLevelCubeDecalsEasy(Game.ar_cubefacedata[Face_X_Plus].lst_level_cubes_locked,
                        Game.ar_cubefacedata[Face_X_Minus].lst_level_cubes_locked,
                        Game.ar_cubefacedata[Face_Y_Plus].lst_level_cubes_locked,
                        Game.ar_cubefacedata[Face_Y_Minus].lst_level_cubes_locked,
                        LevelCubeDecalTypeEnum.LevelCubeDecalNumber);
                break;

            case Face_Normal01:
            case Face_Normal02:
            case Face_Normal03:
            case Face_Normal04:
                drawLevelCubeDecalsNormal(Game.ar_cubefacedata[Face_Z_Plus].lst_level_cubes_locked,
                        Game.ar_cubefacedata[Face_Z_Minus].lst_level_cubes_locked,
                        Game.ar_cubefacedata[Face_Y_Plus].lst_level_cubes_locked,
                        Game.ar_cubefacedata[Face_Y_Minus].lst_level_cubes_locked,
                        LevelCubeDecalTypeEnum.LevelCubeDecalNumber);
                break;

            case Face_Hard01:
            case Face_Hard02:
            case Face_Hard03:
            case Face_Hard04:
                drawLevelCubeDecalsHard(Game.ar_cubefacedata[Face_X_Plus].lst_level_cubes_locked,
                        Game.ar_cubefacedata[Face_X_Minus].lst_level_cubes_locked,
                        Game.ar_cubefacedata[Face_Y_Plus].lst_level_cubes_locked,
                        Game.ar_cubefacedata[Face_Y_Minus].lst_level_cubes_locked,
                        LevelCubeDecalTypeEnum.LevelCubeDecalNumber);
                break;

            default:
                break;
        }
        graphics.renderTriangles();
    }

    public void drawLevelNumbers() {
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

    public void drawTexts(Color color) {
        graphics.resetBufferIndices();

        if (Math.abs(m_navigator.m_cube_rotation_secondary.degree) < EPSILON) {
            drawTextsDefaultOrientation(m_lst_texts.get(Face_X_Plus),
                    m_lst_texts.get(Face_X_Minus),
                    m_lst_texts.get(Face_Y_Plus),
                    m_lst_texts.get(Face_Y_Minus),
                    m_lst_texts.get(Face_Z_Plus),
                    m_lst_texts.get(Face_Z_Minus), color);
        } else {
            switch (m_current_cube_face) {
                case Face_Easy01:
                case Face_Easy02:
                case Face_Easy03:
                case Face_Easy04:
                    drawEasyTitles(m_lst_texts.get(Face_X_Plus), m_lst_texts.get(Face_X_Minus),
                            m_lst_texts.get(Face_Y_Plus), m_lst_texts.get(Face_Y_Minus),
                            m_lst_texts.get(Face_Z_Plus), m_lst_texts.get(Face_Z_Minus),
                            color);
                    break;

                case Face_Normal01:
                case Face_Normal02:
                case Face_Normal03:
                case Face_Normal04:
                    drawNormalTitles(m_lst_texts.get(Face_X_Plus), m_lst_texts.get(Face_X_Minus),
                            m_lst_texts.get(Face_Y_Plus), m_lst_texts.get(Face_Y_Minus),
                            m_lst_texts.get(Face_Z_Plus), m_lst_texts.get(Face_Z_Minus),
                            color);
                    break;

                case Face_Hard01:
                case Face_Hard02:
                case Face_Hard03:
                case Face_Hard04:
                    drawHardTitles(m_lst_texts.get(Face_X_Plus), m_lst_texts.get(Face_X_Minus),
                            m_lst_texts.get(Face_Y_Plus), m_lst_texts.get(Face_Y_Minus),
                            m_lst_texts.get(Face_Z_Plus), m_lst_texts.get(Face_Z_Minus),
                            color);
                    break;

                default:
                    break;
            }
        }

        CubeFont cubeFont;
        TexturedQuad font;
        TexCoordsQuad coords = new TexCoordsQuad();

        // Draw CubeFonts on Red Cubes [P]lay [O]ptions [S]tore [U]nlock [R]estore
        if (m_navigator.isCurrentNavigation(CubeFaceNavigationEnum.Menu_To_Easy1) || m_current_cube_face == CubeFaceNamesEnum.Face_Menu || m_current_cube_face == CubeFaceNamesEnum.Face_Options || m_current_cube_face == CubeFaceNamesEnum.Face_Store) {
            Color colr = new Color(color);

            if (m_pMenuCubePlay.isDone() && m_pMenuCubePlay.m_cube_pos.x == 1) {
                cubeFont = m_cubefont_play;
                font = cubeFont.getFont();

                if (font != null) {
                    coords.tx0 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
                    coords.tx1 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
                    coords.tx2 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
                    coords.tx3 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);

                    graphics.addCubeFace_Z_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, colr);
                }
            }

            if (m_pMenuCubeOptions.isDone() && m_pMenuCubeOptions.m_cube_pos.x == 1) {
                cubeFont = m_cubefont_options;
                font = cubeFont.getFont();

                if (font != null) {
                    coords.tx0 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
                    coords.tx1 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
                    coords.tx2 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
                    coords.tx3 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);

                    graphics.addCubeFace_Z_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, colr);
                }
            }

            if (m_pMenuCubeStore.isDone() && m_pMenuCubeStore.m_cube_pos.x == 1) {
                cubeFont = m_cubefont_store;
                font = cubeFont.getFont();

                if (font != null) {
                    coords.tx0 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
                    coords.tx1 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
                    coords.tx2 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
                    coords.tx3 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);

                    graphics.addCubeFace_Z_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, colr);
                }
            }

            if (m_pStoreCubeNoAds.isDone() && m_pStoreCubeNoAds.m_cube_pos.x == 1) {
                cubeFont = m_cubefont_noads;
                font = cubeFont.getFont();

                if (font != null) {
                    coords.tx0 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
                    coords.tx1 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
                    coords.tx2 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
                    coords.tx3 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);

                    graphics.addCubeFace_Y_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, colr);
                }
            }

            if (m_pStoreCubeSolvers.isDone() && m_pStoreCubeSolvers.m_cube_pos.x == 1) {
                cubeFont = m_cubefont_solvers;
                font = cubeFont.getFont();

                if (font != null) {
                    coords.tx0 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
                    coords.tx1 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
                    coords.tx2 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
                    coords.tx3 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);

                    graphics.addCubeFace_Y_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, colr);
                }
            }

            if (m_pStoreCubeRestore.isDone() && m_pStoreCubeRestore.m_cube_pos.x == 1) {
                cubeFont = m_cubefont_restore;
                font = cubeFont.getFont();

                if (font != null) {
                    coords.tx0 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
                    coords.tx1 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
                    coords.tx2 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
                    coords.tx3 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);

                    graphics.addCubeFace_Y_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, colr);
                }
            }
        }

        graphics.updateBuffers();
        graphics.renderTriangles();
    }

    public void drawTextsTitles(Color color) {
        graphics.resetBufferIndices();

        if (Math.abs(m_navigator.m_cube_rotation_secondary.degree) < EPSILON) {
            drawTextsDefaultOrientation(m_lst_titles.get(Face_X_Plus), m_lst_titles.get(Face_X_Minus),
                    m_lst_titles.get(Face_Y_Plus), m_lst_titles.get(Face_Y_Minus),
                    m_lst_titles.get(Face_Z_Plus), m_lst_titles.get(Face_Z_Minus),
                    color);
        } else {
            switch (m_current_cube_face) {
                case Face_Easy01:
                case Face_Easy02:
                case Face_Easy03:
                case Face_Easy04:
                    drawEasyTitles(m_lst_titles.get(Face_X_Plus), m_lst_titles.get(Face_X_Minus),
                            m_lst_titles.get(Face_Y_Plus), m_lst_titles.get(Face_Y_Minus),
                            m_lst_titles.get(Face_Z_Plus), m_lst_titles.get(Face_Z_Minus),
                            color);
                    break;

                case Face_Normal01:
                case Face_Normal02:
                case Face_Normal03:
                case Face_Normal04:
                    drawNormalTitles(m_lst_titles.get(Face_X_Plus), m_lst_titles.get(Face_X_Minus),
                            m_lst_titles.get(Face_Y_Plus), m_lst_titles.get(Face_Y_Minus),
                            m_lst_titles.get(Face_Z_Plus), m_lst_titles.get(Face_Z_Minus),
                            color);
                    break;

                case Face_Hard01:
                case Face_Hard02:
                case Face_Hard03:
                case Face_Hard04:
                    drawHardTitles(m_lst_titles.get(Face_X_Plus), m_lst_titles.get(Face_X_Minus),
                            m_lst_titles.get(Face_Y_Plus), m_lst_titles.get(Face_Y_Minus),
                            m_lst_titles.get(Face_Z_Plus), m_lst_titles.get(Face_Z_Minus),
                            color);
                    break;

                default:
                    break;
            }
        }
        graphics.updateBuffers();
        graphics.renderTriangles();
    }

    public void drawTextsDefaultOrientation(ArrayList<CubeFont> lst_x_plus,
                                            ArrayList<CubeFont> lst_x_minus,
                                            ArrayList<CubeFont> lst_y_plus,
                                            ArrayList<CubeFont> lst_y_minus,
                                            ArrayList<CubeFont> lst_z_plus,
                                            ArrayList<CubeFont> lst_z_minus,
                                            Color color) {
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

            graphics.addCubeFace_Z_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
        }

        size = lst_x_plus.size();
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_x_plus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx1 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx2 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx3 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);

            graphics.addCubeFace_X_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
        }

        size = lst_z_minus.size();
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_z_minus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx1 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx2 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx3 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);

            graphics.addCubeFace_Z_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
        }

        size = lst_x_minus.size();
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_x_minus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx1 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx2 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx3 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);

            graphics.addCubeFace_X_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
        }

        size = lst_y_plus.size();
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_y_plus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx1 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx2 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx3 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);

            graphics.addCubeFace_Y_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
        }

        size = lst_y_minus.size();
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_y_minus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx1 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx2 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx3 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);

            graphics.addCubeFace_Y_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
        }
    }

    public void drawEasyTitles(ArrayList<CubeFont> lst_x_plus,
                               ArrayList<CubeFont> lst_x_minus,
                               ArrayList<CubeFont> lst_y_plus,
                               ArrayList<CubeFont> lst_y_minus,
                               ArrayList<CubeFont> lst_z_plus,
                               ArrayList<CubeFont> lst_z_minus,
                               Color color) {
        int face_type;
        CubeFont cubeFont;
        TexturedQuad font;
        TexCoordsQuad coords = new TexCoordsQuad();
        int size;

        size = lst_x_plus.size();
        face_type = Face_X_Plus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_x_plus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx1 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx2 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx3 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);

            graphics.addCubeFace_X_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
        }

        size = lst_y_minus.size();
        face_type = Face_Y_Minus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_y_minus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx1 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx2 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx3 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);

            graphics.addCubeFace_Y_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
        }

        size = lst_x_minus.size();
        face_type = Face_X_Minus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_x_minus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx1 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx2 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx3 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);

            graphics.addCubeFace_X_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
        }

        size = lst_y_plus.size();
        face_type = Face_Y_Plus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_y_plus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx1 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx2 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx3 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);

            graphics.addCubeFace_Y_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
        }
    }

    public void drawNormalTitles(ArrayList<CubeFont> lst_x_plus,
                                 ArrayList<CubeFont> lst_x_minus,
                                 ArrayList<CubeFont> lst_y_plus,
                                 ArrayList<CubeFont> lst_y_minus,
                                 ArrayList<CubeFont> lst_z_plus,
                                 ArrayList<CubeFont> lst_z_minus,
                                 Color color) {

        int face_type;
        CubeFont cubeFont;
        TexturedQuad font;
        TexCoordsQuad coords = new TexCoordsQuad();
        int size;

        size = lst_z_minus.size();
        face_type = Face_Z_Minus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_z_minus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx1 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx2 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx3 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);

            graphics.addCubeFace_Z_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
        }

        size = lst_y_minus.size();
        face_type = Face_Y_Minus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_y_minus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx1 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx2 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx3 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);

            graphics.addCubeFace_Y_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
        }

        size = lst_z_plus.size();
        face_type = Face_Z_Plus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_z_plus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx1 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx2 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx3 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);

            graphics.addCubeFace_Z_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
        }

        size = lst_y_plus.size();
        face_type = Face_Y_Plus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_y_plus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx1 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx2 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx3 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);

            graphics.addCubeFace_Y_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
        }
    }

    public void drawHardTitles(ArrayList<CubeFont> lst_x_plus,
                               ArrayList<CubeFont> lst_x_minus,
                               ArrayList<CubeFont> lst_y_plus,
                               ArrayList<CubeFont> lst_y_minus,
                               ArrayList<CubeFont> lst_z_plus,
                               ArrayList<CubeFont> lst_z_minus,
                               Color color) {
        int face_type;
        CubeFont cubeFont;
        TexturedQuad font;
        TexCoordsQuad coords = new TexCoordsQuad();
        int size;

        size = lst_x_minus.size();
        face_type = Face_X_Minus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_x_minus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx1 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx2 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx3 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);

            graphics.addCubeFace_X_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
        }

        size = lst_y_minus.size();
        face_type = Face_Y_Minus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_y_minus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx1 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx2 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx3 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);

            graphics.addCubeFace_Y_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
        }

        size = lst_x_plus.size();
        face_type = Face_X_Plus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_x_plus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx1 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx2 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx3 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);

            graphics.addCubeFace_X_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
        }

        size = lst_y_plus.size();
        face_type = Face_Y_Plus;
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_y_plus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx1 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
            coords.tx2 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
            coords.tx3 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);

            graphics.addCubeFace_Y_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
        }
    }

    public void drawSymbols(Color color) {
        graphics.resetBufferIndices();
        graphics.bindStreamSources3d();

        drawTextsDefaultOrientation(m_lst_symbols.get(Face_X_Plus),
                m_lst_symbols.get(Face_X_Minus),
                m_lst_symbols.get(Face_Y_Plus),
                m_lst_symbols.get(Face_Y_Minus),
                m_lst_symbols.get(Face_Z_Plus),
                m_lst_symbols.get(Face_Z_Minus),
                color);

        graphics.updateBuffers();
        graphics.renderTriangles();
    }

    public void drawCubeFaceOptions() {
        Cube p = Game.cubes[2][7][3];

        float x = p.tx - HALF_CUBE_SIZE;
        float y = p.ty + HALF_CUBE_SIZE + 0.02f;
        float z = p.tz - HALF_CUBE_SIZE;
        float w = CUBE_SIZE * 5.0f * Game.getMusicVolume();
        float ws = CUBE_SIZE * 5.0f * Game.getSoundVolume();
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

    public void drawLevelCubeSymbols() {
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

    public void drawCredits() {
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

    public void drawTheCube() {
        int size;
        Cube cube;

        graphics.resetBufferIndices();
        graphics.zeroBufferPositions();
        graphics.bindStreamSources3d();
        size = m_list_cubes_base.size();
        for (int i = 0; i < size; ++i) {
            cube = m_list_cubes_base.get(i);
            graphics.addCubeWithColor(cube.tx, cube.ty, cube.tz, cube.color_current);
        }
        graphics.updateBuffers();
        graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);

        graphics.resetBufferIndices();
        size = m_list_cubes_face.size();
        for (int i = 0; i < size; ++i) {
            cube = m_list_cubes_face.get(i);
            graphics.addCubeWithColor(cube.tx, cube.ty, cube.tz, cube.color_current);
        }
        graphics.updateBuffers();
        graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);
    }

    public void drawCubeHiLite(Color color) {
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

    public void updateAnimToCredits() {
        if (m_t >= 1.0f) {
            m_state = MenuStateEnum.InCredits;
            return;
        }

        m_t += 0.05f;
        if (m_t > 1.0f) {
            m_t = 1.0f;
        }

        Utils.lerpCamera(m_camera_menu, m_camera_credits, m_t, m_camera_current);
        Game.dirty_alpha = Utils.lerp(DIRTY_ALPHA, 0f, m_t);

        m_pMenuCubeStore.update();
    }

    public void updateAnimFromCredits() {
        if (m_t >= 1.0f) {
            m_state = MenuStateEnum.InMenu;
            return;
        }

        m_t += 0.05f;
        if (m_t > 1.0f) {
            m_t = 1.0f;
        }

        Utils.lerpCamera(m_camera_credits, m_camera_menu, m_t, m_camera_current);
        Game.dirty_alpha = Utils.lerp(0f, DIRTY_ALPHA, m_t);
    }

    public void updatePlayCube() {
        if (m_pMenuCubePlay.isDone()) {
            switch (m_current_cube_face) {
                case Face_Tutorial:
                    if (m_pMenuCubePlay.m_cube_pos.z == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Menu;
                        setCurrentCubeFaceType(Face_Z_Plus);
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_X_Plus);
                        m_navigator.setup(CubeFaceNavigationEnum.Tutorial_To_Menu);
                    }

                    if (m_pMenuCubePlay.m_cube_pos.y == 7 && m_can_alter_text) {
                        m_can_alter_text = false;
                        MenuFaceBuilder.resetTransforms();
                        MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
                        releaseCubeTextsOnFace(Face_X_Minus);
                        MenuFaceBuilder.buildTexts(CubeFaceNamesEnum.Face_Tutorial, Face_X_Minus, true);
                    }

                    if (m_pMenuCubePlay.m_cube_pos.y == 1 && m_can_alter_text) {
                        m_can_alter_text = false;
                        MenuFaceBuilder.resetTransforms();
                        MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
                        releaseCubeTextsOnFace(Face_X_Minus);
                        MenuFaceBuilder.buildTexts(CubeFaceNamesEnum.Face_Tutorial, Face_X_Minus, false);
                    }
                    break;

                case Face_Menu:
                    if (m_pMenuCubePlay.m_cube_pos.x == 2 && m_pMenuCubePlay.m_cube_pos.y == 7 && m_pMenuCubePlay.m_cube_pos.z == 8) {
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_Y_Minus);
                    }

                    if (m_pMenuCubePlay.m_cube_pos.x == 2 && m_pMenuCubePlay.m_cube_pos.y == 5 && m_pMenuCubePlay.m_cube_pos.z == 8) {
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_X_Minus);
                    }

                    if (m_pMenuCubePlay.m_cube_pos.x == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Easy01;
                        setCurrentCubeFaceType(Face_X_Plus);
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_Z_Minus);
                        m_navigator.setup(CubeFaceNavigationEnum.Menu_To_Easy1);
                    }
                    break;

                case Face_Easy01:
                    if (m_pMenuCubePlay.m_cube_pos.z == 0) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Normal01;
                        setCurrentCubeFaceType(Face_Z_Minus);
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_X_Minus);
                        m_navigator.setup(CubeFaceNavigationEnum.Easy1_To_Normal1);
                    }

                    if (m_pMenuCubePlay.m_cube_pos.z == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Menu;
                        setCurrentCubeFaceType(Face_Z_Plus);
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_X_Minus);
                        m_navigator.setup(CubeFaceNavigationEnum.Easy1_To_Menu);
                    }

                    if (m_pMenuCubePlay.m_cube_pos.y == 0) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Easy02;
                        setCurrentCubeFaceType(Face_Y_Minus);
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_X_Minus);
                        m_navigator.setup(CubeFaceNavigationEnum.Easy1_To_Easy2);
                    }

                    if (m_pMenuCubePlay.m_cube_pos.y == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Easy04;
                        setCurrentCubeFaceType(Face_Y_Plus);
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_X_Minus);
                        m_navigator.setup(CubeFaceNavigationEnum.Easy1_To_Easy4);
                    }
                    break;

                case Face_Easy02:
                    if (m_pMenuCubePlay.m_cube_pos.x == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Easy01;
                        setCurrentCubeFaceType(Face_X_Plus);
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_Y_Plus);
                        m_navigator.setup(CubeFaceNavigationEnum.Easy2_To_Easy1);
                    }

                    if (m_pMenuCubePlay.m_cube_pos.x == 0) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Easy03;
                        setCurrentCubeFaceType(Face_X_Minus);
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_Y_Plus);
                        m_navigator.setup(CubeFaceNavigationEnum.Easy2_To_Easy3);
                    }
                    break;

                case Face_Easy03:
                    if (m_pMenuCubePlay.m_cube_pos.y == 0) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Easy02;
                        setCurrentCubeFaceType(Face_Y_Minus);
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_X_Plus);
                        m_navigator.setup(CubeFaceNavigationEnum.Easy3_To_Easy2);
                    }

                    if (m_pMenuCubePlay.m_cube_pos.y == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Easy04;
                        setCurrentCubeFaceType(Face_Y_Plus);
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_X_Plus);
                        m_navigator.setup(CubeFaceNavigationEnum.Easy3_To_Easy4);
                    }
                    break;

                case Face_Easy04:
                    if (m_pMenuCubePlay.m_cube_pos.x == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Easy01;
                        setCurrentCubeFaceType(Face_X_Plus);
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_Y_Minus);
                        m_navigator.setup(CubeFaceNavigationEnum.Easy4_To_Easy1);
                    }

                    if (m_pMenuCubePlay.m_cube_pos.x == 0) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Easy03;
                        setCurrentCubeFaceType(Face_X_Minus);
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_Y_Minus);
                        m_navigator.setup(CubeFaceNavigationEnum.Easy4_To_Easy3);
                    }
                    break;

                case Face_Normal01:
                    if (m_pMenuCubePlay.m_cube_pos.x == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Easy01;
                        setCurrentCubeFaceType(Face_X_Plus);
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_Z_Plus);
                        m_navigator.setup(CubeFaceNavigationEnum.Normal1_To_Easy1);
                    }

                    if (m_pMenuCubePlay.m_cube_pos.x == 0) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Hard01;
                        setCurrentCubeFaceType(Face_X_Minus);
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_Z_Plus);
                        m_navigator.setup(CubeFaceNavigationEnum.Normal1_To_Hard1);
                    }

                    if (m_pMenuCubePlay.m_cube_pos.y == 0) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Normal02;
                        setCurrentCubeFaceType(Face_Y_Minus);
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_Z_Plus);
                        m_navigator.setup(CubeFaceNavigationEnum.Normal1_To_Normal2);
                    }

                    if (m_pMenuCubePlay.m_cube_pos.y == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Normal04;
                        setCurrentCubeFaceType(Face_Y_Plus);
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_Z_Plus);
                        m_navigator.setup(CubeFaceNavigationEnum.Normal1_To_Normal4);
                    }
                    break;

                case Face_Normal02:
                    if (m_pMenuCubePlay.m_cube_pos.z == 0) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Normal01;
                        setCurrentCubeFaceType(Face_Z_Minus);
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_Y_Plus);
                        m_navigator.setup(CubeFaceNavigationEnum.Normal2_To_Normal1);
                    }

                    if (m_pMenuCubePlay.m_cube_pos.z == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Normal03;
                        setCurrentCubeFaceType(Face_Z_Plus);
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_Y_Plus);
                        m_navigator.setup(CubeFaceNavigationEnum.Normal2_To_Normal3);
                    }
                    break;

                case Face_Normal03:
                    if (m_pMenuCubePlay.m_cube_pos.y == 0) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Normal02;
                        setCurrentCubeFaceType(Face_Y_Minus);
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_Z_Minus);
                        m_navigator.setup(CubeFaceNavigationEnum.Normal3_To_Normal2);
                    }

                    if (m_pMenuCubePlay.m_cube_pos.y == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Normal04;
                        setCurrentCubeFaceType(Face_Y_Plus);
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_Z_Minus);
                        m_navigator.setup(CubeFaceNavigationEnum.Normal3_To_Normal4);
                    }
                    break;

                case Face_Normal04:
                    if (m_pMenuCubePlay.m_cube_pos.z == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Normal03;
                        setCurrentCubeFaceType(Face_Z_Plus);
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_Y_Minus);
                        m_navigator.setup(CubeFaceNavigationEnum.Normal4_To_Normal3);
                    }

                    if (m_pMenuCubePlay.m_cube_pos.z == 0) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Normal01;
                        setCurrentCubeFaceType(Face_Z_Minus);
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_Y_Minus);
                        m_navigator.setup(CubeFaceNavigationEnum.Normal4_To_Normal1);
                    }
                    break;

                case Face_Hard01:
                    if (m_pMenuCubePlay.m_cube_pos.z == 0) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Normal01;
                        setCurrentCubeFaceType(Face_Z_Minus);
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_X_Plus);
                        m_navigator.setup(CubeFaceNavigationEnum.Hard1_To_Normal1);
                    }

                    if (m_pMenuCubePlay.m_cube_pos.z == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Menu;
                        setCurrentCubeFaceType(Face_Z_Plus);
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_X_Plus);
                        m_navigator.setup(CubeFaceNavigationEnum.Hard1_To_Menu);
                    }

                    if (m_pMenuCubePlay.m_cube_pos.y == 0) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Hard02;
                        setCurrentCubeFaceType(Face_Y_Minus);
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_X_Plus);
                        m_navigator.setup(CubeFaceNavigationEnum.Hard1_To_Hard2);
                    }

                    if (m_pMenuCubePlay.m_cube_pos.y == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Hard04;
                        setCurrentCubeFaceType(Face_Y_Plus);
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_X_Plus);
                        m_navigator.setup(CubeFaceNavigationEnum.Hard1_To_Hard4);
                    }
                    break;

                case Face_Hard02:
                    if (m_pMenuCubePlay.m_cube_pos.x == 0) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Hard01;
                        setCurrentCubeFaceType(Face_X_Minus);
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_Y_Plus);
                        m_navigator.setup(CubeFaceNavigationEnum.Hard2_To_Hard1);
                    }

                    if (m_pMenuCubePlay.m_cube_pos.x == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Hard03;
                        setCurrentCubeFaceType(Face_X_Plus);
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_Y_Plus);
                        m_navigator.setup(CubeFaceNavigationEnum.Hard2_To_Hard3);
                    }
                    break;

                case Face_Hard03:
                    if (m_pMenuCubePlay.m_cube_pos.y == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Hard04;
                        setCurrentCubeFaceType(Face_Y_Plus);
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_X_Minus);
                        m_navigator.setup(CubeFaceNavigationEnum.Hard3_To_Hard4);
                    }

                    if (m_pMenuCubePlay.m_cube_pos.y == 0) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Hard02;
                        setCurrentCubeFaceType(Face_Y_Minus);
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_X_Minus);
                        m_navigator.setup(CubeFaceNavigationEnum.Hard3_To_Hard2);
                    }
                    break;

                case Face_Hard04:
                    if (m_pMenuCubePlay.m_cube_pos.x == 8) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Hard03;
                        setCurrentCubeFaceType(Face_X_Plus);
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_Y_Minus);
                        m_navigator.setup(CubeFaceNavigationEnum.Hard4_To_Hard3);
                    }

                    if (m_pMenuCubePlay.m_cube_pos.x == 0) {
                        m_current_cube_face = CubeFaceNamesEnum.Face_Hard01;
                        setCurrentCubeFaceType(Face_X_Minus);
                        m_pMenuCubePlay.moveOnAxis(AxisMovement_Y_Minus);
                        m_navigator.setup(CubeFaceNavigationEnum.Hard4_To_Hard1);
                    }
                    break;

                default:
                    break;
            }
        } else {
            m_pMenuCubePlay.update();
            m_can_alter_text = true;
        }
    }

    public void updateOptionsCube() {
        if (m_pMenuCubeOptions.isDone()) {
            switch (m_current_cube_face) {
                case Face_Menu:
                    if (m_pMenuCubeOptions.m_cube_pos.y == 8) {
                        m_prev_face = CubeFaceNamesEnum.Face_Menu;
                        m_current_cube_face = CubeFaceNamesEnum.Face_Options;
                        setCurrentCubeFaceType(Face_Y_Plus);
                        m_pMenuCubeOptions.moveOnAxis(AxisMovement_Z_Minus);

                        m_navigator.setup(CubeFaceNavigationEnum.Menu_To_Options);
                    }

                    if (m_pMenuCubeOptions.m_cube_pos.x == 7) {
                        if (CubeFaceNamesEnum.Face_Options == m_prev_face) {
                            m_prev_face = CubeFaceNamesEnum.Face_Empty;
                            m_pMenuCubeOptions.moveOnAxis(AxisMovement_X_Minus);
                        }
                    }
                    break;

                case Face_Options:
                    if (m_pMenuCubeOptions.m_cube_pos.z == 8) {
                        m_prev_face = CubeFaceNamesEnum.Face_Options;
                        m_current_cube_face = CubeFaceNamesEnum.Face_Menu;
                        setCurrentCubeFaceType(Face_Z_Plus);
                        m_pMenuCubeOptions.moveOnAxis(AxisMovement_Y_Minus);

                        m_navigator.setup(CubeFaceNavigationEnum.Options_To_Menu);
                    }

//                    if (m_pMenuCubeOptions.m_cube_pos.x == 7 && m_pMenuCubeOptions.m_cube_pos.y == 8 && m_pMenuCubeOptions.m_cube_pos.z == 1) {
//                    ArrayList<CubeFont>::iterator it;
//                    for (it = Game.ar_cubefacedata[Face_Y_Plus].lst_symbols.begin(); it != Game.ar_cubefacedata[Face_Y_Plus].lst_symbols.end(); ++it)
//                    {
//                        (*it).visible = true;
//                    }
//                    }
                    break;

                default:
                    break;
            } // switch
        } else {
            m_pMenuCubeOptions.update();
        }
    }

    public void updateStoreCube() {
        if (m_pMenuCubeStore.isDone()) {
            switch (m_current_cube_face) {
                case Face_Menu:
                    if (m_pMenuCubeStore.m_cube_pos.y == 0) {
                        m_prev_face = CubeFaceNamesEnum.Face_Menu;
                        m_current_cube_face = CubeFaceNamesEnum.Face_Store;
                        setCurrentCubeFaceType(Face_Y_Minus);
                        m_pMenuCubeStore.moveOnAxis(AxisMovement_Z_Minus);

                        m_navigator.setup(CubeFaceNavigationEnum.Menu_To_Store);
                    }

                    if (m_pMenuCubeStore.m_cube_pos.x == 7) {
                        if (!m_pMenuCubeOptions.isDone() || !m_pMenuCubePlay.isDone() || m_prev_face == CubeFaceNamesEnum.Face_Store) {
                            if (m_prev_face == CubeFaceNamesEnum.Face_Store) {
                                m_prev_face = CubeFaceNamesEnum.Face_Empty;
                            }

                            m_pMenuCubeStore.moveOnAxis(AxisMovement_X_Minus);
                        }
                    }
                    break;

                case Face_Store:
                    if (m_pMenuCubeStore.m_cube_pos.z == 8) {
                        m_prev_face = CubeFaceNamesEnum.Face_Store;
                        m_current_cube_face = CubeFaceNamesEnum.Face_Menu;
                        setCurrentCubeFaceType(Face_Z_Plus);
                        m_pMenuCubeStore.moveOnAxis(AxisMovement_Y_Plus);

                        Game.hideProgressIndicator();
                        m_navigator.setup(CubeFaceNavigationEnum.Store_To_Menu);
                    }
                    break;

                default:
                    break;
            }
        } else {
            m_pMenuCubeStore.update();
        }
    }

    public void updateCubes() {
        for (int i = 0; i < 6; ++i) {
            m_lst_titles.get(i).clear();
            m_lst_texts.get(i).clear();
            m_lst_symbols.get(i).clear();
        }

        int face_type;
        int size;
        Cube cube;

        size = m_list_cubes_base.size();
        for (int i = 0; i < size; ++i) {
            cube = m_list_cubes_base.get(i);

            face_type = Face_X_Plus;
            if (null != cube.ar_fonts[face_type]) {
                m_lst_texts.get(face_type).add(cube.ar_fonts[face_type]);
            }

            if (null != cube.ar_symbols[face_type]) {
                m_lst_symbols.get(face_type).add(cube.ar_symbols[face_type]);
            }


            face_type = Face_X_Minus;
            if (null != cube.ar_fonts[face_type]) {
                m_lst_texts.get(face_type).add(cube.ar_fonts[face_type]);
            }

            if (null != cube.ar_symbols[face_type]) {
                m_lst_symbols.get(face_type).add(cube.ar_symbols[face_type]);
            }


            face_type = Face_Y_Plus;
            if (null != cube.ar_fonts[face_type]) {
                m_lst_texts.get(face_type).add(cube.ar_fonts[face_type]);
            }

            if (null != cube.ar_symbols[face_type]) {
                m_lst_symbols.get(face_type).add(cube.ar_symbols[face_type]);
            }


            face_type = Face_Y_Minus;
            if (null != cube.ar_fonts[face_type]) {
                m_lst_texts.get(face_type).add(cube.ar_fonts[face_type]);
            }

            if (null != cube.ar_symbols[face_type]) {
                m_lst_symbols.get(face_type).add(cube.ar_symbols[face_type]);
            }


            face_type = Face_Z_Plus;
            if (null != cube.ar_fonts[face_type]) {
                m_lst_texts.get(face_type).add(cube.ar_fonts[face_type]);
            }

            if (null != cube.ar_symbols[face_type]) {
                m_lst_symbols.get(face_type).add(cube.ar_symbols[face_type]);
            }


            face_type = Face_Z_Minus;
            if (null != cube.ar_fonts[face_type]) {
                m_lst_texts.get(face_type).add(cube.ar_fonts[face_type]);
            }

            if (null != cube.ar_symbols[face_type]) {
                m_lst_symbols.get(face_type).add(cube.ar_symbols[face_type]);
            }
        }

        size = m_list_cubes_face.size();
        for (int i = 0; i < size; ++i) {
            cube = m_list_cubes_face.get(i);

            face_type = Face_X_Plus;
            if (null != cube.ar_fonts[face_type]) {
                m_lst_titles.get(face_type).add(cube.ar_fonts[face_type]);
            }

            if (null != cube.ar_symbols[face_type]) {
                m_lst_symbols.get(face_type).add(cube.ar_symbols[face_type]);
            }


            face_type = Face_X_Minus;
            if (null != cube.ar_fonts[face_type]) {
                m_lst_titles.get(face_type).add(cube.ar_fonts[face_type]);
            }

            if (null != cube.ar_symbols[face_type]) {
                m_lst_symbols.get(face_type).add(cube.ar_symbols[face_type]);
            }


            face_type = Face_Y_Plus;
            if (null != cube.ar_fonts[face_type]) {
                m_lst_titles.get(face_type).add(cube.ar_fonts[face_type]);
            }

            if (null != cube.ar_symbols[face_type]) {
                m_lst_symbols.get(face_type).add(cube.ar_symbols[face_type]);
            }


            face_type = Face_Y_Minus;
            if (null != cube.ar_fonts[face_type]) {
                m_lst_titles.get(face_type).add(cube.ar_fonts[face_type]);
            }

            if (null != cube.ar_symbols[face_type]) {
                m_lst_symbols.get(face_type).add(cube.ar_symbols[face_type]);
            }


            face_type = Face_Z_Plus;
            if (null != cube.ar_fonts[face_type]) {
                m_lst_titles.get(face_type).add(cube.ar_fonts[face_type]);
            }

            if (null != cube.ar_symbols[face_type]) {
                m_lst_symbols.get(face_type).add(cube.ar_symbols[face_type]);
            }


            face_type = Face_Z_Minus;
            if (null != cube.ar_fonts[face_type]) {
                m_lst_titles.get(face_type).add(cube.ar_fonts[face_type]);
            }

            if (null != cube.ar_symbols[face_type]) {
                m_lst_symbols.get(face_type).add(cube.ar_symbols[face_type]);
            }
        }

//    printf("\nFace_Y_Symbol Size: %lu", m_lst_symbols[Face_Y_Plus].size());

        if (m_menu_cube_hilite != null) {
            m_hilite_alpha += 0.05f;

            if (m_hilite_alpha > 0.2f) {
                m_hilite_alpha = 0.2f;
            }
        }

        size = m_list_cubes_base.size();
        for (int i = 0; i < size; ++i) {
            cube = m_list_cubes_base.get(i);
            cube.warmByFactor(WARM_FACTOR);
        }
    }

    public void updateInCredits() {
        m_credits_offset -= 0.05f;

        if (m_credits_offset < -5.0f) {
            m_credits_offset = 20.0f;
        }
    }

    public void updateInMenu() {
        switch (m_current_cube_face) {
            case Face_Menu:
                break;

            case Face_Store:
                m_pStoreCubeNoAds.update();
                m_pStoreCubeSolvers.update();
                m_pStoreCubeRestore.update();

                if (m_pStoreCubeNoAds.isDone() && m_pStoreCubeNoAds.m_cube_pos.x == 7) {
                    //Game.purchaseRemoveAds();
                    m_pStoreCubeNoAds.moveOnAxis(AxisMovement_X_Minus);
                }

                if (m_pStoreCubeSolvers.isDone() && m_pStoreCubeSolvers.m_cube_pos.x == 7) {
                    //Game.purchaseSolvers();
                    m_pStoreCubeSolvers.moveOnAxis(AxisMovement_X_Minus);
                }

                if (m_pStoreCubeRestore.isDone() && m_pStoreCubeRestore.m_cube_pos.x == 7) {
                    //Game.purchaseRestore();
                    m_pStoreCubeRestore.moveOnAxis(AxisMovement_X_Minus);
                }
                break;

            default:
                break;
        }

        updatePlayCube();
        updateOptionsCube();
        updateStoreCube();
    }

    public void updateHilite() {
        m_hilite_timeout -= 0.01f;

        if (m_hilite_timeout < 0.0f) {
            m_hilite_timeout = 0.05f;

            Color color = new Color(160, 160, 160, 255);

            if (!m_pMenuCubePlay.lst_cubes_to_hilite.isEmpty()) {
                Cube p = m_pMenuCubePlay.lst_cubes_to_hilite.get(0);
                m_pMenuCubePlay.lst_cubes_to_hilite.remove(p);

                p.color_current = color;
            }

            if (!m_pMenuCubeOptions.lst_cubes_to_hilite.isEmpty()) {
                Cube p = m_pMenuCubeOptions.lst_cubes_to_hilite.get(0);
                m_pMenuCubeOptions.lst_cubes_to_hilite.remove(p);

                p.color_current = color;
            }

            if (!m_pMenuCubeStore.lst_cubes_to_hilite.isEmpty()) {
                Cube p = m_pMenuCubeStore.lst_cubes_to_hilite.get(0);
                m_pMenuCubeStore.lst_cubes_to_hilite.remove(p);

                p.color_current = color;
            }

            if (!m_pStoreCubeNoAds.lst_cubes_to_hilite.isEmpty()) {
                Cube p = m_pStoreCubeNoAds.lst_cubes_to_hilite.get(0);
                m_pStoreCubeNoAds.lst_cubes_to_hilite.remove(p);

                p.color_current = color;
            }

            if (!m_pStoreCubeRestore.lst_cubes_to_hilite.isEmpty()) {
                Cube p = m_pStoreCubeRestore.lst_cubes_to_hilite.get(0);
                m_pStoreCubeRestore.lst_cubes_to_hilite.remove(p);

                p.color_current = color;
            }
        }
    }

    public void update() {
        if (m_showing_help) {
            m_show_help_timeout -= 0.01f;

            if (m_show_help_timeout < 0.0f) {
                m_showing_help = false;

                //printf("\nRestore original text\n");
                releaseCubeTextsOnFace(Face_Z_Plus);
                //cCreator::AlterRedCubeFontsOnFaceMain(false);
                MenuFaceBuilder.resetTransforms();
                MenuFaceBuilder.buildTexts(CubeFaceNamesEnum.Face_Menu, Face_Z_Plus, false);
            }
        }

        if (!m_navigator.isCurrentNavigation(CubeFaceNavigationEnum.NoNavigation)) {
            m_navigator.update();
        } else {
            switch (m_state) {
                case InMenu: updateInMenu(); break;
                case InCredits: updateInCredits(); break;
                case AnimToCredits: updateAnimToCredits(); break;
                case AnimFromCredits: updateAnimFromCredits(); break;
            }
            updateHilite();
        }
        updateCubes();
    }

    public void eventPlayLevel(DifficultyEnum difficulty, int level_number) {
        if (!Game.getCanPlayLockedLevels()) {
            switch (difficulty) {
                case Easy:
                    if (LEVEL_LOCKED == Cubetraz.getStarsEasy(level_number)) {
                        Game.playSound(SOUND_TAP_ON_LOCKED_LEVEL_CUBE);
                        return;
                    }
                    break;

                case Normal:
                    if (LEVEL_LOCKED == Cubetraz.getStarsNormal(level_number)) {
                        Game.playSound(SOUND_TAP_ON_LOCKED_LEVEL_CUBE);
                        return;
                    }
                    break;

                case Hard:
                    if (LEVEL_LOCKED == Cubetraz.getStarsHard(level_number)) {
                        Game.playSound(SOUND_TAP_ON_LOCKED_LEVEL_CUBE);
                        return;
                    }
                    break;
            }
        }

        Game.playSound(SOUND_TAP_ON_LEVEL_CUBE);

        Game.stopMusic();
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

        Game.level_init_data.difficulty = difficulty;
        Game.level_init_data.level_number = level_number;
        Game.level_init_data.init_action = LevelInitActionEnum.FullInit;

        Game.anim_init_data.type = AnimTypeEnum.AnimToLevel;

        Game.anim_init_data.list_cubes_base.clear();

        int size = m_list_cubes_base.size();
        Cube cube;
        for (int i = 0; i < size; ++i) {
            cube = m_list_cubes_base.get(i);
            Game.anim_init_data.list_cubes_base.add(cube);
        }

        Game.anim_init_data.camera_from.init(m_camera_current);
        Game.anim_init_data.camera_to.init(Game.level.m_camera_level);

        Game.anim_init_data.pos_light_from.init(m_pos_light_current);
        Game.anim_init_data.pos_light_to.init(Game.level.m_pos_light);

        Game.showScene(Scene_Anim);
    }

    public void eventShowCredits() {
        m_credits_offset = 20.0f;

        m_t = 0.0f;
        m_state = MenuStateEnum.AnimToCredits;

        float divisor = 20.0f;

        m_interpolators[0].setup(m_camera_menu.eye.x, m_camera_credits.eye.x, divisor);
        m_interpolators[1].setup(m_camera_menu.eye.y, m_camera_credits.eye.y, divisor);
        m_interpolators[2].setup(m_camera_menu.eye.z, m_camera_credits.eye.z, divisor);

        m_interpolators[3].setup(m_camera_menu.target.x, m_camera_credits.target.x, divisor);
        m_interpolators[4].setup(m_camera_menu.target.y, m_camera_credits.target.y, divisor);
        m_interpolators[5].setup(m_camera_menu.target.z, m_camera_credits.target.z, divisor);

        //Game.hideGameCenterInfo();
    }

    public void renderForPicking(PickRenderTypeEnum type) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glDisable(GL_LIGHTING);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);

        glDisable(GL_TEXTURE_2D);

        graphics.setProjection3D();
        graphics.setModelViewMatrix3D(m_camera_current);

        m_navigator.applyRotations();

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
                graphics.addCubeSize(m_pMenuCubePlay.pos.x, m_pMenuCubePlay.pos.y, m_pMenuCubePlay.pos.z, HALF_CUBE_SIZE * 1.5f, m_pMenuCubePlay.color);
                break;

            case RenderOnlyMovingCubeStore:
                graphics.addCubeSize(m_pMenuCubeStore.pos.x, m_pMenuCubeStore.pos.y, m_pMenuCubeStore.pos.z, HALF_CUBE_SIZE * 1.5f, m_pMenuCubeStore.color);
                break;

            case RenderOnlyMovingCubeOptions:
                graphics.addCubeSize(m_pMenuCubeOptions.pos.x, m_pMenuCubeOptions.pos.y, m_pMenuCubeOptions.pos.z, HALF_CUBE_SIZE * 1.5f, m_pMenuCubeOptions.color);
                break;

            case RenderOnlyCubeCredits:
                graphics.addCubeSize(m_cubeCredits.pos.x, m_cubeCredits.pos.y, m_cubeCredits.pos.z, HALF_CUBE_SIZE * 1.5f, m_cubeCredits.color);
                break;

            case RenderOnlyMovingCubes:
                switch (m_current_cube_face) {
                    case Face_Options:
                        graphics.addCubeSize(m_pMenuCubeOptions.pos.x, m_pMenuCubeOptions.pos.y, m_pMenuCubeOptions.pos.z, HALF_CUBE_SIZE * 1.5f, m_pMenuCubeOptions.color);
                        break;

                    case Face_Store:
                        graphics.addCubeSize(m_pMenuCubeStore.pos.x, m_pMenuCubeStore.pos.y, m_pMenuCubeStore.pos.z, HALF_CUBE_SIZE * 1.5f, m_pMenuCubeStore.color);
                        graphics.addCubeSize(m_pStoreCubeNoAds.pos.x, m_pStoreCubeNoAds.pos.y, m_pStoreCubeNoAds.pos.z, HALF_CUBE_SIZE * 1.5f, m_pStoreCubeNoAds.color);
                        graphics.addCubeSize(m_pStoreCubeRestore.pos.x, m_pStoreCubeRestore.pos.y, m_pStoreCubeRestore.pos.z, HALF_CUBE_SIZE * 1.5f, m_pStoreCubeRestore.color);
                        graphics.addCubeSize(m_pStoreCubeSolvers.pos.x, m_pStoreCubeSolvers.pos.y, m_pStoreCubeSolvers.pos.z, HALF_CUBE_SIZE * 1.5f, m_pStoreCubeSolvers.color);
                        break;

                    case Face_Menu:
                        graphics.addCubeSize(m_pMenuCubePlay.pos.x, m_pMenuCubePlay.pos.y, m_pMenuCubePlay.pos.z, HALF_CUBE_SIZE * 1.5f, m_pMenuCubePlay.color);
                        graphics.addCubeSize(m_pMenuCubeOptions.pos.x, m_pMenuCubeOptions.pos.y, m_pMenuCubeOptions.pos.z, HALF_CUBE_SIZE * 1.5f, m_pMenuCubeOptions.color);
                        graphics.addCubeSize(m_pMenuCubeStore.pos.x, m_pMenuCubeStore.pos.y, m_pMenuCubeStore.pos.z, HALF_CUBE_SIZE * 1.5f, m_pMenuCubeStore.color);
                        graphics.addCubeSize(m_cubeCredits.pos.x, m_cubeCredits.pos.y, m_cubeCredits.pos.z, HALF_CUBE_SIZE * 1.5f, m_cubeCredits.color);
                        break;

                    default:
                        graphics.addCubeSize(m_pMenuCubePlay.pos.x, m_pMenuCubePlay.pos.y, m_pMenuCubePlay.pos.z, HALF_CUBE_SIZE * 1.5f, m_pMenuCubePlay.color);
                        break;
                }
                break;

            case RenderOnlyLevelCubes: {
                int size = Game.ar_cubefacedata[m_current_cube_face_type].lst_level_cubes.size();
                LevelCube p;
                LevelCube levelCube;

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
//        renderForPicking(PickRenderTypeEnum.RenderOnlyMovingCubePlay);
//        if (true) {
//            return;
    //        }

        graphics.setProjection2D();
        graphics.setModelViewMatrix2D();
        graphics.bindStreamSources2d();

        glEnable(GL_BLEND);
        glDisable(GL_LIGHTING);
        glDepthMask(false); //GL_FALSE);
        glEnable(GL_TEXTURE_2D);

        Color color = new Color(255, 255, 255, (int) Game.dirty_alpha);
        graphics.drawFullScreenTexture(Graphics.texture_id_dirty, color);

        glDepthMask(true);

        graphics.setProjection3D();
        graphics.setModelViewMatrix3D(m_camera_current);
        graphics.bindStreamSources3d();

        float posLight[] = { m_pos_light_current.x, m_pos_light_current.y, m_pos_light_current.z, 1.0f };
        glLightfv(GL_LIGHT0, GL_POSITION, posLight, 0);

        glEnable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);

        //glEnableClientState(GL_NORMAL_ARRAY);
        //glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        if (MenuStateEnum.InCredits == m_state) {
            glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_credits);
            drawCredits();
            //return;
        }

//        if (false) {
//            glDisable(GL_TEXTURE_2D);
//            glDisable(GL_LIGHTING);
//            graphics.drawAxes();
//            glEnable(GL_LIGHTING);
//            glEnable(GL_TEXTURE_2D);
//        }



        m_navigator.applyRotations();

        glEnable(GL_DEPTH_TEST);
        glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_gray_concrete);
        drawTheCube();

//        if (false) { //#ifdef DRAW_AXES_CUBE
//            glDisable(GL_TEXTURE_2D);
//            glDisable(GL_LIGHTING);
//            graphics.drawAxes();
//            glEnable(GL_LIGHTING);
//            glEnable(GL_TEXTURE_2D);
//        }

        glPushMatrix();
        glTranslatef(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);

        glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_level_cubes);
        drawLevelCubes();

        glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_player);
        drawMenuCubes();

        glPopMatrix();

        glDisable(GL_LIGHTING);

        graphics.resetBufferIndices();
        glEnable(GL_BLEND);
        graphics.bindStreamSources3d();

        glPushMatrix();
            glTranslatef(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);

            glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_fonts);

            color = Game.getTextColor();
            drawTexts(color);

            color = Game.getTitleColor();
            drawTextsTitles(color);

            glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_numbers);
            drawLevelNumbers();

            glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_symbols);
            drawLevelCubeSymbols();

            color = Game.getSymbolColor();
            drawSymbols(color);

            if (m_menu_cube_hilite != null) {
                color = new Color(255, 255, 255, (int)(m_hilite_alpha * 255));
                glDisable(GL_DEPTH_TEST);
                drawCubeHiLite(color);
                glEnable(GL_DEPTH_TEST);
            }

            if (CubeFaceNamesEnum.Face_Options == m_current_cube_face ||
                m_navigator.isCurrentNavigation(CubeFaceNavigationEnum.Menu_To_Options) ||
                m_navigator.isCurrentNavigation(CubeFaceNavigationEnum.Options_To_Menu)) {
                drawCubeFaceOptions();
            }
        glPopMatrix();
    }

    public void onFingerDown(float x, float y, int finger_count) {
        //System.out.println("Menu::OnFingerDown " + x + ", y:" + y);

        if (m_navigator.isCurrentNavigation(
                CubeFaceNavigationEnum.NoNavigation) &&
                m_pMenuCubePlay.isDone() &&
                m_pMenuCubeOptions.isDone() &&
                m_pMenuCubeStore.isDone() &&
                m_pStoreCubeRestore.isDone() &&
                m_pStoreCubeNoAds.isDone()) {
            mIsFingerDown = true;

            mPosDown.x = x;
            mPosDown.y = y;

            renderForPicking(PickRenderTypeEnum.RenderOnlyMovingCubes);

            m_color_down = Graphics.getColorFromScreen(mPosDown);
            MenuCube menuCube = getMovingCubeFromColor(m_color_down.r);

            if (menuCube != null) {
                m_hilite_alpha = 0.0f;
                m_menu_cube_hilite = menuCube;
                CubePos cp = m_menu_cube_hilite.m_cube_pos;
                m_font_hilite.init(SymbolHilite, cp);
            }
        }
    }

    public void onFingerMove(float prev_x, float prev_y, float cur_x, float cur_y, int finger_count) {
        if (mIsFingerDown) {
            //System.out.println("Menu::OnFingerMove " + cur_x + ", " + cur_y);
            mPosMove.x = cur_x;
            mPosMove.y = cur_y;

            float dist = Utils.getDistance2D(mPosDown, mPosMove);
            //System.out.println("OnFingerMove: distance " + dist);

            if (dist > 20.0f * Graphics.device_scale) {
                mIsSwipe = true;
            }
        }
    }

    public void onFingerUp(float x, float y, int finger_count) {
        mIsFingerDown = false;

        if (!m_pMenuCubePlay.isDone() || !m_pMenuCubeOptions.isDone() || !m_pMenuCubeStore.isDone()) {
            return;
        }

        m_menu_cube_hilite = null;

        mPosUp.x = x;
        mPosUp.y = y;

        if (MenuStateEnum.InCredits == m_state || MenuStateEnum.AnimToCredits == m_state || MenuStateEnum.AnimFromCredits == m_state) {
            if (MenuStateEnum.InCredits == m_state) {
                m_t = 0.0f;
                m_state = MenuStateEnum.AnimFromCredits;
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

    public void fingerUpOnFaceOptions() {
        renderForPicking(PickRenderTypeEnum.RenderOnlyOptions);
        m_color_down = Graphics.getColorFromScreen(mPosDown);
        m_color_up = Graphics.getColorFromScreen(mPosUp);

        int downR = (int)(m_color_down.r * 255f);
        int upR = (int)(m_color_up.r * 255f);

        if (downR == upR) {
            switch (downR) {
                case 255:
                    Game.musicVolumeUp();
                    Game.playSound(SOUND_VOLUME_UP);
                    break;

                case 254:
                    Game.musicVolumeDown();
                    Game.playSound(SOUND_VOLUME_DOWN);
                    break;

                case 253:
                    Game.soundVolumeUp();
                    Game.playSound(SOUND_VOLUME_UP);
                    break;

                case 252:
                    Game.soundVolumeDown();
                    Game.playSound(SOUND_VOLUME_DOWN);
                    break;

                default:
                    break;
            }
        }
    }

    public void fingerUpOnFacesEasy() {
        renderForPicking(PickRenderTypeEnum.RenderOnlyLevelCubes);
        m_color_down = Graphics.getColorFromScreen(mPosDown);
        m_color_up = Graphics.getColorFromScreen(mPosUp);

        if ((int) m_color_down.r == (int) m_color_up.r) {
            int realColor = (int)(m_color_up.r * 255f);
            int level_number = 255 - realColor;

            if (level_number >= 1 && level_number <= 60) {
                eventPlayLevel(DifficultyEnum.Easy, level_number);
            }
        }
    }

    public void fingerUpOnFacesNormal() {
        renderForPicking(PickRenderTypeEnum.RenderOnlyLevelCubes);
        m_color_down = Graphics.getColorFromScreen(mPosDown);
        m_color_up = Graphics.getColorFromScreen(mPosUp);

        if ((int) m_color_down.r == (int) m_color_up.r) {
            int realColor = (int)(m_color_up.r * 255f);
            int level_number = 255 - realColor;

            if (level_number >= 1 && level_number <= 60) {
                eventPlayLevel(DifficultyEnum.Normal, level_number);
            }
        }
    }

    public void fingerUpOnFacesHard() {
        renderForPicking(PickRenderTypeEnum.RenderOnlyLevelCubes);
        m_color_down = Graphics.getColorFromScreen(mPosDown);
        m_color_up = Graphics.getColorFromScreen(mPosUp);

        if ((int) m_color_down.r == (int) m_color_up.r) {
            int realColor = (int)(m_color_up.r * 255f);
            int level_number = 255 - realColor;

            if (level_number >= 1 && level_number <= 60) {
                eventPlayLevel(DifficultyEnum.Hard, level_number);
            }
        }
    }

    public void fingerUpOnFaceMenu() {
        renderForPicking(PickRenderTypeEnum.RenderOnlyMovingCubes);
        m_color_down = Graphics.getColorFromScreen(mPosDown);
        m_color_up = Graphics.getColorFromScreen(mPosUp);

        if ((int) m_color_down.r == (int) m_color_up.r) {
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

    public void handleSwipe() {
        SwipeInfo swipeInfo = Engine.getSwipeDirAndLength(mPosDown, mPosUp);

        if (swipeInfo.length > (30.0f * graphics.scaleFactor)) {
            renderForPicking(PickRenderTypeEnum.RenderOnlyMovingCubes);
            Color down_color = Graphics.getColorFromScreen(mPosDown);
            //printf("\nOnFingerUp [SWIPE] color is: %d, %d, %d, %d", m_down_color.r, m_down_color.g, m_down_color.b, m_down_color.a);

            MenuCube menuCube = getMovingCubeFromColor(down_color.r);

            if (menuCube != null) {
                switch (swipeInfo.swipeDir) {
                    case SwipeLeft:
                        switch (m_current_cube_face) {
                            case Face_Menu:
                            case Face_Options:
                            case Face_Store:
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
                            case Face_Store:
                                menuCube.moveOnAxis(AxisMovement_X_Plus);
                                if (CubeFaceNamesEnum.Face_Menu == m_current_cube_face) {
                                    if (menuCube != m_pMenuCubeOptions) {
                                        if (7 == m_pMenuCubeOptions.m_cube_pos.x) {
                                            m_pMenuCubeOptions.moveOnAxis(AxisMovement_X_Minus);
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
                            case Face_Store: menuCube.moveOnAxis(AxisMovement_Z_Plus); break;
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
                            case Face_Store: menuCube.moveOnAxis(AxisMovement_Z_Minus); break;
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

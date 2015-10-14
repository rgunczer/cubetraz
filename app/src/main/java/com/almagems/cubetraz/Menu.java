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

    public enum LevelCubeDecalTypeEnum {
        LevelCubeDecalNumber,
        LevelCubeDecalStars,
        LevelCubeDecalSolver
    }

    private final ArrayList<ArrayList<CubeFont>> m_lst_titles = new ArrayList<ArrayList<CubeFont>>(6);
    private final ArrayList<ArrayList<CubeFont>> m_lst_texts = new ArrayList<ArrayList<CubeFont>>(6);
    private final ArrayList<ArrayList<CubeFont>> m_lst_symbols = new ArrayList<ArrayList<CubeFont>>(6);

    private MenuStateEnum m_state;

    private float m_t;
    private float zoom;

    private float m_hilite_timeout;

    private MenuNavigator m_navigator;

    private boolean m_can_alter_text;

    private float m_credits_offset;

    private Color m_color_down;
    private Color m_color_up;

    private boolean m_showing_help;
    private float m_show_help_timeout;

    private Camera m_camera_menu;
    private Camera m_camera_credits;
    private Camera m_camera_current;

    private int m_prev_face;
    private float m_menu_rotation;

    private Vector m_pos_light_menu;
    private Vector m_pos_light_current;

    private MenuCube m_pMenuCubePlay;
    private MenuCube m_pMenuCubeOptions;
    private MenuCube m_pMenuCubeStore;

    private final MenuCube[] m_arOptionsCubes = new MenuCube[4];

        // store
    private MenuCube m_pStoreCubeNoAds;
    private MenuCube m_pStoreCubeSolvers;
    private MenuCube m_pStoreCubeRestore;

    private MenuCube m_pCubeCredits;

    private MenuCube m_menu_cube_hilite;
    private CubeFont m_font_hilite;
    private float m_hilite_alpha;

    // fonts on red cubes
    private CubeFont m_cubefont_play;
    private CubeFont m_cubefont_options;
    private CubeFont m_cubefont_store;
    private CubeFont m_cubefont_noads;
    private CubeFont m_cubefont_solvers;
    private CubeFont m_cubefont_restore;

// Cubes
    private final ArrayList<Cube> m_list_cubes_base = new ArrayList<Cube>();
    private final ArrayList<Cube> m_list_cubes_face = new ArrayList<Cube>();

    private float m_target_rotation_degree;

    private final EaseOutDivideInterpolation[] m_interpolators = new EaseOutDivideInterpolation[6];

    private CubeFaceNamesEnum m_current_cube_face;
    private int m_current_cube_face_type;
    
    private MenuCube getMovingCubeFromColor(int color) {
        switch (color) {
            case 255: return m_pMenuCubePlay;
            case 200: return m_pMenuCubeOptions;
            case 100: return m_pMenuCubeStore;

            case  40: return m_pStoreCubeNoAds;
            case  50: return m_pStoreCubeSolvers;
            case  60: return m_pStoreCubeRestore;
        }
        return null;
    }

    public void dontHiliteMenuCube() { m_menu_cube_hilite = null; }

    public Camera getCamera() { return m_camera_menu; }
    public Vector getLightPositon() { return m_pos_light_menu; }


    public Menu() {
        m_can_alter_text = false;

        m_pos_light_menu = new Vector(-10.0f, 3.0f, 12.0f);

        m_cubefont_play = new CubeFont();
        m_cubefont_options = new CubeFont();
        m_cubefont_store = new CubeFont();
        m_cubefont_noads = new CubeFont();
        m_cubefont_solvers = new CubeFont();
        m_cubefont_restore = new CubeFont();
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

        m_camera_menu.eye = m_camera_menu.eye.scale(Graphics.aspectRatio);
        m_camera_credits.eye = m_camera_credits.eye.scale(Graphics.aspectRatio);

        m_pos_light_current = m_pos_light_menu;
        m_camera_current = m_camera_menu;
    }

    public public void Init() {
        setupCameras();
        //printf("\ncCube size: %lu byte, all cubes size: %lu kbyte\n", sizeof(cCube), (sizeof(cCube)*9*9*9) / 1024);
        //cMenuFaceBuilder::Custom();
        //Game.dirty_alpha = DIRTY_ALPHA;
        //Game.playMusic(MUSIC_CPU);

        m_hilite_timeout = 0.0f;
        m_menu_cube_hilite = null;
        Creator._pHost = this;
        m_showing_help = false;
        m_navigator.m_menu = this;
        m_prev_face = -1;
        mIsFingerDown = false;
        mIsSwipe = false;
        m_pos_light_current = m_pos_light_menu;
        m_camera_current = m_camera_menu;

        Game.resetCubes();
        Game.setupHollowCube();
        Game.buildVisibleCubesList(m_list_cubes_base);  // could be put in a VBO

        if (true == Game.menu_init_data.reappear) {
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
            //cCreator::CreateStaticTexts();

            m_navigator.init(this);
            m_current_cube_face = CubeFaceNamesEnum.Face_Tutorial;
            setCurrentCubeFaceType(Face_X_Minus);

            m_navigator.createMenuFaces(true);

            m_pMenuCubePlay.setCubePos(new CubePos(0, 5, 4));
            m_pMenuCubeOptions.setCubePos(new CubePos(1, 3, 8));
            m_pMenuCubeStore.setCubePos(new CubePos(1, 1, 8));

            //m_pCubeCredits.SetCubePos(CubePos(1, 1, 8));
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
        for(int i = 0; i < size; ++i) {
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
        Graphics.prepare();
        Graphics.setStreamSource();

        Color color = new Color(255, 255, 255, 255);
        MenuCube p = m_pMenuCubePlay;

        if (p.visible) {
            Graphics.addCubeSize(p.pos.x, p.pos.y, p.pos.z, HALF_CUBE_SIZE, color);
        }

        if (m_current_cube_face == CubeFaceNamesEnum.Face_Menu ||
            m_current_cube_face == CubeFaceNamesEnum.Face_Options ||
            m_navigator.isCurrentNavigation(CubeFaceNavigationEnum.Menu_To_Options) ||
            m_navigator.isCurrentNavigation(CubeFaceNavigationEnum.Menu_To_Easy1)) {
            p = m_pMenuCubeOptions;
            Graphics.addCubeSize(p.pos.x, p.pos.y, p.pos.z, HALF_CUBE_SIZE, color);
        }

        if (m_current_cube_face == CubeFaceNamesEnum.Face_Menu ||
            m_current_cube_face == CubeFaceNamesEnum.Face_Store ||
            m_navigator.isCurrentNavigation(CubeFaceNavigationEnum.Menu_To_Store) ||
            m_navigator.isCurrentNavigation(CubeFaceNavigationEnum.Menu_To_Easy1)) {
                p = m_pMenuCubeStore;
                Graphics.addCubeSize(p.pos.x, p.pos.y, p.pos.z, HALF_CUBE_SIZE, color);

                p = m_pStoreCubeNoAds;
                Graphics.addCubeSize(p.pos.x, p.pos.y, p.pos.z, HALF_CUBE_SIZE, color);

                p = m_pStoreCubeSolvers;
                Graphics.addCubeSize(p.pos.x, p.pos.y, p.pos.z, HALF_CUBE_SIZE, color);

                p = m_pStoreCubeRestore;
                Graphics.addCubeSize(p.pos.x, p.pos.y, p.pos.z, HALF_CUBE_SIZE, color);
            }

            Graphics.renderTriangles();
        }

    public void drawLevelCubes() {
        Graphics.prepare();
        Graphics.setStreamSource();

        int size;
        LevelCube levelCube;
        for (int i = 0; i < 6; ++i) {
            size = Game.ar_cubefacedata[i].lst_level_cubes.size();
            for (int i = 0; i < size; ++i) {
                levelCube = Game.ar_cubefacedata[i].lst_level_cubes.get(i);
                Graphics.addCube(levelCube.pos.x, levelCube.pos.y, levelCube.pos.z);
            }
        }
        Graphics.renderTriangles();
    }

    public void drawLevelCubesLocked() {
        Graphics.prepare();
        Graphics.setStreamSource();

        int size;
        LevelCube levelCube;
        for (int i = 0; i < 6; ++i) {
            size = Game.ar_cubefacedata[i].lst_level_cubes_locked.size();
            for (int i = 0; i < size; ++i) {
                levelCube = Game.ar_cubefacedata[i].lst_level_cubes_locked.get(i);
                Graphics.addCube(levelCube.pos.x, levelCube.pos.y, levelCube.pos.z);
            }
        }
        Graphics.renderTriangles();
    }

    public void drawLevelCubeDecalsEasy(ArrayList<LevelCube> lst_level_cubes_x_plus,
                                        ArrayList<LevelCube> lst_level_cubes_x_minus,
                                        ArrayList<LevelCube> lst_level_cubes_y_plus,
                                        ArrayList<LevelCube> lst_level_cubes_y_minus,
                                        LevelCubeDecalTypeEnum decal_type) {
        LevelCube levelCube;
        TexCoordsQuad coords;
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

            if (p) {
                coords.tx0 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                coords.tx1 = new Vector2(p.tx_up_left.x,  p.tx_up_left.y);
                coords.tx2 = new Vector2(p.tx_lo_left.x,  p.tx_lo_left.y);
                coords.tx3 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);

                Graphics.addCubeFace_X_Plus(levelCube.font_pos.x, levelCube.font_pos.y, levelCube.font_pos.z, coords, color);
            }
        }

        size = lst_level_cubes_x_minus.size();
        for(int i = 0; i < size; ++i) {
            levelCube = lst_level_cubes_x_minus.get(i);

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

            if (p) {
                coords.tx0 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                coords.tx1 = new Vector2(p.tx_up_left.x,  p.tx_up_left.y);
                coords.tx2 = new Vector2(p.tx_lo_left.x,  p.tx_lo_left.y);
                coords.tx3 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);

                Graphics.addCubeFace_X_Minus(levelCube.font_pos.x, levelCube.font_pos.y, levelCube.font_pos.z, coords, color);
            }
        }

        size = lst_level_cubes_y_plus.size();
        for(int i = 0; i < size; ++i) {
            levelCube = lst_level_cubes_y_plus.get(i);

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

            if (p) {
                coords.tx0 = new Vector2(p.tx_lo_left.x,  p.tx_lo_left.y);
                coords.tx1 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);
                coords.tx2 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                coords.tx3 = new Vector2(p.tx_up_left.x,  p.tx_up_left.y);

                Graphics.addCubeFace_Y_Plus(levelCube.font_pos.x, levelCube.font_pos.y, levelCube.font_pos.z, coords, color);
            }
        }

        size = lst_level_cubes_y_minus.size();
        for(int i = 0; i < size; ++i) {
            levelCube = lst_level_cubes_y_minus.get(i);

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

            if (p) {
                coords.tx0 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                coords.tx1 = new Vector2(p.tx_up_left.x,  p.tx_up_left.y);
                coords.tx2 = new Vector2(p.tx_lo_left.x,  p.tx_lo_left.y);
                coords.tx3 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);

                Graphics.addCubeFace_Y_Minus(levelCube.font_pos.x, levelCube.font_pos.y, levelCube.font_pos.z, coords, color);
            }
        }
    }

    public void drawLevelCubeDecalsNormal(ArrayList<LevelCube> lst_level_cubes_z_plus,
                                          ArrayList<LevelCube> lst_level_cubes_z_minus,
                                          ArrayList<LevelCube> lst_level_cubes_y_plus,
                                          ArrayList<LevelCube> lst_level_cubes_y_minus,
                                          LevelCubeDecalTypeEnum decal_type) {
        LevelCube levelCube;
        TexCoordsQuad coords;
        TexturedQuad p;
        Color color = new Color();
        int size;

        size = lst_level_cubes_z_plus.size();
        for(int i = 0; i < size; ++i) {
            levelCube = lst_level_cubes_z_plus.get(i);

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

            if (p) {
                coords.tx0 = new Vector2(p.tx_lo_left.x,  p.tx_lo_left.y);
                coords.tx1 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);
                coords.tx2 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                coords.tx3 = new Vector2(p.tx_up_left.x,  p.tx_up_left.y);

                Graphics.addCubeFace_Z_Plus(levelCube.font_pos.x, levelCube.font_pos.y, levelCube.font_pos.z, coords, color);
            }
        }

        size = lst_level_cubes_z_minus.size();
        for(int i = 0; i < size; ++i) {
            levelCube = lst_level_cubes_z_minus.get(i);

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

            if (p) {
                coords.tx0 = new Vector2(p.tx_up_left.x,  p.tx_up_left.y);
                coords.tx1 = new Vector2(p.tx_lo_left.x,  p.tx_lo_left.y);
                coords.tx2 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);
                coords.tx3 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);

                Graphics.addCubeFace_Z_Minus(levelCube.font_pos.x, levelCube.font_pos.y, levelCube.font_pos.z, coords, color);
            }
        }

        size = lst_level_cubes_y_plus.size();
        for(int i = 0; i < size; ++i) {
            levelCube = lst_level_cubes_y_plus.get(i);

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

            if (p) {
                coords.tx0 = new Vector2(p.tx_up_left.x,  p.tx_up_left.y);
                coords.tx1 = new Vector2(p.tx_lo_left.x,  p.tx_lo_left.y);
                coords.tx2 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);
                coords.tx3 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);

                Graphics.addCubeFace_Y_Plus(levelCube.font_pos.x, levelCube.font_pos.y, levelCube.font_pos.z, coords, color);
            }
        }

        size = lst_level_cubes_y_minus.size();
        for(int i = 0; i < size; ++i) {
            levelCube = lst_level_cubes_y_minus.get(i);

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

            if (p) {
                coords.tx0 = new Vector2(p.tx_up_left.x,  p.tx_up_left.y);
                coords.tx1 = new Vector2(p.tx_lo_left.x,  p.tx_lo_left.y);
                coords.tx2 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);
                coords.tx3 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                
                Graphics.addCubeFace_Y_Minus(levelCube.font_pos.x, levelCube.font_pos.y, levelCube.font_pos.z, coords, color);
            }
        }
    }

    public void drawLevelCubeDecalsHard(ArrayList<LevelCube> lst_level_cubes_x_plus,
                                        ArrayList<LevelCube> lst_level_cubes_x_minus,
                                        ArrayList<LevelCube> lst_level_cubes_y_plus,
                                        ArrayList<LevelCube> lst_level_cubes_y_minus,
                                        LevelCubeDecalTypeEnum decal_type) {
        LevelCube levelCube;
        TexCoordsQuad coords;
        TexturedQuad p;
        Color color = new Color();
        int size;

        size = lst_level_cubes_x_plus.size();
        for(int i = 0; i < size; ++i) {
            levelCube = lst_level_cubes_x_plus.get(i);

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

            if (p) {
                coords.tx0 = new Vector2(p.tx_lo_left.x,  p.tx_lo_left.y);
                coords.tx1 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);
                coords.tx2 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                coords.tx3 = new Vector2(p.tx_up_left.x,  p.tx_up_left.y);

                Graphics.addCubeFace_X_Plus(levelCube.font_pos.x, levelCube.font_pos.y, levelCube.font_pos.z, coords, color);
            }
        }

        size = lst_level_cubes_x_minus.size();
        for(int i = 0; i < size; ++i) {
            levelCube = lst_level_cubes_x_minus.get(i);

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

            if (p) {
                coords.tx0 = new Vector2(p.tx_lo_left.x,  p.tx_lo_left.y);
                coords.tx1 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);
                coords.tx2 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                coords.tx3 = new Vector2(p.tx_up_left.x,  p.tx_up_left.y);

                Graphics.addCubeFace_X_Minus(levelCube.font_pos.x, levelCube.font_pos.y, levelCube.font_pos.z, coords, color);
            }
        }

        size = lst_level_cubes_y_plus.size();
        for(int i = 0; i < size; ++i) {
            levelCube = lst_level_cubes_y_plus.get(i);

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

            if (p) {
                coords.tx0 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                coords.tx1 = new Vector2(p.tx_up_left.x,  p.tx_up_left.y);
                coords.tx2 = new Vector2(p.tx_lo_left.x,  p.tx_lo_left.y);
                coords.tx3 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);

                Graphics.addCubeFace_Y_Plus(levelCube.font_pos.x, levelCube.font_pos.y, levelCube.font_pos.z, coords, color);
            }
        }

        size = lst_level_cubes_y_minus.size();
        for(int i = 0; i < size; ++i) {
            levelCube = lst_level_cubes_y_minus.get(i);

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

            if (p) {
                coords.tx0 = new Vector2(p.tx_lo_left.x,  p.tx_lo_left.y);
                coords.tx1 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);
                coords.tx2 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                coords.tx3 = new Vector2(p.tx_up_left.x,  p.tx_up_left.y);

                Graphics.addCubeFace_Y_Minus(levelCube.font_pos.x, levelCube.font_pos.y, levelCube.font_pos.z, coords, color);
            }
        }
    }

    public void drawLevelNumbersOnLocked() {
        Graphics.prepare();
        Graphics.setStreamSourceFloatAndColor();

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

        Graphics.renderTriangles();
    }


    public void drawLevelNumbers() {
        Graphics.prepare();
        Graphics.setStreamSourceFloatAndColor();

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

        Graphics.renderTriangles();
    }

    public void drawTexts(Color color) {
        Graphics.prepare();

        if ( Math.abs(m_navigator.m_cube_rotation_secondary.degree) < EPSILON) {
            drawTextsDefaultOrientation(m_lst_texts[Face_X_Plus],
                                        m_lst_texts[Face_X_Minus],
                                        m_lst_texts[Face_Y_Plus],
                                        m_lst_texts[Face_Y_Minus],
                                        m_lst_texts[Face_Z_Plus],
                                        m_lst_texts[Face_Z_Minus], color);
        } else {
            switch (m_current_cube_face) {
                case Face_Easy01:
                case Face_Easy02:
                case Face_Easy03:
                case Face_Easy04:
                    drawEasyTitles(m_lst_texts[Face_X_Plus], m_lst_texts[Face_X_Minus],
                                   m_lst_texts[Face_Y_Plus], m_lst_texts[Face_Y_Minus],
                                   m_lst_texts[Face_Z_Plus], m_lst_texts[Face_Z_Minus],
                                   color);
                    break;

                case Face_Normal01:
                case Face_Normal02:
                case Face_Normal03:
                case Face_Normal04:
                    drawNormalTitles(m_lst_texts[Face_X_Plus], m_lst_texts[Face_X_Minus],
                                     m_lst_texts[Face_Y_Plus], m_lst_texts[Face_Y_Minus],
                                     m_lst_texts[Face_Z_Plus], m_lst_texts[Face_Z_Minus],
                                     color);
                    break;

                case Face_Hard01:
                case Face_Hard02:
                case Face_Hard03:
                case Face_Hard04:
                    drawHardTitles(m_lst_texts[Face_X_Plus], m_lst_texts[Face_X_Minus],
                                   m_lst_texts[Face_Y_Plus], m_lst_texts[Face_Y_Minus],
                                   m_lst_texts[Face_Z_Plus],  m_lst_texts[Face_Z_Minus],
                                   color);
                    break;

                default:
                    break;
            }
        }

        CubeFont pCubeFont;
        TexturedQuad* pFont;
        TexCoordsQuad coords;

        // Draw CubeFonts on Red Cubes [P]lay [O]ptions [S]tore [U]nlock [R]estore
        if (m_navigator.IsCurrentNavigation(Menu_To_Easy1) || m_current_cube_face == Face_Menu || m_current_cube_face == Face_Options || m_current_cube_face == Face_Store)
        {
        Color colr = color;

        if (m_pMenuCubePlay.IsDone() && m_pMenuCubePlay.m_cube_pos.x == 1)
        {
        pCubeFont = m_cubefont_play;
        pFont = pCubeFont.GetFont();

        if (pFont)
        {
        coords.tx0 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);
        coords.tx1 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);
        coords.tx2 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
        coords.tx3 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);

        Graphics.AddCubeFace_Z_Plus(pCubeFont.pos.x, pCubeFont.pos.y, pCubeFont.pos.z, coords, colr);
        }
        }

        if (m_pMenuCubeOptions.IsDone() && m_pMenuCubeOptions.m_cube_pos.x == 1)
        {
        pCubeFont = m_cubefont_options;
        pFont = pCubeFont.GetFont();

        if (pFont)
        {
        coords.tx0 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);
        coords.tx1 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);
        coords.tx2 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
        coords.tx3 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);

        Graphics.AddCubeFace_Z_Plus(pCubeFont.pos.x, pCubeFont.pos.y, pCubeFont.pos.z, coords, colr);
        }
        }

        if (m_pMenuCubeStore.IsDone() && m_pMenuCubeStore.m_cube_pos.x == 1)
        {
        pCubeFont = m_cubefont_store;
        pFont = pCubeFont.GetFont();

        if (pFont)
        {
        coords.tx0 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);
        coords.tx1 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);
        coords.tx2 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
        coords.tx3 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);

        Graphics.AddCubeFace_Z_Plus(pCubeFont.pos.x, pCubeFont.pos.y, pCubeFont.pos.z, coords, colr);
        }
        }

        if (m_pStoreCubeNoAds.IsDone() && m_pStoreCubeNoAds.m_cube_pos.x == 1)
        {
        pCubeFont = m_cubefont_noads;
        pFont = pCubeFont.GetFont();

        if (pFont)
        {
        coords.tx0 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
        coords.tx1 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);
        coords.tx2 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);
        coords.tx3 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);

        Graphics.AddCubeFace_Y_Minus(pCubeFont.pos.x, pCubeFont.pos.y, pCubeFont.pos.z, coords, colr);
        }
        }

        if (m_pStoreCubeSolvers.IsDone() && m_pStoreCubeSolvers.m_cube_pos.x == 1)
        {
        pCubeFont = m_cubefont_solvers;
        pFont = pCubeFont.GetFont();

        if (pFont)
        {
        coords.tx0 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
        coords.tx1 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);
        coords.tx2 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);
        coords.tx3 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);

        Graphics.AddCubeFace_Y_Minus(pCubeFont.pos.x, pCubeFont.pos.y, pCubeFont.pos.z, coords, colr);
        }
        }

        if (m_pStoreCubeRestore.IsDone() && m_pStoreCubeRestore.m_cube_pos.x == 1)
        {
        pCubeFont = m_cubefont_restore;
        pFont = pCubeFont.GetFont();

        if (pFont)
        {
        coords.tx0 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
        coords.tx1 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);
        coords.tx2 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);
        coords.tx3 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);

        Graphics.AddCubeFace_Y_Minus(pCubeFont.pos.x, pCubeFont.pos.y, pCubeFont.pos.z, coords, colr);
        }
        }
        }

        Graphics.RenderTriangles();
        }

        public void DrawTextsTitles(Color color)
        {
        Graphics.Prepare();

        if (abs(m_navigator.m_cube_rotation_secondary.degree) < EPSILON)
        DrawTextsDefaultOrientation(m_lst_titles[Face_X_Plus],  m_lst_titles[Face_X_Minus], m_lst_titles[Face_Y_Plus],
        m_lst_titles[Face_Y_Minus], m_lst_titles[Face_Z_Plus],  m_lst_titles[Face_Z_Minus], color);
        else
        {
        switch (m_current_cube_face)
        {
        case Face_Easy01:
        case Face_Easy02:
        case Face_Easy03:
        case Face_Easy04:
        DrawEasyTitles(m_lst_titles[Face_X_Plus],  m_lst_titles[Face_X_Minus], m_lst_titles[Face_Y_Plus],
        m_lst_titles[Face_Y_Minus], m_lst_titles[Face_Z_Plus],  m_lst_titles[Face_Z_Minus], color);
        break;

        case Face_Normal01:
        case Face_Normal02:
        case Face_Normal03:
        case Face_Normal04:
        DrawNormalTitles(m_lst_titles[Face_X_Plus],  m_lst_titles[Face_X_Minus], m_lst_titles[Face_Y_Plus],
        m_lst_titles[Face_Y_Minus], m_lst_titles[Face_Z_Plus],  m_lst_titles[Face_Z_Minus], color);
        break;

        case Face_Hard01:
        case Face_Hard02:
        case Face_Hard03:
        case Face_Hard04:
        DrawHardTitles(m_lst_titles[Face_X_Plus],  m_lst_titles[Face_X_Minus], m_lst_titles[Face_Y_Plus],
        m_lst_titles[Face_Y_Minus], m_lst_titles[Face_Z_Plus],  m_lst_titles[Face_Z_Minus], color);
        break;

default:
        break;
        }
        }

        Graphics.RenderTriangles();
        }

    public void drawTextsDefaultOrientation(ArrayList<CubeFont> lst_x_plus,
                                            ArrayList<CubeFont> lst_x_minus,
                                            ArrayList<CubeFont> lst_y_plus,
                                            ArrayList<CubeFont> lst_y_minus,
                                            ArrayList<CubeFont> lst_z_plus,
                                            ArrayList<CubeFont> lst_z_minus,
                                            Color color) {
        CubeFont cubeFont;
        TexturedQuad  font;
        TexCoordsQuad coords;
        int size;

        size = lst_z_plus.size();
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_z_plus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx1 = new Vector2(font.tx_up_left.x,  font.tx_up_left.y);
            coords.tx2 = new Vector2(font.tx_lo_left.x,  font.tx_lo_left.y);
            coords.tx3 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);

            Graphics.addCubeFace_Z_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
        }

        size = lst_x_plus.size();
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_x_plus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx1 = new Vector2(font.tx_up_left.x,  font.tx_up_left.y);
            coords.tx2 = new Vector2(font.tx_lo_left.x,  font.tx_lo_left.y);
            coords.tx3 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);

            Graphics.addCubeFace_X_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
        }

        size = lst_z_minus.size();
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_z_minus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_up_left.x,  font.tx_up_left.y);
            coords.tx1 = new Vector2(font.tx_lo_left.x,  font.tx_lo_left.y);
            coords.tx2 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx3 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);

            Graphics.addCubeFace_Z_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
        }

        size = lst_x_minus.size();
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_x_minus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_lo_left.x,  font.tx_lo_left.y);
            coords.tx1 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx2 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx3 = new Vector2(font.tx_up_left.x,  font.tx_up_left.y);

            Graphics.addCubeFace_X_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
        }

        size = lst_y_plus.size();
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_y_plus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx1 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx2 = new Vector2(font.tx_up_left.x,  font.tx_up_left.y);
            coords.tx3 = new Vector2(font.tx_lo_left.x,  font.tx_lo_left.y);

            Graphics.addCubeFace_Y_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
        }

        size = lst_y_minus.size();
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_y_minus.get(i);
            font = cubeFont.getFont();

            coords.tx0 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
            coords.tx1 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
            coords.tx2 = new Vector2(font.tx_up_left.x,  font.tx_up_left.y);
            coords.tx3 = new Vector2(font.tx_lo_left.x,  font.tx_lo_left.y);

            Graphics.addCubeFace_Y_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
        }
    }

        public void DrawEasyTitles(ArrayList<CubeFont> lst_x_plus,
        ArrayList<CubeFont> lst_x_minus,
        ArrayList<CubeFont> lst_y_plus,
        ArrayList<CubeFont> lst_y_minus,
        ArrayList<CubeFont> lst_z_plus,
        ArrayList<CubeFont> lst_z_minus, Color color)
        {
        ArrayList<CubeFont>::iterator it;
        CubeFaceTypesEnum face_type;
        CubeFont pCubeFont;
        TexturedQuad* pFont;
        TexCoordsQuad coords;

        face_type = Face_X_Plus;
        for (it = lst_x_plus.begin(); it != lst_x_plus.end(); ++it)
        {
        pCubeFont = *it;
        pFont = pCubeFont.GetFont();

        coords.tx0 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);
        coords.tx1 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);
        coords.tx2 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
        coords.tx3 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);

        Graphics.AddCubeFace_X_Plus(pCubeFont.pos.x, pCubeFont.pos.y, pCubeFont.pos.z, coords, color);
        }

        face_type = Face_Y_Minus;
        for (it = lst_y_minus.begin(); it != lst_y_minus.end(); ++it)
        {
        pCubeFont = *it;
        pFont = pCubeFont.GetFont();

        coords.tx0 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);
        coords.tx1 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);
        coords.tx2 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
        coords.tx3 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);

        Graphics.AddCubeFace_Y_Minus(pCubeFont.pos.x, pCubeFont.pos.y, pCubeFont.pos.z, coords, color);
        }

        face_type = Face_X_Minus;
        for (it = lst_x_minus.begin(); it != lst_x_minus.end(); ++it)
        {
        pCubeFont = *it;
        pFont = pCubeFont.GetFont();

        coords.tx0 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);
        coords.tx1 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);
        coords.tx2 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
        coords.tx3 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);

        Graphics.AddCubeFace_X_Minus(pCubeFont.pos.x, pCubeFont.pos.y, pCubeFont.pos.z, coords, color);
        }

        face_type = Face_Y_Plus;
        for (it = lst_y_plus.begin(); it != lst_y_plus.end(); ++it)
        {
        pCubeFont = *it;
        pFont = pCubeFont.GetFont();

        coords.tx0 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
        coords.tx1 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
        coords.tx2 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);
        coords.tx3 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);

        Graphics.AddCubeFace_Y_Plus(pCubeFont.pos.x, pCubeFont.pos.y, pCubeFont.pos.z, coords, color);
        }
        }

        public void DrawNormalTitles(ArrayList<CubeFont> lst_x_plus,
        ArrayList<CubeFont> lst_x_minus,
        ArrayList<CubeFont> lst_y_plus,
        ArrayList<CubeFont> lst_y_minus,
        ArrayList<CubeFont> lst_z_plus,
        ArrayList<CubeFont> lst_z_minus, Color color)
        {
        ArrayList<CubeFont>::iterator it;
        CubeFaceTypesEnum face_type;
        CubeFont pCubeFont;
        TexturedQuad* pFont;
        TexCoordsQuad coords;

        face_type = Face_Z_Minus;
        for (it = lst_z_minus.begin(); it != lst_z_minus.end(); ++it)
        {
        pCubeFont = *it;
        pFont = pCubeFont.GetFont();

        coords.tx0 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);
        coords.tx1 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
        coords.tx2 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
        coords.tx3 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);

        Graphics.AddCubeFace_Z_Minus(pCubeFont.pos.x, pCubeFont.pos.y, pCubeFont.pos.z, coords, color);
        }

        face_type = Face_Y_Minus;
        for (it = lst_y_minus.begin(); it != lst_y_minus.end(); ++it)
        {
        pCubeFont = *it;
        pFont = pCubeFont.GetFont();

        coords.tx0 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);
        coords.tx1 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
        coords.tx2 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
        coords.tx3 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);

        Graphics.AddCubeFace_Y_Minus(pCubeFont.pos.x, pCubeFont.pos.y, pCubeFont.pos.z, coords, color);
        }

        face_type = Face_Z_Plus;
        for (it = lst_z_plus.begin(); it != lst_z_plus.end(); ++it)
        {
        pCubeFont = *it;
        pFont = pCubeFont.GetFont();

        coords.tx0 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
        coords.tx1 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
        coords.tx2 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);
        coords.tx3 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);

        Graphics.AddCubeFace_Z_Plus(pCubeFont.pos.x, pCubeFont.pos.y, pCubeFont.pos.z, coords, color);
        }

        face_type = Face_Y_Plus;
        for (it = lst_y_plus.begin(); it != lst_y_plus.end(); ++it)
        {
        pCubeFont = *it;
        pFont = pCubeFont.GetFont();

        coords.tx0 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);
        coords.tx1 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
        coords.tx2 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
        coords.tx3 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);

        Graphics.AddCubeFace_Y_Plus(pCubeFont.pos.x, pCubeFont.pos.y, pCubeFont.pos.z, coords, color);
        }
        }

        public void DrawHardTitles(ArrayList<CubeFont> lst_x_plus,
        ArrayList<CubeFont> lst_x_minus,
        ArrayList<CubeFont> lst_y_plus,
        ArrayList<CubeFont> lst_y_minus,
        ArrayList<CubeFont> lst_z_plus,
        ArrayList<CubeFont> lst_z_minus, Color color)
        {
        ArrayList<CubeFont>::iterator it;
        CubeFaceTypesEnum face_type;
        CubeFont pCubeFont;
        TexturedQuad* pFont;
        TexCoordsQuad coords;

        face_type = Face_X_Minus;
        for (it = lst_x_minus.begin(); it != lst_x_minus.end(); ++it)
        {
        pCubeFont = *it;
        pFont = pCubeFont.GetFont();

        coords.tx0 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
        coords.tx1 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
        coords.tx2 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);
        coords.tx3 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);

        Graphics.AddCubeFace_X_Minus(pCubeFont.pos.x, pCubeFont.pos.y, pCubeFont.pos.z, coords, color);
        }

        face_type = Face_Y_Minus;
        for (it = lst_y_minus.begin(); it != lst_y_minus.end(); ++it)
        {
        pCubeFont = *it;
        pFont = pCubeFont.GetFont();

        coords.tx0 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
        coords.tx1 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
        coords.tx2 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);
        coords.tx3 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);

        Graphics.AddCubeFace_Y_Minus(pCubeFont.pos.x, pCubeFont.pos.y, pCubeFont.pos.z, coords, color);
        }

        face_type = Face_X_Plus;
        for (it = lst_x_plus.begin(); it != lst_x_plus.end(); ++it)
        {
        pCubeFont = *it;
        pFont = pCubeFont.GetFont();

        coords.tx0 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
        coords.tx1 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
        coords.tx2 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);
        coords.tx3 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);

        Graphics.AddCubeFace_X_Plus(pCubeFont.pos.x, pCubeFont.pos.y, pCubeFont.pos.z, coords, color);
        }

        face_type = Face_Y_Plus;
        for (it = lst_y_plus.begin(); it != lst_y_plus.end(); ++it)
        {
        pCubeFont = *it;
        pFont = pCubeFont.GetFont();

        coords.tx0 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);
        coords.tx1 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);
        coords.tx2 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
        coords.tx3 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);

        Graphics.AddCubeFace_Y_Plus(pCubeFont.pos.x, pCubeFont.pos.y, pCubeFont.pos.z, coords, color);
        }
        }

        public void DrawSymbols(Color color)
        {
        Graphics.Prepare();
        Graphics.SetStreamSourceFloatAndColor();

        DrawTextsDefaultOrientation(m_lst_symbols[Face_X_Plus],
        m_lst_symbols[Face_X_Minus],
        m_lst_symbols[Face_Y_Plus],
        m_lst_symbols[Face_Y_Minus],
        m_lst_symbols[Face_Z_Plus],
        m_lst_symbols[Face_Z_Minus], color);

        Graphics.RenderTriangles();
        }

        public void DrawCubeFaceOptions()
        {
        cCube* p = &engine.cubes[2][7][3];

        float x = p.tx - HALF_CUBE_SIZE;
        float y = p.ty + HALF_CUBE_SIZE + 0.01f;
        float z = p.tz - HALF_CUBE_SIZE;
        float w = CUBE_SIZE * 5.0f * engine.GetMusicVolume();
        float ws = CUBE_SIZE * 5.0f * engine.GetSoundVolume();
        float zs = engine.cubes[2][7][6].tz - HALF_CUBE_SIZE;

        const GLfloat verts[] =
        {
        // music
        x,      y,          z + CUBE_SIZE,
        x + w,  y,          z + CUBE_SIZE,
        x + w,  y,          z,
        x,      y,          z,

        // sound
        x,      y,          zs + CUBE_SIZE,
        x + ws, y,          zs + CUBE_SIZE,
        x + ws, y,          zs,
        x,      y,          zs
        };

        const GLfloat norms[] =
        {
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,

        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f
        };

        const GLubyte colors[] =
        {
        76, 0, 0, 140,
        76, 0, 0, 220,
        76, 0, 0, 220,
        76, 0, 0, 140,

        76, 0, 0, 140,
        76, 0, 0, 220,
        76, 0, 0, 220,
        76, 0, 0, 140,
        };


        glVertexPointer(3, GL_FLOAT, 0, verts);
        glNormalPointer(GL_FLOAT, 0, norms);
        glColorPointer(4, GL_UNSIGNED_BYTE, 0, colors);

        glEnableClientState(GL_NORMAL_ARRAY);

        glDisable(GL_TEXTURE_2D);

        glPushMatrix();
        glTranslatef(engine.cube_offset.x, engine.cube_offset.y, engine.cube_offset.z);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4); // progress bar music
        glDrawArrays(GL_TRIANGLE_FAN, 4, 4); // progress bar soundfx
        glPopMatrix();

        glEnable(GL_TEXTURE_2D);
        }

        public void DrawLevelCubeSymbols()
        {
        Graphics.Prepare();
        Graphics.SetStreamSourceFloat();

        switch (m_current_cube_face)
        {
        case Face_Easy01:
        case Face_Easy02:
        case Face_Easy03:
        case Face_Easy04:

        DrawLevelCubeDecalsEasy(engine.ar_cubefacedata[Face_X_Plus].lst_level_cubes,
        engine.ar_cubefacedata[Face_X_Minus].lst_level_cubes,
        engine.ar_cubefacedata[Face_Y_Plus].lst_level_cubes,
        engine.ar_cubefacedata[Face_Y_Minus].lst_level_cubes,
        LevelCubeDecalStars);

        DrawLevelCubeDecalsEasy(engine.ar_cubefacedata[Face_X_Plus].lst_level_cubes,
        engine.ar_cubefacedata[Face_X_Minus].lst_level_cubes,
        engine.ar_cubefacedata[Face_Y_Plus].lst_level_cubes,
        engine.ar_cubefacedata[Face_Y_Minus].lst_level_cubes,
        LevelCubeDecalSolver);

        break;

        case Face_Normal01:
        case Face_Normal02:
        case Face_Normal03:
        case Face_Normal04:
        DrawLevelCubeDecalsNormal(engine.ar_cubefacedata[Face_Z_Plus].lst_level_cubes,
        engine.ar_cubefacedata[Face_Z_Minus].lst_level_cubes,
        engine.ar_cubefacedata[Face_Y_Plus].lst_level_cubes,
        engine.ar_cubefacedata[Face_Y_Minus].lst_level_cubes,
        LevelCubeDecalStars);

        DrawLevelCubeDecalsNormal(engine.ar_cubefacedata[Face_Z_Plus].lst_level_cubes,
        engine.ar_cubefacedata[Face_Z_Minus].lst_level_cubes,
        engine.ar_cubefacedata[Face_Y_Plus].lst_level_cubes,
        engine.ar_cubefacedata[Face_Y_Minus].lst_level_cubes,
        LevelCubeDecalSolver);
        break;

        case Face_Hard01:
        case Face_Hard02:
        case Face_Hard03:
        case Face_Hard04:
        DrawLevelCubeDecalsHard(engine.ar_cubefacedata[Face_X_Plus].lst_level_cubes,
        engine.ar_cubefacedata[Face_X_Minus].lst_level_cubes,
        engine.ar_cubefacedata[Face_Y_Plus].lst_level_cubes,
        engine.ar_cubefacedata[Face_Y_Minus].lst_level_cubes,
        LevelCubeDecalStars);

        DrawLevelCubeDecalsHard(engine.ar_cubefacedata[Face_X_Plus].lst_level_cubes,
        engine.ar_cubefacedata[Face_X_Minus].lst_level_cubes,
        engine.ar_cubefacedata[Face_Y_Plus].lst_level_cubes,
        engine.ar_cubefacedata[Face_Y_Minus].lst_level_cubes,
        LevelCubeDecalSolver);
        break;

default:
        break;
        }

        Graphics.RenderTriangles();
        }

        public void DrawCredits()
        {
        const GLfloat vertices[] =
        {
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f,
        0.5f,  0.5f, 0.0f,
        -0.5f,  0.5f, 0.0f
        };

        const GLshort coords[] =
        {
        0,   1,
        1,   1,
        1,   0,
        0,   0
        };

        const GLfloat norms[] =
        {
        0.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f
        };

        const GLubyte colors_white[] =
        {
        255, 255, 255, 255,
        255, 255, 255, 255,
        255, 255, 255, 255,
        255, 255, 255, 255,
        };

        const GLubyte colors_black[] =
        {
        0, 0, 0, 255,
        0, 0, 0, 255,
        0, 0, 0, 255,
        0, 0, 0, 255,
        };

        glVertexPointer(3, GL_FLOAT, 0, vertices);
        glTexCoordPointer(2, GL_SHORT, 0, coords);
        glNormalPointer(GL_FLOAT, 0, norms);
        glColorPointer(4, GL_UNSIGNED_BYTE, 0, colors_white);

        glPushMatrix();
        glTranslatef(0.0f, -5.7f, m_credits_offset);
        glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
        glScalef(11.0, 11.0, 1.0);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
        glPopMatrix();

        glColorPointer(4, GL_UNSIGNED_BYTE, 0, colors_black);

        glPushMatrix();
        glTranslatef(0.0f, -5.6f, -10.0f);
        glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
        glScalef(11.0, 11.0, 1.0);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
        glPopMatrix();
        }

        public void DrawTheCube()
        {
        Graphics.Prepare();
        Graphics.SetStreamSource();

        ArrayList<cCube*>::iterator it;
        for (it = m_list_cubes_base.begin(); it != m_list_cubes_base.end(); ++it)
        Graphics.AddCubeWithColor((*it).tx, (*it).ty, (*it).tz, (*it).color_current);

        Graphics.RenderTriangles(engine.cube_offset.x, engine.cube_offset.y, engine.cube_offset.z);

        Graphics.Prepare();

        for (it = m_list_cubes_face.begin(); it != m_list_cubes_face.end(); ++it)
        Graphics.AddCubeWithColor((*it).tx, (*it).ty, (*it).tz, (*it).color_current);

        Graphics.RenderTriangles(engine.cube_offset.x, engine.cube_offset.y, engine.cube_offset.z);
        }

        public void DrawCubeHiLite(Color color)
        {
        TexCoordsQuad coords;

        TexturedQuad* p = m_font_hilite.GetFont();
        coords.tx0 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
        coords.tx1 = new Vector2(p.tx_up_left.x,  p.tx_up_left.y);
        coords.tx2 = new Vector2(p.tx_lo_left.x,  p.tx_lo_left.y);
        coords.tx3 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);

        Graphics.Prepare();
        Graphics.SetStreamSourceFloatAndColor();

        switch (m_current_cube_face_type)
        {
        case Face_X_Plus:
        Graphics.AddCubeFace_X_Plus(m_font_hilite.pos.x, m_font_hilite.pos.y, m_font_hilite.pos.z, coords, color);
        break;

        case Face_X_Minus:
        Graphics.AddCubeFace_X_Minus(m_font_hilite.pos.x, m_font_hilite.pos.y, m_font_hilite.pos.z, coords, color);
        break;

        case Face_Y_Plus:
        Graphics.AddCubeFace_Y_Plus(m_font_hilite.pos.x, m_font_hilite.pos.y, m_font_hilite.pos.z, coords, color);
        break;

        case Face_Y_Minus:
        Graphics.AddCubeFace_Y_Minus(m_font_hilite.pos.x, m_font_hilite.pos.y, m_font_hilite.pos.z, coords, color);
        break;

        case Face_Z_Plus:
        Graphics.AddCubeFace_Z_Plus(m_font_hilite.pos.x, m_font_hilite.pos.y, m_font_hilite.pos.z, coords, color);
        break;

        case Face_Z_Minus:
        Graphics.AddCubeFace_Z_Minus(m_font_hilite.pos.x, m_font_hilite.pos.y, m_font_hilite.pos.z, coords, color);
        break;

default:
        break;
        }

        Graphics.RenderTriangles();
        }

        #pragma mark - UpdateAnim

        public void UpdateAnimToCredits(float dt)
        {
        if (m_t >= 1.0f)
        {
        m_state = InCredits;
        return;
        }

        m_t += 0.05f;
        if (m_t > 1.0f)
        m_t = 1.0f;

        cUtils::LerpCamera(m_camera_menu, m_camera_credits, m_t, m_camera_current);
        engine.dirty_alpha = LERP(DIRTY_ALPHA, 0, m_t);

        m_pMenuCubeStore.Update(dt);
        }

        public void UpdateAnimFromCredits(float dt)
        {
        if (m_t >= 1.0f)
        {
        m_state = InMenu;
        return;
        }

        m_t += 0.05f;
        if (m_t > 1.0f)
        m_t = 1.0f;

        cUtils::LerpCamera(m_camera_credits, m_camera_menu, m_t, m_camera_current);
        engine.dirty_alpha = LERP(0, DIRTY_ALPHA, m_t);
        }

        public void UpdatePlayCube(float dt)
        {
        if (m_pMenuCubePlay.IsDone())
        {
        switch (m_current_cube_face)
        {
        case Face_Tutorial:

        if (m_pMenuCubePlay.m_cube_pos.z == 8)
        {
        m_current_cube_face = Face_Menu;
        SetCurrentCubeFaceType(Face_Z_Plus);
        m_pMenuCubePlay.MoveOnAxis(X_Plus);
        m_navigator.Setup(Tutorial_To_Menu);
        }

        if (m_pMenuCubePlay.m_cube_pos.y == 7 && m_can_alter_text)
        {
        m_can_alter_text = false;
        cMenuFaceBuilder::ResetTransforms();
        cMenuFaceBuilder::AddTransform(MirrorVert);
        ReleaseCubeTextsOnFace(Face_X_Minus);
        cMenuFaceBuilder::BuildTexts(Face_Tutorial, Face_X_Minus, true);
        }

        if (m_pMenuCubePlay.m_cube_pos.y == 1 && m_can_alter_text)
        {
        m_can_alter_text = false;
        cMenuFaceBuilder::ResetTransforms();
        cMenuFaceBuilder::AddTransform(MirrorVert);
        ReleaseCubeTextsOnFace(Face_X_Minus);
        cMenuFaceBuilder::BuildTexts(Face_Tutorial, Face_X_Minus);
        }

        break;

        case Face_Menu:

        if (m_pMenuCubePlay.m_cube_pos.x == 2 && m_pMenuCubePlay.m_cube_pos.y == 7 && m_pMenuCubePlay.m_cube_pos.z == 8)
        {
        m_pMenuCubePlay.MoveOnAxis(Y_Minus);
        }

        if (m_pMenuCubePlay.m_cube_pos.x == 2 && m_pMenuCubePlay.m_cube_pos.y == 5 && m_pMenuCubePlay.m_cube_pos.z == 8)
        {
        m_pMenuCubePlay.MoveOnAxis(X_Minus);
        }

        if (m_pMenuCubePlay.m_cube_pos.x == 8)
        {
        m_current_cube_face = Face_Easy01;
        SetCurrentCubeFaceType(Face_X_Plus);
        m_pMenuCubePlay.MoveOnAxis(Z_Minus);
        m_navigator.Setup(Menu_To_Easy1);
        }
        break;

        case Face_Easy01:

        if (m_pMenuCubePlay.m_cube_pos.z == 0)
        {
        m_current_cube_face = Face_Normal01;
        SetCurrentCubeFaceType(Face_Z_Minus);
        m_pMenuCubePlay.MoveOnAxis(X_Minus);
        m_navigator.Setup(Easy1_To_Normal1);
        }

        if (m_pMenuCubePlay.m_cube_pos.z == 8)
        {
        m_current_cube_face = Face_Menu;
        SetCurrentCubeFaceType(Face_Z_Plus);
        m_pMenuCubePlay.MoveOnAxis(X_Minus);
        m_navigator.Setup(Easy1_To_Menu);
        }

        if (m_pMenuCubePlay.m_cube_pos.y == 0)
        {
        m_current_cube_face = Face_Easy02;
        SetCurrentCubeFaceType(Face_Y_Minus);
        m_pMenuCubePlay.MoveOnAxis(X_Minus);
        m_navigator.Setup(Easy1_To_Easy2);
        }

        if (m_pMenuCubePlay.m_cube_pos.y == 8)
        {
        m_current_cube_face = Face_Easy04;
        SetCurrentCubeFaceType(Face_Y_Plus);
        m_pMenuCubePlay.MoveOnAxis(X_Minus);
        m_navigator.Setup(Easy1_To_Easy4);
        }

        break;

        case Face_Easy02:

        if (m_pMenuCubePlay.m_cube_pos.x == 8)
        {
        m_current_cube_face = Face_Easy01;
        SetCurrentCubeFaceType(Face_X_Plus);
        m_pMenuCubePlay.MoveOnAxis(Y_Plus);
        m_navigator.Setup(Easy2_To_Easy1);
        }

        if (m_pMenuCubePlay.m_cube_pos.x == 0)
        {
        m_current_cube_face = Face_Easy03;
        SetCurrentCubeFaceType(Face_X_Minus);
        m_pMenuCubePlay.MoveOnAxis(Y_Plus);
        m_navigator.Setup(Easy2_To_Easy3);
        }

        break;

        case Face_Easy03:

        if (m_pMenuCubePlay.m_cube_pos.y == 0)
        {
        m_current_cube_face = Face_Easy02;
        SetCurrentCubeFaceType(Face_Y_Minus);
        m_pMenuCubePlay.MoveOnAxis(X_Plus);
        m_navigator.Setup(Easy3_To_Easy2);
        }

        if (m_pMenuCubePlay.m_cube_pos.y == 8)
        {
        m_current_cube_face = Face_Easy04;
        SetCurrentCubeFaceType(Face_Y_Plus);
        m_pMenuCubePlay.MoveOnAxis(X_Plus);
        m_navigator.Setup(Easy3_To_Easy4);
        }

        break;

        case Face_Easy04:

        if (m_pMenuCubePlay.m_cube_pos.x == 8)
        {
        m_current_cube_face = Face_Easy01;
        SetCurrentCubeFaceType(Face_X_Plus);
        m_pMenuCubePlay.MoveOnAxis(Y_Minus);
        m_navigator.Setup(Easy4_To_Easy1);
        }

        if (m_pMenuCubePlay.m_cube_pos.x == 0)
        {
        m_current_cube_face = Face_Easy03;
        SetCurrentCubeFaceType(Face_X_Minus);
        m_pMenuCubePlay.MoveOnAxis(Y_Minus);
        m_navigator.Setup(Easy4_To_Easy3);
        }

        break;

        case Face_Normal01:

        if (m_pMenuCubePlay.m_cube_pos.x == 8)
        {
        m_current_cube_face = Face_Easy01;
        SetCurrentCubeFaceType(Face_X_Plus);
        m_pMenuCubePlay.MoveOnAxis(Z_Plus);
        m_navigator.Setup(Normal1_To_Easy1);
        }

        if (m_pMenuCubePlay.m_cube_pos.x == 0)
        {
        m_current_cube_face = Face_Hard01;
        SetCurrentCubeFaceType(Face_X_Minus);
        m_pMenuCubePlay.MoveOnAxis(Z_Plus);
        m_navigator.Setup(Normal1_To_Hard1);
        }

        if (m_pMenuCubePlay.m_cube_pos.y == 0)
        {
        m_current_cube_face = Face_Normal02;
        SetCurrentCubeFaceType(Face_Y_Minus);
        m_pMenuCubePlay.MoveOnAxis(Z_Plus);
        m_navigator.Setup(Normal1_To_Normal2);
        }

        if (m_pMenuCubePlay.m_cube_pos.y == 8)
        {
        m_current_cube_face = Face_Normal04;
        SetCurrentCubeFaceType(Face_Y_Plus);
        m_pMenuCubePlay.MoveOnAxis(Z_Plus);
        m_navigator.Setup(Normal1_To_Normal4);
        }

        break;

        case Face_Normal02:

        if (m_pMenuCubePlay.m_cube_pos.z == 0)
        {
        m_current_cube_face = Face_Normal01;
        SetCurrentCubeFaceType(Face_Z_Minus);
        m_pMenuCubePlay.MoveOnAxis(Y_Plus);
        m_navigator.Setup(Normal2_To_Normal1);
        }

        if (m_pMenuCubePlay.m_cube_pos.z == 8)
        {
        m_current_cube_face = Face_Normal03;
        SetCurrentCubeFaceType(Face_Z_Plus);
        m_pMenuCubePlay.MoveOnAxis(Y_Plus);
        m_navigator.Setup(Normal2_To_Normal3);
        }

        break;

        case Face_Normal03:

        if (m_pMenuCubePlay.m_cube_pos.y == 0)
        {
        m_current_cube_face = Face_Normal02;
        SetCurrentCubeFaceType(Face_Y_Minus);
        m_pMenuCubePlay.MoveOnAxis(Z_Minus);
        m_navigator.Setup(Normal3_To_Normal2);
        }

        if (m_pMenuCubePlay.m_cube_pos.y == 8)
        {
        m_current_cube_face = Face_Normal04;
        SetCurrentCubeFaceType(Face_Y_Plus);
        m_pMenuCubePlay.MoveOnAxis(Z_Minus);
        m_navigator.Setup(Normal3_To_Normal4);
        }

        break;

        case Face_Normal04:

        if (m_pMenuCubePlay.m_cube_pos.z == 8)
        {
        m_current_cube_face = Face_Normal03;
        SetCurrentCubeFaceType(Face_Z_Plus);
        m_pMenuCubePlay.MoveOnAxis(Y_Minus);
        m_navigator.Setup(Normal4_To_Normal3);
        }

        if (m_pMenuCubePlay.m_cube_pos.z == 0)
        {
        m_current_cube_face = Face_Normal01;
        SetCurrentCubeFaceType(Face_Z_Minus);
        m_pMenuCubePlay.MoveOnAxis(Y_Minus);
        m_navigator.Setup(Normal4_To_Normal1);
        }

        break;

        case Face_Hard01:

        if (m_pMenuCubePlay.m_cube_pos.z == 0)
        {
        m_current_cube_face = Face_Normal01;
        SetCurrentCubeFaceType(Face_Z_Minus);
        m_pMenuCubePlay.MoveOnAxis(X_Plus);
        m_navigator.Setup(Hard1_To_Normal1);
        }

        if (m_pMenuCubePlay.m_cube_pos.z == 8)
        {
        m_current_cube_face = Face_Menu;
        SetCurrentCubeFaceType(Face_Z_Plus);
        m_pMenuCubePlay.MoveOnAxis(X_Plus);
        m_navigator.Setup(Hard1_To_Menu);
        }

        if (m_pMenuCubePlay.m_cube_pos.y == 0)
        {
        m_current_cube_face = Face_Hard02;
        SetCurrentCubeFaceType(Face_Y_Minus);
        m_pMenuCubePlay.MoveOnAxis(X_Plus);
        m_navigator.Setup(Hard1_To_Hard2);
        }

        if (m_pMenuCubePlay.m_cube_pos.y == 8)
        {
        m_current_cube_face = Face_Hard04;
        SetCurrentCubeFaceType(Face_Y_Plus);
        m_pMenuCubePlay.MoveOnAxis(X_Plus);
        m_navigator.Setup(Hard1_To_Hard4);
        }

        break;

        case Face_Hard02:

        if (m_pMenuCubePlay.m_cube_pos.x == 0)
        {
        m_current_cube_face = Face_Hard01;
        SetCurrentCubeFaceType(Face_X_Minus);
        m_pMenuCubePlay.MoveOnAxis(Y_Plus);
        m_navigator.Setup(Hard2_To_Hard1);
        }

        if (m_pMenuCubePlay.m_cube_pos.x == 8)
        {
        m_current_cube_face = Face_Hard03;
        SetCurrentCubeFaceType(Face_X_Plus);
        m_pMenuCubePlay.MoveOnAxis(Y_Plus);
        m_navigator.Setup(Hard2_To_Hard3);
        }

        break;

        case Face_Hard03:

        if (m_pMenuCubePlay.m_cube_pos.y == 8)
        {
        m_current_cube_face = Face_Hard04;
        SetCurrentCubeFaceType(Face_Y_Plus);
        m_pMenuCubePlay.MoveOnAxis(X_Minus);
        m_navigator.Setup(Hard3_To_Hard4);
        }

        if (m_pMenuCubePlay.m_cube_pos.y == 0)
        {
        m_current_cube_face = Face_Hard02;
        SetCurrentCubeFaceType(Face_Y_Minus);
        m_pMenuCubePlay.MoveOnAxis(X_Minus);
        m_navigator.Setup(Hard3_To_Hard2);
        }

        break;

        case Face_Hard04:

        if (m_pMenuCubePlay.m_cube_pos.x == 8)
        {
        m_current_cube_face = Face_Hard03;
        SetCurrentCubeFaceType(Face_X_Plus);
        m_pMenuCubePlay.MoveOnAxis(Y_Minus);
        m_navigator.Setup(Hard4_To_Hard3);
        }

        if (m_pMenuCubePlay.m_cube_pos.x == 0)
        {
        m_current_cube_face = Face_Hard01;
        SetCurrentCubeFaceType(Face_X_Minus);
        m_pMenuCubePlay.MoveOnAxis(Y_Minus);
        m_navigator.Setup(Hard4_To_Hard1);
        }

        break;

default:
        break;
        } // switch
        }
        else
        {
        m_pMenuCubePlay.Update(dt);
        m_can_alter_text = true;
        }
        }

        public void UpdateOptionsCube(float dt)
        {
        if (m_pMenuCubeOptions.IsDone())
        {
        switch (m_current_cube_face)
        {
        case Face_Menu:

        if (m_pMenuCubeOptions.m_cube_pos.y == 8)
        {
        m_prev_face = Face_Menu;
        m_current_cube_face = Face_Options;
        SetCurrentCubeFaceType(Face_Y_Plus);
        m_pMenuCubeOptions.MoveOnAxis(Z_Minus);

        m_navigator.Setup(Menu_To_Options);
        }

        if (m_pMenuCubeOptions.m_cube_pos.x == 7)
        {
        if (Face_Options == m_prev_face)
        {
        m_prev_face = -1;
        m_pMenuCubeOptions.MoveOnAxis(X_Minus);
        }
        }

        break;

        case Face_Options:

        if (m_pMenuCubeOptions.m_cube_pos.z == 8)
        {
        m_prev_face = Face_Options;
        m_current_cube_face = Face_Menu;
        SetCurrentCubeFaceType(Face_Z_Plus);
        m_pMenuCubeOptions.MoveOnAxis(Y_Minus);

        m_navigator.Setup(Options_To_Menu);
        }

        if (m_pMenuCubeOptions.m_cube_pos.x == 7 && m_pMenuCubeOptions.m_cube_pos.y == 8 && m_pMenuCubeOptions.m_cube_pos.z == 1)
        {
//                    ArrayList<CubeFont>::iterator it;
//                    for (it = engine.ar_cubefacedata[Face_Y_Plus].lst_symbols.begin(); it != engine.ar_cubefacedata[Face_Y_Plus].lst_symbols.end(); ++it)
//                    {
//                        (*it).visible = true;
//                    }
        }
        break;

default:
        break;
        } // switch
        }
        else
        m_pMenuCubeOptions.Update(dt);
        }

        public void UpdateStoreCube(float dt)
        {
        if (m_pMenuCubeStore.IsDone())
        {
        switch (m_current_cube_face)
        {
        case Face_Menu:
        #ifdef LITE_VERSION

        if (m_pMenuCubeStore.m_cube_pos.x == 7)
        {
        m_pMenuCubeStore.MoveOnAxis(X_Minus);
        EventShowCredits();
        }
        #else
        if (m_pMenuCubeStore.m_cube_pos.y == 0)
        {
        m_prev_face = Face_Menu;
        m_current_cube_face = Face_Store;
        SetCurrentCubeFaceType(Face_Y_Minus);
        m_pMenuCubeStore.MoveOnAxis(Z_Minus);

        m_navigator.Setup(Menu_To_Store);
        }

        if (m_pMenuCubeStore.m_cube_pos.x == 7)
        {
        if (!m_pMenuCubeOptions.IsDone() || !m_pMenuCubePlay.IsDone() || m_prev_face == Face_Store)
        {
        if (m_prev_face == Face_Store)
        m_prev_face = -1;

        m_pMenuCubeStore.MoveOnAxis(X_Minus);
        }
        }
        #endif
        break;

        case Face_Store:

        if (m_pMenuCubeStore.m_cube_pos.z == 8)
        {
        m_prev_face = Face_Store;
        m_current_cube_face = Face_Menu;
        SetCurrentCubeFaceType(Face_Z_Plus);
        m_pMenuCubeStore.MoveOnAxis(Y_Plus);

        engine.HideProgressIndicator();
        m_navigator.Setup(Store_To_Menu);
        }

        break;

default:
        break;

        } // switch
        }
        else
        m_pMenuCubeStore.Update(dt);
        }

        public void UpdateCubes(float dt)
        {
        for (int i = 0; i < 6; ++i)
        {
        m_lst_titles[i].clear();
        m_lst_texts[i].clear();
        m_lst_symbols[i].clear();
        }

        CubeFaceTypesEnum face_type;

        cCube* pCube;

        ArrayList<cCube*>::iterator it;

        for (it = m_list_cubes_base.begin(); it != m_list_cubes_base.end(); ++it)
        {
        pCube = *it;

        face_type = Face_X_Plus;
        if (null != pCube.ar_fonts[face_type])
        m_lst_texts[face_type].push_back(pCube.ar_fonts[face_type]);

        if (null != pCube.ar_symbols[face_type])
        m_lst_symbols[face_type].push_back(pCube.ar_symbols[face_type]);


        face_type = Face_X_Minus;
        if (null != pCube.ar_fonts[face_type])
        m_lst_texts[face_type].push_back(pCube.ar_fonts[face_type]);

        if (null != pCube.ar_symbols[face_type])
        m_lst_symbols[face_type].push_back(pCube.ar_symbols[face_type]);


        face_type = Face_Y_Plus;
        if (null != pCube.ar_fonts[face_type])
        m_lst_texts[face_type].push_back(pCube.ar_fonts[face_type]);

        if (null != pCube.ar_symbols[face_type])
        m_lst_symbols[face_type].push_back(pCube.ar_symbols[face_type]);


        face_type = Face_Y_Minus;
        if (null != pCube.ar_fonts[face_type])
        m_lst_texts[face_type].push_back(pCube.ar_fonts[face_type]);

        if (null != pCube.ar_symbols[face_type])
        m_lst_symbols[face_type].push_back(pCube.ar_symbols[face_type]);


        face_type = Face_Z_Plus;
        if (null != pCube.ar_fonts[face_type])
        m_lst_texts[face_type].push_back(pCube.ar_fonts[face_type]);

        if (null != pCube.ar_symbols[face_type])
        m_lst_symbols[face_type].push_back(pCube.ar_symbols[face_type]);


        face_type = Face_Z_Minus;
        if (null != pCube.ar_fonts[face_type])
        m_lst_texts[face_type].push_back(pCube.ar_fonts[face_type]);

        if (null != pCube.ar_symbols[face_type])
        m_lst_symbols[face_type].push_back(pCube.ar_symbols[face_type]);
        }

        for (it = m_list_cubes_face.begin(); it != m_list_cubes_face.end(); ++it)
        {
        pCube = *it;

        face_type = Face_X_Plus;
        if (null != pCube.ar_fonts[face_type])
        m_lst_titles[face_type].push_back(pCube.ar_fonts[face_type]);

        if (null != pCube.ar_symbols[face_type])
        m_lst_symbols[face_type].push_back(pCube.ar_symbols[face_type]);


        face_type = Face_X_Minus;
        if (null != pCube.ar_fonts[face_type])
        m_lst_titles[face_type].push_back(pCube.ar_fonts[face_type]);

        if (null != pCube.ar_symbols[face_type])
        m_lst_symbols[face_type].push_back(pCube.ar_symbols[face_type]);


        face_type = Face_Y_Plus;
        if (null != pCube.ar_fonts[face_type])
        m_lst_titles[face_type].push_back(pCube.ar_fonts[face_type]);

        if (null != pCube.ar_symbols[face_type])
        m_lst_symbols[face_type].push_back(pCube.ar_symbols[face_type]);


        face_type = Face_Y_Minus;
        if (null != pCube.ar_fonts[face_type])
        m_lst_titles[face_type].push_back(pCube.ar_fonts[face_type]);

        if (null != pCube.ar_symbols[face_type])
        m_lst_symbols[face_type].push_back(pCube.ar_symbols[face_type]);


        face_type = Face_Z_Plus;
        if (null != pCube.ar_fonts[face_type])
        m_lst_titles[face_type].push_back(pCube.ar_fonts[face_type]);

        if (null != pCube.ar_symbols[face_type])
        m_lst_symbols[face_type].push_back(pCube.ar_symbols[face_type]);


        face_type = Face_Z_Minus;
        if (null != pCube.ar_fonts[face_type])
        m_lst_titles[face_type].push_back(pCube.ar_fonts[face_type]);

        if (null != pCube.ar_symbols[face_type])
        m_lst_symbols[face_type].push_back(pCube.ar_symbols[face_type]);
        }

//    printf("\nFace_Y_Symbol Size: %lu", m_lst_symbols[Face_Y_Plus].size());

        if (m_menu_cube_hilite)
        {
        m_hilite_alpha += 0.05f;

        if (m_hilite_alpha > 0.2f)
        m_hilite_alpha = 0.2f;
        }

        for (it = m_list_cubes_base.begin(); it != m_list_cubes_base.end(); ++it)
        (*it).WarmByFactor(WARM_FACTOR);
        }

        #pragma mark - UpdateIn

        public void UpdateInCredits(float dt)
        {
        m_credits_offset -= 0.05f;

        if (m_credits_offset < -5.0f)
        m_credits_offset = 20.0f;
        }

        public void UpdateInMenu(float dt)
        {
        switch (m_current_cube_face)
        {
        case Face_Menu:
        break;

        case Face_Store:
        m_pStoreCubeNoAds.Update(dt);
        m_pStoreCubeSolvers.Update(dt);
        m_pStoreCubeRestore.Update(dt);

        if (m_pStoreCubeNoAds.IsDone() && m_pStoreCubeNoAds.m_cube_pos.x == 7)
        {
        engine.PurchaseRemoveAds();
        m_pStoreCubeNoAds.MoveOnAxis(X_Minus);
        }

        if (m_pStoreCubeSolvers.IsDone() && m_pStoreCubeSolvers.m_cube_pos.x == 7)
        {
        engine.PurchaseSolvers();
        m_pStoreCubeSolvers.MoveOnAxis(X_Minus);
        }

        if (m_pStoreCubeRestore.IsDone() && m_pStoreCubeRestore.m_cube_pos.x == 7)
        {
        engine.PurchaseRestore();
        m_pStoreCubeRestore.MoveOnAxis(X_Minus);
        }
        break;

default:
        break;
        }

        UpdatePlayCube(dt);
        UpdateOptionsCube(dt);
        UpdateStoreCube(dt);
        }

        public void UpdateHilite(float dt)
        {
        m_hilite_timeout -= dt;

        if (m_hilite_timeout < 0.0f)
        {
        m_hilite_timeout = 0.05f;

        Color color(160, 160, 160, 255);

        if (!m_pMenuCubePlay.lst_cubes_to_hilite.empty())
        {
        cCube* p = m_pMenuCubePlay.lst_cubes_to_hilite.front();
        m_pMenuCubePlay.lst_cubes_to_hilite.pop_front();

        p.color_current = color;
        }

        if (!m_pMenuCubeOptions.lst_cubes_to_hilite.empty())
        {
        cCube* p = m_pMenuCubeOptions.lst_cubes_to_hilite.front();
        m_pMenuCubeOptions.lst_cubes_to_hilite.pop_front();

        p.color_current = color;
        }

        if (!m_pMenuCubeStore.lst_cubes_to_hilite.empty())
        {
        cCube* p = m_pMenuCubeStore.lst_cubes_to_hilite.front();
        m_pMenuCubeStore.lst_cubes_to_hilite.pop_front();

        p.color_current = color;
        }

        if (!m_pStoreCubeNoAds.lst_cubes_to_hilite.empty())
        {
        cCube* p = m_pStoreCubeNoAds.lst_cubes_to_hilite.front();
        m_pStoreCubeNoAds.lst_cubes_to_hilite.pop_front();

        p.color_current = color;
        }



        if (!m_pStoreCubeRestore.lst_cubes_to_hilite.empty())
        {
        cCube* p = m_pStoreCubeRestore.lst_cubes_to_hilite.front();
        m_pStoreCubeRestore.lst_cubes_to_hilite.pop_front();

        p.color_current = color;
        }
        }
        }

        #pragma mark - Update

        public void Update(float dt)
        {
        if (m_showing_help)
        {
        m_show_help_timeout	-= dt;

        if (m_show_help_timeout < 0.0f)
        {
        m_showing_help = false;

        //printf("\nRestore original text\n");
        ReleaseCubeTextsOnFace(Face_Z_Plus);
        //cCreator::AlterRedCubeFontsOnFaceMain(false);
        cMenuFaceBuilder::ResetTransforms();
        cMenuFaceBuilder::BuildTexts(Face_Menu, Face_Z_Plus, false);
        }
        }

        if (!m_navigator.IsCurrentNavigation(NoNavigation))
        {
        m_navigator.Update();
        }
        else
        {
        switch (m_state)
        {
        case InMenu:            UpdateInMenu(dt);           break;
        case InCredits:         UpdateInCredits(dt);        break;
        case AnimToCredits:     UpdateAnimToCredits(dt);    break;
        case AnimFromCredits:   UpdateAnimFromCredits(dt);  break;
        }
        UpdateHilite(dt);
        }

        UpdateCubes(dt);
        }

        #pragma mark - Event

        public void EventPlayLevel(DifficultyEnum difficulty, int level_number)
        {
        if (false == engine.GetCanPlayLockedLevels())
        {
        switch (difficulty)
        {
        case Easy:
        if (LEVEL_LOCKED == cCubetraz::GetStarsEasy(level_number))
        {
        engine.PlaySound(SOUND_TAP_ON_LOCKED_LEVEL_CUBE);
        return;
        }
        break;

        case Normal:
        if (LEVEL_LOCKED == cCubetraz::GetStarsNormal(level_number))
        {
        engine.PlaySound(SOUND_TAP_ON_LOCKED_LEVEL_CUBE);
        return;
        }
        break;

        case Hard:
        if (LEVEL_LOCKED == cCubetraz::GetStarsHard(level_number))
        {
        engine.PlaySound(SOUND_TAP_ON_LOCKED_LEVEL_CUBE);
        return;
        }
        break;
        }
        }

        engine.PlaySound(SOUND_TAP_ON_LEVEL_CUBE);

        engine.StopMusic();
        engine.anim_init_data.ClearTransforms();

        switch (difficulty)
        {
        case Easy:
        //printf("\nPlay Easy level: %d\n", level_number);

        if (level_number >= 1 && level_number <= 15)
        engine.anim_init_data.SetFaces(Face_Easy01, Face_Easy04, Face_Menu);

        if (level_number >= 16 && level_number <= 30)
        engine.anim_init_data.SetFaces(Face_Easy02, Face_Easy01, Face_Empty);

        if (level_number >= 31 && level_number <= 45)
        engine.anim_init_data.SetFaces(Face_Easy03, Face_Easy02, Face_Empty);

        if (level_number >= 46 && level_number <= 60)
        engine.anim_init_data.SetFaces(Face_Easy04, Face_Easy03, Face_Empty);

        break;

        case Normal:
        //printf("\nPlay Normal level: %d\n", level_number);

        if (level_number >= 1 && level_number <= 15)
        engine.anim_init_data.SetFaces(Face_Normal01, Face_Normal04, Face_Easy01);

        if (level_number >= 16 && level_number <= 30)
        engine.anim_init_data.SetFaces(Face_Normal02, Face_Normal01, Face_Empty);

        if (level_number >= 31 && level_number <= 45)
        engine.anim_init_data.SetFaces(Face_Normal03, Face_Normal02, Face_Empty);

        if (level_number >= 46 && level_number <= 60)
        engine.anim_init_data.SetFaces(Face_Normal04, Face_Normal03, Face_Empty);

        break;

        case Hard:
        //printf("\nPlay Hard level: %d\n", level_number);

        if (level_number >= 1 && level_number <= 15)
        engine.anim_init_data.SetFaces(Face_Hard01, Face_Hard04, Face_Normal01);

        if (level_number >= 16 && level_number <= 30)
        engine.anim_init_data.SetFaces(Face_Hard02, Face_Hard01, Face_Empty);

        if (level_number >= 31 && level_number <= 45)
        engine.anim_init_data.SetFaces(Face_Hard03, Face_Hard02, Face_Empty);

        if (level_number >= 46 && level_number <= 60)
        engine.anim_init_data.SetFaces(Face_Hard04, Face_Hard03, Face_Empty);

        break;
        }

        engine.level_init_data.difficulty = difficulty;
        engine.level_init_data.level_number = level_number;
        engine.level_init_data.init_action = FullInit;

        engine.anim_init_data.type = AnimToLevel;

        engine.anim_init_data.list_cubes_base.clear();

        ArrayList<cCube*>::iterator it;
        for (it = m_list_cubes_base.begin(); it != m_list_cubes_base.end(); ++it)
        {
        engine.anim_init_data.list_cubes_base.push_back(*it);
        }

        engine.anim_init_data.camera_from = m_camera_current;
        engine.anim_init_data.camera_to = engine.m_level.m_camera_level;

        engine.anim_init_data.pos_light_from = m_pos_light_current;
        engine.anim_init_data.pos_light_to = engine.m_level.m_pos_light;

        engine.ShowScene(Scene_Anim);
        }

        public void EventShowCredits()
        {
        m_credits_offset = 20.0f;

        m_t = 0.0f;
        m_state = AnimToCredits;

        float divisor = 20.0f;

        m_interpolators[0].Setup(m_camera_menu.eye.x, m_camera_credits.eye.x, divisor);
        m_interpolators[1].Setup(m_camera_menu.eye.y, m_camera_credits.eye.y, divisor);
        m_interpolators[2].Setup(m_camera_menu.eye.z, m_camera_credits.eye.z, divisor);

        m_interpolators[3].Setup(m_camera_menu.target.x, m_camera_credits.target.x, divisor);
        m_interpolators[4].Setup(m_camera_menu.target.y, m_camera_credits.target.y, divisor);
        m_interpolators[5].Setup(m_camera_menu.target.z, m_camera_credits.target.z, divisor);

        engine.HideGameCenterInfo();
        }

        #pragma mark - Render

        public void RenderForPicking(PickRenderTypeEnum type)
        {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glDisable(GL_LIGHTING);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);

        glDisable(GL_TEXTURE_2D);

        engine.SetProjection3D();
        engine.SetModelViewMatrix3D(m_camera_current);

        m_navigator.ApplyRotations();

        glPushMatrix();
        glTranslatef(engine.cube_offset.x, engine.cube_offset.y, engine.cube_offset.z);

        Graphics.Prepare();

        switch (type)
        {
        case RenderOnlyOptions:
        Graphics.AddCubeSize(m_arOptionsCubes[0].pos.x, m_arOptionsCubes[0].pos.y, m_arOptionsCubes[0].pos.z, HALF_CUBE_SIZE * 1.5f, m_arOptionsCubes[0].color);
        Graphics.AddCubeSize(m_arOptionsCubes[1].pos.x, m_arOptionsCubes[1].pos.y, m_arOptionsCubes[1].pos.z, HALF_CUBE_SIZE * 1.5f, m_arOptionsCubes[1].color);
        Graphics.AddCubeSize(m_arOptionsCubes[2].pos.x, m_arOptionsCubes[2].pos.y, m_arOptionsCubes[2].pos.z, HALF_CUBE_SIZE * 1.5f, m_arOptionsCubes[2].color);
        Graphics.AddCubeSize(m_arOptionsCubes[3].pos.x, m_arOptionsCubes[3].pos.y, m_arOptionsCubes[3].pos.z, HALF_CUBE_SIZE * 1.5f, m_arOptionsCubes[3].color);
        break;

        case RenderOnlyMovingCubePlay:
        Graphics.AddCubeSize(m_pMenuCubePlay.pos.x, m_pMenuCubePlay.pos.y, m_pMenuCubePlay.pos.z, HALF_CUBE_SIZE * 1.5f, m_pMenuCubePlay.color);
        break;

        #ifndef LITE_VERSION
        case RenderOnlyMovingCubeStore:
        Graphics.AddCubeSize(m_pMenuCubeStore.pos.x, m_pMenuCubeStore.pos.y, m_pMenuCubeStore.pos.z, HALF_CUBE_SIZE * 1.5f, m_pMenuCubeStore.color);
        break;
        #endif

        case RenderOnlyMovingCubeOptions:
        Graphics.AddCubeSize(m_pMenuCubeOptions.pos.x, m_pMenuCubeOptions.pos.y, m_pMenuCubeOptions.pos.z, HALF_CUBE_SIZE * 1.5f, m_pMenuCubeOptions.color);
        break;

        #ifndef LITE_VERSION
        case RenderOnlyCubeCredits:
        Graphics.AddCubeSize(m_pCubeCredits.pos.x, m_pCubeCredits.pos.y, m_pCubeCredits.pos.z, HALF_CUBE_SIZE * 1.5f, m_pCubeCredits.color);
        break;
        #endif

        case RenderOnlyMovingCubes:

        switch (m_current_cube_face)
        {
        case Face_Options:
        Graphics.AddCubeSize(m_pMenuCubeOptions.pos.x, m_pMenuCubeOptions.pos.y, m_pMenuCubeOptions.pos.z, HALF_CUBE_SIZE * 1.5f, m_pMenuCubeOptions.color);
        break;
        #ifndef LITE_VERSION
        case Face_Store:
        Graphics.AddCubeSize(m_pMenuCubeStore.pos.x, m_pMenuCubeStore.pos.y, m_pMenuCubeStore.pos.z, HALF_CUBE_SIZE * 1.5f, m_pMenuCubeStore.color);
        Graphics.AddCubeSize(m_pStoreCubeNoAds.pos.x, m_pStoreCubeNoAds.pos.y, m_pStoreCubeNoAds.pos.z, HALF_CUBE_SIZE * 1.5f, m_pStoreCubeNoAds.color);
        Graphics.AddCubeSize(m_pStoreCubeRestore.pos.x, m_pStoreCubeRestore.pos.y, m_pStoreCubeRestore.pos.z, HALF_CUBE_SIZE * 1.5f, m_pStoreCubeRestore.color);
        Graphics.AddCubeSize(m_pStoreCubeSolvers.pos.x, m_pStoreCubeSolvers.pos.y, m_pStoreCubeSolvers.pos.z, HALF_CUBE_SIZE * 1.5f, m_pStoreCubeSolvers.color);
        break;
        #endif
        case Face_Menu:
        Graphics.AddCubeSize(m_pMenuCubePlay.pos.x, m_pMenuCubePlay.pos.y, m_pMenuCubePlay.pos.z, HALF_CUBE_SIZE * 1.5f, m_pMenuCubePlay.color);
        Graphics.AddCubeSize(m_pMenuCubeOptions.pos.x, m_pMenuCubeOptions.pos.y, m_pMenuCubeOptions.pos.z, HALF_CUBE_SIZE * 1.5f, m_pMenuCubeOptions.color);
        Graphics.AddCubeSize(m_pMenuCubeStore.pos.x, m_pMenuCubeStore.pos.y, m_pMenuCubeStore.pos.z, HALF_CUBE_SIZE * 1.5f, m_pMenuCubeStore.color);
        #ifndef LITE_VERSION
        Graphics.AddCubeSize(m_pCubeCredits.pos.x, m_pCubeCredits.pos.y, m_pCubeCredits.pos.z, HALF_CUBE_SIZE * 1.5f, m_pCubeCredits.color);
        #endif
        break;

default:
        Graphics.AddCubeSize(m_pMenuCubePlay.pos.x, m_pMenuCubePlay.pos.y, m_pMenuCubePlay.pos.z, HALF_CUBE_SIZE * 1.5f, m_pMenuCubePlay.color);
        break;
        } // switch

        break;

        case RenderOnlyLevelCubes:
        {
        LevelCube p;
        ArrayList<LevelCube>::iterator it;

        for (it = engine.ar_cubefacedata[m_current_cube_face_type].lst_level_cubes.begin(); it != engine.ar_cubefacedata[m_current_cube_face_type].lst_level_cubes.end(); ++it)
        {
        p = *it;
        if (m_current_cube_face == p.face_id)
        {
        Graphics.AddCubeSize(p.pos.x, p.pos.y, p.pos.z, HALF_CUBE_SIZE * 1.5f, p.color);
        }
        }
        }
        break;

default:
        break;
        }	// switch

        Graphics.SetStreamSourceOnlyVerticeAndColor();
        Graphics.RenderTriangles();

        glPopMatrix();
        }

        public void Render()
        {
        //printf("\nAspect ratio: %f", engine.m_aspectRatio);

        //printf("\nm_camera_current %f, %f, %f", m_camera_current.eye.x, m_camera_current.eye.y, m_camera_current.eye.z);


//    if (render)
//        return RenderForPicking(RenderOnlyLevelCubes);

        engine.SetProjection2D();
        engine.SetModelViewMatrix2D();

        glEnable(GL_BLEND);
        glDisable(GL_LIGHTING);
        glDepthMask(GL_FALSE);
        glEnable(GL_TEXTURE_2D);

        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);

        Color color(255, 255, 255, engine.dirty_alpha);
        engine.DrawFBOTexture(engine.texture_id_dirty, color);

        glDepthMask(GL_TRUE);


        engine.SetProjection3D();
        engine.SetModelViewMatrix3D(m_camera_current);

        const vec4 lightPosition(m_pos_light_current.x, m_pos_light_current.y, m_pos_light_current.z, 1.0f);
        glLightfv(GL_LIGHT0, GL_POSITION, lightPosition.Pointer());

        glEnable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);

        glEnableClientState(GL_NORMAL_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        if (InCredits == m_state)
        {
        glBindTexture(GL_TEXTURE_2D, engine.texture_id_credits);
        DrawCredits();
        }

        #ifdef DRAW_AXES_GLOBAL
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_LIGHTING);
        engine.DrawAxis();
        glEnable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);
        #endif

        m_navigator.ApplyRotations();

        glBindTexture(GL_TEXTURE_2D, engine.texture_id_gray_concrete);
        DrawTheCube();

        #ifdef DRAW_AXES_CUBE
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_LIGHTING);
        engine.DrawAxes();
        glEnable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);
        #endif

        glPushMatrix();

        glTranslatef(engine.cube_offset.x, engine.cube_offset.y, engine.cube_offset.z);

        glBindTexture(GL_TEXTURE_2D, engine.texture_id_level_cubes);
        DrawLevelCubes();

        glBindTexture(GL_TEXTURE_2D, engine.texture_id_player);
        DrawMenuCubes();

        glPopMatrix();

        glDisable(GL_LIGHTING);
        Graphics.EnableBlending();

        Graphics.SetStreamSourceFloatAndColor();

        glPushMatrix();
        glTranslatef(engine.cube_offset.x, engine.cube_offset.y, engine.cube_offset.z);

        color = cEngine::GetTextColor();

        glBindTexture(GL_TEXTURE_2D, engine.texture_id_fonts);
        DrawTexts(color);

        color = cEngine::GetTitleColor();
        DrawTextsTitles(color);

        glBindTexture(GL_TEXTURE_2D, engine.texture_id_numbers);

        DrawLevelNumbers();

        glBindTexture(GL_TEXTURE_2D, engine.texture_id_symbols);

        DrawLevelCubeSymbols();

        color = cEngine::GetSymbolColor();
        DrawSymbols(color);

        if (m_menu_cube_hilite)
        {
        color = Color(255, 255, 255, m_hilite_alpha * 255);
        glDisable(GL_DEPTH_TEST);
        DrawCubeHiLite(color);
        glEnable(GL_DEPTH_TEST);
        }

        glPopMatrix();

        if (Face_Options == m_current_cube_face || m_navigator.IsCurrentNavigation(Menu_To_Options) || m_navigator.IsCurrentNavigation(Options_To_Menu))
        DrawCubeFaceOptions();
/*
    if (m_fingerdown)
    {
        glDisable(GL_CULL_FACE);
        glDisable(GL_DEPTH_TEST);

        glDisable(GL_TEXTURE_2D);
        engine.SetProjection2D();
        SetModelViewMatrix2D();

 //        glDisable(GL_LIGHTING);
 //        glDisableClientState(GL_NORMAL_ARRAY);
 //        glDisableClientState(GL_TEXTURE_COORD_ARRAY);

        Graphics.EnableBlending();

        engine.DrawCircleAt(m_fingermove_x, engine.m_height - m_fingermove_y, 10.0f * engine.m_scaleFactor);

        Graphics.DisableBlending();

        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);

        engine.SetProjection3D();
    }
*/

        }

        #pragma mark - OnFinger

        public void OnFingerDown(float x, float y, int finger_count)
        {
        //render = !render;
//    printf("\ncMenu::OnFingerDown");

        if ( m_navigator.IsCurrentNavigation(NoNavigation) &&
        m_pMenuCubePlay.IsDone() &&
        m_pMenuCubeOptions.IsDone() &&
        m_pMenuCubeStore.IsDone() &&
        m_pStoreCubeRestore.IsDone() &&
        m_pStoreCubeNoAds.IsDone() )
        {
        m_fingerdown = true;

        m_pos_down.x = x;
        m_pos_down.y = y;

        RenderForPicking(RenderOnlyMovingCubes);

        m_color_down = engine.GetColorFromScreen(m_pos_down);
        cMenuCube* pMenuCube = GetMovingCubeFromColor(m_color_down.r);

        if (pMenuCube)
        {
        m_hilite_alpha = 0.0f;
        m_menu_cube_hilite = pMenuCube;
        CubePos cp = m_menu_cube_hilite.m_cube_pos;
        m_font_hilite.Init(SymbolHilite, cp);
        }
        }
        }

        public void OnFingerMove(float prev_x, float prev_y, float cur_x, float cur_y, int finger_count)
        {
        if (m_fingerdown)
        {
        //printf("\ncMenu::OnFingerMove");
        m_pos_move.x = cur_x;
        m_pos_move.y = cur_y;

        float dist = GetDistance2D(m_pos_down, m_pos_move);
        //printf("\nOnFingerMove: %.2f", dist);

        if (dist > 20.0f * engine.device_scale)
        m_swipe = true;
        }
        }

        public void OnFingerUp(float x, float y, int finger_count)
        {
        m_fingerdown = false;

        if (!m_pMenuCubePlay.IsDone() || !m_pMenuCubeOptions.IsDone() || !m_pMenuCubeStore.IsDone())
        return;

        m_menu_cube_hilite = null;

        m_pos_up.x = x;
        m_pos_up.y = y;

        if (InCredits == m_state || AnimToCredits == m_state || AnimFromCredits == m_state)
        {
        if (InCredits == m_state)
        {
        m_t = 0.0f;
        m_state = AnimFromCredits;
        engine.ShowGameCenterInfo();
        }
        return;
        }

        if (m_swipe)
        {
        m_swipe = false;
        HandleSwipe();
        }
        else // single tap
        {
        switch (m_current_cube_face)
        {
        case Face_Menu:
        FingerUpOnFaceMenu();
        break;

        case Face_Options:
        FingerUpOnFaceOptions();
        break;

        case Face_Easy01:
        case Face_Easy02:
        case Face_Easy03:
        case Face_Easy04:
        FingerUpOnFacesEasy();
        break;

        case Face_Normal01:
        case Face_Normal02:
        case Face_Normal03:
        case Face_Normal04:
        FingerUpOnFacesNormal();
        break;

        case Face_Hard01:
        case Face_Hard02:
        case Face_Hard03:
        case Face_Hard04:
        FingerUpOnFacesHard();
        break;

default:
        break;
        }
        }
        }

        #pragma mark - FingerUpOnFace

        public void FingerUpOnFaceOptions()
        {
        RenderForPicking(RenderOnlyOptions);
        m_color_down = engine.GetColorFromScreen(m_pos_down);
        m_color_up = engine.GetColorFromScreen(m_pos_up);

        if (m_color_down.r == m_color_up.r)
        {
        switch (m_color_down.r)
        {
        case 255:
        engine.MusicVolumeUp();
        engine.PlaySound(SOUND_VOLUME_UP);
        break;

        case 254:
        engine.MusicVolumeDown();
        engine.PlaySound(SOUND_VOLUME_DOWN);
        break;

        case 253:
        engine.SoundVolumeUp();
        engine.PlaySound(SOUND_VOLUME_UP);
        break;

        case 252:
        engine.SoundVolumeDown();
        engine.PlaySound(SOUND_VOLUME_DOWN);
        break;

default:
        break;
        }
        }
        }

        public void FingerUpOnFacesEasy()
        {
        RenderForPicking(RenderOnlyLevelCubes);
        m_color_down = engine.GetColorFromScreen(m_pos_down);
        m_color_up = engine.GetColorFromScreen(m_pos_up);

        if (m_color_down.r == m_color_up.r)
        {
        int level_number = 255 - m_color_down.r;

        if (level_number >= 1 && level_number <= 60)
        EventPlayLevel(Easy, level_number);
        }
        }

        public void FingerUpOnFacesNormal()
        {
        RenderForPicking(RenderOnlyLevelCubes);
        m_color_down = engine.GetColorFromScreen(m_pos_down);
        m_color_up = engine.GetColorFromScreen(m_pos_up);

        if (m_color_down.r == m_color_up.r)
        {
        int level_number = 255 - m_color_down.r;

        if (level_number >= 1 && level_number <= 60)
        EventPlayLevel(Normal, level_number);
        }
        }

        public void FingerUpOnFacesHard()
        {
        RenderForPicking(RenderOnlyLevelCubes);
        m_color_down = engine.GetColorFromScreen(m_pos_down);
        m_color_up = engine.GetColorFromScreen(m_pos_up);

        if (m_color_down.r == m_color_up.r)
        {
        int level_number = 255 - m_color_down.r;

        if (level_number >= 1 && level_number <= 60)
        EventPlayLevel(Hard, level_number);
        }
        }

        public void FingerUpOnFaceMenu()
        {
        RenderForPicking(RenderOnlyMovingCubes);
        m_color_down = engine.GetColorFromScreen(m_pos_down);
        m_color_up = engine.GetColorFromScreen(m_pos_up);

        if (m_color_down.r == m_color_up.r)
        {
        if (1 == m_color_down.r)
        {
        EventShowCredits();
        return;
        }

        if (!m_showing_help)
        {
        if (255 == m_color_down.r || 200 == m_color_down.r || 100 == m_color_down.r)
        {
        m_showing_help = true;
        m_show_help_timeout = 3.0f;
        ReleaseCubeTextsOnFace(Face_Z_Plus);
        //cCreator::AlterRedCubeFontsOnFaceMain(true);
        cMenuFaceBuilder::ResetTransforms();
        cMenuFaceBuilder::BuildTexts(Face_Menu, Face_Z_Plus, true);
        }
        }
        }
        }

        #pragma mark - Swipe

        public void HandleSwipe()
        {
        SwipeDirEnums swipeDir;
        float length;
        engine.GetSwipeDirAndLength(m_pos_down, m_pos_up, swipeDir, length);

        if ( length > (30.0f * engine.m_scaleFactor) )
        {
        RenderForPicking(RenderOnlyMovingCubes);
        Color down_color = engine.GetColorFromScreen(m_pos_down);
        //printf("\nOnFingerUp [SWIPE] color is: %d, %d, %d, %d", m_down_color.r, m_down_color.g, m_down_color.b, m_down_color.a);

        cMenuCube* pMenuCube = GetMovingCubeFromColor(down_color.r);

        if (pMenuCube)
        {
        switch (swipeDir)
        {
        case SwipeLeft:

        switch (m_current_cube_face)
        {
        case Face_Menu:
        case Face_Options:
        case Face_Store:
        pMenuCube.MoveOnAxis(X_Minus);
        break;

        case Face_Normal01:
        case Face_Normal02:
        case Face_Normal03:
        case Face_Normal04:
        pMenuCube.MoveOnAxis(X_Plus);
        break;

        case Face_Hard01:
        case Face_Hard02:
        case Face_Hard03:
        case Face_Hard04:
        case Face_Tutorial:
        pMenuCube.MoveOnAxis(Z_Minus);
        break;

        case Face_Easy01:
        case Face_Easy02:
        case Face_Easy03:
        case Face_Easy04:
        pMenuCube.MoveOnAxis(Z_Plus);
        break;

default:
        break;
        }
        break;

        case SwipeRight:

        switch (m_current_cube_face)
        {
        case Face_Menu:
        case Face_Options:
        case Face_Store:
        pMenuCube.MoveOnAxis(X_Plus);

        if (Face_Menu == m_current_cube_face)
        {
        if (pMenuCube != m_pMenuCubeOptions)
        {
        if (7 == m_pMenuCubeOptions.m_cube_pos.x)
        {
        m_pMenuCubeOptions.MoveOnAxis(X_Minus);
        }
        }
        }
        break;

        case Face_Normal01:
        case Face_Normal02:
        case Face_Normal03:
        case Face_Normal04:
        pMenuCube.MoveOnAxis(X_Minus);
        break;

        case Face_Hard01:
        case Face_Hard02:
        case Face_Hard03:
        case Face_Hard04:
        case Face_Tutorial:
        pMenuCube.MoveOnAxis(Z_Plus);
        break;

        case Face_Easy01:
        case Face_Easy02:
        case Face_Easy03:
        case Face_Easy04:
        pMenuCube.MoveOnAxis(Z_Minus);
        break;

default:
        break;
        }
        break;

        case SwipeUp:

        switch (m_current_cube_face)
        {
        case Face_Store:
        pMenuCube.MoveOnAxis(Z_Plus);
        break;

        case Face_Options:
        pMenuCube.MoveOnAxis(Z_Minus);
        break;

        case Face_Easy02:
        pMenuCube.MoveOnAxis(X_Plus);
        break;

        case Face_Easy03:
        pMenuCube.MoveOnAxis(Y_Minus);
        break;

        case Face_Easy04:
        pMenuCube.MoveOnAxis(X_Minus);
        break;

        case Face_Normal02:
        pMenuCube.MoveOnAxis(Z_Minus);
        break;

        case Face_Normal03:
        pMenuCube.MoveOnAxis(Y_Minus);
        break;

        case Face_Normal04:
        pMenuCube.MoveOnAxis(Z_Plus);
        break;

        case Face_Hard02:
        pMenuCube.MoveOnAxis(X_Minus);
        break;

        case Face_Hard03:
        pMenuCube.MoveOnAxis(Y_Minus);
        break;

        case Face_Hard04:
        pMenuCube.MoveOnAxis(X_Plus);
        break;

default:
        pMenuCube.MoveOnAxis(Y_Plus);
        break;
        }
        break;

        case SwipeDown:

        switch (m_current_cube_face)
        {
        case Face_Store:
        pMenuCube.MoveOnAxis(Z_Minus);
        break;

        case Face_Options:
        pMenuCube.MoveOnAxis(Z_Plus);
        break;

        case Face_Easy02:
        pMenuCube.MoveOnAxis(X_Minus);
        break;

        case Face_Easy03:
        pMenuCube.MoveOnAxis(Y_Plus);
        break;

        case Face_Easy04:
        pMenuCube.MoveOnAxis(X_Plus);
        break;

        case Face_Normal02:
        pMenuCube.MoveOnAxis(Z_Plus);
        break;

        case Face_Normal03:
        pMenuCube.MoveOnAxis(Y_Plus);
        break;

        case Face_Normal04:
        pMenuCube.MoveOnAxis(Z_Minus);
        break;

        case Face_Hard02:
        pMenuCube.MoveOnAxis(X_Plus);
        break;

        case Face_Hard03:
        pMenuCube.MoveOnAxis(Y_Plus);
        break;

        case Face_Hard04:
        pMenuCube.MoveOnAxis(X_Minus);
        break;

default:
        pMenuCube.MoveOnAxis(Y_Minus);
        break;
        }
        break;

default:
        break;
        }
        }
        }
        }

}

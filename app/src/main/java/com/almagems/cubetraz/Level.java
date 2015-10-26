package com.almagems.cubetraz;

import java.util.ArrayList;
import static android.opengl.GLES10.*;
import static com.almagems.cubetraz.Constants.*;


public final class Level extends Scene {

    public enum LevelStateEnum {
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

    public enum LevelNextActionEnum {
        NoNextAction,
        ShowSceneSolvers,
    }

    public enum SubAppearStateEnum {
        SubAppearWait,
        SubAppearKeyAndPlayer,
        SubAppearLevel,
        SubAppearWaitAgain,
    }

    private int m_tutor_index;
    private CompletedFaceNextActionEnum m_completed_face_next_action;
    private LevelNextActionEnum m_next_action;
    private float m_timeout_undo;
    private final ArrayList<UndoData> m_lst_undo = new ArrayList<>();

    public LevelStateEnum m_state;
    private LevelStateEnum m_state_to_restore;

    private SubAppearStateEnum m_appear_state;

    private Color m_color_down;
    private Color m_color_up;

    private HUD m_hud = new HUD();

    private UserRotation m_user_rotation = new UserRotation();

    private MenuCube m_menu_cube_hilite;
    private CubeFont m_font_hilite = new CubeFont();
    private float m_hilite_alpha;

    private float m_hilite_timeout;

    public float dead_size;
    public int dead_alpha;
    private int dead_alpha_step;

    private int m_star_count;

    public ArrayList<Cube> m_list_cubes_level = new ArrayList<>();
    public ArrayList<Cube> m_list_cubes_wall_y_minus = new ArrayList<>();
    public ArrayList<Cube> m_list_cubes_wall_x_minus = new ArrayList<>();
    public ArrayList<Cube> m_list_cubes_wall_z_minus = new ArrayList<>();
    public ArrayList<Cube> m_list_cubes_edges = new ArrayList<>();
    public ArrayList<Cube> m_list_cubes_base = new ArrayList<>();
    public ArrayList<Cube> m_list_cubes_face = new ArrayList<>();
    public ArrayList<Cube> m_list_cubes_hint = new ArrayList<>();

    private Cube[] m_ar_hint_cubes = new Cube[MAX_HINT_CUBES];
    private boolean m_show_hint_2nd;
    private int m_hint_index;
    private float m_hint_timeout;

    public final AppearDisappearListData m_ad_level = new AppearDisappearListData();
    public final AppearDisappearListData m_ad_base = new AppearDisappearListData();
    public final AppearDisappearListData m_ad_face = new AppearDisappearListData();

    public final Camera m_camera_level = new Camera();
    public final Camera m_camera_level_completed = new Camera();
    public final Camera m_camera_level_paused = new Camera();
    public final Camera m_camera_current = new Camera();

    private float m_target_rotation_degree;

    private final EaseOutDivideInterpolation m_interpolator = new EaseOutDivideInterpolation();

    private boolean m_alter_view;
    private boolean m_reposition_view;
    private boolean m_draw_texts;

    private DifficultyEnum m_difficulty;
    private int m_level_number;
    private int m_level_number_begin;

    private int m_solution_pointer;
    public int[] m_ar_solution = new int[MAX_SOLUTION_MOVES];

    private int m_moves_counter;
    public int m_min_solution_steps;
    private float m_timeout;
    private float m_timer_to_statistics;

    private CubePos m_cube_pos_key = new CubePos();

// CubeFonts
    public CubeFont m_cubefont_up = new CubeFont();
    public CubeFont m_cubefont_mid = new CubeFont();
    public CubeFont m_cubefont_low = new CubeFont();

    private final ArrayList<CubeFont> m_list_fonts_pool = new ArrayList<>();
    public final ArrayList<CubeFont> m_list_fonts = new ArrayList<>();

// spec cubes
    private final ArrayList<MovingCube> m_lst_moving_cubes = new ArrayList<>();
    private final ArrayList<MoverCube> m_lst_mover_cubes = new ArrayList<>();
    private final ArrayList<DeadCube> m_lst_dead_cubes = new ArrayList<>();

    private boolean m_anim_undo;
    private float m_anim_undo_alpha;
    private float m_anim_undo_scale;

    private float m_fade_value;

    private final CubeRotation m_cube_rotation_current = new CubeRotation();
    private final CubeRotation m_cube_rotation = new CubeRotation();

    public Vector m_pos_light;

    private MovingCube m_moving_cube;
    private MoverCube m_mover_cube;
    private DeadCube m_dead_cube;

// MenuCubes
    private MenuCube m_menu_cube_up;
    private MenuCube m_menu_cube_mid;
    private MenuCube m_menu_cube_low;

    private boolean m_draw_menu_cubes;

    private float m_t;

    public PlayerCube m_player_cube;


    // ctor
    public Level() {
        float d = 1.0f;
        m_anim_undo = false;
        m_level_number = 1;
        
        m_pos_light = new Vector(3.0f, 5.0f, 20.0f);
        
        m_camera_level.eye = new Vector(0.0f, 20.0f, 31.0f);
        m_camera_level.target = new Vector(0.0f, -1.6f, 0.0f);

        m_camera_level_completed.eye = new Vector(0.0f, 5.0f, 34.0f);
        m_camera_level_completed.target = new Vector(0.0f, -1.7f, 0.0f);

        m_camera_level_paused.eye = new Vector(0.0f, 5.0f, 34.0f);
        m_camera_level_paused.target = new Vector(0.0f, -1.7f, 0.0f);

        d = 1.2f;

        m_camera_level.eye.scaled(d);

        m_camera_level_completed.eye.scaled(d);
        m_camera_level_completed.target.scaled(d);

        m_camera_level_paused.eye.scaled(d);
        m_camera_level_paused.target.scaled(d);

        m_player_cube = new PlayerCube();
        m_player_cube.init(new CubePos(4, 4, 4));

        m_menu_cube_up = new MenuCube();
        m_menu_cube_up.init(new CubePos(0, 0, 0), new Color(0, 0, 200, 255));

        m_menu_cube_mid = new MenuCube();
        m_menu_cube_mid.init(new CubePos(0, 0, 0), new Color(0, 0, 150, 255));

        m_menu_cube_low = new MenuCube();
        m_menu_cube_low.init(new CubePos(0, 0, 0), new Color(0, 0, 100, 255));

        m_reposition_view = false;
        m_show_hint_2nd = false;
    }

    private MenuCube getMovingCubeFromColor(int color) {
        switch (color) {
            case 200: return m_menu_cube_up;
            case 150: return m_menu_cube_mid;
            case 100: return m_menu_cube_low;
        }
        return null;
    }

    public CubePos getKeyPos() {
        return m_cube_pos_key;
    }
    public int getLevelNumber() {
        return m_level_number;
    }
    public DifficultyEnum getDifficulty() {
        return m_difficulty;
    }

/*
    void cLevel::ReportAchievementEasy()
    {
        float number_of_solved_levels = 0.0f;
        int star_count = 0;

        if (m_level_number >= 1 && m_level_number <= 15)
        {
            for (int i = 1; i <= 15; ++i)
            {
                star_count = cCubetraz::GetStarsEasy(i);
                if (star_count > 0)
                    number_of_solved_levels += 1.0f;
            }

            float percent = (number_of_solved_levels / 15.0f) * 100.0f;
            engine.ReportAchievement("com.almagems.cubetraz.easy01facesolved", percent);
        }

        if (m_level_number >= 16 && m_level_number <= 30)
        {
            for (int i = 16; i <= 30; ++i)
            {
                star_count = cCubetraz::GetStarsEasy(i);
                if (star_count > 0)
                    number_of_solved_levels += 1.0f;
            }

            float percent = (number_of_solved_levels / 15.0f) * 100.0f;
            engine.ReportAchievement("com.almagems.cubetraz.easy02facesolved", percent);
        }

        if (m_level_number >= 31 && m_level_number <= 45)
        {
            for (int i = 31; i <= 45; ++i)
            {
                star_count = cCubetraz::GetStarsEasy(i);
                if (star_count > 0)
                    number_of_solved_levels += 1.0f;
            }

            float percent = (number_of_solved_levels / 15.0f) * 100.0f;
            engine.ReportAchievement("com.almagems.cubetraz.easy03facesolved", percent);
        }

        if (m_level_number >= 46 && m_level_number <= 60)
        {
            for (int i = 46; i <= 60; ++i)
            {
                star_count = cCubetraz::GetStarsEasy(i);
                if (star_count > 0)
                    number_of_solved_levels += 1.0f;
            }

            float percent = (number_of_solved_levels / 15.0f) * 100.0f;
            engine.ReportAchievement("com.almagems.cubetraz.easy04facesolved", percent);
        }
    }

    void cLevel::ReportAchievementNormal()
    {
        float number_of_solved_levels = 0.0f;
        int star_count = 0;

        if (m_level_number >= 1 && m_level_number <= 15)
        {
            for (int i = 1; i <= 15; ++i)
            {
                star_count = cCubetraz::GetStarsNormal(i);
                if (star_count > 0)
                    number_of_solved_levels += 1.0f;
            }

            float percent = (number_of_solved_levels / 15.0f) * 100.0f;
            engine.ReportAchievement("com.almagems.cubetraz.normal01facesolved", percent);
        }

        if (m_level_number >= 16 && m_level_number <= 30)
        {
            for (int i = 16; i <= 30; ++i)
            {
                star_count = cCubetraz::GetStarsNormal(i);
                if (star_count > 0)
                    number_of_solved_levels += 1.0f;
            }

            float percent = (number_of_solved_levels / 15.0f) * 100.0f;
            engine.ReportAchievement("com.almagems.cubetraz.normal02facesolved", percent);
        }

        if (m_level_number >= 31 && m_level_number <= 45)
        {
            for (int i = 31; i <= 45; ++i)
            {
                star_count = cCubetraz::GetStarsNormal(i);
                if (star_count > 0)
                    number_of_solved_levels += 1.0f;
            }

            float percent = (number_of_solved_levels / 15.0f) * 100.0f;
            engine.ReportAchievement("com.almagems.cubetraz.normal03facesolved", percent);
        }

        if (m_level_number >= 46 && m_level_number <= 60)
        {
            for (int i = 46; i <= 60; ++i)
            {
                star_count = cCubetraz::GetStarsNormal(i);
                if (star_count > 0)
                    number_of_solved_levels += 1.0f;
            }

            float percent = (number_of_solved_levels / 15.0f) * 100.0f;
            engine.ReportAchievement("com.almagems.cubetraz.normal04facesolved", percent);
        }
    }

    void cLevel::ReportAchievementHard()
    {
        float number_of_solved_levels = 0.0f;
        int star_count = 0;

        if (m_level_number >= 1 && m_level_number <= 15)
        {
            for (int i = 1; i <= 15; ++i)
            {
                star_count = cCubetraz::GetStarsHard(i);
                if (star_count > 0)
                    number_of_solved_levels += 1.0f;
            }

            float percent = (number_of_solved_levels / 15.0f) * 100.0f;
            engine.ReportAchievement("com.almagems.cubetraz.hard01facesolved", percent);
        }

        if (m_level_number >= 16 && m_level_number <= 30)
        {
            for (int i = 16; i <= 30; ++i)
            {
                star_count = cCubetraz::GetStarsHard(i);
                if (star_count > 0)
                    number_of_solved_levels += 1.0f;
            }

            float percent = (number_of_solved_levels / 15.0f) * 100.0f;
            engine.ReportAchievement("com.almagems.cubetraz.hard02facesolved", percent);
        }

        if (m_level_number >= 31 && m_level_number <= 45)
        {
            for (int i = 31; i <= 45; ++i)
            {
                star_count = cCubetraz::GetStarsHard(i);
                if (star_count > 0)
                    number_of_solved_levels += 1.0f;
            }

            float percent = (number_of_solved_levels / 15.0f) * 100.0f;
            engine.ReportAchievement("com.almagems.cubetraz.hard03facesolved", percent);
        }

        if (m_level_number >= 46 && m_level_number <= 60)
        {
            for (int i = 46; i <= 60; ++i)
            {
                star_count = cCubetraz::GetStarsHard(i);
                if (star_count > 0)
                    number_of_solved_levels += 1.0f;
            }

            float percent = (number_of_solved_levels / 15.0f) * 100.0f;
            engine.ReportAchievement("com.almagems.cubetraz.hard04facesolved", percent);
        }
    }
*/

    @Override
    public void init() {
        Game.dirty_alpha = 60f;

        m_moving_cube = null;
        m_mover_cube = null;
        m_dead_cube = null;

        LevelInitData lid = Game.level_init_data;

        // uncomment to load alternative level
        //Game.level_init_data.difficulty = DifficultyEnum.Easy;
        //Game.level_init_data.level_number = 6;

        m_tutor_index = 0;
        m_next_action = LevelNextActionEnum.NoNextAction;
        
        switch (lid.init_action) {
            case FullInit: {
                m_menu_cube_hilite = null;
                m_show_hint_2nd = false;

                m_hilite_timeout = 0.0f;

                m_difficulty = lid.difficulty;
                m_level_number = lid.level_number;

                setupMusic();

                CubePos zero = new CubePos(0, 0, 0);
                m_player_cube.init(zero);
                m_cube_pos_key = zero;

                m_lst_moving_cubes.clear();
                m_lst_mover_cubes.clear();
                m_lst_dead_cubes.clear();

                m_moves_counter = 0;
                m_lst_undo.clear();

                m_hud.set1stHint();
                m_hud.setTextMoves(m_moves_counter);
                m_hud.setTextUndo(m_lst_undo.size());

                mIsFingerDown = false;
                mIsSwipe = false;

                m_cube_rotation.degree = -45.0f;
                m_cube_rotation.axis = new Vector(0.0f, 1.0f, 0.0f);

                m_user_rotation.reset();

                m_camera_current.init(m_camera_level);

                m_list_cubes_level.clear();
                m_list_cubes_wall_y_minus.clear();
                m_list_cubes_wall_x_minus.clear();
                m_list_cubes_wall_z_minus.clear();
                m_list_cubes_edges.clear();
                m_list_cubes_base.clear();
                m_list_cubes_face.clear();
                m_list_cubes_hint.clear();

                m_hud.init();

                setupCubesForLevel();
                setupAppear();
            }
            break;

            case JustContinue: // take no action ?
                if (LevelStateEnum.SetupAnimToCompleted == m_state) {
                
                } else {
                    m_hud.setupAppear();
                }
                break;

            case ShowSolution:
                reset();
                m_state = LevelStateEnum.PrepareSolving;
                m_timeout = 3.5f;
                break;
        }

        graphics.setProjection3D();
        graphics.setModelViewMatrix3D(m_camera_current);

        float posLight[] = { m_pos_light.x, m_pos_light.y, m_pos_light.z, 1.0f };
        glLightfv(GL_LIGHT0, GL_POSITION, posLight, 0);

        glEnable(GL_LIGHT0);
    }

    public void getRatings() {
        String sign = "-";

        StatInitData sid = Game.stat_init_data;

        if (m_moves_counter < m_min_solution_steps) {
            sign = "<";
            sid.str_title = "EXPERT";
            sid.stars = 3;
        }

        if (m_moves_counter == m_min_solution_steps) {
            sign = "=";
            sid.str_title = "PERFECT";
            sid.stars = 3;
        }

        if (m_moves_counter > m_min_solution_steps && m_moves_counter <= m_min_solution_steps + 1) {
            sign = ">";
            sid.str_title = "EXCELLENT";
            sid.stars = 3;
        }

        if (m_moves_counter > m_min_solution_steps + 1 && m_moves_counter <= m_min_solution_steps + 4) {
            sign = ">";
            sid.str_title = "GREAT";
            sid.stars = 2;
        }

        if (m_moves_counter > m_min_solution_steps + 4) {
            sign = ">";
            sid.str_title = "GOOD";
            sid.stars = 1;
        }

        sid.str_moves = "PLAYER:" + m_moves_counter + " " + sign + " BEST:" + m_min_solution_steps;
    }
    
    public void showTutor(int index) {
        m_state = LevelStateEnum.Tutorial;
        m_hud.clearTutors();

        switch (index) {
            case Tutor_Swipe: m_hud.showTutorSwipeAndGoal(); break;
            case Tutor_Drag: m_hud.showTutorDrag(); break;
            case Tutor_Moving: m_hud.showTutorMoving(); break;
            case Tutor_Mover: m_hud.showTutorMover(); break;
            case Tutor_Dead: m_hud.showTutorDead(); break;
            case Tutor_Plain: m_hud.showTutorPlain(); break;
            case Tutor_MenuPause: m_hud.showTutorMenuPause(); break;
            case Tutor_MenuUndo: m_hud.showTutorMenuUndo(); break;
            case Tutor_MenuHint: m_hud.showTutorMenuHint(); break;
            case Tutor_MenuSolvers: m_hud.showTutorMenuSolvers(); break;
            default:
                break;
        }
    }
    
    public boolean isMovingCube(CubePos cube_pos, boolean set) {
        MovingCube movingCube;
        int size = m_lst_moving_cubes.size();
        for (int i = 0; i < size; ++i) {
            movingCube = m_lst_moving_cubes.get(i);
            CubePos cp = movingCube.getCubePos();

            if (cp.x == cube_pos.x && cp.y == cube_pos.y && cp.z == cube_pos.z) {
                if (set) {
                    m_player_cube.setMovingCube(movingCube);
                }
                return true;
            }
        }
        return false;
    }

    public boolean isMoverCube(CubePos cube_pos, boolean set) {
        MoverCube moverCube;
        int size = m_lst_mover_cubes.size();
        for(int i = 0; i < size; ++i) {
            moverCube = m_lst_mover_cubes.get(i);
            CubePos cp = moverCube.getCubePos();

            if (cp.x == cube_pos.x && cp.y == cube_pos.y && cp.z == cube_pos.z) {
                if (set) {
                    if (m_mover_cube != moverCube) { // bugfix! when player hits the mover cube from behind!
                        m_player_cube.setMoverCube(moverCube);
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean isDeadCube(CubePos cube_pos, boolean set) {
        DeadCube deadCube;
        CubePos cp;
        int len = m_lst_dead_cubes.size();        
        for(int i = 0; i < len; ++i) {
            deadCube = m_lst_dead_cubes.get(i);
            cp = deadCube.getCubePos();

            if (cp.x == cube_pos.x && cp.y == cube_pos.y && cp.z == cube_pos.z) {
                if (set) {
                    m_player_cube.setDeadCube(deadCube);
                }
                return true;
            }
        }
        return false;
    }

    public boolean isSpecCubeObstacle(CubePos cube_pos,
                                      MovingCube ignore_moving_cube,
                                      MoverCube ignore_mover_cube,
                                      DeadCube ignore_dead_cube) {
        CubePos cp;
        int size;
        if (!m_lst_dead_cubes.isEmpty()) {
            size = m_lst_dead_cubes.size();
            DeadCube deadCube;
            for(int i = 0; i < size; ++i) {
                deadCube = m_lst_dead_cubes.get(i);
                if (deadCube != ignore_dead_cube) {
                    cp = deadCube.getCubePos();
                    if (cp.x == cube_pos.x && cp.y == cube_pos.y && cp.z == cube_pos.z) {
                        return true;
                    }
                }
            }
        }

        if (!m_lst_mover_cubes.isEmpty()) {
            size = m_lst_mover_cubes.size();
            MoverCube moverCube;
            for(int i = 0; i < size; ++i) {
                moverCube = m_lst_mover_cubes.get(i);
                if (moverCube != ignore_mover_cube) {
                    cp = moverCube.getCubePos();
                    if (cp.x == cube_pos.x && cp.y == cube_pos.y && cp.z == cube_pos.z) {
                        return true;
                    }
                }
            }
        }

        if (!m_lst_moving_cubes.isEmpty()) {
            size = m_lst_moving_cubes.size();
            MovingCube movingCube;
            for(int i = 0; i < size; ++i) {
                movingCube = m_lst_moving_cubes.get(i);
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

    public void setupMusic() {
        switch (m_difficulty) {
            case Easy:
                if (m_level_number >= 1 && m_level_number <= 15) {
                    Game.playMusic(MUSIC_BREEZE);
                }

                if (m_level_number >= 16 && m_level_number <= 30) {
                    Game.playMusic(MUSIC_DRONES);
                }

                if (m_level_number >= 31 && m_level_number <= 45) {
                    Game.playMusic(MUSIC_WAVES);
                }

                if (m_level_number >= 46 && m_level_number <= 60) {
                    Game.playMusic(MUSIC_BREEZE);
                }
                break;

            case Normal:
                if (m_level_number >= 1 && m_level_number <= 15) {
                    Game.playMusic(MUSIC_DRONES);
                }

                if (m_level_number >= 16 && m_level_number <= 30) {
                    Game.playMusic(MUSIC_WAVES);
                }

                if (m_level_number >= 31 && m_level_number <= 45) {
                    Game.playMusic(MUSIC_BREEZE);
                }

                if (m_level_number >= 46 && m_level_number <= 60) {
                    Game.playMusic(MUSIC_DRONES);
                }
                break;

            case Hard:
                if (m_level_number >= 1 && m_level_number <= 15) {
                    Game.playMusic(MUSIC_WAVES);
                }

                if (m_level_number >= 16 && m_level_number <= 30) {
                    Game.playMusic(MUSIC_BREEZE);
                }

                if (m_level_number >= 31 && m_level_number <= 45) {
                    Game.playMusic(MUSIC_DRONES);
                }                    

                if (m_level_number >= 46 && m_level_number <= 60) {
                    Game.playMusic(MUSIC_WAVES);
                }
                break;
        }
    }

    public void setupAppear() {
        m_state = LevelStateEnum.Appear;
        m_appear_state = SubAppearStateEnum.SubAppearWait;
        m_draw_menu_cubes = false;
        m_draw_texts = false;
        m_timeout = 0.0f;
        m_alter_view = false;

        if (Utils.rand.nextBoolean()) {
            //printf("\nDownToTop");
            m_ad_level.setLevelAndDirection(0, 1);
        } else {
            //printf("\nTopToDown");
            m_ad_level.setLevelAndDirection(MAX_CUBE_COUNT - 1, -1);
        }
        m_cube_pos_key.reset();
    }

    public void setupCubesForLevel() {
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

                Game.cubes[x][7][z].setColor( Game.getBaseColor() );
                Game.cubes[x][1][z].setColor( Game.getBaseColor() );
            }
        }

        // wall x minus
        for (int z = 0; z < MAX_CUBE_COUNT; ++z) {
            for (int y = 0; y < MAX_CUBE_COUNT; ++y) {
                Game.cubes[1][y][z].type = CubeTypeEnum.CubeIsInvisibleAndObstacle;
                Game.cubes[1][y][z].setColor( Game.getBaseColor() );
            }
        }

        // wall z minus
        for (int x = 0; x < MAX_CUBE_COUNT; ++x) {
            for (int y = 0; y < MAX_CUBE_COUNT; ++y) {
                Game.cubes[x][y][1].type = CubeTypeEnum.CubeIsInvisibleAndObstacle;
                Game.cubes[x][y][1].setColor( Game.getBaseColor() );
            }
        }


        // wall y minus
        for (int z = 2; z < MAX_CUBE_COUNT; ++z) {
            for (int x = 2; x < MAX_CUBE_COUNT; ++x) {
                Game.cubes[x][1][z].type = CubeTypeEnum.CubeIsVisibleAndObstacle;
                Game.cubes[x][1][z].setColor( Game.getBaseColor() );

                m_list_cubes_wall_y_minus.add(Game.cubes[x][1][z]);
            }
        }

        // walls
        for (int y = 2; y < 7; ++y) {
            for (int x = 2; x < MAX_CUBE_COUNT; ++x) {
                Game.cubes[x][y][1].type = CubeTypeEnum.CubeIsVisibleAndObstacle;
                Game.cubes[x][y][1].setColor( Game.getBaseColor() );

                if (y > 0) {
                    m_list_cubes_wall_z_minus.add(Game.cubes[x][y][1]);
                }
            }

            for (int z = 2; z < MAX_CUBE_COUNT; ++z) {
                Game.cubes[1][y][z].type = CubeTypeEnum.CubeIsVisibleAndObstacle;
                Game.cubes[1][y][z].setColor( Game.getBaseColor() );

                if (y > 0) {
                    m_list_cubes_wall_x_minus.add(Game.cubes[1][y][z]);
                }
            }
        }

        // edges
        //engine.cubes[1][1][8].color_current = Color(0, 0, 255, 255);
        Color color = Game.getBaseColor(); //(255,255,0,255);

        for (int i = 1; i < MAX_CUBE_COUNT; ++i) {
            m_list_cubes_edges.add(Game.cubes[i][1][1]);
            Game.cubes[i][1][1].setColor(color);
        }

        for (int i = 1; i < MAX_CUBE_COUNT; ++i) {
            m_list_cubes_edges.add(Game.cubes[1][1][i]);
            Game.cubes[1][1][i].setColor(color);
        }

        for (int i = 1; i < 7; ++i) {
            m_list_cubes_edges.add(Game.cubes[1][i][1]);
            Game.cubes[1][i][1].setColor(color);
        }
    }

    public void setupAnimUndo() {
        m_anim_undo = true;
        m_anim_undo_alpha = 1.0f;
        m_anim_undo_scale = 0.75f;

        m_anim_undo_alpha -= 0.02f;
        m_anim_undo_scale += 0.04f;
    }

    public void setupDeadCube(DeadCube deadCube) {
        m_state = LevelStateEnum.DeadAnim;
        m_timeout = 1.5f;

        dead_size = 60.0f;
        dead_alpha = 0;
        dead_alpha_step = 4;

        m_dead_cube = deadCube;
        m_dead_cube.hiLite();
    }

    public void setupMoveCube(MovingCube movingCube) {
        m_state_to_restore = m_state;
        m_state = LevelStateEnum.MovingCube;
        m_timeout = 0.3f;

        m_moving_cube = movingCube;
        m_moving_cube.move();
    }

    public void setupMoverCube(MoverCube moverCube) {
        int movement = moverCube.getMoveDir();
        m_state_to_restore = m_state;
        m_state = LevelStateEnum.MovingPlayer;
        m_timeout = 0.3f;

        m_mover_cube = moverCube;
        m_mover_cube.hiLite();

        m_player_cube.moveOnAxis(movement);

        UndoData ud = m_lst_undo.get( m_lst_undo.size() - 1);
        ud.moving_cube = m_player_cube.m_moving_cube; //m_lst_moving_cubes.front();

        if (ud.moving_cube != null) {
            ud.moving_cube_move_dir = ud.moving_cube.getMovement();
            ud.moving_cube_pos = ud.moving_cube.getCubePos();
        }
    }
    
    public void setSolversCount() {
        m_hud.setTextSolver(Game.getSolverCount());
    }
    
    public void setAnimToCompleted() {
        m_state = LevelStateEnum.AnimToCompleted;

        m_draw_menu_cubes = true;
        m_draw_texts = true;

        m_list_cubes_base.clear();
        m_list_cubes_face.clear();

        m_ad_face.clear();
        m_ad_base.clear();

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
            cube.setColor( Game.getFaceColor(1f) );
            m_ad_face.addAppear(cube);
        }

        for (int i = 0; i < 7; ++i) {
            cube = Game.cubes[0][i][1];
            cube.type = CubeTypeEnum.CubeIsVisibleAndObstacle;
            cube.setColor( Game.getBaseColor() );
            m_ad_base.addAppear(cube);
        }

        for (int i = 0; i < MAX_CUBE_COUNT; ++i) {
            cube = Game.cubes[i][0][1];
            cube.type = CubeTypeEnum.CubeIsVisibleAndObstacle;
            cube.setColor( Game.getBaseColor() );
            m_ad_base.addAppear(cube);
        }

        cube = Game.cubes[7][0][0];
        cube.type = CubeTypeEnum.CubeIsVisibleAndObstacle;
        cube.setColor(Game.getFaceColor(1f));
        m_ad_face.addAppear(cube);

        cube = Game.cubes[7][2][0];
        cube.type = CubeTypeEnum.CubeIsVisibleAndObstacle;
        cube.setColor(Game.getFaceColor(1f));
        m_ad_face.addAppear(cube);

        cube = Game.cubes[7][4][0];
        cube.type = CubeTypeEnum.CubeIsVisibleAndObstacle;
        cube.setColor(Game.getFaceColor(1f));
        m_ad_face.addAppear(cube);

        cube = Game.cubes[7][6][0];
        cube.type = CubeTypeEnum.CubeIsVisibleAndObstacle;
        cube.setColor( Game.getFaceColor(1f) );
        m_ad_face.addAppear(cube);

        m_ad_face.setLevelAndDirection(0, 1);
        m_ad_base.setLevelAndDirection(0, 1);

        m_t = 0.0f;

        if (DifficultyEnum.Hard == m_difficulty && 60 == m_level_number) {
            m_completed_face_next_action = CompletedFaceNextActionEnum.Finish;
        } else {
            m_completed_face_next_action = CompletedFaceNextActionEnum.Next;
        }

        Creator.createTextsLevelCompletedFace(this, m_completed_face_next_action);

        CubeFont cubeFont;
        Color color = Game.getTextColorOnCubeFace();
        int size = m_list_fonts.size();
        for(int i = 0; i < size; ++i) {
            cubeFont = m_list_fonts.get(i);
            cubeFont.setColor(color);
        }

        color = new Color(20, 0, 0, 255);
        m_cubefont_up.setColor(color);
        m_cubefont_mid.setColor(color);
        m_cubefont_low.setColor(color);

        m_target_rotation_degree = m_cube_rotation.degree - 90.0f - 45.0f;

        m_interpolator.setup(m_cube_rotation.degree, m_target_rotation_degree, 5.0f);

        m_menu_cube_up.setCubePos(new CubePos(0, 5, 0));
        m_menu_cube_mid.setCubePos(new CubePos(0, 3, 0));
        m_menu_cube_low.setCubePos(new CubePos(0, 1, 0));

        CubePos offset = new CubePos(0,0,1);
        m_menu_cube_up.setHiliteOffset(offset);
        m_menu_cube_mid.setHiliteOffset(offset);
        m_menu_cube_low.setHiliteOffset(offset);

        m_menu_cube_up.moveOnAxis(AxisMovement_X_Plus);
        m_menu_cube_mid.moveOnAxis(AxisMovement_X_Plus);
        m_menu_cube_low.moveOnAxis(AxisMovement_X_Plus);
    }

    public void updateAnimToCompleted() {
        m_menu_cube_up.update();
        m_menu_cube_mid.update();
        m_menu_cube_low.update();

        Cube cube;

        cube = m_ad_base.getCubeFromAppearList();
        if (cube != null) {
            m_list_cubes_base.add(cube);
        }

        cube = m_ad_face.getCubeFromAppearList();
        if (cube != null) {
            m_list_cubes_face.add(cube);
        }

        m_t += 0.04f;
        if (m_t > 1f) {
            m_t = 1f;
        }

        Utils.lerpCamera(m_camera_level, m_camera_level_completed, m_t, m_camera_current);

        m_interpolator.interpolate();
        m_cube_rotation.degree = m_interpolator.getValue();

        float diff = Math.abs(m_target_rotation_degree) - Math.abs(m_interpolator.getValue());

        if (diff < 0.1f && m_ad_base.lst_appear.isEmpty() && m_ad_face.lst_appear.isEmpty() && (Math.abs(1.0f - m_t) < EPSILON)) {
            m_cube_rotation.degree = m_target_rotation_degree;
            m_state = LevelStateEnum.Completed;

            Color color = new Color(100, 0, 0, 230);

            m_cubefont_up.setColor(color);
            m_cubefont_mid.setColor(color);
            m_cubefont_low.setColor(color);

            m_show_hint_2nd = false;

            for (int i = 0; i < MAX_HINT_CUBES; ++i) {
                m_ar_hint_cubes[i] = null;
            }
        }
    }

    public void updateCompleted() {
        m_menu_cube_up.update();
        m_menu_cube_mid.update();
        m_menu_cube_low.update();

        m_cubefont_up.warmByFactor(60);
        m_cubefont_mid.warmByFactor(60);
        m_cubefont_low.warmByFactor(60);

        if (m_menu_cube_up.isDone()) { // next        
            if (0 == m_menu_cube_up.m_cube_pos.x) {
                switch (m_completed_face_next_action) {
                    case Next:
                        ++m_level_number;
                        if (m_level_number > 60) {
                            switch (m_difficulty) {
                                case Easy:
                                    m_difficulty = DifficultyEnum.Normal;
                                    m_level_number = 1;
                                    Game.stopMusic();
                                    setupMusic();
                                    break;

                                case Normal:
                                    m_difficulty = DifficultyEnum.Hard;
                                    m_level_number = 1;
                                    Game.stopMusic();
                                    setupMusic();
                                    break;

                                case Hard: // win!
                                    break;
                            }
                        } else {
                            if (m_level_number == 16 || m_level_number == 31 || m_level_number == 46) {
                                Game.stopMusic();
                                setupMusic();
                            }
                        }
                        setAnimFromCompleted();
                        break;

                    case Finish:
                        ++m_level_number;
                        setAnimFromCompleted();
                        break;
                }
            }
        }

        if (m_menu_cube_mid.isDone()) { // replay        
            if (m_menu_cube_mid.m_cube_pos.x == 0) {
                reset();
                setAnimFromCompleted();
            }
        }

        if (m_menu_cube_low.isDone()) { // quit        
            if (m_menu_cube_low.m_cube_pos.x == 0) {
                eventQuit();
            }
        }
    }

    public void setAnimFromCompleted() {
        m_state = LevelStateEnum.AnimFromCompleted;
        m_t = 0.0f;
        m_target_rotation_degree = -45.0f;

        m_interpolator.setup(m_cube_rotation.degree, m_target_rotation_degree, 6.0f);

        m_ad_base.setLevelAndDirection(MAX_CUBE_COUNT - 1, -1);
        m_ad_face.setLevelAndDirection(MAX_CUBE_COUNT - 1, -1);
    }

    public void updateAnimFromCompleted() {
        m_t += 0.04f;
        if (m_t > 1f) {
            m_t = 1f;
        }

        Utils.lerpCamera(m_camera_level_completed, m_camera_level, m_t, m_camera_current);

        Cube cube;

        cube = m_ad_base.getCubeFromDisappearList();
        if (cube != null) {
            m_list_cubes_base.remove(cube);
        }

        cube = m_ad_face.getCubeFromDisappearList();
        if (cube != null) {
            m_list_cubes_face.remove(cube);
        }

        m_interpolator.interpolate();
        m_cube_rotation.degree = m_interpolator.getValue();

        float diff = Math.abs(m_cube_rotation.degree) - Math.abs(m_target_rotation_degree);

        if (diff < 0.1f && ((1.0f - m_t) < EPSILON) && m_ad_face.lst_disappear.isEmpty() && m_ad_base.lst_disappear.isEmpty()) {
            m_cube_rotation.degree = m_target_rotation_degree;

            if (DifficultyEnum.Hard == m_difficulty && 61 == m_level_number) {
                Game.showScene(Scene_Outro);
            } else {
                setupAppear();
            }
        }
    }
    
    public void setAnimToPaused() {
        m_state = LevelStateEnum.AnimToPaused;

        m_draw_menu_cubes = true;
        m_draw_texts = true;

        m_target_rotation_degree = m_cube_rotation.degree + 90.0f + 45.0f;
        m_interpolator.setup(m_cube_rotation.degree, m_target_rotation_degree, 5.0f);
        m_t = 0.0f;

        Creator.createTextsLevelPausedFace(this);

        CubeFont cubeFont;
        Color color = Game.getTextColorOnCubeFace();
        int size = m_list_fonts.size();
        for(int i = 0; i < size; ++i) {
            cubeFont = m_list_fonts.get(i);
            cubeFont.setColor(color);
        }

        color = new Color(20, 0, 0, 20);
        m_cubefont_up.setColor(color);
        m_cubefont_mid.setColor(color);
        m_cubefont_low.setColor(color);

        m_list_cubes_base.clear();
        m_list_cubes_face.clear();

        m_ad_face.clear();
        m_ad_base.clear();

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
            cube.setColor(Game.getFaceColor(1f));
            m_ad_face.addAppear(cube);

            cube = Game.cubes[1][i][0];
            cube.type = CubeTypeEnum.CubeIsVisibleAndObstacle;
            cube.setColor( Game.getBaseColor() );
            m_ad_base.addAppear(cube);
        }

        for (int i = 0; i < MAX_CUBE_COUNT; ++i) {
            cube = Game.cubes[1][0][i];
            cube.type = CubeTypeEnum.CubeIsVisibleAndObstacle;
            cube.setColor( Game.getBaseColor() );
            m_ad_base.addAppear(cube);
        }

        cube = Game.cubes[0][0][1];
        cube.type = CubeTypeEnum.CubeIsVisibleAndObstacle;
        cube.setColor(Game.getFaceColor(1f));
        m_ad_face.addAppear(cube);

        cube = Game.cubes[0][2][1];
        cube.type = CubeTypeEnum.CubeIsVisibleAndObstacle;
        cube.setColor(Game.getFaceColor(1f));
        m_ad_face.addAppear(cube);

        cube = Game.cubes[0][4][1];
        cube.type = CubeTypeEnum.CubeIsVisibleAndObstacle;
        cube.setColor(Game.getFaceColor(1f));
        m_ad_face.addAppear(cube);

        cube = Game.cubes[0][6][1];
        cube.type = CubeTypeEnum.CubeIsVisibleAndObstacle;
        cube.setColor( Game.getFaceColor(1f) );
        m_ad_face.addAppear(cube);

        m_ad_face.setLevelAndDirection(0, 1);
        m_ad_base.setLevelAndDirection(0, 1);

        m_menu_cube_up.setCubePos(new CubePos(0, 5, 8));
        m_menu_cube_mid.setCubePos(new CubePos(0, 3, 8));
        m_menu_cube_low.setCubePos(new CubePos(0, 1, 8));

        CubePos offset = new CubePos(1, 0, 0);
        m_menu_cube_up.setHiliteOffset(offset);
        m_menu_cube_mid.setHiliteOffset(offset);
        m_menu_cube_low.setHiliteOffset(offset);

        m_menu_cube_up.moveOnAxis(AxisMovement_Z_Minus);
        m_menu_cube_mid.moveOnAxis(AxisMovement_Z_Minus);
        m_menu_cube_low.moveOnAxis(AxisMovement_Z_Minus);
    }

    public void updateAnimToPaused() {
        m_menu_cube_up.update();
        m_menu_cube_mid.update();
        m_menu_cube_low.update();

        Cube cube;

        cube = m_ad_base.getCubeFromAppearList();
        if (cube != null) {
            m_list_cubes_base.add(cube);
        }

        cube = m_ad_face.getCubeFromAppearList();
        if (cube != null) {
            m_list_cubes_face.add(cube);
        }

        m_t += 0.04f;
        if (m_t > 1f) {
            m_t = 1f;
        }

        Utils.lerpCamera(m_camera_level, m_camera_level_paused, m_t, m_camera_current);

        m_interpolator.interpolate();
        m_cube_rotation.degree = m_interpolator.getValue();

        float diff = Math.abs(m_interpolator.getValue()) - Math.abs(m_target_rotation_degree);
        diff = Math.abs(diff);

        if (diff < 0.1f && ((1.0f - m_t) < EPSILON_SMALL) && m_ad_face.lst_appear.isEmpty() && m_ad_base.lst_appear.isEmpty() ) {
            m_cube_rotation.degree = m_target_rotation_degree;
            m_state = LevelStateEnum.Paused;

            Color color = new Color(100, 0, 0, 230);
            m_cubefont_up.setColor(color);
            m_cubefont_mid.setColor(color);
            m_cubefont_low.setColor(color);

            m_show_hint_2nd = false;

            for (int i = 0; i < MAX_HINT_CUBES; ++i) {
                m_ar_hint_cubes[i] = null;
            }
        }
    }

    public void updatePaused() {
        m_menu_cube_up.update();
        m_menu_cube_mid.update();
        m_menu_cube_low.update();

        m_cubefont_up.warmByFactor(60);
        m_cubefont_mid.warmByFactor(60);
        m_cubefont_low.warmByFactor(60);

        if (m_menu_cube_up.isDone()) { // back
            if (m_menu_cube_up.m_cube_pos.z == 8) {
                setAnimFromPaused();
            }
        }

        if (m_menu_cube_mid.isDone()) { // reset
            if (m_menu_cube_mid.m_cube_pos.z == 8) {
                reset();
                setAnimFromPaused();
            }
        }

        if (m_menu_cube_low.isDone()) { // quit
            if (m_menu_cube_low.m_cube_pos.z == 8) {
                m_menu_cube_low.lst_cubes_to_hilite.clear();
                eventQuit();
            }
        }
    }

    public void setAnimFromPaused() {
        m_hud.setupAppear();
        m_target_rotation_degree = m_cube_rotation.degree - 90.0f - 45.0f;
        m_interpolator.setup(m_cube_rotation.degree, m_target_rotation_degree, 6.0f);
        m_t = 0.0f;
        m_state = LevelStateEnum.AnimFromPaused;

        m_ad_base.setLevelAndDirection(MAX_CUBE_COUNT - 1, -1);
        m_ad_face.setLevelAndDirection(MAX_CUBE_COUNT - 1, -1);
    }

    public void updateAnimFromPaused() {
        m_t += 0.04f;
        if (m_t > 1f) {
            m_t = 1f;
        }

        Utils.lerpCamera(m_camera_level_paused, m_camera_level, m_t, m_camera_current);

        Cube cube;

        cube = m_ad_base.getCubeFromDisappearList();
        if (cube != null) {
            m_list_cubes_base.remove(cube);
        }

        cube = m_ad_face.getCubeFromDisappearList();
        if (cube != null) {
            m_list_cubes_face.remove(cube);
        }

        m_interpolator.interpolate();
        m_cube_rotation.degree = m_interpolator.getValue();

        // 90 <. - 45
        float diff = Math.abs( Math.abs(m_target_rotation_degree) - Math.abs(m_interpolator.getValue()) );

        if (diff < 0.1f && ((1.0f - m_t) < EPSILON) && m_ad_face.lst_disappear.isEmpty() && m_ad_base.lst_disappear.isEmpty() ) {
            m_cube_rotation.degree = m_target_rotation_degree;
            m_state = LevelStateEnum.Playing;
            m_draw_menu_cubes = false;
            m_draw_texts = false;
        }
    }

    @Override
    public void update() {
        switch (m_state) {
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
                m_user_rotation.reset();
            } else {
                m_user_rotation.current.x = Utils.lerp(m_user_rotation.from.x, 0.0f, m_t);
                m_user_rotation.current.y = Utils.lerp(m_user_rotation.from.y, 0.0f, m_t);
            }
        }

        m_hud.update();

        if (LevelNextActionEnum.NoNextAction != m_next_action) {
            if (HUDStateEnum.DoneHUD == m_hud.getState() ) {
                switch (m_next_action) {
                    case ShowSceneSolvers:
                        Game.renderToFBO(this);
                        Game.showScene(Scene_Solvers);
                        m_next_action = LevelNextActionEnum.NoNextAction;
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
            m_hint_timeout -= 0.01f;

            if (m_hint_timeout < 0.0f) {
                m_hint_timeout = 0.15f;
                Cube cube = m_ar_hint_cubes[m_hint_index];
                if (cube != null) {
                    Color col = new Color(255, 0, 0, 255);

                    cube.color_current = col;
                    ++m_hint_index;

                    // locate cube among moving cubes
                    MovingCube movingCube;
                    int size = m_lst_moving_cubes.size();
                    for(int i = 0; i < size; ++i) {
                        movingCube = m_lst_moving_cubes.get(i);
                        CubePos cube_pos = new CubePos(cube.x, cube.y, cube.z);
                        CubePos moving_cube_pos = movingCube.getCubePos();

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

    public void warmCubes() {
        m_hilite_timeout -= 0.01f;

        if (m_hilite_timeout < 0.0f) {
            m_hilite_timeout = 0.05f;
            Color color = new Color(99, 99, 99, 255);

            if (!m_menu_cube_up.lst_cubes_to_hilite.isEmpty()) {
                Cube p = m_menu_cube_up.lst_cubes_to_hilite.get(0);
                m_menu_cube_up.lst_cubes_to_hilite.remove(p);

                p.color_current = color;
            }

            if (!m_menu_cube_mid.lst_cubes_to_hilite.isEmpty()) {
                Cube p = m_menu_cube_mid.lst_cubes_to_hilite.get(0);
                m_menu_cube_mid.lst_cubes_to_hilite.remove(p);

                p.color_current = color;
            }

            if (!m_menu_cube_low.lst_cubes_to_hilite.isEmpty()) {
                Cube p = m_menu_cube_low.lst_cubes_to_hilite.get(0);
                m_menu_cube_low.lst_cubes_to_hilite.remove(p);

                p.color_current = color;
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

        size = m_lst_moving_cubes.size();
        MovingCube movingCube;
        for (int i = 0; i < size; ++i) {
            movingCube = m_lst_moving_cubes.get(i);
            movingCube.warmByFactor(WARM_FACTOR);
        }
    }

    public void updateDeadAnim() {
        m_timeout -= 0.01f;
        dead_size += 4.0f;
        dead_alpha += dead_alpha_step;

        if (dead_alpha_step > 0) {
            if (dead_alpha > 130)
                dead_alpha_step = -3;
        } else {
            if (dead_alpha < 0) {
                reset();
                m_state = LevelStateEnum.Playing;
                m_dead_cube = null;
            }
        }
    }

    public void updateMovingCube() {
        m_timeout -= 0.01f;

        if (m_timeout < 0.0f) {
            m_moving_cube.update();

            if (m_moving_cube.isDone()) {
                m_state = m_state_to_restore;
                m_moving_cube = null;

                if (LevelStateEnum.Solving == m_state_to_restore) {
                    m_timeout = 1.0f;
                }
            }
        }
    }

    public void updateMovingPlayer() {
        m_timeout -= 0.01f;

        if (m_timeout < 0.0f) {
            m_player_cube.update();

            if (m_player_cube.isDone()) {
                if (m_mover_cube != null) {
                    m_mover_cube.noHiLite();
                    m_mover_cube = null;
                }

                if (m_moving_cube != null) {
                    m_moving_cube.noHiLite();
                    m_moving_cube = null;
                }

                if (m_dead_cube != null) {
                    m_dead_cube.noHilite();
                    m_dead_cube = null;
                }

                Game.playSound(SOUND_CUBE_HIT);

                m_state = m_state_to_restore;

                if (LevelStateEnum.Solving == m_state_to_restore) {
                    m_timeout = 1.0f;
                }

                CubePos player = m_player_cube.getCubePos();
                CubePos key = m_cube_pos_key;

                if (player.x == key.x && player.y == key.y && player.z == key.z) {
                    Game.levelComplete();
                } else {
                    if (m_player_cube.m_dead_cube != null) {
                        setupDeadCube(m_player_cube.m_dead_cube);
                    } else if (m_player_cube.m_moving_cube != null) {
                        setupMoveCube(m_player_cube.m_moving_cube);
                    } else if (m_player_cube.m_mover_cube != null) {
                        setupMoverCube(m_player_cube.m_mover_cube);
                    }
                }
            }
        }
    }

    public void updateSolving() {
        m_timeout -= 0.01f;

        if (m_timeout <= 0.0f) {
            m_timeout = 1.0f;

            if (m_player_cube.isDone()) {
                int dir = m_ar_solution[m_solution_pointer++];
                boolean success = m_player_cube.moveOnAxis(dir);

                if (success) {
                    m_state_to_restore = m_state;
                    m_state = LevelStateEnum.MovingPlayer;
                    m_timeout = 0.0f;

                    ++m_moves_counter;
                    m_hud.setTextMoves(m_moves_counter);
                }
            }
        }

        m_player_cube.update();
    }

    public void updatePrepareSolving() {
        m_hud.update();

        m_timeout -= 0.01f;

        if (m_timeout < 2.5f) {
            m_hud.showPrepareSolving(true, m_min_solution_steps);
        }

        if (m_timeout <= 0.0f) {
            m_hud.showPrepareSolving(false, m_min_solution_steps);

            m_state = LevelStateEnum.Solving;
            m_solution_pointer = 0;
            m_fade_value = 1.0f;
            m_timeout = 1.0f;
            m_hud.setupAppear();
        }
    }

    public void updateAppear() {
        m_timeout += 0.01f;

        switch (m_appear_state) {
            case SubAppearWait: 
                if (m_timeout > 0.2f) {
                    Cube cube;

                    cube = m_ad_level.getCubeFromDisappearList();
                    if (cube != null) {
                        cube.type = CubeTypeEnum.CubeIsInvisible;
                        m_list_cubes_level.remove(cube);
                    }

                    cube = m_ad_level.getCubeFromDisappearList();
                    if (cube != null) {
                        cube.type = CubeTypeEnum.CubeIsInvisible;
                        m_list_cubes_level.remove(cube);
                    }

                    if (!m_lst_moving_cubes.isEmpty()) {
                        MovingCube movingCube = m_lst_moving_cubes.get(0);
                        m_lst_moving_cubes.remove(movingCube);
                        LevelBuilder.lst_moving_cubes.add(movingCube);
                    }

                    if (!m_lst_mover_cubes.isEmpty()) {
                        MoverCube moverCube = m_lst_mover_cubes.get(0);
                        m_lst_mover_cubes.remove(moverCube);
                        LevelBuilder.lst_mover_cubes.add(moverCube);
                    }

                    if (!m_lst_dead_cubes.isEmpty()) {
                        DeadCube deadCube = m_lst_dead_cubes.get(0);
                        m_lst_dead_cubes.remove(deadCube);
                        LevelBuilder.lst_dead_cubes.add(deadCube);
                    }

                    if (m_list_cubes_level.isEmpty() &&
                            m_lst_moving_cubes.isEmpty() &&
                            m_lst_mover_cubes.isEmpty() &&
                            m_lst_dead_cubes.isEmpty()) {
                        m_timeout = 0.0f;
                        m_appear_state = SubAppearStateEnum.SubAppearKeyAndPlayer;
                        m_t = 0.0f;
                        m_fade_value = 0.0f;

                        eventBuildLevel();
                        reset();
                    }
                }
                break;

            case SubAppearKeyAndPlayer:
                if (m_timeout > 0.1f) {
                    m_t += 0.1;

                    if (m_t > 1.0f) {
                        m_timeout = 0.0f;
                        m_t = 1.0f;
                        m_appear_state = SubAppearStateEnum.SubAppearWaitAgain;
                    }

                    m_fade_value = Utils.lerp(0.0f, 1.0f, m_t);
                }
                break;

            case SubAppearWaitAgain:
                if (m_timeout > 0.2) {
                    m_appear_state = SubAppearStateEnum.SubAppearLevel;
                }
                break;

            case SubAppearLevel:
                if (m_ad_level.lst_appear.isEmpty() &&
                    LevelBuilder.lst_moving_cubes.isEmpty() &&
                    LevelBuilder.lst_mover_cubes.isEmpty() &&
                    LevelBuilder.lst_dead_cubes.isEmpty()) {
                    m_state = LevelStateEnum.Playing;

                if (DifficultyEnum.Easy == m_difficulty) {
                    if (1 == m_level_number) {
                        showTutor(Tutor_Swipe);
                    }

                    if (2 == m_level_number) {
                        showTutor(Tutor_MenuPause);
                    }

                    if (3 == m_level_number) {
                        showTutor(Tutor_MenuHint);
                    }

                    if (4 == m_level_number) {
                        showTutor(Tutor_Drag);
                    }

                    if (5 == m_level_number) {
                        showTutor(Tutor_Plain);
                    }

                    if (10 == m_level_number) {
                        showTutor(Tutor_Moving);
                    }

                    if (12 == m_level_number) {
                        showTutor(Tutor_Mover);
                    }

                    if (19 == m_level_number) {
                        showTutor(Tutor_Dead);
                    }
                }                
            } else {
                if (m_timeout > 0.02f) {
                    Cube cube = m_ad_level.getCubeFromAppearList();

                    if (cube != null) {
                        m_list_cubes_level.add(cube);
                    }

                    if (!LevelBuilder.lst_moving_cubes.isEmpty()) {
                        MovingCube movingCube = LevelBuilder.lst_moving_cubes.get(0);
                        LevelBuilder.lst_moving_cubes.remove(movingCube);
                        m_lst_moving_cubes.add(movingCube);
                    }

                    if (!LevelBuilder.lst_mover_cubes.isEmpty()) {
                        MoverCube moverCube = LevelBuilder.lst_mover_cubes.get(0);
                        LevelBuilder.lst_mover_cubes.remove(moverCube);
                        m_lst_mover_cubes.add(moverCube);
                    }

                    if (!LevelBuilder.lst_dead_cubes.isEmpty()) {
                        DeadCube deadCube = LevelBuilder.lst_dead_cubes.get(0);
                        LevelBuilder.lst_dead_cubes.remove(deadCube);
                        m_lst_dead_cubes.add(deadCube);
                    }

                    m_timeout = 0.0f;
                }
            }
            break;

            default:
                break;
        } // switch
    }

    public void updateTutorial() {
        if (!m_hud.isTutorDisplaying()) {
            m_state = LevelStateEnum.Playing;
        }
    }

    public void updatePlaying() {
        m_player_cube.update();
    }

    public void updateUndo() {
        m_timeout_undo -= 0.01f;

        if (m_timeout_undo <= 0.0f) {
            if (m_player_cube.isDone()) {
//            if (!m_lst_undo.empty())
//            {
//                UndoData ud = m_lst_undo.back();
//
//                if (ud.mover_cube)
//                {
//                    EventUndo();
//                    return;
//                }
//            }
                m_state = LevelStateEnum.Playing;
            }
            m_timeout_undo = UNDO_TIMEOUT;
        }
    }

    private boolean isPlayerAndKeyInSamePosition() {
        if (m_cube_pos_key.x == m_player_cube.m_cube_pos.x &&
            m_cube_pos_key.y == m_player_cube.m_cube_pos.y &&
            m_cube_pos_key.z == m_player_cube.m_cube_pos.z) {
            return true;
        } else{
            return false;
        }
    }

    public void drawTheCube() {
        glClear(GL_STENCIL_BUFFER_BIT);

        Cube cube;

        glEnable(GL_LIGHTING);

        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_gray_concrete);

//        Vector light = m_pos_light;
//        Vector light_tmp = new Vector();
        Vector light = Utils.rotate3D_AroundYAxis(m_pos_light.x, m_pos_light.y, m_pos_light.z, -m_cube_rotation.degree - m_user_rotation.current.y);
        Vector light_tmp = Utils.rotate3D_AroundXAxis(light.x, light.y, light.z, -m_user_rotation.current.x);
        light.init(light_tmp);

        glEnable(GL_STENCIL_TEST);
        glDisable(GL_CULL_FACE);

        graphics.bindStreamSources3d();

        int size;
        float[] shadowMat = new float[16];

//    if (false)
        for (int j = 0; j < 3; ++j) { // 3 times (1 floor + 2 walls)
            graphics.resetBufferIndices();

            switch (j) {
                case 0: // floor
                    Utils.calcShadowMatrixFloor(light, shadowMat, 0.0f);
                    size = m_list_cubes_wall_y_minus.size();
                    for(int i = 0; i < size; ++i) {
                        cube = m_list_cubes_wall_y_minus.get(i);
                        graphics.addCubeFace_Y_Plus(cube.tx, cube.ty, cube.tz, cube.color_current);
                    }
                    break;

                case 1: // wall x
                    Utils.calcShadowMatrixWallX(light, shadowMat, 0.0f);
                    size = m_list_cubes_wall_x_minus.size();
                    for(int i = 0; i < size; ++i) {
                        cube = m_list_cubes_wall_x_minus.get(i);
                        graphics.addCubeFace_X_Plus(cube.tx, cube.ty, cube.tz, cube.color_current);
                    }
                    break;

                case 2: // wall z
                    Utils.calcShadowMatrixWallZ(light, shadowMat, 0.0f);
                    size = m_list_cubes_wall_z_minus.size();
                    for(int i = 0; i < size; ++i) {
                        cube = m_list_cubes_wall_z_minus.get(i);
                        graphics.addCubeFace_Z_Plus(cube.tx, cube.ty, cube.tz, cube.color_current);
                    }
                    break;
            } // switch

            glClear(GL_STENCIL_BUFFER_BIT);
            glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
            glStencilFunc(GL_ALWAYS, 0xff, 0xff);

            graphics.disableBlending();
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
            if (!m_lst_moving_cubes.isEmpty()) {
                MovingCube movingCube;
                size = m_lst_moving_cubes.size();
                for(int i = 0; i < size; ++i) {
                    movingCube = m_lst_moving_cubes.get(i);
                    graphics.addCubeSize(movingCube.pos.x + Game.cube_offset.x, movingCube.pos.y + Game.cube_offset.y, movingCube.pos.z + Game.cube_offset.z, HALF_CUBE_SIZE, shadow_color);
                }
            }

            // mover cubes
            if (!m_lst_mover_cubes.isEmpty()) {
                MoverCube moverCube;
                size = m_lst_mover_cubes.size();
                for(int i = 0; i < size; ++i) {
                    moverCube = m_lst_mover_cubes.get(i);
                    graphics.addCubeSize(moverCube.pos.x + Game.cube_offset.x, moverCube.pos.y + Game.cube_offset.y, moverCube.pos.z + Game.cube_offset.z, HALF_CUBE_SIZE, shadow_color);
                }
            }

            // dead cubes
            if (!m_lst_dead_cubes.isEmpty()) {
                DeadCube deadCube;
                size = m_lst_dead_cubes.size();
                for(int i = 0; i < size; ++i) {
                    deadCube = m_lst_dead_cubes.get(i);
                    graphics.addCubeSize(deadCube.pos.x + Game.cube_offset.x, deadCube.pos.y + Game.cube_offset.y, deadCube.pos.z + Game.cube_offset.z, HALF_CUBE_SIZE, shadow_color);
                }
            }

            // with player cube
            if (m_player_cube.m_cube_pos.x != 0) {
                graphics.addCubeSize(m_player_cube.pos.x + Game.cube_offset.x, m_player_cube.pos.y + Game.cube_offset.y, m_player_cube.pos.z + Game.cube_offset.z, HALF_CUBE_SIZE, shadow_color);
            }

            // with key cube
            if (m_cube_pos_key.x != 0 && !isPlayerAndKeyInSamePosition())
            {
                graphics.addCubeSize(Game.cubes[m_cube_pos_key.x][m_cube_pos_key.y][m_cube_pos_key.z].tx + Game.cube_offset.x,
                                     Game.cubes[m_cube_pos_key.x][m_cube_pos_key.y][m_cube_pos_key.z].ty + Game.cube_offset.y,
                                     Game.cubes[m_cube_pos_key.x][m_cube_pos_key.y][m_cube_pos_key.z].tz + Game.cube_offset.z, HALF_CUBE_SIZE, shadow_color);
            }

            graphics.enableBlending();
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
        graphics.disableBlending();
        glEnable(GL_LIGHTING);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);

        // draw rest of the floor and walls (not shadow receivers)
        graphics.resetBufferIndices();

        size = m_list_cubes_wall_y_minus.size();
        for (int i = 0; i < size; ++i) {
            cube = m_list_cubes_wall_y_minus.get(i);
            graphics.addCubeFace_X_Plus(cube.tx, cube.ty, cube.tz, cube.color_current);
            graphics.addCubeFace_X_Minus(cube.tx, cube.ty, cube.tz, cube.color_current);
            graphics.addCubeFace_Z_Plus(cube.tx, cube.ty, cube.tz, cube.color_current);
            graphics.addCubeFace_Z_Minus(cube.tx, cube.ty, cube.tz, cube.color_current);
            graphics.addCubeFace_Y_Minus(cube.tx, cube.ty, cube.tz, cube.color_current);
        }

        size = m_list_cubes_wall_x_minus.size();
        for (int i = 0; i < size; ++i) {
            cube = m_list_cubes_wall_x_minus.get(i);
            graphics.addCubeFace_X_Minus(cube.tx, cube.ty, cube.tz, cube.color_current);
            graphics.addCubeFace_Y_Plus(cube.tx, cube.ty, cube.tz, cube.color_current);
            graphics.addCubeFace_Z_Plus(cube.tx, cube.ty, cube.tz, cube.color_current);
            graphics.addCubeFace_Z_Minus(cube.tx, cube.ty, cube.tz, cube.color_current);
        }

        size = m_list_cubes_wall_z_minus.size();
        for (int i = 0; i < size; ++i) {
            cube = m_list_cubes_wall_z_minus.get(i);
            graphics.addCubeFace_X_Plus(cube.tx, cube.ty, cube.tz, cube.color_current);
            graphics.addCubeFace_X_Minus(cube.tx, cube.ty, cube.tz, cube.color_current);
            graphics.addCubeFace_Y_Plus(cube.tx, cube.ty, cube.tz, cube.color_current);
            graphics.addCubeFace_Z_Minus(cube.tx, cube.ty, cube.tz, cube.color_current);
        }

        // draw edges
        size = m_list_cubes_edges.size();
        for(int i = 0; i < size; ++i) {
            cube = m_list_cubes_edges.get(i);
            graphics.addCubeSize(cube.tx, cube.ty, cube.tz, HALF_CUBE_SIZE, cube.color_current);
        }

        // level cubes
        size = m_list_cubes_level.size();
        for(int i = 0; i < size; ++i) {
            cube = m_list_cubes_level.get(i);
            graphics.addCubeSize(cube.tx, cube.ty, cube.tz, HALF_CUBE_SIZE, cube.color_current);
        }

        MovingCube movingCube;
        size = m_lst_moving_cubes.size();
        for(int i = 0; i < size; ++i) {
            movingCube = m_lst_moving_cubes.get(i);
            movingCube.renderCube();
        }

        MoverCube moverCube;
        size = m_lst_mover_cubes.size();
        for(int i = 0; i < size; ++i) {
            moverCube = m_lst_mover_cubes.get(i);
            moverCube.renderCube();
        }

        DeadCube deadCube;
        size = m_lst_dead_cubes.size();
        for(int i = 0; i < size; ++i) {
            deadCube = m_lst_dead_cubes.get(i);
            deadCube.renderCube();
        }

        if (LevelStateEnum.Playing != m_state) {
            size = m_list_cubes_face.size();
            for(int i = 0; i < size; ++i) {
                cube = m_list_cubes_face.get(i);
                graphics.addCubeSize(cube.tx, cube.ty, cube.tz, HALF_CUBE_SIZE, cube.color_current);
            }

            size = m_list_cubes_base.size();
            for(int i = 0; i < size; ++i) {
                cube = m_list_cubes_base.get(i);
                graphics.addCubeSize(cube.tx, cube.ty, cube.tz, HALF_CUBE_SIZE, cube.color_current);
            }
        }

        graphics.updateBuffers();
        graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);

        glPopMatrix();
    }

    public void drawTextsCompleted() {
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

            graphics.addCubeFace_Z_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.color_current);
        }

        if (m_menu_cube_up.isDone() && m_menu_cube_up.m_cube_pos.x == 7) {
            cubeFont = m_cubefont_up;
            pFont = cubeFont.getFont();

            coords.tx0 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);
            coords.tx1 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
            coords.tx2 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
            coords.tx3 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);

            graphics.addCubeFace_Z_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.color_current);
        }

        if (m_menu_cube_mid.isDone() && m_menu_cube_mid.m_cube_pos.x == 7) {
            cubeFont = m_cubefont_mid;
            pFont = cubeFont.getFont();

            coords.tx0 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);
            coords.tx1 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
            coords.tx2 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
            coords.tx3 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);

            graphics.addCubeFace_Z_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.color_current);
        }

        if (m_menu_cube_low.isDone() && m_menu_cube_low.m_cube_pos.x == 7) {
            cubeFont = m_cubefont_low;
            pFont = cubeFont.getFont();

            coords.tx0 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);
            coords.tx1 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
            coords.tx2 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
            coords.tx3 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);

            graphics.addCubeFace_Z_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.color_current);
        }

        graphics.updateBuffers();
        graphics.renderTriangles();
    }

    public void drawTextsPaused() {
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

            graphics.addCubeFace_X_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.color_current);
        }

        if (m_menu_cube_up.isDone() && m_menu_cube_up.m_cube_pos.z == 1) {
            cubeFont = m_cubefont_up;
            pFont = cubeFont.getFont();

            coords.tx0 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
            coords.tx1 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
            coords.tx2 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);
            coords.tx3 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);

            graphics.addCubeFace_X_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.color_current);
        }

        if (m_menu_cube_mid.isDone() && m_menu_cube_mid.m_cube_pos.z == 1) {
            cubeFont = m_cubefont_mid;
            pFont = cubeFont.getFont();

            coords.tx0 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
            coords.tx1 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
            coords.tx2 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);
            coords.tx3 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);

            graphics.addCubeFace_X_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.color_current);
        }

        if (m_menu_cube_low.isDone() && m_menu_cube_low.m_cube_pos.z == 1) {
            cubeFont = m_cubefont_low;
            pFont = cubeFont.getFont();

            coords.tx0 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
            coords.tx1 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
            coords.tx2 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);
            coords.tx3 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);

            graphics.addCubeFace_X_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.color_current);
        }
        graphics.updateBuffers();
        graphics.renderTriangles();
    }

    public void draw() {
        Color color = new Color(255, 255, 255, (int)(m_fade_value * 255));
        graphics.bindStreamSources3d();

        if (m_fade_value < 1.0f) {
            glEnable(GL_BLEND);
            glDisable(GL_DEPTH_TEST);
        }

        glEnable(GL_LIGHTING);

        if (m_draw_menu_cubes || 0 != m_player_cube.m_cube_pos.x) {
            glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_player);

            graphics.resetBufferIndices();

            if (0 != m_player_cube.m_cube_pos.x) {
                graphics.addCubeSize(m_player_cube.pos.x, m_player_cube.pos.y, m_player_cube.pos.z, HALF_CUBE_SIZE, color);
            }

            if (m_draw_menu_cubes) {
                graphics.addCube(m_menu_cube_up.pos.x, m_menu_cube_up.pos.y, m_menu_cube_up.pos.z);
                graphics.addCube(m_menu_cube_mid.pos.x, m_menu_cube_mid.pos.y, m_menu_cube_mid.pos.z);
                graphics.addCube(m_menu_cube_low.pos.x, m_menu_cube_low.pos.y, m_menu_cube_low.pos.z);
            }
            graphics.updateBuffers();
            graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);
        }

        if (0 != m_cube_pos_key.x && !isPlayerAndKeyInSamePosition()) {
            glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_key);

            Cube pCube = Game.cubes[m_cube_pos_key.x][m_cube_pos_key.y][m_cube_pos_key.z];

            graphics.resetBufferIndices();
            graphics.addCubeSize(pCube.tx, pCube.ty, pCube.tz, HALF_CUBE_SIZE * 0.98f, color);
            graphics.updateBuffers();
            graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);
        }

        if (m_fade_value < 1.0f) {
            glDisable(GL_BLEND);
            glEnable(GL_DEPTH_TEST);
        }

        if (!m_lst_moving_cubes.isEmpty() || !m_lst_mover_cubes.isEmpty() || !m_lst_dead_cubes.isEmpty()) {
            int size;
            MovingCube movingCube;
            MoverCube moverCube;
            DeadCube deadCube;

            graphics.resetBufferIndices();

            size = m_lst_moving_cubes.size();
            for(int i = 0; i < size; ++i) {
                movingCube = m_lst_moving_cubes.get(i);
                movingCube.renderSymbols();
            }

            size = m_lst_mover_cubes.size();
            for(int i = 0; i < size; ++i) {
                moverCube = m_lst_mover_cubes.get(i);
                moverCube.renderSymbols();
            }

            size = m_lst_dead_cubes.size();
            for(int i = 0; i < size; ++i) {
                deadCube = m_lst_dead_cubes.get(i);
                deadCube.renderSymbols();
            }

            glDisable(GL_LIGHTING);
            glEnable(GL_BLEND);
            graphics.bindStreamSources3d();
            glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_symbols);
            graphics.updateBuffers();
            graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);
        }

        if (m_menu_cube_hilite != null) {
            glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_symbols);
            color.init( new Color((int)color.R, (int)color.G, (int)color.B, (int)(m_hilite_alpha * 255)));
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

            switch (m_state) {
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
            glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_fonts);

            glEnable(GL_BLEND);
            glDisable(GL_LIGHTING);
            glDisableClientState(GL_NORMAL_ARRAY);

            if (m_state == LevelStateEnum.AnimToPaused || m_state == LevelStateEnum.AnimFromPaused || m_state == LevelStateEnum.Paused) {
                drawTextsPaused();
            } else {
                drawTextsCompleted();
            }
            glPopMatrix();
        }
    }

    @Override
    public void render() {
//        if (m_state == LevelStateEnum.Completed) {
//            renderForPicking(PickRenderTypeEnum.RenderOnlyMovingCubes);
//            if (true) {
//                return;
//            }
//        }

        graphics.setProjection2D();
        graphics.setModelViewMatrix2D();

        glEnable(GL_BLEND);
        glDisable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);
        glDepthMask(false); //GL_FALSE);

        Color color = new Color(255, 255, 255, (int)Game.dirty_alpha);
        graphics.drawFullScreenTexture(Graphics.texture_id_dirty, color);

        glDepthMask(true); //GL_TRUE);

        graphics.setProjection3D();
        graphics.setModelViewMatrix3D(m_camera_current);
        graphics.bindStreamSources3d();

        if (false) { //#ifdef DRAW_AXES_GLOBAL
            glDisable(GL_TEXTURE_2D);
            graphics.drawAxes();
            glEnable(GL_TEXTURE_2D);
        } //#endif

        float posLight[] = { m_pos_light.x, m_pos_light.y, m_pos_light.z, 1.0f };
        glLightfv(GL_LIGHT0, GL_POSITION, posLight, 0);

        glEnable(GL_LIGHTING);

        glPushMatrix();
        glRotatef(m_user_rotation.current.x, 1.0f, 0.0f, 0.0f);
        glRotatef(m_user_rotation.current.y, 0.0f, 1.0f, 0.0f);

        glPushMatrix();
        glRotatef(m_cube_rotation.degree, m_cube_rotation.axis.x, m_cube_rotation.axis.y, m_cube_rotation.axis.z);

        drawTheCube();

        if (false) {//#ifdef DRAW_AXES_CUBE
            glDisable(GL_TEXTURE_2D);
            glDisable(GL_LIGHTING);
            graphics.drawAxes();
            glEnable(GL_LIGHTING);
            glEnable(GL_TEXTURE_2D);
        } // #endif

        draw();

        glPopMatrix();

        glPopMatrix();

        m_hud.render();
    }

    public void renderForPicking(PickRenderTypeEnum type) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glDisable(GL_LIGHTING);
        graphics.disableBlending();
        glEnable(GL_DEPTH_TEST);

        glDisable(GL_TEXTURE_2D);

        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);

        graphics.setProjection3D();
        graphics.setModelViewMatrix3D(m_camera_current);

        glPushMatrix();

        switch (type) {
            case RenderOnlyHUD:
                m_hud.renderForPicking();
                break;

            case RenderOnlyMovingCubes:
                graphics.resetBufferIndices();
                graphics.bindStreamSources3d();

                glRotatef(m_cube_rotation.degree, m_cube_rotation.axis.x, m_cube_rotation.axis.y, m_cube_rotation.axis.z);

                graphics.addCubeSize(m_menu_cube_up.pos.x, m_menu_cube_up.pos.y, m_menu_cube_up.pos.z, HALF_CUBE_SIZE * 1.5f, m_menu_cube_up.color);
                graphics.addCubeSize(m_menu_cube_mid.pos.x, m_menu_cube_mid.pos.y, m_menu_cube_mid.pos.z, HALF_CUBE_SIZE * 1.5f, m_menu_cube_mid.color);
                graphics.addCubeSize(m_menu_cube_low.pos.x, m_menu_cube_low.pos.y, m_menu_cube_low.pos.z, HALF_CUBE_SIZE * 1.5f, m_menu_cube_low.color);

                graphics.updateBuffers();
                graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);
                break;

            default:
                break;
        } // switch

        glPopMatrix();
    }

    public void renderToFBO() {
        //return Render();

        graphics.setProjection2D();
        graphics.setModelViewMatrix2D();

        glEnable(GL_BLEND);
        glDisable(GL_LIGHTING);
        glDepthMask(false); //GL_FALSE);
        glEnable(GL_TEXTURE_2D);

        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);

        Color color_dirty = new Color(255, 255, 255, Game.dirty_alpha);
        graphics.drawFullScreenTexture(Graphics.texture_id_dirty, color_dirty);

        glDepthMask(true); //GL_TRUE);

        graphics.setProjection3D();
        graphics.setModelViewMatrix3D(m_camera_current);

        glEnable(GL_LIGHTING);
        //glEnable(GL_LIGHT0);

        float posLight[] = { m_pos_light.x, m_pos_light.y, m_pos_light.z, 1.0f };
        glLightfv(GL_LIGHT0, GL_POSITION, posLight, 0);

        glRotatef(m_cube_rotation.degree, m_cube_rotation.axis.x, m_cube_rotation.axis.y, m_cube_rotation.axis.z);
        drawTheCube();

        graphics.enableBlending();

        if (!m_lst_moving_cubes.isEmpty() || !m_lst_mover_cubes.isEmpty() || !m_lst_dead_cubes.isEmpty()) {
            int size;
            MovingCube movingCube;
            MoverCube moverCube;
            DeadCube deadCube;

            graphics.resetBufferIndices();

            size = m_lst_moving_cubes.size();
            for(int i = 0; i < size; ++i) {
                movingCube = m_lst_moving_cubes.get(i);
                movingCube.renderSymbols();
            }

            size = m_lst_mover_cubes.size();
            for(int i = 0; i < size; ++i) {
                moverCube = m_lst_mover_cubes.get(i);
                moverCube.renderSymbols();
            }

            size = m_lst_dead_cubes.size();
            for(int i = 0; i < size; ++i) {
                deadCube = m_lst_dead_cubes.get(i);
                deadCube.renderSymbols();
            }

            glDisable(GL_LIGHTING);
            glEnable(GL_BLEND);
            graphics.bindStreamSources3d();
            glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_symbols);
            graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);
        }

        graphics.bindStreamSources3d();
        glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_key);

        Color color = new Color(255, 255, 255, 255);

        if (0 != m_cube_pos_key.x) {
            glEnable(GL_LIGHTING);
            glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_key);

            Cube cube = Game.cubes[m_cube_pos_key.x][m_cube_pos_key.y][m_cube_pos_key.z];

            graphics.resetBufferIndices();
            graphics.addCubeSize(cube.tx, cube.ty, cube.tz, HALF_CUBE_SIZE * 0.99f, color);
            graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);

            glDisable(GL_LIGHTING);
        }

        graphics.disableBlending();

        // draw player cube
        graphics.resetBufferIndices();
        //graphics.SetStreamSource();
        glEnable(GL_LIGHTING);
        //Color color(255, 255, 255, 255);
        glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_player);
        graphics.addCubeSize(m_player_cube.pos.x, m_player_cube.pos.y, m_player_cube.pos.z, HALF_CUBE_SIZE, color);
        graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);

        glDisable(GL_LIGHTING);
    }

    public void eventBuildLevel() {

        switch (m_difficulty) {
            case Easy: LevelBuilderEasy.build(m_level_number); break;
            case Normal: LevelBuilderNormal.build(m_level_number); break; 
            case Hard: LevelBuilderHard.build(m_level_number); break;
        }

        m_cube_pos_key = LevelBuilder.key;

        m_player_cube.setCubePos(LevelBuilder.player);
        m_player_cube.setKeyCubePos(m_cube_pos_key);

        if (Utils.rand.nextBoolean()) {
            m_ad_level.setLevelAndDirection(0, 1);
        } else {
            m_ad_level.setLevelAndDirection(MAX_CUBE_COUNT - 1, -1);
        }

        String str;

        m_hud.set1stHint();

        // level
        str = Game.getLevelTypeAndNumberString(m_difficulty, m_level_number);
        m_hud.setTextLevel(str);

        // stars
        m_hud.setTextStars(Cubetraz.getStarCount());

        // moves
        m_hud.setTextMoves(0);

        // motto
        str = Creator.getLevelMotto(m_difficulty, m_level_number);
        m_hud.setTextMotto(str);

        m_hud.setupAppear();
    }

    public void eventShowHint() {
        if (0 == m_moves_counter) {
            m_hud.showHint(m_ar_solution[0]); // show first hint
        } else {
            m_show_hint_2nd = true;
            m_hint_index = 0;
            m_hint_timeout = 0.0f;
            
            for (int i = 0; i < MAX_HINT_CUBES; ++i) {
                m_ar_hint_cubes[i] = null;
            }

            int size = m_list_cubes_hint.size();
            Cube cube;
            for(int i = 0; i < size; ++i) {
                cube = m_list_cubes_hint.get(i);
                m_ar_hint_cubes[i++] = cube;
            }
        }
    }

    public void eventSolver() {
        if (LevelNextActionEnum.ShowSceneSolvers == m_next_action || LevelStateEnum.PrepareSolving == m_state) {
            return;
        }

        boolean solved = false;

        switch (m_difficulty) {
            case Easy: solved = Cubetraz.getSolvedEasy(m_level_number); break;
            case Normal: solved = Cubetraz.getSolvedNormal(m_level_number); break;
            case Hard: solved = Cubetraz.getSolvedHard(m_level_number); break;
        }

        if (solved) {
            reset();
            m_state = LevelStateEnum.PrepareSolving;
            m_timeout = 2.0f;
        } else {
            m_next_action = LevelNextActionEnum.ShowSceneSolvers;
        }

        m_hud.setupDisappear();
    }

    public void eventQuit() {
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

        Game.anim_init_data.cube_rotation_degree = m_cube_rotation.degree;

        if (LevelStateEnum.Paused == m_state) {
            Game.anim_init_data.type = AnimTypeEnum.AnimToMenuFromPaused;
        } else {
            Game.anim_init_data.type = AnimTypeEnum.AnimToMenuFromCompleted;
        }

        Game.showScene(Scene_Anim);
    }

    public void eventLevelPause() {
        m_hud.setupDisappear();
        setAnimToPaused();
    }

    public void eventLevelComplete() {
        Game.playSound(SOUND_LEVEL_COMPLETED);

        Game.renderToFBO(this);
        m_hud.setupDisappear();

        StatInitData sid = Game.stat_init_data;
        getRatings();

        switch (m_difficulty) {
            case Easy:
                Cubetraz.setStarsEasy(m_level_number, sid.stars);
                Cubetraz.setMovesEasy(m_level_number, m_moves_counter);

                if (m_level_number < 60) {
                    if (LEVEL_LOCKED == Cubetraz.getStarsEasy(m_level_number + 1))
                    Cubetraz.setStarsEasy(m_level_number + 1, LEVEL_UNLOCKED);
                }

                //reportAchievementEasy();
                break;

            case Normal:
                Cubetraz.setStarsNormal(m_level_number, sid.stars);
                Cubetraz.setMovesNormal(m_level_number, m_moves_counter);

                if (m_level_number < 60) {
                    if (LEVEL_LOCKED == Cubetraz.getStarsNormal(m_level_number + 1)) {
                        Cubetraz.setStarsNormal(m_level_number + 1, LEVEL_UNLOCKED);
                    }
                }

                //reportAchievementNormal();
                break;

            case Hard:
                Cubetraz.setStarsHard(m_level_number, sid.stars);
                Cubetraz.setMovesHard(m_level_number, m_moves_counter);

                if (m_level_number < 60) {
                    if (LEVEL_LOCKED == Cubetraz.getStarsHard(m_level_number + 1)) {
                        Cubetraz.setStarsHard(m_level_number + 1, LEVEL_UNLOCKED);
                    }
                }

                //reportAchievementHard();
                break;
        } // switch

        Cubetraz.save();
        //Game.submitScore(cCubetraz::GetStarCount());
        m_state = LevelStateEnum.SetupAnimToCompleted;
        Game.showScene(Scene_Stat);
    }

    public void reset() {
        int size;
        m_moves_counter = 0;
        m_lst_undo.clear();

        m_hud.set1stHint();
        m_hud.setTextMoves(m_moves_counter);
        m_hud.setTextUndo( m_lst_undo.size() );

        m_player_cube.setCubePos(LevelBuilder.player);

        Game.resetCubesColors();

        if (!m_lst_moving_cubes.isEmpty()) {
            MovingCube movingCube;
            size = m_lst_moving_cubes.size();
            for (int i = 0; i < size; ++i) {
                movingCube = m_lst_moving_cubes.get(i);
                movingCube.reposition();
            }
        }

        if (!m_lst_mover_cubes.isEmpty()) {
            MoverCube moverCube;
            size = m_lst_moving_cubes.size();
            for (int i = 0; i < size; ++i) {
                moverCube = m_lst_mover_cubes.get(i);
                moverCube.reposition();
            }
        }

        if (!m_lst_dead_cubes.isEmpty()) {
            DeadCube deadCube;
            size = m_lst_moving_cubes.size();
            for (int i = 0; i < size; ++i) {
                deadCube = m_lst_dead_cubes.get(i);
                deadCube.reposition();
            }
        }
    }

    public void eventUndo() {
        m_timeout_undo = UNDO_TIMEOUT;

        if (m_moves_counter > 0) {
            --m_moves_counter;
            m_hud.setTextMoves(m_moves_counter);

            UndoData ud = m_lst_undo.get(m_lst_undo.size() - 1);
            m_lst_undo.remove(ud);

            m_player_cube.setCubePos(ud.player_pos);

            if (null != ud.moving_cube) {
                ud.moving_cube.init(ud.moving_cube_pos, ud.moving_cube_move_dir);
            }

            if (0 == m_moves_counter) {
                m_hud.set1stHint();
            }

            m_state = LevelStateEnum.Undo;
        }
    }

    @Override
    public void onFingerDown(float x, float y, int finger_count) {
        mIsFingerDown = true;

        mPosDown.x = x;
        mPosDown.y = y;

        if (1 == finger_count) {
            Color down_color;

            switch (m_state) {
                case Playing:
                    if (HUDStateEnum.DoneHUD == m_hud.getState() ) {
                        renderForPicking(PickRenderTypeEnum.RenderOnlyHUD);

                        down_color = Graphics.getColorFromScreen(mPosDown);
                        int realColor = (int)(down_color.b * 255f);
                        switch (realColor) {
                            case 200: m_hud.setHilitePause(true); break;
                            case 150: m_hud.setHiliteUndo(true); break;
                            case 100: m_hud.setHiliteHint(true); break;
                            case 50: m_hud.setHiliteSolver(true); break;
                            default:
                                break;
                        }
                    }
                    break;

                case Paused:
                case Completed: {
                    renderForPicking(PickRenderTypeEnum.RenderOnlyMovingCubes);

                    down_color = Graphics.getColorFromScreen(mPosDown);
                    int realColor = (int)(down_color.b * 255f);
                    MenuCube menuCube = getMovingCubeFromColor(realColor);
                    if (menuCube != null) {
                        m_hilite_alpha = 0.0f;
                        m_menu_cube_hilite = menuCube;
                        CubePos cp = new CubePos();
                        cp.init(m_menu_cube_hilite.m_cube_pos);
                        //printf("\nCubePos: %d, %d, %d", cp.x, cp.y, cp.z);
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
    int ln = m_level_number + 1;
    DifficultyEnum diff = m_difficulty;

    if (ln > 60)
    {
        ln = 1;
        if (Easy == m_difficulty)
            diff = Normal;

        if (Normal == m_difficulty)
            diff = Hard;

        if (Hard == m_difficulty)
            diff = Easy;
    }

    engine.level_init_data.difficulty = diff;
    engine.level_init_data.level_number = ln;
    engine.level_init_data.init_action = FullInit;

    engine.ShowScene(Scene_Level);
    return;
*/
        mPosUp.x = x;
        mPosUp.y = y;

        switch (m_state) {
            case Playing:
                fingerUpPlaying(x, y, finger_count);
                m_hud.setHilitePause(false);
                m_hud.setHiliteUndo(false);
                m_hud.setHiliteHint(false);
                m_hud.setHiliteSolver(false);
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
                m_hud.hideTutor();
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
            if (m_state == LevelStateEnum.Playing) {
                Vector2 dir = new Vector2();
                dir.x = (mPosDown.x - cur_x) / Graphics.device_scale;
                dir.y = (mPosDown.y - cur_y) / Graphics.device_scale;

                m_alter_view = true;

                m_user_rotation.current.x = -dir.y * 0.3f;
                m_user_rotation.current.y = -dir.x * 0.3f;
                m_user_rotation.clamp();

                mIsSwipe = false;
            }
        }
    }

    public void fingerUpPaused(float x, float y) {
//    float fingerup_x = x;
//    float fingerup_y = y;

        if (mIsSwipe) {
            mIsSwipe = false;
            renderForPicking(PickRenderTypeEnum.RenderOnlyMovingCubes);
            Color down_color = Graphics.getColorFromScreen(mPosDown);
            SwipeInfo swipeInfo = Engine.getSwipeDirAndLength(mPosDown, mPosUp);

            if (swipeInfo.length > 30.0f * graphics.scaleFactor) {
                MenuCube menuCube = null;
                int realColor = (int)(down_color.b * 255f);
                switch (realColor) {
                    case 200: menuCube = m_menu_cube_up; break;
                    case 150: menuCube = m_menu_cube_mid; break;
                    case 100: menuCube = m_menu_cube_low; break;
                }

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

    public void fingerUpCompleted(float x, float y) {
//    float fingerup_x = x;
//    float fingerup_y = y;

        if (mIsSwipe) {
            mIsSwipe = false;

            renderForPicking(PickRenderTypeEnum.RenderOnlyMovingCubes);
            Color down_color = Graphics.getColorFromScreen(mPosDown);

//		printf("\nOnFingerUp [SWIPE] color is: %d, %d, %d, %d", down_color.r, down_color.g, down_color.b, down_color.a);

            SwipeDirEnums swipeDir = SwipeDirEnums.SwipeDown;
            float length = 1f;
            SwipeInfo swipeInfo = Engine.getSwipeDirAndLength(mPosDown, mPosUp);

            if (swipeInfo.length > 30.0f * graphics.scaleFactor) {
                MenuCube menuCube = null;
                int realColor = (int)(down_color.b * 255f);
                switch (realColor) {
                    case 200: menuCube = m_menu_cube_up; break;
                    case 150: menuCube = m_menu_cube_mid; break;
                    case 100: menuCube = m_menu_cube_low; break;
                }

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

    public void fingerUpPlaying(float x, float y, int finger_count) {
        if (m_hud.getShowHint()) {
            return;
        }

        if (!m_player_cube.isDone()) {
            mIsSwipe = false;
            return;
        }

//        if (m_alter_view) {
//            if ( Math.abs(m_user_rotation.current.x) > EPSILON || Math.abs(m_user_rotation.current.y) > EPSILON ) {
//                m_reposition_view = true;
//                m_alter_view = false;
//                m_t = 0.0f;
//                m_user_rotation.from = m_user_rotation.current;
//            }
//            return;
//        }

        if (!mIsSwipe) {
            if (m_state == LevelStateEnum.Playing) {
                renderForPicking(PickRenderTypeEnum.RenderOnlyHUD);
                Vector2 pos = new Vector2(x,y);
                Color color = Graphics.getColorFromScreen(pos);
                int realColor = (int)(color.b * 255f);
                switch (realColor) {
                    case 200: eventLevelPause(); break;
                    case 150: eventUndo(); break;
                    case 100: eventShowHint(); break;
                    case  50: eventSolver(); break;
                    default:
                        break;
                }
            }
            return;
        } else { // swipe        
            mIsSwipe = false;

            if (m_hud.isAnythingHilited()) {
                return;
            }

            // flip the Y-axis because pixel coordinates increase toward the bottom (iPhone 3D Programming 86. page)
            float fingerdown_x = mPosDown.x;
            float fingerdown_y = -mPosDown.y;

            float fingerup_x = x;
            float fingerup_y = -y;

            Vector2 dir = new Vector2();
            dir.x = fingerup_x - fingerdown_x;
            dir.y = fingerup_y - fingerdown_y;

            float len = dir.len(); // swipe length

            System.out.println("Swipe Length: " + len + ", minSwipeLength(" + Game.minSwipeLength + ")");

            if (len < Game.minSwipeLength){
                return;
            }

            dir.nor();

            float degree = (float)Math.toDegrees( Math.atan2(dir.y, dir.x) );

//            printf("\nfinger down x:%f, y:%f", m_fingerdown_x, m_fingerdown_y);
//            printf("\nfinger up x:%f, y:%f", fingerup_x, fingerup_y);
//            printf("\ndir.x:%f, dir.y:%f", dir.x, dir.y);
//            printf("\ndegree: %0.2f", degree);

            boolean success = false;
            float center;
            final float space = 30.0f;

            center = 90.0f; // Y plus
            if (degree > (center - space) && degree < (center + space)) {
                success = m_player_cube.moveOnAxis(AxisMovement_Y_Plus);
            }

            center = -90.0f; // Y minus
            if (degree > (center - space) && degree < (center + space)) {
                success = m_player_cube.moveOnAxis(AxisMovement_Y_Minus);
            }

            center = 30.0f; // Z plus
            if (degree > (center - space) && degree < (center + space)) {
                success = m_player_cube.moveOnAxis(AxisMovement_Z_Minus);
            }

            center = -150.0f; // Z minus
            if (degree > (center - space) && degree < (center + space)) {
                success = m_player_cube.moveOnAxis(AxisMovement_Z_Plus);
            }

            center = -30.0f; // X minus
            if (degree > (center - space) && degree < (center + space)) {
                success = m_player_cube.moveOnAxis(AxisMovement_X_Plus);
            }

            center = 150.0f; // X plus
            if (degree > (center - space) && degree < (center + space)) {
                success = m_player_cube.moveOnAxis(AxisMovement_X_Minus);
            }

            if (success) {
                addMove();
            }
        }
    }

    public void addMove() {
        ++m_moves_counter;
        m_hud.setTextMoves(m_moves_counter);

        if (1 == m_moves_counter) {
            m_hud.set2ndHint();
        }

        UndoData ud = new UndoData(m_player_cube.getCubePos());

        if (m_player_cube.m_moving_cube != null) {
            ud.moving_cube.init(m_player_cube.m_moving_cube);
            ud.moving_cube_pos.init(m_player_cube.m_moving_cube.getCubePos());
            ud.moving_cube_move_dir = m_player_cube.m_moving_cube.getMovement();
        }

        if (m_player_cube.m_mover_cube != null) {
            CubePos cp = m_player_cube.m_cube_pos_destination;
            CubePos cp_new = new CubePos();
            cp_new.init(cp);

            // check if cube can move in a selected dir
            m_player_cube.calcMovement(cp_new, m_player_cube.m_mover_cube.getMoveDir(), false);

            if (cp.x != cp_new.x || cp.y != cp_new.y || cp.z != cp_new.z) {
                ud.mover_cube = m_player_cube.m_mover_cube;
            }
        }

        m_lst_undo.add(ud);

        m_hud.setTextUndo(m_lst_undo.size());

        m_state_to_restore = m_state;
        m_state = LevelStateEnum.MovingPlayer;
        m_timeout = 0.0f; // move immediately
    }

}

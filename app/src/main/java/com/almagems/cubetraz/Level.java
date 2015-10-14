package com.almagems.cubetraz;

import java.util.ArrayList;

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
    private ArrayList<UndoData> m_lst_undo;

    private LevelStateEnum m_state;
    private LevelStateEnum m_state_to_restore;

    private SubAppearStateEnum m_appear_state;

    private Color m_color_down;
    private Color m_color_up;

    private HUD m_hud;

    private UserRotation m_user_rotation;

    private MenuCube m_menu_cube_hilite;
    private CubeFont m_font_hilite;
    private float m_hilite_alpha;

    private float m_hilite_timeout;

    private float dead_size;
    private int dead_alpha;
    private int dead_alpha_step;

    private int m_star_count;

    private ArrayList<Cube> m_list_cubes_level;
    private ArrayList<Cube> m_list_cubes_wall_y_minus;
    private ArrayList<Cube> m_list_cubes_wall_x_minus;
    private ArrayList<Cube> m_list_cubes_wall_z_minus;
    private ArrayList<Cube> m_list_cubes_edges;
    private ArrayList<Cube> m_list_cubes_base;
    private ArrayList<Cube> m_list_cubes_face;
    private ArrayList<Cube> m_list_cubes_hint;

    private Cube[] m_ar_hint_cubes = new Cube[MAX_HINT_CUBES];
    private boolean m_show_hint_2nd;
    private int m_hint_index;
    private float m_hint_timeout;

    private AppearDisappearListData m_ad_level;
    private AppearDisappearListData m_ad_base;
    private AppearDisappearListData m_ad_face;

    private final Camera m_camera_level;
    private final Camera m_camera_level_completed;
    private final Camera m_camera_level_paused;
    private final Camera m_camera_current;

    private float m_target_rotation_degree;

    private final EaseOutDivideInterpolation m_interpolator = new EaseOutDivideInterpolation();

    private boolean m_alter_view;
    private boolean m_reposition_view;

    private boolean m_draw_texts;

    private DifficultyEnum m_difficulty;
    private int m_level_number;
    private int m_level_number_begin;

    private int m_solution_pointer;
    private AxisMovementEnum[] m_ar_solution = new AxisMovementEnum[MAX_SOLUTION_MOVES];

    private int m_moves_counter;
    private int m_min_solution_steps;
    private float m_timeout;

    private float m_timer_to_statistics;

    private CubePos m_cube_pos_key;

// CubeFonts
    private CubeFont m_cubefont_up;
    private CubeFont m_cubefont_mid;
    private CubeFont m_cubefont_low;

    private final ArrayList<CubeFont> m_list_fonts_pool = new ArrayList();
    private final ArrayList<CubeFont> m_list_fonts = new ArrayList();

// spec cubes
    private final ArrayList<MovingCube> m_lst_moving_cubes = new ArrayList();
    private final ArrayList<MoverCube> m_lst_mover_cubes = new ArrayList();
    private final ArrayList<DeadCube> m_lst_dead_cubes = new ArrayList();

    private boolean m_anim_undo;
    private float m_anim_undo_alpha;
    private float m_anim_undo_scale;

    private float m_fade_value;

    private CubeRotation m_cube_rotation_current;
    private CubeRotation m_cube_rotation;

    private Vector m_pos_light;

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

        // case Device_iPad:
        d = 0.99f;

        m_camera_level.eye = m_camera_level.eye * d;

        m_camera_level_completed.eye = m_camera_level_completed.eye * d;
        m_camera_level_completed.target = m_camera_level_completed.target * d;

        m_camera_level_paused.eye = m_camera_level_paused.eye * d;
        m_camera_level_paused.target = m_camera_level_paused.target * d;

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
    
    public void init(LevelInitData lid) {
        m_moving_cube = null;
        m_mover_cube = null;
        m_dead_cube = null;

        //Game.level_init_data.difficulty = Easy;
        //Game.level_init_data.level_number = 12;

        m_tutor_index = 0;
        m_next_action = NoNextAction;
        
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
                m_lst_undo.erase(m_lst_undo.begin(), m_lst_undo.end());
                m_lst_undo.clear();

                m_hud.set1stHint();
                m_hud.setTextMoves(m_moves_counter);
                m_hud.setTextUndo( m_lst_undo.size() );

                m_fingerdown = false;
                m_swipe = false;

                m_cube_rotation.degree = -45.0f;
                m_cube_rotation.axis = vec3(0.0f, 1.0f, 0.0f);

                m_user_rotation.reset();

                m_camera_current = m_camera_level;

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
                if (SetupAnimToCompleted == m_state) {
                
                } else {
                    m_hud.setupAppear();
                }
                break;

            case ShowSolution:
                reset();
                m_state = PrepareSolving;
                m_timeout = 3.5f;
                break;
        }

        Graphics.setProjection3D();
        Graphics.setModelViewMatrix3D(m_camera_current);

        //const vec4 lightPosition(m_pos_light.x, m_pos_light.y, m_pos_light.z, 1.0f);
        //glLightfv(GL_LIGHT0, GL_POSITION, lightPosition.Pointer());

        glEnable(GL_LIGHT0);
    }

    public void getRatings() {
        char sign;

        StatInitData sid = Game.stat_init_data;

        if (m_moves_counter < m_min_solution_steps) {
            sign = '<';
            sid.str_title = "EXPERT";
            sid.stars = 3;
        }

        if (m_moves_counter == m_min_solution_steps) {
            sign = '=';
            sid.str_title = "PERFECT";
            sid.stars = 3;
        }

        if (m_moves_counter > m_min_solution_steps && m_moves_counter <= m_min_solution_steps + 1) {
            sign = '>';
            sid.str_title = "EXCELLENT";
            sid.stars = 3;
        }

        if (m_moves_counter > m_min_solution_steps + 1 && m_moves_counter <= m_min_solution_steps + 4) {
            sign = '>';
            sid.str_title = "GREAT";
            sid.stars = 2;
        }

        if (m_moves_counter > m_min_solution_steps + 4) {
            sign = '>';
            sid.str_title = "GOOD";
            sid.stars = 1;
        }

        sid.str_moves = "PLAYER:" + m_moves_counter + " " + sign + " BEST:" + m_min_solution_steps;
    }
    
    public void showTutor(int index) {
        m_state = Tutorial;
        m_hud.clearTutors();

        switch (index) {
            case Swipe: m_hud.showTutorSwipeAndGoal(); break;
            case Drag: m_hud.showTutorDrag(); break;
            case Moving: m_hud.showTutorMoving(); break;
            case Mover: m_hud.showTutorMover(); break;
            case Dead: m_hud.showTutorDead(); break;
            case Plain: m_hud.showTutorPlain(); break;
            case MenuPause: m_hud.showTutorMenuPause(); break;
            case MenuUndo: m_hud.showTutorMenuUndo(); break;
            case MenuHint: m_hud.showTutorMenuHint(); break;
            case MenuSolvers: m_hud.showTutorMenuSolvers(); break;
            default:
                break;
        }
    }
    
    public boolean isMovingCube(CubePos cube_pos, boolean set) {
        MovingCube movingCube;

        list<cMovingCube*>::iterator it;
        for(it = m_lst_moving_cubes.begin(); it != m_lst_moving_cubes.end(); ++it) {
            movingCube = *it;
            CubePos cp = movingCube.getCubePos();

            if (cp.x == cube_pos.x && cp.y == cube_pos.y && cp.z == cube_pos.z) {
                if (set) {
                    m_player_cube.SetMovingCube(pMovingCube);
                }
                return true;
            }
        }
        return false;
    }

    public boolean isMoverCube(CubePos cube_pos, boolean set) {
        MoverCube moverCube;
        list<cMoverCube*>::iterator it;
        for(it = m_lst_mover_cubes.begin(); it != m_lst_mover_cubes.end(); ++it) {
            moverCube = *it;
            CubePos cp = moverCube.getCubePos();

            if (cp.x == cube_pos.x && cp.y == cube_pos.y && cp.z == cube_pos.z) {
                if (set) {
                    if (m_mover_cube != p) { // bugfix! when player hits the mover cube from behind!
                        m_player_cube.setMoverCube(p);
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
        for(int i = 0; i < len; ++it) {
            deadCube = m_lst_dead_cubes.get(i);
            cp = deadCube.getCubePos();

            if (cp.x == cube_pos.x && cp.y == cube_pos.y && cp.z == cube_pos.z) {
                if (set) {
                    m_player_cube.setDeadCube(p);
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

        if (!m_lst_dead_cubes.isEmpty()) {
            DeadCube deadCube;
            list<cDeadCube*>::iterator it;
            for(it = m_lst_dead_cubes.begin(); it != m_lst_dead_cubes.end(); ++it) {
                deadCube = *it;
                if (deadCube != ignore_dead_cube) {
                    cp = deadCube.getCubePos();

                    if (cp.x == cube_pos.x && cp.y == cube_pos.y && cp.z == cube_pos.z) {
                        return true;
                    }
                }
            }
        }

        if (!m_lst_mover_cubes.isEmpty()) {
            MoverCube moverCube;
            list<cMoverCube*>::iterator it;
            for(it = m_lst_mover_cubes.begin(); it != m_lst_mover_cubes.end(); ++it) {
                moverCube = *it;

                if (moverCube != ignore_mover_cube) {
                    cp = moverCube.getCubePos();

                    if (cp.x == cube_pos.x && cp.y == cube_pos.y && cp.z == cube_pos.z) {
                        return true;
                    }
                }
            }
        }

        if (!m_lst_moving_cubes.isEmpty()) {
            MovingCube movingCube;
            list<cMovingCube*>::iterator it;
            for(it = m_lst_moving_cubes.begin(); it != m_lst_moving_cubes.end(); ++it) {
                movingCube = *it;

                if (p != ignore_moving_cube) {
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
        m_state = Appear;
        m_appear_state = SubAppearWait;
        m_draw_menu_cubes = false;
        m_draw_texts = false;
        m_timeout = 0.0f;
        m_alter_view = false;

        if (rand()%2 == 0) {
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
                Game.cubes[x][7][z].type = CubeIsInvisibleAndObstacle;
                Game.cubes[x][1][z].type = CubeIsInvisibleAndObstacle;

                Game.cubes[x][7][z].setColor( Game.getBaseColor() );
                Game.cubes[x][1][z].setColor( Game.getBaseColor() );
            }
        }

        // wall x minus
        for (int z = 0; z < MAX_CUBE_COUNT; ++z) {
            for (int y = 0; y < MAX_CUBE_COUNT; ++y) {
                Game.cubes[1][y][z].type = CubeIsInvisibleAndObstacle;
                Game.cubes[1][y][z].setColor( Game.getBaseColor() );
            }
        }

        // wall z minus
        for (int x = 0; x < MAX_CUBE_COUNT; ++x) {
            for (int y = 0; y < MAX_CUBE_COUNT; ++y) {
                Game.cubes[x][y][1].type = CubeIsInvisibleAndObstacle;
                Game.cubes[x][y][1].setColor( Game.getBaseColor() );
            }
        }


        // wall y minus
        for (int z = 2; z < MAX_CUBE_COUNT; ++z) {
            for (int x = 2; x < MAX_CUBE_COUNT; ++x) {
                Game.cubes[x][1][z].type = CubeIsVisibleAndObstacle;
                Game.cubes[x][1][z].setColor( Game.getBaseColor() );

                m_list_cubes_wall_y_minus.add(Game.cubes[x][1][z]);
            }
        }

        // walls
        for (int y = 2; y < 7; ++y) {
            for (int x = 2; x < MAX_CUBE_COUNT; ++x) {
                Game.cubes[x][y][1].type = CubeIsVisibleAndObstacle;
                Game.cubes[x][y][1].setColor( Game.getBaseColor() );

                if (y > 0) {
                    m_list_cubes_wall_z_minus.add(Game.cubes[x][y][1]);
                }
            }

            for (int z = 2; z < MAX_CUBE_COUNT; ++z) {
                Game.cubes[1][y][z].type = CubeIsVisibleAndObstacle;
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
        m_state = DeadAnim;
        m_timeout = 1.5f;

        dead_size = 60.0f;
        dead_alpha = 0;
        dead_alpha_step = 4;

        m_dead_cube = deadCube;
        m_dead_cube.hiLite();
    }

    public void setupMoveCube(MovingCube movingCube) {
        m_state_to_restore = m_state;
        m_state = MovingCube;
        m_timeout = 0.3f;

        m_moving_cube = movingCube;
        m_moving_cube.move();
    }

    public void setupMoverCube(MoverCube moverCube) {
        AxisMovementEnum movement = moverCube.getMoveDir();
        m_state_to_restore = m_state;
        m_state = MovingPlayer;
        m_timeout = 0.3f;

        m_mover_cube = pMoverCube;
        m_mover_cube.hiLite();

        m_player_cube.moveOnAxis(movement);

        UndoData ud = m_lst_undo.back();
        ud.moving_cube = m_player_cube.m_moving_cube; //m_lst_moving_cubes.front();

        if (ud.moving_cube) {
            ud.moving_cube_move_dir = ud.moving_cube.getMovement();
            ud.moving_cube_pos = ud.moving_cube.getCubePos();
        }
    }
    
    public void setSolversCount() {
        m_hud.setTextSolver(Game.getSolverCount());
    }
    
    public void setAnimToCompleted() {
        m_state = AnimToCompleted;

        m_draw_menu_cubes = true;
        m_draw_texts = true;

        m_list_cubes_base.clear();
        m_list_cubes_face.clear();

        m_ad_face.Clear();
        m_ad_base.Clear();

        Cube cube;

        // way for menu cubes carved out
        for (int i = 0; i < MAX_CUBE_COUNT; ++i) {
            cube = Game.cubes[i][1][0];
            cube.type = CubeIsInvisible;

            cube = Game.cubes[i][3][0];
            cube.type = CubeIsInvisible;

            cube = Game.cubes[i][5][0];
            cube.type = CubeIsInvisible;
        }

        for (int i = 0; i < 7; ++i) {
            cube = Game.cubes[8][i][0];
            cube.type = CubeIsVisibleAndObstacle;
            cube.setColor( Game.getFaceColor() );
            m_ad_face.addAppear(cube);
        }

        for (int i = 0; i < 7; ++i) {
            cube = Game.cubes[0][i][1];
            cube.type = CubeIsVisibleAndObstacle;
            cube.setColor( Game.getBaseColor() );
            m_ad_base.addAppear(cube);
        }

        for (int i = 0; i < MAX_CUBE_COUNT; ++i) {
            cube = Game.cubes[i][0][1];
            cube.type = CubeIsVisibleAndObstacle;
            cube.setColor( Game.getBaseColor() );
            m_ad_base.addAppear(cube);
        }

        cube = Game.cubes[7][0][0];
        cube.type = CubeIsVisibleAndObstacle;
        cube.setColor( Game.getFaceColor() );
        m_ad_face.addAppear(cube);

        cube = Game.cubes[7][2][0];
        cube.type = CubeIsVisibleAndObstacle;
        cube.setColor( Game.getFaceColor() );
        m_ad_face.addAppear(cube);

        cube = Game.cubes[7][4][0];
        cube.type = CubeIsVisibleAndObstacle;
        cube.setColor( Game.getFaceColor() );
        m_ad_face.addAppear(cube);

        pCube = Game.cubes[7][6][0];
        pCube.type = CubeIsVisibleAndObstacle;
        pCube.setColor( Game.getFaceColor() );
        m_ad_face.addAppear(cube);

        m_ad_face.setLevelAndDirection(0, 1);
        m_ad_base.setLevelAndDirection(0, 1);

        m_t = 0.0f;

        if (Hard == m_difficulty && 60 == m_level_number) {
            m_completed_face_next_action = Finish;
        } else {
            m_completed_face_next_action = Next;
        }

        Creator.createTextsLevelCompletedFace(this, m_completed_face_next_action);

        CubeFont cubeFont;
        Color color = Game.getTextColorOnCubeFace();
        list<cCubeFont*>::iterator it;
        for(it = m_list_fonts.begin(); it != m_list_fonts.end(); ++it) {
            cubeFont = *it;
            cubeFont.setColor(color);
        }

        color = new Color(20, 0, 0, 20);
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

        m_menu_cube_up.moveOnAxis(X_Plus);
        m_menu_cube_mid.moveOnAxis(X_Plus);
        m_menu_cube_low.moveOnAxis(X_Plus);        
    }

    public void updateAnimToCompleted() {
        m_menu_cube_up.update();
        m_menu_cube_mid.update();
        m_menu_cube_low.update();

        Cube cube;
        
        cube = m_ad_base.GetCubeFromAppearList();
        if (cube) {
            m_list_cubes_base.add(cube);
        }

        cube = m_ad_face.GetCubeFromAppearList();
        if (cube) {
            m_list_cubes_face.add(cube);
        }

        //engine.IncT(m_t);

        Utils.lerpCamera(m_camera_level, m_camera_level_completed, m_t, m_camera_current);

        m_interpolator.interpolate();
        m_cube_rotation.degree = m_interpolator.getValue();

        float diff = Math.abs(m_target_rotation_degree) - Math.abs(m_interpolator.getValue());

        if (diff < 0.1f && m_ad_base.lst_appear.isEmpty() && m_ad_face.lst_appear.isEmpty() && (Math.abs(1.0f - m_t) < EPSILON)) {
            m_cube_rotation.degree = m_target_rotation_degree;
            m_state = Completed;

            Color color = new Color(100, 0, 0, 230);

            m_cubefont_up.color  = color;
            m_cubefont_mid.color = color;
            m_cubefont_low.color = color;

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
                                    m_difficulty = Normal;
                                    m_level_number = 1;
                                    Game.stopMusic();
                                    setupMusic();
                                    break;

                                case Normal:
                                    m_difficulty = Hard;
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
        m_state = AnimFromCompleted;
        m_t = 0.0f;
        m_target_rotation_degree = -45.0f;

        m_interpolator.setup(m_cube_rotation.degree, m_target_rotation_degree, 6.0f);

        m_ad_base.setLevelAndDirection(MAX_CUBE_COUNT - 1, -1);
        m_ad_face.setLevelAndDirection(MAX_CUBE_COUNT - 1, -1);
    }

    public void updateAnimFromCompleted() {
        //engine.IncT(m_t);
        Utils.lerpCamera(m_camera_level_completed, m_camera_level, m_t, m_camera_current);

        Cube cube;
        pCube = m_ad_base.getCubeFromDisappearList();

        if (cube) {
            m_list_cubes_base.remove(cube);
        }

        cube = m_ad_face.getCubeFromDisappearList();

        if (cube) {
            m_list_cubes_face.remove(cube);
        }

        m_interpolator.interpolate();
        m_cube_rotation.degree = m_interpolator.getValue();

        float diff = Math.abs(m_cube_rotation.degree) - Math.abs(m_target_rotation_degree);

        if (diff < 0.1f && ((1.0f - m_t) < EPSILON) && m_ad_face.lst_disappear.isEmpty() && m_ad_base.lst_disappear.isEmpty()) {
            m_cube_rotation.degree = m_target_rotation_degree;

            if (Hard == m_difficulty && 61 == m_level_number) {
                Game.showScene(Scene_Outro);
            } else {
                setupAppear();
            }
        }
    }
    
    public void setAnimToPaused() {
        m_state = AnimToPaused;

        m_draw_menu_cubes = true;
        m_draw_texts = true;

        m_target_rotation_degree = m_cube_rotation.degree + 90.0f + 45.0f;
        m_interpolator.setup(m_cube_rotation.degree, m_target_rotation_degree, 5.0f);
        m_t = 0.0f;

        Creator.createTextsLevelPausedFace(this);

        CubeFont cubeFont;
        Color color = Game.getTextColorOnCubeFace();
        list<cCubeFont*>::iterator it;
        for(it = m_list_fonts.begin(); it != m_list_fonts.end(); ++it) {
            cubeFont = *it;
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
            cube.type = CubeIsInvisible;

            cube = Game.cubes[0][3][i];
            cube.type = CubeIsInvisible;

            cube = Game.cubes[0][1][i];
            cube.type = CubeIsInvisible;
        }

        for (int i = 0; i < 7; ++i) {
            cube = Game.cubes[0][i][0];
            cube.type = CubeIsVisibleAndObstacle;
            cube.setColor( Game.getFaceColor() );
            m_ad_face.addAppear(cube);

            cube = Game.cubes[1][i][0];
            cube.type = CubeIsVisibleAndObstacle;
            cube.setColor( Game.getBaseColor() );
            m_ad_base.addAppear(cube);
        }

        for (int i = 0; i < MAX_CUBE_COUNT; ++i) {
            cube = Game.cubes[1][0][i];
            cube.type = CubeIsVisibleAndObstacle;
            cube.setColor( Game.getBaseColor() );
            m_ad_base.addAppear(cube);
        }

        cube = Game.cubes[0][0][1];
        cube.type = CubeIsVisibleAndObstacle;
        cube.setColor( Game.getFaceColor() );
        m_ad_face.addAppear(cube);

        cube = Game.cubes[0][2][1];
        cube.type = CubeIsVisibleAndObstacle;
        cube.setColor( Game.getFaceColor() );
        m_ad_face.addAppear(cube);

        cube = Game.cubes[0][4][1];
        cube.type = CubeIsVisibleAndObstacle;
        cube.setColor( Game.getFaceColor() );
        m_ad_face.addAppear(cube);

        cube = Game.cubes[0][6][1];
        cube.type = CubeIsVisibleAndObstacle;
        cube.setColor( Game.getFaceColor() );
        m_ad_face.addAppear(cube);

        m_ad_face.SetLevelAndDirection(0, 1);
        m_ad_base.SetLevelAndDirection(0, 1);

        m_menu_cube_up.setCubePos( CubePos(0, 5, 8));
        m_menu_cube_mid.setCubePos(CubePos(0, 3, 8));
        m_menu_cube_low.setCubePos(CubePos(0, 1, 8));

        CubePos offset = new CubePos(1, 0, 0);
        m_menu_cube_up.setHiliteOffset(offset);
        m_menu_cube_mid.setHiliteOffset(offset);
        m_menu_cube_low.setHiliteOffset(offset);

        m_menu_cube_up.moveOnAxis(Z_Minus);
        m_menu_cube_mid.moveOnAxis(Z_Minus);
        m_menu_cube_low.moveOnAxis(Z_Minus);        
    }

    public void updateAnimToPaused() {
        m_menu_cube_up.update();
        m_menu_cube_mid.update();
        m_menu_cube_low.update();

        Cube cube;

        cube = m_ad_base.GetCubeFromAppearList();
        if (cube) {
            m_list_cubes_base.add(cube);
        }

        cube = m_ad_face.GetCubeFromAppearList();
        if (pCube) {
            m_list_cubes_face.add(cube);
        }

        //engine.IncT(m_t);
        Utils.lerpCamera(m_camera_level, m_camera_level_paused, m_t, m_camera_current);

        m_interpolator.interpolate();
        m_cube_rotation.degree = m_interpolator.getValue();

        float diff = Math.abs(m_interpolator.getValue()) - Math.abs(m_target_rotation_degree);
        diff = Math.abs(diff);

        if (diff < 0.1f && ((1.0f - m_t) < EPSILON_SMALL) && m_ad_face.lst_appear.isEmpty() && m_ad_base.lst_appear.isEmpty() ) {
            m_cube_rotation.degree = m_target_rotation_degree;
            m_state = Paused;

            Color color = new Color(100, 0, 0, 230);
            m_cubefont_up.color  = color;
            m_cubefont_mid.color = color;
            m_cubefont_low.color = color;

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

        if (m_menu_cube_up.IsDone()) { // back        
            if (m_menu_cube_up.m_cube_pos.z == 8) {
                setAnimFromPaused();
            }
        }

        if (m_menu_cube_mid.IsDone()) { // reset        
            if (m_menu_cube_mid.m_cube_pos.z == 8) {
                reset();
                setAnimFromPaused();
            }
        }

        if (m_menu_cube_low.IsDone()) { // quit
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
        m_state = AnimFromPaused;

        m_ad_base.setLevelAndDirection(MAX_CUBE_COUNT - 1, -1);
        m_ad_face.setLevelAndDirection(MAX_CUBE_COUNT - 1, -1);
    }

    public void updateAnimFromPaused() {
        //engine.IncT(m_t);
        Utils.lerpCamera(m_camera_level_paused, m_camera_level, m_t, m_camera_current);

        Cube cube;

        cube = m_ad_base.getCubeFromDisappearList();
        if (cube) {
            m_list_cubes_base.remove(cube);
        }

        cube = m_ad_face.getCubeFromDisappearList();
        if (cube) {
            m_list_cubes_face.remove(cube);
        }

        m_interpolator.interpolate();
        m_cube_rotation.degree = m_interpolator.getValue();

        // 90 <. - 45
        float diff = Math.abs( Math.abs(m_target_rotation_degree) - Math.abs(m_interpolator.getValue()) );

        if (diff < 0.1f && ((1.0f - m_t) < EPSILON) && m_ad_face.lst_disappear.isEmpty() && m_ad_base.lst_disappear.isEmpty() ) {
            m_cube_rotation.degree = m_target_rotation_degree;
            m_state = Playing;
            m_draw_menu_cubes = false;
            m_draw_texts = false;
        }
    }
    
    public void update() {
        switch (m_state) {
            case Playing:                   UpdatePlaying();            break;
            case Undo:                      UpdateUndo();               break;
            case PrepareSolving:            UpdatePrepareSolving();     break;
            case Solving:                   UpdateSolving();            break;
            case MovingCube:                UpdateMovingCube();         break;
            case MovingPlayer:              UpdateMovingPlayer();       break;
            case Completed:                 UpdateCompleted();          break; 
            case Paused:                    UpdatePaused();             break;
            case Appear:                    UpdateAppear();             break;
            case AnimFromCompleted:         UpdateAnimFromCompleted();  break;
            case AnimFromPaused:            UpdateAnimFromPaused();     break;
            case AnimToCompleted:           UpdateAnimToCompleted();    break;
            case AnimToPaused:              UpdateAnimToPaused();       break;
            case SetupAnimToCompleted:      SetAnimToCompleted();       break;
            case SetupAnimToPaused:         SetAnimToPaused();          break; 
            case SetupAnimFromCompleted:    SetAnimFromCompleted();     break;
            case SetupAnimFromPaused:       SetAnimFromPaused();        break;
            case DeadAnim:                  UpdateDeadAnim();           break; 
            case Tutorial:                  UpdateTutorial();           break;
            default:                
                break;
        }

        warmCubes(dt);

        if (m_reposition_view) {
            m_t += 0.1f;

            if (m_t >= 1.0f) {
                m_t = 1.0f;
                m_reposition_view = false;
                m_user_rotation.reset();
            } else {
                m_user_rotation.current.x = LERP(m_user_rotation.from.x, 0.0f, m_t);
                m_user_rotation.current.y = LERP(m_user_rotation.from.y, 0.0f, m_t);
            }
        }

        m_hud.update();

        if (NoNextAction != m_next_action) {
            if ( DoneHUD == m_hud.GetState() ) {
                switch (m_next_action) {
                    case ShowSceneSolvers:
                        Engine.renderToFBO(this);
                        Game.showScene(Scene_Solvers);
                        m_next_action = NoNextAction;
                        break;

                    default:
                        break;
                }
            }
        }

        if (m_menu_cube_hilite) {
            m_hilite_alpha += 0.05f;

            if (m_hilite_alpha > 0.2f) {
                m_hilite_alpha = 0.2f;
            }
        }

        if (m_show_hint_2nd) {
            m_hint_timeout -= dt;

            if (m_hint_timeout < 0.0f) {
                m_hint_timeout = 0.15f;
                Cube cube = m_ar_hint_cubes[m_hint_index];

                if (cube) {
                    Color col = new Color(255, 0, 0, 255);

                    cube.color_current = col;
                    ++m_hint_index;

                    // locate cube among moving cubes
                    MovingCube movingCube;
                    list<cMovingCube*>::iterator it;
                    for (it = m_lst_moving_cubes.begin(); it != m_lst_moving_cubes.end(); ++it)
                    {
                        movingCube = *it;
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
        m_hilite_timeout -= dt;

        if (m_hilite_timeout < 0.0f) {
            m_hilite_timeout = 0.05f;
            Color color = new Color(99, 99, 99, 255);

            if (!m_menu_cube_up.lst_cubes_to_hilite.isEmpty()) {
                Cube p = m_menu_cube_up.lst_cubes_to_hilite.front();
                m_menu_cube_up.lst_cubes_to_hilite.pop_front();

                p.color_current = color;
            }

            if (!m_menu_cube_mid.lst_cubes_to_hilite.isEmpty()) {
                Cube p = m_menu_cube_mid.lst_cubes_to_hilite.front();
                m_menu_cube_mid.lst_cubes_to_hilite.pop_front();

                p.color_current = color;
            }

            if (!m_menu_cube_low.lst_cubes_to_hilite.isEmpty()) {
                Cube p = m_menu_cube_low.lst_cubes_to_hilite.front();
                m_menu_cube_low.lst_cubes_to_hilite.pop_front();

                p.color_current = color;
            }
        }

        list<cCube*>::iterator it;
        for (it = m_list_cubes_wall_x_minus.begin(); it != m_list_cubes_wall_x_minus.end(); ++it) {
            (*it).warmByFactor(WARM_FACTOR);
        }

        for (it = m_list_cubes_edges.begin(); it != m_list_cubes_edges.end(); ++it) {
            (*it).warmByFactor(WARM_FACTOR);
        }

        for (it = m_list_cubes_wall_y_minus.begin(); it != m_list_cubes_wall_y_minus.end(); ++it) {
            (*it).warmByFactor(WARM_FACTOR);
        }

        for (it = m_list_cubes_wall_z_minus.begin(); it != m_list_cubes_wall_z_minus.end(); ++it) {
            (*it).warmByFactor(WARM_FACTOR);
        }

        for (it = m_list_cubes_base.begin(); it != m_list_cubes_base.end(); ++it) {
            (*it).warmByFactor(WARM_FACTOR);
        }

        for (it = m_list_cubes_level.begin(); it != m_list_cubes_level.end(); ++it) {
            (*it).warmByFactor(WARM_FACTOR);
        }

        list<cMovingCube*>::iterator itm;
        for (itm = m_lst_moving_cubes.begin(); itm != m_lst_moving_cubes.end(); ++itm) {
            (*itm).warmByFactor(WARM_FACTOR);
        }
    }

    public void updateDeadAnim() {
        m_timeout -= dt;
        dead_size += 4.0f;
        dead_alpha += dead_alpha_step;

        if (dead_alpha_step > 0) {
            if (dead_alpha > 130)
                dead_alpha_step = -3;
        } else {
            if (dead_alpha < 0) {
                Reset();
                m_state = Playing;
                m_dead_cube = null;
            }
        }
    }

    public void UpdateMovingCube() {
        m_timeout -= dt;

        if (m_timeout < 0.0f) {
            m_moving_cube.update(dt);

            if (m_moving_cube.isDone()) {
                m_state = m_state_to_restore;
                m_moving_cube = null;

                if (Solving == m_state_to_restore) {
                    m_timeout = 1.0f;
                }
            }
        }
    }

    public void updateMovingPlayer() {
        m_timeout -= dt;

        if (m_timeout < 0.0f) {
            m_player_cube.update(dt);

            if (m_player_cube.isDone()) {
                if (m_mover_cube) {
                    m_mover_cube.NoHiLite();
                    m_mover_cube = null;
                }

                if (m_moving_cube) {
                    m_moving_cube.NoHiLite();
                    m_moving_cube = null;
                }

                if (m_dead_cube) {
                    m_dead_cube.NoHilite();
                    m_dead_cube = null;
                }

                Game.playSound(SOUND_CUBE_HIT);

                m_state = m_state_to_restore;

                if (Solving == m_state_to_restore) {
                    m_timeout = 1.0f;
                }

                CubePos player = m_player_cube.getCubePos();
                CubePos key = m_cube_pos_key;

                if (player.x == key.x && player.y == key.y && player.z == key.z) {
                    Game.levelComplete();
                } else {
                    if (m_player_cube.m_dead_cube) {
                        setupDeadCube(m_player_cube.m_dead_cube);
                    } else if (m_player_cube.m_moving_cube) {
                        setupMoveCube(m_player_cube.m_moving_cube);
                    } else if (m_player_cube.m_mover_cube) {
                        setupMoverCube(m_player_cube.m_mover_cube);
                    }
                }
            }
        }
    }

    public void updateSolving() {
        m_timeout -= dt;

        if (m_timeout <= 0.0f) {
            m_timeout = 1.0f;

            if (m_player_cube.isDone()) {
                AxisMovementEnum dir = m_ar_solution[m_solution_pointer++];

                boolean success = m_player_cube.MoveOnAxis(dir);

                if (success) {
                    m_state_to_restore = m_state;
                    m_state = MovingPlayer;
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

        m_timeout -= dt;

        if (m_timeout < 2.5f) {
            m_hud.showPrepareSolving(true, m_min_solution_steps);
        }

        if (m_timeout <= 0.0f) {
            m_hud.showPrepareSolving(false, m_min_solution_steps);

            m_state = Solving;
            m_solution_pointer = 0;
            m_fade_value = 1.0f;
            m_timeout = 1.0f;
            m_hud.setupAppear();
        }
    }

    public void updateAppear() {
        m_timeout += dt;

        switch (m_appear_state) {
            case SubAppearWait: 
                if (m_timeout > 0.2f) {
                    Cube cube;

                    cube = m_ad_level.getCubeFromDisappearList();
                    if (cube) {
                        cube.type = CubeIsInvisible;
                        m_list_cubes_level.remove(cube);
                    }

                    cube = m_ad_level.getCubeFromDisappearList();
                    if (cube) {
                        cube.type = CubeIsInvisible;
                        m_list_cubes_level.remove(cube);
                    }

                    if (!m_lst_moving_cubes.isEmpty()) {
                        MovingCube* movingCube = m_lst_moving_cubes.front();
                        m_lst_moving_cubes.pop_front();
                        LevelBuilder.lst_moving_cubes.add(movingCube);
                    }

                    if (!m_lst_mover_cubes.isEmpty()) {
                        MoverCube moverCube = m_lst_mover_cubes.front();
                        m_lst_mover_cubes.pop_front();
                        LevelBuilder.lst_mover_cubes.add(moverCube);
                    }

                    if (!m_lst_dead_cubes.empty()) {
                        DeadCube deadCube = m_lst_dead_cubes.front();
                        m_lst_dead_cubes.pop_front();
                        LevelBuilder.lst_dead_cubes.add(deadCube);
                    }

                    if (m_list_cubes_level.isEmpty() &&
                            m_lst_moving_cubes.isEmpty() &&
                            m_lst_mover_cubes.isEmpty() &&
                            m_lst_dead_cubes.isEmpty()) {
                        m_timeout = 0.0f;
                        m_appear_state = SubAppearKeyAndPlayer;
                        m_t = 0.0;
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
                        m_appear_state = SubAppearWaitAgain;
                    }

                    m_fade_value = LERP(0.0f, 1.0f, m_t);
                }
                break;

            case SubAppearWaitAgain:
                if (m_timeout > 0.2) {
                    m_appear_state = SubAppearLevel;
                }
                break;

            case SubAppearLevel:
                if (m_ad_level.lst_appear.isEmpty() &&
                    LevelBuilder.lst_moving_cubes.isEmpty() &&
                    LevelBuilder.lst_mover_cubes.isEmpty() &&
                    LevelBuilder.lst_dead_cubes.isEmpty()) {
                m_state = Playing;

                if (Easy == m_difficulty) {
                    if (1 == m_level_number) {
                        showTutor(Swipe);
                    }

                    if (2 == m_level_number) {
                        showTutor(MenuPause);
                    }

                    if (3 == m_level_number) {
                        showTutor(MenuHint);
                    }

                    if (4 == m_level_number) {
                        showTutor(Drag);
                    }

                    if (5 == m_level_number) {
                        showTutor(Plain);
                    }

                    if (10 == m_level_number) {
                        showTutor(Moving);
                    }

                    if (12 == m_level_number) {
                        showTutor(Mover);
                    }

                    if (19 == m_level_number) {
                        showTutor(Dead);
                    }
                }                
            } else {
                if (m_timeout > 0.02f) {
                    Cube* cube = m_ad_level.getCubeFromAppearList();

                    if (cube) {
                        m_list_cubes_level.add(cube);
                    }

                    if (!LevelBuilder.lst_moving_cubes.isEmpty()) {
                        MovingCube movingCube = LevelBuilder.lst_moving_cubes.front();
                        LevelBuilder.lst_moving_cubes.pop_front();
                        m_lst_moving_cubes.add(movingCube);
                    }

                    if (!cLevelBuilder::lst_mover_cubes.empty()) {
                        MoverCube moverCube = LevelBuilder.lst_mover_cubes.front();
                        cLevelBuilder::lst_mover_cubes.pop_front();
                        m_lst_mover_cubes.add(moverCube);
                    }

                    if (!cLevelBuilder::lst_dead_cubes.empty()) {
                        DeadCube deadCube = LevelBuilder.lst_dead_cubes.front();
                        LevelBuilder.lst_dead_cubes.pop_front();
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
            m_state = Playing;
        }
    }

    public void updatePlaying() {
        m_player_cube.update();
    }

    public void cLevel::updateUndo() {
        m_timeout_undo -= dt;

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
                m_state = Playing;
            }
            m_timeout_undo = UNDO_TIMEOUT;
        }
    }

    public void drawTheCube() {
        glClear(GL_STENCIL_BUFFER_BIT);

        list<cCube*>::iterator it;

        //glEnable(GL_LIGHTING);

        //glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_gray_concrete);

        Vector light = m_pos_light;
        Vector light_tmp;
        Rotate3D_AroundYAxis(m_pos_light.x, m_pos_light.y, m_pos_light.z, -m_cube_rotation.degree - m_user_rotation.current.y, light.x, light.y, light.z);
        Rotate3D_AroundXAxis(light.x, light.y, light.z, -m_user_rotation.current.x, light_tmp.x, light_tmp.y, light_tmp.z);
        light = light_tmp;

        glEnable(GL_STENCIL_TEST);
        glDisable(GL_CULL_FACE);

        Graphics.setStreamSource();

        float shadowMat[16];

//    if (false)
        for (int j = 0; j < 3; ++j) // 3 times (1 floor + 2 walls)
        {
            Graphics.prepare();

            switch (j) {
                case 0: // floor
                    Utils.calcShadowMatrixFloor(light, shadowMat, 0.0f);
                    for (it = m_list_cubes_wall_y_minus.begin(); it != m_list_cubes_wall_y_minus.end(); ++it) {
                        Graphics.addCubeFace_Y_Plus((*it).tx, (*it).ty, (*it).tz, (*it).color_current);
                    }
                    break;

                case 1: // wall x
                    Utils.calcShadowMatrixWallX(light, shadowMat, 0.0f);
                    for (it = m_list_cubes_wall_x_minus.begin(); it != m_list_cubes_wall_x_minus.end(); ++it) {
                        Graphics.addCubeFace_X_Plus((*it).tx, (*it).ty, (*it).tz, (*it).color_current);
                    }
                    break;

                case 2: // wall z
                    Utils.calcShadowMatrixWallZ(light, shadowMat, 0.0f);
                    for (it = m_list_cubes_wall_z_minus.begin(); it != m_list_cubes_wall_z_minus.end(); ++it) {
                        Graphics.addCubeFace_Z_Plus((*it).tx, (*it).ty, (*it).tz, (*it).color_current);
                    }
                    break;
            } // switch

            glClear(GL_STENCIL_BUFFER_BIT);
            glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
            glStencilFunc(GL_ALWAYS, 0xff, 0xff);

            Graphics.DisableBlending();
            glEnable(GL_LIGHTING);
            glEnable(GL_DEPTH_TEST);
            glEnable(GL_TEXTURE_2D);

            // draw shadow receivers (floor and two walls)
            Graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);

            //----------------------------------------
            // cast shadow on a receiver
            Color shadow_color = new Color(0, 0, 0, (m_fade_value / 2.0f) * 255);

            Graphics.prepare();

            // with level cubes
            for (it = m_list_cubes_level.begin(); it != m_list_cubes_level.end(); ++it) {
                Graphics.addCubeSize((*it).tx + engine.cube_offset.x, (*it).ty + engine.cube_offset.y, (*it).tz + engine.cube_offset.z, HALF_CUBE_SIZE, shadow_color);
            }

            // moving cubes
            if (!m_lst_moving_cubes.isEmpty()) {
                list<cMovingCube*>::iterator it;
                for(it = m_lst_moving_cubes.begin(); it != m_lst_moving_cubes.end(); ++it) {
                    Graphics.addCubeSize((*it).pos.x + engine.cube_offset.x, (*it).pos.y + engine.cube_offset.y, (*it).pos.z + engine.cube_offset.z, HALF_CUBE_SIZE, shadow_color);
                }
            }

            // mover cubes
            if (!m_lst_mover_cubes.isEmpty()) {
                list<cMoverCube*>::iterator it;
                for(it = m_lst_mover_cubes.begin(); it != m_lst_mover_cubes.end(); ++it) {
                    Graphics.addCubeSize((*it).pos.x + engine.cube_offset.x, (*it).pos.y + engine.cube_offset.y, (*it).pos.z + engine.cube_offset.z, HALF_CUBE_SIZE, shadow_color);
                }
            }

            // dead cubes
            if (!m_lst_dead_cubes.isEmpty()) {
                list<cDeadCube*>::iterator it;
                for(it = m_lst_dead_cubes.begin(); it != m_lst_dead_cubes.end(); ++it) {
                    Graphics.addCubeSize((*it).pos.x + engine.cube_offset.x, (*it).pos.y + engine.cube_offset.y, (*it).pos.z + engine.cube_offset.z, HALF_CUBE_SIZE, shadow_color);
                }
            }

            // with player cube
            if (m_player_cube.m_cube_pos.x != 0) {
                Graphics.addCubeSize(m_player_cube.pos.x + engine.cube_offset.x, m_player_cube.pos.y + engine.cube_offset.y, m_player_cube.pos.z + engine.cube_offset.z, HALF_CUBE_SIZE, shadow_color);
            }

            // with key cube
            if (m_cube_pos_key.x != 0) {
                Graphics.addCubeSize(engine.cubes[m_cube_pos_key.x][m_cube_pos_key.y][m_cube_pos_key.z].tx + engine.cube_offset.x,
            }

            Game.cubes[m_cube_pos_key.x][m_cube_pos_key.y][m_cube_pos_key.z].ty + engine.cube_offset.y,
            Game.cubes[m_cube_pos_key.x][m_cube_pos_key.y][m_cube_pos_key.z].tz + engine.cube_offset.z, HALF_CUBE_SIZE, shadow_color);

            Graphics.enableBlending();
            glDisable(GL_LIGHTING);
            glDisable(GL_TEXTURE_2D);
            glDisable(GL_DEPTH_TEST);

            glStencilOp(GL_KEEP, GL_KEEP, GL_ZERO);
            glStencilFunc(GL_EQUAL, 0xff, 0xff);

            glPushMatrix();
            glMultMatrixf(shadowMat);
            Graphics.renderTriangles();
            glPopMatrix();
            //----------------------------------------
        } // for

        glPushMatrix();

        glDisable(GL_STENCIL_TEST);
        glEnable(GL_CULL_FACE);
        Graphics.disableBlending();
        glEnable(GL_LIGHTING);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);

        // draw rest of the floor and walls (not shadow receivers)
        Graphics.prepare();

        for (it = m_list_cubes_wall_y_minus.begin(); it != m_list_cubes_wall_y_minus.end(); ++it) {
            Graphics.addCubeFace_X_Plus((*it).tx, (*it).ty, (*it).tz, (*it).color_current);
            Graphics.addCubeFace_X_Minus((*it).tx, (*it).ty, (*it).tz, (*it).color_current);
            Graphics.addCubeFace_Z_Plus((*it).tx, (*it).ty, (*it).tz, (*it).color_current);
            Graphics.addCubeFace_Z_Minus((*it).tx, (*it).ty, (*it).tz, (*it).color_current);
            Graphics.addCubeFace_Y_Minus((*it).tx, (*it).ty, (*it).tz, (*it).color_current);
        }

        for (it = m_list_cubes_wall_x_minus.begin(); it != m_list_cubes_wall_x_minus.end(); ++it) {
            Graphics.addCubeFace_X_Minus((*it).tx, (*it).ty, (*it).tz, (*it).color_current);
            Graphics.addCubeFace_Y_Plus((*it).tx, (*it).ty, (*it).tz, (*it).color_current);
            Graphics.addCubeFace_Z_Plus((*it).tx, (*it).ty, (*it).tz, (*it).color_current);
            Graphics.addCubeFace_Z_Minus((*it).tx, (*it).ty, (*it).tz, (*it).color_current);
        }

        for (it = m_list_cubes_wall_z_minus.begin(); it != m_list_cubes_wall_z_minus.end(); ++it) {
            Graphics.addCubeFace_X_Plus((*it).tx, (*it).ty, (*it).tz, (*it).color_current);
            Graphics.addCubeFace_X_Minus((*it).tx, (*it).ty, (*it).tz, (*it).color_current);
            Graphics.addCubeFace_Y_Plus((*it).tx, (*it).ty, (*it).tz, (*it).color_current);
            Graphics.addCubeFace_Z_Minus((*it).tx, (*it).ty, (*it).tz, (*it).color_current);
        }

        // draw edges
        for (it = m_list_cubes_edges.begin(); it != m_list_cubes_edges.end(); ++it) {
            Graphics.addCubeSize((*it).tx, (*it).ty, (*it).tz, HALF_CUBE_SIZE, (*it).color_current);
        }

        // level cubes
        for (it = m_list_cubes_level.begin(); it != m_list_cubes_level.end(); ++it) {
            Graphics.addCubeSize((*it).tx, (*it).ty, (*it).tz, HALF_CUBE_SIZE, (*it).color_current);
        }

        list<cMovingCube*>::iterator itmo;
        for (itmo = m_lst_moving_cubes.begin(); itmo != m_lst_moving_cubes.end(); ++itmo) {
            (*itmo).renderCube();
        }

        list<cMoverCube*>::iterator itmu;
        for (itmu = m_lst_mover_cubes.begin(); itmu != m_lst_mover_cubes.end(); ++itmu) {
            (*itmu).renderCube();
        }

        list<cDeadCube*>::iterator itde;
        for (itde = m_lst_dead_cubes.begin(); itde != m_lst_dead_cubes.end(); ++itde) {
            (*itde).renderCube();
        }

        if (Playing != m_state) {
            for (it = m_list_cubes_face.begin(); it != m_list_cubes_face.end(); ++it) {
                Graphics.addCubeSize((*it).tx, (*it).ty, (*it).tz, HALF_CUBE_SIZE, (*it).color_current);
            }

            for (it = m_list_cubes_base.begin(); it != m_list_cubes_base.end(); ++it) {
                Graphics.addCubeSize((*it).tx, (*it).ty, (*it).tz, HALF_CUBE_SIZE, (*it).color_current);
            }
        }

        Graphics.renderTriangles(engine.cube_offset.x, engine.cube_offset.y, engine.cube_offset.z);

        glPopMatrix();
    }

    public void drawTextsCompleted() {
        CubeFont cubeFont;
        TexCoordsQuad coords;
        TexturedQuad pFont;

        Graphics.setStreamSourceFloat();
        Graphics.prepare();

        list<cCubeFont*>::iterator it;
        for (it = m_list_fonts.begin(); it != m_list_fonts.end(); ++it) {
            cubeFont = *it;
            pFont = cubeFont.getFont();

            coords.tx0 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);
            coords.tx1 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
            coords.tx2 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
            coords.tx3 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);

            Graphics.addCubeFace_Z_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.color_current);
        }

        if (m_menu_cube_up.isDone() && m_menu_cube_up.m_cube_pos.x == 7) {
            cubeFont = m_cubefont_up;
            pFont = cubeFont.getFont();

            coords.tx0 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);
            coords.tx1 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
            coords.tx2 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
            coords.tx3 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);

            Graphics.addCubeFace_Z_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.color_current);
        }

        if (m_menu_cube_mid.isDone() && m_menu_cube_mid.m_cube_pos.x == 7) {
            cubeFont = m_cubefont_mid;
            pFont = cubeFont.getFont();

            coords.tx0 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);
            coords.tx1 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
            coords.tx2 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
            coords.tx3 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);

            Graphics.addCubeFace_Z_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.color_current);
        }

        if (m_menu_cube_low.isDone() && m_menu_cube_low.m_cube_pos.x == 7) {
            pCubeFont = m_cubefont_low;
            pFont = cubeFont.getFont();

            coords.tx0 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);
            coords.tx1 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
            coords.tx2 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
            coords.tx3 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);

            Graphics.addCubeFace_Z_Minus(pCubeFont.pos.x, pCubeFont.pos.y, pCubeFont.pos.z, coords, pCubeFont.color_current);
        }

        Graphics.renderTriangles();
    }

    public void drawTextsPaused() {
        CubeFont cubeFont;
        TexCoordsQuad coords;
        TexturedQuad pFont;

        Graphics.setStreamSourceFloat();
        Graphics.prepare();

        list<cCubeFont*>::iterator it;
        for (it = m_list_fonts.begin(); it != m_list_fonts.end(); ++it) {
            cubeFont = *it;
            pFont = pCubeFont.getFont();

            coords.tx0 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
            coords.tx1 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
            coords.tx2 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);
            coords.tx3 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);

            Graphics.addCubeFace_X_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.color_current);
        }

        if (m_menu_cube_up.isDone() && m_menu_cube_up.m_cube_pos.z == 1) {
            cubeFont = m_cubefont_up;
            pFont = pCubeFont.getFont();

            coords.tx0 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
            coords.tx1 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
            coords.tx2 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);
            coords.tx3 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);

            Graphics.addCubeFace_X_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.color_current);
        }

        if (m_menu_cube_mid.IsDone() && m_menu_cube_mid.m_cube_pos.z == 1) {
            cubeFont = m_cubefont_mid;
            pFont = cubeFont.getFont();

            coords.tx0 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
            coords.tx1 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
            coords.tx2 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);
            coords.tx3 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);

            Graphics.AddCubeFace_X_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.color_current);
        }

        if (m_menu_cube_low.IsDone() && m_menu_cube_low.m_cube_pos.z == 1) {
            cubeFont = m_cubefont_low;
            pFont = cubeFont.getFont();

            coords.tx0 = new Vector2(pFont.tx_lo_left.x,  pFont.tx_lo_left.y);
            coords.tx1 = new Vector2(pFont.tx_lo_right.x, pFont.tx_lo_right.y);
            coords.tx2 = new Vector2(pFont.tx_up_right.x, pFont.tx_up_right.y);
            coords.tx3 = new Vector2(pFont.tx_up_left.x,  pFont.tx_up_left.y);

            Graphics.AddCubeFace_X_Minus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, cubeFont.color_current);
        }
        Graphics.renderTriangles();
    }

    public void draw() {
        Color color = new Color(255, 255, 255, m_fade_value * 255);
        Graphics.setStreamSource();

        if (m_fade_value < 1.0f) {
            glEnable(GL_BLEND);
            glDisable(GL_DEPTH_TEST);
        }

        glEnable(GL_LIGHTING);

        if (m_draw_menu_cubes || 0 != m_player_cube.m_cube_pos.x) {
            glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_player);

            Graphics.prepare();

            if (0 != m_player_cube.m_cube_pos.x) {
                Graphics.addCubeSize(m_player_cube.pos.x, m_player_cube.pos.y, m_player_cube.pos.z, HALF_CUBE_SIZE, color);
            }

            if (m_draw_menu_cubes) {
                Graphics.addCube(m_menu_cube_up.pos.x, m_menu_cube_up.pos.y, m_menu_cube_up.pos.z);
                Graphics.addCube(m_menu_cube_mid.pos.x, m_menu_cube_mid.pos.y, m_menu_cube_mid.pos.z);
                Graphics.addCube(m_menu_cube_low.pos.x, m_menu_cube_low.pos.y, m_menu_cube_low.pos.z);
            }
            Graphics.renderTriangles(engine.cube_offset.x, engine.cube_offset.y, engine.cube_offset.z);
        }

        if (0 != m_cube_pos_key.x) {
            glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_key);

            cCube* pCube = &engine.cubes[m_cube_pos_key.x][m_cube_pos_key.y][m_cube_pos_key.z];

            Graphics.prepare();
            Graphics.addCubeSize(pCube.tx, pCube.ty, pCube.tz, HALF_CUBE_SIZE * 0.99f, color);
            Graphics.renderTriangles(engine.cube_offset.x, engine.cube_offset.y, engine.cube_offset.z);
        }

        if (m_fade_value < 1.0f) {
            glDisable(GL_BLEND);
            glEnable(GL_DEPTH_TEST);
        }

        if (!m_lst_moving_cubes.isEmpty() || !m_lst_mover_cubes.isEmpty() || !m_lst_dead_cubes.isEmpty()) {
            list<cMovingCube*>::iterator itmo;
            list<cMoverCube*>::iterator itmu;
            list<cDeadCube*>::iterator itde;

            Graphics.prepare();

            for (itmo = m_lst_moving_cubes.begin(); itmo != m_lst_moving_cubes.end(); ++itmo) {
                (*itmo).renderSymbols();
            }

            for (itmu = m_lst_mover_cubes.begin(); itmu != m_lst_mover_cubes.end(); ++itmu) {
                (*itmu).renderSymbols();
            }

            for (itde = m_lst_dead_cubes.begin(); itde != m_lst_dead_cubes.end(); ++itde) {
                (*itde).renderSymbols();
            }

            glDisable(GL_LIGHTING);
            glEnable(GL_BLEND);
            Graphics.setStreamSourceFloatAndColor();
            glBindTexture(GL_TEXTURE_2D, engine.texture_id_symbols);
            Graphics.renderTriangles(engine.cube_offset.x, engine.cube_offset.y, engine.cube_offset.z);
        }

        if (m_menu_cube_hilite) {
            glBindTexture(GL_TEXTURE_2D, engine.texture_id_symbols);
            color.a = m_hilite_alpha * 255;
            glDisable(GL_DEPTH_TEST);
            glDisable(GL_CULL_FACE);
            glDisable(GL_LIGHTING);

            TexCoordsQuad coords;

            TexturedQuad* p = m_font_hilite.getFont();
            coords.tx0 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
            coords.tx1 = new Vector2(p.tx_up_left.x,  p.tx_up_left.y);
            coords.tx2 = new Vector2(p.tx_lo_left.x,  p.tx_lo_left.y);
            coords.tx3 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);

            Graphics.prepare();
            Graphics.setStreamSourceFloatAndColor();

            switch (m_state) {
                case Paused: //Face_X_Minus:
                    Graphics.addCubeFace_X_Minus(m_font_hilite.pos.x, m_font_hilite.pos.y, m_font_hilite.pos.z, coords, color);
                    break;

                case Completed: //Face_Z_Minus:
                    Graphics.addCubeFace_Z_Minus(m_font_hilite.pos.x, m_font_hilite.pos.y, m_font_hilite.pos.z, coords, color);
                    break;

                default:
                    break;
            }
            glEnable(GL_BLEND);
            Graphics.renderTriangles(engine.cube_offset.x, engine.cube_offset.y, engine.cube_offset.z);

            glEnable(GL_LIGHTING);
            glEnable(GL_DEPTH_TEST);
            glEnable(GL_CULL_FACE);
        }

        if (m_draw_texts) {
            glPushMatrix();
            glTranslatef(engine.cube_offset.x, engine.cube_offset.y, engine.cube_offset.z);

            glEnable(GL_TEXTURE_2D);
            glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_fonts);

            glEnable(GL_BLEND);
            glDisable(GL_LIGHTING);
            glDisableClientState(GL_NORMAL_ARRAY);

            if (m_state == AnimToPaused || m_state == AnimFromPaused || m_state == Paused) {
                DrawTextsPaused();
            } else {
                DrawTextsCompleted();
            }
            glPopMatrix();
        }
    }
    
    publci void Render() {
        //return RenderForPicking(RenderOnlyHUD);

        Graphics.setProjection2D();
        Graphics.setModelViewMatrix2D();

        glEnable(GL_BLEND);
        glDisable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);
        glDepthMask(GL_FALSE);

        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);

        Color color = new Color(255, 255, 255, engine.dirty_alpha);
        Graphics.DrawFBOTexture(Graphics.texture_id_dirty, color);

        glDepthMask(GL_TRUE);

        Graphics.setProjection3D();
        Graphics.setModelViewMatrix3D(m_camera_current);

        #ifdef DRAW_AXES_GLOBAL
        glDisable(GL_TEXTURE_2D);
        Graphics.drawAxes();
        glEnable(GL_TEXTURE_2D);
        #endif

        final Vector4 lightPosition(m_pos_light.x, m_pos_light.y, m_pos_light.z, 1.0f);
        glLightfv(GL_LIGHT0, GL_POSITION, lightPosition.Pointer());

        glEnable(GL_LIGHTING);

        glPushMatrix();
        glRotatef(m_user_rotation.current.x, 1.0f, 0.0f, 0.0f);
        glRotatef(m_user_rotation.current.y, 0.0f, 1.0f, 0.0f);

        glPushMatrix();
        glRotatef(m_cube_rotation.degree, m_cube_rotation.axis.x, m_cube_rotation.axis.y, m_cube_rotation.axis.z);

        drawTheCube();

        #ifdef DRAW_AXES_CUBE
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_LIGHTING);
        Graphics.drawAxes();
        glEnable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);
        #endif

        draw();

        glPopMatrix();

        glPopMatrix();

        m_hud.render();
    }

    public void renderForPicking(PickRenderTypeEnum type) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glDisable(GL_LIGHTING);
        Graphics.disableBlending();
        glEnable(GL_DEPTH_TEST);

        glDisable(GL_TEXTURE_2D);

        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);

        Graphics.setProjection3D();
        Graphics.setModelViewMatrix3D(m_camera_current);

        glPushMatrix();

        switch (type) {
            case RenderOnlyHUD:
                m_hud.renderForPicking();
                break;

            case RenderOnlyMovingCubes:
                Graphics.prepare();
                Graphics.setStreamSource();

                glRotatef(m_cube_rotation.degree, m_cube_rotation.axis.x, m_cube_rotation.axis.y, m_cube_rotation.axis.z);

                Graphics.addCubeSize(m_menu_cube_up.pos.x, m_menu_cube_up.pos.y, m_menu_cube_up.pos.z, HALF_CUBE_SIZE * 1.5f, m_menu_cube_up.color);
                Graphics.addCubeSize(m_menu_cube_mid.pos.x, m_menu_cube_mid.pos.y, m_menu_cube_mid.pos.z, HALF_CUBE_SIZE * 1.5f, m_menu_cube_mid.color);
                Graphics.addCubeSize(m_menu_cube_low.pos.x, m_menu_cube_low.pos.y, m_menu_cube_low.pos.z, HALF_CUBE_SIZE * 1.5f, m_menu_cube_low.color);

                Graphics.renderTriangles(engine.cube_offset.x, engine.cube_offset.y, engine.cube_offset.z);

                break;

            default:
                break;
        } // switch

        glPopMatrix();
    }

    public void renderToFBO() {
        //return Render();

        Graphics.setProjection2D();
        Graphics.setModelViewMatrix2D();

        glEnable(GL_BLEND);
        glDisable(GL_LIGHTING);
        glDepthMask(GL_FALSE);
        glEnable(GL_TEXTURE_2D);

        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);

        Color color_dirty(255, 255, 255, Graphics.dirty_alpha);
        Graphics.drawFBOTexture(Graphics.texture_id_dirty, color_dirty);

        glDepthMask(GL_TRUE);

        Graphics.setProjection3D();
        Graphics.setModelViewMatrix3D(m_camera_current);

        glEnable(GL_LIGHTING);
        //glEnable(GL_LIGHT0);
        const vec4 lightPosition(m_pos_light.x, m_pos_light.y, m_pos_light.z, 1.0f);
        glLightfv(GL_LIGHT0, GL_POSITION, lightPosition.Pointer());

        glRotatef(m_cube_rotation.degree, m_cube_rotation.axis.x, m_cube_rotation.axis.y, m_cube_rotation.axis.z);
        drawTheCube();

        Graphics.enableBlending();

        if (!m_lst_moving_cubes.empty() || !m_lst_mover_cubes.empty() || !m_lst_dead_cubes.empty()) {
            list<cMovingCube*>::iterator itmo;
            list<cMoverCube*>::iterator itmu;
            list<cDeadCube*>::iterator itde;

            Graphics.prepare();

            for (itmo = m_lst_moving_cubes.begin(); itmo != m_lst_moving_cubes.end(); ++itmo) {
                (*itmo).renderSymbols();
            }

            for (itmu = m_lst_mover_cubes.begin(); itmu != m_lst_mover_cubes.end(); ++itmu) {
                (*itmu).renderSymbols();
            }

            for (itde = m_lst_dead_cubes.begin(); itde != m_lst_dead_cubes.end(); ++itde) {
                (*itde).renderSymbols();
            }

            glDisable(GL_LIGHTING);
            glEnable(GL_BLEND);
            Graphics.setStreamSourceFloatAndColor();
            glBindTexture(GL_TEXTURE_2D, engine.texture_id_symbols);
            Graphics.renderTriangles(engine.cube_offset.x, engine.cube_offset.y, engine.cube_offset.z);
        }

        Graphics.setStreamSource();
        glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_key);

        Color color = new Color(255, 255, 255, 255);

        if (0 != m_cube_pos_key.x) {
            glEnable(GL_LIGHTING);
            glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_key);

            Cube cube = Game.cubes[m_cube_pos_key.x][m_cube_pos_key.y][m_cube_pos_key.z];

            Graphics.Prepare();
            Graphics.AddCubeSize(cube.tx, cube.ty, cube.tz, HALF_CUBE_SIZE * 0.99f, color);
            Graphics.RenderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);

            glDisable(GL_LIGHTING);
        }

        Graphics.disableBlending();

        // draw player cube
        Graphics.prepare();
        //Graphics.SetStreamSource();
        glEnable(GL_LIGHTING);
        //Color color(255, 255, 255, 255);
        glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_player);
        Graphics.addCubeSize(m_player_cube.pos.x, m_player_cube.pos.y, m_player_cube.pos.z, HALF_CUBE_SIZE, color);
        Graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);

        glDisable(GL_LIGHTING);
    }

    public void eventBuildLevel()
    {
        switch (m_difficulty) {
            case Easy: LevelBuilderEasy.build(m_level_number); break;
            case Normal: LevelBuilderNormal.build(m_level_number); break; 
            case Hard: LevelBuilderHard.build(m_level_number); break;
        }

        m_cube_pos_key = LevelBuilder.key;

        m_player_cube.setCubePos(cLevelBuilder::player);
        m_player_cube.setKeyCubePos(m_cube_pos_key);

        if (rand()%2 == 0) {
            m_ad_level.setLevelAndDirection(0, 1);
        } else {
            m_ad_level.setLevelAndDirection(MAX_CUBE_COUNT - 1, -1);
        }

        char str[512];

        m_hud.set1stHint();

        // level
        engine.getLevelTypeAndNumberString(m_difficulty, m_level_number, str);
        m_hud.setTextLevel(str);

        // stars
        m_hud.setTextStars(Cubetraz.getStarCount());

        // moves
        m_hud.setTextMoves(0);

        // motto
        Creator.getLevelMotto(m_difficulty, m_level_number, str);
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
            
            list<cCube*>::iterator it;
            for (it = m_list_cubes_hint.begin(); it != m_list_cubes_hint.end(); ++it) {
                m_ar_hint_cubes[i++] = (*it);
            }
        }
    }

    public void eventSolver() {
        if (ShowSceneSolvers == m_next_action || PrepareSolving == m_state) {
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
            m_state = PrepareSolving;
            m_timeout = 2.0f;
        } else {
            m_next_action = ShowSceneSolvers;
        }

        m_hud.setupDisappear();
    }

    public void eventQuit() {
        engine.anim_init_data.list_cubes_base.clear();

        list<cCube*>::iterator it;
        for (it = m_list_cubes_base.begin(); it != m_list_cubes_base.end(); ++it) {
            engine.anim_init_data.list_cubes_base.add(*it);
        }

        for (it = m_list_cubes_edges.begin(); it != m_list_cubes_edges.end(); ++it) {
            engine.anim_init_data.list_cubes_base.add(*it);
        }

        engine.anim_init_data.cube_rotation_degree = m_cube_rotation.degree;

        if (Paused == m_state) {
            engine.anim_init_data.type = AnimToMenuFromPaused;
        } else {
            engine.anim_init_data.type = AnimToMenuFromCompleted;
        }

        engine.showScene(Scene_Anim);
    }

    public void eventLevelPause() {
        m_hud.setupDisappear();
        setAnimToPaused();
    }

    public void eventLevelComplete() {
        Game.playSound(SOUND_LEVEL_COMPLETED);

        Game.renderToFBO(this);

        m_hud.setupDisappear();

        StatInitData& sid = engine.stat_init_data;

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
        m_state = SetupAnimToCompleted;
        Game.showScene(Scene_Stat);
    }

    public void reset() {
        m_moves_counter = 0;
        m_lst_undo.erase(m_lst_undo.begin(), m_lst_undo.end());
        m_lst_undo.clear();

        m_hud.set1stHint();
        m_hud.setTextMoves(m_moves_counter);
        m_hud.setTextUndo( m_lst_undo.size() );

        m_player_cube.setCubePos(cLevelBuilder::player);

        Game.resetCubesColors();

        if (!m_lst_moving_cubes.isEmpty()) {
            list<cMovingCube*>::iterator it;
            for (it = m_lst_moving_cubes.begin(); it != m_lst_moving_cubes.end(); ++it) {
                (*it).reposition();
            }
        }

        if (!m_lst_mover_cubes.empty()) {
            list<cMoverCube*>::iterator it;
            for (it = m_lst_mover_cubes.begin(); it != m_lst_mover_cubes.end(); ++it) {
                (*it).reposition();
            }
        }

        if (!m_lst_dead_cubes.empty()) {
            list<cDeadCube*>::iterator it;
            for (it = m_lst_dead_cubes.begin(); it != m_lst_dead_cubes.end(); ++it) {
                (*it).reposition();
            }
        }
    }

    public void eventUndo() {
        m_timeout_undo = UNDO_TIMEOUT;

        if (m_moves_counter > 0) {
            --m_moves_counter;
            m_hud.setTextMoves(m_moves_counter);

            UndoData ud = m_lst_undo.back();
            m_lst_undo.pop_back();

            m_player_cube.setCubePos(ud.player_pos);

            if (null != ud.moving_cube) {
                ud.moving_cube.init(ud.moving_cube_pos, ud.moving_cube_move_dir);
            }

            if (0 == m_moves_counter) {
                m_hud.set1stHint();
            }

            m_state = Undo;
        }
    }

    public void onFingerDown(float x, float y, int finger_count) {
        m_fingerdown = true;

        m_pos_down.x = x;
        m_pos_down.y = y;

        if (1 == finger_count)
        {
            Color down_color;

            switch (m_state) {
                case Playing:
                    if ( DoneHUD == m_hud.getState() ) {
                        renderForPicking(RenderOnlyHUD);

                        down_color = Graphics.getColorFromScreen(m_pos_down);
                        switch (down_color.b) {
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
                    renderForPicking(RenderOnlyMovingCubes);

                    down_color = Graphics.getColorFromScreen(m_pos_down);
                    MenuCube menuCube = getMovingCubeFromColor(down_color.b);

                    if (menuCube) {
                        m_hilite_alpha = 0.0f;
                        m_menu_cube_hilite = pMenuCube;
                        CubePos cp = m_menu_cube_hilite.m_cube_pos;
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
        m_pos_up.x = x;
        m_pos_up.y = y;

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

    public void onFingerMove(float prev_x, float prev_y, float cur_x, float cur_y, int finger_count) {
        m_pos_move.x = cur_x;
        m_pos_move.y = cur_y;

        float dist = getDistance2D(m_pos_down, m_pos_move);

        //printf("\nOnFingerMove: %.2f", dist);

        if (dist > 20.0f * engine.device_scale) {
            m_swipe = true;
        }

        if (2 == finger_count) {
            if (Playing == m_state) {
                Vector2 dir = new Vector2();
                dir.x = (m_pos_down.x - cur_x) / Graphics.device_scale;
                dir.y = (m_pos_down.y - cur_y) / Graphics.device_scale;

                m_alter_view = true;

                m_user_rotation.current.x = -dir.y * 0.3f;
                m_user_rotation.current.y = -dir.x * 0.3f;
                m_user_rotation.clamp();

                m_swipe = false;
            }
        }
    }

    public void fingerUpPaused(float x, float y) {
//    float fingerup_x = x;
//    float fingerup_y = y;

        if (m_swipe) {
            m_swipe = false;
            renderForPicking(RenderOnlyMovingCubes);
            Color down_color = Graphics.getColorFromScreen(m_pos_down);

            SwipeDirEnums swipeDir;
            float length;
            Graphics.getSwipeDirAndLength(m_pos_down, m_pos_up, swipeDir, length);

            if (length > 30.0f * Graphics.m_scaleFactor) {
                MenuCube menuCube = null;

                switch (down_color.b) {
                    case 200: menuCube = m_menu_cube_up; break;
                    case 150: menuCube = m_menu_cube_mid; break;
                    case 100: menuCube = m_menu_cube_low; break;
                }

                if (menuCube) {
                    switch (swipeDir) {
                        case SwipeRight: menuCube.moveOnAxis(Z_Plus); break;
                        case SwipeLeft:  menuCube.moveOnAxis(Z_Minus); break;
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

        if (m_swipe) {
            m_swipe = false;

            renderForPicking(RenderOnlyMovingCubes);
            Color down_color = Graphics.getColorFromScreen(m_pos_down);

//		printf("\nOnFingerUp [SWIPE] color is: %d, %d, %d, %d", down_color.r, down_color.g, down_color.b, down_color.a);

            SwipeDirEnums swipeDir;
            float length;
            Engine.getSwipeDirAndLength(m_pos_down, m_pos_up, swipeDir, length);

            if (length > 30.0f * Graphics.m_scaleFactor) {
                MenuCube menuCube = null;

                switch (down_color.b) {
                    case 200: menuCube = m_menu_cube_up; break;
                    case 150: menuCube = m_menu_cube_mid; break;
                    case 100: menuCube = m_menu_cube_low; break;
                }

                if (menuCube) {
                    switch (swipeDir) {
                        case SwipeLeft: menuCube.moveOnAxis(X_Plus); break;
                        case SwipeRight: menuCube.moveOnAxis(X_Minus); break;
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
            m_swipe = false;
            return;
        }

        if (m_alter_view) {
            if ( Math.abs(m_user_rotation.current.x) > EPSILON || Math.abs(m_user_rotation.current.y) > EPSILON ) {
                m_reposition_view = true;
                m_alter_view = false;
                m_t = 0.0f;
                m_user_rotation.from = m_user_rotation.current;
            }
            return;
        }

        if (!m_swipe) {
            if (Playing == m_state) {
                renderForPicking(RenderOnlyHUD);
                Vector2 pos = new Vector2(x,y);
                Color color = Graphics.getColorFromScreen(pos);

                switch (color.b) {
                    case 200: eventLevelPause(); break;
                    case 150: eventUndo(); break;
                    case 100: eventShowHint(); break;
                    case 50: eventSolver(); break;
                    default:
                        break;
                }

            }
            return;
        } else { // swipe        
            m_swipe = false;

            if (m_hud.isAnythingHilited()) {
                return;
            }

            // flip the Y-axis because pixel coordinates increase toward the bottom (iPhone 3D Programming 86. page)
            float fingerdown_x = m_pos_down.x;
            float fingerdown_y = -m_pos_down.y;

            float fingerup_x = x;
            float fingerup_y = -y;

            Vector2 dir = new Vector2();
            dir.x = fingerup_x - fingerdown_x;
            dir.y = fingerup_y - fingerdown_y;

            float len = dir.Length(); // swipe length

            //printf("\nSwipe Length: %.2f", len);

            if (len < 20.0f * Graphics.device_scale){
                return;
            }

            dir.normalize();

            float degree = TO_DEG(atan2(dir.y, dir.x));

            //            printf("\nfinger down x:%f, y:%f", m_fingerdown_x, m_fingerdown_y);
            //            printf("\nfinger up x:%f, y:%f", fingerup_x, fingerup_y);
            //            printf("\ndir.x:%f, dir.y:%f", dir.x, dir.y);
            //
            //printf("\ndegree: %0.2f", degree);

            bool success = false;
            float center;
            const float space = 30.0f;

            center = 90.0f; // Y plus
            if (degree > (center - space) && degree < (center + space)) {
                success = m_player_cube.MoveOnAxis(Y_Plus);
            }

            center = -90.0f; // Y minus
            if (degree > (center - space) && degree < (center + space)) {
                success = m_player_cube.MoveOnAxis(Y_Minus);
            }

            center = 30.0f; // Z plus
            if (degree > (center - space) && degree < (center + space)) {
                success = m_player_cube.MoveOnAxis(Z_Minus);
            }

            center = -150.0f; // Z minus
            if (degree > (center - space) && degree < (center + space)) {
                success = m_player_cube.MoveOnAxis(Z_Plus);
            }

            center = -30.0f; // X minus
            if (degree > (center - space) && degree < (center + space)) {
                success = m_player_cube.MoveOnAxis(X_Plus);
            }

            center = 150.0f; // X plus
            if (degree > (center - space) && degree < (center + space)) {
                success = m_player_cube.MoveOnAxis(X_Minus);
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

        if (m_player_cube.m_moving_cube) {
            ud.moving_cube = m_player_cube.m_moving_cube;
            ud.moving_cube_pos = m_player_cube.m_moving_cube.GetCubePos();
            ud.moving_cube_move_dir = m_player_cube.m_moving_cube.GetMovement();
        }

        if (m_player_cube.m_mover_cube) {
            CubePos cp = m_player_cube.m_cube_pos_destination;
            CubePos cp_new = cp;

            // check if cube can move in a selected dir
            m_player_cube.calcMovement(cp_new, m_player_cube.m_mover_cube.GetMoveDir(), false);

            if (cp.x != cp_new.x || cp.y != cp_new.y || cp.z != cp_new.z) {
                ud.mover_cube = m_player_cube.m_mover_cube;
            }
        }

        m_lst_undo.add(ud);

        m_hud.setTextUndo( m_lst_undo.size() );

        m_state_to_restore = m_state;
        m_state = MovingPlayer;
        m_timeout = 0.0f; // move immediately
    }

}

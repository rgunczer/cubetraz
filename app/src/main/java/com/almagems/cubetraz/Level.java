package com.almagems.cubetraz;

import java.lang.reflect.Array;
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

    private Camera m_camera_level;
    private Camera m_camera_level_completed;
    private Camera m_camera_level_paused;
    private Camera m_camera_current;

    private float m_target_rotation_degree;

    private EaseOutDivideInterpolation m_interpolator;

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

    private ArrayList<CubeFont> m_list_fonts_pool;
    private ArrayList<CubeFont> m_list_fonts;

// spec cubes
    private ArrayList<MovingCube> m_lst_moving_cubes;
    private ArrayList<MoverCube> m_lst_mover_cubes;
    private ArrayList<DeadCube> m_lst_dead_cubes;

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

    private MenuCube getMovingCubeFromColor(int color) {
        switch (color) {
            case 200: return m_menu_cube_up;
            case 150: return m_menu_cube_mid;
            case 100: return m_menu_cube_low;
        }

        return null;
    }

    public CubePos GetKeyPos() {
        return m_cube_pos_key;
    }

    public int GetLevelNumber() {
        return m_level_number;
    }

    public DifficultyEnum GetDifficulty() {
        return m_difficulty;
    }


    public PlayerCube m_player_cube;



    cLevel::cLevel()
    {
        float d = 1.0f;

        m_anim_undo = false;

        m_level_number = 1;

        m_pos_light = vec3(3.0f, 5.0f, 20.0f);

        m_camera_level.eye = vec3(0.0f, 20.0f, 31.0f);
        m_camera_level.target = vec3(0.0f, -1.6f, 0.0f);

        m_camera_level_completed.eye = vec3(0.0f, 5.0f, 34.0f);
        m_camera_level_completed.target = vec3(0.0f, -1.7f, 0.0f);

        m_camera_level_paused.eye = vec3(0.0f, 5.0f, 34.0f);
        m_camera_level_paused.target = vec3(0.0f, -1.7f, 0.0f);

        switch (engine->device_type)
        {
            case Device_iPhone4inch:
                d = 1.26f;
                break;

            case Device_iPhone35inch:
                d = 1.075f;
                break;

            case Device_iPad:
                d = 0.99f;
                break;

            default:
                break;
        }

        m_camera_level.eye = m_camera_level.eye * d;

        m_camera_level_completed.eye = m_camera_level_completed.eye * d;
        m_camera_level_completed.target = m_camera_level_completed.target * d;

        m_camera_level_paused.eye = m_camera_level_paused.eye * d;
        m_camera_level_paused.target = m_camera_level_paused.target * d;

        m_player_cube = new cPlayerCube();
        m_player_cube->Init(CubePos(4, 4, 4));

        m_menu_cube_up = new cMenuCube();
        m_menu_cube_up->Init(CubePos(0, 0, 0), Color(0, 0, 200, 255));

        m_menu_cube_mid = new cMenuCube();
        m_menu_cube_mid->Init(CubePos(0, 0, 0), Color(0, 0, 150, 255));

        m_menu_cube_low = new cMenuCube();
        m_menu_cube_low->Init(CubePos(0, 0, 0), Color(0, 0, 100, 255));

        m_reposition_view = false;
        m_show_hint_2nd = false;
    }

    cLevel::~cLevel()
    {
        if (m_player_cube)
        {
            delete m_player_cube;
            m_player_cube = NULL;
        }
    }

//void cLevel::CheckCubes()
//{
//    cCube* pCube;
//
//    for (int x = 0; x < MAX_CUBE_COUNT; ++x)
//    {
//        for (int y = 0; y < MAX_CUBE_COUNT; ++y)
//        {
//            for (int z = 0; z < MAX_CUBE_COUNT; ++z)
//            {
//                pCube = &engine->cubes[x][y][z];
//
//                bool a[] = { false, false, false, false, false, false, false };
//
//                a[0] = engine->IsCubeOnAList(pCube, m_list_cubes_level);
//                a[1] = engine->IsCubeOnAList(pCube, m_list_cubes_wall_y_minus);
//                a[2] = engine->IsCubeOnAList(pCube, m_list_cubes_wall_x_minus);
//                a[3] = engine->IsCubeOnAList(pCube, m_list_cubes_wall_z_minus);
//                a[4] = engine->IsCubeOnAList(pCube, m_list_cubes_edges);
//                a[5] = engine->IsCubeOnAList(pCube, m_list_cubes_base);
//                a[6] = engine->IsCubeOnAList(pCube, m_list_cubes_face);
//
//                int counter = 0;
//                for (int i = 0; i < 7; ++i)
//                {
//                    if (a[i])
//                        ++counter;
//                }
//
//                //if (counter > 1)
//                //{
//                //    printf("\n\nHERE!!!\n\n");
//                //}
//                //                else
//                //                {
//                //                    printf("\nOK: %d", counter);
//                //                }
//
//                int count[] = { 0, 0 };
//                count[0] = engine->CountOnAList(pCube, m_ad_base.lst_appear);
//                count[1] = engine->CountOnAList(pCube, m_ad_base.lst_disappear);
//
//                if (count[0] > 1 || count[1] > 1)
//                {
//                    printf("\nagain HERE!!!");
//                }
//            }
//        }
//    }
//}

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
            engine->ReportAchievement("com.almagems.cubetraz.easy01facesolved", percent);
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
            engine->ReportAchievement("com.almagems.cubetraz.easy02facesolved", percent);
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
            engine->ReportAchievement("com.almagems.cubetraz.easy03facesolved", percent);
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
            engine->ReportAchievement("com.almagems.cubetraz.easy04facesolved", percent);
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
            engine->ReportAchievement("com.almagems.cubetraz.normal01facesolved", percent);
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
            engine->ReportAchievement("com.almagems.cubetraz.normal02facesolved", percent);
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
            engine->ReportAchievement("com.almagems.cubetraz.normal03facesolved", percent);
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
            engine->ReportAchievement("com.almagems.cubetraz.normal04facesolved", percent);
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
            engine->ReportAchievement("com.almagems.cubetraz.hard01facesolved", percent);
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
            engine->ReportAchievement("com.almagems.cubetraz.hard02facesolved", percent);
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
            engine->ReportAchievement("com.almagems.cubetraz.hard03facesolved", percent);
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
            engine->ReportAchievement("com.almagems.cubetraz.hard04facesolved", percent);
        }
    }

    #pragma mark - Init

    void cLevel::Init(void* init_data)
    {
        m_moving_cube = NULL;
        m_mover_cube = NULL;
        m_dead_cube = NULL;

        //engine->level_init_data.difficulty = Easy;
        //engine->level_init_data.level_number = 12;

        m_tutor_index = 0;
        m_next_action = NoNextAction;

        LevelInitData* lid = (LevelInitData*)init_data;

        switch (lid->init_action)
        {
            case FullInit:
            {
                m_menu_cube_hilite = NULL;
                m_show_hint_2nd = false;

                m_hilite_timeout = 0.0f;

                m_difficulty = lid->difficulty;
                m_level_number = lid->level_number;

                SetupMusic();

                CubePos zero(0,0,0);
                m_player_cube->Init(zero);
                m_cube_pos_key = zero;

                m_lst_moving_cubes.clear();
                m_lst_mover_cubes.clear();
                m_lst_dead_cubes.clear();

                m_moves_counter = 0;
                m_lst_undo.erase(m_lst_undo.begin(), m_lst_undo.end());
                m_lst_undo.clear();

                m_hud.Set1stHint();
                m_hud.SetTextMoves(m_moves_counter);
                m_hud.SetTextUndo( m_lst_undo.size() );

                m_fingerdown = false;
                m_swipe = false;

                m_cube_rotation.degree = -45.0f;
                m_cube_rotation.axis = vec3(0.0f, 1.0f, 0.0f);

                m_user_rotation.Reset();

                m_camera_current = m_camera_level;

                m_list_cubes_level.clear();
                m_list_cubes_wall_y_minus.clear();
                m_list_cubes_wall_x_minus.clear();
                m_list_cubes_wall_z_minus.clear();
                m_list_cubes_edges.clear();
                m_list_cubes_base.clear();
                m_list_cubes_face.clear();
                m_list_cubes_hint.clear();

                m_hud.Init();

                SetupCubesForLevel();

                SetupAppear();
            }
            break;

            case JustContinue: // take no action ?

                if (SetupAnimToCompleted == m_state)
                {
                }
                else
                {
                    m_hud.SetupAppear();
                }

                break;

            case ShowSolution:

                Reset();

                m_state = PrepareSolving;
                m_timeout = 3.5f;

                break;
        }

        engine->SetProjection3D();
        engine->SetModelViewMatrix3D(m_camera_current);

        //const vec4 lightPosition(m_pos_light.x, m_pos_light.y, m_pos_light.z, 1.0f);
        //glLightfv(GL_LIGHT0, GL_POSITION, lightPosition.Pointer());

        glEnable(GL_LIGHT0);
    }

    void cLevel::GetRatings()
    {
        char sign;

        StatInitData& sid = engine->stat_init_data;

        if (m_moves_counter < m_min_solution_steps)
        {
            sign = '<';
            strcpy(sid.str_title, "EXPERT");
            sid.stars = 3;
        }

        if (m_moves_counter == m_min_solution_steps)
        {
            sign = '=';
            strcpy(sid.str_title, "PERFECT");
            sid.stars = 3;
        }

        if (m_moves_counter > m_min_solution_steps && m_moves_counter <= m_min_solution_steps + 1)
        {
            sign = '>';
            strcpy(sid.str_title, "EXCELLENT");
            sid.stars = 3;
        }

        if (m_moves_counter > m_min_solution_steps + 1 && m_moves_counter <= m_min_solution_steps + 4)
        {
            sign = '>';
            strcpy(sid.str_title, "GREAT");
            sid.stars = 2;
        }

        if (m_moves_counter > m_min_solution_steps + 4)
        {
            sign = '>';
            strcpy(sid.str_title, "GOOD");
            sid.stars = 1;
        }

        sprintf(sid.str_moves, "PLAYER:%d %c BEST:%d", m_moves_counter, sign, m_min_solution_steps);
    }


    #pragma mark - Show

    void cLevel::ShowTutor(int index)
    {
        m_state = Tutorial;
        m_hud.ClearTutors();

        switch (index)
        {
            case Swipe:
                m_hud.ShowTutorSwipeAndGoal();
                break;

            case Drag:
                m_hud.ShowTutorDrag();
                break;

            case Moving:
                m_hud.ShowTutorMoving();
                break;

            case Mover:
                m_hud.ShowTutorMover();
                break;

            case Dead:
                m_hud.ShowTutorDead();
                break;

            case Plain:
                m_hud.ShowTutorPlain();
                break;

            case MenuPause:
                m_hud.ShowTutorMenuPause();
                break;

            case MenuUndo:
                m_hud.ShowTutorMenuUndo();
                break;

            case MenuHint:
                m_hud.ShowTutorMenuHint();
                break;

            case MenuSolvers:
                m_hud.ShowTutorMenuSolvers();
                break;

            default:
                break;
        }
    }

    #pragma mark - Is

    bool cLevel::IsMovingCube(CubePos& cube_pos, bool set)
    {
        cMovingCube* pMovingCube;

        list<cMovingCube*>::iterator it;
        for(it = m_lst_moving_cubes.begin(); it != m_lst_moving_cubes.end(); ++it)
        {
            pMovingCube = *it;
            CubePos cp = pMovingCube->GetCubePos();

            if (cp.x == cube_pos.x && cp.y == cube_pos.y && cp.z == cube_pos.z)
            {
                if (set)
                    m_player_cube->SetMovingCube(pMovingCube);

                return true;
            }
        }
        return false;
    }

    bool cLevel::IsMoverCube(CubePos& cube_pos, bool set)
    {
        cMoverCube* p;

        list<cMoverCube*>::iterator it;
        for(it = m_lst_mover_cubes.begin(); it != m_lst_mover_cubes.end(); ++it)
        {
            p = *it;
            CubePos cp = p->GetCubePos();

            if (cp.x == cube_pos.x && cp.y == cube_pos.y && cp.z == cube_pos.z)
            {
                if (set)
                {
                    if (m_mover_cube != p) // bugfix! when player hits the mover cube from behind!
                        m_player_cube->SetMoverCube(p);
                }

                return true;
            }
        }
        return false;
    }

    bool cLevel::IsDeadCube(CubePos& cube_pos, bool set)
    {
        cDeadCube* p;

        list<cDeadCube*>::iterator it;
        for(it = m_lst_dead_cubes.begin(); it != m_lst_dead_cubes.end(); ++it)
        {
            p = *it;
            CubePos cp = p->GetCubePos();

            if (cp.x == cube_pos.x && cp.y == cube_pos.y && cp.z == cube_pos.z)
            {
                if (set)
                    m_player_cube->SetDeadCube(p);

                return true;
            }
        }
        return false;
    }

    bool cLevel::IsSpecCubeObstacle(CubePos& cube_pos,
                                    cMovingCube* ignore_moving_cube,
                                    cMoverCube* ignore_mover_cube,
                                    cDeadCube* ignore_dead_cube)
    {
        CubePos cp;

        if (!m_lst_dead_cubes.empty())
        {
            cDeadCube* p;
            list<cDeadCube*>::iterator it;
            for(it = m_lst_dead_cubes.begin(); it != m_lst_dead_cubes.end(); ++it)
            {
                p = *it;

                if (p != ignore_dead_cube)
                {
                    cp = p->GetCubePos();

                    if (cp.x == cube_pos.x && cp.y == cube_pos.y && cp.z == cube_pos.z)
                        return true;
                }
            }
        }

        if (!m_lst_mover_cubes.empty())
        {
            cMoverCube* p;
            list<cMoverCube*>::iterator it;
            for(it = m_lst_mover_cubes.begin(); it != m_lst_mover_cubes.end(); ++it)
            {
                p = *it;

                if (p != ignore_mover_cube)
                {
                    cp = p->GetCubePos();

                    if (cp.x == cube_pos.x && cp.y == cube_pos.y && cp.z == cube_pos.z)
                        return true;
                }
            }
        }

        if (!m_lst_moving_cubes.empty())
        {
            cMovingCube* p;
            list<cMovingCube*>::iterator it;
            for(it = m_lst_moving_cubes.begin(); it != m_lst_moving_cubes.end(); ++it)
            {
                p = *it;

                if (p != ignore_moving_cube)
                {
                    cp = p->GetCubePos();

                    if (cp.x == cube_pos.x && cp.y == cube_pos.y && cp.z == cube_pos.z)
                        return true;
                }
            }
        }

        return false;
    }

    #pragma mark - Setup

    void cLevel::SetupMusic()
    {
        switch (m_difficulty)
        {
            case Easy:

                if (m_level_number >= 1 && m_level_number <= 15)
                    engine->PlayMusic(MUSIC_BREEZE);

                if (m_level_number >= 16 && m_level_number <= 30)
                    engine->PlayMusic(MUSIC_DRONES);

                if (m_level_number >= 31 && m_level_number <= 45)
                    engine->PlayMusic(MUSIC_WAVES);

                if (m_level_number >= 46 && m_level_number <= 60)
                    engine->PlayMusic(MUSIC_BREEZE);

                break;

            case Normal:

                if (m_level_number >= 1 && m_level_number <= 15)
                    engine->PlayMusic(MUSIC_DRONES);

                if (m_level_number >= 16 && m_level_number <= 30)
                    engine->PlayMusic(MUSIC_WAVES);

                if (m_level_number >= 31 && m_level_number <= 45)
                    engine->PlayMusic(MUSIC_BREEZE);

                if (m_level_number >= 46 && m_level_number <= 60)
                    engine->PlayMusic(MUSIC_DRONES);

                break;

            case Hard:

                if (m_level_number >= 1 && m_level_number <= 15)
                    engine->PlayMusic(MUSIC_WAVES);

                if (m_level_number >= 16 && m_level_number <= 30)
                    engine->PlayMusic(MUSIC_BREEZE);

                if (m_level_number >= 31 && m_level_number <= 45)
                    engine->PlayMusic(MUSIC_DRONES);

                if (m_level_number >= 46 && m_level_number <= 60)
                    engine->PlayMusic(MUSIC_WAVES);

                break;
        }
    }

    void cLevel::SetupAppear()
    {
        m_state = Appear;
        m_appear_state = SubAppearWait;
        m_draw_menu_cubes = false;
        m_draw_texts = false;
        m_timeout = 0.0f;
        m_alter_view = false;

        if (rand()%2 == 0)
        {
            //printf("\nDownToTop");
            m_ad_level.SetLevelAndDirection(0, 1);
        }
        else
        {
            //printf("\nTopToDown");
            m_ad_level.SetLevelAndDirection(MAX_CUBE_COUNT - 1, -1);
        }

        m_cube_pos_key.Reset();
    }

    void cLevel::SetupCubesForLevel()
    {
        m_list_cubes_wall_y_minus.clear();
        m_list_cubes_wall_x_minus.clear();
        m_list_cubes_wall_z_minus.clear();
        m_list_cubes_edges.clear();

        engine->ResetCubes();

        // ceiling
        for (int z = 0; z < MAX_CUBE_COUNT; ++z)
        {
            for (int x = 0; x < MAX_CUBE_COUNT; ++x)
            {
                engine->cubes[x][7][z].type = CubeIsInvisibleAndObstacle;
                engine->cubes[x][1][z].type = CubeIsInvisibleAndObstacle;

                engine->cubes[x][7][z].SetColor( engine->GetBaseColor() );
                engine->cubes[x][1][z].SetColor( engine->GetBaseColor() );
            }
        }

        // wall x minus
        for (int z = 0; z < MAX_CUBE_COUNT; ++z)
        {
            for (int y = 0; y < MAX_CUBE_COUNT; ++y)
            {
                engine->cubes[1][y][z].type = CubeIsInvisibleAndObstacle;
                engine->cubes[1][y][z].SetColor( engine->GetBaseColor() );
            }
        }

        // wall z minus
        for (int x = 0; x < MAX_CUBE_COUNT; ++x)
        {
            for (int y = 0; y < MAX_CUBE_COUNT; ++y)
            {
                engine->cubes[x][y][1].type = CubeIsInvisibleAndObstacle;
                engine->cubes[x][y][1].SetColor( engine->GetBaseColor() );
            }
        }


        // wall y minus
        for (int z = 2; z < MAX_CUBE_COUNT; ++z)
        {
            for (int x = 2; x < MAX_CUBE_COUNT; ++x)
            {
                engine->cubes[x][1][z].type = CubeIsVisibleAndObstacle;
                engine->cubes[x][1][z].SetColor( engine->GetBaseColor() );

                m_list_cubes_wall_y_minus.push_back(&engine->cubes[x][1][z]);
            }
        }

        // walls
        for (int y = 2; y < 7; ++y)
        {
            for (int x = 2; x < MAX_CUBE_COUNT; ++x)
            {
                engine->cubes[x][y][1].type = CubeIsVisibleAndObstacle;
                engine->cubes[x][y][1].SetColor( engine->GetBaseColor() );

                if (y > 0)
                    m_list_cubes_wall_z_minus.push_back(&engine->cubes[x][y][1]);
            }

            for (int z = 2; z < MAX_CUBE_COUNT; ++z)
            {
                engine->cubes[1][y][z].type = CubeIsVisibleAndObstacle;
                engine->cubes[1][y][z].SetColor( engine->GetBaseColor() );

                if (y > 0)
                    m_list_cubes_wall_x_minus.push_back(&engine->cubes[1][y][z]);
            }
        }

        // edges
        //engine->cubes[1][1][8].color_current = Color(0, 0, 255, 255);
        Color color = engine->GetBaseColor(); //(255,255,0,255);

        for (int i = 1; i < MAX_CUBE_COUNT; ++i)
        {
            m_list_cubes_edges.push_back(&engine->cubes[i][1][1]);
            engine->cubes[i][1][1].SetColor(color);
        }

        for (int i = 1; i < MAX_CUBE_COUNT; ++i)
        {
            m_list_cubes_edges.push_back(&engine->cubes[1][1][i]);
            engine->cubes[1][1][i].SetColor(color);
        }

        for (int i = 1; i < 7; ++i)
        {
            m_list_cubes_edges.push_back(&engine->cubes[1][i][1]);
            engine->cubes[1][i][1].SetColor(color);
        }
    }

    void cLevel::SetupAnimUndo()
    {
        m_anim_undo = true;
        m_anim_undo_alpha = 1.0f;
        m_anim_undo_scale = 0.75f;

        m_anim_undo_alpha -= 0.02f;
        m_anim_undo_scale += 0.04f;
    }

    void cLevel::SetupDeadCube(cDeadCube *pDeadCube)
    {
        m_state = DeadAnim;
        m_timeout = 1.5f;

        dead_size = 60.0f;
        dead_alpha = 0;
        dead_alpha_step = 4;

        m_dead_cube = pDeadCube;
        m_dead_cube->HiLite();
    }

    void cLevel::SetupMoveCube(cMovingCube* pMovingCube)
    {
        m_state_to_restore = m_state;
        m_state = MovingCube;
        m_timeout = 0.3f;

        m_moving_cube = pMovingCube;
        m_moving_cube->Move();
    }

    void cLevel::SetupMoverCube(cMoverCube *pMoverCube)
    {
        AxisMovementEnum movement = pMoverCube->GetMoveDir();
        m_state_to_restore = m_state;
        m_state = MovingPlayer;
        m_timeout = 0.3f;

        m_mover_cube = pMoverCube;
        m_mover_cube->HiLite();

        m_player_cube->MoveOnAxis(movement);

        UndoData& ud = m_lst_undo.back();
        ud.moving_cube = m_player_cube->m_moving_cube; //m_lst_moving_cubes.front();

        if (ud.moving_cube)
        {
            ud.moving_cube_move_dir = ud.moving_cube->GetMovement();
            ud.moving_cube_pos = ud.moving_cube->GetCubePos();
        }
    }


    #pragma mark - Set

    void cLevel::SetSolversCount()
    {
        m_hud.SetTextSolver(engine->GetSolverCount());
    }


    #pragma mark - Completed

    void cLevel::SetAnimToCompleted()
    {
        m_state = AnimToCompleted;

        m_draw_menu_cubes = true;
        m_draw_texts = true;

        m_list_cubes_base.clear();
        m_list_cubes_face.clear();

        m_ad_face.Clear();
        m_ad_base.Clear();

        cCube* pCube;

        // way for menu cubes carved out
        for (int i = 0; i < MAX_CUBE_COUNT; ++i)
        {
            pCube = &engine->cubes[i][1][0];
            pCube->type = CubeIsInvisible;

            pCube = &engine->cubes[i][3][0];
            pCube->type = CubeIsInvisible;

            pCube = &engine->cubes[i][5][0];
            pCube->type = CubeIsInvisible;
        }

        for (int i = 0; i < 7; ++i)
        {
            pCube = &engine->cubes[8][i][0];
            pCube->type = CubeIsVisibleAndObstacle;
            pCube->SetColor( cEngine::GetFaceColor() );
            m_ad_face.AddAppear(pCube);
        }

        for (int i = 0; i < 7; ++i)
        {
            pCube = &engine->cubes[0][i][1];
            pCube->type = CubeIsVisibleAndObstacle;
            pCube->SetColor( cEngine::GetBaseColor() );
            m_ad_base.AddAppear(pCube);
        }

        for (int i = 0; i < MAX_CUBE_COUNT; ++i)
        {
            pCube = &engine->cubes[i][0][1];
            pCube->type = CubeIsVisibleAndObstacle;
            pCube->SetColor( cEngine::GetBaseColor() );
            m_ad_base.AddAppear(pCube);
        }

        pCube = &engine->cubes[7][0][0];
        pCube->type = CubeIsVisibleAndObstacle;
        pCube->SetColor( cEngine::GetFaceColor() );
        m_ad_face.AddAppear(pCube);

        pCube = &engine->cubes[7][2][0];
        pCube->type = CubeIsVisibleAndObstacle;
        pCube->SetColor( cEngine::GetFaceColor() );
        m_ad_face.AddAppear(pCube);

        pCube = &engine->cubes[7][4][0];
        pCube->type = CubeIsVisibleAndObstacle;
        pCube->SetColor( cEngine::GetFaceColor() );
        m_ad_face.AddAppear(pCube);

        pCube = &engine->cubes[7][6][0];
        pCube->type = CubeIsVisibleAndObstacle;
        pCube->SetColor( cEngine::GetFaceColor() );
        m_ad_face.AddAppear(pCube);

        m_ad_face.SetLevelAndDirection(0, 1);
        m_ad_base.SetLevelAndDirection(0, 1);

        m_t = 0.0f;

        if (Hard == m_difficulty && 60 == m_level_number)
            m_completed_face_next_action = Finish;
        else
            m_completed_face_next_action = Next;

        #ifdef LITE_VERSION
        if (15 == m_level_number)
            m_completed_face_next_action = Buy_Full_Version;
        #endif

        cCreator::CreateTextsLevelCompletedFace(this, m_completed_face_next_action);

        cCubeFont* pCubeFont;
        Color color = cEngine::GetTextColorOnCubeFace();
        list<cCubeFont*>::iterator it;
        for(it = m_list_fonts.begin(); it != m_list_fonts.end(); ++it)
        {
            pCubeFont = *it;
            pCubeFont->SetColor(color);
        }

        color = Color(20, 0, 0, 20);
        m_cubefont_up->SetColor(color);
        m_cubefont_mid->SetColor(color);
        m_cubefont_low->SetColor(color);

        m_target_rotation_degree = m_cube_rotation.degree - 90.0f - 45.0f;

        m_interpolator.Setup(m_cube_rotation.degree, m_target_rotation_degree, 5.0f);

        m_menu_cube_up->SetCubePos(CubePos(0, 5, 0));
        m_menu_cube_mid->SetCubePos(CubePos(0, 3, 0));
        m_menu_cube_low->SetCubePos(CubePos(0, 1, 0));

        CubePos offset(0,0,1);
        m_menu_cube_up->SetHiliteOffset(offset);
        m_menu_cube_mid->SetHiliteOffset(offset);
        m_menu_cube_low->SetHiliteOffset(offset);

        m_menu_cube_up->MoveOnAxis(X_Plus);
        m_menu_cube_mid->MoveOnAxis(X_Plus);
        m_menu_cube_low->MoveOnAxis(X_Plus);

        //CheckCubes();
    }

    void cLevel::UpdateAnimToCompleted(float dt)
    {
        m_menu_cube_up->Update(dt);
        m_menu_cube_mid->Update(dt);
        m_menu_cube_low->Update(dt);

        cCube* pCube;

        pCube = m_ad_base.GetCubeFromAppearList();

        if (pCube)
            m_list_cubes_base.push_back(pCube);

        pCube = m_ad_face.GetCubeFromAppearList();

        if (pCube)
            m_list_cubes_face.push_back(pCube);

        engine->IncT(m_t);

        cUtils::LerpCamera(m_camera_level, m_camera_level_completed, m_t, m_camera_current);

        m_interpolator.Interpolate();
        m_cube_rotation.degree = m_interpolator.GetValue();

        float diff = abs(m_target_rotation_degree) - abs(m_interpolator.GetValue());

        if (diff < 0.1f && m_ad_base.lst_appear.empty() && m_ad_face.lst_appear.empty() && (abs(1.0f - m_t) < EPSILON))
        {
            m_cube_rotation.degree = m_target_rotation_degree;
            m_state = Completed;

            Color color(100, 0, 0, 230);

            m_cubefont_up->color  = color;
            m_cubefont_mid->color = color;
            m_cubefont_low->color = color;

            m_show_hint_2nd = false;

            for (int i = 0; i < MAX_HINT_CUBES; ++i)
                m_ar_hint_cubes[i] = NULL;

            #ifdef LITE_VERSION
            if (15 == m_level_number)
                engine->AskToBuyFullVersion();
            #endif
        }
    }

    void cLevel::UpdateCompleted(float dt)
    {
        m_menu_cube_up->Update(dt);
        m_menu_cube_mid->Update(dt);
        m_menu_cube_low->Update(dt);

        m_cubefont_up->WarmByFactor(60);
        m_cubefont_mid->WarmByFactor(60);
        m_cubefont_low->WarmByFactor(60);

        if (m_menu_cube_up->IsDone()) // next
        {
            if (0 == m_menu_cube_up->m_cube_pos.x)
            {
                switch (m_completed_face_next_action)
                {
                    case Buy_Full_Version:
                        #ifdef LITE_VERSION
                        m_menu_cube_up->MoveOnAxis(X_Plus);
                        engine->BuyFullVersion();
                        #endif
                        break;

                    case Next:
                        ++m_level_number;

                        if (m_level_number > 60)
                        {
                            switch (m_difficulty)
                            {
                                case Easy:
                                    m_difficulty = Normal;
                                    m_level_number = 1;
                                    engine->StopMusic();
                                    SetupMusic();
                                    break;

                                case Normal:
                                    m_difficulty = Hard;
                                    m_level_number = 1;
                                    engine->StopMusic();
                                    SetupMusic();
                                    break;

                                case Hard: // win!
                                    break;
                            }
                        }
                        else
                        {
                            if (m_level_number == 16 || m_level_number == 31 || m_level_number == 46)
                            {
                                engine->StopMusic();
                                SetupMusic();
                            }
                        }
                        SetAnimFromCompleted();
                        break;

                    case Finish:
                        ++m_level_number;
                        SetAnimFromCompleted();
                        break;
                }
            }
        }

        if (m_menu_cube_mid->IsDone()) // replay
        {
            if (m_menu_cube_mid->m_cube_pos.x == 0)
            {
                Reset();
                SetAnimFromCompleted();
            }
        }

        if (m_menu_cube_low->IsDone()) // quit
        {
            if (m_menu_cube_low->m_cube_pos.x == 0)
            {
                EventQuit();
            }
        }
    }

    void cLevel::SetAnimFromCompleted()
    {
        m_state = AnimFromCompleted;
        m_t = 0.0f;
        m_target_rotation_degree = -45.0f;

        m_interpolator.Setup(m_cube_rotation.degree, m_target_rotation_degree, 6.0f);

        m_ad_base.SetLevelAndDirection(MAX_CUBE_COUNT - 1, -1);
        m_ad_face.SetLevelAndDirection(MAX_CUBE_COUNT - 1, -1);

        //CheckCubes();
    }

    void cLevel::UpdateAnimFromCompleted(float dt)
    {
        engine->IncT(m_t);

        cUtils::LerpCamera(m_camera_level_completed, m_camera_level, m_t, m_camera_current);

        cCube* pCube;
        pCube = m_ad_base.GetCubeFromDisappearList();

        if (pCube)
            m_list_cubes_base.remove(pCube);

        pCube = m_ad_face.GetCubeFromDisappearList();

        if (pCube)
            m_list_cubes_face.remove(pCube);

        m_interpolator.Interpolate();
        m_cube_rotation.degree = m_interpolator.GetValue();

        float diff = abs(m_cube_rotation.degree) - abs(m_target_rotation_degree);

        if (diff < 0.1f && ((1.0f - m_t) < EPSILON) && m_ad_face.lst_disappear.empty() && m_ad_base.lst_disappear.empty())
        {
            m_cube_rotation.degree = m_target_rotation_degree;

            if (Hard == m_difficulty && 61 == m_level_number)
                engine->ShowScene(Scene_Outro);
            else
                SetupAppear();
        }
    }


    #pragma mark - Paused

    void cLevel::SetAnimToPaused()
    {
        m_state = AnimToPaused;

        m_draw_menu_cubes = true;
        m_draw_texts = true;

        m_target_rotation_degree = m_cube_rotation.degree + 90.0f + 45.0f;
        m_interpolator.Setup(m_cube_rotation.degree, m_target_rotation_degree, 5.0f);
        m_t = 0.0f;

        cCreator::CreateTextsLevelPausedFace(this);

        cCubeFont* pCubeFont;
        Color color = cEngine::GetTextColorOnCubeFace();
        list<cCubeFont*>::iterator it;
        for(it = m_list_fonts.begin(); it != m_list_fonts.end(); ++it)
        {
            pCubeFont = *it;
            pCubeFont->SetColor(color);
        }

        color = Color(20, 0, 0, 20);
        m_cubefont_up->SetColor(color);
        m_cubefont_mid->SetColor(color);
        m_cubefont_low->SetColor(color);

        m_list_cubes_base.clear();
        m_list_cubes_face.clear();

        m_ad_face.Clear();
        m_ad_base.Clear();

        cCube* pCube;

        for (int i = 1; i < MAX_CUBE_COUNT; ++i)
        {
            pCube = &engine->cubes[0][5][i];
            pCube->type = CubeIsInvisible;

            pCube = &engine->cubes[0][3][i];
            pCube->type = CubeIsInvisible;

            pCube = &engine->cubes[0][1][i];
            pCube->type = CubeIsInvisible;
        }

        for (int i = 0; i < 7; ++i)
        {
            pCube = &engine->cubes[0][i][0];
            pCube->type = CubeIsVisibleAndObstacle;
            pCube->SetColor( cEngine::GetFaceColor() );
            m_ad_face.AddAppear(pCube);

            pCube = &engine->cubes[1][i][0];
            pCube->type = CubeIsVisibleAndObstacle;
            pCube->SetColor( cEngine::GetBaseColor() );
            m_ad_base.AddAppear(pCube);
        }

        for (int i = 0; i < MAX_CUBE_COUNT; ++i)
        {
            pCube = &engine->cubes[1][0][i];
            pCube->type = CubeIsVisibleAndObstacle;
            pCube->SetColor( cEngine::GetBaseColor() );
            m_ad_base.AddAppear(pCube);
        }

        pCube = &engine->cubes[0][0][1];
        pCube->type = CubeIsVisibleAndObstacle;
        pCube->SetColor( cEngine::GetFaceColor() );
        m_ad_face.AddAppear(pCube);

        pCube = &engine->cubes[0][2][1];
        pCube->type = CubeIsVisibleAndObstacle;
        pCube->SetColor( cEngine::GetFaceColor() );
        m_ad_face.AddAppear(pCube);

        pCube = &engine->cubes[0][4][1];
        pCube->type = CubeIsVisibleAndObstacle;
        pCube->SetColor( cEngine::GetFaceColor() );
        m_ad_face.AddAppear(pCube);

        pCube = &engine->cubes[0][6][1];
        pCube->type = CubeIsVisibleAndObstacle;
        pCube->SetColor( cEngine::GetFaceColor() );
        m_ad_face.AddAppear(pCube);

        m_ad_face.SetLevelAndDirection(0, 1);
        m_ad_base.SetLevelAndDirection(0, 1);

        m_menu_cube_up->SetCubePos( CubePos(0, 5, 8));
        m_menu_cube_mid->SetCubePos(CubePos(0, 3, 8));
        m_menu_cube_low->SetCubePos(CubePos(0, 1, 8));

        CubePos offset(1,0,0);
        m_menu_cube_up->SetHiliteOffset(offset);
        m_menu_cube_mid->SetHiliteOffset(offset);
        m_menu_cube_low->SetHiliteOffset(offset);

        m_menu_cube_up->MoveOnAxis(Z_Minus);
        m_menu_cube_mid->MoveOnAxis(Z_Minus);
        m_menu_cube_low->MoveOnAxis(Z_Minus);

        //CheckCubes();
    }

    void cLevel::UpdateAnimToPaused(float dt)
    {
        m_menu_cube_up->Update(dt);
        m_menu_cube_mid->Update(dt);
        m_menu_cube_low->Update(dt);

        cCube* pCube;

        pCube = m_ad_base.GetCubeFromAppearList();

        if (pCube)
            m_list_cubes_base.push_back(pCube);

        pCube = m_ad_face.GetCubeFromAppearList();

        if (pCube)
            m_list_cubes_face.push_back(pCube);

        engine->IncT(m_t);

        cUtils::LerpCamera(m_camera_level, m_camera_level_paused, m_t, m_camera_current);

        m_interpolator.Interpolate();
        m_cube_rotation.degree = m_interpolator.GetValue();

        float diff = fabs(m_interpolator.GetValue()) - fabs(m_target_rotation_degree);
        diff = fabs(diff);

        if (diff < 0.1f && ((1.0f - m_t) < EPSILON_SMALL) && m_ad_face.lst_appear.empty() && m_ad_base.lst_appear.empty() )
        {
            m_cube_rotation.degree = m_target_rotation_degree;
            m_state = Paused;

            Color color(100, 0, 0, 230);
            m_cubefont_up->color  = color;
            m_cubefont_mid->color = color;
            m_cubefont_low->color = color;

            m_show_hint_2nd = false;

            for (int i = 0; i < MAX_HINT_CUBES; ++i)
                m_ar_hint_cubes[i] = NULL;
        }
    }

    void cLevel::UpdatePaused(float dt)
    {
        m_menu_cube_up->Update(dt);
        m_menu_cube_mid->Update(dt);
        m_menu_cube_low->Update(dt);

        m_cubefont_up->WarmByFactor(60);
        m_cubefont_mid->WarmByFactor(60);
        m_cubefont_low->WarmByFactor(60);

        if (m_menu_cube_up->IsDone()) // back
        {
            if (m_menu_cube_up->m_cube_pos.z == 8)
            {
                SetAnimFromPaused();
            }
        }

        if (m_menu_cube_mid->IsDone()) // reset
        {
            if (m_menu_cube_mid->m_cube_pos.z == 8)
            {
                Reset();
                SetAnimFromPaused();
            }
        }

        if (m_menu_cube_low->IsDone()) // quit
        {
            if (m_menu_cube_low->m_cube_pos.z == 8)
            {
                m_menu_cube_low->lst_cubes_to_hilite.clear();
                EventQuit();
            }
        }
    }

    void cLevel::SetAnimFromPaused()
    {
        m_hud.SetupAppear();
        m_target_rotation_degree = m_cube_rotation.degree - 90.0f - 45.0f;
        m_interpolator.Setup(m_cube_rotation.degree, m_target_rotation_degree, 6.0f);
        m_t = 0.0f;
        m_state = AnimFromPaused;

        m_ad_base.SetLevelAndDirection(MAX_CUBE_COUNT - 1, -1);
        m_ad_face.SetLevelAndDirection(MAX_CUBE_COUNT - 1, -1);

        //CheckCubes();
    }

    void cLevel::UpdateAnimFromPaused(float dt)
    {
        engine->IncT(m_t);

        cUtils::LerpCamera(m_camera_level_paused, m_camera_level, m_t, m_camera_current);

        cCube* pCube;
        pCube = m_ad_base.GetCubeFromDisappearList();

        if (pCube)
            m_list_cubes_base.remove(pCube);

        pCube = m_ad_face.GetCubeFromDisappearList();

        if (pCube)
            m_list_cubes_face.remove(pCube);

        m_interpolator.Interpolate();
        m_cube_rotation.degree = m_interpolator.GetValue();

        // 90 <-> - 45
        float diff = fabs( fabs(m_target_rotation_degree) - fabs(m_interpolator.GetValue()) );

        if (diff < 0.1f && ((1.0f - m_t) < EPSILON) && m_ad_face.lst_disappear.empty() && m_ad_base.lst_disappear.empty() )
        {
            m_cube_rotation.degree = m_target_rotation_degree;
            m_state = Playing;
            m_draw_menu_cubes = false;
            m_draw_texts = false;
        }
    }


    #pragma mark - Update

    void cLevel::Update(float dt)
    {
//    Color color = engine->cubes[1][1][8].color_current;
//    printf("\n%d %d %d %d",  color.r, color.g, color.b, color.a);

        switch (m_state)
        {
            case Playing:
                UpdatePlaying(dt);
                break;

            case Undo:
                UpdateUndo(dt);
                break;

            case PrepareSolving:
                UpdatePrepareSolving(dt);
                break;

            case Solving:
                UpdateSolving(dt);
                break;

            case MovingCube:
                UpdateMovingCube(dt);
                break;

            case MovingPlayer:
                UpdateMovingPlayer(dt);
                break;

            case Completed:
                UpdateCompleted(dt);
                break;

            case Paused:
                UpdatePaused(dt);
                break;

            case Appear:
                UpdateAppear(dt);
                break;

            case AnimFromCompleted:
                UpdateAnimFromCompleted(dt);
                break;

            case AnimFromPaused:
                UpdateAnimFromPaused(dt);
                break;

            case AnimToCompleted:
                UpdateAnimToCompleted(dt);
                break;

            case AnimToPaused:
                UpdateAnimToPaused(dt);
                break;

            case SetupAnimToCompleted:
                SetAnimToCompleted();
                break;

            case SetupAnimToPaused:
                SetAnimToPaused();
                break;

            case SetupAnimFromCompleted:
                SetAnimFromCompleted();
                break;

            case SetupAnimFromPaused:
                SetAnimFromPaused();
                break;

            case DeadAnim:
                UpdateDeadAnim(dt);
                break;

            case Tutorial:
                UpdateTutorial(dt);
                break;

            default:
                //printf("\nUnhandled state in cLevel Update!!!");
                break;
        }

        WarmCubes(dt);

        if (m_reposition_view)
        {
            m_t += 0.1f;

            if (m_t >= 1.0f)
            {
                m_t = 1.0f;
                m_reposition_view = false;
                m_user_rotation.Reset();
            }
            else
            {
                m_user_rotation.current.x = LERP(m_user_rotation.from.x, 0.0f, m_t);
                m_user_rotation.current.y = LERP(m_user_rotation.from.y, 0.0f, m_t);
            }
        }

        m_hud.Update(dt);

        if (NoNextAction != m_next_action)
        {
            if ( DoneHUD == m_hud.GetState() )
            {
                switch (m_next_action)
                {
                    case ShowSceneSolvers:
                        engine->RenderToFBO(this);
                        engine->ShowScene(Scene_Solvers);
                        m_next_action = NoNextAction;
                        break;

                    default:
                        break;
                }
            }
        }

        if (m_menu_cube_hilite)
        {
            m_hilite_alpha += 0.05f;

            if (m_hilite_alpha > 0.2f)
                m_hilite_alpha = 0.2f;
        }

        if (m_show_hint_2nd)
        {
            m_hint_timeout -= dt;

            if (m_hint_timeout < 0.0f)
            {
                m_hint_timeout = 0.15f;
                cCube* pCube = m_ar_hint_cubes[m_hint_index];

                if (pCube)
                {
                    Color col(255, 0, 0, 255);

                    pCube->color_current = col;
                    ++m_hint_index;

                    // locate cube among moving cubes
                    cMovingCube* pMovingCube;
                    list<cMovingCube*>::iterator it;
                    for (it = m_lst_moving_cubes.begin(); it != m_lst_moving_cubes.end(); ++it)
                    {
                        pMovingCube = *it;
                        CubePos cube_pos(pCube->x, pCube->y, pCube->z);
                        CubePos moving_cube_pos = pMovingCube->GetCubePos();

                        if (moving_cube_pos.x == cube_pos.x && moving_cube_pos.y == cube_pos.y && moving_cube_pos.z == cube_pos.z)
                        {
                            pMovingCube->SetCurrentColor(col);
                        }
                    }
                }
                else
                    m_show_hint_2nd = false;
            }
        }
    }

    void cLevel::WarmCubes(float dt)
    {
        m_hilite_timeout -= dt;

        if (m_hilite_timeout < 0.0f)
        {
            m_hilite_timeout = 0.05f;
            Color color(99, 99, 99, 255);

            if (!m_menu_cube_up->lst_cubes_to_hilite.empty())
            {
                cCube* p = m_menu_cube_up->lst_cubes_to_hilite.front();
                m_menu_cube_up->lst_cubes_to_hilite.pop_front();

                p->color_current = color;
            }

            if (!m_menu_cube_mid->lst_cubes_to_hilite.empty())
            {
                cCube* p = m_menu_cube_mid->lst_cubes_to_hilite.front();
                m_menu_cube_mid->lst_cubes_to_hilite.pop_front();

                p->color_current = color;
            }

            if (!m_menu_cube_low->lst_cubes_to_hilite.empty())
            {
                cCube* p = m_menu_cube_low->lst_cubes_to_hilite.front();
                m_menu_cube_low->lst_cubes_to_hilite.pop_front();

                p->color_current = color;
            }
        }

        list<cCube*>::iterator it;
        for (it = m_list_cubes_wall_x_minus.begin(); it != m_list_cubes_wall_x_minus.end(); ++it)
            (*it)->WarmByFactor(WARM_FACTOR);

        for (it = m_list_cubes_edges.begin(); it != m_list_cubes_edges.end(); ++it)
            (*it)->WarmByFactor(WARM_FACTOR);

        for (it = m_list_cubes_wall_y_minus.begin(); it != m_list_cubes_wall_y_minus.end(); ++it)
            (*it)->WarmByFactor(WARM_FACTOR);

        for (it = m_list_cubes_wall_z_minus.begin(); it != m_list_cubes_wall_z_minus.end(); ++it)
            (*it)->WarmByFactor(WARM_FACTOR);

        for (it = m_list_cubes_base.begin(); it != m_list_cubes_base.end(); ++it)
            (*it)->WarmByFactor(WARM_FACTOR);

        for (it = m_list_cubes_level.begin(); it != m_list_cubes_level.end(); ++it)
            (*it)->WarmByFactor(WARM_FACTOR);

        list<cMovingCube*>::iterator itm;
        for (itm = m_lst_moving_cubes.begin(); itm != m_lst_moving_cubes.end(); ++itm)
            (*itm)->WarmByFactor(WARM_FACTOR);
    }

    void cLevel::UpdateDeadAnim(float dt)
    {
        m_timeout -= dt;
        dead_size += 4.0f;
        dead_alpha += dead_alpha_step;

        if (dead_alpha_step > 0)
        {
            if (dead_alpha > 130)
                dead_alpha_step = -3;
        }
        else
        {
            if (dead_alpha < 0)
            {
                Reset();
                m_state = Playing;
                m_dead_cube = NULL;
            }
        }
    }

    void cLevel::UpdateMovingCube(float dt)
    {
        m_timeout -= dt;

        if (m_timeout < 0.0f)
        {
            m_moving_cube->Update(dt);

            if (m_moving_cube->IsDone())
            {
                m_state = m_state_to_restore;
                m_moving_cube = NULL;

                if (Solving == m_state_to_restore)
                    m_timeout = 1.0f;
            }
        }
    }

    void cLevel::UpdateMovingPlayer(float dt)
    {
        m_timeout -= dt;

        if (m_timeout < 0.0f)
        {
            m_player_cube->Update(dt);

            if (m_player_cube->IsDone())
            {
                if (m_mover_cube)
                {
                    m_mover_cube->NoHiLite();
                    m_mover_cube = NULL;
                }

                if (m_moving_cube)
                {
                    m_moving_cube->NoHiLite();
                    m_moving_cube = NULL;
                }

                if (m_dead_cube)
                {
                    m_dead_cube->NoHilite();
                    m_dead_cube = NULL;
                }

                engine->PlaySound(SOUND_CUBE_HIT);

                m_state = m_state_to_restore;

                if (Solving == m_state_to_restore)
                    m_timeout = 1.0f;

                CubePos player = m_player_cube->GetCubePos();
                CubePos& key = m_cube_pos_key;

                if (player.x == key.x && player.y == key.y && player.z == key.z)
                {
                    engine->LevelComplete();
                }
                else
                {
                    if (m_player_cube->m_dead_cube)
                        SetupDeadCube(m_player_cube->m_dead_cube);
                    else if (m_player_cube->m_moving_cube)
                        SetupMoveCube(m_player_cube->m_moving_cube);
                    else if (m_player_cube->m_mover_cube)
                        SetupMoverCube(m_player_cube->m_mover_cube);
                }
            }
        }
    }

    void cLevel::UpdateSolving(float dt)
    {
        m_timeout -= dt;

        if (m_timeout <= 0.0f)
        {
            m_timeout = 1.0f;

            if (m_player_cube->IsDone())
            {
                AxisMovementEnum dir = m_ar_solution[m_solution_pointer++];

                bool success = m_player_cube->MoveOnAxis(dir);

                if (success)
                {
                    m_state_to_restore = m_state;
                    m_state = MovingPlayer;
                    m_timeout = 0.0f;

                    ++m_moves_counter;
                    m_hud.SetTextMoves(m_moves_counter);
                }
            }
        }

        m_player_cube->Update(dt);
    }

    void cLevel::UpdatePrepareSolving(float dt)
    {
        m_hud.Update(dt);

        m_timeout -= dt;

        if (m_timeout < 2.5f)
            m_hud.ShowPrepareSolving(true, m_min_solution_steps);

        if (m_timeout <= 0.0f)
        {
            m_hud.ShowPrepareSolving(false, m_min_solution_steps);

            m_state = Solving;
            m_solution_pointer = 0;
            m_fade_value = 1.0f;
            m_timeout = 1.0f;
            m_hud.SetupAppear();
        }
    }

    void cLevel::UpdateAppear(float dt)
    {
        m_timeout += dt;

        switch (m_appear_state)
        {
            case SubAppearWait:

                if (m_timeout > 0.2f)
                {
                    cCube* pCube;

                    pCube = m_ad_level.GetCubeFromDisappearList();
                    if (pCube)
                    {
                        pCube->type = CubeIsInvisible;
                        m_list_cubes_level.remove(pCube);
                    }

                    pCube = m_ad_level.GetCubeFromDisappearList();
                    if (pCube)
                    {
                        pCube->type = CubeIsInvisible;
                        m_list_cubes_level.remove(pCube);
                    }

                    if (!m_lst_moving_cubes.empty())
                    {
                        cMovingCube* pMovingCube = m_lst_moving_cubes.front();
                        m_lst_moving_cubes.pop_front();
                        cLevelBuilder::lst_moving_cubes.push_back(pMovingCube);
                    }

                    if (!m_lst_mover_cubes.empty())
                    {
                        cMoverCube* pMoverCube = m_lst_mover_cubes.front();
                        m_lst_mover_cubes.pop_front();
                        cLevelBuilder::lst_mover_cubes.push_back(pMoverCube);
                    }

                    if (!m_lst_dead_cubes.empty())
                    {
                        cDeadCube* pDeadCube = m_lst_dead_cubes.front();
                        m_lst_dead_cubes.pop_front();
                        cLevelBuilder::lst_dead_cubes.push_back(pDeadCube);
                    }

                    if (m_list_cubes_level.empty() &&
                            m_lst_moving_cubes.empty() &&
                            m_lst_mover_cubes.empty() &&
                            m_lst_dead_cubes.empty())
                    {
                        m_timeout = 0.0f;
                        m_appear_state = SubAppearKeyAndPlayer;
                        m_t = 0.0;
                        m_fade_value = 0.0f;

                        EventBuildLevel();
                        Reset();
                    }
                }

                break;

            case SubAppearKeyAndPlayer:

                if (m_timeout > 0.1f)
                {
                    m_t += 0.1;

                    if (m_t > 1.0f)
                    {
                        m_timeout = 0.0f;
                        m_t = 1.0f;
                        m_appear_state = SubAppearWaitAgain;
                    }

                    m_fade_value = LERP(0.0f, 1.0f, m_t);
                }
                break;

            case SubAppearWaitAgain:

                if (m_timeout > 0.2)
                    m_appear_state = SubAppearLevel;

                break;

            case SubAppearLevel:

                if (m_ad_level.lst_appear.empty() &&
                        cLevelBuilder::lst_moving_cubes.empty() &&
                    cLevelBuilder::lst_mover_cubes.empty() &&
                    cLevelBuilder::lst_dead_cubes.empty())
            {
                m_state = Playing;

                if (Easy == m_difficulty)
                {
                    if (1 == m_level_number)
                        ShowTutor(Swipe);

                    if (2 == m_level_number)
                        ShowTutor(MenuPause);

                    if (3 == m_level_number)
                        ShowTutor(MenuHint);

                    if (4 == m_level_number)
                        ShowTutor(Drag);

                    if (5 == m_level_number)
                        ShowTutor(Plain);

                    if (10 == m_level_number)
                        ShowTutor(Moving);

                    if (12 == m_level_number)
                        ShowTutor(Mover);

                    if (19 == m_level_number)
                        ShowTutor(Dead);
                }

                //engine->cubes[1][1][8].color_current = Color(0, 0, 255, 255);
            }
            else
            {
                if (m_timeout > 0.02f)
                {
                    cCube* pCube = m_ad_level.GetCubeFromAppearList();

                    if (pCube)
                        m_list_cubes_level.push_back(pCube);

                    if (!cLevelBuilder::lst_moving_cubes.empty())
                    {
                        cMovingCube* pMovingCube = cLevelBuilder::lst_moving_cubes.front();
                        cLevelBuilder::lst_moving_cubes.pop_front();
                        m_lst_moving_cubes.push_back(pMovingCube);
                    }

                    if (!cLevelBuilder::lst_mover_cubes.empty())
                    {
                        cMoverCube* pMoverCube = cLevelBuilder::lst_mover_cubes.front();
                        cLevelBuilder::lst_mover_cubes.pop_front();
                        m_lst_mover_cubes.push_back(pMoverCube);
                    }

                    if (!cLevelBuilder::lst_dead_cubes.empty())
                    {
                        cDeadCube* pDeadCube = cLevelBuilder::lst_dead_cubes.front();
                        cLevelBuilder::lst_dead_cubes.pop_front();
                        m_lst_dead_cubes.push_back(pDeadCube);
                    }

                    m_timeout = 0.0f;
                }
            }
            break;

            default:
                break;
        } // switch
    }

    void cLevel::UpdateTutorial(float dt)
    {
        if (!m_hud.IsTutorDisplaying())
            m_state = Playing;
    }

    void cLevel::UpdatePlaying(float dt)
    {
        m_player_cube->Update(dt);
    }

    void cLevel::UpdateUndo(float dt)
    {
        m_timeout_undo -= dt;

        if (m_timeout_undo <= 0.0f)
        {
            if (m_player_cube->IsDone())
            {
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


    #pragma mark - Draw

    void cLevel::DrawTheCube()
    {
        glClear(GL_STENCIL_BUFFER_BIT);

        list<cCube*>::iterator it;

        //glEnable(GL_LIGHTING);

        //glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, engine->texture_id_gray_concrete);

        vec3 light = m_pos_light;
        vec3 light_tmp;
        Rotate3D_AroundYAxis(m_pos_light.x, m_pos_light.y, m_pos_light.z, -m_cube_rotation.degree - m_user_rotation.current.y, light.x, light.y, light.z);
        Rotate3D_AroundXAxis(light.x, light.y, light.z, -m_user_rotation.current.x, light_tmp.x, light_tmp.y, light_tmp.z);
        light = light_tmp;

        glEnable(GL_STENCIL_TEST);
        glDisable(GL_CULL_FACE);

        cRenderer::SetStreamSource();

        float shadowMat[16];

//    if (false)
        for (int j = 0; j < 3; ++j) // 3 times (1 floor + 2 walls)
        {
            cRenderer::Prepare();

            switch (j)
            {
                case 0: // floor
                    cUtils::CalcShadowMatrixFloor(light, shadowMat, 0.0f);

                    for (it = m_list_cubes_wall_y_minus.begin(); it != m_list_cubes_wall_y_minus.end(); ++it)
                        cRenderer::AddCubeFace_Y_Plus((*it)->tx, (*it)->ty, (*it)->tz, (*it)->color_current);

                    break;

                case 1: // wall x
                    cUtils::CalcShadowMatrixWallX(light, shadowMat, 0.0f);

                    for (it = m_list_cubes_wall_x_minus.begin(); it != m_list_cubes_wall_x_minus.end(); ++it)
                        cRenderer::AddCubeFace_X_Plus((*it)->tx, (*it)->ty, (*it)->tz, (*it)->color_current);

                    break;

                case 2: // wall z
                    cUtils::CalcShadowMatrixWallZ(light, shadowMat, 0.0f);

                    for (it = m_list_cubes_wall_z_minus.begin(); it != m_list_cubes_wall_z_minus.end(); ++it)
                        cRenderer::AddCubeFace_Z_Plus((*it)->tx, (*it)->ty, (*it)->tz, (*it)->color_current);

                    break;
            } // switch

            glClear(GL_STENCIL_BUFFER_BIT);
            glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
            glStencilFunc(GL_ALWAYS, 0xff, 0xff);

            cRenderer::DisableBlending();
            glEnable(GL_LIGHTING);
            glEnable(GL_DEPTH_TEST);
            glEnable(GL_TEXTURE_2D);

            // draw shadow receivers (floor and two walls)
            cRenderer::RenderTriangles(engine->cube_offset.x, engine->cube_offset.y, engine->cube_offset.z);

            //----------------------------------------
            // cast shadow on a receiver
            Color shadow_color(0, 0, 0, (m_fade_value / 2.0f) * 255);

            cRenderer::Prepare();

            // with level cubes
            for (it = m_list_cubes_level.begin(); it != m_list_cubes_level.end(); ++it)
                cRenderer::AddCubeSize((*it)->tx + engine->cube_offset.x, (*it)->ty + engine->cube_offset.y, (*it)->tz + engine->cube_offset.z, HALF_CUBE_SIZE, shadow_color);

            // moving cubes
            if (!m_lst_moving_cubes.empty())
            {
                list<cMovingCube*>::iterator it;
                for(it = m_lst_moving_cubes.begin(); it != m_lst_moving_cubes.end(); ++it)
                    cRenderer::AddCubeSize((*it)->pos.x + engine->cube_offset.x, (*it)->pos.y + engine->cube_offset.y, (*it)->pos.z + engine->cube_offset.z, HALF_CUBE_SIZE, shadow_color);
            }

            // mover cubes
            if (!m_lst_mover_cubes.empty())
            {
                list<cMoverCube*>::iterator it;
                for(it = m_lst_mover_cubes.begin(); it != m_lst_mover_cubes.end(); ++it)
                    cRenderer::AddCubeSize((*it)->pos.x + engine->cube_offset.x, (*it)->pos.y + engine->cube_offset.y, (*it)->pos.z + engine->cube_offset.z, HALF_CUBE_SIZE, shadow_color);
            }

            // dead cubes
            if (!m_lst_dead_cubes.empty())
            {
                list<cDeadCube*>::iterator it;
                for(it = m_lst_dead_cubes.begin(); it != m_lst_dead_cubes.end(); ++it)
                    cRenderer::AddCubeSize((*it)->pos.x + engine->cube_offset.x, (*it)->pos.y + engine->cube_offset.y, (*it)->pos.z + engine->cube_offset.z, HALF_CUBE_SIZE, shadow_color);
            }

            // with player cube
            if (m_player_cube->m_cube_pos.x != 0)
                cRenderer::AddCubeSize(m_player_cube->pos.x + engine->cube_offset.x, m_player_cube->pos.y + engine->cube_offset.y, m_player_cube->pos.z + engine->cube_offset.z, HALF_CUBE_SIZE, shadow_color);

            // with key cube
            if (m_cube_pos_key.x != 0)
                cRenderer::AddCubeSize(engine->cubes[m_cube_pos_key.x][m_cube_pos_key.y][m_cube_pos_key.z].tx + engine->cube_offset.x,
                engine->cubes[m_cube_pos_key.x][m_cube_pos_key.y][m_cube_pos_key.z].ty + engine->cube_offset.y,
                engine->cubes[m_cube_pos_key.x][m_cube_pos_key.y][m_cube_pos_key.z].tz + engine->cube_offset.z, HALF_CUBE_SIZE, shadow_color);

            cRenderer::EnableBlending();
            glDisable(GL_LIGHTING);
            glDisable(GL_TEXTURE_2D);
            glDisable(GL_DEPTH_TEST);

            glStencilOp(GL_KEEP, GL_KEEP, GL_ZERO);
            glStencilFunc(GL_EQUAL, 0xff, 0xff);

            glPushMatrix();
            glMultMatrixf(shadowMat);
            cRenderer::RenderTriangles();
            glPopMatrix();
            //----------------------------------------
        } // for

        glPushMatrix();

        glDisable(GL_STENCIL_TEST);
        glEnable(GL_CULL_FACE);
        cRenderer::DisableBlending();
        glEnable(GL_LIGHTING);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);

        // draw rest of the floor and walls (not shadow receivers)
        cRenderer::Prepare();

        for (it = m_list_cubes_wall_y_minus.begin(); it != m_list_cubes_wall_y_minus.end(); ++it)
        {
            cRenderer::AddCubeFace_X_Plus((*it)->tx, (*it)->ty, (*it)->tz, (*it)->color_current);
            cRenderer::AddCubeFace_X_Minus((*it)->tx, (*it)->ty, (*it)->tz, (*it)->color_current);
            cRenderer::AddCubeFace_Z_Plus((*it)->tx, (*it)->ty, (*it)->tz, (*it)->color_current);
            cRenderer::AddCubeFace_Z_Minus((*it)->tx, (*it)->ty, (*it)->tz, (*it)->color_current);
            cRenderer::AddCubeFace_Y_Minus((*it)->tx, (*it)->ty, (*it)->tz, (*it)->color_current);
        }

        for (it = m_list_cubes_wall_x_minus.begin(); it != m_list_cubes_wall_x_minus.end(); ++it)
        {
            cRenderer::AddCubeFace_X_Minus((*it)->tx, (*it)->ty, (*it)->tz, (*it)->color_current);
            cRenderer::AddCubeFace_Y_Plus((*it)->tx, (*it)->ty, (*it)->tz, (*it)->color_current);
            cRenderer::AddCubeFace_Z_Plus((*it)->tx, (*it)->ty, (*it)->tz, (*it)->color_current);
            cRenderer::AddCubeFace_Z_Minus((*it)->tx, (*it)->ty, (*it)->tz, (*it)->color_current);
        }

        for (it = m_list_cubes_wall_z_minus.begin(); it != m_list_cubes_wall_z_minus.end(); ++it)
        {
            cRenderer::AddCubeFace_X_Plus((*it)->tx, (*it)->ty, (*it)->tz, (*it)->color_current);
            cRenderer::AddCubeFace_X_Minus((*it)->tx, (*it)->ty, (*it)->tz, (*it)->color_current);
            cRenderer::AddCubeFace_Y_Plus((*it)->tx, (*it)->ty, (*it)->tz, (*it)->color_current);
            cRenderer::AddCubeFace_Z_Minus((*it)->tx, (*it)->ty, (*it)->tz, (*it)->color_current);
        }

        // draw edges
        for (it = m_list_cubes_edges.begin(); it != m_list_cubes_edges.end(); ++it)
            cRenderer::AddCubeSize((*it)->tx, (*it)->ty, (*it)->tz, HALF_CUBE_SIZE, (*it)->color_current);

        // level cubes
        for (it = m_list_cubes_level.begin(); it != m_list_cubes_level.end(); ++it)
            cRenderer::AddCubeSize((*it)->tx, (*it)->ty, (*it)->tz, HALF_CUBE_SIZE, (*it)->color_current);

        list<cMovingCube*>::iterator itmo;
        for (itmo = m_lst_moving_cubes.begin(); itmo != m_lst_moving_cubes.end(); ++itmo)
            (*itmo)->RenderCube();

        list<cMoverCube*>::iterator itmu;
        for (itmu = m_lst_mover_cubes.begin(); itmu != m_lst_mover_cubes.end(); ++itmu)
            (*itmu)->RenderCube();

        list<cDeadCube*>::iterator itde;
        for (itde = m_lst_dead_cubes.begin(); itde != m_lst_dead_cubes.end(); ++itde)
            (*itde)->RenderCube();

        if (Playing != m_state)
        {
            for (it = m_list_cubes_face.begin(); it != m_list_cubes_face.end(); ++it)
                cRenderer::AddCubeSize((*it)->tx, (*it)->ty, (*it)->tz, HALF_CUBE_SIZE, (*it)->color_current);

            for (it = m_list_cubes_base.begin(); it != m_list_cubes_base.end(); ++it)
                cRenderer::AddCubeSize((*it)->tx, (*it)->ty, (*it)->tz, HALF_CUBE_SIZE, (*it)->color_current);
        }

        cRenderer::RenderTriangles(engine->cube_offset.x, engine->cube_offset.y, engine->cube_offset.z);

        glPopMatrix();
    }

    void cLevel::DrawTextsCompleted()
    {
        cCubeFont* pCubeFont;
        TexCoordsQuad coords;
        TexturedQuad* pFont;

        cRenderer::SetStreamSourceFloat();
        cRenderer::Prepare();

        list<cCubeFont*>::iterator it;
        for (it = m_list_fonts.begin(); it != m_list_fonts.end(); ++it)
        {
            pCubeFont = *it;
            pFont = pCubeFont->GetFont();

            coords.tx0 = vec2(pFont->tx_up_left.x,  pFont->tx_up_left.y);
            coords.tx1 = vec2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);
            coords.tx2 = vec2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);
            coords.tx3 = vec2(pFont->tx_up_right.x, pFont->tx_up_right.y);

            cRenderer::AddCubeFace_Z_Minus(pCubeFont->pos.x, pCubeFont->pos.y, pCubeFont->pos.z, coords, pCubeFont->color_current);
        }

        if (m_menu_cube_up->IsDone() && m_menu_cube_up->m_cube_pos.x == 7)
        {
            pCubeFont = m_cubefont_up;
            pFont = pCubeFont->GetFont();

            coords.tx0 = vec2(pFont->tx_up_left.x,  pFont->tx_up_left.y);
            coords.tx1 = vec2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);
            coords.tx2 = vec2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);
            coords.tx3 = vec2(pFont->tx_up_right.x, pFont->tx_up_right.y);

            cRenderer::AddCubeFace_Z_Minus(pCubeFont->pos.x, pCubeFont->pos.y, pCubeFont->pos.z, coords, pCubeFont->color_current);
        }

        if (m_menu_cube_mid->IsDone() && m_menu_cube_mid->m_cube_pos.x == 7)
        {
            pCubeFont = m_cubefont_mid;
            pFont = pCubeFont->GetFont();

            coords.tx0 = vec2(pFont->tx_up_left.x,  pFont->tx_up_left.y);
            coords.tx1 = vec2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);
            coords.tx2 = vec2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);
            coords.tx3 = vec2(pFont->tx_up_right.x, pFont->tx_up_right.y);

            cRenderer::AddCubeFace_Z_Minus(pCubeFont->pos.x, pCubeFont->pos.y, pCubeFont->pos.z, coords, pCubeFont->color_current);
        }

        if (m_menu_cube_low->IsDone() && m_menu_cube_low->m_cube_pos.x == 7)
        {
            pCubeFont = m_cubefont_low;
            pFont = pCubeFont->GetFont();

            coords.tx0 = vec2(pFont->tx_up_left.x,  pFont->tx_up_left.y);
            coords.tx1 = vec2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);
            coords.tx2 = vec2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);
            coords.tx3 = vec2(pFont->tx_up_right.x, pFont->tx_up_right.y);

            cRenderer::AddCubeFace_Z_Minus(pCubeFont->pos.x, pCubeFont->pos.y, pCubeFont->pos.z, coords, pCubeFont->color_current);
        }

        cRenderer::RenderTriangles();
    }

    void cLevel::DrawTextsPaused()
    {
        cCubeFont* pCubeFont;
        TexCoordsQuad coords;
        TexturedQuad* pFont;

        cRenderer::SetStreamSourceFloat();
        cRenderer::Prepare();

        list<cCubeFont*>::iterator it;
        for (it = m_list_fonts.begin(); it != m_list_fonts.end(); ++it)
        {
            pCubeFont = *it;
            pFont = pCubeFont->GetFont();

            coords.tx0 = vec2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);
            coords.tx1 = vec2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);
            coords.tx2 = vec2(pFont->tx_up_right.x, pFont->tx_up_right.y);
            coords.tx3 = vec2(pFont->tx_up_left.x,  pFont->tx_up_left.y);

            cRenderer::AddCubeFace_X_Minus(pCubeFont->pos.x, pCubeFont->pos.y, pCubeFont->pos.z, coords, pCubeFont->color_current);
        }

        if (m_menu_cube_up->IsDone() && m_menu_cube_up->m_cube_pos.z == 1)
        {
            pCubeFont = m_cubefont_up;
            pFont = pCubeFont->GetFont();

            coords.tx0 = vec2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);
            coords.tx1 = vec2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);
            coords.tx2 = vec2(pFont->tx_up_right.x, pFont->tx_up_right.y);
            coords.tx3 = vec2(pFont->tx_up_left.x,  pFont->tx_up_left.y);

            cRenderer::AddCubeFace_X_Minus(pCubeFont->pos.x, pCubeFont->pos.y, pCubeFont->pos.z, coords, pCubeFont->color_current);
        }

        if (m_menu_cube_mid->IsDone() && m_menu_cube_mid->m_cube_pos.z == 1)
        {
            pCubeFont = m_cubefont_mid;
            pFont = pCubeFont->GetFont();

            coords.tx0 = vec2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);
            coords.tx1 = vec2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);
            coords.tx2 = vec2(pFont->tx_up_right.x, pFont->tx_up_right.y);
            coords.tx3 = vec2(pFont->tx_up_left.x,  pFont->tx_up_left.y);

            cRenderer::AddCubeFace_X_Minus(pCubeFont->pos.x, pCubeFont->pos.y, pCubeFont->pos.z, coords, pCubeFont->color_current);
        }

        if (m_menu_cube_low->IsDone() && m_menu_cube_low->m_cube_pos.z == 1)
        {
            pCubeFont = m_cubefont_low;
            pFont = pCubeFont->GetFont();

            coords.tx0 = vec2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);
            coords.tx1 = vec2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);
            coords.tx2 = vec2(pFont->tx_up_right.x, pFont->tx_up_right.y);
            coords.tx3 = vec2(pFont->tx_up_left.x,  pFont->tx_up_left.y);

            cRenderer::AddCubeFace_X_Minus(pCubeFont->pos.x, pCubeFont->pos.y, pCubeFont->pos.z, coords, pCubeFont->color_current);
        }

        cRenderer::RenderTriangles();
    }

    void cLevel::Draw()
    {
        Color color(255, 255, 255, m_fade_value * 255);
        cRenderer::SetStreamSource();

        if (m_fade_value < 1.0f)
        {
            glEnable(GL_BLEND);
            glDisable(GL_DEPTH_TEST);
        }

        glEnable(GL_LIGHTING);

        if (m_draw_menu_cubes || 0 != m_player_cube->m_cube_pos.x)
        {
            glBindTexture(GL_TEXTURE_2D, engine->texture_id_player);

            cRenderer::Prepare();

            if (0 != m_player_cube->m_cube_pos.x)
                cRenderer::AddCubeSize(m_player_cube->pos.x, m_player_cube->pos.y, m_player_cube->pos.z, HALF_CUBE_SIZE, color);

            if (m_draw_menu_cubes)
            {
                cRenderer::AddCube(m_menu_cube_up->pos.x, m_menu_cube_up->pos.y, m_menu_cube_up->pos.z);
                cRenderer::AddCube(m_menu_cube_mid->pos.x, m_menu_cube_mid->pos.y, m_menu_cube_mid->pos.z);
                cRenderer::AddCube(m_menu_cube_low->pos.x, m_menu_cube_low->pos.y, m_menu_cube_low->pos.z);
            }
            cRenderer::RenderTriangles(engine->cube_offset.x, engine->cube_offset.y, engine->cube_offset.z);
        }

        if (0 != m_cube_pos_key.x)
        {
            glBindTexture(GL_TEXTURE_2D, engine->texture_id_key);

            cCube* pCube = &engine->cubes[m_cube_pos_key.x][m_cube_pos_key.y][m_cube_pos_key.z];

            cRenderer::Prepare();
            cRenderer::AddCubeSize(pCube->tx, pCube->ty, pCube->tz, HALF_CUBE_SIZE * 0.99f, color);
            cRenderer::RenderTriangles(engine->cube_offset.x, engine->cube_offset.y, engine->cube_offset.z);
        }

        if (m_fade_value < 1.0f)
        {
            glDisable(GL_BLEND);
            glEnable(GL_DEPTH_TEST);
        }

        if (!m_lst_moving_cubes.empty() || !m_lst_mover_cubes.empty() || !m_lst_dead_cubes.empty())
        {
            list<cMovingCube*>::iterator itmo;
            list<cMoverCube*>::iterator itmu;
            list<cDeadCube*>::iterator itde;

            cRenderer::Prepare();

            for (itmo = m_lst_moving_cubes.begin(); itmo != m_lst_moving_cubes.end(); ++itmo)
                (*itmo)->RenderSymbols();

            for (itmu = m_lst_mover_cubes.begin(); itmu != m_lst_mover_cubes.end(); ++itmu)
                (*itmu)->RenderSymbols();

            for (itde = m_lst_dead_cubes.begin(); itde != m_lst_dead_cubes.end(); ++itde)
                (*itde)->RenderSymbols();

            glDisable(GL_LIGHTING);
            glEnable(GL_BLEND);
            cRenderer::SetStreamSourceFloatAndColor();
            glBindTexture(GL_TEXTURE_2D, engine->texture_id_symbols);
            cRenderer::RenderTriangles(engine->cube_offset.x, engine->cube_offset.y, engine->cube_offset.z);
        }

        if (m_menu_cube_hilite)
        {
            glBindTexture(GL_TEXTURE_2D, engine->texture_id_symbols);
            color.a = m_hilite_alpha * 255;
            glDisable(GL_DEPTH_TEST);
            glDisable(GL_CULL_FACE);
            glDisable(GL_LIGHTING);

            TexCoordsQuad coords;

            TexturedQuad* p = m_font_hilite.GetFont();
            coords.tx0 = vec2(p->tx_up_right.x, p->tx_up_right.y);
            coords.tx1 = vec2(p->tx_up_left.x,  p->tx_up_left.y);
            coords.tx2 = vec2(p->tx_lo_left.x,  p->tx_lo_left.y);
            coords.tx3 = vec2(p->tx_lo_right.x, p->tx_lo_right.y);

            cRenderer::Prepare();
            cRenderer::SetStreamSourceFloatAndColor();

            switch (m_state)
            {
                case Paused: //Face_X_Minus:
                    cRenderer::AddCubeFace_X_Minus(m_font_hilite.pos.x, m_font_hilite.pos.y, m_font_hilite.pos.z, coords, color);
                    break;

                case Completed: //Face_Z_Minus:
                    cRenderer::AddCubeFace_Z_Minus(m_font_hilite.pos.x, m_font_hilite.pos.y, m_font_hilite.pos.z, coords, color);
                    break;

                default:
                    break;
            }
            glEnable(GL_BLEND);
            cRenderer::RenderTriangles(engine->cube_offset.x, engine->cube_offset.y, engine->cube_offset.z);

            glEnable(GL_LIGHTING);
            glEnable(GL_DEPTH_TEST);
            glEnable(GL_CULL_FACE);
        }

        if (m_draw_texts)
        {
            glPushMatrix();
            glTranslatef(engine->cube_offset.x, engine->cube_offset.y, engine->cube_offset.z);

            glEnable(GL_TEXTURE_2D);
            glBindTexture(GL_TEXTURE_2D, engine->texture_id_fonts);

            glEnable(GL_BLEND);
            glDisable(GL_LIGHTING);
            glDisableClientState(GL_NORMAL_ARRAY);

            if (m_state == AnimToPaused || m_state == AnimFromPaused || m_state == Paused)
                DrawTextsPaused();
            else
                DrawTextsCompleted();

            glPopMatrix();
        }
    }


    #pragma mark - Render

    void cLevel::Render()
    {
        //return RenderForPicking(RenderOnlyHUD);

        engine->SetProjection2D();
        engine->SetModelViewMatrix2D();

        glEnable(GL_BLEND);
        glDisable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);
        glDepthMask(GL_FALSE);

        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);

        Color color(255, 255, 255, engine->dirty_alpha);
        engine->DrawFBOTexture(engine->texture_id_dirty, color);

        glDepthMask(GL_TRUE);

        engine->SetProjection3D();
        engine->SetModelViewMatrix3D(m_camera_current);

        #ifdef DRAW_AXES_GLOBAL
        glDisable(GL_TEXTURE_2D);
        engine->DrawAxes();
        glEnable(GL_TEXTURE_2D);
        #endif

        const vec4 lightPosition(m_pos_light.x, m_pos_light.y, m_pos_light.z, 1.0f);
        glLightfv(GL_LIGHT0, GL_POSITION, lightPosition.Pointer());

        glEnable(GL_LIGHTING);

        glPushMatrix();
        glRotatef(m_user_rotation.current.x, 1.0f, 0.0f, 0.0f);
        glRotatef(m_user_rotation.current.y, 0.0f, 1.0f, 0.0f);

        glPushMatrix();
        glRotatef(m_cube_rotation.degree, m_cube_rotation.axis.x, m_cube_rotation.axis.y, m_cube_rotation.axis.z);

        DrawTheCube();

        #ifdef DRAW_AXES_CUBE
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_LIGHTING);
        engine->DrawAxes();
        glEnable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);
        #endif

        Draw();

        glPopMatrix();

        glPopMatrix();

        m_hud.Render();
    }

    void cLevel::RenderForPicking(PickRenderTypeEnum type)
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glDisable(GL_LIGHTING);
        cRenderer::DisableBlending();
        glEnable(GL_DEPTH_TEST);

        glDisable(GL_TEXTURE_2D);

        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);

        engine->SetProjection3D();
        engine->SetModelViewMatrix3D(m_camera_current);

        glPushMatrix();

        switch (type)
        {
            case RenderOnlyHUD:
                m_hud.RenderForPicking();
                break;

            case RenderOnlyMovingCubes:

                cRenderer::Prepare();
                cRenderer::SetStreamSource();

                glRotatef(m_cube_rotation.degree, m_cube_rotation.axis.x, m_cube_rotation.axis.y, m_cube_rotation.axis.z);

                cRenderer::AddCubeSize(m_menu_cube_up->pos.x, m_menu_cube_up->pos.y, m_menu_cube_up->pos.z, HALF_CUBE_SIZE * 1.5f, m_menu_cube_up->color);
                cRenderer::AddCubeSize(m_menu_cube_mid->pos.x, m_menu_cube_mid->pos.y, m_menu_cube_mid->pos.z, HALF_CUBE_SIZE * 1.5f, m_menu_cube_mid->color);
                cRenderer::AddCubeSize(m_menu_cube_low->pos.x, m_menu_cube_low->pos.y, m_menu_cube_low->pos.z, HALF_CUBE_SIZE * 1.5f, m_menu_cube_low->color);

                cRenderer::RenderTriangles(engine->cube_offset.x, engine->cube_offset.y, engine->cube_offset.z);

                break;

            default:
                break;
        } // switch

        glPopMatrix();
    }

    void cLevel::RenderToFBO()
    {
        //return Render();

        engine->SetProjection2D();
        engine->SetModelViewMatrix2D();

        glEnable(GL_BLEND);
        glDisable(GL_LIGHTING);
        glDepthMask(GL_FALSE);
        glEnable(GL_TEXTURE_2D);

        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);

        Color color_dirty(255, 255, 255, engine->dirty_alpha);
        engine->DrawFBOTexture(engine->texture_id_dirty, color_dirty);

        glDepthMask(GL_TRUE);



        engine->SetProjection3D();
        engine->SetModelViewMatrix3D(m_camera_current);

        glEnable(GL_LIGHTING);
        //glEnable(GL_LIGHT0);
        const vec4 lightPosition(m_pos_light.x, m_pos_light.y, m_pos_light.z, 1.0f);
        glLightfv(GL_LIGHT0, GL_POSITION, lightPosition.Pointer());

        glRotatef(m_cube_rotation.degree, m_cube_rotation.axis.x, m_cube_rotation.axis.y, m_cube_rotation.axis.z);
        DrawTheCube();

        cRenderer::EnableBlending();

        if (!m_lst_moving_cubes.empty() || !m_lst_mover_cubes.empty() || !m_lst_dead_cubes.empty())
        {
            list<cMovingCube*>::iterator itmo;
            list<cMoverCube*>::iterator itmu;
            list<cDeadCube*>::iterator itde;

            cRenderer::Prepare();

            for (itmo = m_lst_moving_cubes.begin(); itmo != m_lst_moving_cubes.end(); ++itmo)
                (*itmo)->RenderSymbols();

            for (itmu = m_lst_mover_cubes.begin(); itmu != m_lst_mover_cubes.end(); ++itmu)
                (*itmu)->RenderSymbols();

            for (itde = m_lst_dead_cubes.begin(); itde != m_lst_dead_cubes.end(); ++itde)
                (*itde)->RenderSymbols();

            glDisable(GL_LIGHTING);
            glEnable(GL_BLEND);
            cRenderer::SetStreamSourceFloatAndColor();
            glBindTexture(GL_TEXTURE_2D, engine->texture_id_symbols);
            cRenderer::RenderTriangles(engine->cube_offset.x, engine->cube_offset.y, engine->cube_offset.z);
        }

        cRenderer::SetStreamSource();
        glBindTexture(GL_TEXTURE_2D, engine->texture_id_key);

        Color color(255,255,255,255);

        if (0 != m_cube_pos_key.x)
        {
            glEnable(GL_LIGHTING);
            glBindTexture(GL_TEXTURE_2D, engine->texture_id_key);

            cCube* pCube = &engine->cubes[m_cube_pos_key.x][m_cube_pos_key.y][m_cube_pos_key.z];

            cRenderer::Prepare();
            cRenderer::AddCubeSize(pCube->tx, pCube->ty, pCube->tz, HALF_CUBE_SIZE * 0.99f, color);
            cRenderer::RenderTriangles(engine->cube_offset.x, engine->cube_offset.y, engine->cube_offset.z);

            glDisable(GL_LIGHTING);
        }

        cRenderer::DisableBlending();

        // draw player cube
        cRenderer::Prepare();
        //cRenderer::SetStreamSource();
        glEnable(GL_LIGHTING);
        //Color color(255, 255, 255, 255);
        glBindTexture(GL_TEXTURE_2D, engine->texture_id_player);
        cRenderer::AddCubeSize(m_player_cube->pos.x, m_player_cube->pos.y, m_player_cube->pos.z, HALF_CUBE_SIZE, color);
        cRenderer::RenderTriangles(engine->cube_offset.x, engine->cube_offset.y, engine->cube_offset.z);

        glDisable(GL_LIGHTING);
    }

    #pragma mark - Event

    void cLevel::EventBuildLevel()
    {
        switch (m_difficulty)
        {
            case Easy:
                cLevelBuilderEasy::Build(m_level_number);
                break;

            case Normal:
                cLevelBuilderNormal::Build(m_level_number);
                break;

            case Hard:
                cLevelBuilderHard::Build(m_level_number);
                break;
        }

        m_cube_pos_key = cLevelBuilder::key;

        m_player_cube->SetCubePos(cLevelBuilder::player);
        m_player_cube->SetKeyCubePos(m_cube_pos_key);

        if (rand()%2 == 0)
            m_ad_level.SetLevelAndDirection(0, 1);
        else
            m_ad_level.SetLevelAndDirection(MAX_CUBE_COUNT - 1, -1);

        char str[512];

        m_hud.Set1stHint();

        // level
        engine->GetLevelTypeAndNumberString(m_difficulty, m_level_number, str);
        m_hud.SetTextLevel(str);

        // stars
        m_hud.SetTextStars(cCubetraz::GetStarCount());

        // moves
        m_hud.SetTextMoves(0);

        // motto
        cCreator::GetLevelMotto(m_difficulty, m_level_number, str);
        m_hud.SetTextMotto(str);

        m_hud.SetupAppear();
    }

    void cLevel::EventShowHint()
    {
        if (0 == m_moves_counter)
        {
            m_hud.ShowHint(m_ar_solution[0]); // show first hint
        }
        else
        {
            m_show_hint_2nd = true;
            m_hint_index = 0;
            m_hint_timeout = 0.0f;

            int i;
            for (i = 0; i < MAX_HINT_CUBES; ++i)
                m_ar_hint_cubes[i] = NULL;

            i = 0;
            list<cCube*>::iterator it;
            for (it = m_list_cubes_hint.begin(); it != m_list_cubes_hint.end(); ++it)
                m_ar_hint_cubes[i++] = (*it);
        }
    }

    void cLevel::EventSolver()
    {
        if (ShowSceneSolvers == m_next_action || PrepareSolving == m_state)
            return;

        bool solved = false;

        switch (m_difficulty)
        {
            case Easy:
                solved = cCubetraz::GetSolvedEasy(m_level_number);
                break;

            case Normal:
                solved = cCubetraz::GetSolvedNormal(m_level_number);
                break;

            case Hard:
                solved = cCubetraz::GetSolvedHard(m_level_number);
                break;
        }

        if (solved)
        {
            Reset();
            m_state = PrepareSolving;
            m_timeout = 2.0f;
        }
        else
        {
            m_next_action = ShowSceneSolvers;
        }

        m_hud.SetupDisappear();
    }

    void cLevel::EventQuit()
    {
        engine->anim_init_data.list_cubes_base.clear();

        list<cCube*>::iterator it;
        for (it = m_list_cubes_base.begin(); it != m_list_cubes_base.end(); ++it)
        {
            engine->anim_init_data.list_cubes_base.push_back(*it);
        }

        for (it = m_list_cubes_edges.begin(); it != m_list_cubes_edges.end(); ++it)
        {
            engine->anim_init_data.list_cubes_base.push_back(*it);
        }

        engine->anim_init_data.cube_rotation_degree = m_cube_rotation.degree;

        if (Paused == m_state)
            engine->anim_init_data.type = AnimToMenuFromPaused;
        else
            engine->anim_init_data.type = AnimToMenuFromCompleted;

        engine->ShowScene(Scene_Anim);
    }

    void cLevel::EventLevelPause()
    {
        m_hud.SetupDisappear();
        SetAnimToPaused();
    }

    void cLevel::EventLevelComplete()
    {
        engine->PlaySound(SOUND_LEVEL_COMPLETED);

        engine->RenderToFBO(this);

        m_hud.SetupDisappear();

        StatInitData& sid = engine->stat_init_data;

        GetRatings();

        switch (m_difficulty)
        {
            case Easy:
                cCubetraz::SetStarsEasy(m_level_number, sid.stars);
                cCubetraz::SetMovesEasy(m_level_number, m_moves_counter);

                if (m_level_number < 60)
                {
                    if (LEVEL_LOCKED == cCubetraz::GetStarsEasy(m_level_number + 1))
                    cCubetraz::SetStarsEasy(m_level_number + 1, LEVEL_UNLOCKED);
                }

                ReportAchievementEasy();
                break;

            case Normal:
                cCubetraz::SetStarsNormal(m_level_number, sid.stars);
                cCubetraz::SetMovesNormal(m_level_number, m_moves_counter);

                if (m_level_number < 60)
                {
                    if (LEVEL_LOCKED == cCubetraz::GetStarsNormal(m_level_number + 1))
                    cCubetraz::SetStarsNormal(m_level_number + 1, LEVEL_UNLOCKED);
                }

                ReportAchievementNormal();
                break;

            case Hard:
                cCubetraz::SetStarsHard(m_level_number, sid.stars);
                cCubetraz::SetMovesHard(m_level_number, m_moves_counter);

                if (m_level_number < 60)
                {
                    if (LEVEL_LOCKED == cCubetraz::GetStarsHard(m_level_number + 1))
                    cCubetraz::SetStarsHard(m_level_number + 1, LEVEL_UNLOCKED);
                }

                ReportAchievementHard();
                break;
        } // switch

        cCubetraz::Save();

        engine->SubmitScore(cCubetraz::GetStarCount());

        m_state = SetupAnimToCompleted;

        engine->ShowScene(Scene_Stat);
    }

    void cLevel::Reset()
    {
        m_moves_counter = 0;
        m_lst_undo.erase (m_lst_undo.begin(), m_lst_undo.end());
        m_lst_undo.clear();

        m_hud.Set1stHint();
        m_hud.SetTextMoves(m_moves_counter);
        m_hud.SetTextUndo( m_lst_undo.size() );

        m_player_cube->SetCubePos(cLevelBuilder::player);

        engine->ResetCubesColors();

        if (!m_lst_moving_cubes.empty())
        {
            list<cMovingCube*>::iterator it;
            for (it = m_lst_moving_cubes.begin(); it != m_lst_moving_cubes.end(); ++it)
                (*it)->Reposition();
        }

        if (!m_lst_mover_cubes.empty())
        {
            list<cMoverCube*>::iterator it;
            for (it = m_lst_mover_cubes.begin(); it != m_lst_mover_cubes.end(); ++it)
                (*it)->Reposition();
        }

        if (!m_lst_dead_cubes.empty())
        {
            list<cDeadCube*>::iterator it;
            for (it = m_lst_dead_cubes.begin(); it != m_lst_dead_cubes.end(); ++it)
                (*it)->Reposition();
        }
    }

    void cLevel::EventUndo()
    {
        m_timeout_undo = UNDO_TIMEOUT;

        if (m_moves_counter > 0)
        {
            --m_moves_counter;
            m_hud.SetTextMoves(m_moves_counter);

            UndoData& ud = m_lst_undo.back();
            m_lst_undo.pop_back();

            m_player_cube->SetCubePos(ud.player_pos);

            if (NULL != ud.moving_cube)
                ud.moving_cube->Init(ud.moving_cube_pos, ud.moving_cube_move_dir);

            if (0 == m_moves_counter)
                m_hud.Set1stHint();

            m_state = Undo;
        }
    }


    #pragma mark - OnFinger

    void cLevel::OnFingerDown(float x, float y, int finger_count)
    {
        m_fingerdown = true;

        m_pos_down.x = x;
        m_pos_down.y = y;

        if (1 == finger_count)
        {
            Color down_color;

            switch (m_state)
            {
                case Playing:

                    if ( DoneHUD == m_hud.GetState() )
                    {
                        RenderForPicking(RenderOnlyHUD);

                        down_color = engine->GetColorFromScreen(m_pos_down);

                        switch (down_color.b)
                        {
                            case 200:
                                m_hud.SetHilitePause(true);
                                break;

                            case 150:
                                m_hud.SetHiliteUndo(true);
                                break;

                            case 100:
                                m_hud.SetHiliteHint(true);
                                break;

                            case 50:
                                m_hud.SetHiliteSolver(true);
                                break;

                            default:
                                break;
                        }
                    }
                    break;

                case Paused:
                case Completed:
                {
                    RenderForPicking(RenderOnlyMovingCubes);

                    down_color = engine->GetColorFromScreen(m_pos_down);
                    cMenuCube* pMenuCube = GetMovingCubeFromColor(down_color.b);

                    if (pMenuCube)
                    {
                        m_hilite_alpha = 0.0f;
                        m_menu_cube_hilite = pMenuCube;
                        CubePos cp = m_menu_cube_hilite->m_cube_pos;
                        //printf("\nCubePos: %d, %d, %d", cp.x, cp.y, cp.z);
                        m_font_hilite.Init(SymbolHilite, cp);
                    }
                }
                break;

                default:
                    break;
            } // switch
        }
    }

    void cLevel::OnFingerUp(float x, float y, int finger_count)
    {
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

    engine->level_init_data.difficulty = diff;
    engine->level_init_data.level_number = ln;
    engine->level_init_data.init_action = FullInit;

    engine->ShowScene(Scene_Level);
    return;
*/
        m_pos_up.x = x;
        m_pos_up.y = y;

        switch (m_state)
        {
            case Playing:
                FingerUpPlaying(x, y, finger_count);
                m_hud.SetHilitePause(false);
                m_hud.SetHiliteUndo(false);
                m_hud.SetHiliteHint(false);
                m_hud.SetHiliteSolver(false);
                break;

            case Completed:
                m_menu_cube_hilite = NULL;
                FingerUpCompleted(x, y);
                break;

            case Paused:
                m_menu_cube_hilite = NULL;
                FingerUpPaused(x, y);
                break;

            case Tutorial:
                m_hud.HideTutor();
                break;

            default:
                break;
        }
    }

    void cLevel::OnFingerMove(float prev_x, float prev_y, float cur_x, float cur_y, int finger_count)
    {
        m_pos_move.x = cur_x;
        m_pos_move.y = cur_y;

        float dist = GetDistance2D(m_pos_down, m_pos_move);

        //printf("\nOnFingerMove: %.2f", dist);

        if (dist > 20.0f * engine->device_scale)
            m_swipe = true;

        if (2 == finger_count)
        {
            if (Playing == m_state)
            {
                vec2 dir;
                dir.x = (m_pos_down.x - cur_x) / engine->device_scale;
                dir.y = (m_pos_down.y - cur_y) / engine->device_scale;

                m_alter_view = true;

                m_user_rotation.current.x = -dir.y * 0.3f;
                m_user_rotation.current.y = -dir.x * 0.3f;
                m_user_rotation.Clamp();

                m_swipe = false;
            }
        }
    }

    #pragma mark - FingerUps

    void cLevel::FingerUpPaused(float x, float y)
    {
//    float fingerup_x = x;
//    float fingerup_y = y;

        if (m_swipe)
        {
            m_swipe = false;

            RenderForPicking(RenderOnlyMovingCubes);

            Color down_color = engine->GetColorFromScreen(m_pos_down);

            SwipeDirEnums swipeDir;
            float length;
            engine->GetSwipeDirAndLength(m_pos_down, m_pos_up, swipeDir, length);

            if (length > 30.0f * engine->m_scaleFactor)
            {
                cMenuCube* pMenuCube = NULL;

                switch (down_color.b)
                {
                    case 200:
                        pMenuCube = m_menu_cube_up;
                        break;

                    case 150:
                        pMenuCube = m_menu_cube_mid;
                        break;

                    case 100:
                        pMenuCube = m_menu_cube_low;
                        break;
                } // switch

                if (pMenuCube)
                {
                    switch (swipeDir)
                    {
                        case SwipeRight:
                            pMenuCube->MoveOnAxis(Z_Plus);
                            break;

                        case SwipeLeft:
                            pMenuCube->MoveOnAxis(Z_Minus);
                            break;

                        default:
                            break;
                    } // switch
                }
            }
        }
    }

    void cLevel::FingerUpCompleted(float x, float y)
    {
//    float fingerup_x = x;
//    float fingerup_y = y;

        if (m_swipe)
        {
            m_swipe = false;

            RenderForPicking(RenderOnlyMovingCubes);
            Color down_color = engine->GetColorFromScreen(m_pos_down);

//		printf("\nOnFingerUp [SWIPE] color is: %d, %d, %d, %d", down_color.r, down_color.g, down_color.b, down_color.a);

            SwipeDirEnums swipeDir;
            float length;
            engine->GetSwipeDirAndLength(m_pos_down, m_pos_up, swipeDir, length);

            if (length > 30.0f * engine->m_scaleFactor)
            {
                cMenuCube* pMenuCube = NULL;

                switch (down_color.b)
                {
                    case 200:
                        pMenuCube = m_menu_cube_up;
                        break;

                    case 150:
                        pMenuCube = m_menu_cube_mid;
                        break;

                    case 100:
                        pMenuCube = m_menu_cube_low;
                        break;
                }

                if (pMenuCube)
                {
                    switch (swipeDir)
                    {
                        case SwipeLeft:
                            pMenuCube->MoveOnAxis(X_Plus);
                            break;

                        case SwipeRight:
                            pMenuCube->MoveOnAxis(X_Minus);
                            break;

                        default:
                            break;
                    }
                }
            }
        }
    }

    void cLevel::FingerUpPlaying(float x, float y, int finger_count)
    {
        if (m_hud.GetShowHint())
            return;

        if (!m_player_cube->IsDone())
        {
            m_swipe = false;
            return;
        }

        if (m_alter_view)
        {
            if ( fabs(m_user_rotation.current.x) > EPSILON || fabs(m_user_rotation.current.y) > EPSILON )
            {
                m_reposition_view = true;
                m_alter_view = false;
                m_t = 0.0f;
                m_user_rotation.from = m_user_rotation.current;
            }
            return;
        }

        if (!m_swipe)
        {
            if (Playing == m_state)
            {
                RenderForPicking(RenderOnlyHUD);
                vec2 pos(x,y);
                Color color = engine->GetColorFromScreen(pos);

                switch (color.b)
                {
                    case 200:
                        EventLevelPause();
                        break;

                    case 150:
                        EventUndo();
                        break;

                    case 100:
                        EventShowHint();
                        break;

                    case 50:
                        EventSolver();
                        break;

                    default:
                        break;
                }

            }
            return;
        }
        else // swipe
        {
            m_swipe = false;

            if (m_hud.IsAnythingHilited())
                return;

            // flip the Y-axis because pixel coordinates increase toward the bottom (iPhone 3D Programming 86. page)
            float fingerdown_x = m_pos_down.x;
            float fingerdown_y = -m_pos_down.y;

            float fingerup_x = x;
            float fingerup_y = -y;

            vec2 dir;
            dir.x = fingerup_x - fingerdown_x;
            dir.y = fingerup_y - fingerdown_y;

            float len = dir.Length(); // swipe length

            //printf("\nSwipe Length: %.2f", len);

            if (len < 20.0f * engine->device_scale)
                return;

            dir.Normalize();

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
            if (degree > (center - space) && degree < (center + space))
                success = m_player_cube->MoveOnAxis(Y_Plus);

            center = -90.0f; // Y minus
            if (degree > (center - space) && degree < (center + space))
                success = m_player_cube->MoveOnAxis(Y_Minus);

            center = 30.0f; // Z plus
            if (degree > (center - space) && degree < (center + space))
                success = m_player_cube->MoveOnAxis(Z_Minus);

            center = -150.0f; // Z minus
            if (degree > (center - space) && degree < (center + space))
                success = m_player_cube->MoveOnAxis(Z_Plus);

            center = -30.0f; // X minus
            if (degree > (center - space) && degree < (center + space))
                success = m_player_cube->MoveOnAxis(X_Plus);

            center = 150.0f; // X plus
            if (degree > (center - space) && degree < (center + space))
                success = m_player_cube->MoveOnAxis(X_Minus);

            if (success)
                AddMove();
        }
    }

    void cLevel::AddMove()
    {
        ++m_moves_counter;
        m_hud.SetTextMoves(m_moves_counter);

        if (1 == m_moves_counter)
            m_hud.Set2ndHint();

        UndoData ud(m_player_cube->GetCubePos());

        if (m_player_cube->m_moving_cube)
        {
            ud.moving_cube = m_player_cube->m_moving_cube;
            ud.moving_cube_pos = m_player_cube->m_moving_cube->GetCubePos();
            ud.moving_cube_move_dir = m_player_cube->m_moving_cube->GetMovement();
        }

        if (m_player_cube->m_mover_cube)
        {
            CubePos cp = m_player_cube->m_cube_pos_destination;
            CubePos cp_new = cp;

            // check if cube can move in a selected dir
            m_player_cube->CalcMovement(cp_new, m_player_cube->m_mover_cube->GetMoveDir(), false);

            if (cp.x != cp_new.x || cp.y != cp_new.y || cp.z != cp_new.z)
                ud.mover_cube = m_player_cube->m_mover_cube;
        }

        m_lst_undo.push_back(ud);

        m_hud.SetTextUndo( m_lst_undo.size() );

        m_state_to_restore = m_state;
        m_state = MovingPlayer;
        m_timeout = 0.0f; // move immediately
    }






















}

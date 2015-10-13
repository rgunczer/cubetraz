package com.almagems.cubetraz;

public class Menu {


    enum MenuStateEnum
    {
        InMenu,
        InCredits,
        AnimToCredits,
        AnimFromCredits
    };

    enum LevelCubeDecalTypeEnum
    {
        LevelCubeDecalNumber,
        LevelCubeDecalStars,
        LevelCubeDecalSolver
    };

    struct MenuInitData
    {
        bool reappear;

        MenuInitData()
        {
            reappear = false;
        }
    };


    class cMenu : public cScene
    {

        friend class cAnimator;
        friend class cCreator;
        friend class cMenuNavigator;
        friend class cMenuFaceBuilder;

        private:

        list<cCubeFont*> m_lst_titles[6];
        list<cCubeFont*> m_lst_texts[6];
        list<cCubeFont*> m_lst_symbols[6];

        MenuStateEnum m_state;

        float m_t;
        float zoom;

//    bool render;

        float m_hilite_timeout;

        cMenuNavigator m_navigator;

        bool m_can_alter_text;

        float m_credits_offset;

        Color m_color_down;
        Color m_color_up;

        bool m_showing_help;
        float m_show_help_timeout;

        cCamera m_camera_menu;
        cCamera m_camera_credits;
        cCamera m_camera_current;

        int m_prev_face;
        float m_menu_rotation;

        vec3 m_pos_light_menu;
        vec3 m_pos_light_current;

        cMenuCube* m_pMenuCubePlay;
        cMenuCube* m_pMenuCubeOptions;
        cMenuCube* m_pMenuCubeStore;

        cMenuCube* m_arOptionsCubes[4];

        // store
        cMenuCube* m_pStoreCubeNoAds;
        cMenuCube* m_pStoreCubeSolvers;
        cMenuCube* m_pStoreCubeRestore;

        cMenuCube* m_pCubeCredits;

        cMenuCube* m_menu_cube_hilite;
        cCubeFont m_font_hilite;
        float m_hilite_alpha;


        // fonts on red cubes
        cCubeFont* m_cubefont_play;
        cCubeFont* m_cubefont_options;
        cCubeFont* m_cubefont_store;
        cCubeFont* m_cubefont_noads;
        cCubeFont* m_cubefont_solvers;
        cCubeFont* m_cubefont_restore;

// Cubes
        list<cCube*> m_list_cubes_base;
        list<cCube*> m_list_cubes_face;

        float m_target_rotation_degree;

        cEaseOutDivideInterpolation m_interpolators[6];

        CubeFaceNamesEnum m_current_cube_face;
        CubeFaceTypesEnum m_current_cube_face_type;


    void DisplayCurrentCubeFaceName();
    void DisplayMenuCubePlayCoordinates();

    void EventShowCredits();
    void UpdateInCredits(float dt);
    void UpdateInMenu(float dt);
    void EventPlayLevel(DifficultyEnum difficulty, int level_number);
    void ResetStoreCubes();

    void DrawMenuCubes();
    void DrawLevelCubes();
    void DrawLevelCubesLocked();
    void DrawCredits();
    void DrawLevelCubeOnFace(cLevelCube* pLevelCube, bool scale);
    void DrawLevelNumbersOnFace(float rot, GLuint texture_id, list<cLevelCube*>& list);
    void DrawLevelCubeSymbols();
    void DrawCubeFaceOptions();
    void DrawSymbols(Color color);
    void DrawTexts(Color color);
    void DrawLevelNumbers();
    void DrawLevelNumbersOnLocked();
    void DrawTheCube();
    void DrawLevelCubeDecalsEasy(list<cLevelCube*>& lst_level_cubes_x_plus,
                                 list<cLevelCube*>& lst_level_cubes_x_minus,
                                 list<cLevelCube*>& lst_level_cubes_y_plus,
                                 list<cLevelCube*>& lst_level_cubes_y_minus,
                                 LevelCubeDecalTypeEnum decal_type);

    void DrawLevelCubeDecalsNormal(list<cLevelCube*>& lst_level_cubes_z_plus,
                                   list<cLevelCube*>& lst_level_cubes_z_minus,
                                   list<cLevelCube*>& lst_level_cubes_y_plus,
                                   list<cLevelCube*>& lst_level_cubes_y_minus,
                                   LevelCubeDecalTypeEnum decal_type);

    void DrawLevelCubeDecalsHard(list<cLevelCube*>& lst_level_cubes_x_plus,
                                 list<cLevelCube*>& lst_level_cubes_x_minus,
                                 list<cLevelCube*>& lst_level_cubes_y_plus,
                                 list<cLevelCube*>& lst_level_cubes_y_minus,
                                 LevelCubeDecalTypeEnum decal_type);

    void DrawTextsTitles(Color color);
    void DrawCubeHiLite(Color color);

    void DrawTextsDefaultOrientation(list<cCubeFont*>& lst_x_plus, list<cCubeFont*>& lst_x_minus,
                                     list<cCubeFont*>& lst_y_plus, list<cCubeFont*>& lst_y_minus,
                                     list<cCubeFont*>& lst_z_plus, list<cCubeFont*>& lst_z_minus, Color color);

    void DrawEasyTitles(list<cCubeFont*>& lst_x_plus, list<cCubeFont*>& lst_x_minus,
                        list<cCubeFont*>& lst_y_plus, list<cCubeFont*>& lst_y_minus,
                        list<cCubeFont*>& lst_z_plus, list<cCubeFont*>& lst_z_minus, Color color);

    void DrawNormalTitles(list<cCubeFont*>& lst_x_plus, list<cCubeFont*>& lst_x_minus,
                          list<cCubeFont*>& lst_y_plus, list<cCubeFont*>& lst_y_minus,
                          list<cCubeFont*>& lst_z_plus, list<cCubeFont*>& lst_z_minus, Color color);

    void DrawHardTitles(list<cCubeFont*>& lst_x_plus, list<cCubeFont*>& lst_x_minus,
                        list<cCubeFont*>& lst_y_plus, list<cCubeFont*>& lst_y_minus,
                        list<cCubeFont*>& lst_z_plus, list<cCubeFont*>& lst_z_minus, Color color);

    void UpdateAnimFromCredits(float dt);
    void UpdateAnimToCredits(float dt);

    void UpdatePlayCube(float dt);
    void UpdateOptionsCube(float dt);
    void UpdateStoreCube(float dt);

    void UpdateCubes(float dt);

    void RenderForPicking(PickRenderTypeEnum type);

    void FingerUpOnFaceOptions();
    void FingerUpOnFaceMenu();
    void FingerUpOnFacesEasy();
    void FingerUpOnFacesNormal();
    void FingerUpOnFacesHard();

    void UpdateHilite(float dt);

    void HandleSwipe();
    inline cMenuCube* GetMovingCubeFromColor(int color)
    {
        switch (color)
        {
            case 255: return m_pMenuCubePlay;
            case 200: return m_pMenuCubeOptions;
            case 100: return m_pMenuCubeStore;

            case  40: return m_pStoreCubeNoAds;
            case  50: return m_pStoreCubeSolvers;
            case  60: return m_pStoreCubeRestore;
        }
        return NULL;
    }

    void ReleaseCubeTextsOnFace(CubeFaceTypesEnum face_type);
    void SetCurrentCubeFaceType(CubeFaceTypesEnum face_type);

    public:

    void DontHiliteMenuCube() { m_menu_cube_hilite = NULL; }

    cCamera GetCamera() const { return m_camera_menu; }
    vec3 GetLightPositon() const { return m_pos_light_menu; }

    cMenu();
    virtual ~cMenu();

    virtual void Init(void* init_data = NULL);
    virtual void Update(float dt);
    virtual void Render();

    virtual void OnFingerDown(float x, float y, int finger_count);
    virtual void OnFingerUp(float x, float y, int finger_count);
    virtual void OnFingerMove(float prev_x, float prev_y, float cur_x, float cur_y, int finger_count);


    virtual void SetupCameras();
};


cMenu::cMenu()
        {
//    render = false;
        m_can_alter_text = false;

        m_pos_light_menu = vec3(-10.0f, 3.0f, 12.0f);

        m_cubefont_play = new cCubeFont();
        m_cubefont_options = new cCubeFont();
        m_cubefont_store = new cCubeFont();
        m_cubefont_noads = new cCubeFont();
        m_cubefont_solvers = new cCubeFont();
        m_cubefont_restore = new cCubeFont();
        }

        cMenu::~cMenu()
        {

        }

        #pragma mark - Init

        void cMenu::SetupCameras()
        {
        m_camera_credits.eye = vec3(18.0f / 1.5f, 30.0f / 1.5f, 45.0f / 1.5f);
        m_camera_credits.target = vec3(-2.0f / 1.5f, 0.0f, 5.0f / 1.5f);

        #if defined(DRAW_AXES_CUBE) || defined(DRAW_AXES_GLOBAL)
        m_camera_menu.eye = vec3(1.0f / 1.5f, -1.0f / 1.5f, 34.0f / 1.5f);
        #else
        m_camera_menu.eye = vec3(0.0f, 0.0f, 35.0f / 1.5f);
        #endif

        m_camera_menu.target = vec3(0.0f, 0.0f, 0.0f);

        m_camera_menu.eye = m_camera_menu.eye * engine->m_aspectRatio;
        m_camera_credits.eye = m_camera_credits.eye * engine->m_aspectRatio;

        m_pos_light_current = m_pos_light_menu;
        m_camera_current = m_camera_menu;
        }

        void cMenu::Init(void* init_data)
        {
        SetupCameras();

        //printf("\ncCube size: %lu byte, all cubes size: %lu kbyte\n", sizeof(cCube), (sizeof(cCube)*9*9*9) / 1024);

        engine->dirty_alpha = DIRTY_ALPHA;

        m_hilite_timeout = 0.0f;

        //cMenuFaceBuilder::Custom();

        m_menu_cube_hilite = NULL;

        cCreator::_pHost = this;

        m_showing_help = false;

        m_navigator.m_menu = this;

        engine->PlayMusic(MUSIC_CPU);

        m_prev_face = -1;

        m_fingerdown = false;
        m_swipe = false;

        m_pos_light_current = m_pos_light_menu;
        m_camera_current = m_camera_menu;

        engine->ResetCubes();
        engine->SetupHollowCube();
        engine->BuildVisibleCubesList(m_list_cubes_base);  // could be put in a VBO

        if (true == engine->menu_init_data.reappear)
        {
        switch (m_current_cube_face)
        {
        case Face_Easy01:
        case Face_Easy02:
        case Face_Easy03:
        case Face_Easy04:
        m_navigator.CreateEasyFaces();

        if (Face_Easy02 == m_current_cube_face || Face_Easy03 == m_current_cube_face || Face_Easy04 == m_current_cube_face)
        {
        cMenuFaceBuilder::Build(Face_Empty, Face_Z_Plus);
        cMenuFaceBuilder::Build(Face_Empty, Face_Z_Minus);
        }
        break;

        case Face_Normal01:
        case Face_Normal02:
        case Face_Normal03:
        case Face_Normal04:
        m_navigator.CreateNormalFaces();

        if (Face_Normal02 == m_current_cube_face || Face_Normal03 == m_current_cube_face || Face_Normal04 == m_current_cube_face)
        {
        cMenuFaceBuilder::Build(Face_Empty, Face_X_Plus);
        cMenuFaceBuilder::Build(Face_Empty, Face_X_Minus);
        }
        break;

        case Face_Hard01:
        case Face_Hard02:
        case Face_Hard03:
        case Face_Hard04:
        m_navigator.CreateHardFaces();

        if (Face_Hard02 == m_current_cube_face || Face_Hard03 == m_current_cube_face || Face_Hard04 == m_current_cube_face)
        {
        cMenuFaceBuilder::Build(Face_Empty, Face_Z_Plus);
        cMenuFaceBuilder::Build(Face_Empty, Face_Z_Minus);
        }
        break;

default:
        break;
        }
        }
        else
        {
        cCreator::CreateMovingCubesForMenu();
        //cCreator::CreateStaticTexts();

        m_navigator.Init(this);
        m_current_cube_face = Face_Tutorial;
        SetCurrentCubeFaceType(Face_X_Minus);

        m_navigator.CreateMenuFaces(true);

        m_pMenuCubePlay->SetCubePos(CubePos(0, 5, 4));
        m_pMenuCubeOptions->SetCubePos(CubePos(1, 3, 8));
        m_pMenuCubeStore->SetCubePos(CubePos(1, 1, 8));

        //m_pCubeCredits->SetCubePos(CubePos(1, 1, 8));
        }

        m_state = InMenu;

        engine->BuildVisibleCubesListOnlyOnFaces(m_list_cubes_face);
        Update(0.0);

        glEnable(GL_LIGHT0);
        }

        #pragma mark - Misc

        void cMenu::SetCurrentCubeFaceType(CubeFaceTypesEnum face_type)
        {
        CubePos offset(0,0,0);

        m_current_cube_face_type = face_type;

        switch (face_type)
        {
        case Face_X_Plus:
        offset = CubePos(-1, 0, 0);
        break;

        case Face_X_Minus:
        offset = CubePos(1, 0, 0);
        break;

        case Face_Y_Plus:
        offset = CubePos(0, -1, 0);
        break;

        case Face_Y_Minus:
        offset = CubePos(0, 1, 0);
        break;

        case Face_Z_Plus:
        offset = CubePos(0, 0, -1);
        break;

        case Face_Z_Minus:
        offset = CubePos(0, 0, 1);
        break;

default:
        break;
        }

        m_pMenuCubePlay->SetHiliteOffset(offset);
        m_pMenuCubeOptions->SetHiliteOffset(offset);
        m_pMenuCubeStore->SetHiliteOffset(offset);
        }

        void cMenu::ResetStoreCubes()
        {
        m_pStoreCubeNoAds->SetCubePos( CubePos(1,0,6) );
        m_pStoreCubeSolvers->SetCubePos( CubePos(1,0,4) );
        m_pStoreCubeRestore->SetCubePos( CubePos(1,0,2) );
        }

        void cMenu::ReleaseCubeTextsOnFace(CubeFaceTypesEnum face_type)
        {
        list<cCube*>::iterator it;
        cCube* pCube;

        for (it = m_list_cubes_base.begin(); it != m_list_cubes_base.end(); ++it)
        {
        pCube = *it;

        if (NULL != pCube->ar_fonts[face_type])
        {
        cCreator::CubeFontReleased(pCube->ar_fonts[face_type]);
        pCube->ar_fonts[face_type] = NULL;
        }
        }
        }

//void cMenu::DisplayCurrentCubeFaceName()
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

//void cMenu::DisplayMenuCubePlayCoordinates()
//{
//    printf("\nPlayCube x:%d, y:%d z:%d", m_pMenuCubePlay->m_cube_pos.x, m_pMenuCubePlay->m_cube_pos.y, m_pMenuCubePlay->m_cube_pos.z);
//}

        #pragma mark - Draw

        void cMenu::DrawMenuCubes()
        {
        cRenderer::Prepare();
        cRenderer::SetStreamSource();

        Color color(255, 255, 255, 255);
        cMenuCube* p = m_pMenuCubePlay;

        if (p->visible)
        cRenderer::AddCubeSize(p->pos.x, p->pos.y, p->pos.z, HALF_CUBE_SIZE, color);

        if (m_current_cube_face == Face_Menu || m_current_cube_face == Face_Options || m_navigator.IsCurrentNavigation(Menu_To_Options) || m_navigator.IsCurrentNavigation(Menu_To_Easy1))
        {
        p = m_pMenuCubeOptions;
        cRenderer::AddCubeSize(p->pos.x, p->pos.y, p->pos.z, HALF_CUBE_SIZE, color);
        }

        if (m_current_cube_face == Face_Menu || m_current_cube_face == Face_Store ||
        m_navigator.IsCurrentNavigation(Menu_To_Store) || m_navigator.IsCurrentNavigation(Menu_To_Easy1))
        {
        p = m_pMenuCubeStore;
        cRenderer::AddCubeSize(p->pos.x, p->pos.y, p->pos.z, HALF_CUBE_SIZE, color);

        p = m_pStoreCubeNoAds;
        cRenderer::AddCubeSize(p->pos.x, p->pos.y, p->pos.z, HALF_CUBE_SIZE, color);

        p = m_pStoreCubeSolvers;
        cRenderer::AddCubeSize(p->pos.x, p->pos.y, p->pos.z, HALF_CUBE_SIZE, color);

        p = m_pStoreCubeRestore;
        cRenderer::AddCubeSize(p->pos.x, p->pos.y, p->pos.z, HALF_CUBE_SIZE, color);
        }

        cRenderer::RenderTriangles();
        }

        void cMenu::DrawLevelCubes()
        {
        cRenderer::Prepare();
        cRenderer::SetStreamSource();

        list<cLevelCube*>::iterator it;
        for (int i = 0; i < 6; ++i)
        {
        for (it = engine->ar_cubefacedata[i].lst_level_cubes.begin(); it != engine->ar_cubefacedata[i].lst_level_cubes.end(); ++it)
        cRenderer::AddCube((*it)->pos.x, (*it)->pos.y, (*it)->pos.z);
        }

        cRenderer::RenderTriangles();
        }

        void cMenu::DrawLevelCubesLocked()
        {
        cRenderer::Prepare();
        cRenderer::SetStreamSource();

        list<cLevelCube*>::iterator it;
        for (int i = 0; i < 6; ++i)
        {
        for (it = engine->ar_cubefacedata[i].lst_level_cubes_locked.begin(); it != engine->ar_cubefacedata[i].lst_level_cubes_locked.end(); ++it)
        cRenderer::AddCube((*it)->pos.x, (*it)->pos.y, (*it)->pos.z);
        }

        cRenderer::RenderTriangles();
        }

        void cMenu::DrawLevelCubeDecalsEasy(list<cLevelCube*>& lst_level_cubes_x_plus,
        list<cLevelCube*>& lst_level_cubes_x_minus,
        list<cLevelCube*>& lst_level_cubes_y_plus,
        list<cLevelCube*>& lst_level_cubes_y_minus,
        LevelCubeDecalTypeEnum decal_type)
        {
        cLevelCube* pLevelCube;
        TexCoordsQuad coords;
        TexturedQuad* p;
        Color* color;
        list<cLevelCube*>::iterator it;

        for(it = lst_level_cubes_x_plus.begin(); it != lst_level_cubes_x_plus.end(); ++it)
        {
        pLevelCube = *it;

        p = NULL;

        switch (decal_type)
        {
        case LevelCubeDecalNumber:
        p = pLevelCube->pNumber;
        color = &pLevelCube->color_number;
        break;

        case LevelCubeDecalStars:
        p = pLevelCube->pStars;
        color = &pLevelCube->color_stars_and_solver;
        break;

        case LevelCubeDecalSolver:
        p = pLevelCube->pSolver;
        color = &pLevelCube->color_stars_and_solver;
        break;
        }

        if (p)
        {
        coords.tx0 = vec2(p->tx_up_right.x, p->tx_up_right.y);
        coords.tx1 = vec2(p->tx_up_left.x,  p->tx_up_left.y);
        coords.tx2 = vec2(p->tx_lo_left.x,  p->tx_lo_left.y);
        coords.tx3 = vec2(p->tx_lo_right.x, p->tx_lo_right.y);

        cRenderer::AddCubeFace_X_Plus(pLevelCube->font_pos.x, pLevelCube->font_pos.y, pLevelCube->font_pos.z, coords, *color);
        }
        }

        for(it = lst_level_cubes_x_minus.begin(); it != lst_level_cubes_x_minus.end(); ++it)
        {
        pLevelCube = *it;

        switch (decal_type)
        {
        case LevelCubeDecalNumber:
        p = pLevelCube->pNumber;
        color = &pLevelCube->color_number;
        break;

        case LevelCubeDecalStars:
        p = pLevelCube->pStars;
        color = &pLevelCube->color_stars_and_solver;
        break;

        case LevelCubeDecalSolver:
        p = pLevelCube->pSolver;
        color = &pLevelCube->color_stars_and_solver;
        break;
        }

        if (p)
        {
        coords.tx0 = vec2(p->tx_up_right.x, p->tx_up_right.y);
        coords.tx1 = vec2(p->tx_up_left.x,  p->tx_up_left.y);
        coords.tx2 = vec2(p->tx_lo_left.x,  p->tx_lo_left.y);
        coords.tx3 = vec2(p->tx_lo_right.x, p->tx_lo_right.y);

        cRenderer::AddCubeFace_X_Minus(pLevelCube->font_pos.x, pLevelCube->font_pos.y, pLevelCube->font_pos.z, coords, *color);
        }
        }

        for(it = lst_level_cubes_y_plus.begin(); it != lst_level_cubes_y_plus.end(); ++it)
        {
        pLevelCube = *it;

        switch (decal_type)
        {
        case LevelCubeDecalNumber:
        p = pLevelCube->pNumber;
        color = &pLevelCube->color_number;
        break;

        case LevelCubeDecalStars:
        p = pLevelCube->pStars;
        color = &pLevelCube->color_stars_and_solver;
        break;

        case LevelCubeDecalSolver:
        p = pLevelCube->pSolver;
        color = &pLevelCube->color_stars_and_solver;
        break;
        }

        if (p)
        {
        coords.tx0 = vec2(p->tx_lo_left.x,  p->tx_lo_left.y);
        coords.tx1 = vec2(p->tx_lo_right.x, p->tx_lo_right.y);
        coords.tx2 = vec2(p->tx_up_right.x, p->tx_up_right.y);
        coords.tx3 = vec2(p->tx_up_left.x,  p->tx_up_left.y);

        cRenderer::AddCubeFace_Y_Plus(pLevelCube->font_pos.x, pLevelCube->font_pos.y, pLevelCube->font_pos.z, coords, *color);
        }
        }

        for(it = lst_level_cubes_y_minus.begin(); it != lst_level_cubes_y_minus.end(); ++it)
        {
        pLevelCube = *it;

        switch (decal_type)
        {
        case LevelCubeDecalNumber:
        p = pLevelCube->pNumber;
        color = &pLevelCube->color_number;
        break;

        case LevelCubeDecalStars:
        p = pLevelCube->pStars;
        color = &pLevelCube->color_stars_and_solver;
        break;

        case LevelCubeDecalSolver:
        p = pLevelCube->pSolver;
        color = &pLevelCube->color_stars_and_solver;
        break;
        }

        if (p)
        {
        coords.tx0 = vec2(p->tx_up_right.x, p->tx_up_right.y);
        coords.tx1 = vec2(p->tx_up_left.x,  p->tx_up_left.y);
        coords.tx2 = vec2(p->tx_lo_left.x,  p->tx_lo_left.y);
        coords.tx3 = vec2(p->tx_lo_right.x, p->tx_lo_right.y);

        cRenderer::AddCubeFace_Y_Minus(pLevelCube->font_pos.x, pLevelCube->font_pos.y, pLevelCube->font_pos.z, coords, *color);
        }
        }
        }

        void cMenu::DrawLevelCubeDecalsNormal(list<cLevelCube*>& lst_level_cubes_z_plus,
        list<cLevelCube*>& lst_level_cubes_z_minus,
        list<cLevelCube*>& lst_level_cubes_y_plus,
        list<cLevelCube*>& lst_level_cubes_y_minus,
        LevelCubeDecalTypeEnum decal_type)
        {
        cLevelCube* pLevelCube;
        TexCoordsQuad coords;
        TexturedQuad* p;
        Color* color;
        list<cLevelCube*>::iterator it;

        for(it = lst_level_cubes_z_plus.begin(); it != lst_level_cubes_z_plus.end(); ++it)
        {
        pLevelCube = *it;

        switch (decal_type)
        {
        case LevelCubeDecalNumber:
        p = pLevelCube->pNumber;
        color = &pLevelCube->color_number;
        break;

        case LevelCubeDecalStars:
        p = pLevelCube->pStars;
        color = &pLevelCube->color_stars_and_solver;
        break;

        case LevelCubeDecalSolver:
        p = pLevelCube->pSolver;
        color = &pLevelCube->color_stars_and_solver;
        break;
        }

        if (p)
        {
        coords.tx0 = vec2(p->tx_lo_left.x,  p->tx_lo_left.y);
        coords.tx1 = vec2(p->tx_lo_right.x, p->tx_lo_right.y);
        coords.tx2 = vec2(p->tx_up_right.x, p->tx_up_right.y);
        coords.tx3 = vec2(p->tx_up_left.x,  p->tx_up_left.y);

        cRenderer::AddCubeFace_Z_Plus(pLevelCube->font_pos.x, pLevelCube->font_pos.y, pLevelCube->font_pos.z, coords, *color);
        }
        }

        for(it = lst_level_cubes_z_minus.begin(); it != lst_level_cubes_z_minus.end(); ++it)
        {
        pLevelCube = *it;

        switch (decal_type)
        {
        case LevelCubeDecalNumber:
        p = pLevelCube->pNumber;
        color = &pLevelCube->color_number;
        break;

        case LevelCubeDecalStars:
        p = pLevelCube->pStars;
        color = &pLevelCube->color_stars_and_solver;
        break;

        case LevelCubeDecalSolver:
        p = pLevelCube->pSolver;
        color = &pLevelCube->color_stars_and_solver;
        break;
        }

        if (p)
        {
        coords.tx0 = vec2(p->tx_up_left.x,  p->tx_up_left.y);
        coords.tx1 = vec2(p->tx_lo_left.x,  p->tx_lo_left.y);
        coords.tx2 = vec2(p->tx_lo_right.x, p->tx_lo_right.y);
        coords.tx3 = vec2(p->tx_up_right.x, p->tx_up_right.y);

        cRenderer::AddCubeFace_Z_Minus(pLevelCube->font_pos.x, pLevelCube->font_pos.y, pLevelCube->font_pos.z, coords, *color);
        }
        }

        for(it = lst_level_cubes_y_plus.begin(); it != lst_level_cubes_y_plus.end(); ++it)
        {
        pLevelCube = *it;

        switch (decal_type)
        {
        case LevelCubeDecalNumber:
        p = pLevelCube->pNumber;
        color = &pLevelCube->color_number;
        break;

        case LevelCubeDecalStars:
        p = pLevelCube->pStars;
        color = &pLevelCube->color_stars_and_solver;
        break;

        case LevelCubeDecalSolver:
        p = pLevelCube->pSolver;
        color = &pLevelCube->color_stars_and_solver;
        break;
        }

        if (p)
        {
        coords.tx0 = vec2(p->tx_up_left.x,  p->tx_up_left.y);
        coords.tx1 = vec2(p->tx_lo_left.x,  p->tx_lo_left.y);
        coords.tx2 = vec2(p->tx_lo_right.x, p->tx_lo_right.y);
        coords.tx3 = vec2(p->tx_up_right.x, p->tx_up_right.y);

        cRenderer::AddCubeFace_Y_Plus(pLevelCube->font_pos.x, pLevelCube->font_pos.y, pLevelCube->font_pos.z, coords, *color);
        }
        }

        for(it = lst_level_cubes_y_minus.begin(); it != lst_level_cubes_y_minus.end(); ++it)
        {
        pLevelCube = *it;

        switch (decal_type)
        {
        case LevelCubeDecalNumber:
        p = pLevelCube->pNumber;
        color = &pLevelCube->color_number;
        break;

        case LevelCubeDecalStars:
        p = pLevelCube->pStars;
        color = &pLevelCube->color_stars_and_solver;
        break;

        case LevelCubeDecalSolver:
        p = pLevelCube->pSolver;
        color = &pLevelCube->color_stars_and_solver;
        break;
        }

        if (p)
        {
        coords.tx0 = vec2(p->tx_up_left.x,  p->tx_up_left.y);
        coords.tx1 = vec2(p->tx_lo_left.x,  p->tx_lo_left.y);
        coords.tx2 = vec2(p->tx_lo_right.x, p->tx_lo_right.y);
        coords.tx3 = vec2(p->tx_up_right.x, p->tx_up_right.y);

        cRenderer::AddCubeFace_Y_Minus(pLevelCube->font_pos.x, pLevelCube->font_pos.y, pLevelCube->font_pos.z, coords, *color);
        }
        }
        }

        void cMenu::DrawLevelCubeDecalsHard(list<cLevelCube*>& lst_level_cubes_x_plus,
        list<cLevelCube*>& lst_level_cubes_x_minus,
        list<cLevelCube*>& lst_level_cubes_y_plus,
        list<cLevelCube*>& lst_level_cubes_y_minus,
        LevelCubeDecalTypeEnum decal_type)
        {
        cLevelCube* pLevelCube;
        TexCoordsQuad coords;
        TexturedQuad* p;
        Color* color;
        list<cLevelCube*>::iterator it;

        for(it = lst_level_cubes_x_plus.begin(); it != lst_level_cubes_x_plus.end(); ++it)
        {
        pLevelCube = *it;

        switch (decal_type)
        {
        case LevelCubeDecalNumber:
        p = pLevelCube->pNumber;
        color = &pLevelCube->color_number;
        break;

        case LevelCubeDecalStars:
        p = pLevelCube->pStars;
        color = &pLevelCube->color_stars_and_solver;
        break;

        case LevelCubeDecalSolver:
        p = pLevelCube->pSolver;
        color = &pLevelCube->color_stars_and_solver;
        break;
        }

        if (p)
        {
        coords.tx0 = vec2(p->tx_lo_left.x,  p->tx_lo_left.y);
        coords.tx1 = vec2(p->tx_lo_right.x, p->tx_lo_right.y);
        coords.tx2 = vec2(p->tx_up_right.x, p->tx_up_right.y);
        coords.tx3 = vec2(p->tx_up_left.x,  p->tx_up_left.y);

        cRenderer::AddCubeFace_X_Plus(pLevelCube->font_pos.x, pLevelCube->font_pos.y, pLevelCube->font_pos.z, coords, *color);
        }
        }

        for(it = lst_level_cubes_x_minus.begin(); it != lst_level_cubes_x_minus.end(); ++it)
        {
        pLevelCube = *it;

        switch (decal_type)
        {
        case LevelCubeDecalNumber:
        p = pLevelCube->pNumber;
        color = &pLevelCube->color_number;
        break;

        case LevelCubeDecalStars:
        p = pLevelCube->pStars;
        color = &pLevelCube->color_stars_and_solver;
        break;

        case LevelCubeDecalSolver:
        p = pLevelCube->pSolver;
        color = &pLevelCube->color_stars_and_solver;
        break;
        }

        if (p)
        {
        coords.tx0 = vec2(p->tx_lo_left.x,  p->tx_lo_left.y);
        coords.tx1 = vec2(p->tx_lo_right.x, p->tx_lo_right.y);
        coords.tx2 = vec2(p->tx_up_right.x, p->tx_up_right.y);
        coords.tx3 = vec2(p->tx_up_left.x,  p->tx_up_left.y);

        cRenderer::AddCubeFace_X_Minus(pLevelCube->font_pos.x, pLevelCube->font_pos.y, pLevelCube->font_pos.z, coords, *color);
        }
        }

        for(it = lst_level_cubes_y_plus.begin(); it != lst_level_cubes_y_plus.end(); ++it)
        {
        pLevelCube = *it;

        switch (decal_type)
        {
        case LevelCubeDecalNumber:
        p = pLevelCube->pNumber;
        color = &pLevelCube->color_number;
        break;

        case LevelCubeDecalStars:
        p = pLevelCube->pStars;
        color = &pLevelCube->color_stars_and_solver;
        break;

        case LevelCubeDecalSolver:
        p = pLevelCube->pSolver;
        color = &pLevelCube->color_stars_and_solver;
        break;
        }

        if (p)
        {
        coords.tx0 = vec2(p->tx_up_right.x, p->tx_up_right.y);
        coords.tx1 = vec2(p->tx_up_left.x,  p->tx_up_left.y);
        coords.tx2 = vec2(p->tx_lo_left.x,  p->tx_lo_left.y);
        coords.tx3 = vec2(p->tx_lo_right.x, p->tx_lo_right.y);

        cRenderer::AddCubeFace_Y_Plus(pLevelCube->font_pos.x, pLevelCube->font_pos.y, pLevelCube->font_pos.z, coords, *color);
        }
        }

        for(it = lst_level_cubes_y_minus.begin(); it != lst_level_cubes_y_minus.end(); ++it)
        {
        pLevelCube = *it;

        switch (decal_type)
        {
        case LevelCubeDecalNumber:
        p = pLevelCube->pNumber;
        color = &pLevelCube->color_number;
        break;

        case LevelCubeDecalStars:
        p = pLevelCube->pStars;
        color = &pLevelCube->color_stars_and_solver;
        break;

        case LevelCubeDecalSolver:
        p = pLevelCube->pSolver;
        color = &pLevelCube->color_stars_and_solver;
        break;
        }

        if (p)
        {
        coords.tx0 = vec2(p->tx_lo_left.x,  p->tx_lo_left.y);
        coords.tx1 = vec2(p->tx_lo_right.x, p->tx_lo_right.y);
        coords.tx2 = vec2(p->tx_up_right.x, p->tx_up_right.y);
        coords.tx3 = vec2(p->tx_up_left.x,  p->tx_up_left.y);

        cRenderer::AddCubeFace_Y_Minus(pLevelCube->font_pos.x, pLevelCube->font_pos.y, pLevelCube->font_pos.z, coords, *color);
        }
        }
        }


        void cMenu::DrawLevelNumbersOnLocked()
        {
        cRenderer::Prepare();
        cRenderer::SetStreamSourceFloatAndColor();

        switch (m_current_cube_face)
        {
        case Face_Easy01:
        case Face_Easy02:
        case Face_Easy03:
        case Face_Easy04:
        DrawLevelCubeDecalsEasy(engine->ar_cubefacedata[Face_X_Plus].lst_level_cubes_locked,
        engine->ar_cubefacedata[Face_X_Minus].lst_level_cubes_locked,
        engine->ar_cubefacedata[Face_Y_Plus].lst_level_cubes_locked,
        engine->ar_cubefacedata[Face_Y_Minus].lst_level_cubes_locked,
        LevelCubeDecalNumber);

        break;

        case Face_Normal01:
        case Face_Normal02:
        case Face_Normal03:
        case Face_Normal04:
        DrawLevelCubeDecalsNormal(engine->ar_cubefacedata[Face_Z_Plus].lst_level_cubes_locked,
        engine->ar_cubefacedata[Face_Z_Minus].lst_level_cubes_locked,
        engine->ar_cubefacedata[Face_Y_Plus].lst_level_cubes_locked,
        engine->ar_cubefacedata[Face_Y_Minus].lst_level_cubes_locked,
        LevelCubeDecalNumber);
        break;

        case Face_Hard01:
        case Face_Hard02:
        case Face_Hard03:
        case Face_Hard04:
        DrawLevelCubeDecalsHard(engine->ar_cubefacedata[Face_X_Plus].lst_level_cubes_locked,
        engine->ar_cubefacedata[Face_X_Minus].lst_level_cubes_locked,
        engine->ar_cubefacedata[Face_Y_Plus].lst_level_cubes_locked,
        engine->ar_cubefacedata[Face_Y_Minus].lst_level_cubes_locked,
        LevelCubeDecalNumber);
        break;

default:
        break;
        }

        cRenderer::RenderTriangles();
        }


        void cMenu::DrawLevelNumbers()
        {
        cRenderer::Prepare();
        cRenderer::SetStreamSourceFloatAndColor();

        switch (m_current_cube_face)
        {
        case Face_Easy01:
        case Face_Easy02:
        case Face_Easy03:
        case Face_Easy04:
        DrawLevelCubeDecalsEasy(engine->ar_cubefacedata[Face_X_Plus].lst_level_cubes,
        engine->ar_cubefacedata[Face_X_Minus].lst_level_cubes,
        engine->ar_cubefacedata[Face_Y_Plus].lst_level_cubes,
        engine->ar_cubefacedata[Face_Y_Minus].lst_level_cubes,
        LevelCubeDecalNumber);

        break;

        case Face_Normal01:
        case Face_Normal02:
        case Face_Normal03:
        case Face_Normal04:
        DrawLevelCubeDecalsNormal(engine->ar_cubefacedata[Face_Z_Plus].lst_level_cubes,
        engine->ar_cubefacedata[Face_Z_Minus].lst_level_cubes,
        engine->ar_cubefacedata[Face_Y_Plus].lst_level_cubes,
        engine->ar_cubefacedata[Face_Y_Minus].lst_level_cubes,
        LevelCubeDecalNumber);
        break;

        case Face_Hard01:
        case Face_Hard02:
        case Face_Hard03:
        case Face_Hard04:
        DrawLevelCubeDecalsHard(engine->ar_cubefacedata[Face_X_Plus].lst_level_cubes,
        engine->ar_cubefacedata[Face_X_Minus].lst_level_cubes,
        engine->ar_cubefacedata[Face_Y_Plus].lst_level_cubes,
        engine->ar_cubefacedata[Face_Y_Minus].lst_level_cubes,
        LevelCubeDecalNumber);
        break;

default:
        break;
        }

        cRenderer::RenderTriangles();
        }

        void cMenu::DrawTexts(Color color)
        {
        cRenderer::Prepare();

        if (abs(m_navigator.m_cube_rotation_secondary.degree) < EPSILON)
        {
        DrawTextsDefaultOrientation(m_lst_texts[Face_X_Plus],
        m_lst_texts[Face_X_Minus],
        m_lst_texts[Face_Y_Plus],
        m_lst_texts[Face_Y_Minus],
        m_lst_texts[Face_Z_Plus],
        m_lst_texts[Face_Z_Minus], color);
        }
        else
        {
        switch (m_current_cube_face)
        {
        case Face_Easy01:
        case Face_Easy02:
        case Face_Easy03:
        case Face_Easy04:
        DrawEasyTitles(m_lst_texts[Face_X_Plus],  m_lst_texts[Face_X_Minus], m_lst_texts[Face_Y_Plus],
        m_lst_texts[Face_Y_Minus], m_lst_texts[Face_Z_Plus],  m_lst_texts[Face_Z_Minus], color);
        break;

        case Face_Normal01:
        case Face_Normal02:
        case Face_Normal03:
        case Face_Normal04:
        DrawNormalTitles(m_lst_texts[Face_X_Plus],  m_lst_texts[Face_X_Minus], m_lst_texts[Face_Y_Plus],
        m_lst_texts[Face_Y_Minus], m_lst_texts[Face_Z_Plus],  m_lst_texts[Face_Z_Minus], color);
        break;

        case Face_Hard01:
        case Face_Hard02:
        case Face_Hard03:
        case Face_Hard04:
        DrawHardTitles(m_lst_texts[Face_X_Plus],  m_lst_texts[Face_X_Minus], m_lst_texts[Face_Y_Plus],
        m_lst_texts[Face_Y_Minus], m_lst_texts[Face_Z_Plus],  m_lst_texts[Face_Z_Minus], color);
        break;

default:
        break;
        }
        }

        cCubeFont* pCubeFont;
        TexturedQuad* pFont;
        TexCoordsQuad coords;

        // Draw CubeFonts on Red Cubes [P]lay [O]ptions [S]tore [U]nlock [R]estore
        if (m_navigator.IsCurrentNavigation(Menu_To_Easy1) || m_current_cube_face == Face_Menu || m_current_cube_face == Face_Options || m_current_cube_face == Face_Store)
        {
        Color colr = color;

        if (m_pMenuCubePlay->IsDone() && m_pMenuCubePlay->m_cube_pos.x == 1)
        {
        pCubeFont = m_cubefont_play;
        pFont = pCubeFont->GetFont();

        if (pFont)
        {
        coords.tx0 = vec2(pFont->tx_up_right.x, pFont->tx_up_right.y);
        coords.tx1 = vec2(pFont->tx_up_left.x,  pFont->tx_up_left.y);
        coords.tx2 = vec2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);
        coords.tx3 = vec2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);

        cRenderer::AddCubeFace_Z_Plus(pCubeFont->pos.x, pCubeFont->pos.y, pCubeFont->pos.z, coords, colr);
        }
        }

        if (m_pMenuCubeOptions->IsDone() && m_pMenuCubeOptions->m_cube_pos.x == 1)
        {
        pCubeFont = m_cubefont_options;
        pFont = pCubeFont->GetFont();

        if (pFont)
        {
        coords.tx0 = vec2(pFont->tx_up_right.x, pFont->tx_up_right.y);
        coords.tx1 = vec2(pFont->tx_up_left.x,  pFont->tx_up_left.y);
        coords.tx2 = vec2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);
        coords.tx3 = vec2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);

        cRenderer::AddCubeFace_Z_Plus(pCubeFont->pos.x, pCubeFont->pos.y, pCubeFont->pos.z, coords, colr);
        }
        }

        if (m_pMenuCubeStore->IsDone() && m_pMenuCubeStore->m_cube_pos.x == 1)
        {
        pCubeFont = m_cubefont_store;
        pFont = pCubeFont->GetFont();

        if (pFont)
        {
        coords.tx0 = vec2(pFont->tx_up_right.x, pFont->tx_up_right.y);
        coords.tx1 = vec2(pFont->tx_up_left.x,  pFont->tx_up_left.y);
        coords.tx2 = vec2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);
        coords.tx3 = vec2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);

        cRenderer::AddCubeFace_Z_Plus(pCubeFont->pos.x, pCubeFont->pos.y, pCubeFont->pos.z, coords, colr);
        }
        }

        if (m_pStoreCubeNoAds->IsDone() && m_pStoreCubeNoAds->m_cube_pos.x == 1)
        {
        pCubeFont = m_cubefont_noads;
        pFont = pCubeFont->GetFont();

        if (pFont)
        {
        coords.tx0 = vec2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);
        coords.tx1 = vec2(pFont->tx_up_right.x, pFont->tx_up_right.y);
        coords.tx2 = vec2(pFont->tx_up_left.x,  pFont->tx_up_left.y);
        coords.tx3 = vec2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);

        cRenderer::AddCubeFace_Y_Minus(pCubeFont->pos.x, pCubeFont->pos.y, pCubeFont->pos.z, coords, colr);
        }
        }

        if (m_pStoreCubeSolvers->IsDone() && m_pStoreCubeSolvers->m_cube_pos.x == 1)
        {
        pCubeFont = m_cubefont_solvers;
        pFont = pCubeFont->GetFont();

        if (pFont)
        {
        coords.tx0 = vec2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);
        coords.tx1 = vec2(pFont->tx_up_right.x, pFont->tx_up_right.y);
        coords.tx2 = vec2(pFont->tx_up_left.x,  pFont->tx_up_left.y);
        coords.tx3 = vec2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);

        cRenderer::AddCubeFace_Y_Minus(pCubeFont->pos.x, pCubeFont->pos.y, pCubeFont->pos.z, coords, colr);
        }
        }

        if (m_pStoreCubeRestore->IsDone() && m_pStoreCubeRestore->m_cube_pos.x == 1)
        {
        pCubeFont = m_cubefont_restore;
        pFont = pCubeFont->GetFont();

        if (pFont)
        {
        coords.tx0 = vec2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);
        coords.tx1 = vec2(pFont->tx_up_right.x, pFont->tx_up_right.y);
        coords.tx2 = vec2(pFont->tx_up_left.x,  pFont->tx_up_left.y);
        coords.tx3 = vec2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);

        cRenderer::AddCubeFace_Y_Minus(pCubeFont->pos.x, pCubeFont->pos.y, pCubeFont->pos.z, coords, colr);
        }
        }
        }

        cRenderer::RenderTriangles();
        }

        void cMenu::DrawTextsTitles(Color color)
        {
        cRenderer::Prepare();

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

        cRenderer::RenderTriangles();
        }

        void cMenu::DrawTextsDefaultOrientation(list<cCubeFont*>& lst_x_plus,
        list<cCubeFont*>& lst_x_minus,
        list<cCubeFont*>& lst_y_plus,
        list<cCubeFont*>& lst_y_minus,
        list<cCubeFont*>& lst_z_plus,
        list<cCubeFont*>& lst_z_minus, Color color)
        {
        list<cCubeFont*>::iterator it;
        cCubeFont* pCubeFont;
        TexturedQuad* pFont;
        TexCoordsQuad coords;

        for (it = lst_z_plus.begin(); it != lst_z_plus.end(); ++it)
        {
        pCubeFont = *it;
        pFont = pCubeFont->GetFont();

        coords.tx0 = vec2(pFont->tx_up_right.x, pFont->tx_up_right.y);
        coords.tx1 = vec2(pFont->tx_up_left.x,  pFont->tx_up_left.y);
        coords.tx2 = vec2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);
        coords.tx3 = vec2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);

        cRenderer::AddCubeFace_Z_Plus(pCubeFont->pos.x, pCubeFont->pos.y, pCubeFont->pos.z, coords, color);
        }

        for (it = lst_x_plus.begin(); it != lst_x_plus.end(); ++it)
        {
        pCubeFont = *it;
        pFont = pCubeFont->GetFont();

        coords.tx0 = vec2(pFont->tx_up_right.x, pFont->tx_up_right.y);
        coords.tx1 = vec2(pFont->tx_up_left.x,  pFont->tx_up_left.y);
        coords.tx2 = vec2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);
        coords.tx3 = vec2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);

        cRenderer::AddCubeFace_X_Plus(pCubeFont->pos.x, pCubeFont->pos.y, pCubeFont->pos.z, coords, color);
        }

        for (it = lst_z_minus.begin(); it != lst_z_minus.end(); ++it)
        {
        pCubeFont = *it;
        pFont = pCubeFont->GetFont();

        coords.tx0 = vec2(pFont->tx_up_left.x,  pFont->tx_up_left.y);
        coords.tx1 = vec2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);
        coords.tx2 = vec2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);
        coords.tx3 = vec2(pFont->tx_up_right.x, pFont->tx_up_right.y);

        cRenderer::AddCubeFace_Z_Minus(pCubeFont->pos.x, pCubeFont->pos.y, pCubeFont->pos.z, coords, color);
        }

        for (it = lst_x_minus.begin(); it != lst_x_minus.end(); ++it)
        {
        pCubeFont = *it;
        pFont = pCubeFont->GetFont();

        coords.tx0 = vec2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);
        coords.tx1 = vec2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);
        coords.tx2 = vec2(pFont->tx_up_right.x, pFont->tx_up_right.y);
        coords.tx3 = vec2(pFont->tx_up_left.x,  pFont->tx_up_left.y);

        cRenderer::AddCubeFace_X_Minus(pCubeFont->pos.x, pCubeFont->pos.y, pCubeFont->pos.z, coords, color);
        }

        for (it = lst_y_plus.begin(); it != lst_y_plus.end(); ++it)
        {
        pCubeFont = *it;
        pFont = pCubeFont->GetFont();

        coords.tx0 = vec2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);
        coords.tx1 = vec2(pFont->tx_up_right.x, pFont->tx_up_right.y);
        coords.tx2 = vec2(pFont->tx_up_left.x,  pFont->tx_up_left.y);
        coords.tx3 = vec2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);

        cRenderer::AddCubeFace_Y_Plus(pCubeFont->pos.x, pCubeFont->pos.y, pCubeFont->pos.z, coords, color);
        }

        for (it = lst_y_minus.begin(); it != lst_y_minus.end(); ++it)
        {
        pCubeFont = *it;
        pFont = pCubeFont->GetFont();

        coords.tx0 = vec2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);
        coords.tx1 = vec2(pFont->tx_up_right.x, pFont->tx_up_right.y);
        coords.tx2 = vec2(pFont->tx_up_left.x,  pFont->tx_up_left.y);
        coords.tx3 = vec2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);

        cRenderer::AddCubeFace_Y_Minus(pCubeFont->pos.x, pCubeFont->pos.y, pCubeFont->pos.z, coords, color);
        }
        }

        void cMenu::DrawEasyTitles(list<cCubeFont*>& lst_x_plus,
        list<cCubeFont*>& lst_x_minus,
        list<cCubeFont*>& lst_y_plus,
        list<cCubeFont*>& lst_y_minus,
        list<cCubeFont*>& lst_z_plus,
        list<cCubeFont*>& lst_z_minus, Color color)
        {
        list<cCubeFont*>::iterator it;
        CubeFaceTypesEnum face_type;
        cCubeFont* pCubeFont;
        TexturedQuad* pFont;
        TexCoordsQuad coords;

        face_type = Face_X_Plus;
        for (it = lst_x_plus.begin(); it != lst_x_plus.end(); ++it)
        {
        pCubeFont = *it;
        pFont = pCubeFont->GetFont();

        coords.tx0 = vec2(pFont->tx_up_right.x, pFont->tx_up_right.y);
        coords.tx1 = vec2(pFont->tx_up_left.x,  pFont->tx_up_left.y);
        coords.tx2 = vec2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);
        coords.tx3 = vec2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);

        cRenderer::AddCubeFace_X_Plus(pCubeFont->pos.x, pCubeFont->pos.y, pCubeFont->pos.z, coords, color);
        }

        face_type = Face_Y_Minus;
        for (it = lst_y_minus.begin(); it != lst_y_minus.end(); ++it)
        {
        pCubeFont = *it;
        pFont = pCubeFont->GetFont();

        coords.tx0 = vec2(pFont->tx_up_right.x, pFont->tx_up_right.y);
        coords.tx1 = vec2(pFont->tx_up_left.x,  pFont->tx_up_left.y);
        coords.tx2 = vec2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);
        coords.tx3 = vec2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);

        cRenderer::AddCubeFace_Y_Minus(pCubeFont->pos.x, pCubeFont->pos.y, pCubeFont->pos.z, coords, color);
        }

        face_type = Face_X_Minus;
        for (it = lst_x_minus.begin(); it != lst_x_minus.end(); ++it)
        {
        pCubeFont = *it;
        pFont = pCubeFont->GetFont();

        coords.tx0 = vec2(pFont->tx_up_right.x, pFont->tx_up_right.y);
        coords.tx1 = vec2(pFont->tx_up_left.x,  pFont->tx_up_left.y);
        coords.tx2 = vec2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);
        coords.tx3 = vec2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);

        cRenderer::AddCubeFace_X_Minus(pCubeFont->pos.x, pCubeFont->pos.y, pCubeFont->pos.z, coords, color);
        }

        face_type = Face_Y_Plus;
        for (it = lst_y_plus.begin(); it != lst_y_plus.end(); ++it)
        {
        pCubeFont = *it;
        pFont = pCubeFont->GetFont();

        coords.tx0 = vec2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);
        coords.tx1 = vec2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);
        coords.tx2 = vec2(pFont->tx_up_right.x, pFont->tx_up_right.y);
        coords.tx3 = vec2(pFont->tx_up_left.x,  pFont->tx_up_left.y);

        cRenderer::AddCubeFace_Y_Plus(pCubeFont->pos.x, pCubeFont->pos.y, pCubeFont->pos.z, coords, color);
        }
        }

        void cMenu::DrawNormalTitles(list<cCubeFont*>& lst_x_plus,
        list<cCubeFont*>& lst_x_minus,
        list<cCubeFont*>& lst_y_plus,
        list<cCubeFont*>& lst_y_minus,
        list<cCubeFont*>& lst_z_plus,
        list<cCubeFont*>& lst_z_minus, Color color)
        {
        list<cCubeFont*>::iterator it;
        CubeFaceTypesEnum face_type;
        cCubeFont* pCubeFont;
        TexturedQuad* pFont;
        TexCoordsQuad coords;

        face_type = Face_Z_Minus;
        for (it = lst_z_minus.begin(); it != lst_z_minus.end(); ++it)
        {
        pCubeFont = *it;
        pFont = pCubeFont->GetFont();

        coords.tx0 = vec2(pFont->tx_up_left.x,  pFont->tx_up_left.y);
        coords.tx1 = vec2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);
        coords.tx2 = vec2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);
        coords.tx3 = vec2(pFont->tx_up_right.x, pFont->tx_up_right.y);

        cRenderer::AddCubeFace_Z_Minus(pCubeFont->pos.x, pCubeFont->pos.y, pCubeFont->pos.z, coords, color);
        }

        face_type = Face_Y_Minus;
        for (it = lst_y_minus.begin(); it != lst_y_minus.end(); ++it)
        {
        pCubeFont = *it;
        pFont = pCubeFont->GetFont();

        coords.tx0 = vec2(pFont->tx_up_left.x,  pFont->tx_up_left.y);
        coords.tx1 = vec2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);
        coords.tx2 = vec2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);
        coords.tx3 = vec2(pFont->tx_up_right.x, pFont->tx_up_right.y);

        cRenderer::AddCubeFace_Y_Minus(pCubeFont->pos.x, pCubeFont->pos.y, pCubeFont->pos.z, coords, color);
        }

        face_type = Face_Z_Plus;
        for (it = lst_z_plus.begin(); it != lst_z_plus.end(); ++it)
        {
        pCubeFont = *it;
        pFont = pCubeFont->GetFont();

        coords.tx0 = vec2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);
        coords.tx1 = vec2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);
        coords.tx2 = vec2(pFont->tx_up_right.x, pFont->tx_up_right.y);
        coords.tx3 = vec2(pFont->tx_up_left.x,  pFont->tx_up_left.y);

        cRenderer::AddCubeFace_Z_Plus(pCubeFont->pos.x, pCubeFont->pos.y, pCubeFont->pos.z, coords, color);
        }

        face_type = Face_Y_Plus;
        for (it = lst_y_plus.begin(); it != lst_y_plus.end(); ++it)
        {
        pCubeFont = *it;
        pFont = pCubeFont->GetFont();

        coords.tx0 = vec2(pFont->tx_up_left.x,  pFont->tx_up_left.y);
        coords.tx1 = vec2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);
        coords.tx2 = vec2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);
        coords.tx3 = vec2(pFont->tx_up_right.x, pFont->tx_up_right.y);

        cRenderer::AddCubeFace_Y_Plus(pCubeFont->pos.x, pCubeFont->pos.y, pCubeFont->pos.z, coords, color);
        }
        }

        void cMenu::DrawHardTitles(list<cCubeFont*>& lst_x_plus,
        list<cCubeFont*>& lst_x_minus,
        list<cCubeFont*>& lst_y_plus,
        list<cCubeFont*>& lst_y_minus,
        list<cCubeFont*>& lst_z_plus,
        list<cCubeFont*>& lst_z_minus, Color color)
        {
        list<cCubeFont*>::iterator it;
        CubeFaceTypesEnum face_type;
        cCubeFont* pCubeFont;
        TexturedQuad* pFont;
        TexCoordsQuad coords;

        face_type = Face_X_Minus;
        for (it = lst_x_minus.begin(); it != lst_x_minus.end(); ++it)
        {
        pCubeFont = *it;
        pFont = pCubeFont->GetFont();

        coords.tx0 = vec2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);
        coords.tx1 = vec2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);
        coords.tx2 = vec2(pFont->tx_up_right.x, pFont->tx_up_right.y);
        coords.tx3 = vec2(pFont->tx_up_left.x,  pFont->tx_up_left.y);

        cRenderer::AddCubeFace_X_Minus(pCubeFont->pos.x, pCubeFont->pos.y, pCubeFont->pos.z, coords, color);
        }

        face_type = Face_Y_Minus;
        for (it = lst_y_minus.begin(); it != lst_y_minus.end(); ++it)
        {
        pCubeFont = *it;
        pFont = pCubeFont->GetFont();

        coords.tx0 = vec2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);
        coords.tx1 = vec2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);
        coords.tx2 = vec2(pFont->tx_up_right.x, pFont->tx_up_right.y);
        coords.tx3 = vec2(pFont->tx_up_left.x,  pFont->tx_up_left.y);

        cRenderer::AddCubeFace_Y_Minus(pCubeFont->pos.x, pCubeFont->pos.y, pCubeFont->pos.z, coords, color);
        }

        face_type = Face_X_Plus;
        for (it = lst_x_plus.begin(); it != lst_x_plus.end(); ++it)
        {
        pCubeFont = *it;
        pFont = pCubeFont->GetFont();

        coords.tx0 = vec2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);
        coords.tx1 = vec2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);
        coords.tx2 = vec2(pFont->tx_up_right.x, pFont->tx_up_right.y);
        coords.tx3 = vec2(pFont->tx_up_left.x,  pFont->tx_up_left.y);

        cRenderer::AddCubeFace_X_Plus(pCubeFont->pos.x, pCubeFont->pos.y, pCubeFont->pos.z, coords, color);
        }

        face_type = Face_Y_Plus;
        for (it = lst_y_plus.begin(); it != lst_y_plus.end(); ++it)
        {
        pCubeFont = *it;
        pFont = pCubeFont->GetFont();

        coords.tx0 = vec2(pFont->tx_up_right.x, pFont->tx_up_right.y);
        coords.tx1 = vec2(pFont->tx_up_left.x,  pFont->tx_up_left.y);
        coords.tx2 = vec2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);
        coords.tx3 = vec2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);

        cRenderer::AddCubeFace_Y_Plus(pCubeFont->pos.x, pCubeFont->pos.y, pCubeFont->pos.z, coords, color);
        }
        }

        void cMenu::DrawSymbols(Color color)
        {
        cRenderer::Prepare();
        cRenderer::SetStreamSourceFloatAndColor();

        DrawTextsDefaultOrientation(m_lst_symbols[Face_X_Plus],
        m_lst_symbols[Face_X_Minus],
        m_lst_symbols[Face_Y_Plus],
        m_lst_symbols[Face_Y_Minus],
        m_lst_symbols[Face_Z_Plus],
        m_lst_symbols[Face_Z_Minus], color);

        cRenderer::RenderTriangles();
        }

        void cMenu::DrawCubeFaceOptions()
        {
        cCube* p = &engine->cubes[2][7][3];

        float x = p->tx - HALF_CUBE_SIZE;
        float y = p->ty + HALF_CUBE_SIZE + 0.01f;
        float z = p->tz - HALF_CUBE_SIZE;
        float w = CUBE_SIZE * 5.0f * engine->GetMusicVolume();
        float ws = CUBE_SIZE * 5.0f * engine->GetSoundVolume();
        float zs = engine->cubes[2][7][6].tz - HALF_CUBE_SIZE;

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
        glTranslatef(engine->cube_offset.x, engine->cube_offset.y, engine->cube_offset.z);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4); // progress bar music
        glDrawArrays(GL_TRIANGLE_FAN, 4, 4); // progress bar soundfx
        glPopMatrix();

        glEnable(GL_TEXTURE_2D);
        }

        void cMenu::DrawLevelCubeSymbols()
        {
        cRenderer::Prepare();
        cRenderer::SetStreamSourceFloat();

        switch (m_current_cube_face)
        {
        case Face_Easy01:
        case Face_Easy02:
        case Face_Easy03:
        case Face_Easy04:

        DrawLevelCubeDecalsEasy(engine->ar_cubefacedata[Face_X_Plus].lst_level_cubes,
        engine->ar_cubefacedata[Face_X_Minus].lst_level_cubes,
        engine->ar_cubefacedata[Face_Y_Plus].lst_level_cubes,
        engine->ar_cubefacedata[Face_Y_Minus].lst_level_cubes,
        LevelCubeDecalStars);

        DrawLevelCubeDecalsEasy(engine->ar_cubefacedata[Face_X_Plus].lst_level_cubes,
        engine->ar_cubefacedata[Face_X_Minus].lst_level_cubes,
        engine->ar_cubefacedata[Face_Y_Plus].lst_level_cubes,
        engine->ar_cubefacedata[Face_Y_Minus].lst_level_cubes,
        LevelCubeDecalSolver);

        break;

        case Face_Normal01:
        case Face_Normal02:
        case Face_Normal03:
        case Face_Normal04:
        DrawLevelCubeDecalsNormal(engine->ar_cubefacedata[Face_Z_Plus].lst_level_cubes,
        engine->ar_cubefacedata[Face_Z_Minus].lst_level_cubes,
        engine->ar_cubefacedata[Face_Y_Plus].lst_level_cubes,
        engine->ar_cubefacedata[Face_Y_Minus].lst_level_cubes,
        LevelCubeDecalStars);

        DrawLevelCubeDecalsNormal(engine->ar_cubefacedata[Face_Z_Plus].lst_level_cubes,
        engine->ar_cubefacedata[Face_Z_Minus].lst_level_cubes,
        engine->ar_cubefacedata[Face_Y_Plus].lst_level_cubes,
        engine->ar_cubefacedata[Face_Y_Minus].lst_level_cubes,
        LevelCubeDecalSolver);
        break;

        case Face_Hard01:
        case Face_Hard02:
        case Face_Hard03:
        case Face_Hard04:
        DrawLevelCubeDecalsHard(engine->ar_cubefacedata[Face_X_Plus].lst_level_cubes,
        engine->ar_cubefacedata[Face_X_Minus].lst_level_cubes,
        engine->ar_cubefacedata[Face_Y_Plus].lst_level_cubes,
        engine->ar_cubefacedata[Face_Y_Minus].lst_level_cubes,
        LevelCubeDecalStars);

        DrawLevelCubeDecalsHard(engine->ar_cubefacedata[Face_X_Plus].lst_level_cubes,
        engine->ar_cubefacedata[Face_X_Minus].lst_level_cubes,
        engine->ar_cubefacedata[Face_Y_Plus].lst_level_cubes,
        engine->ar_cubefacedata[Face_Y_Minus].lst_level_cubes,
        LevelCubeDecalSolver);
        break;

default:
        break;
        }

        cRenderer::RenderTriangles();
        }

        void cMenu::DrawCredits()
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

        void cMenu::DrawTheCube()
        {
        cRenderer::Prepare();
        cRenderer::SetStreamSource();

        list<cCube*>::iterator it;
        for (it = m_list_cubes_base.begin(); it != m_list_cubes_base.end(); ++it)
        cRenderer::AddCubeWithColor((*it)->tx, (*it)->ty, (*it)->tz, (*it)->color_current);

        cRenderer::RenderTriangles(engine->cube_offset.x, engine->cube_offset.y, engine->cube_offset.z);

        cRenderer::Prepare();

        for (it = m_list_cubes_face.begin(); it != m_list_cubes_face.end(); ++it)
        cRenderer::AddCubeWithColor((*it)->tx, (*it)->ty, (*it)->tz, (*it)->color_current);

        cRenderer::RenderTriangles(engine->cube_offset.x, engine->cube_offset.y, engine->cube_offset.z);
        }

        void cMenu::DrawCubeHiLite(Color color)
        {
        TexCoordsQuad coords;

        TexturedQuad* p = m_font_hilite.GetFont();
        coords.tx0 = vec2(p->tx_up_right.x, p->tx_up_right.y);
        coords.tx1 = vec2(p->tx_up_left.x,  p->tx_up_left.y);
        coords.tx2 = vec2(p->tx_lo_left.x,  p->tx_lo_left.y);
        coords.tx3 = vec2(p->tx_lo_right.x, p->tx_lo_right.y);

        cRenderer::Prepare();
        cRenderer::SetStreamSourceFloatAndColor();

        switch (m_current_cube_face_type)
        {
        case Face_X_Plus:
        cRenderer::AddCubeFace_X_Plus(m_font_hilite.pos.x, m_font_hilite.pos.y, m_font_hilite.pos.z, coords, color);
        break;

        case Face_X_Minus:
        cRenderer::AddCubeFace_X_Minus(m_font_hilite.pos.x, m_font_hilite.pos.y, m_font_hilite.pos.z, coords, color);
        break;

        case Face_Y_Plus:
        cRenderer::AddCubeFace_Y_Plus(m_font_hilite.pos.x, m_font_hilite.pos.y, m_font_hilite.pos.z, coords, color);
        break;

        case Face_Y_Minus:
        cRenderer::AddCubeFace_Y_Minus(m_font_hilite.pos.x, m_font_hilite.pos.y, m_font_hilite.pos.z, coords, color);
        break;

        case Face_Z_Plus:
        cRenderer::AddCubeFace_Z_Plus(m_font_hilite.pos.x, m_font_hilite.pos.y, m_font_hilite.pos.z, coords, color);
        break;

        case Face_Z_Minus:
        cRenderer::AddCubeFace_Z_Minus(m_font_hilite.pos.x, m_font_hilite.pos.y, m_font_hilite.pos.z, coords, color);
        break;

default:
        break;
        }

        cRenderer::RenderTriangles();
        }

        #pragma mark - UpdateAnim

        void cMenu::UpdateAnimToCredits(float dt)
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
        engine->dirty_alpha = LERP(DIRTY_ALPHA, 0, m_t);

        m_pMenuCubeStore->Update(dt);
        }

        void cMenu::UpdateAnimFromCredits(float dt)
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
        engine->dirty_alpha = LERP(0, DIRTY_ALPHA, m_t);
        }

        void cMenu::UpdatePlayCube(float dt)
        {
        if (m_pMenuCubePlay->IsDone())
        {
        switch (m_current_cube_face)
        {
        case Face_Tutorial:

        if (m_pMenuCubePlay->m_cube_pos.z == 8)
        {
        m_current_cube_face = Face_Menu;
        SetCurrentCubeFaceType(Face_Z_Plus);
        m_pMenuCubePlay->MoveOnAxis(X_Plus);
        m_navigator.Setup(Tutorial_To_Menu);
        }

        if (m_pMenuCubePlay->m_cube_pos.y == 7 && m_can_alter_text)
        {
        m_can_alter_text = false;
        cMenuFaceBuilder::ResetTransforms();
        cMenuFaceBuilder::AddTransform(MirrorVert);
        ReleaseCubeTextsOnFace(Face_X_Minus);
        cMenuFaceBuilder::BuildTexts(Face_Tutorial, Face_X_Minus, true);
        }

        if (m_pMenuCubePlay->m_cube_pos.y == 1 && m_can_alter_text)
        {
        m_can_alter_text = false;
        cMenuFaceBuilder::ResetTransforms();
        cMenuFaceBuilder::AddTransform(MirrorVert);
        ReleaseCubeTextsOnFace(Face_X_Minus);
        cMenuFaceBuilder::BuildTexts(Face_Tutorial, Face_X_Minus);
        }

        break;

        case Face_Menu:

        if (m_pMenuCubePlay->m_cube_pos.x == 2 && m_pMenuCubePlay->m_cube_pos.y == 7 && m_pMenuCubePlay->m_cube_pos.z == 8)
        {
        m_pMenuCubePlay->MoveOnAxis(Y_Minus);
        }

        if (m_pMenuCubePlay->m_cube_pos.x == 2 && m_pMenuCubePlay->m_cube_pos.y == 5 && m_pMenuCubePlay->m_cube_pos.z == 8)
        {
        m_pMenuCubePlay->MoveOnAxis(X_Minus);
        }

        if (m_pMenuCubePlay->m_cube_pos.x == 8)
        {
        m_current_cube_face = Face_Easy01;
        SetCurrentCubeFaceType(Face_X_Plus);
        m_pMenuCubePlay->MoveOnAxis(Z_Minus);
        m_navigator.Setup(Menu_To_Easy1);
        }
        break;

        case Face_Easy01:

        if (m_pMenuCubePlay->m_cube_pos.z == 0)
        {
        m_current_cube_face = Face_Normal01;
        SetCurrentCubeFaceType(Face_Z_Minus);
        m_pMenuCubePlay->MoveOnAxis(X_Minus);
        m_navigator.Setup(Easy1_To_Normal1);
        }

        if (m_pMenuCubePlay->m_cube_pos.z == 8)
        {
        m_current_cube_face = Face_Menu;
        SetCurrentCubeFaceType(Face_Z_Plus);
        m_pMenuCubePlay->MoveOnAxis(X_Minus);
        m_navigator.Setup(Easy1_To_Menu);
        }

        if (m_pMenuCubePlay->m_cube_pos.y == 0)
        {
        m_current_cube_face = Face_Easy02;
        SetCurrentCubeFaceType(Face_Y_Minus);
        m_pMenuCubePlay->MoveOnAxis(X_Minus);
        m_navigator.Setup(Easy1_To_Easy2);
        }

        if (m_pMenuCubePlay->m_cube_pos.y == 8)
        {
        m_current_cube_face = Face_Easy04;
        SetCurrentCubeFaceType(Face_Y_Plus);
        m_pMenuCubePlay->MoveOnAxis(X_Minus);
        m_navigator.Setup(Easy1_To_Easy4);
        }

        break;

        case Face_Easy02:

        if (m_pMenuCubePlay->m_cube_pos.x == 8)
        {
        m_current_cube_face = Face_Easy01;
        SetCurrentCubeFaceType(Face_X_Plus);
        m_pMenuCubePlay->MoveOnAxis(Y_Plus);
        m_navigator.Setup(Easy2_To_Easy1);
        }

        if (m_pMenuCubePlay->m_cube_pos.x == 0)
        {
        m_current_cube_face = Face_Easy03;
        SetCurrentCubeFaceType(Face_X_Minus);
        m_pMenuCubePlay->MoveOnAxis(Y_Plus);
        m_navigator.Setup(Easy2_To_Easy3);
        }

        break;

        case Face_Easy03:

        if (m_pMenuCubePlay->m_cube_pos.y == 0)
        {
        m_current_cube_face = Face_Easy02;
        SetCurrentCubeFaceType(Face_Y_Minus);
        m_pMenuCubePlay->MoveOnAxis(X_Plus);
        m_navigator.Setup(Easy3_To_Easy2);
        }

        if (m_pMenuCubePlay->m_cube_pos.y == 8)
        {
        m_current_cube_face = Face_Easy04;
        SetCurrentCubeFaceType(Face_Y_Plus);
        m_pMenuCubePlay->MoveOnAxis(X_Plus);
        m_navigator.Setup(Easy3_To_Easy4);
        }

        break;

        case Face_Easy04:

        if (m_pMenuCubePlay->m_cube_pos.x == 8)
        {
        m_current_cube_face = Face_Easy01;
        SetCurrentCubeFaceType(Face_X_Plus);
        m_pMenuCubePlay->MoveOnAxis(Y_Minus);
        m_navigator.Setup(Easy4_To_Easy1);
        }

        if (m_pMenuCubePlay->m_cube_pos.x == 0)
        {
        m_current_cube_face = Face_Easy03;
        SetCurrentCubeFaceType(Face_X_Minus);
        m_pMenuCubePlay->MoveOnAxis(Y_Minus);
        m_navigator.Setup(Easy4_To_Easy3);
        }

        break;

        case Face_Normal01:

        if (m_pMenuCubePlay->m_cube_pos.x == 8)
        {
        m_current_cube_face = Face_Easy01;
        SetCurrentCubeFaceType(Face_X_Plus);
        m_pMenuCubePlay->MoveOnAxis(Z_Plus);
        m_navigator.Setup(Normal1_To_Easy1);
        }

        if (m_pMenuCubePlay->m_cube_pos.x == 0)
        {
        m_current_cube_face = Face_Hard01;
        SetCurrentCubeFaceType(Face_X_Minus);
        m_pMenuCubePlay->MoveOnAxis(Z_Plus);
        m_navigator.Setup(Normal1_To_Hard1);
        }

        if (m_pMenuCubePlay->m_cube_pos.y == 0)
        {
        m_current_cube_face = Face_Normal02;
        SetCurrentCubeFaceType(Face_Y_Minus);
        m_pMenuCubePlay->MoveOnAxis(Z_Plus);
        m_navigator.Setup(Normal1_To_Normal2);
        }

        if (m_pMenuCubePlay->m_cube_pos.y == 8)
        {
        m_current_cube_face = Face_Normal04;
        SetCurrentCubeFaceType(Face_Y_Plus);
        m_pMenuCubePlay->MoveOnAxis(Z_Plus);
        m_navigator.Setup(Normal1_To_Normal4);
        }

        break;

        case Face_Normal02:

        if (m_pMenuCubePlay->m_cube_pos.z == 0)
        {
        m_current_cube_face = Face_Normal01;
        SetCurrentCubeFaceType(Face_Z_Minus);
        m_pMenuCubePlay->MoveOnAxis(Y_Plus);
        m_navigator.Setup(Normal2_To_Normal1);
        }

        if (m_pMenuCubePlay->m_cube_pos.z == 8)
        {
        m_current_cube_face = Face_Normal03;
        SetCurrentCubeFaceType(Face_Z_Plus);
        m_pMenuCubePlay->MoveOnAxis(Y_Plus);
        m_navigator.Setup(Normal2_To_Normal3);
        }

        break;

        case Face_Normal03:

        if (m_pMenuCubePlay->m_cube_pos.y == 0)
        {
        m_current_cube_face = Face_Normal02;
        SetCurrentCubeFaceType(Face_Y_Minus);
        m_pMenuCubePlay->MoveOnAxis(Z_Minus);
        m_navigator.Setup(Normal3_To_Normal2);
        }

        if (m_pMenuCubePlay->m_cube_pos.y == 8)
        {
        m_current_cube_face = Face_Normal04;
        SetCurrentCubeFaceType(Face_Y_Plus);
        m_pMenuCubePlay->MoveOnAxis(Z_Minus);
        m_navigator.Setup(Normal3_To_Normal4);
        }

        break;

        case Face_Normal04:

        if (m_pMenuCubePlay->m_cube_pos.z == 8)
        {
        m_current_cube_face = Face_Normal03;
        SetCurrentCubeFaceType(Face_Z_Plus);
        m_pMenuCubePlay->MoveOnAxis(Y_Minus);
        m_navigator.Setup(Normal4_To_Normal3);
        }

        if (m_pMenuCubePlay->m_cube_pos.z == 0)
        {
        m_current_cube_face = Face_Normal01;
        SetCurrentCubeFaceType(Face_Z_Minus);
        m_pMenuCubePlay->MoveOnAxis(Y_Minus);
        m_navigator.Setup(Normal4_To_Normal1);
        }

        break;

        case Face_Hard01:

        if (m_pMenuCubePlay->m_cube_pos.z == 0)
        {
        m_current_cube_face = Face_Normal01;
        SetCurrentCubeFaceType(Face_Z_Minus);
        m_pMenuCubePlay->MoveOnAxis(X_Plus);
        m_navigator.Setup(Hard1_To_Normal1);
        }

        if (m_pMenuCubePlay->m_cube_pos.z == 8)
        {
        m_current_cube_face = Face_Menu;
        SetCurrentCubeFaceType(Face_Z_Plus);
        m_pMenuCubePlay->MoveOnAxis(X_Plus);
        m_navigator.Setup(Hard1_To_Menu);
        }

        if (m_pMenuCubePlay->m_cube_pos.y == 0)
        {
        m_current_cube_face = Face_Hard02;
        SetCurrentCubeFaceType(Face_Y_Minus);
        m_pMenuCubePlay->MoveOnAxis(X_Plus);
        m_navigator.Setup(Hard1_To_Hard2);
        }

        if (m_pMenuCubePlay->m_cube_pos.y == 8)
        {
        m_current_cube_face = Face_Hard04;
        SetCurrentCubeFaceType(Face_Y_Plus);
        m_pMenuCubePlay->MoveOnAxis(X_Plus);
        m_navigator.Setup(Hard1_To_Hard4);
        }

        break;

        case Face_Hard02:

        if (m_pMenuCubePlay->m_cube_pos.x == 0)
        {
        m_current_cube_face = Face_Hard01;
        SetCurrentCubeFaceType(Face_X_Minus);
        m_pMenuCubePlay->MoveOnAxis(Y_Plus);
        m_navigator.Setup(Hard2_To_Hard1);
        }

        if (m_pMenuCubePlay->m_cube_pos.x == 8)
        {
        m_current_cube_face = Face_Hard03;
        SetCurrentCubeFaceType(Face_X_Plus);
        m_pMenuCubePlay->MoveOnAxis(Y_Plus);
        m_navigator.Setup(Hard2_To_Hard3);
        }

        break;

        case Face_Hard03:

        if (m_pMenuCubePlay->m_cube_pos.y == 8)
        {
        m_current_cube_face = Face_Hard04;
        SetCurrentCubeFaceType(Face_Y_Plus);
        m_pMenuCubePlay->MoveOnAxis(X_Minus);
        m_navigator.Setup(Hard3_To_Hard4);
        }

        if (m_pMenuCubePlay->m_cube_pos.y == 0)
        {
        m_current_cube_face = Face_Hard02;
        SetCurrentCubeFaceType(Face_Y_Minus);
        m_pMenuCubePlay->MoveOnAxis(X_Minus);
        m_navigator.Setup(Hard3_To_Hard2);
        }

        break;

        case Face_Hard04:

        if (m_pMenuCubePlay->m_cube_pos.x == 8)
        {
        m_current_cube_face = Face_Hard03;
        SetCurrentCubeFaceType(Face_X_Plus);
        m_pMenuCubePlay->MoveOnAxis(Y_Minus);
        m_navigator.Setup(Hard4_To_Hard3);
        }

        if (m_pMenuCubePlay->m_cube_pos.x == 0)
        {
        m_current_cube_face = Face_Hard01;
        SetCurrentCubeFaceType(Face_X_Minus);
        m_pMenuCubePlay->MoveOnAxis(Y_Minus);
        m_navigator.Setup(Hard4_To_Hard1);
        }

        break;

default:
        break;
        } // switch
        }
        else
        {
        m_pMenuCubePlay->Update(dt);
        m_can_alter_text = true;
        }
        }

        void cMenu::UpdateOptionsCube(float dt)
        {
        if (m_pMenuCubeOptions->IsDone())
        {
        switch (m_current_cube_face)
        {
        case Face_Menu:

        if (m_pMenuCubeOptions->m_cube_pos.y == 8)
        {
        m_prev_face = Face_Menu;
        m_current_cube_face = Face_Options;
        SetCurrentCubeFaceType(Face_Y_Plus);
        m_pMenuCubeOptions->MoveOnAxis(Z_Minus);

        m_navigator.Setup(Menu_To_Options);
        }

        if (m_pMenuCubeOptions->m_cube_pos.x == 7)
        {
        if (Face_Options == m_prev_face)
        {
        m_prev_face = -1;
        m_pMenuCubeOptions->MoveOnAxis(X_Minus);
        }
        }

        break;

        case Face_Options:

        if (m_pMenuCubeOptions->m_cube_pos.z == 8)
        {
        m_prev_face = Face_Options;
        m_current_cube_face = Face_Menu;
        SetCurrentCubeFaceType(Face_Z_Plus);
        m_pMenuCubeOptions->MoveOnAxis(Y_Minus);

        m_navigator.Setup(Options_To_Menu);
        }

        if (m_pMenuCubeOptions->m_cube_pos.x == 7 && m_pMenuCubeOptions->m_cube_pos.y == 8 && m_pMenuCubeOptions->m_cube_pos.z == 1)
        {
//                    list<cCubeFont*>::iterator it;
//                    for (it = engine->ar_cubefacedata[Face_Y_Plus].lst_symbols.begin(); it != engine->ar_cubefacedata[Face_Y_Plus].lst_symbols.end(); ++it)
//                    {
//                        (*it)->visible = true;
//                    }
        }
        break;

default:
        break;
        } // switch
        }
        else
        m_pMenuCubeOptions->Update(dt);
        }

        void cMenu::UpdateStoreCube(float dt)
        {
        if (m_pMenuCubeStore->IsDone())
        {
        switch (m_current_cube_face)
        {
        case Face_Menu:
        #ifdef LITE_VERSION

        if (m_pMenuCubeStore->m_cube_pos.x == 7)
        {
        m_pMenuCubeStore->MoveOnAxis(X_Minus);
        EventShowCredits();
        }
        #else
        if (m_pMenuCubeStore->m_cube_pos.y == 0)
        {
        m_prev_face = Face_Menu;
        m_current_cube_face = Face_Store;
        SetCurrentCubeFaceType(Face_Y_Minus);
        m_pMenuCubeStore->MoveOnAxis(Z_Minus);

        m_navigator.Setup(Menu_To_Store);
        }

        if (m_pMenuCubeStore->m_cube_pos.x == 7)
        {
        if (!m_pMenuCubeOptions->IsDone() || !m_pMenuCubePlay->IsDone() || m_prev_face == Face_Store)
        {
        if (m_prev_face == Face_Store)
        m_prev_face = -1;

        m_pMenuCubeStore->MoveOnAxis(X_Minus);
        }
        }
        #endif
        break;

        case Face_Store:

        if (m_pMenuCubeStore->m_cube_pos.z == 8)
        {
        m_prev_face = Face_Store;
        m_current_cube_face = Face_Menu;
        SetCurrentCubeFaceType(Face_Z_Plus);
        m_pMenuCubeStore->MoveOnAxis(Y_Plus);

        engine->HideProgressIndicator();
        m_navigator.Setup(Store_To_Menu);
        }

        break;

default:
        break;

        } // switch
        }
        else
        m_pMenuCubeStore->Update(dt);
        }

        void cMenu::UpdateCubes(float dt)
        {
        for (int i = 0; i < 6; ++i)
        {
        m_lst_titles[i].clear();
        m_lst_texts[i].clear();
        m_lst_symbols[i].clear();
        }

        CubeFaceTypesEnum face_type;

        cCube* pCube;

        list<cCube*>::iterator it;

        for (it = m_list_cubes_base.begin(); it != m_list_cubes_base.end(); ++it)
        {
        pCube = *it;

        face_type = Face_X_Plus;
        if (NULL != pCube->ar_fonts[face_type])
        m_lst_texts[face_type].push_back(pCube->ar_fonts[face_type]);

        if (NULL != pCube->ar_symbols[face_type])
        m_lst_symbols[face_type].push_back(pCube->ar_symbols[face_type]);


        face_type = Face_X_Minus;
        if (NULL != pCube->ar_fonts[face_type])
        m_lst_texts[face_type].push_back(pCube->ar_fonts[face_type]);

        if (NULL != pCube->ar_symbols[face_type])
        m_lst_symbols[face_type].push_back(pCube->ar_symbols[face_type]);


        face_type = Face_Y_Plus;
        if (NULL != pCube->ar_fonts[face_type])
        m_lst_texts[face_type].push_back(pCube->ar_fonts[face_type]);

        if (NULL != pCube->ar_symbols[face_type])
        m_lst_symbols[face_type].push_back(pCube->ar_symbols[face_type]);


        face_type = Face_Y_Minus;
        if (NULL != pCube->ar_fonts[face_type])
        m_lst_texts[face_type].push_back(pCube->ar_fonts[face_type]);

        if (NULL != pCube->ar_symbols[face_type])
        m_lst_symbols[face_type].push_back(pCube->ar_symbols[face_type]);


        face_type = Face_Z_Plus;
        if (NULL != pCube->ar_fonts[face_type])
        m_lst_texts[face_type].push_back(pCube->ar_fonts[face_type]);

        if (NULL != pCube->ar_symbols[face_type])
        m_lst_symbols[face_type].push_back(pCube->ar_symbols[face_type]);


        face_type = Face_Z_Minus;
        if (NULL != pCube->ar_fonts[face_type])
        m_lst_texts[face_type].push_back(pCube->ar_fonts[face_type]);

        if (NULL != pCube->ar_symbols[face_type])
        m_lst_symbols[face_type].push_back(pCube->ar_symbols[face_type]);
        }

        for (it = m_list_cubes_face.begin(); it != m_list_cubes_face.end(); ++it)
        {
        pCube = *it;

        face_type = Face_X_Plus;
        if (NULL != pCube->ar_fonts[face_type])
        m_lst_titles[face_type].push_back(pCube->ar_fonts[face_type]);

        if (NULL != pCube->ar_symbols[face_type])
        m_lst_symbols[face_type].push_back(pCube->ar_symbols[face_type]);


        face_type = Face_X_Minus;
        if (NULL != pCube->ar_fonts[face_type])
        m_lst_titles[face_type].push_back(pCube->ar_fonts[face_type]);

        if (NULL != pCube->ar_symbols[face_type])
        m_lst_symbols[face_type].push_back(pCube->ar_symbols[face_type]);


        face_type = Face_Y_Plus;
        if (NULL != pCube->ar_fonts[face_type])
        m_lst_titles[face_type].push_back(pCube->ar_fonts[face_type]);

        if (NULL != pCube->ar_symbols[face_type])
        m_lst_symbols[face_type].push_back(pCube->ar_symbols[face_type]);


        face_type = Face_Y_Minus;
        if (NULL != pCube->ar_fonts[face_type])
        m_lst_titles[face_type].push_back(pCube->ar_fonts[face_type]);

        if (NULL != pCube->ar_symbols[face_type])
        m_lst_symbols[face_type].push_back(pCube->ar_symbols[face_type]);


        face_type = Face_Z_Plus;
        if (NULL != pCube->ar_fonts[face_type])
        m_lst_titles[face_type].push_back(pCube->ar_fonts[face_type]);

        if (NULL != pCube->ar_symbols[face_type])
        m_lst_symbols[face_type].push_back(pCube->ar_symbols[face_type]);


        face_type = Face_Z_Minus;
        if (NULL != pCube->ar_fonts[face_type])
        m_lst_titles[face_type].push_back(pCube->ar_fonts[face_type]);

        if (NULL != pCube->ar_symbols[face_type])
        m_lst_symbols[face_type].push_back(pCube->ar_symbols[face_type]);
        }

//    printf("\nFace_Y_Symbol Size: %lu", m_lst_symbols[Face_Y_Plus].size());

        if (m_menu_cube_hilite)
        {
        m_hilite_alpha += 0.05f;

        if (m_hilite_alpha > 0.2f)
        m_hilite_alpha = 0.2f;
        }

        for (it = m_list_cubes_base.begin(); it != m_list_cubes_base.end(); ++it)
        (*it)->WarmByFactor(WARM_FACTOR);
        }

        #pragma mark - UpdateIn

        void cMenu::UpdateInCredits(float dt)
        {
        m_credits_offset -= 0.05f;

        if (m_credits_offset < -5.0f)
        m_credits_offset = 20.0f;
        }

        void cMenu::UpdateInMenu(float dt)
        {
        switch (m_current_cube_face)
        {
        case Face_Menu:
        break;

        case Face_Store:
        m_pStoreCubeNoAds->Update(dt);
        m_pStoreCubeSolvers->Update(dt);
        m_pStoreCubeRestore->Update(dt);

        if (m_pStoreCubeNoAds->IsDone() && m_pStoreCubeNoAds->m_cube_pos.x == 7)
        {
        engine->PurchaseRemoveAds();
        m_pStoreCubeNoAds->MoveOnAxis(X_Minus);
        }

        if (m_pStoreCubeSolvers->IsDone() && m_pStoreCubeSolvers->m_cube_pos.x == 7)
        {
        engine->PurchaseSolvers();
        m_pStoreCubeSolvers->MoveOnAxis(X_Minus);
        }

        if (m_pStoreCubeRestore->IsDone() && m_pStoreCubeRestore->m_cube_pos.x == 7)
        {
        engine->PurchaseRestore();
        m_pStoreCubeRestore->MoveOnAxis(X_Minus);
        }
        break;

default:
        break;
        }

        UpdatePlayCube(dt);
        UpdateOptionsCube(dt);
        UpdateStoreCube(dt);
        }

        void cMenu::UpdateHilite(float dt)
        {
        m_hilite_timeout -= dt;

        if (m_hilite_timeout < 0.0f)
        {
        m_hilite_timeout = 0.05f;

        Color color(160, 160, 160, 255);

        if (!m_pMenuCubePlay->lst_cubes_to_hilite.empty())
        {
        cCube* p = m_pMenuCubePlay->lst_cubes_to_hilite.front();
        m_pMenuCubePlay->lst_cubes_to_hilite.pop_front();

        p->color_current = color;
        }

        if (!m_pMenuCubeOptions->lst_cubes_to_hilite.empty())
        {
        cCube* p = m_pMenuCubeOptions->lst_cubes_to_hilite.front();
        m_pMenuCubeOptions->lst_cubes_to_hilite.pop_front();

        p->color_current = color;
        }

        if (!m_pMenuCubeStore->lst_cubes_to_hilite.empty())
        {
        cCube* p = m_pMenuCubeStore->lst_cubes_to_hilite.front();
        m_pMenuCubeStore->lst_cubes_to_hilite.pop_front();

        p->color_current = color;
        }

        if (!m_pStoreCubeNoAds->lst_cubes_to_hilite.empty())
        {
        cCube* p = m_pStoreCubeNoAds->lst_cubes_to_hilite.front();
        m_pStoreCubeNoAds->lst_cubes_to_hilite.pop_front();

        p->color_current = color;
        }



        if (!m_pStoreCubeRestore->lst_cubes_to_hilite.empty())
        {
        cCube* p = m_pStoreCubeRestore->lst_cubes_to_hilite.front();
        m_pStoreCubeRestore->lst_cubes_to_hilite.pop_front();

        p->color_current = color;
        }
        }
        }

        #pragma mark - Update

        void cMenu::Update(float dt)
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

        void cMenu::EventPlayLevel(DifficultyEnum difficulty, int level_number)
        {
        if (false == engine->GetCanPlayLockedLevels())
        {
        switch (difficulty)
        {
        case Easy:
        if (LEVEL_LOCKED == cCubetraz::GetStarsEasy(level_number))
        {
        engine->PlaySound(SOUND_TAP_ON_LOCKED_LEVEL_CUBE);
        return;
        }
        break;

        case Normal:
        if (LEVEL_LOCKED == cCubetraz::GetStarsNormal(level_number))
        {
        engine->PlaySound(SOUND_TAP_ON_LOCKED_LEVEL_CUBE);
        return;
        }
        break;

        case Hard:
        if (LEVEL_LOCKED == cCubetraz::GetStarsHard(level_number))
        {
        engine->PlaySound(SOUND_TAP_ON_LOCKED_LEVEL_CUBE);
        return;
        }
        break;
        }
        }

        engine->PlaySound(SOUND_TAP_ON_LEVEL_CUBE);

        engine->StopMusic();
        engine->anim_init_data.ClearTransforms();

        switch (difficulty)
        {
        case Easy:
        //printf("\nPlay Easy level: %d\n", level_number);

        if (level_number >= 1 && level_number <= 15)
        engine->anim_init_data.SetFaces(Face_Easy01, Face_Easy04, Face_Menu);

        if (level_number >= 16 && level_number <= 30)
        engine->anim_init_data.SetFaces(Face_Easy02, Face_Easy01, Face_Empty);

        if (level_number >= 31 && level_number <= 45)
        engine->anim_init_data.SetFaces(Face_Easy03, Face_Easy02, Face_Empty);

        if (level_number >= 46 && level_number <= 60)
        engine->anim_init_data.SetFaces(Face_Easy04, Face_Easy03, Face_Empty);

        break;

        case Normal:
        //printf("\nPlay Normal level: %d\n", level_number);

        if (level_number >= 1 && level_number <= 15)
        engine->anim_init_data.SetFaces(Face_Normal01, Face_Normal04, Face_Easy01);

        if (level_number >= 16 && level_number <= 30)
        engine->anim_init_data.SetFaces(Face_Normal02, Face_Normal01, Face_Empty);

        if (level_number >= 31 && level_number <= 45)
        engine->anim_init_data.SetFaces(Face_Normal03, Face_Normal02, Face_Empty);

        if (level_number >= 46 && level_number <= 60)
        engine->anim_init_data.SetFaces(Face_Normal04, Face_Normal03, Face_Empty);

        break;

        case Hard:
        //printf("\nPlay Hard level: %d\n", level_number);

        if (level_number >= 1 && level_number <= 15)
        engine->anim_init_data.SetFaces(Face_Hard01, Face_Hard04, Face_Normal01);

        if (level_number >= 16 && level_number <= 30)
        engine->anim_init_data.SetFaces(Face_Hard02, Face_Hard01, Face_Empty);

        if (level_number >= 31 && level_number <= 45)
        engine->anim_init_data.SetFaces(Face_Hard03, Face_Hard02, Face_Empty);

        if (level_number >= 46 && level_number <= 60)
        engine->anim_init_data.SetFaces(Face_Hard04, Face_Hard03, Face_Empty);

        break;
        }

        engine->level_init_data.difficulty = difficulty;
        engine->level_init_data.level_number = level_number;
        engine->level_init_data.init_action = FullInit;

        engine->anim_init_data.type = AnimToLevel;

        engine->anim_init_data.list_cubes_base.clear();

        list<cCube*>::iterator it;
        for (it = m_list_cubes_base.begin(); it != m_list_cubes_base.end(); ++it)
        {
        engine->anim_init_data.list_cubes_base.push_back(*it);
        }

        engine->anim_init_data.camera_from = m_camera_current;
        engine->anim_init_data.camera_to = engine->m_level->m_camera_level;

        engine->anim_init_data.pos_light_from = m_pos_light_current;
        engine->anim_init_data.pos_light_to = engine->m_level->m_pos_light;

        engine->ShowScene(Scene_Anim);
        }

        void cMenu::EventShowCredits()
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

        engine->HideGameCenterInfo();
        }

        #pragma mark - Render

        void cMenu::RenderForPicking(PickRenderTypeEnum type)
        {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glDisable(GL_LIGHTING);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);

        glDisable(GL_TEXTURE_2D);

        engine->SetProjection3D();
        engine->SetModelViewMatrix3D(m_camera_current);

        m_navigator.ApplyRotations();

        glPushMatrix();
        glTranslatef(engine->cube_offset.x, engine->cube_offset.y, engine->cube_offset.z);

        cRenderer::Prepare();

        switch (type)
        {
        case RenderOnlyOptions:
        cRenderer::AddCubeSize(m_arOptionsCubes[0]->pos.x, m_arOptionsCubes[0]->pos.y, m_arOptionsCubes[0]->pos.z, HALF_CUBE_SIZE * 1.5f, m_arOptionsCubes[0]->color);
        cRenderer::AddCubeSize(m_arOptionsCubes[1]->pos.x, m_arOptionsCubes[1]->pos.y, m_arOptionsCubes[1]->pos.z, HALF_CUBE_SIZE * 1.5f, m_arOptionsCubes[1]->color);
        cRenderer::AddCubeSize(m_arOptionsCubes[2]->pos.x, m_arOptionsCubes[2]->pos.y, m_arOptionsCubes[2]->pos.z, HALF_CUBE_SIZE * 1.5f, m_arOptionsCubes[2]->color);
        cRenderer::AddCubeSize(m_arOptionsCubes[3]->pos.x, m_arOptionsCubes[3]->pos.y, m_arOptionsCubes[3]->pos.z, HALF_CUBE_SIZE * 1.5f, m_arOptionsCubes[3]->color);
        break;

        case RenderOnlyMovingCubePlay:
        cRenderer::AddCubeSize(m_pMenuCubePlay->pos.x, m_pMenuCubePlay->pos.y, m_pMenuCubePlay->pos.z, HALF_CUBE_SIZE * 1.5f, m_pMenuCubePlay->color);
        break;

        #ifndef LITE_VERSION
        case RenderOnlyMovingCubeStore:
        cRenderer::AddCubeSize(m_pMenuCubeStore->pos.x, m_pMenuCubeStore->pos.y, m_pMenuCubeStore->pos.z, HALF_CUBE_SIZE * 1.5f, m_pMenuCubeStore->color);
        break;
        #endif

        case RenderOnlyMovingCubeOptions:
        cRenderer::AddCubeSize(m_pMenuCubeOptions->pos.x, m_pMenuCubeOptions->pos.y, m_pMenuCubeOptions->pos.z, HALF_CUBE_SIZE * 1.5f, m_pMenuCubeOptions->color);
        break;

        #ifndef LITE_VERSION
        case RenderOnlyCubeCredits:
        cRenderer::AddCubeSize(m_pCubeCredits->pos.x, m_pCubeCredits->pos.y, m_pCubeCredits->pos.z, HALF_CUBE_SIZE * 1.5f, m_pCubeCredits->color);
        break;
        #endif

        case RenderOnlyMovingCubes:

        switch (m_current_cube_face)
        {
        case Face_Options:
        cRenderer::AddCubeSize(m_pMenuCubeOptions->pos.x, m_pMenuCubeOptions->pos.y, m_pMenuCubeOptions->pos.z, HALF_CUBE_SIZE * 1.5f, m_pMenuCubeOptions->color);
        break;
        #ifndef LITE_VERSION
        case Face_Store:
        cRenderer::AddCubeSize(m_pMenuCubeStore->pos.x, m_pMenuCubeStore->pos.y, m_pMenuCubeStore->pos.z, HALF_CUBE_SIZE * 1.5f, m_pMenuCubeStore->color);
        cRenderer::AddCubeSize(m_pStoreCubeNoAds->pos.x, m_pStoreCubeNoAds->pos.y, m_pStoreCubeNoAds->pos.z, HALF_CUBE_SIZE * 1.5f, m_pStoreCubeNoAds->color);
        cRenderer::AddCubeSize(m_pStoreCubeRestore->pos.x, m_pStoreCubeRestore->pos.y, m_pStoreCubeRestore->pos.z, HALF_CUBE_SIZE * 1.5f, m_pStoreCubeRestore->color);
        cRenderer::AddCubeSize(m_pStoreCubeSolvers->pos.x, m_pStoreCubeSolvers->pos.y, m_pStoreCubeSolvers->pos.z, HALF_CUBE_SIZE * 1.5f, m_pStoreCubeSolvers->color);
        break;
        #endif
        case Face_Menu:
        cRenderer::AddCubeSize(m_pMenuCubePlay->pos.x, m_pMenuCubePlay->pos.y, m_pMenuCubePlay->pos.z, HALF_CUBE_SIZE * 1.5f, m_pMenuCubePlay->color);
        cRenderer::AddCubeSize(m_pMenuCubeOptions->pos.x, m_pMenuCubeOptions->pos.y, m_pMenuCubeOptions->pos.z, HALF_CUBE_SIZE * 1.5f, m_pMenuCubeOptions->color);
        cRenderer::AddCubeSize(m_pMenuCubeStore->pos.x, m_pMenuCubeStore->pos.y, m_pMenuCubeStore->pos.z, HALF_CUBE_SIZE * 1.5f, m_pMenuCubeStore->color);
        #ifndef LITE_VERSION
        cRenderer::AddCubeSize(m_pCubeCredits->pos.x, m_pCubeCredits->pos.y, m_pCubeCredits->pos.z, HALF_CUBE_SIZE * 1.5f, m_pCubeCredits->color);
        #endif
        break;

default:
        cRenderer::AddCubeSize(m_pMenuCubePlay->pos.x, m_pMenuCubePlay->pos.y, m_pMenuCubePlay->pos.z, HALF_CUBE_SIZE * 1.5f, m_pMenuCubePlay->color);
        break;
        } // switch

        break;

        case RenderOnlyLevelCubes:
        {
        cLevelCube* p;
        list<cLevelCube*>::iterator it;

        for (it = engine->ar_cubefacedata[m_current_cube_face_type].lst_level_cubes.begin(); it != engine->ar_cubefacedata[m_current_cube_face_type].lst_level_cubes.end(); ++it)
        {
        p = *it;
        if (m_current_cube_face == p->face_id)
        {
        cRenderer::AddCubeSize(p->pos.x, p->pos.y, p->pos.z, HALF_CUBE_SIZE * 1.5f, p->color);
        }
        }
        }
        break;

default:
        break;
        }	// switch

        cRenderer::SetStreamSourceOnlyVerticeAndColor();
        cRenderer::RenderTriangles();

        glPopMatrix();
        }

        void cMenu::Render()
        {
        //printf("\nAspect ratio: %f", engine->m_aspectRatio);

        //printf("\nm_camera_current %f, %f, %f", m_camera_current.eye.x, m_camera_current.eye.y, m_camera_current.eye.z);


//    if (render)
//        return RenderForPicking(RenderOnlyLevelCubes);

        engine->SetProjection2D();
        engine->SetModelViewMatrix2D();

        glEnable(GL_BLEND);
        glDisable(GL_LIGHTING);
        glDepthMask(GL_FALSE);
        glEnable(GL_TEXTURE_2D);

        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);

        Color color(255, 255, 255, engine->dirty_alpha);
        engine->DrawFBOTexture(engine->texture_id_dirty, color);

        glDepthMask(GL_TRUE);


        engine->SetProjection3D();
        engine->SetModelViewMatrix3D(m_camera_current);

        const vec4 lightPosition(m_pos_light_current.x, m_pos_light_current.y, m_pos_light_current.z, 1.0f);
        glLightfv(GL_LIGHT0, GL_POSITION, lightPosition.Pointer());

        glEnable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);

        glEnableClientState(GL_NORMAL_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        if (InCredits == m_state)
        {
        glBindTexture(GL_TEXTURE_2D, engine->texture_id_credits);
        DrawCredits();
        }

        #ifdef DRAW_AXES_GLOBAL
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_LIGHTING);
        engine->DrawAxis();
        glEnable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);
        #endif

        m_navigator.ApplyRotations();

        glBindTexture(GL_TEXTURE_2D, engine->texture_id_gray_concrete);
        DrawTheCube();

        #ifdef DRAW_AXES_CUBE
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_LIGHTING);
        engine->DrawAxes();
        glEnable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);
        #endif

        glPushMatrix();

        glTranslatef(engine->cube_offset.x, engine->cube_offset.y, engine->cube_offset.z);

        glBindTexture(GL_TEXTURE_2D, engine->texture_id_level_cubes);
        DrawLevelCubes();

        glBindTexture(GL_TEXTURE_2D, engine->texture_id_player);
        DrawMenuCubes();

        glPopMatrix();

        glDisable(GL_LIGHTING);
        cRenderer::EnableBlending();

        cRenderer::SetStreamSourceFloatAndColor();

        glPushMatrix();
        glTranslatef(engine->cube_offset.x, engine->cube_offset.y, engine->cube_offset.z);

        color = cEngine::GetTextColor();

        glBindTexture(GL_TEXTURE_2D, engine->texture_id_fonts);
        DrawTexts(color);

        color = cEngine::GetTitleColor();
        DrawTextsTitles(color);

        glBindTexture(GL_TEXTURE_2D, engine->texture_id_numbers);

        DrawLevelNumbers();

        glBindTexture(GL_TEXTURE_2D, engine->texture_id_symbols);

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
        engine->SetProjection2D();
        SetModelViewMatrix2D();

 //        glDisable(GL_LIGHTING);
 //        glDisableClientState(GL_NORMAL_ARRAY);
 //        glDisableClientState(GL_TEXTURE_COORD_ARRAY);

        cRenderer::EnableBlending();

        engine->DrawCircleAt(m_fingermove_x, engine->m_height - m_fingermove_y, 10.0f * engine->m_scaleFactor);

        cRenderer::DisableBlending();

        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);

        engine->SetProjection3D();
    }
*/

        }

        #pragma mark - OnFinger

        void cMenu::OnFingerDown(float x, float y, int finger_count)
        {
        //render = !render;
//    printf("\ncMenu::OnFingerDown");

        if ( m_navigator.IsCurrentNavigation(NoNavigation) &&
        m_pMenuCubePlay->IsDone() &&
        m_pMenuCubeOptions->IsDone() &&
        m_pMenuCubeStore->IsDone() &&
        m_pStoreCubeRestore->IsDone() &&
        m_pStoreCubeNoAds->IsDone() )
        {
        m_fingerdown = true;

        m_pos_down.x = x;
        m_pos_down.y = y;

        RenderForPicking(RenderOnlyMovingCubes);

        m_color_down = engine->GetColorFromScreen(m_pos_down);
        cMenuCube* pMenuCube = GetMovingCubeFromColor(m_color_down.r);

        if (pMenuCube)
        {
        m_hilite_alpha = 0.0f;
        m_menu_cube_hilite = pMenuCube;
        CubePos cp = m_menu_cube_hilite->m_cube_pos;
        m_font_hilite.Init(SymbolHilite, cp);
        }
        }
        }

        void cMenu::OnFingerMove(float prev_x, float prev_y, float cur_x, float cur_y, int finger_count)
        {
        if (m_fingerdown)
        {
        //printf("\ncMenu::OnFingerMove");
        m_pos_move.x = cur_x;
        m_pos_move.y = cur_y;

        float dist = GetDistance2D(m_pos_down, m_pos_move);
        //printf("\nOnFingerMove: %.2f", dist);

        if (dist > 20.0f * engine->device_scale)
        m_swipe = true;
        }
        }

        void cMenu::OnFingerUp(float x, float y, int finger_count)
        {
        m_fingerdown = false;

        if (!m_pMenuCubePlay->IsDone() || !m_pMenuCubeOptions->IsDone() || !m_pMenuCubeStore->IsDone())
        return;

        m_menu_cube_hilite = NULL;

        m_pos_up.x = x;
        m_pos_up.y = y;

        if (InCredits == m_state || AnimToCredits == m_state || AnimFromCredits == m_state)
        {
        if (InCredits == m_state)
        {
        m_t = 0.0f;
        m_state = AnimFromCredits;
        engine->ShowGameCenterInfo();
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

        void cMenu::FingerUpOnFaceOptions()
        {
        RenderForPicking(RenderOnlyOptions);
        m_color_down = engine->GetColorFromScreen(m_pos_down);
        m_color_up = engine->GetColorFromScreen(m_pos_up);

        if (m_color_down.r == m_color_up.r)
        {
        switch (m_color_down.r)
        {
        case 255:
        engine->MusicVolumeUp();
        engine->PlaySound(SOUND_VOLUME_UP);
        break;

        case 254:
        engine->MusicVolumeDown();
        engine->PlaySound(SOUND_VOLUME_DOWN);
        break;

        case 253:
        engine->SoundVolumeUp();
        engine->PlaySound(SOUND_VOLUME_UP);
        break;

        case 252:
        engine->SoundVolumeDown();
        engine->PlaySound(SOUND_VOLUME_DOWN);
        break;

default:
        break;
        }
        }
        }

        void cMenu::FingerUpOnFacesEasy()
        {
        RenderForPicking(RenderOnlyLevelCubes);
        m_color_down = engine->GetColorFromScreen(m_pos_down);
        m_color_up = engine->GetColorFromScreen(m_pos_up);

        if (m_color_down.r == m_color_up.r)
        {
        int level_number = 255 - m_color_down.r;

        if (level_number >= 1 && level_number <= 60)
        EventPlayLevel(Easy, level_number);
        }
        }

        void cMenu::FingerUpOnFacesNormal()
        {
        RenderForPicking(RenderOnlyLevelCubes);
        m_color_down = engine->GetColorFromScreen(m_pos_down);
        m_color_up = engine->GetColorFromScreen(m_pos_up);

        if (m_color_down.r == m_color_up.r)
        {
        int level_number = 255 - m_color_down.r;

        if (level_number >= 1 && level_number <= 60)
        EventPlayLevel(Normal, level_number);
        }
        }

        void cMenu::FingerUpOnFacesHard()
        {
        RenderForPicking(RenderOnlyLevelCubes);
        m_color_down = engine->GetColorFromScreen(m_pos_down);
        m_color_up = engine->GetColorFromScreen(m_pos_up);

        if (m_color_down.r == m_color_up.r)
        {
        int level_number = 255 - m_color_down.r;

        if (level_number >= 1 && level_number <= 60)
        EventPlayLevel(Hard, level_number);
        }
        }

        void cMenu::FingerUpOnFaceMenu()
        {
        RenderForPicking(RenderOnlyMovingCubes);
        m_color_down = engine->GetColorFromScreen(m_pos_down);
        m_color_up = engine->GetColorFromScreen(m_pos_up);

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

        void cMenu::HandleSwipe()
        {
        SwipeDirEnums swipeDir;
        float length;
        engine->GetSwipeDirAndLength(m_pos_down, m_pos_up, swipeDir, length);

        if ( length > (30.0f * engine->m_scaleFactor) )
        {
        RenderForPicking(RenderOnlyMovingCubes);
        Color down_color = engine->GetColorFromScreen(m_pos_down);
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
        pMenuCube->MoveOnAxis(X_Minus);
        break;

        case Face_Normal01:
        case Face_Normal02:
        case Face_Normal03:
        case Face_Normal04:
        pMenuCube->MoveOnAxis(X_Plus);
        break;

        case Face_Hard01:
        case Face_Hard02:
        case Face_Hard03:
        case Face_Hard04:
        case Face_Tutorial:
        pMenuCube->MoveOnAxis(Z_Minus);
        break;

        case Face_Easy01:
        case Face_Easy02:
        case Face_Easy03:
        case Face_Easy04:
        pMenuCube->MoveOnAxis(Z_Plus);
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
        pMenuCube->MoveOnAxis(X_Plus);

        if (Face_Menu == m_current_cube_face)
        {
        if (pMenuCube != m_pMenuCubeOptions)
        {
        if (7 == m_pMenuCubeOptions->m_cube_pos.x)
        {
        m_pMenuCubeOptions->MoveOnAxis(X_Minus);
        }
        }
        }
        break;

        case Face_Normal01:
        case Face_Normal02:
        case Face_Normal03:
        case Face_Normal04:
        pMenuCube->MoveOnAxis(X_Minus);
        break;

        case Face_Hard01:
        case Face_Hard02:
        case Face_Hard03:
        case Face_Hard04:
        case Face_Tutorial:
        pMenuCube->MoveOnAxis(Z_Plus);
        break;

        case Face_Easy01:
        case Face_Easy02:
        case Face_Easy03:
        case Face_Easy04:
        pMenuCube->MoveOnAxis(Z_Minus);
        break;

default:
        break;
        }
        break;

        case SwipeUp:

        switch (m_current_cube_face)
        {
        case Face_Store:
        pMenuCube->MoveOnAxis(Z_Plus);
        break;

        case Face_Options:
        pMenuCube->MoveOnAxis(Z_Minus);
        break;

        case Face_Easy02:
        pMenuCube->MoveOnAxis(X_Plus);
        break;

        case Face_Easy03:
        pMenuCube->MoveOnAxis(Y_Minus);
        break;

        case Face_Easy04:
        pMenuCube->MoveOnAxis(X_Minus);
        break;

        case Face_Normal02:
        pMenuCube->MoveOnAxis(Z_Minus);
        break;

        case Face_Normal03:
        pMenuCube->MoveOnAxis(Y_Minus);
        break;

        case Face_Normal04:
        pMenuCube->MoveOnAxis(Z_Plus);
        break;

        case Face_Hard02:
        pMenuCube->MoveOnAxis(X_Minus);
        break;

        case Face_Hard03:
        pMenuCube->MoveOnAxis(Y_Minus);
        break;

        case Face_Hard04:
        pMenuCube->MoveOnAxis(X_Plus);
        break;

default:
        pMenuCube->MoveOnAxis(Y_Plus);
        break;
        }
        break;

        case SwipeDown:

        switch (m_current_cube_face)
        {
        case Face_Store:
        pMenuCube->MoveOnAxis(Z_Minus);
        break;

        case Face_Options:
        pMenuCube->MoveOnAxis(Z_Plus);
        break;

        case Face_Easy02:
        pMenuCube->MoveOnAxis(X_Minus);
        break;

        case Face_Easy03:
        pMenuCube->MoveOnAxis(Y_Plus);
        break;

        case Face_Easy04:
        pMenuCube->MoveOnAxis(X_Plus);
        break;

        case Face_Normal02:
        pMenuCube->MoveOnAxis(Z_Plus);
        break;

        case Face_Normal03:
        pMenuCube->MoveOnAxis(Y_Plus);
        break;

        case Face_Normal04:
        pMenuCube->MoveOnAxis(Z_Minus);
        break;

        case Face_Hard02:
        pMenuCube->MoveOnAxis(X_Plus);
        break;

        case Face_Hard03:
        pMenuCube->MoveOnAxis(Y_Plus);
        break;

        case Face_Hard04:
        pMenuCube->MoveOnAxis(X_Minus);
        break;

default:
        pMenuCube->MoveOnAxis(Y_Minus);
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

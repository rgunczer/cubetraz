package com.almagems.cubetraz;


public class Outro {


    enum OutroStateEnum
    {
        AnimToOutro,
        RotateFull,
        OutroExplosion,
        OutroDone
    };



    class cOutro : public cScene
    {

        private:

        int m_cube_alpha;

        cTextDisplay m_text_display;
        cText m_ar_text_center[2];

        OutroStateEnum m_state;

        cCamera m_camera_menu;
        cCamera m_camera_outro;
        cCamera m_camera_current;

        cStarfield m_starfield;

        float m_stars_alpha;
        float m_t;
        float m_degree;

        vec3 m_pos_light_outro;
        vec3 m_pos_light_menu;
        vec3 m_pos_light_current;

        vec3 m_pos_cube_player;

        bool m_draw_starfield;
        float m_cube_rot_speed;

        CubeRotation m_cube_rotation;

        list<cCube*> m_lst_cubes_base;
        list<cCube*> m_lst_cubes_level;
        list<cCube*> m_lst_cubes_base_appear;
        list<cCube*> m_lst_cubes_base_disappear;

        float m_center_alpha;

    void SetupExplosion();

    void DrawTheCube();
    void DrawText();

    void UpdateInAnimTo(float dt);
    void UpdateInRotateFull(float dt);
    void UpdateInOutroExplosion(float dt);
    void UpdateInOutroDone(float dt);

    public:

    cOutro();
    virtual ~cOutro();

    virtual void Init(void* init_data = NULL);
    virtual void Update(float dt);
    virtual void Render();

    virtual void OnFingerDown(float x, float y, int finger_count) {}
    virtual void OnFingerUp(float x, float y, int finger_count);
    virtual void OnFingerMove(float prev_x, float prev_y, float cur_x, float cur_y, int finger_count) {}


    cOutro::cOutro()
    {
        m_camera_outro.eye = vec3(0.0f, 10.0f, 30.0f);
        m_camera_outro.target = vec3(0.0f, 0.0f, 0.0f);

        m_ar_text_center[0].SetDisplay(&m_text_display);
        m_ar_text_center[1].SetDisplay(&m_text_display);

        m_ar_text_center[0].SetScale(1.0f, 1.0f);
        m_ar_text_center[1].SetScale(1.0f, 1.0f);

        m_ar_text_center[0].SetVSPace(0.8f);
        m_ar_text_center[1].SetVSPace(0.8f);
    }

    cOutro::~cOutro()
    {
        m_lst_cubes_base.clear();
        m_lst_cubes_level.clear();
        m_lst_cubes_base_appear.clear();
        m_lst_cubes_base_disappear.clear();
    }

    #pragma mark - Init

    void cOutro::Init(void* init_data)
    {
        m_ar_text_center[0].Init("CONGRATULATIONS", true);
        m_ar_text_center[1].Init("CUBETRAZ IS SOLVED", true);

        m_center_alpha = 0.0f;
        m_cube_alpha = 255;

        m_starfield.speed = 0.2f;
        m_starfield.Create();

        for (int i = 0; i < 30*10; ++i)
            m_starfield.Update(1.0f / 30.0f);

        m_camera_outro = engine->m_level->m_camera_level;
        m_camera_menu = engine->m_menu->GetCamera();
        m_camera_current = m_camera_outro;

        m_pos_light_outro = engine->m_level->m_pos_light;
        m_pos_light_menu = engine->m_menu->GetLightPositon();
        m_pos_light_current = m_pos_light_outro;

        m_cube_rotation.degree = -45.0f;
        m_cube_rotation.axis = vec3(0.0f, 1.0f, 0.0f);

        m_draw_starfield = false;
        m_stars_alpha = 0.0f;
        m_starfield.alpha = m_stars_alpha * 255;

        m_cube_rot_speed = 0.1f;

        m_state = AnimToOutro;

        m_t = 0.0f;

        m_lst_cubes_base.clear();
        m_lst_cubes_level.clear();

        list<cCube*>::iterator it;

        for (it = engine->m_level->m_list_cubes_level.begin(); it != engine->m_level->m_list_cubes_level.end(); ++it)
            m_lst_cubes_level.push_back(*it);

        for (it = engine->m_level->m_list_cubes_wall_y_minus.begin(); it != engine->m_level->m_list_cubes_wall_y_minus.end(); ++it)
            m_lst_cubes_base.push_back(*it);

        for (it = engine->m_level->m_list_cubes_wall_x_minus.begin(); it != engine->m_level->m_list_cubes_wall_x_minus.end(); ++it)
            m_lst_cubes_base.push_back(*it);

        for (it = engine->m_level->m_list_cubes_wall_z_minus.begin(); it != engine->m_level->m_list_cubes_wall_z_minus.end(); ++it)
            m_lst_cubes_base.push_back(*it);

        for (it = engine->m_level->m_list_cubes_edges.begin(); it != engine->m_level->m_list_cubes_edges.end(); ++it)
            m_lst_cubes_base.push_back(*it);


        m_lst_cubes_base_disappear.clear();

        cCube* pCube;
        for (it = m_lst_cubes_base.begin(); it != m_lst_cubes_base.end(); ++it)
        {
            pCube = *it;

            if (pCube)
            {
                if (8 == pCube->x || 8 == pCube->z)
                    m_lst_cubes_base_disappear.push_back(pCube);
            }
        }

        m_lst_cubes_base_appear.clear();

        list<cCube*> lst;
        engine->CreateBaseCubesList(lst);

        for (it = lst.begin(); it != lst.end(); ++it)
        {
            if ( !engine->IsOnAList(*it, m_lst_cubes_base) )
            {
                (*it)->SetColor(Color(255, 255, 255, 255));
                m_lst_cubes_base_appear.push_back(*it);
            }
        }

        m_pos_cube_player = engine->GetCubePosAt(engine->m_level->m_player_cube->GetCubePos());

        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        glEnable(GL_BLEND);

        const vec4 lightPosition(m_pos_light_current.x, m_pos_light_current.y, m_pos_light_current.z, 1.0f);
        glLightfv(GL_LIGHT0, GL_POSITION, lightPosition.Pointer());
    }

    void cOutro::SetupExplosion()
    {
        cCube* pCube;
        list<cCube*>::iterator it;

        for (it = m_lst_cubes_base.begin(); it != m_lst_cubes_base.end(); ++it)
        {
            pCube = *it;

            pCube->v = vec3((-60 + rand()%120), (-60 + rand()%120), (-60 + rand()%120));

            if ( pCube->v.x > 0.0f && pCube->v.x <  30.0f ) pCube->v.x = RANDOM_FLOAT(30.0f, 50.0f);
            if ( pCube->v.x < 0.0f && pCube->v.x > -30.0f ) pCube->v.x = RANDOM_FLOAT(30.0f, 50.0f) * -1.0f;

            if ( pCube->v.y > 0.0f && pCube->v.y <  30.0f ) pCube->v.y = RANDOM_FLOAT(30.0f, 50.0f);
            if ( pCube->v.y < 0.0f && pCube->v.y > -30.0f ) pCube->v.y = RANDOM_FLOAT(30.0f, 50.0f) * -1.0f;

            if ( pCube->v.z > 0.0f && pCube->v.z <  30.0f ) pCube->v.z = RANDOM_FLOAT(30.0f, 50.0f);
            if ( pCube->v.z < 0.0f && pCube->v.z > -30.0f ) pCube->v.z = RANDOM_FLOAT(30.0f, 50.0f) * -1.0f;

            pCube->v.x *= 0.001f;
            pCube->v.y *= 0.001f;
            pCube->v.z *= 0.001f;
        }
    }

    #pragma mark - Update

    void cOutro::Update(float dt)
    {
        switch (m_state)
        {
            case AnimToOutro:
                UpdateInAnimTo(dt);
                break;

            case RotateFull:

                engine->IncT(m_center_alpha);

                --engine->dirty_alpha;

                if (engine->dirty_alpha < 0)
                    engine->dirty_alpha = 0;

                UpdateInRotateFull(dt);
                break;

            case OutroExplosion:
                UpdateInOutroExplosion(dt);
                break;

            case OutroDone:
                engine->DecT(m_center_alpha);
                UpdateInOutroDone(dt);
                break;
        }
    }

    void cOutro::UpdateInAnimTo(float dt)
    {
        bool done = true;

        if (!m_lst_cubes_base_appear.empty())
        {
            done = false;
            cCube* pCube = m_lst_cubes_base_appear.front();
            m_lst_cubes_base_appear.pop_front();

            m_lst_cubes_base.push_back(pCube);
        }

        if (!m_lst_cubes_base_disappear.empty())
        {
            done = false;
            cCube* pCube = m_lst_cubes_base_disappear.front();
            m_lst_cubes_base_disappear.pop_front();

            m_lst_cubes_base.remove(pCube);
        }

        if (!m_lst_cubes_level.empty())
        {
            done = false;
            m_lst_cubes_level.pop_back();
        }

        if (m_cube_rot_speed < 4.0f && m_cube_rot_speed > 0.0f)
        {
            done = false;
            m_cube_rotation.degree -= m_cube_rot_speed;
            m_cube_rot_speed += 0.01f;
        }

        if (m_lst_cubes_base_appear.empty())
        {
            m_t += 0.01f;
            if (m_t > 1.0f) m_t = 1.0f;
            cUtils::LerpCamera(m_camera_outro, m_camera_menu, m_t, m_camera_current);
            cUtils::LerpVec3(m_pos_light_outro, m_pos_light_menu, m_t, m_pos_light_current);
        }

        if (done && fabs(1.0f - m_t) < EPSILON)
        {
            m_state = RotateFull;
            m_draw_starfield = true;
            m_stars_alpha = 0.0f;
            engine->StopMusic();
        }
    }

    void cOutro::UpdateInRotateFull(float dt)
    {
        m_cube_rotation.degree -= m_cube_rot_speed;

        m_stars_alpha += 0.02f;

        if (m_stars_alpha > 1.0f)
        {
            m_state = OutroExplosion;

            m_stars_alpha = 1.0f;

            m_pos_cube_player = engine->GetCubePosAt(4, 4, 4);

            m_degree = 0.0f;

            SetupExplosion();
            engine->PlayMusic(MUSIC_VECTORS);
        }

        m_starfield.alpha = m_stars_alpha * 255;
        m_starfield.Update(dt);
    }

    void cOutro::UpdateInOutroExplosion(float dt)
    {
        m_starfield.speed += 0.0003f;
        m_degree += 1.0f;

        list<cCube*>::iterator it;
        for (it = m_lst_cubes_base.begin(); it != m_lst_cubes_base.end(); ++it)
        {
            (*it)->Update(dt);
//        if (m_degree > 25.0f)
//            (*it)->v = vec3(0.0f, 0.0f, 0.0f);
        }

        if ( int(m_cube_rotation.degree) % 360 != 0)
        {
            m_cube_rotation.degree += 0.5f;
        }
        else
        {
            m_cube_rotation.degree = 0.0f;
            m_state = OutroDone;
        }

        m_starfield.alpha = m_stars_alpha * 255;
        m_starfield.Update(dt);
    }

    void cOutro::UpdateInOutroDone(float dt)
    {
        m_starfield.speed += 0.0003f;
        m_degree += 1.25f;
        m_cube_alpha -= 2;

        if (m_cube_alpha < 0)
            m_cube_alpha = 0;

        cCube* pCube;
        list<cCube*>::iterator it;
        for (it = m_lst_cubes_base.begin(); it != m_lst_cubes_base.end(); ++it)
        {
            pCube = *it;
            pCube->color_current.a = m_cube_alpha;
            pCube->Update(dt);
        }

        m_starfield.alpha = m_stars_alpha * 255;
        m_starfield.Update(dt);

        if (m_starfield.speed > 1.2f)
            engine->ShowScene(Scene_Menu);
    }

    #pragma mark - Draw

    void cOutro::DrawTheCube()
    {
        cRenderer::Prepare();
        cRenderer::SetStreamSource();

        list<cCube*>::iterator it;
        for (it = m_lst_cubes_base.begin(); it != m_lst_cubes_base.end(); ++it)
            cRenderer::AddCubeSize((*it)->tx, (*it)->ty, (*it)->tz, HALF_CUBE_SIZE, (*it)->color_current);

        for (it = m_lst_cubes_level.begin(); it != m_lst_cubes_level.end(); ++it)
            cRenderer::AddCubeSize((*it)->tx, (*it)->ty, (*it)->tz, HALF_CUBE_SIZE, (*it)->color_current);

        glEnable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, engine->texture_id_gray_concrete);
        cRenderer::RenderTriangles(engine->cube_offset.x, engine->cube_offset.y, engine->cube_offset.z);
    }

    void cOutro::DrawText()
    {
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_LIGHTING);
        glDisable(GL_CULL_FACE);
        glDisable(GL_DEPTH_TEST);

        engine->SetProjection2D();
        engine->SetModelViewMatrix2D();

        int a = m_center_alpha * 255;

        glDisable(GL_TEXTURE_2D);
        Color color_bg(30, 30, 15, 150 * m_center_alpha);

        cRenderer::SetStreamSourceFloat2DNoTexture();
        cRenderer::Prepare();
        cRenderer::AddQuad(0.0f, engine->m_half_height - 20.0f * engine->device_scale, engine->m_width, 75.0f * engine->device_scale, color_bg);
        cRenderer::RenderTriangles();

        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, engine->texture_id_fonts_big);

        float scale = 0.75f * engine->device_scale;
        m_ar_text_center[0].SetScale(scale, scale);
        m_ar_text_center[1].SetScale(scale, scale);

        //printf("\na: %d", a);

        Color color(0, 0, 0, a);

        m_text_display.Init();
        m_ar_text_center[0].Emitt(engine->m_half_width - m_ar_text_center[0].GetHalfWidth(), engine->m_half_height + m_ar_text_center[0].GetHalfHeight(), color);
        m_text_display.m_vertex_count += m_ar_text_center[0].GetVertexCount();

        m_ar_text_center[1].Emitt(engine->m_half_width - m_ar_text_center[1].GetHalfWidth(), engine->m_half_height - m_ar_text_center[1].GetHalfHeight(), color);
        m_text_display.m_vertex_count += m_ar_text_center[1].GetVertexCount();

        glPushMatrix();
        glTranslatef(engine->device_scale, engine->device_scale, 0.0f);
        m_text_display.Render();
        glPopMatrix();

        color = Color(255, 255, 0, a);

        m_text_display.Init();
        m_ar_text_center[0].Emitt(engine->m_half_width - m_ar_text_center[0].GetHalfWidth(), engine->m_half_height + m_ar_text_center[0].GetHalfHeight(), color);
        m_text_display.m_vertex_count += m_ar_text_center[0].GetVertexCount();

        m_ar_text_center[1].Emitt(engine->m_half_width - m_ar_text_center[1].GetHalfWidth(), engine->m_half_height - m_ar_text_center[1].GetHalfHeight(), color);
        m_text_display.m_vertex_count += m_ar_text_center[1].GetVertexCount();

        m_text_display.Render();

        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);

    }

    #pragma mark - Render

    void cOutro::Render()
    {
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);

        if (engine->dirty_alpha > 0)
        {
            engine->SetProjection2D();
            engine->SetModelViewMatrix2D();

            glDisable(GL_LIGHTING);
            glDepthMask(GL_FALSE);
            glEnable(GL_TEXTURE_2D);

            Color color_dirty(255, 255, 255, engine->dirty_alpha);
            engine->DrawFBOTexture(engine->texture_id_dirty, color_dirty);

            glDepthMask(GL_TRUE);
            glEnable(GL_LIGHTING);
        }

        cRenderer::SetStreamSource();

        engine->SetProjection3D();
        engine->SetModelViewMatrix3D(m_camera_current);

        const vec4 lightPosition(m_pos_light_current.x, m_pos_light_current.y, m_pos_light_current.z, 1.0f);
        glLightfv(GL_LIGHT0, GL_POSITION, lightPosition.Pointer());

        if (m_draw_starfield)
        {
            glDisable(GL_TEXTURE_2D);
            glDisable(GL_LIGHTING);

            glDisableClientState(GL_NORMAL_ARRAY);
            glDisableClientState(GL_TEXTURE_COORD_ARRAY);

            glDepthMask(GL_FALSE);

            glEnable(GL_POINT_SMOOTH);
            m_starfield.Render();
            glDisable(GL_POINT_SMOOTH);

            glEnable(GL_LIGHTING);
            glDepthMask(GL_TRUE);
            glEnable(GL_TEXTURE_2D);

            glEnableClientState(GL_NORMAL_ARRAY);
            glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        }

        glBindTexture(GL_TEXTURE_2D, engine->texture_id_player);

        if (OutroExplosion == m_state || OutroDone == m_state)
        {
            glPushMatrix();

            glRotatef(m_degree, 1.0f, 0.0f, 0.0f);
            glRotatef(m_degree, 0.0f, 1.0f, 0.0f);
            glRotatef(m_degree, 0.0f, 0.0f, 1.0f);

            cRenderer::Prepare();
            cRenderer::AddCube(m_pos_cube_player.x, m_pos_cube_player.y, m_pos_cube_player.z);
            cRenderer::RenderTriangles(engine->cube_offset.x, engine->cube_offset.y, engine->cube_offset.z);

            glPopMatrix();
        }
        else
        {
            glPushMatrix();
            glRotatef(m_cube_rotation.degree, m_cube_rotation.axis.x, m_cube_rotation.axis.y, m_cube_rotation.axis.z);

            cRenderer::Prepare();
            cRenderer::AddCube(m_pos_cube_player.x, m_pos_cube_player.y, m_pos_cube_player.z);
            cRenderer::RenderTriangles(engine->cube_offset.x, engine->cube_offset.y, engine->cube_offset.z);

            glPopMatrix();
        }

        if (m_cube_alpha > 0)
        {
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

            glPopMatrix();
        }

        DrawText();

    }

    #pragma mark - FingerUp

    void cOutro::OnFingerUp(float x, float y, int finger_count)
    {
        if (OutroDone == m_state)
            engine->ShowScene(Scene_Menu);
    }


}

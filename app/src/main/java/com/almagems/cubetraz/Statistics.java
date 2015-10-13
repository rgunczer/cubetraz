package com.almagems.cubetraz;

public class Statistics {


    enum StatisticsStateEnum
    {
        StatNone,
        StatAppear,
        StatAppearTitle,
        StatAppearMovesPlayer,
        StatAppearMovesBest,
        StatShow,
        StatWait,
    };


    struct FallingCube
    {
        float degree;
        vec3 pos;
        float speed;
    };


    struct StatInitData
    {
        char str_title[32];
        char str_moves[64];
        int stars;

        StatInitData()
        {
            strcpy(str_title, "EXCELLENT");
            strcpy(str_moves, "PLAYER:42 > BEST:16");
            stars = 3;
        }
    };

    class cStatistics : public cScene
    {

        private:

        bool m_falling_cubes;

        float m_timeout;

        StatisticsStateEnum m_state;
        StatisticsStateEnum m_state_next;

        cTextDisplay m_text_display;

        cCamera m_camera;

        int m_vertex_count1;
        int m_vindex1;
        int m_cindex1;

        int m_star_count;
        float m_star_alpha;

        float zoom;
        float trans;

        bool m_UpdateBackground;
        bool m_DrawTitle;
        bool m_DrawMoves;
        bool m_DrawStars;
        bool m_DrawTap;

        float m_rating_y;
        float m_title_y;
        float m_moves_y;
        float m_stars_y;
        float m_tap_y;

        float m_background_alpha;
        float m_background_rot_degree;
        float m_background_alpha_pulse;

        float m_text_scale;
        float m_text_scales[3]; // title, time, moves

        cText m_text_title;
        cText m_text_middle;
        cText m_text_moves;
        cText m_text_tap;

        list<FallingCube*> m_lst_falling_cubes;

        float m_pulse;
        bool m_can_dismiss;

    void DrawFallingCubes();
    void DrawTheCubeTexture();
    void DrawText(Color& color);
    void DrawStars();
    void DrawBackground();

    void InitFallingCubes();

    void Setup(StatInitData& sid);

    void UpdateFallingCubes(float dt);

    bool CanDismiss() { return m_can_dismiss; }

    public:

    cStatistics();
    virtual ~cStatistics();

    virtual void Init(void* init_data = NULL);
    virtual void Update(float dt);
    virtual void Render();

    virtual void OnFingerDown(float x, float y, int finger_count) {}
    virtual void OnFingerUp(float x, float y, int finger_count);
    virtual void OnFingerMove(float prev_x, float prev_y, float cur_x, float cur_y, int finger_count) {}




    cStatistics::cStatistics()
    {
        m_camera.eye = vec3(0.0f, 10.0f, 30.0f);
        m_camera.target = vec3(0.0f, 0.0f, 0.0f);

        for (int i = 0; i < 10; ++i)
            m_lst_falling_cubes.push_back(new FallingCube());
    }

    cStatistics::~cStatistics()
    {
        list<FallingCube*>::iterator it;
        for (it = m_lst_falling_cubes.begin(); it != m_lst_falling_cubes.end(); ++it)
            delete *it;

        m_lst_falling_cubes.clear();
    }

    #pragma mark - Init

    void cStatistics::InitFallingCubes()
    {
        m_falling_cubes = true;

        int i = 1;
        float x = -7.0f;

        FallingCube* fc;
        list<FallingCube*>::iterator it;

        for (it = m_lst_falling_cubes.begin(); it != m_lst_falling_cubes.end(); ++it)
        {
            fc = *it;

            fc->degree = RANDOM_FLOAT(0.0, 180.0f);
            fc->pos = vec3(x + i*1.25f, RANDOM_FLOAT(9.0f, 1.0f), 0.0f);
            fc->speed = RANDOM_FLOAT(0.03f, 0.05f);

            ++i;
        }

        glDisable(GL_DEPTH_TEST);
    }

    void cStatistics::Init(void* init_data)
    {
        m_can_dismiss = false;

        m_text_display.Init();

        m_vertex_count1 = 0;
        m_vindex1 = -1;
        m_cindex1 = -1;

        m_star_alpha = 0.0f;
        zoom = 0.0f;
        trans = 0.0f;
        m_pulse = 0.0f;

        m_state = StatAppear;
        m_state_next = StatNone;
        m_timeout = 0.0f;
        m_background_alpha = 0.0f;
        m_background_rot_degree = 0.0f;
        m_background_alpha_pulse = 0.0f;

        m_UpdateBackground = false;
        m_DrawTitle = false;
        m_DrawMoves = false;
        m_DrawStars = false;
        m_DrawTap = false;

        m_text_scales[0] =  0.0f;   // title
        m_text_scales[1] =  0.0f;   // time
        m_text_scales[2] =  0.0f;   // moves

        InitFallingCubes();

        m_text_title.SetDisplay(&m_text_display);
        m_text_middle.SetDisplay(&m_text_display);
        m_text_moves.SetDisplay(&m_text_display);
        m_text_tap.SetDisplay(&m_text_display);

        m_text_title.SetUseBigFonts(true);
        m_text_middle.SetUseBigFonts(true);
        m_text_moves.SetUseBigFonts(true);
        m_text_tap.SetUseBigFonts(true);

        m_text_title.SetVSPace(1.0f);
        m_text_middle.SetVSPace(0.8f);
        m_text_moves.SetVSPace(0.8f);
        m_text_tap.SetVSPace(0.7f);

        Setup(engine->stat_init_data);
    }

    #pragma mark - Setup

    void cStatistics::Setup(StatInitData& sid)
    {
        m_star_count = sid.stars;

        // texts
        m_text_title.SetVSPace(1.0f);

        m_text_title.Init(sid.str_title);
        m_text_middle.Init("MOVES");
        m_text_moves.Init(sid.str_moves);
        m_text_tap.Init("TAP TO CONTINUE");

        // pre calc y positions
        m_stars_y = engine->m_height * 0.75f;
        m_rating_y = engine->m_height * 0.5f;

        m_title_y = engine->m_height * 0.33f;
        m_moves_y = engine->m_height * 0.2f;
        m_tap_y = engine->m_height * 0.017f;
    }

    #pragma mark - Update

    void cStatistics::UpdateFallingCubes(float dt)
    {
        FallingCube* fc;
        list<FallingCube*>::iterator it;

        for (it = m_lst_falling_cubes.begin(); it != m_lst_falling_cubes.end(); ++it)
        {
            fc = *it;

            fc->pos.y -= fc->speed;

            if (fc->pos.y < -4.0f)
            {
                fc->pos.y = RANDOM_FLOAT(9.0f, 1.0f);
                fc->speed = RANDOM_FLOAT(0.03f, 0.05f);
            }

            fc->degree += 1.0f;
        }
    }

    void cStatistics::Update(float dt)
    {
        UpdateFallingCubes(dt);

        switch (m_state)
        {
            case StatWait:

                if (StatNone != m_state_next)
                {
                    m_pulse += 0.3f;
                    m_text_scale = (sinf(m_pulse) / 5.0f) * engine->device_scale;

                    //printf("\nPulse: %f, sinf(m_pulse): %f, text_scale: %f", m_pulse, sinf(m_pulse), m_text_scale);

                    switch (m_state_next)
                    {
                        case StatAppearTitle:
                            m_text_scales[0] = m_text_scale;
                            m_DrawStars = true;
                            break;

                        case StatAppearMovesPlayer:
                            m_text_scales[1] = m_text_scale;
                            break;

                        case StatAppearMovesBest:
                            m_text_scales[2] = m_text_scale;
                            break;

                        default:
                            break;
                    }

                    if (m_text_scale < EPSILON)
                    {
                        m_state = m_state_next;
                        m_pulse = 0.0f;
                        m_text_scale = 0.0f;

                        switch (m_state_next)
                        {
                            case StatAppearTitle:
                                m_text_scales[0] = m_text_scale;
                                break;

                            case StatAppearMovesPlayer:
                                m_text_scales[1] = m_text_scale;
                                break;

                            case StatAppearMovesBest:
                                m_text_scales[2] = m_text_scale;
                                engine->ShowFullScreenAd();
                                break;

                            case StatShow:
                                m_can_dismiss = true;
                                m_DrawTap = true;
                                break;

                            default:
                                break;
                        }
                    }
                }

                break;

            case StatAppear:

                m_background_alpha += 0.025f;

                if (m_background_alpha > 0.5f)
                {
                    m_background_alpha = 0.5f;
                    m_state_next = StatAppearTitle;
                    m_state = StatWait;
                    m_timeout = 1.0f;
                    m_DrawTitle = true;
                    m_UpdateBackground = true;
                }

                break;

            case StatAppearTitle:
                m_state_next = StatAppearMovesPlayer;
                m_state = StatWait;
                m_timeout = 0.75f;
                m_text_scale = 0.0f;
                m_pulse = 0.0f;
                break;

            case StatAppearMovesPlayer:
                m_state_next = StatAppearMovesBest;
                m_state = StatWait;
                m_timeout = 0.75f;
                m_text_scale = 0.0f;
                m_pulse = 0.0f;
                m_DrawMoves = true;
                break;

            case StatAppearMovesBest:
                m_state_next = StatShow;
                m_state = StatWait;
                m_timeout = 0.75f;
                m_text_scale = 0.0f;
                m_pulse = 0.0f;
                trans = 0.0f;
                zoom = 0.0f;
                break;

            case StatShow:
                trans += 0.175f;
                zoom = (GLfloat)(sinf(trans)*12.0f);
                break;

            default:
                break;
        } // switch

        if (m_DrawStars)
        {
            m_star_alpha += 0.009f;

            if (m_star_alpha > 1.0f)
                m_star_alpha = 1.0f;
        }

        if (m_UpdateBackground)
        {
            m_background_rot_degree += 0.125f;
            m_background_alpha = 0.5f + (sinf(m_background_alpha_pulse) / 10.0f);
            m_background_alpha_pulse += 0.05f;
        }
    }

    #pragma mark - Draw

    void cStatistics::DrawFallingCubes()
    {
        engine->SetProjection3D();
        engine->SetModelViewMatrix3D(m_camera);
        const vec4 lightPosition(-100.0f, 300.0f, 900.0f, 1.0f);
        glLightfv(GL_LIGHT0, GL_POSITION, lightPosition.Pointer());

        glNormalPointer(GL_FLOAT, 0, cEngine::normals);
        glVertexPointer(3, GL_FLOAT, 0, cEngine::vertices);
        glTexCoordPointer(2, GL_SHORT, 0, cEngine::texture_coordinates);
        glColorPointer(4, GL_UNSIGNED_BYTE, 0, cEngine::colors);

        glPushMatrix();
        glTranslatef(0.0f, 0.0f, 10.0f);

        FallingCube* fc;
        list<FallingCube*>::iterator it;

        for (it = m_lst_falling_cubes.begin(); it != m_lst_falling_cubes.end(); ++it)
        {
            fc = *it;

            glPushMatrix();
            glTranslatef(fc->pos.x, fc->pos.y, fc->pos.z);

            glRotatef(fc->degree, 1.0f, 0.0f, 0.0f);
            glRotatef(fc->degree, 0.0f, 1.0f, 0.0f);
            glRotatef(fc->degree, 0.0f, 0.0f, 1.0f);

            glScalef(0.6f, 0.6f, 0.6f);
            glDrawArrays(GL_TRIANGLES, 0, 36);
            glPopMatrix();
        }
        glPopMatrix();
    }

    void cStatistics::DrawBackground()
    {
        float dim = max(engine->m_width, engine->m_height) * 1.25f;
        float q = dim / 2.0f;

        GLfloat vertices[] =
                {
                        -q, -q,
                        q, -q,
                        q,  q,
                        -q,  q
                };

        const GLshort coords[] =
            {
                    0, 1,
                    1, 1,
                    1, 0,
                    0, 0
            };

        const GLubyte color = 100;

        const GLubyte colors[] =
            {
                    color, color, color, m_background_alpha * 255,
                    color, color, color, m_background_alpha * 255,
                    color, color, color, m_background_alpha * 255,
                    color, color, color, m_background_alpha * 255
            };

        glVertexPointer(2, GL_FLOAT, 0, vertices);
        glTexCoordPointer(2, GL_SHORT, 0, coords);
        glColorPointer(4, GL_UNSIGNED_BYTE, 0, colors);

        glPushMatrix();
        glTranslatef(engine->m_half_width, engine->m_half_height, 0.0f);
        glRotatef(m_background_rot_degree, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
        glPopMatrix();
    }

    void cStatistics::DrawText(Color& color)
    {
        float x;
        float y;
        float scale;

        m_text_display.Init();

        if (m_DrawTitle)
        {
            scale = engine->device_scale + m_text_scales[0];
            m_text_title.SetScale(scale + (0.3f * engine->device_scale), scale + (0.6f * engine->device_scale));

            x = engine->m_half_width - m_text_title.GetHalfWidth();
            y = m_rating_y - m_text_title.GetHalfHeight();

            m_text_title.Emitt(x, y, color);

            m_text_display.m_vertex_count += m_text_title.GetVertexCount();
        }

        if (m_DrawMoves)
        {
            // middle
            scale = engine->device_scale * 0.9f;
            m_text_middle.SetScale(scale, scale + (0.2f * engine->device_scale));

            x = engine->m_half_width - m_text_middle.GetHalfWidth();
            y = m_title_y - m_text_middle.GetHalfHeight();

            m_text_middle.Emitt(x, y, color);
            m_text_display.m_vertex_count += m_text_middle.GetVertexCount();

            // moves
            scale = engine->device_scale + m_text_scales[2] - (0.25f * engine->device_scale);
            m_text_moves.SetScale(scale + (0.1f * engine->device_scale), scale + (0.1f * engine->device_scale));

            x = engine->m_half_width - m_text_moves.GetHalfWidth();
            y = m_moves_y - m_text_moves.GetHalfHeight();

            m_text_moves.Emitt(x, y, color);
            m_text_display.m_vertex_count += m_text_moves.GetVertexCount();
        }

        if (m_DrawTap)
        {
            scale = engine->device_scale * 0.25f;
            m_text_tap.SetScale (scale + (0.3f * engine->device_scale), scale + (0.3f * engine->device_scale));

            x = engine->m_half_width - m_text_tap.GetHalfWidth();
            y = m_tap_y;

            m_text_tap.Emitt(x, y, color);

            m_text_display.m_vertex_count += m_text_tap.GetVertexCount();
        }
    }

    void cStatistics::DrawStars()
    {
        //return;

        const GLfloat vertices[] =
            {
                    -0.5f, -0.5f,
                    0.5f, -0.5f,
                    0.5f,  0.5f,
                    -0.5f,  0.5f
            };

        const GLshort coords[] =
            {
                    0, 1,
                    1, 1,
                    1, 0,
                    0, 0
            };

        int a = m_star_alpha * 255;

        Color color_text(220, 20, 60, 240);

        const GLubyte colors_red[] =
            {
                    color_text.r, color_text.g, color_text.b, a,
                    color_text.r, color_text.g, color_text.b, a,
                    color_text.r, color_text.g, color_text.b, a,
                    color_text.r, color_text.g, color_text.b, a
            };

        const GLubyte colors_yellow[] =
            {
                    255, 255, 0, a,
                    255, 255, 0, a,
                    255, 255, 0, a,
                    255, 255, 0, a
            };

        glVertexPointer(2, GL_FLOAT, 0, vertices);
        glTexCoordPointer(2, GL_SHORT, 0, coords);
        glColorPointer(4, GL_UNSIGNED_BYTE, 0, colors_red);

        float s = engine->m_width * 0.14f;
        float w = engine->m_width * 0.2f;
        float half_w = w / 2.0f;
        float x = engine->m_half_width - ((w * m_star_count) / 2.0f);
        float y = m_stars_y;
        float raise = 3.0f * engine->device_scale;

        for (int i = 0; i < m_star_count; ++i)
        {
            glPushMatrix();
            glTranslatef(x + (i * w) + half_w, y, 0.0f);
            glRotatef(zoom, 0.0f, 0.0f, 1.0f);
            glScalef(s + zoom, s + zoom, 1.0f);
            glScalef(1.2f, 1.2f, 1.0f);
            glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
            glPopMatrix();
            y += raise;
        }

        y = m_stars_y;

        glColorPointer(4, GL_UNSIGNED_BYTE, 0, colors_yellow);

        for (int i = 0; i < m_star_count; ++i)
        {
            glPushMatrix();
            glTranslatef(x + (i * w) + half_w, y, 0.0f);
            glRotatef(zoom, 0.0f, 0.0f, 1.0f);
            glScalef(s + zoom, s + zoom, 1.0f);
            glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
            glPopMatrix();
            y += raise;
        }
    }

    #pragma mark - Render

    void cStatistics::Render()
    {
        glEnable(GL_TEXTURE_2D);
        glDisableClientState(GL_NORMAL_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        glDisable(GL_LIGHTING);
        glDisable(GL_LIGHT0);
        glDisable(GL_BLEND);

        engine->SetProjection2D();
        engine->SetModelViewMatrix2D();

        Color color(255,255,255,255);
        engine->DrawFBOTexture(engine->m_fbo.m_Texture, color, true);

        glEnable(GL_LIGHTING);
        glEnableClientState(GL_NORMAL_ARRAY);
        glEnable(GL_LIGHT0);
        glBindTexture(GL_TEXTURE_2D, engine->texture_id_gray_concrete);
        DrawFallingCubes();
        glDisable(GL_LIGHTING);
        glDisable(GL_LIGHT0);

        glDisableClientState(GL_NORMAL_ARRAY);

        glEnable(GL_BLEND);

        engine->SetProjection2D();
        engine->SetModelViewMatrix2D();

        glBindTexture(GL_TEXTURE_2D, engine->texture_id_stat_background);
        DrawBackground();

        glBindTexture(GL_TEXTURE_2D, engine->texture_id_fonts_big);

        color = Color(0, 0, 0, 225);
        DrawText(color);

        if (m_text_display.m_vertex_count > 0)
        {
            glPushMatrix();
            glTranslatef(-2.0f * engine->device_scale, -2.0f * engine->device_scale, 0.0f);
            glDrawArrays(GL_TRIANGLES, 0, m_text_display.m_vertex_count);
            glPopMatrix();
        }

        color = Color(225,10,50,255);
        DrawText(color);

        if (m_text_display.m_vertex_count > 0)
        {
            glPushMatrix();
            glDrawArrays(GL_TRIANGLES, 0, m_text_display.m_vertex_count);
            glPopMatrix();
        }

        if (m_DrawStars)
        {
            glBindTexture(GL_TEXTURE_2D, engine->texture_id_star);
            DrawStars();
        }
    }

    #pragma mark - Input

    void cStatistics::OnFingerUp(float x, float y, int finger_count)
    {
        if (StatShow == m_state)
        {
            engine->level_init_data.init_action = JustContinue;
            engine->ShowScene(Scene_Level);
        }
    }

}

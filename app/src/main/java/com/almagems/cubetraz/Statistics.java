package com.almagems.cubetraz;

import java.util.ArrayList;

import static android.opengl.GLES10.*;
import static com.almagems.cubetraz.Constants.*;


public final class Statistics extends Scene {

    enum StatisticsStateEnum {
        StatNone,
        StatAppear,
        StatAppearTitle,
        StatAppearMovesPlayer,
        StatAppearMovesBest,
        StatShow,
        StatWait,
    }

    private boolean m_falling_cubes;

    private float m_timeout;

    private StatisticsStateEnum m_state;
    private StatisticsStateEnum m_state_next;

    private TextDisplay m_text_display;

    private Camera m_camera;

    private int m_vertex_count1;
    private int m_vindex1;
    private int m_cindex1;

    private int m_star_count;
    private float m_star_alpha;

    private float zoom;
    private float trans;

    private boolean m_UpdateBackground;
    private boolean m_DrawTitle;
    private boolean m_DrawMoves;
    private boolean m_DrawStars;
    private boolean m_DrawTap;

    private float m_rating_y;
    private float m_title_y;
    private float m_moves_y;
    private float m_stars_y;
    private float m_tap_y;

    private float m_background_alpha;
    private float m_background_rot_degree;
    private float m_background_alpha_pulse;

    private float m_text_scale;
    private float[] m_text_scales = new float[3]; // title, time, moves

    private Text m_text_title;
    private Text m_text_middle;
    private Text m_text_moves;
    private Text m_text_tap;

    private ArrayList<FallingCube> m_lst_falling_cubes;

    private float m_pulse;
    private boolean m_can_dismiss;


    private boolean canDismiss() { return m_can_dismiss; }


    @Override
    public void onFingerDown(float x, float y, int finger_count) {}

    @Override
    public  void onFingerMove(float prev_x, float prev_y, float cur_x, float cur_y, int finger_count) {}



    // ctor
    public Statistics() {
        m_camera.eye = new Vector(0.0f, 10.0f, 30.0f);
        m_camera.target = new Vector(0.0f, 0.0f, 0.0f);

        for (int i = 0; i < 10; ++i) {
            m_lst_falling_cubes.add(new FallingCube());
        }
    }

    public void initFallingCubes() {
        m_falling_cubes = true;

        float x = -7.0f;

        FallingCube fc;
        int size = m_lst_falling_cubes.size();
        for (int i = 0; i < size; ++i) {
            fc = m_lst_falling_cubes.get(i);

            fc.degree = Utils.randInt(0, 180);
            fc.pos = new Vector(x + i * 1.25f, Utils.randInt(9, 1), 0.0f);
            fc.speed = (float)Utils.randInt(3, 5) / 100f;

            ++i;
        }

        glDisable(GL_DEPTH_TEST);
    }

    @Override
    public void init() {
        m_can_dismiss = false;

        m_text_display.init();

        m_vertex_count1 = 0;
        m_vindex1 = -1;
        m_cindex1 = -1;

        m_star_alpha = 0.0f;
        zoom = 0.0f;
        trans = 0.0f;
        m_pulse = 0.0f;

        m_state = StatisticsStateEnum.StatAppear;
        m_state_next = StatisticsStateEnum.StatNone;
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

        initFallingCubes();

        m_text_title.setDisplay(m_text_display);
        m_text_middle.setDisplay(m_text_display);
        m_text_moves.setDisplay(m_text_display);
        m_text_tap.setDisplay(m_text_display);

        m_text_title.setUseBigFonts(true);
        m_text_middle.setUseBigFonts(true);
        m_text_moves.setUseBigFonts(true);
        m_text_tap.setUseBigFonts(true);

        m_text_title.setVSPace(1.0f);
        m_text_middle.setVSPace(0.8f);
        m_text_moves.setVSPace(0.8f);
        m_text_tap.setVSPace(0.7f);

        setup(Game.stat_init_data);
    }

    public void setup(StatInitData sid) {
        m_star_count = sid.stars;

        // texts
        m_text_title.setVSPace(1.0f);

        m_text_title.init(sid.str_title, true);
        m_text_middle.init("MOVES", true);
        m_text_moves.init(sid.str_moves, true);
        m_text_tap.init("TAP TO CONTINUE", true);

        // pre calc y positions
        m_stars_y = graphics.height * 0.75f;
        m_rating_y = graphics.height * 0.5f;

        m_title_y = graphics.height * 0.33f;
        m_moves_y = graphics.height * 0.2f;
        m_tap_y = graphics.height * 0.017f;
    }

    public void updateFallingCubes() {
        FallingCube fc;
        int size = m_lst_falling_cubes.size();
        for (int i = 0; i < size; ++i) {
            fc = m_lst_falling_cubes.get(i);

            fc.pos.y -= fc.speed;

            if (fc.pos.y < -4.0f) {
                fc.pos.y = Utils.randInt(9, 1);
                fc.speed = (float)Utils.randInt(3, 5) / 100f;
            }

            fc.degree += 1.0f;
        }
    }

    @Override
    public void update() {
        updateFallingCubes();

        switch (m_state) {
            case StatWait:

                if (StatisticsStateEnum.StatNone != m_state_next) {
                    m_pulse += 0.3f;
                    m_text_scale = (float)( Math.sin(m_pulse) / 5.0f) * graphics.device_scale;

                    //printf("\nPulse: %f, sinf(m_pulse): %f, text_scale: %f", m_pulse, sinf(m_pulse), m_text_scale);

                    switch (m_state_next) {
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

                    if (m_text_scale < EPSILON) {
                        m_state = m_state_next;
                        m_pulse = 0.0f;
                        m_text_scale = 0.0f;

                        switch (m_state_next) {
                            case StatAppearTitle:
                                m_text_scales[0] = m_text_scale;
                                break;

                            case StatAppearMovesPlayer:
                                m_text_scales[1] = m_text_scale;
                                break;

                            case StatAppearMovesBest:
                                m_text_scales[2] = m_text_scale;
                                //Game.showFullScreenAd();
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

                if (m_background_alpha > 0.5f) {
                    m_background_alpha = 0.5f;
                    m_state_next = StatisticsStateEnum.StatAppearTitle;
                    m_state = StatisticsStateEnum.StatWait;
                    m_timeout = 1.0f;
                    m_DrawTitle = true;
                    m_UpdateBackground = true;
                }

                break;

            case StatAppearTitle:
                m_state_next = StatisticsStateEnum.StatAppearMovesPlayer;
                m_state = StatisticsStateEnum.StatWait;
                m_timeout = 0.75f;
                m_text_scale = 0.0f;
                m_pulse = 0.0f;
                break;

            case StatAppearMovesPlayer:
                m_state_next = StatisticsStateEnum.StatAppearMovesBest;
                m_state = StatisticsStateEnum.StatWait;
                m_timeout = 0.75f;
                m_text_scale = 0.0f;
                m_pulse = 0.0f;
                m_DrawMoves = true;
                break;

            case StatAppearMovesBest:
                m_state_next = StatisticsStateEnum.StatShow;
                m_state = StatisticsStateEnum.StatWait;
                m_timeout = 0.75f;
                m_text_scale = 0.0f;
                m_pulse = 0.0f;
                trans = 0.0f;
                zoom = 0.0f;
                break;

            case StatShow:
                trans += 0.175f;
                zoom = (float)(Math.sin(trans) * 12.0f);
                break;

            default:
                break;
        } // switch

        if (m_DrawStars) {
            m_star_alpha += 0.009f;

            if (m_star_alpha > 1.0f)
                m_star_alpha = 1.0f;
        }

        if (m_UpdateBackground) {
            m_background_rot_degree += 0.125f;
            m_background_alpha = 0.5f + (float)(Math.sin(m_background_alpha_pulse) / 10.0f);
            m_background_alpha_pulse += 0.05f;
        }
    }

    public void drawFallingCubes() {
        graphics.setProjection3D();
        graphics.setModelViewMatrix3D(m_camera);
        //const vec4 lightPosition(-100.0f, 300.0f, 900.0f, 1.0f);
        //glLightfv(GL_LIGHT0, GL_POSITION, lightPosition.Pointer());

//        glNormalPointer(GL_FLOAT, 0, graphics.normals);
//        glVertexPointer(3, GL_FLOAT, 0, graphics.vertices);
//        glTexCoordPointer(2, GL_SHORT, 0, graphics.texture_coordinates);
//        glColorPointer(4, GL_UNSIGNED_BYTE, 0, graphics.colors);

        glPushMatrix();
        glTranslatef(0.0f, 0.0f, 10.0f);

        FallingCube fc;
        int size = m_lst_falling_cubes.size();
        for (int i = 0; i < size; ++i) {
            fc = m_lst_falling_cubes.get(i);

            glPushMatrix();
            glTranslatef(fc.pos.x, fc.pos.y, fc.pos.z);

            glRotatef(fc.degree, 1.0f, 0.0f, 0.0f);
            glRotatef(fc.degree, 0.0f, 1.0f, 0.0f);
            glRotatef(fc.degree, 0.0f, 0.0f, 1.0f);

            glScalef(0.6f, 0.6f, 0.6f);
            glDrawArrays(GL_TRIANGLES, 0, 36);
            glPopMatrix();
        }
        glPopMatrix();
    }

    public void drawBackground() {
        float dim = Math.max(graphics.width, graphics.height) * 1.25f;
        float q = dim / 2.0f;

        float vertices[] =
                {
                        -q, -q,
                        q, -q,
                        q,  q,
                        -q,  q
                };

        final short coords[] =
            {
                    0, 1,
                    1, 1,
                    1, 0,
                    0, 0
            };

        final byte color = 100;

        final float colors[] =
            {
                    color, color, color, m_background_alpha * 255,
                    color, color, color, m_background_alpha * 255,
                    color, color, color, m_background_alpha * 255,
                    color, color, color, m_background_alpha * 255
            };

//        glVertexPointer(2, GL_FLOAT, 0, vertices);
//        glTexCoordPointer(2, GL_SHORT, 0, coords);
//        glColorPointer(4, GL_UNSIGNED_BYTE, 0, colors);

        glPushMatrix();
        glTranslatef(graphics.half_width, graphics.half_height, 0.0f);
        glRotatef(m_background_rot_degree, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
        glPopMatrix();
    }

    public void drawText(Color color) {
        float x;
        float y;
        float scale;

        m_text_display.init();

        if (m_DrawTitle) {
            scale = graphics.device_scale + m_text_scales[0];
            m_text_title.setScale(scale + (0.3f * graphics.device_scale), scale + (0.6f * graphics.device_scale));

            x = graphics.half_width - m_text_title.getHalfWidth();
            y = m_rating_y - m_text_title.getHalfHeight();

            m_text_title.emitt(x, y, color);

            m_text_display.m_vertex_count += m_text_title.getVertexCount();
        }

        if (m_DrawMoves) {
            // middle
            scale = graphics.device_scale * 0.9f;
            m_text_middle.setScale(scale, scale + (0.2f * graphics.device_scale));

            x = graphics.half_width - m_text_middle.getHalfWidth();
            y = m_title_y - m_text_middle.getHalfHeight();

            m_text_middle.emitt(x, y, color);
            m_text_display.m_vertex_count += m_text_middle.getVertexCount();

            // moves
            scale = graphics.device_scale + m_text_scales[2] - (0.25f * graphics.device_scale);
            m_text_moves.setScale(scale + (0.1f * graphics.device_scale), scale + (0.1f * graphics.device_scale));

            x = graphics.half_width - m_text_moves.getHalfWidth();
            y = m_moves_y - m_text_moves.getHalfHeight();

            m_text_moves.emitt(x, y, color);
            m_text_display.m_vertex_count += m_text_moves.getVertexCount();
        }

        if (m_DrawTap) {
            scale = graphics.device_scale * 0.25f;
            m_text_tap.setScale(scale + (0.3f * graphics.device_scale), scale + (0.3f * graphics.device_scale));

            x = graphics.half_width - m_text_tap.getHalfWidth();
            y = m_tap_y;

            m_text_tap.emitt(x, y, color);

            m_text_display.m_vertex_count += m_text_tap.getVertexCount();
        }
    }

    public void drawStars() {
        //return;

        final float vertices[] =
            {
                    -0.5f, -0.5f,
                    0.5f, -0.5f,
                    0.5f,  0.5f,
                    -0.5f,  0.5f
            };

        final short coords[] =
            {
                    0, 1,
                    1, 1,
                    1, 0,
                    0, 0
            };

        int a = (int)m_star_alpha * 255;

        Color color_text = new Color(220, 20, 60, 240);

        final float colors_red[] =
            {
                    color_text.r, color_text.g, color_text.b, a,
                    color_text.r, color_text.g, color_text.b, a,
                    color_text.r, color_text.g, color_text.b, a,
                    color_text.r, color_text.g, color_text.b, a
            };

        final float colors_yellow[] =
            {
                    255, 255, 0, a,
                    255, 255, 0, a,
                    255, 255, 0, a,
                    255, 255, 0, a
            };

//        glVertexPointer(2, GL_FLOAT, 0, vertices);
//        glTexCoordPointer(2, GL_SHORT, 0, coords);
//        glColorPointer(4, GL_UNSIGNED_BYTE, 0, colors_red);

        float s = graphics.width * 0.14f;
        float w = graphics.width * 0.2f;
        float half_w = w / 2.0f;
        float x = graphics.half_width - ((w * m_star_count) / 2.0f);
        float y = m_stars_y;
        float raise = 3.0f * graphics.device_scale;

        for (int i = 0; i < m_star_count; ++i) {
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

        //glColorPointer(4, GL_UNSIGNED_BYTE, 0, colors_yellow);

        for (int i = 0; i < m_star_count; ++i) {
            glPushMatrix();
            glTranslatef(x + (i * w) + half_w, y, 0.0f);
            glRotatef(zoom, 0.0f, 0.0f, 1.0f);
            glScalef(s + zoom, s + zoom, 1.0f);
            glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
            glPopMatrix();
            y += raise;
        }
    }

    public void render() {
        glEnable(GL_TEXTURE_2D);
        glDisableClientState(GL_NORMAL_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        glDisable(GL_LIGHTING);
        glDisable(GL_LIGHT0);
        glDisable(GL_BLEND);

        graphics.setProjection2D();
        graphics.setModelViewMatrix2D();

        Color color = new Color(255,255,255,255);
        graphics.drawFBOTexture(Game.m_fbo.m_TextureId, color, true);

        glEnable(GL_LIGHTING);
        glEnableClientState(GL_NORMAL_ARRAY);
        glEnable(GL_LIGHT0);
        glBindTexture(GL_TEXTURE_2D, graphics.texture_id_gray_concrete);
        drawFallingCubes();
        glDisable(GL_LIGHTING);
        glDisable(GL_LIGHT0);

        glDisableClientState(GL_NORMAL_ARRAY);

        glEnable(GL_BLEND);

        graphics.setProjection2D();
        graphics.setModelViewMatrix2D();

        glBindTexture(GL_TEXTURE_2D, graphics.texture_id_stat_background);
        drawBackground();

        glBindTexture(GL_TEXTURE_2D, graphics.texture_id_fonts_big);

        color = new Color(0, 0, 0, 225);
        drawText(color);

        if (m_text_display.m_vertex_count > 0) {
            glPushMatrix();
            glTranslatef(-2.0f * graphics.device_scale, -2.0f * graphics.device_scale, 0.0f);
            glDrawArrays(GL_TRIANGLES, 0, m_text_display.m_vertex_count);
            glPopMatrix();
        }

        color = new Color(225,10,50,255);
        drawText(color);

        if (m_text_display.m_vertex_count > 0) {
            glPushMatrix();
            glDrawArrays(GL_TRIANGLES, 0, m_text_display.m_vertex_count);
            glPopMatrix();
        }

        if (m_DrawStars) {
            glBindTexture(GL_TEXTURE_2D, graphics.texture_id_star);
            drawStars();
        }
    }

    @Override
    public void onFingerUp(float x, float y, int finger_count) {
        if (StatisticsStateEnum.StatShow == m_state) {
            Game.level_init_data.init_action = LevelInitActionEnum.JustContinue;
            Game.showScene(Scene_Level);
        }
    }

}

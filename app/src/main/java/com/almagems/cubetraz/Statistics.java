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

    private Camera m_camera = new Camera();

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

    private Text m_text_title = new Text();
    private Text m_text_middle = new Text();
    private Text m_text_moves = new Text();
    private Text m_text_tap = new Text();

    private ArrayList<FallingCube> m_lst_falling_cubes = new ArrayList<>();

    private float m_pulse;
    private boolean m_can_dismiss;

    private boolean canDismiss() { return m_can_dismiss; }



    // ctor
    public Statistics() {
        m_camera.eye = new Vector(0.0f, 10.0f, 30.0f);
        m_camera.target = new Vector(0.0f, 0.0f, 0.0f);

        for (int i = 0; i < 12; ++i) {
            FallingCube fc = new FallingCube();
            m_lst_falling_cubes.add(fc);
        }
    }

    public void initFallingCubes() {
        m_falling_cubes = true;

        final float x = -7.0f;
        FallingCube fc;
        int size = m_lst_falling_cubes.size();
        for (int i = 0; i < size; ++i) {
            fc = m_lst_falling_cubes.get(i);

            fc.degree = Utils.randInt(0, 180);
            fc.pos.x = x + i * 1.25f;
            fc.pos.y = Utils.randInt(6, 12);
            fc.pos.z = 0f;
            fc.speed = 0.05f + (float)Utils.randInt(3, 5) / 100f;
        }

        glDisable(GL_DEPTH_TEST);
    }

    @Override
    public void init() {
        m_can_dismiss = false;

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

        m_text_scales[0] = 0f;   // title
        m_text_scales[1] = 0f;   // time
        m_text_scales[2] = 0f;   // moves

        initFallingCubes();

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
        m_stars_y = Graphics.height * 0.75f;
        m_rating_y = Graphics.height * 0.5f;

        m_title_y = Graphics.height * 0.33f;
        m_moves_y = Graphics.height * 0.2f;
        m_tap_y = Graphics.height * 0.017f;
    }

    public void updateFallingCubes() {
        FallingCube fc;
        int size = m_lst_falling_cubes.size();
        for (int i = 0; i < size; ++i) {
            fc = m_lst_falling_cubes.get(i);
            fc.pos.y -= fc.speed;
            if (fc.pos.y < -4.0f) {
                fc.pos.y = Utils.randInt(7, 10);
                fc.speed = 0.05f + (float)Utils.randInt(3, 5) / 100f;
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
                    m_text_scale = (float)( Math.sin(m_pulse) / 5.0f) * Graphics.device_scale;
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
        Game.bindCubeGLData();
        graphics.resetBufferIndices();
        graphics.zeroBufferPositions();

        glPushMatrix();
            glTranslatef(0f, 0f, 5f);

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
        float dim = Math.max(Graphics.width, Graphics.height) * 1.25f;
        float q = dim / 2.0f;

        final float verts[] = {
            -q, -q,
             q, -q,
             q,  q,
            -q,  q
        };

        final float coords[] = {
            0f, 1f,
            1f, 1f,
            1f, 0f,
            0f, 0f
        };

        final byte color = (byte)100;
        final byte maxColor = (byte)(m_background_alpha * 255);

        final byte colors[] = {
            color, color, color, maxColor,
            color, color, color, maxColor,
            color, color, color, maxColor,
            color, color, color, maxColor
        };

        graphics.addVerticesCoordsColors(verts, coords, colors);

        glPushMatrix();
            glTranslatef(Graphics.half_width, Graphics.half_height, 0.0f);
            glRotatef(m_background_rot_degree, 0.0f, 0.0f, 1.0f);
            glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
        glPopMatrix();
    }

    public boolean drawText(Color color) {
        boolean shouldDraw = false;
        Vector2 pos = new Vector2();
        float scale;

        if (m_DrawTitle) {
            scale = Graphics.device_scale + m_text_scales[0];
            m_text_title.setScale(scale + (0.3f * Graphics.device_scale), scale + (0.6f * Graphics.device_scale));

            pos.x = Graphics.half_width - m_text_title.getHalfWidth();
            pos.y = m_rating_y - m_text_title.getHalfHeight();
            m_text_title.emitt(pos, color);
            shouldDraw = true;
        }

        if (m_DrawMoves) {
            // middle
            scale = Graphics.device_scale * 0.9f;
            m_text_middle.setScale(scale, scale + (0.2f * Graphics.device_scale));

            pos.x = Graphics.half_width - m_text_middle.getHalfWidth();
            pos.y = m_title_y - m_text_middle.getHalfHeight();

            m_text_middle.emitt(pos, color);

            // moves
            scale = Graphics.device_scale + m_text_scales[2] - (0.25f * Graphics.device_scale);
            m_text_moves.setScale(scale + (0.1f * Graphics.device_scale), scale + (0.1f * Graphics.device_scale));

            pos.x = Graphics.half_width - m_text_moves.getHalfWidth();
            pos.y = m_moves_y - m_text_moves.getHalfHeight();

            m_text_moves.emitt(pos, color);
            shouldDraw = true;
        }

        if (m_DrawTap) {
            scale = Graphics.device_scale * 0.25f;
            m_text_tap.setScale(scale + (0.3f * Graphics.device_scale), scale + (0.3f * Graphics.device_scale));

            pos.x = Graphics.half_width - m_text_tap.getHalfWidth();
            pos.y = m_tap_y;

            m_text_tap.emitt(pos, color);
            shouldDraw = true;
        }
        return shouldDraw;
    }

    public void drawStars() {
        final float verts[] = {
            -0.5f, -0.5f,
             0.5f, -0.5f,
             0.5f,  0.5f,
            -0.5f,  0.5f
        };

        final float coords[] = {
            0f, 1f,
            1f, 1f,
            1f, 0f,
            0f, 0f
        };

        byte a = (byte)(int)(m_star_alpha * 255);

        Color color_text = new Color(220, 20, 60, 240);

        final byte colors_red[] = {
            color_text.R, color_text.G, color_text.B, a,
            color_text.R, color_text.G, color_text.B, a,
            color_text.R, color_text.G, color_text.B, a,
            color_text.R, color_text.G, color_text.B, a
        };

        byte maxColor = (byte)255;

        final byte colors_yellow[] = {
            maxColor, maxColor, 0, a,
            maxColor, maxColor, 0, a,
            maxColor, maxColor, 0, a,
            maxColor, maxColor, 0, a
        };

        graphics.addVerticesCoordsColors(verts, coords, colors_red);

        float s = Graphics.width * 0.14f;
        float w = Graphics.width * 0.2f;
        float half_w = w / 2.0f;
        float x = Graphics.half_width - ((w * m_star_count) / 2.0f);
        float y = m_stars_y;
        float raise = 3.0f * Graphics.device_scale;

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

        graphics.addVerticesCoordsColors(verts, coords, colors_yellow);

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

    @Override
    public void render() {
        Color color;

        glEnable(GL_TEXTURE_2D);

        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        glDisable(GL_LIGHTING);
        glDisable(GL_LIGHT0);
        glDisable(GL_BLEND);

        graphics.setProjection2D();
        graphics.setModelViewMatrix2D();
        graphics.bindStreamSources2d();

        color = new Color(255, 255, 255, 255);
        graphics.drawFullScreenTexture(Graphics.texture_id_dirty, color); // TODO: pass FBO texutre id as param!

        graphics.setProjection3D();
        graphics.setModelViewMatrix3D(m_camera);

        graphics.bindStreamSources3d();
        graphics.resetBufferIndices();
        graphics.zeroBufferPositions();

        glDepthMask(true);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_gray_concrete);

        final float[] lightPosition = { -100.0f, 300.0f, 900.0f, 1.0f };
        glLightfv(GL_LIGHT0, GL_POSITION, lightPosition, 0);

        drawFallingCubes();
        glDisable(GL_LIGHTING);
        glDisable(GL_LIGHT0);
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glDepthMask(false);

        graphics.setProjection2D();
        graphics.setModelViewMatrix2D();
        graphics.bindStreamSources2d();

        graphics.resetBufferIndices();
        graphics.zeroBufferPositions();

        glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_stat_background);
        drawBackground();

        if (m_DrawStars) {
            glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_star);
            drawStars();
        }

        graphics.resetBufferIndices();
        graphics.zeroBufferPositions();

        glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_fonts_big);

        color = new Color(0, 0, 0, 225);
        boolean shouldDraw;
        shouldDraw = drawText(color);
        if (shouldDraw) {
            glPushMatrix();
                glTranslatef(-2.0f * Graphics.device_scale, -2.0f * Graphics.device_scale, 0.0f);
                graphics.updateBuffersAll();
                graphics.renderTriangles();
            glPopMatrix();
        }

        color = new Color(225, 10, 50, 255);
        shouldDraw = drawText(color);
        if (shouldDraw) {
            graphics.updateBuffersAll();
            graphics.renderTriangles();
        }

        glDepthMask(true);
    }

    @Override
    public void onFingerDown(float x, float y, int finger_count) {}

    @Override
    public  void onFingerMove(float prev_x, float prev_y, float cur_x, float cur_y, int finger_count) {}

    @Override
    public void onFingerUp(float x, float y, int finger_count) {
        if (StatisticsStateEnum.StatShow == m_state) {
            Game.level_init_data.init_action = LevelInitActionEnum.JustContinue;
            Game.showScene(Scene_Level);
        }
    }

}

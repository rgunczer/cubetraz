package com.almagems.cubetraz.scenes.stat;

import com.almagems.cubetraz.game.Engine;
import com.almagems.cubetraz.graphics.Color;
import com.almagems.cubetraz.game.Game;
import com.almagems.cubetraz.graphics.Graphics;
import com.almagems.cubetraz.graphics.Text;
import com.almagems.cubetraz.math.Utils;
import com.almagems.cubetraz.math.Vector;
import com.almagems.cubetraz.math.Vector2;
import com.almagems.cubetraz.scenes.Scene;

import java.util.ArrayList;
import static android.opengl.GLES10.*;
import static com.almagems.cubetraz.game.Constants.*;


public final class Statistics extends Scene {

    private enum State {
        StatNone,
        StatAppear,
        StatAppearTitle,
        StatAppearMovesPlayer,
        StatAppearMovesBest,
        StatShow,
        StatWait,
    }

    private State mState;
    private State mStateNext;

    private int mStarCount;
    private float mStarAlpha;

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

    private float mBackgroundAlpha;
    private float mBackgroundRotDegree;
    private float mBackgroundAlphaPulse;

    private float[] m_text_scales = new float[3]; // title, time, moves

    private Text mTextTitle = new Text();
    private Text mTextMiddle = new Text();
    private Text mTextMoves = new Text();
    private Text mTextTap = new Text();

    private ArrayList<FallingCube> mFallingCubes = new ArrayList<>();

    private float m_pulse;
    private boolean m_can_dismiss;

    private boolean canDismiss() { return m_can_dismiss; }

    private Vector2 rot = new Vector2(3f, 0f);

    public Statistics() {
        mCameraCurrent.eye = new Vector(0.0f, 10.0f, 30.0f);
        mCameraCurrent.target = new Vector(0.0f, 0.0f, 0.0f);

        for (int i = 0; i < 12; ++i) {
            FallingCube fc = new FallingCube();
            mFallingCubes.add(fc);
        }
    }

    public void initFallingCubes() {
        final float x = -7.0f;
        FallingCube fc;
        int size = mFallingCubes.size();
        for (int i = 0; i < size; ++i) {
            fc = mFallingCubes.get(i);

            fc.degree = Utils.randInt(0, 180);
            fc.pos.x = x + i * 1.25f;
            fc.pos.y = Utils.randInt(6, 12);
            fc.pos.z = 0f;
            fc.speed = 0.05f + (float)Utils.randInt(3, 5) / 100f;
        }
    }

    @Override
    public void init() {
        m_can_dismiss = false;

        mStarAlpha = 0.0f;
        zoom = 0.0f;
        trans = 0.0f;
        m_pulse = 0.0f;

        mState = State.StatAppear;
        mStateNext = State.StatNone;
        mBackgroundAlpha = 0.0f;
        mBackgroundRotDegree = 0.0f;
        mBackgroundAlphaPulse = 0.0f;

        m_UpdateBackground = false;
        m_DrawTitle = false;
        m_DrawMoves = false;
        m_DrawStars = false;
        m_DrawTap = false;

        m_text_scales[0] = 0f;   // title
        m_text_scales[1] = 0f;   // time
        m_text_scales[2] = 0f;   // moves

        initFallingCubes();

        mTextTitle.setUseBigFonts(true);
        mTextMiddle.setUseBigFonts(true);
        mTextMoves.setUseBigFonts(true);
        mTextTap.setUseBigFonts(true);

        mTextTitle.setVSPace(1.0f);
        mTextMiddle.setVSPace(0.8f);
        mTextMoves.setVSPace(0.8f);
        mTextTap.setVSPace(0.7f);

        setup(Game.stat_init_data);
    }

    public void setup(StatInitData sid) {
        mStarCount = sid.stars;

        // texts
        mTextTitle.setVSPace(1.0f);

        mTextTitle.init(sid.str_title, true);
        mTextMiddle.init("MOVES", true);
        mTextMoves.init(sid.str_moves, true);
        mTextTap.init("TAP TO CONTINUE", true);

        // pre calc y positions
        m_stars_y = Engine.graphics.height * 0.75f;
        m_rating_y = Engine.graphics.height * 0.5f;

        m_title_y = Engine.graphics.height * 0.33f;
        m_moves_y = Engine.graphics.height * 0.2f;
        m_tap_y = Engine.graphics.height * 0.017f;
    }

    public void updateFallingCubes() {
        FallingCube fc;
        int size = mFallingCubes.size();
        for (int i = 0; i < size; ++i) {
            fc = mFallingCubes.get(i);
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
        switch (mState) {
            case StatWait:
                if (State.StatNone != mStateNext) {
                    m_pulse += 0.3f;
                    float mTextScale = (float)( Math.sin(m_pulse) / 5.0f) * Engine.graphics.deviceScale;

                    switch (mStateNext) {
                        case StatAppearTitle:
                            m_text_scales[0] = mTextScale;
                            m_DrawStars = true;
                            break;

                        case StatAppearMovesPlayer:
                            m_text_scales[1] = mTextScale;
                            break;

                        case StatAppearMovesBest:
                            m_text_scales[2] = mTextScale;
                            break;

                        default:
                            break;
                    }

                    if (mTextScale < EPSILON) {
                        mState = mStateNext;
                        m_pulse = 0.0f;
                        mTextScale = 0.0f;

                        switch (mStateNext) {
                            case StatAppearTitle:
                                m_text_scales[0] = mTextScale;
                                break;

                            case StatAppearMovesPlayer:
                                m_text_scales[1] = mTextScale;
                                break;

                            case StatAppearMovesBest:
                                m_text_scales[2] = mTextScale;
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
                mBackgroundAlpha += 0.025f;
                if (mBackgroundAlpha > 0.5f) {
                    mBackgroundAlpha = 0.5f;
                    mStateNext = State.StatAppearTitle;
                    mState = State.StatWait;
                    m_DrawTitle = true;
                    m_UpdateBackground = true;
                }
                break;

            case StatAppearTitle:
                mStateNext = State.StatAppearMovesPlayer;
                mState = State.StatWait;
                m_pulse = 0.0f;
                break;

            case StatAppearMovesPlayer:
                mStateNext = State.StatAppearMovesBest;
                mState = State.StatWait;
                m_pulse = 0.0f;
                m_DrawMoves = true;
                break;

            case StatAppearMovesBest:
                mStateNext = State.StatShow;
                mState = State.StatWait;
                m_pulse = 0.0f;
                trans = 0.0f;
                zoom = 0.0f;
                break;

            case StatShow:
                trans += 0.15f;
                zoom = (float)(Math.sin(trans) * 22.0f);
                rot.rotateRad(0.025f);
                break;

            default:
                break;
        } // switch

        if (m_DrawStars) {
            mStarAlpha += 0.009f;

            if (mStarAlpha > 1.0f)
                mStarAlpha = 1.0f;
        }

        if (m_UpdateBackground) {
            mBackgroundRotDegree += 0.125f;
            mBackgroundAlpha = 0.5f + (float)(Math.sin(mBackgroundAlphaPulse) / 10.0f);
            mBackgroundAlphaPulse += 0.05f;
        }
    }

    public void drawFallingCubes() {
        Graphics graphics = Engine.graphics;
        Game.bindCubeGLData();
        graphics.resetBufferIndices();
        graphics.zeroBufferPositions();

        glPushMatrix();
            glTranslatef(0f, 0f, 5f);

            FallingCube fc;
            int size = mFallingCubes.size();
            for (int i = 0; i < size; ++i) {
                fc = mFallingCubes.get(i);

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
        Graphics graphics = Engine.graphics;
        float dim = Math.max(Engine.graphics.width, Engine.graphics.height) * 1.25f;
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
        final byte maxColor = (byte)(mBackgroundAlpha * 255);

        final byte colors[] = {
            color, color, color, maxColor,
            color, color, color, maxColor,
            color, color, color, maxColor,
            color, color, color, maxColor
        };

        graphics.addVerticesCoordsColors(verts, coords, colors);

        glPushMatrix();
            glTranslatef(Engine.graphics.halfWidth, Engine.graphics.halfHeight, 0.0f);
            glRotatef(mBackgroundRotDegree, 0.0f, 0.0f, 1.0f);
            glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
        glPopMatrix();
    }

    public boolean drawText(Color color, Color tapToContinueColor) {
        Graphics graphics = Engine.graphics;
        boolean shouldDraw = false;
        Vector2 pos = new Vector2();
        float scale;

        if (m_DrawTitle) {
            scale = graphics.deviceScale + m_text_scales[0];
            mTextTitle.setScale(scale + (0.3f * graphics.deviceScale), scale + (0.6f * graphics.deviceScale));

            pos.x = graphics.halfWidth - mTextTitle.getHalfWidth();
            pos.y = m_rating_y - mTextTitle.getHalfHeight();
            mTextTitle.emitt(pos, color);
            shouldDraw = true;
        }

        if (m_DrawMoves) {
            // middle
            scale = graphics.deviceScale * 0.9f;
            mTextMiddle.setScale(scale, scale + (0.2f * graphics.deviceScale));

            pos.x = graphics.halfWidth - mTextMiddle.getHalfWidth();
            pos.y = m_title_y - mTextMiddle.getHalfHeight();

            mTextMiddle.emitt(pos, color);

            // moves
            scale = graphics.deviceScale + m_text_scales[2] - (0.25f * graphics.deviceScale);
            mTextMoves.setScale(scale + (0.1f * graphics.deviceScale), scale + (0.1f * graphics.deviceScale));

            pos.x = graphics.halfWidth - mTextMoves.getHalfWidth();
            pos.y = m_moves_y - mTextMoves.getHalfHeight();

            mTextMoves.emitt(pos, color);
            shouldDraw = true;
        }

        if (m_DrawTap) {
            scale = graphics.deviceScale * 0.25f;
            mTextTap.setScale(scale + (0.3f * graphics.deviceScale), scale + (0.3f * graphics.deviceScale));

            pos.x = graphics.halfWidth - mTextTap.getHalfWidth();
            pos.y = m_tap_y;

            mTextTap.emitt(pos, tapToContinueColor);
            shouldDraw = true;
        }
        return shouldDraw;
    }

    public void drawStars() {
        Graphics graphics = Engine.graphics;
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

        byte a = (byte)(int)(mStarAlpha * 255);

        Color color_text = new Color(175, 0, 0, 200);

        final byte colors_red[] = {
            (byte)color_text.r, (byte)color_text.g, (byte)color_text.b, a,
            (byte)color_text.r, (byte)color_text.g, (byte)color_text.b, a,
            (byte)color_text.r, (byte)color_text.g, (byte)color_text.b, a,
            (byte)color_text.r, (byte)color_text.g, (byte)color_text.b, a
        };

        byte maxColor = (byte)200;
        final byte colors_yellow[] = {
            maxColor, maxColor, 0, a,
            maxColor, maxColor, 0, a,
            maxColor, maxColor, 0, a,
            maxColor, maxColor, 0, a
        };

        graphics.addVerticesCoordsColors(verts, coords, colors_red);

        float s = graphics.width * 0.14f;
        float w = graphics.width * 0.2f;
        float half_w = w / 2.0f;
        float x = graphics.halfWidth - ((w * mStarCount) / 2.0f);
        float y = m_stars_y;
        float raise = 3.0f * graphics.deviceScale;

        for (int i = 0; i < mStarCount; ++i) {
            glPushMatrix();
                glTranslatef(x + (i * w) + half_w, y, 0.0f);
                glRotatef(zoom, 0.0f, 0.0f, 1.0f);
                glScalef(s, s, 1.0f);
                glScalef(1.2f, 1.2f, 1.0f);
                glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
            glPopMatrix();
            y += raise;
        }

        y = m_stars_y;

        graphics.addVerticesCoordsColors(verts, coords, colors_yellow);

        for (int i = 0; i < mStarCount; ++i) {
            glPushMatrix();
                glTranslatef(x + (i * w) + half_w, y, 0.0f);
                glRotatef(zoom, 0.0f, 0.0f, 1.0f);
                glScalef(s, s, 1.0f);
                glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
            glPopMatrix();
            y += raise;
        }
    }

    @Override
    public void render() {
        Graphics graphics = Engine.graphics;
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

        graphics.drawFullScreenTexture(graphics.fbo, Color.WHITE);

        graphics.setProjection3D();
        graphics.setModelViewMatrix3D(mCameraCurrent);

        graphics.bindStreamSources3d();
        graphics.resetBufferIndices();
        graphics.zeroBufferPositions();

        glDepthMask(true);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        graphics.textureGrayConcrete.bind();

        graphics.setLightPosition(new Vector(-100.0f, 300.0f, 900.0f));

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

        graphics.textureStatBackground.bind();
        drawBackground();

        if (m_DrawStars) {
            graphics.textureStar.bind();
            drawStars();
        }

        graphics.resetBufferIndices();
        graphics.zeroBufferPositions();

        graphics.textureFontsBig.bind();

        color = new Color(0, 0, 0, 200);
        boolean shouldDraw;
        shouldDraw = drawText(color, color);
        if (shouldDraw) {
            glPushMatrix();
                glTranslatef(rot.x * graphics.deviceScale, rot.y * graphics.deviceScale, 0.0f);
                graphics.updateBuffers();
                graphics.renderTriangles();
            glPopMatrix();
        }

        Color colorTapToContinue = new Color(240, 200, 200, 250);
        color = new Color(200, 0, 0, 250);
        shouldDraw = drawText(color, colorTapToContinue);
        if (shouldDraw) {
            graphics.updateBuffers();
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
        if (State.StatShow == mState) {
            Game.level_init_data.init_action = LevelInitActionEnum.JustContinue;
            Game.showScene(Scene_Level);
        }
    }

}

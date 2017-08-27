package com.almagems.cubetraz.scenes.stat;

import com.almagems.cubetraz.graphics.Color;
import com.almagems.cubetraz.Game;
import com.almagems.cubetraz.graphics.Graphics;
import com.almagems.cubetraz.graphics.Text;
import com.almagems.cubetraz.math.Utils;
import com.almagems.cubetraz.math.Vector;
import com.almagems.cubetraz.math.Vector2;
import com.almagems.cubetraz.scenes.Scene;

import java.util.ArrayList;
import static android.opengl.GLES10.*;
import static com.almagems.cubetraz.Game.*;


public final class Statistics extends Scene {

    private enum State {
        None,
        Appear,
        AppearTitle,
        AppearMovesPlayer,
        AppearMovesBest,
        Show,
        Wait,
    }

    private State mState;
    private State mStateNext;

    private int mStarCount;
    private float mStarAlpha;

    private float zoom;
    private float trans;

    private boolean mUpdateBackground;
    private boolean mDrawTitle;
    private boolean mDrawMoves;
    private boolean mDrawStars;
    private boolean mDrawTap;

    private float mRatingY;
    private float mTitleY;
    private float mMovesY;
    private float mStarsY;
    private float mTapY;

    private float mBackgroundAlpha;
    private float mBackgroundRotDegree;
    private float mBackgroundAlphaPulse;

    private float[] mTextScales = new float[3];

    private Text mTextTitle = new Text();
    private Text mTextMiddle = new Text();
    private Text mTextMoves = new Text();
    private Text mTextTap = new Text();

    private Color mColorTapToContinue = new Color(240, 240, 240, 250);
    private Color mShadowColor = new Color(0, 0, 0, 200);
    private Color mTextColor = new Color(240, 0, 0, 255);

    private ArrayList<FallingCube> mFallingCubes = new ArrayList<>();

    private float mPulse;

    private Vector2 rot = new Vector2(3f, 0f);

    public Statistics() {
        mCameraCurrent.setEye(0.0f, 10.0f, 30.0f);
        mCameraCurrent.setTarget(0.0f, 0.0f, 0.0f);

        for (int i = 0; i < 12; ++i) {
            mFallingCubes.add(new FallingCube());
        }
    }

    private void initFallingCubes() {
        final float x = -7.0f;
        FallingCube cube;
        final int size = mFallingCubes.size();
        for (int i = 0; i < size; ++i) {
            cube = mFallingCubes.get(i);

            cube.degree = Utils.randInt(0, 180);
            cube.pos.x = x + i * 1.25f;
            cube.pos.y = Utils.randInt(6, 12);
            cube.pos.z = 0f;
            cube.speed = 0.05f + (float)Utils.randInt(3, 5) / 100f;
        }
    }

    @Override
    public void init() {
        mStarAlpha = 0.0f;
        zoom = 0.0f;
        trans = 0.0f;
        mPulse = 0.0f;

        mPosLightCurrent.init(new Vector(-100.0f, 300.0f, 900.0f));

        mState = State.Appear;
        mStateNext = State.None;
        mBackgroundAlpha = 0.0f;
        mBackgroundRotDegree = 0.0f;
        mBackgroundAlphaPulse = 0.0f;

        mUpdateBackground = false;
        mDrawTitle = false;
        mDrawMoves = false;
        mDrawStars = false;
        mDrawTap = false;

        mTextScales[0] = 0f;
        mTextScales[1] = 0f;
        mTextScales[2] = 0f;

        initFallingCubes();

        mTextTitle.setVSPace(1.0f);
        mTextMiddle.setVSPace(0.8f);
        mTextMoves.setVSPace(0.8f);
        mTextTap.setVSPace(0.7f);

        setup();
    }

    public void setup() {
        mStarCount = Game.statInit.stars;

        mTextTitle.setVSPace(1.0f);

        mTextTitle.init(Game.statInit.title);
        mTextMiddle.init("MOVES");
        mTextMoves.init(Game.statInit.moves);
        mTextTap.init("TAP TO CONTINUE");

        // pre calc y positions
        mStarsY = Graphics.height * 0.75f;
        mRatingY = Graphics.height * 0.5f;

        mTitleY = Graphics.height * 0.33f;
        mMovesY = Graphics.height * 0.2f;
        mTapY = Graphics.height * 0.017f;
    }

    private void updateFallingCubes() {
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
            case Wait:
                if (mStateNext != State.None) {
                    mPulse += 0.3f;
                    float mTextScale = (float)( Math.sin(mPulse) / 5.0f) * Graphics.deviceScale;

                    switch (mStateNext) {
                        case AppearTitle:
                            mTextScales[0] = mTextScale;
                            mDrawStars = true;
                            break;

                        case AppearMovesPlayer:
                            mTextScales[1] = mTextScale;
                            break;

                        case AppearMovesBest:
                            mTextScales[2] = mTextScale;
                            break;

                        default:
                            break;
                    }

                    if (mTextScale < EPSILON) {
                        mState = mStateNext;
                        mPulse = 0.0f;
                        mTextScale = 0.0f;

                        switch (mStateNext) {
                            case AppearTitle:
                                mTextScales[0] = mTextScale;
                                break;

                            case AppearMovesPlayer:
                                mTextScales[1] = mTextScale;
                                break;

                            case AppearMovesBest:
                                mTextScales[2] = mTextScale;
                                break;

                            case Show:
                                mDrawTap = true;
                                break;

                            default:
                                break;
                        }
                    }
                }
                break;

            case Appear:
                mBackgroundAlpha += 0.025f;
                if (mBackgroundAlpha > 0.5f) {
                    mBackgroundAlpha = 0.5f;
                    mStateNext = State.AppearTitle;
                    mState = State.Wait;
                    mDrawTitle = true;
                    mUpdateBackground = true;
                }
                break;

            case AppearTitle:
                mStateNext = State.AppearMovesPlayer;
                mState = State.Wait;
                mPulse = 0.0f;
                break;

            case AppearMovesPlayer:
                mStateNext = State.AppearMovesBest;
                mState = State.Wait;
                mPulse = 0.0f;
                mDrawMoves = true;
                break;

            case AppearMovesBest:
                mStateNext = State.Show;
                mState = State.Wait;
                mPulse = 0.0f;
                trans = 0.0f;
                zoom = 0.0f;
                break;

            case Show:
                trans += 0.15f;
                zoom = (float)(Math.sin(trans) * 22.0f);
                rot.rotateRad(0.025f);
                break;

            default:
                break;
        }

        if (mDrawStars) {
            mStarAlpha += 0.009f;
            mStarAlpha = Game.clamp(mStarAlpha);
        }

        if (mUpdateBackground) {
            mBackgroundRotDegree += 0.125f;
            mBackgroundAlpha = 0.5f + (float)(Math.sin(mBackgroundAlphaPulse) / 10.0f);
            mBackgroundAlphaPulse += 0.05f;
        }
    }

    private void drawFallingCubes() {
        Game.bindCubeGLData();
        Graphics.resetBufferIndices();
        Graphics.zeroBufferPositions();

        glPushMatrix();
            glTranslatef(0f, 0f, 5f);

            FallingCube cube;
            final int size = mFallingCubes.size();
            for (int i = 0; i < size; ++i) {
                cube = mFallingCubes.get(i);

                glPushMatrix();
                    Graphics.translate(cube.pos);
                    Graphics.rotateX(cube.degree);
                    Graphics.rotateY(cube.degree);
                    Graphics.rotateZ(cube.degree);
                    Graphics.scale(0.6f, 0.6f, 0.6f);
                    Graphics.drawCube();
                glPopMatrix();
            }
        glPopMatrix();
    }

    private void drawBackground() {
        
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
        final byte maxColor = (byte)(mBackgroundAlpha * 255);

        final byte colors[] = {
            color, color, color, maxColor,
            color, color, color, maxColor,
            color, color, color, maxColor,
            color, color, color, maxColor
        };

        Graphics.addVerticesCoordsColors(verts, coords, colors);

        glPushMatrix();
            glTranslatef(Graphics.halfWidth, Graphics.halfHeight, 0.0f);
            glRotatef(mBackgroundRotDegree, 0.0f, 0.0f, 1.0f);
            glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
        glPopMatrix();
    }

    private boolean drawText(Color color, Color tapToContinueColor) {
        boolean shouldDraw = false;
        Vector2 pos = new Vector2();
        float scale;

        if (mDrawTitle) {
            scale = Graphics.deviceScale + mTextScales[0];
            mTextTitle.setScale(scale + (0.3f * Graphics.deviceScale), scale + (0.6f * Graphics.deviceScale));

            pos.x = Graphics.halfWidth - mTextTitle.getHalfWidth();
            pos.y = mRatingY - mTextTitle.getHalfHeight();
            mTextTitle.emit(pos, color);
            shouldDraw = true;
        }

        if (mDrawMoves) {
            // middle
            scale = Graphics.deviceScale * 0.9f;
            mTextMiddle.setScale(scale, scale + (0.2f * Graphics.deviceScale));

            pos.x = Graphics.halfWidth - mTextMiddle.getHalfWidth();
            pos.y = mTitleY - mTextMiddle.getHalfHeight();

            mTextMiddle.emit(pos, color);

            // moves
            scale = Graphics.deviceScale + mTextScales[2] - (0.25f * Graphics.deviceScale);
            mTextMoves.setScale(scale + (0.1f * Graphics.deviceScale), scale + (0.1f * Graphics.deviceScale));

            pos.x = Graphics.halfWidth - mTextMoves.getHalfWidth();
            pos.y = mMovesY - mTextMoves.getHalfHeight();

            mTextMoves.emit(pos, color);
            shouldDraw = true;
        }

        if (mDrawTap) {
            scale = Graphics.deviceScale * 0.25f;
            mTextTap.setScale(scale + (0.3f * Graphics.deviceScale), scale + (0.3f * Graphics.deviceScale));

            pos.x = Graphics.halfWidth - mTextTap.getHalfWidth();
            pos.y = mTapY;

            mTextTap.emit(pos, tapToContinueColor);
            shouldDraw = true;
        }
        return shouldDraw;
    }

    private void drawStars() {
        final float vertices[] = {
            -0.5f, -0.5f,
             0.5f, -0.5f,
             0.5f,  0.5f,
            -0.5f,  0.5f
        };

        final float coordinates[] = {
            0f, 1f,
            1f, 1f,
            1f, 0f,
            0f, 0f
        };

        byte a = (byte)(int)(mStarAlpha * 255);

        Color color_text = new Color(240, 0, 0, 255);

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

        Graphics.addVerticesCoordsColors(vertices, coordinates, colors_red);

        float s = Graphics.width * 0.14f;
        float w = Graphics.width * 0.2f;
        float half_w = w / 2.0f;
        float x = Graphics.halfWidth - ((w * mStarCount) / 2.0f);
        float y = mStarsY;
        float raise = 3.0f * Graphics.deviceScale;

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

        y = mStarsY;

        Graphics.addVerticesCoordsColors(vertices, coordinates, colors_yellow);

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
        glEnable(GL_TEXTURE_2D);

        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        glDisable(GL_LIGHTING);
        glDisable(GL_LIGHT0);
        glDisable(GL_BLEND);

        Graphics.setProjection2D();
        Graphics.setModelViewMatrix2D();
        Graphics.bindStreamSources2d();

        Graphics.drawFullScreenTexture(Graphics.fbo, Color.WHITE);

        Graphics.setProjection3D();
        Graphics.setModelViewMatrix3D(mCameraCurrent);

        Graphics.bindStreamSources3d();
        Graphics.resetBufferIndices();
        Graphics.zeroBufferPositions();

        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        Graphics.textureGrayConcrete.bind();

        Graphics.setLightPosition(mPosLightCurrent);

        drawFallingCubes();
        glDisable(GL_LIGHTING);
        glDisable(GL_LIGHT0);
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glDepthMask(false);

        Graphics.setProjection2D();
        Graphics.setModelViewMatrix2D();
        Graphics.bindStreamSources2d();

        Graphics.resetBufferIndices();
        Graphics.zeroBufferPositions();

        Graphics.textureStatBackground.bind();
        drawBackground();

        if (mDrawStars) {
            Graphics.textureStar.bind();
            drawStars();
        }

        Graphics.resetBufferIndices();
        Graphics.zeroBufferPositions();

        Graphics.textureFontsBig.bind();

        boolean shouldDraw;
        shouldDraw = drawText(mShadowColor, mShadowColor);
        if (shouldDraw) {
            glPushMatrix();
                glTranslatef(rot.x * Graphics.deviceScale, rot.y * Graphics.deviceScale, 0.0f);
                Graphics.updateBuffers();
                Graphics.renderTriangles();
            glPopMatrix();
        }

        shouldDraw = drawText(mTextColor, mColorTapToContinue);
        if (shouldDraw) {
            Graphics.updateBuffers();
            Graphics.renderTriangles();
        }
    }

    @Override
    public void onFingerDown(float x, float y, int fingerCount) {}

    @Override
    public  void onFingerMove(float prevX, float prevY, float curX, float curY, int fingerCount) {}

    @Override
    public void onFingerUp(float x, float y, int fingerCount) {
        if (mState == State.Show) {
            Game.levelInitData.initAction = LevelInitActionEnum.JustContinue;
            Game.showScene(Scene_Level);
        }
    }

}

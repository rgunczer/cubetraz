package com.almagems.cubetraz.scenes.level;

import com.almagems.cubetraz.Game;
import com.almagems.cubetraz.GameOptions;
import com.almagems.cubetraz.GameProgress;
import com.almagems.cubetraz.graphics.Color;
import com.almagems.cubetraz.graphics.Graphics;
import com.almagems.cubetraz.graphics.Text;
import com.almagems.cubetraz.math.Vector2;
import com.almagems.cubetraz.scenes.Scene;

import static android.opengl.GLES10.*;
import static com.almagems.cubetraz.Game.*;


public final class Solvers extends Scene {

    private static int MAX_MENU_ITEMS = 4;

    private enum SolverMenuItemsEnum {
        MenuItemNone,
        MenuItemEarn,
        MenuItemSolve,
        MenuItemDismiss
    }

    private enum SolverState {
        Appear,
        Ready,
        Done,
        Disappear
    }

    private SolverMenuItemsEnum m_action;
    private SolverState mState;

    private Text m_text_title = new Text();
    private Text m_text_avail = new Text();

    private Text m_text_buy_pack_of = new Text();
    private Text m_text_buy_5_solver = new Text();

    private Text m_text_show_the_solution = new Text();
    private Text m_text_for_one_solver = new Text();
    private Text m_text_dismiss = new Text();

    private float[] m_y = new float[MAX_MENU_ITEMS];
    private float m_w;
    private float m_h;
    private float m_x;
    private float m_gap;

    private float m_alpha;

    private boolean mShowBuySome;
    private float m_timeout;
    private boolean[] mOver = new boolean[MAX_MENU_ITEMS];

    public Solvers() {
    }

    private void resetOverArray() {
        for (int i = 0; i < MAX_MENU_ITEMS; ++i) {
            mOver[i] = false;
        }
    }


    @Override
    public void init() {
        final float scale = Graphics.deviceScale;

        m_text_title.setScale(0.9f * scale, 0.9f * scale);
        m_text_avail.setScale(0.5f * scale, 0.5f * scale);

        m_text_buy_pack_of.setScale(0.5f * scale, 0.5f * scale);
        m_text_buy_5_solver.setScale(0.5f * scale, 0.5f * scale);

        m_text_show_the_solution.setScale(0.5f * scale, 0.5f * scale);
        m_text_for_one_solver.setScale(0.5f * scale, 0.5f * scale);
        m_text_dismiss.setScale(0.5f * scale, 0.5f * scale);

        m_text_title.setVSPace(0.9f);
        m_text_avail.setVSPace(0.8f);

        m_text_buy_pack_of.setVSPace(0.8f);
        m_text_buy_5_solver.setVSPace(0.8f);

        m_text_show_the_solution.setVSPace(0.8f);
        m_text_for_one_solver.setVSPace(0.8f);
        m_text_dismiss.setVSPace(0.8f);

        m_action = SolverMenuItemsEnum.MenuItemNone;
        mShowBuySome = false;
        mState = SolverState.Appear;
        m_alpha = 0.0f;

        m_text_title.init("SOLVERS");
        m_text_avail.init("AVAILABLE: " + GameOptions.getSolverCount());
        m_text_buy_pack_of.init("EARN");
        m_text_buy_5_solver.init("ONE SOLVER");
        m_text_show_the_solution.init("SHOW THE SOLUTION");
        m_text_for_one_solver.init("FOR ONE SOLVER");
        m_text_dismiss.init("DISMISS");

        final float h = Graphics.height;

        m_gap = 0.02f * h;

        m_y[0] = h * 0.8f + m_gap;
        m_y[1] = h * 0.5f + m_gap;
        m_y[2] = h * 0.3f + m_gap;
        m_y[3] = h * 0.1f + m_gap;

        m_w = 300.0f * scale;
        m_h = h / 5.5f;
        m_x = Graphics.halfWidth - (m_w / 2.0f);

        resetOverArray();
    }

    @Override
    public void update() {
        switch (mState) {
            case Appear:
                m_alpha += 0.075f;
                if (m_alpha >= 1.0f) {
                    m_alpha = 1.0f;
                    mState = SolverState.Ready;
                }
                break;

            case Disappear:
                m_alpha -= 0.2f;
                if (m_alpha <= 0.0f) {
                    m_alpha = 0.0f;
                    mState = SolverState.Done;
                }
                break;

            case Done:
                if (SolverMenuItemsEnum.MenuItemSolve == m_action) {
                    Game.levelInitData.initAction = LevelInitActionEnum.ShowSolution;
                    Game.showScene(Scene_Level);
                } else if (SolverMenuItemsEnum.MenuItemDismiss == m_action) {
                    Game.levelInitData.initAction = LevelInitActionEnum.JustContinue;
                    Game.showScene(Scene_Level);
                }
                break;

            case Ready:
                if (mShowBuySome) {
                    m_timeout -= 0.3f;
                    if (m_timeout <= 0.0f) {
                        mShowBuySome = false;
                        m_text_show_the_solution.init("SHOW THE SOLUTION");
                        m_text_for_one_solver.init("FOR ONE SOLVER");
                    }
                }
                break;
        }
    }

    private void drawText(Color color, Color colorHi) {
        Vector2 pos = new Vector2();
        float halfWidth = Graphics.halfWidth;

        pos.x = halfWidth - m_text_title.getHalfWidth();
        pos.y = m_y[0] + m_h * 0.2f + m_gap;
        m_text_title.emit(pos, color);

        pos.x = halfWidth - m_text_avail.getHalfWidth();
        pos.y = m_y[0] + m_gap;
        m_text_avail.emit(pos, color);

        pos.x = halfWidth - m_text_show_the_solution.getHalfWidth();
        pos.y = m_y[1] + m_h * 0.3f + m_gap;
        m_text_show_the_solution.emit(pos, colorHi);

        pos.x = halfWidth - m_text_for_one_solver.getHalfWidth();
        pos.y = m_y[1] - m_h * 0.01f + m_gap;
        m_text_for_one_solver.emit(pos, colorHi);

        pos.x = halfWidth - m_text_buy_pack_of.getHalfWidth();
        pos.y = m_y[2] + m_h * 0.3f + m_gap;
        m_text_buy_pack_of.emit(pos, color);

        pos.x = halfWidth - m_text_buy_5_solver.getHalfWidth();
        pos.y = m_y[2] - m_h * 0.01f + m_gap;
        m_text_buy_5_solver.emit(pos, color);

        pos.x = halfWidth - m_text_dismiss.getHalfWidth();
        pos.y = m_y[3] + m_h * 0.18f + m_gap;
        m_text_dismiss.emit(pos, color);
    }

    @Override
    public void render() {
        if (Game.fboLost) {
            Game.renderToFBO(Game.level);
        }

        Color color;
        Color colorHilite;

        glDisable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);
        glDisable(GL_LIGHTING);
        glDisable(GL_LIGHT0);

        Graphics.setProjection2D();
        Graphics.setModelViewMatrix2D();
        Graphics.bindStreamSources2d();

        Graphics.resetBufferIndices();
        Graphics.zeroBufferPositions();

        glEnable(GL_TEXTURE_2D);
        Graphics.drawFullScreenTexture(Graphics.fbo, Color.WHITE);

        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);

        color = new Color(0, 0, 0, 100 * (int)m_alpha);
        Graphics.drawFullScreenQuad(color);

        Graphics.setProjection2D();
        Graphics.setModelViewMatrix2D();

        color = new Color(47, 79, 79, 100 * (int)m_alpha);
        colorHilite = new Color(50, 80, 80, 200 * (int)m_alpha);

        float w_title = Graphics.width;
        float x_title = 0.0f;
        float x = m_x;

        Graphics.addQuad(x_title, m_y[0], w_title, m_h, color);
        Graphics.addQuad(x, m_y[1], m_w, m_h, mOver[1] ? colorHilite : color);
        Graphics.addQuad(x, m_y[2], m_w, m_h, mOver[2] ? colorHilite : color);
        Graphics.addQuad(x, m_y[3], m_w, m_h, mOver[3] ? colorHilite : color);
        Graphics.updateBuffers();
        Graphics.renderTriangles();

        // texts
        Graphics.resetBufferIndices();
        Graphics.zeroBufferPositions();

        glEnable(GL_TEXTURE_2D);
        Graphics.textureFontsBig.bind();

        Color colorShadow = new Color(0, 0, 0, 255 * (int)m_alpha);
        drawText(colorShadow, colorShadow);

        glPushMatrix();
        glTranslatef(Graphics.deviceScale, Graphics.deviceScale, 0.0f);
        Graphics.updateBuffers();
        Graphics.renderTriangles();
        glPopMatrix();

        color = new Color(210, 210, 210, 240 * (int)m_alpha);

        if (mShowBuySome) {
            colorHilite = new Color(225, 10, 50, 255 * (int)m_alpha);
        } else {
            colorHilite = new Color(255, 255, 0, 255 * (int)m_alpha);
        }

        drawText(color, colorHilite);
        Graphics.updateBuffers();
        Graphics.renderTriangles();
    }

    public void onFingerDown(float x, float y, int fingerCount) {
        if (1 == fingerCount) {
            mPosDown.x = x;
            mPosDown.y = y;

            resetOverArray();

            SolverMenuItemsEnum index = getMenuItem(x, y);
            switch (index) {
                case MenuItemEarn:
                    mOver[2] = true;
                    break;

                case MenuItemSolve:
                    mOver[1] = true;
                    break;

                case MenuItemDismiss:
                    mOver[3] = true;
                    break;
            }
        }
    }

    public void onFingerUp(float x, float y, int fingerCount) {
        if (1 != fingerCount) {
            return;
        }

        resetOverArray();

        SolverMenuItemsEnum index_down = getMenuItem(mPosDown.x, mPosDown.y);
        SolverMenuItemsEnum index_up = getMenuItem(x, y);

        if (index_down == index_up) {
            switch (index_down) {
                case MenuItemEarn:
                    if (Game.adReady) {
                        Game.showAd();
                    } else {
                        Game.showAdNotReady();
                    }
                    break;

                case MenuItemSolve:
                    int solverCount = GameOptions.getSolverCount();
                    if (solverCount > 0) {
                        m_action = SolverMenuItemsEnum.MenuItemSolve;
                        mState = SolverState.Disappear;
                        int levelNumber = Game.level.getLevelNumber();
                        GameOptions.setSolverCount(solverCount - 1);
                        Game.updateDisplayedSolvers();

                        switch(Game.level.getDifficulty()) {
                            case Easy:
                                GameProgress.setSolvedEasy(levelNumber, true);
                                break;

                            case Normal:
                                GameProgress.setSolvedNormal(levelNumber, true);
                                break;

                            case Hard:
                                GameProgress.setSolvedHard(levelNumber, true);
                                break;
                        }
                        GameProgress.save();
                    } else {
                        if (!mShowBuySome) {
                            m_text_show_the_solution.init("NO AVAILABLE SOLVERS");
                            m_text_for_one_solver.init("EARN SOME FROM BELOW");
                            m_timeout = 50.0f;
                            mShowBuySome = true;
                        }
                    }
                    break;

                case MenuItemDismiss:
                    m_action = SolverMenuItemsEnum.MenuItemDismiss;
                    mState = SolverState.Disappear;
                    break;
            }
        }
    }

    private SolverMenuItemsEnum getMenuItem(float x, float y) {
        if (x > m_x && x < m_x + m_w) {
            float y_gl = Graphics.height - y;

            if (y_gl > m_y[3] && y_gl < m_y[3] + m_h) {
                return SolverMenuItemsEnum.MenuItemDismiss;
            }

            if (y_gl > m_y[2] && y_gl < m_y[2] + m_h) {
                return SolverMenuItemsEnum.MenuItemEarn;
            }

            if (y_gl > m_y[1] && y_gl < m_y[1] + m_h) {
                return SolverMenuItemsEnum.MenuItemSolve;
            }
        }
        return SolverMenuItemsEnum.MenuItemNone;
    }

    public void updateSolversCount(int solver) {
        m_text_avail.init("AVAILABLE: " + solver);
    }

}

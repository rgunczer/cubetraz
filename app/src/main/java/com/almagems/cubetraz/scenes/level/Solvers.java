package com.almagems.cubetraz.scenes.level;

import com.almagems.cubetraz.Game;
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

    private enum SolverStateEnum {
        SolverAppear,
        SolverReady,
        SolverDone,
        SolverDisappear
    }

    private SolverMenuItemsEnum m_action;
    private SolverStateEnum mState;

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
        final float scale = Game.graphics.deviceScale;

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
        mState = SolverStateEnum.SolverAppear;
        m_alpha = 0.0f;

        m_text_title.init("SOLVERS");
        m_text_avail.init("AVAILABLE: " + Game.options.getSolverCount());
        m_text_buy_pack_of.init("EARN");
        m_text_buy_5_solver.init("ONE SOLVER");
        m_text_show_the_solution.init("SHOW THE SOLUTION");
        m_text_for_one_solver.init("FOR ONE SOLVER");
        m_text_dismiss.init("DISMISS");

        final float h = Game.graphics.height;

        m_gap = 0.02f * h;

        m_y[0] = h * 0.8f + m_gap;
        m_y[1] = h * 0.5f + m_gap;
        m_y[2] = h * 0.3f + m_gap;
        m_y[3] = h * 0.1f + m_gap;

        m_w = 300.0f * scale;
        m_h = h / 5.5f;
        m_x = Game.graphics.halfWidth - (m_w / 2.0f);

        resetOverArray();
    }

    @Override
    public void update() {
        switch (mState) {
            case SolverAppear:
                m_alpha += 0.075f;
                if (m_alpha >= 1.0f) {
                    m_alpha = 1.0f;
                    mState = SolverStateEnum.SolverReady;
                }
                break;

            case SolverDisappear:
                m_alpha -= 0.2f;
                if (m_alpha <= 0.0f) {
                    m_alpha = 0.0f;
                    mState = SolverStateEnum.SolverDone;
                }
                break;

            case SolverDone:
                if (SolverMenuItemsEnum.MenuItemSolve == m_action) {
                    Game.levelInitData.initAction = LevelInitActionEnum.ShowSolution;
                    Game.showScene(Scene_Level);
                } else if (SolverMenuItemsEnum.MenuItemDismiss == m_action) {
                    Game.levelInitData.initAction = LevelInitActionEnum.JustContinue;
                    Game.showScene(Scene_Level);
                }
                break;

            case SolverReady:
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
        float halfWidth = Game.graphics.halfWidth;

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
        Graphics graphics = Game.graphics;

        glDisable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);
        glDisable(GL_LIGHTING);
        glDisable(GL_LIGHT0);

        graphics.setProjection2D();
        graphics.setModelViewMatrix2D();
        graphics.bindStreamSources2d();

        graphics.resetBufferIndices();
        graphics.zeroBufferPositions();

        glEnable(GL_TEXTURE_2D);
        graphics.drawFullScreenTexture(graphics.fbo, Color.WHITE);

        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);

        color = new Color(0, 0, 0, 100 * (int)m_alpha);
        graphics.drawFullScreenQuad(color);

        graphics.setProjection2D();
        graphics.setModelViewMatrix2D();

        color = new Color(47, 79, 79, 100 * (int)m_alpha);
        colorHilite = new Color(50, 80, 80, 200 * (int)m_alpha);

        float w_title = graphics.width;
        float x_title = 0.0f;
        float x = m_x;

        graphics.addQuad(x_title, m_y[0], w_title, m_h, color);
        graphics.addQuad(x, m_y[1], m_w, m_h, mOver[1] ? colorHilite : color);
        graphics.addQuad(x, m_y[2], m_w, m_h, mOver[2] ? colorHilite : color);
        graphics.addQuad(x, m_y[3], m_w, m_h, mOver[3] ? colorHilite : color);
        graphics.updateBuffers();
        graphics.renderTriangles();

        // texts
        graphics.resetBufferIndices();
        graphics.zeroBufferPositions();

        glEnable(GL_TEXTURE_2D);
        graphics.textureFontsBig.bind();

        Color colorShadow = new Color(0, 0, 0, 255 * (int)m_alpha);
        drawText(colorShadow, colorShadow);

        glPushMatrix();
        glTranslatef(graphics.deviceScale, graphics.deviceScale, 0.0f);
        graphics.updateBuffers();
        graphics.renderTriangles();
        glPopMatrix();

        color = new Color(210, 210, 210, 240 * (int)m_alpha);

        if (mShowBuySome) {
            colorHilite = new Color(225, 10, 50, 255 * (int)m_alpha);
        } else {
            colorHilite = new Color(255, 255, 0, 255 * (int)m_alpha);
        }

        drawText(color, colorHilite);
        graphics.updateBuffers();
        graphics.renderTriangles();
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
                    //engine->BuyPackOfSolvers(5)
                    Game.showAd();
                    break;

                case MenuItemSolve:
                    int solverCount = Game.options.getSolverCount();
                    if (solverCount > 0) {
                        m_action = SolverMenuItemsEnum.MenuItemSolve;
                        mState = SolverStateEnum.SolverDisappear;
                        int levelNumber = Game.level.getLevelNumber();
                        Game.options.setSolverCount(solverCount - 1);
                        Game.updateDisplayedSolvers();

                        switch(Game.level.getDifficulty()) {
                            case Easy:
                                Game.progress.setSolvedEasy(levelNumber, true);
                                break;

                            case Normal:
                                Game.progress.setSolvedNormal(levelNumber, true);
                                break;

                            case Hard:
                                Game.progress.setSolvedHard(levelNumber, true);
                                break;
                        }
                        Game.progress.save();
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
                    mState = SolverStateEnum.SolverDisappear;
                    break;
            }
        }
    }

    private SolverMenuItemsEnum getMenuItem(float x, float y) {
        if (x > m_x && x < m_x + m_w) {
            float y_gl = Game.graphics.height - y;

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

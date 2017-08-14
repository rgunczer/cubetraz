package com.almagems.cubetraz.scenes.level;

import com.almagems.cubetraz.game.Engine;
import com.almagems.cubetraz.game.Game;
import com.almagems.cubetraz.graphics.Color;
import com.almagems.cubetraz.graphics.Graphics;
import com.almagems.cubetraz.graphics.Text;
import com.almagems.cubetraz.math.Vector2;
import com.almagems.cubetraz.scenes.Scene;

import static android.opengl.GLES10.*;
import static com.almagems.cubetraz.game.Constants.*;


public final class Solvers extends Scene {

    private static int MAX_SOLVER_MENU_ITEMS = 5;

    private enum SolverMenuItemsEnum {
        MenuItemNull,
        MenuItemBuy5,
        MenuItemBuy15,
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
    private SolverStateEnum m_state;

    private Text m_text_title = new Text();
    private Text m_text_avail = new Text();

    private Text m_text_buy_pack_of = new Text();
    private Text m_text_buy_5_solver = new Text();
    private Text m_text_buy_15_solver = new Text();

    private Text m_text_show_the_solution = new Text();
    private Text m_text_for_one_solver = new Text();
    private Text m_text_dismiss = new Text();

    private float[] m_y = new float[5];
    private float m_w;
    private float m_h;
    private float m_x;
    private float m_gap;

    private float m_alpha;

    private boolean m_show_buy_some;
    private float m_timeout;

    private boolean[] m_ar_over = new boolean[MAX_SOLVER_MENU_ITEMS];

    
    public Solvers() {
    }

    @Override
    public void init() {
        final float scale = Engine.graphics.deviceScale;

        m_text_title.setScale(0.9f * scale, 0.9f * scale);
        m_text_avail.setScale(0.5f * scale, 0.5f * scale);

        m_text_buy_pack_of.setScale(0.5f * scale, 0.5f * scale);
        m_text_buy_5_solver.setScale(0.5f * scale, 0.5f * scale);
        m_text_buy_15_solver.setScale(0.5f * scale, 0.5f * scale);

        m_text_show_the_solution.setScale(0.5f * scale, 0.5f * scale);
        m_text_for_one_solver.setScale(0.5f * scale, 0.5f * scale);
        m_text_dismiss.setScale(0.5f * scale, 0.5f * scale);

        m_text_title.setVSPace(0.9f);
        m_text_avail.setVSPace(0.8f);

        m_text_buy_pack_of.setVSPace(0.8f);
        m_text_buy_5_solver.setVSPace(0.8f);
        m_text_buy_15_solver.setVSPace(0.8f);

        m_text_show_the_solution.setVSPace(0.8f);
        m_text_for_one_solver.setVSPace(0.8f);
        m_text_dismiss.setVSPace(0.8f);

        m_action = SolverMenuItemsEnum.MenuItemNull;
        m_show_buy_some = false;
        m_state = SolverStateEnum.SolverAppear;
        m_alpha = 0.0f;

        m_text_title.init("SOLVERS", true);

        String str = "AVAILABLE: " + Game.getSolverCount();

        m_text_avail.init(str, true);

        m_text_buy_pack_of.init("BUY PACK OF", true);
        m_text_buy_5_solver.init("5 SOLVERS", true);
        m_text_buy_15_solver.init("15 SOLVERS", true);

        m_text_show_the_solution.init("SHOW THE SOLUTION", true);
        m_text_for_one_solver.init("FOR ONE SOLVER", true);
        m_text_dismiss.init("DISMISS", true);

        m_gap = 0.01f * Engine.graphics.height;

        m_y[0] = Engine.graphics.height * 0.8f + m_gap;
        m_y[1] = Engine.graphics.height * 0.6f + m_gap;
        m_y[2] = Engine.graphics.height * 0.4f + m_gap;
        m_y[3] = Engine.graphics.height * 0.2f + m_gap;
        m_y[4] = Engine.graphics.height * 0.0f + m_gap;

        m_w = 300.0f * scale;
        m_h = Engine.graphics.height / 5.5f;
        m_x = Engine.graphics.halfWidth - (m_w / 2.0f);

        for(int i = 0; i < MAX_SOLVER_MENU_ITEMS; ++i) {
            m_ar_over[i] = false;
        }
    }

    @Override
    public void update() {
        switch (m_state) {
            case SolverAppear:
                m_alpha += 0.075f;
                if (m_alpha >= 1.0f) {
                    m_alpha = 1.0f;
                    m_state = SolverStateEnum.SolverReady;
                }
                break;

            case SolverDisappear:
                m_alpha -= 0.2f;
                if (m_alpha <= 0.0f) {
                    m_alpha = 0.0f;
                    m_state = SolverStateEnum.SolverDone;
                }
                break;

            case SolverDone:
                if (SolverMenuItemsEnum.MenuItemSolve == m_action) {
                    Game.level_init_data.init_action = LevelInitActionEnum.ShowSolution;
                    Game.showScene(Scene_Level);
                } else if (SolverMenuItemsEnum.MenuItemDismiss == m_action) {
                    Game.level_init_data.init_action = LevelInitActionEnum.JustContinue;
                    Game.showScene(Scene_Level);
                }
                break;

            case SolverReady:
                if (m_show_buy_some) {
                    m_timeout -= 0.3f;
                    if (m_timeout <= 0.0f) {
                        m_show_buy_some = false;
                        m_text_show_the_solution.init("SHOW THE SOLUTION", true);
                        m_text_for_one_solver.init("FOR ONE SOLVER", true);
                    }
                }
                break;
        }
    }

    public void emit(Color color, Color color_hi) {
        float x;
        float halfWidth = Engine.graphics.halfWidth;

        x = halfWidth -  m_text_title.getHalfWidth();
        m_text_title.emitt(new Vector2(x, m_y[0] + m_h * 0.3f + m_gap), color);

        x = halfWidth - m_text_avail.getHalfWidth();
        m_text_avail.emitt(new Vector2(x, m_y[0] + m_gap), color);

        x = halfWidth - m_text_show_the_solution.getHalfWidth();
        m_text_show_the_solution.emitt(new Vector2(x, m_y[1] + m_h * 0.4f + m_gap), color_hi);

        x = halfWidth - m_text_for_one_solver.getHalfWidth();
        m_text_for_one_solver.emitt(new Vector2(x, m_y[1] + m_h * 0.1f + m_gap), color_hi);

        x = halfWidth - m_text_buy_pack_of.getHalfWidth();
        m_text_buy_pack_of.emitt(new Vector2(x, m_y[2] + m_h * 0.4f + m_gap), color);

        x = halfWidth - m_text_buy_5_solver.getHalfWidth();
        m_text_buy_5_solver.emitt(new Vector2(x, m_y[2] + m_h * 0.1f + m_gap), color);

        x = halfWidth - m_text_buy_pack_of.getHalfWidth();
        m_text_buy_pack_of.emitt(new Vector2(x, m_y[3] + m_h * 0.4f + m_gap), color);

        x = halfWidth - m_text_buy_15_solver.getHalfWidth();
        m_text_buy_15_solver.emitt(new Vector2(x, m_y[3] + m_h * 0.1f + m_gap), color);

        x = halfWidth - m_text_dismiss.getHalfWidth();
        m_text_dismiss.emitt(new Vector2(x, m_y[4] + m_h * 0.25f + m_gap), color);
    }

    @Override
    public void render() {
        Color color;
        Color colorHilite;
        Graphics graphics = Engine.graphics;

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
        graphics.addQuad(x, m_y[1], m_w, m_h, m_ar_over[3] ? colorHilite : color);
        graphics.addQuad(x, m_y[2], m_w, m_h, m_ar_over[1] ? colorHilite : color);
        graphics.addQuad(x, m_y[3], m_w, m_h, m_ar_over[2] ? colorHilite : color);
        graphics.addQuad(x, m_y[4], m_w, m_h, m_ar_over[4] ? colorHilite : color);
        graphics.updateBuffers();
        graphics.renderTriangles();

        // texts
        graphics.resetBufferIndices();
        graphics.zeroBufferPositions();

        glEnable(GL_TEXTURE_2D);
        graphics.textureFontsBig.bind();

        Color colorShadow = new Color(0, 0, 0, 255 * (int)m_alpha);
        emit(colorShadow, colorShadow);

        glPushMatrix();
        glTranslatef(graphics.deviceScale, graphics.deviceScale, 0.0f);
        graphics.updateBuffers();
        graphics.renderTriangles();
        glPopMatrix();

        color = new Color(210, 210, 210, 240 * (int)m_alpha);

        if (m_show_buy_some) {
            colorHilite = new Color(225, 10, 50, 255 * (int)m_alpha);
        } else {
            colorHilite = new Color(255, 255, 0, 255 * (int)m_alpha);
        }

        emit(color, colorHilite);
        graphics.updateBuffers();
        graphics.renderTriangles();
    }

    public void onFingerDown(float x, float y, int fingerCount) {
        if (1 == fingerCount) {
            mPosDown.x = x;
            mPosDown.y = y;

            for (int i = 0; i < MAX_SOLVER_MENU_ITEMS; ++i) {
                m_ar_over[i] = false;
            }

            SolverMenuItemsEnum index = getMenuItem(x, y);
            switch (index) {
                case MenuItemNull:
                    //m_ar_over[0] = true;
                    break;

                case MenuItemBuy5:
                    m_ar_over[1] = true;
                    break;

                case MenuItemBuy15:
                    m_ar_over[2] = true;
                    break;

                case MenuItemSolve:
                    m_ar_over[3] = true;
                    break;

                case MenuItemDismiss:
                    m_ar_over[4] = true;
                    break;
            }
        }
    }

    public void onFingerUp(float x, float y, int fingerCount) {
        if (1 != fingerCount) {
            return;
        }

        for (int i = 0; i < MAX_SOLVER_MENU_ITEMS; ++i) {
            m_ar_over[i] = false;
        }

        SolverMenuItemsEnum index_down = getMenuItem(mPosDown.x, mPosDown.y);
        SolverMenuItemsEnum index_up = getMenuItem(x, y);

        if (index_down == index_up) {
            switch (index_down) {
                case MenuItemNull:
                    break;

                case MenuItemBuy5:
                    //engine->BuyPackOfSolvers(5);
                    break;

                case MenuItemBuy15:
                    //engine->BuyPackOfSolvers(15);
                    break;

                case MenuItemSolve:
                    if (Game.getSolverCount() > 0) {
                        m_action = SolverMenuItemsEnum.MenuItemSolve;
                        m_state = SolverStateEnum.SolverDisappear;
                        int levelNumber = Game.level.getLevelNumber();
                        Game.decSolverCount();

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
                        m_text_show_the_solution.init("NO AVAILABLE", true);
                        m_text_for_one_solver.init("SOLVERS", true);
                        m_timeout = 5.0f;
                        m_show_buy_some = true;

                        m_text_show_the_solution.init("NO AVAILABLE SOLVERS", true);
                        m_text_for_one_solver.init("BUY SOME FROM BELOW", true);
                        m_timeout = 5.0f;
                        m_show_buy_some = true;
                    }
                    break;

                case MenuItemDismiss:
                    m_action = SolverMenuItemsEnum.MenuItemDismiss;
                    m_state = SolverStateEnum.SolverDisappear;
                    break;
            }
        }
    }

    private SolverMenuItemsEnum getMenuItem(float x, float y) {
        if (x > m_x && x < m_x + m_w) {
            float y_gl = Engine.graphics.height - y;

            if (y_gl > m_y[4] && y_gl < m_y[4] + m_h) {
                return SolverMenuItemsEnum.MenuItemDismiss;
            }

            if (y_gl > m_y[3] && y_gl < m_y[3] + m_h) {
                return SolverMenuItemsEnum.MenuItemBuy15;
            }

            if (y_gl > m_y[2] && y_gl < m_y[2] + m_h) {
                return SolverMenuItemsEnum.MenuItemBuy5;
            }

            if (y_gl > m_y[1] && y_gl < m_y[1] + m_h) {
                return SolverMenuItemsEnum.MenuItemSolve;
            }
        }
        return SolverMenuItemsEnum.MenuItemNull;
    }

    public void updateSolversCount() {
        String str = "AVAILABLE: " + Game.getSolverCount();
        m_text_avail.init(str, true);
    }

}

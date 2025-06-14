package com.almagems.cubetraz.scenes;

import com.almagems.cubetraz.Audio;
import com.almagems.cubetraz.GameOptions;
import com.almagems.cubetraz.graphics.Camera;
import com.almagems.cubetraz.cubes.Cube;
import com.almagems.cubetraz.Game;
import com.almagems.cubetraz.graphics.Graphics;
import com.almagems.cubetraz.utils.Starfield;
import com.almagems.cubetraz.math.Utils;
import com.almagems.cubetraz.math.Vector;
import com.almagems.cubetraz.graphics.Color;

import static android.opengl.GLES10.*;

import java.util.ArrayList;

import static com.almagems.cubetraz.Audio.*;
import static com.almagems.cubetraz.Game.*;


public final class Intro extends Scene {

    private enum State {
        AppearStarfield,
        ShowStarfield,
        BuildCubetraz,
        FinalizeBuild,
        BuildCubetrazFace,
    }

    private State mState;
    private Starfield mStarfield = new Starfield();

    private Camera mCameraBegin = new Camera();
    private Camera mCameraEnd = new Camera();

    private float m_t_camera;

    private float m_degree;
    private int m_build_phase;
    private int m_build_to;
    private boolean mCanSkipIntro;

    private float m_offset_y;
    private float m_stars_alpha;

    private ArrayList<Cube> mCubesBase = new ArrayList<>();
    private ArrayList<Cube> mCubesFace = new ArrayList<>();


    public Intro() {
        m_build_phase = 0;
        m_build_to = 1;
        mTick = 0;
        m_offset_y = 10.0f;
        m_stars_alpha = 1.0f;
    }

    private void setupCameras() {
        mCameraBegin.setEye(0.0f, 0.0f, 40.0f / 1.5f);
        mCameraBegin.setTarget(0.0f, 0.0f, 0.0f);

        mCameraEnd.setEye(0.0f, 0.0f, 35.0f / 1.5f);
        mCameraEnd.setTarget(0.0f, 0.0f, 0.0f);

        mCameraBegin.eye.scaled(Graphics.aspectRatio);
        mCameraEnd.eye.scaled(Graphics.aspectRatio);

        mCameraCurrent.setEye(mCameraBegin.eye);
        mCameraCurrent.setTarget(mCameraBegin.target);
    }

    @Override
    public void init() {
        setupCameras();
        mStarfield.speed = 0.2f;
        m_degree = 45.0f;
        mPosLightCurrent = new Vector(-10.0f, 3.0f, 12.0f);
        m_t_camera = 0.0f;
        m_stars_alpha = 0.0f;

        mStarfield.create();

        for (int i = 0; i < 400; ++i) {
            mStarfield.update();
        }

        mStarfield.alpha = m_stars_alpha;

        setupCubetraz();
        
        mCanSkipIntro = GameOptions.getCanSkipIntro();
        mState = State.AppearStarfield;
    }

    private Cube setCubeVisible(int x, int y, int z) {
        Cube cube = Game.cubes[x][y][z];
        cube.type = CubeTypeEnum.CubeIsVisibleAndObstacle;
        return cube;
    }

    private void setCubeInvisible(int x, int y, int z) {
        Game.cubes[x][y][z].type = CubeTypeEnum.CubeIsInvisible;
    }

    private void setCubeVisibleAndBuild(int x, int y, int z) {
        Cube cube = setCubeVisible(x, y, z);

        if (State.BuildCubetrazFace == mState)
            mCubesFace.add(cube);
        else
            mCubesBase.add(cube);
    }

    private void setupCubetraz() {
        Game.setupHollowCube();

        for (int x = 0; x < MAX_CUBE_COUNT; ++x) {
            for (int y = 0; y < MAX_CUBE_COUNT; ++y) {
                for (int z = 0; z < MAX_CUBE_COUNT; ++z) {
                    if ((x == 0 || x == MAX_CUBE_COUNT - 1) ||
                            (y == 0 || y == MAX_CUBE_COUNT - 1) ||
                            (z == 0 || z == MAX_CUBE_COUNT - 1)) {
                        Game.cubes[x][y][z].type = CubeTypeEnum.CubeIsInvisible;
                    }

                    if ((x == 1 || x == MAX_CUBE_COUNT - 2) ||
                            (y == 1 || y == MAX_CUBE_COUNT - 2) ||
                            (z == 1 || z == MAX_CUBE_COUNT - 2)) {
                        Game.cubes[x][y][z].type = CubeTypeEnum.CubeIsInvisible;
                    }
                }
            }
        }
    }

    private void builCubetraz() {
        switch (m_build_phase) {
            case 0: {
                switch (m_build_to) {
                    case 1:
                        setCubeVisible(1, 7, 7);
                        break;
                    case 2:
                        setCubeVisible(7, 7, 1);
                        break;
                    case 3:
                        setCubeVisible(7, 1, 7);
                        break;
                    case 4:
                        setCubeVisible(1, 1, 1);
                        break;
                }

                if (5 == m_build_to) {
                    m_build_phase = 1;
                    m_build_to = 2;
                } else {
                    ++m_build_to;
                }
            }
            break;

            case 1: {
                int x, y, z;

                x = 1;
                y = 7;
                z = 7;
                for (int i = 0; i < m_build_to; ++i) {
                    setCubeVisible(1, y, 7);
                    --y;

                    setCubeVisible(x, 7, 7);
                    ++x;

                    setCubeVisible(1, 7, z);
                    --z;
                }

                x = 7;
                y = 7;
                z = 1;
                for (int i = 0; i < m_build_to; ++i) {
                    setCubeVisible(7, y, 1);
                    --y;

                    setCubeVisible(x, 7, 1);
                    --x;

                    setCubeVisible(7, 7, z);
                    ++z;
                }

                x = 7;
                y = 1;
                z = 7;
                for (int i = 0; i < m_build_to; ++i) {
                    setCubeVisible(7, y, 7);
                    ++y;

                    setCubeVisible(7, 1, z);
                    --z;

                    setCubeVisible(x, 1, 7);
                    --x;
                }

                x = 1;
                y = 1;
                z = 1;
                for (int i = 0; i < m_build_to; ++i) {
                    setCubeVisible(x, 1, 1);
                    ++x;

                    setCubeVisible(1, y, 1);
                    ++y;

                    setCubeVisible(1, 1, z);
                    ++z;
                }

                if (7 == m_build_to) {
                    m_build_phase = 2;
                    m_build_to = 1;
                } else {
                    ++m_build_to;
                }
            }
            break;

            case 2: {
                int y = 7;
                for (int i = 0; i < m_build_to; ++i) {
                    setCubeVisible(3, y, 7);
                    setCubeVisible(5, y, 7);

                    setCubeVisible(3, y, 1);
                    setCubeVisible(5, y, 1);

                    setCubeVisible(1, y, 3);
                    setCubeVisible(1, y, 5);

                    setCubeVisible(7, y, 3);
                    setCubeVisible(7, y, 5);
                    --y;
                }

                if (7 == m_build_to) {
                    m_build_phase = 3;
                    m_build_to = 1;
                } else {
                    ++m_build_to;
                }
            }
            break;

            case 3: {
                int x = 1;
                for (int i = 0; i < m_build_to; ++i) {
                    setCubeVisible(x, 7, 3);
                    setCubeVisible(x, 7, 5);

                    setCubeVisible(x, 1, 3);
                    setCubeVisible(x, 1, 5);

                    ++x;
                }

                if (7 == m_build_to) {
                    m_build_phase = 4;
                    m_build_to = 1;
                } else {
                    ++m_build_to;
                }
            }
            break;
        } // switch

        Game.buildVisibleCubesList(mCubesBase);
    }

    @Override
    public void update() {
        //System.out.println("Intro.update...");

        if (mState != State.AppearStarfield) {
            ++mTick;
        }
        //printf("\nCounter: %d", mTick);

        switch (mState) {
            case AppearStarfield:
                m_stars_alpha += 0.025f;

//                if (m_dirty_alpha > engine->dirtyAlpha) {
//                    m_dirty_alpha = engine -> dirtyAlpha;
//                }

                if (m_stars_alpha >= 1.0f) {
                    m_stars_alpha = 1.0f;
                    mState = State.ShowStarfield;

                    //m_dirty_alpha = engine->dirtyAlpha;
                }
                //mStarfield.alpha = m_stars_alpha;
                break;

            case ShowStarfield:
                if (mTick > 200) {
                    mTick = 400;
                    mState = State.BuildCubetraz;
                    m_offset_y = 0.0f;
                    Audio.playMusic(MUSIC_VECTORS);
                }
                break;

            case BuildCubetraz: {
                mStarfield.speed += 0.0003f;

                switch (mTick) {
                    // build four corner cubes
                    case 600:
                        builCubetraz();
                        break;
                    case 640:
                        builCubetraz();
                        break;
                    case 680:
                        builCubetraz();
                        break;
                    case 720:
                        builCubetraz();
                        break;

                    // build cube frame
                    case 900:
                        builCubetraz();
                        break;
                    case 903:
                        builCubetraz();
                        break;
                    case 906:
                        builCubetraz();
                        break;
                    case 909:
                        builCubetraz();
                        break;
                    case 912:
                        builCubetraz();
                        break;
                    case 915:
                        builCubetraz();
                        break;
                    case 918:
                        builCubetraz();
                        break;

                    case 1010:
                        builCubetraz();
                        break;
                    case 1015:
                        builCubetraz();
                        break;
                    case 1020:
                        builCubetraz();
                        break;
                    case 1025:
                        builCubetraz();
                        break;
                    case 1030:
                        builCubetraz();
                        break;
                    case 1035:
                        builCubetraz();
                        break;

                    case 1090:
                        builCubetraz();
                        break;
                    case 1100:
                        builCubetraz();
                        break;
                    case 1110:
                        builCubetraz();
                        break;
                    case 1120:
                        builCubetraz();
                        break;
                    case 1130:
                        builCubetraz();
                        break;
                    case 1140:
                        builCubetraz();
                        break;
                    case 1150:
                        builCubetraz();
                        break;
                    case 1160:
                        builCubetraz();
                        break;
                } // switch

                m_degree += 1.5f;

                if (m_degree > 360.0f) {
                    m_degree = 0.0f;

                    if (4 == m_build_phase) {
                        mState = State.FinalizeBuild;
                        mTick = 0;
                    }
                }
            }
            break;

            case FinalizeBuild:
                switch (mTick) {
                    case 30:
                        setCubeVisibleAndBuild(2, 6, 7);
                        break;
                    case 32:
                        setCubeVisibleAndBuild(2, 5, 7);
                        break;
                    case 34:
                        setCubeVisibleAndBuild(2, 4, 7);
                        break;
                    case 36:
                        setCubeVisibleAndBuild(2, 3, 7);
                        break;
                    case 38:
                        setCubeVisibleAndBuild(2, 2, 7);
                        break;

                    case 40:
                        setCubeVisibleAndBuild(6, 2, 7);
                        break;
                    case 42:
                        setCubeVisibleAndBuild(6, 3, 7);
                        break;
                    case 44:
                        setCubeVisibleAndBuild(6, 4, 7);
                        break;
                    case 46:
                        setCubeVisibleAndBuild(6, 5, 7);
                        break;
                    case 48:
                        setCubeVisibleAndBuild(6, 6, 7);
                        break;

                    case 50:
                        setCubeVisibleAndBuild(4, 6, 7);
                        break;
                    case 52:
                        setCubeVisibleAndBuild(4, 2, 7);
                        break;
                    case 54:
                        setCubeVisibleAndBuild(4, 5, 7);
                        break;
                    case 56:
                        setCubeVisibleAndBuild(4, 3, 7);
                        break;

                    case 60:
                        setCubeVisibleAndBuild(4, 4, 7);
                        mState = State.BuildCubetrazFace;
                        Audio.stopMusic();
                        break;
                } // switch
                break;

            case BuildCubetrazFace:
                Game.dirtyAlpha += 2f;

                if (Game.dirtyAlpha > DIRTY_ALPHA) {
                    Game.dirtyAlpha = DIRTY_ALPHA;
                }

                m_stars_alpha -= 0.012f;
                if (m_stars_alpha < 0.0f) {
                    m_stars_alpha = 0.0f;
                }

                m_t_camera += 0.03f;

                if (m_t_camera > 1.0f) {
                    m_t_camera = 1.0f;
                }

                Utils.lerpCamera(mCameraBegin, mCameraEnd, m_t_camera, mCameraCurrent);

                switch (mTick) {
                    case 90:
                        setCubeVisibleAndBuild(8, 7, 5);
                        break;
                    case 91:
                        setCubeVisibleAndBuild(8, 8, 5);
                        break;
                    case 92:
                        setCubeVisibleAndBuild(8, 8, 6);
                        break;
                    case 93:
                        setCubeVisibleAndBuild(8, 8, 7);
                        break;

                    case 94:
                        setCubeVisibleAndBuild(8, 8, 8);
                        break;
                    case 95:
                        setCubeVisibleAndBuild(7, 8, 8);
                        break;
                    case 96:
                        setCubeVisibleAndBuild(6, 8, 8);
                        break;
                    case 97:
                        setCubeVisibleAndBuild(5, 8, 8);
                        break;
                    case 98:
                        setCubeVisibleAndBuild(4, 8, 8);
                        break;
                    case 99:
                        setCubeVisibleAndBuild(3, 8, 8);
                        break;
                    case 100:
                        setCubeVisibleAndBuild(2, 8, 8);
                        break;
                    case 101:
                        setCubeVisibleAndBuild(1, 8, 8);
                        break;
                    case 102:
                        setCubeVisibleAndBuild(0, 8, 8);
                        break;

                    case 110:
                        setCubeVisibleAndBuild(0, 7, 8);
                        break;
                    case 111:
                        setCubeVisibleAndBuild(0, 6, 8);
                        break;
                    case 112:
                        setCubeVisibleAndBuild(0, 5, 8);
                        break;
                    case 113:
                        setCubeVisibleAndBuild(0, 4, 8);
                        break;
                    case 114:
                        setCubeVisibleAndBuild(0, 3, 8);
                        break;
                    case 115:
                        setCubeVisibleAndBuild(0, 2, 8);
                        break;
                    case 116:
                        setCubeVisibleAndBuild(0, 1, 8);
                        break;
                    case 117:
                        setCubeVisibleAndBuild(0, 0, 8);
                        break;

                    case 120:
                        setCubeVisibleAndBuild(0, 0, 8);
                        break;
                    case 121:
                        setCubeVisibleAndBuild(1, 0, 8);
                        break;
                    case 122:
                        setCubeVisibleAndBuild(2, 0, 8);
                        break;
                    case 123:
                        setCubeVisibleAndBuild(3, 0, 8);
                        break;
                    case 124:
                        setCubeVisibleAndBuild(4, 0, 8);
                        break;
                    case 125:
                        setCubeVisibleAndBuild(5, 0, 8);
                        break;
                    case 126:
                        setCubeVisibleAndBuild(6, 0, 8);
                        break;
                    case 127:
                        setCubeVisibleAndBuild(7, 0, 8);
                        break;
                    case 128:
                        setCubeVisibleAndBuild(8, 0, 8);
                        break;

                    case 130:
                        setCubeVisibleAndBuild(8, 0, 8);
                        break;
                    case 131:
                        setCubeVisibleAndBuild(8, 1, 8);
                        break;
                    case 132:
                        setCubeVisibleAndBuild(8, 2, 8);
                        break;
                    case 133:
                        setCubeVisibleAndBuild(8, 3, 8);
                        break;
                    case 134:
                        setCubeVisibleAndBuild(8, 4, 8);
                        break;
                    case 135:
                        setCubeVisibleAndBuild(8, 5, 8);
                        break;
                    case 136:
                        setCubeVisibleAndBuild(8, 6, 8);
                        break;

                    case 150:
                        GameOptions.setCanSkipIntro(true);
                        Game.showScene(Scene_Menu);
                        break;
                }
                break;
        }

        mStarfield.alpha = m_stars_alpha * 255;
        mStarfield.update();
    }

    @Override
    public void render() {
        glDisable(GL_BLEND);
        glEnable(GL_LIGHT0);

        Graphics.setProjection2D();
        Graphics.setModelViewMatrix2D();
        Graphics.bindStreamSources2d();

        glEnable(GL_BLEND);

        glEnable(GL_TEXTURE_2D);
        glDepthMask(false);

        Color color_dirty = new Color(255, 255, 255, (int)Game.dirtyAlpha);
        Graphics.drawFullScreenTexture(Graphics.textureDirty, color_dirty);

        glDisable(GL_TEXTURE_2D);
        //Graphics.drawAxes();

        Graphics.setProjection3D();
        Graphics.setModelViewMatrix3D(mCameraCurrent);

        Graphics.setLightPosition(mPosLightCurrent);

        glDisable(GL_LIGHTING);

        Graphics.zeroBufferPositions();
        Graphics.resetBufferIndices();
        Graphics.bindStreamSources3d();

        glDisableClientState(GL_NORMAL_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);


        glEnable(GL_POINT_SMOOTH);
        mStarfield.render();
        glDisable(GL_POINT_SMOOTH);

        glDepthMask(true);
        glDisable(GL_BLEND);

        //Graphics.setStreamSourcesFull3D();
        //Graphics.drawAxes();

        Graphics.bindStreamSources3d();

        glEnable(GL_LIGHTING);

        glDepthMask(true); //GL_TRUE);

        glEnable(GL_TEXTURE_2D);
        Graphics.texturePlayer.bind();

        glEnableClientState(GL_NORMAL_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        Graphics.resetBufferIndices();
        Graphics.zeroBufferPositions();
        Graphics.addCube(0.0f, 0.0f, 0.0f);

        int size;
        Cube cube;

        size = mCubesBase.size();
        for (int i = 0; i < size; ++i) {
            cube = mCubesBase.get(i);
            Graphics.addCubeSize(cube.tx, cube.ty, cube.tz, HALF_CUBE_SIZE, Game.baseColor);
        }

        int count_base = Graphics._vertices_count - 36;

        Color color = new Color(Game.faceColor);

        size = mCubesFace.size();
        for (int i = 0; i < size; ++i) {
            cube = mCubesFace.get(i);
            Graphics.addCubeSize(cube.tx, cube.ty, cube.tz, HALF_CUBE_SIZE, color);
        }

        int count_face = Graphics._vertices_count - count_base - 36;

        Graphics.updateBuffers();

        glPushMatrix();
            glTranslatef(0.0f, m_offset_y, 0.0f);
            glRotatef(m_degree, 1.0f, 0.0f, 0.0f);
            glRotatef(m_degree, 0.0f, 1.0f, 0.0f);
            glRotatef(m_degree, 0.0f, 0.0f, 1.0f);
            glDrawArrays(GL_TRIANGLES, 0, 36);

            if (count_base > 0 || count_face > 0) {
                Graphics.textureGrayConcrete.bind();
                glPushMatrix();
                glTranslatef(Game.cubeOffset.x, Game.cubeOffset.y, Game.cubeOffset.z);
                glDrawArrays(GL_TRIANGLES, 36, count_base);

                    if (count_face > 0) {
                        glDrawArrays(GL_TRIANGLES, count_base + 36, count_face);
                    }
                glPopMatrix();
            }
        glPopMatrix();

        glDisable(GL_LIGHTING);
    }

    @Override
    public void onFingerUp(float x, float y, int fingerCount) {
        if (mCanSkipIntro) {
            Game.dirtyAlpha = DIRTY_ALPHA;
            Audio.stopMusic();
            Game.showScene(Scene_Menu);
        }                
    }

}

package com.almagems.cubetraz;

import static android.opengl.GLES10.*;

import java.util.ArrayList;

import static com.almagems.cubetraz.Constants.*;


public final class Intro extends Scene {

    enum IntroState {
        AppearStarfield,
        ShowStarfield,
        BuildCubetraz,
        FinalizeBuild,
        BuildCubetrazFace,
    }

    private IntroState m_state;
    private Starfield m_starfield = new Starfield();

    private Camera m_camera_begin = new Camera();
    private Camera m_camera_end = new Camera();
    private Camera m_camera_current = new Camera();

    private float m_t_camera;

    private float m_degree;
    private int m_build_phase;
    private int m_build_to;
    private int m_counter;
    private boolean m_can_skip_intro;
    private Vector m_pos_light_current = new Vector();
    private float m_offset_y;
    private float m_stars_alpha;

    private ArrayList<Cube> m_list_cubes_base = new ArrayList<>();
    private ArrayList<Cube> m_list_cubes_face = new ArrayList<>();

    private int m_dirty_alpha;
    private int m_dirty_alpha_step;


    // ctor
    public Intro() {
        m_build_phase = 0;
        m_build_to = 1;
        m_counter = 0;
        m_can_skip_intro = true;
        m_offset_y = 10.0f;
        m_stars_alpha = 1.0f;
    }

    public void setupCameras() {
        m_camera_begin.eye = new Vector(0.0f, 0.0f, 40.0f / 1.5f);
        m_camera_begin.target = new Vector(0.0f, 0.0f, 0.0f);

        m_camera_end.eye = new Vector(0.0f, 0.0f, 35.0f / 1.5f);
        m_camera_end.target = new Vector(0.0f, 0.0f, 0.0f);

        m_camera_begin.eye = m_camera_begin.eye.scale(graphics.aspectRatio);
        m_camera_end.eye = m_camera_end.eye.scale(graphics.aspectRatio);

        m_camera_current.eye.init(m_camera_begin.eye);
        m_camera_current.target.init(m_camera_begin.target);
    }

    public void init() {
        setupCameras();

        m_starfield.speed = 0.2f;
        m_degree = 45.0f;
        m_pos_light_current = new Vector(-10.0f, 3.0f, 12.0f);
        m_t_camera = 0.0f;
        m_stars_alpha = 0.0f;
        m_dirty_alpha = 0;

        m_starfield.create();

        for (int i = 0; i < 400; ++i) {
            m_starfield.update();
        }

        m_starfield.alpha = m_stars_alpha;

        glDisable(GL_BLEND);
        glEnable(GL_LIGHT0);

        //const vec4 lightPosition(m_pos_light_current.x, m_pos_light_current.y, m_pos_light_current.z, 1.0f);
        //glLightfv(GL_LIGHT0, GL_POSITION, lightPosition.Pointer());

        setupCubetraz();

        m_can_skip_intro = true;
        //m_can_skip_intro = engine->GetCanSkipIntro();

        //engine->PrepareMusicToPlay(MUSIC_VECTORS);

        m_state = IntroState.AppearStarfield;

        //engine->dirty_alpha = 0;
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

        if (IntroState.BuildCubetrazFace == m_state)
            m_list_cubes_face.add(cube);
        else
            m_list_cubes_base.add(cube);
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

        Game.buildVisibleCubesList(m_list_cubes_base);
    }

    @Override
    public void update() {
        //System.out.println("Intro.update...");

        if (m_state != IntroState.AppearStarfield) {
            ++m_counter;
        }
        //printf("\nCounter: %d", m_counter);

        switch (m_state) {
            case AppearStarfield:
                m_stars_alpha += 0.025f;
                m_dirty_alpha += 2;

//                if (m_dirty_alpha > engine->dirty_alpha) {
//                    m_dirty_alpha = engine -> dirty_alpha;
//                }

                if (m_stars_alpha >= 1.0f) {
                    m_stars_alpha = 1.0f;
                    m_state = IntroState.ShowStarfield;
                    //m_dirty_alpha = engine->dirty_alpha;
                }
                //m_starfield.alpha = m_stars_alpha;
                break;

            case ShowStarfield:
                if (m_counter > 200) {
                    m_counter = 400;
                    m_state = IntroState.BuildCubetraz;
                    m_offset_y = 0.0f;
                    //engine->PlayPreparedMusic();
                    //m_starfield.speed = 0.25f;
                }
                break;

            case BuildCubetraz: {
                m_starfield.speed += 0.0003f;

                switch (m_counter) {
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
                        m_state = IntroState.FinalizeBuild;
                        m_counter = 0;
                    }
                }
            }
            break;

            case FinalizeBuild:
                switch (m_counter) {
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
                        m_state = IntroState.BuildCubetrazFace;
                        break;
                } // switch
                break;

            case BuildCubetrazFace:
                Game.dirty_alpha += 2f;

                if (Game.dirty_alpha > DIRTY_ALPHA) {
                    Game.dirty_alpha = DIRTY_ALPHA;
                }

                m_stars_alpha -= 0.012f;
                if (m_stars_alpha < 0.0f) {
                    m_stars_alpha = 0.0f;
                }

                m_t_camera += 0.03f;

                if (m_t_camera > 1.0f) {
                    m_t_camera = 1.0f;
                }

                Utils.lerpCamera(m_camera_begin, m_camera_end, m_t_camera, m_camera_current);

                switch (m_counter) {
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
                        Game.setCanSkipIntro();
                        Game.showScene(Scene_Menu);
                        break;
                } // switch
                break;
        } // switch

        m_starfield.alpha = m_stars_alpha * 255;
        m_starfield.update();
    }

    @Override
    public void render() {
        graphics.setProjection2D();
        graphics.setModelViewMatrix2D();
        graphics.bindStreamSources2d();

        glEnable(GL_BLEND);

        glEnable(GL_TEXTURE_2D);
        glDepthMask(false);

        Color color_dirty = new Color(255, 255, 255, (int)Game.dirty_alpha);
        graphics.drawFullScreenTexture(Graphics.texture_id_dirty, color_dirty);

        glDisable(GL_TEXTURE_2D);
        //graphics.drawAxes();

        graphics.setProjection3D();
        graphics.setModelViewMatrix3D(m_camera_current);

        float posLight[] = { m_pos_light_current.x, m_pos_light_current.y, m_pos_light_current.z, 1.0f };
        glLightfv(GL_LIGHT0, GL_POSITION, posLight, 0);

        glDisable(GL_LIGHTING);


        graphics.zeroBufferPositions();
        graphics.resetBufferIndices();
        graphics.bindStreamSources3d();

        glDisableClientState(GL_NORMAL_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);


        glEnable(GL_POINT_SMOOTH);
        m_starfield.render();
        glDisable(GL_POINT_SMOOTH);

        glDepthMask(true);
        glDisable(GL_BLEND);

        //graphics.setStreamSourcesFull3D();

        //graphics.drawAxes();

        graphics.bindStreamSources3d();

        glEnable(GL_LIGHTING);

        glDepthMask(true); //GL_TRUE);

        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_player);

        glEnableClientState(GL_NORMAL_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        Color color = Game.getBaseColor();

        graphics.resetBufferIndices();
        graphics.zeroBufferPositions();
        graphics.addCube(0.0f, 0.0f, 0.0f);

        int size;
        Cube cube;

        size = m_list_cubes_base.size();
        for (int i = 0; i < size; ++i) {
            cube = m_list_cubes_base.get(i);
            graphics.addCubeSize(cube.tx, cube.ty, cube.tz, HALF_CUBE_SIZE, color);
        }

        int count_base = graphics._vertices_count - 36;

        color = Game.getFaceColor(1f);

        size = m_list_cubes_face.size();
        for (int i = 0; i < size; ++i) {
            cube = m_list_cubes_face.get(i);
            graphics.addCubeSize(cube.tx, cube.ty, cube.tz, HALF_CUBE_SIZE, color);
        }

        int count_face = graphics._vertices_count - count_base - 36;

        graphics.updateBuffers();

        glPushMatrix();
            glTranslatef(0.0f, m_offset_y, 0.0f);
            glRotatef(m_degree, 1.0f, 0.0f, 0.0f);
            glRotatef(m_degree, 0.0f, 1.0f, 0.0f);
            glRotatef(m_degree, 0.0f, 0.0f, 1.0f);
            glDrawArrays(GL_TRIANGLES, 0, 36);

            if (count_base > 0 || count_face > 0) {
                glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_gray_concrete);
                glPushMatrix();
                glTranslatef(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);
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
    public void onFingerUp(float x, float y, int finger_count) {
        if (m_can_skip_intro) {
            Game.dirty_alpha = DIRTY_ALPHA;
            Game.stopMusic();
            Game.showScene(Scene_Menu);
        }                
    }

}

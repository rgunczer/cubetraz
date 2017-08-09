package com.almagems.cubetraz;

import java.util.ArrayList;

import static android.opengl.GLES10.*;
import static com.almagems.cubetraz.Constants.*;


public final class Outro extends Scene {

    public enum OutroStateEnum {
        AnimToOutro,
        RotateFull,
        OutroExplosion,
        OutroDone
    }

    private int m_cube_alpha;

    private Text[] m_ar_text_center = new Text[2];

    private OutroStateEnum m_state;

    private Camera m_camera_menu = new Camera();
    private Camera m_camera_outro = new Camera();
    private Camera m_camera_current = new Camera();

    private Starfield m_starfield = new Starfield();

    private float m_stars_alpha;
    private float m_t;
    private float m_degree;

    private Vector m_pos_light_outro;
    private Vector m_pos_light_menu;
    private Vector m_pos_light_current;

    private Vector m_pos_cube_player;

    private boolean m_draw_starfield;
    private float m_cube_rot_speed;

    private CubeRotation m_cube_rotation = new CubeRotation();

    private ArrayList<Cube> m_lst_cubes_base = new ArrayList<>();
    private ArrayList<Cube> m_lst_cubes_level = new ArrayList<>();
    private ArrayList<Cube> m_lst_cubes_base_appear = new ArrayList<>();
    private ArrayList<Cube> m_lst_cubes_base_disappear = new ArrayList<>();

    private float m_center_alpha;


    public void onFingerDown(float x, float y, int finger_count) {}    
    public void onFingerMove(float prev_x, float prev_y, float cur_x, float cur_y, int finger_count) {}


    public Outro() {
        m_camera_outro.eye = new Vector(0.0f, 10.0f, 30.0f);
        m_camera_outro.target = new Vector(0.0f, 0.0f, 0.0f);

        m_ar_text_center[0] = new Text();
        m_ar_text_center[1] = new Text();

        m_ar_text_center[0].setScale(1.0f, 1.0f);
        m_ar_text_center[1].setScale(1.0f, 1.0f);

        m_ar_text_center[0].setVSPace(0.8f);
        m_ar_text_center[1].setVSPace(0.8f);
    }

    @Override
    public void init() {
        m_ar_text_center[0].init("CONGRATULATIONS", true);
        m_ar_text_center[1].init("CUBETRAZ IS SOLVED", true);

        m_center_alpha = 0.0f;
        m_cube_alpha = 255;

        m_starfield.speed = 0.2f;
        m_starfield.create();

        for (int i = 0; i < 30*10; ++i) {
            m_starfield.update();
        }

        m_camera_outro = Game.level.m_camera_level;
        m_camera_menu = Game.menu.getCamera();
        m_camera_current = m_camera_outro;

        m_pos_light_outro = Game.level.m_pos_light;
        m_pos_light_menu = Game.menu.getLightPositon();
        m_pos_light_current = m_pos_light_outro;

        m_cube_rotation.degree = -45.0f;
        m_cube_rotation.axis = new Vector(0.0f, 1.0f, 0.0f);

        m_draw_starfield = false;
        m_stars_alpha = 0.0f;
        m_starfield.alpha = m_stars_alpha * 255;

        m_cube_rot_speed = 0.1f;

        m_state = OutroStateEnum.AnimToOutro;

        m_t = 0.0f;

        m_lst_cubes_base.clear();
        m_lst_cubes_level.clear();

        int size;
        Cube cube;

        size = Game.level.m_list_cubes_level.size();
        for (int i = 0; i < size; ++i) {
            cube = Game.level.m_list_cubes_level.get(i);
            m_lst_cubes_level.add(cube);
        }

        size = Game.level.m_list_cubes_wall_y_minus.size();
        for (int i = 0; i < size; ++i) {
            cube = Game.level.m_list_cubes_wall_y_minus.get(i);
            m_lst_cubes_base.add(cube);
        }

        size = Game.level.m_list_cubes_wall_x_minus.size();
        for (int i = 0; i < size; ++i) {
            cube = Game.level.m_list_cubes_wall_x_minus.get(i);
            m_lst_cubes_base.add(cube);
        }

        size = Game.level.m_list_cubes_wall_z_minus.size();
        for (int i = 0; i < size; ++i) {
            cube = Game.level.m_list_cubes_wall_z_minus.get(i);
            m_lst_cubes_base.add(cube);
        }

        size = Game.level.m_list_cubes_edges.size();
        for (int i = 0; i < size; ++i) {
            cube = Game.level.m_list_cubes_edges.get(i);
            m_lst_cubes_base.add(cube);
        }

        m_lst_cubes_base_disappear.clear();
        
        size = m_lst_cubes_base.size();
        for (int i = 0; i < size; ++i) {
            cube = m_lst_cubes_base.get(i);
            if (cube != null) {
                if (8 == cube.x || 8 == cube.z) {
                    m_lst_cubes_base_disappear.add(cube);
                }
            }
        }

        m_lst_cubes_base_appear.clear();

        ArrayList<Cube> lst = Game.createBaseCubesList();
        size = lst.size();
        for (int i = 0; i < size; ++i) {
            cube = lst.get(i);
            if ( !Game.isOnAList(cube, m_lst_cubes_base) ) {
                cube.setColor(Color.WHITE);
                m_lst_cubes_base_appear.add(cube);
            }
        }

        m_pos_cube_player = Game.getCubePosAt(Game.level.m_player_cube.getCubePos());

        //glClearColor(1f, 0f, 0f, 0f); // TODO: remove later

        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        glEnable(GL_BLEND);

        float posLight[] = { m_pos_light_current.x, m_pos_light_current.y, m_pos_light_current.z, 1.0f };
        glLightfv(GL_LIGHT0, GL_POSITION, posLight, 0);
    }

    public void setupExplosion() {        
        Cube cube;
        int size = m_lst_cubes_base.size();

        for (int i = 0; i < size; ++i) {
            cube = m_lst_cubes_base.get(i);

            cube.v = new Vector((-60 + Utils.rand.nextInt()%120), (-60 + Utils.rand.nextInt()%120), (-60 + Utils.rand.nextInt()%120));

            if ( cube.v.x > 0.0f && cube.v.x <  30.0f ) cube.v.x = Utils.randInt(30, 50);
            if ( cube.v.x < 0.0f && cube.v.x > -30.0f ) cube.v.x = Utils.randInt(30, 50) * -1.0f;

            if ( cube.v.y > 0.0f && cube.v.y <  30.0f ) cube.v.y = Utils.randInt(30, 50);
            if ( cube.v.y < 0.0f && cube.v.y > -30.0f ) cube.v.y = Utils.randInt(30, 50) * -1.0f;

            if ( cube.v.z > 0.0f && cube.v.z <  30.0f ) cube.v.z = Utils.randInt(30, 50);
            if ( cube.v.z < 0.0f && cube.v.z > -30.0f ) cube.v.z = Utils.randInt(30, 50) * -1.0f;

            cube.v.x *= 0.001f;
            cube.v.y *= 0.001f;
            cube.v.z *= 0.001f;
        }
    }

    @Override
    public void update() {
        switch (m_state) {
            case AnimToOutro:
                updateInAnimTo();
                break;

            case RotateFull:
                m_center_alpha += 0.04f;
                --Game.dirty_alpha;

                if (Game.dirty_alpha < 0) {
                    Game.dirty_alpha = 0;
                }
                updateInRotateFull();
                break;

            case OutroExplosion:
                updateInOutroExplosion();
                break;

            case OutroDone:
                m_center_alpha -= 0.03f;
                updateInOutroDone();
                break;
        }
    }

    public void updateInAnimTo() {
        boolean done = true;
        Cube cube;

        if (!m_lst_cubes_base_appear.isEmpty()) {
            done = false;
            cube = m_lst_cubes_base_appear.get(0);
            m_lst_cubes_base_appear.remove(cube);

            m_lst_cubes_base.add(cube);
        }

        if (!m_lst_cubes_base_disappear.isEmpty()) {
            done = false;
            cube = m_lst_cubes_base_disappear.get(0);
            m_lst_cubes_base_disappear.remove(cube);

            m_lst_cubes_base.remove(cube);
        }

        if (!m_lst_cubes_level.isEmpty()) {
            done = false;
            cube = m_lst_cubes_level.get( m_lst_cubes_level.size() - 1);
            m_lst_cubes_level.remove(cube);
        }

        if (m_cube_rot_speed < 4.0f && m_cube_rot_speed > 0.0f) {
            done = false;
            m_cube_rotation.degree -= m_cube_rot_speed;
            m_cube_rot_speed += 0.01f;
        }

        if (m_lst_cubes_base_appear.isEmpty()) {
            m_t += 0.01f;
            if (m_t > 1.0f) m_t = 1.0f;
            Utils.lerpCamera(m_camera_outro, m_camera_menu, m_t, m_camera_current);
            Utils.lerpVector3(m_pos_light_outro, m_pos_light_menu, m_t, m_pos_light_current);
        }

        if (done && Math.abs(1.0f - m_t) < EPSILON) {
            m_state = OutroStateEnum.RotateFull;
            m_draw_starfield = true;
            m_stars_alpha = 0.0f;
            Game.stopMusic();
        }
    }

    public void updateInRotateFull() {
        m_cube_rotation.degree -= m_cube_rot_speed;

        m_stars_alpha += 0.02f;

        if (m_stars_alpha > 1.0f) {
            m_state = OutroStateEnum.OutroExplosion;
            m_stars_alpha = 1.0f;
            m_pos_cube_player = Game.getCubePosAt(new CubePos(4, 4, 4));
            m_degree = 0.0f;
            setupExplosion();
            Game.playMusic(MUSIC_VECTORS);
        }

        m_starfield.alpha = m_stars_alpha * 255;
        m_starfield.update();
    }

    public void updateInOutroExplosion() {
        m_starfield.speed += 0.0003f;
        m_degree += 1.0f;

        Cube cube;
        int size = m_lst_cubes_base.size();
        for (int i = 0; i < size; ++i) {
            cube = m_lst_cubes_base.get(i);
            cube.update();
        }

        if ( (Math.ceil(m_cube_rotation.degree) % 360) != 0) {
            m_cube_rotation.degree += 0.5f;
        } else {
            m_cube_rotation.degree = 0.0f;
            m_state = OutroStateEnum.OutroDone;
        }

        m_starfield.alpha = m_stars_alpha * 255;
        m_starfield.update();
    }

    public void updateInOutroDone() {
        m_starfield.speed += 0.0003f;
        m_degree += 1.25f;
        m_cube_alpha -= 2;

        if (m_cube_alpha < 0) {
            m_cube_alpha = 0;
        }

        Cube cube;
        int size = m_lst_cubes_base.size();
        for (int i = 0; i < size; ++i) {
            cube = m_lst_cubes_base.get(i);
            cube.color_current.a = m_cube_alpha;
            cube.update();
        }

        m_starfield.alpha = m_stars_alpha * 255;
        m_starfield.update();

        if (m_starfield.speed > 1.2f) {
            Game.showScene(Scene_Menu);
        }
    }

    public void drawTheCube() {
        graphics.bindStreamSources3d();
        graphics.resetBufferIndices();

        Cube cube;
        int size = m_lst_cubes_base.size();
        for (int i = 0; i < size; ++i) {
            cube = m_lst_cubes_base.get(i);
            graphics.addCubeSize(cube.tx, cube.ty, cube.tz, HALF_CUBE_SIZE, cube.color_current);
        }

        size = m_lst_cubes_level.size();
        for (int i = 0; i < size; ++i) {
            cube = m_lst_cubes_level.get(i);
            graphics.addCubeSize(cube.tx, cube.ty, cube.tz, HALF_CUBE_SIZE, cube.color_current);   
        }

        glEnable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);

        glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_gray_concrete);
        graphics.updateBuffers();
        graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);
    }

    public void drawText() {
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_LIGHTING);
        glDisable(GL_CULL_FACE);
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);

        graphics.setProjection2D();
        graphics.setModelViewMatrix2D();

        int a = (int)m_center_alpha * 255;

        glDisable(GL_TEXTURE_2D);
        Color color_bg = new Color(30, 30, 15, (int)m_center_alpha * 150);

        graphics.bindStreamSources2dNoTextures();
        graphics.resetBufferIndices();
        graphics.addQuad(0.0f, Graphics.half_height - 20.0f * Graphics.device_scale, Graphics.width, 75.0f * Graphics.device_scale, color_bg);
        graphics.updateBuffers();
        graphics.renderTriangles();

        graphics.bindStreamSources2d();
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_fonts_big);

        float scale = 0.75f * Graphics.device_scale;
        m_ar_text_center[0].setScale(scale, scale);
        m_ar_text_center[1].setScale(scale, scale);

        //printf("\na: %d", a);

        Color color = new Color(0, 0, 0, a);

        graphics.resetBufferIndices();

        Vector2 pos = new Vector2();
        pos.x = Graphics.half_width - m_ar_text_center[0].getHalfWidth();
        pos.y = Graphics.half_height + m_ar_text_center[0].getHalfHeight();
        m_ar_text_center[0].emitt(pos, color);

        pos.x = Graphics.half_width - m_ar_text_center[1].getHalfWidth();
        pos.y = Graphics.half_height - m_ar_text_center[1].getHalfHeight();
        m_ar_text_center[1].emitt(pos, color);

        glPushMatrix();
        glTranslatef(Graphics.device_scale, Graphics.device_scale, 0.0f);
        graphics.updateBuffers();
        graphics.renderTriangles();
        glPopMatrix();

        color = new Color(255, 255, 0, a);

        graphics.resetBufferIndices();

        pos.x = Graphics.half_width - m_ar_text_center[0].getHalfWidth();
        pos.y = Graphics.half_height + m_ar_text_center[0].getHalfHeight();
        m_ar_text_center[0].emitt(pos, color);

        pos.x = Graphics.half_width - m_ar_text_center[1].getHalfWidth();
        pos.y = Graphics.half_height - m_ar_text_center[1].getHalfHeight();
        m_ar_text_center[1].emitt(pos, color);

        graphics.updateBuffers();
        graphics.renderTriangles();

        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
    }

    public void render() {
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);

        if (Game.dirty_alpha > 0) {
            graphics.setProjection2D();
            graphics.setModelViewMatrix2D();

            glDisable(GL_LIGHTING);
            glDepthMask(false);
            glEnable(GL_TEXTURE_2D);

            Color color_dirty = new Color(255, 255, 255, (int)Game.dirty_alpha);
            graphics.drawFullScreenTexture(Graphics.texture_id_dirty, color_dirty);

            glDepthMask(true);
            glEnable(GL_LIGHTING);
        }

        graphics.bindStreamSources3d();

        graphics.setProjection3D();
        graphics.setModelViewMatrix3D(m_camera_current);

        if (m_draw_starfield) {
            glDisable(GL_TEXTURE_2D);
            glDisable(GL_LIGHTING);

            glDisableClientState(GL_NORMAL_ARRAY);
            glDisableClientState(GL_TEXTURE_COORD_ARRAY);

            glDepthMask(false);

            glEnable(GL_POINT_SMOOTH);
            m_starfield.render();
            glDisable(GL_POINT_SMOOTH);

            glEnable(GL_LIGHTING);
            glDepthMask(true);
            glEnable(GL_TEXTURE_2D);

            glEnableClientState(GL_NORMAL_ARRAY);
            glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        }

        glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_player);

        if (OutroStateEnum.OutroExplosion == m_state || OutroStateEnum.OutroDone == m_state) {
            glPushMatrix();

            glRotatef(m_degree, 1.0f, 0.0f, 0.0f);
            glRotatef(m_degree, 0.0f, 1.0f, 0.0f);
            glRotatef(m_degree, 0.0f, 0.0f, 1.0f);

            graphics.resetBufferIndices();
            graphics.addCube(m_pos_cube_player.x, m_pos_cube_player.y, m_pos_cube_player.z);
            graphics.updateBuffers();
            graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);

            glPopMatrix();
        } else {
            glPushMatrix();
            glRotatef(m_cube_rotation.degree, m_cube_rotation.axis.x, m_cube_rotation.axis.y, m_cube_rotation.axis.z);

            graphics.resetBufferIndices();
            graphics.addCube(m_pos_cube_player.x, m_pos_cube_player.y, m_pos_cube_player.z);
            graphics.updateBuffers();
            graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);

            glPopMatrix();
        }

        if (m_cube_alpha > 0) {
            glPushMatrix();
            glRotatef(m_cube_rotation.degree, m_cube_rotation.axis.x, m_cube_rotation.axis.y, m_cube_rotation.axis.z);

            drawTheCube();

            if (false) { //#ifdef DRAW_AXES_CUBE
                glDisable(GL_TEXTURE_2D);
                glDisable(GL_LIGHTING);
                graphics.drawAxes();
                glEnable(GL_LIGHTING);
                glEnable(GL_TEXTURE_2D);
            } //#endif

            glPopMatrix();
        }
        if (m_center_alpha > EPSILON) {
            drawText();
        }
    }
    
    public void onFingerUp(float x, float y, int finger_count) {
        if (OutroStateEnum.OutroDone == m_state) {
            Game.showScene(Scene_Menu);
        }
    }

}

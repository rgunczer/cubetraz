package com.almagems.cubetraz;


public final class Outro {

    public enum OutroStateEnum {
        AnimToOutro,
        RotateFull,
        OutroExplosion,
        OutroDone
    };

    private int m_cube_alpha;

    private TextDisplay m_text_display;
    private Text[] m_ar_text_center = new Text[2];

    private OutroStateEnum m_state;

    private Camera m_camera_menu;
    private Camera m_camera_outro;
    private Camera m_camera_current;

    private Starfield m_starfield;

    private float m_stars_alpha;
    private float m_t;
    private float m_degree;

    private Vector m_pos_light_outro;
    private Vector m_pos_light_menu;
    private Vector m_pos_light_current;

    private Vector m_pos_cube_player;

    private boolean m_draw_starfield;
    private float m_cube_rot_speed;

    private CubeRotation m_cube_rotation;

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

        m_ar_text_center[0].setDisplay(m_text_display);
        m_ar_text_center[1].setDisplay(m_text_display);

        m_ar_text_center[0].setScale(1.0f, 1.0f);
        m_ar_text_center[1].setScale(1.0f, 1.0f);

        m_ar_text_center[0].setVSPace(0.8f);
        m_ar_text_center[1].setVSPace(0.8f);
    }

    public void nit() {
        m_ar_text_center[0].init("CONGRATULATIONS", true);
        m_ar_text_center[1].init("CUBETRAZ IS SOLVED", true);

        m_center_alpha = 0.0f;
        m_cube_alpha = 255;

        m_starfield.speed = 0.2f;
        m_starfield.create();

        for (int i = 0; i < 30*10; ++i) {
            m_starfield.update(1.0f / 30.0f);
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

        m_state = AnimToOutro;

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
            if (cube) {
                if (8 == cube.x || 8 == cube.z) {
                    m_lst_cubes_base_disappear.add(cube);
                }
            }
        }

        m_lst_cubes_base_appear.clear();
            
        Game.createBaseCubesList(lst);
        size = lst.size();
        for (int i = 0; i < size; ++i) {
            cube = lst.get(i);
            if ( !Game.isOnAList(cube, m_lst_cubes_base) ) {
                cube.setColor(Color.WHITE);
                m_lst_cubes_base_appear.add(cube);
            }
        }

        m_pos_cube_player = Game.getCubePosAt(Game.level.m_player_cube.getCubePos());

        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        glEnable(GL_BLEND);

        //const vec4 lightPosition(m_pos_light_current.x, m_pos_light_current.y, m_pos_light_current.z, 1.0f);
        //glLightfv(GL_LIGHT0, GL_POSITION, lightPosition.Pointer());
    }

    public void setupExplosion() {        
        Cube cube;
        int size = m_lst_cubes_base.size();

        for (int i = 0; i < size; ++i) {
            cube = m_lst_cubes_base.get(i);

            cube.v = new Vector((-60 + Utils.rand.getNext()%120), (-60 + Utils.rand.getNext()%120), (-60 + Utils.rand.getNext()%120));

            if ( cube.v.x > 0.0f && cube.v.x <  30.0f ) cube.v.x = RANDOM_FLOAT(30.0f, 50.0f);
            if ( cube.v.x < 0.0f && cube.v.x > -30.0f ) cube.v.x = RANDOM_FLOAT(30.0f, 50.0f) * -1.0f;

            if ( cube.v.y > 0.0f && cube.v.y <  30.0f ) cube.v.y = RANDOM_FLOAT(30.0f, 50.0f);
            if ( cube.v.y < 0.0f && cube.v.y > -30.0f ) cube.v.y = RANDOM_FLOAT(30.0f, 50.0f) * -1.0f;

            if ( cube.v.z > 0.0f && cube.v.z <  30.0f ) cube.v.z = RANDOM_FLOAT(30.0f, 50.0f);
            if ( cube.v.z < 0.0f && cube.v.z > -30.0f ) cube.v.z = RANDOM_FLOAT(30.0f, 50.0f) * -1.0f;

            cube.v.x *= 0.001f;
            cube.v.y *= 0.001f;
            cube.v.z *= 0.001f;
        }
    }

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

        if (!m_lst_cubes_base_appear.isEmpty()) {
            done = false;
            Cube cube = m_lst_cubes_base_appear.get(0);
            m_lst_cubes_base_appear.remove(cube);

            m_lst_cubes_base.add(cube);
        }

        if (!m_lst_cubes_base_disappear.isEmpty()) {
            done = false;
            Cube pCube = m_lst_cubes_base_disappear.get(0);
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
            m_state = RotateFull;
            m_draw_starfield = true;
            m_stars_alpha = 0.0f;
            Games.stopMusic();
        }
    }

    public void UpdateInRotateFull() {
        m_cube_rotation.degree -= m_cube_rot_speed;

        m_stars_alpha += 0.02f;

        if (m_stars_alpha > 1.0f) {
            m_state = OutroExplosion;
            m_stars_alpha = 1.0f;
            m_pos_cube_player = Game.getCubePosAt(4, 4, 4);
            m_degree = 0.0f;
            setupExplosion();
            Game.playMusic(MUSIC_VECTORS);
        }

        m_starfield.alpha = m_stars_alpha * 255;
        m_starfield.update();
    }

    public void UpdateInOutroExplosion()) {
        m_starfield.speed += 0.0003f;
        m_degree += 1.0f;

        Cube cube;
        int size = m_lst_cubes_base.size();
        for (int i = 0; i < size; ++i) {
            cube = m_lst_cubes_base.get(i);
            cube.update();
//        if (m_degree > 25.0f)
//            (*it).v = vec3(0.0f, 0.0f, 0.0f);
        }

        if ( int(m_cube_rotation.degree) % 360 != 0) {
            m_cube_rotation.degree += 0.5f;
        } else {
            m_cube_rotation.degree = 0.0f;
            m_state = OutroDone;
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
        Graphics.prepare();
        Graphics.setStreamSource();

        Cube cube;
        int size = m_lst_cubes_base.size();
        for (int i = 0; i < size; ++i) {
            cube = m_lst_cubes_base.get(i);
            Graphics.addCubeSize(cube.tx, cube.ty, cube.tz, HALF_CUBE_SIZE, cube.color_current);
        }

        size = m_lst_cubes_level.size();
        for (int i = 0; i < size; ++i) {
            cube = m_lst_cubes_level.get(i);
            Graphics.addCubeSize(cube.tx, cube.ty, cube.tz, HALF_CUBE_SIZE, cube.color_current);   
        }

        glEnable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_gray_concrete);
        Graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);
    }

    public void drawText() {
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_LIGHTING);
        glDisable(GL_CULL_FACE);
        glDisable(GL_DEPTH_TEST);

        Graphics.setProjection2D();
        Graphics.setModelViewMatrix2D();

        int a = m_center_alpha * 255;

        glDisable(GL_TEXTURE_2D);
        Color color_bg = new Color(30, 30, 15, 150 * m_center_alpha);

        Graphics.setStreamSourceFloat2DNoTexture();
        Graphics.prepare();
        Graphics.addQuad(0.0f, Graphics.half_height - 20.0f * Graphics.device_scale, Graphics.width, 75.0f * Graphics.device_scale, color_bg);
        Graphics.renderTriangles();

        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_fonts_big);

        float scale = 0.75f * Graphics.device_scale;
        m_ar_text_center[0].setScale(scale, scale);
        m_ar_text_center[1].setScale(scale, scale);

        //printf("\na: %d", a);

        Color color = new Color(0, 0, 0, a);

        m_text_display.init();
        m_ar_text_center[0].emitt(Graphics.half_width - m_ar_text_center[0].getHalfWidth(), Graphics.half_height + m_ar_text_center[0].getHalfHeight(), color);
        m_text_display.m_vertex_count += m_ar_text_center[0].getVertexCount();

        m_ar_text_center[1].emitt(Graphics.half_width - m_ar_text_center[1].getHalfWidth(), Graphics.half_height - m_ar_text_center[1].getHalfHeight(), color);
        m_text_display.m_vertex_count += m_ar_text_center[1].getVertexCount();

        glPushMatrix();
        glTranslatef(Graphics.device_scale, Graphics.device_scale, 0.0f);
        m_text_display.render();
        glPopMatrix();

        color = new Color(255, 255, 0, a);

        m_text_display.init();
        m_ar_text_center[0].emitt(Graphics.half_width - m_ar_text_center[0].getHalfWidth(), Graphics.half_height + m_ar_text_center[0].getHalfHeight(), color);
        m_text_display.m_vertex_count += m_ar_text_center[0].getVertexCount();

        m_ar_text_center[1].emitt(Graphics.half_width - m_ar_text_center[1].getHalfWidth(), Graphics.half_height - m_ar_text_center[1].getHalfHeight(), color);
        m_text_display.m_vertex_count += m_ar_text_center[1].getVertexCount();

        m_text_display.render();

        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);

    }

    public void render() {
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);

        if (engine.dirty_alpha > 0) {
            Graphics.setProjection2D();
            Graphics.setModelViewMatrix2D();

            glDisable(GL_LIGHTING);
            glDepthMask(GL_FALSE);
            glEnable(GL_TEXTURE_2D);

            Color color_dirty = new Color(255, 255, 255, engine.dirty_alpha);
            Game.drawFBOTexture(Graphics.texture_id_dirty, color_dirty);

            glDepthMask(true); //GL_TRUE);
            glEnable(GL_LIGHTING);
        }

        Graphics.setStreamSource();

        Graphics.setProjection3D();
        Graphics.setModelViewMatrix3D(m_camera_current);

        //const vec4 lightPosition(m_pos_light_current.x, m_pos_light_current.y, m_pos_light_current.z, 1.0f);
        //glLightfv(GL_LIGHT0, GL_POSITION, lightPosition.Pointer());

        if (m_draw_starfield) {
            glDisable(GL_TEXTURE_2D);
            glDisable(GL_LIGHTING);

            glDisableClientState(GL_NORMAL_ARRAY);
            glDisableClientState(GL_TEXTURE_COORD_ARRAY);

            glDepthMask(false); //GL_FALSE);

            glEnable(GL_POINT_SMOOTH);
            m_starfield.render();
            glDisable(GL_POINT_SMOOTH);

            glEnable(GL_LIGHTING);
            glDepthMask(true); //GL_TRUE);
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

            Graphics.prepare();
            Graphics.addCube(m_pos_cube_player.x, m_pos_cube_player.y, m_pos_cube_player.z);
            Graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);

            glPopMatrix();
        } else {
            glPushMatrix();
            glRotatef(m_cube_rotation.degree, m_cube_rotation.axis.x, m_cube_rotation.axis.y, m_cube_rotation.axis.z);

            Graphics.prepare();
            Graphics.addCube(m_pos_cube_player.x, m_pos_cube_player.y, m_pos_cube_player.z);
            Graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);

            glPopMatrix();
        }

        if (m_cube_alpha > 0) {
            glPushMatrix();
            glRotatef(m_cube_rotation.degree, m_cube_rotation.axis.x, m_cube_rotation.axis.y, m_cube_rotation.axis.z);

            drawTheCube();

            if (true) { //#ifdef DRAW_AXES_CUBE
                glDisable(GL_TEXTURE_2D);
                glDisable(GL_LIGHTING);
                engine.drawAxes();
                glEnable(GL_LIGHTING);
                glEnable(GL_TEXTURE_2D);
            } //#endif

            glPopMatrix();
        }
        drawText();
    }
    
    public void onFingerUp(float x, float y, int finger_count) {
        if (OutroStateEnum.OutroDone == m_state) {
            Graphics.showScene(Scene_Menu);
        }
    }

}

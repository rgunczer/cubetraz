package com.almagems.cubetraz;

import java.util.ArrayList;

import static android.opengl.GLES10.*;

import static com.almagems.cubetraz.Constants.*;


public final class Animator extends Scene {
      
    private float m_t;
	private float m_t_camera;
	private float m_timeout;
	private float m_target_rotation_degree;
	
	private int m_anim_to_menu_phase;

	private final Vector m_pos_light_from = new Vector();
	private final Vector m_pos_light_to = new Vector();
    private final Vector m_pos_light_current = new Vector();
	
	private final Camera m_camera_from = new Camera();
	private final Camera m_camera_to = new Camera();
	private final Camera m_camera_current = new Camera();
	
	private CubeFaceNamesEnum m_face_name_x_plus;
	private CubeFaceNamesEnum m_face_name_y_plus;
	private CubeFaceNamesEnum m_face_name_z_plus;	
	
	private AnimTypeEnum m_type;
	
	private LevelCube m_level_cube_hilite;
	private float m_hilite_alpha;
    
    private final EaseOutDivideInterpolation m_interpolator = new EaseOutDivideInterpolation();
    private final EaseOutDivideInterpolation[] m_interpolators = new EaseOutDivideInterpolation[6];

	private CubeRotation m_cube_rotation;
	
	private final ArrayList<CubeFont> m_lst_titles[6];
	private final ArrayList<CubeFont> m_lst_texts[6];
    private final ArrayList<CubeFont> m_lst_symbols[6];
	
	private final ArrayList<LevelCube> m_lst_level_cubes_x_plus = new ArrayList();
	private final ArrayList<LevelCube> m_lst_level_cubes_y_plus = new ArrayList();
	private final ArrayList<LevelCube> m_lst_level_cubes_z_plus = new ArrayList();
	
// cubes
	private final ArrayList<Cube> m_list_cubes_base = new ArrayList();
    private final ArrayList<Cube> m_list_cubes_face = new ArrayList();
	
	private AppearDisappearListData m_ad_base_disappear;
	private AppearDisappearListData m_ad_base_appear;
	private AppearDisappearListData m_ad_face;
			        	
	private LevelCube getLevelCubeFrom(ArrayList<LevelCube> lst) {
		if (lst.isEmpty()) {
			return null;
		} else {
			return lst.get( lst.size() - 1);
        }
	}
    
	private Cube getCubeFrom(ArrayList<Cube> lst) {
		if (lst.empty()) {
			return null;
		} else {
			return lst.get( lst.size() - 1);
        }
	}
					
    // ctor   
    public Animator() {
	    m_face_name_x_plus = Face_Empty;
	    m_face_name_y_plus = Face_Empty;
	    m_face_name_z_plus = Face_Empty;
    }

    public void init() {
        m_level_cube_hilite = null;
    
	    AnimInitData aid = Game.anim_init_data;
	    m_type = aid.type;

	    m_t_camera = 0.0f;
		
	    Game.resetCubes();
	    Game.clearCubeFaceData();
	
	    switch (m_type) {
		    case AnimToLevel:
			    setupAnimToLevel();
			    break;
			
		    case AnimToMenuFromCompleted:
		    case AnimToMenuFromPaused:
			    setupAnimToMenu();
			    break;
	    }

	    updateCubes(0.0f);
    }
    
    public void createListOfLevelCubes(ArrayList<Cube> lst) {
	    lst.clear();

	    // floor
        for (int z = 2; z < MAX_CUBE_COUNT; ++z) {
            for (int x = 2; x < MAX_CUBE_COUNT; ++x) {
                lst.add(Game.cubes[x][1][z]);
            }
        }
	
        // walls
        for (int y = 2; y < 7; ++y) {
            for (int x = 2; x < MAX_CUBE_COUNT; ++x) {
                lst.add(Game.cubes[x][y][1]);
            }
        
            for (int z = 2; z < MAX_CUBE_COUNT; ++z) {
                lst.add(Game.cubes[1][y][z]);
            }
        }
    
        // edges
	    lst.add(Game.cubes[1][1][1]);

	    lst.add(Game.cubes[2][1][1]);
	    lst.add(Game.cubes[3][1][1]);
	    lst.add(Game.cubes[4][1][1]);
	    lst.add(Game.cubes[5][1][1]);
	    lst.add(Game.cubes[6][1][1]);
	    lst.add(Game.cubes[7][1][1]);

	    lst.add(Game.cubes[1][1][2]);
	    lst.add(Game.cubes[1][1][3]);
	    lst.add(Game.cubes[1][1][4]);
	    lst.add(Game.cubes[1][1][5]);
	    lst.add(Game.cubes[1][1][6]);
	    lst.add(Game.cubes[1][1][7]);

	    lst.add(Game.cubes[1][2][1]);
	    lst.add(Game.cubes[1][3][1]);
	    lst.add(Game.cubes[1][4][1]);
	    lst.add(Game.cubes[1][5][1]);
	    lst.add(Game.cubes[1][6][1]);
    }

    public void createMenuFaces() {
        Game.resetCubesFonts();
	
        Creator.fillPools();
	    
        MenuFaceBuilder.resetTransforms();
	    MenuFaceBuilder.build(m_face_name_x_plus, Face_X_Plus);
	    MenuFaceBuilder.addTransform(RotateCCW90);
	    MenuFaceBuilder.build(m_face_name_y_plus, Face_Y_Plus);
	    MenuFaceBuilder.build(m_face_name_z_plus, Face_Z_Plus);
    }

    public void localCopyLevelCubes() {
        int len;
        LevelCube cube;
        
	    m_lst_level_cubes_x_plus.clear();
        len = Game.ar_cubefacedata[Face_X_Plus].lst_level_cubes.size();
	    for (int i = 0; i < len; ++i) {
            cube = Game.ar_cubefacedata[Face_X_Plus].lst_level_cubes.get(i);
		    m_lst_level_cubes_x_plus.add(cube);
	    }
	    Game.ar_cubefacedata[Face_X_Plus].lst_level_cubes.clear();
	
	    m_lst_level_cubes_y_plus.clear();
        len = Game.ar_cubefacedata[Face_Y_Plus].lst_level_cubes.size();
	    for (int i = 0; i < len; ++i) {	
            cube = Game.ar_cubefacedata[Face_Y_Plus].lst_level_cubes.get(i);
		    m_lst_level_cubes_y_plus.add(cube);
	    }
	    Game.ar_cubefacedata[Face_Y_Plus].lst_level_cubes.clear();
	
	    m_lst_level_cubes_z_plus.clear();
        len = Game.ar_cubefacedata[Face_Z_Plus].lst_level_cubes.size(i);
	    for (int i = 0 ; i < len; ++i) {	
            cube = Game.ar_cubefacedata[Face_Z_Plus].lst_level_cubes.get(i);
		    m_lst_level_cubes_z_plus.add(cube);
	    }
	    Game.ar_cubefacedata[Face_Z_Plus].lst_level_cubes.clear();
    }

    public void setupAnimToMenu() {
	    //printf("\nAnim To Menu...");

	    m_t = 0.0f;
	    m_timeout = 0.0f;
	    m_anim_to_menu_phase = 0;
	
	    AnimInitData aid = Game.anim_init_data;
	
	    createMenuFaces();
	
	    m_ad_face.Clear();
	    Game.buildVisibleCubesListOnlyOnFaces(m_ad_face.lst_appear);
	    m_list_cubes_face.clear();

	    createListOfLevelCubes(m_list_cubes_base);
		
	    aid.list_cubes_base.clear();
	    m_ad_base_disappear.Clear();
	
	    //list<cCube*> lst;
	    ArrayList<Cube> lst = Game.createBaseCubesList(); //lst); function name should begin with get.... (todo)
	
        int len = m_list_cubes_base.size();
	    Cube cube;
	    for (int i = 0; i < len; ++i) {
            cube = m_list_cubes_base.get(i);
		    if ( !Game.isOnAList(cube, lst) ) {
			    m_ad_base_disappear.addDisappear(cube);
            }
	    }

        len = lst.size();
	    for (int i = 0; i < len; ++i) {
            cube = lst.get(i);
		    if ( !Game->isOnAList(cube, m_list_cubes_base)) {
			    m_ad_base_appear.addAppear(cube);
            }
	    }
	
	    localCopyLevelCubes();
	
	    m_cube_rotation.degree = aid.cube_rotation_degree;
	    m_target_rotation_degree = -45.0f;
	
	    m_interpolator.setup(m_cube_rotation.degree, m_target_rotation_degree, 9);
		
	    m_camera_from = Game.level.m_camera_current;
	    m_camera_to = Game.level.m_camera_level;
	    m_camera_current = m_camera_from;
	
	    m_pos_light_from = Game.level.m_pos_light;
	    m_pos_light_to = Game.menu.m_pos_light_current;
	    m_pos_light_current = m_pos_light_from;

	    m_ad_face.setLevelAndDirection(0, 1);
	    m_ad_base_appear.setLevelAndDirection(0, 1);
	    m_ad_base_disappear.setLevelAndDirection(0, 1);
    }

    public void cAnimator::SetupAnimToLevel() {
	    //printf("\nSetup Anim To Level...");
    
	    AnimInitData aid = Game.anim_init_data;

	    m_hilite_alpha = 0.0f;
	    m_t = 0.0f;
	    m_timeout = 0.1f;

	    m_cube_rotation.degree = -90.0f;
	    m_cube_rotation.axis = new Vector(0.0f, 1.0f, 0.0f);
	
        m_target_rotation_degree = m_cube_rotation.degree + 45.0f;
    
        m_interpolator.setup(m_cube_rotation.degree, m_target_rotation_degree, 15);

	    m_face_name_x_plus = aid.face_name_x_plus;
	    m_face_name_y_plus = aid.face_name_y_plus;
	    m_face_name_z_plus = aid.face_name_z_plus;
	
	    createMenuFaces();
	
	    Game.buildVisibleCubesListOnlyOnFaces(m_list_cubes_face);
	    m_ad_face.initDisappearListFrom(m_list_cubes_face);
	
	    m_list_cubes_base.clear();
	
	    list<cCube*>::iterator it;
	    for (it = aid.list_cubes_base.begin(); it != aid.list_cubes_base.end(); ++it) {
		    m_list_cubes_base.push_back(*it);
        }
	
	    aid.list_cubes_base.clear();
	
	    m_camera_from.init(aid.camera_from);
	    m_camera_to.init(aid.camera_to);
	    m_camera_current.init(m_camera_from);
	
	    m_pos_light_from.init(aid.pos_light_from);
	    m_pos_light_to.init(aid.pos_light_to);
	    m_pos_light_current.init(m_pos_light_from);
	
	    m_ad_base_disappear.clear();
	        
	    list<cCube*> lst_level;
	    createListOfLevelCubes(lst_level);
	
	    for (it = m_list_cubes_base.begin(); it != m_list_cubes_base.end(); ++it) {
		    if ( !engine->IsOnAList(*it, lst_level) ) {
			    m_ad_base_disappear.AddDisappear(*it);
            }
	    }

	    m_ad_base_appear.clear();
	    for (int i = 6; i > 0; --i) {
		    m_ad_base_appear.addAppear(Game.cubes[8][i][1]);
		    m_ad_base_appear.addAppear(Game.cubes[1][i][8]);
	    }
	
	    for (int i = 2; i < 8; ++i) {
		    m_ad_base_appear.AddAppear(Game.cubes[8][1][i]);
		    m_ad_base_appear.AddAppear(Game.cubes[i][1][8]);
	    }
	
	    m_ad_base_appear.AddAppear(Game.cubes[8][1][8]);

	    m_ad_face.setLevelAndDirection(MAX_CUBE_COUNT - 1, -1);
	    m_ad_base_disappear.setLevelAndDirection(MAX_CUBE_COUNT - 1, -1);
	    m_ad_base_appear.setLevelAndDirection(MAX_CUBE_COUNT - 1, -1);

	    locateLevelCube();
    }

    public void locateLevelCube() {
	    int level_number = engine->level_init_data.level_number;

	    list<cLevelCube*>::iterator it;
	    for (it = engine->ar_cubefacedata[Face_X_Plus].lst_level_cubes.begin(); it != engine->ar_cubefacedata[Face_X_Plus].lst_level_cubes.end(); ++it) {
		    if ((*it)->level_number == level_number) {
                m_level_cube_hilite = *it;
                m_level_cube_hilite->color_number = new Color(255, 255, 0, 255);
                return;
            }
	    }
    }

    public void updateAnimToMenu(float dt) {
	    if (0 == m_anim_to_menu_phase) {
		    updateAnimToMenuPhaseOne(dt);
        } else {
		    updateAnimToMenuPhaseTwo(dt);
        }
    }

    public void updateAnimToMenuPhaseTwo(float dt) {
        m_interpolator.interpolate();
        m_cube_rotation.degree = m_interpolator.getValue();
	
	    //engine->IncT(m_t_camera); // TODO replace remove this 
	    Utils.lerpCamera(m_camera_from, m_camera_to, m_t_camera, m_camera_current);
	
	    Cube cube;    
        cube = m_ad_face.getCubeFromAppearList();
	    if (cube) {
		    m_list_cubes_face.add(cube);
	    }

        cube = m_ad_face.getCubeFromAppearList();
	    if (cube) {
		    m_list_cubes_face.add(cube);
	    }

        cube = m_ad_face.getCubeFromAppearList(); 
	    if (pCube) {
		    m_list_cubes_face.add(cube);
	    }
    
        float diff = Math.abs( Math.abs(m_target_rotation_degree) - Math.abs(m_interpolator.getValue()));
	
        if (diff < EPSILON && fabs(1.0f - m_t) < EPSILON && Math.abs(1.0f - m_t_camera) < EPSILON && m_ad_face.lst_appear.isEmpty()) {
		    Game.menu_init_data.reappear = true;
		    Game.showScene(Scene_Menu);
        }
    }

    public void updateAnimToMenuPhaseOne(float dt) {
        m_interpolator.interpolate();
        m_cube_rotation.degree = m_interpolator.getValue();
	
        //engine->IncT(m_t);
        Utils.lerpVec3(m_pos_light_from, m_pos_light_to, m_t, m_pos_light_current);
	    Utils.lerpCamera(m_camera_from, m_camera_to, m_t, m_camera_current);
		
        Cube cube;    
	    boolean done_base = true;
	
        cube = m_ad_base_appear.getCubeFromAppearList();    
        if (cube) {
            m_list_cubes_base.add(cube);
            done_base = false;
        }
    
        cube = m_ad_base_appear.GetCubeFromAppearList();    
        if (cube) {
            m_list_cubes_base.add(cube);
            done_base = false;
        }

        cube = m_ad_base_disappear.GetCubeFromDisappearList();    
        if (cube) {
            m_list_cubes_base.remove(cube);
            done_base = false;
        }
 
	    if (done_base) {
            m_timeout -= dt;
        
            if (m_timeout <= 0.0f) {
                LevelCube pLevelCube;
            
                pLevelCube = getLevelCubeFrom(m_lst_level_cubes_x_plus);
                if (pLevelCube) {
                    Game.ar_cubefacedata[Face_X_Plus].lst_level_cubes.add(pLevelCube);
                    m_lst_level_cubes_x_plus.remove(pLevelCube);
                }
            
                pLevelCube = GetLevelCubeFrom(m_lst_level_cubes_y_plus);
                if (pLevelCube) {
                    Game.ar_cubefacedata[Face_Y_Plus].lst_level_cubes.add(pLevelCube);
                    m_lst_level_cubes_y_plus.remove(pLevelCube);
                }
            
                pLevelCube = GetLevelCubeFrom(m_lst_level_cubes_z_plus);
                if (pLevelCube) {
                    Game.ar_cubefacedata[Face_Z_Plus].lst_level_cubes.add(pLevelCube);
                    m_lst_level_cubes_z_plus.remove(pLevelCube);
                }
            }
        }
    
        if (m_lst_level_cubes_x_plus.empty() && m_lst_level_cubes_y_plus.empty() && m_lst_level_cubes_z_plus.empty()) {
		    pCube = m_ad_face.getCubeFromAppearList();
		
		    if (pCube) {
			    m_list_cubes_face.add(pCube);
            }
        
            //engine->StopMusic();
		
		    m_anim_to_menu_phase = 1;
		
		    m_t_camera = 0.0f;
		    m_camera_from.init(m_camera_current);
		    m_camera_to.init(Game.m_menu->m_camera_menu);
		
		    m_cube_rotation.degree = -45.0f;
		    m_target_rotation_degree = -90.0f;
		    m_interpolator.setup(m_cube_rotation.degree, m_target_rotation_degree, 6);
	    }
    }

    public void updateAnimToLevel(float dt) {	
	    m_timeout -= dt;
	
	    if (m_timeout <= 0.0f) {
		    LevelCube pLevelCube;
		
		    pLevelCube = getLevelCubeFrom(engine->ar_cubefacedata[Face_X_Plus].lst_level_cubes);
		    if (pLevelCube) {
			    Game.ar_cubefacedata[Face_X_Plus].lst_level_cubes.remove(pLevelCube);
			
			    if (pLevelCube == m_level_cube_hilite) {
				    Game.ar_cubefacedata[Face_X_Plus].lst_level_cubes.add(0, pLevelCube);
                }
		    }
		
		    //printf("\nlevelcubes count: %lu", engine->ar_cubefacedata[Face_X_Plus].lst_level_cubes.size());
		
		    pLevelCube = GetLevelCubeFrom(engine->ar_cubefacedata[Face_Y_Plus].lst_level_cubes);
		    if (pLevelCube) {
			    Game.ar_cubefacedata[Face_Y_Plus].lst_level_cubes.remove(pLevelCube);
            }
		
		    pLevelCube = GetLevelCubeFrom(engine->ar_cubefacedata[Face_Z_Plus].lst_level_cubes);
		    if (pLevelCube) {
			    Game.ar_cubefacedata[Face_Z_Plus].lst_level_cubes.remove(pLevelCube);
            }
				
		    m_timeout = 0.03f;
	    }
	
	    //engine->IncT(m_hilite_alpha);
	
	    boolean done = true;

	    Cube cube;
	
	    // face
	    cube = m_ad_face.getCubeFromDisappearList();
	    if (cube) {
		    m_list_cubes_face.remove(cube);
		    done = false;
	    }

        cube = m_ad_face.getCubeFromDisappearList();
	    if (cube) {
		    m_list_cubes_face.remove(cube);
		    done = false;
	    }	
    
	    // base
	    cube = m_ad_base_disappear.getCubeFromDisappearList();	
	    if (cube) {
		    m_list_cubes_base.remove(cube);
	
		    cube = m_ad_base_disappear.getCubeFromDisappearList();
		    if (cube) {
			    m_list_cubes_base.remove(cube);
			    done = false;
		    }
        
		    cube = m_ad_base_disappear.getCubeFromDisappearList();
		    if (cube) {		
			    m_list_cubes_base.remove(cube);
			    done = false;
		    }        
	    }
	
	    if (done) {
		    if (1 == engine->ar_cubefacedata[Face_X_Plus].lst_level_cubes.size()) {
			    engine->ar_cubefacedata[Face_X_Plus].lst_level_cubes.clear();
            }
		
		    cube = m_ad_base_appear.getCubeFromAppearList();
		
		    if (cube) {
			    m_list_cubes_base.add(cube);
            }
	    }
	
        m_t += 0.05f;
        if (m_t > 1.0f) {
            m_t = 1.0f;
        }
    
        Utils.lerpCamera(m_camera_from, m_camera_to, m_t, m_camera_current);
        Utils.lerpVec3(m_pos_light_from, m_pos_light_to, m_t, m_pos_light_current);

	    m_interpolator.interpolate();
        m_cube_rotation.degree = m_interpolator.getValue();

        float diff = m_target_rotation_degree - m_interpolator.getValue();
    
        if (diff < 0.1f && (1.0f - m_t) < EPSILON && m_ad_base_appear.lst_appear.isEmpty()) {
            m_cube_rotation.degree = m_target_rotation_degree;
		    Game.ShowScene(Scene_Level);
        }
    }

    public void updateCubes(float dt) {
	    for (int i = 0; i < 6; ++i) {
		    m_lst_titles[i].clear();
		    m_lst_texts[i].clear();
            m_lst_symbols[i].clear();
	    }
	
	    CubeFaceTypesEnum face_type;	
	    Cube cube;	
        list<cCube*>::iterator it;
	
	    for (it = m_list_cubes_base.begin(); it != m_list_cubes_base.end(); ++it) {
		    pCube = *it;
		
		    face_type = Face_X_Plus;
		    if (null != pCube->ar_fonts[face_type]) {
			    m_lst_texts[face_type].add(pCube->ar_fonts[face_type]);
            }
				
            if (null != pCube->ar_symbols[face_type]) {
                m_lst_symbols[face_type].add(pCube->ar_symbols[face_type]);
            }
        
		    face_type = Face_Y_Plus;
		    if (null != pCube->ar_fonts[face_type]) {
			    m_lst_texts[face_type].add(pCube->ar_fonts[face_type]);
            }

            if (null != pCube->ar_symbols[face_type]) {
                m_lst_symbols[face_type].add(pCube->ar_symbols[face_type]);
            }
        
		    face_type = Face_Z_Plus;
		    if (null != pCube->ar_fonts[face_type]) {
			    m_lst_texts[face_type].add(pCube->ar_fonts[face_type]);
            }
        
            if (null != pCube->ar_symbols[face_type]) {
                m_lst_symbols[face_type].add(pCube->ar_symbols[face_type]);
            }
	    }
	
	    for (it = m_list_cubes_face.begin(); it != m_list_cubes_face.end(); ++it) {
		    pCube = *it;
				
		    face_type = Face_X_Plus;
		    if (null != pCube->ar_fonts[face_type]) {            
			    m_lst_titles[face_type].add(pCube->ar_fonts[face_type]);
            }
				
		    face_type = Face_Y_Plus;
		    if (null != pCube->ar_fonts[face_type]) {
			    m_lst_titles[face_type].add(pCube->ar_fonts[face_type]);
            }
	
		    face_type = Face_Z_Plus;
		    if (null != pCube->ar_fonts[face_type]) {
			    m_lst_titles[face_type].add(pCube->ar_fonts[face_type]);
            }
	    }
    }

    public void update() {
        switch (m_type) {
            case AnimToLevel:
                updateAnimToLevel(dt);
                break;
            
		    case AnimToMenuFromCompleted:
            case AnimToMenuFromPaused:
                updateAnimToMenu(dt);
                break;
            
            default:
                break;
        }
	
	    updateCubes(dt);
    }

    public void drawTheCube() {        
	    glEnable(GL_LIGHTING);
	    glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, engine->texture_id_gray_concrete);
	
        Graphics.prepare();
        Graphics.setStreamSource();

	    Color color = Game.getBaseColor();
        int len;
        Cube cube;
        len = m_list_cubes_base.size();
        for (int i = 0; i < size; ++i) {
            cube = m_list_cubes_face.get(i);
            Graphics.addCubeSize(cube.tx, cube.ty, cube.tz, HALF_CUBE_SIZE, color);
        }
    
        Graphics.renderTriangles(Graphics.cube_offset.x, Graphics.cube_offset.y, Graphics.cube_offset.z);
	
	    color = Graphics.getFaceColor();
	    Graphics.prepare();
        len = m_list_cubes_face.size();
        for (int i = 0; i < len; ++i) {
            cube = m_list_cubes_face.get(i);
            Graphics.addCubeSize(cube.tx, cube.ty, cube.tz, HALF_CUBE_SIZE, color);
        }
	
	    Graphics.renderTriangles(engine->cube_offset.x, engine->cube_offset.y, engine->cube_offset.z);
    }

    public void drawLevelCubes() {
	    Graphics.prepare();
	    Graphics.setStreamSource();
	
	    LevelCube cube;
        int len = Game.ar_cubefacedata[Face_X_Plus].lst_level_cubes.size();
	    for (int i = 0; i < len; ++i) {
            cube = Game.ar_cubefacedata[Face_X_Plus].lst_level_cubes.get(i);
		    Graphics.addCube(cube.pos.x, cube.pos.y, cube.pos.z);
        }

        len = Game.ar_cubefacedata[Face_Y_Plus].lst_level_cubes.size();
	    for (int i = 0; i < len; ++i) {
            cube = Game.ar_cubefacedata[Face_Y_Plus].lst_level_cubes.get(i);
		    Graphics.addCube(cube.pos.x, cube.pos.y, cube.pos.z);
        }
	
        len = Game.ar_cubefacedata[Face_Z_Plus].lst_level_cubes.size();
	    for (int i = 0; i < len; ++i) {
            cube = Game.ar_cubefacedata[Face_Z_Plus].lst_level_cubes.get(i);
		    Graphics.addCube(cube.pos.x, cube.pos.y, cube.pos.z);
        }
	
	    if (Graphics._vertices_count > 0) {
		    glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_level_cubes);
		    Graphics.renderTriangles();
	    }
    }

    public void drawLevelCubeDecals(LevelCubeDecalTypeEnum decal_type) {
	    Graphics.prepare();
	
	    TexCoordsQuad coords;
        cLevelCube* pLevelCube;
	    TexturedQuad* p;
        Color* color;
	    CubeFaceTypesEnum face_type;
	    list<cLevelCube*>::iterator it;

	    face_type = Face_X_Plus;
	    for(it = engine->ar_cubefacedata[face_type].lst_level_cubes.begin(); it != engine->ar_cubefacedata[face_type].lst_level_cubes.end(); ++it) {
		    pLevelCube = *it;
            p = null;

            switch (decal_type) {
                case LevelCubeDecalNumber:
                    p = pLevelCube->pNumber;
                    color = &pLevelCube->color_number;
                    break;
                
                case LevelCubeDecalStars:
                    p = pLevelCube->pStars;
                    color = &pLevelCube->color_stars_and_solver;
                    break;
                
                case LevelCubeDecalSolver:
                    p = pLevelCube->pSolver;
                    color = &pLevelCube->color_stars_and_solver;
                    break;
            }

            if (p) {
                coords.tx0 = vec2(p->tx_up_right.x, p->tx_up_right.y);
                coords.tx1 = vec2(p->tx_up_left.x,  p->tx_up_left.y);
                coords.tx2 = vec2(p->tx_lo_left.x,  p->tx_lo_left.y);
                coords.tx3 = vec2(p->tx_lo_right.x, p->tx_lo_right.y);
            
                Graphics.addCubeFace_X_Plus((*it)->pos.x, (*it)->pos.y, (*it)->pos.z, coords, *color);
            }
	    }
	
	    face_type = Face_Y_Plus;
	    for(it = engine->ar_cubefacedata[face_type].lst_level_cubes.begin(); it != engine->ar_cubefacedata[face_type].lst_level_cubes.end(); ++it) {
		    pLevelCube = *it;
            p = null;
        
            switch (decal_type) {
                case LevelCubeDecalNumber:
                    p = pLevelCube->pNumber;
                    color = &pLevelCube->color_number;
                    break;
                
                case LevelCubeDecalStars:
                    p = pLevelCube->pStars;
                    color = &pLevelCube->color_stars_and_solver;
                    break;
                
                case LevelCubeDecalSolver:
                    p = pLevelCube->pSolver;
                    color = &pLevelCube->color_stars_and_solver;
                    break;
            }
        
            if (p) {
                coords.tx0 = new Vector2(p->tx_lo_left.x,  p->tx_lo_left.y);
                coords.tx1 = vec2(p->tx_lo_right.x, p->tx_lo_right.y);
                coords.tx2 = vec2(p->tx_up_right.x, p->tx_up_right.y);
                coords.tx3 = vec2(p->tx_up_left.x,  p->tx_up_left.y);

                Graphics.addCubeFace_Y_Plus((*it)->font_pos.x, (*it)->font_pos.y, (*it)->font_pos.z, coords, *color);
            }
	    }
	
	    face_type = Face_Z_Plus;
	    for(it = engine->ar_cubefacedata[face_type].lst_level_cubes.begin(); it != engine->ar_cubefacedata[face_type].lst_level_cubes.end(); ++it) {
		    pLevelCube = *it;
            p = null;
        
            switch (decal_type) {
                case LevelCubeDecalNumber:
                    p = pLevelCube->pNumber;
                    color = &pLevelCube->color_number;
                    break;
                
                case LevelCubeDecalStars:
                    p = pLevelCube->pStars;
                    color = &pLevelCube->color_stars_and_solver;
                    break;
                
                case LevelCubeDecalSolver:
                    p = pLevelCube->pSolver;
                    color = &pLevelCube->color_stars_and_solver;
                    break;
            }

            if (p) {
                coords.tx0 = new Vector2(p->tx_up_right.x, p->tx_up_right.y);
                coords.tx1 = new Vector2(p->tx_up_left.x,  p->tx_up_left.y);
                coords.tx2 = new Vector2(p->tx_lo_left.x,  p->tx_lo_left.y);
                coords.tx3 = new Vector2(p->tx_lo_right.x, p->tx_lo_right.y);

                Graphics.addCubeFace_Z_Plus((*it)->pos.x, (*it)->pos.y, (*it)->pos.z, coords, *color);
            }
	    }
	
	    Graphics.renderTriangles();
    }

    public void drawTexts(list<cCubeFont*>& lst_face_x_plus, list<cCubeFont*>& lst_face_y_plus, list<cCubeFont*>& lst_face_z_plus, Color color) {
	    list<cCubeFont*>::iterator it;
	    TexturedQuad* pFont;
	    TexCoordsQuad coords;
	
	    Graphics.prepare();

	    for (it = lst_face_x_plus.begin(); it != lst_face_x_plus.end(); ++it) {
		    pFont = (*it)->GetFont();
		
		    coords.tx0 = new Vector2(pFont->tx_up_right.x, pFont->tx_up_right.y);
		    coords.tx1 = new Vector2(pFont->tx_up_left.x,  pFont->tx_up_left.y);
		    coords.tx2 = new Vector2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);
		    coords.tx3 = new Vector2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);
		
		    Graphics.addCubeFace_X_Plus((*it)->pos.x, (*it)->pos.y, (*it)->pos.z, coords, color);
	    }
	
	    for (it = lst_face_y_plus.begin(); it != lst_face_y_plus.end(); ++it) {
		    pFont = (*it)->GetFont();
		
		    coords.tx0 = new Vector2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);
		    coords.tx1 = new Vector2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);
		    coords.tx2 = new Vector2(pFont->tx_up_right.x, pFont->tx_up_right.y);
		    coords.tx3 = new Vector2(pFont->tx_up_left.x,  pFont->tx_up_left.y);
		
		    Graphics.addCubeFace_Y_Plus((*it)->pos.x, (*it)->pos.y, (*it)->pos.z, coords, color);
	    }

	    for (it = lst_face_z_plus.begin(); it != lst_face_z_plus.end(); ++it) {
		    pFont = (*it)->GetFont();
					
		    coords.tx0 = vec2(pFont->tx_up_right.x, pFont->tx_up_right.y);
		    coords.tx1 = vec2(pFont->tx_up_left.x,  pFont->tx_up_left.y);
		    coords.tx2 = vec2(pFont->tx_lo_left.x,  pFont->tx_lo_left.y);
		    coords.tx3 = vec2(pFont->tx_lo_right.x, pFont->tx_lo_right.y);
		
		    Graphics.addCubeFace_Z_Plus((*it)->pos.x, (*it)->pos.y, (*it)->pos.z, coords, color);
	    }
	
	    Graphics.renderTriangles();
    }

    public void render() {
        Graphics.setProjection2D();
        Graphics.setModelViewMatrix2D();
    
        glEnable(GL_BLEND);
        glDisable(GL_LIGHTING);
        glDepthMask(GL_FALSE);
        glEnable(GL_TEXTURE_2D);
    
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);
    
        Color color = new Color(255, 255, 255, engine->dirty_alpha);
        Graphics.drawFBOTexture(engine->texture_id_dirty, color);
    
        glDepthMask(GL_TRUE);
    
        Graphics.setProjection3D();
        Graphics.setModelViewMatrix3D(m_camera_current);
	
        const vec4 lightPosition(m_pos_light_current.x, m_pos_light_current.y, m_pos_light_current.z, 1.0f);
        glLightfv(GL_LIGHT0, GL_POSITION, lightPosition.Pointer());
	
        glEnable(GL_LIGHTING);
    
	    glRotatef(m_cube_rotation.degree, m_cube_rotation.axis.x, m_cube_rotation.axis.y, m_cube_rotation.axis.z);
	
        drawTheCube();
    
        if (false) { // DRAW_AXES_CUBE
            glDisable(GL_TEXTURE_2D);
            glDisable(GL_LIGHTING);
            engine->DrawAxes();
            glEnable(GL_LIGHTING);
            glEnable(GL_TEXTURE_2D);
        }

	    glPushMatrix();
	    glTranslatef(engine->cube_offset.x, engine->cube_offset.y, engine->cube_offset.z);

	    drawLevelCubes();
    
        glDisable(GL_LIGHTING);
    

	    Graphics.enableBlending();
	    glDisableClientState(GL_NORMAL_ARRAY);
	
	    Graphics.setStreamSourceFloatAndColor();
	
        color = cEngine::GetTextColor();
        glBindTexture(GL_TEXTURE_2D, engine->texture_id_fonts);
        drawTexts(m_lst_texts[Face_X_Plus], m_lst_texts[Face_Y_Plus], m_lst_texts[Face_Z_Plus], color);
	
	    color = cEngine::GetTitleColor();
	    drawTexts(m_lst_titles[Face_X_Plus], m_lst_titles[Face_Y_Plus], m_lst_titles[Face_Z_Plus], color);

	    color = cEngine::GetSymbolColor();
        glBindTexture(GL_TEXTURE_2D, engine->texture_id_numbers);
        drawLevelCubeDecals(LevelCubeDecalNumber);
	
        glBindTexture(GL_TEXTURE_2D, engine->texture_id_symbols);
        drawLevelCubeDecals(LevelCubeDecalStars);
        drawLevelCubeDecals(LevelCubeDecalSolver);
    
        drawTexts(m_lst_symbols[Face_X_Plus], m_lst_symbols[Face_Y_Plus], m_lst_symbols[Face_Z_Plus], color);
    
	    Graphics.disableBlending();
	
	    glPopMatrix();
    }    

    public void onFingerDown(float x, float y, int finger_count) {
    }
    
    public void onFingerUp(float x, float y, int finger_count) {
    }
    
    public void onFingerMove(float prev_x, float prev_y, float cur_x, float cur_y, int finger_count) {
    }

}
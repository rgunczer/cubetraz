package com.almagems.cubetraz.scenes.anim;

import com.almagems.cubetraz.game.Engine;
import com.almagems.cubetraz.graphics.Graphics;
import com.almagems.cubetraz.utils.AppearDisappearListData;
import com.almagems.cubetraz.graphics.Camera;
import com.almagems.cubetraz.graphics.Color;
import com.almagems.cubetraz.scenes.Scene;
import com.almagems.cubetraz.scenes.Creator;
import com.almagems.cubetraz.cubes.Cube;
import com.almagems.cubetraz.cubes.CubeFont;
import com.almagems.cubetraz.utils.CubeRotation;
import com.almagems.cubetraz.utils.EaseOutDivideInterpolation;
import com.almagems.cubetraz.game.Game;
import com.almagems.cubetraz.cubes.LevelCube;
import com.almagems.cubetraz.scenes.menu.MenuFaceBuilder;
import com.almagems.cubetraz.graphics.TexCoordsQuad;
import com.almagems.cubetraz.graphics.TexturedQuad;
import com.almagems.cubetraz.math.Utils;
import com.almagems.cubetraz.math.Vector;
import com.almagems.cubetraz.math.Vector2;


import java.util.ArrayList;
import static android.opengl.GLES10.*;
import static com.almagems.cubetraz.game.Constants.*;


public final class Animator extends Scene {

    private float m_t;
	private float m_t_camera;
	private float m_timeout;
	private float m_target_rotation_degree;
	
	private int m_anim_to_menu_phase;

	private final Vector m_pos_light_from = new Vector();
	private final Vector m_pos_light_to = new Vector();
	
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
    //private final EaseOutDivideInterpolation[] m_interpolators = new EaseOutDivideInterpolation[6];

	private final CubeRotation m_cube_rotation = new CubeRotation();
	
	private final ArrayList<ArrayList<CubeFont>> m_lst_titles = new ArrayList<>(6);
	private final ArrayList<ArrayList<CubeFont>> m_lst_texts = new ArrayList<>(6);
    private final ArrayList<ArrayList<CubeFont>> m_lst_symbols = new ArrayList<>(6);
	
	private final ArrayList<LevelCube> m_lst_level_cubes_x_plus = new ArrayList<>();
	private final ArrayList<LevelCube> m_lst_level_cubes_y_plus = new ArrayList<>();
	private final ArrayList<LevelCube> m_lst_level_cubes_z_plus = new ArrayList<>();
	
// cubes
	private final ArrayList<Cube> m_list_cubes_base = new ArrayList<>();
    private final ArrayList<Cube> m_list_cubes_face = new ArrayList<>();
	
	private final AppearDisappearListData m_ad_base_disappear = new AppearDisappearListData();
	private final AppearDisappearListData m_ad_base_appear = new AppearDisappearListData();
	private final AppearDisappearListData m_ad_face = new AppearDisappearListData();


    // ctor   
    public Animator() {
	    m_face_name_x_plus = CubeFaceNamesEnum.Face_Empty;
	    m_face_name_y_plus = CubeFaceNamesEnum.Face_Empty;
	    m_face_name_z_plus = CubeFaceNamesEnum.Face_Empty;

        for(int i = 0; i < 6; ++i) {
            m_lst_titles.add(new ArrayList<CubeFont>());
            m_lst_texts.add(new ArrayList<CubeFont>());
            m_lst_symbols.add(new ArrayList<CubeFont>());
        }
    }

    @Override
    public void init() {
        m_level_cube_hilite = null;
    
	    AnimInitData aid = Game.anim_init_data;
	    m_type = aid.type;
	    m_t_camera = 0f;
		
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
	    updateCubes();
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
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.RotateCCW90);
	    MenuFaceBuilder.build(m_face_name_y_plus, Face_Y_Plus);
	    MenuFaceBuilder.build(m_face_name_z_plus, Face_Z_Plus);
    }

	private LevelCube getLevelCubeFrom(ArrayList<LevelCube> lst) {
		if (lst.isEmpty()) {
			return null;
		} else {
			return lst.get( lst.size() - 1);
		}
	}

	private Cube getCubeFrom(ArrayList<Cube> lst) {
		if (lst.isEmpty()) {
			return null;
		} else {
			return lst.get( lst.size() - 1);
		}
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
        len = Game.ar_cubefacedata[Face_Z_Plus].lst_level_cubes.size();
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
	
	    m_ad_face.clear();
	    Game.buildVisibleCubesListOnlyOnFaces(m_ad_face.lst_appear);
	    m_list_cubes_face.clear();

	    createListOfLevelCubes(m_list_cubes_base);
		
	    aid.list_cubes_base.clear();
	    m_ad_base_disappear.clear();

	    ArrayList<Cube> lst = Game.createBaseCubesList();
	
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
		    if ( !Game.isOnAList(cube, m_list_cubes_base)) {
			    m_ad_base_appear.addAppear(cube);
            }
	    }
	
	    localCopyLevelCubes();
	
	    m_cube_rotation.degree = aid.cube_rotation_degree;
	    m_target_rotation_degree = -45.0f;
	
	    m_interpolator.setup(m_cube_rotation.degree, m_target_rotation_degree, 9);
		
	    m_camera_from.init(Game.level.getCameraCurrent());
	    m_camera_to.init(Game.level.m_camera_level);
	    m_camera_current.init(m_camera_from);
	
	    m_pos_light_from.init(Game.level.getLightPositionCurrent());
	    m_pos_light_to.init(Game.menu.getLightPositionCurrent());
	    mPosLightCurrent.init(m_pos_light_from);

	    m_ad_face.setLevelAndDirection(0, 1);
	    m_ad_base_appear.setLevelAndDirection(0, 1);
	    m_ad_base_disappear.setLevelAndDirection(0, 1);
    }

    public void setupAnimToLevel() {
	    //printf("\nSetup Anim To Level...");
	    AnimInitData aid = Game.anim_init_data;

	    m_hilite_alpha = 0f;
	    m_t = 0f;
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

        Cube cube;
		int size = aid.list_cubes_base.size();
	    for (int i = 0; i < size; ++i) {
            cube = aid.list_cubes_base.get(i);
		    m_list_cubes_base.add(cube);
        }
	
	    aid.list_cubes_base.clear();
	
	    m_camera_from.init(aid.camera_from);
	    m_camera_to.init(aid.camera_to);
	    m_camera_current.init(m_camera_from);
	
	    m_pos_light_from.init(aid.pos_light_from);
        m_pos_light_to.init(aid.pos_light_to);
	    mPosLightCurrent.init(m_pos_light_from);
	
	    m_ad_base_disappear.clear();
	        
	    ArrayList<Cube> lst_level = new ArrayList<>();
	    createListOfLevelCubes(lst_level);
	    size = m_list_cubes_base.size();
	    for (int i = 0; i < size; ++i) {
            cube = m_list_cubes_base.get(i);
		    if ( !Game.isOnAList(cube, lst_level) ) {
			    m_ad_base_disappear.addDisappear(cube);
            }
	    }

	    m_ad_base_appear.clear();
	    for (int i = 6; i > 0; --i) {
		    m_ad_base_appear.addAppear(Game.cubes[8][i][1]);
		    m_ad_base_appear.addAppear(Game.cubes[1][i][8]);
	    }
	
	    for (int i = 2; i < 8; ++i) {
		    m_ad_base_appear.addAppear(Game.cubes[8][1][i]);
		    m_ad_base_appear.addAppear(Game.cubes[i][1][8]);
	    }
	
	    m_ad_base_appear.addAppear(Game.cubes[8][1][8]);

	    m_ad_face.setLevelAndDirection(MAX_CUBE_COUNT - 1, -1);
	    m_ad_base_disappear.setLevelAndDirection(MAX_CUBE_COUNT - 1, -1);
	    m_ad_base_appear.setLevelAndDirection(MAX_CUBE_COUNT - 1, -1);

	    locateLevelCube();
    }

    public void locateLevelCube() {
	    int level_number = Game.level_init_data.level_number;
        int size = Game.ar_cubefacedata[Face_X_Plus].lst_level_cubes.size();
	    LevelCube levelCube;
	    for(int i = 0; i < size; ++i) {
            levelCube = Game.ar_cubefacedata[Face_X_Plus].lst_level_cubes.get(i);
		    if (levelCube.level_number == level_number) {
                m_level_cube_hilite = levelCube;
                m_level_cube_hilite.color_number = new Color(255, 255, 0, 255);
                return;
            }
	    }
    }

    public void updateAnimToMenu() {
	    if (0 == m_anim_to_menu_phase) {
		    updateAnimToMenuPhaseOne();
        } else {
		    updateAnimToMenuPhaseTwo();
        }
    }

    public void updateAnimToMenuPhaseTwo() {
        m_interpolator.interpolate();
        m_cube_rotation.degree = m_interpolator.getValue();

        m_t_camera += 0.04f;
        if (m_t_camera > 1f) {
            m_t_camera = 1f;
        }
	    Utils.lerpCamera(m_camera_from, m_camera_to, m_t_camera, m_camera_current);
	
	    Cube cube;    
        cube = m_ad_face.getCubeFromAppearList();
	    if (cube != null) {
		    m_list_cubes_face.add(cube);
	    }

        cube = m_ad_face.getCubeFromAppearList();
	    if (cube != null) {
		    m_list_cubes_face.add(cube);
	    }

        cube = m_ad_face.getCubeFromAppearList(); 
	    if (cube != null) {
		    m_list_cubes_face.add(cube);
	    }
    
        float diff = Math.abs( Math.abs(m_target_rotation_degree) - Math.abs(m_interpolator.getValue()));
	
        if (diff < EPSILON && Math.abs(1.0f - m_t) < EPSILON && Math.abs(1.0f - m_t_camera) < EPSILON && m_ad_face.lst_appear.isEmpty()) {
		    Game.menu_init_data.reappear = true;
		    Game.showScene(Scene_Menu);
        }
    }

    public void updateAnimToMenuPhaseOne() {
        m_interpolator.interpolate();
        m_cube_rotation.degree = m_interpolator.getValue();

        m_t += 0.04f;
        if (m_t > 1f) {
            m_t = 1f;
        }
        Utils.lerpVector3(m_pos_light_from, m_pos_light_to, m_t, mPosLightCurrent);
	    Utils.lerpCamera(m_camera_from, m_camera_to, m_t, m_camera_current);
		
        Cube cube;    
	    boolean done_base = true;
	
        cube = m_ad_base_appear.getCubeFromAppearList();    
        if (cube != null) {
            m_list_cubes_base.add(cube);
            done_base = false;
        }
    
        cube = m_ad_base_appear.getCubeFromAppearList();
        if (cube != null) {
            m_list_cubes_base.add(cube);
            done_base = false;
        }

        cube = m_ad_base_disappear.getCubeFromDisappearList();
        if (cube != null) {
            m_list_cubes_base.remove(cube);
            done_base = false;
        }
 
	    if (done_base) {
            m_timeout -= 0.01f;
        
            if (m_timeout <= 0.0f) {
                LevelCube levelCube;
            
                levelCube = getLevelCubeFrom(m_lst_level_cubes_x_plus);
                if (levelCube != null) {
                    Game.ar_cubefacedata[Face_X_Plus].lst_level_cubes.add(levelCube);
                    m_lst_level_cubes_x_plus.remove(levelCube);
                }
            
                levelCube = getLevelCubeFrom(m_lst_level_cubes_y_plus);
                if (levelCube != null) {
                    Game.ar_cubefacedata[Face_Y_Plus].lst_level_cubes.add(levelCube);
                    m_lst_level_cubes_y_plus.remove(levelCube);
                }
            
                levelCube = getLevelCubeFrom(m_lst_level_cubes_z_plus);
                if (levelCube != null) {
                    Game.ar_cubefacedata[Face_Z_Plus].lst_level_cubes.add(levelCube);
                    m_lst_level_cubes_z_plus.remove(levelCube);
                }
            }
        }
    
        if (m_lst_level_cubes_x_plus.isEmpty() && m_lst_level_cubes_y_plus.isEmpty() && m_lst_level_cubes_z_plus.isEmpty()) {
		    cube = m_ad_face.getCubeFromAppearList();
		
		    if (cube != null) {
			    m_list_cubes_face.add(cube);
            }
        
            Game.audio.stopMusic();
		
		    m_anim_to_menu_phase = 1;
		
		    m_t_camera = 0.0f;
		    m_camera_from.init(m_camera_current);
		    m_camera_to.init(Game.menu.mCameraMenu);
		
		    m_cube_rotation.degree = -45.0f;
		    m_target_rotation_degree = -90.0f;
		    m_interpolator.setup(m_cube_rotation.degree, m_target_rotation_degree, 6);
	    }
    }

    public void updateAnimToLevel() {
	    m_timeout -= 0.04f;
	
	    if (m_timeout <= 0f) {
		    LevelCube levelCube;
		
		    levelCube = getLevelCubeFrom(Game.ar_cubefacedata[Face_X_Plus].lst_level_cubes);
		    if (levelCube != null) {
			    Game.ar_cubefacedata[Face_X_Plus].lst_level_cubes.remove(levelCube);
			
			    if (levelCube == m_level_cube_hilite) {
				    Game.ar_cubefacedata[Face_X_Plus].lst_level_cubes.add(0, levelCube);
                }
		    }
		
		    //printf("\nlevelcubes count: %lu", engine.ar_cubefacedata[Face_X_Plus].lst_level_cubes.size());
		
		    levelCube = getLevelCubeFrom(Game.ar_cubefacedata[Face_Y_Plus].lst_level_cubes);
		    if (levelCube != null) {
			    Game.ar_cubefacedata[Face_Y_Plus].lst_level_cubes.remove(levelCube);
            }
		
		    levelCube = getLevelCubeFrom(Game.ar_cubefacedata[Face_Z_Plus].lst_level_cubes);
		    if (levelCube != null) {
			    Game.ar_cubefacedata[Face_Z_Plus].lst_level_cubes.remove(levelCube);
            }
		    m_timeout = 0.03f;
	    }

        m_hilite_alpha += 0.01f;
	
	    boolean done = true;
	    Cube cube;
	
	    // face
	    cube = m_ad_face.getCubeFromDisappearList();
	    if (cube !=  null) {
		    m_list_cubes_face.remove(cube);
		    done = false;
	    }

        cube = m_ad_face.getCubeFromDisappearList();
	    if (cube != null) {
		    m_list_cubes_face.remove(cube);
		    done = false;
	    }
    
	    // base
	    cube = m_ad_base_disappear.getCubeFromDisappearList();	
	    if (cube != null) {
		    m_list_cubes_base.remove(cube);
	
		    cube = m_ad_base_disappear.getCubeFromDisappearList();
		    if (cube != null) {
			    m_list_cubes_base.remove(cube);
			    done = false;
		    }

		    cube = m_ad_base_disappear.getCubeFromDisappearList();
		    if (cube != null) {
			    m_list_cubes_base.remove(cube);
			    done = false;
		    }
	    }
	
	    if (done) {
		    if (1 == Game.ar_cubefacedata[Face_X_Plus].lst_level_cubes.size()) {
			    Game.ar_cubefacedata[Face_X_Plus].lst_level_cubes.clear();
            }
		
		    cube = m_ad_base_appear.getCubeFromAppearList();
		    if (cube != null) {
			    m_list_cubes_base.add(cube);
            }
	    }
	
        m_t += 0.04f;
        if (m_t > 1f) {
            m_t = 1f;
        }
    
        Utils.lerpCamera(m_camera_from, m_camera_to, m_t, m_camera_current);
        Utils.lerpVector3(m_pos_light_from, m_pos_light_to, m_t, mPosLightCurrent);

	    m_interpolator.interpolate();
        m_cube_rotation.degree = m_interpolator.getValue();

        float diff = m_target_rotation_degree - m_interpolator.getValue();
        if (diff < 0.1f && (1.0f - m_t) < EPSILON && m_ad_base_appear.lst_appear.isEmpty()) {
            m_cube_rotation.degree = m_target_rotation_degree;
		    Game.showScene(Scene_Level);
        }
    }

    public void updateCubes() {
	    for (int i = 0; i < 6; ++i) {
		    m_lst_titles.get(i).clear();
		    m_lst_texts.get(i).clear();
            m_lst_symbols.get(i).clear();
	    }

        int size = m_list_cubes_base.size();
	    int face_type;
	    Cube cube;
	    for (int i = 0; i < size; ++i) {
		    cube = m_list_cubes_base.get(i);
		
		    face_type = Face_X_Plus;
		    if (null != cube.ar_fonts[face_type]) {
			    m_lst_texts.get(face_type).add(cube.ar_fonts[face_type]);
            }
				
            if (null != cube.ar_symbols[face_type]) {
                m_lst_symbols.get(face_type).add(cube.ar_symbols[face_type]);
            }
        
		    face_type = Face_Y_Plus;
		    if (null != cube.ar_fonts[face_type]) {
			    m_lst_texts.get(face_type).add(cube.ar_fonts[face_type]);
            }

            if (null != cube.ar_symbols[face_type]) {
                m_lst_symbols.get(face_type).add(cube.ar_symbols[face_type]);
            }
        
		    face_type = Face_Z_Plus;
		    if (null != cube.ar_fonts[face_type]) {
			    m_lst_texts.get(face_type).add(cube.ar_fonts[face_type]);
            }
        
            if (null != cube.ar_symbols[face_type]) {
                m_lst_symbols.get(face_type).add(cube.ar_symbols[face_type]);
            }
	    }

        size = m_list_cubes_face.size();
	    for (int i = 0; i < size; ++i) {
		    cube = m_list_cubes_face.get(i);
				
		    face_type = Face_X_Plus;
		    if (null != cube.ar_fonts[face_type]) {
			    m_lst_titles.get(face_type).add(cube.ar_fonts[face_type]);
            }
				
		    face_type = Face_Y_Plus;
		    if (null != cube.ar_fonts[face_type]) {
			    m_lst_titles.get(face_type).add(cube.ar_fonts[face_type]);
            }
	
		    face_type = Face_Z_Plus;
		    if (null != cube.ar_fonts[face_type]) {
			    m_lst_titles.get(face_type).add(cube.ar_fonts[face_type]);
            }
	    }
    }

    @Override
    public void update() {
        switch (m_type) {
            case AnimToLevel:
                updateAnimToLevel();
                break;

            case AnimToMenuFromCompleted:
            case AnimToMenuFromPaused:
                updateAnimToMenu();
                break;

            default:
                break;
        }
        updateCubes();
    }

    public void drawTheCube() {
		Graphics graphics = Engine.graphics;
	    glEnable(GL_LIGHTING);
	    glEnable(GL_TEXTURE_2D);
		graphics.textureGrayConcrete.bind();
	
        graphics.resetBufferIndices();
        graphics.bindStreamSources3d();

        int size;
        Cube cube;
        size = m_list_cubes_base.size();
        for (int i = 0; i < size; ++i) {
            cube = m_list_cubes_base.get(i);
            graphics.addCubeSize(cube.tx, cube.ty, cube.tz, HALF_CUBE_SIZE, Game.baseColor);
        }
        graphics.updateBuffers();
        graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);
	
	    Color color = new Color(Game.faceColor);
	    graphics.resetBufferIndices();
        size = m_list_cubes_face.size();
        for (int i = 0; i < size; ++i) {
            cube = m_list_cubes_face.get(i);
            graphics.addCubeSize(cube.tx, cube.ty, cube.tz, HALF_CUBE_SIZE, color);
        }
        graphics.updateBuffers();
	    graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);
    }

    private void drawLevelCubes() {
        Graphics graphics = Engine.graphics;
	    graphics.resetBufferIndices();
	    graphics.bindStreamSources3d();
	
	    LevelCube cube;
        int len = Game.ar_cubefacedata[Face_X_Plus].lst_level_cubes.size();
	    for (int i = 0; i < len; ++i) {
            cube = Game.ar_cubefacedata[Face_X_Plus].lst_level_cubes.get(i);
		    graphics.addCube(cube.pos.x, cube.pos.y, cube.pos.z);
        }

        len = Game.ar_cubefacedata[Face_Y_Plus].lst_level_cubes.size();
	    for (int i = 0; i < len; ++i) {
            cube = Game.ar_cubefacedata[Face_Y_Plus].lst_level_cubes.get(i);
		    graphics.addCube(cube.pos.x, cube.pos.y, cube.pos.z);
        }
	
        len = Game.ar_cubefacedata[Face_Z_Plus].lst_level_cubes.size();
	    for (int i = 0; i < len; ++i) {
            cube = Game.ar_cubefacedata[Face_Z_Plus].lst_level_cubes.get(i);
		    graphics.addCube(cube.pos.x, cube.pos.y, cube.pos.z);
        }
	
	    if (graphics._vertices_count > 0) {
			Engine.graphics.textureLevelCubes.bind();
            graphics.updateBuffers();
		    graphics.renderTriangles();
	    }
    }

    public void drawLevelCubeDecals(LevelCubeDecalTypeEnum decal_type) {
        Graphics graphics = Engine.graphics;
	    graphics.resetBufferIndices();
	
	    TexCoordsQuad coords = new TexCoordsQuad();
        LevelCube levelCube;
	    TexturedQuad p;
        Color color = new Color();
	    int face_type;
        int size;

        face_type = Face_X_Plus;
        size = Game.ar_cubefacedata[face_type].lst_level_cubes.size();
	    for(int i = 0; i < size; ++i) {
		    levelCube = Game.ar_cubefacedata[face_type].lst_level_cubes.get(i);
            p = null;

            switch (decal_type) {
                case LevelCubeDecalNumber:
                    p = levelCube.pNumber;
                    color = levelCube.color_number;
                    break;
                
                case LevelCubeDecalStars:
                    p = levelCube.pStars;
                    color = levelCube.color_stars_and_solver;
                    break;
                
                case LevelCubeDecalSolver:
                    p = levelCube.pSolver;
                    color = levelCube.color_stars_and_solver;
                    break;
            }

            if (p != null) {
                coords.tx0 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                coords.tx1 = new Vector2(p.tx_up_left.x,  p.tx_up_left.y);
                coords.tx2 = new Vector2(p.tx_lo_left.x,  p.tx_lo_left.y);
                coords.tx3 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);
            
                graphics.addCubeFace_X_Plus(levelCube.pos.x, levelCube.pos.y, levelCube.pos.z, coords, color);
            }
	    }

        face_type = Face_Y_Plus;
        size = Game.ar_cubefacedata[face_type].lst_level_cubes.size();
	    for(int i = 0; i < size; ++i) {
		    levelCube = Game.ar_cubefacedata[face_type].lst_level_cubes.get(i);
            p = null;
        
            switch (decal_type) {
                case LevelCubeDecalNumber:
                    p = levelCube.pNumber;
                    color = levelCube.color_number;
                    break;
                
                case LevelCubeDecalStars:
                    p = levelCube.pStars;
                    color = levelCube.color_stars_and_solver;
                    break;
                
                case LevelCubeDecalSolver:
                    p = levelCube.pSolver;
                    color = levelCube.color_stars_and_solver;
                    break;
            }
        
            if (p != null) {
                coords.tx0 = new Vector2(p.tx_lo_left.x,  p.tx_lo_left.y);
                coords.tx1 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);
                coords.tx2 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                coords.tx3 = new Vector2(p.tx_up_left.x, p.tx_up_left.y);

                graphics.addCubeFace_Y_Plus(levelCube.font_pos.x, levelCube.font_pos.y, levelCube.font_pos.z, coords, color);
            }
	    }

        face_type = Face_Z_Plus;
        size = Game.ar_cubefacedata[face_type].lst_level_cubes.size();
	    for(int i = 0; i < size; ++i) {
		    levelCube = Game.ar_cubefacedata[face_type].lst_level_cubes.get(i);
            p = null;
        
            switch (decal_type) {
                case LevelCubeDecalNumber:
                    p = levelCube.pNumber;
                    color = levelCube.color_number;
                    break;
                
                case LevelCubeDecalStars:
                    p = levelCube.pStars;
                    color = levelCube.color_stars_and_solver;
                    break;
                
                case LevelCubeDecalSolver:
                    p = levelCube.pSolver;
                    color = levelCube.color_stars_and_solver;
                    break;
            }

            if (p != null) {
                coords.tx0 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                coords.tx1 = new Vector2(p.tx_up_left.x,  p.tx_up_left.y);
                coords.tx2 = new Vector2(p.tx_lo_left.x,  p.tx_lo_left.y);
                coords.tx3 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);

                graphics.addCubeFace_Z_Plus(levelCube.pos.x, levelCube.pos.y, levelCube.pos.z, coords, color);
            }
	    }
        graphics.updateBuffers();
	    graphics.renderTriangles();
    }

    public void drawTexts(ArrayList<CubeFont> lst_face_x_plus, ArrayList<CubeFont> lst_face_y_plus, ArrayList<CubeFont> lst_face_z_plus, Color color) {
        Graphics graphics = Engine.graphics;
	    int size;
        CubeFont cubeFont;
	    TexturedQuad font;
	    TexCoordsQuad coords = new TexCoordsQuad();
	
	    graphics.resetBufferIndices();

        size = lst_face_x_plus.size();
	    for (int i = 0; i < size; ++i) {
            cubeFont = lst_face_x_plus.get(i);
		    font = cubeFont.getFont();
		
		    coords.tx0 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
		    coords.tx1 = new Vector2(font.tx_up_left.x,  font.tx_up_left.y);
		    coords.tx2 = new Vector2(font.tx_lo_left.x,  font.tx_lo_left.y);
		    coords.tx3 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
		
		    graphics.addCubeFace_X_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
	    }

        size = lst_face_y_plus.size();
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_face_y_plus.get(i);
		    font = cubeFont.getFont();
		
		    coords.tx0 = new Vector2(font.tx_lo_left.x,  font.tx_lo_left.y);
		    coords.tx1 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
		    coords.tx2 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
		    coords.tx3 = new Vector2(font.tx_up_left.x,  font.tx_up_left.y);
		
		    graphics.addCubeFace_Y_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
	    }

	    size = lst_face_z_plus.size();
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_face_z_plus.get(i);
            font = cubeFont.getFont();
					
		    coords.tx0 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
		    coords.tx1 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
		    coords.tx2 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
		    coords.tx3 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
		
		    graphics.addCubeFace_Z_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
	    }
	    graphics.updateBuffers();
	    graphics.renderTriangles();
    }

    @Override
    public void render() {
        Graphics graphics = Engine.graphics;
        graphics.setProjection2D();
        graphics.setModelViewMatrix2D();
        graphics.bindStreamSources2d();
        graphics.resetBufferIndices();

        glEnable(GL_BLEND);
        glDisable(GL_LIGHTING);
        glDepthMask(false);// GL_FALSE);
        glEnable(GL_TEXTURE_2D);
    
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);
    
        Color color = new Color(255, 255, 255, (int)Game.dirtyAlpha);
        graphics.drawFullScreenTexture(Engine.graphics.textureDirty, color);
    
        glDepthMask(true); //GL_TRUE);
    
        graphics.setProjection3D();
        graphics.setModelViewMatrix3D(m_camera_current);
        graphics.bindStreamSources3d();
        graphics.resetBufferIndices();

        graphics.setLightPosition(mPosLightCurrent);

        glEnable(GL_LIGHTING);
    
	    glRotatef(m_cube_rotation.degree, m_cube_rotation.axis.x, m_cube_rotation.axis.y, m_cube_rotation.axis.z);
	
        drawTheCube();
    
        if (false) { // DRAW_AXES_CUBE
            glDisable(GL_TEXTURE_2D);
            glDisable(GL_LIGHTING);
            graphics.drawAxes();
            glEnable(GL_LIGHTING);
            glEnable(GL_TEXTURE_2D);
        }

	    glPushMatrix();
	    glTranslatef(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);

	    drawLevelCubes();
    
        glDisable(GL_LIGHTING);

	    glEnable(GL_BLEND);
	    glDisableClientState(GL_NORMAL_ARRAY);
	
	    graphics.bindStreamSources3d();
	
        color = Game.textColor;
		graphics.textureFonts.bind();
        drawTexts(m_lst_texts.get(Face_X_Plus), m_lst_texts.get(Face_Y_Plus), m_lst_texts.get(Face_Z_Plus), color);
	
	    color = Game.titleColor;
	    drawTexts(m_lst_titles.get(Face_X_Plus), m_lst_titles.get(Face_Y_Plus), m_lst_titles.get(Face_Z_Plus), color);

	    color = Game.symbolColor;
		graphics.textureNumbers.bind();
        drawLevelCubeDecals(LevelCubeDecalTypeEnum.LevelCubeDecalNumber);

		graphics.textureSymbols.bind();
        drawLevelCubeDecals(LevelCubeDecalTypeEnum.LevelCubeDecalStars);
        drawLevelCubeDecals(LevelCubeDecalTypeEnum.LevelCubeDecalSolver);
    
        drawTexts(m_lst_symbols.get(Face_X_Plus), m_lst_symbols.get(Face_Y_Plus), m_lst_symbols.get(Face_Z_Plus), color);
    
	    glDisable(GL_BLEND);
	
	    glPopMatrix();
    }    

	@Override
    public void onFingerDown(float x, float y, int finger_count) {}

    @Override
    public void onFingerUp(float x, float y, int finger_count) {}

    @Override
    public void onFingerMove(float prev_x, float prev_y, float cur_x, float cur_y, int finger_count) {}

}
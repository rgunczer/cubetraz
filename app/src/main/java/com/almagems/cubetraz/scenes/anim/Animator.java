package com.almagems.cubetraz.scenes.anim;

import com.almagems.cubetraz.graphics.Graphics;
import com.almagems.cubetraz.scenes.level.AppearDisappearListData;
import com.almagems.cubetraz.graphics.Camera;
import com.almagems.cubetraz.graphics.Color;
import com.almagems.cubetraz.scenes.Scene;
import com.almagems.cubetraz.scenes.Creator;
import com.almagems.cubetraz.cubes.Cube;
import com.almagems.cubetraz.cubes.CubeFont;
import com.almagems.cubetraz.utils.CubeRotation;
import com.almagems.cubetraz.utils.EaseOutDivideInterpolation;
import com.almagems.cubetraz.Game;
import com.almagems.cubetraz.cubes.LevelCube;
import com.almagems.cubetraz.scenes.menu.MenuFaceBuilder;
import com.almagems.cubetraz.graphics.TexCoordsQuad;
import com.almagems.cubetraz.graphics.TexturedQuad;
import com.almagems.cubetraz.math.Utils;
import com.almagems.cubetraz.math.Vector;
import com.almagems.cubetraz.math.Vector2;

import java.util.ArrayList;
import static android.opengl.GLES10.*;
import static com.almagems.cubetraz.Game.*;


public final class Animator extends Scene {

    private float mElapsed;
	private float mElapsedCamera;
	private float mTimeout;
	private float mTargetRotationDegree;
	
	private int mAnimToMenuPhase;

	private final Vector mPosLightFrom = new Vector();
	private final Vector mPosLightTo = new Vector();
	
	private final Camera mCameraFrom = new Camera();
	private final Camera mCameraTo = new Camera();
	
	private LevelCube mLevelCubeHilite;
    
    private final EaseOutDivideInterpolation mInterpolator = new EaseOutDivideInterpolation();

	private final CubeRotation mCubeRotation = new CubeRotation();
	
	private final ArrayList<ArrayList<CubeFont>> mTitles = new ArrayList<>(6);
	private final ArrayList<ArrayList<CubeFont>> mTexts = new ArrayList<>(6);
    private final ArrayList<ArrayList<CubeFont>> mSymbols = new ArrayList<>(6);
	
	private final ArrayList<LevelCube> mLevelCubesXplus = new ArrayList<>();
	private final ArrayList<LevelCube> mLevelCubesYplus = new ArrayList<>();
	private final ArrayList<LevelCube> mLevelCubesZplus = new ArrayList<>();

	private final ArrayList<Cube> mCubesBase = new ArrayList<>();
    private final ArrayList<Cube> mCubesFace = new ArrayList<>();
	
	private final AppearDisappearListData m_ad_base_disappear = new AppearDisappearListData();
	private final AppearDisappearListData m_ad_base_appear = new AppearDisappearListData();
	private final AppearDisappearListData m_ad_face = new AppearDisappearListData();


    public Animator() {
        for(int i = 0; i < 7; ++i) {
            mTitles.add(new ArrayList<CubeFont>());
            mTexts.add(new ArrayList<CubeFont>());
            mSymbols.add(new ArrayList<CubeFont>());
        }
    }

    @Override
    public void init() {
        mLevelCubeHilite = null;
	    mElapsedCamera = 0f;
		
	    Game.resetCubes();
	    Game.clearCubeFaceData();
	
	    switch (Game.animInitData.type) {
		    case AnimToLevel:
			    setupAnimToLevel();
			    break;
			
            case AnimToMenu:
			    setupAnimToMenu();
			    break;
	    }
	    updateCubes();
    }
    
    private void createListOfLevelCubes(ArrayList<Cube> lst) {
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

    private void createMenuFaces() {
        Game.resetCubesFonts();
	
        Creator.fillPools();
	    
        MenuFaceBuilder.resetTransforms();
	    MenuFaceBuilder.build(Game.animInitData.faceNameXPlus, X_Plus);
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.RotateCCW90);
	    MenuFaceBuilder.build(Game.animInitData.faceNameYPlus, Y_Plus);
	    MenuFaceBuilder.build(Game.animInitData.faceNameZPlus, Z_Plus);
    }

	private static LevelCube getLevelCubeFrom(ArrayList<LevelCube> lst) {
		if (lst.isEmpty()) {
			return null;
		} else {
			return lst.get( lst.size() - 1);
		}
	}

    private void localCopyLevelCubes() {
        int len;
        LevelCube cube;
        
	    mLevelCubesXplus.clear();
        len = Game.cubeFacesData[X_Plus].levelCubes.size();
	    for (int i = 0; i < len; ++i) {
            cube = Game.cubeFacesData[X_Plus].levelCubes.get(i);
		    mLevelCubesXplus.add(cube);
	    }
	    Game.cubeFacesData[X_Plus].levelCubes.clear();
	
	    mLevelCubesYplus.clear();
        len = Game.cubeFacesData[Y_Plus].levelCubes.size();
	    for (int i = 0; i < len; ++i) {	
            cube = Game.cubeFacesData[Y_Plus].levelCubes.get(i);
		    mLevelCubesYplus.add(cube);
	    }
	    Game.cubeFacesData[Y_Plus].levelCubes.clear();
	
	    mLevelCubesZplus.clear();
        len = Game.cubeFacesData[Z_Plus].levelCubes.size();
	    for (int i = 0 ; i < len; ++i) {	
            cube = Game.cubeFacesData[Z_Plus].levelCubes.get(i);
		    mLevelCubesZplus.add(cube);
	    }
	    Game.cubeFacesData[Z_Plus].levelCubes.clear();
    }

    private void setupAnimToMenu() {
	    mElapsed = 0.0f;
	    mTimeout = 0.0f;
	    mAnimToMenuPhase = 0;
	
	    AnimInitData aid = Game.animInitData;

	    createMenuFaces();
	
	    m_ad_face.clear();
	    Game.buildVisibleCubesListOnlyOnFaces(m_ad_face.appear);
	    mCubesFace.clear();

	    createListOfLevelCubes(mCubesBase);
		
	    aid.cubesBase.clear();
	    m_ad_base_disappear.clear();

	    ArrayList<Cube> lst = Game.createBaseCubesList();
	
        int len = mCubesBase.size();
	    Cube cube;
	    for (int i = 0; i < len; ++i) {
            cube = mCubesBase.get(i);
		    if ( !Game.isOnAList(cube, lst) ) {
			    m_ad_base_disappear.addDisappear(cube);
            }
	    }

        len = lst.size();
	    for (int i = 0; i < len; ++i) {
            cube = lst.get(i);
		    if ( !Game.isOnAList(cube, mCubesBase)) {
			    m_ad_base_appear.addAppear(cube);
            }
	    }
	
	    localCopyLevelCubes();
	
	    mCubeRotation.degree = aid.cubeRotationDegree;
	    mTargetRotationDegree = -45.0f;
	
	    mInterpolator.setup(mCubeRotation.degree, mTargetRotationDegree, 9);
		
	    mCameraFrom.init(Game.level.getCameraCurrent());
	    mCameraTo.init(Game.level.mCameraLevel);
	    mCameraCurrent.init(mCameraFrom);
	
	    mPosLightFrom.init(Game.level.getLightPositionCurrent());
	    mPosLightTo.init(Game.menu.getLightPositionCurrent());
	    mPosLightCurrent.init(mPosLightFrom);

	    m_ad_face.setLevelAndDirection(0, 1);
	    m_ad_base_appear.setLevelAndDirection(0, 1);
	    m_ad_base_disappear.setLevelAndDirection(0, 1);
    }

    private void setupAnimToLevel() {
	    AnimInitData aid = Game.animInitData;

	    mElapsed = 0f;
	    mTimeout = 0.1f;

	    mCubeRotation.degree = -90.0f;
	    mCubeRotation.axis = new Vector(0.0f, 1.0f, 0.0f);
	
        mTargetRotationDegree = mCubeRotation.degree + 45.0f;
    
        mInterpolator.setup(mCubeRotation.degree, mTargetRotationDegree, 15);
	
	    createMenuFaces();
	
	    Game.buildVisibleCubesListOnlyOnFaces(mCubesFace);
	    m_ad_face.initDisappearListFrom(mCubesFace);
	
	    mCubesBase.clear();

        Cube cube;
		int size = aid.cubesBase.size();
	    for (int i = 0; i < size; ++i) {
            cube = aid.cubesBase.get(i);
		    mCubesBase.add(cube);
        }
	
	    aid.cubesBase.clear();
	
	    mCameraFrom.init(aid.cameraFrom);
	    mCameraTo.init(aid.cameraTo);
	    mCameraCurrent.init(mCameraFrom);
	
	    mPosLightFrom.init(aid.posLightFrom);
        mPosLightTo.init(aid.posLightTo);
	    mPosLightCurrent.init(mPosLightFrom);
	
	    m_ad_base_disappear.clear();
	        
	    ArrayList<Cube> lst_level = new ArrayList<>();
	    createListOfLevelCubes(lst_level);
	    size = mCubesBase.size();
	    for (int i = 0; i < size; ++i) {
            cube = mCubesBase.get(i);
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
	    int level_number = Game.levelInitData.levelNumber;
        int size = Game.cubeFacesData[X_Plus].levelCubes.size();
	    LevelCube levelCube;
	    for(int i = 0; i < size; ++i) {
            levelCube = Game.cubeFacesData[X_Plus].levelCubes.get(i);
		    if (levelCube.levelNumber == level_number) {
                mLevelCubeHilite = levelCube;
                mLevelCubeHilite.colorNumber = new Color(255, 255, 0, 255);
                return;
            }
	    }
    }

    public void updateAnimToMenu() {
	    if (0 == mAnimToMenuPhase) {
		    updateAnimToMenuPhaseOne();
        } else {
		    updateAnimToMenuPhaseTwo();
        }
    }

    public void updateAnimToMenuPhaseTwo() {
        mInterpolator.interpolate();
        mCubeRotation.degree = mInterpolator.getValue();

        mElapsedCamera += 0.04f;
        if (mElapsedCamera > 1f) {
            mElapsedCamera = 1f;
        }
	    Utils.lerpCamera(mCameraFrom, mCameraTo, mElapsedCamera, mCameraCurrent);
	
	    Cube cube;    
        cube = m_ad_face.getCubeFromAppearList();
	    if (cube != null) {
		    mCubesFace.add(cube);
	    }

        cube = m_ad_face.getCubeFromAppearList();
	    if (cube != null) {
		    mCubesFace.add(cube);
	    }

        cube = m_ad_face.getCubeFromAppearList(); 
	    if (cube != null) {
		    mCubesFace.add(cube);
	    }
    
        float diff = Math.abs( Math.abs(mTargetRotationDegree) - Math.abs(mInterpolator.getValue()));
	
        if (diff < EPSILON && Math.abs(1.0f - mElapsed) < EPSILON && Math.abs(1.0f - mElapsedCamera) < EPSILON && m_ad_face.appear.isEmpty()) {
		    Game.menuInitData.reappear = true;
		    Game.showScene(Scene_Menu);
        }
    }

    public void updateAnimToMenuPhaseOne() {
        mInterpolator.interpolate();
        mCubeRotation.degree = mInterpolator.getValue();

        mElapsed += 0.04f;
        if (mElapsed > 1f) {
            mElapsed = 1f;
        }
        Utils.lerpVector3(mPosLightFrom, mPosLightTo, mElapsed, mPosLightCurrent);
	    Utils.lerpCamera(mCameraFrom, mCameraTo, mElapsed, mCameraCurrent);
		
        Cube cube;    
	    boolean done_base = true;
	
        cube = m_ad_base_appear.getCubeFromAppearList();    
        if (cube != null) {
            mCubesBase.add(cube);
            done_base = false;
        }
    
        cube = m_ad_base_appear.getCubeFromAppearList();
        if (cube != null) {
            mCubesBase.add(cube);
            done_base = false;
        }

        cube = m_ad_base_disappear.getCubeFromDisappearList();
        if (cube != null) {
            mCubesBase.remove(cube);
            done_base = false;
        }
 
	    if (done_base) {
            mTimeout -= 0.01f;
        
            if (mTimeout <= 0.0f) {
                LevelCube levelCube;
            
                levelCube = getLevelCubeFrom(mLevelCubesXplus);
                if (levelCube != null) {
                    Game.cubeFacesData[X_Plus].levelCubes.add(levelCube);
                    mLevelCubesXplus.remove(levelCube);
                }
            
                levelCube = getLevelCubeFrom(mLevelCubesYplus);
                if (levelCube != null) {
                    Game.cubeFacesData[Y_Plus].levelCubes.add(levelCube);
                    mLevelCubesYplus.remove(levelCube);
                }
            
                levelCube = getLevelCubeFrom(mLevelCubesZplus);
                if (levelCube != null) {
                    Game.cubeFacesData[Z_Plus].levelCubes.add(levelCube);
                    mLevelCubesZplus.remove(levelCube);
                }
            }
        }
    
        if (mLevelCubesXplus.isEmpty() && mLevelCubesYplus.isEmpty() && mLevelCubesZplus.isEmpty()) {
		    cube = m_ad_face.getCubeFromAppearList();
		
		    if (cube != null) {
			    mCubesFace.add(cube);
            }

		    mAnimToMenuPhase = 1;
		
		    mElapsedCamera = 0.0f;
		    mCameraFrom.init(mCameraCurrent);
		    mCameraTo.init(Game.menu.mCameraMenu);
		
		    mCubeRotation.degree = -45.0f;
		    mTargetRotationDegree = -90.0f;
		    mInterpolator.setup(mCubeRotation.degree, mTargetRotationDegree, 6);
	    }
    }

    public void updateAnimToLevel() {
	    mTimeout -= 0.04f;
	
	    if (mTimeout <= 0f) {
		    LevelCube levelCube;
		
		    levelCube = getLevelCubeFrom(Game.cubeFacesData[X_Plus].levelCubes);
		    if (levelCube != null) {
			    Game.cubeFacesData[X_Plus].levelCubes.remove(levelCube);
			
			    if (levelCube == mLevelCubeHilite) {
				    Game.cubeFacesData[X_Plus].levelCubes.add(0, levelCube);
                }
		    }
		
		    levelCube = getLevelCubeFrom(Game.cubeFacesData[Y_Plus].levelCubes);
		    if (levelCube != null) {
			    Game.cubeFacesData[Y_Plus].levelCubes.remove(levelCube);
            }
		
		    levelCube = getLevelCubeFrom(Game.cubeFacesData[Z_Plus].levelCubes);
		    if (levelCube != null) {
			    Game.cubeFacesData[Z_Plus].levelCubes.remove(levelCube);
            }
		    mTimeout = 0.03f;
	    }

	    boolean done = true;
	    Cube cube;
	
	    // face
	    cube = m_ad_face.getCubeFromDisappearList();
	    if (cube !=  null) {
		    mCubesFace.remove(cube);
		    done = false;
	    }

        cube = m_ad_face.getCubeFromDisappearList();
	    if (cube != null) {
		    mCubesFace.remove(cube);
		    done = false;
	    }
    
	    // base
	    cube = m_ad_base_disappear.getCubeFromDisappearList();	
	    if (cube != null) {
		    mCubesBase.remove(cube);
	
		    cube = m_ad_base_disappear.getCubeFromDisappearList();
		    if (cube != null) {
			    mCubesBase.remove(cube);
			    done = false;
		    }

		    cube = m_ad_base_disappear.getCubeFromDisappearList();
		    if (cube != null) {
			    mCubesBase.remove(cube);
			    done = false;
		    }
	    }
	
	    if (done) {
		    if (1 == Game.cubeFacesData[X_Plus].levelCubes.size()) {
			    Game.cubeFacesData[X_Plus].levelCubes.clear();
            }
		
		    cube = m_ad_base_appear.getCubeFromAppearList();
		    if (cube != null) {
			    mCubesBase.add(cube);
            }
	    }
	
        mElapsed += 0.04f;
        if (mElapsed > 1f) {
            mElapsed = 1f;
        }
    
        Utils.lerpCamera(mCameraFrom, mCameraTo, mElapsed, mCameraCurrent);
        Utils.lerpVector3(mPosLightFrom, mPosLightTo, mElapsed, mPosLightCurrent);

	    mInterpolator.interpolate();
        mCubeRotation.degree = mInterpolator.getValue();

        float diff = mTargetRotationDegree - mInterpolator.getValue();
        if (diff < 0.1f && (1.0f - mElapsed) < EPSILON && m_ad_base_appear.appear.isEmpty()) {
            mCubeRotation.degree = mTargetRotationDegree;
		    Game.showScene(Scene_Level);
        }
    }

    public void updateCubes() {
	    for (int i = 0; i < 7; ++i) {
		    mTitles.get(i).clear();
		    mTexts.get(i).clear();
            mSymbols.get(i).clear();
	    }

        int size = mCubesBase.size();
	    int face_type;
	    Cube cube;
	    for (int i = 0; i < size; ++i) {
		    cube = mCubesBase.get(i);
		
		    face_type = X_Plus;
		    if (null != cube.fonts[face_type]) {
			    mTexts.get(face_type).add(cube.fonts[face_type]);
            }
				
            if (null != cube.symbols[face_type]) {
                mSymbols.get(face_type).add(cube.symbols[face_type]);
            }
        
		    face_type = Y_Plus;
		    if (null != cube.fonts[face_type]) {
			    mTexts.get(face_type).add(cube.fonts[face_type]);
            }

            if (null != cube.symbols[face_type]) {
                mSymbols.get(face_type).add(cube.symbols[face_type]);
            }
        
		    face_type = Z_Plus;
		    if (null != cube.fonts[face_type]) {
			    mTexts.get(face_type).add(cube.fonts[face_type]);
            }
        
            if (null != cube.symbols[face_type]) {
                mSymbols.get(face_type).add(cube.symbols[face_type]);
            }
	    }

        size = mCubesFace.size();
	    for (int i = 0; i < size; ++i) {
		    cube = mCubesFace.get(i);
				
		    face_type = X_Plus;
		    if (null != cube.fonts[face_type]) {
			    mTitles.get(face_type).add(cube.fonts[face_type]);
            }
				
		    face_type = Y_Plus;
		    if (null != cube.fonts[face_type]) {
			    mTitles.get(face_type).add(cube.fonts[face_type]);
            }
	
		    face_type = Z_Plus;
		    if (null != cube.fonts[face_type]) {
			    mTitles.get(face_type).add(cube.fonts[face_type]);
            }
	    }
    }

    @Override
    public void update() {
        switch (Game.animInitData.type) {
            case AnimToLevel:
                updateAnimToLevel();
                break;

            case AnimToMenu:
                updateAnimToMenu();
                //Game.menuInitData.reappear = true;
                //Game.showScene(Scene_Menu);
                break;

            default:
                break;
        }
        updateCubes();
    }

    private void drawTheCube() {
		
	    glEnable(GL_LIGHTING);
	    glEnable(GL_TEXTURE_2D);
		Graphics.textureGrayConcrete.bind();
	
        Graphics.resetBufferIndices();
        Graphics.bindStreamSources3d();

        int size;
        Cube cube;
        size = mCubesBase.size();
        for (int i = 0; i < size; ++i) {
            cube = mCubesBase.get(i);
            Graphics.addCubeSize(cube.tx, cube.ty, cube.tz, HALF_CUBE_SIZE, Game.baseColor);
        }
        Graphics.updateBuffers();
        Graphics.renderTriangles(Game.cubeOffset.x, Game.cubeOffset.y, Game.cubeOffset.z);

	    Graphics.resetBufferIndices();
        size = mCubesFace.size();
        for (int i = 0; i < size; ++i) {
            cube = mCubesFace.get(i);
            Graphics.addCubeSize(cube.tx, cube.ty, cube.tz, HALF_CUBE_SIZE, Game.faceColor);
        }
        Graphics.updateBuffers();
	    Graphics.renderTriangles(Game.cubeOffset.x, Game.cubeOffset.y, Game.cubeOffset.z);
    }

    private void drawLevelCubes() {
        
	    Graphics.resetBufferIndices();
	    Graphics.bindStreamSources3d();
	
	    LevelCube cube;
        int len = Game.cubeFacesData[X_Plus].levelCubes.size();
	    for (int i = 0; i < len; ++i) {
            cube = Game.cubeFacesData[X_Plus].levelCubes.get(i);
		    Graphics.addCube(cube.pos.x, cube.pos.y, cube.pos.z);
        }

        len = Game.cubeFacesData[Y_Plus].levelCubes.size();
	    for (int i = 0; i < len; ++i) {
            cube = Game.cubeFacesData[Y_Plus].levelCubes.get(i);
		    Graphics.addCube(cube.pos.x, cube.pos.y, cube.pos.z);
        }
	
        len = Game.cubeFacesData[Z_Plus].levelCubes.size();
	    for (int i = 0; i < len; ++i) {
            cube = Game.cubeFacesData[Z_Plus].levelCubes.get(i);
		    Graphics.addCube(cube.pos.x, cube.pos.y, cube.pos.z);
        }
	
	    if (Graphics._vertices_count > 0) {
			Graphics.textureLevelCubes.bind();
            Graphics.updateBuffers();
		    Graphics.renderTriangles();
	    }
    }

    public void drawLevelCubeDecals(LevelCubeDecalTypeEnum decalType) {
        
	    Graphics.resetBufferIndices();
	
	    TexCoordsQuad coords = new TexCoordsQuad();
        LevelCube levelCube;
	    TexturedQuad p;
        Color color = new Color();
	    int faceType;
        int size;

        faceType = X_Plus;
        size = Game.cubeFacesData[faceType].levelCubes.size();
	    for(int i = 0; i < size; ++i) {
		    levelCube = Game.cubeFacesData[faceType].levelCubes.get(i);
            p = null;

            switch (decalType) {
                case LevelCubeDecalNumber:
                    p = levelCube.pNumber;
                    color = levelCube.colorNumber;
                    break;
                
                case LevelCubeDecalStars:
                    p = levelCube.pStars;
                    color = levelCube.colorStarsAndSolver;
                    break;
                
                case LevelCubeDecalSolver:
                    p = levelCube.pSolver;
                    color = levelCube.colorStarsAndSolver;
                    break;
            }

            if (p != null) {
                coords.tx0 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                coords.tx1 = new Vector2(p.tx_up_left.x,  p.tx_up_left.y);
                coords.tx2 = new Vector2(p.tx_lo_left.x,  p.tx_lo_left.y);
                coords.tx3 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);
            
                Graphics.addCubeFace_X_Plus(levelCube.pos.x, levelCube.pos.y, levelCube.pos.z, coords, color);
            }
	    }

        faceType = Y_Plus;
        size = Game.cubeFacesData[faceType].levelCubes.size();
	    for(int i = 0; i < size; ++i) {
		    levelCube = Game.cubeFacesData[faceType].levelCubes.get(i);
            p = null;
        
            switch (decalType) {
                case LevelCubeDecalNumber:
                    p = levelCube.pNumber;
                    color = levelCube.colorNumber;
                    break;
                
                case LevelCubeDecalStars:
                    p = levelCube.pStars;
                    color = levelCube.colorStarsAndSolver;
                    break;
                
                case LevelCubeDecalSolver:
                    p = levelCube.pSolver;
                    color = levelCube.colorStarsAndSolver;
                    break;
            }
        
            if (p != null) {
                coords.tx0 = new Vector2(p.tx_lo_left.x,  p.tx_lo_left.y);
                coords.tx1 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);
                coords.tx2 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                coords.tx3 = new Vector2(p.tx_up_left.x, p.tx_up_left.y);

                Graphics.addCubeFace_Y_Plus(levelCube.fontPos.x, levelCube.fontPos.y, levelCube.fontPos.z, coords, color);
            }
	    }

        faceType = Z_Plus;
        size = Game.cubeFacesData[faceType].levelCubes.size();
	    for(int i = 0; i < size; ++i) {
		    levelCube = Game.cubeFacesData[faceType].levelCubes.get(i);
            p = null;
        
            switch (decalType) {
                case LevelCubeDecalNumber:
                    p = levelCube.pNumber;
                    color = levelCube.colorNumber;
                    break;
                
                case LevelCubeDecalStars:
                    p = levelCube.pStars;
                    color = levelCube.colorStarsAndSolver;
                    break;
                
                case LevelCubeDecalSolver:
                    p = levelCube.pSolver;
                    color = levelCube.colorStarsAndSolver;
                    break;
            }

            if (p != null) {
                coords.tx0 = new Vector2(p.tx_up_right.x, p.tx_up_right.y);
                coords.tx1 = new Vector2(p.tx_up_left.x,  p.tx_up_left.y);
                coords.tx2 = new Vector2(p.tx_lo_left.x,  p.tx_lo_left.y);
                coords.tx3 = new Vector2(p.tx_lo_right.x, p.tx_lo_right.y);

                Graphics.addCubeFace_Z_Plus(levelCube.pos.x, levelCube.pos.y, levelCube.pos.z, coords, color);
            }
	    }
        Graphics.updateBuffers();
	    Graphics.renderTriangles();
    }

    public void drawTexts(ArrayList<CubeFont> lst_face_x_plus, ArrayList<CubeFont> lst_face_y_plus, ArrayList<CubeFont> lst_face_z_plus, Color color) {
        
	    int size;
        CubeFont cubeFont;
	    TexturedQuad font;
	    TexCoordsQuad coords = new TexCoordsQuad();
	
	    Graphics.resetBufferIndices();

        size = lst_face_x_plus.size();
	    for (int i = 0; i < size; ++i) {
            cubeFont = lst_face_x_plus.get(i);
		    font = cubeFont.texturedQuad;
		
		    coords.tx0 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
		    coords.tx1 = new Vector2(font.tx_up_left.x,  font.tx_up_left.y);
		    coords.tx2 = new Vector2(font.tx_lo_left.x,  font.tx_lo_left.y);
		    coords.tx3 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
		
		    Graphics.addCubeFace_X_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
	    }

        size = lst_face_y_plus.size();
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_face_y_plus.get(i);
		    font = cubeFont.texturedQuad;
		
		    coords.tx0 = new Vector2(font.tx_lo_left.x,  font.tx_lo_left.y);
		    coords.tx1 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
		    coords.tx2 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
		    coords.tx3 = new Vector2(font.tx_up_left.x,  font.tx_up_left.y);
		
		    Graphics.addCubeFace_Y_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
	    }

	    size = lst_face_z_plus.size();
        for (int i = 0; i < size; ++i) {
            cubeFont = lst_face_z_plus.get(i);
            font = cubeFont.texturedQuad;
					
		    coords.tx0 = new Vector2(font.tx_up_right.x, font.tx_up_right.y);
		    coords.tx1 = new Vector2(font.tx_up_left.x, font.tx_up_left.y);
		    coords.tx2 = new Vector2(font.tx_lo_left.x, font.tx_lo_left.y);
		    coords.tx3 = new Vector2(font.tx_lo_right.x, font.tx_lo_right.y);
		
		    Graphics.addCubeFace_Z_Plus(cubeFont.pos.x, cubeFont.pos.y, cubeFont.pos.z, coords, color);
	    }
	    Graphics.updateBuffers();
	    Graphics.renderTriangles();
    }

    @Override
    public void render() {
        Graphics.setProjection2D();
        Graphics.setModelViewMatrix2D();
        Graphics.bindStreamSources2d();
        Graphics.resetBufferIndices();

        glEnable(GL_BLEND);
        glDisable(GL_LIGHTING);
        glDepthMask(false);
        glEnable(GL_TEXTURE_2D);
    
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);
    
        Color color = new Color(255, 255, 255, (int)Game.dirtyAlpha);
        Graphics.drawFullScreenTexture(Graphics.textureDirty, color);
    
        glDepthMask(true);
    
        Graphics.setProjection3D();
        Graphics.setModelViewMatrix3D(mCameraCurrent);
        Graphics.bindStreamSources3d();
        Graphics.resetBufferIndices();

        Graphics.setLightPosition(mPosLightCurrent);

        glEnable(GL_LIGHTING);
    
	    glRotatef(mCubeRotation.degree, mCubeRotation.axis.x, mCubeRotation.axis.y, mCubeRotation.axis.z);
	
        drawTheCube();
    
        // DRAW_AXES_CUBE
//		glDisable(GL_TEXTURE_2D);
//		glDisable(GL_LIGHTING);
//		Graphics.drawAxes();
//		glEnable(GL_LIGHTING);
//		glEnable(GL_TEXTURE_2D);

	    glPushMatrix();
	    glTranslatef(Game.cubeOffset.x, Game.cubeOffset.y, Game.cubeOffset.z);

	    drawLevelCubes();
    
        glDisable(GL_LIGHTING);

	    glEnable(GL_BLEND);
	    glDisableClientState(GL_NORMAL_ARRAY);
	
	    Graphics.bindStreamSources3d();
	
        color = Game.textColor;
		Graphics.textureFonts.bind();
        drawTexts(mTexts.get(X_Plus), mTexts.get(Y_Plus), mTexts.get(Z_Plus), color);
	
	    color = Game.titleColor;
	    drawTexts(mTitles.get(X_Plus), mTitles.get(Y_Plus), mTitles.get(Z_Plus), color);

	    color = Game.symbolColor;
		Graphics.textureNumbers.bind();
        drawLevelCubeDecals(LevelCubeDecalTypeEnum.LevelCubeDecalNumber);

		Graphics.textureSymbols.bind();
        drawLevelCubeDecals(LevelCubeDecalTypeEnum.LevelCubeDecalStars);
        drawLevelCubeDecals(LevelCubeDecalTypeEnum.LevelCubeDecalSolver);
    
        drawTexts(mSymbols.get(X_Plus), mSymbols.get(Y_Plus), mSymbols.get(Z_Plus), color);

	    glPopMatrix();
    }    

	@Override
    public void onFingerDown(float x, float y, int fingerCount) {}

    @Override
    public void onFingerUp(float x, float y, int fingerCount) {}

    @Override
    public void onFingerMove(float prevX, float prevY, float curX, float curY, int fingerCount) {}

}
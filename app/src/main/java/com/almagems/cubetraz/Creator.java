package com.almagems.cubetraz;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Locale;

import static com.almagems.cubetraz.Constants.*;


public final class Creator {

	private static final ArrayList<CubeFont> m_list_cubefonts_used = new ArrayList<CubeFont>();
	private static final ArrayList<LevelCube> m_list_levelcubes_used = new ArrayList<LevelCube>();
	
    private static final ArrayList<CubeFont> m_list_cubefonts_pool = new ArrayList<CubeFont>();
	private static final ArrayList<LevelCube> m_list_levelcubes_pool = new ArrayList<LevelCube>();

	public static void CubeFontReleased(CubeFont cubeFont) {
		m_list_cubefonts_used.remove(cubeFont);
		m_list_cubefonts_pool.add(cubeFont);
	}

    public static String getLevelMotto(DifficultyEnum difficulty, int levelNumber) {
        String motto = "";
        switch (difficulty) {
            case Easy:
                motto = getMottoFrom(R.raw.easy_motto, levelNumber);
                break;
            case Normal:
                motto = getMottoFrom(R.raw.normal_motto, levelNumber);
                break;
            case Hard:
                motto = getMottoFrom(R.raw.hard_motto, levelNumber);
                break;
        }
        return motto;
    }

    private static String getMottoFrom(int resourceId, int levelNumber) {
        String value = null;
        String level2digits = String.format(Locale.US, "%02d", levelNumber);
        String buffer = TextResourceReader.readTextFileFromResource(Engine.activity, resourceId);

        try {
            JSONObject jObject = new JSONObject(buffer);
            value = jObject.getString(level2digits);
        } catch (final Exception ex) {
            System.out.print(ex.toString());
        }

        if (value != null) {
            value = value.toUpperCase();
        }

        return value;
    }

    public static void fillPools() {
		int size;
        // cubefont
	    CubeFont cubeFont;
        size = m_list_cubefonts_used.size();
	    for(int i = 0; i < size; ++i) {
            cubeFont = m_list_cubefonts_used.get(i);
		    m_list_cubefonts_pool.add(cubeFont);
	    }
	    m_list_cubefonts_used.clear();
	
        // levelcube
        size = m_list_levelcubes_used.size();
	    LevelCube levelCube;
        for(int i = 0; i < size; ++i) {
            levelCube = m_list_levelcubes_used.get(i);
            m_list_levelcubes_pool.add(levelCube);
	    }
	    m_list_levelcubes_used.clear();
    }

    public static LevelCube getLevelCubeFromPool() {
        LevelCube levelCube;
    
        if (m_list_levelcubes_pool.isEmpty()) {
            levelCube = new LevelCube();
        } else {
            levelCube = m_list_levelcubes_pool.get( m_list_levelcubes_pool.size() - 1 );
            m_list_levelcubes_pool.remove(levelCube);
        }
            
	    m_list_levelcubes_used.add(levelCube);
        return levelCube;
    }

    public static CubeFont getCubeFontFromPool() {
        CubeFont cubeFont;
    
        if (m_list_cubefonts_pool.isEmpty()) {
            cubeFont = new CubeFont();
        } else {
            cubeFont = m_list_cubefonts_pool.get( m_list_cubefonts_pool.size() - 1 );
            m_list_cubefonts_pool.remove(cubeFont);
        }
    	
	    m_list_cubefonts_used.add(cubeFont);
        return cubeFont;
    }

	public static void cubeFontReleased(CubeFont cubeFont) {
		m_list_cubefonts_used.remove(cubeFont);
		m_list_cubefonts_pool.add(cubeFont);
	}

//void cCreator::CreateStaticTexts()
//{
//    cCubeFont* pCubeFont;
//    
//    // [P]lay
//    pCubeFont = new cCubeFont();
//    pCubeFont->Init('P', CubePos(1, 5, 8));
//    pCubeFont->pos.z += FONT_OVERLAY_OFFSET;
//    _pHost->m_cubefont_play = pCubeFont;
//
//    // [O]ptions
//    pCubeFont = new cCubeFont();
//    pCubeFont->Init('O', CubePos(1, 3, 8));
//    pCubeFont->pos.z += FONT_OVERLAY_OFFSET;
//    _pHost->m_cubefont_options = pCubeFont;
//
//    // [S]tore
//    pCubeFont = new cCubeFont();
//    pCubeFont->Init('S', CubePos(1, 1, 8));
//    pCubeFont->pos.z += FONT_OVERLAY_OFFSET;
//    _pHost->m_cubefont_store = pCubeFont;
//    
//    // [U]nlock
//    pCubeFont = new cCubeFont();
//    pCubeFont->Init('U', CubePos(1, 0, 5));
//    pCubeFont->pos.y -= FONT_OVERLAY_OFFSET;
//    _pHost->m_cubefont_unlock = pCubeFont;
//    
//    // [R]estore
//    pCubeFont = new cCubeFont();
//    pCubeFont->Init('R', CubePos(1, 0, 3));
//    pCubeFont->pos.y -= FONT_OVERLAY_OFFSET;
//  	_pHost->m_cubefont_restore = pCubeFont;
//}

    public static void createTextsLevelPausedFace(Level level) {
	    fillPools();
	
        level.m_list_fonts.clear();
	
        CubeFont pCubeFont;

        pCubeFont = getCubeFontFromPool();
        pCubeFont.init('R', new CubePos(0, 5, 1));
        pCubeFont.pos.x -= FONT_OVERLAY_OFFSET;
        level.m_cubefont_up = pCubeFont;
    
        pCubeFont = getCubeFontFromPool();
        pCubeFont.init('R', new CubePos(0, 3, 1));
        pCubeFont.pos.x -= FONT_OVERLAY_OFFSET;
        level.m_cubefont_mid = pCubeFont;
    
        pCubeFont = getCubeFontFromPool();
        pCubeFont.init('Q', new CubePos(0, 1, 1));
        pCubeFont.pos.x -= FONT_OVERLAY_OFFSET;
        level.m_cubefont_low = pCubeFont;
    
        createCubeFonts(new CubePos(1, 5, 1), new CubePos(0, 0, 1), "RESUME", level.m_list_fonts, new Vector(-FONT_OVERLAY_OFFSET, 0.0f, 0.0f));
        createCubeFonts(new CubePos(1, 3, 1), new CubePos(0, 0, 1), "RESET", level.m_list_fonts, new Vector(-FONT_OVERLAY_OFFSET, 0.0f, 0.0f));
        createCubeFonts(new CubePos(1, 1, 1), new CubePos(0, 0, 1), "QUIT", level.m_list_fonts, new Vector(FONT_OVERLAY_OFFSET, 0.0f, 0.0f));
    }
 
    public static void createTextsLevelCompletedFace(Level level, CompletedFaceNextActionEnum type) {
	    fillPools();
	
        level.m_list_fonts.clear();
    
        String text = "";
        char ch = 'a';
    
        switch (type) {
            case Next:
                ch = 'N';
                text = "NEXT";
                break;
            
//        case Unlock:
//            ch = 'U';
//            strcpy(text, "UNLOCK");
//            break;
            
            case Finish:
                ch = 'F';
                text = "FINISH";
                break;
            
            case Buy_Full_Version:
                ch = 'G';
                text = "GET FULL";
                break;
        }
    
        CubeFont cubeFont;

        cubeFont = getCubeFontFromPool();
        cubeFont.init(ch,  new CubePos(7, 5, 0));
        cubeFont.pos.z -= FONT_OVERLAY_OFFSET;
        level.m_cubefont_up = cubeFont;
    
        cubeFont = getCubeFontFromPool();
        cubeFont.init('R', new CubePos(7, 3, 0));
        cubeFont.pos.z -= FONT_OVERLAY_OFFSET;
        level.m_cubefont_mid = cubeFont;
    
        cubeFont = getCubeFontFromPool();
        cubeFont.init('Q', new CubePos(7, 1, 0));
        cubeFont.pos.z -= FONT_OVERLAY_OFFSET;
        level.m_cubefont_low = cubeFont;
    
        createCubeFonts(new CubePos(7, 5, 1), new CubePos(-1, 0, 0), text, level.m_list_fonts, new Vector(0.0f, 0.0f, FONT_OVERLAY_OFFSET));
        createCubeFonts(new CubePos(7, 3, 1), new CubePos(-1, 0, 0), "REPLAY", level.m_list_fonts, new Vector(0.0f, 0.0f, FONT_OVERLAY_OFFSET));
        createCubeFonts(new CubePos(7, 1, 1), new CubePos(-1, 0, 0), "QUIT", level.m_list_fonts, new Vector(0.0f, 0.0f, FONT_OVERLAY_OFFSET));
    }

    public static void createCubeFonts(CubePos from, CubePos step, String text, ArrayList<CubeFont> list, Vector offset) {
        char ch;
        int len = text.length();
 
        Cube cube;
        CubeFont cubeFont;
        CubePos cube_pos = from;
 
        for (int i = 0; i < len; ++i) {
            ch = text.charAt(i);
 
            cubeFont = getCubeFontFromPool();
            cubeFont.init(ch, cube_pos);
 
            list.add(cubeFont);
 
            cube = Game.cubes[cube_pos.x][cube_pos.y][cube_pos.z];
 
            cube_pos.x += step.x;
            cube_pos.y += step.y;
            cube_pos.z += step.z;
        }
    }
 
//void cCreator::AlterRedCubeFontsOnFaceMain(bool forHelp)
//{
//	cCubeFont* pCubeFont;
//
//    char a[3];
//	
//	if (forHelp)
//	{
//		a[0] = 'S';
//		a[1] = 'R';
//		a[2] = 'C';
//	}
//	else
//	{
//		a[0] = 'P';
//		a[1] = 'O';
//		a[2] = 'S';
//	}
//	
//    // [D]rag
//    pCubeFont = _pHost->m_cubefont_play;
//    pCubeFont->Init(a[0], CubePos(1, 5, 8));
//    pCubeFont->pos.z += FONT_OVERLAY_OFFSET;
//
//    // [R]ed
//    pCubeFont = _pHost->m_cubefont_options;
//    pCubeFont->Init(a[1], CubePos(1, 3, 8));
//    pCubeFont->pos.z += FONT_OVERLAY_OFFSET;
//	
//    // [C]ubes
//    pCubeFont = _pHost->m_cubefont_store;
//    pCubeFont->Init(a[2], CubePos(1, 1, 8));
//    pCubeFont->pos.z += FONT_OVERLAY_OFFSET;
//}

    public static void createCubes() {
        Cube cube;
    
        for(int x = 0; x < MAX_CUBE_COUNT; ++x) {        
            for(int y = 0; y < MAX_CUBE_COUNT; ++y) {            
                for(int z = 0; z < MAX_CUBE_COUNT; ++z) {				
                    cube = Game.cubes[x][y][z];
				
				    cube.x = x;
                    cube.y = y;
				    cube.z = z;
                
                    cube.tx = x * CUBE_SIZE;
                    cube.ty = y * CUBE_SIZE;
                    cube.tz = z * CUBE_SIZE;
                
				    cube.reset();
				
                    if ( (x > 1 && x < MAX_CUBE_COUNT - 2) && (y > 1 && y < MAX_CUBE_COUNT - 2) && (z > 1 && z < MAX_CUBE_COUNT - 2) ) {
                        cube.type = CubeTypeEnum.CubeIsInvisible;
                    } else {
                        cube.type = CubeTypeEnum.CubeIsVisibleAndObstacle;
                    }
                }
            }
        }
    }

    public static void createMovingCubesForMenu() {
        Menu menu = Game.menu;
        MenuCube menuCube;
    
        menuCube = new MenuCube();
        menuCube.init(new CubePos(7, 5, 8), new Color(255, 0, 0, 255));
        menu.m_pMenuCubePlay = menuCube;
    
        menuCube = new MenuCube();
        menuCube.init(new CubePos(7, 3, 8), new Color(200, 0, 0, 255));
        menu.m_pMenuCubeOptions = menuCube;
   
        menuCube = new MenuCube();
        menuCube.init(new CubePos(7, 1, 8), new Color(100, 0, 0, 255));
        menu.m_pMenuCubeStore = menuCube;

    
        // option cubes    
	    menuCube = new MenuCube();
	    menuCube.init(new CubePos(7,8,3), new Color(255, 255, 255, 255));
        menu.m_arOptionsCubes[0] = menuCube;
    
        menuCube = new MenuCube();
	    menuCube.init(new CubePos(1,8,3), new Color(254, 255, 255, 255));
        menu.m_arOptionsCubes[1] = menuCube;
    
        menuCube = new MenuCube();
	    menuCube.init(new CubePos(7,8,6), new Color(253, 255, 255, 255));
        menu.m_arOptionsCubes[2] = menuCube;
    
        menuCube = new MenuCube();
	    menuCube.init(new CubePos(1, 8, 6), new Color(252, 255, 255, 255));
        menu.m_arOptionsCubes[3] = menuCube;
    
    
        // store cubes
        menuCube = new MenuCube();
        menuCube.init(new CubePos(1,0,6), new Color(40, 255, 255, 255));
        menuCube.setHiliteOffset(new CubePos(0,1,0));
        menu.m_pStoreCubeNoAds = menuCube;
    
        menuCube = new MenuCube();
        menuCube.init(new CubePos(1,0,4), new Color(50, 255, 255, 255));
        menuCube.setHiliteOffset(new CubePos(0,1,0));
        menu.m_pStoreCubeSolvers = menuCube;
    
        menuCube = new MenuCube();
        menuCube.init(new CubePos(1,0,2), new Color(60, 255, 255, 255));
        menuCube.setHiliteOffset(new CubePos(0,1,0));
        menu.m_pStoreCubeRestore = menuCube;

        // credits
        menuCube = new MenuCube();
        menuCube.init(new CubePos(8,0,8), new Color(1,100,100,255));
        menu.m_cubeCredits = menuCube;
    }

    public static void addLevelCube(int level_number, int face_type, CubeFaceNamesEnum face_id, int x, int y, int z) {
        LevelCube cube = getLevelCubeFromPool();
        cube.init(level_number, face_type, face_id, new CubePos(x, y, z));
    
        switch (face_id) {
            case Face_Easy01:
            case Face_Easy02:
            case Face_Easy03:
            case Face_Easy04:
                cube.setStars(Cubetraz.getStarsEasy(level_number));
                cube.setSolver(Cubetraz.getSolvedEasy(level_number));
                break;
            
            case Face_Normal01:
            case Face_Normal02:
            case Face_Normal03:
            case Face_Normal04:
                cube.setStars(Cubetraz.getStarsNormal(level_number));
                cube.setSolver(Cubetraz.getSolvedNormal(level_number));
                break;
            
            case Face_Hard01:
            case Face_Hard02:
            case Face_Hard03:
            case Face_Hard04:
			    cube.setStars(Cubetraz.getStarsHard(level_number));
                cube.setSolver(Cubetraz.getSolvedHard(level_number));
                break;
            
            default:
                break;
        }
        Game.ar_cubefacedata[face_type].lst_level_cubes.add(cube);
    }

	public static void addCubeFont(final char ch, CubePos cube_pos, int face_type) {
		CubeFont cubeFont = getCubeFontFromPool();
		Cube cube = Game.cubes[cube_pos.x][cube_pos.y][cube_pos.z];

		cubeFont.init(ch, cube_pos);    
		cube.ar_fonts[face_type] = cubeFont;
	}

	public static void addCubeFontSymbol(final int symbol_id, CubePos cube_pos, int face_type) {
		CubeFont cubeFont = getCubeFontFromPool();
		Cube cube = Game.cubes[cube_pos.x][cube_pos.y][cube_pos.z];
		
		cubeFont.init(symbol_id, cube_pos);    
		cube.ar_symbols[face_type] = cubeFont;
	}	
}
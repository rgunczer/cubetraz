package com.almagems.cubetraz.scenes;

import com.almagems.cubetraz.cubes.CubeLocation;
import com.almagems.cubetraz.Game;
import com.almagems.cubetraz.R;
import com.almagems.cubetraz.scenes.level.Level;
import com.almagems.cubetraz.TextResourceReader;
import com.almagems.cubetraz.math.Vector;
import com.almagems.cubetraz.cubes.Cube;
import com.almagems.cubetraz.cubes.CubeFont;
import com.almagems.cubetraz.cubes.LevelCube;
import com.almagems.cubetraz.cubes.MenuCube;
import com.almagems.cubetraz.graphics.Color;
import com.almagems.cubetraz.scenes.menu.Menu;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Locale;

import static com.almagems.cubetraz.Game.*;


public final class Creator {

	private static final ArrayList<CubeFont> cubefontsUsed = new ArrayList<>();
	private static final ArrayList<LevelCube> levelcubesUsed = new ArrayList<>();
	
    private static final ArrayList<CubeFont> cubefontsPool = new ArrayList<>();
	private static final ArrayList<LevelCube> levelcubesPool = new ArrayList<>();

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
        String buffer = TextResourceReader.readTextFileFromResource(resourceId);

        try {
            JSONObject jObject = new JSONObject(buffer);
            JSONArray jsonArray = jObject.getJSONArray(level2digits);
            value = jsonArray.get(0) + "\n" + jsonArray.get(1) + "\n" + jsonArray.get(2);
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
        size = cubefontsUsed.size();
	    for(int i = 0; i < size; ++i) {
            cubeFont = cubefontsUsed.get(i);
		    cubefontsPool.add(cubeFont);
	    }
	    cubefontsUsed.clear();
	
        // levelcube
        size = levelcubesUsed.size();
	    LevelCube levelCube;
        for(int i = 0; i < size; ++i) {
            levelCube = levelcubesUsed.get(i);
            levelcubesPool.add(levelCube);
	    }
	    levelcubesUsed.clear();
    }

    private static LevelCube getLevelCubeFromPool() {
        LevelCube levelCube;
    
        if (levelcubesPool.isEmpty()) {
            levelCube = new LevelCube();
        } else {
            levelCube = levelcubesPool.get( levelcubesPool.size() - 1 );
            levelcubesPool.remove(levelCube);
        }
            
	    levelcubesUsed.add(levelCube);
        return levelCube;
    }

    private static CubeFont getCubeFontFromPool() {
        CubeFont cubeFont;
    
        if (cubefontsPool.isEmpty()) {
            cubeFont = new CubeFont();
        } else {
            cubeFont = cubefontsPool.get( cubefontsPool.size() - 1 );
            cubefontsPool.remove(cubeFont);
        }
    	
	    cubefontsUsed.add(cubeFont);
        return cubeFont;
    }

	public static void cubeFontReleased(CubeFont cubeFont) {
		cubefontsUsed.remove(cubeFont);
		cubefontsPool.add(cubeFont);
	}

    public static void createTextsLevelPausedFace(Level level) {
	    fillPools();
	
        level.fonts.clear();
	
        CubeFont pCubeFont;

        pCubeFont = getCubeFontFromPool();
        pCubeFont.init('R', new CubeLocation(0, 5, 1));
        pCubeFont.pos.x -= FONT_OVERLAY_OFFSET;
        level.mLevelMenu.fontUp = pCubeFont;
    
        pCubeFont = getCubeFontFromPool();
        pCubeFont.init('R', new CubeLocation(0, 3, 1));
        pCubeFont.pos.x -= FONT_OVERLAY_OFFSET;
        level.mLevelMenu.fontMid = pCubeFont;
    
        pCubeFont = getCubeFontFromPool();
        pCubeFont.init('Q', new CubeLocation(0, 1, 1));
        pCubeFont.pos.x -= FONT_OVERLAY_OFFSET;
        level.mLevelMenu.fontLow = pCubeFont;
    
        createCubeFonts(new CubeLocation(1, 5, 1), new CubeLocation(0, 0, 1), "RESUME", level.fonts, new Vector(-FONT_OVERLAY_OFFSET, 0.0f, 0.0f));
        createCubeFonts(new CubeLocation(1, 3, 1), new CubeLocation(0, 0, 1), "RESET", level.fonts, new Vector(-FONT_OVERLAY_OFFSET, 0.0f, 0.0f));
        createCubeFonts(new CubeLocation(1, 1, 1), new CubeLocation(0, 0, 1), "QUIT", level.fonts, new Vector(FONT_OVERLAY_OFFSET, 0.0f, 0.0f));
    }
 
    public static void createTextsLevelCompletedFace(Level level, CompletedFaceNextActionEnum type) {
	    fillPools();
	
        level.fonts.clear();
    
        String text = "";
        char ch = 'a';
    
        switch (type) {
            case Next:
                ch = 'N';
                text = "NEXT";
                break;
            
            case Finish:
                ch = 'F';
                text = "FINISH";
                break;
        }
    
        CubeFont cubeFont;

        cubeFont = getCubeFontFromPool();
        cubeFont.init(ch,  new CubeLocation(7, 5, 0));
        cubeFont.pos.z -= FONT_OVERLAY_OFFSET;
        level.mLevelMenu.fontUp = cubeFont;
    
        cubeFont = getCubeFontFromPool();
        cubeFont.init('R', new CubeLocation(7, 3, 0));
        cubeFont.pos.z -= FONT_OVERLAY_OFFSET;
        level.mLevelMenu.fontMid = cubeFont;
    
        cubeFont = getCubeFontFromPool();
        cubeFont.init('Q', new CubeLocation(7, 1, 0));
        cubeFont.pos.z -= FONT_OVERLAY_OFFSET;
        level.mLevelMenu.fontLow = cubeFont;
    
        createCubeFonts(new CubeLocation(7, 5, 1), new CubeLocation(-1, 0, 0), text, level.fonts, new Vector(0.0f, 0.0f, FONT_OVERLAY_OFFSET));
        createCubeFonts(new CubeLocation(7, 3, 1), new CubeLocation(-1, 0, 0), "REPLAY", level.fonts, new Vector(0.0f, 0.0f, FONT_OVERLAY_OFFSET));
        createCubeFonts(new CubeLocation(7, 1, 1), new CubeLocation(-1, 0, 0), "QUIT", level.fonts, new Vector(0.0f, 0.0f, FONT_OVERLAY_OFFSET));
    }

    private static void createCubeFonts(CubeLocation from, CubeLocation step, String text, ArrayList<CubeFont> list, Vector offset) {
        char ch;
        int len = text.length();
        CubeFont cubeFont;
        CubeLocation cubeLocation = new CubeLocation(from);
 
        for (int i = 0; i < len; ++i) {
            ch = text.charAt(i);
 
            cubeFont = getCubeFontFromPool();
            cubeFont.init(ch, cubeLocation);
 
            list.add(cubeFont);

            cubeLocation.x += step.x;
            cubeLocation.y += step.y;
            cubeLocation.z += step.z;
        }
    }

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
        CubeLocation location = new CubeLocation();
        Color color = new Color();
    
        menuCube = new MenuCube();
        location.init(7, 5, 8);
        color.init(255, 0, 0, 255);
        menuCube.init(location, color);
        menu.mMenuCubePlay = menuCube;
    
        menuCube = new MenuCube();
        location.init(7, 3, 8);
        color.init(200, 0, 0, 255);
        menuCube.init(location, color);
        menu.mMenuCubeOptions = menuCube;
   
        menuCube = new MenuCube();
        location.init(7, 1, 8);
        color.init(100, 0, 0, 255);
        menuCube.init(location, color);
        menu.mMenuCubeStore = menuCube;

    
        // option cubes    
	    menuCube = new MenuCube();
        location.init(7, 8, 3);
        color.init(255, 255, 255, 255);
	    menuCube.init(location, color);
        menu.optionCubes[0] = menuCube;
    
        menuCube = new MenuCube();
        location.init(1, 8, 3);
        color.init(254, 255, 255, 255);
	    menuCube.init(location, color);
        menu.optionCubes[1] = menuCube;
    
        menuCube = new MenuCube();
        location.init(7, 8, 6);
        color.init(253, 255, 255, 255);
	    menuCube.init(location, color);
        menu.optionCubes[2] = menuCube;
    
        menuCube = new MenuCube();
        location.init(1, 8, 6);
        color.init(252, 255, 255, 255);
	    menuCube.init(location, color);
        menu.optionCubes[3] = menuCube;

        // credits
        menuCube = new MenuCube();
        location.init(8, 0, 8);
        color.init(1, 100, 100, 255);
        menuCube.init(location, color);
        menu.cubeCredits = menuCube;
    }

    public static void addLevelCube(int levelNumber, int faceType, CubeFaceNames faceName, int x, int y, int z) {
        LevelCube cube = getLevelCubeFromPool();
        cube.init(levelNumber, faceType, faceName, new CubeLocation(x, y, z));
    
        switch (faceName) {
            case Face_Easy01:
            case Face_Easy02:
            case Face_Easy03:
            case Face_Easy04:
                cube.setStars(Game.progress.getStarsEasy(levelNumber));
                cube.setSolver(Game.progress.isSolvedEasy(levelNumber));
                break;
            
            case Face_Normal01:
            case Face_Normal02:
            case Face_Normal03:
            case Face_Normal04:
                cube.setStars(Game.progress.getStarsNormal(levelNumber));
                cube.setSolver(Game.progress.isSolvedNormal(levelNumber));
                break;
            
            case Face_Hard01:
            case Face_Hard02:
            case Face_Hard03:
            case Face_Hard04:
			    cube.setStars(Game.progress.getStarsHard(levelNumber));
                cube.setSolver(Game.progress.isSolvedHard(levelNumber));
                break;
            
            default:
                break;
        }
        Game.cubeFacesData[faceType].levelCubes.add(cube);
    }

	public static void addCubeFont(final char ch, CubeLocation cubePos, int faceType, CubeFaceNames faceName, Color color) {
		CubeFont cubeFont = getCubeFontFromPool();
		Cube cube = Game.cubes[cubePos.x][cubePos.y][cubePos.z];

		cubeFont.init(ch, cubePos);
        cubeFont.setColor(color);

        if (faceName == CubeFaceNames.Face_Tutorial) {
            cubeFont.colorCurrent.a = 0;
        }

		cube.fonts[faceType] = cubeFont;
	}

	public static void addCubeFontSymbol(final int symbolId, CubeLocation cubePos, int faceType, CubeFaceNames faceName, Color color) {
		CubeFont cubeFont = getCubeFontFromPool();
		Cube cube = Game.cubes[cubePos.x][cubePos.y][cubePos.z];
		
		cubeFont.init(symbolId, cubePos);
        cubeFont.setColor(color);

		cube.symbols[faceType] = cubeFont;
	}	
}
package com.almagems.cubetraz;


import java.util.ArrayList;

import static com.almagems.cubetraz.Constants.*;


public final class Creator
{	
    public static Menu _pHost;
		
	private static final ArrayList<CubeFont> m_list_cubefonts_used = new ArrayList<CubeFont>();
	private static final ArrayList<LevelCube> m_list_levelcubes_used = new ArrayList<LevelCube>();
	
    private static final ArrayList<CubeFont> m_list_cubefonts_pool = new ArrayList<CubeFont>();
	private static final ArrayList<LevelCube> m_list_levelcubes_pool = new ArrayList<LevelCube>();
    
    // ctor
    private Creator() {
    }

	public static void CubeFontReleased(CubeFont cubeFont) {
		m_list_cubefonts_used.remove(cubeFont);
		m_list_cubefonts_pool.add(cubeFont);
	}

    public static String getLevelMotto(DifficultyEnum difficulty, int level_number) {
	    final String[] aeasy = {
		    "00.dummy",
		    "01.to get to the key\nsimply swipe once\nto the right direction",
            "02.same setup\ndifferent angle\none swipe to the key",
		    "03.i call\nthis a real\ncorner case",
		    "04.at the center\ngravity will\ndo just fine",
		    "05.sometimes\ncubes just\nget in the way",
		    "06.between right\nangles with a little\nhelp to get to the key",
		    "07.two\nsimple forks\non the wall",
		    "08.snails made from\ncubes if you stare\nat them long enough",
		    "09.the bridge\nstay on same\nheight or raise",
		    "10.this time\ncube is nice\njust collide with it",
		    "11. \nsometimes one\nhit is not enough",
		    "12.simple\nmandatory\ndirection",
		    "13.heard of\nchain reaction?\ntry this one!",
		    "14.rise and fall\nbut do something\nbetween them",
		    "15. \nurbanized\nstructure",
		    "16. \n \nthe built-in key",
		    "17. \nrandom bars\na simple setup",
		    "18.kind of\niron maiden\nfrom cubes",
		    "19.wrong swipe\nand you can\nstart over",
		    "20.one of my\nfavorite level\nsome vertical bars",
		    "21.\ntiny setup\njust to keep\nthe low profile",
		    "22.this is art\neven if you\ndon't see it",
		    "23.i hate to say\nbut you have\nno choice where to go",
		    "24.before getting\ntoo mad this is\na nice chessboard",
		    "25.always wanted\nto be an architect\nyou, too?",
		    "26.don't panic\nthese cubes are\njust noise",
		    "27. \n \nsurrounded by death",
		    "28.tricky setup\ncount to ten\nbefore you start",
		    "29.again\njust noise\nremember?",
		    "30.mirrors\none for the key\nand one for the player",
		    "31.lot of dead ends\nso watch\nyour step (swipe)",
		    "32. \nlike ruins\nwith rules to follow",
		    "33.staying\non the ground but\nyou have no choice",
		    "34. \njust noise\nfocus",
		    "35.only death is\nbetween you\nand the key",
		    "36.the gatekeeper\nhides the key\nremove it to succeed",
		    "37. \n \nstar in the center",
		    "38. \n \nno comment",
		    "39. \n \nlittle more patience",
		    "40.use the\nhorizontal bar from\nbottom and from top",
		    "41. \ntwo finger drag\nto get a better view",
		    "42. \n \nso simple",
		    "43.take a\ncloser look\n don't let 3d trick you",
		    "44. \n \npiece of cake",
		    "45.for me\nit's like an octopus\nand for you?",
		    "46. \n \nc'mon",
		    "47. \n \nlong road to the key",
		    "48. \n \nrandomness",
		    "49. \neasier\nthan you think",
		    "50. \narchitectural\ncross bars",
		    "51. \ni don't\nremember this one",
		    "52. \n \ncliche",
		    "53. \nsort of\nharder cliche",
		    "54. \n \npharaoh maze",
		    "55. \n \ncomplicated setup",
		    "56. \n \ndangerous cubes",
		    "57. \n \ncomplicated disaster",
		    "58. \n \nat least almost done",
		    "59. \n \nin the finish",
		    "60.tricky loop\nautomatics\nbehind the wall"
	    };
	
	    final String[] anormal = {
		    "00.dummy",
		    "01.to get to the key\nswipe several times\nto the right direction",
		    "02.key is on\nthe second floor\nand we are downstairs",
		    "03.the clue is\nalmost hidden\nfrom this view",
		    "04.letter m is\nclearly visible\nfrom this view",
		    "05.go under\nthe bridge\nthen up",
		    "06. \nopen labyrinth\nwatch for columns",
		    "07.key at the\ncenter trickier\nthan you think",
		    "08.i call\nthis level\nthe 'waterfall'",
		    "09. \nhidden corridors\nclue is on the top",
		    "10. \nthis is by far\nmy favorite",
		    "11. \nhard to miss\neasy to lost",
		    "12. \n \nthe exposed key",
		    "13.small bridge\nis helpful\nmoving away",
		    "14. \nlot of cubes\nnot much options",
		    "15.three crosses\nmaster the\n3d vision",
		    "16.looks noisy\nbut there\nis a way",
		    "17.the key is\n on the floor\ncannot be difficult",
		    "18.easy to miss\nlook twice\nbefore rush",
		    "19.try to get\nto the same\nlevel as the key",
		    "20.watch only\ncubes at\nthe top",
		    "21.see if you\ncan plan\nthe path ahead",
		    "22.after previous\none this is a\npiece of cake",
		    "23. \nkey movements\nare closer",
		    "24.hit the cube\ncloser to you\nmore than once",
		    "25.this time\nignore the\ncloser cube",
		    "26. \nalmost\nsymmetrical setup",
		    "27. \nthis is easy\nand i mean it",
		    "28. \n \nso close and so far",
		    "29.just stay\nclose to the\ncamera",
		    "30. \nmake a big\ncircle around",
		    "31.again the\nkey moves\nare closer",
		    "32.long time\nsince\nsomething urbanized",
		    "33.easy\nbut not\nthat easy",
		    "34. \n \nlittle relaxing",
		    "35. \nattack\nfrom the sky",
		    "36. \nsort of\neasy thing",
		    "37. \nsame height\nand good to go",
		    "38. \n \nsky is the winner",
		    "39. \n \nstoned helicopter",
		    "40. \n \none cube between",
		    "41. \n \nthe corner key",
		    "42.few is\nsometimes more\nor complicated",
		    "43. \nstay on\nthis height",
		    "44.this time\nattack from\nbottom",
		    "45.in version\n1.0 this was\nthe final level",
		    "46.things can get\nreally nasty\n10+ moves",
		    "47. \nhit and\nhit again",
		    "48. \nnot easy but\nnot that hard",
		    "49.brave\nstraight\nto the death",
		    "50. \n \none big circle",
		    "51.never\nreally\nunderstood",
		    "52. \n \norder in chaos",
		    "53. \ndo not\nover complicate",
		    "54. \n \nthe spiral",
		    "55. \nonly two\nis required",
		    "56.hit\nthree times\ntwo cube",
		    "57. \n \nstaircase",
		    "58. \n \nthe dam",
		    "59.four corners\nfour deaths\nand one key",
		    "60.last\nnormal level\nhope you like it"
	    };

        final String[] ahard = {
		    "00.dummy",
		    "01.cubes on the wall\nswipe several times\nto the goal(key)",
		    "02.really!?\nis this a\nhard level",
		    "03.how about\nthis one\nharder!?",
		    "04.for some reason\nthis looks like a\nski lift for me",
		    "05.the symmetry\non this level\nis beautiful",
		    "06.hard to spot\nbut your cube\nis there",
		    "07. \n \nthe hidden setup",
		    "08.like an\nabstract\nchristmas tree",
		    "09. \n \nthe key is the king",
		    "10. \n \ncubes for relaxing",
		    "11.this one\ntricked me\nonce or twice",
		    "12.like a maze\ngo here and there\nand finally the key",
		    "13.guess what\nthis structure\nresemble",
		    "14.many have\ncomplained but\ni like this one",
		    "15. \n \nbehind a grid",
		    "16.being smart\nis just half\nthe story, right?",
		    "17. \njungle\nmade from cubes",
		    "18.from this\npoint key is\nalmost invisible",
		    "19.i know\nyou missed\nthese cubes", 
		    "20. \n \ncubes in the sky",
		    "21. \n \nsame cube twice",
		    "22. \nthree times\nthe same cube",
		    "23. \nlot of cubes\nbut don't panic",
		    "24.only one\ncube with a\ntriangle is useful",
		    "25.this is a\nbit difficult\nfor first time",
		    "26.get to the\nright height\nand can't miss it",
		    "27.don't forget\ntwo finger drag\nto rotate",
		    "28.again\nsame cube twice\nbut which one?",
		    "29. \n \ntricks in the center",
		    "30.kind of\nblack box\nin the basement",
		    "31.take a\ngood look\nbefore you start",
		    "32.this one\nwill make\nyou laugh",
		    "33. \n \nremember this one?",
		    "34.and\nhow about\nthis one?",
		    "35. \ndeja vu\nwith a twist",
		    "36. \ndeja vu\nwith outbreak",
		    "37.kick\nboth of\nthem away",
		    "38. \ni call it\nthe gate",
		    "39.getting so far\nthis is like\npiece of cake",
		    "40. \n \nonly for experts",
		    "41.think\noutside\nthe box",
		    "42. \n \nlittle relax",
		    "43.the glory\nof the\nforward thinkers",
		    "44. \ncontinuous\ncrosses",
		    "45.figuring\nout the\nmoves",
		    "46.the\nroller coaster\nbeauty",
		    "47.make no\nassumptions\nabout difficulty",
		    "48. \nless is\nsometimes more",
		    "49.close\ncloser\nkey",
		    "50.good parts\nare hidden\nso rotate",
		    "51. \nget to the\nsecond floor",
		    "52.going dark\nthen to the key\nand done",
		    "53.make\nyour moves\non the floor",
		    "54.just\nignore\nthe noise",
		    "55. \n \nhere and there",
		    "56. \ncubes\nfor thinkers",
		    "57. \n \nmixture of cubes",
		    "58.patience\ntwo more\nto solve",
		    "59. \n \nlast but one",
		    "60.let me\njust say\nnot many got here"
	    };
	
        String str;    
	    switch (difficulty) {
            case Easy: str = aeasy[level_number]; break;	
		    case Normal: str = anormal[level_number]; break; 		
		    case Hard: str = ahard[level_number]; break;
        }
	
        str.toUpperCase();	
	    return str;
    }

    public static void fillPools() {
        // cubefont
	    list<cCubeFont*>::iterator it;
	    for (it = m_list_cubefonts_used.begin(); it != m_list_cubefonts_used.end(); ++it) {
		    m_list_cubefonts_pool.push_back(*it);
	    }
	    m_list_cubefonts_used.clear();
	
        // levelcube
	    list<cLevelCube*>::iterator lit;
	    for (lit = m_list_levelcubes_used.begin(); lit != m_list_levelcubes_used.end(); ++lit) {
		    m_list_levelcubes_pool.push_back(*lit);
	    }
	    m_list_levelcubes_used.clear();
    }

    public static LevelCube getLevelCubeFromPool() {
        LevelCube cube;
    
        if (m_list_levelcubes_pool.isEmpty()) {
            cube = new cLevelCube();
        } else {
            cube = m_list_levelcubes_pool.get( m_list_levelcubes_pool.size() - 1 );
            m_list_levelcubes_pool.remove(cube);
        }
            
	    m_list_levelcubes_used.add(cube);
        return cube;
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
        pCubeFont.init('R', new CubePos(0,5,1));
        pCubeFont.pos.x -= FONT_OVERLAY_OFFSET;
        level.m_cubefont_up = pCubeFont;
    
        pCubeFont = getCubeFontFromPool();
        pCubeFont.init('R', new CubePos(0,3,1));
        pCubeFont.pos.x -= FONT_OVERLAY_OFFSET;
        level.m_cubefont_mid = pCubeFont;
    
        pCubeFont = getCubeFontFromPool();
        pCubeFont.init('Q', new CubePos(0,1,1));
        pCubeFont.pos.x -= FONT_OVERLAY_OFFSET;
        level.m_cubefont_low = pCubeFont;
    
        createCubeFonts(new CubePos(1, 5, 1), new CubePos(0, 0, 1), "RESUME", level.m_list_fonts, new Vector(-FONT_OVERLAY_OFFSET, 0.0f, 0.0f));
        createCubeFonts(new CubePos(1, 3, 1), new CubePos(0, 0, 1), "RESET", level.m_list_fonts, new Vector(-FONT_OVERLAY_OFFSET, 0.0f, 0.0f));
        createCubeFonts(new CubePos(1, 1, 1), new CubePos(0, 0, 1), "QUIT", level.m_list_fonts, new Vector(FONT_OVERLAY_OFFSET, 0.0f, 0.0f));
    }
 
    public static void createTextsLevelCompletedFace(Level level, CompletedFaceNextActionEnum type) {
	    fillPools();
	
        level.m_list_fonts.clear();
    
        char text[16];
        char ch;
    
        switch (type) {
            case Next:
                ch = 'N';
                strcpy(text, "NEXT");
                break;
            
//        case Unlock:
//            ch = 'U';
//            strcpy(text, "UNLOCK");
//            break;
            
            case Finish:
                ch = 'F';
                strcpy(text, "FINISH");
                break;
            
            case Buy_Full_Version:
                ch = 'G';
                strcpy(text, "GET FULL");
                break;
        }
    
        CubeFont pCubeFont;

        pCubeFont = getCubeFontFromPool();
        pCubeFont.init(ch,  new CubePos(7,5,0));
        pCubeFont.pos.z -= FONT_OVERLAY_OFFSET;
        pLevel.m_cubefont_up = pCubeFont;
    
        pCubeFont = getCubeFontFromPool();
        pCubeFont.init('R', new CubePos(7,3,0));
        pCubeFont.pos.z -= FONT_OVERLAY_OFFSET;
        pLevel.m_cubefont_mid = pCubeFont;
    
        pCubeFont = getCubeFontFromPool();
        pCubeFont.init('Q', new CubePos(7,1,0));
        pCubeFont.pos.z -= FONT_OVERLAY_OFFSET;
        pLevel.m_cubefont_low = pCubeFont;
    
        createCubeFonts(new CubePos(7, 5, 1), new CubePos(-1, 0, 0), text, level.m_list_fonts, new Vector(0.0f, 0.0f, FONT_OVERLAY_OFFSET));
        createCubeFonts(new CubePos(7, 3, 1), new CubePos(-1, 0, 0), "REPLAY", level.m_list_fonts, new Vector(0.0f, 0.0f, FONT_OVERLAY_OFFSET));
        createCubeFonts(new CubePos(7, 1, 1), new CubePos(-1, 0, 0), "QUIT", level.m_list_fonts, new Vector(0.0f, 0.0f, FONT_OVERLAY_OFFSET));
    }

    public static void createCubeFonts(CubePos from, CubePos step, const char* text, list<cCubeFont*>& list, vec3 offset) {
        char ch;
        int len = (int)strlen(text);
 
        Cube pCube;
        CubeFont pCubeFont;
        CubePos cube_pos = from;
 
        for (int i = 0; i < len; ++i) {
            ch = text[i];
 
            pCubeFont = getCubeFontFromPool();
            pCubeFont.init(ch, cube_pos);
 
            list.add(pCubeFont);
 
            pCube = Game.cubes[cube_pos.x][cube_pos.y][cube_pos.z];
 
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
        MenuCube menuCube;
    
        menuCube = new MenuCube();
        menuCube.init(new CubePos(7, 5, 8), new Color(255, 0, 0, 255));
        _pHost.m_pMenuCubePlay = menuCube;
    
        menuCube = new MenuCube();
        menuCube.init(new CubePos(7, 3, 8), new Color(200, 0, 0, 255));
        _pHost.m_pMenuCubeOptions = menuCube;
   
        menuCube = new MenuCube();
        menuCube.init(new CubePos(7, 1, 8), new Color(100, 0, 0, 255));
        _pHost.m_pMenuCubeStore = menuCube;

    
        // option cubes    
	    menuCube = new MenuCube();
	    menuCube.init(new CubePos(7,8,3), new Color(255, 255, 255, 255));
        _pHost.m_arOptionsCubes[0] = menuCube;
    
        menuCube = new MenuCube();
	    menuCube.init(new CubePos(1,8,3), new Color(254, 255, 255, 255));
        _pHost.m_arOptionsCubes[1] = menuCube;
    
        menuCube = new MenuCube();
	    menuCube.init(new CubePos(7,8,6), new Color(253, 255, 255, 255));
        _pHost.m_arOptionsCubes[2] = menuCube;
    
        pMenuCube = new MenuCube();
	    menuCube.init(new CubePos(1, 8, 6), new Color(252, 255, 255, 255));
        _pHost.m_arOptionsCubes[3] = menuCube;
    
    
        // store cubes
        menuCube = new MenuCube();
        menuCube.init(new CubePos(1,0,6), new Color(40, 255, 255, 255));
        menuCube.setHiliteOffset(new CubePos(0,1,0));
        _pHost.m_pStoreCubeNoAds = menuCube;
    
        menuCube = new MenuCube();
        menuCube.init(new CubePos(1,0,4), new Color(50, 255, 255, 255));
        menuCube.setHiliteOffset(new CubePos(0,1,0));
        _pHost.m_pStoreCubeSolvers = menuCube;
    
        menuCube = new MenuCube();
        menuCube.init(new CubePos(1,0,2), new Color(60, 255, 255, 255));
        menuCube.setHiliteOffset(new CubePos(0,1,0));
        _pHost.m_pStoreCubeRestore = menuCube;

        // credits
        menuCube = new MenuCube();
        menuCube.init(new CubePos(8,0,8), new Color(1,100,100,255));
        _pHost.m_pCubeCredits = menuCube;
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
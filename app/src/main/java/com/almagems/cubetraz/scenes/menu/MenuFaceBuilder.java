package com.almagems.cubetraz.scenes.menu;


import com.almagems.cubetraz.GameProgress;
import com.almagems.cubetraz.cubes.CubeLocation;
import com.almagems.cubetraz.graphics.Color;
import com.almagems.cubetraz.scenes.Creator;
import com.almagems.cubetraz.Game;
import com.almagems.cubetraz.cubes.Cube;
import com.almagems.cubetraz.cubes.CubeFont;
import com.almagems.cubetraz.graphics.TexturedQuad;

import java.util.Locale;

import static com.almagems.cubetraz.Game.*;


public final class MenuFaceBuilder {

	private static final FaceTransformsEnum[] transforms = new FaceTransformsEnum[MAX_FACE_TRANSFORM_COUNT];
	
    // ctor
    private MenuFaceBuilder() {
    }


	static void doTransforms(int[] a) {
        for (int i = 0; i < MAX_FACE_TRANSFORM_COUNT; ++i) {
            switch (transforms[i]) {
                case NoTransform: break;
                case RotateCW90: rotateFace90CW(a); break;
                case RotateCCW90: rotateFace90CCW(a); break;
                case MirrorHoriz: mirrorFaceHoriz(a); break;
                case MirrorVert: mirrorFaceVert(a); break;
            }
        }
	}

    static void rotateFace90CW(int[] a) {
        int[] tmp = new int[MAX_CUBE_COUNT * MAX_CUBE_COUNT];

        int start = MAX_CUBE_COUNT - 1;
        int row = 0;
        int pos = start;

        for (int i = 0; i < MAX_CUBE_COUNT * MAX_CUBE_COUNT; ++i) {
            if (row > MAX_CUBE_COUNT - 1) {
                pos = --start;
                row = 0;
            }

            tmp[pos] = a[i];
            pos += MAX_CUBE_COUNT;
            ++row;
        }

        for (int i = 0; i < MAX_CUBE_COUNT * MAX_CUBE_COUNT; ++i) {
            a[i] = tmp[i];
        }
    }

    static void rotateFace90CCW(int[] a) {
		int[] tmp = new int[MAX_CUBE_COUNT * MAX_CUBE_COUNT];
		int col = 0;
		int row = MAX_CUBE_COUNT - 1;
		int pos;
		
		for (int i = 0; i < MAX_CUBE_COUNT * MAX_CUBE_COUNT; ++i) {
			if (row < 0) {
				++col;
				row = MAX_CUBE_COUNT - 1;
			}
			
			pos = row * MAX_CUBE_COUNT + col;
			tmp[pos] = a[i];
			--row;
		}
		
		for (int i = 0; i < MAX_CUBE_COUNT * MAX_CUBE_COUNT; ++i) {
            a[i] = tmp[i];
        }
	}

    static void mirrorFaceHoriz(int[] a) {
		int[] tmp = new int[MAX_CUBE_COUNT * MAX_CUBE_COUNT];
		int row = MAX_CUBE_COUNT - 1;
		int col = 0;
		int pos;
		
		for (int i = 0; i < MAX_CUBE_COUNT * MAX_CUBE_COUNT; ++i) {
			if (col > MAX_CUBE_COUNT - 1) {
				--row;
				col = 0;
			}
			
			pos = row * MAX_CUBE_COUNT + col;
			tmp[pos] = a[i];
			++col;
		}
		
		for (int i = 0; i < MAX_CUBE_COUNT * MAX_CUBE_COUNT; ++i) {
            a[i] = tmp[i];
        }
	}

    static void mirrorFaceVert(int[] a) {
		int[] tmp = new int[MAX_CUBE_COUNT * MAX_CUBE_COUNT];
		int row = 0;
		int col = MAX_CUBE_COUNT - 1;
		int pos;
		
		for (int i = 0; i < MAX_CUBE_COUNT * MAX_CUBE_COUNT; ++i) {
			if (col < 0) {
				++row;
				col = MAX_CUBE_COUNT - 1;
			}
			
			pos = row * MAX_CUBE_COUNT + col;
			tmp[pos] = a[i];
			--col;
		}
		
		for (int i = 0; i < MAX_CUBE_COUNT * MAX_CUBE_COUNT; ++i) {
            a[i] = tmp[i];
        }
	}

    public static void resetTransforms() {
	    for (int i = 0; i < MAX_FACE_TRANSFORM_COUNT; ++i) {
            transforms[i] = FaceTransformsEnum.NoTransform;
        }
    }

    public static void copyTransforms(FaceTransformsEnum[] trans) {
	    for (int i = 0; i < MAX_FACE_TRANSFORM_COUNT; ++i) {
            transforms[i] = trans[i];
        }
    }

    public static void addTransform(FaceTransformsEnum type) {
        int count = 1;
	    for (int i = 0; i < MAX_FACE_TRANSFORM_COUNT; ++i) {
		    if (FaceTransformsEnum.NoTransform == transforms[i]) {
                for (int j = i; j < i + count; ++j) {
                    transforms[j] = type;
                }
                return;
            }
		}
	}

    private static void setupFaceX(final int[] arr, int faceType, CubeFaceNames faceId) {
	    int x = (faceType == X_Plus ? MAX_CUBE_COUNT - 1 : 0);
        int counter = 0;
        char ch;
	    Cube cube;
	
	    for (int y = MAX_CUBE_COUNT - 1; y > -1; --y) {
		    for (int z = MAX_CUBE_COUNT - 1; z > -1; --z) {
			    cube = Game.cubes[x][y][z];
			    ch = (char)arr[counter];
			
			    Game.setCubeTypeOnFace(cube, ch, faceType, faceId);
			    ++counter;
		    }
	    }
    }

    private static void setupFaceY(final int[] arr, int faceType, CubeFaceNames faceId) {
	    int y = (faceType == Y_Plus ? MAX_CUBE_COUNT - 1 : 0);
        int counter = 0;
        char ch;
	    Cube cube;
    
	    for (int z = 0; z < MAX_CUBE_COUNT; ++z) {
		    for (int x = 0; x < MAX_CUBE_COUNT; ++x) {
			    cube = Game.cubes[x][y][z];
                ch = (char)arr[counter];
			
			    Game.setCubeTypeOnFace(cube, ch, faceType, faceId);
			    ++counter;
		    }
	    }
    }

    private static void setupFaceZ(final int[] arr, int faceType, CubeFaceNames faceId) {
	    int z = (faceType == Z_Plus ? MAX_CUBE_COUNT - 1 : 0);
        int counter = 0;
        char ch;
	    Cube cube;
    
	    for (int y = MAX_CUBE_COUNT - 1; y > -1; --y) {
		    for (int x = 0; x < MAX_CUBE_COUNT; ++x) {
			    cube = Game.cubes[x][y][z];
                ch = (char)arr[counter];
			
			    Game.setCubeTypeOnFace(cube, ch, faceType, faceId);
			    ++counter;
		    }
	    }
    }

    private static void setupFontFaceX(final int[] arr, int faceType, CubeFaceNames faceName, int x, Color color) {
        int counter = 0;
        char ch;
	    Cube cube;
	
	    for (int y = MAX_CUBE_COUNT - 1; y > -1; --y) {
		    for (int z = MAX_CUBE_COUNT - 1; z > -1; --z) {
			    cube = Game.cubes[x][y][z];
                ch = (char)arr[counter];
			
			    if (' ' != ch && 'x' != ch && 'o' != ch) {
				    Creator.addCubeFont(ch, new CubeLocation(x,y,z), faceType, faceName, color);
			    }
			    ++counter;
		    }
	    }
    }

    private static void setupFontFaceY(final int[] arr, int faceType, CubeFaceNames faceName, int y, Color color) {
        int counter = 0;
        char ch;
	    Cube cube;
    
	    for (int z = 0; z < MAX_CUBE_COUNT; ++z) {
		    for (int x = 0; x < MAX_CUBE_COUNT; ++x) {
			    cube = Game.cubes[x][y][z];
                ch = (char)arr[counter];
			
			    if (' ' != ch && 'x' != ch && 'o' != ch) {
				    Creator.addCubeFont(ch, new CubeLocation(x,y,z), faceType, faceName, color);
			    }
			    ++counter;
		    }
	    }
    }

    private static void setupFontFaceZ(final int[] arr, int faceType, CubeFaceNames faceName, int z, Color color) {
        int counter = 0;
        char ch;
	    Cube cube;
    
	    for (int y = MAX_CUBE_COUNT - 1; y > -1; --y) {
		    for (int x = 0; x < MAX_CUBE_COUNT; ++x) {
			    cube = Game.cubes[x][y][z];
                ch = (char)arr[counter];
	
			    if (' ' != ch && 'x' != ch && 'o' != ch) {
				    Creator.addCubeFont(ch, new CubeLocation(x, y, z), faceType, faceName, color);
			    }
			    ++counter;
		    }
	    }
    }

    private static void setupFontFaceSymbolX(final int[] a, int faceType, CubeFaceNames faceName, int x, Color color) {
        int counter = 0;
        int symbol_id;
	    Cube cube;
	
	    for (int y = MAX_CUBE_COUNT - 1; y > -1; --y) {
		    for (int z = MAX_CUBE_COUNT - 1; z > -1; --z) {
			    cube = Game.cubes[x][y][z];
			    symbol_id = a[counter];
			
			    if (Symbol_Empty != symbol_id) {
				    Creator.addCubeFontSymbol(symbol_id, new CubeLocation(x,y,z), faceType, faceName, color);
			    }
			    ++counter;
		    }
	    }
    }

    private static void setupFontFaceSymbolY(final int[] a, int faceType, CubeFaceNames faceName, int y, Color color) {
        int counter = 0;
        int symbol_id;
	    Cube cube;
    
	    for (int z = 0; z < MAX_CUBE_COUNT; ++z) {
		    for (int x = 0; x < MAX_CUBE_COUNT; ++x) {
			    cube = Game.cubes[x][y][z];
			    symbol_id = a[counter];
			
			    if (Symbol_Empty != symbol_id) {
				    Creator.addCubeFontSymbol(symbol_id, new CubeLocation(x,y,z), faceType, faceName, color);
			    }
			    ++counter;
		    }
	    }
    }

    private static void setupFontFaceSymbolZ(final int[] a, int faceType, CubeFaceNames faceName, int z, Color color) {
        int counter = 0;
        int symbol_id;
	    Cube cube;
    
	    for (int y = MAX_CUBE_COUNT - 1; y > -1; --y) {
            for (int x = 0; x < MAX_CUBE_COUNT; ++x) {
			    cube = Game.cubes[x][y][z];
			    symbol_id = a[counter];
			
			    if (Symbol_Empty != symbol_id) {
				    Creator.addCubeFontSymbol(symbol_id, new CubeLocation(x,y,z), faceType, faceName, color);
			    }
			    ++counter;
		    }
	    }
    }

    public static void build(CubeFaceNames faceName, int faceType) {
	    buildCubeFaces(faceName, faceType);
	    buildTitleTexts(faceName, faceType);
	    buildTexts(faceName, faceType, false);
	    buildSymbolsOnFace(faceName, faceType);
	    buildSymbolsOnBase(faceName, faceType);
	
	    resetTransforms();
    }

    private static int[] convertToIntArray(String str) {
        int[] arr = new int[str.length()];
        for(int i = 0; i < str.length(); ++i) {
            arr[i] = str.charAt(i);
        }
        return arr;
    }

    private static void buildCubeFaces(CubeFaceNames faceName, int faceType) {
        String str = getFace(faceName);
        int[] arr = convertToIntArray(str);

	    doTransforms(arr);
	
	    switch (faceType) {
		    case X_Plus: 	setupFaceX(arr, faceType, faceName); break;
		    case X_Minus: 	setupFaceX(arr, faceType, faceName); break;
		    case Y_Plus: 	setupFaceY(arr, faceType, faceName); break;
		    case Y_Minus: 	setupFaceY(arr, faceType, faceName); break;
		    case Z_Plus: 	setupFaceZ(arr, faceType, faceName); break;
		    case Z_Minus: 	setupFaceZ(arr, faceType, faceName); break;
	    }
    }

    private static void setFontFromCube(CubeFont cubeFontTarget, CubeLocation cp, CubeLocation offset, int faceType) {
        char ch;
        Cube cube;
        CubeFont cubeFont;
        TexturedQuad texturedQuad;
    
        cube = Game.cubes[cp.x + offset.x][cp.y + offset.y][cp.z + offset.z];
        cubeFont = cube.fonts[faceType];

        if (cubeFont != null) {
            texturedQuad = cubeFont.texturedQuad;
        
            if (texturedQuad != null) {
                ch = texturedQuad.ch;
                cubeFontTarget.init(ch, cp);
            }
        }
    }

    static void buildTexts(CubeFaceNames faceName, int faceType, boolean alt) {
        String str;
	
	    if (!alt) {
            str = getFaceText(faceName);
        } else {
            str = getFaceHelpText(faceName);
        }

        int[] arr = convertToIntArray(str);

	    doTransforms(arr);
	
	    int x = (faceType == X_Plus ? MAX_CUBE_COUNT - 1 : 0);
	    int y = (faceType == Y_Plus ? MAX_CUBE_COUNT - 1 : 0);
	    int z = (faceType == Z_Plus ? MAX_CUBE_COUNT - 1 : 0);

        Color color = Game.textColor;

	    switch (faceType) {
		    case X_Plus: 	setupFontFaceX(arr, faceType, faceName, x - 1, color); break;
		    case X_Minus:
		        setupFontFaceX(arr, faceType, faceName, x + 1, color);
                break;

		    case Y_Plus: 	setupFontFaceY(arr, faceType, faceName, y - 1, color); break;
		    case Y_Minus: {
			    setupFontFaceY(arr, faceType, faceName, y + 1, color);
            
                if ( CubeFaceNames.Face_Score == faceName) {
                    CubeLocation cp;
                    cp = new CubeLocation(1, 0, 6);
                    setFontFromCube(Game.menu.m_cubefont_noads, cp, new CubeLocation(0, 1, 0), faceType);
                    Game.menu.m_cubefont_noads.pos.y -= FONT_OVERLAY_OFFSET;

                    cp = new CubeLocation(1, 0, 4);
                    setFontFromCube(Game.menu.m_cubefont_solvers, cp, new CubeLocation(0, 1, 0), faceType);
                    Game.menu.m_cubefont_solvers.pos.y -= FONT_OVERLAY_OFFSET;
                
                    cp = new CubeLocation(1, 0, 2);
                    setFontFromCube(Game.menu.m_cubefont_restore, cp, new CubeLocation(0, 1, 0), faceType);
                    Game.menu.m_cubefont_restore.pos.y -= FONT_OVERLAY_OFFSET;
                }
            }
			break;
			
			case Z_Plus: {
				setupFontFaceZ(arr, faceType, faceName, z - 1, color);
            
            	if (CubeFaceNames.Face_Menu == faceName) {
                	CubeLocation cp;
                	cp = new CubeLocation(1, 5, 8);
                	setFontFromCube(Game.menu.m_cubefont_play, cp, new CubeLocation(0, 0, -1), faceType);
                	Game.menu.m_cubefont_play.pos.z += FONT_OVERLAY_OFFSET;
                
                	cp = new CubeLocation(1, 3, 8);
                	setFontFromCube(Game.menu.m_cubefont_options, cp, new CubeLocation(0, 0, -1), faceType);
                	Game.menu.m_cubefont_options.pos.z += FONT_OVERLAY_OFFSET;
                
                	cp = new CubeLocation(1, 1, 8);
                	setFontFromCube(Game.menu.m_cubefont_store, cp, new CubeLocation(0, 0, -1), faceType);
                	Game.menu.m_cubefont_store.pos.z += FONT_OVERLAY_OFFSET;                
            	}
        	}
			break;
			
			case Z_Minus:
			    setupFontFaceZ(arr, faceType, faceName, z + 1, color);
                break;
		}
	}

    private static void buildTitleTexts(CubeFaceNames faceName, int faceType) {
    	String str = getFaceTitle(faceName);
        int[] arr = convertToIntArray(str);

		doTransforms(arr);
	
		int x = (faceType == X_Plus ? MAX_CUBE_COUNT - 1 : 0);
		int y = (faceType == Y_Plus ? MAX_CUBE_COUNT - 1 : 0);
		int z = (faceType == Z_Plus ? MAX_CUBE_COUNT - 1 : 0);

        Color color = Game.titleColor;

		switch (faceType) {
			case X_Plus: 	setupFontFaceX(arr, faceType, faceName, x, color); break;
			case X_Minus: 	setupFontFaceX(arr, faceType, faceName, x, color); break;
			case Y_Plus: 	setupFontFaceY(arr, faceType, faceName, y, color); break;
			case Y_Minus: 	setupFontFaceY(arr, faceType, faceName, y, color); break;
			case Z_Plus: 	setupFontFaceZ(arr, faceType, faceName, z, color); break;
			case Z_Minus: 	setupFontFaceZ(arr, faceType, faceName, z, color); break;
		}	    
	}

    private static void buildSymbolsOnFace(CubeFaceNames faceId, int faceType) {
		int[] arr = getFaceSymbol(faceId);

		doTransforms(arr);
	
		int x = (faceType == X_Plus ? MAX_CUBE_COUNT - 1 : 0);
		int y = (faceType == Y_Plus ? MAX_CUBE_COUNT - 1 : 0);
		int z = (faceType == Z_Plus ? MAX_CUBE_COUNT - 1 : 0);

        Color color = Game.symbolColor;
	
		switch (faceType) {
			case X_Plus:	setupFontFaceSymbolX(arr, faceType, faceId, x, color); break;
			case X_Minus:	setupFontFaceSymbolX(arr, faceType, faceId, x, color);	break;
			case Y_Plus:	setupFontFaceSymbolY(arr, faceType, faceId, y, color);	break;
			case Y_Minus:	setupFontFaceSymbolY(arr, faceType, faceId, y, color);	break;
			case Z_Plus:	setupFontFaceSymbolZ(arr, faceType, faceId, z, color);	break;
			case Z_Minus:	setupFontFaceSymbolZ(arr, faceType, faceId, z, color);	break;
		}    
	}

    private static void buildSymbolsOnBase(CubeFaceNames faceId, int faceType) {
    	int[] arr = getFaceSymbolOnBase(faceId);

		doTransforms(arr);
	
		int x = (faceType == X_Plus ? MAX_CUBE_COUNT - 2 : 1);
		int y = (faceType == Y_Plus ? MAX_CUBE_COUNT - 2 : 1);
		int z = (faceType == Z_Plus ? MAX_CUBE_COUNT - 2 : 1);

        Color color = Game.symbolColor;
	
		switch (faceType) {
			case X_Plus:   setupFontFaceSymbolX(arr, faceType, faceId, x, color);	break;
			case X_Minus:	setupFontFaceSymbolX(arr, faceType, faceId, x, color);	break;
			case Y_Plus:	setupFontFaceSymbolY(arr, faceType, faceId, y, color);	break;
			case Y_Minus:	setupFontFaceSymbolY(arr, faceType, faceId, y, color);	break;
			case Z_Plus:	setupFontFaceSymbolZ(arr, faceType, faceId, z, color);	break;
			case Z_Minus:	setupFontFaceSymbolZ(arr, faceType, faceId, z, color);	break;
		}    
	}

    private static String getFaceHelpText(CubeFaceNames faceId) {
		switch (faceId) {
			case Face_Tutorial:
                return
                    "xxxxxxxxx" +
                    "x>>>>>>> " +
                    "xGREAT  x" +
                    "xNOW    x" +
                    "xSLIDE  x" +
                    "xRIGHT  x" +
                    "xTO     x" +
                    "xBEGIN  x" +
                    "xxxxxxxxx";

            
	    	case Face_Menu: {
                return
                    "xxxxxxx x" +
                    "   xxxx x" +
                    "xx      x" +
                    "xSLIDE   " +
                    "xxx     x" +
                    "xRED    x" +
                    "xx     xx" +
                    "xCUBE   x" +
                    "xxxxxxx x";
            }
			
			default:
				break;
		}
		return null;
	}

	private static String getNumberIn3CharLongString(int number) {
        String str = String.format(Locale.US, "%03d", number);
//        char[] chars = str.toCharArray();
//        for(int i = 0; i < chars.length - 1; ++i) {
//            if (chars[i] == '0') {
//                chars[i] = ' ';
//            }
//        }
//        str = new String(chars);
        return str;
    }

	private static String getScoreFaceText() {
         String score =
            "xxxxxxx x" +
            "x       x" +
            "xSTARS: x" +
            "xsss/540x" +
            "x       x" +
            "xSOLVED:x" +
            "xggg/180x" +
            "x       x" +
            "xxxxxxxxx";

        String stars = getNumberIn3CharLongString( GameProgress.getStarCount() ); //String.format(Locale.US, "%03d", GameProgress.getStarCount());
        String solved = getNumberIn3CharLongString( GameProgress.getSolvedLevelCount() ); //String.format(Locale.US, "%03d", GameProgress.getSolvedLevelCount());

        score = score.replace("sss", stars);
        score = score.replace("ggg", solved);
        return score;
    }

    private static String getFaceText(CubeFaceNames faceName) {
		final String empty =
		"xxxxxxxxx" +
		"x       x" +
		"x       x" +
		"x       x" +
		"x       x" +
		"x       x" +
		"x       x" +
		"x       x" +
		"xxxxxxxxx";

		final String tutorial =
		"xxxxxxxxx" +
		"xTAP     " +
		"xON     x" +
		"xRED    x" +
		"xCUBE   x" +
		"xAND    x" +
		"xSLIDE  x" +
		"xUP     x" +
		"xxxxxxxxx";
	    
		final String menu =
		"xxxxxxx x" +
		"         " +
		"xx      x" +
		"xPLAY    " +
		"xxx     x" +
		"xOPTIONSx" +
		"xx     xx" +
		"xSCORE  x" +
		"xxxxxxx x";
	    
	   	final String easy2 =
		"xx xxxxxx" +
		"xoBoxo ox" +
		"x A x   x" +
		"xoCoxo ox" +
		"x K x   x" +
		"xo oxo ox" +
		"xx   NEXx" +
		"xoxo o Tx" +
		"xxxxxxx x";
	
		final String easy3 =
		"xxxxxxx x" +
		"xoxo oxKx" +
		"xx   BACx" +
		"xo oxo ox" +
		"x   x   x" +
		"xo oxo ox" +
		"xxEXT   x" +
		"xoNoxo ox" +
		"xx xxxxxx";
		
		final String easy4 =
		"xx xxxxxx" +
		"xoBoxo ox" +
		"x ACK   x" +
		"xo oxo ox" +
		"x   x   x" +
		"xo oxoxox" +
		"xx    #xx" +
		"xoxoxo1xx" +
		"xxxxxx xx";
	    
	    final String options =
		"xxxxxxxxx" +
		"x       x" +
		"x MUSIC x" +
		"x       x" +
		"x       x" +
		"x SOUND x" +
		"x       x" +
		"x       x" +
		"xxxxxxx x";
	
//	    final String store =
//		"xxxxxxx x" +
//		"x       x" +
//		"xSTARS  x" +
//		"x sss   x" +
//		"x       x" +
//		"xSOLVED x" +
//		"x ggg   x" +
//		"x       x" +
//		"xxxxxxxxx";
			
		final String easy1 =
		"xxxxxx xx" +
		"xo oxo ox" +
		"xxxTAP   " +
		"  oONoAox" +
		"xNUMBxR x" +
		"xoToOo ox" +
		"xxPLAY xx" +
		"xo oxoxox" +
		"xx xxxxxx";
			
		final String normal1 =
		"xxxxxx xx" +
		"xoxo oUox" +
		" BACK P x" +
		"xo  o  ox" +
		"xxD    xx" +
		"xoOo o ox" +
		"x WxNEXT " +
		"xoNoxoxox" +
		"xx xxxxxx";
	
		final String normal2 =
		"xx xxxxxx" +
		"xo o o ox" +
		"x  x    x" +
		"xo o o ox" +
		"x  x    x" +
		"xo o o ox" +
		"x  x    x" +
		"x  o o ox" +
		"x xxxxxxx";
		
		final String normal3 =
		"x xxxxxxx" +
		"x  o o ox" +
		"x  x    x" +
		"xo o o ox" +
		"x  x    x" +
		"xo o o ox" +
		"x  x    x" +
		"xo o o ox" +
		"xx xxxxxx";
		
		final String normal4 =
		"xx xxxxxx" +
		"xx o o ox" +
		"xx x    x" +
		"xo o o ox" +
		"x  x    x" +
		"xo oxoxox" +
		"xx     xx" +
		"xoxo o ox" +
		"xxxxxx xx";
			
		final String hard1 =
		"xxxx xxxx" +
		"xo oUoxM " +
		"x  xP NEx" +
		"xoxo oUox" +
		"xxB    xx" +
		"xoAoxo ox" +
		" KCxDOWxx" +
		"xoxo oNox" +
		"xxxxxx xx";
	
		final String hard2 =
	    "xxxxxx xx" +
	    "xo o o ox" +
	    "x    x  x" +
	    "xo o o ox" +
	    "x    x  x" +
	    "xo o o ox" +
	    "x    x  x" +
	    "xo o o  x" +
	    "xxxxxxx x";
		
		final String hard3 =
	    "xxxxxxx x" +
	    "xo o o  x" +
	    "x    x  x" +
	    "xo o o ox" +
	    "x    x  x" +
	    "xo o o ox" +
	    "x    x  x" +
	    "xo o o ox" +
	    "xxxxxx xx";
		
		final String hard4 =
	    "xxxxxx xx" +
	    "xo o o ox" +
	    "x    x  x" +
	    "xo o o ox" +
	    "x    x  x" +
	    "xo oxo ox" +
	    "x  x   xx" +
	    "xo o ox x" +
	    "xxxx xxxx";
	
		switch (faceName) {
			case Face_Empty:	return empty;
				
			case Face_Tutorial: return tutorial;
				
			case Face_Menu:		return menu;
			case Face_Options:	return options;
	        case Face_Score:	return getScoreFaceText();
				
			case Face_Easy01:	return easy1;
			case Face_Easy02:	return easy2;
			case Face_Easy03:	return easy3;
			case Face_Easy04:	return easy4;
	            
			case Face_Normal01:	return normal1;
			case Face_Normal02:	return normal2;
			case Face_Normal03:	return normal3;
			case Face_Normal04:	return normal4;

			case Face_Hard01:	return hard1;
			case Face_Hard02:	return hard2;
			case Face_Hard03:	return hard3;
			case Face_Hard04:	return hard4;
				
			default:
				break;
		}
		return null;
	}

    private static String getFaceTitle(CubeFaceNames faceId) {
		final String empty =
		"xxxxxxxxx" +
		"x       x" +
		"x       x" +
		"x       x" +
		"x       x" +
		"x       x" +
		"x       x" +
		"x       x" +
		"xxxxxxxxx";

		final String tutorial =
        "WELCOME x" +
		"x       x" +
		"x       x" +
		"x       x" +
		"x       x" +
		"x       x" +
		"x       x" +
		"x       x" +
		"xxxxxxxxx";
	
		final String menu =
		"CUBExxx x" +
		"   TRAZ x" +
		"xx      x" +
		"         " +
		"xxxx    x" +
		"x       x" +
		"xxx    xx" +
		"x       x" +
		"xxxxxxx x";
	
	    final String options =
		"OPTIONSxx" +
		"x       x" +
		"x       x" +
		"x       x" +
		"x       x" +
		"xx      x" +
		"x       x" +
		"x       x" +
		"xxxxxxx x";

	    final String store =
		"SCORE   x" +
		"x       x" +
		"xx      x" +
		"x       x" +
		"xx      x" +
		"x       x" +
		"xx      x" +
		"x    x  x" +
		"xxxxxxxxx";	
	
		final String easy1 =
		"EASY#1 xx" +
		"xo oxo ox" +
		"xxx      " +
		"  o  o ox" +
		"x    E  x" +
		"xo o o ox" +
		"xx     xx" +
		"xo oxoxox" +
		"xx xxxxxx";
	
		final String easy2 =
		"Ex xxxxxx" +
		"Ao oxo ox" +
		"S   x   x" +
		"Yo oxo ox" +
		"#   x   x" +
		"2o oxo ox" +
		"xx      x" +
		"xoxo o  x" +
		"xxxxxxx x";
	
		final String easy3 =
		"Exxxxxx x" +
		"Aoxo o  x" +
		"Sx      x" +
		"Yo oxo ox" +
		"#   x   x" +
		"3o oxo ox" +
		"xx  x   x" +
		"xo oxo ox" +
		"xx xxxxxx";
	
		final String easy4 =
		"Ex xxxxxx" +
		"Ao oxo ox" +
		"S   x   x" +
		"Yo oxo ox" +
		"#   x   x" +
		"4o oxoxox" +
		"xx     xx" +
		"xoxoxo xx" +
		"xxxxxx xx";
	
		final String normal1 =
		"NORMAL #1" +
		"xoxo o ox" +
		"    x   x" +
		"xo  o  ox" +
		"xx     xx" +
		"xo o o ox" +
		"x  x     " +
		"xo oxoxox" +
		"xx xxxxxx";
	
		final String normal2 =
		"Nx xxxxxx" +
		"Oo o o ox" +
		"R  x    x" +
		"Mo o o ox" +
		"A  x    x" +
		"Lo o o ox" +
		"#  x    x" +
		"2  o o ox" +
		"x xxxxxxx";
	
		final String normal3 =
		"N xxxxxxx" +
		"O  o o ox" +
		"R  x    x" +
		"Mo o o ox" +
		"A  x    x" +
		"Lo o o ox" +
		"#  x    x" +
		"3o o o ox" +
		"xx xxxxxx";
	
		final String normal4 =
		"Nx xxxxxx" +
		"Ox o o ox" +
		"Rx x    x" +
		"Mo o o ox" +
		"A  x    x" +
		"Lo oxoxox" +
		"#x     xx" +
		"4oxo o ox" +
		"xxxxxx xx";
	
		final String hard1 =
		"HARD #1xx" +
		"xo o ox  " +
		"x  x    x" +
		"xoxo o ox" +
		"xx     xx" +
		"xo oxo ox" +
		"   x   xx" +
		"xoxo o ox" +
		"xxxxxx xx";
	
		final String hard2 =
	    "Hxxxxx xx" +
	    "Ao o o ox" +
	    "R    x  x" +
	    "Do o o ox" +
	    "#    x  x" +
	    "2o o o ox" +
	    "x    x  x" +
	    "xo o o  x" +
	    "xxxxxxx x";
	
		final String hard3 =
	    "Hxxxxxx x" +
	    "Ao o o  x" +
	    "R    x  x" +
	    "Do o o ox" +
	    "#    x  x" +
	    "3o o o ox" +
	    "x    x  x" +
	    "xo o o ox" +
	    "xxxxxx xx";
		
		final String hard4 =
	    "Hxxxxx xx" +
	    "Ao o o ox" +
	    "R    x  x" +
	    "Do o o ox" +
	    "#    x  x" +
	    "4o oxo ox" +
	    "x  x   xx" +
	    "xo o ox x" +
	    "xxxx xxxx";

		switch (faceId) {
			case Face_Empty:	return empty;
			
			case Face_Tutorial: return tutorial;
			
			case Face_Menu:		return menu;
			case Face_Options:	return options;
        	case Face_Score:	return store;
			
			case Face_Easy01:	return easy1;
			case Face_Easy02:	return easy2;
			case Face_Easy03:	return easy3;
			case Face_Easy04:	return easy4;
            
			case Face_Normal01:	return normal1;
			case Face_Normal02:	return normal2;
			case Face_Normal03:	return normal3;
			case Face_Normal04:	return normal4;
			
			case Face_Hard01:	return hard1;
			case Face_Hard02:	return hard2;
			case Face_Hard03:	return hard3;
			case Face_Hard04:	return hard4;
			
			default:
				break;
		}
		return null;
	}

    private static String getFace(CubeFaceNames faceId) {
		final String empty =
		"xxxxxxxxx" +
		"x       x" +
		"x       x" +
		"x       x" +
		"x       x" +
		"x       x" +
		"x       x" +
		"x       x" +
		"xxxxxxxxx";
	
		final String tutorial =
		"xxxxxxxxx" +
		"x        " +
		"x       x" +
		"x       x" +
		"x       x" +
		"x       x" +
		"x       x" +
		"x       x" +
		"xxxxxxxxx";
    
		final String options =
		"xxxxxxxxx" +
		"x       x" +
		"x       x" +
		"x       x" +
		"x       x" +
		"xx      x" +
		"x       x" +
		"x       x" +
		"xxxxxxx x";

		final String menu =
		"xxxxxxx x" +
		"   xxxx x" +
		"xx      x" +
		"x        " +
		"xxx     x" +
		"x       x" +
		"xxx    xx" +
		"x       x" +
		"xxxxxxx x";
    
		final String store =
		"xxxxxxx x" +
		"x       x" +
		"x       x" +
		"x       x" +
		"x xxxxx x" +
		"x       x" +
		"x       x" +
		"x       x" +
		"xxxxxxxxx";
    
		final String easy1 =
		"xxxxxx xx" +
		"x1x2x3 4x" +
		"xxx      " +
		"  5  6 7x" +
		"x    x xx" +
		"x8 9 A Bx" +
		"xx     xx" +
		"xC DxExFx" +
		"xx xxxxxx";
    
		final String easy2 =
		"xx xxxxxx" +
		"x1 2x3x4x" +
		"xx x   xx" +
		"x5 6 7 8x" +
		"xx x x xx" +
		"x9 AxB Cx" +
		"xx      x" +
		"xDxExFx x" +
		"xxxxxxx x";

		final String easy3 =
		"xxxxxxx x" +
		"x1x2x3x x" +
		"xx      x" +
		"x4 5x6x7x" +
		"xx     xx" +
		"x8x9xA Bx" +
		"xx     xx" +
		"xC DxExFx" +
		"xx xxxxxx";
    
		final String easy4 =
		"xx xxxxxx" +
		"x1 2x3x4x" +
		"xx     xx" +
		"x5x6x7 8x" +
		"xx     xx" +
		"x9 AxBxCx" +
		"xx     xx" +
		"xDxExF xx" +
		"xxxxxx xx";
    
		final String normal1 =
		"xxxxxx xx" +
		"x1x2x3 4x" +
		"     x xx" +
		"x5   6 7x" +
		"xx     xx" +
		"x8 9xA Bx" +
		"xx       " +
		"xC DxExFx" +
		"xx xxxxxx";

		final String normal2 =
		"xx xxxxxx" +
		"x1 2x3x4x" +
		"xx     xx" +
		"x5 6x7 8x" +
		"xx     xx" +
		"x9xAxB Cx" +
		"x      xx" +
		"x  DxExFx" +
		"x xxxxxxx";

		final String normal3 =
		"x xxxxxxx" +
		"x  1x2x3x" +
		"x    xxxx" +
		"x4 5 6x7x" +
		"xx     xx" +
		"x8 9 A Bx" +
		"xx x   xx" +
		"xC DxExFx" +
		"xx xxxxxx";
	
		final String normal4 =
		"xx xxxxxx" +
		"xx 1x2x3x" +
		"xx     xx" +
		"x4 5 6 7x" +
		"xx   x xx" +
		"x8x9xA Bx" +
		"xxxxxx xx" +
		"xCxDxE Fx" +
		"xxxxxx xx";

		final String hard1 =
		"xxxx xxxx" +
		"x1x2 3x  " +
		"xxxx    x" +
		"x4x5 6 7x" +
		"xx     xx" +
		"x8 9xA Bx" +
		"   x   xx" +
		"xCxDxE Fx" +
		"xxxxxx xx";

		final String hard2 =
	    "xxxxxx xx" +
	    "x1x2x3 4x" +
	    "xx     xx" +
	    "x5 6x7x8x" +
	    "xx x    x" +
	    "x9 AxBxCx" +
	    "xx      x" +
	    "xDxExFx x" +
	    "xxxxxxx x";

		final String hard3 =
	    "xxxxxxx x" +
	    "x1x2x3x x" +
	    "xx      x" +
	    "x4 5 6 7x" +
	    "xx     xx" +
	    "x8 9 A Bx" +
	    "xx     xx" +
	    "xCxDxE Fx" +
	    "xxxxxx xx";

		final String hard4 =
	    "xxxxxx xx" +
	    "x1x2x3 4x" +
	    "xx     xx" +
	    "x5 6 7x8x" +
	    "xx     xx" +
	    "x9xA B Cx" +
	    "xxxx    x" +
	    "xDxE Fxxx" +
	    "xxxx xxxx";
	
		switch (faceId) {
			case Face_Empty:	return empty;
		
			case Face_Tutorial: return tutorial;
		
			case Face_Menu:		return menu;
			case Face_Options:	return options;
			case Face_Score:	return store;
	
			case Face_Easy01:	return easy1;
			case Face_Easy02:	return easy2;
			case Face_Easy03:	return easy3;
			case Face_Easy04:	return easy4;
        
			case Face_Normal01:	return normal1;
			case Face_Normal02:	return normal2;
			case Face_Normal03:	return normal3;
			case Face_Normal04:	return normal4;
		
			case Face_Hard01:	return hard1;
			case Face_Hard02:	return hard2;
			case Face_Hard03:	return hard3;
			case Face_Hard04:	return hard4;

			default:
				break;
		}
		return null;
	}

    private static int[] getFaceSymbol(CubeFaceNames faceId) {
		switch (faceId) {
			case Face_Menu:
                return new int[]{
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Info,
                };

			default:
                return new int[]{
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
                };
        }
    }


	private static int[] getFaceSymbolOnBase(CubeFaceNames faceId) {
		switch (faceId) {
			case Face_Menu:
                return new int[] {
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_GoUp, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_GoRight, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_GoDown, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
                };

			case Face_Options:
                return new int[] {
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Minus, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Plus, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Minus, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Plus, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
                };

			case Face_Easy01:
                return new int[] {
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_GoUp, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_GoLeft, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_GoDown, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
                };
            
			default:
                return new int[] {
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
						Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty, Symbol_Empty,
                };
        }
	}
    
}
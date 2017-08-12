package com.almagems.cubetraz.scenes.menu;


import com.almagems.cubetraz.scenes.level.Creator;
import com.almagems.cubetraz.game.Game;
import com.almagems.cubetraz.cubes.Cube;
import com.almagems.cubetraz.cubes.CubeFont;
import com.almagems.cubetraz.cubes.CubePos;
import com.almagems.cubetraz.graphics.TexturedQuad;

import static com.almagems.cubetraz.game.Constants.*;


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

    private static void setupFaceX(final int[] arr, int faceType, CubeFaceNamesEnum faceId) {
	    int x = (faceType == Face_X_Plus ? MAX_CUBE_COUNT - 1 : 0);
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

    private static void setupFaceY(final int[] arr, int faceType, CubeFaceNamesEnum faceId) {
	    int y = (faceType == Face_Y_Plus ? MAX_CUBE_COUNT - 1 : 0);
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

    private static void setupFaceZ(final int[] arr, int faceType, CubeFaceNamesEnum faceId) {
	    int z = (faceType == Face_Z_Plus ? MAX_CUBE_COUNT - 1 : 0);
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

    private static void setupFontFaceX(final int[] arr, int faceType, CubeFaceNamesEnum faceId, int x) {
        int counter = 0;
        char ch;
	    Cube cube;
	
	    for (int y = MAX_CUBE_COUNT - 1; y > -1; --y) {
		    for (int z = MAX_CUBE_COUNT - 1; z > -1; --z) {
			    cube = Game.cubes[x][y][z];
                ch = (char)arr[counter];
			
			    if (' ' != ch && 'x' != ch && 'o' != ch) {
				    Creator.addCubeFont(ch, new CubePos(x,y,z), faceType);
			    }
			    ++counter;
		    }
	    }
    }

    private static void setupFontFaceY(final int[] arr, int faceType, CubeFaceNamesEnum faceId, int y) {
        int counter = 0;
        char ch;
	    Cube cube;
    
	    for (int z = 0; z < MAX_CUBE_COUNT; ++z) {
		    for (int x = 0; x < MAX_CUBE_COUNT; ++x) {
			    cube = Game.cubes[x][y][z];
                ch = (char)arr[counter];
			
			    if (' ' != ch && 'x' != ch && 'o' != ch) {
				    Creator.addCubeFont(ch, new CubePos(x,y,z), faceType);
			    }
			    ++counter;
		    }
	    }
    }

    private static void setupFontFaceZ(final int[] arr, int faceType, CubeFaceNamesEnum faceId, int z) {
        int counter = 0;
        char ch;
	    Cube cube;
    
	    for (int y = MAX_CUBE_COUNT - 1; y > -1; --y) {
		    for (int x = 0; x < MAX_CUBE_COUNT; ++x) {
			    cube = Game.cubes[x][y][z];
                ch = (char)arr[counter];
	
			    if (' ' != ch && 'x' != ch && 'o' != ch) {
				    Creator.addCubeFont(ch, new CubePos(x, y, z), faceType);
			    }
			    ++counter;
		    }
	    }
    }

    private static void setupFontFaceSymbolX(final int[] a, int faceType, CubeFaceNamesEnum faceId, int x) {
        int counter = 0;
        int symbol_id;
	    Cube cube;
	
	    for (int y = MAX_CUBE_COUNT - 1; y > -1; --y) {
		    for (int z = MAX_CUBE_COUNT - 1; z > -1; --z) {
			    cube = Game.cubes[x][y][z];
			    symbol_id = a[counter];
			
			    if (SymbolEmpty != symbol_id) {
				    Creator.addCubeFontSymbol(symbol_id, new CubePos(x,y,z), faceType);
			    }
			    ++counter;
		    }
	    }
    }

    private static void setupFontFaceSymbolY(final int[] a, int faceType, CubeFaceNamesEnum faceId, int y) {
        int counter = 0;
        int symbol_id;
	    Cube cube;
    
	    for (int z = 0; z < MAX_CUBE_COUNT; ++z) {
		    for (int x = 0; x < MAX_CUBE_COUNT; ++x) {
			    cube = Game.cubes[x][y][z];
			    symbol_id = a[counter];
			
			    if (SymbolEmpty != symbol_id) {
				    Creator.addCubeFontSymbol(symbol_id, new CubePos(x,y,z), faceType);
			    }
			    ++counter;
		    }
	    }
    }

    private static void setupFontFaceSymbolZ(final int[] a, int faceType, CubeFaceNamesEnum faceId, int z) {
        int counter = 0;
        int symbol_id;
	    Cube cube;
    
	    for (int y = MAX_CUBE_COUNT - 1; y > -1; --y) {
            for (int x = 0; x < MAX_CUBE_COUNT; ++x) {
			    cube = Game.cubes[x][y][z];
			    symbol_id = a[counter];
			
			    if (SymbolEmpty != symbol_id) {
				    Creator.addCubeFontSymbol(symbol_id, new CubePos(x,y,z), faceType);
			    }
			    ++counter;
		    }
	    }
    }

//void cMenuFaceBuilder::DumpFace(char* ar)
//{
//	final int face_width = MAX_CUBE_COUNT - 1;
//	final int face_size = MAX_CUBE_COUNT * MAX_CUBE_COUNT;
//	
//	printf("\n");
//	
//	int cnt = 0;
//	for (int i = 0; i < face_size; ++i)
//	{
//		printf("%c", ar[i]);
//		++cnt;
//		
//		if (cnt > face_width)
//		{
//			cnt = 0;
//			printf("\n");
//		}
//	}
//}

//void cMenuFaceBuilder::Custom()
//{
//	return;
//
//    char* a = new char[FACE_SIZE];
//    
//    GetFace(Face_Normal03, a);
//    
//    //printf("\nbefore...");
//    DumpFace(a);
//	MirrorFaceVert(a);
//    //printf("\nafter...");
//    DumpFace(a);
//    
////	char* arr = GetFace(Face_Easy04);
////	ConvertToIntArray(arr);
////	
////	RotateFace90CW(&iarr[0]);
////	//RotateFace90CW(&iarr[0]);
////	//RotateFace90CW(&iarr[0]);
////	
////	DumpIntArrayAsChars();
//}


    public static void build(CubeFaceNamesEnum faceId, int faceType) {
	    buildCubeFaces(faceId, faceType);
	    buildTitleTexts(faceId, faceType);
	    buildTexts(faceId, faceType, false);
	    buildSymbolsOnFace(faceId, faceType);
	    buildSymbolsOnBase(faceId, faceType);
	
	    resetTransforms();
    }

    private static int[] convertToIntArray(String str) {
        int[] arr = new int[str.length()];
        for(int i = 0; i < str.length(); ++i) {
            arr[i] = str.charAt(i); //(str.charAt(i) - '0');
        }
        return arr;
    }

    private static void buildCubeFaces(CubeFaceNamesEnum faceId, int faceType) {
        String str = getFace(faceId);
        int[] arr = convertToIntArray(str);

	    doTransforms(arr);
	
	    switch (faceType) {
		    case Face_X_Plus: 	setupFaceX(arr, faceType, faceId); break;
		    case Face_X_Minus: 	setupFaceX(arr, faceType, faceId); break;
		    case Face_Y_Plus: 	setupFaceY(arr, faceType, faceId); break;
		    case Face_Y_Minus: 	setupFaceY(arr, faceType, faceId); break;
		    case Face_Z_Plus: 	setupFaceZ(arr, faceType, faceId); break;
		    case Face_Z_Minus: 	setupFaceZ(arr, faceType, faceId); break;
	    }
    }

    private static void setFontFromCube(CubeFont cubeFontTarget, CubePos cp, CubePos offset, int faceType) {
        char ch;
        Cube cube;
        CubeFont cubeFont;
        TexturedQuad texturedQuad;
    
        cube = Game.cubes[cp.x + offset.x][cp.y + offset.y][cp.z + offset.z];
        cubeFont = cube.ar_fonts[faceType];

        if (cubeFont != null) {
            texturedQuad = cubeFont.getFont();
        
            if (texturedQuad != null) {
                ch = texturedQuad.ch;
                cubeFontTarget.init(ch, cp);
            }
        }
    }

    public static void buildTexts(CubeFaceNamesEnum faceId, int faceType, boolean alt) {
        String str;
	
	    if (!alt) {
            str = getFaceText(faceId);
        } else {
            str = getFaceHelpText(faceId);
        }

        int[] arr = convertToIntArray(str);

	    doTransforms(arr);
	
	    int x = (faceType == Face_X_Plus ? MAX_CUBE_COUNT - 1 : 0);
	    int y = (faceType == Face_Y_Plus ? MAX_CUBE_COUNT - 1 : 0);
	    int z = (faceType == Face_Z_Plus ? MAX_CUBE_COUNT - 1 : 0);
	
	    switch (faceType) {
		    case Face_X_Plus: 	setupFontFaceX(arr, faceType, faceId, x - 1); break;
		    case Face_X_Minus: 	setupFontFaceX(arr, faceType, faceId, x + 1); break;
		    case Face_Y_Plus: 	setupFontFaceY(arr, faceType, faceId, y - 1); break;
		    case Face_Y_Minus: {
			    setupFontFaceY(arr, faceType, faceId, y + 1);
            
                if ( CubeFaceNamesEnum.Face_Store == faceId) {
                    CubePos cp;
                    cp = new CubePos(1, 0, 6);
                    setFontFromCube(Game.menu.m_cubefont_noads, cp, new CubePos(0, 1, 0), faceType);
                    Game.menu.m_cubefont_noads.pos.y -= FONT_OVERLAY_OFFSET;

                    cp = new CubePos(1, 0, 4);
                    setFontFromCube(Game.menu.m_cubefont_solvers, cp, new CubePos(0, 1, 0), faceType);
                    Game.menu.m_cubefont_solvers.pos.y -= FONT_OVERLAY_OFFSET;
                
                    cp = new CubePos(1, 0, 2);
                    setFontFromCube(Game.menu.m_cubefont_restore, cp, new CubePos(0, 1, 0), faceType);
                    Game.menu.m_cubefont_restore.pos.y -= FONT_OVERLAY_OFFSET;
                }
            }
			break;
			
			case Face_Z_Plus: {
				setupFontFaceZ(arr, faceType, faceId, z - 1);
            
            	if (CubeFaceNamesEnum.Face_Menu == faceId) {
                	CubePos cp;
                	cp = new CubePos(1, 5, 8);
                	setFontFromCube(Game.menu.m_cubefont_play, cp, new CubePos(0, 0, -1), faceType);
                	Game.menu.m_cubefont_play.pos.z += FONT_OVERLAY_OFFSET;
                
                	cp = new CubePos(1, 3, 8);
                	setFontFromCube(Game.menu.m_cubefont_options, cp, new CubePos(0, 0, -1), faceType);
                	Game.menu.m_cubefont_options.pos.z += FONT_OVERLAY_OFFSET;
                
                	cp = new CubePos(1, 1, 8);
                	setFontFromCube(Game.menu.m_cubefont_store, cp, new CubePos(0, 0, -1), faceType);
                	Game.menu.m_cubefont_store.pos.z += FONT_OVERLAY_OFFSET;                
            	}
        	}
			break;
			
			case Face_Z_Minus: setupFontFaceZ(arr, faceType, faceId, z + 1); break;
		}        	
	}

    private static void buildTitleTexts(CubeFaceNamesEnum faceId, int faceType) {
    	String str = getFaceTitle(faceId);
        int[] arr = convertToIntArray(str);
	
		doTransforms(arr);
	
		int x = (faceType == Face_X_Plus ? MAX_CUBE_COUNT - 1 : 0);
		int y = (faceType == Face_Y_Plus ? MAX_CUBE_COUNT - 1 : 0);
		int z = (faceType == Face_Z_Plus ? MAX_CUBE_COUNT - 1 : 0);
	
		switch (faceType) {
			case Face_X_Plus: 	setupFontFaceX(arr, faceType, faceId, x); break;
			case Face_X_Minus: 	setupFontFaceX(arr, faceType, faceId, x); break;
			case Face_Y_Plus: 	setupFontFaceY(arr, faceType, faceId, y); break;
			case Face_Y_Minus: 	setupFontFaceY(arr, faceType, faceId, y); break;
			case Face_Z_Plus: 	setupFontFaceZ(arr, faceType, faceId, z); break;
			case Face_Z_Minus: 	setupFontFaceZ(arr, faceType, faceId, z); break;
		}	    
	}

    private static void buildSymbolsOnFace(CubeFaceNamesEnum faceId, int faceType) {
		int[] arr = getFaceSymbol(faceId);

		doTransforms(arr);
	
		int x = (faceType == Face_X_Plus ? MAX_CUBE_COUNT - 1 : 0);
		int y = (faceType == Face_Y_Plus ? MAX_CUBE_COUNT - 1 : 0);
		int z = (faceType == Face_Z_Plus ? MAX_CUBE_COUNT - 1 : 0);
	
		switch (faceType) {
			case Face_X_Plus:	setupFontFaceSymbolX(arr, faceType, faceId, x); break;
			case Face_X_Minus:	setupFontFaceSymbolX(arr, faceType, faceId, x);	break;
			case Face_Y_Plus:	setupFontFaceSymbolY(arr, faceType, faceId, y);	break;
			case Face_Y_Minus:	setupFontFaceSymbolY(arr, faceType, faceId, y);	break;
			case Face_Z_Plus:	setupFontFaceSymbolZ(arr, faceType, faceId, z);	break;
			case Face_Z_Minus:	setupFontFaceSymbolZ(arr, faceType, faceId, z);	break;
		}    
	}

    private static void buildSymbolsOnBase(CubeFaceNamesEnum faceId, int faceType) {
    	int[] arr = getFaceSymbolOnBase(faceId);

		doTransforms(arr);
	
		int x = (faceType == Face_X_Plus ? MAX_CUBE_COUNT - 2 : 1);
		int y = (faceType == Face_Y_Plus ? MAX_CUBE_COUNT - 2 : 1);
		int z = (faceType == Face_Z_Plus ? MAX_CUBE_COUNT - 2 : 1);
	
		switch (faceType) {
			case Face_X_Plus:   setupFontFaceSymbolX(arr, faceType, faceId, x);	break;
			case Face_X_Minus:	setupFontFaceSymbolX(arr, faceType, faceId, x);	break;
			case Face_Y_Plus:	setupFontFaceSymbolY(arr, faceType, faceId, y);	break;
			case Face_Y_Minus:	setupFontFaceSymbolY(arr, faceType, faceId, y);	break;
			case Face_Z_Plus:	setupFontFaceSymbolZ(arr, faceType, faceId, z);	break;
			case Face_Z_Minus:	setupFontFaceSymbolZ(arr, faceType, faceId, z);	break;
		}    
	}

    private static String getFaceHelpText(CubeFaceNamesEnum faceId) {
		switch (faceId) {
			case Face_Tutorial:
                return
                    "xxxxxxxxx" +
                    "x>>>>>>> " +
                    "xNOW    x" +
                    "xSLIDE  x" +
                    "xRIGHT  x" +
                    "xTO     x" +
                    "xSTART  x" +
                    "xGAME   x" +
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

    private static String getFaceText(CubeFaceNamesEnum faceId) {
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
		"xEXTRA  x" +
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
	
	    final String store =
		"xxxxxxx x" +
		"xx      x" +
		"xHINT   x" +
		"xx      x" +
		"xSOLVER x" +
		"xx      x" +
		"xUNLOCK x" +
		"xx      x" +
		"xxxxxxxxx";
			
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
	
		switch (faceId) {
			case Face_Empty:	return empty;
				
			case Face_Tutorial: return tutorial;
				
			case Face_Menu:		return menu;
			case Face_Options:	return options;
	        case Face_Store:	return store;
				
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

    private static String getFaceTitle(CubeFaceNamesEnum faceId) {
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
		"BEGINNING" +
		"x        " +
		"xxxxxxx x" +
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
		"EXTRA   x" +
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
        	case Face_Store:	return store;
			
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

    private static String getFace(CubeFaceNamesEnum faceId) {
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
		"xx      x" +
		"x       x" +
		"xx      x" +
		"x       x" +
		"xx      x" +
		"x       x" +
		"xx      x" +
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
			case Face_Store:	return store;
	
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

    private static int[] getFaceSymbol(CubeFaceNamesEnum faceId) {
		switch (faceId) {
			case Face_Menu:
                return new int[]{
                        SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                        SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                        SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                        SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                        SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                        SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                        SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                        SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                        SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolInfo,
                };

			default:
                return new int[]{
                        SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                        SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                        SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                        SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                        SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                        SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                        SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                        SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                        SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                };
        }
    }


	private static int[] getFaceSymbolOnBase(CubeFaceNamesEnum faceId) {
		switch (faceId) {
			case Face_Menu:
                return new int[] {
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,     SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolGoUp,      SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,     SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolGoRight,   SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,     SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,     SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,     SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolGoDown,    SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,     SymbolEmpty,
                };

			case Face_Options:
                return new int[] {
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,     SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,     SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,     SymbolEmpty,
                    SymbolEmpty, SymbolMinus, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolPlus,      SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,     SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,     SymbolEmpty,
                    SymbolEmpty, SymbolMinus, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolPlus,      SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,     SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,     SymbolEmpty,
                };

			case Face_Easy01:
                return new int[] {
                    SymbolEmpty, SymbolEmpty,  SymbolEmpty,  SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,   SymbolEmpty,
                    SymbolEmpty, SymbolEmpty,  SymbolEmpty,  SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolGoUp,  SymbolEmpty,   SymbolEmpty,
                    SymbolEmpty, SymbolEmpty,  SymbolEmpty,  SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,   SymbolEmpty,
                    SymbolEmpty, SymbolGoLeft, SymbolEmpty,  SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,   SymbolEmpty,
                    SymbolEmpty, SymbolEmpty,  SymbolEmpty,  SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,   SymbolEmpty,
                    SymbolEmpty, SymbolEmpty,  SymbolEmpty,  SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,   SymbolEmpty,
                    SymbolEmpty, SymbolEmpty,  SymbolEmpty,  SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,   SymbolEmpty,
                    SymbolEmpty, SymbolEmpty,  SymbolGoDown, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,   SymbolEmpty,
                    SymbolEmpty, SymbolEmpty,  SymbolEmpty,  SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,   SymbolEmpty,
                };
            
			default:
                return new int[] {
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                };
        }
	}
    
}
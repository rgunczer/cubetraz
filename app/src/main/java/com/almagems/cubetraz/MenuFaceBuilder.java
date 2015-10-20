package com.almagems.cubetraz;


import static com.almagems.cubetraz.Constants.*;


public final class MenuFaceBuilder {

	private static final FaceTransformsEnum[] transforms = new FaceTransformsEnum[MAX_FACE_TRANSFORM_COUNT];
	
    // ctor
    private MenuFaceBuilder() {
    }

/*
	template<class T>
	static void DoTransforms(T a[])
	{
		for (int i = 0; i < MAX_FACE_TRANSFORM_COUNT; ++i)
		{
			switch (transforms[i])
			{
				case NoTransform:
					break;
					
				case RotateCW90:
					RotateFace90CW(a);
					break;
					
				case RotateCCW90:
					RotateFace90CCW(a);
					break;
					
				case MirrorHoriz:
					MirrorFaceHoriz(a);
					break;
					
				case MirrorVert:
					MirrorFaceVert(a);
					break;
			}
		}
	}

	template<class T>
    static void RotateFace90CW(T a[])
	{		
		T tmp[MAX_CUBE_COUNT * MAX_CUBE_COUNT];
		
		int start = MAX_CUBE_COUNT - 1;
		int row = 0;
		int pos = start;
		
		for (int i = 0; i < MAX_CUBE_COUNT * MAX_CUBE_COUNT; ++i)
		{
			if (row > MAX_CUBE_COUNT - 1)
			{
				pos = --start;
				row = 0;
			}
			
			tmp[pos] = a[i];
			pos += MAX_CUBE_COUNT;
			++row;
		}
        
		for (int i = 0; i < MAX_CUBE_COUNT * MAX_CUBE_COUNT; ++i)
			a[i] = tmp[i];
	}
	
	template<typename T>
    static void RotateFace90CCW(T a[])
	{		
		T tmp[MAX_CUBE_COUNT * MAX_CUBE_COUNT];
        
		int col = 0;
		int row = MAX_CUBE_COUNT - 1;
		int pos;
		
		for (int i = 0; i < MAX_CUBE_COUNT * MAX_CUBE_COUNT; ++i)
		{
			if (row < 0)
			{
				++col;
				row = MAX_CUBE_COUNT - 1;
			}
			
			pos = row * MAX_CUBE_COUNT + col;
			
			tmp[pos] = a[i];
			--row;
		}
		
		for (int i = 0; i < MAX_CUBE_COUNT * MAX_CUBE_COUNT; ++i)
			a[i] = tmp[i];
	}

	template<class T>
    static void MirrorFaceHoriz(T a[])
	{
		T tmp[MAX_CUBE_COUNT * MAX_CUBE_COUNT];
		
		int row = MAX_CUBE_COUNT - 1;
		int col = 0;
		int pos;
		
		for (int i = 0; i < MAX_CUBE_COUNT * MAX_CUBE_COUNT; ++i)
		{
			if (col > MAX_CUBE_COUNT - 1)
			{
				--row;
				col = 0;
			}
			
			pos = row * MAX_CUBE_COUNT + col;
			
			tmp[pos] = a[i];
			
			++col;
		}
		
		for (int i = 0; i < MAX_CUBE_COUNT * MAX_CUBE_COUNT; ++i)
			a[i] = tmp[i];
	}

	template<class T>
    static void MirrorFaceVert(T a[])
	{
		T tmp[MAX_CUBE_COUNT * MAX_CUBE_COUNT];
		
		int row = 0;
		int col = MAX_CUBE_COUNT - 1;
		int pos;
		
		for (int i = 0; i < MAX_CUBE_COUNT * MAX_CUBE_COUNT; ++i)
		{
			if (col < 0)
			{
				++row;
				col = MAX_CUBE_COUNT - 1;
			}
			
			pos = row * MAX_CUBE_COUNT + col;
			
			tmp[pos] = a[i];
			
			--col;
		}
		
		for (int i = 0; i < MAX_CUBE_COUNT * MAX_CUBE_COUNT; ++i)
			a[i] = tmp[i];
	}
*/

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

    public static void addTransform(FaceTransformsEnum type, int count) {
	    for (int i = 0; i < MAX_FACE_TRANSFORM_COUNT; ++i) {
		    if (FaceTransformsEnum.NoTransform == transforms[i]) {
                for (int j = i; j < i + count; ++j) {
                    transforms[j] = type;
                }
                return;
            }
		}
	}

    public static void setupFaceX(final String a, int face_type, CubeFaceNamesEnum face_id) {
	    int x = (face_type == Face_X_Plus ? MAX_CUBE_COUNT - 1 : 0);
        int counter = 0;
        char ch;
	    Cube cube;
	
	    for (int y = MAX_CUBE_COUNT - 1; y > -1; --y) {
		    for (int z = MAX_CUBE_COUNT - 1; z > -1; --z) {
			    cube = Game.cubes[x][y][z];
			    ch = a[counter];
			
			    Game.setCubeTypeOnFace(cube, ch, face_type, face_id);
			    ++counter;
		    }
	    }
    }

    public static void setupFaceY(final String a, int face_type, CubeFaceNamesEnum face_id) {
	    int y = (face_type == Face_Y_Plus ? MAX_CUBE_COUNT - 1 : 0);
        int counter = 0;
        char ch;
	    Cube cube;
    
	    for (int z = 0; z < MAX_CUBE_COUNT; ++z) {
		    for (int x = 0; x < MAX_CUBE_COUNT; ++x) {
			    cube = Game.cubes[x][y][z];
			    ch = a[counter];
			
			    Game.setCubeTypeOnFace(cube, ch, face_type, face_id);
			    ++counter;
		    }
	    }
    }

    public static void setupFaceZ(final String a, int face_type, CubeFaceNamesEnum face_id) {
	    int z = (face_type == Face_Z_Plus ? MAX_CUBE_COUNT - 1 : 0);
        int counter = 0;
        char ch;
	    Cube cube;
    
	    for (int y = MAX_CUBE_COUNT - 1; y > -1; --y) {
		    for (int x = 0; x < MAX_CUBE_COUNT; ++x) {
			    cube = Game.cubes[x][y][z];
			    ch = a[counter];
			
			    Game.setCubeTypeOnFace(cube, ch, face_type, face_id);
			    ++counter;
		    }
	    }
    }

    public static void setupFontFaceX(final String a, int face_type, CubeFaceNamesEnum face_id, int x) {
        int counter = 0;
        char ch;
	    Cube cube;
	
	    for (int y = MAX_CUBE_COUNT - 1; y > -1; --y) {
		    for (int z = MAX_CUBE_COUNT - 1; z > -1; --z) {
			    cube = Game.cubes[x][y][z];
			    ch = a[counter];
			
			    if (' ' != ch && 'x' != ch && 'o' != ch) {
				    Creator.addCubeFont(ch, new CubePos(x,y,z), face_type);
			    }
			    ++counter;
		    }
	    }
    }

    public static void setupFontFaceY(final String a, int face_type, CubeFaceNamesEnum face_id, int y) {
        int counter = 0;
        char ch;
	    Cube cube;
    
	    for (int z = 0; z < MAX_CUBE_COUNT; ++z) {
		    for (int x = 0; x < MAX_CUBE_COUNT; ++x) {
			    cube = Game.cubes[x][y][z];
			    ch = a[counter];
			
			    if (' ' != ch && 'x' != ch && 'o' != ch) {
				    Creator.addCubeFont(ch, new CubePos(x,y,z), face_type);
			    }
			    ++counter;
		    }
	    }
    }

    public static void setupFontFaceZ(final String a, int face_type, CubeFaceNamesEnum face_id, int z) {
        int counter = 0;
        char ch;
	    Cube cube;
    
	    for (int y = MAX_CUBE_COUNT - 1; y > -1; --y) {
		    for (int x = 0; x < MAX_CUBE_COUNT; ++x) {
			    cube = Game.cubes[x][y][z];
			    ch = a[counter];
	
			    if (' ' != ch && 'x' != ch && 'o' != ch) {
				    Creator::AddCubeFont(ch, CubePos(x,y,z), face_type);
			    }
			    ++counter;
		    }
	    }
    }

    public static void setupFontFaceSymbolX(final int[] a, int face_type, CubeFaceNamesEnum face_id, int x) {
        int counter = 0;
        int symbol_id;
	    Cube cube;
	
	    for (int y = MAX_CUBE_COUNT - 1; y > -1; --y) {
		    for (int z = MAX_CUBE_COUNT - 1; z > -1; --z) {
			    cube = Game.cubes[x][y][z];
			    symbol_id = a[counter];
			
			    if (SymbolEmpty != symbol_id) {
				    Creator.addCubeFontSymbol(symbol_id, new CubePos(x,y,z), face_type);
			    }
			    ++counter;
		    }
	    }
    }

    public static void setupFontFaceSymbolY(final int[] a, int face_type, CubeFaceNamesEnum face_id, int y) {
        int counter = 0;
        int symbol_id;
	    Cube cube;
    
	    for (int z = 0; z < MAX_CUBE_COUNT; ++z) {
		    for (int x = 0; x < MAX_CUBE_COUNT; ++x) {
			    cube = Game.cubes[x][y][z];
			    symbol_id = a[counter];
			
			    if (SymbolEmpty != symbol_id) {
				    Creator.addCubeFontSymbol(symbol_id, new CubePos(x,y,z), face_type);
			    }
			    ++counter;
		    }
	    }
    }

    public static void setupFontFaceSymbolZ(final int[] a, int face_type, CubeFaceNamesEnum face_id, int z) {
        int counter = 0;
        int symbol_id;
	    Cube cube;
    
	    for (int y = MAX_CUBE_COUNT - 1; y > -1; --y) {
            for (int x = 0; x < MAX_CUBE_COUNT; ++x) {
			    cube = Game.cubes[x][y][z];
			    symbol_id = a[counter];
			
			    if (SymbolEmpty != symbol_id) {
				    Creator.addCubeFontSymbol(symbol_id, new CubePos(x,y,z), face_type);
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


    public static void build(CubeFaceNamesEnum face_id, int face_type) {
	    buildCubeFaces(face_id, face_type);
	    buildTitleTexts(face_id, face_type);
	    buildTexts(face_id, face_type);
	    buildSymbolsOnFace(face_id, face_type);
	    buildSymbolsOnBase(face_id, face_type);
	
	    resetTransforms();
    }

    public static void buildCubeFaces(CubeFaceNamesEnum face_id, int face_type) {	    
        String a = getFace(face_id);
	    doTransforms(a);
	
	    switch (face_type) {
		    case Face_X_Plus: 	setupFaceX(a, face_type, face_id); break;
		    case Face_X_Minus: 	setupFaceX(a, face_type, face_id); break;
		    case Face_Y_Plus: 	setupFaceY(a, face_type, face_id); break;
		    case Face_Y_Minus: 	setupFaceY(a, face_type, face_id); break;
		    case Face_Z_Plus: 	setupFaceZ(a, face_type, face_id); break;
		    case Face_Z_Minus: 	setupFaceZ(a, face_type, face_id); break;
	    }
    }

    public static void setFontFromCube(CubeFont cubeFontTarget, CubePos cp, CubePos offset, int face_type) {
        char ch;
        Cube cube;
        CubeFont cubeFont;
        TexturedQuad texturedQuad;
    
        cube = Game.cubes[cp.x + offset.x][cp.y + offset.y][cp.z + offset.z];
        cubeFont = cube.ar_fonts[face_type];

        if (cubeFont != null) {
            texturedQuad = cubeFont.getFont();
        
            if (texturedQuad != null) {
                ch = texturedQuad.ch;
                cubeFontTarget.init(ch, cp);
            }
        }
    }

    public static void buildTexts(CubeFaceNamesEnum face_id, int face_type, boolean alt) {
	    String str;
	
	    if (false == alt) {
            str = getFaceText(face_id);
        } else {
            str = getFaceHelpText(face_id);
        }
		
	    doTransforms(a);
	
	    int x = (face_type == Face_X_Plus ? MAX_CUBE_COUNT - 1 : 0);
	    int y = (face_type == Face_Y_Plus ? MAX_CUBE_COUNT - 1 : 0);
	    int z = (face_type == Face_Z_Plus ? MAX_CUBE_COUNT - 1 : 0);
	
	    switch (face_type) {
		    case Face_X_Plus: 	setupFontFaceX(a, face_type, face_id, x - 1); break;
		    case Face_X_Minus: 	setupFontFaceX(a, face_type, face_id, x + 1); break;
		    case Face_Y_Plus: 	setupFontFaceY(a, face_type, face_id, y - 1); break;
		    case Face_Y_Minus: {
			    setupFontFaceY(a, face_type, face_id, y + 1);
            
                if (Face_Store == face_id) {
                    CubePos cp;
                    cp = new CubePos(1, 0, 6);
                    setFontFromCube(Game.menu.m_cubefont_noads, cp, new CubePos(0, 1, 0), face_type);
                    Game.menu.m_cubefont_noads.pos.y -= FONT_OVERLAY_OFFSET;

                    cp = new CubePos(1, 0, 4);
                    setFontFromCube(Game.menu.m_cubefont_solvers, cp, new CubePos(0, 1, 0), face_type);
                    Game.menu.m_cubefont_solvers.pos.y -= FONT_OVERLAY_OFFSET;
                
                    cp = new CubePos(1, 0, 2);
                    SetFontFromCube(Game.menu.m_cubefont_restore, cp, new CubePos(0, 1, 0), face_type);
                    Game.menu.m_cubefont_restore.pos.y -= FONT_OVERLAY_OFFSET;
                }
            }
			break;
			
			case Face_Z_Plus: {
				setupFontFaceZ(a, face_type, face_id, z - 1);
            
            	if (Face_Menu == face_id) {
                	CubePos cp;
                	cp = new CubePos(1, 5, 8);
                	setFontFromCube(Game.menu.m_cubefont_play, cp, CubePos(0, 0, -1), face_type);
                	Game.menu.m_cubefont_play.pos.z += FONT_OVERLAY_OFFSET;
                
                	cp = new CubePos(1, 3, 8);
                	setFontFromCube(Game.menu.m_cubefont_options, cp, CubePos(0, 0, -1), face_type);
                	Game.menu.m_cubefont_options.pos.z += FONT_OVERLAY_OFFSET;
                
                	cp = new CubePos(1, 1, 8);
                	setFontFromCube(Game.menu.m_cubefont_store, cp, CubePos(0, 0, -1), face_type);
                	Game.menu.m_cubefont_store.pos.z += FONT_OVERLAY_OFFSET;                
            	}
        	}
			break;
			
			case Face_Z_Minus:
				setupFontFaceZ(a, face_type, face_id, z + 1);
				break;
		}        	
	}

	public static void buildTitleTexts(CubeFaceNamesEnum face_id, CubeFaceTypesEnum face_type) {		 
    	String str = getFaceTitle(face_id);
	
		doTransforms(a);
	
		int x = (face_type == Face_X_Plus ? MAX_CUBE_COUNT - 1 : 0);
		int y = (face_type == Face_Y_Plus ? MAX_CUBE_COUNT - 1 : 0);
		int z = (face_type == Face_Z_Plus ? MAX_CUBE_COUNT - 1 : 0);
	
		switch (face_type) {
			case Face_X_Plus: 	setupFontFaceX(a, face_type, face_id, x); break;			
			case Face_X_Minus: 	setupFontFaceX(a, face_type, face_id, x); break;			
			case Face_Y_Plus: 	setupFontFaceY(a, face_type, face_id, y); break;			
			case Face_Y_Minus: 	setupFontFaceY(a, face_type, face_id, y); break;			
			case Face_Z_Plus: 	setupFontFaceZ(a, face_type, face_id, z); break;			
			case Face_Z_Minus: 	setupFontFaceZ(a, face_type, face_id, z); break;
		}	    
	}

	public static void buildSymbolsOnFace(CubeFaceNamesEnum face_id, CubeFaceTypesEnum face_type) {    	
		int[] a = getFaceSymbol(face_id);

		doTransforms(a);
	
		int x = (face_type == Face_X_Plus ? MAX_CUBE_COUNT - 1 : 0);
		int y = (face_type == Face_Y_Plus ? MAX_CUBE_COUNT - 1 : 0);
		int z = (face_type == Face_Z_Plus ? MAX_CUBE_COUNT - 1 : 0);
	
		switch (face_type) {
			case Face_X_Plus:	setupFontFaceSymbolX(a, face_type, face_id, x); break;
			case Face_X_Minus:	setupFontFaceSymbolX(a, face_type, face_id, x);	break;
			case Face_Y_Plus:	setupFontFaceSymbolY(a, face_type, face_id, y);	break;
			case Face_Y_Minus:	setupFontFaceSymbolY(a, face_type, face_id, y);	break;
			case Face_Z_Plus:	setupFontFaceSymbolZ(a, face_type, face_id, z);	break;
			case Face_Z_Minus:	setupFontFaceSymbolZ(a, face_type, face_id, z);	break;
		}    
	}

	public static void buildSymbolsOnBase(CubeFaceNamesEnum face_id, CubeFaceTypesEnum face_type) {
    	int[] a = getFaceSymbolOnBase(face_id, a);		

		doTransforms(a);
	
		int x = (face_type == Face_X_Plus ? MAX_CUBE_COUNT - 2 : 1);
		int y = (face_type == Face_Y_Plus ? MAX_CUBE_COUNT - 2 : 1);
		int z = (face_type == Face_Z_Plus ? MAX_CUBE_COUNT - 2 : 1);
	
		switch (face_type) {
			case Face_X_Plus:   setupFontFaceSymbolX(a, face_type, face_id, x);	break;
			case Face_X_Minus:	setupFontFaceSymbolX(a, face_type, face_id, x);	break;
			case Face_Y_Plus:	setupFontFaceSymbolY(a, face_type, face_id, y);	break;
			case Face_Y_Minus:	setupFontFaceSymbolY(a, face_type, face_id, y);	break;
			case Face_Z_Plus:	setupFontFaceSymbolZ(a, face_type, face_id, z);	break;
			case Face_Z_Minus:	setupFontFaceSymbolZ(a, face_type, face_id, z);	break;
		}    
	}

	public static String getFaceHelpText(CubeFaceNamesEnum face_id) {
		switch (face_id) {
			case Face_Tutorial: {
                final String tutorial[] =
                "xxxxxxxxx"
                "x>>>>>>> "
                "xNOW    x"
                "xSLIDE  x"
                "xRIGHT  x"
                "xTO     x"
                "xSTART  x"
                "xGAME   x"
                "xxxxxxxxx";                
                return tutorial;
            }
            break;
            
		case Face_Menu: {
                final String menu[] =
                "xxxxxxx x"
                "   xxxx x"
                "xx      x"
                "xSLIDE   "
                "xxx     x"
                "xRED    x"
                "xx     xx"
                "xCUBE   x"
                "xxxxxxx x";    
                return menu;
            }
            break;
			
			default:
				break;
		}
		return null;
	}

	public String getFaceText(CubeFaceNamesEnum face_id) {
		final String empty[] =
		"xxxxxxxxx"
		"x       x"
		"x       x"
		"x       x"
		"x       x"
		"x       x"
		"x       x"
		"x       x"
		"xxxxxxxxx";

		final String tutorial[] =
		"xxxxxxxxx"
		"xTAP     "
		"xON     x"
		"xRED    x"
		"xCUBE   x"
		"xAND    x"
		"xSLIDE  x"
		"xUP     x"
		"xxxxxxxxx";
	    
		final String menu[] =
		"xxxxxxx x"
		"         "
		"xx      x"
		"xPLAY    "
		"xxx     x"
		"xOPTIONSx"
		"xx     xx"
		"xSTORE  x"
		"xxxxxxx x";
	    
	   	final String easy2[] =
		"xx xxxxxx"
		"xoBoxo ox"
		"x A x   x"
		"xoCoxo ox"
		"x K x   x"
		"xo oxo ox"
		"xx   NEXx"
		"xoxo o Tx"
		"xxxxxxx x";
	
		final String easy3[] =
		"xxxxxxx x"
		"xoxo oxKx"
		"xx   BACx"
		"xo oxo ox"
		"x   x   x"
		"xo oxo ox"
		"xxEXT   x"
		"xoNoxo ox"
		"xx xxxxxx";
		
		final String easy4[] =
		"xx xxxxxx"
		"xoBoxo ox"
		"x ACK   x"
		"xo oxo ox"
		"x   x   x"
		"xo oxoxox"
		"xx    #xx"
		"xoxoxo1xx"
		"xxxxxx xx";
	    
	    final String options[] =
		"xxxxxxxxx"
		"x       x"
		"x MUSIC x"
		"x       x"
		"x       x"
		"x SOUND x"
		"x       x"
		"x       x"
		"xxxxxxx x";
	
	    final String store[] =
		"xxxxxxx x"
		"xx      x"
		"xNOADS  x"
		"xx      x"
		"xSOLVERSx"
		"xx      x"
		"xRESTOREx"
		"xx      x"
		"xxxxxxxxx";
			
		final String easy1[] =
		"xxxxxx xx"
		"xo oxo ox"
		"xxxTAP   "
		"  oONoAox"
		"xNUMBxR x"
		"xoToOo ox"
		"xxPLAY xx"
		"xo oxoxox"
		"xx xxxxxx";
			
		final String normal1[] =
		"xxxxxx xx"
		"xoxo oUox"
		" BACK P x"
		"xo  o  ox"
		"xxD    xx"
		"xoOo o ox"
		"x WxNEXT "
		"xoNoxoxox"
		"xx xxxxxx";
	
		final String normal2[] =
		"xx xxxxxx"
		"xo o o ox"
		"x  x    x"
		"xo o o ox"
		"x  x    x"
		"xo o o ox"
		"x  x    x"
		"x  o o ox"
		"x xxxxxxx";
		
		final String normal3[] =
		"x xxxxxxx"
		"x  o o ox"
		"x  x    x"
		"xo o o ox"
		"x  x    x"
		"xo o o ox"
		"x  x    x"
		"xo o o ox"
		"xx xxxxxx";
		
		final String normal4[] =
		"xx xxxxxx"
		"xx o o ox"
		"xx x    x"
		"xo o o ox"
		"x  x    x"
		"xo oxoxox"
		"xx     xx"
		"xoxo o ox"
		"xxxxxx xx";
			
		final String hard1[] =
		"xxxx xxxx"
		"xo oUoxM "
		"x  xP NEx"
		"xoxo oUox"
		"xxB    xx"
		"xoAoxo ox"
		" KCxDOWxx"
		"xoxo oNox"
		"xxxxxx xx";
	
		final String hard2[] =
	    "xxxxxx xx"
	    "xo o o ox"
	    "x    x  x"
	    "xo o o ox"
	    "x    x  x"
	    "xo o o ox"
	    "x    x  x"
	    "xo o o  x"
	    "xxxxxxx x";
		
		final String hard3[] =
	    "xxxxxxx x"
	    "xo o o  x"
	    "x    x  x"
	    "xo o o ox"
	    "x    x  x"
	    "xo o o ox"
	    "x    x  x"
	    "xo o o ox"
	    "xxxxxx xx";
		
		final String hard4[] =
	    "xxxxxx xx"
	    "xo o o ox"
	    "x    x  x"
	    "xo o o ox"
	    "x    x  x"
	    "xo oxo ox"
	    "x  x   xx"
	    "xo o ox x"
	    "xxxx xxxx";
	
		switch (face_id) {
			case Face_Empty:	return empty;		break;
				
			case Face_Tutorial: return tutorial;    break;
				
			case Face_Menu:		return menu;        break;
			case Face_Options:	return options;		break;
	        case Face_Store:	return store;		break;
				
			case Face_Easy01:	return easy1;		break;
			case Face_Easy02:	return easy2;		break;
			case Face_Easy03:	return easy3;		break;
			case Face_Easy04:	return easy4;		break;
	            
			case Face_Normal01:	return normal1;		break;
			case Face_Normal02:	return normal2;		break;
			case Face_Normal03:	return normal3;		break;
			case Face_Normal04:	return normal4;		break;
				
			case Face_Hard01:	return hard1;		break;
			case Face_Hard02:	return hard2;		break;
			case Face_Hard03:	return hard3;		break;
			case Face_Hard04:	return hard4;		break;
				
			default:
				break;
		}
		return null;
	}

	public static String getFaceTitle(CubeFaceNamesEnum face_id) {
		final String empty[] =
		"xxxxxxxxx"
		"x       x"
		"x       x"
		"x       x"
		"x       x"
		"x       x"
		"x       x"
		"x       x"
		"xxxxxxxxx";

		final String tutorial[] =
		"BEGINNING"
		"x        "
		"xxxxxxx x"
		"x       x"
		"x       x"
		"x       x"
		"x       x"
		"x       x"
		"xxxxxxxxx";
	
		final String menu[] =
		"CUBExxx x"
		"   TRAZ x"
		"xx      x"
		"         "
		"xxxx    x"
		"x       x"
		"xxx    xx"
		"x       x"
		"xxxxxxx x";
	
	    final String options[] =
		"OPTIONSxx"
		"x       x"
		"x       x"
		"x       x"
		"x       x"
		"xx      x"
		"x       x"
		"x       x"
		"xxxxxxx x";

	    final String store[] =
		"STORE   x"
		"x       x"
		"xx      x"
		"x       x"
		"xx      x"
		"x       x"
		"xx      x"
		"x    x  x"
		"xxxxxxxxx";	
	
		final String easy1[] =
		"EASY#1 xx"
		"xo oxo ox"
		"xxx      "
		"  o  o ox"
		"x    E  x"
		"xo o o ox"
		"xx     xx"
		"xo oxoxox"
		"xx xxxxxx";
	
		final String easy2[] =
		"Ex xxxxxx"
		"Ao oxo ox"
		"S   x   x"
		"Yo oxo ox"
		"#   x   x"
		"2o oxo ox"
		"xx      x"
		"xoxo o  x"
		"xxxxxxx x";
	
		final String easy3[] =
		"Exxxxxx x"
		"Aoxo o  x"
		"Sx      x"
		"Yo oxo ox"
		"#   x   x"
		"3o oxo ox"
		"xx  x   x"
		"xo oxo ox"
		"xx xxxxxx";
	
		final String easy4[] =
		"Ex xxxxxx"
		"Ao oxo ox"
		"S   x   x"
		"Yo oxo ox"
		"#   x   x"
		"4o oxoxox"
		"xx     xx"
		"xoxoxo xx"
		"xxxxxx xx";
	
		final String normal1[] =
		"NORMAL #1"
		"xoxo o ox"
		"    x   x"
		"xo  o  ox"
		"xx     xx"
		"xo o o ox"
		"x  x     "
		"xo oxoxox"
		"xx xxxxxx";
	
		final String normal2[] =
		"Nx xxxxxx"
		"Oo o o ox"
		"R  x    x"
		"Mo o o ox"
		"A  x    x"
		"Lo o o ox"
		"#  x    x"
		"2  o o ox"
		"x xxxxxxx";
	
		final String normal3[] =
		"N xxxxxxx"
		"O  o o ox"
		"R  x    x"
		"Mo o o ox"
		"A  x    x"
		"Lo o o ox"
		"#  x    x"
		"3o o o ox"
		"xx xxxxxx";
	
		final String normal4[] =
		"Nx xxxxxx"
		"Ox o o ox"
		"Rx x    x"
		"Mo o o ox"
		"A  x    x"
		"Lo oxoxox"
		"#x     xx"
		"4oxo o ox"
		"xxxxxx xx";
	
		final String hard1[] =
		"HARD #1xx"
		"xo o ox  "
		"x  x    x"
		"xoxo o ox"
		"xx     xx"
		"xo oxo ox"
		"   x   xx"
		"xoxo o ox"
		"xxxxxx xx";
	
		final String hard2[] =
	    "Hxxxxx xx"
	    "Ao o o ox"
	    "R    x  x"
	    "Do o o ox"
	    "#    x  x"
	    "2o o o ox"
	    "x    x  x"
	    "xo o o  x"
	    "xxxxxxx x";
	
		final String hard3[] =
	    "Hxxxxxx x"
	    "Ao o o  x"
	    "R    x  x"
	    "Do o o ox"
	    "#    x  x"
	    "3o o o ox"
	    "x    x  x"
	    "xo o o ox"
	    "xxxxxx xx";
		
		final String hard4[] =
	    "Hxxxxx xx"
	    "Ao o o ox"
	    "R    x  x"
	    "Do o o ox"
	    "#    x  x"
	    "4o oxo ox"
	    "x  x   xx"
	    "xo o ox x"
	    "xxxx xxxx";

		switch (face_id) {
			case Face_Empty:	return empty;		break;
			
			case Face_Tutorial: return tutorial;	break;
			
			case Face_Menu:		return menu;        break;
			case Face_Options:	return options;     break;
        	case Face_Store:	return store;		break;
			
			case Face_Easy01:	return easy1;		break;
			case Face_Easy02:	return easy2;		break;
			case Face_Easy03:	return easy3;		break;
			case Face_Easy04:	return easy4;		break;
            
			case Face_Normal01:	return normal1;      break;
			case Face_Normal02:	return normal2;      break;
			case Face_Normal03:	return normal3;      break;
			case Face_Normal04:	return normal4;      break;
			
			case Face_Hard01:	return hard1;		break;
			case Face_Hard02:	return hard2;		break;
			case Face_Hard03:	return hard3;		break;
			case Face_Hard04:	return hard4;		break;
			
			default:
				break;
		}
		return null;
	}

	public static String getFace(CubeFaceNamesEnum face_id) {
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

		final String normal2[] =
		"xx xxxxxx"
		"x1 2x3x4x"
		"xx     xx"
		"x5 6x7 8x"
		"xx     xx"
		"x9xAxB Cx"
		"x      xx"
		"x  DxExFx"
		"x xxxxxxx";

		final String normal3[] =
		"x xxxxxxx"
		"x  1x2x3x"
		"x    xxxx"
		"x4 5 6x7x"
		"xx     xx"
		"x8 9 A Bx"
		"xx x   xx"
		"xC DxExFx"
		"xx xxxxxx";
	
		final String normal4[] =
		"xx xxxxxx"
		"xx 1x2x3x"
		"xx     xx"
		"x4 5 6 7x"
		"xx   x xx"
		"x8x9xA Bx"
		"xxxxxx xx"
		"xCxDxE Fx"
		"xxxxxx xx";

		final String hard1[] =
		"xxxx xxxx"
		"x1x2 3x  "
		"xxxx    x"
		"x4x5 6 7x"
		"xx     xx"
		"x8 9xA Bx"
		"   x   xx"
		"xCxDxE Fx"
		"xxxxxx xx";

		final String hard2[] =
	    "xxxxxx xx"
	    "x1x2x3 4x"
	    "xx     xx"
	    "x5 6x7x8x"
	    "xx x    x"
	    "x9 AxBxCx"
	    "xx      x"
	    "xDxExFx x"
	    "xxxxxxx x";

		final String hard3[] =
	    "xxxxxxx x"
	    "x1x2x3x x"
	    "xx      x"
	    "x4 5 6 7x"
	    "xx     xx"
	    "x8 9 A Bx"
	    "xx     xx"
	    "xCxDxE Fx"
	    "xxxxxx xx";

		final String hard4[] =
	    "xxxxxx xx"
	    "x1x2x3 4x"
	    "xx     xx"
	    "x5 6 7x8x"
	    "xx     xx"
	    "x9xA B Cx"
	    "xxxx    x"
	    "xDxE Fxxx"
	    "xxxx xxxx";
	
		switch (face_id) {
			case Face_Empty:	return empty;		break;
		
			case Face_Tutorial: return tutorial;    break;
		
			case Face_Menu:		return menu;        break;
			case Face_Options:	return options;		break;
			case Face_Store:	return store;		break;
	
			case Face_Easy01:	return easy1;		break;
			case Face_Easy02:	return easy2;		break;
			case Face_Easy03:	return easy3;		break;
			case Face_Easy04:	return easy4;		break;
        
			case Face_Normal01:	return normal1;		break;
			case Face_Normal02:	return normal2;		break;
			case Face_Normal03:	return normal3;		break;
			case Face_Normal04:	return normal4;		break;
		
			case Face_Hard01:	return hard1;		break;
			case Face_Hard02:	return hard2;		break;
			case Face_Hard03:	return hard3;		break;
			case Face_Hard04:	return hard4;		break;

			default:
				break;
		}
		return null;
	}

	public int[] getFaceSymbol(CubeFaceNamesEnum face_id) {
		switch (face_id) {
			case Face_Menu: {
                final int ar[] = {
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolInfo,
                }
                return ar;
            }
            break;
			
			default: {            
                final int ar[] = {
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                }                
                return ar;
            }            
			break;
		}
		return null;
	}

	public int[] getFaceSymbolOnBase(CubeFaceNamesEnum face_id) {
		switch (face_id) 
			case Face_Menu: {                
                final int ar[] = {
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,     SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolGoUp,      SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,     SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolGoRight,   SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,     SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,     SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,     SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolGoDown,    SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,     SymbolEmpty,
                }
                return ar;
            }
            break;
            
			case Face_Options: {
                final int ar[] = {
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,     SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,     SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,     SymbolEmpty,
                    SymbolEmpty, SymbolMinus, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolPlus,      SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,     SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,     SymbolEmpty,
                    SymbolEmpty, SymbolMinus, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolPlus,      SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,     SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,     SymbolEmpty,
                }
				return ar;                
            }
            break;
            
			case Face_Easy01: {
                final int ar[] = {
                    SymbolEmpty, SymbolEmpty,  SymbolEmpty,  SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,   SymbolEmpty,
                    SymbolEmpty, SymbolEmpty,  SymbolEmpty,  SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolGoUp,  SymbolEmpty,   SymbolEmpty,
                    SymbolEmpty, SymbolEmpty,  SymbolEmpty,  SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,   SymbolEmpty,
                    SymbolEmpty, SymbolGoLeft, SymbolEmpty,  SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,   SymbolEmpty,
                    SymbolEmpty, SymbolEmpty,  SymbolEmpty,  SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,   SymbolEmpty,
                    SymbolEmpty, SymbolEmpty,  SymbolEmpty,  SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,   SymbolEmpty,
                    SymbolEmpty, SymbolEmpty,  SymbolEmpty,  SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,   SymbolEmpty,
                    SymbolEmpty, SymbolEmpty,  SymbolGoDown, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,   SymbolEmpty,
                    SymbolEmpty, SymbolEmpty,  SymbolEmpty,  SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,   SymbolEmpty,
                }
                return ar;
            }
            break;
            
			default: {            
                final int ar[] = {
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,
                }                
                return ar;
            }
			break;
		}
	}
    
}
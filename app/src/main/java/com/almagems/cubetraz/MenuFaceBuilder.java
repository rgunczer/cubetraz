package com.almagems.cubetraz;


#define FACE_SIZE MAX_CUBE_COUNT * MAX_CUBE_COUNT 

public final class MenuFaceBuilder
{	
	private static final FaceTransformsEnum transforms[MAX_FACE_TRANSFORM_COUNT];
	
    // ctor
    private MenuFaceBuilder() {
    }
    
//    static void DumpFace(char* ar);

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

    static void SetupFaceX(const char a[], CubeFaceTypesEnum face_type, CubeFaceNamesEnum face_id);
    static void SetupFaceY(const char a[], CubeFaceTypesEnum face_type, CubeFaceNamesEnum face_id);
    static void SetupFaceZ(const char a[], CubeFaceTypesEnum face_type, CubeFaceNamesEnum face_id);

    static void SetupFontFaceX(const char a[], CubeFaceTypesEnum face_type, CubeFaceNamesEnum face_id, int x);
    static void SetupFontFaceY(const char a[], CubeFaceTypesEnum face_type, CubeFaceNamesEnum face_id, int y);
    static void SetupFontFaceZ(const char a[], CubeFaceTypesEnum face_type, CubeFaceNamesEnum face_id, int z);
	
	static void SetupFontFaceSymbolX(const int a[], CubeFaceTypesEnum face_type, CubeFaceNamesEnum face_id, int x);
	static void SetupFontFaceSymbolY(const int a[], CubeFaceTypesEnum face_type, CubeFaceNamesEnum face_id, int y);
	static void SetupFontFaceSymbolZ(const int a[], CubeFaceTypesEnum face_type, CubeFaceNamesEnum face_id, int z);
	
	static void GetFaceHelpText(CubeFaceNamesEnum face_id, char* a);
	static void GetFaceText(CubeFaceNamesEnum face_id, char* a);
	static void GetFaceTitle(CubeFaceNamesEnum face_id, char* a);
	static void GetFace(CubeFaceNamesEnum face_id, char* a);
	static void GetFaceSymbol(CubeFaceNamesEnum face_id, int* a);
	static void GetFaceSymbolOnBase(CubeFaceNamesEnum face_id, int* a);



FaceTransformsEnum cMenuFaceBuilder::transforms[MAX_FACE_TRANSFORM_COUNT];


#pragma mark - ResetTransforms

void cMenuFaceBuilder::ResetTransforms()
{
	for (int i = 0; i < MAX_FACE_TRANSFORM_COUNT; ++i)
		transforms[i] = NoTransform;
}

#pragma mark - CopyTransforms

void cMenuFaceBuilder::CopyTransforms(FaceTransformsEnum trans[MAX_FACE_TRANSFORM_COUNT])
{
	for (int i = 0; i < MAX_FACE_TRANSFORM_COUNT; ++i)
		transforms[i] = trans[i];
}

#pragma mark - AddTransfrorm

void cMenuFaceBuilder::AddTransform(FaceTransformsEnum type, int count)
{
	for (int i = 0; i < MAX_FACE_TRANSFORM_COUNT; ++i)
	{
		if (NoTransform == transforms[i])
		{
			for (int j = i; j < i + count; ++j)
				transforms[j] = type;
			
			return;
		}
	}
}

#pragma mark - SetupFace

void cMenuFaceBuilder::SetupFaceX(const char a[], CubeFaceTypesEnum face_type, CubeFaceNamesEnum face_id)
{
	int x = (face_type == Face_X_Plus ? MAX_CUBE_COUNT - 1 : 0);
    int counter = 0;
    char ch;
	cCube* pCube;
	
	for (int y = MAX_CUBE_COUNT - 1; y > -1; --y)
	{
		for (int z = MAX_CUBE_COUNT - 1; z > -1; --z)
		{
			pCube = &engine->cubes[x][y][z];
			ch = a[counter];
			
			engine->SetCubeTypeOnFace(pCube, ch, face_type, face_id);
			++counter;
		}
	}
}

void cMenuFaceBuilder::SetupFaceY(const char a[], CubeFaceTypesEnum face_type, CubeFaceNamesEnum face_id)
{
	int y = (face_type == Face_Y_Plus ? MAX_CUBE_COUNT - 1 : 0);
    int counter = 0;
    char ch;
	cCube* pCube;
    
	for (int z = 0; z < MAX_CUBE_COUNT; ++z)
	{
		for (int x = 0; x < MAX_CUBE_COUNT; ++x)
		{
			pCube = &engine->cubes[x][y][z];
			ch = a[counter];
			
			engine->SetCubeTypeOnFace(pCube, ch, face_type, face_id);
			++counter;
		}
	}
}

void cMenuFaceBuilder::SetupFaceZ(const char a[], CubeFaceTypesEnum face_type, CubeFaceNamesEnum face_id)
{
	int z = (face_type == Face_Z_Plus ? MAX_CUBE_COUNT - 1 : 0);
    int counter = 0;
    char ch;
	cCube* pCube;
    
	for (int y = MAX_CUBE_COUNT - 1; y > -1; --y)
	{
		for (int x = 0; x < MAX_CUBE_COUNT; ++x)
		{
			pCube = &engine->cubes[x][y][z];
			ch = a[counter];
			
			engine->SetCubeTypeOnFace(pCube, ch, face_type, face_id);
			++counter;
		}
	}
}

#pragma mark - SetupFontFace

void cMenuFaceBuilder::SetupFontFaceX(const char a[], CubeFaceTypesEnum face_type, CubeFaceNamesEnum face_id, int x)
{
    int counter = 0;
    char ch;
	cCube* pCube;
	
	for (int y = MAX_CUBE_COUNT - 1; y > -1; --y)
	{
		for (int z = MAX_CUBE_COUNT - 1; z > -1; --z)
		{
			pCube = &engine->cubes[x][y][z];
			ch = a[counter];
			
			if (' ' != ch && 'x' != ch && 'o' != ch)
			{
				cCreator::AddCubeFont(ch, CubePos(x,y,z), face_type);
			}

			++counter;
		}
	}	
}

void cMenuFaceBuilder::SetupFontFaceY(const char a[], CubeFaceTypesEnum face_type, CubeFaceNamesEnum face_id, int y)
{
    int counter = 0;
    char ch;
	cCube* pCube;
    
	for (int z = 0; z < MAX_CUBE_COUNT; ++z)
	{
		for (int x = 0; x < MAX_CUBE_COUNT; ++x)
		{
			pCube = &engine->cubes[x][y][z];
			ch = a[counter];
			
			if (' ' != ch && 'x' != ch && 'o' != ch)
			{
				cCreator::AddCubeFont(ch, CubePos(x,y,z), face_type);
			}
			
			++counter;
		}
	}
}

void cMenuFaceBuilder::SetupFontFaceZ(const char a[], CubeFaceTypesEnum face_type, CubeFaceNamesEnum face_id, int z)
{
    int counter = 0;
    char ch;
	cCube* pCube;
    
	for (int y = MAX_CUBE_COUNT - 1; y > -1; --y)
	{
		for (int x = 0; x < MAX_CUBE_COUNT; ++x)
		{
			pCube = &engine->cubes[x][y][z];
			ch = a[counter];
	
			if (' ' != ch && 'x' != ch && 'o' != ch)
			{
				cCreator::AddCubeFont(ch, CubePos(x,y,z), face_type);
			}
			++counter;
		}
	}
}


#pragma mark - SetupFontFaceSymbol

void cMenuFaceBuilder::SetupFontFaceSymbolX(const int a[], CubeFaceTypesEnum face_type, CubeFaceNamesEnum face_id, int x)
{
    int counter = 0;
    int symbol_id;
	cCube* pCube;
	
	for (int y = MAX_CUBE_COUNT - 1; y > -1; --y)
	{
		for (int z = MAX_CUBE_COUNT - 1; z > -1; --z)
		{
			pCube = &engine->cubes[x][y][z];
			symbol_id = a[counter];
			
			if (SymbolEmpty != symbol_id)
			{
				cCreator::AddCubeFontSymbol((SymbolEnum)symbol_id, CubePos(x,y,z), face_type);
			}
			
			++counter;
		}
	}
}

void cMenuFaceBuilder::SetupFontFaceSymbolY(const int a[], CubeFaceTypesEnum face_type, CubeFaceNamesEnum face_id, int y)
{
    int counter = 0;
    int symbol_id;
	cCube* pCube;
    
	for (int z = 0; z < MAX_CUBE_COUNT; ++z)
	{
		for (int x = 0; x < MAX_CUBE_COUNT; ++x)
		{
			pCube = &engine->cubes[x][y][z];
			symbol_id = a[counter];
			
			if (SymbolEmpty != symbol_id)
			{
				cCreator::AddCubeFontSymbol((SymbolEnum)symbol_id, CubePos(x,y,z), face_type);
			}
			
			++counter;
		}
	}
}

void cMenuFaceBuilder::SetupFontFaceSymbolZ(const int a[], CubeFaceTypesEnum face_type, CubeFaceNamesEnum face_id, int z)
{
    int counter = 0;
    int symbol_id;
	cCube* pCube;
    
	for (int y = MAX_CUBE_COUNT - 1; y > -1; --y)
	{
		for (int x = 0; x < MAX_CUBE_COUNT; ++x)
		{
			pCube = &engine->cubes[x][y][z];
			symbol_id = a[counter];
			
			if (SymbolEmpty != symbol_id)
			{
				cCreator::AddCubeFontSymbol((SymbolEnum)symbol_id, CubePos(x,y,z), face_type);
			}
			++counter;
		}
	}
}

#pragma mark - Dump

//void cMenuFaceBuilder::DumpFace(char* ar)
//{
//	const int face_width = MAX_CUBE_COUNT - 1;
//	const int face_size = MAX_CUBE_COUNT * MAX_CUBE_COUNT;
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

#pragma mark - Custom

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

#pragma mark - Build

void cMenuFaceBuilder::Build(CubeFaceNamesEnum face_id, CubeFaceTypesEnum face_type)
{
	BuildCubeFaces(face_id, face_type);
	BuildTitleTexts(face_id, face_type);
	BuildTexts(face_id, face_type);
	BuildSymbolsOnFace(face_id, face_type);
	BuildSymbolsOnBase(face_id, face_type);
	
	ResetTransforms();
}

void cMenuFaceBuilder::BuildCubeFaces(CubeFaceNamesEnum face_id, CubeFaceTypesEnum face_type)
{
	char* a = new char[FACE_SIZE];
    GetFace(face_id, a);
	
	DoTransforms(a);
	
	switch (face_type)
	{
		case Face_X_Plus:
			SetupFaceX(a, face_type, face_id);
			break;
			
		case Face_X_Minus:
			SetupFaceX(a, face_type, face_id);
			break;
			
		case Face_Y_Plus:
			SetupFaceY(a, face_type, face_id);
			break;
			
		case Face_Y_Minus:
			SetupFaceY(a, face_type, face_id);
			break;
			
		case Face_Z_Plus:
			SetupFaceZ(a, face_type, face_id);
			break;
			
		case Face_Z_Minus:
			SetupFaceZ(a, face_type, face_id);
			break;
	}
    
    delete [] a;
}

void cMenuFaceBuilder::SetFontFromCube(cCubeFont* pCubeFontTarget, CubePos cp, CubePos offset, CubeFaceTypesEnum face_type)
{
    char ch;
    cCube* pCube;
    cCubeFont* pCubeFont;
    TexturedQuad* pTexturedQuad;
    
    pCube = &engine->cubes[cp.x + offset.x][cp.y + offset.y][cp.z + offset.z];
    pCubeFont = pCube->ar_fonts[face_type];

    if (pCubeFont)
    {
        pTexturedQuad = pCubeFont->GetFont();
        
        if (pTexturedQuad)
        {
            ch = pTexturedQuad->ch;
            
            pCubeFontTarget->Init(ch, cp);
        }
    }
}

void cMenuFaceBuilder::BuildTexts(CubeFaceNamesEnum face_id, CubeFaceTypesEnum face_type, bool alt)
{
	char* a = new char[FACE_SIZE];
	
	if (false == alt)
		GetFaceText(face_id, a);
	else
		GetFaceHelpText(face_id, a);
		
	DoTransforms(a);
	
	int x = (face_type == Face_X_Plus ? MAX_CUBE_COUNT - 1 : 0);
	int y = (face_type == Face_Y_Plus ? MAX_CUBE_COUNT - 1 : 0);
	int z = (face_type == Face_Z_Plus ? MAX_CUBE_COUNT - 1 : 0);
	
	switch (face_type)
	{
		case Face_X_Plus:
			SetupFontFaceX(a, face_type, face_id, x - 1);
			break;
			
		case Face_X_Minus:
			SetupFontFaceX(a, face_type, face_id, x + 1);
			break;
			
		case Face_Y_Plus:
			SetupFontFaceY(a, face_type, face_id, y - 1);
			break;
			
		case Face_Y_Minus:
        {
			SetupFontFaceY(a, face_type, face_id, y + 1);
            
            if (Face_Store == face_id)
            {
                CubePos cp;
                cp = CubePos(1, 0, 6);
                SetFontFromCube(engine->m_menu->m_cubefont_noads, cp, CubePos(0, 1, 0), face_type);
                engine->m_menu->m_cubefont_noads->pos.y -= FONT_OVERLAY_OFFSET;

                cp = CubePos(1, 0, 4);
                SetFontFromCube(engine->m_menu->m_cubefont_solvers, cp, CubePos(0, 1, 0), face_type);
                engine->m_menu->m_cubefont_solvers->pos.y -= FONT_OVERLAY_OFFSET;
                
                cp = CubePos(1, 0, 2);
                SetFontFromCube(engine->m_menu->m_cubefont_restore, cp, CubePos(0, 1, 0), face_type);
                engine->m_menu->m_cubefont_restore->pos.y -= FONT_OVERLAY_OFFSET;
            }
        }
			break;
			
		case Face_Z_Plus:
        {
			SetupFontFaceZ(a, face_type, face_id, z - 1);
            
            if (Face_Menu == face_id)
            {
                CubePos cp;
                cp = CubePos(1, 5, 8);
                SetFontFromCube(engine->m_menu->m_cubefont_play, cp, CubePos(0, 0, -1), face_type);
                engine->m_menu->m_cubefont_play->pos.z += FONT_OVERLAY_OFFSET;
                
                cp = CubePos(1, 3, 8);
                SetFontFromCube(engine->m_menu->m_cubefont_options, cp, CubePos(0, 0, -1), face_type);
                engine->m_menu->m_cubefont_options->pos.z += FONT_OVERLAY_OFFSET;
                
                cp = CubePos(1, 1, 8);
                SetFontFromCube(engine->m_menu->m_cubefont_store, cp, CubePos(0, 0, -1), face_type);
                engine->m_menu->m_cubefont_store->pos.z += FONT_OVERLAY_OFFSET;
                
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
            }
        }
			break;
			
		case Face_Z_Minus:
			SetupFontFaceZ(a, face_type, face_id, z + 1);
			break;
	}
    
    delete [] a;
}

void cMenuFaceBuilder::BuildTitleTexts(CubeFaceNamesEnum face_id, CubeFaceTypesEnum face_type)
{
	char* a = new char[FACE_SIZE];
    GetFaceTitle(face_id, a);
	
	DoTransforms(a);
	
	int x = (face_type == Face_X_Plus ? MAX_CUBE_COUNT - 1 : 0);
	int y = (face_type == Face_Y_Plus ? MAX_CUBE_COUNT - 1 : 0);
	int z = (face_type == Face_Z_Plus ? MAX_CUBE_COUNT - 1 : 0);
	
	switch (face_type)
	{
		case Face_X_Plus:
			SetupFontFaceX(a, face_type, face_id, x);
			break;
			
		case Face_X_Minus:
			SetupFontFaceX(a, face_type, face_id, x);
			break;
			
		case Face_Y_Plus:
			SetupFontFaceY(a, face_type, face_id, y);
			break;
			
		case Face_Y_Minus:
			SetupFontFaceY(a, face_type, face_id, y);
			break;
			
		case Face_Z_Plus:
			SetupFontFaceZ(a, face_type, face_id, z);
			break;
			
		case Face_Z_Minus:
			SetupFontFaceZ(a, face_type, face_id, z);
			break;
	}
	
    delete [] a;
}

void cMenuFaceBuilder::BuildSymbolsOnFace(CubeFaceNamesEnum face_id, CubeFaceTypesEnum face_type)
{
    int a[FACE_SIZE];
    //int* a = new int[FACE_SIZE];
	GetFaceSymbol(face_id, a);

	DoTransforms(a);
	
	int x = (face_type == Face_X_Plus ? MAX_CUBE_COUNT - 1 : 0);
	int y = (face_type == Face_Y_Plus ? MAX_CUBE_COUNT - 1 : 0);
	int z = (face_type == Face_Z_Plus ? MAX_CUBE_COUNT - 1 : 0);
	
	switch (face_type)
	{
		case Face_X_Plus:	SetupFontFaceSymbolX(a, face_type, face_id, x); break;
		case Face_X_Minus:	SetupFontFaceSymbolX(a, face_type, face_id, x);	break;
		case Face_Y_Plus:	SetupFontFaceSymbolY(a, face_type, face_id, y);	break;
		case Face_Y_Minus:	SetupFontFaceSymbolY(a, face_type, face_id, y);	break;
		case Face_Z_Plus:	SetupFontFaceSymbolZ(a, face_type, face_id, z);	break;
		case Face_Z_Minus:	SetupFontFaceSymbolZ(a, face_type, face_id, z);	break;
	}
    //delete [] a;
}

void cMenuFaceBuilder::BuildSymbolsOnBase(CubeFaceNamesEnum face_id, CubeFaceTypesEnum face_type)
{
    int a[FACE_SIZE];
   	//int* a = new int[FACE_SIZE];
	GetFaceSymbolOnBase(face_id, a);
		
	DoTransforms(a);
	
	int x = (face_type == Face_X_Plus ? MAX_CUBE_COUNT - 2 : 1);
	int y = (face_type == Face_Y_Plus ? MAX_CUBE_COUNT - 2 : 1);
	int z = (face_type == Face_Z_Plus ? MAX_CUBE_COUNT - 2 : 1);
	
	switch (face_type)
	{
		case Face_X_Plus:   SetupFontFaceSymbolX(a, face_type, face_id, x);	break;
		case Face_X_Minus:	SetupFontFaceSymbolX(a, face_type, face_id, x);	break;
		case Face_Y_Plus:	SetupFontFaceSymbolY(a, face_type, face_id, y);	break;
		case Face_Y_Minus:	SetupFontFaceSymbolY(a, face_type, face_id, y);	break;
		case Face_Z_Plus:	SetupFontFaceSymbolZ(a, face_type, face_id, z);	break;
		case Face_Z_Minus:	SetupFontFaceSymbolZ(a, face_type, face_id, z);	break;
	}
    //delete [] a;
}

#pragma mark - Faces

void cMenuFaceBuilder::GetFaceHelpText(CubeFaceNamesEnum face_id, char* a)
{
	switch (face_id)
	{
		case Face_Tutorial:
            {
                const char tutorial[] =
                "xxxxxxxxx"
                "x>>>>>>> "
                "xNOW    x"
                "xSLIDE  x"
                "xRIGHT  x"
                "xTO     x"
                "xSTART  x"
                "xGAME   x"
                "xxxxxxxxx";
                
//                a = new char[MAX_CUBE_COUNT * MAX_CUBE_COUNT];
                memcpy(a, tutorial, MAX_CUBE_COUNT * MAX_CUBE_COUNT);
            }
            break;
            
		case Face_Menu:
            {
                const char menu[] =
                "xxxxxxx x"
                "   xxxx x"
                "xx      x"
                "xSLIDE   "
                "xxx     x"
                "xRED    x"
                "xx     xx"
                "xCUBE   x"
                "xxxxxxx x";
                
//                char* a = new char[MAX_CUBE_COUNT * MAX_CUBE_COUNT];
                memcpy(a, menu, MAX_CUBE_COUNT * MAX_CUBE_COUNT);
            }
            break;
			
		default:
			break;
	}
}

void cMenuFaceBuilder::GetFaceText(CubeFaceNamesEnum face_id, char* a)
{
	const char empty[] =
	"xxxxxxxxx"
	"x       x"
	"x       x"
	"x       x"
	"x       x"
	"x       x"
	"x       x"
	"x       x"
	"xxxxxxxxx";

	const char tutorial[] =
	"xxxxxxxxx"
	"xTAP     "
	"xON     x"
	"xRED    x"
	"xCUBE   x"
	"xAND    x"
	"xSLIDE  x"
	"xUP     x"
	"xxxxxxxxx";

#ifdef LITE_VERSION
    
	const char menu[] =
	"xxxxxxx x"
	"         "
	"xx      x"
	"xPLAY    "
	"xxx     x"
	"xOPTIONSx"
	"xx     xx"
	"xCREDITSx"
	"xxxxxxx x";
    
  	const char easy2[] =
	"xx xxxxxx"
	"xo oxo ox"
	"x   x   x"
	"xo oxo ox"
	"x   x   x"
	"xo oxo ox"
	"xx      x"
	"xoxo o  x"
	"xxxxxxx x";
	
	const char easy3[] =
	"xxxxxxx x"
	"xoxo ox x"
	"xx      x"
	"xo oxo ox"
	"x   x   x"
	"xo oxo ox"
	"xx      x"
	"xo oxo ox"
	"xx xxxxxx";
	
	const char easy4[] =
	"xx xxxxxx"
	"xo oxo ox"
	"x       x"
	"xo oxo ox"
	"x   x   x"
	"xo oxoxox"
	"xx     xx"
	"xoxoxo xx"
	"xxxxxx xx";
  
    
#else
    
	const char menu[] =
	"xxxxxxx x"
	"         "
	"xx      x"
	"xPLAY    "
	"xxx     x"
	"xOPTIONSx"
	"xx     xx"
	"xSTORE  x"
	"xxxxxxx x";
    
   	const char easy2[] =
	"xx xxxxxx"
	"xoBoxo ox"
	"x A x   x"
	"xoCoxo ox"
	"x K x   x"
	"xo oxo ox"
	"xx   NEXx"
	"xoxo o Tx"
	"xxxxxxx x";
	
	const char easy3[] =
	"xxxxxxx x"
	"xoxo oxKx"
	"xx   BACx"
	"xo oxo ox"
	"x   x   x"
	"xo oxo ox"
	"xxEXT   x"
	"xoNoxo ox"
	"xx xxxxxx";
	
	const char easy4[] =
	"xx xxxxxx"
	"xoBoxo ox"
	"x ACK   x"
	"xo oxo ox"
	"x   x   x"
	"xo oxoxox"
	"xx    #xx"
	"xoxoxo1xx"
	"xxxxxx xx";
 
	
#endif
    
    const char options[] =
	"xxxxxxxxx"
	"x       x"
	"x MUSIC x"
	"x       x"
	"x       x"
	"x SOUND x"
	"x       x"
	"x       x"
	"xxxxxxx x";
	
    const char store[] =
	"xxxxxxx x"
	"xx      x"
	"xNOADS  x"
	"xx      x"
	"xSOLVERSx"
	"xx      x"
	"xRESTOREx"
	"xx      x"
	"xxxxxxxxx";
	
	
	const char easy1[] =
	"xxxxxx xx"
	"xo oxo ox"
	"xxxTAP   "
	"  oONoAox"
	"xNUMBxR x"
	"xoToOo ox"
	"xxPLAY xx"
	"xo oxoxox"
	"xx xxxxxx";
	
	
	
	const char normal1[] =
	"xxxxxx xx"
	"xoxo oUox"
	" BACK P x"
	"xo  o  ox"
	"xxD    xx"
	"xoOo o ox"
	"x WxNEXT "
	"xoNoxoxox"
	"xx xxxxxx";
	
	const char normal2[] =
	"xx xxxxxx"
	"xo o o ox"
	"x  x    x"
	"xo o o ox"
	"x  x    x"
	"xo o o ox"
	"x  x    x"
	"x  o o ox"
	"x xxxxxxx";
	
	const char normal3[] =
	"x xxxxxxx"
	"x  o o ox"
	"x  x    x"
	"xo o o ox"
	"x  x    x"
	"xo o o ox"
	"x  x    x"
	"xo o o ox"
	"xx xxxxxx";
	
	const char normal4[] =
	"xx xxxxxx"
	"xx o o ox"
	"xx x    x"
	"xo o o ox"
	"x  x    x"
	"xo oxoxox"
	"xx     xx"
	"xoxo o ox"
	"xxxxxx xx";
	
	
	const char hard1[] =
	"xxxx xxxx"
	"xo oUoxM "
	"x  xP NEx"
	"xoxo oUox"
	"xxB    xx"
	"xoAoxo ox"
	" KCxDOWxx"
	"xoxo oNox"
	"xxxxxx xx";
	
	const char hard2[] =
    "xxxxxx xx"
    "xo o o ox"
    "x    x  x"
    "xo o o ox"
    "x    x  x"
    "xo o o ox"
    "x    x  x"
    "xo o o  x"
    "xxxxxxx x";
	
	const char hard3[] =
    "xxxxxxx x"
    "xo o o  x"
    "x    x  x"
    "xo o o ox"
    "x    x  x"
    "xo o o ox"
    "x    x  x"
    "xo o o ox"
    "xxxxxx xx";
	
	const char hard4[] =
    "xxxxxx xx"
    "xo o o ox"
    "x    x  x"
    "xo o o ox"
    "x    x  x"
    "xo oxo ox"
    "x  x   xx"
    "xo o ox x"
    "xxxx xxxx";
	
	switch (face_id)
	{
		case Face_Empty:	memcpy(a, empty, FACE_SIZE);		break;
			
		case Face_Tutorial: memcpy(a, tutorial, FACE_SIZE);     break;
			
		case Face_Menu:		memcpy(a, menu, FACE_SIZE);         break;
		case Face_Options:	memcpy(a, options, FACE_SIZE);		break;
        case Face_Store:	memcpy(a, store, FACE_SIZE);		break;
			
		case Face_Easy01:	memcpy(a, easy1, FACE_SIZE);		break;
		case Face_Easy02:	memcpy(a, easy2, FACE_SIZE);		break;
		case Face_Easy03:	memcpy(a, easy3, FACE_SIZE);		break;
		case Face_Easy04:	memcpy(a, easy4, FACE_SIZE);		break;
            
		case Face_Normal01:	memcpy(a, normal1, FACE_SIZE);		break;
		case Face_Normal02:	memcpy(a, normal2, FACE_SIZE);		break;
		case Face_Normal03:	memcpy(a, normal3, FACE_SIZE);		break;
		case Face_Normal04:	memcpy(a, normal4, FACE_SIZE);		break;
			
		case Face_Hard01:	memcpy(a, hard1, FACE_SIZE);		break;
		case Face_Hard02:	memcpy(a, hard2, FACE_SIZE);		break;
		case Face_Hard03:	memcpy(a, hard3, FACE_SIZE);		break;
		case Face_Hard04:	memcpy(a, hard4, FACE_SIZE);		break;
			
		default:
			break;
	}
}

void cMenuFaceBuilder::GetFaceTitle(CubeFaceNamesEnum face_id, char* a)
{
	const char empty[] =
	"xxxxxxxxx"
	"x       x"
	"x       x"
	"x       x"
	"x       x"
	"x       x"
	"x       x"
	"x       x"
	"xxxxxxxxx";

	const char tutorial[] =
	"BEGINNING"
	"x        "
	"xxxxxxx x"
	"x       x"
	"x       x"
	"x       x"
	"x       x"
	"x       x"
	"xxxxxxxxx";
	
	const char menu[] =
	"CUBExxx x"
	"   TRAZ x"
	"xx      x"
	"         "
	"xxxx    x"
	"x       x"
	"xxx    xx"
	"x       x"
	"xxxxxxx x";
	
    const char options[] =
	"OPTIONSxx"
	"x       x"
	"x       x"
	"x       x"
	"x       x"
	"xx      x"
	"x       x"
	"x       x"
	"xxxxxxx x";

    const char store[] =
	"STORE   x"
	"x       x"
	"xx      x"
	"x       x"
	"xx      x"
	"x       x"
	"xx      x"
	"x    x  x"
	"xxxxxxxxx";
	
	
	const char easy1[] =
	"EASY#1 xx"
	"xo oxo ox"
	"xxx      "
	"  o  o ox"
	"x    E  x"
	"xo o o ox"
	"xx     xx"
	"xo oxoxox"
	"xx xxxxxx";
	
	const char easy2[] =
	"Ex xxxxxx"
	"Ao oxo ox"
	"S   x   x"
	"Yo oxo ox"
	"#   x   x"
	"2o oxo ox"
	"xx      x"
	"xoxo o  x"
	"xxxxxxx x";
	
	const char easy3[] =
	"Exxxxxx x"
	"Aoxo o  x"
	"Sx      x"
	"Yo oxo ox"
	"#   x   x"
	"3o oxo ox"
	"xx  x   x"
	"xo oxo ox"
	"xx xxxxxx";
	
	const char easy4[] =
	"Ex xxxxxx"
	"Ao oxo ox"
	"S   x   x"
	"Yo oxo ox"
	"#   x   x"
	"4o oxoxox"
	"xx     xx"
	"xoxoxo xx"
	"xxxxxx xx";

	
	const char normal1[] =
	"NORMAL #1"
	"xoxo o ox"
	"    x   x"
	"xo  o  ox"
	"xx     xx"
	"xo o o ox"
	"x  x     "
	"xo oxoxox"
	"xx xxxxxx";
	
	const char normal2[] =
	"Nx xxxxxx"
	"Oo o o ox"
	"R  x    x"
	"Mo o o ox"
	"A  x    x"
	"Lo o o ox"
	"#  x    x"
	"2  o o ox"
	"x xxxxxxx";
	
	const char normal3[] =
	"N xxxxxxx"
	"O  o o ox"
	"R  x    x"
	"Mo o o ox"
	"A  x    x"
	"Lo o o ox"
	"#  x    x"
	"3o o o ox"
	"xx xxxxxx";
	
	const char normal4[] =
	"Nx xxxxxx"
	"Ox o o ox"
	"Rx x    x"
	"Mo o o ox"
	"A  x    x"
	"Lo oxoxox"
	"#x     xx"
	"4oxo o ox"
	"xxxxxx xx";

	
	const char hard1[] =
	"HARD #1xx"
	"xo o ox  "
	"x  x    x"
	"xoxo o ox"
	"xx     xx"
	"xo oxo ox"
	"   x   xx"
	"xoxo o ox"
	"xxxxxx xx";
	
	const char hard2[] =
    "Hxxxxx xx"
    "Ao o o ox"
    "R    x  x"
    "Do o o ox"
    "#    x  x"
    "2o o o ox"
    "x    x  x"
    "xo o o  x"
    "xxxxxxx x";
	
	const char hard3[] =
    "Hxxxxxx x"
    "Ao o o  x"
    "R    x  x"
    "Do o o ox"
    "#    x  x"
    "3o o o ox"
    "x    x  x"
    "xo o o ox"
    "xxxxxx xx";
	
	const char hard4[] =
    "Hxxxxx xx"
    "Ao o o ox"
    "R    x  x"
    "Do o o ox"
    "#    x  x"
    "4o oxo ox"
    "x  x   xx"
    "xo o ox x"
    "xxxx xxxx";

	switch (face_id)
	{
		case Face_Empty:	memcpy(a, empty, FACE_SIZE);		break;
			
		case Face_Tutorial: memcpy(a, tutorial, FACE_SIZE);     break;
			
		case Face_Menu:		memcpy(a, menu, FACE_SIZE);         break;
		case Face_Options:	memcpy(a, options, FACE_SIZE);      break;
        case Face_Store:	memcpy(a, store, FACE_SIZE);		break;
			
		case Face_Easy01:	memcpy(a, easy1, FACE_SIZE);		break;
		case Face_Easy02:	memcpy(a, easy2, FACE_SIZE);		break;
		case Face_Easy03:	memcpy(a, easy3, FACE_SIZE);		break;
		case Face_Easy04:	memcpy(a, easy4, FACE_SIZE);		break;
            
		case Face_Normal01:	memcpy(a, normal1, FACE_SIZE);      break;
		case Face_Normal02:	memcpy(a, normal2, FACE_SIZE);      break;
		case Face_Normal03:	memcpy(a, normal3, FACE_SIZE);      break;
		case Face_Normal04:	memcpy(a, normal4, FACE_SIZE);      break;
			
		case Face_Hard01:	memcpy(a, hard1, FACE_SIZE);		break;
		case Face_Hard02:	memcpy(a, hard2, FACE_SIZE);		break;
		case Face_Hard03:	memcpy(a, hard3, FACE_SIZE);		break;
		case Face_Hard04:	memcpy(a, hard4, FACE_SIZE);		break;
			
		default:
			break;
	}
}

void cMenuFaceBuilder::GetFace(CubeFaceNamesEnum face_id, char* a)
{
	const char empty[] =
	"xxxxxxxxx"
	"x       x"
	"x       x"
	"x       x"
	"x       x"
	"x       x"
	"x       x"
	"x       x"
	"xxxxxxxxx";
	
	const char tutorial[] =
	"xxxxxxxxx"
	"x        "
	"x       x"
	"x       x"
	"x       x"
	"x       x"
	"x       x"
	"x       x"
	"xxxxxxxxx";
    
    const char options[] =
	"xxxxxxxxx"
	"x       x"
	"x       x"
	"x       x"
	"x       x"
	"xx      x"
	"x       x"
	"x       x"
	"xxxxxxx x";
	
#ifndef LITE_VERSION
    
	const char menu[] =
	"xxxxxxx x"
	"   xxxx x"
	"xx      x"
	"x        "
	"xxx     x"
	"x       x"
	"xxx    xx"
	"x       x"
	"xxxxxxx x";
    
    const char store[] =
	"xxxxxxx x"
	"xx      x"
	"x       x"
	"xx      x"
	"x       x"
	"xx      x"
	"x       x"
	"xx      x"
	"xxxxxxxxx";
    
    const char easy1[] =
	"xxxxxx xx"
	"x1x2x3 4x"
	"xxx      "
	"  5  6 7x"
	"x    x xx"
	"x8 9 A Bx"
	"xx     xx"
	"xC DxExFx"
	"xx xxxxxx";
    
	const char easy2[] =
	"xx xxxxxx"
	"x1 2x3x4x"
	"xx x   xx"
	"x5 6 7 8x"
	"xx x x xx"
	"x9 AxB Cx"
	"xx      x"
	"xDxExFx x"
	"xxxxxxx x";
    
	const char easy3[] =
	"xxxxxxx x"
	"x1x2x3x x"
	"xx      x"
	"x4 5x6x7x"
	"xx     xx"
	"x8x9xA Bx"
	"xx     xx"
	"xC DxExFx"
	"xx xxxxxx";
    
	const char easy4[] =
	"xx xxxxxx"
	"x1 2x3x4x"
	"xx     xx"
	"x5x6x7 8x"
	"xx     xx"
	"x9 AxBxCx"
	"xx     xx"
	"xDxExF xx"
	"xxxxxx xx";
    
	const char normal1[] =
	"xxxxxx xx"
	"x1x2x3 4x"
	"     x xx"
	"x5   6 7x"
	"xx     xx"
	"x8 9xA Bx"
	"xx       "
	"xC DxExFx"
	"xx xxxxxx";
    
    
#else
    
	const char menu[] =
	"xxxxxxx x"
	"   xxxx x"
	"xx      x"
	"x        "
	"xxx     x"
	"x       x"
	"xx     xx"
	"x       x"
	"xxxxxxxxx";
    
    const char store[] =
	"xxxxxxxxx"
	"x       x"
	"xx      x"
	"x       x"
	"xx      x"
	"x       x"
	"xx      x"
	"x    x  x"
	"xxxxxxxxx";
    
    const char easy1[] =
	"xxxxxxxxx"
	"x1x2x3x4x"
	"xxx    xx"
	"  5  6 7x"
	"x    x xx"
	"x8 9 A Bx"
	"xx     xx"
	"xCxDxExFx"
	"xxxxxxxxx";
 
    const char easy2[] =
	"xxxxxxxxx"
	"x       x"
	"x       x"
	"x       x"
	"x       x"
	"x       x"
	"x       x"
	"x       x"
	"xxxxxxxxx";

	const char easy3[] =
	"xxxxxxxxx"
	"x       x"
	"x       x"
	"x       x"
	"x       x"
	"x       x"
	"x       x"
	"x       x"
	"xxxxxxxxx";
    
	const char easy4[] =
	"xxxxxxxxx"
	"x       x"
	"x       x"
	"x       x"
	"x       x"
	"x       x"
	"x       x"
	"x       x"
	"xxxxxxxxx";
    
    
    const char normal1[] =
	"xxxxxx xx"
	"x1x2x3 4x"
	"x    x xx"
	"x5   6 7x"
	"xx     xx"
	"x8 9xA Bx"
	"xx       "
	"xC DxExFx"
	"xx xxxxxx";

    
#endif
			

	const char normal2[] =
	"xx xxxxxx"
	"x1 2x3x4x"
	"xx     xx"
	"x5 6x7 8x"
	"xx     xx"
	"x9xAxB Cx"
	"x      xx"
	"x  DxExFx"
	"x xxxxxxx";

	const char normal3[] =
	"x xxxxxxx"
	"x  1x2x3x"
	"x    xxxx"
	"x4 5 6x7x"
	"xx     xx"
	"x8 9 A Bx"
	"xx x   xx"
	"xC DxExFx"
	"xx xxxxxx";
		
	const char normal4[] =
	"xx xxxxxx"
	"xx 1x2x3x"
	"xx     xx"
	"x4 5 6 7x"
	"xx   x xx"
	"x8x9xA Bx"
	"xxxxxx xx"
	"xCxDxE Fx"
	"xxxxxx xx";
	
	
	const char hard1[] =
	"xxxx xxxx"
	"x1x2 3x  "
	"xxxx    x"
	"x4x5 6 7x"
	"xx     xx"
	"x8 9xA Bx"
	"   x   xx"
	"xCxDxE Fx"
	"xxxxxx xx";

	const char hard2[] =
    "xxxxxx xx"
    "x1x2x3 4x"
    "xx     xx"
    "x5 6x7x8x"
    "xx x    x"
    "x9 AxBxCx"
    "xx      x"
    "xDxExFx x"
    "xxxxxxx x";

	const char hard3[] =
    "xxxxxxx x"
    "x1x2x3x x"
    "xx      x"
    "x4 5 6 7x"
    "xx     xx"
    "x8 9 A Bx"
    "xx     xx"
    "xCxDxE Fx"
    "xxxxxx xx";

	const char hard4[] =
    "xxxxxx xx"
    "x1x2x3 4x"
    "xx     xx"
    "x5 6 7x8x"
    "xx     xx"
    "x9xA B Cx"
    "xxxx    x"
    "xDxE Fxxx"
    "xxxx xxxx";    
	
	switch (face_id)
	{
		case Face_Empty:	memcpy(a, empty, FACE_SIZE);		break;
			
		case Face_Tutorial: memcpy(a, tutorial, FACE_SIZE);     break;
			
		case Face_Menu:		memcpy(a, menu, FACE_SIZE);         break;
		case Face_Options:	memcpy(a, options, FACE_SIZE);		break;
        case Face_Store:	memcpy(a, store, FACE_SIZE);		break;
		
		case Face_Easy01:	memcpy(a, easy1, FACE_SIZE);		break;
		case Face_Easy02:	memcpy(a, easy2, FACE_SIZE);		break;
		case Face_Easy03:	memcpy(a, easy3, FACE_SIZE);		break;
		case Face_Easy04:	memcpy(a, easy4, FACE_SIZE);		break;
            
		case Face_Normal01:	memcpy(a, normal1, FACE_SIZE);		break;
		case Face_Normal02:	memcpy(a, normal2, FACE_SIZE);		break;
		case Face_Normal03:	memcpy(a, normal3, FACE_SIZE);		break;
		case Face_Normal04:	memcpy(a, normal4, FACE_SIZE);		break;
			
		case Face_Hard01:	memcpy(a, hard1, FACE_SIZE);		break;
		case Face_Hard02:	memcpy(a, hard2, FACE_SIZE);		break;
		case Face_Hard03:	memcpy(a, hard3, FACE_SIZE);		break;
		case Face_Hard04:	memcpy(a, hard4, FACE_SIZE);		break;

		default:
			break;
	}
}


//enum SymbolEnum
//{
//    SymbolInfo = 0,
//    SymbolLock = 1,
//    SymbolPlus = 2,
//    SymbolMinus = 3,
//    SymbolQuestionmark = 4,
//    SymbolGoLeft = 5,
//    SymbolGoRight = 6,
//    SymbolGoUp = 7,
//    SymbolGoDown = 8,
//    SymbolUndo = 9,
//    SymbolSolver = 10,
//    SymbolPause = 11,
//    Symbol3Star = 12,
//    Symbol2Star = 13,
//    Symbol1Star = 14,
//    SymbolStar = 15,
//    SymbolEmpty = 16,
//    SymbolTriangleUp = 17,
//    SymbolTriangleDown = 18,
//    SymbolHilite = 19,
//    SymbolMagnet = 20,
//    SymbolDeath = 21,
//    SymbolLightning = 22,
//    SymbolCracks = 23,
//    SymbolTriangleLeft = 24,
//    SymbolTriangleRight = 25
//};

void cMenuFaceBuilder::GetFaceSymbol(CubeFaceNamesEnum face_id, int* a)
{
	switch (face_id)
	{
		case Face_Menu:
            {
#ifdef LITE_VERSION
                const int ar[] = {
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
#else
                const int ar[] = {
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
#endif
                memcpy(a, ar, FACE_SIZE * sizeof(int));
            }
            break;
			
		default:
            {
                const int ar[] = {
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
                
                memcpy(a, ar, FACE_SIZE * sizeof(int));
            }            
			break;
	}
}

void cMenuFaceBuilder::GetFaceSymbolOnBase(CubeFaceNamesEnum face_id, int* a)
{
	switch (face_id)
	{
		case Face_Menu:
            {
                
#ifndef LITE_VERSION
                const int ar[] = {
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
#else
                const int ar[] = {
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,     SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolGoUp,      SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,     SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolGoRight,   SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,     SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,     SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,     SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,     SymbolEmpty,
                    SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,     SymbolEmpty,
                };
#endif

                memcpy(a, ar, FACE_SIZE * sizeof(int));
            }
            break;
            
		case Face_Options:
            {
                const int ar[] = {
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

                memcpy(a, ar, FACE_SIZE * sizeof(int));
            }
            break;
            
		case Face_Easy01:
            {
#ifndef LITE_VERSION
                const int ar[] = {
                    SymbolEmpty, SymbolEmpty,  SymbolEmpty,  SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,   SymbolEmpty,
                    SymbolEmpty, SymbolEmpty,  SymbolEmpty,  SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolGoUp,  SymbolEmpty,   SymbolEmpty,
                    SymbolEmpty, SymbolEmpty,  SymbolEmpty,  SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolGoRight, SymbolEmpty,
                    SymbolEmpty, SymbolGoLeft, SymbolEmpty,  SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,   SymbolEmpty,
                    SymbolEmpty, SymbolEmpty,  SymbolEmpty,  SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,   SymbolEmpty,
                    SymbolEmpty, SymbolEmpty,  SymbolEmpty,  SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,   SymbolEmpty,
                    SymbolEmpty, SymbolEmpty,  SymbolEmpty,  SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,   SymbolEmpty,
                    SymbolEmpty, SymbolEmpty,  SymbolGoDown, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,   SymbolEmpty,
                    SymbolEmpty, SymbolEmpty,  SymbolEmpty,  SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty, SymbolEmpty,   SymbolEmpty,
                };
#else
                const int ar[] = {
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
#endif
                
                memcpy(a, ar, FACE_SIZE * sizeof(int));
            }
            break;
            
		default:
            {
                const int ar[] = {
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
                
                memcpy(a, ar, FACE_SIZE * sizeof(int));
            }
			break;
	}
}

    
}
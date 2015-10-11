package com.almagems.cubetraz;

import static com.almagems.cubetraz.Constants.*;

public final class Cube {

    public enum CubeTypeEnum {
        CubeIsNotSet,

        CubeIsInvisible,
        CubeIsInvisibleAndObstacle,

        CubeIsVisibleAndObstacle,
        CubeIsVisibleAndObstacleAndLevel
    }

    public Color color_current;
    public Color color;

    public float tx;
    public float ty;
    public float tz;

    public int x;
    public int y;
    public int z;

    public CubeTypeEnum type;

    public CubeFont[] ar_fonts = new CubeFont[6];
    public CubeFont[] ar_symbols = new CubeFont[6];

    public Vector v;

    public void SetColor(Color col) {
        color_current.init(col);
        color.init(col);
    }

    public Cube() {
        //printf("\ncCube Constructor");
    }

    public void Update(float dt) {
        tx += v.x;
        ty += v.y;
        tz += v.z;
    }

    public void Reset()	{
		type = CubeTypeEnum.CubeIsInvisible;
		
		tx = x * CUBE_SIZE;
		ty = y * CUBE_SIZE;
		tz = z * CUBE_SIZE;
	
        ResetColor();
		ResetFonts();
        ResetSymbols();
	}

    public void ResetColor() {
        color_current = color;
    }

    public void ResetFonts() {
        //printf("\nReset Fonts...");
        
		ar_fonts[0] = null;
		ar_fonts[1] = null;
		
		ar_fonts[2] = null;
		ar_fonts[3] = null;
		
		ar_fonts[4] = null;
		ar_fonts[5] = null;
	}

    public void ResetSymbols() {
        //printf("\nReset Symbols...");
        
		ar_symbols[0] = null;
		ar_symbols[1] = null;
		
		ar_symbols[2] = null;
		ar_symbols[3] = null;
		
		ar_symbols[4] = null;
		ar_symbols[5] = null;
    }

    public void WarmByFactor(int factor) {
        //return;
        
        if (color_current.r < color.r)
        {
            color_current.r += factor;
            
            if (color_current.r > color.r)
                color_current.r = color.r;
        }
        
        if (color_current.r > color.r)
        {
            color_current.r -= factor;
            
            if (color_current.r < color.r)
                color_current.r = color.r;
        }

        
        if (color_current.g < color.g)
        {
            color_current.g += factor;
            
            if (color_current.g > color.g)
                color_current.g = color.g;
        }
        
        if (color_current.g > color.g)
        {
            color_current.g -= factor;
            
            if (color_current.g < color.g)
                color_current.g = color.g;
        }
        
        
        if (color_current.b < color.b)
        {
            color_current.b += factor;
            
            if (color_current.b > color.b)
                color_current.b = color.b;
        }
        
        if (color_current.b > color.b)
        {
            color_current.b -= factor;
            
            if (color_current.b < color.b)
                color_current.b = color.b;
        }
        
    }
}


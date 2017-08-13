package com.almagems.cubetraz.cubes;

import com.almagems.cubetraz.game.Game;
import com.almagems.cubetraz.math.Utils;
import com.almagems.cubetraz.math.Vector;
import com.almagems.cubetraz.graphics.Color;

import static com.almagems.cubetraz.game.Constants.*;

public final class Cube {

    public Color colorCurrent = new Color();
    public Color color = new Color();

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

    public void setColor(Color color) {
        this.color.init(color);
        this.colorCurrent.init(color);
    }

    public Cube() {
        color.init(Color.WHITE);
        colorCurrent.init(color);
    }

    public void update() {
        tx += v.x;
        ty += v.y;
        tz += v.z;
    }

    public void warmFonts() {
        for(int i = 0; i < ar_fonts.length; ++i) {
            if (ar_fonts[i] != null) {
                ar_fonts[i].warmByFactor( Utils.randInt(0, 12) );
            }
        }
    }

    public void warmSymbols() {
        for(int i = 0; i < ar_symbols.length; ++i) {
            if (ar_symbols[i] != null) {
                ar_symbols[i].warmByFactor( Utils.randInt(0, 12) );
            }
        }
    }

    public void reset()	{
		type = CubeTypeEnum.CubeIsInvisible;
		
		tx = x * CUBE_SIZE;
		ty = y * CUBE_SIZE;
		tz = z * CUBE_SIZE;
	
        resetColor();
		resetFonts();
        resetSymbols();
	}

    public void resetColor() {
        colorCurrent.init(color);
    }

    public void resetFonts() {
		ar_fonts[0] = null;
		ar_fonts[1] = null;
		
		ar_fonts[2] = null;
		ar_fonts[3] = null;
		
		ar_fonts[4] = null;
		ar_fonts[5] = null;
	}

    public void resetSymbols() {
		ar_symbols[0] = null;
		ar_symbols[1] = null;
		
		ar_symbols[2] = null;
		ar_symbols[3] = null;
		
		ar_symbols[4] = null;
		ar_symbols[5] = null;
    }

    public void warmByFactor(int factor) {
        if (colorCurrent.r < color.r) {
            colorCurrent.r += factor;
            
            if (colorCurrent.r > color.r) {
                colorCurrent.r = color.r;
            }
        }
        
        if (colorCurrent.r > color.r) {
            colorCurrent.r -= factor;
            
            if (colorCurrent.r < color.r) {
                colorCurrent.r = color.r;
            }
        }
        
        if (colorCurrent.g < color.g) {
            colorCurrent.g += factor;
            
            if (colorCurrent.g > color.g) {
                colorCurrent.g = color.g;
            }
        }
        
        if (colorCurrent.g > color.g) {
            colorCurrent.g -= factor;
            
            if (colorCurrent.g < color.g) {
                colorCurrent.g = color.g;
            }
        }
                
        if (colorCurrent.b < color.b) {
            colorCurrent.b += factor;
            
            if (colorCurrent.b > color.b) {
                colorCurrent.b = color.b;
            }
        }
        
        if (colorCurrent.b > color.b) {
            colorCurrent.b -= factor;
            
            if (colorCurrent.b < color.b) {
                colorCurrent.b = color.b;
            }
        }        
    }
    
}


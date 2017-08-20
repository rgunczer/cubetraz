package com.almagems.cubetraz.cubes;

import com.almagems.cubetraz.math.Utils;
import com.almagems.cubetraz.math.Vector;
import com.almagems.cubetraz.graphics.Color;

import static com.almagems.cubetraz.game.Game.*;

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

    public CubeFont[] fonts = new CubeFont[6];
    public CubeFont[] symbols = new CubeFont[6];

    public Vector velocity = new Vector();

    public void setColor(Color color) {
        this.color.init(color);
        this.colorCurrent.init(color);
    }

    public Cube() {
        color.init(Color.WHITE);
        colorCurrent.init(color);
    }

    public void update() {
        tx += velocity.x;
        ty += velocity.y;
        tz += velocity.z;
    }

    public void warmFonts() {
        for(int i = 0; i < fonts.length; ++i) {
            if (fonts[i] != null) {
                fonts[i].warmByFactor( Utils.randInt(0, 12) );
            }
        }
    }

    public void warmSymbols() {
        for(int i = 0; i < symbols.length; ++i) {
            if (symbols[i] != null) {
                symbols[i].warmByFactor( Utils.randInt(0, 12) );
            }
        }
    }

    public void reset()	{
		type = CubeTypeEnum.CubeIsInvisible;
		
		tx = x * CUBE_SIZE;
		ty = y * CUBE_SIZE;
		tz = z * CUBE_SIZE;
	
        resetColor();
		removeFonts();
        removeSymbols();
	}

    public void resetColor() {
        colorCurrent.init(color);
    }

    public void removeFonts() {
		fonts[0] = null;
		fonts[1] = null;
		
		fonts[2] = null;
		fonts[3] = null;
		
		fonts[4] = null;
		fonts[5] = null;
	}

    public void removeSymbols() {
		symbols[0] = null;
		symbols[1] = null;
		
		symbols[2] = null;
		symbols[3] = null;
		
		symbols[4] = null;
		symbols[5] = null;
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


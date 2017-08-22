package com.almagems.cubetraz.cubes;

import com.almagems.cubetraz.Game;
import com.almagems.cubetraz.math.Vector;
import com.almagems.cubetraz.graphics.Color;
import com.almagems.cubetraz.graphics.TexturedQuad;

public final class CubeFont {
    
    public TexturedQuad texturedQuad = new TexturedQuad();

    public Vector pos = new Vector();

    public Color colorCurrent = new Color();
    public Color color = new Color();


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

        if (colorCurrent.a < color.a) {
            colorCurrent.a += factor;
            
            if (colorCurrent.a > color.a) {
                colorCurrent.a = color.a;
            }
        }
        
        if (colorCurrent.a > color.a) {
            colorCurrent.a -= factor;
            
            if (colorCurrent.a < color.a) {
                colorCurrent.a = color.a;
            }
        }
    }
    
    public void setColor(Color color) {
        this.color.init(color);
        this.colorCurrent.init(color);
    }

    public void init(CubeLocation location) {
        Vector v = Game.getCubePosition(location);
        pos.init(v);
    }

    public void init(char ch, CubeLocation location) {
        texturedQuad = Game.getFont(ch);
        init(location);
    }

    public void init(int type, CubeLocation location) {
        texturedQuad = Game.getSymbol(type);
        init(location);
    }

}


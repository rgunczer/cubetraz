package com.almagems.cubetraz.cubes;


import com.almagems.cubetraz.game.Game;
import com.almagems.cubetraz.math.Vector;
import com.almagems.cubetraz.graphics.Color;
import com.almagems.cubetraz.graphics.TexturedQuad;
import com.almagems.cubetraz.scenes.menu.MenuCube;

import static com.almagems.cubetraz.game.Constants.*;


public final class CubeFont {
    
    private TexturedQuad m_pFont = new TexturedQuad();
    private CubePos m_cube_pos = new CubePos();
    private MenuCube m_pTriggerCube = new MenuCube();
    private AxisEnum m_trigger_axis;
    private AxisEnum m_align_axis;
    private int m_align_axis_value;
    private int m_align_value;
        
    private Vector pos_origin = new Vector();
            
    public Vector pos = new Vector();
    public Color colorCurrent = new Color();
    public Color color = new Color();


    public CubeFont() {
    
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
    
    public void setColor(Color col) { 
        color.init(col);
        colorCurrent.init(col);
    }
    
    public void setAxis(AxisEnum trigger_axis, AxisEnum align_axis) {
//        m_trigger_axis = trigger_axis;
//        m_align_axis = align_axis;
//
//        switch (m_align_axis) {
//            case X_Axis:
//                m_align_axis_value = &m_pTriggerCube->m_cube_pos.x;
//                m_align_value = &m_cube_pos.x;
//                break;
//
//            case Y_Axis:
//                m_align_axis_value = &m_pTriggerCube->m_cube_pos.y;
//                m_align_value = &m_cube_pos.y;
//                break;
//
//            case Z_Axis:
//                m_align_axis_value = &m_pTriggerCube->m_cube_pos.z;
//                m_align_value = &m_cube_pos.z;
//                break;
//        }
    }
    
    public void setTriggerCube(MenuCube cube) { 
        m_pTriggerCube = cube; 
    }
        
    public TexturedQuad getFont() {
        return m_pFont; 
    }

    public void init(CubePos cube_pos) {    
        m_trigger_axis = AxisEnum.X_Axis;
        m_pTriggerCube = null;

        m_cube_pos.init(cube_pos);

        Vector v = Game.getCubePosAt(m_cube_pos);
        pos_origin.init(v);
        pos.init(v);
    }

    public void init(char ch, CubePos cube_pos) {
        m_pFont = Game.getFont(ch);
        if (m_pFont == null) {
            System.out.println("m_pFont is null");
        }

        init(cube_pos);
    }

    public void init(int type, CubePos cube_pos) {
        m_pFont = Game.getSymbol(type);    
        init(cube_pos);
    }

}


package com.almagems.cubetraz;


import static com.almagems.cubetraz.Constants.*;


public final class CubeFont {
    
    private TexturedQuad m_pFont;
    private CubePos m_cube_pos;
    private MenuCube m_pTriggerCube;
    private AxisEnum m_trigger_axis;
    private AxisEnum m_align_axis;
    private int m_align_axis_value;
    private int m_align_value;
        
    private Vector pos_origin = new Vector();
            
    public Vector pos;
    public Color color_current;
    public Color color;


    // ctor
    public CubeFont() {
    
    }
    
    public void warmByFactor(int factor) {
        if (color_current.r < color.r) {
            color_current.r += factor;
            
            if (color_current.r > color.r) {
                color_current.r = color.r;
            }
        }
        
        if (color_current.r > color.r) {
            color_current.r -= factor;
            
            if (color_current.r < color.r) {
                color_current.r = color.r;
            }
        }        
        
        if (color_current.g < color.g) {
            color_current.g += factor;
            
            if (color_current.g > color.g) {
                color_current.g = color.g;
            }
        }
        
        if (color_current.g > color.g) {
            color_current.g -= factor;
            
            if (color_current.g < color.g) {
                color_current.g = color.g;
            }
        }
                
        if (color_current.b < color.b) {
            color_current.b += factor;
            
            if (color_current.b > color.b) {
                color_current.b = color.b;
            }
        }
        
        if (color_current.b > color.b) {
            color_current.b -= factor;
            
            if (color_current.b < color.b) {
                color_current.b = color.b;
            }
        }

        if (color_current.a < color.a) {
            color_current.a += factor;
            
            if (color_current.a > color.a) {
                color_current.a = color.a;
            }
        }
        
        if (color_current.a > color.a) {
            color_current.a -= factor;
            
            if (color_current.a < color.a) {
                color_current.a = color.a;
            }
        }
    }
    
    public void setColor(Color col) { 
        color.init(col);
        color_current.init(col);
    }
    
    public void setAxis(AxisEnum trigger_axis, AxisEnum align_axis) { 
    /*
        m_trigger_axis = trigger_axis; 
        m_align_axis = align_axis; 
        
        switch (m_align_axis) {
            case X_Axis:
                m_align_axis_value = &m_pTriggerCube->m_cube_pos.x;
                m_align_value = &m_cube_pos.x;
                break;
                
            case Y_Axis:
                m_align_axis_value = &m_pTriggerCube->m_cube_pos.y;
                m_align_value = &m_cube_pos.y;
                break;
                
            case Z_Axis:
                m_align_axis_value = &m_pTriggerCube->m_cube_pos.z;
                m_align_value = &m_cube_pos.z;
                break;
        }
    */
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

        m_cube_pos = cube_pos;
        pos_origin = pos = Game.getCubePosAt(m_cube_pos.x, m_cube_pos.y, m_cube_pos.z);
    }

    public void init(char ch, CubePos cube_pos) {
        m_pFont = Game.getFont(ch);
        init(cube_pos);
    }

    public void init(int type, CubePos cube_pos) {
        m_pFont = Game.getSymbol(type);    
        init(cube_pos);
    }

}


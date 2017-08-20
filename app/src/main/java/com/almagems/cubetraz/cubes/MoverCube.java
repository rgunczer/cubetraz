package com.almagems.cubetraz.cubes;

import com.almagems.cubetraz.Game;
import com.almagems.cubetraz.math.Vector;
import com.almagems.cubetraz.math.Vector2;
import com.almagems.cubetraz.graphics.Color;
import com.almagems.cubetraz.graphics.Graphics;
import com.almagems.cubetraz.graphics.TexCoordsQuad;
import com.almagems.cubetraz.graphics.TexturedQuad;
import static com.almagems.cubetraz.Game.*;

public final class MoverCube {
    private CubeLocation m_cube_pos_starting = new CubeLocation();
    private CubeLocation m_cube_pos = new CubeLocation();
    private int m_move_dir;
    private TexturedQuad[] m_ar_cube_symbols = new TexturedQuad[6];

    private Color m_color = new Color();
    private Color m_color_current = new Color();
    
    private Color m_color_symbol = new Color();
    private Color m_color_symbol_current = new Color();
        
    public Vector pos;


    // ctor
    public MoverCube() {
        for (int i = 0; i < 6; ++i) {
            m_ar_cube_symbols[i] = null;
        }
    }

    public void reposition() {
        setCubePos(m_cube_pos_starting);
        m_color_symbol_current.init(m_color_symbol);
    }

    public void setCubePos(CubeLocation coordinate) {
        m_cube_pos.init(coordinate); // here!?
        pos = Game.getCubePosAt(m_cube_pos);
    }

    public void setSymbols() {
        m_ar_cube_symbols[Face_X_Plus] = null;
        m_ar_cube_symbols[Face_Y_Plus] = null;
        m_ar_cube_symbols[Face_Z_Plus] = null;
    
        switch (m_move_dir) {
            case AxisMovement_X_Plus:
                m_ar_cube_symbols[Face_Y_Plus] = Game.getSymbol(Symbol_GoLeft);
                m_ar_cube_symbols[Face_Z_Plus] = Game.getSymbol(Symbol_GoLeft);
                break;
            
            case AxisMovement_X_Minus:
                m_ar_cube_symbols[Face_Y_Plus] = Game.getSymbol(Symbol_GoRight);
                m_ar_cube_symbols[Face_Z_Plus] = Game.getSymbol(Symbol_GoRight);
                break;
            
            case AxisMovement_Y_Plus:
                m_ar_cube_symbols[Face_X_Plus] = Game.getSymbol(Symbol_GoUp);
                m_ar_cube_symbols[Face_Z_Plus] = Game.getSymbol(Symbol_GoUp);
                break;
            
            case AxisMovement_Y_Minus:
                m_ar_cube_symbols[Face_X_Plus] = Game.getSymbol(Symbol_GoDown);
                m_ar_cube_symbols[Face_Z_Plus] = Game.getSymbol(Symbol_GoDown);
                break;
            
            case AxisMovement_Z_Plus:
                m_ar_cube_symbols[Face_X_Plus] = Game.getSymbol(Symbol_GoRight);
                m_ar_cube_symbols[Face_Y_Plus] = Game.getSymbol(Symbol_GoDown);
                break;
            
            case AxisMovement_Z_Minus:
                m_ar_cube_symbols[Face_X_Plus] = Game.getSymbol(Symbol_GoLeft);
                m_ar_cube_symbols[Face_Y_Plus] = Game.getSymbol(Symbol_GoUp);
                break;
            
            default:
                break;
        }
    }

    public void init(CubeLocation cube_pos, int move_dir) {
        m_move_dir = move_dir;
        setCubePos(cube_pos);
        m_cube_pos_starting = m_cube_pos;
        setSymbols();
    
        m_color.init(Game.faceColor);
        m_color_current.init(Game.faceColor);
        m_color_symbol_current.init(new Color(50, 50, 50, 255));
        m_color_symbol.init(new Color(50, 50, 50, 255));
    }

    public void update() {
    
    }

    public void renderCube() {
        Game.graphics.addCubeSize(pos.x, pos.y, pos.z, HALF_CUBE_SIZE, m_color_current);
    }

    public void renderSymbols() {
        Graphics graphics = Game.graphics;
        TexCoordsQuad coords = new TexCoordsQuad();
        TexturedQuad pTQ;
        
        pTQ = m_ar_cube_symbols[Face_X_Plus];    
        if (pTQ != null) {
            coords.tx0 = new Vector2(pTQ.tx_lo_left.x,  pTQ.tx_up_left.y);
            coords.tx1 = new Vector2(pTQ.tx_lo_right.x, pTQ.tx_up_right.y);
            coords.tx2 = new Vector2(pTQ.tx_up_right.x, pTQ.tx_lo_right.y);
            coords.tx3 = new Vector2(pTQ.tx_up_left.x,  pTQ.tx_lo_left.y);
            graphics.addCubeFace_X_Plus(pos.x, pos.y, pos.z, coords, m_color_symbol_current);
        }
    
        pTQ = m_ar_cube_symbols[Face_Z_Plus];    
        if (pTQ != null) {
            coords.tx0 = new Vector2(pTQ.tx_lo_left.x,  pTQ.tx_up_left.y);
            coords.tx1 = new Vector2(pTQ.tx_lo_right.x, pTQ.tx_up_right.y);
            coords.tx2 = new Vector2(pTQ.tx_up_right.x, pTQ.tx_lo_right.y);
            coords.tx3 = new Vector2(pTQ.tx_up_left.x,  pTQ.tx_lo_left.y);
            graphics.addCubeFace_Z_Plus(pos.x, pos.y, pos.z, coords, m_color_symbol_current);
        }
    
        pTQ = m_ar_cube_symbols[Face_Y_Plus];    
        if (pTQ != null) {
            coords.tx0 = new Vector2(pTQ.tx_up_left.x,  pTQ.tx_lo_left.y);
            coords.tx1 = new Vector2(pTQ.tx_lo_left.x,  pTQ.tx_up_left.y);
            coords.tx2 = new Vector2(pTQ.tx_lo_right.x, pTQ.tx_up_right.y);
            coords.tx3 = new Vector2(pTQ.tx_up_right.x, pTQ.tx_lo_right.y);
            graphics.addCubeFace_Y_Plus(pos.x, pos.y, pos.z, coords, m_color_symbol_current);
        }
    }
    
    public CubeLocation getCubePos() {
        return m_cube_pos; 
    }
    
    public int getMoveDir() {
        return m_move_dir; 
    }
    
    public void hiLite() { 
        m_color_symbol_current.init(new Color(51, 255, 51, 204));
    }

    public void noHiLite() {
        m_color_symbol_current.init(m_color_symbol);
    }
    
}
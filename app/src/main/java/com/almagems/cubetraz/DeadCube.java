package com.almagems.cubetraz;

import static com.almagems.cubetraz.Constants.*;


public final class DeadCube {

    public static Graphics graphics;
    
    private CubePos m_cube_pos_starting;
    private CubePos m_cube_pos;

    private TexturedQuad[] m_ar_cube_textures = new TexturedQuad[6];
    
    private final Color m_color = new Color();
    private final Color m_color_current = new Color();
    
    private final Color m_color_symbol = new Color();
    private final Color m_color_symbol_current = new Color();
      	    
    public Vector pos = new Vector();
    
    public CubePos getCubePos() { 
        return m_cube_pos; 
    }
    
    public void hiLite() { 
        m_color_symbol_current.init(new Color(255, 25, 25, 204));
    }
    
    public void noHilite() { 
        m_color_symbol_current.init(m_color_symbol);
    }
 
    // ctor
    public DeadCube() {
        for (int i = 0; i < 6; ++i) {
            m_ar_cube_textures[i] = null;
        }
    }

    public void reposition() {
        setCubePos(m_cube_pos_starting);
        m_color_symbol_current.init(m_color_symbol);
    }

    public void setCubePos(CubePos coordinate) {
        m_cube_pos = coordinate;
        pos = Game.getCubePosAt(m_cube_pos.x, m_cube_pos.y, m_cube_pos.z);
    }

    public void init(CubePos cube_pos) {
        setCubePos(cube_pos);
        m_cube_pos_starting = m_cube_pos;
    
        m_ar_cube_textures[Face_Z_Plus] = Game.getSymbol(SymbolDeath);
        m_ar_cube_textures[Face_X_Plus] = Game.getSymbol(SymbolDeath);
    
        m_color_current.init( Game.getFaceColor(1f) );
        m_color.init( Game.getFaceColor(1f) );
        m_color_symbol_current.init((new Color(50, 50, 50, 255)));
        m_color_symbol.init(m_color_symbol_current);
    }

    public void update() {
    }

    public void renderCube() {
        graphics.addCubeSize(pos.x, pos.y, pos.z, HALF_CUBE_SIZE, m_color_current);
    }

    public void renderSymbols() {
        TexCoordsQuad coords = new TexCoordsQuad();
        TexturedQuad pTQ = m_ar_cube_textures[Face_X_Plus];
    
        if (pTQ != null) {
            coords.tx0 = new Vector2(pTQ.tx_lo_left.x,  pTQ.tx_up_left.y);
            coords.tx1 = new Vector2(pTQ.tx_lo_right.x, pTQ.tx_up_right.y);
            coords.tx2 = new Vector2(pTQ.tx_up_right.x, pTQ.tx_lo_right.y);
            coords.tx3 = new Vector2(pTQ.tx_up_left.x,  pTQ.tx_lo_left.y);
            graphics.addCubeFace_X_Plus(pos.x, pos.y, pos.z, coords, m_color_symbol_current);
        }
    
        pTQ = m_ar_cube_textures[Face_Z_Plus];
        if (pTQ != null) {
            coords.tx0 = new Vector2(pTQ.tx_lo_left.x,  pTQ.tx_up_left.y);
            coords.tx1 = new Vector2(pTQ.tx_lo_right.x, pTQ.tx_up_right.y);
            coords.tx2 = new Vector2(pTQ.tx_up_right.x, pTQ.tx_lo_right.y);
            coords.tx3 = new Vector2(pTQ.tx_up_left.x,  pTQ.tx_lo_left.y);
            graphics.addCubeFace_Z_Plus(pos.x, pos.y, pos.z, coords, m_color_symbol_current);
        }
    }
}
package com.almagems.cubetraz;

import static com.almagems.cubetraz.Constants.*;


public final class MovingCube {
        
	private boolean m_done;
    private float m_t;
    private float m_step_t;
    private float m_start_value;
    private float m_end_value;
    
    private Color m_color = new Color();
    private Color m_color_current = new Color();

    private Color m_color_symbol = new Color();
    private Color m_color_symbol_current = new Color();
    
    private float position; // pointer! (java)
        
    private CubePos m_cube_pos_starting = new CubePos();
    private CubePos m_cube_pos = new CubePos();
	private CubePos m_cube_pos_destination = new CubePos();
	
    private int m_move_dir;
    private int m_move_dir_starting;
    
    public Vector pos;
    public TexturedQuad[] ar_cube_textures = new TexturedQuad[6];
	
    
    // ctor  
    public MovingCube() {
	    m_cube_pos.reset();
    }

    public void reposition() {
        setCubePos(m_cube_pos_starting);
        m_move_dir = m_move_dir_starting;
        updateSymbols();
    }

    public void setCubePos(CubePos coordinate) {
	    m_done = true;
        m_cube_pos = coordinate;
        pos = Game.getCubePosAt(m_cube_pos.x, m_cube_pos.y, m_cube_pos.z);
    }

	public void init(MovingCube other) {

	}

    public void init(CubePos cube_pos, int move_dir) {
        setCubePos(cube_pos);
        m_cube_pos_starting = m_cube_pos;
        m_move_dir_starting = move_dir;
	    m_move_dir = move_dir;
	
	    updateSymbols();

        m_color_current = m_color = Game.getFaceColor(1f);
        m_color_symbol_current = new Color(40, 40, 40, 255);
		m_color_symbol = new Color(40, 40, 40, 255);
    }

    public void updateSymbols() {
	    ar_cube_textures[Face_X_Plus] = null;
	    ar_cube_textures[Face_Y_Plus] = null;
	    ar_cube_textures[Face_Z_Plus] = null;
	
	    switch (m_move_dir) {
		    case AxisMovement_X_Plus:
			    ar_cube_textures[Face_Z_Plus] = Game.getSymbol(SymbolTriangleLeft);
			    ar_cube_textures[Face_Y_Plus] = Game.getSymbol(SymbolTriangleLeft);
			    break;
			
		    case AxisMovement_X_Minus:
                ar_cube_textures[Face_Z_Plus] = Game.getSymbol(SymbolTriangleRight);
			    ar_cube_textures[Face_Y_Plus] = Game.getSymbol(SymbolTriangleRight);
			    break;
			
		    case AxisMovement_Y_Plus:
			    ar_cube_textures[Face_X_Plus] = Game.getSymbol(SymbolTriangleUp);
			    ar_cube_textures[Face_Z_Plus] = Game.getSymbol(SymbolTriangleUp);
			    break;
			
		    case AxisMovement_Y_Minus:
			    ar_cube_textures[Face_X_Plus] = Game.getSymbol(SymbolTriangleDown);
			    ar_cube_textures[Face_Z_Plus] = Game.getSymbol(SymbolTriangleDown);
			    break;
			
		    case AxisMovement_Z_Plus:
			    ar_cube_textures[Face_Y_Plus] = Game.getSymbol(SymbolTriangleDown);
			    ar_cube_textures[Face_X_Plus] = Game.getSymbol(SymbolTriangleRight);
			    break;

		    case AxisMovement_Z_Minus:
			    ar_cube_textures[Face_Y_Plus] = Game.getSymbol(SymbolTriangleUp);
			    ar_cube_textures[Face_X_Plus] = Game.getSymbol(SymbolTriangleLeft);
			    break;
			
		    default:
			    break;
	    }
    }

    public void update() {
        if (!m_done) {
		    m_t += m_step_t;
        
		    if (m_t >= 1.0f) {
			    m_t = 1.0f;
			    m_done = true;
                m_color_symbol_current = m_color_symbol;
			
			    //engine.PlaySound(SOUND_CUBE_HIT);
			
			    setCubePos(m_cube_pos_destination);
			
			    switch (m_move_dir) {
				    case AxisMovement_X_Plus: m_move_dir = AxisMovement_X_Minus; break;
				    case AxisMovement_X_Minus: m_move_dir = AxisMovement_X_Plus; break;
				    case AxisMovement_Y_Plus: m_move_dir = AxisMovement_Y_Minus; break;
				    case AxisMovement_Y_Minus: m_move_dir = AxisMovement_Y_Plus; break;
				    case AxisMovement_Z_Plus: m_move_dir = AxisMovement_Z_Minus; break;
				    case AxisMovement_Z_Minus: m_move_dir = AxisMovement_Z_Plus; break;
				    default: m_move_dir = AxisMovement_X_Plus; break;
			    }
			
			    updateSymbols();
		    } else {
                position = Utils.lerp(m_start_value, m_end_value, m_t);
            }
        }
    }

    void calcMovement(CubePos cube_pos) {	
	    CubePos prev = cube_pos;

        boolean is_obstacle;
        boolean is_player;
        boolean is_spec;
        boolean is_key;
		
	    switch(m_move_dir) {
		    case AxisMovement_X_Plus:
			    while (cube_pos.x <= MAX_CUBE_COUNT - 1) {
				    is_obstacle = Game.isObstacle(cube_pos);
                    is_player = Game.isPlayerCube(cube_pos);
                    is_spec = Game.level.isSpecCubeObstacle(cube_pos, this, null, null);
                    is_key = Game.isKeyCube(cube_pos);

                    if (is_obstacle || is_player || is_spec || is_key) {
					    cube_pos.init(prev);
					    return;
                    }
				    prev.x = cube_pos.x;
				    ++cube_pos.x;
			    }
			    --cube_pos.x;
			    break;
				
		    case AxisMovement_X_Minus:
			    while (cube_pos.x >= 0) {
				    is_obstacle = Game.isObstacle(cube_pos);
                    is_player = Game.isPlayerCube(cube_pos);
                    is_spec = Game.level.isSpecCubeObstacle(cube_pos, this, null, null);
                    is_key = Game.isKeyCube(cube_pos);
                
                    if (is_obstacle || is_player || is_spec || is_key) {
					    cube_pos.init(prev);
					    return;
				    }
				    prev.x = cube_pos.x;
				    --cube_pos.x;
			    }
			    ++cube_pos.x;
			    break;
				
		    case AxisMovement_Y_Plus:
			    while (cube_pos.y <= MAX_CUBE_COUNT - 1) {
				    is_obstacle = Game.isObstacle(cube_pos);
                    is_player = Game.isPlayerCube(cube_pos);
                    is_spec = Game.level.isSpecCubeObstacle(cube_pos, this, null, null);
                    is_key = Game.isKeyCube(cube_pos);
                
                    if (is_obstacle || is_player || is_spec || is_key) {
					    cube_pos.init(prev);
					    return;
				    }
				    prev.y = cube_pos.y;
				    ++cube_pos.y;
			    }
			    --cube_pos.y;
			    break;
				
		    case AxisMovement_Y_Minus:
			    while (cube_pos.y >= 0) {
				    is_obstacle = Game.isObstacle(cube_pos);
                    is_player = Game.isPlayerCube(cube_pos);
                    is_spec = Game.level.isSpecCubeObstacle(cube_pos, this, null, null);
                    is_key = Game.isKeyCube(cube_pos);
                
                    if (is_obstacle || is_player || is_spec || is_key) {
					    cube_pos.init(prev);
					    return;
				    }
				    prev.y = cube_pos.y;
				    --cube_pos.y;
			    }
			    ++cube_pos.y;
			    break;
			
		    case AxisMovement_Z_Plus:
			    while (cube_pos.z <= MAX_CUBE_COUNT - 1) {
				    is_obstacle = Game.isObstacle(cube_pos);
                    is_player = Game.isPlayerCube(cube_pos);
                    is_spec = Game.level.isSpecCubeObstacle(cube_pos, this, null, null);
                    is_key = Game.isKeyCube(cube_pos);
                
                    if (is_obstacle || is_player || is_spec || is_key) {
					    cube_pos.init(prev);
					    return;
				    }
				    prev.z = cube_pos.z;
				    ++cube_pos.z;
			    }
			    --cube_pos.z;
			    break;
			
		    case AxisMovement_Z_Minus:
			    while (cube_pos.z >= 0) {
				    is_obstacle = Game.isObstacle(cube_pos);
                    is_player = Game.isPlayerCube(cube_pos);
                    is_spec = Game.level.isSpecCubeObstacle(cube_pos, this, null, null);
                    is_key = Game.isKeyCube(cube_pos);
                
                    if (is_obstacle || is_player || is_spec || is_key) {
					    cube_pos.init(prev);
					    return;
				    }
				    prev.z = cube_pos.z;
				    --cube_pos.z;
			    }
			    ++cube_pos.z;
			    break;
            
            default:
                break;
	    }	
    }

    public void move() {
	    if (m_done) {
		    CubePos cube_pos = m_cube_pos;		
		    calcMovement(cube_pos);

		    if (m_cube_pos.x != cube_pos.x || m_cube_pos.y != cube_pos.y || m_cube_pos.z != cube_pos.z) {
			    m_cube_pos_destination = cube_pos;
            
			    Vector pos_destination = Game.getCubePosAt(cube_pos.x, cube_pos.y, cube_pos.z);
            
			    switch (m_move_dir) {
				    case AxisMovement_X_Plus:
				    case AxisMovement_X_Minus:
					    m_start_value = pos.x;
					    m_end_value = pos_destination.x;
					    position = pos.x;
					    break;
					
				    case AxisMovement_Y_Plus:
				    case AxisMovement_Y_Minus:
					    m_start_value = pos.y;
					    m_end_value = pos_destination.y;
					    position = pos.y;
					    break;
					
				    case AxisMovement_Z_Plus:
				    case AxisMovement_Z_Minus:
					    m_start_value = pos.z;
					    m_end_value = pos_destination.z;
					    position = pos.z;
					    break;
                    
                    default:
                        break;
			    }
            
			    final float speed = 0.5f;
			    float distance = Math.abs(m_start_value - m_end_value);
			    float step = distance / speed;
			    m_t = 0.0f;
			    m_step_t = 1.0f / step;
			    m_done = false;
                m_color_symbol_current = new Color(255, 140, 0, 204);
		    }
	    }
    }

    public void renderCube() {
        Graphics.addCubeSize(pos.x, pos.y, pos.z, HALF_CUBE_SIZE, m_color_current);
    }

    public void renderSymbols() {
        TexCoordsQuad coords = new TexCoordsQuad();
        TexturedQuad pTQ;
        
        pTQ = ar_cube_textures[Face_X_Plus];    
        if (pTQ != null) {
            coords.tx0 = new Vector2(pTQ.tx_lo_left.x,  pTQ.tx_up_left.y);
            coords.tx1 = new Vector2(pTQ.tx_lo_right.x, pTQ.tx_up_right.y);
            coords.tx2 = new Vector2(pTQ.tx_up_right.x, pTQ.tx_lo_right.y);
            coords.tx3 = new Vector2(pTQ.tx_up_left.x,  pTQ.tx_lo_left.y);
            Graphics.addCubeFace_X_Plus(pos.x, pos.y, pos.z, coords, m_color_symbol_current);
        }
    
        pTQ = ar_cube_textures[Face_Z_Plus];    
        if (pTQ != null) {
            coords.tx0 = new Vector2(pTQ.tx_lo_left.x,  pTQ.tx_up_left.y);
            coords.tx1 = new Vector2(pTQ.tx_lo_right.x, pTQ.tx_up_right.y);
            coords.tx2 = new Vector2(pTQ.tx_up_right.x, pTQ.tx_lo_right.y);
            coords.tx3 = new Vector2(pTQ.tx_up_left.x,  pTQ.tx_lo_left.y);
            Graphics.addCubeFace_Z_Plus(pos.x, pos.y, pos.z, coords, m_color_symbol_current);
        }
    
        pTQ = ar_cube_textures[Face_Y_Plus];    
        if (pTQ != null) {
            coords.tx0 = new Vector2(pTQ.tx_up_left.x,  pTQ.tx_lo_left.y);
            coords.tx1 = new Vector2(pTQ.tx_lo_left.x,  pTQ.tx_up_left.y);
            coords.tx2 = new Vector2(pTQ.tx_lo_right.x, pTQ.tx_up_right.y);
            coords.tx3 = new Vector2(pTQ.tx_up_right.x, pTQ.tx_lo_right.y);
            Graphics.addCubeFace_Y_Plus(pos.x, pos.y, pos.z, coords, m_color_symbol_current);
        }
    }
        
    public CubePos getCubePos() { 
        return m_cube_pos; 
    }
	
	public boolean isDone() { 
        return m_done; 
    }
    
	public int getMovement() {
        return m_move_dir; 
    }
    
    public void warmByFactor(int factor) {
        if (m_color_current.r != m_color.r) {
            m_color_current.r += factor;
            
            if (m_color_current.r > m_color.r) {
                m_color_current.r = m_color.r;
            }
        }
        
        if (m_color_current.g != m_color.g) {
            m_color_current.g += factor;
            
            if (m_color_current.g > m_color.g) {
                m_color_current.g = m_color.g;
            }
        }
        
        if (m_color_current.b != m_color.b) {
            m_color_current.b += factor;
            
            if (m_color_current.b > m_color.b) {
                m_color_current.b = m_color.b;
            }
        }
    }
    
    public void setCurrentColor(Color color) {
        m_color_current.init(color);
    }

    public void hiLite() { 
        m_color_symbol_current = new Color(51, 255, 51, 204); 
    }
    
    public void noHiLite() { 
        m_color_symbol_current = m_color_symbol; 
    }
        
}
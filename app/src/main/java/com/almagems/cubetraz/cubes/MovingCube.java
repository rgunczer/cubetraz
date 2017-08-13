package com.almagems.cubetraz.cubes;

import com.almagems.cubetraz.game.Engine;
import com.almagems.cubetraz.game.Game;
import com.almagems.cubetraz.math.Utils;
import com.almagems.cubetraz.math.Vector;
import com.almagems.cubetraz.math.Vector2;
import com.almagems.cubetraz.graphics.Color;
import com.almagems.cubetraz.graphics.Graphics;
import com.almagems.cubetraz.graphics.TexCoordsQuad;
import com.almagems.cubetraz.graphics.TexturedQuad;

import static com.almagems.cubetraz.game.Audio.SOUND_CUBE_HIT;
import static com.almagems.cubetraz.game.Constants.*;


public final class MovingCube {
	private boolean m_done;
    private float m_t;
    private float m_step_t;
    private float m_start_value;
    private float m_end_value;

    private int m_moveType;

    private Color m_color = new Color();
    private Color m_color_current = new Color();

    private Color m_color_symbol = new Color();
    private Color m_color_symbol_current = new Color();
    
    private Vector position;
        
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
        m_cube_pos.init(coordinate);
        pos = Game.getCubePosAt(m_cube_pos);
    }

	public void init(MovingCube other) {

	}

    public void init(CubePos cubePos, int moveDir) {
        setCubePos(cubePos);
        m_cube_pos_starting.init(m_cube_pos);
        m_move_dir_starting = moveDir;
	    m_move_dir = moveDir;
	
	    updateSymbols();

        m_color = new Color(Game.faceColor);
        m_color_current = new Color(m_color);
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
			
			    Game.audio.playSound(SOUND_CUBE_HIT);
			
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
                float value = Utils.lerp(m_start_value, m_end_value, m_t);
                switch (m_moveType) {
                    case AxisMovement_X_Plus:
                    case AxisMovement_X_Minus:
                        position.x = value;
                        pos.x = position.x;
                        break;

                    case AxisMovement_Y_Plus:
                    case AxisMovement_Y_Minus:
                        position.y = value;
                        pos.y = position.y;
                        break;

                    case AxisMovement_Z_Plus:
                    case AxisMovement_Z_Minus:
                        position.z = value;
                        pos.z = position.z;
                        break;
                }
            }
        }
    }

    void calcMovement(CubePos cube_pos) {	
	    CubePos prev = new CubePos();
        prev.init(cube_pos);

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
		    CubePos cube_pos = new CubePos();
			cube_pos.init(m_cube_pos);
		    calcMovement(cube_pos);

		    if (m_cube_pos.x != cube_pos.x || m_cube_pos.y != cube_pos.y || m_cube_pos.z != cube_pos.z) {
			    m_cube_pos_destination = cube_pos;
            
			    Vector pos_destination = Game.getCubePosAt(cube_pos);
                m_moveType = m_move_dir;

			    switch (m_move_dir) {
				    case AxisMovement_X_Plus:
				    case AxisMovement_X_Minus:
					    m_start_value = pos.x;
					    m_end_value = pos_destination.x;
					    position = pos;
					    break;
					
				    case AxisMovement_Y_Plus:
				    case AxisMovement_Y_Minus:
					    m_start_value = pos.y;
					    m_end_value = pos_destination.y;
					    position = pos;
					    break;
					
				    case AxisMovement_Z_Plus:
				    case AxisMovement_Z_Minus:
					    m_start_value = pos.z;
					    m_end_value = pos_destination.z;
					    position = pos;
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
        Engine.graphics.addCubeSize(pos.x, pos.y, pos.z, HALF_CUBE_SIZE, m_color_current);
    }

    public void renderSymbols() {
        Graphics graphics = Engine.graphics;
        TexCoordsQuad coords = new TexCoordsQuad();
        TexturedQuad pTQ;
        
        pTQ = ar_cube_textures[Face_X_Plus];    
        if (pTQ != null) {
            coords.tx0 = new Vector2(pTQ.tx_lo_left.x,  pTQ.tx_up_left.y);
            coords.tx1 = new Vector2(pTQ.tx_lo_right.x, pTQ.tx_up_right.y);
            coords.tx2 = new Vector2(pTQ.tx_up_right.x, pTQ.tx_lo_right.y);
            coords.tx3 = new Vector2(pTQ.tx_up_left.x,  pTQ.tx_lo_left.y);
            graphics.addCubeFace_X_Plus(pos.x, pos.y, pos.z, coords, m_color_symbol_current);
        }
    
        pTQ = ar_cube_textures[Face_Z_Plus];    
        if (pTQ != null) {
            coords.tx0 = new Vector2(pTQ.tx_lo_left.x,  pTQ.tx_up_left.y);
            coords.tx1 = new Vector2(pTQ.tx_lo_right.x, pTQ.tx_up_right.y);
            coords.tx2 = new Vector2(pTQ.tx_up_right.x, pTQ.tx_lo_right.y);
            coords.tx3 = new Vector2(pTQ.tx_up_left.x,  pTQ.tx_lo_left.y);
            graphics.addCubeFace_Z_Plus(pos.x, pos.y, pos.z, coords, m_color_symbol_current);
        }
    
        pTQ = ar_cube_textures[Face_Y_Plus];    
        if (pTQ != null) {
            coords.tx0 = new Vector2(pTQ.tx_up_left.x,  pTQ.tx_lo_left.y);
            coords.tx1 = new Vector2(pTQ.tx_lo_left.x,  pTQ.tx_up_left.y);
            coords.tx2 = new Vector2(pTQ.tx_lo_right.x, pTQ.tx_up_right.y);
            coords.tx3 = new Vector2(pTQ.tx_up_right.x, pTQ.tx_lo_right.y);
            graphics.addCubeFace_Y_Plus(pos.x, pos.y, pos.z, coords, m_color_symbol_current);
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
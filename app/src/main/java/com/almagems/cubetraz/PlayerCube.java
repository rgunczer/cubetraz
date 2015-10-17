package com.almagems.cubetraz;

import static com.almagems.cubetraz.Constants.*;


public final class PlayerCube {
        
	private boolean m_done;
    private float m_t;
    private float m_step_t;
	private float m_start_value;
    private float m_end_value;
    private float position; // pointer (java)
	
    private int m_prev_movement;
    private CubePos m_cube_pos_key;

	public CubePos m_cube_pos;
	public CubePos m_cube_pos_destination;
    
	public MovingCube m_moving_cube; // pointer (java)
    public MoverCube m_mover_cube; // pointer (java)
    public DeadCube m_dead_cube; // pointer (java)
		    
	public Vector pos;
	
    
    // ctor
    public PlayerCube() {
    }
    
        
    public void setCubePos(CubePos cube_pos) {
	    m_cube_pos = cube_pos;
	    pos = Game.getCubePosAt(cube_pos.x, cube_pos.y, cube_pos.z);
	    m_done = true;
    }

    public void init(CubePos cube_pos) {
	    setCubePos(cube_pos);

        m_moving_cube = null;
        m_mover_cube = null;
        m_dead_cube = null;
    }
    
    public void update() {
	    if (!m_done) {
		    m_t += m_step_t;
		
		    if (m_t >= 1.0f) {
			    m_t = 1.0f;
			    setCubePos(m_cube_pos_destination);
		    } else {
			    position = Utils.lerp(m_start_value, m_end_value, m_t);
            }
	    }
    }

    public boolean isKeyCube(CubePos cube_pos) {
	    if (cube_pos.x == m_cube_pos_key.x && cube_pos.y == m_cube_pos_key.y && cube_pos.z == m_cube_pos_key.z) {
		    return true;
        } else {
            return false;
        }
    }

    public void calcMovement(CubePos cube_pos, int type, boolean set) {
        boolean is_obstacle;
        boolean is_moving;
        boolean is_mover;
        boolean is_dead;

        CubePos prev = cube_pos;

        switch(type) {
            case AxisMovement_X_Plus:
			    while (cube_pos.x <= MAX_CUBE_COUNT - 1) {
				    is_obstacle = Game.isObstacle(cube_pos);
                    is_moving = Game.level.isMovingCube(cube_pos, set);
                    is_mover = Game.level.isMoverCube(cube_pos, set);
                    is_dead = Game.level.isDeadCube(cube_pos, set);

                    if (is_obstacle || is_moving || is_mover || is_dead) {
					    cube_pos.init(prev);
					    return;
				    }
				
				    if ( isKeyCube(cube_pos) ) {
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
                    is_moving = Game.level.isMovingCube(cube_pos, set);
                    is_mover = Game.level.isMoverCube(cube_pos, set);
                    is_dead = Game.level.isDeadCube(cube_pos, set);

                    if (is_obstacle || is_moving || is_mover || is_dead) {
                        cube_pos.init(prev);
					    return;
				    }
				
				    if ( isKeyCube(cube_pos) ) {
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
                    is_moving = Game.level.isMovingCube(cube_pos, set);
                    is_mover = Game.level.isMoverCube(cube_pos, set);
                    is_dead = Game.level.isDeadCube(cube_pos, set);

                    if (is_obstacle || is_moving || is_mover || is_dead) {
					    cube_pos.init(prev);
					    return;
				    }
				
				    if ( isKeyCube(cube_pos) ) {
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
                    is_moving = Game.level.isMovingCube(cube_pos, set);
                    is_mover = Game.level.isMoverCube(cube_pos, set);
                    is_dead = Game.level.isDeadCube(cube_pos, set);

                    if (is_obstacle || is_moving || is_mover || is_dead) {
					    cube_pos.init(prev);
					    return;
				    }
				
				    if ( isKeyCube(cube_pos) ) {
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
                    is_moving = Game.level.isMovingCube(cube_pos, set);
                    is_mover = Game.level.isMoverCube(cube_pos, set);
                    is_dead = Game.level.isDeadCube(cube_pos, set);

                    if (is_obstacle || is_moving || is_mover || is_dead) {
					    cube_pos.init(prev);
					    return;
				    }
				
				    if ( isKeyCube(cube_pos) ) {
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
                    is_moving = Game.level.isMovingCube(cube_pos, set);
                    is_mover = Game.level.isMoverCube(cube_pos, set);
                    is_dead = Game.level.isDeadCube(cube_pos, set);

                    if (is_obstacle || is_moving || is_mover || is_dead) {
					    cube_pos.init(prev);
					    return;
				    }

				    if ( isKeyCube(cube_pos) ) {
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

    public boolean moveOnAxis(int type) {
	    if (m_done) {
		    m_moving_cube = null;
            m_mover_cube = null;
            m_dead_cube = null;
		
		    CubePos cube_pos = m_cube_pos;
		    calcMovement(cube_pos, type, false);
		
		    if (m_cube_pos.x != cube_pos.x || m_cube_pos.y != cube_pos.y || m_cube_pos.z != cube_pos.z) {            
			    m_cube_pos_destination = cube_pos;
			
			    Vector pos_destination = Game.getCubePosAt(cube_pos.x, cube_pos.y, cube_pos.z);
			
			    switch (type) {
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
			    return true;
		    }
	    }
        return false;
    }
            
    public void setKeyCubePos(CubePos cube_pos) { 
        m_cube_pos_key.init(cube_pos);
    }
    
    public void setKeyCubePos(int x, int y, int z) { 
        m_cube_pos_key = new CubePos(x, y, z); 
    }
	    
    public int getPrevMovement() {
        return m_prev_movement; 
    }
    
	CubePos getCubePos() { 
        return m_cube_pos; 
    }
    
    public boolean isLevelCompleted(int x, int y, int z) {
        CubePos cp = new CubePos(x, y, z);
        return isKeyCube(cp);
    }
	
	public boolean isDone() { 
        return m_done; 
    }
	        
    public void setMovingCube(MovingCube cube) { 
        m_moving_cube = cube; 
    }
    
    public void setMoverCube(MoverCube cube) { 
        m_mover_cube = cube; 
    }
    
    public void setDeadCube(DeadCube cube) { 
        m_dead_cube = cube; 
    }

}
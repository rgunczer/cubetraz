package com.almagems.cubetraz.cubes;

import com.almagems.cubetraz.Game;
import com.almagems.cubetraz.math.Utils;
import com.almagems.cubetraz.math.Vector;
import static com.almagems.cubetraz.Game.*;

public final class PlayerCube {
        
	private boolean mDone;
    private float m_t;
    private float m_step_t;
	private float mStartValue;
    private float mEndValue;

    private int m_moveType;

    private Vector position;

    private CubeLocation m_cube_pos_key = new CubeLocation();

	public CubeLocation location = new CubeLocation();
	public CubeLocation destination = new CubeLocation();
    
	public MovingCube movingCube;
    public MoverCube moverCube;
    public DeadCube deadCube;
		    
	public Vector pos = new Vector();
	

    public PlayerCube() {
    }

    public void setLocation(CubeLocation location) {
        setLocation(location.x, location.y, location.z);
    }

    public void setLocation(int x, int y, int z) {
	    this.location.init(x, y, z);
	    pos = Game.getCubePosAt(location);
	    mDone = true;
    }

    public void init(CubeLocation location) {
	    setLocation(location);

        movingCube = null;
        moverCube = null;
        deadCube = null;
    }
    
    public void update() {
	    if (!mDone) {
		    m_t += m_step_t;
		
		    if (m_t >= 1.0f) {
			    m_t = 1.0f;
			    setLocation(destination);
		    } else {
			    float value = Utils.lerp(mStartValue, mEndValue, m_t);
                switch (m_moveType) {
                    case AxisMovement_X_Plus:
                    case AxisMovement_X_Minus:
                        position.x = value;
                        break;

                    case AxisMovement_Y_Plus:
                    case AxisMovement_Y_Minus:
                        position.y = value;
                        break;

                    case AxisMovement_Z_Plus:
                    case AxisMovement_Z_Minus:
                        position.z = value;
                        break;
                }
            }
	    }
    }

    private boolean isKeyCube(CubeLocation location) {
	    if (location.x == m_cube_pos_key.x && location.y == m_cube_pos_key.y && location.z == m_cube_pos_key.z) {
		    return true;
        } else {
            return false;
        }
    }

    public void calcMovement(CubeLocation cube_pos, int type, boolean set) {
        boolean is_obstacle;
        boolean is_moving;
        boolean is_mover;
        boolean is_dead;

        CubeLocation prev = new CubeLocation();
        prev.init(cube_pos);

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
	    if (mDone) {
		    movingCube = null;
            moverCube = null;
            deadCube = null;
		
		    CubeLocation cube_pos = new CubeLocation();
            cube_pos.init(location);
		    calcMovement(cube_pos, type, true);
		
		    if (location.x != cube_pos.x || location.y != cube_pos.y || location.z != cube_pos.z) {
			    destination = cube_pos;
			    Vector pos_destination = Game.getCubePosAt(cube_pos);
                m_moveType = type;
			
			    switch (type) {
				    case AxisMovement_X_Plus:
				    case AxisMovement_X_Minus:
					    mStartValue = pos.x;
					    mEndValue = pos_destination.x;
					    position = pos;
					    break;
					
				    case AxisMovement_Y_Plus:
				    case AxisMovement_Y_Minus:
					    mStartValue = pos.y;
					    mEndValue = pos_destination.y;
					    position = pos;
					    break;
					
				    case AxisMovement_Z_Plus:
				    case AxisMovement_Z_Minus:
					    mStartValue = pos.z;
					    mEndValue = pos_destination.z;
					    position = pos;
					    break;
                    
                    default:
                        break;
			    }
			
			    final float speed = 0.5f;
			    float distance = Math.abs(mStartValue - mEndValue);
			    float step = distance / speed;
			    m_t = 0.0f;
			    m_step_t = 1.0f / step;
			    mDone = false;
			    return true;
		    }
	    }
        return false;
    }
            
    public void setKeyCubePos(CubeLocation location) {
        m_cube_pos_key.init(location);
    }

	public CubeLocation getLocation() {
        return location;
    }

	public boolean isDone() { 
        return mDone;
    }
	        
    public void setMovingCube(MovingCube cube) { 
        movingCube = cube;
    }
    
    public void setMoverCube(MoverCube cube) { 
        moverCube = cube;
    }
    
    public void setDeadCube(DeadCube cube) { 
        deadCube = cube;
    }

}
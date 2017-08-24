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
	    pos = Game.getCubePosition(location);
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
                    case X_Plus:
                    case X_Minus:
                        position.x = value;
                        break;

                    case Y_Plus:
                    case Y_Minus:
                        position.y = value;
                        break;

                    case Z_Plus:
                    case Z_Minus:
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

    public void calcMovement(CubeLocation location, int type, boolean set) {
        boolean is_obstacle;
        boolean is_moving;
        boolean is_mover;
        boolean is_dead;

        CubeLocation prev = new CubeLocation();
        prev.init(location);

        switch(type) {
            case X_Plus:
			    while (location.x <= MAX_CUBE_COUNT - 1) {
				    is_obstacle = Game.isObstacle(location);
                    is_moving = Game.level.isMovingCube(location, set);
                    is_mover = Game.level.isMoverCube(location, set);
                    is_dead = Game.level.isDeadCube(location, set);

                    if (is_obstacle || is_moving || is_mover || is_dead) {
					    location.init(prev);
					    return;
				    }
				
				    if ( isKeyCube(location) ) {
					    return;
                    }
				
				    prev.x = location.x;
				    ++location.x;
			    }
			    --location.x;
                break;
            
            case X_Minus:
			    while (location.x >= 0) {
				    is_obstacle = Game.isObstacle(location);
                    is_moving = Game.level.isMovingCube(location, set);
                    is_mover = Game.level.isMoverCube(location, set);
                    is_dead = Game.level.isDeadCube(location, set);

                    if (is_obstacle || is_moving || is_mover || is_dead) {
                        location.init(prev);
					    return;
				    }
				
				    if ( isKeyCube(location) ) {
					    return;
                    }
				
				    prev.x = location.x;
				    --location.x;
			    }
			    ++location.x;
                break;
            
            case Y_Plus:
			    while (location.y <= MAX_CUBE_COUNT - 1) {
				    is_obstacle = Game.isObstacle(location);
                    is_moving = Game.level.isMovingCube(location, set);
                    is_mover = Game.level.isMoverCube(location, set);
                    is_dead = Game.level.isDeadCube(location, set);

                    if (is_obstacle || is_moving || is_mover || is_dead) {
					    location.init(prev);
					    return;
				    }
				
				    if ( isKeyCube(location) ) {
					    return;
                    }
				
				    prev.y = location.y;
				    ++location.y;
			    }
			    --location.y;
                break;
            
            case Y_Minus:
			    while (location.y >= 0) {
				    is_obstacle = Game.isObstacle(location);
                    is_moving = Game.level.isMovingCube(location, set);
                    is_mover = Game.level.isMoverCube(location, set);
                    is_dead = Game.level.isDeadCube(location, set);

                    if (is_obstacle || is_moving || is_mover || is_dead) {
					    location.init(prev);
					    return;
				    }
				
				    if ( isKeyCube(location) ) {
					    return;
                    }
				
				    prev.y = location.y;
				    --location.y;
			    }
			    ++location.y;
                break;
            
            case Z_Plus:
			    while (location.z <= MAX_CUBE_COUNT - 1) {
				    is_obstacle = Game.isObstacle(location);
                    is_moving = Game.level.isMovingCube(location, set);
                    is_mover = Game.level.isMoverCube(location, set);
                    is_dead = Game.level.isDeadCube(location, set);

                    if (is_obstacle || is_moving || is_mover || is_dead) {
					    location.init(prev);
					    return;
				    }
				
				    if ( isKeyCube(location) ) {
					    return;
                    }

				    prev.z = location.z;
				    ++location.z;
			    }
			    --location.z;
                break;
            
            case Z_Minus:
			    while (location.z >= 0) {
                    is_obstacle = Game.isObstacle(location);
                    is_moving = Game.level.isMovingCube(location, set);
                    is_mover = Game.level.isMoverCube(location, set);
                    is_dead = Game.level.isDeadCube(location, set);

                    if (is_obstacle || is_moving || is_mover || is_dead) {
					    location.init(prev);
					    return;
				    }

				    if ( isKeyCube(location) ) {
					    return;
                    }

				    prev.z = location.z;
				    --location.z;
			    }
			    ++location.z;
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
			    Vector pos_destination = Game.getCubePosition(cube_pos);
                m_moveType = type;
			
			    switch (type) {
				    case X_Plus:
				    case X_Minus:
					    mStartValue = pos.x;
					    mEndValue = pos_destination.x;
					    position = pos;
					    break;
					
				    case Y_Plus:
				    case Y_Minus:
					    mStartValue = pos.y;
					    mEndValue = pos_destination.y;
					    position = pos;
					    break;
					
				    case Z_Plus:
				    case Z_Minus:
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
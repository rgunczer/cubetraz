package com.almagems.cubetraz.cubes;

import com.almagems.cubetraz.Audio;
import com.almagems.cubetraz.Game;
import com.almagems.cubetraz.math.Utils;
import com.almagems.cubetraz.math.Vector;
import com.almagems.cubetraz.graphics.Color;

import java.util.ArrayList;

import static com.almagems.cubetraz.Audio.SOUND_CUBE_HIT;
import static com.almagems.cubetraz.Game.*;

public final class MenuCube {
    
    private CubeLocation mCubeHiliteOffset = new CubeLocation();
    
    private boolean mDone;
    private float m_t;
    private float m_step_t;
    private float m_start_value;
    private float m_end_value;

    private int mMoveType;

    private Vector position;
    
    public float speed;

    public Vector pos;
	public Color color;
	public boolean visible;
    
    public CubeLocation location = new CubeLocation();
    public CubeLocation m_cube_pos_destination = new CubeLocation();

    public ArrayList<Cube> cubesToHilite = new ArrayList<>();


    public MenuCube() {
        color = new Color();
        speed = 0.5f;
        mCubeHiliteOffset = new CubeLocation(0,0,0);
    }

    public void init(CubeLocation location, Color color) {
        cubesToHilite.clear();
        this.color.init(color);
	    visible = true;
        setCubeLocation(location.x, location.y, location.z);
    }

    public void setCubeLocation(int x, int y, int z) {
	    mDone = true;
        location.x = x;
        location.y = y;
        location.z = z;
        pos = Game.getCubePosition(location);
    }

    private CubeLocation calculateHiLiteOffset(CubeLocation location) {
        CubeLocation offset = new CubeLocation(0, 0, 0);

        if (location.x == 0) {
            offset.x = 1;
        } else if (location.x == MAX_CUBE_COUNT - 1) {
            offset.x = -1;
        }

        if (location.y == 0) {
            offset.y = 1;
        } else if (location.y == MAX_CUBE_COUNT - 1) {
            offset.y = -1;
        }

        if (location.z == 0) {
            offset.z = 1;
        } else if (location.z == MAX_CUBE_COUNT - 1) {
            offset.z = -1;
        }
        System.out.println("calcHiLiteOffset: " + offset);
        return offset;
    }

    public void update() {
        if (!mDone) {
		    m_t += m_step_t;        
		    if (m_t >= 1.0f) {
			    m_t = 1.0f;
			    mDone = true;
			    Audio.playSound(SOUND_CUBE_HIT);
			    setCubeLocation(m_cube_pos_destination.x, m_cube_pos_destination.y, m_cube_pos_destination.z);
		    } else {
                float value = Utils.lerp(m_start_value, m_end_value, m_t);
                switch (mMoveType) {
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

    public void calcMove(CubeLocation cube_pos, int type) {
        CubeLocation prev = new CubeLocation();
        prev.init(cube_pos);
	
        switch(type) {
            case AxisMovement_X_Plus:
			    while (cube_pos.x <= MAX_CUBE_COUNT - 1) {
				    if ( Game.isObstacle(cube_pos) ) {
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
				if ( Game.isObstacle(cube_pos) ) {
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
				if ( Game.isObstacle(cube_pos) ) {
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
				if ( Game.isObstacle(cube_pos) ) {
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
				if ( Game.isObstacle(cube_pos) ) {
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
				if ( Game.isObstacle(cube_pos) ) {
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

    public void moveOnAxis(int type) {
        if (mDone) {
            CubeLocation cubeLocation = new CubeLocation();
            cubeLocation.init(location);
            calcMove(cubeLocation, type);

            if (location.x != cubeLocation.x || location.y != cubeLocation.y || location.z != cubeLocation.z) {
                Game.menu.dontHiliteMenuCube();
                mCubeHiliteOffset.init( calculateHiLiteOffset(location) );
                m_cube_pos_destination = cubeLocation;
                Vector pos_destination = Game.getCubePosition(cubeLocation);
                mMoveType = type;

                switch (mMoveType) {
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

                float distance = Math.abs(m_start_value - m_end_value);
                float step = distance / speed;
                m_t = 0.0f;
                m_step_t = 1.0f / step;
                mDone = false;

                if (0 != mCubeHiliteOffset.x || 0 != mCubeHiliteOffset.y || 0 != mCubeHiliteOffset.z) {
                    cubesToHilite.clear();

                    switch (type) {
                        case AxisMovement_X_Plus: {
                            int y, z;
                            y = location.y + mCubeHiliteOffset.y;
                            z = location.z + mCubeHiliteOffset.z;

                            for (int i = location.x; i <= m_cube_pos_destination.x; ++i) {
                                cubesToHilite.add(Game.cubes[i][y][z]);
                            }
                        }
                        break;

                        case AxisMovement_X_Minus: {
                            int y, z;
                            y = location.y + mCubeHiliteOffset.y;
                            z = location.z + mCubeHiliteOffset.z;

                            for (int i = location.x; i >= m_cube_pos_destination.x; --i) {
                                cubesToHilite.add(Game.cubes[i][y][z]);
                            }
                        }
                        break;

                        case AxisMovement_Y_Plus: {
                            int x, z;
                            x = location.x + mCubeHiliteOffset.x;
                            z = location.z + mCubeHiliteOffset.z;

                            for (int i = location.y; i <= m_cube_pos_destination.y; ++i) {
                                cubesToHilite.add(Game.cubes[x][i][z]);
                            }
                        }
                        break;

                        case AxisMovement_Y_Minus: {
                            int x, z;
                            x = location.x + mCubeHiliteOffset.x;
                            z = location.z + mCubeHiliteOffset.z;

                            for (int i = location.y; i >= m_cube_pos_destination.y; --i) {
                                cubesToHilite.add(Game.cubes[x][i][z]);
                            }
                        }
                        break;

                        case AxisMovement_Z_Plus: {
                            int x, y;
                            x = location.x + mCubeHiliteOffset.x;
                            y = location.y + mCubeHiliteOffset.y;

                            for (int i = location.z; i <= m_cube_pos_destination.z; ++i) {
                                cubesToHilite.add(Game.cubes[x][y][i]);
                            }
                        }
                        break;

                        case AxisMovement_Z_Minus: {
                            int x, y;
                            x = location.x + mCubeHiliteOffset.x;
                            y = location.y + mCubeHiliteOffset.y;

                            for (int i = location.z; i >= m_cube_pos_destination.z; --i) {
                                cubesToHilite.add(Game.cubes[x][y][i]);
                            }
                        }
                        break;

                        default:
                            break;
                    }
                }
            }
        }
    }
    
    public boolean isDone() {
        return mDone;
    }
    
}
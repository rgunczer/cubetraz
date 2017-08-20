package com.almagems.cubetraz.scenes.menu;

import com.almagems.cubetraz.cubes.Cube;
import com.almagems.cubetraz.cubes.CubeLocation;
import com.almagems.cubetraz.Game;
import com.almagems.cubetraz.math.Utils;
import com.almagems.cubetraz.math.Vector;
import com.almagems.cubetraz.graphics.Color;

import java.util.ArrayList;

import static com.almagems.cubetraz.Audio.SOUND_CUBE_HIT;
import static com.almagems.cubetraz.Game.*;

public final class MenuCube {
    
    private CubeLocation m_cube_hilite_offset;
    
    private boolean m_done;
    private float m_t;
    private float m_step_t;
    private float m_start_value;
    private float m_end_value;

    private int m_moveType;

    private Vector position;
    
    public float speed;
    
    public final ArrayList<Cube> lst_cubes_to_hilite = new ArrayList<>();

    public Vector pos;
	public Color color;
	public boolean visible;
    
    public CubeLocation cubeLocation = new CubeLocation();
    public CubeLocation m_cube_pos_destination = new CubeLocation();


    public MenuCube() {
        color = new Color();
        speed = 0.5f;
        m_cube_hilite_offset = new CubeLocation(0,0,0);
    }

    public void init(CubeLocation location, Color color) {
        lst_cubes_to_hilite.clear();
        this.color.init(color);
	    visible = true;
        setCubePos(location.x, location.y, location.z);
    }

    public void setCubePos(int x, int y, int z) {
	    m_done = true;
        cubeLocation.x = x;
        cubeLocation.y = y;
        cubeLocation.z = z;
        pos = Game.getCubePosAt(cubeLocation);
    }

    public void setHiliteOffset(CubeLocation offset) {
        m_cube_hilite_offset = offset;
    }
    
    public void update() {
        if (!m_done) {
		    m_t += m_step_t;        
		    if (m_t >= 1.0f) {
			    m_t = 1.0f;
			    m_done = true;                
			    Game.audio.playSound(SOUND_CUBE_HIT);
			    setCubePos(m_cube_pos_destination.x, m_cube_pos_destination.y, m_cube_pos_destination.z);
		    } else {
                float value = Utils.lerp(m_start_value, m_end_value, m_t);
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
        if (m_done) {
            CubeLocation cube_pos = new CubeLocation();
            cube_pos.init(cubeLocation);
            calcMove(cube_pos, type);

            if (cubeLocation.x != cube_pos.x || cubeLocation.y != cube_pos.y || cubeLocation.z != cube_pos.z) {
                Game.menu.dontHiliteMenuCube();
                m_cube_pos_destination = cube_pos;
                Vector pos_destination = Game.getCubePosAt(cube_pos);
                m_moveType = type;

                switch (m_moveType) {
                    case AxisMovement_X_Plus:
                    case AxisMovement_X_Minus:
                        m_start_value = pos.x;
                        m_end_value = pos_destination.x;
                        position = pos; //.x; // pointer! (java)
                        break;

                    case AxisMovement_Y_Plus:
                    case AxisMovement_Y_Minus:
                        m_start_value = pos.y;
                        m_end_value = pos_destination.y;
                        position = pos; //.y; // pointer! (java)
                        break;

                    case AxisMovement_Z_Plus:
                    case AxisMovement_Z_Minus:
                        m_start_value = pos.z;
                        m_end_value = pos_destination.z;
                        position = pos; //.z; // pointer! (java)
                        break;

                    default:
                        break;
                }

                float distance = Math.abs(m_start_value - m_end_value);
                float step = distance / speed;
                m_t = 0.0f;
                m_step_t = 1.0f / step;
                m_done = false;

                if (0 != m_cube_hilite_offset.x || 0 != m_cube_hilite_offset.y || 0 != m_cube_hilite_offset.z) {
                    lst_cubes_to_hilite.clear();

                    switch (type) {
                        case AxisMovement_X_Plus: {
                            int y, z;
                            y = cubeLocation.y + m_cube_hilite_offset.y;
                            z = cubeLocation.z + m_cube_hilite_offset.z;

                            for (int i = cubeLocation.x; i <= m_cube_pos_destination.x; ++i) {
                                lst_cubes_to_hilite.add(Game.cubes[i][y][z]);
                            }
                        }
                        break;

                        case AxisMovement_X_Minus: {
                            int y, z;
                            y = cubeLocation.y + m_cube_hilite_offset.y;
                            z = cubeLocation.z + m_cube_hilite_offset.z;

                            for (int i = cubeLocation.x; i >= m_cube_pos_destination.x; --i) {
                                lst_cubes_to_hilite.add(Game.cubes[i][y][z]);
                            }
                        }
                        break;

                        case AxisMovement_Y_Plus: {
                            int x, z;
                            x = cubeLocation.x + m_cube_hilite_offset.x;
                            z = cubeLocation.z + m_cube_hilite_offset.z;

                            for (int i = cubeLocation.y; i <= m_cube_pos_destination.y; ++i) {
                                lst_cubes_to_hilite.add(Game.cubes[x][i][z]);
                            }
                        }
                        break;

                        case AxisMovement_Y_Minus: {
                            int x, z;
                            x = cubeLocation.x + m_cube_hilite_offset.x;
                            z = cubeLocation.z + m_cube_hilite_offset.z;

                            for (int i = cubeLocation.y; i >= m_cube_pos_destination.y; --i) {
                                lst_cubes_to_hilite.add(Game.cubes[x][i][z]);
                            }
                        }
                        break;

                        case AxisMovement_Z_Plus: {
                            int x, y;
                            x = cubeLocation.x + m_cube_hilite_offset.x;
                            y = cubeLocation.y + m_cube_hilite_offset.y;

                            for (int i = cubeLocation.z; i <= m_cube_pos_destination.z; ++i) {
                                lst_cubes_to_hilite.add(Game.cubes[x][y][i]);
                            }
                        }
                        break;

                        case AxisMovement_Z_Minus: {
                            int x, y;
                            x = cubeLocation.x + m_cube_hilite_offset.x;
                            y = cubeLocation.y + m_cube_hilite_offset.y;

                            for (int i = cubeLocation.z; i >= m_cube_pos_destination.z; --i) {
                                lst_cubes_to_hilite.add(Game.cubes[x][y][i]);
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
        return m_done; 
    }
    
}
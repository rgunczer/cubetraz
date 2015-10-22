package com.almagems.cubetraz;

import java.util.ArrayList;

import static com.almagems.cubetraz.Constants.*;


public final class MenuCube {
    
    private CubePos m_cube_hilite_offset;
    
    private boolean m_done;
    private float m_t;
    private float m_step_t;
    private float m_start_value;
    private float m_end_value;
    
    private float position; // pointer!
    
    public float speed;
    
    public final ArrayList<Cube> lst_cubes_to_hilite = new ArrayList<>();

    public Vector pos;
	public Color color;
	public boolean visible;
    
    public CubePos m_cube_pos = new CubePos();
    public CubePos m_cube_pos_destination = new CubePos();
    
    // ctor    
    public MenuCube() {
        color = new Color();
        speed = 0.5f;
        m_cube_hilite_offset = new CubePos(0,0,0);
    }

    public void init(CubePos cube_pos, Color color) {
        lst_cubes_to_hilite.clear();
        this.color.init(color);
	    visible = true;
        setCubePos(cube_pos);
    }

    public void setCubePos(CubePos coordinate) {
	    m_done = true;
        m_cube_pos.x = coordinate.x;
        m_cube_pos.y = coordinate.y;
        m_cube_pos.z = coordinate.z;
        pos = Game.getCubePosAt(m_cube_pos.x, m_cube_pos.y, m_cube_pos.z);
    }

    public void setHiliteOffset(CubePos offset) {
        m_cube_hilite_offset = offset;
    }
    
    public void update() {
        if (!m_done) {
		    m_t += m_step_t;        
		    if (m_t >= 1.0f) {
			    m_t = 1.0f;
			    m_done = true;                
			    //engine->PlaySound(SOUND_CUBE_HIT);			
			    setCubePos(m_cube_pos_destination);
		    } else {
                //*position = LERP(m_start_value, m_end_value, m_t);
            }
        }
	}

    public void calcMove(CubePos cube_pos, int type) {
        CubePos prev = cube_pos;
	
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
            CubePos cube_pos = m_cube_pos;
            calcMove(cube_pos, type);

            if (m_cube_pos.x != cube_pos.x || m_cube_pos.y != cube_pos.y || m_cube_pos.z != cube_pos.z) {
                Game.menu.dontHiliteMenuCube();
                m_cube_pos_destination = cube_pos;
                Vector pos_destination = Game.getCubePosAt(cube_pos.x, cube_pos.y, cube_pos.z);

                switch (type) {
                    case AxisMovement_X_Plus:
                    case AxisMovement_X_Minus:
                        m_start_value = pos.x;
                        m_end_value = pos_destination.x;
                        position = pos.x; // pointer! (java)
                        break;

                    case AxisMovement_Y_Plus:
                    case AxisMovement_Y_Minus:
                        m_start_value = pos.y;
                        m_end_value = pos_destination.y;
                        position = pos.y; // pointer! (java)
                        break;

                    case AxisMovement_Z_Plus:
                    case AxisMovement_Z_Minus:
                        m_start_value = pos.z;
                        m_end_value = pos_destination.z;
                        position = pos.z; // pointer! (java)
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
                            y = m_cube_pos.y + m_cube_hilite_offset.y;
                            z = m_cube_pos.z + m_cube_hilite_offset.z;

                            for (int i = m_cube_pos.x; i <= m_cube_pos_destination.x; ++i) {
                                lst_cubes_to_hilite.add(Game.cubes[i][y][z]);
                            }
                        }
                        break;

                        case AxisMovement_X_Minus: {
                            int y, z;
                            y = m_cube_pos.y + m_cube_hilite_offset.y;
                            z = m_cube_pos.z + m_cube_hilite_offset.z;

                            for (int i = m_cube_pos.x; i >= m_cube_pos_destination.x; --i) {
                                lst_cubes_to_hilite.add(Game.cubes[i][y][z]);
                            }
                        }
                        break;

                        case AxisMovement_Y_Plus: {
                            int x, z;
                            x = m_cube_pos.x + m_cube_hilite_offset.x;
                            z = m_cube_pos.z + m_cube_hilite_offset.z;

                            for (int i = m_cube_pos.y; i <= m_cube_pos_destination.y; ++i) {
                                lst_cubes_to_hilite.add(Game.cubes[x][i][z]);
                            }
                        }
                        break;

                        case AxisMovement_Y_Minus: {
                            int x, z;
                            x = m_cube_pos.x + m_cube_hilite_offset.x;
                            z = m_cube_pos.z + m_cube_hilite_offset.z;

                            for (int i = m_cube_pos.y; i >= m_cube_pos_destination.y; --i) {
                                lst_cubes_to_hilite.add(Game.cubes[x][i][z]);
                            }
                        }
                        break;

                        case AxisMovement_Z_Plus: {
                            int x, y;
                            x = m_cube_pos.x + m_cube_hilite_offset.x;
                            y = m_cube_pos.y + m_cube_hilite_offset.y;

                            for (int i = m_cube_pos.z; i <= m_cube_pos_destination.z; ++i) {
                                lst_cubes_to_hilite.add(Game.cubes[x][y][i]);
                            }
                        }
                        break;

                        case AxisMovement_Z_Minus: {
                            int x, y;
                            x = m_cube_pos.x + m_cube_hilite_offset.x;
                            y = m_cube_pos.y + m_cube_hilite_offset.y;

                            for (int i = m_cube_pos.z; i >= m_cube_pos_destination.z; --i) {
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
package com.almagems.cubetraz;

import java.util.ArrayList;


public class cLevelBuilder {
        
    public static Level level;
    public static final CubePos player = new CubePos();
    public static final CubePos key = new CubePos();

    public static final ArrayList<MovingCube> lst_moving_cubes = new ArrayList();
    public static final ArrayList<MoverCube> lst_mover_cubes = new ArrayList();
    public static final ArrayList<DeadCube> lst_dead_cubes = new ArrayList();
    
    public static final ArrayList<MovingCube> lst_moving_cubes_pool = new ArrayList();
    public static final ArrayList<MoverCube> lst_mover_cubes_pool = new ArrayList();
    public static final ArrayList<DeadCube> lst_dead_cubes_pool = new ArrayList();
    
}
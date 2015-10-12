package com.almagems.cubetraz;

import java.util.ArrayList;


public final class LevelBuilder {
        
    public static Level level;
    public static final CubePos player = new CubePos();
    public static final CubePos key = new CubePos();

    public static final ArrayList<MovingCube> lst_moving_cubes = new ArrayList();
    public static final ArrayList<MoverCube> lst_mover_cubes = new ArrayList();
    public static final ArrayList<DeadCube> lst_dead_cubes = new ArrayList();
    
    public static final ArrayList<MovingCube> lst_moving_cubes_pool = new ArrayList();
    public static final ArrayList<MoverCube> lst_mover_cubes_pool = new ArrayList();
    public static final ArrayList<DeadCube> lst_dead_cubes_pool = new ArrayList();


    public static MovingCube getNewMovingCube() {
        MovingCube p = null;
    
        if (lst_moving_cubes_pool.isEmpty()) {
            p = new MovingCube();
        } else {
            p = lst_moving_cubes_pool.get( lst_moving_cubes_pool.size() - 1 );
            lst_moving_cubes_pool.remove(p);
        }    
        return p;
    }

    public static MoverCube getNewMoverCube() {
        MoverCube* p = null;
    
        if (lst_mover_cubes_pool.isEmpty()) {
            p = new MoverCube();
        } else {    
            p = lst_mover_cubes_pool.get( lst_mover_cubes_pool.size() - 1 );
            lst_mover_cubes_pool.remove(p);
        }    
        return p;
    }

    public static DeadCube getNewDeadCube() {
        DeadCube* p = null;
    
        if (lst_dead_cubes_pool.empty()) { 
            p = new DeadCube();
        } else {
            p = lst_dead_cubes_pool.get( lst_dead_cubes_pool.size() - 1 );
            lst_dead_cubes_pool.remove(p);
        }    
        return p;
    }

    public static void recycleMovingCubes() {
        //printf("\nRecycle Moving Cubes...");
        MovingCube cube;
        int len = lst_moving_cubes.size();            
        for (int i = 0; i < len; ++i)  {
            cube = lst_moving_cubes.get(i);
            Game->setCubeTypeInvisible( cube.getCubePos() );
            lst_moving_cubes_pool.add(cube);
        }

        lst_moving_cubes.clear();
    }

    public static void recycleMoverCubes() {
        //printf("\nRecycle Mover Cubes...");
/*
        list<cMoverCube*>::iterator it;
        for (it = lst_mover_cubes.begin(); it != lst_mover_cubes.end(); ++it) {
            engine->SetCubeTypeInvisible( (*it)->GetCubePos() );
            lst_mover_cubes_pool.push_back(*it);
        }
        lst_mover_cubes.clear();
*/
    }

    public static void recycleDeadCubes() {
        //printf("\nRecycle Dead Cubes...");
/*
        list<cDeadCube*>::iterator it;
        for (it = lst_dead_cubes.begin(); it != lst_dead_cubes.end(); ++it) 
        {
            engine->SetCubeTypeInvisible( (*it)->GetCubePos() );
            lst_dead_cubes_pool.push_back(*it);
        }
        lst_dead_cubes.clear();
*/
    }

    public static void prepare() {
        recycleMovingCubes();
	    recycleMoverCubes();
	    recycleDeadCubes();
	    level.m_list_cubes_hint.clear();
    }

    public void setup(int arr[], int size) {
        player.x = arr[0];
        player.y = arr[1];
        player.z = arr[2];
    
        key.x = arr[3];
        key.y = arr[4];
        key.z = arr[5];
    
        level.m_ad_level.clear();
    
        Color color = Game.getFaceColor();
        Cube pCube;
	    int x, y, z;
    
        for (int i = 6; i < size; i += 3) {
            x = arr[i];
            y = arr[i+1];
            z = arr[i+2];
        
            pCube = Game.cubes[x][y][z];
        
            pCube.type = CubeIsVisibleAndObstacleAndLevel;
		    pCube.setColor(color);
        
            level.m_ad_level.AddAppear(pCube);
        }
    }

    public void setupSolution(int arr[], int size) {
        int i;
        for (i = 0; i < MAX_SOLUTION_MOVES; ++i) {
            level.m_ar_solution[i] = No_Move;
        }
	
        for (i = 0; i < size; ++i) {
            level.m_ar_solution[i] = (AxisMovementEnum)arr[i];
        }
	
	    level.m_min_solution_steps = size;
    }

    public void setupMovingCubes(int arr[], int size) {
        MovingCube pMovingCube;
	
        for (int i = 0; i < size; i+=4) {
            pMovingCube = getNewMovingCube();
            pMovingCube.init(new CubePos(arr[i], arr[i+1], arr[i+2]), (AxisMovementEnum)arr[i+3]);        
            lst_moving_cubes.add(pMovingCube);
        }
    }

    void setupMoverCubes(int arr[], int size) {
	    MoverCube pMoverCube;
	
	    for (int i = 0; i < size; i+=4) {	
		    pMoverCube = getNewMoverCube();
		    pMoverCube.init(new CubePos(arr[i], arr[i+1], arr[i+2]), (AxisMovementEnum)arr[i+3]);
            lst_mover_cubes.add(pMoverCube);
	    }
    }

    public void setupDeathCubes(int arr[], int size) {
	    DeadCube pDeadCube;
	
	    for (int i = 0; i < size; i+=3) {
		    pDeadCube = getNewDeadCube();
		    pDeadCube.init(new CubePos(arr[i], arr[i+1], arr[i+2]));
            lst_dead_cubes.add(pDeadCube);
	    }
    }

    public void setupHintCubes(int arr[], int size) {
	    Cube pCube;
	
	    for (int i = 0; i < size; i+=3) {
		    pCube = Game.cubes[arr[i]][arr[i+1]][arr[i+2]];
		    level.m_list_cubes_hint.add(pCube);
	    }
    }

}
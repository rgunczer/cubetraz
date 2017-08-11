package com.almagems.cubetraz.builder;

import com.almagems.cubetraz.game.Game;
import com.almagems.cubetraz.cubes.Cube;
import com.almagems.cubetraz.cubes.CubePos;
import com.almagems.cubetraz.cubes.DeadCube;
import com.almagems.cubetraz.cubes.MoverCube;
import com.almagems.cubetraz.cubes.MovingCube;
import com.almagems.cubetraz.graphics.Color;
import com.almagems.cubetraz.scenes.level.Level;

import java.util.ArrayList;

import static com.almagems.cubetraz.game.Constants.*;


public final class LevelBuilder {
        
    public static Level level;
    public static final CubePos player = new CubePos();
    public static final CubePos key = new CubePos();

    public static final ArrayList<MovingCube> lst_moving_cubes = new ArrayList<>();
    public static final ArrayList<MoverCube> lst_mover_cubes = new ArrayList<>();
    public static final ArrayList<DeadCube> lst_dead_cubes = new ArrayList<>();
    
    public static final ArrayList<MovingCube> lst_moving_cubes_pool = new ArrayList<>();
    public static final ArrayList<MoverCube> lst_mover_cubes_pool = new ArrayList<>();
    public static final ArrayList<DeadCube> lst_dead_cubes_pool = new ArrayList<>();


    public static MovingCube getNewMovingCube() {
        MovingCube p;
    
        if (lst_moving_cubes_pool.isEmpty()) {
            p = new MovingCube();
        } else {
            p = lst_moving_cubes_pool.get( lst_moving_cubes_pool.size() - 1 );
            lst_moving_cubes_pool.remove(p);
        }    
        return p;
    }

    public static MoverCube getNewMoverCube() {
        MoverCube p;
    
        if (lst_mover_cubes_pool.isEmpty()) {
            p = new MoverCube();
        } else {    
            p = lst_mover_cubes_pool.get( lst_mover_cubes_pool.size() - 1 );
            lst_mover_cubes_pool.remove(p);
        }    
        return p;
    }

    public static DeadCube getNewDeadCube() {
        DeadCube p;
    
        if (lst_dead_cubes_pool.isEmpty()) {
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
            Game.setCubeTypeInvisible(cube.getCubePos());
            lst_moving_cubes_pool.add(cube);
        }
        lst_moving_cubes.clear();
    }

    public static void recycleMoverCubes() {
        //printf("\nRecycle Mover Cubes...");
        MoverCube cube;
        int len = lst_mover_cubes.size();
        for (int i = 0; i < len; ++i) {
            cube = lst_mover_cubes.get(i);
            Game.setCubeTypeInvisible( cube.getCubePos() );
            lst_mover_cubes_pool.add(cube);
        }
        lst_mover_cubes.clear();
    }

    public static void recycleDeadCubes() {
        //printf("\nRecycle Dead Cubes...");
        DeadCube cube;
        int len = lst_dead_cubes.size();
        for (int i = 0; i < len; ++i) {
            cube = lst_dead_cubes.get(i);
            Game.setCubeTypeInvisible( cube.getCubePos() );
            lst_dead_cubes_pool.add(cube);
        }
        lst_dead_cubes.clear();
    }

    public static void prepare() {
        recycleMovingCubes();
	    recycleMoverCubes();
	    recycleDeadCubes();
	    level.m_list_cubes_hint.clear();
    }

    public static void setup(int arr[]) {
        player.x = arr[0];
        player.y = arr[1];
        player.z = arr[2];
    
        key.x = arr[3];
        key.y = arr[4];
        key.z = arr[5];
    
        level.m_ad_level.clear();
    
        Color color = Game.getFaceColor(1f);
        Cube pCube;
	    int x, y, z;
        int size = arr.length;
        for (int i = 6; i < size; i += 3) {
            x = arr[i];
            y = arr[i+1];
            z = arr[i+2];
        
            pCube = Game.cubes[x][y][z];
        
            pCube.type = CubeTypeEnum.CubeIsVisibleAndObstacleAndLevel;
		    pCube.setColor(color);
        
            level.m_ad_level.addAppear(pCube);
        }
    }

    public static void setupSolution(int arr[]) {
        int i;
        int size = arr.length;
        for (i = 0; i < MAX_SOLUTION_MOVES; ++i) {
            level.m_ar_solution[i] = AxisMovement_No_Move;
        }
	
        for (i = 0; i < size; ++i) {
            level.m_ar_solution[i] = arr[i];
        }
	
	    level.m_min_solution_steps = size;
    }

    public static void setupMovingCubes(int arr[]) {
        MovingCube pMovingCube;
        int size = arr.length;
        for (int i = 0; i < size; i+=4) {
            pMovingCube = getNewMovingCube();
            pMovingCube.init(new CubePos(arr[i], arr[i+1], arr[i+2]), arr[i+3]);
            lst_moving_cubes.add(pMovingCube);
        }
    }

    public static void setupMoverCubes(int arr[]) {
	    MoverCube pMoverCube;
        int size = arr.length;
	    for (int i = 0; i < size; i+=4) {	
		    pMoverCube = getNewMoverCube();
		    pMoverCube.init(new CubePos(arr[i], arr[i+1], arr[i+2]), arr[i+3]);
            lst_mover_cubes.add(pMoverCube);
	    }
    }

    public static void setupDeathCubes(int arr[]) {
	    DeadCube pDeadCube;
        int size = arr.length;
	    for (int i = 0; i < size; i+=3) {
		    pDeadCube = getNewDeadCube();
		    pDeadCube.init(new CubePos(arr[i], arr[i+1], arr[i+2]));
            lst_dead_cubes.add(pDeadCube);
	    }
    }

    public static void setupHintCubes(int arr[]) {
	    Cube pCube;
        int size = arr.length;
	    for (int i = 0; i < size; i+=3) {
		    pCube = Game.cubes[arr[i]][arr[i+1]][arr[i+2]];
		    level.m_list_cubes_hint.add(pCube);
	    }
    }

}
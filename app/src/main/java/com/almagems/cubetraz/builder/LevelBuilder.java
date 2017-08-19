package com.almagems.cubetraz.builder;

import com.almagems.cubetraz.game.Game;
import com.almagems.cubetraz.cubes.Cube;
import com.almagems.cubetraz.cubes.CubePos;
import com.almagems.cubetraz.cubes.DeadCube;
import com.almagems.cubetraz.cubes.MoverCube;
import com.almagems.cubetraz.cubes.MovingCube;
import com.almagems.cubetraz.scenes.level.Level;

import java.util.ArrayList;

import static com.almagems.cubetraz.game.Game.*;

public final class LevelBuilder {
        
    public static Level level;
    public static final CubePos player = new CubePos();
    public static final CubePos key = new CubePos();

    public static final ArrayList<MovingCube> movingCubes = new ArrayList<>();
    public static final ArrayList<MoverCube> moverCubes = new ArrayList<>();
    public static final ArrayList<DeadCube> deadCubes = new ArrayList<>();
    
    private static final ArrayList<MovingCube> poolMovingCubes = new ArrayList<>();
    private static final ArrayList<MoverCube> poolMoverCubes = new ArrayList<>();
    private static final ArrayList<DeadCube> poolDeadCubes = new ArrayList<>();


    private static MovingCube getNewMovingCube() {
        MovingCube cube;
    
        if (poolMovingCubes.isEmpty()) {
            cube = new MovingCube();
        } else {
            cube = poolMovingCubes.get( poolMovingCubes.size() - 1 );
            poolMovingCubes.remove(cube);
        }    
        return cube;
    }

    private static MoverCube getNewMoverCube() {
        MoverCube cube;
    
        if (poolMoverCubes.isEmpty()) {
            cube = new MoverCube();
        } else {    
            cube = poolMoverCubes.get( poolMoverCubes.size() - 1 );
            poolMoverCubes.remove(cube);
        }    
        return cube;
    }

    private static DeadCube getNewDeadCube() {
        DeadCube cube;
    
        if (poolDeadCubes.isEmpty()) {
            cube = new DeadCube();
        } else {
            cube = poolDeadCubes.get( poolDeadCubes.size() - 1 );
            poolDeadCubes.remove(cube);
        }    
        return cube;
    }

    private static void recycleMovingCubes() {
        MovingCube cube;
        int len = movingCubes.size();
        for (int i = 0; i < len; ++i)  {
            cube = movingCubes.get(i);
            Game.setCubeTypeInvisible(cube.getCubePos());
            poolMovingCubes.add(cube);
        }
        movingCubes.clear();
    }

    private static void recycleMoverCubes() {
        MoverCube cube;
        int len = moverCubes.size();
        for (int i = 0; i < len; ++i) {
            cube = moverCubes.get(i);
            Game.setCubeTypeInvisible( cube.getCubePos() );
            poolMoverCubes.add(cube);
        }
        moverCubes.clear();
    }

    private static void recycleDeadCubes() {
        DeadCube cube;
        int len = deadCubes.size();
        for (int i = 0; i < len; ++i) {
            cube = deadCubes.get(i);
            Game.setCubeTypeInvisible( cube.getCubePos() );
            poolDeadCubes.add(cube);
        }
        deadCubes.clear();
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

        Cube cube;
	    int x, y, z;
        int size = arr.length;
        for (int i = 6; i < size; i += 3) {
            x = arr[i];
            y = arr[i+1];
            z = arr[i+2];
        
            cube = Game.cubes[x][y][z];
        
            cube.type = CubeTypeEnum.CubeIsVisibleAndObstacleAndLevel;
		    cube.setColor(Game.baseColor);
        
            level.m_ad_level.addAppear(cube);
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
        MovingCube cube;
        int x, y, z, moveDir;
        for (int i = 0; i < arr.length; i+=4) {
            x = arr[i];
            y = arr[i+1];
            z = arr[i+2];
            moveDir = arr[i+3];
            cube = getNewMovingCube();
            cube.init(new CubePos(x, y, z), moveDir);
            movingCubes.add(cube);
        }
    }

    public static void setupMoverCubes(int arr[]) {
	    MoverCube cube;
	    for (int i = 0; i < arr.length; i+=4) {
		    cube = getNewMoverCube();
		    cube.init(new CubePos(arr[i], arr[i+1], arr[i+2]), arr[i+3]);
            moverCubes.add(cube);
	    }
    }

    public static void setupDeathCubes(int arr[]) {
	    DeadCube cube;
	    for (int i = 0; i < arr.length; i+=3) {
		    cube = getNewDeadCube();
		    cube.init(new CubePos(arr[i], arr[i+1], arr[i+2]));
            deadCubes.add(cube);
	    }
    }

    public static void setupHintCubes(int arr[]) {
	    Cube cube;
	    for (int i = 0; i < arr.length; i+=3) {
		    cube = Game.cubes[arr[i]][arr[i+1]][arr[i+2]];
		    level.m_list_cubes_hint.add(cube);
	    }
    }

}
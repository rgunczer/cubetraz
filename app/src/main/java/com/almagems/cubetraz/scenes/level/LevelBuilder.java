package com.almagems.cubetraz.scenes.level;

import com.almagems.cubetraz.cubes.CubeLocation;
import com.almagems.cubetraz.Game;
import com.almagems.cubetraz.cubes.Cube;
import com.almagems.cubetraz.cubes.DeadCube;
import com.almagems.cubetraz.cubes.MoverCube;
import com.almagems.cubetraz.cubes.MovingCube;

import java.util.ArrayList;

import static com.almagems.cubetraz.Game.*;

public final class LevelBuilder {
        
    public static Level level;
    public static final CubeLocation locationPlayer = new CubeLocation();
    public static final CubeLocation locationKey = new CubeLocation();

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
            Game.setCubeTypeInvisible(cube.getLocation());
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
            Game.setCubeTypeInvisible( cube.getLocation() );
            poolDeadCubes.add(cube);
        }
        deadCubes.clear();
    }

    public static void prepare() {
        recycleMovingCubes();
	    recycleMoverCubes();
	    recycleDeadCubes();
	    level.cubesHint.clear();
    }

    public static void setup(int arr[]) {
        locationPlayer.x = arr[0];
        locationPlayer.y = arr[1];
        locationPlayer.z = arr[2];
    
        locationKey.x = arr[3];
        locationKey.y = arr[4];
        locationKey.z = arr[5];
    
        level.appearDisappear.level.clear();

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
        
            level.appearDisappear.level.addAppear(cube);
        }
    }

    static void setupSolution(int arr[]) {
        level.solution.init(arr);
    }

    static void setupMovingCubes(int arr[]) {
        MovingCube cube;
        int x, y, z, moveDir;
        for (int i = 0; i < arr.length; i+=4) {
            x = arr[i];
            y = arr[i+1];
            z = arr[i+2];
            moveDir = arr[i+3];
            cube = getNewMovingCube();
            cube.init(new CubeLocation(x, y, z), moveDir);
            movingCubes.add(cube);
        }
    }

    public static void setupMoverCubes(int arr[]) {
	    MoverCube cube;
	    for (int i = 0; i < arr.length; i+=4) {
		    cube = getNewMoverCube();
		    cube.init(new CubeLocation(arr[i], arr[i+1], arr[i+2]), arr[i+3]);
            moverCubes.add(cube);
	    }
    }

    public static void setupDeathCubes(int arr[]) {
	    DeadCube cube;
	    for (int i = 0; i < arr.length; i+=3) {
		    cube = getNewDeadCube();
		    cube.init(new CubeLocation(arr[i], arr[i+1], arr[i+2]));
            deadCubes.add(cube);
	    }
    }

    public static void setupHintCubes(int arr[]) {
	    Cube cube;
	    for (int i = 0; i < arr.length; i+=3) {
		    cube = Game.cubes[arr[i]][arr[i+1]][arr[i+2]];
		    level.cubesHint.add(cube);
	    }
    }

}
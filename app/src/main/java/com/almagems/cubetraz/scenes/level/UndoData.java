package com.almagems.cubetraz.scenes.level;

import com.almagems.cubetraz.cubes.CubeLocation;
import com.almagems.cubetraz.cubes.MoverCube;
import com.almagems.cubetraz.cubes.MovingCube;

class UndoData {

    MovingCube movingCube;
    MoverCube moverCube;

    CubeLocation playerLocation = new CubeLocation();
    CubeLocation movingCubeLocation = new CubeLocation();

    int movingCubeMoveDir;

    UndoData(CubeLocation playerLocation) {
        this.playerLocation.init(playerLocation);

        movingCube = null;
        movingCubeLocation.reset();

        moverCube = null;
    }
}

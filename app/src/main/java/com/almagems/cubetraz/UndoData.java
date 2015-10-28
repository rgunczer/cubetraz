package com.almagems.cubetraz;

import static com.almagems.cubetraz.Constants.*;


public class UndoData {

    CubePos player_pos;
    MovingCube moving_cube;
    CubePos moving_cube_pos;
    int moving_cube_move_dir;
    MoverCube mover_cube;

    // ctor
    public UndoData() {
        moving_cube = null;
        mover_cube = null;
    }

    public UndoData(CubePos player_pos) {
        this.player_pos = player_pos;

        moving_cube = new MovingCube();
        moving_cube_pos = new CubePos(0,0,0);

        mover_cube = new MoverCube();
    }

}

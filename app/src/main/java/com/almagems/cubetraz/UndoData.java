package com.almagems.cubetraz;

import static com.almagems.cubetraz.Constants.*;


public class UndoData {

    MovingCube moving_cube;
    MoverCube mover_cube;

    CubePos player_pos = new CubePos();
    CubePos moving_cube_pos = new CubePos();

    int moving_cube_move_dir;

    // ctor
    public UndoData() {
    }

    public UndoData(CubePos player_pos) {
        this.player_pos.init(player_pos);

        moving_cube = null;
        moving_cube_pos.reset();

        mover_cube = null;
    }

}

package com.almagems.cubetraz.scenes.level;

import com.almagems.cubetraz.cubes.CubePos;
import com.almagems.cubetraz.cubes.MoverCube;
import com.almagems.cubetraz.cubes.MovingCube;

public class UndoData {

    public MovingCube moving_cube;
    public MoverCube mover_cube;

    public CubePos player_pos = new CubePos();
    public CubePos moving_cube_pos = new CubePos();

    public int moving_cube_move_dir;

    public UndoData(CubePos player_pos) {
        this.player_pos.init(player_pos);

        moving_cube = null;
        moving_cube_pos.reset();

        mover_cube = null;
    }
}

package com.almagems.cubetraz;

import java.util.ArrayList;

public final class CubeFaceData {

    public ArrayList<LevelCube> lst_level_cubes;
    public ArrayList<LevelCube> lst_level_cubes_locked;
    public ArrayList<LevelCube> lst_level_cubes_solvered;

    public void clear() {
        lst_level_cubes.clear();
        lst_level_cubes_locked.clear();
        lst_level_cubes_solvered.clear();
    }

}

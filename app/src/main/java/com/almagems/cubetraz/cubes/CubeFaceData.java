package com.almagems.cubetraz.cubes;

import java.util.ArrayList;

public final class CubeFaceData {

    public ArrayList<LevelCube> lst_level_cubes = new ArrayList<>();
    public ArrayList<LevelCube> lst_level_cubes_locked = new ArrayList<>();
    public ArrayList<LevelCube> lst_level_cubes_solvered = new ArrayList<>();

    public void clear() {
        lst_level_cubes.clear();
        lst_level_cubes_locked.clear();
        lst_level_cubes_solvered.clear();
    }

}

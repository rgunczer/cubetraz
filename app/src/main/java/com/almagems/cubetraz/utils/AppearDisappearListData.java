package com.almagems.cubetraz.utils;

import com.almagems.cubetraz.cubes.Cube;

import java.util.ArrayList;
import static com.almagems.cubetraz.game.Game.*;

public final class AppearDisappearListData {

    public int level;
    public int direction;

    public final ArrayList<Cube> lst_appear = new ArrayList<>();
    public final ArrayList<Cube> lst_disappear = new ArrayList<>();

    public void clear() {
        lst_appear.clear();
        lst_disappear.clear();
    }

    public void setLevelAndDirection(int level, int direction) {
        this.level = level;
        this.direction = direction;
    }

    public void initAppearListFrom(ArrayList<Cube> lst) {
        lst_appear.clear();
        int size = lst.size();
        Cube cube;
        for (int i = 0; i < size; ++i) {
            cube = lst.get(i);
            addAppear(cube);
        }
    }

    public void initDisappearListFrom(ArrayList<Cube> lst) {
        lst_disappear.clear();
        int size = lst.size();
        Cube cube;
        for (int i = 0; i < size; ++i) {
            cube = lst.get(i);
            addDisappear(cube);
        }
    }

    public void addAppear(Cube theCube) {
        if (!lst_appear.contains(theCube)) {
            lst_appear.add(theCube);
        }
    }

    public void addDisappear(Cube theCube) {
        if (!lst_disappear.contains(theCube)) {
            lst_disappear.add(theCube);
        }
    }

    public Cube getCubeFromAppearList() {
        int size;
        Cube cube;

        while (level > -1 && level < MAX_CUBE_COUNT) {
            size = lst_appear.size();
            for(int i = 0; i < size; ++i) {
                cube = lst_appear.get(i);
                if (cube.y == level) {
                    lst_disappear.add(cube);
                    lst_appear.remove(cube);
                    return cube;
                }
            }
            level += direction;
        }
        return null;
    }

    public Cube getCubeFromDisappearList() {
        int size;
        Cube cube;

        while (level > -1 && level < MAX_CUBE_COUNT) {
            size = lst_disappear.size();
            for(int i = 0; i < size; ++i) {
                cube = lst_disappear.get(i);

                if (cube.y == level) {
                    lst_appear.add(cube);
                    lst_disappear.remove(cube);
                    return cube;
                }
            }
            level += direction;
        }
        return null;
    }

}

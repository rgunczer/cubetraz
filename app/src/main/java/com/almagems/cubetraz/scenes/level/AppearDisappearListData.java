package com.almagems.cubetraz.scenes.level;

import com.almagems.cubetraz.cubes.Cube;

import java.util.ArrayList;
import static com.almagems.cubetraz.Game.*;

public final class AppearDisappearListData {

    public int level;
    public int direction;

    public final ArrayList<Cube> appear = new ArrayList<>();
    public final ArrayList<Cube> disappear = new ArrayList<>();

    public void clear() {
        appear.clear();
        disappear.clear();
    }

    public void setLevelAndDirection(int level, int direction) {
        this.level = level;
        this.direction = direction;
    }

    public void initAppearListFrom(ArrayList<Cube> lst) {
        appear.clear();
        int size = lst.size();
        Cube cube;
        for (int i = 0; i < size; ++i) {
            cube = lst.get(i);
            addAppear(cube);
        }
    }

    public void initDisappearListFrom(ArrayList<Cube> lst) {
        disappear.clear();
        int size = lst.size();
        Cube cube;
        for (int i = 0; i < size; ++i) {
            cube = lst.get(i);
            addDisappear(cube);
        }
    }

    public void addAppear(Cube cube) {
        if (!appear.contains(cube)) {
            appear.add(cube);
        }
    }

    public void addDisappear(Cube cube) {
        if (!disappear.contains(cube)) {
            disappear.add(cube);
        }
    }

    public Cube getCubeFromAppearList() {
        int size;
        Cube cube;

        while (level > -1 && level < MAX_CUBE_COUNT) {
            size = appear.size();
            for(int i = 0; i < size; ++i) {
                cube = appear.get(i);
                if (cube.y == level) {
                    disappear.add(cube);
                    appear.remove(cube);
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
            size = disappear.size();
            for(int i = 0; i < size; ++i) {
                cube = disappear.get(i);

                if (cube.y == level) {
                    appear.add(cube);
                    disappear.remove(cube);
                    return cube;
                }
            }
            level += direction;
        }
        return null;
    }

}

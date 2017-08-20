package com.almagems.cubetraz.scenes.level;

import com.almagems.cubetraz.cubes.CubeLocation;
import com.almagems.cubetraz.game.Game;
import com.almagems.cubetraz.graphics.Color;
import com.almagems.cubetraz.scenes.menu.MenuCube;

import static com.almagems.cubetraz.game.Game.AxisMovement_X_Plus;
import static com.almagems.cubetraz.game.Game.AxisMovement_Z_Minus;
import static com.almagems.cubetraz.game.Game.HALF_CUBE_SIZE;

public class LevelMenu {

    MenuCube cubeUp = new MenuCube();
    MenuCube cubeMid = new MenuCube();
    MenuCube cubeLow = new MenuCube();

    public void init() {
        CubeLocation location = new CubeLocation(0, 0, 0);
        Color color = new Color();

        color.init(0, 0, 200, 255);
        cubeUp.init(location, color);

        color.init(0, 0, 150, 255);
        cubeMid.init(location, color);

        color.init(0, 0, 100, 255);
        cubeLow.init(location, color);
    }

    MenuCube getMenuCubeFromColor(int color) {
        switch (color) {
            case 200: return cubeUp;
            case 150: return cubeMid;
            case 100: return cubeLow;
        }
        return null;
    }

    void setupForAnimCompleted() {
        cubeUp.setCubePos(0, 5, 0);
        cubeMid.setCubePos(0, 3, 0);
        cubeLow.setCubePos(0, 1, 0);

        CubeLocation offset = new CubeLocation(0,0,1);
        cubeUp.setHiliteOffset(offset);
        cubeMid.setHiliteOffset(offset);
        cubeLow.setHiliteOffset(offset);

        cubeUp.moveOnAxis(AxisMovement_X_Plus);
        cubeMid.moveOnAxis(AxisMovement_X_Plus);
        cubeLow.moveOnAxis(AxisMovement_X_Plus);
    }

    void setupForAnimPaused() {
        cubeUp.setCubePos(0, 5, 8);
        cubeMid.setCubePos(0, 3, 8);
        cubeLow.setCubePos(0, 1, 8);

        CubeLocation offset = new CubeLocation(1, 0, 0);
        cubeUp.setHiliteOffset(offset);
        cubeMid.setHiliteOffset(offset);
        cubeLow.setHiliteOffset(offset);

        cubeUp.moveOnAxis(AxisMovement_Z_Minus);
        cubeMid.moveOnAxis(AxisMovement_Z_Minus);
        cubeLow.moveOnAxis(AxisMovement_Z_Minus);
    }

    void update() {
        cubeUp.update();
        cubeMid.update();
        cubeLow.update();
    }

    void draw() {
        Game.graphics.addCube(cubeUp.pos.x, cubeUp.pos.y, cubeUp.pos.z);
        Game.graphics.addCube(cubeMid.pos.x, cubeMid.pos.y, cubeMid.pos.z);
        Game.graphics.addCube(cubeLow.pos.x, cubeLow.pos.y, cubeLow.pos.z);
    }

    void drawForPicking() {
        Game.graphics.addCubeSize(cubeUp.pos.x, cubeUp.pos.y, cubeUp.pos.z, HALF_CUBE_SIZE * 1.5f, cubeUp.color);
        Game.graphics.addCubeSize(cubeMid.pos.x, cubeMid.pos.y, cubeMid.pos.z, HALF_CUBE_SIZE * 1.5f, cubeMid.color);
        Game.graphics.addCubeSize(cubeLow.pos.x, cubeLow.pos.y, cubeLow.pos.z, HALF_CUBE_SIZE * 1.5f, cubeLow.color);
    }

}

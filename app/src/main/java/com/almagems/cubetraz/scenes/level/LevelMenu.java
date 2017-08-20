package com.almagems.cubetraz.scenes.level;

import com.almagems.cubetraz.cubes.CubeFont;
import com.almagems.cubetraz.cubes.CubeLocation;
import com.almagems.cubetraz.Game;
import com.almagems.cubetraz.graphics.Color;
import com.almagems.cubetraz.scenes.menu.MenuCube;

import static com.almagems.cubetraz.Game.AxisMovement_X_Plus;
import static com.almagems.cubetraz.Game.AxisMovement_Z_Minus;
import static com.almagems.cubetraz.Game.HALF_CUBE_SIZE;

public class LevelMenu {

    private Color color = new Color();

    public CubeFont fontUp = new CubeFont();
    public CubeFont fontMid = new CubeFont();
    public CubeFont fontLow = new CubeFont();

    MenuCube cubeUp = new MenuCube();
    MenuCube cubeMid = new MenuCube();
    MenuCube cubeLow = new MenuCube();

    public void init() {
        CubeLocation location = new CubeLocation(0, 0, 0);

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

        fontUp.warmByFactor(60);
        fontMid.warmByFactor(60);
        fontLow.warmByFactor(60);
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

    void setFontColor(int r, int g, int b, int a) {
        color.init(r, g, b, a);

        fontUp.setColor(color);
        fontMid.setColor(color);
        fontLow.setColor(color);
    }

    void setFontColor(Color color) {
        setFontColor(color.r, color.g, color.b, color.a);
    }

}

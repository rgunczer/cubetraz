package com.almagems.cubetraz.scenes.level;

import com.almagems.cubetraz.cubes.CubeFont;
import com.almagems.cubetraz.cubes.CubeLocation;
import com.almagems.cubetraz.Game;
import com.almagems.cubetraz.graphics.Color;
import com.almagems.cubetraz.cubes.MenuCube;
import com.almagems.cubetraz.graphics.Graphics;

import java.util.ArrayList;

import static com.almagems.cubetraz.Game.*;

public class LevelMenu {

    private Color color = new Color();

    public CubeFont fontUp = new CubeFont();
    public CubeFont fontMid = new CubeFont();
    public CubeFont fontLow = new CubeFont();

    MenuCube cubeUp = new MenuCube();
    MenuCube cubeMid = new MenuCube();
    MenuCube cubeLow = new MenuCube();

    public ArrayList<MenuCube> menuCubes = new ArrayList<>(3);

    public LevelMenu() {
        menuCubes.add(cubeUp);
        menuCubes.add(cubeMid);
        menuCubes.add(cubeLow);
    }

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
        cubeUp.setCubeLocation(0, 5, 0);
        cubeMid.setCubeLocation(0, 3, 0);
        cubeLow.setCubeLocation(0, 1, 0);

        cubeUp.moveOnAxis(X_Plus);
        cubeMid.moveOnAxis(X_Plus);
        cubeLow.moveOnAxis(X_Plus);
    }

    void setupForAnimPaused() {
        cubeUp.setCubeLocation(0, 5, 8);
        cubeMid.setCubeLocation(0, 3, 8);
        cubeLow.setCubeLocation(0, 1, 8);

        cubeUp.moveOnAxis(Z_Minus);
        cubeMid.moveOnAxis(Z_Minus);
        cubeLow.moveOnAxis(Z_Minus);
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
        Graphics.addCube(cubeUp.pos.x, cubeUp.pos.y, cubeUp.pos.z);
        Graphics.addCube(cubeMid.pos.x, cubeMid.pos.y, cubeMid.pos.z);
        Graphics.addCube(cubeLow.pos.x, cubeLow.pos.y, cubeLow.pos.z);
    }

    void drawForPicking() {
        Graphics.addCubeSize(cubeUp.pos.x, cubeUp.pos.y, cubeUp.pos.z, HALF_CUBE_SIZE * 1.5f, cubeUp.color);
        Graphics.addCubeSize(cubeMid.pos.x, cubeMid.pos.y, cubeMid.pos.z, HALF_CUBE_SIZE * 1.5f, cubeMid.color);
        Graphics.addCubeSize(cubeLow.pos.x, cubeLow.pos.y, cubeLow.pos.z, HALF_CUBE_SIZE * 1.5f, cubeLow.color);
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

    void done() {
        cubeUp.cubesToHilite.clear();
        cubeMid.cubesToHilite.clear();
        cubeLow.cubesToHilite.clear();
    }

}

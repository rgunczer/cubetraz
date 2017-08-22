package com.almagems.cubetraz.cubes;

import com.almagems.cubetraz.Game;
import com.almagems.cubetraz.math.Vector;
import com.almagems.cubetraz.graphics.Color;
import com.almagems.cubetraz.graphics.TexturedQuad;

import static com.almagems.cubetraz.Game.*;

public final class LevelCube {
            
    public int levelNumber;
	public Color color;
    public Vector pos;
    
    public TexturedQuad pNumber;
    public TexturedQuad pStars;
    public TexturedQuad pSolver;

    public Color colorNumber;
    public Color colorStarsAndSolver;
    
    public Vector fontPos;
    
    public CubeLocation location;
	public CubeFaceNames faceName;
    public int faceType;


    public LevelCube() {
    }

    public void init(int levelNumber, int faceType, CubeFaceNames faceName, CubeLocation location) {
        this.faceType = faceType;
        this.faceName = faceName;
        this.color = new Color(255 - levelNumber, 0, 0, 255);
        this.location = location;
        this.levelNumber = levelNumber;
    
        pos = Game.getCubePosition(location);
    
        pNumber = Game.getNumberFont(levelNumber);
        pStars = Game.getSymbol(Symbol_Lock);
        pSolver = null;
    
        colorNumber = new Color(Game.levelNumberColor);
        colorStarsAndSolver = new Color(255, 255, 255, 180);
    
        fontPos = pos;
    }

    public void setStars(int starCount) {
        switch (starCount) {
            case -1: pStars = Game.getSymbol(Symbol_Lock); break;
            case 0: pStars = null; break;            
            case 1: pStars = Game.getSymbol(Symbol_1Star); break;
            case 2: pStars = Game.getSymbol(Symbol_2Star); break;
            case 3: pStars = Game.getSymbol(Symbol_3Star); break;
            default: pStars = null; break;
        }
    }

    public void setSolver(boolean solved) {
        if (solved) {
            pSolver = Game.getSymbol(Symbol_Solved);
        }
    }
 
}
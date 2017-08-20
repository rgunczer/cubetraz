package com.almagems.cubetraz.cubes;

import com.almagems.cubetraz.Game;
import com.almagems.cubetraz.math.Vector;
import com.almagems.cubetraz.graphics.Color;
import com.almagems.cubetraz.graphics.TexturedQuad;

import static com.almagems.cubetraz.Game.*;


public final class LevelCube {
            
    public int level_number;
	public Color color;
    public Vector pos;
    
    public TexturedQuad pNumber;
    public TexturedQuad pStars;
    public TexturedQuad pSolver;

    public Color color_number;
    public Color color_stars_and_solver;
    
    public Vector font_pos;
    
    public CubeLocation cube_pos;
	public CubeFaceNamesEnum face_id;
    public int face_type;


    public LevelCube() {
    }

    public void init(int level_number, int face_type, CubeFaceNamesEnum face_id, CubeLocation cube_pos) {
        this.face_type = face_type;
        this.face_id = face_id;
        this.color = new Color(255 - level_number, 0, 0, 255);
        this.cube_pos = cube_pos;
        this.level_number = level_number;
    
        pos = Game.getCubePosAt(cube_pos);
    
        pNumber = Game.getNumberFont(level_number);
        pStars = Game.getSymbol(Symbol_Lock);
        pSolver = null;
    
        color_number = new Color(Game.levelNumberColor);
        color_stars_and_solver = new Color(255, 255, 255, 180);
    
        font_pos = pos;
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
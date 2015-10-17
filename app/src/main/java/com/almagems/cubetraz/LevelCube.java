package com.almagems.cubetraz;

import static com.almagems.cubetraz.Constants.*;


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
    
    public CubePos cube_pos;
	public CubeFaceNamesEnum face_id;
    public int face_type;

    // ctor
    public LevelCube() {
    }

    public void init(int level_number, int face_type, CubeFaceNamesEnum face_id, CubePos cube_pos) {
        this.face_type = face_type;
        this.face_id = face_id;
        this.color = new Color(255 - level_number, 0, 0, 255);
        this.cube_pos = cube_pos;
        this.level_number = level_number;
    
        pos = Game.getCubePosAt(cube_pos.x, cube_pos.y, cube_pos.z);
    
        pNumber = Game.getNumberFont(level_number);
        pStars = Game.getSymbol(SymbolLock);
        pSolver = null;
    
        color_number = Game.getLevelNumberColor();
        color_stars_and_solver = new Color(255, 255, 255, 180);
    
        font_pos = pos;
    }

    public void setStars(int star_count) {
        switch (star_count) {
            case -1: pStars = Game.getSymbol(SymbolLock); break;
            case 0: pStars = null; break;            
            case 1: pStars = Game.getSymbol(Symbol1Star); break;            
            case 2: pStars = Game.getSymbol(Symbol2Star); break;            
            case 3: pStars = Game.getSymbol(Symbol3Star); break;
            default: pStars = null; break;
        }
    }

    public void setSolver(boolean solved) {
        if (solved) {
            pSolver = Game.getSymbol(SymbolSolved);
        }
    }
 
}
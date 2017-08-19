package com.almagems.cubetraz.game;

import java.io.Serializable;

import static com.almagems.cubetraz.game.Game.*;


public class LevelData implements Serializable {

    public int stars;
    public int moves;
    public boolean solved;

    public void reset() {
        stars = LEVEL_LOCKED;
        moves = 0;
        solved = false;
    }

}

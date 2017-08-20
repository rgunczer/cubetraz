package com.almagems.cubetraz.game;

import java.io.Serializable;

import static com.almagems.cubetraz.game.Game.*;


class LevelData implements Serializable {

    int stars;
    int moves;
    boolean solved;

    public void reset() {
        stars = LEVEL_LOCKED;
        moves = 0;
        solved = false;
    }

}

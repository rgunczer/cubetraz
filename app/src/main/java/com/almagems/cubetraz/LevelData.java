package com.almagems.cubetraz;

import java.io.Serializable;


class LevelData implements Serializable {

    int stars;
    int moves;
    boolean solved;

    public void reset() {
        stars = Game.LEVEL_LOCKED;
        moves = 0;
        solved = false;
    }

}

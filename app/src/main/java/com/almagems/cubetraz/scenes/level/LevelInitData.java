package com.almagems.cubetraz.scenes.level;

import static com.almagems.cubetraz.game.Game.*;


public final class LevelInitData {

    public int level_number;
    public DifficultyEnum difficulty;
    public LevelInitActionEnum init_action;

    // ctor
    public LevelInitData() {
        difficulty = DifficultyEnum.Easy;
        level_number = 13;
        init_action = LevelInitActionEnum.FullInit;
    }

}

package com.almagems.cubetraz;

import static com.almagems.cubetraz.Constants.*;


public final class LevelInitData {

    int level_number;
    DifficultyEnum difficulty;
    LevelInitActionEnum init_action;

    // ctor
    public LevelInitData() {
        difficulty = DifficultyEnum.Easy;
        level_number = 1;
        init_action = LevelInitActionEnum.FullInit;
    }

}

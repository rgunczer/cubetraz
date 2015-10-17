package com.almagems.cubetraz;

import static com.almagems.cubetraz.Constants.*;


public final class LevelInitData {

    public int level_number;
    public DifficultyEnum difficulty;
    public LevelInitActionEnum init_action;

    // ctor
    public LevelInitData() {
        difficulty = DifficultyEnum.Easy;
        level_number = 1;
        init_action = LevelInitActionEnum.FullInit;
    }

}

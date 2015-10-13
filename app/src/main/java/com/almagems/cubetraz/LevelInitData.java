package com.almagems.cubetraz;

public class LevelInitData {

    DifficultyEnum difficulty;
    int level_number;
    LevelInitActionEnum init_action;

    LevelInitData()
    {
        difficulty = Easy;
        level_number = 1;
        init_action = FullInit;
    }

}

package com.almagems.cubetraz.scenes.level;

import static com.almagems.cubetraz.game.Game.*;

public final class LevelInitData {

    public int levelNumber;
    public DifficultyEnum difficulty;
    public LevelInitActionEnum initAction;

    public LevelInitData() {
        difficulty = DifficultyEnum.Easy;
        levelNumber = 13;
        initAction = LevelInitActionEnum.FullInit;
    }

}

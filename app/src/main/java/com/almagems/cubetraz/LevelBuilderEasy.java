package com.almagems.cubetraz;

import java.util.ArrayList;

import static com.almagems.cubetraz.Constants.*;


public static final class LevelBuilderEasy {

    public static void build(int level_number) {
    
        LevelBuilder.prepare();

        switch (level_number) {

            case 1: {
                int[] level = new int[]{
                        5, 2, 8,
                        5, 2, 2,
                };
                LevelBuilder.setup(level);

                int[] hint = new int[]{
                        5, 1, 8,
                        5, 1, 7,
                        5, 1, 6,
                        5, 1, 5,
                        5, 1, 4,
                        5, 1, 3,
                        5, 1, 2,
                };
                LevelBuilder.setupHintCubes(hint);

                int[] solution = new int[]{
                        6,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

        }
    
    }
    
}

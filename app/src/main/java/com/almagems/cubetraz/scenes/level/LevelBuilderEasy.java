package com.almagems.cubetraz.scenes.level;

import static com.almagems.cubetraz.Game.*;


public final class LevelBuilderEasy {

    public static void build(int levelNumber) {
        LevelBuilder.prepare();

        switch (levelNumber) {
            case 1: {
                final int[] level = {
                        5, 2, 8,
                        5, 2, 2,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                        5, 1, 8,
                        5, 1, 7,
                        5, 1, 6,
                        5, 1, 5,
                        5, 1, 4,
                        5, 1, 3,
                        5, 1, 2,
                };
                LevelBuilder.setupHintCubes(hint);

                final int[] solution = {
                        6,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 2: {
                final int[] level = {
                        8, 2, 5,
                        2, 2, 5,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                        8, 1, 5,
                        7, 1, 5,
                        6, 1, 5,
                        5, 1, 5,
                        4, 1, 5,
                        3, 1, 5,
                        2, 1, 5,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                        4,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 3: {
                final int[] level = {
                        2, 2, 2,
                        2, 6, 2,
                };
                LevelBuilder.setup(level);

                final int[] hint  = {
                        2, 2, 1,
                        1, 2, 2,
                        1, 3, 2,
                        2, 3, 1,
                        2, 4, 1,
                        1, 4, 2,
                        1, 5, 2,
                        2, 5, 1,
                        2, 6, 1,
                        1, 6, 2,
                };
                LevelBuilder.setupHintCubes(hint);

                int[] solution = {
                        1,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 4: {
                final int[] level = {
                        5, 5, 5,
                        5, 2, 5,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                        2, 1, 5,
                        3, 1, 5,
                        4, 1, 5,
                        5, 1, 2,
                        5, 1, 3,
                        5, 1, 4,
                        8, 1, 5,
                        7, 1, 5,
                        6, 1, 5,
                        5, 1, 8,
                        5, 1, 7,
                        5, 1, 6,
                };
                LevelBuilder.setupHintCubes(hint);

                final int[] solution = {
                        2,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 5: {
                final int[] level = {
                        5, 2, 2,
                        5, 2, 8,
                        5, 2, 5,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                        5, 1, 2,
                        6, 1, 2,
                        7, 1, 2,
                        8, 1, 2,
                        8, 1, 3,
                        8, 1, 4,
                        8, 1, 5,
                        8, 1, 6,
                        8, 1, 7,
                        8, 1, 8,
                        7, 1, 8,
                        6, 1, 8,
                        5, 1, 8,
                };
                LevelBuilder.setupHintCubes(hint);

                final int[] solution = {
                        3,
                        5,
                        4,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 6: {
                final int level[] = {
                        7, 2, 7,
                        3, 2, 3,
                        2, 2, 2,
                        2, 2, 3,
                        2, 2, 4,
                        2, 2, 7,
                        2, 3, 2,
                        2, 4, 2,
                        3, 2, 2,
                        4, 2, 2,
                        6, 2, 8,
                        7, 2, 8,
                        8, 2, 6,
                        8, 2, 7,
                        8, 2, 8,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                        7, 1, 7,
                        6, 1, 7,
                        5, 1, 7,
                        4, 1, 7,
                        3, 1, 7,
                        3, 1, 6,
                        3, 1, 5,
                        3, 1, 4,
                        3, 1, 3,
                };
                LevelBuilder.setupHintCubes(hint);

                final int[] solution = {
                        4,
                        6,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 7: {
                final int level[] = {
                        7, 2, 2,
                        3, 4, 2,
                        2, 5, 2,
                        2, 6, 2,
                        3, 2, 2,
                        3, 3, 2,
                        4, 5, 2,
                        4, 6, 2,
                        6, 5, 2,
                        6, 6, 2,
                        7, 3, 2,
                        7, 4, 2,
                        8, 5, 2,
                        8, 6, 2,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                        7, 1, 2,
                        6, 1, 2,
                        5, 1, 2,
                        4, 1, 2,
                        4, 2, 1,
                        4, 3, 1,
                        4, 4, 1,
                        3, 4, 1,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                        4,
                        1,
                        4,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 8: {
                final int level[] = {
                        2, 3, 8,
                        8, 3, 8,
                        2, 4, 8,
                        3, 3, 8,
                        3, 4, 8,
                        7, 3, 8,
                        7, 4, 8,
                        8, 4, 8,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                        2, 1, 8,
                        3, 1, 8,
                        4, 1, 8,
                        5, 1, 8,
                        6, 1, 8,
                        7, 1, 8,
                        8, 1, 8,
                };
                LevelBuilder.setupHintCubes(hint);


                final int solution[] = {
                        2,
                        3,
                        1,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 9: {
                final int level[] = {
                        5, 3, 8,
                        5, 3, 2,
                        5, 2, 2,
                        5, 2, 8,
                        5, 3, 3,
                        5, 3, 4,
                        5, 3, 5,
                        5, 3, 6,
                        5, 3, 7,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                        1, 3, 8,
                        1, 3, 7,
                        1, 3, 6,
                        1, 3, 5,
                        1, 3, 4,
                        1, 3, 3,
                        1, 3, 2,
                        2, 3, 1,
                        3, 3, 1,
                        4, 3, 1,
                        5, 3, 1,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                        X_Minus,
                        Z_Minus,
                        X_Plus,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 10: {
                final int level[] = {
                        5, 2, 8,
                        5, 2, 2,
                };
                LevelBuilder.setup(level);

                final int moving[] = {
                        5, 2, 5, 1,
                };
                LevelBuilder.setupMovingCubes(moving);

                final int[] hint = {
                        5, 1, 8,
                        5, 1, 7,
                        5, 1, 6,
                        5, 1, 5,
                        5, 1, 4,
                        5, 1, 3,
                        5, 1, 2,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                        6,
                        6,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 11: {
                final int level[] = {
                        8, 3, 5,
                        4, 5, 2,
                        5, 2, 5,
                        8, 2, 5,
                };
                LevelBuilder.setup(level);

                final int moving[] = {
                        5, 3, 5, 1,
                };
                LevelBuilder.setupMovingCubes(moving);

                final int[] hint = {
                        7, 1, 5,
                        6, 1, 5,
                        4, 1, 5,
                        3, 1, 5,
                        2, 1, 5,
                        1, 2, 5,
                        1, 3, 5,
                        1, 4, 5,
                        1, 5, 5,
                        1, 6, 5,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                        4,
                        4,
                        1,
                        3,
                        6,
                        2,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 12: {
                final int level[] =
                        {
                                4, 2, 8,
                                2, 2, 4,
                                2, 2, 3,
                                2, 2, 5,
                                3, 2, 8,
                                8, 2, 3,
                                8, 2, 5,
                                8, 2, 8,
                        };
                LevelBuilder.setup(level);

                final int mover[] = {
                        4, 2, 3, 3,
                };
                LevelBuilder.setupMoverCubes(mover);

                final int[] hint = {
                        4, 1, 8,
                        4, 1, 7,
                        4, 1, 6,
                        4, 1, 5,
                        4, 1, 4,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                        6,
                        4,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

/*
        case 12:
        {
            final int level[] =
            {
                4, 2, 8,
                2, 2, 4,
                2, 2, 3,
                2, 2, 5,
                3, 2, 8,
                8, 2, 3,
                8, 2, 5,
                8, 2, 8,
            };
            LevelBuilder.setup(level);
            
            int moving[] =
            {
                7,6,4,	4,
            };
            LevelBuilder.setupMovingCubes();
            
            int mover[] =
            {
                4,2,3,	3,
                8,2,4,	1,
            };
            LevelBuilder.setupMoverCubes(mover);
            
            int hfinal int[] =
            {
                1,5,4,
                7,1,4,
                1,4,4,
                6,1,4,
                1,3,4,
                5,1,4,
                4,1,4,
                3,1,4,
            };
            LevelBuilder.setupHintCubes(hint);
            
            
            int solution[] =
            {
                6,
                4,
                2,
            };
            LevelBuilder.setupSolution(solution);
        }
            break;
*/
            case 13: {
                final int level[] = {
                        2, 6, 8,
                        7, 3, 2,
                };
                LevelBuilder.setup(level);

                final int mover[] = {
                        2, 2, 3, 5,
                        2, 3, 8, 3,
                        2, 6, 2, 2,
                        8, 3, 7, 6,
                };
                LevelBuilder.setupMoverCubes(mover);

                final int[] hint = {
                        1, 6, 8,
                        1, 6, 7,
                        1, 6, 6,
                        1, 6, 5,
                        1, 6, 4,
                        1, 6, 3,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                        6,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 14: {
                final int level[] = {
                        5, 2, 6,
                        5, 2, 4,
                        4, 2, 4,
                        4, 2, 5,
                        4, 2, 6,
                        5, 2, 5,
                        6, 2, 4,
                        6, 2, 5,
                        6, 2, 6,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                        5, 6, 1,
                        5, 5, 1,
                        5, 4, 1,
                        5, 3, 1,
                        5, 2, 1,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                        1,
                        6,
                        2,
                        5,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 15: {
                final int level[] = {
                        2, 3, 8,
                        8, 3, 2,
                        2, 2, 2,
                        2, 2, 3,
                        2, 2, 4,
                        2, 2, 5,
                        2, 2, 6,
                        2, 2, 7,
                        2, 2, 8,
                        2, 4, 2,
                        2, 4, 3,
                        2, 5, 2,
                        2, 6, 2,
                        3, 2, 2,
                        3, 2, 3,
                        3, 2, 8,
                        3, 3, 3,
                        3, 3, 8,
                        3, 4, 2,
                        3, 4, 3,
                        4, 2, 2,
                        5, 2, 2,
                        6, 2, 2,
                        7, 2, 2,
                        8, 2, 2,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                        1, 3, 5,
                        1, 3, 4,
                        1, 3, 3,
                        1, 3, 2,
                        2, 3, 1,
                        3, 3, 1,
                        4, 3, 1,
                        5, 3, 1,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                        6,
                        3,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 16: {
                final int level[] = {
                        5, 2, 8,
                        5, 2, 2,
                        2, 2, 2,
                        2, 2, 8,
                        4, 2, 2,
                        4, 2, 3,
                        5, 2, 3,
                        5, 3, 8,
                        5, 6, 2,
                        5, 6, 3,
                        6, 2, 2,
                        6, 2, 3,
                        8, 2, 2,
                        8, 2, 8,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                        5, 1, 8,
                        5, 1, 7,
                        5, 1, 6,
                        5, 1, 5,
                        5, 1, 4,
                        5, 2, 3,
                        5, 6, 3,
                        5, 1, 4,
                        5, 1, 5,
                        5, 1, 6,
                        5, 1, 7,
                        5, 3, 8,
                        5, 4, 1,
                        5, 3, 1,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                        6,
                        1,
                        5,
                        2,
                        6,
                        2,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 17: {
                final int level[] = {
                        8, 2, 8,
                        7, 2, 2,
                        2, 2, 2,
                        2, 2, 3,
                        2, 2, 4,
                        2, 2, 5,
                        2, 2, 6,
                        2, 2, 7,
                        2, 3, 2,
                        2, 4, 2,
                        2, 4, 3,
                        2, 4, 4,
                        2, 4, 5,
                        2, 4, 6,
                        2, 4, 7,
                        2, 4, 8,
                        3, 3, 2,
                        4, 3, 2,
                        5, 3, 2,
                        6, 3, 2,
                        7, 3, 2,
                        8, 2, 2,
                        8, 3, 2,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                        8, 1, 8,
                        8, 1, 7,
                        8, 1, 6,
                        8, 1, 3,
                        7, 1, 3,
                        6, 1, 3,
                        4, 1, 3,
                        3, 1, 3,
                        3, 1, 2,
                        4, 1, 2,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                        6,
                        4,
                        6,
                        3,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 18: {
                final int level[] = {
                        8, 2, 5,
                        2, 2, 5,
                        2, 2, 3,
                        2, 2, 7,
                        2, 4, 3,
                        2, 4, 7,
                        2, 6, 3,
                        2, 6, 7,
                        3, 2, 3,
                        3, 2, 7,
                        3, 4, 3,
                        3, 4, 7,
                        3, 6, 3,
                        3, 6, 7,
                        4, 2, 3,
                        4, 2, 4,
                        4, 2, 5,
                        4, 2, 6,
                        4, 2, 7,
                        4, 4, 3,
                        4, 4, 4,
                        4, 4, 5,
                        4, 4, 6,
                        4, 4, 7,
                        4, 6, 3,
                        4, 6, 4,
                        4, 6, 6,
                        4, 6, 7,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                        1, 6, 5,
                        1, 5, 5,
                        1, 4, 5,
                        1, 3, 5,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                        1,
                        4,
                        2,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 19: {
                final int level[] = {
                        2, 2, 8,
                        8, 2, 2,
                        2, 3, 8,
                        6, 2, 8,
                        7, 2, 7,
                        8, 2, 6,
                        8, 3, 2,
                };
                LevelBuilder.setup(level);

                final int dead[] = {
                        2, 2, 2,
                };
                LevelBuilder.setupDeathCubes(dead);

                final int[] hint = {
                        2, 1, 8,
                        3, 1, 8,
                        4, 1, 8,
                        5, 1, 8,
                        5, 1, 7,
                        5, 1, 6,
                        5, 1, 5,
                        5, 1, 4,
                        5, 1, 3,
                        5, 1, 2,
                        6, 1, 2,
                        7, 1, 2,
                        8, 1, 2,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                        3,
                        6,
                        3,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 20: {
                final int level[] = {
                        6, 6, 2,
                        2, 6, 6,
                        2, 2, 5,
                        2, 3, 5,
                        2, 4, 5,
                        2, 5, 5,
                        2, 6, 2,
                        2, 6, 3,
                        2, 6, 4,
                        2, 6, 5,
                        3, 2, 3,
                        3, 3, 3,
                        3, 4, 3,
                        3, 6, 2,
                        4, 2, 7,
                        4, 3, 7,
                        4, 4, 7,
                        4, 5, 7,
                        4, 6, 2,
                        5, 2, 2,
                        5, 3, 2,
                        5, 4, 2,
                        5, 5, 2,
                        5, 6, 2,
                        6, 2, 6,
                        6, 3, 6,
                        6, 4, 6,
                        7, 2, 4,
                        7, 2, 8,
                        7, 3, 4,
                        7, 4, 4,
                        7, 5, 4,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                        6, 1, 2,
                        6, 1, 3,
                        6, 1, 4,
                        6, 1, 5,
                        6, 1, 7,
                        6, 1, 8,
                        5, 1, 8,
                        4, 1, 8,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                        5,
                        4,
                        6,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 21: {
                final int level[] = {
                        4, 2, 7,
                        5, 3, 5,
                        4, 2, 6,
                        5, 2, 5,
                        5, 2, 8,
                        5, 4, 6,
                        6, 2, 6,
                        6, 2, 7,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                        5, 1, 7,
                        5, 1, 6,
                        5, 4, 6,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                        3,
                        6,
                        1,
                        6,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 22: {
                final int level[] = {
                        8, 2, 8,
                        2, 6, 2,
                        2, 3, 3,
                        2, 3, 5,
                        2, 3, 7,
                        2, 6, 4,
                        3, 2, 3,
                        3, 2, 5,
                        3, 2, 7,
                        3, 3, 2,
                        4, 6, 2,
                        5, 2, 3,
                        5, 3, 2,
                        7, 2, 3,
                        7, 3, 2,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                        2, 1, 8,
                        8, 1, 2,
                        2, 1, 7,
                        7, 1, 2,
                        2, 1, 5,
                        6, 1, 2,
                        1, 2, 2,
                        2, 2, 1,
                        1, 3, 2,
                        2, 3, 1,
                        1, 4, 2,
                        2, 4, 1,
                        1, 5, 2,
                        2, 5, 1,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                        4,
                        6,
                        1,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 23: {
                final int level[] = {
                        2, 2, 8,
                        3, 2, 2,
                        2, 2, 3,
                        2, 2, 7,
                        2, 3, 3,
                        2, 3, 5,
                        2, 3, 7,
                        2, 4, 3,
                        2, 4, 5,
                        2, 4, 7,
                        2, 5, 3,
                        2, 5, 5,
                        2, 5, 7,
                        2, 6, 5,
                        3, 2, 3,
                        3, 2, 4,
                        3, 2, 5,
                        3, 2, 6,
                        3, 2, 7,
                        3, 2, 8,
                        3, 6, 2,
                        3, 6, 3,
                        3, 6, 4,
                        3, 6, 5,
                        3, 6, 6,
                        3, 6, 7,
                        3, 6, 8,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                        1, 6, 8,
                        1, 6, 7,
                        1, 6, 6,
                        1, 2, 6,
                        1, 2, 5,
                        1, 2, 4,
                        1, 6, 4,
                        1, 6, 3,
                        1, 6, 2,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                        1,
                        6,
                        2,
                        6,
                        1,
                        6,
                        2,
                        3,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 24: {
                final int level[] = {
                        3, 3, 2,
                        2, 3, 3,
                        2, 2, 3,
                        2, 2, 5,
                        2, 2, 7,
                        2, 3, 2,
                        2, 3, 4,
                        2, 3, 6,
                        2, 3, 8,
                        2, 4, 3,
                        2, 4, 5,
                        2, 4, 7,
                        2, 5, 2,
                        2, 5, 4,
                        2, 5, 6,
                        2, 5, 8,
                        2, 6, 3,
                        2, 6, 5,
                        2, 6, 7,
                        3, 2, 2,
                        3, 2, 4,
                        3, 2, 6,
                        3, 2, 8,
                        3, 4, 2,
                        3, 6, 2,
                        4, 2, 3,
                        4, 2, 5,
                        4, 2, 7,
                        4, 3, 2,
                        4, 5, 2,
                        5, 2, 2,
                        5, 2, 4,
                        5, 2, 6,
                        5, 2, 8,
                        5, 4, 2,
                        5, 6, 2,
                        6, 2, 3,
                        6, 2, 5,
                        6, 2, 7,
                        6, 3, 2,
                        6, 5, 2,
                        7, 2, 2,
                        7, 2, 4,
                        7, 2, 6,
                        7, 2, 8,
                        7, 4, 2,
                        7, 6, 2,
                        8, 2, 3,
                        8, 2, 5,
                        8, 2, 7,
                        8, 3, 2,
                        8, 5, 2,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                        2, 3, 4,
                        2, 3, 6,
                        2, 3, 8,
                        8, 3, 2,
                        6, 3, 2,
                        4, 3, 2,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                        5,
                        3,
                        6,
                        4,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 25: {
                final int level[] = {
                        2, 6, 2,
                        8, 2, 8,
                        2, 2, 4,
                        2, 2, 6,
                        2, 2, 8,
                        2, 3, 6,
                        2, 3, 8,
                        2, 4, 8,
                        3, 2, 3,
                        4, 2, 2,
                        6, 2, 2,
                        6, 3, 2,
                        8, 2, 2,
                        8, 3, 2,
                        8, 4, 2,
                };
                LevelBuilder.setup(level);

                final int moving[] = {
                    2, 6, 4, 2,
                    2, 6, 6, 2,
                    2, 6, 8, 2,
                    4, 6, 2, 2,
                    6, 6, 2, 2,
                    8, 6, 2, 2,
                };
                LevelBuilder.setupMovingCubes(moving);

                final int[] hint = {
                    2, 6, 1,
                    1, 6, 2,
                    3, 6, 1,
                    1, 6, 3,
                    4, 6, 1,
                    1, 6, 4,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    3,
                    5,
                    3,
                    2,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 26: {
                final int level[] = {
                    6, 6, 8,
                    6, 5, 2,
                    2, 5, 8,
                    4, 6, 7,
                    5, 2, 8,
                    6, 5, 3,
                    7, 5, 4,
                    8, 2, 6,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                    6, 6, 1,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    6,
                    2,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 27: {
                final int level[] = {
                    5, 2, 5,
                    5, 2, 2,
                    5, 6, 2,
                };
                LevelBuilder.setup(level);

                final int dead[] = {
                    3, 2, 5,
                    5, 2, 3,
                    5, 2, 7,
                    7, 2, 5,
                };
                LevelBuilder.setupDeathCubes(dead);

                final int[] hint = {
                    5, 1, 4,
                    5, 1, 6,
                    4, 1, 5,
                    6, 1, 5,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    1,
                    3,
                    2,
                    6,
                    4,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 28: {
                final int level[] = {
                    7, 6, 2,
                    5, 4, 5,
                    2, 2, 4,
                    2, 6, 5,
                    3, 6, 5,
                    4, 6, 5,
                    5, 2, 4,
                    5, 2, 5,
                    5, 3, 5,
                    5, 6, 5,
                    8, 2, 7,
                };
                LevelBuilder.setup(level);

                final int moving[] = {
                    4, 2, 5, 1,
                    5, 2, 6, 1,
                    6, 2, 5, 1,
                };
                LevelBuilder.setupMovingCubes(moving);

                final int[] hint = {
                    7, 6, 1,
                    7, 5, 1,
                    7, 4, 1,
                    7, 1, 6,
                    7, 1, 7,
                    7, 1, 8,
                    6, 1, 8,
                    5, 1, 8,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    2,
                    5,
                    4,
                    6,
                    3,
                    3,
                    1,
                    3,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 29: {
                final int level[] = {
                    7, 5, 2,
                    8, 6, 8,
                    2, 2, 7,
                    3, 6, 3,
                    4, 3, 2,
                    4, 4, 8,
                    4, 6, 2,
                    4, 6, 7,
                    6, 6, 2,
                    6, 6, 4,
                    6, 6, 7,
                    7, 4, 7,
                    7, 5, 7,
                    8, 6, 7,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                    7, 5, 1,
                    8, 5, 1,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    3,
                    5,
                    1,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 30: {
                final int level[] = {
                    3, 3, 2,
                    7, 3, 2,
                    2, 2, 2,
                    2, 2, 8,
                    2, 3, 2,
                    2, 4, 2,
                    2, 4, 8,
                    3, 2, 2,
                    3, 2, 8,
                    3, 4, 2,
                    3, 4, 8,
                    4, 2, 2,
                    4, 2, 8,
                    4, 3, 2,
                    4, 3, 8,
                    4, 4, 2,
                    4, 4, 8,
                    6, 2, 2,
                    6, 2, 8,
                    6, 3, 2,
                    6, 3, 8,
                    6, 4, 2,
                    6, 4, 8,
                    7, 2, 2,
                    7, 2, 8,
                    7, 4, 2,
                    7, 4, 8,
                    8, 2, 2,
                    8, 2, 8,
                    8, 3, 2,
                    8, 4, 2,
                    8, 4, 8,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                    3, 2, 8,
                    2, 2, 8,
                    8, 2, 8,
                    7, 2, 8,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    5,
                    4,
                    6,
                    3,
                    5,
                    4,
                    6,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 31: {
                final int level[] = {
                    2, 2, 8,
                    3, 5, 2,
                    2, 2, 2,
                    2, 3, 2,
                    2, 4, 2,
                    2, 6, 8,
                    3, 4, 2,
                    3, 5, 7,
                    3, 6, 7,
                    4, 4, 2,
                    4, 5, 8,
                    4, 6, 8,
                    5, 4, 2,
                    6, 2, 4,
                    6, 4, 2,
                    7, 4, 2,
                    8, 4, 2,
                    8, 5, 2,
                };
                LevelBuilder.setup(level);

                final int moving[] = {
                    2, 2, 3, 1,
                };
                LevelBuilder.setupMovingCubes(moving);

                final int dead[] = {
                    2, 5, 2,
                    7, 6, 3,
                    8, 2, 8,
                    8, 5, 3,
                };
                LevelBuilder.setupDeathCubes(dead);

                final int[] hint = {
                    6, 2, 4,
                    5, 6, 1,
                    5, 5, 1,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    6,
                    3,
                    1,
                    6,
                    2,
                    4,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 32: {
                final int level[] = {
                    7, 2, 2,
                    2, 2, 3,
                    2, 2, 4,
                    2, 2, 6,
                    2, 2, 8,
                    2, 3, 3,
                    2, 3, 4,
                    2, 3, 6,
                    2, 3, 8,
                    2, 6, 4,
                    2, 6, 8,
                    3, 3, 2,
                    3, 6, 5,
                    3, 6, 7,
                    4, 2, 2,
                    4, 3, 2,
                    6, 2, 2,
                    6, 3, 2,
                    7, 3, 2,
                    8, 2, 2,
                    8, 3, 2,
                };
                LevelBuilder.setup(level);

                final int mover[] = {
                    6, 2, 5, 6,
                    7, 2, 8, 4,
                };
                LevelBuilder.setupMoverCubes(mover);

                final int[] hint = {
                    4, 1, 7,
                    3, 1, 7,
                    2, 1, 7,
                    5, 1, 7,
                    6, 1, 7,
                    7, 1, 7,
                    8, 2, 2,
                    5, 1, 3,
                    4, 1, 3,
                    3, 1, 3,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    5,
                    3,
                    6,
                    4,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 33: {
                final int level[] = {
                    8, 2, 8,
                    8, 2, 2,
                    2, 2, 5,
                    2, 3, 2,
                    2, 3, 4,
                    2, 3, 6,
                    2, 3, 8,
                    2, 6, 2,
                    2, 6, 4,
                    2, 6, 6,
                    2, 6, 8,
                    3, 2, 3,
                    3, 2, 5,
                    3, 2, 7,
                    4, 2, 3,
                    4, 2, 5,
                    4, 2, 7,
                    5, 2, 3,
                    5, 2, 5,
                    5, 2, 7,
                    6, 2, 3,
                    6, 2, 5,
                    6, 2, 7,
                    7, 2, 3,
                    7, 2, 5,
                    7, 2, 7,
                    8, 2, 3,
                    8, 2, 7,
                    8, 3, 4,
                    8, 3, 6,
                    8, 3, 8,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                    8, 1, 8,
                    7, 1, 8,
                    6, 1, 8,
                    5, 1, 8,
                    2, 1, 6,
                    3, 1, 6,
                    4, 1, 6,
                    8, 1, 4,
                    7, 1, 4,
                    6, 1, 4,
                    2, 1, 4,
                    2, 1, 3,
                    2, 1, 2,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    4,
                    6,
                    3,
                    6,
                    4,
                    6,
                    3,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 34: {
                final int level[] = {
                    4, 3, 7,
                    5, 6, 5,
                    2, 3, 5,
                    2, 5, 2,
                    2, 5, 6,
                    4, 2, 8,
                    4, 3, 3,
                    5, 6, 6,
                    6, 2, 5,
                    6, 3, 6,
                    6, 3, 7,
                    6, 6, 4,
                    8, 6, 3,
                    8, 6, 7,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                    5, 3, 1,
                    5, 4, 1,
                    5, 5, 1,
                    5, 6, 1,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    3,
                    6,
                    1,
                    5,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 35: {
                final int level[] = {
                    5, 6, 5,
                    5, 2, 5,
                    2, 2, 2,
                    2, 2, 8,
                    2, 3, 8,
                    4, 2, 4,
                    6, 2, 3,
                    7, 2, 7,
                    8, 2, 2,
                    8, 2, 8,
                };
                LevelBuilder.setup(level);

                final int mover[] = {
                    2, 2, 5, 1,
                    5, 2, 2, 1,
                    5, 2, 8, 1,
                    8, 2, 5, 1,
                };
                LevelBuilder.setupMoverCubes(mover);

                final int dead[] = {
                    5, 4, 5,
                };
                LevelBuilder.setupDeathCubes(dead);

                final int[] hint = {
                    4, 1, 7,
                    5, 1, 7,
                    6, 1, 7,
                    6, 1, 6,
                    6, 1, 5,
                    6, 1, 4,
                    5, 1, 4,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    4,
                    6,
                    2,
                    5,
                    2,
                    3,
                    6,
                    4,
                    5,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 36: {
                final int level[] = {
                    8, 3, 2,
                    2, 3, 6,
                    2, 2, 4,
                    2, 2, 5,
                    2, 2, 6,
                    2, 2, 7,
                    2, 2, 8,
                    2, 3, 5,
                    2, 3, 7,
                    2, 4, 6,
                    3, 2, 5,
                    3, 2, 6,
                    3, 2, 7,
                    4, 2, 6,
                    6, 2, 4,
                    7, 2, 3,
                    7, 2, 4,
                    7, 2, 5,
                    7, 3, 4,
                    8, 2, 2,
                    8, 2, 3,
                    8, 2, 4,
                    8, 2, 5,
                    8, 2, 6,
                    8, 3, 3,
                    8, 3, 4,
                    8, 3, 5,
                    8, 4, 4,
                };
                LevelBuilder.setup(level);

                final int moving[] = {
                    3, 3, 6, 1,
                };
                LevelBuilder.setupMovingCubes(moving);

                final int[] hint = {
                    3, 3, 1,
                    2, 3, 1,
                    1, 3, 2,
                    1, 3, 3,
                    2, 2, 8,
                    8, 2, 6,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    4,
                    1,
                    5,
                    2,
                    3,
                    6,
                    4,
                    4,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 37: {
                final int level[] = {
                    4, 5, 5,
                    6, 3, 5,
                    4, 4, 5,
                    5, 3, 2,
                    5, 3, 5,
                    5, 4, 4,
                    5, 4, 5,
                    5, 4, 6,
                    5, 5, 5,
                    6, 4, 5,
                    8, 4, 2,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                    5, 1, 2,
                    6, 1, 2,
                    7, 1, 2,
                    8, 1, 2,
                    8, 4, 2,
                    5, 3, 2,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    6,
                    2,
                    3,
                    1,
                    4,
                    5,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 38: {
                final int level[] = {
                    2, 2, 8,
                    6, 3, 2,
                    2, 6, 2,
                    3, 6, 2,
                    4, 6, 2,
                    5, 3, 2,
                    5, 4, 2,
                    5, 5, 2,
                    5, 6, 2,
                    6, 2, 2,
                    7, 3, 2,
                    8, 3, 2,
                };
                LevelBuilder.setup(level);

                final int moving[] = {
                    6, 2, 4, 1,
                };
                LevelBuilder.setupMovingCubes(moving);

                final int mover[] = {
                    7, 2, 8, 6,
                };
                LevelBuilder.setupMoverCubes(mover);

                final int dead[] = {
                    2, 6, 8,
                    6, 2, 3,
                };
                LevelBuilder.setupDeathCubes(dead);

                final int[] hint = {
                    1, 2, 8,
                    1, 2, 7,
                    1, 2, 6,
                    1, 2, 5,
                    1, 2, 4,
                    1, 2, 3,
                    1, 2, 2,
                    1, 3, 2,
                    1, 4, 2,
                    1, 5, 2,
                    1, 5, 3,
                    1, 5, 4,
                    1, 5, 5,
                    1, 5, 6,
                    1, 5, 7,
                    1, 5, 8,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    6,
                    1,
                    5,
                    3,
                    6,
                    4,
                    2,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 39: {
                final int level[] = {
                    6, 4, 3,
                    5, 6, 7,
                    3, 2, 2,
                    3, 4, 2,
                    3, 4, 6,
                    3, 4, 8,
                    3, 6, 2,
                    3, 6, 3,
                    4, 5, 4,
                    5, 3, 4,
                    5, 3, 8,
                    7, 5, 8,
                    7, 6, 6,
                    8, 6, 6,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                    6, 4, 1,
                    7, 4, 1,
                    8, 4, 1,
                    8, 1, 4,
                    8, 1, 5,
                    8, 1, 6,
                    8, 1, 7,
                    8, 6, 6,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    3,
                    5,
                    1,
                    6,
                    4,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 40: {
                final int level[] = {
                    2, 2, 8,
                    8, 5, 4,
                    2, 4, 3,
                    2, 4, 4,
                    2, 4, 5,
                    2, 4, 6,
                    2, 4, 7,
                    2, 4, 8,
                    8, 2, 4,
                    8, 3, 4,
                    8, 4, 4,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                    5, 1, 8,
                    6, 1, 8,
                    7, 1, 8,
                    2, 4, 8,
                    8, 1, 7,
                    8, 1, 6,
                    8, 1, 5,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    3,
                    1,
                    4,
                    2,
                    3,
                    6,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 41: {
                final int level[] = {
                    8, 4, 8,
                    5, 3, 2,
                    2, 4, 3,
                    2, 5, 6,
                    3, 4, 7,
                    4, 4, 5,
                    5, 3, 7,
                    7, 4, 5,
                    7, 5, 5,
                    8, 4, 4,
                    8, 4, 5,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                    2, 4, 3,
                    2, 1, 4,
                    3, 1, 4,
                    4, 1, 4,
                    8, 4, 4,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    4,
                    6,
                    2,
                    3,
                    1,
                    6,
                    4,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 42: {
                final int level[] = {
                    8, 3, 6,
                    5, 6, 2,
                    3, 3, 8,
                    3, 5, 2,
                    4, 3, 8,
                    5, 3, 6,
                    5, 4, 7,
                    6, 2, 8,
                    7, 2, 4,
                    7, 3, 6,
                    8, 2, 4,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                    8, 6, 1,
                    7, 6, 1,
                    6, 6, 1,
                    5, 6, 1,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    1,
                    6,
                    4,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 43: {
                final int level[] = {
                    5, 4, 5,
                    8, 6, 8,
                };
                LevelBuilder.setup(level);

                final int dead[] = {
                    2, 3, 5,
                    5, 2, 5,
                    5, 3, 2,
                    5, 3, 8,
                    5, 6, 5,
                    8, 3, 5,
                };
                LevelBuilder.setupDeathCubes(dead);

                final int[] hint = {
                    1, 4, 2,
                    2, 4, 1,
                    1, 4, 3,
                    3, 4, 1,
                    1, 4, 4,
                    4, 4, 1,
                    1, 4, 5,
                    5, 4, 1,
                    1, 4, 6,
                    6, 4, 1,
                    1, 4, 7,
                    7, 4, 1,
                    1, 4, 8,
                    8, 4, 1,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    3,
                    1,
                    5,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 44: {
                final int level[] = {
                    8, 5, 7,
                    2, 4, 2,
                    3, 3, 6,
                    3, 4, 8,
                    4, 3, 2,
                    4, 6, 5,
                    5, 6, 2,
                    6, 4, 6,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                    1, 4, 5,
                    1, 4, 4,
                    1, 4, 3,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    4,
                    6,
                    2,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 45: {
                final int level[] = {
                    2, 5, 5,
                    4, 4, 8,
                    2, 3, 5,
                    2, 4, 5,
                    3, 3, 5,
                    4, 2, 5,
                    4, 2, 8,
                    4, 3, 2,
                    4, 3, 3,
                    4, 3, 4,
                    4, 3, 5,
                    4, 3, 6,
                    4, 3, 7,
                    4, 3, 8,
                    5, 3, 2,
                    5, 3, 5,
                    6, 2, 2,
                    6, 3, 2,
                    6, 3, 5,
                    7, 2, 5,
                    7, 3, 5,
                    8, 2, 8,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                    8, 2, 8,
                    4, 3, 8,
                    5, 3, 2,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    3,
                    5,
                    2,
                    4,
                    1,
                    6,
                    2,
                    5,
                    4,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 46: {
                final int level[] = {
                    7, 3, 6,
                    8, 2, 4,
                    2, 2, 2,
                    3, 5, 3,
                    4, 2, 3,
                    4, 3, 3,
                    5, 5, 3,
                    6, 3, 5,
                    7, 2, 4,
                    7, 3, 5,
                    7, 3, 7,
                    7, 6, 8,
                    8, 2, 6,
                    8, 6, 6,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                    8, 3, 1,
                    8, 2, 1,
                    8, 1, 1,
                    8, 1, 2,
                    8, 1, 3,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    3,
                    6,
                    2,
                    5,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 47: {
                final int level[] = {
                    5, 2, 8,
                    7, 6, 4,
                    2, 6, 5,
                    3, 2, 6,
                    3, 6, 5,
                    5, 5, 2,
                    6, 5, 2,
                    7, 3, 2,
                    8, 5, 2,
                    8, 5, 3,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                    1, 2, 8,
                    1, 2, 7,
                    1, 2, 6,
                    1, 2, 5,
                    1, 2, 4,
                    1, 2, 3,
                    1, 2, 2,
                    1, 3, 2,
                    1, 4, 2,
                    1, 5, 2,
                    1, 6, 2,
                    1, 6, 3,
                    1, 6, 4,
                    1, 6, 5,
                    2, 6, 5,
                    3, 6, 5,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    4,
                    6,
                    1,
                    5,
                    3,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 48: {
                final int level[] = {
                    2, 6, 8,
                    6, 3, 2,
                    2, 4, 4,
                    2, 4, 8,
                    4, 3, 5,
                    4, 3, 6,
                    5, 2, 3,
                    5, 4, 2,
                    5, 5, 8,
                    6, 2, 2,
                    7, 3, 7,
                    7, 4, 3,
                    7, 6, 8,
                    8, 2, 2,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                    7, 6, 8,
                    6, 6, 1,
                    6, 5, 1,
                    6, 4, 1,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    3,
                    6,
                    2,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 49: {
                final int level[] = {
                    6, 4, 3,
                    5, 2, 5,
                    2, 4, 3,
                    3, 2, 8,
                    4, 3, 5,
                    4, 3, 6,
                    4, 4, 8,
                    4, 5, 5,
                    4, 5, 7,
                    7, 2, 8,
                    7, 4, 3,
                    8, 3, 5,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                    4, 4, 8,
                    5, 1, 8,
                    5, 1, 7,
                    5, 1, 6,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    5,
                    4,
                    2,
                    6,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 50: {
                final int level[] = {
                    2, 6, 8,
                    8, 2, 2,
                    2, 3, 7,
                    2, 5, 6,
                    3, 3, 7,
                    3, 5, 6,
                    4, 3, 7,
                    4, 5, 6,
                    5, 3, 7,
                    5, 5, 6,
                    5, 6, 2,
                    5, 6, 3,
                    5, 6, 4,
                    5, 6, 5,
                    5, 6, 6,
                    5, 6, 7,
                    5, 6, 8,
                    6, 3, 7,
                    6, 4, 2,
                    6, 4, 3,
                    6, 4, 4,
                    6, 4, 5,
                    6, 4, 6,
                    6, 4, 7,
                    6, 4, 8,
                    6, 5, 6,
                    7, 2, 2,
                    7, 2, 3,
                    7, 2, 4,
                    7, 2, 5,
                    7, 2, 6,
                    7, 2, 7,
                    7, 2, 8,
                    7, 3, 7,
                    7, 5, 6,
                    8, 3, 7,
                    8, 5, 6,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                    7, 2, 8,
                    6, 4, 8,
                    8, 1, 8,
                    8, 1, 7,
                    8, 1, 6,
                    8, 1, 5,
                    8, 1, 4,
                    8, 1, 3,
                    8, 1, 2,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    2,
                    3,
                    1,
                    3,
                    2,
                    6,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 51: {
                final int level[] = {
                    5, 3, 3,
                    5, 5, 7,
                    2, 3, 2,
                    2, 3, 3,
                    2, 4, 8,
                    4, 2, 5,
                    4, 2, 8,
                    5, 2, 3,
                    5, 2, 8,
                    5, 3, 2,
                    5, 3, 5,
                    5, 3, 6,
                    5, 3, 7,
                    5, 3, 8,
                    5, 4, 8,
                    5, 5, 8,
                    6, 2, 5,
                    8, 3, 2,
                    8, 3, 3,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                    5, 6, 1,
                    5, 5, 1,
                    5, 4, 1,
                    5, 3, 2,
                    5, 3, 5,
                    5, 3, 6,
                    5, 3, 7,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    1,
                    6,
                    2,
                    5,
                    1,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 52: {
                final int level[] = {
                    8, 5, 7,
                    2, 2, 3,
                    3, 6, 6,
                    4, 6, 3,
                    6, 2, 6,
                    6, 5, 7,
                    7, 4, 5,
                    7, 5, 5,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                    5, 1, 7,
                    4, 1, 7,
                    3, 1, 7,
                    2, 1, 7,
                    2, 1, 6,
                    2, 1, 5,
                    2, 1, 4,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    2,
                    4,
                    6,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 53: {
                final int level[] = {
                    2, 4, 5,
                    2, 5, 3,
                    4, 3, 8,
                    4, 5, 6,
                    5, 5, 3,
                    6, 2, 7,
                    8, 2, 3,
                    8, 4, 5,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                    1, 5, 5,
                    1, 6, 5,
                    8, 4, 5,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    1,
                    3,
                    2,
                    4,
                    6,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 54: {
                final int level[] = {
                    2, 5, 3,
                    2, 2, 2,
                    2, 3, 4,
                    2, 3, 5,
                    2, 3, 6,
                    2, 3, 7,
                    2, 3, 8,
                    2, 5, 4,
                    2, 5, 5,
                    2, 5, 6,
                    2, 5, 8,
                    3, 3, 2,
                    3, 3, 8,
                    3, 5, 8,
                    4, 3, 2,
                    4, 3, 8,
                    4, 5, 2,
                    4, 5, 8,
                    5, 3, 2,
                    5, 3, 8,
                    5, 5, 2,
                    5, 5, 8,
                    6, 3, 2,
                    6, 3, 8,
                    6, 5, 2,
                    6, 5, 8,
                    7, 3, 2,
                    7, 3, 8,
                    7, 5, 2,
                    8, 3, 2,
                    8, 3, 3,
                    8, 3, 4,
                    8, 3, 5,
                    8, 3, 6,
                    8, 3, 7,
                    8, 3, 8,
                    8, 4, 8,
                    8, 5, 2,
                    8, 5, 4,
                    8, 5, 5,
                    8, 5, 6,
                    8, 5, 7,
                    8, 5, 8,
                };
                LevelBuilder.setup(level);

                final int moving[] = {
                    2, 4, 2, 1,
                };
                LevelBuilder.setupMovingCubes(moving);

                final int mover[] = {
                    2, 3, 3, 5,
                    2, 4, 8, 3,
                    2, 5, 7, 6,
                    7, 5, 8, 4,
                    8, 5, 3, 5,
                };
                LevelBuilder.setupMoverCubes(mover);

                final int dead[] = {
                    3, 5, 2,
                };
                LevelBuilder.setupDeathCubes(dead);

                final int[] hint = {
                    2, 3, 6,
                    2, 3, 5,
                    2, 3, 4,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    2,
                    4,
                    6,
                    6,
                    2,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 55: {
                final int level[] = {
                    8, 2, 8,
                    8, 4, 2,
                    2, 2, 5,
                    2, 6, 8,
                    4, 5, 2,
                    4, 6, 2,
                    8, 2, 2,
                    8, 3, 2,
                };
                LevelBuilder.setup(level);

                final int moving[] = {
                    5, 2, 8, 6,
                    8, 2, 5, 4,
                };
                LevelBuilder.setupMovingCubes(moving);

                final int mover[] = {
                    2, 2, 8, 1,
                };
                LevelBuilder.setupMoverCubes(mover);

                final int dead[] = {
                    6, 2, 2,
                    8, 6, 2,
                };
                LevelBuilder.setupDeathCubes(dead);

                final int[] hint = {
                    2, 6, 8,
                    4, 1, 8,
                    4, 1, 7,
                    4, 1, 6,
                    4, 1, 5,
                    4, 1, 4,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    1,
                    4,
                    2,
                    3,
                    6,
                    1,
                    3,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 56: {
                final int level[] = {
                    8, 2, 8,
                    3, 5, 2,
                    2, 2, 2,
                    2, 4, 2,
                    2, 5, 2,
                    3, 2, 6,
                    3, 3, 6,
                    3, 4, 6,
                    5, 2, 5,
                    5, 2, 6,
                    6, 2, 5,
                    6, 2, 6,
                    7, 2, 2,
                    7, 2, 7,
                    7, 3, 2,
                    7, 5, 2,
                    7, 6, 2,
                    7, 6, 3,
                    8, 2, 2,
                    8, 6, 3,
                };
                LevelBuilder.setup(level);

                final int moving[] = {
                    5, 2, 8, 6,
                    8, 2, 5, 4,
                };
                LevelBuilder.setupMovingCubes(moving);

                final int mover[] = {
                    2, 3, 2, 1,
                    2, 5, 3, 5,
                };
                LevelBuilder.setupMoverCubes(mover);

                final int dead[] = {
                    2, 6, 2,
                    3, 5, 6,
                    8, 3, 2,
                };
                LevelBuilder.setupDeathCubes(dead);

                final int[] hint = {
                    6, 6, 1,
                    6, 5, 1,
                    6, 4, 1,
                    6, 3, 1,
                    6, 2, 1,
                    5, 2, 1,
                    4, 2, 1,
                    3, 2, 1,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    4,
                    1,
                    6,
                    2,
                    4,
                    1,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 57: {
                final int level[] = {
                    3, 6, 7,
                    6, 2, 4,
                    2, 2, 7,
                    2, 6, 4,
                    4, 6, 8,
                    5, 5, 5,
                    6, 2, 8,
                    6, 3, 5,
                    7, 3, 3,
                    7, 3, 7,
                    8, 2, 4,
                    8, 5, 8,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                    2, 2, 7,
                    7, 3, 7,
                    6, 1, 7,
                    6, 1, 6,
                    6, 1, 5,
                    6, 1, 4,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    4,
                    2,
                    3,
                    2,
                    6,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 58: {
                final int level[] = {
                    3, 6, 7,
                    6, 2, 4,
                    2, 2, 7,
                    2, 6, 4,
                    4, 6, 8,
                    5, 5, 5,
                    6, 2, 8,
                    6, 3, 5,
                    7, 3, 3,
                    7, 3, 7,
                    8, 2, 4,
                    8, 5, 8,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                    2, 2, 7,
                    7, 3, 7,
                    6, 1, 7,
                    6, 1, 6,
                    6, 1, 5,
                    6, 1, 4,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    4,
                    2,
                    3,
                    2,
                    6,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 59: {
                final int level[] = {
                    2, 6, 2,
                    3, 3, 8,
                    2, 2, 2,
                    2, 3, 2,
                    2, 4, 5,
                    2, 4, 8,
                    2, 5, 6,
                    2, 6, 3,
                    2, 6, 7,
                    3, 4, 3,
                    3, 4, 5,
                    4, 3, 8,
                    4, 4, 4,
                    4, 5, 2,
                    5, 2, 4,
                    5, 3, 5,
                    5, 4, 6,
                    6, 3, 8,
                    6, 4, 2,
                    7, 2, 3,
                    7, 3, 6,
                    8, 4, 7,
                };
                LevelBuilder.setup(level);

                final int[] hint = {
                    2, 6, 1,
                    3, 6, 1,
                    4, 6, 1,
                    5, 6, 1,
                    6, 6, 1,
                    7, 6, 1,
                    8, 6, 1,
                    8, 5, 1,
                    8, 4, 1,
                    8, 3, 1,
                    8, 2, 1,
                    7, 2, 1,
                    6, 2, 1,
                    5, 2, 1,
                    4, 2, 1,
                    3, 2, 1,
                    2, 2, 1,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    3,
                    2,
                    4,
                    5,
                    1,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 60: {
                final int level[] = {
                    8, 2, 8,
                    7, 6, 2,
                    2, 2, 3,
                    2, 3, 3,
                    2, 4, 3,
                    2, 5, 3,
                    3, 2, 3,
                    3, 3, 3,
                    3, 4, 3,
                    3, 5, 3,
                    3, 6, 3,
                    4, 2, 3,
                    4, 3, 3,
                    4, 4, 3,
                    4, 5, 3,
                    4, 6, 2,
                    4, 6, 3,
                    5, 2, 3,
                    5, 2, 6,
                    5, 3, 3,
                    5, 3, 4,
                    5, 3, 5,
                    5, 3, 6,
                    5, 3, 7,
                    5, 3, 8,
                    5, 4, 3,
                    5, 4, 4,
                    5, 4, 5,
                    5, 4, 6,
                    5, 4, 7,
                    5, 4, 8,
                    5, 5, 3,
                    5, 5, 4,
                    5, 5, 5,
                    5, 5, 6,
                    5, 5, 7,
                    5, 5, 8,
                    5, 6, 3,
                    5, 6, 4,
                    5, 6, 5,
                    5, 6, 6,
                    5, 6, 7,
                    5, 6, 8,
                    6, 3, 3,
                    6, 4, 3,
                    6, 5, 3,
                    6, 6, 3,
                    7, 2, 3,
                    7, 3, 3,
                    7, 4, 3,
                    7, 5, 3,
                    7, 6, 3,
                    8, 3, 3,
                    8, 4, 3,
                    8, 5, 3,
                    8, 6, 3,
                };
                LevelBuilder.setup(level);

                final int moving[] = {
                    6, 6, 4, 2,
                };
                LevelBuilder.setupMovingCubes(moving);

                final int mover[] = {
                    2, 2, 4, 5,
                    2, 2, 7, 3,
                    3, 2, 8, 3,
                    5, 2, 8, 1,
                    8, 2, 2, 1,
                    8, 2, 3, 4,
                };
                LevelBuilder.setupMoverCubes(mover);

                final int dead[] = {
                    6, 2, 3,
                };
                LevelBuilder.setupDeathCubes(dead);

                final int[] hint = {
                    6, 1, 5,
                    5, 1, 5,
                    1, 6, 4,
                    1, 6, 3,
                    1, 6, 2,
                };
                LevelBuilder.setupHintCubes(hint);

                final int solution[] = {
                    4,
                    6,
                    2,
                    4,
                    1,
                    6,
                    2,
                    3,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;
        }
    }
}

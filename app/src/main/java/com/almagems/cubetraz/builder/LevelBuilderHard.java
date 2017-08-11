package com.almagems.cubetraz.builder;


public final class LevelBuilderHard {

    public static void build(int levelNumber) {
        LevelBuilder.prepare();

        switch(levelNumber) {
            case 1: {
                int[] level = {
                    2, 2, 8,
                    2, 5, 8,
                    2, 2, 7,
                    2, 3, 7,
                    2, 3, 8,
                    2, 4, 7,
                    2, 5, 7,
                    2, 6, 7,
                    3, 2, 7,
                    3, 3, 7,
                    3, 4, 7,
                    3, 5, 7,
                    3, 6, 7,
                    4, 2, 7,
                    4, 3, 7,
                    4, 4, 7,
                    4, 5, 7,
                    4, 6, 7,
                    5, 2, 7,
                    5, 3, 7,
                    5, 4, 7,
                    5, 5, 7,
                    5, 6, 7,
                    6, 2, 7,
                    6, 3, 7,
                    6, 3, 8,
                    6, 4, 7,
                    6, 5, 7,
                    6, 6, 7,
                    7, 2, 2,
                    7, 2, 3,
                    7, 2, 4,
                    7, 2, 5,
                    7, 2, 6,
                    7, 2, 7,
                    7, 3, 2,
                    7, 3, 3,
                    7, 3, 4,
                    7, 3, 5,
                    7, 3, 6,
                    7, 3, 7,
                    7, 4, 2,
                    7, 4, 3,
                    7, 4, 4,
                    7, 4, 5,
                    7, 4, 6,
                    7, 4, 7,
                    7, 4, 8,
                    7, 5, 2,
                    7, 5, 3,
                    7, 5, 4,
                    7, 5, 5,
                    7, 5, 6,
                    7, 5, 7,
                    7, 6, 2,
                    7, 6, 3,
                    7, 6, 4,
                    7, 6, 5,
                    7, 6, 6,
                    7, 6, 7,
                    8, 2, 2,
                    8, 2, 8,
                    8, 4, 8,
                    8, 5, 2,
                };
                LevelBuilder.setup(level);

                int[] hint = {
                    7,4,7,
                    7,5,7,
                    7,6,7,
                };
                LevelBuilder.setupHintCubes(hint);

                int[] solution = {
                    3,
                    1,
                    3,
                    6,
                    1,
                    5,
                    1,
                    5,
                    4,
                    2,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 2: {
                int[] level = {
                    3, 4, 6,
                    8, 4, 2,
                    2, 2, 6,
                    2, 3, 6,
                    2, 4, 2,
                    2, 4, 3,
                    2, 4, 4,
                    2, 4, 5,
                    2, 4, 6,
                    3, 4, 2,
                    4, 4, 2,
                    5, 2, 4,
                    5, 3, 4,
                    5, 4, 2,
                    5, 4, 3,
                    5, 4, 4,
                    6, 3, 4,
                    6, 4, 2,
                    7, 3, 4,
                    7, 4, 2,
                    8, 3, 4,
                    8, 4, 4,
                    8, 5, 2,
                    8, 5, 3,
                    8, 5, 4,
                };
                LevelBuilder.setup(level);

                int[] hint = {
                    8,1,5,
                    8,1,4,
                    8,1,3,
                    8,1,2,
                };
                LevelBuilder.setupHintCubes(hint);

                int solution[] = {
                    3,
                    2,
                    6,
                    1,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 3: {
                int[] level = {
                    6, 3, 3,
                    2, 5, 6,
                    2, 2, 6,
                    2, 3, 6,
                    2, 4, 6,
                    2, 4, 8,
                    2, 5, 5,
                    3, 4, 4,
                    4, 5, 5,
                    5, 2, 8,
                    5, 4, 2,
                    5, 4, 3,
                    6, 2, 2,
                    6, 2, 3,
                    7, 4, 2,
                    7, 4, 3,
                };
                LevelBuilder.setup(level);

                int hint[] = {
                    2,4,8,
                };
                LevelBuilder.setupHintCubes(hint);

                int solution[] = {
                    4,
                    1,
                    5,
                    2,
                    6,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 4: {
                int[] level = {
                    2, 3, 6,
                    4, 6, 3,
                    2, 2, 2,
                    2, 2, 6,
                    2, 3, 2,
                    2, 4, 2,
                    2, 4, 6,
                    2, 5, 2,
                    2, 6, 2,
                    2, 6, 3,
                    3, 6, 2,
                    4, 2, 8,
                    4, 6, 2,
                    6, 2, 6,
                    6, 2, 8,
                    6, 3, 6,
                    6, 3, 8,
                    7, 2, 8,
                };
                LevelBuilder.setup(level);

                int hint[] = {
                    2,3,2,
                };
                LevelBuilder.setupHintCubes(hint);

                int solution[] = {
                    6,
                    3,
                    1,
                    4,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 5: {
                int[] level = {
                    7, 6, 3,
                    5, 2, 5,
                    2, 4, 5,
                    2, 4, 6,
                    2, 4, 7,
                    2, 5, 7,
                    2, 6, 7,
                    3, 4, 5,
                    4, 2, 5,
                    4, 3, 5,
                    4, 4, 5,
                    5, 2, 4,
                    5, 3, 4,
                    5, 4, 2,
                    5, 4, 3,
                    5, 4, 4,
                    6, 2, 8,
                    6, 4, 2,
                    7, 4, 2,
                    7, 5, 2,
                    7, 6, 2,
                    8, 2, 6,
                };
                LevelBuilder.setup(level);

                int hint[] = {
                    8,2,6,
                    6,2,8,
                };
                LevelBuilder.setupHintCubes(hint);

                int solution[] = {
                    3,
                    2,
                    5,
                    4,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 6: {
                int[] level = {
                    2, 4, 4,
                    8, 4, 6,
                    2, 2, 3,
                    2, 2, 5,
                    2, 4, 3,
                    2, 4, 5,
                    2, 6, 3,
                    2, 6, 4,
                    2, 6, 5,
                    3, 2, 4,
                    3, 4, 4,
                    5, 6, 3,
                    8, 2, 6,
                    8, 3, 6,
                    8, 6, 3,
                };
                LevelBuilder.setup(level);

                int hint[] = {
                    1,5,6,
                    1,5,7,
                    1,5,8,
                    1,6,8,
                    1,6,7,
                    1,6,6,
                };
                LevelBuilder.setupHintCubes(hint);

                int solution[] = {
                    1,
                    5,
                    1,
                    6,
                    3,
                    2,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 7: {
                int[] level = {
                    5, 6, 4,
                    5, 3, 6,
                    2, 5, 3,
                    3, 2, 4,
                    3, 4, 6,
                    3, 5, 7,
                    3, 6, 6,
                    4, 2, 6,
                    4, 3, 6,
                    4, 4, 5,
                    6, 3, 2,
                    6, 3, 6,
                    7, 2, 6,
                    7, 3, 6,
                    7, 6, 2,
                    7, 6, 3,
                    7, 6, 4,
                    7, 6, 5,
                    7, 6, 6,
                    7, 6, 7,
                    8, 5, 2,
                };
                LevelBuilder.setup(level);

                int hint[] = {
                    6,3,2,
                };
                LevelBuilder.setupHintCubes(hint);

                int solution[] = {
                    2,
                    4,
                    5,
                    1,
                    6,
                    3,
                    5,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 8: {
                int[] level = {
                    5, 5, 5,
                    6, 2, 7,
                    2, 6, 3,
                    3, 2, 4,
                    3, 3, 2,
                    3, 6, 3,
                    4, 5, 5,
                    4, 6, 3,
                    4, 6, 4,
                    5, 2, 7,
                    5, 3, 4,
                    5, 3, 7,
                    5, 4, 7,
                    5, 5, 4,
                    6, 2, 3,
                    6, 2, 8,
                    6, 4, 7,
                    8, 5, 5,
                };
                LevelBuilder.setup(level);

                int hint[] = {
                    8,5,5,
                    5,5,4,
                };
                LevelBuilder.setupHintCubes(hint);

                int solution[] = {
                    3,
                    6,
                    3,
                    5,
                    4,
                    2,
                    5,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 9: {
                int[] level = {
                    5, 5, 7,
                    6, 6, 3,
                    2, 2, 6,
                    3, 2, 5,
                    3, 3, 6,
                    3, 4, 4,
                    4, 2, 4,
                    4, 4, 2,
                    4, 4, 3,
                    4, 5, 2,
                    4, 5, 8,
                    4, 6, 2,
                    4, 6, 3,
                    5, 2, 3,
                    5, 2, 8,
                    5, 3, 8,
                    5, 4, 4,
                    6, 2, 2,
                    6, 3, 8,
                    6, 4, 5,
                    7, 2, 3,
                    7, 3, 8,
                    7, 4, 4,
                    8, 2, 4,
                    8, 4, 2,
                    8, 4, 3,
                    8, 5, 2,
                    8, 6, 2,
                    8, 6, 3,
                };
                LevelBuilder.setup(level);

                int hint[] = {
                    6,1,8,
                    6,1,7,
                    6,1,6,
                    6,1,5,
                    6,1,4,
                    6,1,3,
                };
                LevelBuilder.setupHintCubes(hint);

                int solution[] = {
                    3,
                    2,
                    5,
                    4,
                    6,
                    1,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 10: {
                int[] level = {
                    7, 4, 8,
                    4, 6, 4,
                    2, 6, 6,
                    3, 4, 2,
                    5, 2, 2,
                    5, 3, 2,
                    5, 4, 7,
                    8, 2, 7,
                };
                LevelBuilder.setup(level);

                int hint[] = {
                    4,4,1,
                    4,5,1,
                    4,6,1,
                };
                LevelBuilder.setupHintCubes(hint);

                int solution[] = {
                    6,
                    4,
                    1,
                    5,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 11: {
                int[] level = {
                    2, 5, 3,
                    5, 4, 3,
                    2, 2, 7,
                    2, 6, 7,
                    3, 4, 4,
                    4, 3, 4,
                    4, 5, 2,
                    4, 6, 4,
                    5, 2, 7,
                    5, 3, 2,
                    6, 4, 7,
                };
                LevelBuilder.setup(level);

                int hint[] = {
                    5,5,1,
                    5,4,1,
                };
                LevelBuilder.setupHintCubes(hint);

                int solution[] = {
                    3,
                    6,
                    4,
                    2,
                    5,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 12: {
                int[] level = {
                    2, 4, 4,
                    4, 4, 7,
                    2, 6, 4,
                    3, 5, 4,
                    3, 6, 4,
                    4, 2, 7,
                    4, 4, 8,
                    4, 5, 6,
                    4, 6, 2,
                    5, 3, 5,
                    5, 4, 8,
                    6, 3, 2,
                    8, 3, 8,
                };
                LevelBuilder.setup(level);

                int hint[] = {
                    3,6,4,
                    3,5,4,
                };
                LevelBuilder.setupHintCubes(hint);

                int solution[] = {
                    3,
                    1,
                    4,
                    2,
                    5,
                    1,
                    5,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 13: {
                int[] level = {
                    5, 6, 2,
                    5, 3, 4,
                    2, 4, 8,
                    2, 6, 4,
                    2, 6, 8,
                    3, 2, 4,
                    3, 4, 8,
                    3, 6, 4,
                    4, 3, 4,
                    4, 4, 8,
                    4, 6, 4,
                    5, 2, 8,
                    5, 4, 2,
                    5, 4, 3,
                    5, 4, 4,
                    5, 5, 4,
                    5, 6, 4,
                    6, 3, 4,
                    7, 2, 4,
                    8, 2, 8,
                    8, 3, 8,
                    8, 6, 2,
                };
                LevelBuilder.setup(level);

                int hint[] = {
                    8,6,2,
                    8,3,8,
                    4,4,8,
                    5,4,4,
                };
                LevelBuilder.setupHintCubes(hint);

                int solution[] = {
                    3,
                    5,
                    3,
                    2,
                    4,
                    2,
                    6,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 14: {
                int[] level = {
                    4, 4, 7,
                    4, 5, 3,
                    2, 2, 4,
                    2, 2, 8,
                    2, 3, 4,
                    2, 3, 8,
                    2, 4, 4,
                    2, 4, 5,
                    2, 4, 7,
                    2, 4, 8,
                    2, 5, 5,
                    2, 5, 7,
                    3, 3, 5,
                    3, 3, 6,
                    3, 3, 7,
                    3, 4, 5,
                    3, 4, 7,
                    4, 3, 6,
                    5, 3, 6,
                    6, 2, 6,
                    6, 3, 6,
                    7, 4, 6,
                    8, 4, 2,
                    8, 4, 3,
                    8, 4, 4,
                    8, 4, 5,
                    8, 4, 6,
                };
                LevelBuilder.setup(level);

                int hint[] = {
                    4,4,1,
                    3,4,1,
                    2,4,1,
                    1,4,2,
                    1,4,3,
                };
                LevelBuilder.setupHintCubes(hint);

                int solution[] = {
                    6,
                    4,
                    5,
                    1,
                    3,
                    2,
                    4,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 15:
            {
                int[] level =
                        {
                                2, 2, 8,
                                8, 2, 2,
                                2, 2, 3,
                                2, 4, 3,
                                2, 6, 2,
                                2, 6, 3,
                                2, 6, 6,
                                3, 3, 3,
                                3, 5, 3,
                                3, 6, 2,
                                3, 6, 3,
                                4, 2, 3,
                                4, 4, 3,
                                4, 4, 4,
                                4, 6, 2,
                                4, 6, 3,
                                5, 2, 5,
                                5, 3, 3,
                                5, 5, 3,
                                5, 6, 2,
                                5, 6, 3,
                                6, 2, 3,
                                6, 4, 3,
                                6, 6, 2,
                                6, 6, 3,
                                7, 3, 3,
                                7, 5, 3,
                                7, 6, 2,
                                7, 6, 3,
                                8, 2, 3,
                                8, 4, 3,
                                8, 6, 2,
                                8, 6, 3,
                        };
                LevelBuilder.setup(level);

                int hint[] =
                        {
                                5,2,5,
                                2,6,6,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                6,
                                1,
                                5,
                                2,
                                3,
                                6,
                                1,
                                3,
                                6,
                                2,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 16:
            {
                int[] level =
                        {
                                3, 4, 6,
                                3, 4, 4,
                                2, 4, 2,
                                2, 4, 3,
                                2, 4, 5,
                                2, 4, 6,
                                2, 4, 7,
                                3, 2, 3,
                                3, 2, 4,
                                3, 2, 5,
                                3, 2, 6,
                                3, 2, 7,
                                3, 2, 8,
                                3, 3, 3,
                                3, 3, 4,
                                3, 3, 5,
                                3, 3, 6,
                                3, 3, 7,
                                3, 3, 8,
                                3, 4, 3,
                                3, 4, 5,
                                3, 4, 7,
                                4, 2, 3,
                                4, 3, 3,
                                4, 4, 2,
                                4, 4, 3,
                                5, 2, 3,
                                5, 3, 3,
                                6, 2, 3,
                                6, 3, 3,
                                6, 4, 2,
                                6, 4, 3,
                                7, 2, 3,
                                7, 2, 5,
                                7, 3, 3,
                                7, 3, 5,
                                7, 3, 6,
                                7, 3, 7,
                                7, 3, 8,
                                7, 4, 5,
                                8, 2, 5,
                                8, 2, 8,
                                8, 3, 4,
                                8, 3, 5,
                                8, 3, 8,
                                8, 4, 2,
                                8, 4, 3,
                                8, 4, 5,
                        };
                LevelBuilder.setup(level);

                int hint[] =
                        {
                                8,1,6,
                                7,1,6,
                                6,1,6,
                                5,1,6,
                                4,1,6,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                3,
                                2,
                                4,
                                6,
                                1,
                                3,
                                2,
                                4,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 17:
            {
                int[] level =
                        {
                                2, 2, 7,
                                6, 3, 5,
                                2, 3, 5,
                                2, 3, 7,
                                2, 4, 8,
                                2, 5, 7,
                                2, 6, 3,
                                2, 6, 7,
                                3, 2, 8,
                                3, 4, 5,
                                3, 4, 7,
                                3, 5, 6,
                                4, 3, 4,
                                4, 3, 6,
                                4, 6, 2,
                                5, 2, 6,
                                5, 3, 5,
                                5, 4, 5,
                                5, 6, 2,
                                5, 6, 8,
                                6, 2, 8,
                                6, 4, 4,
                                6, 4, 8,
                                6, 5, 2,
                                6, 6, 4,
                                6, 6, 8,
                                7, 3, 2,
                                7, 3, 3,
                                7, 6, 6,
                                7, 6, 7,
                                8, 2, 6,
                                8, 6, 6,
                        };
                LevelBuilder.setup(level);

                int hint[] =
                        {
                                3,2,8,
                                7,3,2,
                                7,3,3,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                6,
                                1,
                                3,
                                5,
                                2,
                                6,
                                3,
                                5,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 18:
            {
                int[] level =
                        {
                                6, 5, 6,
                                4, 4, 5,
                                2, 5, 8,
                                2, 6, 2,
                                3, 3, 7,
                                4, 3, 5,
                                4, 3, 8,
                                4, 4, 3,
                                4, 6, 2,
                                5, 4, 3,
                                5, 6, 6,
                                5, 6, 7,
                                6, 2, 3,
                                6, 6, 8,
                        };
                LevelBuilder.setup(level);

                int hint[] =
                        {
                                1,5,6,
                                1,6,6,
                                4,3,8,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                4,
                                1,
                                3,
                                5,
                                2,
                                6,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 19:
            {
                int[] level =
                        {
                                6, 3, 5,
                                4, 4, 7,
                                2, 2, 3,
                                2, 2, 8,
                                3, 2, 2,
                                3, 3, 2,
                                6, 2, 2,
                                6, 2, 5,
                                6, 2, 8,
                                6, 6, 2,
                                7, 5, 2,
                        };
                LevelBuilder.setup(level);

                int moving[] = {
                    3,6,2,	2,
                };
                LevelBuilder.setupMovingCubes(moving);

                int mover[] = {
                    2,3,5,	5,
                    6,3,2,	1,
                    6,3,8,	2,
                    8,3,5,	6,
                };
                LevelBuilder.setupMoverCubes(mover);

                int dead[] = {
                    6,6,5,
                    8,2,5,
                };
                LevelBuilder.setupDeathCubes(dead);

                int hint[] = {
                    8,1,3,
                    8,1,2,
                    7,1,2,
                };
                LevelBuilder.setupHintCubes(hint);

                int solution[] = {
                    4,
                    1,
                    6,
                    3,
                    2,
                    6,
                    4,
                    1,
                    4,
                    5,
                };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 20:
            {
                int[] level =
                        {
                                7, 6, 6,
                                4, 5, 6,
                                2, 3, 6,
                                2, 3, 7,
                                2, 6, 4,
                                3, 4, 8,
                                3, 5, 5,
                                5, 3, 4,
                                5, 6, 6,
                                6, 6, 7,
                                7, 6, 8,
                                8, 3, 7,
                                8, 4, 3,
                                8, 5, 3,
                        };
                LevelBuilder.setup(level);

                int hint[] =
                        {
                                8,3,7,
                                8,4,3,
                                8,5,3,
                                2,6,4,
                                3,4,8,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                5,
                                3,
                                2,
                                6,
                                1,
                                4,
                                5,
                                2,
                                6,
                                3,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 21:
            {
                int[] level =
                        {
                                2, 4, 8,
                                6, 5, 2,
                                2, 5, 3,
                                3, 2, 6,
                                3, 4, 2,
                                3, 4, 6,
                        };
                LevelBuilder.setup(level);

                int moving[] =
                        {
                                4,4,2,	5,
                                8,2,7,	2,
                        };
                LevelBuilder.setupMovingCubes(moving);

                int mover[] =
                        {
                                2,4,5,	5,
                                6,4,5,	2,
                        };
                LevelBuilder.setupMoverCubes(mover);

                int hint[] =
                        {
                                8,4,1,
                                7,4,1,
                                6,4,1,
                                5,4,1,
                                4,4,1,
                                4,5,1,
                                4,6,1,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                3,
                                6,
                                4,
                                4,
                                1,
                                5,
                                2,
                                6,
                                3,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 22:
            {
                int[] level =
                        {
                                7, 4, 4,
                                3, 4, 5,
                        };
                LevelBuilder.setup(level);

                int moving[] =
                        {
                                2,3,2,	4,
                                2,3,8,	2,
                                3,6,2,	6,
                                6,4,2,	3,
                                6,5,2,	4,
                                7,3,5,	1,
                                7,3,8,	1,
                                7,4,5,	4,
                        };
                LevelBuilder.setupMovingCubes(moving);

                int hint[] =
                        {
                                2,4,1,
                                3,4,1,
                                4,4,1,
                                5,4,1,
                                8,4,1,
                                7,4,1,
                                6,4,1,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                4,
                                6,
                                3,
                                5,
                                3,
                                6,
                                6,
                                4,
                                5,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 23:
            {
                int[] level =
                        {
                                6, 6, 8,
                                5, 5, 3,
                                2, 3, 7,
                                2, 6, 2,
                                2, 6, 5,
                                3, 3, 2,
                                3, 6, 7,
                                4, 5, 3,
                                4, 5, 8,
                                4, 6, 6,
                                5, 2, 4,
                                5, 3, 7,
                                5, 5, 5,
                                5, 6, 6,
                                6, 6, 5,
                                6, 6, 7,
                                7, 2, 7,
                                7, 4, 3,
                                8, 2, 3,
                                8, 2, 7,
                                8, 3, 3,
                                8, 4, 4,
                                8, 5, 5,
                                8, 6, 3,
                                8, 6, 4,
                                8, 6, 6,
                        };
                LevelBuilder.setup(level);

                int moving[] =
                        {
                                3,4,7,	2,
                                6,2,7,	1,
                                6,4,2,	1,
                                8,3,7,	2,
                        };
                LevelBuilder.setupMovingCubes(moving);

                int hint[] =
                        {
                                8,6,6,
                                5,1,2,
                                5,1,3,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                3,
                                6,
                                2,
                                4,
                                6,
                                3,
                                2,
                                5,
                                1,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 24:
            {
                int[] level =
                        {
                                4, 6, 7,
                                6, 3, 8,
                                2, 3, 8,
                                3, 3, 8,
                                3, 4, 5,
                                3, 5, 6,
                                3, 5, 8,
                                3, 6, 7,
                                4, 5, 4,
                                5, 3, 6,
                                5, 3, 8,
                                6, 3, 5,
                                8, 3, 4,
                                8, 5, 2,
                        };
                LevelBuilder.setup(level);

                int moving[] =
                        {
                                4,6,4,	2,
                                5,2,3,	1,
                        };
                LevelBuilder.setupMovingCubes(moving);

                int hint[] =
                        {
                                4,2,1,
                                4,3,1,
                                4,4,1,
                                4,5,1,
                                4,6,1,
                                8,1,3,
                                7,1,3,
                                6,1,3,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                2,
                                6,
                                1,
                                5,
                                3,
                                2,
                                4,
                                5,
                                1,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 25:
            {
                int[] level =
                        {
                                5, 6, 6,
                                8, 5, 5,
                                2, 5, 2,
                                2, 5, 8,
                                2, 6, 8,
                                3, 5, 6,
                                3, 5, 8,
                                3, 6, 6,
                                4, 4, 6,
                                4, 6, 5,
                                4, 6, 6,
                                5, 3, 3,
                                7, 2, 3,
                                7, 3, 8,
                        };
                LevelBuilder.setup(level);

                int hint[] =
                        {
                                2,6,8,
                                3,6,6,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                5,
                                4,
                                6,
                                2,
                                6,
                                1,
                                5,
                                2,
                                3,
                                1,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 26:
            {
                int[] level =
                        {
                                6, 2, 6,
                                4, 6, 7,
                                2, 3, 3,
                                2, 4, 8,
                                4, 3, 5,
                                5, 2, 3,
                                6, 2, 4,
                                6, 4, 7,
                                6, 5, 7,
                                6, 6, 4,
                                7, 3, 3,
                                8, 3, 6,
                                8, 5, 2,
                                8, 6, 5,
                        };
                LevelBuilder.setup(level);

                int hint[] =
                        {
                                5,4,1,
                                4,4,1,
                                3,4,1,
                                2,4,1,
                                1,4,2,
                                1,4,3,
                                1,4,4,
                                1,4,5,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                3,
                                6,
                                1,
                                4,
                                5,
                                1,
                                3,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 27:
            {
                int[] level =
                        {
                                3, 2, 3,
                                7, 4, 5,
                                2, 2, 2,
                                2, 2, 3,
                                2, 2, 4,
                                2, 3, 3,
                                2, 4, 2,
                                2, 4, 3,
                                2, 4, 4,
                                3, 2, 2,
                                3, 2, 4,
                                3, 4, 2,
                                3, 4, 4,
                                4, 2, 2,
                                4, 2, 3,
                                4, 2, 4,
                                4, 3, 3,
                                4, 4, 2,
                                4, 4, 3,
                                4, 4, 4,
                                6, 2, 4,
                                6, 2, 5,
                                6, 2, 6,
                                6, 4, 4,
                                6, 4, 6,
                                6, 5, 8,
                                7, 2, 4,
                                7, 2, 6,
                                7, 3, 6,
                                7, 4, 4,
                                7, 4, 6,
                                8, 2, 4,
                                8, 2, 5,
                                8, 2, 6,
                                8, 4, 4,
                                8, 4, 5,
                                8, 4, 6,
                        };
                LevelBuilder.setup(level);

                int moving[] =
                        {
                                5,5,4,	2,
                                5,5,5,	2,
                                5,5,6,	2,
                                5,6,4,	2,
                                5,6,5,	2,
                                5,6,6,	2,
                        };
                LevelBuilder.setupMovingCubes(moving);

                int mover[] =
                        {
                                3,6,3,	5,
                        };
                LevelBuilder.setupMoverCubes(mover);

                int dead[] =
                        {
                                4,2,5,
                                5,2,8,
                        };
                LevelBuilder.setupDeathCubes(dead);

                int hint[] =
                        {
                                5,6,6,
                                5,5,6,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                1,
                                3,
                                6,
                                1,
                                5,
                                6,
                                6,
                                2,
                                4,
                                6,
                                3,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 28:
            {
                int[] level =
                        {
                                4, 6, 2,
                                2, 5, 3,
                                2, 5, 4,
                                3, 6, 6,
                                4, 2, 4,
                                4, 3, 4,
                                4, 4, 3,
                                5, 3, 7,
                                5, 4, 4,
                                5, 5, 7,
                                7, 2, 3,
                                7, 3, 6,
                                7, 4, 3,
                                8, 5, 7,
                        };
                LevelBuilder.setup(level);

                int moving[] =
                        {
                                2,6,5,	6,
                                2,6,7,	6,
                                4,2,3,	5,
                        };
                LevelBuilder.setupMovingCubes(moving);

                int hint[] =
                        {
                                2,6,5,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                5,
                                2,
                                6,
                                4,
                                1,
                                1,
                                6,
                                2,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 29:
            {
                int[] level =
                        {
                                5, 6, 6,
                                6, 5, 5,
                                3, 4, 2,
                                3, 5, 3,
                                3, 5, 6,
                                3, 6, 3,
                                4, 5, 3,
                                5, 2, 8,
                                5, 4, 4,
                                7, 5, 8,
                                7, 6, 6,
                                8, 4, 7,
                        };
                LevelBuilder.setup(level);

                int moving[] =
                        {
                                4,5,7,	1,
                                8,3,3,	3,
                        };
                LevelBuilder.setupMovingCubes(moving);

                int hint[] =
                        {
                                8,4,7,
                                7,5,8,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                2,
                                5,
                                1,
                                3,
                                2,
                                4,
                                5,
                                3,
                                6,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 30:
            {
                int[] level =
                        {
                                8, 2, 8,
                                8, 5, 2,
                                2, 3, 2,
                                2, 3, 3,
                                2, 3, 4,
                                2, 3, 5,
                                2, 3, 6,
                                2, 3, 7,
                                2, 3, 8,
                                3, 3, 2,
                                3, 3, 4,
                                3, 3, 5,
                                3, 3, 6,
                                3, 3, 8,
                                4, 3, 2,
                                4, 3, 3,
                                4, 3, 4,
                                4, 3, 5,
                                4, 3, 6,
                                4, 3, 7,
                                4, 3, 8,
                                5, 3, 2,
                                5, 3, 3,
                                5, 3, 5,
                                5, 3, 6,
                                5, 3, 7,
                                5, 3, 8,
                                6, 2, 2,
                                6, 2, 8,
                                6, 3, 2,
                                6, 3, 3,
                                6, 3, 4,
                                6, 3, 5,
                                6, 3, 6,
                                6, 3, 7,
                                6, 3, 8,
                                7, 3, 2,
                                7, 3, 8,
                                8, 2, 2,
                                8, 3, 2,
                                8, 3, 8,
                                8, 6, 2,
                        };
                LevelBuilder.setup(level);

                int moving[] =
                        {
                                5,6,3,	3,
                        };
                LevelBuilder.setupMovingCubes(moving);

                int mover[] =
                        {
                                2,2,7,	1,
                                3,6,2,	2,
                                6,4,7,	4,
                                6,6,7,	4,
                                6,6,8,	4,
                                8,2,6,	4,
                        };
                LevelBuilder.setupMoverCubes(mover);

                int dead[] =
                        {
                                3,2,3,
                                5,4,2,
                                8,4,8,
                        };
                LevelBuilder.setupDeathCubes(dead);

                int hint[] =
                        {
                                5,6,3,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                6,
                                2,
                                3,
                                1,
                                6,
                                4,
                                2,
                                3,
                                6,
                                1,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 31:
            {
                int[] level =
                        {
                                7, 2, 4,
                                5, 5, 4,
                                2, 6, 6,
                                3, 3, 3,
                                4, 2, 3,
                                4, 4, 7,
                                5, 3, 2,
                                5, 5, 8,
                                6, 3, 4,
                                7, 2, 8,
                        };
                LevelBuilder.setup(level);

                int moving[] =
                        {
                                2,2,4,	6,
                                6,2,4,	1,
                        };
                LevelBuilder.setupMovingCubes(moving);

                int mover[] =
                        {
                                7,3,4,	5,
                        };
                LevelBuilder.setupMoverCubes(mover);

                int dead[] =
                        {
                                3,5,4,
                                4,3,6,
                                5,6,3,
                        };
                LevelBuilder.setupDeathCubes(dead);

                int hint[] =
                        {
                                6,3,4,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                3,
                                1,
                                4,
                                2,
                                3,
                                1,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 32:
            {
                int[] level =
                        {
                                4, 4, 4,
                                7, 5, 2,
                                2, 2, 2,
                                2, 4, 6,
                                5, 5, 7,
                                6, 3, 7,
                                6, 4, 2,
                                7, 3, 8,
                                7, 4, 4,
                                8, 2, 6,
                        };
                LevelBuilder.setup(level);

                int moving[] =
                        {
                                4,6,7,	6,
                                8,4,8,	6,
                        };
                LevelBuilder.setupMovingCubes(moving);

                int mover[] =
                        {
                                4,2,5,	6,
                        };
                LevelBuilder.setupMoverCubes(mover);

                int dead[] =
                        {
                                2,5,3,
                                4,4,3,
                                7,2,5,
                        };
                LevelBuilder.setupDeathCubes(dead);

                int hint[] =
                        {
                                7,4,4,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                3,
                                1,
                                6,
                                2,
                                3,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 33:
            {
                int[] level =
                        {
                                2, 6, 2,
                                4, 3, 3,
                                2, 2, 2,
                                2, 2, 3,
                                2, 2, 4,
                                2, 5, 2,
                                3, 2, 2,
                                3, 2, 3,
                                3, 2, 4,
                                3, 2, 8,
                                3, 3, 2,
                                3, 3, 3,
                                3, 3, 4,
                                3, 3, 5,
                                3, 4, 2,
                                3, 4, 3,
                                3, 4, 4,
                                3, 5, 2,
                                4, 2, 2,
                                4, 2, 3,
                                4, 2, 4,
                                4, 3, 2,
                                4, 3, 5,
                                4, 4, 2,
                                4, 4, 4,
                                4, 5, 2,
                                4, 6, 2,
                                5, 2, 2,
                                5, 2, 3,
                                5, 2, 4,
                                5, 3, 2,
                                5, 3, 3,
                                5, 3, 5,
                                7, 2, 3,
                                8, 2, 8,
                                8, 3, 8,
                        };
                LevelBuilder.setup(level);

                int dead[] =
                        {
                                7,3,3,
                        };
                LevelBuilder.setupDeathCubes(dead);

                int hint[] =
                        {
                                4,6,2,
                                3,2,8,
                                8,3,8,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                3,
                                5,
                                2,
                                3,
                                2,
                                4,
                                1,
                                6,
                                2,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 34:
            {
                int[] level =
                        {
                                2, 6, 2,
                                4, 4, 4,
                                2, 4, 3,
                                2, 4, 5,
                                2, 4, 7,
                                3, 2, 3,
                                3, 2, 5,
                                3, 2, 7,
                                3, 3, 3,
                                3, 3, 5,
                                3, 3, 7,
                                3, 4, 2,
                                3, 4, 3,
                                3, 4, 5,
                                3, 4, 7,
                                5, 2, 3,
                                5, 3, 3,
                                5, 4, 2,
                                5, 4, 3,
                                7, 2, 3,
                                7, 3, 3,
                                7, 4, 2,
                                7, 4, 3,
                                7, 5, 4,
                                8, 3, 8,
                        };
                LevelBuilder.setup(level);

                int moving[] =
                        {
                                2,5,6,	2,
                        };
                LevelBuilder.setupMovingCubes(moving);

                int mover[] =
                        {
                                2,6,8,	6,
                                8,6,2,	4,
                        };
                LevelBuilder.setupMoverCubes(mover);

                int dead[] =
                        {
                                8,2,8,
                        };
                LevelBuilder.setupDeathCubes(dead);

                int hint[] =
                        {
                                7,5,4,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                2,
                                5,
                                1,
                                5,
                                2,
                                6,
                                1,
                                5,
                                3,
                                6,
                                1,
                                4,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 35:
            {
                int[] level =
                        {
                                7, 2, 2,
                                5, 5, 2,
                                2, 2, 2,
                                2, 2, 3,
                                2, 2, 4,
                                2, 2, 5,
                                2, 2, 6,
                                2, 2, 7,
                                2, 2, 8,
                                2, 4, 2,
                                2, 6, 8,
                                3, 4, 2,
                                4, 4, 2,
                                5, 4, 2,
                                7, 3, 2,
                                7, 4, 2,
                                7, 5, 2,
                                8, 2, 2,
                        };
                LevelBuilder.setup(level);

                int moving[] =
                        {
                                8,6,2,	2,
                        };
                LevelBuilder.setupMovingCubes(moving);

                int mover[] =
                        {
                                2,4,3,	5,
                                2,4,4,	5,
                                2,4,5,	5,
                                2,4,6,	5,
                                2,4,7,	5,
                                2,6,2,	5,
                                2,6,3,	5,
                                2,6,4,	5,
                                2,6,5,	5,
                                2,6,6,	5,
                                2,6,7,	5,
                                6,2,2,	4,
                        };
                LevelBuilder.setupMoverCubes(mover);

                int dead[] =
                        {
                                3,6,8,
                                7,6,2,
                                8,2,8,
                        };
                LevelBuilder.setupDeathCubes(dead);

                int hint[] =
                        {
                                1,3,5,
                                1,3,6,
                                1,3,7,
                                1,3,8,
                                1,4,8,
                                1,5,8,
                                1,5,7,
                                1,5,6,
                                1,5,5,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                5,
                                4,
                                6,
                                1,
                                4,
                                5,
                                1,
                                6,
                                3,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 36:
            {
                int[] level =
                        {
                                2, 4, 5,
                                5, 4, 2,
                                2, 2, 5,
                                2, 3, 5,
                                2, 4, 3,
                                2, 4, 4,
                                2, 4, 6,
                                2, 4, 7,
                                2, 4, 8,
                                2, 5, 5,
                                3, 4, 2,
                                3, 4, 8,
                                3, 5, 5,
                                4, 4, 2,
                                5, 2, 2,
                                5, 3, 2,
                                5, 3, 5,
                                5, 3, 8,
                                5, 5, 2,
                                5, 6, 2,
                                6, 2, 5,
                                6, 2, 8,
                                6, 4, 2,
                                7, 4, 2,
                                8, 4, 2,
                        };
                LevelBuilder.setup(level);

                int moving[] =
                        {
                                4,4,5,	3,
                        };
                LevelBuilder.setupMovingCubes(moving);

                int dead[] =
                        {
                                4,4,8,
                        };
                LevelBuilder.setupDeathCubes(dead);

                int hint[] =
                        {
                                5,3,8,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                3,
                                2,
                                3,
                                6,
                                1,
                                5,
                                2,
                                6,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 37:
            {
                int[] level =
                        {
                                5, 4, 2,
                                7, 3, 6,
                                2, 3, 4,
                                2, 6, 2,
                                3, 4, 6,
                                3, 5, 4,
                                5, 3, 4,
                                5, 6, 3,
                                6, 4, 6,
                                6, 6, 2,
                                7, 2, 5,
                                7, 3, 5,
                                7, 6, 6,
                        };
                LevelBuilder.setup(level);

                int moving[] =
                        {
                                6,5,6,	4,
                                8,5,5,	6,
                        };
                LevelBuilder.setupMovingCubes(moving);

                int hint[] =
                        {
                                8,5,5,
                                6,5,6,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                4,
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

            case 38:
            {
                int[] level =
                        {
                                8, 2, 5,
                                2, 2, 5,
                        };
                LevelBuilder.setup(level);

                int moving[] =
                        {
                                8,2,4,	5,
                                8,2,6,	6,
                                8,4,4,	5,
                                8,6,4,	5,
                                8,6,6,	6,
                        };
                LevelBuilder.setupMovingCubes(moving);

                int mover[] =
                        {
                                5,2,3,	5,
                                5,2,4,	5,
                                5,2,5,	5,
                                5,2,6,	5,
                                5,2,7,	5,
                                5,6,3,	6,
                                5,6,4,	6,
                                5,6,5,	6,
                                5,6,6,	6,
                                5,6,7,	6,
                        };
                LevelBuilder.setupMoverCubes(mover);

                int dead[] =
                        {
                                5,2,2,
                                5,2,8,
                                5,3,2,
                                5,3,8,
                                5,4,2,
                                5,4,8,
                                5,5,2,
                                5,5,8,
                                5,6,2,
                                5,6,8,
                                8,4,6,
                        };
                LevelBuilder.setupDeathCubes(dead);

                int hint[] =
                        {
                                1,3,4,
                                1,2,4,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                4,
                                6,
                                3,
                                5,
                                5,
                                1,
                                4,
                                2,
                                5,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 39:
            {
                int[] level =
                        {
                                8, 3, 2,
                                5, 4, 8,
                                3, 5, 6,
                                4, 2, 6,
                                4, 4, 2,
                                5, 5, 5,
                                5, 5, 7,
                                6, 2, 2,
                                6, 5, 8,
                                7, 2, 3,
                                7, 4, 5,
                                7, 4, 8,
                                7, 6, 8,
                                8, 5, 3,
                        };
                LevelBuilder.setup(level);

                int moving[] =
                        {
                                3,2,2,	2,
                                6,2,3,	1,
                        };
                LevelBuilder.setupMovingCubes(moving);

                int hint[] =
                        {
                                6,5,8,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                4,
                                1,
                                5,
                                3,
                                6,
                                2,
                                5,
                                1,
                                4,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 40:
            {
                int[] level =
                        {
                                7, 6, 2,
                                5, 5, 6,
                                2, 6, 7,
                                5, 2, 3,
                                5, 3, 6,
                                5, 4, 7,
                                5, 5, 7,
                                7, 5, 5,
                                7, 6, 4,
                                8, 3, 4,
                                8, 5, 4,
                                8, 5, 8,
                                8, 6, 4,
                                8, 6, 5,
                        };
                LevelBuilder.setup(level);

                int moving[] =
                        {
                                2,2,3,	5,
                                8,3,6,	1,
                        };
                LevelBuilder.setupMovingCubes(moving);

                int hint[] =
                        {
                                2,2,3,
                                8,5,8,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                5,
                                4,
                                2,
                                2,
                                5,
                                1,
                                5,
                                3,
                                6,
                                4,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 41:
            {
                int[] level =
                        {
                                2, 2, 2,
                                6, 4, 4,
                                3, 2, 3,
                                3, 3, 6,
                                3, 6, 6,
                                7, 4, 7,
                                8, 6, 5,
                                8, 6, 8,
                        };
                LevelBuilder.setup(level);

                int moving[] =
                        {
                                2,3,4,	1,
                                2,4,8,	2,
                                7,3,3,	2,
                        };
                LevelBuilder.setupMovingCubes(moving);

                int hint[] =
                        {
                                1,6,4,
                                1,5,4,
                                1,4,4,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                3,
                                1,
                                5,
                                4,
                                2,
                                3,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 42:
            {
                int[] level =
                        {
                                7, 2, 5,
                                5, 6, 4,
                                7, 2, 2,
                                7, 3, 5,
                                7, 4, 7,
                                8, 3, 6,
                                8, 3, 8,
                        };
                LevelBuilder.setup(level);

                int moving[] =
                        {
                                2,3,2,	2,
                                5,6,5,	2,
                                7,3,8,	2,
                        };
                LevelBuilder.setupMovingCubes(moving);

                int hint[] =
                        {
                                7,2,2,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                6,
                                1,
                                6,
                                2,
                                5,
                                1,
                                4,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;
/*
    case 43:
    {
        int[] level =
        {
            8, 2, 8,
            5, 4, 2,
            2, 3, 2,
            2, 4, 2,
            3, 3, 2,
            5, 3, 2,
            7, 3, 2,
            8, 3, 2,
            8, 4, 2,
        };
        LevelBuilder.setup(level);

        int moving[] =
        {
            4,2,2,	1,
            6,2,2,	1,
        };
        LevelBuilder.setupMovingCubes(moving);

        int mover[] =
        {
            2,2,2,	3,
            5,2,8,	6,
            8,2,2,	4,
        };
        LevelBuilder.setupMoverCubes(mover);

        int dead[] =
        {
            5,2,3,
        };
        LevelBuilder.setupDeathCubes(dead);

        int hint[] =
        {
            2,6,1,
            3,6,1,
            4,6,1,
        };
        LevelBuilder.setupHintCubes(hint);


        int solution[] =
        {
            4,
            1,
            4,
            6,
            3,
            2,
        };
        LevelBuilder.setupSolution(solution);
    }
        break;
*/


            case 43:
            {
                int[] level =
                        {
                                8, 2, 8,
                                5, 4, 2,
                                2, 3, 2,
                                2, 4, 2,
                                3, 3, 2,
                                5, 3, 2,
                                7, 3, 2,
                                8, 3, 2,
                                8, 4, 2,
                        };
                LevelBuilder.setup(level);

                int moving[] =
                        {
                                4,2,2,	1,
                                6,2,2,	1,
                        };
                LevelBuilder.setupMovingCubes(moving);

                int mover[] =
                        {
                                5,2,8,	6,
                                8,2,2,	4,
                        };
                LevelBuilder.setupMoverCubes(mover);

                int dead[] =
                        {
                                5,2,3,
                        };
                LevelBuilder.setupDeathCubes(dead);

                int hint[] =
                        {
                                2,6,1,
                                3,6,1,
                                4,6,1,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                4,
                                1,
                                4,
                                6,
                                3,
                                2,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;


            case 44:
            {
                int[] level =
                        {
                                8, 4, 8,
                                5, 5, 5,
                                2, 3, 4,
                                2, 3, 7,
                                2, 4, 3,
                                2, 4, 4,
                                2, 4, 5,
                                2, 4, 6,
                                2, 4, 7,
                                2, 4, 8,
                                2, 5, 4,
                                2, 5, 7,
                                3, 2, 3,
                                3, 3, 3,
                                3, 4, 2,
                                3, 4, 3,
                                3, 4, 7,
                                4, 3, 2,
                                4, 4, 2,
                                4, 5, 2,
                                5, 4, 2,
                                6, 4, 2,
                                7, 3, 2,
                                7, 3, 8,
                                7, 4, 2,
                                7, 4, 3,
                                7, 5, 2,
                                8, 2, 8,
                                8, 3, 7,
                                8, 3, 8,
                                8, 4, 2,
                                8, 6, 8,
                        };
                LevelBuilder.setup(level);

                int hint[] =
                        {
                                2,4,8,
                                7,3,8,
                                6,4,2,
                                5,4,2,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                4,
                                2,
                                4,
                                1,
                                3,
                                1,
                                6,
                                2,
                                4,
                                5,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 45:
            {
                int[] level =
                        {
                                4, 5, 7,
                                3, 4, 8,
                                2, 3, 6,
                                3, 2, 5,
                                3, 4, 6,
                                4, 6, 3,
                                5, 2, 5,
                                5, 5, 8,
                                6, 4, 3,
                                6, 5, 5,
                                7, 4, 2,
                                8, 2, 6,
                        };
                LevelBuilder.setup(level);

                int hint[] =
                        {
                                5,5,8,
                                6,5,5,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                3,
                                5,
                                4,
                                6,
                                4,
                                2,
                                5,
                                3,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 46:
            {
                int[] level =
                        {
                                5, 2, 8,
                                8, 5, 3,
                                2, 2, 6,
                                2, 2, 7,
                                2, 2, 8,
                                2, 3, 7,
                                2, 3, 8,
                                2, 4, 2,
                                2, 4, 7,
                                3, 4, 2,
                                5, 2, 3,
                                5, 3, 2,
                                5, 3, 4,
                                5, 3, 5,
                                5, 3, 6,
                                5, 3, 7,
                                5, 3, 8,
                                7, 2, 8,
                                8, 2, 3,
                                8, 6, 7,
                        };
                LevelBuilder.setup(level);

                int mover[] =
                        {
                                2,2,5,	5,
                                2,5,7,	6,
                                3,2,8,	1,
                                3,3,2,	3,
                                3,4,7,	6,
                                3,5,2,	3,
                                5,2,4,	4,
                                7,3,8,	1,
                                7,6,7,	4,
                                8,3,3,	5,
                        };
                LevelBuilder.setupMoverCubes(mover);

                int hint[] =
                        {
                                4,5,1,
                                5,5,1,
                                6,5,1,
                                7,5,1,
                                8,5,1,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                6,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 47:
            {
                int[] level =
                        {
                                2, 5, 6,
                                6, 2, 2,
                                2, 2, 4,
                                2, 5, 3,
                                2, 6, 8,
                                3, 6, 4,
                                4, 4, 7,
                                4, 6, 3,
                                5, 5, 7,
                                6, 3, 4,
                                6, 5, 8,
                                7, 3, 7,
                                7, 4, 4,
                                8, 6, 8,
                        };
                LevelBuilder.setup(level);

                int hint[] =
                        {
                                8,1,2,
                                7,1,2,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                3,
                                2,
                                6,
                                4,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 48:
            {
                int[] level =
                        {
                                8, 4, 5,
                                4, 5, 4,
                                2, 4, 7,
                                2, 5, 4,
                                2, 5, 5,
                                2, 6, 4,
                                2, 6, 6,
                                3, 2, 7,
                                3, 3, 5,
                                3, 6, 6,
                                4, 2, 2,
                                4, 2, 8,
                                4, 3, 5,
                                4, 3, 6,
                                4, 5, 8,
                                5, 3, 8,
                                5, 6, 4,
                                6, 3, 4,
                                6, 6, 3,
                                7, 2, 5,
                                7, 3, 6,
                                7, 5, 2,
                                7, 5, 3,
                                7, 6, 2,
                                8, 2, 8,
                                8, 5, 7,
                        };
                LevelBuilder.setup(level);

                int hint[] =
                        {
                                7,5,3,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                1,
                                5,
                                4,
                                6,
                                2,
                                3,
                                6,
                                4,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 49:
            {
                int[] level =
                        {
                                3, 3, 2,
                                4, 5, 8,
                                2, 2, 6,
                                2, 2, 8,
                                2, 4, 3,
                                3, 2, 6,
                                3, 5, 7,
                                3, 6, 3,
                                3, 6, 4,
                                4, 3, 7,
                                4, 4, 3,
                                4, 4, 4,
                                4, 4, 6,
                                4, 5, 6,
                                4, 6, 2,
                                4, 6, 3,
                                5, 2, 4,
                                5, 5, 3,
                                6, 2, 3,
                                6, 3, 6,
                                6, 4, 7,
                                6, 5, 3,
                                7, 3, 7,
                                7, 4, 2,
                                7, 6, 8,
                                8, 5, 4,
                        };
                LevelBuilder.setup(level);

                int hint[] =
                        {
                                4,6,3,
                                3,6,3,
                                3,6,4,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                5,
                                1,
                                3,
                                2,
                                6,
                                1,
                                4,
                                5,
                                2,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 50:
            {
                int[] level =
                        {
                                6, 4, 4,
                                3, 3, 3,
                                2, 5, 5,
                                2, 6, 5,
                                3, 2, 8,
                                3, 6, 2,
                                3, 6, 4,
                                3, 6, 6,
                                4, 2, 2,
                                4, 3, 6,
                                4, 3, 7,
                                4, 5, 6,
                                5, 3, 2,
                                5, 5, 8,
                                5, 6, 3,
                                5, 6, 5,
                                5, 6, 7,
                                6, 3, 3,
                                6, 3, 5,
                                6, 4, 3,
                                6, 5, 3,
                                6, 6, 8,
                                7, 2, 4,
                                7, 5, 3,
                                7, 6, 7,
                                8, 3, 3,
                        };
                LevelBuilder.setup(level);

                int hint[] =
                        {
                                3,6,2,
                                3,2,8,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                4,
                                2,
                                6,
                                3,
                                1,
                                5,
                                2,
                                6,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 51:
            {
                int[] level =
                        {
                                2, 6, 4,
                                6, 3, 6,
                                2, 2, 6,
                                3, 3, 2,
                                3, 4, 7,
                                3, 5, 7,
                                3, 6, 8,
                                4, 2, 2,
                                4, 2, 5,
                                4, 3, 6,
                                4, 5, 7,
                                4, 6, 8,
                                5, 3, 7,
                                5, 4, 4,
                                5, 5, 8,
                                6, 2, 8,
                                6, 3, 5,
                                6, 3, 7,
                                7, 2, 3,
                                7, 2, 7,
                                7, 3, 3,
                                7, 3, 8,
                                7, 4, 8,
                                7, 5, 4,
                                8, 4, 2,
                                8, 4, 6,
                        };
                LevelBuilder.setup(level);

                int hint[] =
                        {
                                6,3,5,
                                4,3,6,
                                5,3,7,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                3,
                                2,
                                6,
                                1,
                                4,
                                5,
                                3,
                                5,
                                3,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 52:
            {
                int[] level =
                        {
                                8, 5, 5,
                                4, 3, 3,
                                2, 4, 6,
                                2, 5, 4,
                                3, 4, 4,
                                3, 4, 7,
                                4, 2, 5,
                                4, 4, 2,
                                4, 4, 7,
                                4, 6, 5,
                                5, 2, 4,
                                5, 2, 8,
                                5, 3, 2,
                                5, 4, 6,
                                5, 6, 6,
                                6, 4, 8,
                                6, 5, 5,
                                6, 5, 6,
                                6, 6, 3,
                                7, 3, 3,
                                7, 3, 8,
                                7, 6, 3,
                                7, 6, 7,
                                8, 3, 3,
                                8, 4, 2,
                                8, 6, 7,
                        };
                LevelBuilder.setup(level);

                int hint[] =
                        {
                                8,1,6,
                                7,1,6,
                                6,1,6,
                                5,1,6,
                                4,1,6,
                                3,1,6,
                                2,1,6,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                1,
                                5,
                                2,
                                4,
                                1,
                                6,
                                3,
                                5,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 53:
            {
                int[] level =
                        {
                                8, 2, 2,
                                5, 6, 6,
                                2, 2, 8,
                                2, 6, 4,
                                2, 6, 7,
                                3, 4, 2,
                                4, 3, 5,
                                4, 3, 7,
                                4, 5, 8,
                                4, 6, 3,
                                5, 2, 4,
                                5, 3, 2,
                                5, 3, 6,
                                5, 4, 2,
                                5, 5, 2,
                                6, 2, 8,
                                6, 3, 6,
                                6, 4, 5,
                                6, 6, 5,
                                7, 2, 2,
                                7, 2, 7,
                                7, 3, 5,
                                7, 6, 3,
                                8, 2, 3,
                        };
                LevelBuilder.setup(level);

                int dead[] =
                        {
                                3,6,7,
                                7,6,2,
                        };
                LevelBuilder.setupDeathCubes(dead);

                int hint[] =
                        {
                                8,1,8,
                                8,1,7,
                                8,1,6,
                                8,1,5,
                                8,1,4,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                1,
                                5,
                                2,
                                6,
                                4,
                                5,
                                1,
                                6,
                                4,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 54:
            {
                int[] level =
                        {
                                2, 2, 2,
                                4, 3, 7,
                                2, 4, 4,
                                2, 5, 4,
                                2, 5, 7,
                                2, 6, 2,
                                3, 4, 5,
                                3, 4, 7,
                                3, 6, 3,
                                3, 6, 8,
                                4, 5, 5,
                                4, 5, 6,
                                4, 6, 2,
                                4, 6, 7,
                                5, 3, 2,
                                6, 3, 6,
                                6, 5, 5,
                                7, 3, 3,
                                7, 3, 4,
                                7, 3, 7,
                                7, 5, 6,
                                8, 2, 6,
                                8, 3, 5,
                                8, 6, 3,
                        };
                LevelBuilder.setup(level);

                int mover[] =
                        {
                                4,5,2,	4,
                                5,6,8,	1,
                        };
                LevelBuilder.setupMoverCubes(mover);

                int hint[] =
                        {
                                8,1,4,
                                7,1,4,
                                6,1,4,
                                5,1,4,
                                4,1,4,
                                3,1,4,
                                2,1,4,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                5,
                                3,
                                1,
                                6,
                                2,
                                4,
                                1,
                                6,
                                3,
                                5,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 55:
            {
                int[] level =
                        {
                                8, 6, 8,
                                2, 5, 7,
                                2, 3, 8,
                                3, 2, 7,
                                4, 2, 8,
                                4, 4, 3,
                                5, 2, 7,
                                5, 2, 8,
                                5, 4, 4,
                                5, 4, 5,
                                5, 6, 2,
                                7, 2, 4,
                                7, 2, 8,
                                7, 3, 8,
                                7, 4, 7,
                                7, 5, 6,
                                7, 5, 7,
                                7, 6, 6,
                                7, 6, 8,
                                8, 2, 3,
                                8, 4, 7,
                                8, 5, 3,
                        };
                LevelBuilder.setup(level);

                int moving[] =
                        {
                                4,3,6,	2,
                                5,2,5,	1,
                                5,5,2,	1,
                        };
                LevelBuilder.setupMovingCubes(moving);

                int hint[] =
                        {
                                2,2,1,
                                1,2,2,
                                2,3,1,
                                1,3,2,
                                2,4,1,
                                1,4,2,
                                2,5,1,
                                1,5,2,
                                2,6,1,
                                1,6,2,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                6,
                                2,
                                4,
                                1,
                                3,
                                2,
                                5,
                                1,
                                4,
                                2,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 56:
            {
                int[] level =
                        {
                                3, 2, 5,
                                6, 4, 4,
                                2, 2, 4,
                                3, 2, 4,
                                3, 4, 2,
                                3, 5, 3,
                                3, 5, 4,
                                4, 2, 5,
                                4, 5, 6,
                                4, 6, 7,
                                5, 2, 5,
                                5, 3, 2,
                                5, 3, 3,
                                5, 3, 5,
                                5, 5, 6,
                                6, 2, 4,
                                6, 2, 5,
                                6, 2, 6,
                                6, 5, 3,
                                6, 5, 7,
                                6, 6, 5,
                                7, 2, 8,
                                8, 2, 3,
                                8, 4, 2,
                                8, 5, 2,
                                8, 6, 5,
                        };
                LevelBuilder.setup(level);

                int dead[] =
                        {
                                5,4,7,
                                6,6,7,
                        };
                LevelBuilder.setupDeathCubes(dead);

                int hint[] =
                        {
                                8,4,2,
                                8,2,3,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                1,
                                3,
                                2,
                                3,
                                6,
                                2,
                                4,
                                1,
                                5,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 57:
            {
                int[] level =
                        {
                                6, 3, 2,
                                5, 5, 7,
                                2, 5, 4,
                                2, 6, 5,
                                2, 6, 7,
                                3, 2, 2,
                                3, 5, 2,
                                3, 6, 6,
                                4, 2, 3,
                                4, 4, 6,
                                4, 5, 5,
                                5, 4, 6,
                                5, 5, 8,
                                5, 6, 3,
                                5, 6, 5,
                                6, 2, 4,
                                6, 3, 3,
                                6, 3, 7,
                                6, 6, 6,
                                7, 2, 5,
                                7, 4, 4,
                                7, 4, 8,
                                7, 6, 2,
                                8, 2, 2,
                                8, 5, 5,
                                8, 6, 5,
                        };
                LevelBuilder.setup(level);

                int moving[] =
                        {
                                5,2,5,	4,
                                7,5,4,	1,
                        };
                LevelBuilder.setupMovingCubes(moving);

                int mover[] =
                        {
                                2,2,4,	1,
                                6,6,4,	3,
                        };
                LevelBuilder.setupMoverCubes(mover);

                int dead[] =
                        {
                                4,3,5,
                                5,4,3,
                        };
                LevelBuilder.setupDeathCubes(dead);

                int hint[] =
                        {
                                8,6,5,
                                6,6,6,
                                7,4,8,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                3,
                                5,
                                1,
                                6,
                                4,
                                5,
                                2,
                                6,
                                4,
                                5,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 58:
            {
                int[] level =
                        {
                                8, 4, 7,
                                4, 5, 3,
                                2, 3, 5,
                                2, 5, 7,
                                3, 3, 8,
                                3, 5, 8,
                                3, 6, 3,
                                3, 6, 6,
                                4, 2, 6,
                                4, 4, 3,
                                5, 3, 7,
                                5, 6, 8,
                                6, 2, 2,
                                6, 2, 6,
                                6, 4, 6,
                                6, 5, 6,
                                6, 5, 7,
                                7, 2, 2,
                                7, 2, 3,
                                7, 2, 4,
                                8, 2, 2,
                                8, 3, 4,
                        };
                LevelBuilder.setup(level);

                int moving[] =
                        {
                                5,5,2,	5,
                                5,5,6,	3,
                                6,5,8,	5,
                                6,6,3,	3,
                        };
                LevelBuilder.setupMovingCubes(moving);

                int hint[] =
                        {
                                5,1,2,
                                5,1,3,
                                5,1,4,
                                5,1,5,
                                5,1,6,
                                5,1,7,
                                5,1,8,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                4,
                                2,
                                6,
                                3,
                                5,
                                1,
                                4,
                                6,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 59:
            {
                int[] level =
                        {
                                5, 3, 8,
                                6, 4, 3,
                                2, 3, 3,
                                2, 3, 7,
                                2, 5, 2,
                                2, 5, 8,
                                3, 2, 2,
                                3, 2, 6,
                                3, 3, 6,
                                3, 4, 8,
                                3, 6, 4,
                                3, 6, 6,
                                4, 3, 2,
                                4, 3, 3,
                                5, 6, 6,
                                6, 4, 2,
                                6, 5, 5,
                                6, 6, 7,
                                7, 2, 4,
                                7, 2, 7,
                                7, 4, 5,
                                7, 5, 5,
                        };
                LevelBuilder.setup(level);

                int moving[] =
                        {
                                3,3,4,	5,
                                3,6,3,	2,
                                4,6,4,	1,
                                8,4,3,	5,
                        };
                LevelBuilder.setupMovingCubes(moving);

                int hint[] =
                        {
                                1,5,7,
                                1,5,6,
                                1,5,5,
                                1,5,4,
                                1,5,3,
                                1,4,3,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                1,
                                6,
                                2,
                                3,
                                1,
                                4,
                                6,
                                2,
                                3,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;

            case 60:
            {
                int[] level =
                        {
                                2, 6, 2,
                                3, 4, 4,
                                2, 2, 4,
                                2, 3, 7,
                                3, 2, 7,
                                3, 5, 2,
                                3, 6, 2,
                                4, 5, 7,
                                5, 2, 4,
                                5, 5, 7,
                                5, 5, 8,
                                5, 6, 2,
                                5, 6, 7,
                                6, 2, 3,
                                6, 3, 2,
                                6, 3, 5,
                                6, 4, 3,
                                6, 4, 4,
                                7, 6, 7,
                                8, 3, 5,
                                8, 5, 3,
                                8, 6, 7,
                        };
                LevelBuilder.setup(level);

                int moving[] =
                        {
                                5,3,5,	5,
                                5,4,2,	1,
                                7,2,3,	2,
                                8,6,6,	4,
                        };
                LevelBuilder.setupMovingCubes(moving);

                int hint[] =
                        {
                                8,1,8,
                                8,1,7,
                                7,1,8,
                                7,1,7,
                                6,1,8,
                                8,1,6,
                                7,1,6,
                                6,1,7,
                                8,1,5,
                                5,1,8,
                                6,1,6,
                                7,1,5,
                                5,1,7,
                                8,1,4,
                                4,1,8,
                                6,1,5,
                                5,1,6,
                                7,1,4,
                                4,1,7,
                                8,1,3,
                                3,1,8,
                                5,1,5,
                                6,1,4,
                                4,1,6,
                                7,1,3,
                                3,1,7,
                                8,1,2,
                                2,1,8,
                        };
                LevelBuilder.setupHintCubes(hint);


                int solution[] =
                        {
                                2,
                                3,
                                1,
                                5,
                                5,
                                4,
                                2,
                                6,
                                1,
                                5,
                        };
                LevelBuilder.setupSolution(solution);
            }
            break;


        }










    }
}

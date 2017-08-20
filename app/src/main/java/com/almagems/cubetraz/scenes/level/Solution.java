package com.almagems.cubetraz.scenes.level;

import java.util.ArrayList;


class Solution {

    int index;
    ArrayList<Integer> steps = new ArrayList<>();

    void init(int[] array) {
        reset();

        int size = array.length;
        for (int i = 0; i < size; ++i) {
            steps.add(array[i]);
        }
    }

    void reset() {
        steps.clear();
        index = 0;
    }

    int getStepsCount() {
        return steps.size();
    }

    int getStep() {
        return steps.get(index++);
    }

    int getFirstStep() {
        return  steps.get(0);
    }

}

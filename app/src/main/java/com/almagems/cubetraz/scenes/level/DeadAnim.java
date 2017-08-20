package com.almagems.cubetraz.scenes.level;

public class DeadAnim {

    public float size;
    public int alpha;
    public int alphaStep;

    public void init() {
        size = 60.0f;
        alpha = 1;
        alphaStep = 4;
    }

    public void update() {
        size += 4.0f;
        alpha += alphaStep;

        if (alpha < 0) {
            alpha = 0;
        }

        if (alphaStep > 0) {
            if (alpha > 130) {
                alphaStep = -3;
            }
        }
    }

    public boolean isDone() {
        return (alpha <= 0);
    }

}

package com.almagems.cubetraz.utils;


import com.almagems.cubetraz.math.Vector2;

public final class UserRotation {

    public Vector2 current = new Vector2();
    public Vector2 from = new Vector2();

    public void reset() {
        current.x = current.y = 0.0f;
        from.x = from.y = 0.0f;
    }

    public void clamp() {
        if ( current.y > 45.0f) {
            current.y = 45.0f;
        }

        if ( current.y < -45.0f) {
            current.y = -45.0f;
        }

        if ( current.x < -20.0f) {
            current.x = -20.0f;
        }

        if ( current.x > 40.0f) {
            current.x = 40.0f;
        }
    }

}

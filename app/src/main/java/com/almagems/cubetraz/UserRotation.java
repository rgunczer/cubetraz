package com.almagems.cubetraz;


public final class UserRotation {

    Vector2 current;
    Vector2 from;

    void reset() {
        current.x = current.y = 0.0f;
        from.x = from.y = 0.0f;
    }

    void clamp() {
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

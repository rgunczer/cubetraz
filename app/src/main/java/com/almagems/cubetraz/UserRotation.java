package com.almagems.cubetraz;

public class UserRotation {

    vec2 current;
    vec2 from;

    void Reset()
    {
        current.x = current.y = 0.0f;
        from.x = from.y = 0.0f;
    }

    void Clamp()
    {
        if ( current.y > 45.0f)
            current.y = 45.0f;

        if ( current.y < -45.0f)
            current.y = -45.0f;

        if ( current.x < -20.0f)
            current.x = -20.0f;

        if ( current.x > 40.0f)
            current.x = 40.0f;
    }

}

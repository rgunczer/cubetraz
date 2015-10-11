package com.almagems.cubetraz;

public class Camera {

    public Vector eye;
    public Vector target;

    // ctor
    public Camera()
    {
        eye = new Vector(0.0f, 0.0f, 0.0f);
        target = new Vector(0.0f, 0.0f, 0.0f);
    }

}

package com.almagems.cubetraz.graphics;


import com.almagems.cubetraz.math.Vector;

public final class Camera {

    public Vector eye = new Vector(0f, 0f, 0f);
    public Vector target = new Vector(0f, 0f, 0f);

    public void init(Camera other) {
        eye.x = other.eye.x;
        eye.y = other.eye.y;
        eye.z = other.eye.z;
        
        target.x = other.target.x;
        target.y = other.target.y;
        target.z = other.target.z;
    }

}

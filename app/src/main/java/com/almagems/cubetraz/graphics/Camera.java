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

    public void setEye(Vector eye) {
        this.eye.x = eye.x;
        this.eye.y = eye.y;
        this.eye.z = eye.z;
    }

    public void setEye(float x, float y, float z) {
        eye.x = x;
        eye.y = y;
        eye.z = z;
    }

    public void setTarget(Vector target) {
        this.target.x = target.x;
        this.target.y = target.y;
        this.target.z = target.z;
    }

    public void setTarget(float x, float y, float z) {
        target.x = x;
        target.y = y;
        target.z = z;
    }

}

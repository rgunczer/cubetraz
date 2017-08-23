package com.almagems.cubetraz.utils;


import com.almagems.cubetraz.math.Vector;

public class CubeRotation {

    public float degree;
    public Vector axis = new Vector();

    public void init(float degree, Vector axis) {
        this.degree = degree;
        this.axis.init(axis);
    }

    public String toString() {
        return "degree: " + degree + ", axis: " + axis.x + ", " + axis.y + ", " + axis.z;
    }
}

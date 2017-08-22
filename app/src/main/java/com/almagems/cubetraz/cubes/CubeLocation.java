package com.almagems.cubetraz.cubes;

public final class CubeLocation {

    public int x;
    public int y;
    public int z;

    public CubeLocation() {
        this.reset();
    }

    public CubeLocation(CubeLocation another) {
        this.x = another.x;
        this.y = another.y;
        this.z = another.z;
    }

    public CubeLocation(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void reset() {
        x = y = z = 0;
    }
    
    public void init(CubeLocation other) {
        x = other.x;
        y = other.y;
        z = other.z;
    }

    public void init(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean equals(CubeLocation other) {
        return (this.x == other.x && this.y == other.y && this.z == other.z);
    }
}

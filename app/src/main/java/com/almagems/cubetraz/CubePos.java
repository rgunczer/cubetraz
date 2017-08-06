package com.almagems.cubetraz;


public final class CubePos {

    public int x;
    public int y;
    public int z;

    public CubePos() {
        this.reset();
    }

    public CubePos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void reset() {
        x = y = z = 0;
    }
    
    public void init(CubePos other) {
        x = other.x;
        y = other.y;
        z = other.z;
    }

}

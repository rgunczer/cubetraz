package com.almagems.cubetraz;


public final class CubePos {

    public int x;
    public int y;
    public int z;


    // ctor
    public CubePos() {
        x = y = z = 0;
    }

    // ctor
    public CubePos(int ix, int iy, int iz) {
        x = ix;
        y = iy;
        z = iz;
    }

    public void reset() {
        x = y = z = 0;
    }
    
    public void init(final CubePos other) {
        x = other.x;
        y = other.y;
        z = other.z;
    }

}

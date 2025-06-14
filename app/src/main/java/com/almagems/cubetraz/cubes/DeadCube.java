package com.almagems.cubetraz.cubes;

import com.almagems.cubetraz.Game;
import com.almagems.cubetraz.graphics.Graphics;
import com.almagems.cubetraz.math.Vector;
import com.almagems.cubetraz.math.Vector2;
import com.almagems.cubetraz.graphics.Color;
import com.almagems.cubetraz.graphics.TexCoordsQuad;
import com.almagems.cubetraz.graphics.TexturedQuad;
import static com.almagems.cubetraz.Game.*;

public final class DeadCube {

    private CubeLocation mLocation = new CubeLocation();

    private TexturedQuad[] mCubeFaceTextures = new TexturedQuad[7];
    
    private final Color mColor = new Color();
    private final Color mColorCurrent = new Color();
    
    private final Color mColorSymbol = new Color();
      	    
    public final Vector pos = new Vector();
    private final TexCoordsQuad coords = new TexCoordsQuad();
    
    public CubeLocation getLocation() {
        return mLocation;
    }

    public void hiLite() {
        mColorSymbol.init(255, 25, 25, 204);
    }
    public void noHilite() {
        mColorSymbol.init(Game.symbolColor);
    }

    public DeadCube() {
        for (int i = 0; i < 7; ++i) {
            mCubeFaceTextures[i] = null;
        }
    }

    public void reposition() {
        noHilite();
    }

    private void setCubePos(CubeLocation coordinate) {
        mLocation.init(coordinate);
        pos.init(Game.getCubePosition(mLocation));
    }

    public void init(CubeLocation cubePos) {
        setCubePos(cubePos);
    
        mCubeFaceTextures[Z_Plus] = Game.getSymbol(Symbol_Death);
        mCubeFaceTextures[X_Plus] = Game.getSymbol(Symbol_Death);
    
        mColorCurrent.init( Game.faceColor );
        mColor.init( Game.faceColor );
        noHilite();
    }

    public void update() {
    }

    public void renderCube() {
        Graphics.addCubeSize(pos.x, pos.y, pos.z, HALF_CUBE_SIZE, mColorCurrent);
    }

    public void renderSymbols() {
        TexturedQuad pTQ = mCubeFaceTextures[X_Plus];
        coords.tx0 = new Vector2(pTQ.tx_lo_left.x,  pTQ.tx_up_left.y);
        coords.tx1 = new Vector2(pTQ.tx_lo_right.x, pTQ.tx_up_right.y);
        coords.tx2 = new Vector2(pTQ.tx_up_right.x, pTQ.tx_lo_right.y);
        coords.tx3 = new Vector2(pTQ.tx_up_left.x,  pTQ.tx_lo_left.y);
        Graphics.addCubeFace_X_Plus(pos.x, pos.y, pos.z, coords, mColorSymbol);

        pTQ = mCubeFaceTextures[Z_Plus];
        coords.tx0 = new Vector2(pTQ.tx_lo_left.x,  pTQ.tx_up_left.y);
        coords.tx1 = new Vector2(pTQ.tx_lo_right.x, pTQ.tx_up_right.y);
        coords.tx2 = new Vector2(pTQ.tx_up_right.x, pTQ.tx_lo_right.y);
        coords.tx3 = new Vector2(pTQ.tx_up_left.x,  pTQ.tx_lo_left.y);
        Graphics.addCubeFace_Z_Plus(pos.x, pos.y, pos.z, coords, mColorSymbol);
    }

}
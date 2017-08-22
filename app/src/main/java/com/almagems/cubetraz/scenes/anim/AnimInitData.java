package com.almagems.cubetraz.scenes.anim;

import com.almagems.cubetraz.math.Vector;
import com.almagems.cubetraz.cubes.Cube;
import com.almagems.cubetraz.graphics.Camera;

import java.util.ArrayList;

import static com.almagems.cubetraz.Game.*;

public final class AnimInitData {

	public AnimTypeEnum type;
	
	public final ArrayList<Cube> cubesBase = new ArrayList<>();
	
	public float cubeRotationDegree;
	
	public final Camera cameraFrom = new Camera();
	public final Camera cameraTo = new Camera();
	
	public final Vector posLightFrom = new Vector();
	public final Vector posLightTo = new Vector();
	
	public CubeFaceNames faceNameXPlus;
	public CubeFaceNames faceNameYPlus;
	public CubeFaceNames faceNameZPlus;
        
    public FaceTransformsEnum[] transformsXPlus = new FaceTransformsEnum[MAX_FACE_TRANSFORM_COUNT];
    public FaceTransformsEnum[] transformsYPlus = new FaceTransformsEnum[MAX_FACE_TRANSFORM_COUNT];
    public FaceTransformsEnum[] transformsZPlus = new FaceTransformsEnum[MAX_FACE_TRANSFORM_COUNT];
    
	public void setFaces(CubeFaceNames faceIdX, CubeFaceNames faceIdY, CubeFaceNames faceIdZ) {
		faceNameXPlus = faceIdX;
		faceNameYPlus = faceIdY;
		faceNameZPlus = faceIdZ;
	}
	
    public void clearTransforms() {
        for (int i = 0; i < MAX_FACE_TRANSFORM_COUNT; ++i) {
            transformsXPlus[i] = FaceTransformsEnum.NoTransform;
            transformsYPlus[i] = FaceTransformsEnum.NoTransform;
            transformsZPlus[i] = FaceTransformsEnum.NoTransform;
        }
    }
}
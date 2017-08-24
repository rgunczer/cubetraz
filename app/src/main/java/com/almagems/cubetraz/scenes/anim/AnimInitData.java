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

	public void setFaces(CubeFaceNames faceNameXPlus, CubeFaceNames faceNameYPlus, CubeFaceNames faceNameZPlus) {
		this.faceNameXPlus = faceNameXPlus;
		this.faceNameYPlus = faceNameYPlus;
		this.faceNameZPlus = faceNameZPlus;
	}
}
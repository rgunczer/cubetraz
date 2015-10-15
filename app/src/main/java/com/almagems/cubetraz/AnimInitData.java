package com.almagems.cubetraz;

import java.util.ArrayList;

import static com.almagems.cubetraz.Constants.*;


public final class AnimInitData {

	public AnimTypeEnum type;
	
	public final ArrayList<Cube> list_cubes_base = new ArrayList();
	
	public float cube_rotation_degree;
	public boolean from_level_paused;
	
	public final Camera camera_from = new Camera();
	public final Camera camera_to = new Camera();
	
	public final Vector pos_light_from = new Vector();
	public final Vector pos_light_to = new Vector();
	
	CubeFaceNamesEnum face_name_x_plus;
	CubeFaceNamesEnum face_name_y_plus;
	CubeFaceNamesEnum face_name_z_plus;
        
    FaceTransformsEnum[] transforms_x_plus = new FaceTransformsEnum[MAX_FACE_TRANSFORM_COUNT];
    FaceTransformsEnum[] transforms_y_plus = new FaceTransformsEnum[MAX_FACE_TRANSFORM_COUNT];
    FaceTransformsEnum[] transforms_z_plus = new FaceTransformsEnum[MAX_FACE_TRANSFORM_COUNT];
    
	public void setFaces(CubeFaceNamesEnum face_id_x, CubeFaceNamesEnum face_id_y, CubeFaceNamesEnum face_id_z) {
		face_name_x_plus = face_id_x;
		face_name_y_plus = face_id_y;
		face_name_z_plus = face_id_z;
	}
	
    public void clearTransforms() {
        for (int i = 0; i < MAX_FACE_TRANSFORM_COUNT; ++i) {
            transforms_x_plus[i] = FaceTransformsEnum.NoTransform;
            transforms_y_plus[i] = FaceTransformsEnum.NoTransform;
            transforms_z_plus[i] = FaceTransformsEnum.NoTransform;
        }
    }
    
    public void addTransform_X_Plus(FaceTransformsEnum transform) {
        for (int i = 0; i < MAX_FACE_TRANSFORM_COUNT; ++i) {
            if (FaceTransformsEnum.NoTransform == transforms_x_plus[i]) {
                transforms_x_plus[i] = transform;
                return;
            }
        }
    }

    public void addTransform_Y_Plus(FaceTransformsEnum transform) {
        for (int i = 0; i < MAX_FACE_TRANSFORM_COUNT; ++i) {
            if (FaceTransformsEnum.NoTransform == transforms_y_plus[i]) {
                transforms_y_plus[i] = transform;
                return;
            }
        }
    }
    
    public void addTransform_Z_Plus(FaceTransformsEnum transform) {
        for (int i = 0; i < MAX_FACE_TRANSFORM_COUNT; ++i) {
            if (FaceTransformsEnum.NoTransform == transforms_z_plus[i]) {
                transforms_z_plus[i] = transform;
                return;
            }
        }
    }
}
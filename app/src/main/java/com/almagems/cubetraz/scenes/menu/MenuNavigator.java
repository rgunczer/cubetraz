package com.almagems.cubetraz.scenes.menu;

import com.almagems.cubetraz.scenes.Creator;
import com.almagems.cubetraz.utils.CubeRotation;
import com.almagems.cubetraz.utils.EaseOutDivideInterpolation;
import com.almagems.cubetraz.Game;
import com.almagems.cubetraz.math.Vector;

import static android.opengl.GLES10.*;

import static com.almagems.cubetraz.Game.*;


public final class MenuNavigator
{    
    private final EaseOutDivideInterpolation m_interpolator = new EaseOutDivideInterpolation();
    private boolean m_secondary_rotation;
    
    public CubeRotation cubeRotation = new CubeRotation();
    public CubeRotation cubeRotationSecondary = new CubeRotation();
   
    private CubeFaceNavigationEnum mNavigation;
	
    public boolean isCurrentNavigation(CubeFaceNavigationEnum navigation) {
        if (navigation == mNavigation) {
            return true;
        } else {
            return false;
        }
    }
    
    public void applyRotations() {
        glRotatef(cubeRotation.degree, cubeRotation.axis.x, cubeRotation.axis.y, cubeRotation.axis.z);
        glRotatef(cubeRotationSecondary.degree, cubeRotationSecondary.axis.x, cubeRotationSecondary.axis.y, cubeRotationSecondary.axis.z);
    }
            
    public void createMenuFaces(boolean tutorial) {
	    Game.clearCubeFaceData();
	    Creator.fillPools();
	    MenuFaceBuilder.resetTransforms();
		
        MenuFaceBuilder.build(CubeFaceNames.Face_Easy01, Face_X_Plus);
	
	    if (tutorial) {
		    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
		    MenuFaceBuilder.build(CubeFaceNames.Face_Tutorial, Face_X_Minus);
	    } else {
		    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
		    MenuFaceBuilder.build(CubeFaceNames.Face_Hard01, Face_X_Minus);
	    }
    
  	    MenuFaceBuilder.build(CubeFaceNames.Face_Menu, Face_Z_Plus);
        MenuFaceBuilder.build(CubeFaceNames.Face_Empty, Face_Z_Minus);
	
	    MenuFaceBuilder.build(CubeFaceNames.Face_Options, Face_Y_Plus);
	
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorHoriz);
        MenuFaceBuilder.build(CubeFaceNames.Face_Score, Face_Y_Minus);
    }

    public void createEasyFaces() {
	    Game.clearCubeFaceData();
	    Creator.fillPools();
	    MenuFaceBuilder.resetTransforms();
	
        MenuFaceBuilder.build(CubeFaceNames.Face_Easy01, Face_X_Plus);
	
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.RotateCW90);
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorHoriz);
        MenuFaceBuilder.build(CubeFaceNames.Face_Easy02, Face_Y_Minus);

	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorHoriz);
        MenuFaceBuilder.build(CubeFaceNames.Face_Easy03, Face_X_Minus);
    
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.RotateCCW90);
        MenuFaceBuilder.build(CubeFaceNames.Face_Easy04, Face_Y_Plus);
	
        MenuFaceBuilder.build(CubeFaceNames.Face_Menu, Face_Z_Plus);
	
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
        MenuFaceBuilder.build(CubeFaceNames.Face_Normal01, Face_Z_Minus);
    }

    public void createNormalFaces() {
	    Game.clearCubeFaceData();
	    Creator.fillPools();
	    MenuFaceBuilder.resetTransforms();

        MenuFaceBuilder.build(CubeFaceNames.Face_Easy01, Face_X_Plus);
	
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
        MenuFaceBuilder.build(CubeFaceNames.Face_Hard01, Face_X_Minus);
    
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.RotateCCW90);
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.RotateCCW90);
        MenuFaceBuilder.build(CubeFaceNames.Face_Normal04, Face_Y_Plus);

	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
        MenuFaceBuilder.build(CubeFaceNames.Face_Normal02, Face_Y_Minus);

	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorHoriz);
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
        MenuFaceBuilder.build(CubeFaceNames.Face_Normal03, Face_Z_Plus);
	
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
        MenuFaceBuilder.build(CubeFaceNames.Face_Normal01, Face_Z_Minus);
    }

    public void createHardFaces() {
	    Game.clearCubeFaceData();
	    Creator.fillPools();
	    MenuFaceBuilder.resetTransforms();
	
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.RotateCW90);
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.RotateCW90);
        MenuFaceBuilder.build(CubeFaceNames.Face_Hard03, Face_X_Plus);
	
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
        MenuFaceBuilder.build(CubeFaceNames.Face_Hard01, Face_X_Minus);
    
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.RotateCCW90);
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorHoriz);
        MenuFaceBuilder.build(CubeFaceNames.Face_Hard04, Face_Y_Plus);
	
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.RotateCCW90);
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorHoriz);
        MenuFaceBuilder.build(CubeFaceNames.Face_Hard02, Face_Y_Minus);

        MenuFaceBuilder.build(CubeFaceNames.Face_Menu, Face_Z_Plus);
	
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
        MenuFaceBuilder.build(CubeFaceNames.Face_Normal01, Face_Z_Minus);
    }

    public void init() {
        mNavigation = CubeFaceNavigationEnum.NoNavigation;
    
        cubeRotation.degree = 90.0f;
        cubeRotation.axis = new Vector(0.0f, 1.0f, 0.0f);
    
        m_secondary_rotation = false;
    
        cubeRotationSecondary.degree = 0.0f;
        cubeRotationSecondary.axis = new Vector(0.0f, 0.0f, 1.0f);
    }

    public void setup(CubeFaceNavigationEnum navigation) {
        mNavigation = navigation;
    
        cubeRotation.axis = new Vector(0.0f, 1.0f, 0.0f);
    
        final float divisor = 5.0f;
    
        switch (navigation) {
            case Hard3_To_Hard2:
                m_interpolator.setup(-180.0f, -90.0f, divisor);
                m_secondary_rotation = true;
                cubeRotationSecondary.degree = -180.0f;
                cubeRotationSecondary.axis = new Vector(0.0f, 0.0f, 1.0f);
                break;
            
            case Hard1_To_Hard4:
                m_interpolator.setup(0.0f, 90.0f, divisor);
                m_secondary_rotation = true;
                cubeRotationSecondary.degree = 0.0f;
                cubeRotationSecondary.axis = new Vector(0.0f, 0.0f, 1.0f);
                MenuFaceBuilder.build(CubeFaceNames.Face_Empty, Face_Z_Plus);
                MenuFaceBuilder.build(CubeFaceNames.Face_Empty, Face_Z_Minus);
                break;
            
            case Hard4_To_Hard1:
                m_interpolator.setup(-270.0f, -360.0f, divisor);
                m_secondary_rotation = true;
                cubeRotationSecondary.degree = -270.0f;
                cubeRotationSecondary.axis = new Vector(0.0f, 0.0f, 1.0f);
			    createHardFaces();
                break;
            
            case Hard4_To_Hard3:
                m_interpolator.setup(-270.0f, -180.0f, divisor);
                m_secondary_rotation = true;
                cubeRotationSecondary.degree = -270.0f;
                cubeRotationSecondary.axis = new Vector(0.0f, 0.0f, 1.0f);
                break;
            
            case Hard3_To_Hard4:
                m_interpolator.setup(-180.0f, -270.0f, divisor);
                m_secondary_rotation = true;
                cubeRotationSecondary.degree = -180.0f;
                cubeRotationSecondary.axis = new Vector(0.0f, 0.0f, 1.0f);
                break;
            
            case Hard2_To_Hard3:
                m_interpolator.setup(-90.0f, -180.0f, divisor);
                m_secondary_rotation = true;
                cubeRotationSecondary.degree = -90.0f;
                cubeRotationSecondary.axis = new Vector(0.0f, 0.0f, 1.0f);
                break;
        
            case Hard2_To_Hard1:
                m_interpolator.setup(-90.0f, 0.0f, divisor);
                m_secondary_rotation = true;
                cubeRotationSecondary.degree = -90.0f;
                cubeRotationSecondary.axis = new Vector(0.0f, 0.0f, 1.0f);
                createHardFaces();
			    break;
            
            case Hard1_To_Hard2:
                m_interpolator.setup(0.0f, -90.0f, divisor);
                m_secondary_rotation = true;
                cubeRotationSecondary.degree = 0.0f;
                cubeRotationSecondary.axis = new Vector(0.0f, 0.0f, 1.0f);
                MenuFaceBuilder.build(CubeFaceNames.Face_Empty, Face_Z_Plus);;
                MenuFaceBuilder.build(CubeFaceNames.Face_Empty, Face_Z_Minus);
                break;
            
            case Normal1_To_Normal4:
                m_interpolator.setup(0.0f, -90.0f, divisor);
                m_secondary_rotation = true;
                cubeRotationSecondary.degree = 0.0f;
                cubeRotationSecondary.axis = new Vector(1.0f, 0.0f, 0.0f);
                MenuFaceBuilder.build(CubeFaceNames.Face_Empty, Face_X_Plus);
                MenuFaceBuilder.build(CubeFaceNames.Face_Empty, Face_X_Minus);
                break;
            
            case Normal4_To_Normal1:
                m_interpolator.setup(270.0f, 360.0f, divisor);
                m_secondary_rotation = true;
                cubeRotationSecondary.degree = 270.0f;
                cubeRotationSecondary.axis = new Vector(1.0f, 0.0f, 0.0f);
			    createNormalFaces();
                break;
            
            case Normal4_To_Normal3:
                m_interpolator.setup(270.0f, 180.0f, divisor);
                m_secondary_rotation = true;
                cubeRotationSecondary.degree = 270.0f;
                cubeRotationSecondary.axis = new Vector(1.0f, 0.0f, 0.0f);
                break;
            
            case Normal3_To_Normal4:
                m_interpolator.setup(180.0f, 270.0f, divisor);
                m_secondary_rotation = true;
                cubeRotationSecondary.degree = 180.0f;
                cubeRotationSecondary.axis = new Vector(1.0f, 0.0f, 0.0f);
                break;
            
            case Normal3_To_Normal2:
                m_interpolator.setup(180.0f, 90.0f, divisor);
                m_secondary_rotation = true;
                cubeRotationSecondary.degree = 180.0f;
                cubeRotationSecondary.axis = new Vector(1.0f, 0.0f, 0.0f);
                break;
            
            case Normal2_To_Normal3:
                m_interpolator.setup(90.0f, 180.0f, divisor);
                m_secondary_rotation = true;
                cubeRotationSecondary.degree = 90.0f;
                cubeRotationSecondary.axis = new Vector(1.0f, 0.0f, 0.0f);
                break;
            
            case Normal2_To_Normal1:
                m_interpolator.setup(90.0f, 0.0f, divisor);
                m_secondary_rotation = true;
                cubeRotationSecondary.degree = 90.0f;
                cubeRotationSecondary.axis = new Vector(1.0f, 0.0f, 0.0f);
			    createNormalFaces();
                break;
            
            case Normal1_To_Normal2:
                m_interpolator.setup(0.0f, 90.0f, divisor);
                m_secondary_rotation = true;
                cubeRotationSecondary.degree = 0.0f;
                cubeRotationSecondary.axis = new Vector(1.0f, 0.0f, 0.0f);
                MenuFaceBuilder.build(CubeFaceNames.Face_Empty, Face_X_Plus);
                MenuFaceBuilder.build(CubeFaceNames.Face_Empty, Face_X_Minus);
                break;
            
            case Easy1_To_Easy4:
                m_interpolator.setup(0.0f, -90.0f, divisor);
                m_secondary_rotation = true;
                cubeRotationSecondary.degree = 0.0f;
                cubeRotationSecondary.axis = new Vector(0.0f, 0.0f, 1.0f);
                MenuFaceBuilder.build(CubeFaceNames.Face_Empty, Face_Z_Plus);
                MenuFaceBuilder.build(CubeFaceNames.Face_Empty, Face_Z_Minus);
                break;
            
            case Easy4_To_Easy1:
                m_interpolator.setup(270.0f, 360.0f, divisor);
                m_secondary_rotation = true;
                cubeRotationSecondary.degree = 270.0f;
                cubeRotationSecondary.axis = new Vector(0.0f, 0.0f, 1.0f);
			    createEasyFaces();
                break;
            
            case Easy4_To_Easy3:
                m_interpolator.setup(270.0f, 180.0f, divisor);
                m_secondary_rotation = true;
                cubeRotationSecondary.degree = 270.0f;
                cubeRotationSecondary.axis = new Vector(0.0f, 0.0f, 1.0f);
                break;
            
            case Easy3_To_Easy4:
                m_interpolator.setup(180.0f, 270.0f, divisor);
                m_secondary_rotation = true;
                cubeRotationSecondary.degree = 180.0f;
                cubeRotationSecondary.axis = new Vector(0.0f, 0.0f, 1.0f);
                break;
            
            case Easy3_To_Easy2:
                m_interpolator.setup(180.0f, 90.0f, divisor);
                m_secondary_rotation = true;
                cubeRotationSecondary.degree = 180.0f;
                cubeRotationSecondary.axis = new Vector(0.0f, 0.0f, 1.0f);
                break;
            
            case Easy2_To_Easy3:
                m_interpolator.setup(90.0f, 180.0f, divisor);
                m_secondary_rotation = true;
                cubeRotationSecondary.degree = 90.0f;
                cubeRotationSecondary.axis = new Vector(0.0f, 0.0f, 1.0f);
                break;
            
            case Easy1_To_Easy2:
                m_interpolator.setup(0.0f, 90.0f, divisor);
                m_secondary_rotation = true;
                cubeRotationSecondary.degree = 0.0f;
			    cubeRotationSecondary.axis = new Vector(0.0f, 0.0f, 1.0f);
                MenuFaceBuilder.build(CubeFaceNames.Face_Empty, Face_Z_Plus);
                MenuFaceBuilder.build(CubeFaceNames.Face_Empty, Face_Z_Minus);
                break;
            
            case Easy2_To_Easy1:
                m_interpolator.setup(90.0f, 0.0f, divisor);
                m_secondary_rotation = true;
                cubeRotationSecondary.degree = 0.0f;
			    cubeRotationSecondary.axis = new Vector(0.0f, 0.0f, 1.0f);
			    createEasyFaces();
                break;
            
            case Menu_To_Easy1:
                m_interpolator.setup(0.0f, -90.0f, divisor);
                createEasyFaces();
                break;
            
            case Easy1_To_Menu:
                m_interpolator.setup(-90.0f, 0.0f, divisor);
                createMenuFaces(false);
                break;
            
            case Easy1_To_Normal1:
                m_interpolator.setup(-90.0f, -180.0f, divisor);
                createNormalFaces();
                break;
            
            case Normal1_To_Easy1:
                m_interpolator.setup(-180.0f, -90.0f, divisor);
                createEasyFaces();
                break;
            
            case Normal1_To_Hard1:
                m_interpolator.setup(-180.0f, -270.0f, divisor);
                createHardFaces();
                break;
            
            case Hard1_To_Normal1:
                m_interpolator.setup(-270.0f, -180.0f, divisor);
                createNormalFaces();
                break;
           
		    case Tutorial_To_Menu:
                m_interpolator.setup(-270.0f, -360.0f, divisor);
			    createMenuFaces(true);
			    break;
			
            case Hard1_To_Menu:
                m_interpolator.setup(-270.0f, -360.0f, divisor);
                createMenuFaces(false);
                break;

   		    case Options_To_Menu:
                m_interpolator.setup(90.0f, 0.0f, divisor);
			    cubeRotation.axis = new Vector(1.0f, 0.0f, 0.0f);
                //Game.saveOptions();
                break;
            
            case Menu_To_Options:
                m_interpolator.setup(0.0f, 90.0f, divisor);
			    cubeRotation.axis = new Vector(1.0f, 0.0f, 0.0f);
                break;

   		    case Menu_To_Store:
                m_interpolator.setup(0.0f, -90.0f, divisor);
			    cubeRotation.axis = new Vector(1.0f, 0.0f, 0.0f);
                break;
            
		    case Store_To_Menu:
                m_interpolator.setup(-90.0f, 0.0f, divisor);
			    cubeRotation.axis = new Vector(1.0f, 0.0f, 0.0f);
                break;
            
            default:
                break;
        }
    
        Game.buildVisibleCubesListOnlyOnFaces(Game.menu.mCubesFace);
    }

    public void update() {
        m_interpolator.interpolate();
    
        if (m_secondary_rotation) {
            cubeRotationSecondary.degree = m_interpolator.getValue();
        } else {
            cubeRotation.degree = m_interpolator.getValue();
        }
    
        switch (mNavigation) {
            case Hard3_To_Hard2:            
                if ( ( Math.abs(cubeRotationSecondary.degree) - 90.0f) < EPSILON) {
                    cubeRotationSecondary.degree = -90.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Hard1_To_Hard4: 
                if ( (90.0f - cubeRotationSecondary.degree) < EPSILON) {
                    cubeRotationSecondary.degree = -270.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Hard4_To_Hard1: 
                if ( 360.0f - Math.abs(cubeRotationSecondary.degree) < EPSILON) {
                    cubeRotationSecondary.degree = 0.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Hard4_To_Hard3:            
                if ( Math.abs(cubeRotationSecondary.degree) - 180.0f < EPSILON) {
                    cubeRotationSecondary.degree = -180.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Hard3_To_Hard4:
                if ( 270.0f - Math.abs(cubeRotationSecondary.degree) < EPSILON) {
                    cubeRotationSecondary.degree = -270.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Hard2_To_Hard3:
                if ( 180.0f - Math.abs(cubeRotationSecondary.degree) < EPSILON) {
                    cubeRotationSecondary.degree = -180.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
                
            case Hard2_To_Hard1:            
                if ( Math.abs(cubeRotationSecondary.degree) < EPSILON) {
                    cubeRotationSecondary.degree = 0.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Hard1_To_Hard2:            
                if ( (90.0f - Math.abs(cubeRotationSecondary.degree)) < EPSILON) {
                    cubeRotationSecondary.degree = -90.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;            
            
            case Normal1_To_Normal4:             
                if ( (90.0f - Math.abs(cubeRotationSecondary.degree)) < EPSILON) {
                    cubeRotationSecondary.degree = 270.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Normal4_To_Normal1:             
                if ( (360.0f - cubeRotationSecondary.degree) < EPSILON) {
                    cubeRotationSecondary.degree = 0.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Normal4_To_Normal3:             
                if ( (cubeRotationSecondary.degree - 180.0f) < EPSILON) {
                    cubeRotationSecondary.degree = 180.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Normal3_To_Normal4:
                if ( (270.0f - cubeRotationSecondary.degree) < EPSILON) {
                    cubeRotationSecondary.degree = 270.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Normal3_To_Normal2:             
                if ( (cubeRotationSecondary.degree - 90.0f) < EPSILON) {
                    cubeRotationSecondary.degree = 90.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Normal2_To_Normal3:                
                if ( (180.0f - cubeRotationSecondary.degree) < EPSILON) {
                    cubeRotationSecondary.degree = 180.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Normal2_To_Normal1:             
                if ( (cubeRotationSecondary.degree) < EPSILON) {
                    cubeRotationSecondary.degree = 0.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Normal1_To_Normal2:            
                if ( (90.0f - Math.abs(cubeRotationSecondary.degree)) < EPSILON) {
                    cubeRotationSecondary.degree = 90.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Easy1_To_Easy4:            
                if ( (90.0f - Math.abs(cubeRotationSecondary.degree)) < EPSILON) {
                    cubeRotationSecondary.degree = 270.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }
                break;
            
            case Easy4_To_Easy1:            
                if ( (360.0f - cubeRotationSecondary.degree) < EPSILON) {
                    cubeRotationSecondary.degree = 0.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;                
                }            
                break;
            
            case Easy4_To_Easy3:            
                if ( (cubeRotationSecondary.degree - 180.0f) < EPSILON) {
                    cubeRotationSecondary.degree = 180.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }                        
                break;
            
            case Easy3_To_Easy4:            
                if ( (270.0f - cubeRotationSecondary.degree) < EPSILON) {
                    cubeRotationSecondary.degree = 270.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Easy3_To_Easy2:            
                if ( (cubeRotationSecondary.degree - 90.0f) < EPSILON) {
                    cubeRotationSecondary.degree = 90.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Easy2_To_Easy3:             
                if ( (180.0f - cubeRotationSecondary.degree) < EPSILON) {
                    cubeRotationSecondary.degree = 180.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Easy1_To_Easy2:            
                if ( Math.abs(Math.abs(90.0f) - Math.abs(cubeRotationSecondary.degree)) < EPSILON) {
                    cubeRotationSecondary.degree = 90.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;                
                }            
                break;
            
            case Easy2_To_Easy1:            
                if ( cubeRotationSecondary.degree < EPSILON ) {
                    cubeRotationSecondary.degree = 0.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Menu_To_Easy1:            
                if ( (Math.abs(-90.0f) - Math.abs(cubeRotation.degree))  < EPSILON) {
                    cubeRotation.degree = -90.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                }            
                break;
            
            case Easy1_To_Menu:            
                if ( Math.abs(0.0f - Math.abs(cubeRotation.degree)) < EPSILON) {
                    cubeRotation.degree = 0.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                }            
                break;
            
            case Easy1_To_Normal1:            
                if ( Math.abs(180.0f - Math.abs(cubeRotation.degree)) < EPSILON) {
                    cubeRotation.degree = -180.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                }            
                break;
            
            case Normal1_To_Easy1:            
                if ( Math.abs(Math.abs(-90.0f) - Math.abs(cubeRotation.degree)) < EPSILON) {
                    cubeRotation.degree = -90.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                }            
                break;
            
            case Normal1_To_Hard1:            
                if ( Math.abs(Math.abs(-270.0f) - Math.abs(cubeRotation.degree)) < EPSILON) {
                    cubeRotation.degree = -270.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                }            
                break;
            
            case Hard1_To_Normal1:            
                if ( Math.abs(Math.abs(-180.0f) - Math.abs(cubeRotation.degree)) < EPSILON) {
                    cubeRotation.degree = -180.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                }            
                break;
            
		    case Tutorial_To_Menu:			
            case Hard1_To_Menu:            
                if ( Math.abs(Math.abs(-360.0f) - Math.abs(cubeRotation.degree)) < EPSILON) {
                    cubeRotation.degree = 0.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                }            
                break;
			
		    case Menu_To_Options:            
                if ( Math.abs(90.0f - Math.abs(cubeRotation.degree)) < EPSILON) {
                    cubeRotation.degree = 90.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                }            
			    break;
            
		    case Options_To_Menu:            
                if ( Math.abs(0.0f - Math.abs(cubeRotation.degree)) < EPSILON) {
                    cubeRotation.degree = 0.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    cubeRotation.axis = new Vector(0.0f, 1.0f, 0.0f);
                }            
			    break;
            
		    case Menu_To_Store:            
                if ( Math.abs(Math.abs(-90.0f) - Math.abs(cubeRotation.degree)) < EPSILON) {
                    cubeRotation.degree = -90.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                }            
			    break;
            
		    case Store_To_Menu:            
                if ( Math.abs(0.0f - Math.abs(cubeRotation.degree)) < EPSILON) {
                    cubeRotation.degree = 0.0f;
                    cubeRotation.axis = new Vector(0.0f, 1.0f, 0.0f);
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                }            
			    break;
			
		    case NoNavigation:
			    break;
    } // switch
}

}
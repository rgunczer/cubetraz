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
    private final EaseOutDivideInterpolation mInterpolator = new EaseOutDivideInterpolation();
    private boolean mSecondaryRotation;
    
    public CubeRotation primaryRotation = new CubeRotation();
    public CubeRotation secondaryRotation = new CubeRotation();
   
    private CubeFaceNavigationEnum mNavigation;
	
     boolean isCurrentNavigation(CubeFaceNavigationEnum navigation) {
        return (navigation == mNavigation);
     }
    
    void applyRotations() {
        glRotatef(primaryRotation.degree, primaryRotation.axis.x, primaryRotation.axis.y, primaryRotation.axis.z);
        glRotatef(secondaryRotation.degree, secondaryRotation.axis.x, secondaryRotation.axis.y, secondaryRotation.axis.z);
    }

    private String getAxisFromVector(Vector vec) {
        if (vec.x == 1f && vec.y == 0f && vec.z == 0f) {
            return "Game.vectorXaxis";
        } else if (vec.x == 0f && vec.y == 1f && vec.z == 0f) {
            return "Game.vectorYaxis";
        } else if (vec.x == 0f && vec.y == 0f && vec.z == 1f) {
            return "Game.vectorZaxis";
        }
        return "";
    }

    void dump() {
        System.out.println("mNavigator.primaryRotation.init(" + primaryRotation.degree + "f,  " + getAxisFromVector(primaryRotation.axis) + " );");
        System.out.println("mNavigator.secondaryRotation.init(" + secondaryRotation.degree + "f, " + getAxisFromVector(secondaryRotation.axis) + ");");
    }
            
     void createMenuFaces(boolean tutorial) {
	    Game.clearCubeFaceData();
	    Creator.fillPools();
	    MenuFaceBuilder.resetTransforms();
		
        MenuFaceBuilder.build(CubeFaceNames.Face_Easy01, X_Plus);
	
	    if (tutorial) {
		    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
		    MenuFaceBuilder.build(CubeFaceNames.Face_Tutorial, X_Minus);
	    } else {
		    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
		    MenuFaceBuilder.build(CubeFaceNames.Face_Hard01, X_Minus);
	    }
    
  	    MenuFaceBuilder.build(CubeFaceNames.Face_Menu, Z_Plus);
        MenuFaceBuilder.build(CubeFaceNames.Face_Empty, Z_Minus);
	
	    MenuFaceBuilder.build(CubeFaceNames.Face_Options, Y_Plus);
	
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorHoriz);
        MenuFaceBuilder.build(CubeFaceNames.Face_Score, Y_Minus);
    }

     void createEasyFaces() {
	    Game.clearCubeFaceData();
	    Creator.fillPools();
	    MenuFaceBuilder.resetTransforms();
	
        MenuFaceBuilder.build(CubeFaceNames.Face_Easy01, X_Plus);
	
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.RotateCW90);
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorHoriz);
        MenuFaceBuilder.build(CubeFaceNames.Face_Easy02, Y_Minus);

	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorHoriz);
        MenuFaceBuilder.build(CubeFaceNames.Face_Easy03, X_Minus);
    
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.RotateCCW90);
        MenuFaceBuilder.build(CubeFaceNames.Face_Easy04, Y_Plus);
	
        MenuFaceBuilder.build(CubeFaceNames.Face_Menu, Z_Plus);
	
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
        MenuFaceBuilder.build(CubeFaceNames.Face_Normal01, Z_Minus);
    }

     void createNormalFaces() {
	    Game.clearCubeFaceData();
	    Creator.fillPools();
	    MenuFaceBuilder.resetTransforms();

        MenuFaceBuilder.build(CubeFaceNames.Face_Easy01, X_Plus);
	
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
        MenuFaceBuilder.build(CubeFaceNames.Face_Hard01, X_Minus);
    
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.RotateCCW90);
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.RotateCCW90);
        MenuFaceBuilder.build(CubeFaceNames.Face_Normal04, Y_Plus);

	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
        MenuFaceBuilder.build(CubeFaceNames.Face_Normal02, Y_Minus);

	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorHoriz);
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
        MenuFaceBuilder.build(CubeFaceNames.Face_Normal03, Z_Plus);
	
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
        MenuFaceBuilder.build(CubeFaceNames.Face_Normal01, Z_Minus);
    }

    void createHardFaces() {
	    Game.clearCubeFaceData();
	    Creator.fillPools();
	    MenuFaceBuilder.resetTransforms();
	
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.RotateCW90);
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.RotateCW90);
        MenuFaceBuilder.build(CubeFaceNames.Face_Hard03, X_Plus);
	
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
        MenuFaceBuilder.build(CubeFaceNames.Face_Hard01, X_Minus);
    
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.RotateCCW90);
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorHoriz);
        MenuFaceBuilder.build(CubeFaceNames.Face_Hard04, Y_Plus);
	
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.RotateCCW90);
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorHoriz);
        MenuFaceBuilder.build(CubeFaceNames.Face_Hard02, Y_Minus);

        MenuFaceBuilder.build(CubeFaceNames.Face_Menu, Z_Plus);
	
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
        MenuFaceBuilder.build(CubeFaceNames.Face_Normal01, Z_Minus);
    }

    public void init() {
        mNavigation = CubeFaceNavigationEnum.NoNavigation;
    
        primaryRotation.degree = 90.0f;
        primaryRotation.axis = new Vector(0.0f, 1.0f, 0.0f);
    
        mSecondaryRotation = false;
    
        secondaryRotation.degree = 0.0f;
        secondaryRotation.axis = new Vector(0.0f, 0.0f, 1.0f);
    }

    public void setup(CubeFaceNavigationEnum navigation) {
        mNavigation = navigation;
    
        primaryRotation.axis = new Vector(0.0f, 1.0f, 0.0f);
    
        final float divisor = 5.0f;
    
        switch (navigation) {
            case Hard3_To_Hard2:
                mInterpolator.setup(-180.0f, -90.0f, divisor);
                mSecondaryRotation = true;
                secondaryRotation.degree = -180.0f;
                secondaryRotation.axis = new Vector(0.0f, 0.0f, 1.0f);
                break;
            
            case Hard1_To_Hard4:
                mInterpolator.setup(0.0f, 90.0f, divisor);
                mSecondaryRotation = true;
                secondaryRotation.degree = 0.0f;
                secondaryRotation.axis = new Vector(0.0f, 0.0f, 1.0f);
                MenuFaceBuilder.build(CubeFaceNames.Face_Empty, Z_Plus);
                MenuFaceBuilder.build(CubeFaceNames.Face_Empty, Z_Minus);
                break;
            
            case Hard4_To_Hard1:
                mInterpolator.setup(-270.0f, -360.0f, divisor);
                mSecondaryRotation = true;
                secondaryRotation.degree = -270.0f;
                secondaryRotation.axis = new Vector(0.0f, 0.0f, 1.0f);
			    createHardFaces();
                break;
            
            case Hard4_To_Hard3:
                mInterpolator.setup(-270.0f, -180.0f, divisor);
                mSecondaryRotation = true;
                secondaryRotation.degree = -270.0f;
                secondaryRotation.axis = new Vector(0.0f, 0.0f, 1.0f);
                break;
            
            case Hard3_To_Hard4:
                mInterpolator.setup(-180.0f, -270.0f, divisor);
                mSecondaryRotation = true;
                secondaryRotation.degree = -180.0f;
                secondaryRotation.axis = new Vector(0.0f, 0.0f, 1.0f);
                break;
            
            case Hard2_To_Hard3:
                mInterpolator.setup(-90.0f, -180.0f, divisor);
                mSecondaryRotation = true;
                secondaryRotation.degree = -90.0f;
                secondaryRotation.axis = new Vector(0.0f, 0.0f, 1.0f);
                break;
        
            case Hard2_To_Hard1:
                mInterpolator.setup(-90.0f, 0.0f, divisor);
                mSecondaryRotation = true;
                secondaryRotation.degree = -90.0f;
                secondaryRotation.axis = new Vector(0.0f, 0.0f, 1.0f);
                createHardFaces();
			    break;
            
            case Hard1_To_Hard2:
                mInterpolator.setup(0.0f, -90.0f, divisor);
                mSecondaryRotation = true;
                secondaryRotation.degree = 0.0f;
                secondaryRotation.axis = new Vector(0.0f, 0.0f, 1.0f);
                MenuFaceBuilder.build(CubeFaceNames.Face_Empty, Z_Plus);
                MenuFaceBuilder.build(CubeFaceNames.Face_Empty, Z_Minus);
                break;
            
            case Normal1_To_Normal4:
                mInterpolator.setup(0.0f, -90.0f, divisor);
                mSecondaryRotation = true;
                secondaryRotation.degree = 0.0f;
                secondaryRotation.axis = new Vector(1.0f, 0.0f, 0.0f);
                MenuFaceBuilder.build(CubeFaceNames.Face_Empty, X_Plus);
                MenuFaceBuilder.build(CubeFaceNames.Face_Empty, X_Minus);
                break;
            
            case Normal4_To_Normal1:
                mInterpolator.setup(270.0f, 360.0f, divisor);
                mSecondaryRotation = true;
                secondaryRotation.degree = 270.0f;
                secondaryRotation.axis = new Vector(1.0f, 0.0f, 0.0f);
			    createNormalFaces();
                break;
            
            case Normal4_To_Normal3:
                mInterpolator.setup(270.0f, 180.0f, divisor);
                mSecondaryRotation = true;
                secondaryRotation.degree = 270.0f;
                secondaryRotation.axis = new Vector(1.0f, 0.0f, 0.0f);
                break;
            
            case Normal3_To_Normal4:
                mInterpolator.setup(180.0f, 270.0f, divisor);
                mSecondaryRotation = true;
                secondaryRotation.degree = 180.0f;
                secondaryRotation.axis = new Vector(1.0f, 0.0f, 0.0f);
                break;
            
            case Normal3_To_Normal2:
                mInterpolator.setup(180.0f, 90.0f, divisor);
                mSecondaryRotation = true;
                secondaryRotation.degree = 180.0f;
                secondaryRotation.axis = new Vector(1.0f, 0.0f, 0.0f);
                break;
            
            case Normal2_To_Normal3:
                mInterpolator.setup(90.0f, 180.0f, divisor);
                mSecondaryRotation = true;
                secondaryRotation.degree = 90.0f;
                secondaryRotation.axis = new Vector(1.0f, 0.0f, 0.0f);
                break;
            
            case Normal2_To_Normal1:
                mInterpolator.setup(90.0f, 0.0f, divisor);
                mSecondaryRotation = true;
                secondaryRotation.degree = 90.0f;
                secondaryRotation.axis = new Vector(1.0f, 0.0f, 0.0f);
			    createNormalFaces();
                break;
            
            case Normal1_To_Normal2:
                mInterpolator.setup(0.0f, 90.0f, divisor);
                mSecondaryRotation = true;
                secondaryRotation.degree = 0.0f;
                secondaryRotation.axis = new Vector(1.0f, 0.0f, 0.0f);
                MenuFaceBuilder.build(CubeFaceNames.Face_Empty, X_Plus);
                MenuFaceBuilder.build(CubeFaceNames.Face_Empty, X_Minus);
                break;
            
            case Easy1_To_Easy4:
                mInterpolator.setup(0.0f, -90.0f, divisor);
                mSecondaryRotation = true;
                secondaryRotation.degree = 0.0f;
                secondaryRotation.axis = new Vector(0.0f, 0.0f, 1.0f);
                MenuFaceBuilder.build(CubeFaceNames.Face_Empty, Z_Plus);
                MenuFaceBuilder.build(CubeFaceNames.Face_Empty, Z_Minus);
                break;
            
            case Easy4_To_Easy1:
                mInterpolator.setup(270.0f, 360.0f, divisor);
                mSecondaryRotation = true;
                secondaryRotation.degree = 270.0f;
                secondaryRotation.axis = new Vector(0.0f, 0.0f, 1.0f);
			    createEasyFaces();
                break;
            
            case Easy4_To_Easy3:
                mInterpolator.setup(270.0f, 180.0f, divisor);
                mSecondaryRotation = true;
                secondaryRotation.degree = 270.0f;
                secondaryRotation.axis = new Vector(0.0f, 0.0f, 1.0f);
                break;
            
            case Easy3_To_Easy4:
                mInterpolator.setup(180.0f, 270.0f, divisor);
                mSecondaryRotation = true;
                secondaryRotation.degree = 180.0f;
                secondaryRotation.axis = new Vector(0.0f, 0.0f, 1.0f);
                break;
            
            case Easy3_To_Easy2:
                mInterpolator.setup(180.0f, 90.0f, divisor);
                mSecondaryRotation = true;
                secondaryRotation.degree = 180.0f;
                secondaryRotation.axis = new Vector(0.0f, 0.0f, 1.0f);
                break;
            
            case Easy2_To_Easy3:
                mInterpolator.setup(90.0f, 180.0f, divisor);
                mSecondaryRotation = true;
                secondaryRotation.degree = 90.0f;
                secondaryRotation.axis = new Vector(0.0f, 0.0f, 1.0f);
                break;
            
            case Easy1_To_Easy2:
                mInterpolator.setup(0.0f, 90.0f, divisor);
                mSecondaryRotation = true;
                secondaryRotation.degree = 0.0f;
			    secondaryRotation.axis = new Vector(0.0f, 0.0f, 1.0f);
                MenuFaceBuilder.build(CubeFaceNames.Face_Empty, Z_Plus);
                MenuFaceBuilder.build(CubeFaceNames.Face_Empty, Z_Minus);
                break;
            
            case Easy2_To_Easy1:
                mInterpolator.setup(90.0f, 0.0f, divisor);
                mSecondaryRotation = true;
                secondaryRotation.degree = 0.0f;
			    secondaryRotation.axis = new Vector(0.0f, 0.0f, 1.0f);
			    createEasyFaces();
                break;
            
            case Menu_To_Easy1:
                mInterpolator.setup(0.0f, -90.0f, divisor);
                createEasyFaces();
                break;
            
            case Easy1_To_Menu:
                mInterpolator.setup(-90.0f, 0.0f, divisor);
                createMenuFaces(false);
                break;
            
            case Easy1_To_Normal1:
                mInterpolator.setup(-90.0f, -180.0f, divisor);
                createNormalFaces();
                break;
            
            case Normal1_To_Easy1:
                mInterpolator.setup(-180.0f, -90.0f, divisor);
                createEasyFaces();
                break;
            
            case Normal1_To_Hard1:
                mInterpolator.setup(-180.0f, -270.0f, divisor);
                createHardFaces();
                break;
            
            case Hard1_To_Normal1:
                mInterpolator.setup(-270.0f, -180.0f, divisor);
                createNormalFaces();
                break;
           
		    case Tutorial_To_Menu:
                mInterpolator.setup(-270.0f, -360.0f, divisor);
			    createMenuFaces(true);
			    break;
			
            case Hard1_To_Menu:
                mInterpolator.setup(-270.0f, -360.0f, divisor);
                createMenuFaces(false);
                break;

   		    case Options_To_Menu:
                mInterpolator.setup(90.0f, 0.0f, divisor);
			    primaryRotation.axis = new Vector(1.0f, 0.0f, 0.0f);
                //Game.saveOptions();
                break;
            
            case Menu_To_Options:
                mInterpolator.setup(0.0f, 90.0f, divisor);
			    primaryRotation.axis = new Vector(1.0f, 0.0f, 0.0f);
                break;

   		    case Menu_To_Store:
                mInterpolator.setup(0.0f, -90.0f, divisor);
			    primaryRotation.axis = new Vector(1.0f, 0.0f, 0.0f);
                break;
            
		    case Store_To_Menu:
                mInterpolator.setup(-90.0f, 0.0f, divisor);
			    primaryRotation.axis = new Vector(1.0f, 0.0f, 0.0f);
                break;
            
            default:
                break;
        }
    
        Game.buildVisibleCubesListOnlyOnFaces(Game.menu.mCubesFace);
    }

    public void update() {
        mInterpolator.interpolate();
    
        if (mSecondaryRotation) {
            secondaryRotation.degree = mInterpolator.getValue();
        } else {
            primaryRotation.degree = mInterpolator.getValue();
        }
    
        switch (mNavigation) {
            case Hard3_To_Hard2:            
                if ( ( Math.abs(secondaryRotation.degree) - 90.0f) < EPSILON) {
                    secondaryRotation.degree = -90.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    mSecondaryRotation = false;
                }            
                break;
            
            case Hard1_To_Hard4: 
                if ( (90.0f - secondaryRotation.degree) < EPSILON) {
                    secondaryRotation.degree = -270.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    mSecondaryRotation = false;
                }            
                break;
            
            case Hard4_To_Hard1: 
                if ( 360.0f - Math.abs(secondaryRotation.degree) < EPSILON) {
                    secondaryRotation.degree = 0.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    mSecondaryRotation = false;
                }            
                break;
            
            case Hard4_To_Hard3:            
                if ( Math.abs(secondaryRotation.degree) - 180.0f < EPSILON) {
                    secondaryRotation.degree = -180.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    mSecondaryRotation = false;
                }            
                break;
            
            case Hard3_To_Hard4:
                if ( 270.0f - Math.abs(secondaryRotation.degree) < EPSILON) {
                    secondaryRotation.degree = -270.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    mSecondaryRotation = false;
                }            
                break;
            
            case Hard2_To_Hard3:
                if ( 180.0f - Math.abs(secondaryRotation.degree) < EPSILON) {
                    secondaryRotation.degree = -180.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    mSecondaryRotation = false;
                }            
                break;
                
            case Hard2_To_Hard1:            
                if ( Math.abs(secondaryRotation.degree) < EPSILON) {
                    secondaryRotation.degree = 0.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    mSecondaryRotation = false;
                }            
                break;
            
            case Hard1_To_Hard2:            
                if ( (90.0f - Math.abs(secondaryRotation.degree)) < EPSILON) {
                    secondaryRotation.degree = -90.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    mSecondaryRotation = false;
                }            
                break;            
            
            case Normal1_To_Normal4:             
                if ( (90.0f - Math.abs(secondaryRotation.degree)) < EPSILON) {
                    secondaryRotation.degree = 270.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    mSecondaryRotation = false;
                }            
                break;
            
            case Normal4_To_Normal1:             
                if ( (360.0f - secondaryRotation.degree) < EPSILON) {
                    secondaryRotation.degree = 0.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    mSecondaryRotation = false;
                }            
                break;
            
            case Normal4_To_Normal3:             
                if ( (secondaryRotation.degree - 180.0f) < EPSILON) {
                    secondaryRotation.degree = 180.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    mSecondaryRotation = false;
                }            
                break;
            
            case Normal3_To_Normal4:
                if ( (270.0f - secondaryRotation.degree) < EPSILON) {
                    secondaryRotation.degree = 270.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    mSecondaryRotation = false;
                }            
                break;
            
            case Normal3_To_Normal2:             
                if ( (secondaryRotation.degree - 90.0f) < EPSILON) {
                    secondaryRotation.degree = 90.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    mSecondaryRotation = false;
                }            
                break;
            
            case Normal2_To_Normal3:                
                if ( (180.0f - secondaryRotation.degree) < EPSILON) {
                    secondaryRotation.degree = 180.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    mSecondaryRotation = false;
                }            
                break;
            
            case Normal2_To_Normal1:             
                if ( (secondaryRotation.degree) < EPSILON) {
                    secondaryRotation.degree = 0.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    mSecondaryRotation = false;
                }            
                break;
            
            case Normal1_To_Normal2:            
                if ( (90.0f - Math.abs(secondaryRotation.degree)) < EPSILON) {
                    secondaryRotation.degree = 90.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    mSecondaryRotation = false;
                }            
                break;
            
            case Easy1_To_Easy4:            
                if ( (90.0f - Math.abs(secondaryRotation.degree)) < EPSILON) {
                    secondaryRotation.degree = 270.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    mSecondaryRotation = false;
                }
                break;
            
            case Easy4_To_Easy1:            
                if ( (360.0f - secondaryRotation.degree) < EPSILON) {
                    secondaryRotation.degree = 0.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    mSecondaryRotation = false;
                }            
                break;
            
            case Easy4_To_Easy3:            
                if ( (secondaryRotation.degree - 180.0f) < EPSILON) {
                    secondaryRotation.degree = 180.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    mSecondaryRotation = false;
                }                        
                break;
            
            case Easy3_To_Easy4:            
                if ( (270.0f - secondaryRotation.degree) < EPSILON) {
                    secondaryRotation.degree = 270.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    mSecondaryRotation = false;
                }            
                break;
            
            case Easy3_To_Easy2:            
                if ( (secondaryRotation.degree - 90.0f) < EPSILON) {
                    secondaryRotation.degree = 90.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    mSecondaryRotation = false;
                }            
                break;
            
            case Easy2_To_Easy3:             
                if ( (180.0f - secondaryRotation.degree) < EPSILON) {
                    secondaryRotation.degree = 180.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    mSecondaryRotation = false;
                }            
                break;
            
            case Easy1_To_Easy2:            
                if ( Math.abs(Math.abs(90.0f) - Math.abs(secondaryRotation.degree)) < EPSILON) {
                    secondaryRotation.degree = 90.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    mSecondaryRotation = false;
                }            
                break;
            
            case Easy2_To_Easy1:            
                if ( secondaryRotation.degree < EPSILON ) {
                    secondaryRotation.degree = 0.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    mSecondaryRotation = false;
                }            
                break;
            
            case Menu_To_Easy1:            
                if ( (Math.abs(-90.0f) - Math.abs(primaryRotation.degree))  < EPSILON) {
                    primaryRotation.degree = -90.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                }            
                break;
            
            case Easy1_To_Menu:            
                if ( Math.abs(0.0f - Math.abs(primaryRotation.degree)) < EPSILON) {
                    primaryRotation.degree = 0.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                }            
                break;
            
            case Easy1_To_Normal1:            
                if ( Math.abs(180.0f - Math.abs(primaryRotation.degree)) < EPSILON) {
                    primaryRotation.degree = -180.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                }            
                break;
            
            case Normal1_To_Easy1:            
                if ( Math.abs(Math.abs(-90.0f) - Math.abs(primaryRotation.degree)) < EPSILON) {
                    primaryRotation.degree = -90.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                }            
                break;
            
            case Normal1_To_Hard1:            
                if ( Math.abs(Math.abs(-270.0f) - Math.abs(primaryRotation.degree)) < EPSILON) {
                    primaryRotation.degree = -270.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                }            
                break;
            
            case Hard1_To_Normal1:            
                if ( Math.abs(Math.abs(-180.0f) - Math.abs(primaryRotation.degree)) < EPSILON) {
                    primaryRotation.degree = -180.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                }            
                break;
            
		    case Tutorial_To_Menu:			
            case Hard1_To_Menu:            
                if ( Math.abs(Math.abs(-360.0f) - Math.abs(primaryRotation.degree)) < EPSILON) {
                    primaryRotation.degree = 0.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                }            
                break;
			
		    case Menu_To_Options:            
                if ( Math.abs(90.0f - Math.abs(primaryRotation.degree)) < EPSILON) {
                    primaryRotation.degree = 90.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                }            
			    break;
            
		    case Options_To_Menu:            
                if ( Math.abs(0.0f - Math.abs(primaryRotation.degree)) < EPSILON) {
                    primaryRotation.degree = 0.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                    primaryRotation.axis = new Vector(0.0f, 1.0f, 0.0f);
                }            
			    break;
            
		    case Menu_To_Store:            
                if ( Math.abs(Math.abs(-90.0f) - Math.abs(primaryRotation.degree)) < EPSILON) {
                    primaryRotation.degree = -90.0f;
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                }            
			    break;
            
		    case Store_To_Menu:            
                if ( Math.abs(0.0f - Math.abs(primaryRotation.degree)) < EPSILON) {
                    primaryRotation.degree = 0.0f;
                    primaryRotation.axis = new Vector(0.0f, 1.0f, 0.0f);
                    mNavigation = CubeFaceNavigationEnum.NoNavigation;
                }            
			    break;
			
		    case NoNavigation:
			    break;
        }
    }

}
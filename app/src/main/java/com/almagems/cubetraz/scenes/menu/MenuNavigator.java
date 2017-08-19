package com.almagems.cubetraz.scenes.menu;

import com.almagems.cubetraz.scenes.Creator;
import com.almagems.cubetraz.utils.CubeRotation;
import com.almagems.cubetraz.utils.EaseOutDivideInterpolation;
import com.almagems.cubetraz.game.Game;
import com.almagems.cubetraz.math.Vector;

import static android.opengl.GLES10.*;

import static com.almagems.cubetraz.game.Game.*;


public final class MenuNavigator
{    
    private final EaseOutDivideInterpolation m_interpolator = new EaseOutDivideInterpolation();
    private boolean m_secondary_rotation;
    
    public CubeRotation m_cube_rotation = new CubeRotation();
    public CubeRotation m_cube_rotation_secondary = new CubeRotation();
   
    private CubeFaceNavigationEnum m_navigation;
    
    public Menu m_menu;
        
	public CubeRotation getCurrentRotation() {
		return m_cube_rotation;
	}
	
    public boolean isCurrentNavigation(CubeFaceNavigationEnum navigation) {
        if (navigation == m_navigation) {
            return true;
        } else {
            return false;
        }
    }
    
    public void applyRotations() {
        glRotatef(m_cube_rotation.degree, m_cube_rotation.axis.x, m_cube_rotation.axis.y, m_cube_rotation.axis.z);
        glRotatef(m_cube_rotation_secondary.degree, m_cube_rotation_secondary.axis.x, m_cube_rotation_secondary.axis.y, m_cube_rotation_secondary.axis.z);
    }
            
    public void createMenuFaces(boolean tutorial) {
        //printf("\nCreateMenuFaces");
	
	    Game.clearCubeFaceData();
	    Creator.fillPools();
	    MenuFaceBuilder.resetTransforms();
		
        MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Easy01, Face_X_Plus);
	
	    if (tutorial) {
		    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
		    MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Tutorial, Face_X_Minus);
	    } else {
		    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
		    MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Hard01, Face_X_Minus);
	    }
    
  	    MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Menu, Face_Z_Plus);
        MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Empty, Face_Z_Minus);
	
	    MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Options, Face_Y_Plus);
	
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorHoriz);
        MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Score, Face_Y_Minus);
    }

    public void createEasyFaces() {
        //printf("\nCreateEasyFaces");

	    Game.clearCubeFaceData();
	    Creator.fillPools();
	    MenuFaceBuilder.resetTransforms();
	
        MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Easy01, Face_X_Plus);
	
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.RotateCW90);
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorHoriz);
        MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Easy02, Face_Y_Minus);

	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorHoriz);
        MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Easy03, Face_X_Minus);
    
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.RotateCCW90);
        MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Easy04, Face_Y_Plus);
	
        MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Menu, Face_Z_Plus);
	
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
        MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Normal01, Face_Z_Minus);
    }

    public void createNormalFaces() {
        //printf("\nCreateNormalFaces");

	    Game.clearCubeFaceData();
	    Creator.fillPools();
	    MenuFaceBuilder.resetTransforms();

        MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Easy01, Face_X_Plus);
	
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
        MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Hard01, Face_X_Minus);
    
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.RotateCCW90);
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.RotateCCW90);
        MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Normal04, Face_Y_Plus);

	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
        MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Normal02, Face_Y_Minus);

	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorHoriz);
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
        MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Normal03, Face_Z_Plus);
	
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
        MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Normal01, Face_Z_Minus);
    }

    public void createHardFaces() {
        //printf("\nCreateHardFaces");

	    Game.clearCubeFaceData();
	    Creator.fillPools();
	    MenuFaceBuilder.resetTransforms();
	
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.RotateCW90);
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.RotateCW90);
        MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Hard03, Face_X_Plus);
	
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
        MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Hard01, Face_X_Minus);
    
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.RotateCCW90);
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorHoriz);
        MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Hard04, Face_Y_Plus);
	
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.RotateCCW90);
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorHoriz);
        MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Hard02, Face_Y_Minus);

        MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Menu, Face_Z_Plus);
	
	    MenuFaceBuilder.addTransform(FaceTransformsEnum.MirrorVert);
        MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Normal01, Face_Z_Minus);
    }

    public void init(Menu menu) {
        m_menu = menu;    
        m_navigation = CubeFaceNavigationEnum.NoNavigation;
    
        m_cube_rotation.degree = 90.0f;
        m_cube_rotation.axis = new Vector(0.0f, 1.0f, 0.0f);
    
        m_secondary_rotation = false;
    
        m_cube_rotation_secondary.degree = 0.0f;
        m_cube_rotation_secondary.axis = new Vector(0.0f, 0.0f, 1.0f);   
    }

    public void setup(CubeFaceNavigationEnum navigation) {
        m_navigation = navigation;
    
        m_cube_rotation.axis = new Vector(0.0f, 1.0f, 0.0f);
    
        final float divisor = 5.0f;
    
        switch (navigation) {
            case Hard3_To_Hard2:
                m_interpolator.setup(-180.0f, -90.0f, divisor);
                m_secondary_rotation = true;
                m_cube_rotation_secondary.degree = -180.0f;
                m_cube_rotation_secondary.axis = new Vector(0.0f, 0.0f, 1.0f);
                break;
            
            case Hard1_To_Hard4:
                m_interpolator.setup(0.0f, 90.0f, divisor);
                m_secondary_rotation = true;
                m_cube_rotation_secondary.degree = 0.0f;
                m_cube_rotation_secondary.axis = new Vector(0.0f, 0.0f, 1.0f);
                MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Empty, Face_Z_Plus);
                MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Empty, Face_Z_Minus);
                break;
            
            case Hard4_To_Hard1:
                m_interpolator.setup(-270.0f, -360.0f, divisor);
                m_secondary_rotation = true;
                m_cube_rotation_secondary.degree = -270.0f;
                m_cube_rotation_secondary.axis = new Vector(0.0f, 0.0f, 1.0f);
			    createHardFaces();
                break;
            
            case Hard4_To_Hard3:
                m_interpolator.setup(-270.0f, -180.0f, divisor);
                m_secondary_rotation = true;
                m_cube_rotation_secondary.degree = -270.0f;
                m_cube_rotation_secondary.axis = new Vector(0.0f, 0.0f, 1.0f);
                break;
            
            case Hard3_To_Hard4:
                m_interpolator.setup(-180.0f, -270.0f, divisor);
                m_secondary_rotation = true;
                m_cube_rotation_secondary.degree = -180.0f;
                m_cube_rotation_secondary.axis = new Vector(0.0f, 0.0f, 1.0f);
                break;
            
            case Hard2_To_Hard3:
                m_interpolator.setup(-90.0f, -180.0f, divisor);
                m_secondary_rotation = true;
                m_cube_rotation_secondary.degree = -90.0f;
                m_cube_rotation_secondary.axis = new Vector(0.0f, 0.0f, 1.0f);
                break;
        
            case Hard2_To_Hard1:
                m_interpolator.setup(-90.0f, 0.0f, divisor);
                m_secondary_rotation = true;
                m_cube_rotation_secondary.degree = -90.0f;
                m_cube_rotation_secondary.axis = new Vector(0.0f, 0.0f, 1.0f);
                createHardFaces();
			    break;
            
            case Hard1_To_Hard2:
                m_interpolator.setup(0.0f, -90.0f, divisor);
                m_secondary_rotation = true;
                m_cube_rotation_secondary.degree = 0.0f;
                m_cube_rotation_secondary.axis = new Vector(0.0f, 0.0f, 1.0f);
                MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Empty, Face_Z_Plus);;
                MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Empty, Face_Z_Minus);
                break;
            
            case Normal1_To_Normal4:
                m_interpolator.setup(0.0f, -90.0f, divisor);
                m_secondary_rotation = true;
                m_cube_rotation_secondary.degree = 0.0f;
                m_cube_rotation_secondary.axis = new Vector(1.0f, 0.0f, 0.0f);
                MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Empty, Face_X_Plus);
                MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Empty, Face_X_Minus);
                break;
            
            case Normal4_To_Normal1:
                m_interpolator.setup(270.0f, 360.0f, divisor);
                m_secondary_rotation = true;
                m_cube_rotation_secondary.degree = 270.0f;
                m_cube_rotation_secondary.axis = new Vector(1.0f, 0.0f, 0.0f);
			    createNormalFaces();
                break;
            
            case Normal4_To_Normal3:
                m_interpolator.setup(270.0f, 180.0f, divisor);
                m_secondary_rotation = true;
                m_cube_rotation_secondary.degree = 270.0f;
                m_cube_rotation_secondary.axis = new Vector(1.0f, 0.0f, 0.0f);
                break;
            
            case Normal3_To_Normal4:
                m_interpolator.setup(180.0f, 270.0f, divisor);
                m_secondary_rotation = true;
                m_cube_rotation_secondary.degree = 180.0f;
                m_cube_rotation_secondary.axis = new Vector(1.0f, 0.0f, 0.0f);
                break;
            
            case Normal3_To_Normal2:
                m_interpolator.setup(180.0f, 90.0f, divisor);
                m_secondary_rotation = true;
                m_cube_rotation_secondary.degree = 180.0f;
                m_cube_rotation_secondary.axis = new Vector(1.0f, 0.0f, 0.0f);
                break;
            
            case Normal2_To_Normal3:
                m_interpolator.setup(90.0f, 180.0f, divisor);
                m_secondary_rotation = true;
                m_cube_rotation_secondary.degree = 90.0f;
                m_cube_rotation_secondary.axis = new Vector(1.0f, 0.0f, 0.0f);
                break;
            
            case Normal2_To_Normal1:
                m_interpolator.setup(90.0f, 0.0f, divisor);
                m_secondary_rotation = true;
                m_cube_rotation_secondary.degree = 90.0f;
                m_cube_rotation_secondary.axis = new Vector(1.0f, 0.0f, 0.0f);
			    createNormalFaces();
                break;
            
            case Normal1_To_Normal2:
                m_interpolator.setup(0.0f, 90.0f, divisor);
                m_secondary_rotation = true;
                m_cube_rotation_secondary.degree = 0.0f;
                m_cube_rotation_secondary.axis = new Vector(1.0f, 0.0f, 0.0f);
                MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Empty, Face_X_Plus);
                MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Empty, Face_X_Minus);
                break;
            
            case Easy1_To_Easy4:
                m_interpolator.setup(0.0f, -90.0f, divisor);
                m_secondary_rotation = true;
                m_cube_rotation_secondary.degree = 0.0f;
                m_cube_rotation_secondary.axis = new Vector(0.0f, 0.0f, 1.0f);
                MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Empty, Face_Z_Plus);
                MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Empty, Face_Z_Minus);
                break;
            
            case Easy4_To_Easy1:
                m_interpolator.setup(270.0f, 360.0f, divisor);
                m_secondary_rotation = true;
                m_cube_rotation_secondary.degree = 270.0f;
                m_cube_rotation_secondary.axis = new Vector(0.0f, 0.0f, 1.0f);
			    createEasyFaces();
                break;
            
            case Easy4_To_Easy3:
                m_interpolator.setup(270.0f, 180.0f, divisor);
                m_secondary_rotation = true;
                m_cube_rotation_secondary.degree = 270.0f;
                m_cube_rotation_secondary.axis = new Vector(0.0f, 0.0f, 1.0f);
                break;
            
            case Easy3_To_Easy4:
                m_interpolator.setup(180.0f, 270.0f, divisor);
                m_secondary_rotation = true;
                m_cube_rotation_secondary.degree = 180.0f;
                m_cube_rotation_secondary.axis = new Vector(0.0f, 0.0f, 1.0f);
                break;
            
            case Easy3_To_Easy2:
                m_interpolator.setup(180.0f, 90.0f, divisor);
                m_secondary_rotation = true;
                m_cube_rotation_secondary.degree = 180.0f;
                m_cube_rotation_secondary.axis = new Vector(0.0f, 0.0f, 1.0f);
                break;
            
            case Easy2_To_Easy3:
                m_interpolator.setup(90.0f, 180.0f, divisor);
                m_secondary_rotation = true;
                m_cube_rotation_secondary.degree = 90.0f;
                m_cube_rotation_secondary.axis = new Vector(0.0f, 0.0f, 1.0f);
                break;
            
            case Easy1_To_Easy2:
                m_interpolator.setup(0.0f, 90.0f, divisor);
                m_secondary_rotation = true;
                m_cube_rotation_secondary.degree = 0.0f;
			    m_cube_rotation_secondary.axis = new Vector(0.0f, 0.0f, 1.0f);
                MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Empty, Face_Z_Plus);
                MenuFaceBuilder.build(CubeFaceNamesEnum.Face_Empty, Face_Z_Minus);
                break;
            
            case Easy2_To_Easy1:
                m_interpolator.setup(90.0f, 0.0f, divisor);
                m_secondary_rotation = true;
                m_cube_rotation_secondary.degree = 0.0f;
			    m_cube_rotation_secondary.axis = new Vector(0.0f, 0.0f, 1.0f);
			    createEasyFaces();
                break;
            
            case Menu_To_Easy1:
                //Game.hideGameCenterInfo();
                m_interpolator.setup(0.0f, -90.0f, divisor);
                createEasyFaces();
                break;
            
            case Easy1_To_Menu:
                //Game.showGameCenterInfo();
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
                //engine.reportAchievement("com.almagems.cubetraz.basicmenuskills", 100.0f);
                //engine.ShowGameCenterInfo();
                m_interpolator.setup(-270.0f, -360.0f, divisor);
			    createMenuFaces(true);
			    break;
			
            case Hard1_To_Menu:
                //engine.ShowGameCenterInfo();
                m_interpolator.setup(-270.0f, -360.0f, divisor);
                createMenuFaces(false);
                break;
            
// menu <. options
   		    case Options_To_Menu:
                //engine.ReportAchievement("com.almagems.cubetraz.advancedmenuskills", 100.0f);
                m_interpolator.setup(90.0f, 0.0f, divisor);
			    m_cube_rotation.axis = new Vector(1.0f, 0.0f, 0.0f);
                //Game.saveOptions();
                break;
            
            case Menu_To_Options:
                m_interpolator.setup(0.0f, 90.0f, divisor);
			    m_cube_rotation.axis = new Vector(1.0f, 0.0f, 0.0f);
                break;
            
// menu <. store
   		    case Menu_To_Store:
                m_interpolator.setup(0.0f, -90.0f, divisor);
			    m_cube_rotation.axis = new Vector(1.0f, 0.0f, 0.0f);
                break;
            
		    case Store_To_Menu:
                m_interpolator.setup(-90.0f, 0.0f, divisor);
			    m_cube_rotation.axis = new Vector(1.0f, 0.0f, 0.0f);
                break;
            
            default:
                break;
        } // switch
    
        Game.buildVisibleCubesListOnlyOnFaces(m_menu.mCubesFace);
    }

    public void update() {
        m_interpolator.interpolate();
    
        if (m_secondary_rotation) {
            m_cube_rotation_secondary.degree = m_interpolator.getValue();
        } else {
            m_cube_rotation.degree = m_interpolator.getValue();
        }
    
        switch (m_navigation) {
            case Hard3_To_Hard2:            
                if ( ( Math.abs(m_cube_rotation_secondary.degree) - 90.0f) < EPSILON) {
                    m_cube_rotation_secondary.degree = -90.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Hard1_To_Hard4: 
                if ( (90.0f - m_cube_rotation_secondary.degree) < EPSILON) {
                    m_cube_rotation_secondary.degree = -270.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Hard4_To_Hard1: 
                if ( 360.0f - Math.abs(m_cube_rotation_secondary.degree) < EPSILON) {
                    m_cube_rotation_secondary.degree = 0.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Hard4_To_Hard3:            
                if ( Math.abs(m_cube_rotation_secondary.degree) - 180.0f < EPSILON) {
                    m_cube_rotation_secondary.degree = -180.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Hard3_To_Hard4:
                if ( 270.0f - Math.abs(m_cube_rotation_secondary.degree) < EPSILON) {
                    m_cube_rotation_secondary.degree = -270.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Hard2_To_Hard3:
                if ( 180.0f - Math.abs(m_cube_rotation_secondary.degree) < EPSILON) {
                    m_cube_rotation_secondary.degree = -180.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
                
            case Hard2_To_Hard1:            
                if ( Math.abs(m_cube_rotation_secondary.degree) < EPSILON) {
                    m_cube_rotation_secondary.degree = 0.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Hard1_To_Hard2:            
                if ( (90.0f - Math.abs(m_cube_rotation_secondary.degree)) < EPSILON) {
                    m_cube_rotation_secondary.degree = -90.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;            
            
            case Normal1_To_Normal4:             
                if ( (90.0f - Math.abs(m_cube_rotation_secondary.degree)) < EPSILON) {
                    m_cube_rotation_secondary.degree = 270.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Normal4_To_Normal1:             
                if ( (360.0f - m_cube_rotation_secondary.degree) < EPSILON) {
                    m_cube_rotation_secondary.degree = 0.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Normal4_To_Normal3:             
                if ( (m_cube_rotation_secondary.degree - 180.0f) < EPSILON) {
                    m_cube_rotation_secondary.degree = 180.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Normal3_To_Normal4:
                if ( (270.0f - m_cube_rotation_secondary.degree) < EPSILON) {
                    m_cube_rotation_secondary.degree = 270.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Normal3_To_Normal2:             
                if ( (m_cube_rotation_secondary.degree - 90.0f) < EPSILON) {
                    m_cube_rotation_secondary.degree = 90.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Normal2_To_Normal3:                
                if ( (180.0f - m_cube_rotation_secondary.degree) < EPSILON) {
                    m_cube_rotation_secondary.degree = 180.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Normal2_To_Normal1:             
                if ( (m_cube_rotation_secondary.degree) < EPSILON) {
                    m_cube_rotation_secondary.degree = 0.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Normal1_To_Normal2:            
                if ( (90.0f - Math.abs(m_cube_rotation_secondary.degree)) < EPSILON) {
                    m_cube_rotation_secondary.degree = 90.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Easy1_To_Easy4:            
                if ( (90.0f - Math.abs(m_cube_rotation_secondary.degree)) < EPSILON) {
                    m_cube_rotation_secondary.degree = 270.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }
                break;
            
            case Easy4_To_Easy1:            
                if ( (360.0f - m_cube_rotation_secondary.degree) < EPSILON) {
                    m_cube_rotation_secondary.degree = 0.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;                
                }            
                break;
            
            case Easy4_To_Easy3:            
                if ( (m_cube_rotation_secondary.degree - 180.0f) < EPSILON) {
                    m_cube_rotation_secondary.degree = 180.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }                        
                break;
            
            case Easy3_To_Easy4:            
                if ( (270.0f - m_cube_rotation_secondary.degree) < EPSILON) {
                    m_cube_rotation_secondary.degree = 270.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Easy3_To_Easy2:            
                if ( (m_cube_rotation_secondary.degree - 90.0f) < EPSILON) {
                    m_cube_rotation_secondary.degree = 90.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Easy2_To_Easy3:             
                if ( (180.0f - m_cube_rotation_secondary.degree) < EPSILON) {
                    m_cube_rotation_secondary.degree = 180.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Easy1_To_Easy2:            
                if ( Math.abs(Math.abs(90.0f) - Math.abs(m_cube_rotation_secondary.degree)) < EPSILON) {
                    m_cube_rotation_secondary.degree = 90.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;                
                }            
                break;
            
            case Easy2_To_Easy1:            
                if ( m_cube_rotation_secondary.degree < EPSILON ) {
                    m_cube_rotation_secondary.degree = 0.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                    m_secondary_rotation = false;
                }            
                break;
            
            case Menu_To_Easy1:            
                if ( (Math.abs(-90.0f) - Math.abs(m_cube_rotation.degree))  < EPSILON) {
                    m_cube_rotation.degree = -90.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                }            
                break;
            
            case Easy1_To_Menu:            
                if ( Math.abs(0.0f - Math.abs(m_cube_rotation.degree)) < EPSILON) {
                    m_cube_rotation.degree = 0.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                }            
                break;
            
            case Easy1_To_Normal1:            
                if ( Math.abs(180.0f - Math.abs(m_cube_rotation.degree)) < EPSILON) {
                    m_cube_rotation.degree = -180.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;                
                }            
                break;
            
            case Normal1_To_Easy1:            
                if ( Math.abs(Math.abs(-90.0f) - Math.abs(m_cube_rotation.degree)) < EPSILON) {
                    m_cube_rotation.degree = -90.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                }            
                break;
            
            case Normal1_To_Hard1:            
                if ( Math.abs(Math.abs(-270.0f) - Math.abs(m_cube_rotation.degree)) < EPSILON) {
                    m_cube_rotation.degree = -270.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                }            
                break;
            
            case Hard1_To_Normal1:            
                if ( Math.abs(Math.abs(-180.0f) - Math.abs(m_cube_rotation.degree)) < EPSILON) {
                    m_cube_rotation.degree = -180.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                }            
                break;
            
		    case Tutorial_To_Menu:			
            case Hard1_To_Menu:            
                if ( Math.abs(Math.abs(-360.0f) - Math.abs(m_cube_rotation.degree)) < EPSILON) {
                    m_cube_rotation.degree = 0.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                }            
                break;
			
		    case Menu_To_Options:            
                if ( Math.abs(90.0f - Math.abs(m_cube_rotation.degree)) < EPSILON) {
                    m_cube_rotation.degree = 90.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                }            
			    break;
            
		    case Options_To_Menu:            
                if ( Math.abs(0.0f - Math.abs(m_cube_rotation.degree)) < EPSILON) {
                    m_cube_rotation.degree = 0.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                    m_cube_rotation.axis = new Vector(0.0f, 1.0f, 0.0f);
                }            
			    break;
            
		    case Menu_To_Store:            
                if ( Math.abs(Math.abs(-90.0f) - Math.abs(m_cube_rotation.degree)) < EPSILON) {
                    m_cube_rotation.degree = -90.0f;
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                }            
			    break;
            
		    case Store_To_Menu:            
                if ( Math.abs(0.0f - Math.abs(m_cube_rotation.degree)) < EPSILON) {
                    m_cube_rotation.degree = 0.0f;
                    m_cube_rotation.axis = new Vector(0.0f, 1.0f, 0.0f);
                    m_navigation = CubeFaceNavigationEnum.NoNavigation;
                }            
			    break;
			
		    case NoNavigation:
			    break;
    } // switch
}

}
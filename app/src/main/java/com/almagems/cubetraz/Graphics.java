package com.almagems.cubetraz;


import static android.opengl.GLES10.*;
import javax.microedition.khronos.opengles.GL10;
import static android.opengl.Matrix.*;
import static com.almagems.cubetraz.Constants.*;

import android.content.Context;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public final class Graphics {

    public static float screenWidth;
    public static float screenHeight;
    public static float aspectRatio;
    public static final float referenceScreenWidth = 1080f;
    public static float scaleFactor;

	public static Context context;

    //public final FBO fboBackground;

    //public Map<String, TexturedQuad> fonts = new HashMap<String, TexturedQuad>();
    private ArrayList<Texture> textures = new ArrayList<Texture>(20);
	
	// matrices

		
	// textures


    // ctor
	public Graphics(Context context) {
        //System.out.println("Visuals ctor...");
		Graphics.context = context;
        //fboBackground = new FBO();
	}

    public void initialSetup() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        //glClearColor(1.0f, 0.3f, 0.3f, 0.0f);
        //glClearColor(0.1f, 0.1f, 0.1f, 0.0f);

        glEnable(GL_CULL_FACE);
        //glDisable(GL_CULL_FACE);

        glDepthFunc(GL_LESS);
        //glDepthMask(true);
        glDisable(GL_DITHER);

        //glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        //glBlendFunc(GL_ONE_MINUS_DST_ALPHA,GL_DST_ALPHA);
        //glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        //glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        glBlendFunc(GL_ONE, GL_ONE);
    }

    public void loadStartupAssets() throws Exception {
        //System.out.println("Load startup Assets...");

        // shader
        //BaseShader.graphics = this;
        //textureShader = new TextureShader();
        //singleColorShader = new SingleColorShader();

        // texture
        //textureLoading = loadTexture(R.drawable.almagems_android_loading);
    }

    private int loadTexture(int resourceId) {
        //Texture texture = TextureHelper.loadTexture(context, resourceId);
        //textures.add(texture);
        //return texture.id;
        return 0;
    }

    private int loadTextureAndJson(int textureResourceId, int jsonResourceId) {
        //Texture texture = TextureHelper.loadTexture(context, textureResourceId);
        //String jsonText = TextResourceReader.readTextFileFromResource(context, jsonResourceId);
        //texture.loadFrames(jsonText);
        //textures.add(texture);
        //return texture.id;
        return 0;
    }

    @Nullable
    public Texture getTextureObj(int textureId) {
        Texture texture;
        int size = textures.size();
        for(int i = 0; i < size; ++i) {
            texture = textures.get(i);
            if (texture.id == textureId) {
                return texture;
            }
        }
        return null;
    }

	public void loadTexturesPart01() {
//        textureGems = loadTexture(R.drawable.gems_textures);
//        textureCart = loadTexture(R.drawable.cart_texture);
//        textureRailRoad = loadTexture(R.drawable.railroad_texture);
//        textureParticle = loadTexture(R.drawable.smokeparticle);
//        textureFloor = loadTexture(R.drawable.floor_texture);
//        textureWall = loadTexture(R.drawable.wall_texture);
    }

    public void loadTexturesPart02() {
//        texturePillar = loadTexture(R.drawable.pillar_texture);
//        textureCrate = loadTexture(R.drawable.crate_texture);
//        textureSoil = loadTexture(R.drawable.soil_texture);
//        textureWheel = loadTexture(R.drawable.wheel_texture);
//        textureBeam = loadTexture(R.drawable.beam_texture);
//        textureCliff142 = loadTexture(R.drawable.cliffs0142);
    }

    public void loadTexturesPart03() {
//		texturePickAxe = loadTexture(R.drawable.pickaxe_texture);
//        textureFonts = loadTexture(R.drawable.fontsandroid);
//        textureEditorButtons = loadTexture(R.drawable.editor_buttons);
//        textureHudPauseButton = loadTexture(R.drawable.hud_pause_button);
//		textureMenuItems = loadTextureAndJson(R.drawable.menu_items, R.raw.data);
	}

	public void bindNoTexture() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}

    public void setProjectionMatrix2D() {
        setProjectionMatrix2D((int) screenWidth, (int) screenHeight);
    }

	private void setProjectionMatrix2D(int width, int height) {
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glOrthof(-1f, 1f, -1f, 1f, -1f, 1f);

//		final float aspectRatio = width > height ? (float)width / (float)height : (float)height / (float)width;

//		if (width > height) {
//            // landscape
//			orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
//		} else {
//		    // portrait
//			orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
//		}

//        setIdentityM(viewMatrix, 0);

//		orthoM(projectionMatrix, 0, 0f, width, 0f, height, 0f, 100f);
		//setLookAtM(viewMatrix, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f);
	}

	public void setProjectionMatrix3D() {
		float eyeZ = 49.0f;
		//float eyeZ = 90.0f;

		//MatrixHelper.perspectiveM(projectionMatrix, 45, screenWidth / screenHeight, 1f, 100f);
		
//		setLookAtM(viewMatrix, 0,
//				// real
//				0f,  12f, eyeZ,	// eye
//				0f,  1.2f, 0f,    // at
//				0f,  1f, 0f);   // up
					
//				// debug railway upclose
//				-13f, -19f, 3f, // eye				
//				0f, -22f, 0f, 	// at
//				0f, 1f, 0f);	// up

				// debug 
//				-20f, 5f,  1f, 	// eye
//				  0f, 5f, -1f, 	// at
//				  0f, 1f,   0f);	// up

	/*	
        glViewport(0, 0, width, height);        

        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width
            / (float) height, 1f, 10f);
        
        setIdentityM(viewMatrix, 0);
        translateM(viewMatrix, 0, 0f, -1.5f, -5f);   
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0,
            viewMatrix, 0);
    */
	}
	
	public void updateViewProjMatrix() {
//		multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
//		invertM(invertedViewProjectionMatrix, 0, viewProjectionMatrix, 0);
	}

	public void calcMatricesForObject(PositionInfo op, float tx, float ty) {
//        setIdentityM(modelMatrix, 0);
//
//        translateM(modelMatrix, 0, 0f, ty, 0f);
//
//        // rotation
//        if (Math.abs(op.rx) > Constants.EPSILON) {
//            rotateM(modelMatrix, 0, op.rx, 1f, 0f, 0f);
//        }
//
//        if (Math.abs(op.ry) > Constants.EPSILON) {
//            rotateM(modelMatrix, 0, op.ry, 0f, 1f, 0f);
//        }
//
//        if (Math.abs(op.rz) > Constants.EPSILON) {
//            rotateM(modelMatrix, 0, op.rz, 0f, 0, 1f);
//        }
//
//
//
//        // translation
//        translateM(modelMatrix, 0, op.tx, op.ty, op.tz);
//
//        translateM(modelMatrix, 0, 0f, -ty, 0f);
//
////        rotateM(modelMatrix, 0, op.rx, 1f, 0f, 0f);
////        rotateM(modelMatrix, 0, op.ry, 0f, 1f, 0f);
////        rotateM(modelMatrix, 0, op.rz, 0f, 0f, 1f);
//
//        // scale
//        if ( (Math.abs(1f - op.sx) > Constants.EPSILON) ||
//                (Math.abs(1f - op.sy) > Constants.EPSILON) ||
//                (Math.abs(1f - op.sz) > Constants.EPSILON) ) {
//            scaleM(modelMatrix, 0, op.sx, op.sy, op.sz);
//        }
//
//        //scaleM(modelMatrix, 0, op.sx, op.sy, op.sz);
//
//        setIdentityM(mLightModelMatrix, 0);
//        translateM(mLightModelMatrix, 0, lightDir.x, lightDir.y, lightDir.z);
//
//        multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0, mLightPosInModelSpace, 0);
//        multiplyMV(mLightPosInEyeSpace, 0, viewMatrix, 0, mLightPosInWorldSpace, 0);
//
//        multiplyMM(mvpMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
//
//        multiplyMM(mvMatrix, 0, viewMatrix, 0, modelMatrix, 0);
//        //multiplyMM(mvMatrix, 0, modelMatrix, 0, viewMatrix, 0);
//
//        //multiplyMV()
//
//        multiplyMM(modelProjMatrix, 0, projectionMatrix, 0, modelMatrix, 0);
//
//        // calc matrix to transform normal based on the model matrix
//        invertM(normalMatrix, 0, modelMatrix, 0);
//        transposeM(normalMatrix, 0, normalMatrix, 0);
    }

    public void calcMatricesForObject(PositionInfo op) {
//		setIdentityM(modelMatrix, 0);
//
//        // translation
//		translateM(modelMatrix, 0, op.tx, op.ty, op.tz);
//
//        // rotation
//		if (Math.abs(op.rx) > Constants.EPSILON) {
//			rotateM(modelMatrix, 0, op.rx, 1f, 0f, 0f);
//		}
//
//		if (Math.abs(op.ry) > Constants.EPSILON) {
//			rotateM(modelMatrix, 0, op.ry, 0f, 1f, 0f);
//		}
//
//		if (Math.abs(op.rz) > Constants.EPSILON) {
//			rotateM(modelMatrix, 0, op.rz, 0f, 0, 1f);
//		}

//        rotateM(modelMatrix, 0, op.rx, 1f, 0f, 0f);
//        rotateM(modelMatrix, 0, op.ry, 0f, 1f, 0f);
//        rotateM(modelMatrix, 0, op.rz, 0f, 0f, 1f);

        // scale
//		if ( (Math.abs(1f - op.sx) > Constants.EPSILON) ||
//             (Math.abs(1f - op.sy) > Constants.EPSILON) ||
//             (Math.abs(1f - op.sz) > Constants.EPSILON) ) {
//			scaleM(modelMatrix, 0, op.sx, op.sy, op.sz);
//		}

        //scaleM(modelMatrix, 0, op.sx, op.sy, op.sz);

//		setIdentityM(mLightModelMatrix, 0);
//		translateM(mLightModelMatrix, 0, lightDir.x, lightDir.y, lightDir.z);
//
//		multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0, mLightPosInModelSpace, 0);
//		multiplyMV(mLightPosInEyeSpace, 0, viewMatrix, 0, mLightPosInWorldSpace, 0);
//
//		multiplyMM(mvpMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
//
//		multiplyMM(mvMatrix, 0, viewMatrix, 0, modelMatrix, 0);
//		//multiplyMM(mvMatrix, 0, modelMatrix, 0, viewMatrix, 0);
//
//		//multiplyMV()
//
//		multiplyMM(modelProjMatrix, 0, projectionMatrix, 0, modelMatrix, 0);
//
//		// calc matrix to transform normal based on the model matrix
//		invertM(normalMatrix, 0, modelMatrix, 0);
//		transposeM(normalMatrix, 0, normalMatrix, 0);
	}

//    public VertexBuffer createFullScreenVertexBuffer(Rectangle rc, Texture texture) {
//        float width = screenWidth;
//        float scale = width / referenceScreenWidth;
//
//        float tw = texture.width;
//        float th = texture.height;
//
//        float r = 1f;
//        float g = 1f;
//        float b = 1f;
//        float a = 1f;
//
//        float tx0 = rc.x / tw;
//        float tx1 = (rc.x + rc.w) / tw;
//        float ty0 = ((th - rc.y) - rc.h) / th;
//        float ty1 = ((th - rc.y)) / th;
//
//        float x = (rc.w / width) * scale;
//        float y = (rc.h / width) * scale;
//        float[] vertexData = {
//                // x, y, z, 	                u, v,
//                -x, -y, 0.0f,   r, g, b, a,     tx0, ty0,
//                 x, -y, 0.0f,   r, g, b, a,     tx1, ty0,
//                 x,  y, 0.0f,   r, g, b, a,     tx1, ty1,
//
//                -x, -y, 0.0f,   r, g, b, a,     tx0, ty0,
//                 x,  y, 0.0f,   r, g, b, a,     tx1, ty1,
//                -x,  y, 0.0f,   r, g, b, a,     tx0, ty1
//        };
//
//        VertexBuffer vb = new VertexBuffer(vertexData);
//        return vb;
//    }


    public void loadFonts() {
    }

    public static int createTexture(int w, int h) {
        int[] temp = new int[1];
        glGenTextures(1, temp, 0);

        glBindTexture(GL10.GL_TEXTURE_2D, temp[0]);

//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, null);
        //glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, w, h, 0, GL_RGB, GL_UNSIGNED_SHORT_5_6_5, null);
        return temp[0];
    }

    public void drawAxes() {
//        EdgeDrawer edgeDrawer = new EdgeDrawer(2);
//
//        float x = 1f;
//        float y = aspectRatio; // 1f
//
//        edgeDrawer.begin();
//        edgeDrawer.addLine(-x, 0f, 0f, x, 0f, 0f);
//
//        edgeDrawer.addLine(0f, -y, 0f, 0f, y, 0f);
//
//        setIdentityM(modelMatrix, 0);
//        multiplyMM(mvpMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
//
//        singleColorShader.useProgram();
//        singleColorShader.setUniforms(mvpMatrix, Color.YELLOW);
//        edgeDrawer.bindData(singleColorShader);
//        edgeDrawer.draw();
    }

    public static void prepareFrame() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void onSurfaceChanged(int width, int height) {
        glViewport(0, 0, width, height);

        Graphics.screenWidth = width;
        Graphics.screenHeight = height;
        Graphics.aspectRatio = width > height ? (float)width / (float)height : (float)height / (float)width;
        Graphics.scaleFactor = Graphics.screenWidth / Graphics.referenceScreenWidth;

//        ParticleShader.pointSize = (float)width * 0.12f;
//
//        if (width > 1080) {
//            fboBackground.create(width / 2, height / 2); // on really big displays avoid creating a full screen texture
//        } else {
//            fboBackground.create(width, height);
//        }
    }

    public void releaseUnusedAssets() {
//        int[] arr = new int[]{
//                textureRailRoad,
//                textureFloor,
//                textureWall,
//                texturePillar,
//                textureCrate,
//                textureSoil,
//                textureBeam,
//                textureCliff142,
//                texturePickAxe,
//                textureEditorButtons
//        };
//
//        for (int i = 0; i < arr.length; ++i) {
//            Texture texture = getTextureObj( arr[i] );
//            if (texture != null) {
//                textures.remove(texture);
//            }
//        }
//
//        glDeleteTextures(arr.length, arr, 0);
//
//        railroad = null;
//        floor = null;
//        wall = null;
//        pillar = null;
//        crate = null;
//        soil = null;
//        beam = null;
//        rock0 = null;
//        rock1 = null;
//        rock2 = null;
//        rock3 = null;
//        rock4 = null;
//        rock5 = null;
//        rock6 = null;
//        rock7 = null;
//        rock8 = null;
//        pickAxe = null;
    }

}

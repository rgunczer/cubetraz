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


    public static int width;
    public static int height;
    public static int half_width;
    public static int half_height;

    public static float device_scale = 1f;

    public static Color getColorFromScreen(Vector2 pos) {
//        int ix = pos.x;
//        int iy = m_height - pos.y;
//
//        GLuint argb;
//        glReadPixels(ix, iy, 1, 1, GL_RGBA, GL_UNSIGNED_BYTE, &argb);
//
//        Color color;
//        color.r = (argb)       & 0xFF;
//        color.g = (argb >> 8)  & 0xFF;
//        color.b = (argb >> 16) & 0xFF;
//        color.a = (argb >> 24) & 0xFF;
//
//        //printf("\nx:%d, y:%d, red:%d, green:%d, blue:%d, alpha:%d", ix, iy, color.r, color.g, color.b, color.a);
//
//        return color;
        return null;
    }


    public static void drawAxes() {



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



        final float axis[] =
            {
                    -100.0f,    0.0f,    0.0f,		  0.0f,    0.0f,    0.0f,    // x
                    0.0f, -100.0f,    0.0f,		  0.0f,    0.0f,    0.0f,    // y
                    0.0f,    0.0f, -100.0f,        0.0f,    0.0f,    0.0f,    // z

                    0.0f,    0.0f,    0.0f,		100.0f,    0.0f,    0.0f,    // x
                    0.0f,    0.0f,    0.0f,		  0.0f,  100.0f,    0.0f,    // y
                    0.0f,    0.0f,    0.0f,	      0.0f,    0.0f,  100.0f,    // z
            };

        final short colors[] =
            {
                    0, 0, 255, 255,				0, 0, 255, 255,	// x
                    0, 255, 0, 255,				0, 255, 0, 255, // y
                    255, 0, 0, 255,				255, 0, 0, 255, // z

                    0, 0, 255, 255,				0, 0, 255, 255, // x
                    0, 255, 0, 255,				0, 255, 0, 255, // y
                    255, 0, 0, 255,				255, 0, 0, 255, // z
            };

//        glDisableClientState(GL_NORMAL_ARRAY);
//        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
//
//        glVertexPointer(3, GL_FLOAT, 0, axis);
//        glColorPointer(4, GL_UNSIGNED_BYTE, 0, colors);
//        glLineWidth(3.0f);
//
//        glDrawArrays(GL_LINES, 6, 6);
//
//        glLineWidth(1.0f);
//        glDrawArrays(GL_LINES, 0, 6);
    }

    // textures
    public static int texture_id_gray_concrete;
    public static int texture_id_key;
    public static int texture_id_fonts;
    public static int texture_id_fonts_clear;
    public static int texture_id_level_cubes;
    public static int texture_id_fonts_big;
    public static int texture_id_level_cubes_locked;
    public static int texture_id_numbers;
    public static int texture_id_player;
    public static int texture_id_star;
    public static int texture_id_symbols;
    public static int texture_id_stat_background;
    public static int texture_id_credits;
    public static int texture_id_dirty;
    public static int texture_id_tutor;


    public static void drawFBOTexture(int texture_id, Color color, boolean magic) {
//        const GLfloat verts[] =
//            {
//                    0.0f,               engine->m_height,
//                    0.0f,               0.0f,
//                    engine->m_width,    0.0f,
//                    engine->m_width,    engine->m_height
//            };
//
//        GLfloat coords[] =
//                {
//                        0, 1,
//                        0, 0,
//                        1, 0,
//                        1, 1
//                };
//
//        if (magic)
//        {
//            coords[1] = 1.0f - (m_banner_height / m_height);
//            coords[7] = coords[1];
//        }
//
//        const GLubyte colors[] =
//            {
//                    color.r, color.g, color.b, color.a,
//                    color.r, color.g, color.b, color.a,
//                    color.r, color.g, color.b, color.a,
//                    color.r, color.g, color.b, color.a
//            };
//
//        glBindTexture(GL_TEXTURE_2D, texture_id);
//
//        glVertexPointer(2, GL_FLOAT, 0, verts);
//        glTexCoordPointer(2, GL_FLOAT, 0, coords);
//        glColorPointer(4, GL_UNSIGNED_BYTE, 0, colors);
//
//        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
    }


    public static void setModelViewMatrix2D() {
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }

    public static void setProjection2D() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrthof(0.0f, m_width, 0.0f, m_height, -1.0f, 1.0f);
    }


    public static float m_aspectRatio = 1.0f;


    public static void setModelViewMatrix3D(final Camera camera) {
        //mat4 matLookAt = mat4::LookAt(camera.eye, camera.target, vec3(0.0f, 1.0f, 0.0f));

        glMatrixMode(GL_MODELVIEW);
        //glLoadMatrixf(matLookAt.Pointer());
    }


    public static void setProjection3D() {
        final float zNear = 1.0f;
        final float zFar = 1000.0f;
        final float fieldOfView = 30.0f;
        final float size = zNear * Math.tan(( Math.toRadians(fieldOfView) / 2.0f );

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glFrustumf(-size, size, -size / m_aspectRatio, size / m_aspectRatio, zNear, zFar);
    }


    public static int _vertices_count = 0;
    public static int _vindex = -1;
    public static int _cindex = -1;
    public static int _color_index = -1;


    private static float[] _vertices = new float[BUF_SIZE * KILOBYTE];  /*3 * 36 * MAX_CUBE_COUNT * MAX_CUBE_COUNT * MAX_CUBE_COUNT */
    private static float[] _normals = new float[BUF_SIZE * KILOBYTE];
    private static float[] _coords_float = new float[BUF_SIZE * KILOBYTE];
    private static short[] _coords_byte = new short[BUF_SIZE * KILOBYTE];
    private static byte[] _colors_ubyte = new byte[BUF_SIZE * KILOBYTE];

    public static boolean _blending_enabled = false;



    public static void addCubeWithColor(float tx, float ty, float tz, Color color) {
        addCubeSize(tx, ty, tz, HALF_CUBE_SIZE, color);
    }


    private static boolean _blending_enabled;



    public static int _vertices_count;

    public static void enableBlending() {
        if (!_blending_enabled) {
            glEnable(GL_BLEND);
            _blending_enabled = true;
        }
    }

    public static void disableBlending() {
        if (_blending_enabled) {
            glDisable(GL_BLEND);
            _blending_enabled = false;
        }
    }


    private static int _vindex;
    private static int _cindex;
    private static int _color_index;


    public static void renderPoints() {
        glDrawArrays(GL_POINTS, 0, _vertices_count);
    }

    public static void addCube(float tx, float ty, float tz) {
        //const Color color(255, 255, 255, 255);
        //AddCubeSize(tx, ty, tz, HALF_CUBE_SIZE, color);
    }

    public static void prepare() {
        _vertices_count = 0;
        _vindex = -1;
        _cindex = -1;
        _color_index = -1;
    }

    public static void setStreamSource() {
        glVertexPointer(3, GL_FLOAT, 0, _vertices);
        glNormalPointer(GL_FLOAT, 0, _normals);
        glTexCoordPointer(2, GL_SHORT, 0, _coords_byte);
        glColorPointer(4, GL_UNSIGNED_BYTE, 0, _colors_ubyte);

        glEnableClientState(GL_NORMAL_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
    }






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

    public static int loadTexture(String fileName) {
        // todo
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



















//    static inline void DumpVerticesBuffer(int count)
//    {
//		printf("\nDumpVerticesBuffer: %d", count);
//
//        for (int i = 0; i < count*2; i+=2)
//        {
//            printf("\nx:%.2f, y:%.2f", _vertices[i], _vertices[i+1]);
//        }
//    }
//
//    static inline void DumpNormalsBuffer(int count)
//    {
//		printf("\nDumpNormalsBuffer: %d", count);
//
//        for (int i = 0; i < count*3; i+=3)
//        {
//            printf("\nx:%.2f, y:%.2f z:%.2f", _normals[i], _normals[i+1], _normals[i+2]);
//        }
//    }
//
//	static inline void DumpCoordsFloatBuffer(int count)
//	{
//		printf("\nDumpCoordsFloatBuffer: %d", count);
//
//        for (int i = 0; i < count*2; i+=2)
//        {
//            printf("\nx:%.2f, y:%.2f", _coords_float[i], _coords_float[i+1]);
//        }
//	}
//
//	static inline void DumpCoordsByteBuffer(int count)
//	{
//		printf("\nDumpCoordsByteBuffer: %d", count);
//
//        for (int i = 0; i < count*2; i+=2)
//        {
//            printf("\nx:%d, y:%d", _coords_byte[i], _coords_byte[i+1]);
//        }
//	}
//
//	static inline void DumpColorsBuffer(int count)
//	{
//		printf("\nDumpColorsBuffer: %d", count);
//
//        for (int i = 0; i < count*4; i+=4)
//        {
//            printf("\nr:%d, g:%d, b:%d, a:%d", _colors_ubyte[i], _colors_ubyte[i+1], _colors_ubyte[i+2], _colors_ubyte[i+3]);
//        }
//	}

    public static void setStreamSourceOld() {
        glVertexPointer(3, GL_FLOAT, 0, _vertices);
        glNormalPointer(GL_FLOAT, 0, _normals);
        glTexCoordPointer(2, GL_SHORT, 0, _coords_byte);
    }

    public static void setStreamSourceOnlyVerticeAndColor() {
        glVertexPointer(3, GL_FLOAT, 0, _vertices);
        glColorPointer(4, GL_UNSIGNED_BYTE, 0, _colors_ubyte);

        glDisableClientState(GL_NORMAL_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
    }

    public static void setStreamSourceFloatAndColor() {
        glVertexPointer(3, GL_FLOAT, 0, _vertices);
        glNormalPointer(GL_FLOAT, 0, _normals);
        glTexCoordPointer(2, GL_FLOAT, 0, _coords_float);
        glColorPointer(4, GL_UNSIGNED_BYTE, 0, _colors_ubyte);
    }

    public static void setStreamSourceFloat() {
        glVertexPointer(3, GL_FLOAT, 0, _vertices);
        glNormalPointer(GL_FLOAT, 0, _normals);
        glTexCoordPointer(2, GL_FLOAT, 0, _coords_float);
    }

    public static void setStreamSourceFloat2D() {
        glVertexPointer(2, GL_FLOAT, 0, _vertices);
        glTexCoordPointer(2, GL_FLOAT, 0, _coords_float);
        glColorPointer(4, GL_UNSIGNED_BYTE, 0, _colors_ubyte);

        glDisableClientState(GL_NORMAL_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
    }

    public static void setStreamSourceFloat2DNoTexture() {
        glVertexPointer(2, GL_FLOAT, 0, _vertices);
        glColorPointer(4, GL_UNSIGNED_BYTE, 0, _colors_ubyte);

        glDisableClientState(GL_NORMAL_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
    }

    public static void renderTriangles() {
        glDrawArrays(GL_TRIANGLES, 0, _vertices_count);
    }

    public static void renderTriangles(float tx, float ty, float tz) {
        glPushMatrix();
        glTranslatef(tx, ty, tz);
        glDrawArrays(GL_TRIANGLES, 0, _vertices_count);
        glPopMatrix();
    }

    public static void addCubeSize(float tx, float ty, float tz, float size, Color color) {
        //printf("\nColor: %d %d %d %d", color.r, color.g, color.b, color.a);

        float xp = size + tx;
        float yp = size + ty;
        float zp = size + tz;

        float xn = -size + tx;
        float yn = -size + ty;
        float zn = -size + tz;

//------------------------------------
        int i = _vindex;

        // x-plus
        _vertices[++i] = xp; _vertices[++i] = yp; _vertices[++i] = zn;
        _vertices[++i] = xp; _vertices[++i] = yp; _vertices[++i] = zp;
        _vertices[++i] = xp; _vertices[++i] = yn; _vertices[++i] = zp;

        _vertices[++i] = xp; _vertices[++i] = yn; _vertices[++i] = zp;
        _vertices[++i] = xp; _vertices[++i] = yn; _vertices[++i] = zn;
        _vertices[++i] = xp; _vertices[++i] = yp; _vertices[++i] = zn;

        // x-minus
        _vertices[++i] = xn; _vertices[++i] = yp; _vertices[++i] = zp;
        _vertices[++i] = xn; _vertices[++i] = yp; _vertices[++i] = zn;
        _vertices[++i] = xn; _vertices[++i] = yn; _vertices[++i] = zn;

        _vertices[++i] = xn; _vertices[++i] = yn; _vertices[++i] = zn;
        _vertices[++i] = xn; _vertices[++i] = yn; _vertices[++i] = zp;
        _vertices[++i] = xn; _vertices[++i] = yp; _vertices[++i] = zp;


        // y-plus
        _vertices[++i] = xp; _vertices[++i] = yp; _vertices[++i] = zp;
        _vertices[++i] = xp; _vertices[++i] = yp; _vertices[++i] = zn;
        _vertices[++i] = xn; _vertices[++i] = yp; _vertices[++i] = zn;

        _vertices[++i] = xn; _vertices[++i] = yp; _vertices[++i] = zn;
        _vertices[++i] = xn; _vertices[++i] = yp; _vertices[++i] = zp;
        _vertices[++i] = xp; _vertices[++i] = yp; _vertices[++i] = zp;

        // y-minus
        _vertices[++i] = xp; _vertices[++i] = yn; _vertices[++i] = zn;
        _vertices[++i] = xp; _vertices[++i] = yn; _vertices[++i] = zp;
        _vertices[++i] = xn; _vertices[++i] = yn; _vertices[++i] = zp;

        _vertices[++i] = xn; _vertices[++i] = yn; _vertices[++i] = zp;
        _vertices[++i] = xn; _vertices[++i] = yn; _vertices[++i] = zn;
        _vertices[++i] = xp; _vertices[++i] = yn; _vertices[++i] = zn;


        // z-plus
        _vertices[++i] = xp; _vertices[++i] = yp; _vertices[++i] = zp;
        _vertices[++i] = xn; _vertices[++i] = yp; _vertices[++i] = zp;
        _vertices[++i] = xn; _vertices[++i] = yn; _vertices[++i] = zp;

        _vertices[++i] = xn; _vertices[++i] = yn; _vertices[++i] = zp;
        _vertices[++i] = xp; _vertices[++i] = yn; _vertices[++i] = zp;
        _vertices[++i] = xp; _vertices[++i] = yp; _vertices[++i] = zp;

        // z-minus
        _vertices[++i] = xp; _vertices[++i] = yp; _vertices[++i] = zn;
        _vertices[++i] = xp; _vertices[++i] = yn; _vertices[++i] = zn;
        _vertices[++i] = xn; _vertices[++i] = yn; _vertices[++i] = zn;

        _vertices[++i] = xn; _vertices[++i] = yn; _vertices[++i] = zn;
        _vertices[++i] = xn; _vertices[++i] = yp; _vertices[++i] = zn;
        _vertices[++i] = xp; _vertices[++i] = yp; _vertices[++i] = zn;

//------------------------------------
        int j = _vindex;

        // x-plus
        _normals[++j] = 1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = 1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = 1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = 1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = 1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = 1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;

        // x-minus
        _normals[++j] = -1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = -1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = -1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = -1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = -1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = -1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;


        // y-plus
        _normals[++j] = 0.0f; _normals[++j] = 1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = 1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = 1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = 1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = 1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = 1.0f; _normals[++j] = 0.0f;

        // y-minus
        _normals[++j] = 0.0f; _normals[++j] = -1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = -1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = -1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = -1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = -1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = -1.0f; _normals[++j] = 0.0f;


        // z-plus
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = 1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = 1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = 1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = 1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = 1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = 1.0f;

        // z-minus
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = -1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = -1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = -1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = -1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = -1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = -1.0f;

        _vindex = i;
//------------------------------------
        int k = _cindex;

        // x-plus
        _coords_byte[++k] = 1; _coords_byte[++k] = 1;
        _coords_byte[++k] = 0; _coords_byte[++k] = 1;
        _coords_byte[++k] = 0; _coords_byte[++k] = 0;

        _coords_byte[++k] = 0; _coords_byte[++k] = 0;
        _coords_byte[++k] = 1; _coords_byte[++k] = 0;
        _coords_byte[++k] = 1; _coords_byte[++k] = 1;

        // x-minus
        _coords_byte[++k] = 1; _coords_byte[++k] = 1;
        _coords_byte[++k] = 0; _coords_byte[++k] = 1;
        _coords_byte[++k] = 0; _coords_byte[++k] = 0;

        _coords_byte[++k] = 0; _coords_byte[++k] = 0;
        _coords_byte[++k] = 1; _coords_byte[++k] = 0;
        _coords_byte[++k] = 1; _coords_byte[++k] = 1;


        // y-plus
        _coords_byte[++k] = 1; _coords_byte[++k] = 0;
        _coords_byte[++k] = 1; _coords_byte[++k] = 1;
        _coords_byte[++k] = 0; _coords_byte[++k] = 1;

        _coords_byte[++k] = 0; _coords_byte[++k] = 1;
        _coords_byte[++k] = 0; _coords_byte[++k] = 0;
        _coords_byte[++k] = 1; _coords_byte[++k] = 0;

        // y-minus
        _coords_byte[++k] = 1; _coords_byte[++k] = 0; // 0
        _coords_byte[++k] = 1; _coords_byte[++k] = 1; // 1
        _coords_byte[++k] = 0; _coords_byte[++k] = 1; // 2

        _coords_byte[++k] = 0; _coords_byte[++k] = 1; // 2
        _coords_byte[++k] = 0; _coords_byte[++k] = 0; // 3
        _coords_byte[++k] = 1; _coords_byte[++k] = 0; // 0


        // z-plus
        _coords_byte[++k] = 1; _coords_byte[++k] = 1;
        _coords_byte[++k] = 0; _coords_byte[++k] = 1;
        _coords_byte[++k] = 0; _coords_byte[++k] = 0;

        _coords_byte[++k] = 0; _coords_byte[++k] = 0;
        _coords_byte[++k] = 1; _coords_byte[++k] = 0;
        _coords_byte[++k] = 1; _coords_byte[++k] = 1;

        // z-minus
        _coords_byte[++k] = 0; _coords_byte[++k] = 1;
        _coords_byte[++k] = 0; _coords_byte[++k] = 0;
        _coords_byte[++k] = 1; _coords_byte[++k] = 0;

        _coords_byte[++k] = 1; _coords_byte[++k] = 0;
        _coords_byte[++k] = 1; _coords_byte[++k] = 1;
        _coords_byte[++k] = 0; _coords_byte[++k] = 1;

        _cindex = k;

//------------------------------------
        int c = _color_index;

        // x-plus
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;

        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;

        // x-minus
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;

        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;


        // y-plus
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;

        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;

        // y-minus
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;

        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;


        // z-plus
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;

        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;

        // z-minus
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;

        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;

        _color_index = c;

//	DumpVerticesBuffer(36);
//	DumpNormalsBuffer(36);
//	DumpCoordsByteBuffer(36);
//	DumpColorsBuffer(36);

        _vertices_count += 36;
    }


    public static void addQuad(float x, float y, float w, float h, Color color) {
        int i = _vindex;

        // vertices
        _vertices[++i] = x;         _vertices[++i] = y;
        _vertices[++i] = x + w;		_vertices[++i] = y;
        _vertices[++i] = x + w;		_vertices[++i] = y + h;

        _vertices[++i] = x + w;		_vertices[++i] = y + h;
        _vertices[++i] = x;         _vertices[++i] = y + h;
        _vertices[++i] = x;         _vertices[++i] = y;

        _vindex = i;

        // colors
        int c = _color_index;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;

        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _color_index = c;

        _vertices_count += 6;
    }


    public static void addQuad(float size, float tx, float ty, TexCoordsQuad coords) {
        int i = _vindex;

        _vertices[++i] = tx;			_vertices[++i] = ty;
        _vertices[++i] = tx + size;		_vertices[++i] = ty;
        _vertices[++i] = tx + size;		_vertices[++i] = ty + size;

        _vertices[++i] = tx + size;		_vertices[++i] = ty + size;
        _vertices[++i] = tx;			_vertices[++i] = ty + size;
        _vertices[++i] = tx;			_vertices[++i] = ty;

        _vindex = i;

        int k = _cindex;

        // coordinates
        _coords_float[++k] = coords.tx0.x; _coords_float[++k] = coords.tx0.y; // 0
        _coords_float[++k] = coords.tx1.x; _coords_float[++k] = coords.tx1.y; // 1
        _coords_float[++k] = coords.tx2.x; _coords_float[++k] = coords.tx2.y; // 2

        _coords_float[++k] = coords.tx2.x; _coords_float[++k] = coords.tx2.y; // 2
        _coords_float[++k] = coords.tx3.x; _coords_float[++k] = coords.tx3.y; // 3
        _coords_float[++k] = coords.tx0.x; _coords_float[++k] = coords.tx0.y; // 0

        _cindex = k;

        _vertices_count += 6;
    }

    public static void addQuad(float size, float tx, float ty, TexCoordsQuad coords, Color color) {
        int i = _vindex;

        // vertices
        _vertices[++i] = tx;			_vertices[++i] = ty;
        _vertices[++i] = tx + size;		_vertices[++i] = ty;
        _vertices[++i] = tx + size;		_vertices[++i] = ty + size;

        _vertices[++i] = tx + size;		_vertices[++i] = ty + size;
        _vertices[++i] = tx;			_vertices[++i] = ty + size;
        _vertices[++i] = tx;			_vertices[++i] = ty;

        _vindex = i;

        int k = _cindex;

        // coordinates
        _coords_float[++k] = coords.tx0.x; _coords_float[++k] = coords.tx0.y; // 0
        _coords_float[++k] = coords.tx1.x; _coords_float[++k] = coords.tx1.y; // 1
        _coords_float[++k] = coords.tx2.x; _coords_float[++k] = coords.tx2.y; // 2

        _coords_float[++k] = coords.tx2.x; _coords_float[++k] = coords.tx2.y; // 2
        _coords_float[++k] = coords.tx3.x; _coords_float[++k] = coords.tx3.y; // 3
        _coords_float[++k] = coords.tx0.x; _coords_float[++k] = coords.tx0.y; // 0

        _cindex = k;

        int c = _color_index;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;

        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; _colors_ubyte[++c] = color.g; _colors_ubyte[++c] = color.b; _colors_ubyte[++c] = color.a;
        _color_index = c;

        _vertices_count += 6;
    }

    public static void addPoint(float x, float y, float z) {
        int i = _vindex;

        // z-minus
        _vertices[++i] = x;
        _vertices[++i] = y;
        _vertices[++i] = z;

        _vindex = i;

        ++_vertices_count;
    }

    public static void addPoint(Vector pos, Color color) {
        int i = _vindex;

        _vertices[++i] = pos.x;
        _vertices[++i] = pos.y;
        _vertices[++i] = pos.z;

        _vindex = i;

        int c = _color_index;

        _colors_ubyte[++c] = color.r;
        _colors_ubyte[++c] = color.g;
        _colors_ubyte[++c] = color.b;
        _colors_ubyte[++c] = color.a;

        _color_index = c;

        ++_vertices_count;
    }

    public static void addPoint2D(float x, float y) {
        int i = _vindex;

        // z-minus
        _vertices[++i] = x;
        _vertices[++i] = y;

        _vindex = i;

        ++_vertices_count;
    }

    public static void addCubeFace_X_Plus(float tx, float ty, float tz) {
        int i = _vindex;
        int j = _vindex;

// vertices
        // x                                   y                                      z
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 12
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 1 - 13
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 2 - 14

        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 2 - 15
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 3 - 16
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 17

        _vindex = i;

// normals
        _normals[++j] = 1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = 1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = 1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = 1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = 1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = 1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;

        int k = _cindex;

// coordinates
        _coords_byte[++k] = 1; _coords_byte[++k] = 1; // 0
        _coords_byte[++k] = 0; _coords_byte[++k] = 1; // 1
        _coords_byte[++k] = 0; _coords_byte[++k] = 0; // 2

        _coords_byte[++k] = 0; _coords_byte[++k] = 0; // 2
        _coords_byte[++k] = 1; _coords_byte[++k] = 0; // 3
        _coords_byte[++k] = 1; _coords_byte[++k] = 1; // 0

        _cindex = k;

        _vertices_count += 6;
    }

    public static void addCubeFace_X_Minus(float tx, float ty, float tz) {
        int i = _vindex;
        int j = _vindex;

// vertices
        // x                                   y                                      z
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 24
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 1 - 25
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 2 - 26

        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 2 - 27
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 3 - 28
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 29

        _vindex = i;

// normals
        _normals[++j] = -1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = -1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = -1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = -1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = -1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = -1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;

        int k = _cindex;

// coordinates
        _coords_byte[++k] = 1; _coords_byte[++k] = 1; // 0
        _coords_byte[++k] = 0; _coords_byte[++k] = 1; // 1
        _coords_byte[++k] = 0; _coords_byte[++k] = 0; // 2

        _coords_byte[++k] = 0; _coords_byte[++k] = 0; // 2
        _coords_byte[++k] = 1; _coords_byte[++k] = 0; // 3
        _coords_byte[++k] = 1; _coords_byte[++k] = 1; // 0

        _cindex = k;

        _vertices_count += 6;
    }

    public static void addCubeFace_Y_Plus(float tx, float ty, float tz) {
        int i = _vindex;
        int j = _vindex;

// vertices
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 0 - 30
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 1 - 31
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 2 - 32

        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz;  // 2 - 33
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz;  // 3 - 34
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz;  // 0 - 35

        _vindex = i;

// normals
        _normals[++j] = 0.0f; _normals[++j] = 1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = 1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = 1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = 1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = 1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = 1.0f; _normals[++j] = 0.0f;

        int k = _cindex;

// coordinates
//    _coords_byte[++k] = 1; _coords_byte[++k] = 0; // 0
//    _coords_byte[++k] = 0; _coords_byte[++k] = 0; // 1
//    _coords_byte[++k] = 0; _coords_byte[++k] = 1; // 2
//
//	_coords_byte[++k] = 0; _coords_byte[++k] = 1; // 2
//	_coords_byte[++k] = 1; _coords_byte[++k] = 1; // 3
//    _coords_byte[++k] = 1; _coords_byte[++k] = 0; // 0

        _coords_byte[++k] = 1; _coords_byte[++k] = 0;
        _coords_byte[++k] = 1; _coords_byte[++k] = 1;
        _coords_byte[++k] = 0; _coords_byte[++k] = 1;

        _coords_byte[++k] = 0; _coords_byte[++k] = 1;
        _coords_byte[++k] = 0; _coords_byte[++k] = 0;
        _coords_byte[++k] = 1; _coords_byte[++k] = 0;


        _cindex = k;

        _vertices_count += 6;
    }

    public static void addCubeFace_Y_Minus(float tx, float ty, float tz) {
        int i = _vindex;
        int j = _vindex;

// vertices
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 18
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 1 - 19
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 2 - 20

        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 2 - 21
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 3 - 22
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 23

        _vindex = i;

// normals
        _normals[++j] = 0.0f; _normals[++j] = -1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = -1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = -1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = -1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = -1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = -1.0f; _normals[++j] = 0.0f;

        int k = _cindex;

// coordinates
        _coords_byte[++k] = 1; _coords_byte[++k] = 1; // 0
        _coords_byte[++k] = 0; _coords_byte[++k] = 1; // 1
        _coords_byte[++k] = 0; _coords_byte[++k] = 0; // 2

        _coords_byte[++k] = 0; _coords_byte[++k] = 0; // 2
        _coords_byte[++k] = 1; _coords_byte[++k] = 0; // 3
        _coords_byte[++k] = 1; _coords_byte[++k] = 1; // 0

        _cindex = k;

        _vertices_count += 6;
    }

    public static void addCubeFace_Z_Plus(float tx, float ty, float tz) {
        int i = _vindex;
        int j = _vindex;

// vertices
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 0 - 6
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 1 - 7
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 2 - 8

        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 2 - 9
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 3 - 10
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 0 - 11

        _vindex = i;

// normals
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = 1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = 1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = 1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = 1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = 1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = 1.0f;

        int k = _cindex;

// coordinates
        _coords_byte[++k] = 1; _coords_byte[++k] = 1; // 0
        _coords_byte[++k] = 0; _coords_byte[++k] = 1; // 1
        _coords_byte[++k] = 0; _coords_byte[++k] = 0; // 2

        _coords_byte[++k] = 0; _coords_byte[++k] = 0; // 2
        _coords_byte[++k] = 1; _coords_byte[++k] = 0; // 3
        _coords_byte[++k] = 1; _coords_byte[++k] = 1; // 0

        _cindex = k;

        _vertices_count += 6;
    }

    public static void addCubeFace_Z_Minus(float tx, float ty, float tz) {
        int i = _vindex;
        int j = _vindex;

// vertices
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 0
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 1 - 1
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 2 - 2

        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 2 - 3
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 3 - 4
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 5

        _vindex = i;

// normals
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = -1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = -1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = -1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = -1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = -1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = -1.0f;

        int k = _cindex;

// coordinates
        _coords_byte[++k] = 1; _coords_byte[++k] = 0; // 0
        _coords_byte[++k] = 1; _coords_byte[++k] = 1; // 1
        _coords_byte[++k] = 0; _coords_byte[++k] = 1; // 2

        _coords_byte[++k] = 0; _coords_byte[++k] = 1; // 2
        _coords_byte[++k] = 0; _coords_byte[++k] = 0; // 3
        _coords_byte[++k] = 1; _coords_byte[++k] = 0; // 0

        _cindex = k;

        _vertices_count += 6;
    }

    public static void addCubeFace_X_Plus(float tx, float ty, float tz, TexCoordsQuad coords) {
        int i = _vindex;
        int j = _vindex;

        // vertices
        // x-plus
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 12
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 1 - 13
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 2 - 14

        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 2 - 15
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 3 - 16
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 17

        _vindex = i;

        // normals
        // left
        _normals[++j] = 1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = 1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = 1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = 1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = 1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = 1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;

        int k = _cindex;

// coordinates

        // left
//  _coords[++k] = 1; _coords[++k] = 0; // 0
//  _coords[++k] = 0; _coords[++k] = 0; // 1
//  _coords[++k] = 0; _coords[++k] = 1; // 2
//
//	_coords[++k] = 0; _coords[++k] = 1; // 2
//  _coords[++k] = 1; _coords[++k] = 1; // 3
//  _coords[++k] = 1; _coords[++k] = 0; // 0

        _coords_float[++k] = coords.tx0.x; _coords_float[++k] = coords.tx0.y; // 0
        _coords_float[++k] = coords.tx1.x; _coords_float[++k] = coords.tx1.y; // 1
        _coords_float[++k] = coords.tx2.x; _coords_float[++k] = coords.tx2.y; // 2

        _coords_float[++k] = coords.tx2.x; _coords_float[++k] = coords.tx2.y; // 2
        _coords_float[++k] = coords.tx3.x; _coords_float[++k] = coords.tx3.y; // 3
        _coords_float[++k] = coords.tx0.x; _coords_float[++k] = coords.tx0.y; // 0

        _cindex = k;

        _vertices_count += 6;
    }

    public static void addCubeFace_X_Minus(float tx, float ty, float tz, TexCoordsQuad coords) {
        int i = _vindex;
        int j = _vindex;

        // vertices
        // x-minus
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 24
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 1 - 25
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 2 - 26

        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz;  // 2 - 27
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz;  // 3 - 28
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz;  // 0 - 29

        _vindex = i;

        // normals
        // right
        _normals[++j] = -1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = -1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = -1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = -1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = -1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = -1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;

        int k = _cindex;

        // coordinates
        // right
//    _coords[++k] = 1; _coords[++k] = 1; // 0
//    _coords[++k] = 0; _coords[++k] = 1; // 1
//    _coords[++k] = 0; _coords[++k] = 0; // 2
//
//    _coords[++k] = 0; _coords[++k] = 0; // 2
//	_coords[++k] = 1; _coords[++k] = 0; // 3
//    _coords[++k] = 1; _coords[++k] = 1; // 0

        _coords_float[++k] = coords.tx0.x; _coords_float[++k] = coords.tx0.y; // 0
        _coords_float[++k] = coords.tx1.x; _coords_float[++k] = coords.tx1.y; // 1
        _coords_float[++k] = coords.tx2.x; _coords_float[++k] = coords.tx2.y; // 2

        _coords_float[++k] = coords.tx2.x; _coords_float[++k] = coords.tx2.y; // 2
        _coords_float[++k] = coords.tx3.x; _coords_float[++k] = coords.tx3.y; // 3
        _coords_float[++k] = coords.tx0.x; _coords_float[++k] = coords.tx0.y; // 0

        _cindex = k;

        _vertices_count += 6;
    }

    public static void addCubeFace_Y_Plus(float tx, float ty, float tz, TexCoordsQuad coords) {
        int i = _vindex;
        int j = _vindex;

        // vertices
        // y-plus
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 0 - 30
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 1 - 31
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 2 - 32

        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz;  // 2 - 33
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz;  // 3 - 34
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz;  // 0 - 35

        _vindex = i;

        // normals
        // top
        _normals[++j] = 0.0f; _normals[++j] = 1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = 1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = 1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = 1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = 1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = 1.0f; _normals[++j] = 0.0f;

        int k = _cindex;

        // coordinates
        // top
//    _coords[++k] = 1; _coords[++k] = 0; // 0
//    _coords[++k] = 0; _coords[++k] = 0; // 1
//    _coords[++k] = 0; _coords[++k] = 1; // 2
//
//	_coords[++k] = 0; _coords[++k] = 1; // 2
//	_coords[++k] = 1; _coords[++k] = 1; // 3
//    _coords[++k] = 1; _coords[++k] = 0; // 0

        _coords_float[++k] = coords.tx0.x; _coords_float[++k] = coords.tx0.y; // 0
        _coords_float[++k] = coords.tx1.x; _coords_float[++k] = coords.tx1.y; // 1
        _coords_float[++k] = coords.tx2.x; _coords_float[++k] = coords.tx2.y; // 2

        _coords_float[++k] = coords.tx2.x; _coords_float[++k] = coords.tx2.y; // 2
        _coords_float[++k] = coords.tx3.x; _coords_float[++k] = coords.tx3.y; // 3
        _coords_float[++k] = coords.tx0.x; _coords_float[++k] = coords.tx0.y; // 0

        _cindex = k;

        _vertices_count += 6;
    }

    public static void addCubeFace_Y_Minus(float tx, float ty, float tz, TexCoordsQuad coords) {
        int i = _vindex;
        int j = _vindex;

        // vertices
        // y-minus
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 18
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 1 - 19
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 2 - 20

        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 2 - 21
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 3 - 22
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 23

        _vindex = i;

        // normals
        // bottom
        _normals[++j] = 0.0f; _normals[++j] = -1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = -1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = -1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = -1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = -1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = -1.0f; _normals[++j] = 0.0f;

        int k = _cindex;

// coordinates
        // bottom
//		_coords[++k] = 1; _coords[++k] = 1; // 0
//		_coords[++k] = 0; _coords[++k] = 1; // 1
//		_coords[++k] = 0; _coords[++k] = 0; // 2
//
//		_coords[++k] = 0; _coords[++k] = 0; // 2
//		_coords[++k] = 1; _coords[++k] = 0; // 3
//		_coords[++k] = 1; _coords[++k] = 1; // 0

        _coords_float[++k] = coords.tx0.x; _coords_float[++k] = coords.tx0.y; // 0
        _coords_float[++k] = coords.tx1.x; _coords_float[++k] = coords.tx1.y; // 1
        _coords_float[++k] = coords.tx2.x; _coords_float[++k] = coords.tx2.y; // 2

        _coords_float[++k] = coords.tx2.x; _coords_float[++k] = coords.tx2.y; // 2
        _coords_float[++k] = coords.tx3.x; _coords_float[++k] = coords.tx3.y; // 3
        _coords_float[++k] = coords.tx0.x; _coords_float[++k] = coords.tx0.y; // 0

        _cindex = k;

        _vertices_count += 6;
    }

    public static void addCubeFace_Z_Plus(float tx, float ty, float tz, TexCoordsQuad coords) {
        int i = _vindex;
        int j = _vindex;

        // vertices
        // z-plus
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 0 - 6
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 1 - 7
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 2 - 8

        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 2 - 9
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 3 - 10
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 0 - 11

        _vindex = i;

        // normals
        // front
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = 1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = 1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = 1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = 1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = 1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = 1.0f;

        int k = _cindex;

        // coordinates
        // front
//    _coords_float[++k] = 1; _coords_float[++k] = 1; // 0
//    _coords_float[++k] = 0; _coords_float[++k] = 1; // 1
//    _coords_float[++k] = 0; _coords_float[++k] = 0; // 2
//
//	_coords_float[++k] = 0; _coords_float[++k] = 0; // 2
//    _coords_float[++k] = 1; _coords_float[++k] = 0; // 3
//    _coords_float[++k] = 1; _coords_float[++k] = 1; // 0


        _coords_float[++k] = coords.tx0.x; _coords_float[++k] = coords.tx0.y; // 0
        _coords_float[++k] = coords.tx1.x; _coords_float[++k] = coords.tx1.y; // 1
        _coords_float[++k] = coords.tx2.x; _coords_float[++k] = coords.tx2.y; // 2

        _coords_float[++k] = coords.tx2.x; _coords_float[++k] = coords.tx2.y; // 2
        _coords_float[++k] = coords.tx3.x; _coords_float[++k] = coords.tx3.y; // 3
        _coords_float[++k] = coords.tx0.x; _coords_float[++k] = coords.tx0.y; // 0

        _cindex = k;

        _vertices_count += 6;
    }

    public static void addCubeFace_Z_Minus(float tx, float ty, float tz, TexCoordsQuad coords) {
        int i = _vindex;
        int j = _vindex;

        // z-minus
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 0
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 1 - 1
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 2 - 2

        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 2 - 3
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 3 - 4
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 5

        _vindex = i;

        // back
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = -1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = -1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = -1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = -1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = -1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = -1.0f;

        int k = _cindex;

        // back
//	_coords_float[++k] = 1; _coords_float[++k] = 0; // 0
//	_coords_float[++k] = 1; _coords_float[++k] = 1; // 1
//	_coords_float[++k] = 0; _coords_float[++k] = 1; // 2
//
//	_coords_float[++k] = 0; _coords_float[++k] = 1; // 2
//	_coords_float[++k] = 0; _coords_float[++k] = 0; // 3
//	_coords_float[++k] = 1; _coords_float[++k] = 0; // 0

        _coords_float[++k] = coords.tx0.x; _coords_float[++k] = coords.tx0.y; // 0
        _coords_float[++k] = coords.tx1.x; _coords_float[++k] = coords.tx1.y; // 1
        _coords_float[++k] = coords.tx2.x; _coords_float[++k] = coords.tx2.y; // 2

        _coords_float[++k] = coords.tx2.x; _coords_float[++k] = coords.tx2.y; // 2
        _coords_float[++k] = coords.tx3.x; _coords_float[++k] = coords.tx3.y; // 3
        _coords_float[++k] = coords.tx0.x; _coords_float[++k] = coords.tx0.y; // 0

        _cindex = k;

        _vertices_count += 6;
    }

    public static void addCubeFace_X_Plus(float tx, float ty, float tz, TexCoordsQuad coords, Color color) {
        int i = _vindex;
        int j = _vindex;

        // vertices
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 12
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 1 - 13
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 2 - 14

        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 2 - 15
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 3 - 16
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 17

        _vindex = i;

        // normals
        _normals[++j] = 1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = 1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = 1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;

        _normals[++j] = 1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = 1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = 1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;

        // textures
        int k = _cindex;
        _coords_float[++k] = coords.tx0.x; _coords_float[++k] = coords.tx0.y; // 0
        _coords_float[++k] = coords.tx1.x; _coords_float[++k] = coords.tx1.y; // 1
        _coords_float[++k] = coords.tx2.x; _coords_float[++k] = coords.tx2.y; // 2

        _coords_float[++k] = coords.tx2.x; _coords_float[++k] = coords.tx2.y; // 2
        _coords_float[++k] = coords.tx3.x; _coords_float[++k] = coords.tx3.y; // 3
        _coords_float[++k] = coords.tx0.x; _coords_float[++k] = coords.tx0.y; // 0

        _cindex = k;

        // colors
        int c = _color_index;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;

        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;

        _color_index = c;

        _vertices_count += 6;
    }

    public static void addCubeFace_X_Minus(float tx, float ty, float tz, TexCoordsQuad coords, Color color) {
        int i = _vindex;
        int j = _vindex;

        // vertices
        // x-minus
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 24
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 1 - 25
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 2 - 26

        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz;  // 2 - 27
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz;  // 3 - 28
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz;  // 0 - 29

        _vindex = i;

        // normals
        // right
        _normals[++j] = -1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = -1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = -1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = -1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = -1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = -1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;

        int k = _cindex;

        _coords_float[++k] = coords.tx0.x; _coords_float[++k] = coords.tx0.y; // 0
        _coords_float[++k] = coords.tx1.x; _coords_float[++k] = coords.tx1.y; // 1
        _coords_float[++k] = coords.tx2.x; _coords_float[++k] = coords.tx2.y; // 2

        _coords_float[++k] = coords.tx2.x; _coords_float[++k] = coords.tx2.y; // 2
        _coords_float[++k] = coords.tx3.x; _coords_float[++k] = coords.tx3.y; // 3
        _coords_float[++k] = coords.tx0.x; _coords_float[++k] = coords.tx0.y; // 0

        _cindex = k;

        // colors
        int c = _color_index;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;

        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;

        _color_index = c;

        _vertices_count += 6;
    }

    public static void addCubeFace_Y_Plus(float tx, float ty, float tz, TexCoordsQuad coords, Color color) {
        int i = _vindex;
        int j = _vindex;

        // vertices
        // y-plus
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 0 - 30
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 1 - 31
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 2 - 32

        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz;  // 2 - 33
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz;  // 3 - 34
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz;  // 0 - 35

        _vindex = i;

        // normals
        // top
        _normals[++j] = 0.0f; _normals[++j] = 1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = 1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = 1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = 1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = 1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = 1.0f; _normals[++j] = 0.0f;

        int k = _cindex;

        // coordinates
        _coords_float[++k] = coords.tx0.x; _coords_float[++k] = coords.tx0.y; // 0
        _coords_float[++k] = coords.tx1.x; _coords_float[++k] = coords.tx1.y; // 1
        _coords_float[++k] = coords.tx2.x; _coords_float[++k] = coords.tx2.y; // 2

        _coords_float[++k] = coords.tx2.x; _coords_float[++k] = coords.tx2.y; // 2
        _coords_float[++k] = coords.tx3.x; _coords_float[++k] = coords.tx3.y; // 3
        _coords_float[++k] = coords.tx0.x; _coords_float[++k] = coords.tx0.y; // 0

        _cindex = k;

        // colors
        int c = _color_index;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;

        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;

        _color_index = c;

        _vertices_count += 6;
    }

    public static void addCubeFace_Y_Minus(float tx, float ty, float tz, TexCoordsQuad coords, Color color) {
        int i = _vindex;
        int j = _vindex;

        // vertices
        // y-minus
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 18
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 1 - 19
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 2 - 20

        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 2 - 21
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 3 - 22
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 23

        _vindex = i;

        // normals
        // bottom
        _normals[++j] = 0.0f; _normals[++j] = -1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = -1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = -1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = -1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = -1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = -1.0f; _normals[++j] = 0.0f;

        int k = _cindex;

        _coords_float[++k] = coords.tx0.x; _coords_float[++k] = coords.tx0.y; // 0
        _coords_float[++k] = coords.tx1.x; _coords_float[++k] = coords.tx1.y; // 1
        _coords_float[++k] = coords.tx2.x; _coords_float[++k] = coords.tx2.y; // 2

        _coords_float[++k] = coords.tx2.x; _coords_float[++k] = coords.tx2.y; // 2
        _coords_float[++k] = coords.tx3.x; _coords_float[++k] = coords.tx3.y; // 3
        _coords_float[++k] = coords.tx0.x; _coords_float[++k] = coords.tx0.y; // 0

        _cindex = k;

        // colors
        int c = _color_index;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;

        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;

        _color_index = c;

        _vertices_count += 6;
    }

    public static void addCubeFace_Z_Plus(float tx, float ty, float tz, TexCoordsQuad coords, Color color) {
        int i = _vindex;
        int j = _vindex;

        // vertices
        // z-plus
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 0 - 6
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 1 - 7
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 2 - 8

        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 2 - 9
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 3 - 10
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 0 - 11

        _vindex = i;

        // normals
        // front
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = 1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = 1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = 1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = 1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = 1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = 1.0f;

        int k = _cindex;

        _coords_float[++k] = coords.tx0.x; _coords_float[++k] = coords.tx0.y; // 0
        _coords_float[++k] = coords.tx1.x; _coords_float[++k] = coords.tx1.y; // 1
        _coords_float[++k] = coords.tx2.x; _coords_float[++k] = coords.tx2.y; // 2

        _coords_float[++k] = coords.tx2.x; _coords_float[++k] = coords.tx2.y; // 2
        _coords_float[++k] = coords.tx3.x; _coords_float[++k] = coords.tx3.y; // 3
        _coords_float[++k] = coords.tx0.x; _coords_float[++k] = coords.tx0.y; // 0

        _cindex = k;

        // colors
        int c = _color_index;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;

        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;

        _color_index = c;

        _vertices_count += 6;
    }

    public static void addCubeFace_Z_Minus(float tx, float ty, float tz, TexCoordsQuad coords, Color color) {
        int i = _vindex;
        int j = _vindex;

        // z-minus
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 0
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 1 - 1
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 2 - 2

        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 2 - 3
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 3 - 4
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 5

        _vindex = i;

        // back
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = -1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = -1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = -1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = -1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = -1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = -1.0f;

        int k = _cindex;

        _coords_float[++k] = coords.tx0.x; _coords_float[++k] = coords.tx0.y; // 0
        _coords_float[++k] = coords.tx1.x; _coords_float[++k] = coords.tx1.y; // 1
        _coords_float[++k] = coords.tx2.x; _coords_float[++k] = coords.tx2.y; // 2

        _coords_float[++k] = coords.tx2.x; _coords_float[++k] = coords.tx2.y; // 2
        _coords_float[++k] = coords.tx3.x; _coords_float[++k] = coords.tx3.y; // 3
        _coords_float[++k] = coords.tx0.x; _coords_float[++k] = coords.tx0.y; // 0

        _cindex = k;

        // colors
        int c = _color_index;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;

        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;

        _color_index = c;

        _vertices_count += 6;
    }

    public static void addCubeFace_X_Plus(float tx, float ty, float tz, Color color) {
        int i = _vindex;
        int j = _vindex;

        // vertices
        // x                                   y                                      z
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 12
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 1 - 13
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 2 - 14

        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 2 - 15
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 3 - 16
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 17

        _vindex = i;

        // normals
        _normals[++j] = 1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = 1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = 1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = 1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = 1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = 1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;

        int k = _cindex;

        // coordinates
        _coords_byte[++k] = 1; _coords_byte[++k] = 1; // 0
        _coords_byte[++k] = 0; _coords_byte[++k] = 1; // 1
        _coords_byte[++k] = 0; _coords_byte[++k] = 0; // 2

        _coords_byte[++k] = 0; _coords_byte[++k] = 0; // 2
        _coords_byte[++k] = 1; _coords_byte[++k] = 0; // 3
        _coords_byte[++k] = 1; _coords_byte[++k] = 1; // 0

        _cindex = k;

        // colors
        int c = _color_index;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;

        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;

        _color_index = c;

        _vertices_count += 6;
    }

    public static void addCubeFace_X_Minus(float tx, float ty, float tz, Color color) {
        int i = _vindex;
        int j = _vindex;

        // vertices
        // x                                   y                                      z
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 24
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 1 - 25
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 2 - 26

        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 2 - 27
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 3 - 28
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 29

        _vindex = i;

        // normals
        _normals[++j] = -1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = -1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = -1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = -1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = -1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;
        _normals[++j] = -1.0f; _normals[++j] = 0.0f; _normals[++j] = 0.0f;

        int k = _cindex;

        // coordinates
        _coords_byte[++k] = 1; _coords_byte[++k] = 1; // 0
        _coords_byte[++k] = 0; _coords_byte[++k] = 1; // 1
        _coords_byte[++k] = 0; _coords_byte[++k] = 0; // 2

        _coords_byte[++k] = 0; _coords_byte[++k] = 0; // 2
        _coords_byte[++k] = 1; _coords_byte[++k] = 0; // 3
        _coords_byte[++k] = 1; _coords_byte[++k] = 1; // 0

        _cindex = k;

        // colors
        int c = _color_index;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;

        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;

        _color_index = c;

        _vertices_count += 6;
    }

    public static void addCubeFace_Y_Plus(float tx, float ty, float tz, Color color) {
        int i = _vindex;
        int j = _vindex;

        // vertices
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 0 - 30
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 1 - 31
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 2 - 32

        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz;  // 2 - 33
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz;  // 3 - 34
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz;  // 0 - 35

        _vindex = i;

        // normals
        _normals[++j] = 0.0f; _normals[++j] = 1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = 1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = 1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = 1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = 1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = 1.0f; _normals[++j] = 0.0f;

        int k = _cindex;

        // coordinates
        //    _coords_byte[++k] = 1; _coords_byte[++k] = 0; // 0
        //    _coords_byte[++k] = 0; _coords_byte[++k] = 0; // 1
        //    _coords_byte[++k] = 0; _coords_byte[++k] = 1; // 2
        //
        //	_coords_byte[++k] = 0; _coords_byte[++k] = 1; // 2
        //	_coords_byte[++k] = 1; _coords_byte[++k] = 1; // 3
        //    _coords_byte[++k] = 1; _coords_byte[++k] = 0; // 0

        _coords_byte[++k] = 1; _coords_byte[++k] = 0;
        _coords_byte[++k] = 1; _coords_byte[++k] = 1;
        _coords_byte[++k] = 0; _coords_byte[++k] = 1;

        _coords_byte[++k] = 0; _coords_byte[++k] = 1;
        _coords_byte[++k] = 0; _coords_byte[++k] = 0;
        _coords_byte[++k] = 1; _coords_byte[++k] = 0;

        _cindex = k;

        // colors
        int c = _color_index;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;

        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;

        _color_index = c;

        _vertices_count += 6;
    }

    public static void addCubeFace_Y_Minus(float tx, float ty, float tz, Color color) {
        int i = _vindex;
        int j = _vindex;

        // vertices
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 18
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 1 - 19
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 2 - 20

        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 2 - 21
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 3 - 22
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 23

        _vindex = i;

        // normals
        _normals[++j] = 0.0f; _normals[++j] = -1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = -1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = -1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = -1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = -1.0f; _normals[++j] = 0.0f;
        _normals[++j] = 0.0f; _normals[++j] = -1.0f; _normals[++j] = 0.0f;

        int k = _cindex;

        // coordinates
        _coords_byte[++k] = 1; _coords_byte[++k] = 1; // 0
        _coords_byte[++k] = 0; _coords_byte[++k] = 1; // 1
        _coords_byte[++k] = 0; _coords_byte[++k] = 0; // 2

        _coords_byte[++k] = 0; _coords_byte[++k] = 0; // 2
        _coords_byte[++k] = 1; _coords_byte[++k] = 0; // 3
        _coords_byte[++k] = 1; _coords_byte[++k] = 1; // 0

        _cindex = k;

        // colors
        int c = _color_index;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;

        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;

        _color_index = c;

        _vertices_count += 6;
    }

    public static void addCubeFace_Z_Plus(float tx, float ty, float tz, Color color) {
        int i = _vindex;
        int j = _vindex;

        // vertices
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 0 - 6
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 1 - 7
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 2 - 8

        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 2 - 9
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 3 - 10
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] =  HALF_CUBE_SIZE + tz; // 0 - 11

        _vindex = i;

        // normals
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = 1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = 1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = 1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = 1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = 1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = 1.0f;

        int k = _cindex;

        // coordinates
        _coords_byte[++k] = 1; _coords_byte[++k] = 1; // 0
        _coords_byte[++k] = 0; _coords_byte[++k] = 1; // 1
        _coords_byte[++k] = 0; _coords_byte[++k] = 0; // 2

        _coords_byte[++k] = 0; _coords_byte[++k] = 0; // 2
        _coords_byte[++k] = 1; _coords_byte[++k] = 0; // 3
        _coords_byte[++k] = 1; _coords_byte[++k] = 1; // 0

        _cindex = k;

        // colors
        int c = _color_index;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;

        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;

        _color_index = c;

        _vertices_count += 6;
    }

    public static void addCubeFace_Z_Minus(float tx, float ty, float tz, Color color) {
        int i = _vindex;
        int j = _vindex;

        // vertices
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 0
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 1 - 1
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 2 - 2

        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] = -HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 2 - 3
        _vertices[++i] = -HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 3 - 4
        _vertices[++i] =  HALF_CUBE_SIZE + tx; _vertices[++i] =  HALF_CUBE_SIZE + ty; _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 5

        _vindex = i;

        // normals
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = -1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = -1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = -1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = -1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = -1.0f;
        _normals[++j] = 0.0f; _normals[++j] = 0.0f; _normals[++j] = -1.0f;

        int k = _cindex;

        // coordinates
        _coords_byte[++k] = 1; _coords_byte[++k] = 0; // 0
        _coords_byte[++k] = 1; _coords_byte[++k] = 1; // 1
        _coords_byte[++k] = 0; _coords_byte[++k] = 1; // 2

        _coords_byte[++k] = 0; _coords_byte[++k] = 1; // 2
        _coords_byte[++k] = 0; _coords_byte[++k] = 0; // 3
        _coords_byte[++k] = 1; _coords_byte[++k] = 0; // 0

        _cindex = k;

        // colors
        int c = _color_index;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;

        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;
        _colors_ubyte[++c] = color.r; 	_colors_ubyte[++c] = color.g; 	_colors_ubyte[++c] = color.b; 	_colors_ubyte[++c] = color.a;

        _color_index = c;

        _vertices_count += 6;
    }

}

package com.almagems.cubetraz;


import static android.opengl.GLES10.*;
import static android.opengl.GLU.*;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import static android.opengl.Matrix.*;
import static com.almagems.cubetraz.Constants.*;

import android.content.Context;
import android.opengl.GLES11;
import android.support.annotation.Nullable;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;


public final class Graphics {

    public GL10 gl;

    public static int width;
    public static int height;
    public static int half_width;
    public static int half_height;
    public static float device_scale = 2f;

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
    private ArrayList<Texture> textures = new ArrayList<Texture>(20);

    public int _vertices_count = 0;
    public int _vindex = -1;
    public int _cindex = -1;
    public int _color_index = -1;

    private float[] _vertices = new float[BUF_SIZE * KILOBYTE];  /*3 * 36 * MAX_CUBE_COUNT * MAX_CUBE_COUNT * MAX_CUBE_COUNT */
    private float[] _normals = new float[BUF_SIZE * KILOBYTE];
    private float[] _coords = new float[BUF_SIZE * KILOBYTE];
    private byte[] _colors = new byte[BUF_SIZE * KILOBYTE];

    public boolean _blending_enabled = false;

    public float screenWidth;
    public float screenHeight;
    public float aspectRatio;
    public final float referenceScreenWidth = 1080f;
    public float scaleFactor;

    public Context context;

    private FloatBuffer _vertexBuffer;
    private FloatBuffer _normalBuffer;
    private FloatBuffer _coordsBuffer;
    private ByteBuffer _colorBuffer;


    //public Map<String, TexturedQuad> fonts = new HashMap<String, TexturedQuad>();


    // ctor
    public Graphics(Context context, GL10 gl) {
        System.out.println("Graphics ctor...");
        this.context = context;
        this.gl = gl;
    }

    public void initialSetup(int width, int height) {
        Graphics.width = width;
        Graphics.height = height;

        half_width = width / 2;
        half_height = height / 2;

        aspectRatio = (float) width / (float) height;

        glViewport(0, 0, width, height);

        //m_menu.SetupCameras(); TODO!


//        m_banner_height = banner_height;
//        m_scaleFactor = scaleFactor;
//        this.device_type = device_type;
//
//        // create packed depth & stencil buffer with same size as the color buffer
//        glGenRenderbuffersOES(1, &m_depthstencilbuffer);
//        glBindRenderbufferOES(GL_RENDERBUFFER_OES, m_depthstencilbuffer);
//        glRenderbufferStorageOES(GL_RENDERBUFFER_OES, GL_DEPTH24_STENCIL8_OES, width, height);
//
//        // Create the framebuffer object and attach
//        // - the color buffer
//        // - the packed depth & stencil buffer
//        glGenFramebuffersOES(1, &m_framebuffer);
//        glBindFramebufferOES(GL_FRAMEBUFFER_OES, m_framebuffer);
//
//        glFramebufferRenderbufferOES(GL_FRAMEBUFFER_OES, GL_COLOR_ATTACHMENT0_OES, GL_RENDERBUFFER_OES, m_colorbuffer);         // color
//        glFramebufferRenderbufferOES(GL_FRAMEBUFFER_OES, GL_DEPTH_ATTACHMENT_OES, GL_RENDERBUFFER_OES, m_depthstencilbuffer);   // depth
//        glFramebufferRenderbufferOES(GL_FRAMEBUFFER_OES, GL_STENCIL_ATTACHMENT_OES, GL_RENDERBUFFER_OES, m_depthstencilbuffer); // stencil
//
//        glBindRenderbufferOES(GL_RENDERBUFFER_OES, m_colorbuffer);
//
//        GLenum status = glCheckFramebufferStatusOES(GL_FRAMEBUFFER_OES);
//
//        if (GL_FRAMEBUFFER_COMPLETE_OES != status) {
////        printf("\nFailure with framebuffer generation: %d", glCheckFramebufferStatusOES(GL_FRAMEBUFFER_OES));
//
//            switch (status) {
//                case GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_OES:
////                printf("\nIncomplete!!!");
//                    break;
//
//                case GL_FRAMEBUFFER_UNSUPPORTED_OES:
////                printf("\nUnsupported!!!");
//                    break;
//            }
//        }
//
//        int h = m_height - banner_height;

//        m_fbo.createWithColorAndDepthStencilBuffer(width, height);
//
//        // make the OpenGL ModelView matrix the default
//        glMatrixMode(GL_MODELVIEW);
//
        //glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        //glClearColor(0.4f, 0.4f, 0.4f, 0.0f);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
//
//        // setup material properties
//        vec4 specular(1.0f, 1.0f, 1.0f, 1.0f);
//        glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, specular.Pointer());
//        glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, 35.0f);
//
        glEnable(GL_COLOR_MATERIAL);

        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);

//        // create frame buffer object
//        glBindFramebufferOES(GL_FRAMEBUFFER_OES, m_framebuffer);
//        glBindRenderbufferOES(GL_RENDERBUFFER_OES, m_colorbuffer);
//
//        glEnableClientState(GL_VERTEX_ARRAY);
//        glEnableClientState(GL_COLOR_ARRAY);
//
//        glGenRenderbuffersOES(1, m_colorbuffer);
//        glBindRenderbufferOES(GL_RENDERBUFFER_OES, m_colorbuffer);
//
//        glEnable(GL_CULL_FACE);
//        //glDisable(GL_CULL_FACE);
//
//        glDepthFunc(GL_LESS);
//        //glDepthMask(true);
//        glDisable(GL_DITHER);
//
//        //glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
//        //glBlendFunc(GL_ONE_MINUS_DST_ALPHA,GL_DST_ALPHA);
//        //glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
//        //glBlendFunc(GL_SRC_ALPHA, GL_ONE);
//        glBlendFunc(GL_ONE, GL_ONE);

        ByteBuffer vbb;

        vbb = ByteBuffer.allocateDirect(_vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        _vertexBuffer = vbb.asFloatBuffer();

        vbb = ByteBuffer.allocateDirect(_normals.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        _normalBuffer = vbb.asFloatBuffer();

        vbb = ByteBuffer.allocateDirect(_coords.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        _coordsBuffer = vbb.asFloatBuffer();

        _colorBuffer = ByteBuffer.allocateDirect(_colors.length);
    }

    public void updateBuffers() {
        _vertexBuffer.put(_vertices);
        _vertexBuffer.position(0);

        _colorBuffer.put(_colors);
        _colorBuffer.position(0);
    }

    public void updateBuffersAll() {
        _vertexBuffer.put(_vertices);
        _vertexBuffer.position(0);

        _normalBuffer.put(_normals);
        _normalBuffer.position(0);

        _coordsBuffer.put(_coords);
        _coordsBuffer.position(0);

        _colorBuffer.put(_colors);
        _colorBuffer.position(0);
    }

    public void updateViewProjMatrix() {
//		multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
//		invertM(invertedViewProjectionMatrix, 0, viewProjectionMatrix, 0);
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

    public void setProjectionMatrix2D() {
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


    public void warmCache() {
        glEnable(GL_TEXTURE_2D);

        prepare();
        setStreamSource();
        addCube(0.0f, 0.0f, 0.0f);
        glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_player);
        glDrawArrays(GL_TRIANGLES, 0, 36);

        glBindTexture(GL_TEXTURE_2D, Graphics.texture_id_gray_concrete);
        glDrawArrays(GL_TRIANGLES, 0, 36);

        glDisable(GL_TEXTURE_2D);
    }

    public void drawQuad() {
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
    }

    public void drawCube() {
        glDrawArrays(GL_TRIANGLES, 0, 36);
    }

    // draw cube face
    public void drawCubeFaceY_Plus() {
        glDrawArrays(GL_TRIANGLES, 30, 6);
    }

    public void drawCubeFaceY_Minus() {
        glDrawArrays(GL_TRIANGLES, 18, 6);
    }

    public void drawCubeFaceX_Plus() {
        glDrawArrays(GL_TRIANGLES, 12, 6);
    }

    public void drawCubeFaceX_Minus() {
        glDrawArrays(GL_TRIANGLES, 24, 6);
    }

    public void drawCubeFaceZ_Plus() {
        glDrawArrays(GL_TRIANGLES, 6, 6);
    }

    public void drawCubeFaceZ_Minus() {
        glDrawArrays(GL_TRIANGLES, 0, 6);
    }

    public static Color getColorFromScreen(Vector2 pos) {
        int ix = (int) pos.x; //(int)Engine.rawX; //(int)pos.x * width/2;
        int iy = height - (int) pos.y; //(int)Engine.rawY; //(int)pos.y * height/2;

        ByteBuffer pixelBuf = ByteBuffer.allocateDirect(1 * 4);
        pixelBuf.order(ByteOrder.LITTLE_ENDIAN);
        glReadPixels(ix, iy, 1, 1, GL_RGBA, GL_UNSIGNED_BYTE, pixelBuf);

        byte R = pixelBuf.get(0);
        byte G = pixelBuf.get(1);
        byte B = pixelBuf.get(2);
        byte A = pixelBuf.get(3);

        int r = R & 0xFF;
        int g = G & 0xFF;
        int b = B & 0xFF;
        int a = A & 0xFF;

        Color color = new Color(r, g, b, a);
//        color.r = (argb)       & 0xFF;
//        color.g = (argb >> 8)  & 0xFF;
//        color.b = (argb >> 16) & 0xFF;
//        color.a = (argb >> 24) & 0xFF;

        System.out.println("\nx:" + ix + ", y:" + iy + ", red:" + color.r + ", green:" + color.g + ", blue:" + color.b + ", alpha: " + color.a);
        return color;
    }

    public void drawAxes() {
        final float length = width;
        final float axis[] = {
                -length, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,    // x
                0.0f, -length, 0.0f, 0.0f, 0.0f, 0.0f,    // y
                0.0f, 0.0f, -length, 0.0f, 0.0f, 0.0f,    // z

                0.0f, 0.0f, 0.0f, length, 0.0f, 0.0f,    // x
                0.0f, 0.0f, 0.0f, 0.0f, length, 0.0f,    // y
                0.0f, 0.0f, 0.0f, 0.0f, 0.0f, length,    // z
        };

        byte maxColor = (byte) 255;

        final byte colors[] = {
                0, 0, maxColor, maxColor, 0, 0, maxColor, maxColor, // x
                0, maxColor, 0, maxColor, 0, maxColor, 0, maxColor, // y
                maxColor, 0, 0, maxColor, maxColor, 0, 0, maxColor, // z

                0, 0, maxColor, maxColor, 0, 0, maxColor, maxColor, // x
                0, maxColor, 0, maxColor, 0, maxColor, 0, maxColor, // y
                maxColor, 0, 0, maxColor, maxColor, 0, 0, maxColor, // z
        };

        prepare();

        _vertexBuffer.put(axis);
        _vertexBuffer.position(0);

        _colorBuffer.put(colors);
        _colorBuffer.position(0);

        glDisableClientState(GL_NORMAL_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);

        glVertexPointer(3, GL_FLOAT, 0, _vertexBuffer);
        glColorPointer(4, GL_UNSIGNED_BYTE, 0, _colorBuffer);
        glLineWidth(3f);

//        float[] model_matrix = new float[16];
//        GLES11.glGetFloatv(GLES11.GL_MODELVIEW_MATRIX, model_matrix, 0);

        glDrawArrays(GL_LINES, 6, 6);

        glLineWidth(1f);
        glDrawArrays(GL_LINES, 0, 6);
    }

    public void drawFBOTexture(int texture_id, Color color, boolean magic) {
        final float verts[] = {
                0f, height,
                0f, 0f,
                width, 0f,
                width, height
        };

        final float coords[] = {
                0f, 1f,
                0f, 0f,
                1f, 0f,
                1f, 1f
        };

//        if (magic) {
//            coords[1] = 1.0f - (m_banner_height / m_height);
//            coords[7] = coords[1];
//        }

        final byte colors[] = {
                color.R, color.G, color.B, color.A,
                color.R, color.G, color.B, color.A,
                color.R, color.G, color.B, color.A,
                color.R, color.G, color.B, color.A
        };

        _vertexBuffer.put(verts);
        _vertexBuffer.position(0);

        _coordsBuffer.put(coords);
        _coordsBuffer.position(0);

        _colorBuffer.put(colors);
        _colorBuffer.position(0);

        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);

        glVertexPointer(2, GL_FLOAT, 0, _vertexBuffer);
        glTexCoordPointer(2, GL_FLOAT, 0, _coordsBuffer);
        glColorPointer(4, GL_UNSIGNED_BYTE, 0, _colorBuffer);

        glBindTexture(GL_TEXTURE_2D, texture_id);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
    }

    public void setModelViewMatrix2D() {
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }

    public void setProjection2D() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrthof(0.0f, width, 0.0f, height, -1.0f, 1.0f);
    }

    public void setModelViewMatrix3D(final Camera camera) {
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

//        float[] model_matrix = new float[16];
//        GLES11.glGetFloatv(GLES11.GL_MODELVIEW_MATRIX, model_matrix, 0);

        gluLookAt(gl,
                camera.eye.x, camera.eye.y, camera.eye.z,
                camera.target.x, camera.target.y, camera.target.z,
                0f, 1f, 0f);

//        GLES11.glGetFloatv(GLES11.GL_MODELVIEW_MATRIX, model_matrix, 0);
    }

    public void setProjection3D() {
        final float zNear = 0.01f;
        final float zFar = 1000.0f;
        final float fieldOfView = 30.0f;
        final float size = zNear * (float) Math.tan((Math.toRadians(fieldOfView) / 2.0f));

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glFrustumf(-size, size, -size / aspectRatio, size / aspectRatio, zNear, zFar);
    }

    public void addCubeWithColor(float tx, float ty, float tz, Color color) {
        addCubeSize(tx, ty, tz, HALF_CUBE_SIZE, color);
    }

    public void enableBlending() {
        if (!_blending_enabled) {
            glEnable(GL_BLEND);
            glDisable(GL_DEPTH_TEST);
            _blending_enabled = true;
        }
    }

    public void disableBlending() {
        if (_blending_enabled) {
            glDisable(GL_BLEND);
            glEnable(GL_DEPTH_TEST);
            _blending_enabled = false;
        }
    }

    public void renderPoints() {
        glDrawArrays(GL_POINTS, 0, _vertices_count);
    }

    public void addCube(float tx, float ty, float tz) {
        final Color color = new Color(255, 255, 255, 255);
        addCubeSize(tx, ty, tz, HALF_CUBE_SIZE, color);
    }

    public void prepare() {
        _vertices_count = 0;
        _vindex = -1;
        _cindex = -1;
        _color_index = -1;
    }

    public void setStreamSource() {
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);

        glVertexPointer(3, GL_FLOAT, 0, _vertexBuffer);
        glNormalPointer(GL_FLOAT, 0, _normalBuffer);
        glTexCoordPointer(2, GL_FLOAT, 0, _coordsBuffer);
        glColorPointer(4, GL_UNSIGNED_BYTE, 0, _colorBuffer);

        //glEnableClientState(GL_NORMAL_ARRAY);
        //glEnableClientState(GL_TEXTURE_COORD_ARRAY);
    }

    public void loadStartupAssets() throws Exception {
        //System.out.println("Load startup Assets...");

        // textures
        texture_id_gray_concrete = loadTexture(R.drawable.grey_concrete128_stroke);
        texture_id_key = loadTexture(R.drawable.key);
        texture_id_fonts = loadTexture(R.drawable.fonts);
        texture_id_fonts_clear = loadTexture(R.drawable.fonts_clear);
        texture_id_level_cubes = loadTexture(R.drawable.level_cube);
        texture_id_fonts_big = loadTexture(R.drawable.fonts_big);
        texture_id_level_cubes_locked = loadTexture(R.drawable.level_cube_locked);
        texture_id_numbers = loadTexture(R.drawable.level_numbers);
        texture_id_player = loadTexture(R.drawable.player);
        texture_id_star = loadTexture(R.drawable.star);
        texture_id_symbols = loadTexture(R.drawable.symbols);
        texture_id_stat_background = loadTexture(R.drawable.stat_background);
        texture_id_credits = loadTexture(R.drawable.credits);
        texture_id_dirty = loadTexture(R.drawable.dirty);
        texture_id_tutor = loadTexture(R.drawable.tutor_swipe);
    }

    private int loadTexture(int resourceId) {
        Texture texture = TextureHelper.loadTexture(context, resourceId);
        textures.add(texture);
        return texture.id;
    }

    private int loadTextureAndJson(int textureResourceId, int jsonResourceId) {
        Texture texture = TextureHelper.loadTexture(context, textureResourceId);
        String jsonText = TextResourceReader.readTextFileFromResource(context, jsonResourceId);
        texture.loadFrames(jsonText);
        textures.add(texture);
        return texture.id;
    }

    @Nullable
    public Texture getTextureObj(int textureId) {
        Texture texture;
        int size = textures.size();
        for (int i = 0; i < size; ++i) {
            texture = textures.get(i);
            if (texture.id == textureId) {
                return texture;
            }
        }
        return null;
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

    public int createTexture(int w, int h) {
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


    public void prepareFrame() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void onSurfaceChanged(int width, int height) {
        //glViewport(0, 0, width, height);

//        screenWidth = width;
//        screenHeight = height;
//        aspectRatio = width > height ? (float)width / (float)height : (float)height / (float)width;
//        scaleFactor = screenWidth / referenceScreenWidth;

//        ParticleShader.pointSize = (float)width * 0.12f;
//
//        if (width > 1080) {
//            fboBackground.create(width / 2, height / 2); // on really big displays avoid creating a full screen texture
//        } else {
//            fboBackground.create(width, height);
//        }
    }


//     inline void DumpVerticesBuffer(int count)
//    {
//		printf("\nDumpVerticesBuffer: %d", count);
//
//        for (int i = 0; i < count*2; i+=2)
//        {
//            printf("\nx:%.2f, y:%.2f", _vertices[i], _vertices[i+1]);
//        }
//    }
//
//     inline void DumpNormalsBuffer(int count)
//    {
//		printf("\nDumpNormalsBuffer: %d", count);
//
//        for (int i = 0; i < count*3; i+=3)
//        {
//            printf("\nx:%.2f, y:%.2f z:%.2f", _normals[i], _normals[i+1], _normals[i+2]);
//        }
//    }
//
//	 inline void DumpCoordsFloatBuffer(int count)
//	{
//		printf("\nDumpCoordsFloatBuffer: %d", count);
//
//        for (int i = 0; i < count*2; i+=2)
//        {
//            printf("\nx:%.2f, y:%.2f", _coords[i], _coords[i+1]);
//        }
//	}
//
//	 inline void DumpCoordsByteBuffer(int count)
//	{
//		printf("\nDumpCoordsByteBuffer: %d", count);
//
//        for (int i = 0; i < count*2; i+=2)
//        {
//            printf("\nx:%d, y:%d", _coords_byte[i], _coords_byte[i+1]);
//        }
//	}
//
//	 inline void DumpColorsBuffer(int count)
//	{
//		printf("\nDumpColorsBuffer: %d", count);
//
//        for (int i = 0; i < count*4; i+=4)
//        {
//            printf("\nr:%d, g:%d, b:%d, a:%d", _colors[i], _colors[i+1], _colors[i+2], _colors[i+3]);
//        }
//	}

    public void setStreamSourceOld() {
//        glVertexPointer(3, GL_FLOAT, 0, _vertices);
//        glNormalPointer(GL_FLOAT, 0, _normals);
//        glTexCoordPointer(2, GL_SHORT, 0, _coords_byte);
    }

    public void setStreamSourceOnlyVerticeAndColor() {
//        glVertexPointer(3, GL_FLOAT, 0, _vertices);
//        glColorPointer(4, GL_UNSIGNED_BYTE, 0, _colors);

        glDisableClientState(GL_NORMAL_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
    }

    public void setStreamSourceFloatAndColor() {
        glVertexPointer(3, GL_FLOAT, 0, _vertexBuffer);
        glNormalPointer(GL_FLOAT, 0, _normalBuffer);
        glTexCoordPointer(2, GL_FLOAT, 0, _coordsBuffer);
        glColorPointer(4, GL_UNSIGNED_BYTE, 0, _colorBuffer);
    }

    public void setStreamSourceFloat() {
//        glVertexPointer(3, GL_FLOAT, 0, _vertices);
//        glNormalPointer(GL_FLOAT, 0, _normals);
//        glTexCoordPointer(2, GL_FLOAT, 0, _coords_float);
    }

    public void setStreamSourceFloat2D() {
//        glVertexPointer(2, GL_FLOAT, 0, _vertices);
//        glTexCoordPointer(2, GL_FLOAT, 0, _coords_float);
//        glColorPointer(4, GL_UNSIGNED_BYTE, 0, _colors);

        glDisableClientState(GL_NORMAL_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
    }

    public void setStreamSourceFloat2DNoTexture() {
//        glVertexPointer(2, GL_FLOAT, 0, _vertices);
//        glColorPointer(4, GL_UNSIGNED_BYTE, 0, _colors);

        glDisableClientState(GL_NORMAL_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
    }

    public void renderTriangles() {
        glDrawArrays(GL_TRIANGLES, 0, _vertices_count);
    }

    public void renderTriangles(float tx, float ty, float tz) {
        glPushMatrix();
        glTranslatef(tx, ty, tz);
        glDrawArrays(GL_TRIANGLES, 0, _vertices_count);
        glPopMatrix();
    }

    public void addCubeSize(float tx, float ty, float tz, float size, Color color) {
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
        _vertices[++i] = xp;
        _vertices[++i] = yp;
        _vertices[++i] = zn;
        _vertices[++i] = xp;
        _vertices[++i] = yp;
        _vertices[++i] = zp;
        _vertices[++i] = xp;
        _vertices[++i] = yn;
        _vertices[++i] = zp;

        _vertices[++i] = xp;
        _vertices[++i] = yn;
        _vertices[++i] = zp;
        _vertices[++i] = xp;
        _vertices[++i] = yn;
        _vertices[++i] = zn;
        _vertices[++i] = xp;
        _vertices[++i] = yp;
        _vertices[++i] = zn;

        // x-minus
        _vertices[++i] = xn;
        _vertices[++i] = yp;
        _vertices[++i] = zp;
        _vertices[++i] = xn;
        _vertices[++i] = yp;
        _vertices[++i] = zn;
        _vertices[++i] = xn;
        _vertices[++i] = yn;
        _vertices[++i] = zn;

        _vertices[++i] = xn;
        _vertices[++i] = yn;
        _vertices[++i] = zn;
        _vertices[++i] = xn;
        _vertices[++i] = yn;
        _vertices[++i] = zp;
        _vertices[++i] = xn;
        _vertices[++i] = yp;
        _vertices[++i] = zp;


        // y-plus
        _vertices[++i] = xp;
        _vertices[++i] = yp;
        _vertices[++i] = zp;
        _vertices[++i] = xp;
        _vertices[++i] = yp;
        _vertices[++i] = zn;
        _vertices[++i] = xn;
        _vertices[++i] = yp;
        _vertices[++i] = zn;

        _vertices[++i] = xn;
        _vertices[++i] = yp;
        _vertices[++i] = zn;
        _vertices[++i] = xn;
        _vertices[++i] = yp;
        _vertices[++i] = zp;
        _vertices[++i] = xp;
        _vertices[++i] = yp;
        _vertices[++i] = zp;

        // y-minus
        _vertices[++i] = xp;
        _vertices[++i] = yn;
        _vertices[++i] = zn;
        _vertices[++i] = xp;
        _vertices[++i] = yn;
        _vertices[++i] = zp;
        _vertices[++i] = xn;
        _vertices[++i] = yn;
        _vertices[++i] = zp;

        _vertices[++i] = xn;
        _vertices[++i] = yn;
        _vertices[++i] = zp;
        _vertices[++i] = xn;
        _vertices[++i] = yn;
        _vertices[++i] = zn;
        _vertices[++i] = xp;
        _vertices[++i] = yn;
        _vertices[++i] = zn;


        // z-plus
        _vertices[++i] = xp;
        _vertices[++i] = yp;
        _vertices[++i] = zp;
        _vertices[++i] = xn;
        _vertices[++i] = yp;
        _vertices[++i] = zp;
        _vertices[++i] = xn;
        _vertices[++i] = yn;
        _vertices[++i] = zp;

        _vertices[++i] = xn;
        _vertices[++i] = yn;
        _vertices[++i] = zp;
        _vertices[++i] = xp;
        _vertices[++i] = yn;
        _vertices[++i] = zp;
        _vertices[++i] = xp;
        _vertices[++i] = yp;
        _vertices[++i] = zp;

        // z-minus
        _vertices[++i] = xp;
        _vertices[++i] = yp;
        _vertices[++i] = zn;
        _vertices[++i] = xp;
        _vertices[++i] = yn;
        _vertices[++i] = zn;
        _vertices[++i] = xn;
        _vertices[++i] = yn;
        _vertices[++i] = zn;

        _vertices[++i] = xn;
        _vertices[++i] = yn;
        _vertices[++i] = zn;
        _vertices[++i] = xn;
        _vertices[++i] = yp;
        _vertices[++i] = zn;
        _vertices[++i] = xp;
        _vertices[++i] = yp;
        _vertices[++i] = zn;

//------------------------------------
        int j = _vindex;

        // x-plus
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;

        // x-minus
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;


        // y-plus
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;

        // y-minus
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;


        // z-plus
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;

        // z-minus
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;

        _vindex = i;
//------------------------------------
        int k = _cindex;

        // x-plus
        _coords[++k] = 1f;
        _coords[++k] = 1f;
        _coords[++k] = 0f;
        _coords[++k] = 1f;
        _coords[++k] = 0f;
        _coords[++k] = 0f;

        _coords[++k] = 0f;
        _coords[++k] = 0f;
        _coords[++k] = 1f;
        _coords[++k] = 0f;
        _coords[++k] = 1f;
        _coords[++k] = 1f;

        // x-minus
        _coords[++k] = 1f;
        _coords[++k] = 1f;
        _coords[++k] = 0f;
        _coords[++k] = 1f;
        _coords[++k] = 0f;
        _coords[++k] = 0f;

        _coords[++k] = 0f;
        _coords[++k] = 0f;
        _coords[++k] = 1f;
        _coords[++k] = 0f;
        _coords[++k] = 1f;
        _coords[++k] = 1f;


        // y-plus
        _coords[++k] = 1f;
        _coords[++k] = 0f;
        _coords[++k] = 1f;
        _coords[++k] = 1f;
        _coords[++k] = 0f;
        _coords[++k] = 1f;

        _coords[++k] = 0f;
        _coords[++k] = 1f;
        _coords[++k] = 0f;
        _coords[++k] = 0f;
        _coords[++k] = 1f;
        _coords[++k] = 0f;

        // y-minus
        _coords[++k] = 1f;
        _coords[++k] = 0f; // 0
        _coords[++k] = 1f;
        _coords[++k] = 1f; // 1
        _coords[++k] = 0f;
        _coords[++k] = 1f; // 2

        _coords[++k] = 0f;
        _coords[++k] = 1f; // 2
        _coords[++k] = 0f;
        _coords[++k] = 0f; // 3
        _coords[++k] = 1f;
        _coords[++k] = 0f; // 0


        // z-plus
        _coords[++k] = 1f;
        _coords[++k] = 1f;
        _coords[++k] = 0f;
        _coords[++k] = 1f;
        _coords[++k] = 0f;
        _coords[++k] = 0f;

        _coords[++k] = 0f;
        _coords[++k] = 0f;
        _coords[++k] = 1f;
        _coords[++k] = 0f;
        _coords[++k] = 1f;
        _coords[++k] = 1f;

        // z-minus
        _coords[++k] = 0f;
        _coords[++k] = 1f;
        _coords[++k] = 0f;
        _coords[++k] = 0f;
        _coords[++k] = 1f;
        _coords[++k] = 0f;

        _coords[++k] = 1f;
        _coords[++k] = 0f;
        _coords[++k] = 1f;
        _coords[++k] = 1f;
        _coords[++k] = 0f;
        _coords[++k] = 1f;

        _cindex = k;

//------------------------------------
        int c = _color_index;

        // x-plus
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        // x-minus
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;


        // y-plus
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        // y-minus
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;


        // z-plus
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        // z-minus
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        _color_index = c;

//	DumpVerticesBuffer(36);
//	DumpNormalsBuffer(36);
//	DumpCoordsByteBuffer(36);
//	DumpColorsBuffer(36);

        _vertices_count += 36;
    }

    public void addQuad(float x, float y, float w, float h, Color color) {
        int i = _vindex;

        // vertices
        _vertices[++i] = x;
        _vertices[++i] = y;
        _vertices[++i] = x + w;
        _vertices[++i] = y;
        _vertices[++i] = x + w;
        _vertices[++i] = y + h;

        _vertices[++i] = x + w;
        _vertices[++i] = y + h;
        _vertices[++i] = x;
        _vertices[++i] = y + h;
        _vertices[++i] = x;
        _vertices[++i] = y;

        _vindex = i;

        // colors
        int c = _color_index;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _color_index = c;

        _vertices_count += 6;
    }


    public void addQuad(float size, float tx, float ty, TexCoordsQuad coords) {
        int i = _vindex;

        _vertices[++i] = tx;
        _vertices[++i] = ty;
        _vertices[++i] = tx + size;
        _vertices[++i] = ty;
        _vertices[++i] = tx + size;
        _vertices[++i] = ty + size;

        _vertices[++i] = tx + size;
        _vertices[++i] = ty + size;
        _vertices[++i] = tx;
        _vertices[++i] = ty + size;
        _vertices[++i] = tx;
        _vertices[++i] = ty;

        _vindex = i;

        int k = _cindex;

        // coordinates
        _coords[++k] = coords.tx0.x;
        _coords[++k] = coords.tx0.y; // 0
        _coords[++k] = coords.tx1.x;
        _coords[++k] = coords.tx1.y; // 1
        _coords[++k] = coords.tx2.x;
        _coords[++k] = coords.tx2.y; // 2

        _coords[++k] = coords.tx2.x;
        _coords[++k] = coords.tx2.y; // 2
        _coords[++k] = coords.tx3.x;
        _coords[++k] = coords.tx3.y; // 3
        _coords[++k] = coords.tx0.x;
        _coords[++k] = coords.tx0.y; // 0

        _cindex = k;

        _vertices_count += 6;
    }

    public void addQuad(float size, float tx, float ty, TexCoordsQuad coords, Color color) {
        int i = _vindex;

        // vertices
        _vertices[++i] = tx;
        _vertices[++i] = ty;
        _vertices[++i] = tx + size;
        _vertices[++i] = ty;
        _vertices[++i] = tx + size;
        _vertices[++i] = ty + size;

        _vertices[++i] = tx + size;
        _vertices[++i] = ty + size;
        _vertices[++i] = tx;
        _vertices[++i] = ty + size;
        _vertices[++i] = tx;
        _vertices[++i] = ty;

        _vindex = i;

        int k = _cindex;

        // coordinates
        _coords[++k] = coords.tx0.x;
        _coords[++k] = coords.tx0.y; // 0
        _coords[++k] = coords.tx1.x;
        _coords[++k] = coords.tx1.y; // 1
        _coords[++k] = coords.tx2.x;
        _coords[++k] = coords.tx2.y; // 2

        _coords[++k] = coords.tx2.x;
        _coords[++k] = coords.tx2.y; // 2
        _coords[++k] = coords.tx3.x;
        _coords[++k] = coords.tx3.y; // 3
        _coords[++k] = coords.tx0.x;
        _coords[++k] = coords.tx0.y; // 0

        _cindex = k;

        int c = _color_index;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _color_index = c;

        _vertices_count += 6;
    }

    public void addPoint(float x, float y, float z) {
        int i = _vindex;

        // z-minus
        _vertices[++i] = x;
        _vertices[++i] = y;
        _vertices[++i] = z;

        _vindex = i;

        ++_vertices_count;
    }

    public void addPoint(Vector pos, Color color) {
        int i = _vindex;

        _vertices[++i] = pos.x;
        _vertices[++i] = pos.y;
        _vertices[++i] = pos.z;

        _vindex = i;

        int c = _color_index;

        _colors[++c] = (byte) color.R;
        _colors[++c] = (byte) color.G;
        _colors[++c] = (byte) color.B;
        _colors[++c] = (byte) color.A;

        _color_index = c;

        ++_vertices_count;
    }

    public void addPoint2D(float x, float y) {
        int i = _vindex;

        // z-minus
        _vertices[++i] = x;
        _vertices[++i] = y;

        _vindex = i;

        ++_vertices_count;
    }

    public void addCubeFace_X_Plus(float tx, float ty, float tz) {
        int i = _vindex;
        int j = _vindex;

// vertices
        // x                                   y                                      z
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 12
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 1 - 13
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 2 - 14

        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 2 - 15
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 3 - 16
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 17

        _vindex = i;

// normals
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;

        int k = _cindex;

// coordinates
        _coords[++k] = 1f;
        _coords[++k] = 1f; // 0
        _coords[++k] = 0f;
        _coords[++k] = 1f; // 1
        _coords[++k] = 0f;
        _coords[++k] = 0f; // 2

        _coords[++k] = 0f;
        _coords[++k] = 0f; // 2
        _coords[++k] = 1f;
        _coords[++k] = 0f; // 3
        _coords[++k] = 1f;
        _coords[++k] = 1f; // 0

        _cindex = k;

        _vertices_count += 6;
    }

    public void addCubeFace_X_Minus(float tx, float ty, float tz) {
        int i = _vindex;
        int j = _vindex;

// vertices
        // x                                   y                                      z
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 24
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 1 - 25
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 2 - 26

        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 2 - 27
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 3 - 28
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 29

        _vindex = i;

// normals
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;

        int k = _cindex;

// coordinates
        _coords[++k] = 1f;
        _coords[++k] = 1f; // 0
        _coords[++k] = 0f;
        _coords[++k] = 1f; // 1
        _coords[++k] = 0f;
        _coords[++k] = 0f; // 2

        _coords[++k] = 0f;
        _coords[++k] = 0f; // 2
        _coords[++k] = 1f;
        _coords[++k] = 0f; // 3
        _coords[++k] = 1f;
        _coords[++k] = 1f; // 0

        _cindex = k;

        _vertices_count += 6;
    }

    public void addCubeFace_Y_Plus(float tx, float ty, float tz) {
        int i = _vindex;
        int j = _vindex;

// vertices
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 0 - 30
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 1 - 31
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 2 - 32

        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz;  // 2 - 33
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz;  // 3 - 34
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz;  // 0 - 35

        _vindex = i;

// normals
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;

        int k = _cindex;

// coordinates
//    _coords_byte[++k] = 1; _coords_byte[++k] = 0; // 0
//    _coords_byte[++k] = 0; _coords_byte[++k] = 0; // 1
//    _coords_byte[++k] = 0; _coords_byte[++k] = 1; // 2
//
//	_coords_byte[++k] = 0; _coords_byte[++k] = 1; // 2
//	_coords_byte[++k] = 1; _coords_byte[++k] = 1; // 3
//    _coords_byte[++k] = 1; _coords_byte[++k] = 0; // 0

        _coords[++k] = 1f;
        _coords[++k] = 0f;
        _coords[++k] = 1f;
        _coords[++k] = 1f;
        _coords[++k] = 0f;
        _coords[++k] = 1f;

        _coords[++k] = 0f;
        _coords[++k] = 1f;
        _coords[++k] = 0f;
        _coords[++k] = 0f;
        _coords[++k] = 1f;
        _coords[++k] = 0f;


        _cindex = k;

        _vertices_count += 6;
    }

    public void addCubeFace_Y_Minus(float tx, float ty, float tz) {
        int i = _vindex;
        int j = _vindex;

// vertices
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 18
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 1 - 19
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 2 - 20

        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 2 - 21
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 3 - 22
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 23

        _vindex = i;

// normals
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;

        int k = _cindex;

// coordinates
        _coords[++k] = 1f;
        _coords[++k] = 1f; // 0
        _coords[++k] = 0f;
        _coords[++k] = 1f; // 1
        _coords[++k] = 0f;
        _coords[++k] = 0f; // 2

        _coords[++k] = 0f;
        _coords[++k] = 0f; // 2
        _coords[++k] = 1f;
        _coords[++k] = 0f; // 3
        _coords[++k] = 1f;
        _coords[++k] = 1f; // 0

        _cindex = k;

        _vertices_count += 6;
    }

    public void addCubeFace_Z_Plus(float tx, float ty, float tz) {
        int i = _vindex;
        int j = _vindex;

// vertices
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 0 - 6
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 1 - 7
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 2 - 8

        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 2 - 9
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 3 - 10
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 0 - 11

        _vindex = i;

// normals
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;

        int k = _cindex;

// coordinates
        _coords[++k] = 1f;
        _coords[++k] = 1f; // 0
        _coords[++k] = 0f;
        _coords[++k] = 1f; // 1
        _coords[++k] = 0f;
        _coords[++k] = 0f; // 2

        _coords[++k] = 0f;
        _coords[++k] = 0f; // 2
        _coords[++k] = 1f;
        _coords[++k] = 0f; // 3
        _coords[++k] = 1f;
        _coords[++k] = 1f; // 0

        _cindex = k;

        _vertices_count += 6;
    }

    public void addCubeFace_Z_Minus(float tx, float ty, float tz) {
        int i = _vindex;
        int j = _vindex;

// vertices
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 0
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 1 - 1
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 2 - 2

        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 2 - 3
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 3 - 4
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 5

        _vindex = i;

// normals
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;

        int k = _cindex;

// coordinates
        _coords[++k] = 1f;
        _coords[++k] = 0f; // 0
        _coords[++k] = 1f;
        _coords[++k] = 1f; // 1
        _coords[++k] = 0f;
        _coords[++k] = 1f; // 2

        _coords[++k] = 0f;
        _coords[++k] = 1f; // 2
        _coords[++k] = 0f;
        _coords[++k] = 0f; // 3
        _coords[++k] = 1f;
        _coords[++k] = 0f; // 0

        _cindex = k;

        _vertices_count += 6;
    }

    public void addCubeFace_X_Plus(float tx, float ty, float tz, TexCoordsQuad coords) {
        int i = _vindex;
        int j = _vindex;

        // vertices
        // x-plus
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 12
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 1 - 13
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 2 - 14

        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 2 - 15
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 3 - 16
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 17

        _vindex = i;

        // normals
        // left
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;

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

        _coords[++k] = coords.tx0.x;
        _coords[++k] = coords.tx0.y; // 0
        _coords[++k] = coords.tx1.x;
        _coords[++k] = coords.tx1.y; // 1
        _coords[++k] = coords.tx2.x;
        _coords[++k] = coords.tx2.y; // 2

        _coords[++k] = coords.tx2.x;
        _coords[++k] = coords.tx2.y; // 2
        _coords[++k] = coords.tx3.x;
        _coords[++k] = coords.tx3.y; // 3
        _coords[++k] = coords.tx0.x;
        _coords[++k] = coords.tx0.y; // 0

        _cindex = k;

        _vertices_count += 6;
    }

    public void addCubeFace_X_Minus(float tx, float ty, float tz, TexCoordsQuad coords) {
        int i = _vindex;
        int j = _vindex;

        // vertices
        // x-minus
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 24
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 1 - 25
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 2 - 26

        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz;  // 2 - 27
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz;  // 3 - 28
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz;  // 0 - 29

        _vindex = i;

        // normals
        // right
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;

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

        _coords[++k] = coords.tx0.x;
        _coords[++k] = coords.tx0.y; // 0
        _coords[++k] = coords.tx1.x;
        _coords[++k] = coords.tx1.y; // 1
        _coords[++k] = coords.tx2.x;
        _coords[++k] = coords.tx2.y; // 2

        _coords[++k] = coords.tx2.x;
        _coords[++k] = coords.tx2.y; // 2
        _coords[++k] = coords.tx3.x;
        _coords[++k] = coords.tx3.y; // 3
        _coords[++k] = coords.tx0.x;
        _coords[++k] = coords.tx0.y; // 0

        _cindex = k;

        _vertices_count += 6;
    }

    public void addCubeFace_Y_Plus(float tx, float ty, float tz, TexCoordsQuad coords) {
        int i = _vindex;
        int j = _vindex;

        // vertices
        // y-plus
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 0 - 30
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 1 - 31
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 2 - 32

        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz;  // 2 - 33
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz;  // 3 - 34
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz;  // 0 - 35

        _vindex = i;

        // normals
        // top
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;

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

        _coords[++k] = coords.tx0.x;
        _coords[++k] = coords.tx0.y; // 0
        _coords[++k] = coords.tx1.x;
        _coords[++k] = coords.tx1.y; // 1
        _coords[++k] = coords.tx2.x;
        _coords[++k] = coords.tx2.y; // 2

        _coords[++k] = coords.tx2.x;
        _coords[++k] = coords.tx2.y; // 2
        _coords[++k] = coords.tx3.x;
        _coords[++k] = coords.tx3.y; // 3
        _coords[++k] = coords.tx0.x;
        _coords[++k] = coords.tx0.y; // 0

        _cindex = k;

        _vertices_count += 6;
    }

    public void addCubeFace_Y_Minus(float tx, float ty, float tz, TexCoordsQuad coords) {
        int i = _vindex;
        int j = _vindex;

        // vertices
        // y-minus
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 18
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 1 - 19
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 2 - 20

        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 2 - 21
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 3 - 22
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 23

        _vindex = i;

        // normals
        // bottom
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;

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

        _coords[++k] = coords.tx0.x;
        _coords[++k] = coords.tx0.y; // 0
        _coords[++k] = coords.tx1.x;
        _coords[++k] = coords.tx1.y; // 1
        _coords[++k] = coords.tx2.x;
        _coords[++k] = coords.tx2.y; // 2

        _coords[++k] = coords.tx2.x;
        _coords[++k] = coords.tx2.y; // 2
        _coords[++k] = coords.tx3.x;
        _coords[++k] = coords.tx3.y; // 3
        _coords[++k] = coords.tx0.x;
        _coords[++k] = coords.tx0.y; // 0

        _cindex = k;

        _vertices_count += 6;
    }

    public void addCubeFace_Z_Plus(float tx, float ty, float tz, TexCoordsQuad coords) {
        int i = _vindex;
        int j = _vindex;

        // vertices
        // z-plus
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 0 - 6
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 1 - 7
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 2 - 8

        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 2 - 9
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 3 - 10
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 0 - 11

        _vindex = i;

        // normals
        // front
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;

        int k = _cindex;

        // coordinates
        // front
//    _coords[++k] = 1; _coords[++k] = 1; // 0
//    _coords[++k] = 0; _coords[++k] = 1; // 1
//    _coords[++k] = 0; _coords[++k] = 0; // 2
//
//	_coords[++k] = 0; _coords[++k] = 0; // 2
//    _coords[++k] = 1; _coords[++k] = 0; // 3
//    _coords[++k] = 1; _coords[++k] = 1; // 0


        _coords[++k] = coords.tx0.x;
        _coords[++k] = coords.tx0.y; // 0
        _coords[++k] = coords.tx1.x;
        _coords[++k] = coords.tx1.y; // 1
        _coords[++k] = coords.tx2.x;
        _coords[++k] = coords.tx2.y; // 2

        _coords[++k] = coords.tx2.x;
        _coords[++k] = coords.tx2.y; // 2
        _coords[++k] = coords.tx3.x;
        _coords[++k] = coords.tx3.y; // 3
        _coords[++k] = coords.tx0.x;
        _coords[++k] = coords.tx0.y; // 0

        _cindex = k;

        _vertices_count += 6;
    }

    public void addCubeFace_Z_Minus(float tx, float ty, float tz, TexCoordsQuad coords) {
        int i = _vindex;
        int j = _vindex;

        // z-minus
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 0
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 1 - 1
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 2 - 2

        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 2 - 3
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 3 - 4
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 5

        _vindex = i;

        // back
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;

        int k = _cindex;

        // back
//	_coords[++k] = 1; _coords[++k] = 0; // 0
//	_coords[++k] = 1; _coords[++k] = 1; // 1
//	_coords[++k] = 0; _coords[++k] = 1; // 2
//
//	_coords[++k] = 0; _coords[++k] = 1; // 2
//	_coords[++k] = 0; _coords[++k] = 0; // 3
//	_coords[++k] = 1; _coords[++k] = 0; // 0

        _coords[++k] = coords.tx0.x;
        _coords[++k] = coords.tx0.y; // 0
        _coords[++k] = coords.tx1.x;
        _coords[++k] = coords.tx1.y; // 1
        _coords[++k] = coords.tx2.x;
        _coords[++k] = coords.tx2.y; // 2

        _coords[++k] = coords.tx2.x;
        _coords[++k] = coords.tx2.y; // 2
        _coords[++k] = coords.tx3.x;
        _coords[++k] = coords.tx3.y; // 3
        _coords[++k] = coords.tx0.x;
        _coords[++k] = coords.tx0.y; // 0

        _cindex = k;

        _vertices_count += 6;
    }

    public void addCubeFace_X_Plus(float tx, float ty, float tz, TexCoordsQuad coords, Color color) {
        int i = _vindex;
        int j = _vindex;

        // vertices
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 12
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 1 - 13
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 2 - 14

        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 2 - 15
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 3 - 16
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 17

        _vindex = i;

        // normals
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;

        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;

        // textures
        int k = _cindex;
        _coords[++k] = coords.tx0.x;
        _coords[++k] = coords.tx0.y; // 0
        _coords[++k] = coords.tx1.x;
        _coords[++k] = coords.tx1.y; // 1
        _coords[++k] = coords.tx2.x;
        _coords[++k] = coords.tx2.y; // 2

        _coords[++k] = coords.tx2.x;
        _coords[++k] = coords.tx2.y; // 2
        _coords[++k] = coords.tx3.x;
        _coords[++k] = coords.tx3.y; // 3
        _coords[++k] = coords.tx0.x;
        _coords[++k] = coords.tx0.y; // 0

        _cindex = k;

        // colors
        int c = _color_index;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        _color_index = c;

        _vertices_count += 6;
    }

    public void addCubeFace_X_Minus(float tx, float ty, float tz, TexCoordsQuad coords, Color color) {
        int i = _vindex;
        int j = _vindex;

        // vertices
        // x-minus
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 24
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 1 - 25
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 2 - 26

        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz;  // 2 - 27
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz;  // 3 - 28
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz;  // 0 - 29

        _vindex = i;

        // normals
        // right
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;

        int k = _cindex;

        _coords[++k] = coords.tx0.x;
        _coords[++k] = coords.tx0.y; // 0
        _coords[++k] = coords.tx1.x;
        _coords[++k] = coords.tx1.y; // 1
        _coords[++k] = coords.tx2.x;
        _coords[++k] = coords.tx2.y; // 2

        _coords[++k] = coords.tx2.x;
        _coords[++k] = coords.tx2.y; // 2
        _coords[++k] = coords.tx3.x;
        _coords[++k] = coords.tx3.y; // 3
        _coords[++k] = coords.tx0.x;
        _coords[++k] = coords.tx0.y; // 0

        _cindex = k;

        // colors
        int c = _color_index;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        _color_index = c;

        _vertices_count += 6;
    }

    public void addCubeFace_Y_Plus(float tx, float ty, float tz, TexCoordsQuad coords, Color color) {
        int i = _vindex;
        int j = _vindex;

        // vertices
        // y-plus
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 0 - 30
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 1 - 31
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 2 - 32

        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz;  // 2 - 33
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz;  // 3 - 34
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz;  // 0 - 35

        _vindex = i;

        // normals
        // top
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;

        int k = _cindex;

        // coordinates
        _coords[++k] = coords.tx0.x;
        _coords[++k] = coords.tx0.y; // 0
        _coords[++k] = coords.tx1.x;
        _coords[++k] = coords.tx1.y; // 1
        _coords[++k] = coords.tx2.x;
        _coords[++k] = coords.tx2.y; // 2

        _coords[++k] = coords.tx2.x;
        _coords[++k] = coords.tx2.y; // 2
        _coords[++k] = coords.tx3.x;
        _coords[++k] = coords.tx3.y; // 3
        _coords[++k] = coords.tx0.x;
        _coords[++k] = coords.tx0.y; // 0

        _cindex = k;

        // colors
        int c = _color_index;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        _color_index = c;

        _vertices_count += 6;
    }

    public void addCubeFace_Y_Minus(float tx, float ty, float tz, TexCoordsQuad coords, Color color) {
        int i = _vindex;
        int j = _vindex;

        // vertices
        // y-minus
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 18
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 1 - 19
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 2 - 20

        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 2 - 21
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 3 - 22
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 23

        _vindex = i;

        // normals
        // bottom
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;

        int k = _cindex;

        _coords[++k] = coords.tx0.x;
        _coords[++k] = coords.tx0.y; // 0
        _coords[++k] = coords.tx1.x;
        _coords[++k] = coords.tx1.y; // 1
        _coords[++k] = coords.tx2.x;
        _coords[++k] = coords.tx2.y; // 2

        _coords[++k] = coords.tx2.x;
        _coords[++k] = coords.tx2.y; // 2
        _coords[++k] = coords.tx3.x;
        _coords[++k] = coords.tx3.y; // 3
        _coords[++k] = coords.tx0.x;
        _coords[++k] = coords.tx0.y; // 0

        _cindex = k;

        // colors
        int c = _color_index;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        _color_index = c;

        _vertices_count += 6;
    }

    public void addCubeFace_Z_Plus(float tx, float ty, float tz, TexCoordsQuad coords, Color color) {
        int i = _vindex;
        int j = _vindex;

        // vertices
        // z-plus
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 0 - 6
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 1 - 7
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 2 - 8

        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 2 - 9
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 3 - 10
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 0 - 11

        _vindex = i;

        // normals
        // front
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;

        int k = _cindex;

        _coords[++k] = coords.tx0.x;
        _coords[++k] = coords.tx0.y; // 0
        _coords[++k] = coords.tx1.x;
        _coords[++k] = coords.tx1.y; // 1
        _coords[++k] = coords.tx2.x;
        _coords[++k] = coords.tx2.y; // 2

        _coords[++k] = coords.tx2.x;
        _coords[++k] = coords.tx2.y; // 2
        _coords[++k] = coords.tx3.x;
        _coords[++k] = coords.tx3.y; // 3
        _coords[++k] = coords.tx0.x;
        _coords[++k] = coords.tx0.y; // 0

        _cindex = k;

        // colors
        int c = _color_index;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        _color_index = c;

        _vertices_count += 6;
    }

    public void addCubeFace_Z_Minus(float tx, float ty, float tz, TexCoordsQuad coords, Color color) {
        int i = _vindex;
        int j = _vindex;

        // z-minus
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 0
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 1 - 1
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 2 - 2

        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 2 - 3
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 3 - 4
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 5

        _vindex = i;

        // back
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;

        int k = _cindex;

        _coords[++k] = coords.tx0.x;
        _coords[++k] = coords.tx0.y; // 0
        _coords[++k] = coords.tx1.x;
        _coords[++k] = coords.tx1.y; // 1
        _coords[++k] = coords.tx2.x;
        _coords[++k] = coords.tx2.y; // 2

        _coords[++k] = coords.tx2.x;
        _coords[++k] = coords.tx2.y; // 2
        _coords[++k] = coords.tx3.x;
        _coords[++k] = coords.tx3.y; // 3
        _coords[++k] = coords.tx0.x;
        _coords[++k] = coords.tx0.y; // 0

        _cindex = k;

        // colors
        int c = _color_index;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        _color_index = c;

        _vertices_count += 6;
    }

    public void addCubeFace_X_Plus(float tx, float ty, float tz, Color color) {
        int i = _vindex;
        int j = _vindex;

        // vertices
        // x                                   y                                      z
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 12
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 1 - 13
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 2 - 14

        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 2 - 15
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 3 - 16
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 17

        _vindex = i;

        // normals
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;

        int k = _cindex;

        // coordinates
        _coords[++k] = 1f;
        _coords[++k] = 1f; // 0
        _coords[++k] = 0f;
        _coords[++k] = 1f; // 1
        _coords[++k] = 0f;
        _coords[++k] = 0f; // 2

        _coords[++k] = 0f;
        _coords[++k] = 0f; // 2
        _coords[++k] = 1f;
        _coords[++k] = 0f; // 3
        _coords[++k] = 1f;
        _coords[++k] = 1f; // 0

        _cindex = k;

        // colors
        int c = _color_index;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        _color_index = c;

        _vertices_count += 6;
    }

    public void addCubeFace_X_Minus(float tx, float ty, float tz, Color color) {
        int i = _vindex;
        int j = _vindex;

        // vertices
        // x                                   y                                      z
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 24
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 1 - 25
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 2 - 26

        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 2 - 27
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 3 - 28
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 29

        _vindex = i;

        // normals
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;

        int k = _cindex;

        // coordinates
        _coords[++k] = 1f;
        _coords[++k] = 1f; // 0
        _coords[++k] = 0f;
        _coords[++k] = 1f; // 1
        _coords[++k] = 0f;
        _coords[++k] = 0f; // 2

        _coords[++k] = 0f;
        _coords[++k] = 0f; // 2
        _coords[++k] = 1f;
        _coords[++k] = 0f; // 3
        _coords[++k] = 1f;
        _coords[++k] = 1f; // 0

        _cindex = k;

        // colors
        int c = _color_index;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        _color_index = c;

        _vertices_count += 6;
    }

    public void addCubeFace_Y_Plus(float tx, float ty, float tz, Color color) {
        int i = _vindex;
        int j = _vindex;

        // vertices
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 0 - 30
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 1 - 31
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 2 - 32

        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz;  // 2 - 33
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz;  // 3 - 34
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz;  // 0 - 35

        _vindex = i;

        // normals
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;

        int k = _cindex;

        // coordinates
        //    _coords_byte[++k] = 1; _coords_byte[++k] = 0; // 0
        //    _coords_byte[++k] = 0; _coords_byte[++k] = 0; // 1
        //    _coords_byte[++k] = 0; _coords_byte[++k] = 1; // 2
        //
        //	_coords_byte[++k] = 0; _coords_byte[++k] = 1; // 2
        //	_coords_byte[++k] = 1; _coords_byte[++k] = 1; // 3
        //    _coords_byte[++k] = 1; _coords_byte[++k] = 0; // 0

        _coords[++k] = 1f;
        _coords[++k] = 0f;
        _coords[++k] = 1f;
        _coords[++k] = 1f;
        _coords[++k] = 0f;
        _coords[++k] = 1f;

        _coords[++k] = 0f;
        _coords[++k] = 1f;
        _coords[++k] = 0f;
        _coords[++k] = 0f;
        _coords[++k] = 1f;
        _coords[++k] = 0f;

        _cindex = k;

        // colors
        int c = _color_index;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        _color_index = c;

        _vertices_count += 6;
    }

    public void addCubeFace_Y_Minus(float tx, float ty, float tz, Color color) {
        int i = _vindex;
        int j = _vindex;

        // vertices
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 18
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 1 - 19
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 2 - 20

        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 2 - 21
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 3 - 22
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 23

        _vindex = i;

        // normals
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;

        int k = _cindex;

        // coordinates
        _coords[++k] = 1f;
        _coords[++k] = 1f; // 0
        _coords[++k] = 0f;
        _coords[++k] = 1f; // 1
        _coords[++k] = 0f;
        _coords[++k] = 0f; // 2

        _coords[++k] = 0f;
        _coords[++k] = 0f; // 2
        _coords[++k] = 1f;
        _coords[++k] = 0f; // 3
        _coords[++k] = 1f;
        _coords[++k] = 1f; // 0

        _cindex = k;

        // colors
        int c = _color_index;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        _color_index = c;

        _vertices_count += 6;
    }

    public void addCubeFace_Z_Plus(float tx, float ty, float tz, Color color) {
        int i = _vindex;
        int j = _vindex;

        // vertices
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 0 - 6
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 1 - 7
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 2 - 8

        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 2 - 9
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 3 - 10
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = HALF_CUBE_SIZE + tz; // 0 - 11

        _vindex = i;

        // normals
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 1.0f;

        int k = _cindex;

        // coordinates
        _coords[++k] = 1f;
        _coords[++k] = 1f; // 0
        _coords[++k] = 0f;
        _coords[++k] = 1f; // 1
        _coords[++k] = 0f;
        _coords[++k] = 0f; // 2

        _coords[++k] = 0f;
        _coords[++k] = 0f; // 2
        _coords[++k] = 1f;
        _coords[++k] = 0f; // 3
        _coords[++k] = 1f;
        _coords[++k] = 1f; // 0

        _cindex = k;

        // colors
        int c = _color_index;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        _color_index = c;

        _vertices_count += 6;
    }

    public void addCubeFace_Z_Minus(float tx, float ty, float tz, Color color) {
        int i = _vindex;
        int j = _vindex;

        // vertices
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 0
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 1 - 1
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 2 - 2

        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = -HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 2 - 3
        _vertices[++i] = -HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 3 - 4
        _vertices[++i] = HALF_CUBE_SIZE + tx;
        _vertices[++i] = HALF_CUBE_SIZE + ty;
        _vertices[++i] = -HALF_CUBE_SIZE + tz; // 0 - 5

        _vindex = i;

        // normals
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = 0.0f;
        _normals[++j] = -1.0f;

        int k = _cindex;

        // coordinates
        _coords[++k] = 1f;
        _coords[++k] = 0f; // 0
        _coords[++k] = 1f;
        _coords[++k] = 1f; // 1
        _coords[++k] = 0f;
        _coords[++k] = 1f; // 2

        _coords[++k] = 0f;
        _coords[++k] = 1f; // 2
        _coords[++k] = 0f;
        _coords[++k] = 0f; // 3
        _coords[++k] = 1f;
        _coords[++k] = 0f; // 0

        _cindex = k;

        // colors
        int c = _color_index;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;
        _colors[++c] = color.R;
        _colors[++c] = color.G;
        _colors[++c] = color.B;
        _colors[++c] = color.A;

        _color_index = c;

        _vertices_count += 6;
    }

}

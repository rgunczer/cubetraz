package com.almagems.cubetraz.graphics;

import static android.opengl.GLES10.*;
import static android.opengl.GLES11Ext.*;
import static android.opengl.GLU.*;

import javax.microedition.khronos.opengles.GL10;

import static com.almagems.cubetraz.game.Constants.*;

import com.almagems.cubetraz.utils.PositionInfo;
import com.almagems.cubetraz.R;
import com.almagems.cubetraz.system.TextResourceReader;
import com.almagems.cubetraz.math.Vector;
import com.almagems.cubetraz.math.Vector2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


public final class Graphics {

    private GL10 gl;

    public int width;
    public int height;
    public int halfWidth;
    public int halfHeight;
    public float deviceScale;

    private int[] originalFBO = new int[1];

    // textures
    public Texture textureGrayConcrete;
    public Texture textureKey;
    public Texture textureFonts;
    public Texture textureFontsClear;
    public Texture textureLevelCubes;
    public Texture textureFontsBig;
    public Texture textureNumbers;
    public Texture texturePlayer;
    public Texture textureStar;
    public Texture textureSymbols;
    public Texture textureStatBackground;
    public Texture textureCredits;
    public Texture textureDirty;
    public Texture textureTutor;

    public int _vertices_count = 0;
    public int _vindex = -1;
    public int _cindex = -1;
    public int _color_index = -1;

    private float[] _vertices = new float[BUF_SIZE * KILOBYTE];  /*3 * 36 * MAX_CUBE_COUNT * MAX_CUBE_COUNT * MAX_CUBE_COUNT */
    private float[] _normals = new float[BUF_SIZE * KILOBYTE];
    private float[] _coords = new float[BUF_SIZE * KILOBYTE];
    private byte[] _colors = new byte[BUF_SIZE * KILOBYTE];

    public float screenWidth;
    public float screenHeight;
    public float aspectRatio;
    public final float referenceScreenWidth = 1080f;
    public float scaleFactor;

    private FloatBuffer _vertexBuffer;
    private FloatBuffer _normalBuffer;
    private FloatBuffer _coordsBuffer;
    private ByteBuffer _colorBuffer;

    public FBO fbo;

    public Graphics(GL10 gl) {
        this.gl = gl;
    }

    public void initialSetup() {


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

    public void saveOriginalFBO() {
        glGetIntegerv(GL_FRAMEBUFFER_BINDING_OES, originalFBO, 0);
    }

    public void restoreOriginalFBO() {
        glBindFramebufferOES(GL_FRAMEBUFFER_OES, originalFBO[0]);
    }

    public void updateBuffers() {
        _vertexBuffer.put(_vertices);
        _vertexBuffer.position(0);

        _normalBuffer.put(_normals);
        _normalBuffer.position(0);

        _coordsBuffer.put(_coords);
        _coordsBuffer.position(0);

        _colorBuffer.put(_colors);
        _colorBuffer.position(0);
    }

    public void zeroBufferPositions() {
        _vertexBuffer.position(0);
        _colorBuffer.position(0);
        _coordsBuffer.position(0);
        _normalBuffer.position(0);
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

    public void setLightPosition(Vector pos) {
        float posLight[] = { pos.x, pos.y, pos.z, 1.0f };
        glLightfv(GL_LIGHT0, GL_POSITION, posLight, 0);
    }

//    public void warmCache() {
//        glEnable(GL_TEXTURE_2D);
//
//        resetBufferIndices();
//        bindStreamSources3d();
//        addCube(0.0f, 0.0f, 0.0f);
//        texturePlayer.bind();
//        glDrawArrays(GL_TRIANGLES, 0, 36);
//
//        textureGrayConcrete.bind();
//        glDrawArrays(GL_TRIANGLES, 0, 36);
//
//        glDisable(GL_TEXTURE_2D);
//    }

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

    public Color getColorFromScreen(Vector2 pos) {
        int ix = (int) pos.x; //(int)Engine.rawX; //(int)pos.x * width/2;
        int iy = height - (int) pos.y; //(int)Engine.rawY; //(int)pos.y * height/2;

        ByteBuffer pixelBuf = ByteBuffer.allocateDirect(4);
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

        resetBufferIndices();

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

    public void drawFullScreenTexture(FBO fbo, Color color) {
        drawFullScreenTexture(fbo.textureId, color);
    }

    public void drawFullScreenTexture(Texture texture, Color color) {
        drawFullScreenTexture(texture.id, color);
    }

    private void drawFullScreenTexture(int textureId, Color color) {
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

        final byte colors[] = {
                (byte)color.r, (byte)color.g, (byte)color.b, (byte)color.a,
                (byte)color.r, (byte)color.g, (byte)color.b, (byte)color.a,
                (byte)color.r, (byte)color.g, (byte)color.b, (byte)color.a,
                (byte)color.r, (byte)color.g, (byte)color.b, (byte)color.a
        };

        final float norms[] = {};

        addVerticesCoordsNormalsColors(verts, coords, norms, colors);

        glBindTexture(GL_TEXTURE_2D, textureId);
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

    public void renderPoints() {
        glDrawArrays(GL_POINTS, 0, _vertices_count);
    }

    public void addCube(float tx, float ty, float tz) {
        addCubeSize(tx, ty, tz, HALF_CUBE_SIZE, Color.WHITE);
    }

    public void resetBufferIndices() {
        _vertices_count = 0;
        _vindex = -1;
        _cindex = -1;
        _color_index = -1;
        zeroBufferPositions();
    }

    public void bindStreamSources2dNoTextures() {
        glEnableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);

        glVertexPointer(2, GL_FLOAT, 0, _vertexBuffer);
        glColorPointer(4, GL_UNSIGNED_BYTE, 0, _colorBuffer);
    }

    public void bindStreamSources2d() {
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);

        glVertexPointer(2, GL_FLOAT, 0, _vertexBuffer);
        glTexCoordPointer(2, GL_FLOAT, 0, _coordsBuffer);
        glColorPointer(4, GL_UNSIGNED_BYTE, 0, _colorBuffer);
    }

    public void bindStreamSources3d() {
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glEnableClientState(GL_NORMAL_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        glVertexPointer(3, GL_FLOAT, 0, _vertexBuffer);
        glNormalPointer(GL_FLOAT, 0, _normalBuffer);
        glTexCoordPointer(2, GL_FLOAT, 0, _coordsBuffer);
        glColorPointer(4, GL_UNSIGNED_BYTE, 0, _colorBuffer);
    }

    public void bindStreamSources3dNoTexture() {
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glEnableClientState(GL_NORMAL_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);

        glVertexPointer(3, GL_FLOAT, 0, _vertexBuffer);
        glNormalPointer(GL_FLOAT, 0, _normalBuffer);
        glColorPointer(4, GL_UNSIGNED_BYTE, 0, _colorBuffer);
    }


    public void loadStartupAssets() throws Exception {
        //System.out.println("Load startup Assets...");

        // textures
        textureGrayConcrete = loadTexture(R.drawable.grey_concrete128_stroke);
        textureKey = loadTexture(R.drawable.key);
        textureFonts = loadTexture(R.drawable.fonts);
        textureFontsClear = loadTexture(R.drawable.fonts_clear);
        textureLevelCubes = loadTexture(R.drawable.level_cube);
        textureFontsBig = loadTexture(R.drawable.fonts_big);
        textureNumbers = loadTexture(R.drawable.level_numbers);
        texturePlayer = loadTexture(R.drawable.player);
        textureStar = loadTexture(R.drawable.star);
        textureSymbols = loadTexture(R.drawable.symbols);
        textureStatBackground = loadTexture(R.drawable.stat_background);
        textureCredits = loadTexture(R.drawable.credits);
        textureDirty = loadTexture(R.drawable.dirty);
        textureTutor = loadTexture(R.drawable.tutor_swipe);
    }

    public Texture loadTexture(int resourceId) {
        Texture texture = TextureHelper.loadTexture(resourceId);
        return texture;
    }

    private int loadTextureAndJson(int textureResourceId, int jsonResourceId) {
        Texture texture = TextureHelper.loadTexture(textureResourceId);
        String jsonText = TextResourceReader.readTextFileFromResource(jsonResourceId);
        texture.loadFrames(jsonText);
        return texture.id;
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

        glBindTexture(GL_TEXTURE_2D, temp[0]);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

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
        System.out.println("Graphics.onSurfaceChanged");

        this.width = width;
        this.height = height;

        halfWidth = width / 2;
        halfHeight = height / 2;

        aspectRatio = (float) width / (float) height;

        glViewport(0, 0, width, height);

        deviceScale = 4.5f;

        if (height < 1201) {
            deviceScale = 3.5f;
        }

        if (height < 900) {
            deviceScale = 2.5f;
        }

        if (height < 500) {
            deviceScale = 1.25f;
        }

        if (fbo == null) {
            saveOriginalFBO();

            fbo = new FBO(width, height);
            fbo.createWithColorAndDepthStencilBuffer();

            restoreOriginalFBO();
        }
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
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

        // x-minus
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;


        // y-plus
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

        // y-minus
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;


        // z-plus
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

        // z-minus
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

        _color_index = c;

//	DumpVerticesBuffer(36);
//	DumpNormalsBuffer(36);
//	DumpCoordsByteBuffer(36);
//	DumpColorsBuffer(36);

        _vertices_count += 36;
    }

    public void addVerticesCoordsColors(float[] verts, float[] coords, byte[] colors) {
        _vertexBuffer.position(0);
        _coordsBuffer.position(0);
        _colorBuffer.position(0);

        _vertexBuffer.put(verts);
        _coordsBuffer.put(coords);
        _colorBuffer.put(colors);
    }

    public void addVerticesCoordsNormalsColors(float[] verts, float[] coords, float[] norms, byte[] colors) {
        _vertexBuffer.put(verts);
        _coordsBuffer.put(coords);
        _normalBuffer.put(norms);
        _colorBuffer.put(colors);

        _vertexBuffer.position(0);
        _normalBuffer.position(0);
        _coordsBuffer.position(0);
        _colorBuffer.position(0);
    }

    public void addFont(Vector2 pos, Vector2 scale, Color color, TexturedQuad pFont) {
        // verts
        int i = _vindex;
        _vertices[++i] = pos.x;						_vertices[++i] = pos.y;
        _vertices[++i] = pos.x + pFont.w * scale.x;	_vertices[++i] = pos.y;
        _vertices[++i] = pos.x + pFont.w * scale.x;	_vertices[++i] = pos.y + pFont.h * scale.y;

        _vertices[++i] = pos.x + pFont.w * scale.x;	_vertices[++i] = pos.y + pFont.h * scale.y;
        _vertices[++i] = pos.x;						_vertices[++i] = pos.y + pFont.h * scale.y;
        _vertices[++i] = pos.x;						_vertices[++i] = pos.y;
        _vindex = i;

        // coords
        int k = _cindex;
        _coords[++k] = pFont.tx_lo_left.x;		_coords[++k] = pFont.tx_lo_left.y;		// 0
        _coords[++k] = pFont.tx_lo_right.x;	    _coords[++k] = pFont.tx_lo_right.y;	    // 1
        _coords[++k] = pFont.tx_up_right.x;	    _coords[++k] = pFont.tx_up_right.y;	    // 2

        _coords[++k] = pFont.tx_up_right.x;	    _coords[++k] = pFont.tx_up_right.y;	    // 2
        _coords[++k] = pFont.tx_up_left.x;		_coords[++k] = pFont.tx_up_left.y;		// 3
        _coords[++k] = pFont.tx_lo_left.x;		_coords[++k] = pFont.tx_lo_left.y;		// 0
        _cindex = k;

        // color
        int c = _color_index;
        _colors[++c] = (byte)color.r; _colors[++c] = (byte)color.g; _colors[++c] = (byte)color.b; _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r; _colors[++c] = (byte)color.g; _colors[++c] = (byte)color.b; _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r; _colors[++c] = (byte)color.g; _colors[++c] = (byte)color.b; _colors[++c] = (byte)color.a;

        _colors[++c] = (byte)color.r; _colors[++c] = (byte)color.g; _colors[++c] = (byte)color.b; _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r; _colors[++c] = (byte)color.g; _colors[++c] = (byte)color.b; _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r; _colors[++c] = (byte)color.g; _colors[++c] = (byte)color.b; _colors[++c] = (byte)color.a;
        _color_index = c;

        _vertices_count += 6;
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
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
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
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
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

        _colors[++c] = (byte) color.r;
        _colors[++c] = (byte) color.g;
        _colors[++c] = (byte) color.b;
        _colors[++c] = (byte) color.a;

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
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

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
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

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
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

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
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

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
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

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
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

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
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

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
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

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
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

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
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

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
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

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
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;
        _colors[++c] = (byte)color.r;
        _colors[++c] = (byte)color.g;
        _colors[++c] = (byte)color.b;
        _colors[++c] = (byte)color.a;

        _color_index = c;
        _vertices_count += 6;
    }

    public void drawCircleAt(float x, float y, float radius, Color color) {
        float[] vertices = new float[80];
        byte[] colors = new byte[150];
        int v_index = -1;
        int color_index = -1;
        float radian;
        Vector2 pos = new Vector2();
        Vector2 pt = new Vector2();

        pos.x = x;
        pos.y = y;

        vertices[++v_index] = pos.x;
        vertices[++v_index] = pos.y;

        colors[++color_index] = (byte)color.r;
        colors[++color_index] = (byte)color.g;
        colors[++color_index] = (byte)color.b;
        colors[++color_index] = (byte)color.a;

        for (float degree = 0.0f; degree <= 360.0f; degree += 36.0f) {
            radian = (float)Math.toRadians(degree);

            pt.x = pos.x + (float)Math.sin(radian) * radius;
            pt.y = pos.y + (float)Math.cos(radian) * radius;

            vertices[++v_index] = pt.x;
            vertices[++v_index] = pt.y;

            colors[++color_index] = (byte)color.r;
            colors[++color_index] = (byte)color.g;
            colors[++color_index] = (byte)color.b;
            colors[++color_index] = (byte)color.a;
        }

        float[] coords = {};
        float[] norms = {};

        zeroBufferPositions();
        addVerticesCoordsNormalsColors(vertices, coords, norms, colors);
        bindStreamSources2dNoTextures();
        glDrawArrays(GL_TRIANGLE_FAN, 0, v_index / 2 + 1);
    }


}

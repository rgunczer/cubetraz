package com.almagems.cubetraz.graphics;

import static android.opengl.GLES10.*;
import static android.opengl.GLES11Ext.*;
import static android.opengl.GLU.*;

import javax.microedition.khronos.opengles.GL10;

import static com.almagems.cubetraz.Game.*;

import com.almagems.cubetraz.utils.PositionInfo;
import com.almagems.cubetraz.R;
import com.almagems.cubetraz.TextResourceReader;
import com.almagems.cubetraz.math.Vector;
import com.almagems.cubetraz.math.Vector2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


public final class Graphics {

    public static GL10 gl;

    public static int width;
    public static int height;
    public static int halfWidth;
    public static int halfHeight;
    public static float deviceScale;

    private static int[] mOriginalFBO = new int[1];

    public static Texture textureGrayConcrete;
    public static Texture textureKey;
    public static Texture textureFonts;
    public static Texture textureFontsClear;
    public static Texture textureLevelCubes;
    public static Texture textureFontsBig;
    public static Texture textureNumbers;
    public static Texture texturePlayer;
    public static Texture textureStar;
    public static Texture textureSymbols;
    public static Texture textureStatBackground;
    public static Texture textureCredits;
    public static Texture textureDirty;
    public static Texture textureTutor;

    public static int _vertices_count = 0;
    public static int _vindex = -1;
    public static int _cindex = -1;
    public static int _color_index = -1;

    private static float[] _vertices = new float[BUF_SIZE * KILOBYTE];  /*3 * 36 * MAX_CUBE_COUNT * MAX_CUBE_COUNT * MAX_CUBE_COUNT */
    private static float[] _normals = new float[BUF_SIZE * KILOBYTE];
    private static float[] _coords = new float[BUF_SIZE * KILOBYTE];
    private static byte[] _colors = new byte[BUF_SIZE * KILOBYTE];

    public static float aspectRatio;
    public static float scaleFactor;

    private static FloatBuffer _vertexBuffer;
    private static FloatBuffer _normalBuffer;
    private static FloatBuffer _coordsBuffer;
    private static ByteBuffer _colorBuffer;

    public static FBO fbo;

    private Graphics() {}

    public static void createBuffers() {
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

    public static void saveOriginalFBO() {
        glGetIntegerv(GL_FRAMEBUFFER_BINDING_OES, mOriginalFBO, 0);
    }

    public static void restoreOriginalFBO() {
        glBindFramebufferOES(GL_FRAMEBUFFER_OES, mOriginalFBO[0]);
    }

    public static void updateBuffers() {
        _vertexBuffer.put(_vertices);
        _vertexBuffer.position(0);

        _normalBuffer.put(_normals);
        _normalBuffer.position(0);

        _coordsBuffer.put(_coords);
        _coordsBuffer.position(0);

        _colorBuffer.put(_colors);
        _colorBuffer.position(0);
    }

    public static void zeroBufferPositions() {
        _vertexBuffer.position(0);
        _colorBuffer.position(0);
        _coordsBuffer.position(0);
        _normalBuffer.position(0);
    }

    public static void setLightPosition(Vector pos) {
        float posLight[] = { pos.x, pos.y, pos.z, 1.0f };
        glLightfv(GL_LIGHT0, GL_POSITION, posLight, 0);
    }

    public static void drawQuad() {
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
    }
    public static void drawCube() {
        glDrawArrays(GL_TRIANGLES, 0, 36);
    }

    // draw cube face
    public static void drawCubeFaceY_Plus() {
        glDrawArrays(GL_TRIANGLES, 30, 6);
    }
    public static void drawCubeFaceY_Minus() {
        glDrawArrays(GL_TRIANGLES, 18, 6);
    }
    public static void drawCubeFaceX_Plus() {
        glDrawArrays(GL_TRIANGLES, 12, 6);
    }
    public static void drawCubeFaceX_Minus() {
        glDrawArrays(GL_TRIANGLES, 24, 6);
    }
    public static void drawCubeFaceZ_Plus() {
        glDrawArrays(GL_TRIANGLES, 6, 6);
    }
    public static void drawCubeFaceZ_Minus() {
        glDrawArrays(GL_TRIANGLES, 0, 6);
    }

    public static Color getColorFromScreen(Vector2 pos) {
        int x = (int) pos.x;
        int y = height - (int) pos.y;

        ByteBuffer pixelBuf = ByteBuffer.allocateDirect(4);
        pixelBuf.order(ByteOrder.LITTLE_ENDIAN);
        glReadPixels(x, y, 1, 1, GL_RGBA, GL_UNSIGNED_BYTE, pixelBuf);

        byte R = pixelBuf.get(0);
        byte G = pixelBuf.get(1);
        byte B = pixelBuf.get(2);
        byte A = pixelBuf.get(3);

        int r = R & 0xFF;
        int g = G & 0xFF;
        int b = B & 0xFF;
        int a = A & 0xFF;

        Color color = new Color(r, g, b, a);
//        System.out.println("\nx:" + x + ", y:" + y + ", red:" + color.r + ", green:" + color.g + ", blue:" + color.b + ", alpha: " + color.a);
        return color;
    }

    public static void drawAxes() {
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
        glLineWidth(6f);

        glDrawArrays(GL_LINES, 6, 6);

        glLineWidth(1f);
        glDrawArrays(GL_LINES, 0, 6);
    }

    public static void drawFullScreenTexture(FBO fbo, Color color) {
        drawFullScreenTexture(fbo.textureId, color);
    }

    public static void drawFullScreenTexture(Texture texture, Color color) {
        drawFullScreenTexture(texture.id, color);
    }

    private static void drawFullScreenTexture(int textureId, Color color) {
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

    public static void drawFullScreenQuad(Color color) {
        final float verts[] = {
                0f, height,
                0f, 0f,
                width, 0f,
                width, height
        };

        final byte colors[] = {
                (byte)color.r, (byte)color.g, (byte)color.b, (byte)color.a,
                (byte)color.r, (byte)color.g, (byte)color.b, (byte)color.a,
                (byte)color.r, (byte)color.g, (byte)color.b, (byte)color.a,
                (byte)color.r, (byte)color.g, (byte)color.b, (byte)color.a
        };

        final float coords[] = {};
        final float norms[] = {};

        addVerticesCoordsNormalsColors(verts, coords, norms, colors);

        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
    }

    public static void setModelViewMatrix2D() {
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }

    public static void setProjection2D() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrthof(0.0f, width, 0.0f, height, -1.0f, 1.0f);
    }

    public static void setModelViewMatrix3D(final Camera camera) {
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        gluLookAt(gl,
                camera.eye.x,    camera.eye.y,    camera.eye.z,
                camera.target.x, camera.target.y, camera.target.z,
                0f, 1f, 0f);
    }

    public static void setProjection3D() {
        final float zNear = 0.01f;
        final float zFar = 1000.0f;
        final float fieldOfView = 30.0f;
        final float size = zNear * (float) Math.tan((Math.toRadians(fieldOfView) / 2.0f));

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glFrustumf(-size, size, -size / aspectRatio, size / aspectRatio, zNear, zFar);
    }

    public static void addCubeWithColor(float tx, float ty, float tz, Color color) {
        addCubeSize(tx, ty, tz, HALF_CUBE_SIZE, color);
    }

    public static void renderPoints() {
        glDrawArrays(GL_POINTS, 0, _vertices_count);
    }

    public static void addCube(float tx, float ty, float tz) {
        addCubeSize(tx, ty, tz, HALF_CUBE_SIZE, Color.WHITE);
    }

    public static void resetBufferIndices() {
        _vertices_count = 0;
        _vindex = -1;
        _cindex = -1;
        _color_index = -1;
        zeroBufferPositions();
    }

    public static void bindStreamSources2dNoTextures() {
        glEnableClientState(GL_VERTEX_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);

        glVertexPointer(2, GL_FLOAT, 0, _vertexBuffer);
        glColorPointer(4, GL_UNSIGNED_BYTE, 0, _colorBuffer);
    }

    public static void bindStreamSources2d() {
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);

        glVertexPointer(2, GL_FLOAT, 0, _vertexBuffer);
        glTexCoordPointer(2, GL_FLOAT, 0, _coordsBuffer);
        glColorPointer(4, GL_UNSIGNED_BYTE, 0, _colorBuffer);
    }

    public static void bindStreamSources3d() {
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glEnableClientState(GL_NORMAL_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        glVertexPointer(3, GL_FLOAT, 0, _vertexBuffer);
        glNormalPointer(GL_FLOAT, 0, _normalBuffer);
        glTexCoordPointer(2, GL_FLOAT, 0, _coordsBuffer);
        glColorPointer(4, GL_UNSIGNED_BYTE, 0, _colorBuffer);
    }

    public static void bindStreamSources3dNoTexture() {
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glEnableClientState(GL_NORMAL_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);

        glVertexPointer(3, GL_FLOAT, 0, _vertexBuffer);
        glNormalPointer(GL_FLOAT, 0, _normalBuffer);
        glColorPointer(4, GL_UNSIGNED_BYTE, 0, _colorBuffer);
    }

    public static void loadStartupAssets() throws Exception {
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

    public static Texture loadTexture(int resourceId) {
        Texture texture = TextureHelper.loadTexture(resourceId);
        return texture;
    }

    public static int createTexture(int w, int h) {
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

    public static void prepareFrame() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public static void onSurfaceChanged(int width, int height) {
        System.out.println("Graphics.onSurfaceChanged");

        Graphics.width = width;
        Graphics.height = height;

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

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_COLOR_MATERIAL);

        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);

        if (fbo != null) {
            fbo.release();
        }
        saveOriginalFBO();

        fbo = new FBO(width, height);
        fbo.createWithColorAndDepthStencilBuffer();

        restoreOriginalFBO();
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

    public static void addVerticesCoordsColors(float[] verts, float[] coords, byte[] colors) {
        _vertexBuffer.position(0);
        _coordsBuffer.position(0);
        _colorBuffer.position(0);

        _vertexBuffer.put(verts);
        _coordsBuffer.put(coords);
        _colorBuffer.put(colors);
    }

    public static void addVerticesCoordsNormalsColors(float[] verts, float[] coords, float[] norms, byte[] colors) {
        _vertexBuffer.put(verts);
        _coordsBuffer.put(coords);
        _normalBuffer.put(norms);
        _colorBuffer.put(colors);

        _vertexBuffer.position(0);
        _normalBuffer.position(0);
        _coordsBuffer.position(0);
        _colorBuffer.position(0);
    }

    public static void addFont(Vector2 pos, Vector2 scale, Color color, TexturedQuad pFont) {
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

    public static void addQuad(float x, float y, float w, float h, Color color) {
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

    public static void addQuad(float size, float tx, float ty, TexCoordsQuad coords) {
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

    public static void addQuad(float size, float tx, float ty, TexCoordsQuad coords, Color color) {
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

        _colors[++c] = (byte) color.r;
        _colors[++c] = (byte) color.g;
        _colors[++c] = (byte) color.b;
        _colors[++c] = (byte) color.a;

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

    public static void addCubeFace_X_Minus(float tx, float ty, float tz) {
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

    public static void addCubeFace_Y_Plus(float tx, float ty, float tz) {
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

    public static void addCubeFace_Y_Minus(float tx, float ty, float tz) {
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

    public static void addCubeFace_Z_Plus(float tx, float ty, float tz) {
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

    public static void addCubeFace_Z_Minus(float tx, float ty, float tz) {
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

    public static void addCubeFace_X_Plus(float tx, float ty, float tz, TexCoordsQuad coords) {
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

    public static void addCubeFace_X_Minus(float tx, float ty, float tz, TexCoordsQuad coords) {
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

    public static void addCubeFace_Y_Plus(float tx, float ty, float tz, TexCoordsQuad coords) {
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

    public static void addCubeFace_Y_Minus(float tx, float ty, float tz, TexCoordsQuad coords) {
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

    public static void addCubeFace_Z_Plus(float tx, float ty, float tz, TexCoordsQuad coords) {
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

    public static void addCubeFace_Z_Minus(float tx, float ty, float tz, TexCoordsQuad coords) {
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

    public static void addCubeFace_X_Plus(float tx, float ty, float tz, TexCoordsQuad coords, Color color) {
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

    public static void addCubeFace_X_Minus(float tx, float ty, float tz, TexCoordsQuad coords, Color color) {
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

    public static void addCubeFace_Y_Plus(float tx, float ty, float tz, TexCoordsQuad coords, Color color) {
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

    public static void addCubeFace_Y_Minus(float tx, float ty, float tz, TexCoordsQuad coords, Color color) {
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

    public static void addCubeFace_Z_Plus(float tx, float ty, float tz, TexCoordsQuad coords, Color color) {
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

    public static void addCubeFace_Z_Minus(float tx, float ty, float tz, TexCoordsQuad coords, Color color) {
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

    public static void addCubeFace_X_Plus(float tx, float ty, float tz, Color color) {
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

    public static void addCubeFace_X_Minus(float tx, float ty, float tz, Color color) {
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

    public static void addCubeFace_Y_Plus(float tx, float ty, float tz, Color color) {
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

    public static void addCubeFace_Y_Minus(float tx, float ty, float tz, Color color) {
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

    public static void addCubeFace_Z_Plus(float tx, float ty, float tz, Color color) {
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

    public static void addCubeFace_Z_Minus(float tx, float ty, float tz, Color color) {
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

    public static void drawCircleAt(float x, float y, float radius, Color color) {
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

    public static void translate(final Vector vec) {
        glTranslatef(vec.x, vec.y, vec.z);
    }

    public static void rotateX(final float degree) {
        glRotatef(degree, 1.0f, 0.0f, 0.0f);
    }

    public static void rotateY(final float degree) {
        glRotatef(degree, 0.0f, 1.0f, 0.0f);
    }

    public static void rotateZ(final float degree) {
        glRotatef(degree, 0.0f, 0.0f, 1.0f);
    }

    public static void scale(final float x, final float y, final float z) {
        glScalef(x, y, z);
    }

}

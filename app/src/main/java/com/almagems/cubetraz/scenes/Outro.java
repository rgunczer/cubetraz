package com.almagems.cubetraz.scenes;

import com.almagems.cubetraz.graphics.Camera;
import com.almagems.cubetraz.graphics.Color;
import com.almagems.cubetraz.cubes.Cube;
import com.almagems.cubetraz.graphics.Graphics;
import com.almagems.cubetraz.utils.CubeRotation;
import com.almagems.cubetraz.game.Game;
import com.almagems.cubetraz.utils.Starfield;
import com.almagems.cubetraz.graphics.Text;
import com.almagems.cubetraz.math.Utils;
import com.almagems.cubetraz.math.Vector;
import com.almagems.cubetraz.math.Vector2;

import java.util.ArrayList;

import static android.opengl.GLES10.*;
import static com.almagems.cubetraz.game.Audio.*;
import static com.almagems.cubetraz.game.Game.*;


public final class Outro extends Scene {

    private enum OutroStateEnum {
        AnimToOutro,
        RotateFull,
        OutroExplosion,
        OutroDone
    }

    private int mCubeTrazAlpha;

    private Text[] mTextCenter = new Text[2];

    private OutroStateEnum mState;

    private Camera mCameraMenu = new Camera();
    private Camera mCameraOutro = new Camera();

    private Starfield mStarfield = new Starfield();

    private float mStarsAlpha;
    private float m_t;
    private float mDegreePlayerCube;

    private Vector mPosLightOutro = new Vector();
    private Vector mPosLightMenu = new Vector();
    private Vector mPosCubePlayer = new Vector();

    private boolean mDrawStarfield;
    private float mCubeRotSpeed;
    private float mCenterAlpha;

    private CubeRotation mCubeRotation = new CubeRotation();

    private ArrayList<Cube> mCubesBase = new ArrayList<>();
    private ArrayList<Cube> mCubesLevel = new ArrayList<>();
    private ArrayList<Cube> mCubesBaseAppear = new ArrayList<>();
    private ArrayList<Cube> mCubesBaseDisappear = new ArrayList<>();

    public void onFingerDown(float x, float y, int fingerCount) {}
    public void onFingerMove(float prevX, float prevY, float curX, float curY, int fingerCount) {}


    public Outro() {
        mCameraOutro.eye = new Vector(0.0f, 10.0f, 30.0f);
        mCameraOutro.target = new Vector(0.0f, 0.0f, 0.0f);

        mTextCenter[0] = new Text();
        mTextCenter[1] = new Text();

        mTextCenter[0].setScale(1.0f, 1.0f);
        mTextCenter[1].setScale(1.0f, 1.0f);

        mTextCenter[0].setVSPace(0.8f);
        mTextCenter[1].setVSPace(0.8f);
    }

    @Override
    public void init() {
        mTick = 0;
        mTextCenter[0].init("CONGRATULATIONS");
        mTextCenter[1].init("CUBETRAZ IS SOLVED");

        mCenterAlpha = 0.0f;
        mCubeTrazAlpha = 255;

        mStarfield.speed = 0.2f;
        mStarfield.create();

        for (int i = 0; i < 30*10; ++i) {
            mStarfield.update();
        }

        mCameraOutro.init(Game.level.mCameraLevel);
        mCameraMenu.init(Game.menu.getCamera());
        mCameraCurrent.init(mCameraOutro);

        mPosLightOutro.init(Game.level.mPosLightCurrent);
        mPosLightMenu.init(Game.menu.getLightPositon());
        mPosLightCurrent.init(mPosLightOutro);

        mCubeRotation.degree = -45.0f;
        mCubeRotation.axis = new Vector(0.0f, 1.0f, 0.0f);

        mDrawStarfield = false;
        mStarsAlpha = 0.0f;
        mStarfield.alpha = mStarsAlpha * 255f;

        mCubeRotSpeed = 0.1f;

        mState = OutroStateEnum.AnimToOutro;

        m_t = 0.0f;

        mCubesBase.clear();
        mCubesLevel.clear();

        int size;
        Cube cube;

        size = Game.level.m_list_cubes_level.size();
        for (int i = 0; i < size; ++i) {
            cube = Game.level.m_list_cubes_level.get(i);
            mCubesLevel.add(cube);
        }

        size = Game.level.m_list_cubes_wall_y_minus.size();
        for (int i = 0; i < size; ++i) {
            cube = Game.level.m_list_cubes_wall_y_minus.get(i);
            mCubesBase.add(cube);
        }

        size = Game.level.m_list_cubes_wall_x_minus.size();
        for (int i = 0; i < size; ++i) {
            cube = Game.level.m_list_cubes_wall_x_minus.get(i);
            mCubesBase.add(cube);
        }

        size = Game.level.m_list_cubes_wall_z_minus.size();
        for (int i = 0; i < size; ++i) {
            cube = Game.level.m_list_cubes_wall_z_minus.get(i);
            mCubesBase.add(cube);
        }

        size = Game.level.m_list_cubes_edges.size();
        for (int i = 0; i < size; ++i) {
            cube = Game.level.m_list_cubes_edges.get(i);
            mCubesBase.add(cube);
        }

        mCubesBaseDisappear.clear();
        
        size = mCubesBase.size();
        for (int i = 0; i < size; ++i) {
            cube = mCubesBase.get(i);
            if (cube != null) {
                if (8 == cube.x || 8 == cube.z) {
                    mCubesBaseDisappear.add(cube);
                }
            }
        }

        mCubesBaseAppear.clear();

        ArrayList<Cube> lst = Game.createBaseCubesList();
        size = lst.size();
        for (int i = 0; i < size; ++i) {
            cube = lst.get(i);
            if ( !Game.isOnAList(cube, mCubesBase) ) {
                cube.setColor(Color.WHITE);
                mCubesBaseAppear.add(cube);
            }
        }

        mPosCubePlayer.init(Game.getCubePosAt(Game.level.mPlayerCube.getLocation()));

        Game.graphics.setLightPosition(mPosLightCurrent);
    }

    private void setupExplosion() {
        Cube cube;
        int size = mCubesBase.size();

        for (int i = 0; i < size; ++i) {
            cube = mCubesBase.get(i);

            cube.velocity.x = -60 + Utils.rand.nextInt()%120;
            cube.velocity.y = -60 + Utils.rand.nextInt()%120;
            cube.velocity.z = -60 + Utils.rand.nextInt()%120;

            if ( cube.velocity.x > 0.0f && cube.velocity.x <  30.0f ) cube.velocity.x = Utils.randInt(30, 50);
            if ( cube.velocity.x < 0.0f && cube.velocity.x > -30.0f ) cube.velocity.x = Utils.randInt(30, 50) * -1.0f;

            if ( cube.velocity.y > 0.0f && cube.velocity.y <  30.0f ) cube.velocity.y = Utils.randInt(30, 50);
            if ( cube.velocity.y < 0.0f && cube.velocity.y > -30.0f ) cube.velocity.y = Utils.randInt(30, 50) * -1.0f;

            if ( cube.velocity.z > 0.0f && cube.velocity.z <  30.0f ) cube.velocity.z = Utils.randInt(30, 50);
            if ( cube.velocity.z < 0.0f && cube.velocity.z > -30.0f ) cube.velocity.z = Utils.randInt(30, 50) * -1.0f;

            cube.velocity.x *= 0.001f;
            cube.velocity.y *= 0.001f;
            cube.velocity.z *= 0.001f;
        }
    }

    @Override
    public void update() {
        ++mTick;
        switch (mState) {
            case AnimToOutro:
                updateInAnimTo();
                break;

            case RotateFull:
                mCenterAlpha += 0.04f;
                --Game.dirtyAlpha;

                if (Game.dirtyAlpha < 0) {
                    Game.dirtyAlpha = 0;
                }
                updateInRotateFull();
                break;

            case OutroExplosion:
                updateInOutroExplosion();
                break;

            case OutroDone:
                mCenterAlpha -= 0.03f;
                updateInOutroDone();
                break;
        }
    }

    private void updateInAnimTo() {
        boolean done = true;
        Cube cube;

        if (!mCubesBaseAppear.isEmpty()) {
            done = false;
            cube = mCubesBaseAppear.get(0);
            mCubesBaseAppear.remove(cube);

            mCubesBase.add(cube);
        }

        if (!mCubesBaseDisappear.isEmpty()) {
            done = false;
            cube = mCubesBaseDisappear.get(0);
            mCubesBaseDisappear.remove(cube);

            mCubesBase.remove(cube);
        }

        if (!mCubesLevel.isEmpty()) {
            done = false;
            cube = mCubesLevel.get( mCubesLevel.size() - 1);
            mCubesLevel.remove(cube);
        }

        if (mCubeRotSpeed < 4.0f && mCubeRotSpeed > 0.0f) {
            done = false;
            mCubeRotation.degree -= mCubeRotSpeed;
            mCubeRotSpeed += 0.01f;
        }

        if (mCubesBaseAppear.isEmpty()) {
            m_t += 0.01f;
            if (m_t > 1.0f) m_t = 1.0f;
            Utils.lerpCamera(mCameraOutro, mCameraMenu, m_t, mCameraCurrent);
            Utils.lerpVector3(mPosLightOutro, mPosLightMenu, m_t, mPosLightCurrent);
        }

        // do warm by factor
        int size = mCubesBase.size();
        for (int i = 0; i < size; ++i) {
            cube = mCubesBase.get(i);
            cube.warmByFactor(60);
        }

        if (done && Math.abs(1.0f - m_t) < EPSILON) {
            mState = OutroStateEnum.RotateFull;
            mDrawStarfield = true;
            mStarsAlpha = 0.0f;
            Game.audio.stopMusic();
        }
    }

    private void updateInRotateFull() {
        mCubeRotation.degree -= mCubeRotSpeed;

        mStarsAlpha += 0.02f;

        if (mStarsAlpha > 1.0f) {
            mState = OutroStateEnum.OutroExplosion;
            mStarsAlpha = 1.0f;
            mPosCubePlayer.init(Game.getCubePosAt(4, 4, 4));
            mDegreePlayerCube = 0.0f;
            setupExplosion();
            Game.audio.playMusic(MUSIC_VECTORS);
        }

        mStarfield.alpha = mStarsAlpha * 255f;
        mStarfield.update();
    }

    private void updateInOutroExplosion() {
        mStarfield.speed += 0.0003f;
        mDegreePlayerCube += 1.0f;

        Cube cube;
        int size = mCubesBase.size();
        for (int i = 0; i < size; ++i) {
            cube = mCubesBase.get(i);
            cube.update();

            if (mTick % 4 == 0) {
                cube.colorCurrent.randomizeGray();
            }
        }

        float rot = (float)Math.ceil(mCubeRotation.degree);
        if ((rot % 180) == 0 ) {
            mState = OutroStateEnum.OutroDone;
        } else {
            mCubeRotation.degree += 0.5f;
        }

        mStarfield.alpha = mStarsAlpha * 255f;
        mStarfield.update();
    }

    private void updateInOutroDone() {
        mCubeRotation.degree += 0.5f;
        mStarfield.speed += 0.0003f;
        mDegreePlayerCube += 1.25f;
        mCubeTrazAlpha -= 2;

        if (mCubeTrazAlpha < 0) {
            mCubeTrazAlpha = 0;
        }

        Cube cube;
        int size = mCubesBase.size();
        for (int i = 0; i < size; ++i) {
            cube = mCubesBase.get(i);
            cube.colorCurrent.a = mCubeTrazAlpha;
            cube.update();
        }

        mStarfield.alpha = mStarsAlpha * 255f;
        mStarfield.update();

        if (mStarfield.speed > 1.2f && mCubeTrazAlpha == 0) {
            goToMenuScene();
        }
    }

    private void drawTheCube() {
        Graphics graphics = Game.graphics;
        graphics.bindStreamSources3d();
        graphics.resetBufferIndices();

        Cube cube;
        int size = mCubesBase.size();
        for (int i = 0; i < size; ++i) {
            cube = mCubesBase.get(i);
            graphics.addCubeSize(cube.tx, cube.ty, cube.tz, HALF_CUBE_SIZE, cube.colorCurrent);
        }

        size = mCubesLevel.size();
        for (int i = 0; i < size; ++i) {
            cube = mCubesLevel.get(i);
            graphics.addCubeSize(cube.tx, cube.ty, cube.tz, HALF_CUBE_SIZE, cube.colorCurrent);
        }

        glEnable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);

        graphics.textureGrayConcrete.bind();
        graphics.updateBuffers();
        graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);
    }

    private void drawText() {
        Graphics graphics = Game.graphics;
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_LIGHTING);
        glDisable(GL_CULL_FACE);
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);

        graphics.setProjection2D();
        graphics.setModelViewMatrix2D();

        int a = (int) mCenterAlpha * 255;

        glDisable(GL_TEXTURE_2D);
        Color color_bg = new Color(30, 30, 15, (int) mCenterAlpha * 150);

        graphics.bindStreamSources2dNoTextures();
        graphics.resetBufferIndices();
        graphics.addQuad(0.0f, Game.graphics.halfHeight - 20.0f * Game.graphics.deviceScale, Game.graphics.width, 75.0f * Game.graphics.deviceScale, color_bg);
        graphics.updateBuffers();
        graphics.renderTriangles();

        graphics.bindStreamSources2d();
        glEnable(GL_TEXTURE_2D);
        graphics.textureFontsBig.bind();

        float scale = 0.75f * Game.graphics.deviceScale;
        mTextCenter[0].setScale(scale, scale);
        mTextCenter[1].setScale(scale, scale);

        Color color = new Color(0, 0, 0, a);

        graphics.resetBufferIndices();

        Vector2 pos = new Vector2();
        pos.x = Game.graphics.halfWidth - mTextCenter[0].getHalfWidth();
        pos.y = Game.graphics.halfHeight + mTextCenter[0].getHalfHeight();
        mTextCenter[0].emitt(pos, color);

        pos.x = Game.graphics.halfWidth - mTextCenter[1].getHalfWidth();
        pos.y = Game.graphics.halfHeight - mTextCenter[1].getHalfHeight();
        mTextCenter[1].emitt(pos, color);

        glPushMatrix();
        glTranslatef(Game.graphics.deviceScale, Game.graphics.deviceScale, 0.0f);
        graphics.updateBuffers();
        graphics.renderTriangles();
        glPopMatrix();

        color = new Color(255, 255, 0, a);

        graphics.resetBufferIndices();

        pos.x = Game.graphics.halfWidth - mTextCenter[0].getHalfWidth();
        pos.y = Game.graphics.halfHeight + mTextCenter[0].getHalfHeight();
        mTextCenter[0].emitt(pos, color);

        pos.x = Game.graphics.halfWidth - mTextCenter[1].getHalfWidth();
        pos.y = Game.graphics.halfHeight - mTextCenter[1].getHalfHeight();
        mTextCenter[1].emitt(pos, color);

        graphics.updateBuffers();
        graphics.renderTriangles();

        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
    }

    public void render() {
        Graphics graphics = Game.graphics;
        Color color = new Color();

        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        glEnable(GL_BLEND);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);

        if (Game.dirtyAlpha > 0) {
            graphics.setProjection2D();
            graphics.setModelViewMatrix2D();
            graphics.bindStreamSources2d();

            glDisable(GL_LIGHTING);
            glDepthMask(false);
            glEnable(GL_TEXTURE_2D);

            color.init(255, 255, 255, (int)Game.dirtyAlpha);
            graphics.drawFullScreenTexture(Game.graphics.textureDirty, color);

            glDepthMask(true);
            glEnable(GL_LIGHTING);
        }

        graphics.setProjection3D();
        graphics.setModelViewMatrix3D(mCameraCurrent);
        graphics.bindStreamSources3d();
        graphics.zeroBufferPositions();
        graphics.resetBufferIndices();

        if (mDrawStarfield) {
            glDisable(GL_TEXTURE_2D);
            glDisable(GL_LIGHTING);

            glDisableClientState(GL_NORMAL_ARRAY);
            glDisableClientState(GL_TEXTURE_COORD_ARRAY);

            glDepthMask(false);

            glEnable(GL_POINT_SMOOTH);
            mStarfield.render();
            glDisable(GL_POINT_SMOOTH);

            glEnable(GL_LIGHTING);
            glDepthMask(true);
            glEnable(GL_TEXTURE_2D);

            glEnableClientState(GL_NORMAL_ARRAY);
            glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        }

        graphics.texturePlayer.bind();

        if (OutroStateEnum.OutroExplosion == mState || OutroStateEnum.OutroDone == mState) {
            glPushMatrix();

            glRotatef(mDegreePlayerCube, 1.0f, 0.0f, 0.0f);
            glRotatef(mDegreePlayerCube, 0.0f, 1.0f, 0.0f);
            glRotatef(mDegreePlayerCube, 0.0f, 0.0f, 1.0f);

            graphics.resetBufferIndices();
            graphics.addCube(mPosCubePlayer.x, mPosCubePlayer.y, mPosCubePlayer.z);
            graphics.updateBuffers();
            graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);

            glPopMatrix();
        } else {
            glPushMatrix();
            glRotatef(mCubeRotation.degree, mCubeRotation.axis.x, mCubeRotation.axis.y, mCubeRotation.axis.z);

            graphics.resetBufferIndices();
            graphics.addCube(mPosCubePlayer.x, mPosCubePlayer.y, mPosCubePlayer.z);
            graphics.updateBuffers();
            graphics.renderTriangles(Game.cube_offset.x, Game.cube_offset.y, Game.cube_offset.z);

            glPopMatrix();
        }

        if (mCubeTrazAlpha > 0) {
            glPushMatrix();
            glRotatef(mCubeRotation.degree, mCubeRotation.axis.x, mCubeRotation.axis.y, mCubeRotation.axis.z);

            drawTheCube();

//            if (false) { //#ifdef DRAW_AXES_CUBE
//                glDisable(GL_TEXTURE_2D);
//                glDisable(GL_LIGHTING);
//                graphics.drawAxes();
//                glEnable(GL_LIGHTING);
//                glEnable(GL_TEXTURE_2D);
//            } //#endif

            glPopMatrix();
        }
        if (mCenterAlpha > EPSILON) {
            drawText();
        }
    }
    
    public void onFingerUp(float x, float y, int fingerCount) {
        if (OutroStateEnum.OutroDone == mState) {
            goToMenuScene();
        }
    }

    private void goToMenuScene() {
        Game.audio.stopMusic();
        Game.showScene(Scene_Menu);
    }

}

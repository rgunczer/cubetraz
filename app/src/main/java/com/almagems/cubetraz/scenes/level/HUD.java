package com.almagems.cubetraz.scenes.level;

import com.almagems.cubetraz.Game;
import com.almagems.cubetraz.graphics.Texture;
import com.almagems.cubetraz.math.Utils;
import com.almagems.cubetraz.math.Vector2;
import com.almagems.cubetraz.graphics.Color;
import com.almagems.cubetraz.graphics.Graphics;
import com.almagems.cubetraz.graphics.TexCoordsQuad;
import com.almagems.cubetraz.graphics.Text;
import com.almagems.cubetraz.graphics.TexturedQuad;

import static android.opengl.GLES10.*;

import java.util.Stack;

import static com.almagems.cubetraz.Game.*;

final class HUD {

    private final boolean[] mArrTutorDisplayed = new boolean[MAX_TUTOR_COUNT];
    
	private HUDStateEnum mState;
	
    private TexturedQuad mSymbolPause;
    private TexturedQuad mSymbolUndo;
    private TexturedQuad mSymbolHint;
    private TexturedQuad mSymbolSolver;
    private TexturedQuad mSymbolStar;
    private TexturedQuad mSymbolDeath;

    private Text mTextMotto = new Text();

    private boolean mFlashMotto = false;

    private Text mTextPause = new Text();
    private Text mTextUndo = new Text();
    private Text mTextHint = new Text();
    private Text mTextSolver = new Text();
    
    private Text mTextLevel = new Text();
    private Text mTextMoves = new Text();
    private Text mTextStars = new Text();

    private TexCoordsQuad tcoords = new TexCoordsQuad();

    private Text[] mArTextCenter = new Text[2];

	private float mPosXTextLeft;
	private float mPosXTextLeftStart;
	private float mPosXTextLeftEnd;

    private float mPosXTextRight;
    private float mPosXTextRightStart;
    private float mPosXTextRightEnd;
	
	private float mPosYMotto;
	private float mPosYMottoStart;
	private float mPosYMottoEnd;
	
	private int mIconsAlpha;

    private boolean mHilitePause;
    private boolean mHiliteUndo;
    private boolean mHiliteHint;
    private boolean mHiliteSolver;

	private boolean mShowHint;
    private float mElapsed;
    
    private boolean mShowPrepareSolving;
    private boolean mShowAppear;
    private float mCenterAlpha;
    
    private Vector2 mPosCenter;
    private float mRadius;
    private Vector2 mStart;
    private Vector2 mEnd;
    
    private Stack<Integer> mTutors = new Stack<>();
    private TutorStateEnum mTutorState;
    private boolean mTutorActive;
    private int mTutorAlpha;
    private Texture mTutorTexture;

    private Color colorIcon = new Color(140, 0, 0, mIconsAlpha);
    private Color colorText = new Color(225, 10, 50, mIconsAlpha);
    private Color colorHilite = new Color(240, 240, 240, mIconsAlpha);
    private Color colorShadow = new Color(0, 0, 0, mIconsAlpha);

    private DeadAnim mDeadAnim;

    HUD(DeadAnim deadAnim) {
        mDeadAnim = deadAnim;
        mArTextCenter[0] = new Text();
        mArTextCenter[1] = new Text();

        // align
        mTextLevel.setAlign(TextAlignEnum.RightAlign);
        mTextStars.setAlign(TextAlignEnum.RightAlign);
        mTextMoves.setAlign(TextAlignEnum.RightAlign);
        mTextMotto.setAlign(TextAlignEnum.RightAlign);
        
        // setscale
        mTextPause.setScale(1.2f, 1.0f);
        mTextUndo.setScale(1.2f, 1.0f);
        mTextHint.setScale(1.2f, 1.0f);
        mTextSolver.setScale(1.2f, 1.0f);
    
        mTextLevel.setScale(2.6f, 2.3f);
        mTextStars.setScale(1.0f, 1.0f);
        mTextMoves.setScale(1.0f, 1.0f);
        mTextMotto.setScale(1.0f, 1.0f);
    
        mArTextCenter[0].setScale(1.0f, 1.0f);
        mArTextCenter[1].setScale(1.0f, 1.0f);
    
        // vspace
        mTextPause.setVSPace(0.75f);
        mTextUndo.setVSPace(0.75f);
        mTextHint.setVSPace(0.75f);
        mTextSolver.setVSPace(0.75f);
    
        mTextLevel.setVSPace(0.75f);
        mTextStars.setVSPace(0.75f);
        mTextMoves.setVSPace(0.75f);
        mTextMotto.setVSPace(0.75f);
    
        mArTextCenter[0].setVSPace(0.8f);
        mArTextCenter[1].setVSPace(0.8f);
    
        mHilitePause = mHiliteUndo = mHiliteHint = mHiliteSolver = false;
    
        for (int i = 0; i < MAX_TUTOR_COUNT; ++i) {        
            mArrTutorDisplayed[i] = false;
        }
    }

    void clearTutors() {
        while ( !mTutors.isEmpty() ) {
            mTutors.pop();
        }
    }

    void showTutor(final int tutorId) {
        switch (tutorId) {
            case Tutor_Swipe:
                showTutorSwipeAndGoal();
                break;

            case Tutor_MenuPause:
                showTutorMenuPause();
                break;

            case Tutor_MenuHint:
                showTutorMenuHint();
                break;

            default:
                if (!mArrTutorDisplayed[tutorId]) {
                    mTutors.push(tutorId);
                    mTutorActive = true;
                    mTutorAlpha = 0;
                    mTutorState = TutorStateEnum.Appear;
                    mTutorTexture = Game.loadTutorTexture(tutorId);
                    mArrTutorDisplayed[tutorId] = true;
                }
        }
    }

    private void showTutorSwipeAndGoal() {
        if (!mArrTutorDisplayed[Tutor_Swipe]) {
            mTutors.push(Tutor_Goal);
            mTutors.push(Tutor_Swipe);
            mTutorActive = true;
            mTutorAlpha = 0;
            mTutorState = TutorStateEnum.Appear;
            mTutorTexture = Game.loadTutorTexture(Tutor_Swipe);
            mArrTutorDisplayed[Tutor_Swipe] = true;
        }
    }

    private void showTutorGoal() {
        if (!mArrTutorDisplayed[Tutor_Goal]) {
            mTutorActive = true;
            mTutorAlpha = 0;
            mTutorState = TutorStateEnum.Appear;
            mTutorTexture = Game.loadTutorTexture(Tutor_Goal);
            mArrTutorDisplayed[Tutor_Goal] = true;
        }
    }

    private void showTutorMenuPause() {
        if (!mArrTutorDisplayed[Tutor_MenuPause]) {
            mTutors.push(Tutor_MenuUndo);
            mTutors.push(Tutor_MenuPause);
            mTutorActive = true;
            mTutorAlpha = 0;
            mTutorState = TutorStateEnum.Appear;
            mTutorTexture = Game.loadTutorTexture(Tutor_MenuPause);
            mArrTutorDisplayed[Tutor_MenuPause] = true;
        }
    }

    private void showTutorMenuUndo() {
        if (!mArrTutorDisplayed[Tutor_MenuUndo]) {
            mTutorActive = true;
            mTutorAlpha = 0;
            mTutorState = TutorStateEnum.Appear;
            mTutorTexture = Game.loadTutorTexture(Tutor_MenuUndo);
            mArrTutorDisplayed[Tutor_MenuUndo] = true;
        }
    }

    private void showTutorMenuHint() {
        if (!mArrTutorDisplayed[Tutor_MenuHint]) {
            mTutors.push(Tutor_MenuSolvers);
            mTutors.push(Tutor_MenuHint);
            mTutorActive = true;
            mTutorAlpha = 0;
            mTutorState = TutorStateEnum.Appear;
            mTutorTexture = Game.loadTutorTexture(Tutor_MenuHint);
            mArrTutorDisplayed[Tutor_MenuHint] = true;
        }
    }

    private void showTutorMenuSolvers() {
        if (!mArrTutorDisplayed[Tutor_MenuSolvers]) {
            mTutorActive = true;
            mTutorAlpha = 0;
            mTutorState = TutorStateEnum.Appear;
            mTutorTexture = Game.loadTutorTexture(Tutor_MenuSolvers);
            mArrTutorDisplayed[Tutor_MenuSolvers] = true;
        }
    }

    void hideTutor() {
        if (mTutorActive && (TutorStateEnum.Done == mTutorState)) {
            mTutorState = TutorStateEnum.Disappear;
        }
    }

    void setTextMotto(final String str) {
        mTextMotto.init(str);
    }
    void setTextLevel(final String str) {
        mTextLevel.init("LEVEL\n" + str);
    }
    void setTextMoves(final int moves) {
        mTextMoves.init("MOVES\n" + moves);
    }
    void setTextStars(final int stars) {
        mTextStars.init("STARS\n" + stars);
    }
    void set1stHint() {
	    mTextHint.init("HINT\nTO\nBEGIN");
    }
    void set2ndHint() { mTextHint.init("HINT\nTO\nFINISH");}
    void setTextUndo(final int count) {
        mTextUndo.init("UNDO\n" + count);
    }
    void setTextSolver(final int count) {
        mTextSolver.init("SOLVERS\n" + count);
    }

    void showPrepareSolving(boolean show, int moves) {
        if (mShowPrepareSolving != show) {
            mShowPrepareSolving = show;

            mArTextCenter[0].init("THE SOLUTION");
            mArTextCenter[1].init("IN " + moves + " MOVES");

            mCenterAlpha = (show ? 0.0f : 1.0f);
            mShowAppear = show;
        }
    }

    void flashMotto(boolean value) {
        mFlashMotto = value;
    }

    void setupAppear() {
	    mState = HUDStateEnum.Appear;
    
	    mIconsAlpha = 0;
        mHilitePause = mHiliteUndo = mHiliteHint = mHiliteSolver = false;

        mPosXTextLeft = mPosXTextLeftStart;
        mPosXTextRight = mPosXTextRightStart;
	    mPosYMotto = mPosYMottoStart;

	    mTextPause.setVisible(true);
	    mTextUndo.setVisible(true);
	    mTextHint.setVisible(true);
	    mTextSolver.setVisible(true);
    	
	    mTextLevel.setVisible(true);
	    mTextStars.setVisible(true);
	    mTextMoves.setVisible(true);
	    mTextMotto.setVisible(true);
    }

    void setupDisappear() {
	    mState = HUDStateEnum.Disappear;
	
	    mIconsAlpha = 255;
        mHilitePause = mHiliteUndo = mHiliteHint = mHiliteSolver = false;

	    mPosXTextLeft = mPosXTextLeftEnd;
        mPosXTextRight = mPosXTextRightEnd;
        mPosYMotto = mPosYMottoEnd;
    }

    void showHintToBegin(int hintType) {
        float degree = 0.0f;
        float rad;
        float deviceScale = Game.graphics.deviceScale;
    
	    mShowHint = true;
        mElapsed = 0.0f;
        mRadius = 10.0f * deviceScale;

        mStart = new Vector2(0.0f, -40.0f * deviceScale);
        mEnd = new Vector2(0.0f, 40.0f * deviceScale);

        switch (hintType) {
            case AxisMovement_X_Plus:
                degree = -120.0f;
                rad = (float)Math.toRadians(degree);
                mStart.rotateRad(rad);
                mEnd.rotateRad(rad);
                break;
            
            case AxisMovement_X_Minus:
                degree = 60.0f;
                rad = (float)Math.toRadians(degree);
                mStart.rotateRad(rad);
                mEnd.rotateRad(rad);
                break;
            
            case AxisMovement_Y_Plus:
                rad = (float)Math.toRadians(degree);
                mStart.rotateRad(rad);
                mEnd.rotateRad(rad);
                break;
            
            case AxisMovement_Y_Minus:
                degree = 180.0f;
                rad = (float)Math.toRadians(degree);
                mStart.rotateRad(rad);
                mEnd.rotateRad(rad);
                break;
            
            case AxisMovement_Z_Plus:
                degree = 120.0f;
                rad = (float)Math.toRadians(degree);
                mStart.rotateRad(rad);
                mEnd.rotateRad(rad);
                break;
            
            case AxisMovement_Z_Minus:
                degree = -60.0f;
                rad = (float)Math.toRadians(degree);
                mStart.rotateRad(rad);
                mEnd.rotateRad(rad);
                break;
            
            default:
                break;
        }
    }

    public void init() {
        float deviceScale = Game.graphics.deviceScale;
        mTutorActive = false;
        mFlashMotto = false;
        mPosCenter = new Vector2(Game.graphics.halfWidth, Game.graphics.halfHeight);
        mShowPrepareSolving = false;
        mHilitePause = mHiliteUndo = mHiliteHint = mHiliteSolver = false;
	
        mTextPause.init("PAUSE");
        mTextUndo.init("UNDO\nLAST\nMOVE");
        mTextHint.init("HINT\nFIRST\nMOVE");
        mTextSolver.init("SOLVERS\n" + Game.options.getSolverCount());
    
        mTextLevel.init("LEVEL\nEASY-01");
        mTextStars.init("STARS\n0");
        mTextMoves.init("MOVES\n0");
        mTextMotto.init("MOTTO\n0\n0");
    
        float scale = 0.4f * deviceScale;
        mTextLevel.setScale(scale*1.1f, scale);
        scale = 0.33f * deviceScale;
        mTextStars.setScale(scale, scale);
        mTextMoves.setScale(scale, scale);
        mTextMotto.setScale(scale, scale);
    
        mTextPause.setScale(scale, scale);
        mTextUndo.setScale(scale, scale);
        mTextHint.setScale(scale, scale);
        mTextSolver.setScale(scale, scale);
    
        mArTextCenter[0].init("THE SOLUTION IS...");
        mArTextCenter[1].init("THE SOLUTION IS...");
    
        mSymbolPause = Game.getSymbol(Symbol_Pause);
        mSymbolUndo = Game.getSymbol(Symbol_Undo);
        mSymbolHint = Game.getSymbol(Symbol_Questionmark);
        mSymbolSolver = Game.getSymbol(Symbol_Solver);
        mSymbolStar = Game.getSymbol(Symbol_Star);
	    mSymbolDeath = Game.getSymbol(Symbol_Death);
    
	    mPosXTextLeftStart = -100.0f * deviceScale;
	    mPosXTextLeftEnd = 5.0f * deviceScale;

	    mPosYMottoStart = -15.0f * deviceScale;
	    mPosYMottoEnd = 30.0f * deviceScale;
	
        mPosXTextRightStart = Game.graphics.width + (75.0f * deviceScale);
	    mPosXTextRightEnd = Game.graphics.width - (6.0f * deviceScale);

	    mState = HUDStateEnum.Done;
	    mShowHint = false;
    }

    public void update() {
        if (mTutorActive) {
            switch(mTutorState) {
                case Appear:
                    mTutorAlpha += 10;
            
                    if (mTutorAlpha > 255) {
                        mTutorAlpha = 255;
                        mTutorState = TutorStateEnum.Done;
                    }                
                    break;
        
                case Disappear:
                    mTutorAlpha -= 30;
            
                    if (mTutorAlpha < 0) {
                        if (!mTutors.empty()) {
                            mTutors.pop();
                        }

                        if (!mTutors.isEmpty()) {
                            int nextTutorId = mTutors.pop();
                        
                            switch(nextTutorId) {
                                case Tutor_Goal: showTutorGoal(); break;
                                case Tutor_MenuUndo: showTutorMenuUndo(); break;
                                case Tutor_MenuHint: showTutorMenuHint(); break;
                                case Tutor_MenuSolvers: showTutorMenuSolvers(); break;
                            }
                        } else {
                            mTutorActive = false;
                            mTutorState = TutorStateEnum.Done;
                        }
                    }                
                    break;
                
                case Done:
                    break;
            }
        }
    
	    if (mState == HUDStateEnum.Disappear) {
		    final int icon_alpha_speed = -40;
		    final float text_anim_speed = -10.0f * Game.graphics.deviceScale;
            final float text_anim_speed_motto = -10.0f * Game.graphics.deviceScale;
		
		    mIconsAlpha += icon_alpha_speed;

		    int done_counter = 0;
		
		    if (mIconsAlpha < 0) {
			    mIconsAlpha = 0;
			    ++done_counter;
		    }

		    mPosXTextLeft += text_anim_speed;
		
		    if (mPosXTextLeft < mPosXTextLeftStart) {
			    mPosXTextLeft = mPosXTextLeftStart;
			    ++done_counter;
		    }
		
		    mPosYMotto += text_anim_speed_motto;
		
		    if (mPosYMotto < mPosYMottoStart) {
			    mPosYMotto = mPosYMottoStart;
			    ++done_counter;
		    }
		
		    mPosXTextRight -= text_anim_speed;
		
		    if (mPosXTextRight > mPosXTextRightStart) {
			    mPosXTextRight = mPosXTextRightStart;
			    ++done_counter;
		    }        
		
		    if (4 == done_counter) {
			    mState = HUDStateEnum.Done;
            }
	    }

	    if (HUDStateEnum.Appear == mState) {
		    int done_counter = 0;
		
            final int icon_alpha_speed = 20;
		    final float text_anim_speed = 8.0f * Game.graphics.deviceScale;
            final float text_anim_speed_motto = 3.0f * Game.graphics.deviceScale;
				
            mIconsAlpha += icon_alpha_speed;
		
		    if (mIconsAlpha > 255) {
			    mIconsAlpha = 255;
			    ++done_counter;
		    }
        
		    mPosXTextLeft += text_anim_speed;
		
		    if (mPosXTextLeft > mPosXTextLeftEnd) {
			    mPosXTextLeft = mPosXTextLeftEnd;
			    ++done_counter;
		    }
						
		    mPosYMotto += text_anim_speed_motto;
		
		    if (mPosYMotto > mPosYMottoEnd) {
			    mPosYMotto = mPosYMottoEnd;
			    ++done_counter;
		    }
		
		    mPosXTextRight -= text_anim_speed;
		
		    if (mPosXTextRight < mPosXTextRightEnd) {
			    mPosXTextRight = mPosXTextRightEnd;
			    ++done_counter;
		    }
		
		    if (4 == done_counter) {
			    mState = HUDStateEnum.Done;
            }
	    }
	
	    if (mShowHint) {
            mElapsed += 0.03f;
        
            if (mElapsed > 1.0f) {
                mElapsed = 1.0f;
			    mShowHint = false;
		    }
	    }
    
        if (mShowPrepareSolving) {
            if (mShowAppear) {
                mCenterAlpha += 0.05f;
            
                if (mCenterAlpha >= 1.0f) {
                    mCenterAlpha = 1.0f;
                }
            } else {
                mCenterAlpha -= 0.1f;
            
                if (mCenterAlpha <= 0.0f) {
                    mCenterAlpha = 0.0f;
                    mShowPrepareSolving = false;
                }
            }
        }
    }

    void renderForPicking() {
        Graphics graphics = Game.graphics;
	    float solver_quad_y = graphics.height * 0.09f;
	    float hint_quad_y   = graphics.height * 0.37f;
        float undo_quad_y   = graphics.height * 0.65f;
	    float pause_quad_y  = graphics.height * 0.93f;

	    solver_quad_y -= 0.1f * graphics.height;
	    hint_quad_y   -= 0.1f * graphics.height;
        undo_quad_y   -= 0.1f * graphics.height;
	    pause_quad_y  -= 0.1f * graphics.height;
    
        graphics.setProjection2D();
        graphics.setModelViewMatrix2D();
  	    graphics.bindStreamSources2d();
        graphics.resetBufferIndices();
        graphics.zeroBufferPositions();
    
        TexCoordsQuad tcoords = new TexCoordsQuad();
        tcoords.tx0 = new Vector2(0.0f, 0.0f);
        tcoords.tx1 = new Vector2(0.0f, 0.0f);
        tcoords.tx2 = new Vector2(0.0f, 0.0f);
        tcoords.tx3 = new Vector2(0.0f, 0.0f);

	    Color color_pause = new Color(0, 0, 200, 255);
        Color color_undo = new Color(0, 0, 150, 255);
        Color color_hint = new Color(0, 0, 100, 255);
	    Color color_solver = new Color(0, 0, 50, 255);
    
        float x = 5.0f * graphics.deviceScale;
        float size = 60.0f * graphics.deviceScale;

        graphics.addQuad(size, x, pause_quad_y,  tcoords, color_pause);
        graphics.addQuad(size, x, undo_quad_y,   tcoords, color_undo);
        graphics.addQuad(size, x, hint_quad_y,   tcoords, color_hint);
        graphics.addQuad(size, x, solver_quad_y, tcoords, color_solver);
        graphics.updateBuffers();
        glDisable(GL_TEXTURE_2D);
	    graphics.renderTriangles();
        glEnable(GL_TEXTURE_2D);
    }

    public void render() {
        Graphics graphics = Game.graphics;
        Color color = new Color();
        Vector2 vec = new Vector2();
        float scale;
	    float pause_quad_y  = graphics.height * 0.91f;
        float undo_quad_y   = graphics.height * 0.67f;
	    float hint_quad_y   = graphics.height * 0.38f;
	    float solver_quad_y = graphics.height * 0.1f;
        float shadow_offset_x = 1.5f * graphics.deviceScale;
        float shadow_offset_y = 1.5f * graphics.deviceScale;

        colorIcon.a = mIconsAlpha;
        colorText.a = mIconsAlpha;
        colorHilite.a = mIconsAlpha;
        colorShadow.a = mIconsAlpha;

        glEnable(GL_BLEND);
        glDisable(GL_LIGHTING);
        glDisable(GL_CULL_FACE);
        glDisable(GL_DEPTH_TEST);

        graphics.setProjection2D();
        graphics.setModelViewMatrix2D();
        graphics.zeroBufferPositions();
        graphics.resetBufferIndices();
        graphics.bindStreamSources2d();

        graphics.textureFontsClear.bind();

        if (mTextLevel.isVisible()) {
            vec.x = mPosXTextRight - shadow_offset_x;
            vec.y = (pause_quad_y + 9.0f * graphics.deviceScale) - shadow_offset_y;
            mTextLevel.emitt(vec, colorShadow);

            vec.x = mPosXTextRight;
            vec.y = pause_quad_y + 9.0f * graphics.deviceScale;
            mTextLevel.emitt(vec, colorText);
        }
    
        if (mTextStars.isVisible()) {
            vec.x = mPosXTextRight - shadow_offset_x;
            vec.y = (undo_quad_y - 12.5f * graphics.deviceScale) - shadow_offset_y;
            mTextStars.emitt(vec, colorShadow);

            vec.x = mPosXTextRight;
            vec.y = undo_quad_y - 12.5f * graphics.deviceScale;
            mTextStars.emitt(vec, colorText);
        }
    
        if (mTextMoves.isVisible()) {
            vec.x = mPosXTextRight - shadow_offset_x;
            vec.y = (hint_quad_y - 10.0f * graphics.deviceScale) + shadow_offset_y;
            mTextMoves.emitt(vec, colorShadow);

            vec.x = mPosXTextRight;
            vec.y = hint_quad_y - 10.0f * graphics.deviceScale;
            mTextMoves.emitt(vec, colorText);
        }
    
        if (mTextMotto.isVisible()) {
            vec.x = (graphics.width - 5.0f * graphics.deviceScale) - shadow_offset_x;
            vec.y = mPosYMotto + shadow_offset_y;
            mTextMotto.emitt(vec, colorShadow);

            vec.x = graphics.width - 5.0f * graphics.deviceScale;
            vec.y = mPosYMotto;
            if (mFlashMotto) {
                mTextMotto.emitt(vec, Color.WHITE);
            } else {
                mTextMotto.emitt(vec, colorText);
            }
        }
    
        float yOffset = 14.0f * graphics.deviceScale;
        
        if (mTextPause.isVisible()) {
            vec.x = mPosXTextLeft + shadow_offset_x;
            vec.y = pause_quad_y - yOffset - shadow_offset_y;
            mTextPause.emitt(vec, colorShadow);

            vec.x = mPosXTextLeft;
            vec.y = pause_quad_y - yOffset;
            mTextPause.emitt(vec, mHilitePause ? colorHilite : colorText);
        }
        
        if (mTextUndo.isVisible()) {
            vec.x = mPosXTextLeft + shadow_offset_x;
            vec.y = undo_quad_y - yOffset - shadow_offset_y;
            mTextUndo.emitt(vec, colorShadow);

            vec.x = mPosXTextLeft;
            vec.y = undo_quad_y - yOffset;
            mTextUndo.emitt(vec, mHiliteUndo ? colorHilite : colorText);
        }
        
        if (mTextHint.isVisible()) {
            vec.x = mPosXTextLeft + shadow_offset_x;
            vec.y = hint_quad_y - yOffset + shadow_offset_y;
            mTextHint.emitt(vec, colorShadow);

            vec.x = mPosXTextLeft;
            vec.y = hint_quad_y - yOffset;
            mTextHint.emitt(vec, mHiliteHint ? colorHilite : colorText);
        }
        
        if (mTextSolver.isVisible()) {
            vec.x = mPosXTextLeft + shadow_offset_x;
            vec.y = solver_quad_y - yOffset + shadow_offset_y;
            mTextSolver.emitt(vec, colorShadow);

            vec.x = mPosXTextLeft;
            vec.y = solver_quad_y - yOffset;
            mTextSolver.emitt(vec, mHiliteSolver ? colorHilite : colorText);
        }

        graphics.updateBuffers();
        graphics.renderTriangles();

        if (mShowPrepareSolving) {
            int a = (int) mCenterAlpha * 255;
        
            glDisable(GL_TEXTURE_2D);
            color.init(30, 30, 15, (int)(150 * mCenterAlpha));
        
            graphics.bindStreamSources2dNoTextures();
            graphics.resetBufferIndices();
            graphics.addQuad(0.0f, graphics.halfHeight - 20.0f * graphics.deviceScale, graphics.width, 75.0f * graphics.deviceScale, color);
            graphics.updateBuffers();
            graphics.renderTriangles();

            graphics.setProjection2D();
            graphics.setModelViewMatrix2D();
            graphics.zeroBufferPositions();
            graphics.resetBufferIndices();
            graphics.bindStreamSources2d();

            glEnable(GL_TEXTURE_2D);
            graphics.textureFontsBig.bind();
        
            scale = 0.75f * graphics.deviceScale;
            mArTextCenter[0].setScale(scale, scale);
            mArTextCenter[1].setScale(scale, scale);

		    color.init(0, 0, 0, a);

            vec.x = graphics.halfWidth - mArTextCenter[0].getHalfWidth();
            vec.y = graphics.halfHeight + mArTextCenter[0].getHalfHeight();
            mArTextCenter[0].emitt(vec, color);

            vec.x = graphics.halfWidth - mArTextCenter[1].getHalfWidth();
            vec.y = graphics.halfHeight - mArTextCenter[1].getHalfHeight();
            mArTextCenter[1].emitt(vec, color);

            glPushMatrix();
            glTranslatef(graphics.deviceScale, graphics.deviceScale, 0.0f);
            graphics.updateBuffers();
            graphics.renderTriangles();
            glPopMatrix();
    
		    color.init(255, 255, 0, a);

            vec.x = graphics.halfWidth - mArTextCenter[0].getHalfWidth();
            vec.y = graphics.halfHeight + mArTextCenter[0].getHalfHeight();
            mArTextCenter[0].emitt(vec, color);

            vec.x = graphics.halfWidth - mArTextCenter[1].getHalfWidth();
            vec.y = graphics.halfHeight - mArTextCenter[1].getHalfHeight();
            mArTextCenter[1].emitt(vec, color);

            graphics.updateBuffers();
            graphics.renderTriangles();
        }

        graphics.textureSymbols.bind();

        final float ics = 25.0f * graphics.deviceScale;
    
        graphics.resetBufferIndices();


        tcoords.tx0 = mSymbolPause.tx_lo_left;
        tcoords.tx1 = mSymbolPause.tx_lo_right;
        tcoords.tx2 = mSymbolPause.tx_up_right;
        tcoords.tx3 = mSymbolPause.tx_up_left;
        graphics.addQuad(ics, 5.0f * graphics.deviceScale + shadow_offset_x,  pause_quad_y - shadow_offset_y, tcoords, colorShadow);
        graphics.addQuad(ics, 5.0f * graphics.deviceScale,                    pause_quad_y,                   tcoords, mHilitePause ? colorHilite : colorIcon);
    
        tcoords.tx0 = mSymbolUndo.tx_lo_left;
        tcoords.tx1 = mSymbolUndo.tx_lo_right;
        tcoords.tx2 = mSymbolUndo.tx_up_right;
        tcoords.tx3 = mSymbolUndo.tx_up_left;
        graphics.addQuad(ics, 5.0f * graphics.deviceScale + shadow_offset_x,  undo_quad_y - shadow_offset_y,  tcoords, colorShadow);
        graphics.addQuad(ics, 5.0f * graphics.deviceScale,                    undo_quad_y,                    tcoords, mHiliteUndo ? colorHilite : colorIcon);
	
        tcoords.tx0 = mSymbolHint.tx_lo_left;
        tcoords.tx1 = mSymbolHint.tx_lo_right;
        tcoords.tx2 = mSymbolHint.tx_up_right;
        tcoords.tx3 = mSymbolHint.tx_up_left;
        graphics.addQuad(ics, 5.0f * graphics.deviceScale + shadow_offset_x,  hint_quad_y + shadow_offset_y,  tcoords, colorShadow);
        graphics.addQuad(ics, 5.0f * graphics.deviceScale,                   hint_quad_y,                    tcoords, mHiliteHint ? colorHilite : colorIcon);
    
        tcoords.tx0 = mSymbolSolver.tx_lo_left;
        tcoords.tx1 = mSymbolSolver.tx_lo_right;
        tcoords.tx2 = mSymbolSolver.tx_up_right;
        tcoords.tx3 = mSymbolSolver.tx_up_left;
        graphics.addQuad(ics, 5.0f * graphics.deviceScale + shadow_offset_x,  solver_quad_y + shadow_offset_y, tcoords, colorShadow);
        graphics.addQuad(ics, 5.0f * graphics.deviceScale, solver_quad_y, tcoords, mHiliteSolver ? colorHilite : colorIcon);

        tcoords.tx0 = mSymbolStar.tx_lo_left;
        tcoords.tx1 = mSymbolStar.tx_lo_right;
        tcoords.tx2 = mSymbolStar.tx_up_right;
        tcoords.tx3 = mSymbolStar.tx_up_left;
        graphics.addQuad(ics, graphics.width - 25.0f * graphics.deviceScale - shadow_offset_x, undo_quad_y - shadow_offset_y, tcoords, colorShadow);
        graphics.addQuad(ics, graphics.width - 25.0f * graphics.deviceScale, undo_quad_y, tcoords, colorIcon);

	    graphics.bindStreamSources2d();
        graphics.updateBuffers();
	    graphics.renderTriangles();

        if (mTutorActive) {
            mTutorTexture.bind();

            tcoords.tx0 = new Vector2(0,1);
            tcoords.tx1 = new Vector2(1,1);
            tcoords.tx2 = new Vector2(1,0);
            tcoords.tx3 = new Vector2(0,0);

            color.init(255, 255, 255, mTutorAlpha);
            float sz = 160.0f * graphics.deviceScale;
            float x = graphics.halfWidth - sz / 2.0f;
            float y = graphics.halfHeight - sz / 2.0f;

            graphics.resetBufferIndices();
            graphics.addQuad(sz, x, y, tcoords, color);
            graphics.updateBuffers();
            graphics.renderTriangles();
        }
    
        if (Level.State.DeadAnim == Game.level.mState) {
            graphics.resetBufferIndices();
        
		    color.init(200, 0, 0, mDeadAnim.alpha);
		
            tcoords.tx0 = mSymbolDeath.tx_lo_left;
            tcoords.tx1 = mSymbolDeath.tx_lo_right;
            tcoords.tx2 = mSymbolDeath.tx_up_right;
            tcoords.tx3 = mSymbolDeath.tx_up_left;
        
            float size = mDeadAnim.size * graphics.deviceScale;
            graphics.addQuad(size, graphics.halfWidth - (size / 2.0f), graphics.halfHeight - (size / 2.0f), tcoords, color);
        
            graphics.bindStreamSources2d();
            graphics.updateBuffers();
            graphics.renderTriangles();
        }

        glDisable(GL_TEXTURE_2D);

	    if (mShowHint) {
            vec.x = Utils.lerp(mStart.x, mEnd.x, mElapsed);
            vec.y = Utils.lerp(mStart.y, mEnd.y, mElapsed);

		    final float verts[] = {
			    mPosCenter.x + mStart.x, mPosCenter.y + mStart.y,
			    mPosCenter.x + mEnd.x,  mPosCenter.y + mEnd.y,
		    };

            byte maxColor = (byte)255;
		    final byte colors[] = {
			    maxColor,  maxColor,  0, maxColor,
			    maxColor,  maxColor,  0, maxColor,
		    };

            final float norms[] = {};
            final float coords[] = {};

            graphics.zeroBufferPositions();
            graphics.bindStreamSources2dNoTextures();
            graphics.addVerticesCoordsNormalsColors(verts, coords, norms, colors);

		    glLineWidth(2.0f * graphics.deviceScale);
            glEnable(GL_LINE_SMOOTH);
		    glDrawArrays(GL_LINES, 0, 2);
            glDisable(GL_LINE_SMOOTH);
            glLineWidth(1.0f);

            color.init(255, 255, 0, 255);
            graphics.drawCircleAt(mPosCenter.x + vec.x, mPosCenter.y + vec.y, mRadius * 0.5f, color);

            color.init(255, 0, 0, 255);
            graphics.drawCircleAt(mPosCenter.x + vec.x, mPosCenter.y + vec.y, mRadius * 0.25f, color);
        }
        
        glDisable(GL_BLEND);
        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
    }

    void setHilitePause(boolean hilite) {
        mHilitePause = hilite;
    }
    void setHiliteUndo(boolean hilite) {
        mHiliteUndo = hilite;
    }
    void setHiliteHint(boolean hilite) {
        mHiliteHint = hilite;
    }
    void setHiliteSolver(boolean hilite) {
        mHiliteSolver = hilite;
    }
    boolean isAnythingHilited() {
        return (mHilitePause || mHiliteUndo || mHiliteHint || mHiliteSolver);
    }
    boolean isTutorDisplaying() {
        return mTutorActive;
    }
    HUDStateEnum getState() {
        return mState;
    }
    boolean getShowHint() {
        return mShowHint;
    }        
}
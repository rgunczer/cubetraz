package com.almagems.cubetraz;

import static android.opengl.GLES10.*;

import java.util.Stack;

import static com.almagems.cubetraz.Constants.*;

public final class HUD {

    public static Graphics graphics;

    private final boolean[] m_arr_tutor_displayed = new boolean[MAX_TUTOR_COUNT];
    
	private HUDStateEnum m_state;
	
    private TexturedQuad m_symbol_pause;
    private TexturedQuad m_symbol_undo;
    private TexturedQuad m_symbol_hint;
    private TexturedQuad m_symbol_solver;
    private TexturedQuad m_symbol_star;
    private TexturedQuad m_symbol_death;

    private Text m_text_motto = new Text();
    
    private Text m_text_pause = new Text();
    private Text m_text_undo = new Text();
    private Text m_text_hint = new Text();
    private Text m_text_solver = new Text();
    
    private Text m_text_level = new Text();
    private Text m_text_moves = new Text();
    private Text m_text_stars = new Text();
    
    private Text[] m_ar_text_center = new Text[2];
    
    private TextDisplay m_text_display;
	
	private float m_pos_x_text_left;
	private float m_pos_x_text_left_start;
	private float m_pos_x_text_left_end;

	private float m_motto_height;

    private float m_pos_x_text_right;
    private float m_pos_x_text_right_start;
    private float m_pos_x_text_right_end;
	
	private float m_pos_y_motto;
	private float m_pos_y_motto_start;
	private float m_pos_y_motto_end;
	
	private int m_icons_alpha;

    private boolean m_hilite_pause;
    private boolean m_hilite_undo;
    private boolean m_hilite_hint;
    private boolean m_hilite_solver;

	private boolean m_show_hint;
	private int m_hint_type;
	private float m_hint_timeout;
    private float m_t;
	private int m_hint_index;
    
    private boolean m_show_prepare_solving;
    private boolean m_show_appear;
    private float m_center_alpha;
    
    private Vector2 m_pos_center;
    private float m_radius;
    private Vector2 m_start;
    private Vector2 m_end;
    
    private Stack<Integer> m_tutors = new Stack<>();
    private TutorStateEnum m_tutor_state;
    private boolean m_tutor_active;
    private int m_tutor_alpha;
    private int m_tutor_texture_id;
    
    
    // ctor
    public HUD() {
        // setdisplay
        m_text_motto.setDisplay(m_text_display);
    
        m_text_pause.setDisplay(m_text_display);
        m_text_undo.setDisplay(m_text_display);
        m_text_hint.setDisplay(m_text_display);
        m_text_solver.setDisplay(m_text_display);
    
        m_text_level.setDisplay(m_text_display);
        m_text_moves.setDisplay(m_text_display);
        m_text_stars.setDisplay(m_text_display);

        m_ar_text_center[0].setDisplay(m_text_display);
        m_ar_text_center[1].setDisplay(m_text_display);
    
        // align
        m_text_level.setAlign(TextAlignEnum.RightAlign);
        m_text_stars.setAlign(TextAlignEnum.RightAlign);
        m_text_moves.setAlign(TextAlignEnum.RightAlign);
        m_text_motto.setAlign(TextAlignEnum.RightAlign);
        
        // setscale
        m_text_pause.setScale(1.0f, 1.0f);
        m_text_undo.setScale(1.0f, 1.0f);
        m_text_hint.setScale(1.0f, 1.0f);
        m_text_solver.setScale(1.0f, 1.0f);
    
        m_text_level.setScale(1.0f, 1.0f);
        m_text_stars.setScale(1.0f, 1.0f);
        m_text_moves.setScale(1.0f, 1.0f);
        m_text_motto.setScale(1.0f, 1.0f);
    
        m_ar_text_center[0].setScale(1.0f, 1.0f);
        m_ar_text_center[1].setScale(1.0f, 1.0f);
    
        // vspace
        m_text_pause.setVSPace(0.75f);
        m_text_undo.setVSPace(0.75f);
        m_text_hint.setVSPace(0.75f);
        m_text_solver.setVSPace(0.75f);
    
        m_text_level.setVSPace(0.75f);
        m_text_stars.setVSPace(0.75f);
        m_text_moves.setVSPace(0.75f);
        m_text_motto.setVSPace(0.75f);
    
        m_ar_text_center[0].setVSPace(0.8f);
        m_ar_text_center[1].setVSPace(0.8f);
    
        m_hilite_pause = m_hilite_undo = m_hilite_hint = m_hilite_solver = false;
    
        for (int i = 0; i < MAX_TUTOR_COUNT; ++i) {        
            m_arr_tutor_displayed[i] = false;
        }
    }

    public void clearTutors() {
        while ( !m_tutors.isEmpty() ) {
            m_tutors.pop();
        }
    }

    public void showTutorSwipeAndGoal() {
        if (!m_arr_tutor_displayed[Tutor_Swipe]) {
            m_tutors.push(Tutor_Goal);
            m_tutors.push(Tutor_Swipe);
            m_tutor_active = true;
            m_tutor_alpha = 0;
            m_tutor_state = TutorStateEnum.TutorAppear;
            m_tutor_texture_id = Game.loadTexture("tutor_swipe");
            m_arr_tutor_displayed[Tutor_Swipe] = true;
        }
    }

    public void showTutorGoal() {
        if (!m_arr_tutor_displayed[Tutor_Goal]) {
            m_tutor_active = true;
            m_tutor_alpha = 0;
            m_tutor_state = TutorStateEnum.TutorAppear;
            m_tutor_texture_id = Game.loadTexture("tutor_goal");
            m_arr_tutor_displayed[Tutor_Goal] = true;
        }
    }

    public void showTutorDrag() {
        if (!m_arr_tutor_displayed[Tutor_Drag]) {
            m_tutors.push(Tutor_Drag);
            m_tutor_active = true;
            m_tutor_alpha = 0;
            m_tutor_state = TutorStateEnum.TutorAppear;
            m_tutor_texture_id = Game.loadTexture("tutor_drag");
            m_arr_tutor_displayed[Tutor_Drag] = true;
        }
    }

    public void showTutorDead() {
        if (!m_arr_tutor_displayed[Tutor_Dead]) {
            m_tutors.push(Tutor_Dead);
            m_tutor_active = true;
            m_tutor_alpha = 0;
            m_tutor_state = TutorStateEnum.TutorAppear;
            m_tutor_texture_id = Game.loadTexture("tutor_dead");
            m_arr_tutor_displayed[Tutor_Dead] = true;
        }
    }

    public void showTutorMoving() {
        if (!m_arr_tutor_displayed[Tutor_Moving]) {
            m_tutors.push(Tutor_Moving);
            m_tutor_active = true;
            m_tutor_alpha = 0;
            m_tutor_state = TutorStateEnum.TutorAppear;
            m_tutor_texture_id = Game.loadTexture("tutor_moving");
            m_arr_tutor_displayed[Tutor_Moving] = true;
        }
    }

    public void showTutorMover() {
        if (!m_arr_tutor_displayed[Tutor_Mover]) {
            m_tutors.push(Tutor_Mover);
            m_tutor_active = true;
            m_tutor_alpha = 0;
            m_tutor_state = TutorStateEnum.TutorAppear;
            m_tutor_texture_id = Game.loadTexture("tutor_pusher");
            m_arr_tutor_displayed[Tutor_Mover] = true;
        }
    }

    public void showTutorPlain() {
        if (!m_arr_tutor_displayed[Tutor_Plain]) {
            m_tutors.push(Tutor_Plain);
            m_tutor_active = true;
            m_tutor_alpha = 0;
            m_tutor_state = TutorStateEnum.TutorAppear;
            m_tutor_texture_id = Game.loadTexture("tutor_plain");
            m_arr_tutor_displayed[Tutor_Plain] = true;
        }
    }

    public void showTutorMenuPause() {
        if (!m_arr_tutor_displayed[Tutor_MenuPause]) {
            m_tutors.push(Tutor_MenuUndo);
            m_tutors.push(Tutor_MenuPause);
            m_tutor_active = true;
            m_tutor_alpha = 0;
            m_tutor_state = TutorStateEnum.TutorAppear;
            m_tutor_texture_id = Game.loadTexture("tutor_menu_pause");
            m_arr_tutor_displayed[Tutor_MenuPause] = true;
            //printf("\ntutors %lu", m_tutors.size());
        }
    }

    public void showTutorMenuUndo() {
        if (!m_arr_tutor_displayed[Tutor_MenuUndo]) {
            m_tutor_active = true;
            m_tutor_alpha = 0;
            m_tutor_state = TutorStateEnum.TutorAppear;
            m_tutor_texture_id = Game.loadTexture("tutor_menu_undo");
            m_arr_tutor_displayed[Tutor_MenuUndo] = true;
        }
    }

    public void showTutorMenuHint() {
        if (!m_arr_tutor_displayed[Tutor_MenuHint]) {
            m_tutors.push(Tutor_MenuSolvers);
            m_tutors.push(Tutor_MenuHint);
            m_tutor_active = true;
            m_tutor_alpha = 0;
            m_tutor_state = TutorStateEnum.TutorAppear;
            m_tutor_texture_id = Game.loadTexture("tutor_menu_hint");
            m_arr_tutor_displayed[Tutor_MenuHint] = true;
        }
    }

    public void showTutorMenuSolvers() {
        if (!m_arr_tutor_displayed[Tutor_MenuSolvers]) {
            m_tutor_active = true;
            m_tutor_alpha = 0;
            m_tutor_state = TutorStateEnum.TutorAppear;
            m_tutor_texture_id = Game.loadTexture("tutor_menu_solver");
            m_arr_tutor_displayed[Tutor_MenuSolvers] = true;
        }
    }

    public void hideTutor() {
        if (m_tutor_active && TutorStateEnum.TutorDone == m_tutor_state) {
            m_tutor_state = TutorStateEnum.TutorDisappear;
        }
    }

    public void setTextMotto(final String str) {
        m_text_motto.init(str, true);
	    m_motto_height = m_text_motto.getHeight();
    }

    public void setTextLevel(final String str) {
        String buf = "LEVEL\n" + str;
        m_text_level.init(buf, true);
    }

    public void setTextMoves(final int moves_count) {
        String buf = "MOVES\n" + moves_count;
        m_text_moves.init(buf, true);
    }

    public void setTextStars(final int stars) {
        String buf = "STARS\n" + stars;
        m_text_stars.init(buf, true);
    }

    public void set1stHint() {
	    m_text_hint.init("HINT\nTO\nBEGIN", true);
	    m_hint_index = 1;
    }

    public void set2ndHint() {
	    m_text_hint.init("HINT\nTO\nPATH", true);
	    m_hint_index = 2;
    }

    public void setTextUndo(final int count) {
//        char str[32];
//        sprintf(str, "UNDO\n%d", count);    
//        m_text_undo.Init(str, true);
    }

    public void setTextSolver(final int count) {
        String str = "SOLVERS\n" + count;
        m_text_solver.init(str, true);
    }

    public void showPrepareSolving(boolean value, int moves_count) {
        if (m_show_prepare_solving == value) {
            return;
        }
    
        if (value) {
            m_show_prepare_solving = value;
        }
    
        String str = "IN " + moves_count + " MOVES";
        
        m_ar_text_center[0].init("THE SOLUTION", true);
        m_ar_text_center[1].init(str, true);
    
        m_center_alpha = (value ? 0.0f : 1.0f);
    
        m_show_appear = value;
    }

    public void setupAppear() {
	    m_state = HUDStateEnum.AppearHUD;
    
	    m_icons_alpha = 0;
        m_hilite_pause = m_hilite_undo = m_hilite_hint = m_hilite_solver = false;

        m_pos_x_text_left = m_pos_x_text_left_start;
        m_pos_x_text_right = m_pos_x_text_right_start;
	    m_pos_y_motto = m_pos_y_motto_start;

	    m_text_pause.setVisible(true);
	    m_text_undo.setVisible(true);
	    m_text_hint.setVisible(true);
	    m_text_solver.setVisible(true);
    	
	    m_text_level.setVisible(true);
	    m_text_stars.setVisible(true);
	    m_text_moves.setVisible(true);
	    m_text_motto.setVisible(true);
    }

    public void setupDisappear() {
	    m_state = HUDStateEnum.DisappearHUD;
	
	    m_icons_alpha = 255;
        m_hilite_pause = m_hilite_undo = m_hilite_hint = m_hilite_solver = false;

	    m_pos_x_text_left = m_pos_x_text_left_end;
        m_pos_x_text_right = m_pos_x_text_right_end;
        m_pos_y_motto = m_pos_y_motto_end;
    }

    public void showHint(int hint_type) {
        float degree = 0.0f;
        float rad;
    
	    m_show_hint = true;
	    m_hint_type = hint_type;
	    m_hint_timeout = 2.0f;
        m_t = 0.0f;
        m_radius = 10.0f * graphics.device_scale;
    
        m_start = new Vector2(0.0f, -40.0f * graphics.device_scale);
        m_end = new Vector2(0.0f, 40.0f * graphics.device_scale);

        switch (m_hint_type) {
            case AxisMovement_X_Plus:
                degree = -120.0f;
                rad = (float)Math.toRadians(degree);
                m_start.rotateRad(rad); // =  engine.Rotate(m_start, degree);
                m_end.rotateRad(rad); //  = engine.Rotate(m_end, degree);
                break;
            
            case AxisMovement_X_Minus:
                degree = 60.0f;
                rad = (float)Math.toRadians(degree);
                m_start.rotateRad(rad); //= engine.Rotate(m_start, degree);
                m_end.rotateRad(rad); // = engine.Rotate(m_end, degree);
                break;
            
            case AxisMovement_Y_Plus:
                rad = (float)Math.toRadians(degree);
                m_start.rotateRad(rad); // = engine.Rotate(m_start, degree);
                m_end.rotateRad(rad); //  = engine.Rotate(m_end, degree);
                break;
            
            case AxisMovement_Y_Minus:
                degree = 180.0f;
                rad = (float)Math.toRadians(degree);
                m_start.rotateRad(rad); // = engine.Rotate(m_start, degree);
                m_end.rotateRad(rad); //  = engine.Rotate(m_end, degree);
                break;
            
            case AxisMovement_Z_Plus:
                degree = 120.0f;
                rad = (float)Math.toRadians(degree);
                m_start.rotateRad(rad); // = engine.Rotate(m_start, degree);
                m_end.rotateRad(rad); // = engine.Rotate(m_end, degree);
                break;
            
            case AxisMovement_Z_Minus:
                degree = -60.0f;
                rad = (float)Math.toRadians(degree);
                m_start.rotateRad(rad); // = engine.Rotate(m_start, degree);
                m_end.rotateRad(rad); //  = engine.Rotate(m_end, degree);
                break;
            
            default:
                break;
        }
    }

    public void init() {
        m_tutor_active = false;
    
        m_pos_center = new Vector2(graphics.half_width, graphics.half_height);
    
	    m_hint_index = 1;
        m_show_prepare_solving = false;
    
        m_hilite_pause = m_hilite_undo = m_hilite_hint = m_hilite_solver = false;
	
        m_text_pause.init("PAUSE", true);
        m_text_undo.init("UNDO\nLAST\nMOVE", true);
        m_text_hint.init("HINT\nFIRST\nMOVE", true);
    
        String str = "SOLVERS\n" + Game.getSolverCount();
        m_text_solver.init(str, true);
    
        m_text_level.init("LEVEL\nEASY-01", true);
        m_text_stars.init("STARS\n0", true);
        m_text_moves.init("MOVES\n0", true);
        m_text_motto.init("MOTTO\n0\n0", true);
    
        float scale = 0.3f * graphics.device_scale;
        m_text_level.setScale(scale, scale);
        m_text_stars.setScale(scale, scale);
        m_text_moves.setScale(scale, scale);
        m_text_motto.setScale(scale, scale);
    
        m_text_pause.setScale(scale, scale);
        m_text_undo.setScale(scale, scale);
        m_text_hint.setScale(scale, scale);
        m_text_solver.setScale(scale, scale);
    
        m_ar_text_center[0].init("THE SOLUTION IS...", true);
        m_ar_text_center[1].init("THE SOLUTION IS...", true);
    
        m_symbol_pause = Game.getSymbol(SymbolPause);
        m_symbol_undo = Game.getSymbol(SymbolUndo);
        m_symbol_hint = Game.getSymbol(SymbolQuestionmark);
        m_symbol_solver = Game.getSymbol(SymbolSolver);
        m_symbol_star = Game.getSymbol(SymbolStar);
	    m_symbol_death = Game.getSymbol(SymbolDeath);
    
	    m_pos_x_text_left_start = -100.0f * graphics.device_scale;
	    m_pos_x_text_left_end = 5.0f * graphics.device_scale;

	    m_pos_y_motto_start = -10.0f * graphics.device_scale;
	    m_pos_y_motto_end = 30.0f * graphics.device_scale;
	
        m_pos_x_text_right_start = graphics.width + (75.0f * graphics.device_scale);
	    m_pos_x_text_right_end = graphics.width - (6.0f * graphics.device_scale);

	    m_state = HUDStateEnum.DoneHUD;
	    m_show_hint = false;
    }

    public void update() {
        if (m_tutor_active) {
            switch(m_tutor_state) {
                case TutorAppear:
                    m_tutor_alpha += 10;
            
                    if (m_tutor_alpha > 255) {
                        m_tutor_alpha = 255;
                        m_tutor_state = TutorStateEnum.TutorDone;
                    }                
                    break;
        
                case TutorDisappear:
                    m_tutor_alpha -= 30;
            
                    if (m_tutor_alpha < 0) {
                        //printf("\ntutors: %lu", m_tutors.size());
                                        
                        if (!m_tutors.empty()) {
                            m_tutors.pop();
                        }

                        //printf("\ntutors: %lu", m_tutors.size());
                    
                        if (!m_tutors.isEmpty()) {
                            int tutor_id = m_tutors.pop();
                        
                            switch(tutor_id) {
                                case Tutor_Goal: showTutorGoal(); break;
                                case Tutor_MenuUndo: showTutorMenuUndo(); break;
                                case Tutor_MenuHint: showTutorMenuHint(); break;
                                case Tutor_MenuSolvers: showTutorMenuSolvers(); break;
                            }
                        } else {
                            m_tutor_active = false;
                            m_tutor_state = TutorStateEnum.TutorDone;
                        }
                    }                
                    break;
                
                case TutorDone:
                    break;
            }
        }
    
	    if (m_state == HUDStateEnum.DisappearHUD) {
		    final int icon_alpha_speed = -40;
		    final float text_anim_speed = -10.0f * graphics.device_scale;
            final float text_anim_speed_motto = -10.0f * graphics.device_scale;
		
		    m_icons_alpha += icon_alpha_speed;

		    int done_counter = 0;
		
		    if (m_icons_alpha < 0) {
			    m_icons_alpha = 0;
			    ++done_counter;
		    }

		    m_pos_x_text_left += text_anim_speed;
		
		    if (m_pos_x_text_left < m_pos_x_text_left_start) {
			    m_pos_x_text_left = m_pos_x_text_left_start;
			    ++done_counter;
		    }
		
		    m_pos_y_motto += text_anim_speed_motto;
		
		    if (m_pos_y_motto < m_pos_y_motto_start) {
			    m_pos_y_motto = m_pos_y_motto_start;
			    ++done_counter;
		    }
		
		    m_pos_x_text_right -= text_anim_speed;
		
		    if (m_pos_x_text_right > m_pos_x_text_right_start) {
			    m_pos_x_text_right = m_pos_x_text_right_start;
			    ++done_counter;
		    }        
		
		    if (4 == done_counter) {
			    m_state = HUDStateEnum.DoneHUD;
            }
	    }

	    if (HUDStateEnum.AppearHUD == m_state) {
		    int done_counter = 0;
		
            final int icon_alpha_speed = 20;
		    final float text_anim_speed = 8.0f * graphics.device_scale;
            final float text_anim_speed_motto = 3.0f * graphics.device_scale;
				
            m_icons_alpha += icon_alpha_speed;
		
		    if (m_icons_alpha > 255) {
			    m_icons_alpha = 255;
			    ++done_counter;
		    }
        
		    m_pos_x_text_left += text_anim_speed;
		
		    if (m_pos_x_text_left > m_pos_x_text_left_end) {
			    m_pos_x_text_left = m_pos_x_text_left_end;
			    ++done_counter;
		    }
						
		    m_pos_y_motto += text_anim_speed_motto; 
		
		    if (m_pos_y_motto > m_pos_y_motto_end) {
			    m_pos_y_motto = m_pos_y_motto_end;
			    ++done_counter;
		    }
		
		    m_pos_x_text_right -= text_anim_speed;
		
		    if (m_pos_x_text_right < m_pos_x_text_right_end) {
			    m_pos_x_text_right = m_pos_x_text_right_end;
			    ++done_counter;
		    }
		
		    if (4 == done_counter) {
			    m_state = HUDStateEnum.DoneHUD;
            }
	    }
	
	    if (m_show_hint) {
            m_t += 0.03f;
        
            if (m_t > 1.0f) {
                m_t = 1.0f;
			    m_show_hint = false;
                m_hint_timeout = 0.0f;
		    }
	    }
    
        if (m_show_prepare_solving) {
            if (m_show_appear) {
                m_center_alpha += 0.05f;
            
                if (m_center_alpha >= 1.0f) {
                    m_center_alpha = 1.0f;
                }
            } else {
                m_center_alpha -= 0.1f;
            
                if (m_center_alpha <= 0.0f) {
                    m_center_alpha = 0.0f;
                    m_show_prepare_solving = false;
                }
            }
        }
    }

    public void renderForPicking() {
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

  	    graphics.setStreamSourceFloat2D();
    
        TexCoordsQuad tcoords = new TexCoordsQuad();
        tcoords.tx0 = new Vector2(0.0f, 0.0f);
        tcoords.tx1 = new Vector2(0.0f, 0.0f);
        tcoords.tx2 = new Vector2(0.0f, 0.0f);
        tcoords.tx3 = new Vector2(0.0f, 0.0f);

	    Color color_pause = new Color(0, 0, 200, 255);
        Color color_undo = new Color(0, 0, 150, 255);
        Color color_hint = new Color(0, 0, 100, 255);
	    Color color_solver = new Color(0, 0, 50, 255);
    
        float x = 5.0f * graphics.device_scale;
        float size = 60.0f * graphics.device_scale;
    
        graphics.prepare();
        graphics.addQuad(size, x, pause_quad_y,  tcoords, color_pause);
        graphics.addQuad(size, x, undo_quad_y,   tcoords, color_undo);
        graphics.addQuad(size, x, hint_quad_y,   tcoords, color_hint);
        graphics.addQuad(size, x, solver_quad_y, tcoords, color_solver);
	    graphics.renderTriangles();
    }

    public void render() {
//    RenderForPicking();
//    return;
    
        float scale;
	    float pause_quad_y  = graphics.height * 0.91f;
        float undo_quad_y   = graphics.height * 0.67f;
	    float hint_quad_y   = graphics.height * 0.38f;
	    float solver_quad_y = graphics.height * 0.1f;

        Color color_icon = new Color(139, 0, 0, m_icons_alpha);
        Color color_text = new Color(225,10,50,200);//220, 20, 60, 240);
        Color color_hilite = new Color(255, 228, 225, m_icons_alpha);
        Color color_shadow = new Color(0, 0, 0, m_icons_alpha / 2);
    
        float shadow_offset_x = 2.5f * graphics.device_scale;
        float shadow_offset_y = 2.5f * graphics.device_scale;
    
        glEnable(GL_BLEND);
        glDisable(GL_LIGHTING);
        glDisable(GL_CULL_FACE);
        glDisable(GL_DEPTH_TEST);

        graphics.setProjection2D();
        graphics.setModelViewMatrix2D();
    
        glBindTexture(GL_TEXTURE_2D, graphics.texture_id_fonts_clear);
    
        m_text_display.init();
        
        if (m_text_level.isVisible()) {
            m_text_level.emitt(m_pos_x_text_right - shadow_offset_x, (pause_quad_y + 9.0f * graphics.device_scale) - shadow_offset_y, color_shadow);
            m_text_display.m_vertex_count += m_text_level.getVertexCount();
        
            m_text_level.emitt(m_pos_x_text_right, pause_quad_y + 9.0f * graphics.device_scale, color_text);
            m_text_display.m_vertex_count += m_text_level.getVertexCount();
        }
    
        if (m_text_stars.isVisible()) {
            m_text_stars.emitt(m_pos_x_text_right - shadow_offset_x, (undo_quad_y - 12.5f * graphics.device_scale) - shadow_offset_y, color_shadow);
            m_text_display.m_vertex_count += m_text_stars.getVertexCount();
        
            m_text_stars.emitt(m_pos_x_text_right, undo_quad_y - 12.5f * graphics.device_scale, color_text);
            m_text_display.m_vertex_count += m_text_stars.getVertexCount();
        }
    
        if (m_text_moves.isVisible()) {
            m_text_moves.emitt(m_pos_x_text_right - shadow_offset_x, (hint_quad_y - 10.0f * graphics.device_scale) + shadow_offset_y, color_shadow);
            m_text_display.m_vertex_count += m_text_moves.getVertexCount();

            m_text_moves.emitt(m_pos_x_text_right, hint_quad_y - 10.0f * graphics.device_scale, color_text);
            m_text_display.m_vertex_count += m_text_moves.getVertexCount();
        }
    
        if (m_text_motto.isVisible()) {
            m_text_motto.emitt((graphics.width - 5.0f * graphics.device_scale) - shadow_offset_x, m_pos_y_motto + shadow_offset_y, color_shadow);
            m_text_display.m_vertex_count += m_text_motto.getVertexCount();
    
            m_text_motto.emitt(graphics.width - 5.0f * graphics.device_scale, m_pos_y_motto, color_text);
            m_text_display.m_vertex_count += m_text_motto.getVertexCount();
        }
    
        float yOffset = 14.0f * graphics.device_scale;
        
        if (m_text_pause.isVisible()) {
            m_text_pause.emitt(m_pos_x_text_left + shadow_offset_x, pause_quad_y - yOffset - shadow_offset_y,  color_shadow);
            m_text_display.m_vertex_count += m_text_pause.getVertexCount();

            m_text_pause.emitt(m_pos_x_text_left, pause_quad_y - yOffset, m_hilite_pause ? color_hilite : color_text);
            m_text_display.m_vertex_count += m_text_pause.getVertexCount();
        }
        
        if (m_text_undo.isVisible()) {
            m_text_undo.emitt(m_pos_x_text_left + shadow_offset_x, undo_quad_y - yOffset - shadow_offset_y, color_shadow);
            m_text_display.m_vertex_count += m_text_undo.getVertexCount();

            m_text_undo.emitt(m_pos_x_text_left, undo_quad_y - yOffset, m_hilite_undo ? color_hilite : color_text);
            m_text_display.m_vertex_count += m_text_undo.getVertexCount();
        }
        
        if (m_text_hint.isVisible()) {
            m_text_hint.emitt(m_pos_x_text_left + shadow_offset_x, hint_quad_y - yOffset + shadow_offset_y, color_shadow);
            m_text_display.m_vertex_count += m_text_hint.getVertexCount();

            m_text_hint.emitt(m_pos_x_text_left, hint_quad_y - yOffset, m_hilite_hint ? color_hilite : color_text);
            m_text_display.m_vertex_count += m_text_hint.getVertexCount();
        }
        
        if (m_text_solver.isVisible()) {
            m_text_solver.emitt(m_pos_x_text_left + shadow_offset_x, solver_quad_y - yOffset + shadow_offset_y, color_shadow);
            m_text_display.m_vertex_count += m_text_solver.getVertexCount();

            m_text_solver.emitt(m_pos_x_text_left, solver_quad_y - yOffset, m_hilite_solver ? color_hilite : color_text);
            m_text_display.m_vertex_count += m_text_solver.getVertexCount();
        }
    
        if (m_show_prepare_solving) {
            int a = (int)m_center_alpha * 255;
        
            glDisable(GL_TEXTURE_2D);
            Color color_bg = new Color(30, 30, 15, 150 * m_center_alpha);
        
            graphics.setStreamSourceFloat2DNoTexture();
            graphics.prepare();
            graphics.addQuad(0.0f, graphics.half_height - 20.0f * graphics.device_scale, graphics.width, 75.0f * graphics.device_scale, color_bg);
            graphics.renderTriangles();
        
            glEnable(GL_TEXTURE_2D);
            glBindTexture(GL_TEXTURE_2D, graphics.texture_id_fonts_big);
        
            scale = 0.75f * graphics.device_scale;
            m_ar_text_center[0].setScale(scale, scale);
            m_ar_text_center[1].setScale(scale, scale);
        
            //printf("\na: %d", a);
        
		    Color color = new Color(0, 0, 0, a);
        
            m_text_display.init();
            m_ar_text_center[0].emitt(graphics.half_width - m_ar_text_center[0].getHalfWidth(), graphics.half_height + m_ar_text_center[0].getHalfHeight(), color);
            m_text_display.m_vertex_count += m_ar_text_center[0].getVertexCount();
        
            m_ar_text_center[1].emitt(graphics.half_width - m_ar_text_center[1].getHalfWidth(), graphics.half_height - m_ar_text_center[1].getHalfHeight(), color);
            m_text_display.m_vertex_count += m_ar_text_center[1].getVertexCount();

            glPushMatrix();
                glTranslatef(graphics.device_scale, graphics.device_scale, 0.0f);
                m_text_display.render();
            glPopMatrix();
    
		    color = new Color(255, 255, 0, a);
            
            m_text_display.init();
            m_ar_text_center[0].emitt(graphics.half_width - m_ar_text_center[0].getHalfWidth(), graphics.half_height + m_ar_text_center[0].getHalfHeight(), color);
            m_text_display.m_vertex_count += m_ar_text_center[0].getVertexCount();
            
            m_ar_text_center[1].emitt(graphics.half_width - m_ar_text_center[1].getHalfWidth(), graphics.half_height - m_ar_text_center[1].getHalfHeight(), color);
            m_text_display.m_vertex_count += m_ar_text_center[1].getVertexCount();
        }
    
        m_text_display.render();
    
        glBindTexture(GL_TEXTURE_2D, graphics.texture_id_symbols);

        final float ics = 25.0f * graphics.device_scale;
    
        graphics.prepare();

        TexCoordsQuad tcoords = new TexCoordsQuad();
        tcoords.tx0 = m_symbol_pause.tx_lo_left;
        tcoords.tx1 = m_symbol_pause.tx_lo_right;
        tcoords.tx2 = m_symbol_pause.tx_up_right;
        tcoords.tx3 = m_symbol_pause.tx_up_left;
        graphics.addQuad(ics, 5.0f * graphics.device_scale + shadow_offset_x,  pause_quad_y - shadow_offset_y, tcoords, color_shadow);
        graphics.addQuad(ics, 5.0f * graphics.device_scale,                    pause_quad_y,                   tcoords, m_hilite_pause ? color_hilite : color_icon);
    
        tcoords.tx0 = m_symbol_undo.tx_lo_left;
        tcoords.tx1 = m_symbol_undo.tx_lo_right;
        tcoords.tx2 = m_symbol_undo.tx_up_right;
        tcoords.tx3 = m_symbol_undo.tx_up_left;
        graphics.addQuad(ics, 5.0f * graphics.device_scale + shadow_offset_x,  undo_quad_y - shadow_offset_y,  tcoords, color_shadow);
        graphics.addQuad(ics, 5.0f * graphics.device_scale,                    undo_quad_y,                    tcoords, m_hilite_undo ? color_hilite : color_icon);
	
        tcoords.tx0 = m_symbol_hint.tx_lo_left;
        tcoords.tx1 = m_symbol_hint.tx_lo_right;
        tcoords.tx2 = m_symbol_hint.tx_up_right;
        tcoords.tx3 = m_symbol_hint.tx_up_left;
        graphics.addQuad(ics, 5.0f * graphics.device_scale + shadow_offset_x,  hint_quad_y + shadow_offset_y,  tcoords, color_shadow);
        graphics.addQuad(ics, 5.0f * graphics.device_scale ,                   hint_quad_y,                    tcoords, m_hilite_hint ? color_hilite : color_icon);
    
        tcoords.tx0 = m_symbol_solver.tx_lo_left;
        tcoords.tx1 = m_symbol_solver.tx_lo_right;
        tcoords.tx2 = m_symbol_solver.tx_up_right;
        tcoords.tx3 = m_symbol_solver.tx_up_left;
        graphics.addQuad(ics, 5.0f * graphics.device_scale + shadow_offset_x,  solver_quad_y + shadow_offset_y, tcoords, color_shadow);
        graphics.addQuad(ics, 5.0f * graphics.device_scale, solver_quad_y, tcoords, m_hilite_solver ? color_hilite : color_icon);

        tcoords.tx0 = m_symbol_star.tx_lo_left;
        tcoords.tx1 = m_symbol_star.tx_lo_right;
        tcoords.tx2 = m_symbol_star.tx_up_right;
        tcoords.tx3 = m_symbol_star.tx_up_left;
        graphics.addQuad(ics, graphics.width - 25.0f * graphics.device_scale - shadow_offset_x, undo_quad_y - shadow_offset_y, tcoords, color_shadow);
        graphics.addQuad(ics, graphics.width - 25.0f * graphics.device_scale, undo_quad_y, tcoords, color_icon);
    
	    graphics.setStreamSourceFloat2D();
	    graphics.renderTriangles();

        if (m_tutor_active) {
            //if (1 == m_tutor_index)
            {
                glBindTexture(GL_TEXTURE_2D, m_tutor_texture_id);
            
                tcoords.tx0 = new Vector2(0,1);
                tcoords.tx1 = new Vector2(1,1);
                tcoords.tx2 = new Vector2(1,0);
                tcoords.tx3 = new Vector2(0,0);
            
                Color col = new Color(255, 255, 255, m_tutor_alpha);
                float sz = 160.0f * graphics.device_scale;
                float x = graphics.half_width - sz / 2.0f;
                float y = graphics.half_height - sz / 2.0f;
            
                graphics.prepare();
                graphics.addQuad(sz, x, y, tcoords, col);
                graphics.renderTriangles();
            }
        }
    
        if (Level.LevelStateEnum.DeadAnim == Game.level.m_state) {
            graphics.prepare();
        
		    Color color = new Color(200, 0, 0, Game.level.dead_alpha);
		
            tcoords.tx0 = m_symbol_death.tx_lo_left;
            tcoords.tx1 = m_symbol_death.tx_lo_right;
            tcoords.tx2 = m_symbol_death.tx_up_right;
            tcoords.tx3 = m_symbol_death.tx_up_left;
        
            float size = Game.level.dead_size * graphics.device_scale;
            graphics.addQuad(size, graphics.half_width - (size / 2.0f), graphics.half_height - (size / 2.0f), tcoords, color);
        
            graphics.setStreamSourceFloat2D();
            graphics.renderTriangles();
        }

        glDisable(GL_TEXTURE_2D);

	    if (m_show_hint) {
            Vector2 dir = new Vector2(Utils.lerp(m_start.x, m_end.x, m_t), Utils.lerp(m_start.y, m_end.y, m_t));

		    final float verts[] = {
			    m_pos_center.x + m_start.x, m_pos_center.y + m_start.y,
			    m_pos_center.x + m_end.x,  m_pos_center.y + m_end.y,
		    };
		
		    final short colors[] = {
			    255,  255,  0, 255,
			    255,  255,  0, 255,
		    };
		
//		    glVertexPointer(2, GL_FLOAT, 0, verts);
//		    glColorPointer(4, GL_UNSIGNED_BYTE, 0, colors);
//		    glLineWidth(2.0f * graphics.device_scale);
//            glEnable(GL_LINE_SMOOTH);
//		    glDrawArrays(GL_LINES, 0, 2);
//            glDisable(GL_LINE_SMOOTH);
//            glLineWidth(1.0f);
//
//            Color color_circle = new Color(255, 255, 0, 255);
//            graphics.drawCircleAt(m_pos_center.x + dir.x, m_pos_center.y + dir.y, m_radius * 0.5f, color_circle);
//
//            color_circle = new Color(255, 0, 0, 255);
//            graphics.drawCircleAt(m_pos_center.x + dir.x, m_pos_center.y + dir.y, m_radius * 0.25f, color_circle);
        }
        
        glDisable(GL_BLEND);
        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);

        graphics.setProjection3D();
    }

    public void setHilitePause(boolean hilite) {
        m_hilite_pause = hilite; 
    }
    
    public void setHiliteUndo(boolean hilite) {
        m_hilite_undo = hilite; 
    }
    
    public void setHiliteHint(boolean hilite) {
        m_hilite_hint = hilite; 
    }
    
    public void setHiliteSolver(boolean hilite) {
        m_hilite_solver = hilite; 
    }
    
    public boolean isAnythingHilited() { 
        return (m_hilite_pause || m_hilite_undo || m_hilite_hint || m_hilite_solver); 
    }
    
    public boolean isTutorDisplaying() { 
        return m_tutor_active; 
    }    
    
    public HUDStateEnum getState() {
        return m_state; 
    }
        
    public boolean getShowHint() {
        return m_show_hint; 
    }        
}
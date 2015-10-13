package com.almagems.cubetraz;


import java.util.ArrayList;

public final class Text {

    private static final int MAX_TEXT_LINES = 4;

    public enum TextAlignEnum {
        LeftAlign,
        CenterAlign,
        RightAlign
    }

    private String m_text;
    private ArrayList<TexturedQuad> m_ar_lines[MAX_TEXT_LINES];
    private float m_sx; // scale x
    private float m_sy; // scale y
    private int m_length;
    private int m_lines_count;
    private int m_vertex_count;
    private boolean m_use_big_fonts;
    private boolean m_visible;

    private TextAlignEnum m_align;

    private float m_vspace;

    private float m_width;
    private float m_half_width;

    private float m_height;
    private float m_half_height;

    private TextDisplay m_display;


    // ctor
    public Text() {
        m_use_big_fonts = false;
        m_vspace = 1.0f;
        m_lines_count = 0;
        m_length = 0;
        m_height = 0.0f;
        m_width = 0.0f;
        m_half_height = 0.0f;
        m_half_width = 0.0f;
        m_align = TextAlignEnum.LeftAlign;
        m_sx = 1.0f;
        m_sy = 1.0f;
    }

    public void cleanUp() {
        for (int i = 0; i < MAX_TEXT_LINES; ++i) {
            m_ar_lines[i].clear();
        }
    }

    public void init(final String text, boolean visible) {
        setVisible(visible);
        cleanUp();

        m_lines_count = 0;
        m_text = text;
        m_length = m_text.length();

        TexturedQuad pFont;
        char ch;
        for (int i = 0; i < m_length; ++i) {
            ch = m_text.charAt(i);

            if ('\n' == ch) {
                ++m_lines_count;
                continue;
            } else {
                pFont = Game.getFont(ch);
            }
            m_ar_lines[m_lines_count].add(pFont);
        }
        calcTextDimensions();
    }

    public void setScale(float sx, float sy) {
        m_sx = sx;
        m_sy = sy;
        calcTextDimensions();
    }

    public void setVSPace(float vspace) {
        m_vspace = vspace;
        calcTextDimensions();
    }

    public void calcTextDimensions() {
        float[] w = new float[MAX_TEXT_LINES];
        float[] h = new float[MAX_TEXT_LINES];

        for (int i = 0; i < MAX_TEXT_LINES; ++i) {
            w[i] = 0.0f;
            h[i] = 0.0f;
        }
/*
        TexturedQuad pFont;
        list<TexturedQuad*>::iterator it;

        for (int i = 0; i <= m_lines_count; ++i) {
            if (!m_ar_lines[i].isEmpty()) {
                pFont = m_ar_lines[i].front();
                h[i] = pFont->h;

                for (it = m_ar_lines[i].begin(); it != m_ar_lines[i].end(); ++it) {
                    pFont = *it;
                    w[i] += (pFont->w * m_vspace);
                }
            }
        }
*/
        m_width = 0.0f;
        m_height = 0.0f;

        for (int i = 0; i < MAX_TEXT_LINES; ++i) {
            if (m_width < w[i]) {
                m_width = w[i];
            }
            m_height += h[i];
        }

        m_width *= m_sx;
        m_height *= m_sy;

        m_half_width = m_width / 2.0f;
        m_half_height = m_height / 2.0f;
    }

    public void emitt(float x, float y, Color color) {
        Vector2 pos = new Vector2(x, y);
        emitt(m_display.m_verts, m_display.m_coords, m_display.m_colors,
                m_display.m_vindex, m_display.m_cindex, m_display.m_color_index,
                pos, color);
    }

    public void emitt(Vector2 pos, Color color) {
        emitt(m_display.m_verts, m_display.m_coords, m_display.m_colors,
                m_display.m_vindex, m_display.m_cindex, m_display.m_color_index,
                pos, color);
    }
/*
    public void emitt(float* verts, float* coords, GLubyte* colors,
                      int& vindex, int& cindex, int& color_index,
                      Vector2 pos, Color& color) {
        m_vertex_count = 0;

        list<TexturedQuad*>::iterator it;
        TexturedQuad* pFont;
        float x_start;
        float x;
        float y = pos.y;

        for (int i = 0; i <= m_lines_count; ++i)
        {
            x_start = pos.x;

            if (RightAlign == m_align)
            {
                float w = 0.0f;

                for (it = m_ar_lines[i].begin(); it != m_ar_lines[i].end(); ++it)
                {
                    pFont = *it;
                    w += (pFont->w * m_vspace) * m_sx;
                }

                x_start -= w;
            }

            x = x_start;
            //float x1 = x;

            for (it = m_ar_lines[i].begin(); it != m_ar_lines[i].end(); ++it)
            {
                pFont = *it;

                // verts
                verts[++vindex] = x;						verts[++vindex] = y;
                verts[++vindex] = x + pFont->w * m_sx;		verts[++vindex] = y;
                verts[++vindex] = x + pFont->w * m_sx;		verts[++vindex] = y + pFont->h * m_sy;

                verts[++vindex] = x + pFont->w * m_sx;		verts[++vindex] = y + pFont->h * m_sy;
                verts[++vindex] = x;						verts[++vindex] = y + pFont->h * m_sy;
                verts[++vindex] = x;						verts[++vindex] = y;

                // coords
                coords[++cindex] = pFont->tx_lo_left.x;		coords[++cindex] = pFont->tx_lo_left.y;		// 0
                coords[++cindex] = pFont->tx_lo_right.x;	coords[++cindex] = pFont->tx_lo_right.y;	// 1
                coords[++cindex] = pFont->tx_up_right.x;	coords[++cindex] = pFont->tx_up_right.y;	// 2

                coords[++cindex] = pFont->tx_up_right.x;	coords[++cindex] = pFont->tx_up_right.y;	// 2
                coords[++cindex] = pFont->tx_up_left.x;		coords[++cindex] = pFont->tx_up_left.y;		// 3
                coords[++cindex] = pFont->tx_lo_left.x;		coords[++cindex] = pFont->tx_lo_left.y;		// 0

                // color
                colors[++color_index] = color.r; colors[++color_index] = color.g; colors[++color_index] = color.b; colors[++color_index] = color.a;
                colors[++color_index] = color.r; colors[++color_index] = color.g; colors[++color_index] = color.b; colors[++color_index] = color.a;
                colors[++color_index] = color.r; colors[++color_index] = color.g; colors[++color_index] = color.b; colors[++color_index] = color.a;

                colors[++color_index] = color.r; colors[++color_index] = color.g; colors[++color_index] = color.b; colors[++color_index] = color.a;
                colors[++color_index] = color.r; colors[++color_index] = color.g; colors[++color_index] = color.b; colors[++color_index] = color.a;
                colors[++color_index] = color.r; colors[++color_index] = color.g; colors[++color_index] = color.b; colors[++color_index] = color.a;

                m_vertex_count += 6;

                x += (pFont->w * m_vspace) * m_sx;
            }

            pFont = m_ar_lines[i].front();

            y -= pFont->h * m_sy;
        }
    }
*/

    public void setUseBigFonts(boolean use) {
        m_use_big_fonts = use;
    }

    public void setDisplay(TextDisplay display) {
        m_display = display;
    }

    public void setAlign(TextAlignEnum align) {
        m_align = align;
    }

    public void setVisible(boolean visible) {
        m_visible = visible;
    }

    public boolean isVisible() {
        return m_visible;
    }

    public int getVertexCount() {
        return m_vertex_count;
    }

    public int getLength() {
        return m_length;
    }

    public float getWidth() {
        return m_width;
    }

    public float getHalfWidth() {
        return m_half_width ;
    }

    public float getHeight() {
        return m_height;
    }

    public float getHalfHeight() {
        return m_half_height;
    }

}

package com.almagems.cubetraz.graphics;

import com.almagems.cubetraz.Game;
import com.almagems.cubetraz.math.Vector2;

import java.util.ArrayList;
import static com.almagems.cubetraz.Game.*;


public final class Text {
    private String m_text;
    private ArrayList<ArrayList<TexturedQuad>> m_ar_lines = new ArrayList<>(MAX_TEXT_LINES);
    private Vector2 scale = new Vector2();
    private int m_length;
    private int m_lines_count;
    private boolean m_visible;

    private TextAlignEnum m_align;

    private float m_vspace;

    private float m_width;
    private float m_half_width;

    private float m_height;
    private float m_half_height;


    public Text() {
        m_vspace = 1.0f;
        m_lines_count = 0;
        m_length = 0;
        m_height = 0.0f;
        m_width = 0.0f;
        m_half_height = 0.0f;
        m_half_width = 0.0f;
        m_align = TextAlignEnum.LeftAlign;
        scale.x = 1.0f;
        scale.y = 1.0f;

        for(int i = 0; i < MAX_TEXT_LINES; ++i) {
            m_ar_lines.add(new ArrayList<TexturedQuad>());
        }
    }

    public void cleanUp() {
        for (int i = 0; i < MAX_TEXT_LINES; ++i) {
            m_ar_lines.get(i).clear();
        }
    }

    public void init(final String text) {
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
            m_ar_lines.get(m_lines_count).add(pFont);
        }
        calcTextDimensions();
    }

    public void setScale(float sx, float sy) {
        scale.x = sx;
        scale.y = sy;
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

        int size;
        TexturedQuad pFont;
        for (int i = 0; i <= m_lines_count; ++i) {
            if (!m_ar_lines.get(i).isEmpty()) {
                pFont = m_ar_lines.get(i).get(0);
                h[i] = pFont.h;
                size = m_ar_lines.get(i).size();
                for (int j = 0; j < size; ++j) {
                    pFont = m_ar_lines.get(i).get(j);
                    w[i] += (pFont.w * m_vspace);
                }
            }
        }

        m_width = 0.0f;
        m_height = 0.0f;

        for (int i = 0; i < MAX_TEXT_LINES; ++i) {
            if (m_width < w[i]) {
                m_width = w[i];
            }
            m_height += h[i];
        }

        m_width *= scale.x;
        m_height *= scale.y;

        m_half_width = m_width / 2.0f;
        m_half_height = m_height / 2.0f;
    }

    public void emit(Vector2 pos, Color color) {
        int size;
        TexturedQuad pFont;
        float x_start;
        Vector2 tmp = new Vector2();
        tmp.y = pos.y;

        for (int i = 0; i <= m_lines_count; ++i) {
            x_start = pos.x;

            if (m_align == TextAlignEnum.RightAlign) {
                float w = 0.0f;
                size = m_ar_lines.get(i).size();
                for (int j = 0; j < size; ++j) {
                    pFont = m_ar_lines.get(i).get(j);
                    w += (pFont.w * m_vspace) * scale.x;
                }
                x_start -= w;
            }

            tmp.x = x_start;
            //float x1 = x;
            size = m_ar_lines.get(i).size();
            for (int j = 0; j < size; ++j) {
                pFont = m_ar_lines.get(i).get(j);
                Graphics.addFont(tmp, scale, color, pFont);
                tmp.x += (pFont.w * m_vspace) * scale.x;
            }

            pFont = m_ar_lines.get(i).get(0);
            tmp.y -= pFont.h * scale.y;
        }
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

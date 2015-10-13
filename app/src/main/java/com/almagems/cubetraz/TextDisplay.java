package com.almagems.cubetraz;

import static android.opengl.GLES10.*;


public final class TextDisplay {

    public GLfloat m_verts[3*2048];
    public GLfloat m_coords[3*2048];
    public GLubyte m_colors[6*2048];

    int m_vertex_count;
    int m_vindex;
    int m_cindex;
    int m_color_index;


    // ctor
    public TextDisplay() {

    }

    public void init() {
        m_vertex_count = 0;
        m_vindex = -1;
        m_cindex = -1;
        m_color_index = -1;

        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);

        glVertexPointer(2, GL_FLOAT, 0, m_verts);
        glTexCoordPointer(2, GL_FLOAT, 0, m_coords);
        glColorPointer(4, GL_UNSIGNED_BYTE, 0, m_colors);
    }

    public void render() {
//		printf("\ncTextDisplay->Render");
//		printf("\nvindex = %d", m_vindex);
//		printf("\ncindex = %d", m_cindex);
//      printf("\ncolor_index = %d", m_color_index);
//		printf("\nvertex_count = %d", m_vertex_count);

        if (m_vertex_count > 0) {
            glDrawArrays(GL_TRIANGLES, 0, m_vertex_count);
        }
    }

}

package com.almagems.cubetraz;

import static android.opengl.GLES10.*;
import static com.almagems.cubetraz.Constants.*;

public class Starfield {

    public float speed;
    public float alpha;

    private int m_star_count;

    private Vector createRandomStar(float x, float y, float z) {
        Vector v = new Vector();
//        v.x = RANDOM_FLOAT(-10.0f, 20.0f);
//        v.y = RANDOM_FLOAT(-7.5f,  15.0f);
//        v.z = RANDOM_FLOAT(-30.0f, 30.0f);
        return v;
    }


    private Vector[] m_stars = new Vector[MAX_STARS];

    // ctor
    public Starfield() {
        speed = 0.75f;
    }

    public void create() {
        m_star_count = 256 * 2; //engine->device_scale;

        if (m_star_count > MAX_STARS)
            m_star_count = MAX_STARS;

        for (int i = 0; i < m_star_count; ++i) {
            createRandomStar(m_stars[i].x, m_stars[i].y, m_stars[i].z);
        }
    }

    public void update() {
        for (int i = 0; i < m_star_count; ++i) {
            m_stars[i].z += speed;
            if (m_stars[i].z > 40.0f) {
                createRandomStar(m_stars[i].x, m_stars[i].y, m_stars[i].z);
            }
        }
    }

    public void render() {
        Graphics.prepareFrame();
/*
        int big_points = 0;
        Color color(224, 255, 255, alpha);

        for (int i = 0; i < m_star_count; ++i)
        {
            if (m_stars[i].z > 10.0f)
            {
                cRenderer::AddPoint(m_stars[i], color);
                ++big_points;
            }
        }

        int small_points = 0;
        color = Color(255, 255, 224, alpha);
        for (int i = 0; i < m_star_count; ++i)
        {
            if (m_stars[i].z < 10.0f)
            {
                cRenderer::AddPoint(m_stars[i], color);
                ++small_points;
            }
        }

        if (small_points > 0 && big_points > 0)
        {
            glPointSize(2.0f*engine->device_scale);
            glDrawArrays(GL_POINTS, 0, big_points);

            glPointSize(1.0f*engine->device_scale);
            glDrawArrays(GL_POINTS, big_points, small_points);
        }

        if (small_points > 0 && big_points == 0)
        {
            glDrawArrays(GL_POINTS, 0, small_points);
        }
*/
    }

}

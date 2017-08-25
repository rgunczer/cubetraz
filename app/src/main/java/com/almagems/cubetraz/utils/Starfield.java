package com.almagems.cubetraz.utils;

import com.almagems.cubetraz.Game;
import com.almagems.cubetraz.graphics.Color;
import com.almagems.cubetraz.graphics.Graphics;
import com.almagems.cubetraz.math.Utils;
import com.almagems.cubetraz.math.Vector;

import static android.opengl.GLES10.*;
import static com.almagems.cubetraz.Game.*;

public class Starfield {
    public float speed;
    public float alpha;
    private int m_star_count;
    private final Vector[] m_stars = new Vector[MAX_STARS];

    // ctor
    public Starfield() {
        speed = 0.75f;
    }

    private void initStarPosition(Vector v) {
        v.x = Utils.randInt(-10, 20) + Utils.rand.nextFloat();
        v.y = Utils.randInt(-75, 150) / 10f + Utils.rand.nextFloat();
        v.z = Utils.randInt(-30, 30);
    }

    public void create() {
        m_star_count = (int)(384f * Graphics.deviceScale);

        for (int i = 0; i < MAX_STARS; ++i) {
            m_stars[i] = new Vector();
        }

        if (m_star_count > MAX_STARS) {
            m_star_count = MAX_STARS;
        }

        for (int i = 0; i < m_star_count; ++i) {
            initStarPosition(m_stars[i]);
        }
    }

    public void update() {
        for (int i = 0; i < m_star_count; ++i) {
            m_stars[i].z += speed;
            if (m_stars[i].z > 40.0f) {
                initStarPosition(m_stars[i]);
            }
        }
    }

    public void render() {
        int big_points = 0;
        Color color = new Color(224, 255, 255, (int)alpha);

        for (int i = 0; i < m_star_count; ++i) {
            if (m_stars[i].z > 10.0f) {
                Graphics.addPoint(m_stars[i], color);
                ++big_points;
            }
        }

        int small_points = 0;
        color = new Color(255, 255, 224, (int)alpha);
        for (int i = 0; i < m_star_count; ++i) {
            if (m_stars[i].z < 10.0f) {
                Graphics.addPoint(m_stars[i], color);
                ++small_points;
            }
        }

        Graphics.updateBuffers();

        if (small_points > 0 && big_points > 0) {
            glPointSize(2.0f * Graphics.deviceScale);
            glDrawArrays(GL_POINTS, 0, big_points);

            glPointSize(1.0f * Graphics.deviceScale);
            glDrawArrays(GL_POINTS, big_points, small_points);
//            glDrawArrays(GL_LINE_LOOP, 0, big_points);
//            glDrawArrays(GL_LINE_LOOP, big_points, small_points);
        }

        if (small_points > 0 && big_points == 0) {
            glDrawArrays(GL_POINTS, 0, small_points);
//            glDrawArrays(GL_LINE_LOOP, 0, small_points);
        }
    }

}

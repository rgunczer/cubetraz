package com.almagems.cubetraz.graphics;

import com.almagems.cubetraz.math.Utils;

public final class Color {

	public static final Color CLEAR = new Color(0f, 0f, 0f, 0f);
	public static final Color BLACK = new Color(0f, 0f, 0f, 1f);
	public static final Color WHITE = new Color(1f, 1f, 1f, 1f);
    public static final Color GRAY = new Color(0.5f, 0.5f, 0.5f, 1f);
    public static final Color YELLOW = new Color(1f, 1f, 0f, 1f);
    public static final Color RED = new Color(1f, 0f, 0f, 1f);

    public static Color randomColor() {
        return new Color( Utils.rand.nextInt(255),
                          Utils.rand.nextInt(255),
                          Utils.rand.nextInt(255),
                          /*Utils.rand.nextInt(255)*/ 255 );
    }

	public int r, g, b, a;

	public Color() {
		r = 0;
		g = 0;
		b = 0;
		a = 0;
	}

    public Color(Color another) {
        r = another.r;
        g = another.g;
        b = another.b;
        a = another.a;
    }

    public Color(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

	public Color(float r, float g, float b, float a) {
		this.r = (int)(r * 255);
		this.g = (int)(g * 255);
		this.b = (int)(b * 255);
		this.a = (int)(a * 255);
	}
	
	public Color(float r, float g, float b) {
		this.r = (int)(r * 255);
		this.g = (int)(g * 255);
		this.b = (int)(b * 255);
		this.a = 255;
	}

    public void init(Color from) {
        this.r = from.r;
        this.g = from.g;
        this.b = from.b;
        this.a = from.a;
    }

    public void randomize() {
        this.r = Utils.rand.nextInt(255);
        this.g = Utils.rand.nextInt(255);
        this.b = Utils.rand.nextInt(255);
    }

    public String toString() {
        return "MyColor (" + this.r + ", " + this.g + ", " + this.b + ", " + this.a + ")";
    }

}

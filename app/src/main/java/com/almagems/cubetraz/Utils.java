package com.almagems.cubetraz;

public final class Utils {

    public static final RandomXS128 rand = new RandomXS128();

    public static int randInt(int min, int max) {
        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        return rand.nextInt((max - min) + 1) + min;
    }

    public static float getDistance2D(final Vector2 a, final Vector2 b) {
//        System.out.println("getDistance2D x1:" + a.x + ", y1:" + a.y + ", x2:" + b.x + ", y2:" + b.y);
        float dx = a.x - b.x;
        float dy = a.y - b.y;

        return (float)Math.sqrt(dx * dx + dy * dy);
    }

    public static float lerp(float a, float b, float t) {
        return (a + t * (b - a) );
    }

    public static void lerpCamera(Camera a, Camera b, float t, Camera c) {
        c.eye.x = lerp(a.eye.x, b.eye.x, t);
        c.eye.y = lerp(a.eye.y, b.eye.y, t);
        c.eye.z = lerp(a.eye.z, b.eye.z, t);

        c.target.x = lerp(a.target.x, b.target.x, t);
        c.target.y = lerp(a.target.y, b.target.y, t);
        c.target.z = lerp(a.target.z, b.target.z, t);
    }

    public static void lerpVector3(Vector from, Vector to, float t, Vector result) {
        result.x = lerp(from.x, to.x, t);
        result.y = lerp(from.y, to.y, t);
        result.z = lerp(from.z, to.z, t);
    }

    public static void calcShadowMatrixFloor(Vector light_pos, float[] shadow_mat, float degree) {
        float[] vLightPos = { light_pos.x, light_pos.y, light_pos.z, 1.0f };

        float[] vPointOnPlane0 = { 1.0f, -3.3f + 0.55f, 0.0f };
        float[] vPointOnPlane1 = { 0.0f, -3.3f + 0.55f, 0.0f };
        float[] vPointOnPlane2 = { 0.0f, -3.3f + 0.55f, 1.0f };

        Vector v0 = rotate3D_AroundYAxis(vPointOnPlane0[0], vPointOnPlane0[1], vPointOnPlane0[2], degree);
        Vector v1 = rotate3D_AroundYAxis(vPointOnPlane1[0], vPointOnPlane1[1], vPointOnPlane1[2], degree);
        Vector v2 = rotate3D_AroundYAxis(vPointOnPlane2[0], vPointOnPlane2[1], vPointOnPlane2[2], degree);

        float [] p0 = new float[] { v0.x, v0.y, v0.z };
        float [] p1 = new float[] { v1.x, v1.y, v1.z };
        float [] p2 = new float[] { v2.x, v2.y, v2.z };

        Game.makeShadowMatrix(p0, p1, p2, vLightPos, shadow_mat);
        //Engine.makeShadowMatrix(p0, p1, p2, vLightPos, shadow_matrix);
        //Graphics.makeShadowMatrix(vPointOnPlane0, vPointOnPlane1, vPointOnPlane2, vLightPos, shadow_matrix);
    }

    public static void calcShadowMatrixWallZ(Vector light_pos, float shadow_matrix[], float degree) {
        float[] vLightPos = { light_pos.x, light_pos.y, light_pos.z, 1.0f };

        float[] vPointOnPlane0 = {  0.0f,          0.0f, -3.3f + 0.55f };
        float[] vPointOnPlane1 = { -1.0f,          0.0f, -3.3f + 0.55f };
        float[] vPointOnPlane2 = {  0.0f, -4.4f + 0.55f, -3.3f + 0.55f };

        Vector v0 = rotate3D_AroundYAxis(vPointOnPlane0[0], vPointOnPlane0[1], vPointOnPlane0[2], degree);
        Vector v1 = rotate3D_AroundYAxis(vPointOnPlane1[0], vPointOnPlane1[1], vPointOnPlane1[2], degree);
        Vector v2 = rotate3D_AroundYAxis(vPointOnPlane2[0], vPointOnPlane2[1], vPointOnPlane2[2], degree);

        float [] p0 = new float[] { v0.x, v0.y, v0.z };
        float [] p1 = new float[] { v1.x, v1.y, v1.z };
        float [] p2 = new float[] { v2.x, v2.y, v2.z };

        Game.makeShadowMatrix(p0, p1, p2, vLightPos, shadow_matrix);
        //Engine.makeShadowMatrix(vPointOnPlane0, vPointOnPlane1, vPointOnPlane2, vLightPos, shadow_matrix);
//    engine->MakeShadowMatrix(p0, p1, p2, vLightPos, shadow_matrix);
    }

    public static void calcShadowMatrixWallX(Vector light_pos, float shadow_matrix[], float degree) {
        float[] vLightPos = new float[] { light_pos.x, light_pos.y, light_pos.z, 1.0f };

        float[] vPointOnPlane0 = new float[] { -3.3f + 0.55f,          0.0f,  0.0f };
        float[] vPointOnPlane1 = new float[] { -3.3f + 0.55f, -4.4f + 0.55f,  0.0f };
        float[] vPointOnPlane2 = new float[] { -3.3f + 0.55f,          0.0f, -1.0f };

        Vector v0 = rotate3D_AroundYAxis(vPointOnPlane0[0], vPointOnPlane0[1], vPointOnPlane0[2], degree);
        Vector v1 = rotate3D_AroundYAxis(vPointOnPlane1[0], vPointOnPlane1[1], vPointOnPlane1[2], degree);
        Vector v2 = rotate3D_AroundYAxis(vPointOnPlane2[0], vPointOnPlane2[1], vPointOnPlane2[2], degree);

        float [] p0 = new float[] { v0.x, v0.y, v0.z };
        float [] p1 = new float[] { v1.x, v1.y, v1.z };
        float [] p2 = new float[] { v2.x, v2.y, v2.z };

        Game.makeShadowMatrix(p0, p1, p2, vLightPos, shadow_matrix);

//	engine->MakeShadowMatrix(p0, p1, p2, vLightPos, shadow_matrix);
        //Engine.makeShadowMatrix(vPointOnPlane0, vPointOnPlane1, vPointOnPlane2, vLightPos, shadow_matrix);
    }

    public static float quadraticIn(float time) {
        return time * time;
    }

    public static float quadraticOut(float time) {
        return time * (2 - time);
    }

    public static Vector rotate3D_AroundXAxis(float x, float y, float z, float degree) {
        Vector v = new Vector();
        float q = (float)Math.toRadians(degree);

        v.y = y * (float)Math.cos(q) - z * (float)Math.sin(q);
        v.z = y * (float)Math.sin(q) + z * (float)Math.cos(q);
        v.x = x;

        return v;
    }

    public static Vector rotate3D_AroundYAxis(float x, float y, float z, float degree) {
        Vector v = new Vector();
        float q = (float)Math.toRadians(degree);

        v.z = z * (float)Math.cos(q) - x * (float)Math.sin(q);
        v.x = z * (float)Math.sin(q) + x * (float)Math.cos(q);
        v.y = y ;

        return v;
    }

    public static Vector rotate3D_AroundZAxis(float x, float y, float z, float degree) {
        Vector v = new Vector();
        float q = (float)Math.toRadians(degree);

        v.x = x * (float)Math.cos(q) - y * (float)Math.sin(q);
        v.y = x * (float)Math.sin(q) + y * (float)Math.cos(q);
        v.z = z;

        return v;
    }


    // ctor
    private Utils() {
    }

}

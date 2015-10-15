package com.almagems.cubetraz;

public final class Utils {

    public static float getDistance2D(final Vector2 a, final Vector2 b) {
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

    public static void lerpVec3(Vector from, Vector to, float t, Vector result) {
        result.x = lerp(from.x, to.x, t);
        result.y = lerp(from.y, to.y, t);
        result.z = lerp(from.z, to.z, t);
    }

    public static void calcShadowMatrixFloor(Vector light_pos, float shadow_matrix[], float degree) {
        float[] vLightPos = { light_pos.x, light_pos.y, light_pos.z, 1.0f };

        float[] vPointOnPlane0 = { 1.0f, -3.3f + 0.55f, 0.0f };
        float[] vPointOnPlane1 = { 0.0f, -3.3f + 0.55f, 0.0f };
        float[] vPointOnPlane2 = { 0.0f, -3.3f + 0.55f, 1.0f };

//    float p0[3];
//    float p1[3];
//    float p2[3];
//
//    Rotate3D_AroundYAxis(vPointOnPlane0[0], vPointOnPlane0[1], vPointOnPlane0[2], degree, p0[0], p0[1], p0[2]);
//    Rotate3D_AroundYAxis(vPointOnPlane1[0], vPointOnPlane1[1], vPointOnPlane1[2], degree, p1[0], p1[1], p1[2]);
//    Rotate3D_AroundYAxis(vPointOnPlane2[0], vPointOnPlane2[1], vPointOnPlane2[2], degree, p2[0], p2[1], p2[2]);
//
//    engine->MakeShadowMatrix(p0, p1, p2, vLightPos, shadow_matrix);
        Graphics.makeShadowMatrix(vPointOnPlane0, vPointOnPlane1, vPointOnPlane2, vLightPos, shadow_matrix);
    }

    public static void calcShadowMatrixWallZ(Vector light_pos, float shadow_matrix[], float degree) {
        float[] vLightPos = { light_pos.x, light_pos.y, light_pos.z, 1.0f };

        float[] vPointOnPlane0 = {  0.0f,          0.0f, -3.3f + 0.55f };
        float[] vPointOnPlane1 = { -1.0f,          0.0f, -3.3f + 0.55f };
        float[] vPointOnPlane2 = {  0.0f, -4.4f + 0.55f, -3.3f + 0.55f };

//    float p0[3];
//    float p1[3];
//    float p2[3];
//
//    Rotate3D_AroundYAxis(vPointOnPlane0[0], vPointOnPlane0[1], vPointOnPlane0[2], degree, p0[0], p0[1], p0[2]);
//    Rotate3D_AroundYAxis(vPointOnPlane1[0], vPointOnPlane1[1], vPointOnPlane1[2], degree, p1[0], p1[1], p1[2]);
//    Rotate3D_AroundYAxis(vPointOnPlane2[0], vPointOnPlane2[1], vPointOnPlane2[2], degree, p2[0], p2[1], p2[2]);

        Engine.makeShadowMatrix(vPointOnPlane0, vPointOnPlane1, vPointOnPlane2, vLightPos, shadow_matrix);
//    engine->MakeShadowMatrix(p0, p1, p2, vLightPos, shadow_matrix);
    }

    public static void calcShadowMatrixWallX(Vector light_pos, float shadow_matrix[], float degree) {
        float[] vLightPos = { light_pos.x, light_pos.y, light_pos.z, 1.0f };

        float[] vPointOnPlane0 = { -3.3f + 0.55f,          0.0f,  0.0f };
        float[] vPointOnPlane1 = { -3.3f + 0.55f, -4.4f + 0.55f,  0.0f };
        float[] vPointOnPlane2 = { -3.3f + 0.55f,          0.0f, -1.0f };

//    float p0[3];
//    float p1[3];
//    float p2[3];
//
//    Rotate3D_AroundYAxis(vPointOnPlane0[0], vPointOnPlane0[1], vPointOnPlane0[2], degree, p0[0], p0[1], p0[2]);
//    Rotate3D_AroundYAxis(vPointOnPlane1[0], vPointOnPlane1[1], vPointOnPlane1[2], degree, p1[0], p1[1], p1[2]);
//    Rotate3D_AroundYAxis(vPointOnPlane2[0], vPointOnPlane2[1], vPointOnPlane2[2], degree, p2[0], p2[1], p2[2]);
//
//	engine->MakeShadowMatrix(p0, p1, p2, vLightPos, shadow_matrix);

        Engine.makeShadowMatrix(vPointOnPlane0, vPointOnPlane1, vPointOnPlane2, vLightPos, shadow_matrix);
    }

    // ctor
    private Utils() {
    }

}

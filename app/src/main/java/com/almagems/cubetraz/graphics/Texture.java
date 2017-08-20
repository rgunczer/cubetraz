package com.almagems.cubetraz.graphics;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.opengl.GLES10.GL_TEXTURE_2D;
import static android.opengl.GLES10.glBindTexture;
import static android.opengl.GLES10.glDeleteTextures;


public final class Texture {
    public int id;
    public float width;
    public float height;
    public String name;

    private Map<String, Rectangle> frames = new HashMap<String, Rectangle>(20);

    public Texture() {
    }

    public Rectangle getFrame(String key) {
        Rectangle rect = frames.get(key);

        if (rect != null) {
            Rectangle newRect = new Rectangle(rect);
            newRect.y += newRect.h;
            return newRect;
        }

        //System.out.println("Frame with locationKey: [" + locationKey + "]. Not found!" );
        return null;
    }

    public void loadFrames(String jsonText) {
        try {
            JSONObject jObject = new JSONObject(jsonText);
            JSONArray arr = jObject.getJSONArray("frames");
            JSONObject obj, frame;
            String fileName;
            int x, y, w, h;

            for(int i = 0; i < arr.length(); ++i) {
                obj = arr.getJSONObject(i);

                fileName = obj.getString("filename");
                frame = obj.getJSONObject("frame");
                x = frame.getInt("x");
                y = frame.getInt("y");
                w = frame.getInt("w");
                h = frame.getInt("h");
                //System.out.println("FileName is: " + fileName + ", x: " + x + ", y: " + y + ", w: " + w + ", h: " + h);

                frames.put(fileName, new Rectangle(x, y, w, h));
            }
        } catch (final Exception ex) {
            //System.out.println( ex.toString() );
        }
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void release() {
        int[] arr = { id };
        glDeleteTextures(arr.length, arr, 0);
    }


    public String toString() {
        return "Texture name:" + this.name + ", w:" + width + ", h:" + height + ", id:" + id;
    }

}

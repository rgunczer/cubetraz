package com.almagems.cubetraz.graphics;

import static android.opengl.GLES10.*;
import static android.opengl.GLUtils.texImage2D;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.almagems.cubetraz.Game;


final class TextureHelper {

	 static Texture loadTexture(int resourceId) {
		
		final int[] textureObjectIds = new int[1];
		glGenTextures(1, textureObjectIds, 0);
		
		if (textureObjectIds[0] == 0) {
			return null;
		}
	
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = false;

        Texture texture = new Texture();

		final Bitmap bmp = BitmapFactory.decodeResource(Game.getContext().getResources(), resourceId, options);
        final int w = bmp.getWidth();
        final int h = bmp.getHeight();

        texture.name = Game.getContext().getResources().getResourceEntryName(resourceId);
        texture.width = w;
        texture.height = h;

		Matrix flip = new Matrix();
		//flip.postScale(1f, -1f);
		
		Bitmap bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), flip, true);
		
		if (bitmap == null) {
			glDeleteTextures(1, textureObjectIds, 0);
			return null;
		}
		
		glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		
		texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
		
		bitmap.recycle();

		glBindTexture(GL_TEXTURE_2D, 0);

        texture.id = textureObjectIds[0];

        return texture;
	}

}

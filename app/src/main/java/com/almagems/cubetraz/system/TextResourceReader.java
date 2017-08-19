package com.almagems.cubetraz.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.res.Resources;

import com.almagems.cubetraz.game.Game;

public final class TextResourceReader {

	public static String readTextFileFromResource(int resourceId) {
		StringBuilder body = new StringBuilder();
		
		try {
			InputStream inputStream = Game.getContext().getResources().openRawResource(resourceId);
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			
			String nextLine;
			while((nextLine = bufferedReader.readLine()) != null) {
				body.append(nextLine);
				body.append('\n');
			}
		} catch (IOException e) {
			throw new RuntimeException("Could not open resource: " + resourceId);
		} catch (Resources.NotFoundException nfe) {
			throw new RuntimeException("Resource not found: " + resourceId, nfe);
		}
		return body.toString();
	}
	
}

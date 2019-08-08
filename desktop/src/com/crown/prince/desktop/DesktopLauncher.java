package com.crown.prince.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.crown.prince.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1152;
		config.height = 648;
		config.foregroundFPS = 60;
		config.backgroundFPS = -1;
		//config.resizable = false;
		//config.fullscreen = true;
		config.title = "Crown Prince";
		new LwjglApplication(new Main(), config);
	}
}

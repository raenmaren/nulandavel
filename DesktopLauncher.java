package com.emeraldSword.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.emeraldSword.game.EmeraldSword;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Emerald Sword";
	    config.width = 480;
	    config.height = 800;
		new LwjglApplication(new EmeraldSword(), config);
	}
}

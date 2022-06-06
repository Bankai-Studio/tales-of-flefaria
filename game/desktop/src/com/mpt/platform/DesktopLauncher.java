package com.mpt.platform;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		config.setTitle("Tales of Flefaria");
		config.useVsync(true);
		config.setForegroundFPS(60);
		config.setWindowedMode(Lwjgl3ApplicationConfiguration.getDisplayMode().width, Lwjgl3ApplicationConfiguration.getDisplayMode().height);
		config.setMaximized(true);
		config.setResizable(false);
		config.setWindowIcon("icons/gameIcon.png");

		new Lwjgl3Application(new PlatformGame(), config);
	}
}

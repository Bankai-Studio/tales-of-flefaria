package com.mpt.platform;

import com.badlogic.gdx.Game;
import com.mpt.modules.MusicModule;

public class PlatformGame extends Game {

	MenuScreen menuScreen;

	@Override
	public void create() {
		MusicModule.setup();
		menuScreen = new MenuScreen();
		MusicModule.getMainMenuMusic().play();
		MusicModule.getMainMenuMusic().setVolume(0.5f);
		MusicModule.getMainMenuMusic().setLooping(true);
		setScreen(menuScreen);
	}

	@Override
	public void dispose() {
		menuScreen.dispose();
		MusicModule.dispose();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		menuScreen.resize(width, height);
	}
}

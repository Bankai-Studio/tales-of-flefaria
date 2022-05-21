package com.mpt.platform;

import com.badlogic.gdx.Game;

public class PlatformGame extends Game {

	MenuScreen menuScreen;

	@Override
	public void create() {
		menuScreen = new MenuScreen();
		setScreen(menuScreen);
	}

	@Override
	public void dispose() {
		menuScreen.dispose();
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

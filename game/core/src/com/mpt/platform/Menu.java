package com.mpt.platform;

import com.badlogic.gdx.Game;

public class Menu extends Game {
    @Override
    public void create() {
        this.setScreen(new LoginScreen(this));
    }
}

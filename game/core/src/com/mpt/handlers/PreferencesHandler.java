package com.mpt.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class PreferencesHandler {
    public PreferencesHandler() {}

    protected Preferences getPreferences() {
        return Gdx.app.getPreferences("gamesavings");
    }
}

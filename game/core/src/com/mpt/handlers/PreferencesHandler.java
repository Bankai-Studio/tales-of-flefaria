package com.mpt.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Vector2;

public class PreferencesHandler {
    public PreferencesHandler() {}

    protected Preferences getPreferences() {
        return Gdx.app.getPreferences("gamesavings");
    }

    public void setRespawnPosition(Vector2 coordinates) {
        getPreferences().putFloat("xRespawnPosition", coordinates.x);
        getPreferences().putFloat("yRespawnPosition", coordinates.y);
        getPreferences().flush();
    }

    public Vector2 getRespawnPosition() {
        return new Vector2(getPreferences().getFloat("xRespawnPosition", 6.46875f), getPreferences().getFloat("yRespawnPosition", 9.53125f));
    }

}

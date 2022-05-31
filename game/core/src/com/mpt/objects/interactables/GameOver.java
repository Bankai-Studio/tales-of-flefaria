package com.mpt.objects.interactables;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.mpt.objects.GameObject;

public class GameOver extends GameObject {
    public GameOver(float width, float height, Body body) {
        super(width, height, body);
        body.setUserData(this);
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void render(SpriteBatch batch) {

    }

}

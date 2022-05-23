package com.mpt.objects.interactables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.mpt.objects.GameObject;

import static com.mpt.constants.Constants.PPM;

public class Ladder extends GameObject {
    public Ladder(float width, float height, Body body) {
        super(width, height, body);
    }
    @Override
    public void update(float delta) {}
    @Override
    public void render(SpriteBatch batch) {}
}

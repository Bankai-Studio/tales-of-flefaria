package com.mpt.objects.interactables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.mpt.objects.GameObject;

import static com.mpt.constants.Constants.PPM;

public class Box extends GameObject {
    private final Texture texture;

    public Box(float width, float height, Body body) {
        super(width, height, body);
        texture = new Texture(Gdx.files.internal("box/box.png"));
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, body.getPosition().x * PPM - width / 2, body.getPosition().y * PPM - height / 2, width, height);
    }

    public void dispose() {
        texture.dispose();
    }

}

package com.mpt.objects.interactables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.mpt.objects.GameObject;

import static com.mpt.constants.Constants.PPM;

public class KillBlock extends GameObject {
    private final Texture texture;

    public KillBlock(float width, float height, Body body, String name) {
        super(width, height, body);
        body.setUserData(this);
        texture = new Texture(Gdx.files.internal("./killBlock/"+name+".png"));
    }

    @Override
    public void update(float delta) {}

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, body.getPosition().x * PPM - width/2, body.getPosition().y * PPM - height/2, width, height);
    }
}

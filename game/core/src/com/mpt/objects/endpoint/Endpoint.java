package com.mpt.objects.endpoint;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import com.mpt.handlers.AnimationHandler;
import com.mpt.objects.GameObject;

import static com.mpt.constants.Constants.PPM;

public class Endpoint extends GameObject {
    private final AnimationHandler animationHandler = new AnimationHandler();

    public Endpoint(float width, float height, Body body) {
        super(width, height, body);
        body.setUserData(this);
        TextureAtlas charset = new TextureAtlas(Gdx.files.internal("./portal/portal.atlas"));
        animationHandler.add("portal", new Animation<>(1 / 16f, charset.findRegions("portal")));
        animationHandler.setCurrent("portal");
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(animationHandler.getFrame(), x * PPM - width, y * PPM - height / 2, width * 2, height);
    }
}

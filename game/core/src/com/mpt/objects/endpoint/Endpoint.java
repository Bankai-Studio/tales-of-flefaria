package com.mpt.objects.endpoint;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.mpt.handlers.AnimationHandler;
import com.mpt.objects.GameObject;

import static com.mpt.constants.Constants.PPM;

public class Endpoint extends GameObject {
    private final AnimationHandler animationHandler = new AnimationHandler();
    private final boolean isFlipped;
    public Endpoint(float width, float height, Body body, boolean isFlipped) {
        super(width, height, body);
        body.setUserData(this);
        TextureAtlas charset = new TextureAtlas(Gdx.files.internal("./portal/portal.atlas"));
        animationHandler.add("portal", new Animation<>(1 / 16f, charset.findRegions("portal")));
        animationHandler.setCurrent("portal");
        this.isFlipped = isFlipped;
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = animationHandler.getFrame();
        if(isFlipped && !currentFrame.isFlipX()) currentFrame.flip(true, false);
        batch.draw(currentFrame, x * PPM - width, y * PPM - height / 2, width * 2, height);
    }
}

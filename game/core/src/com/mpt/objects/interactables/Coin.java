package com.mpt.objects.interactables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.mpt.handlers.AnimationHandler;
import com.mpt.objects.GameObject;

import static com.mpt.constants.Constants.PPM;

public class Coin extends GameObject {
    private final AnimationHandler animationHandler = new AnimationHandler();
    private boolean isCollected;

    public Coin(float width, float height, Body body) {
        super(width, height, body);
        body.setUserData(this);
        TextureAtlas charset = new TextureAtlas(Gdx.files.internal("coin/coin.atlas"));
        animationHandler.add("coin", new Animation<>(1 / 16f, charset.findRegions("coin")));
        animationHandler.setCurrent("coin");
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = animationHandler.getFrame();
        if(!isCollected) batch.draw(currentFrame, x * PPM - width / 2, y * PPM - height / 2, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
    }

    public void setIsCollected(boolean isCollected) {
        this.isCollected = isCollected;
    }

    public boolean isCollected() {
        return isCollected;
    }
}
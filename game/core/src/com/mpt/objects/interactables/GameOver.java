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

public class GameOver extends GameObject {
    private final AnimationHandler animationHandler;
    private final TextureAtlas charset;

    public GameOver(float width, float height, Body body) {
        super(width, height, body);
        body.setUserData(this);
        animationHandler = new AnimationHandler();
        charset = new TextureAtlas(Gdx.files.internal("./NPCs/Old_man/idle.atlas"));
        animationHandler.add("idle", new Animation<>(1 / 4f, charset.findRegions("idle")));
        animationHandler.setCurrent("idle");
    }

    public void dispose(){
        charset.dispose();
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = animationHandler.getFrame();
        if (!currentFrame.isFlipX()) currentFrame.flip(true, false);
        batch.draw(currentFrame, x * PPM - width, y * PPM - height / 2);
    }

}

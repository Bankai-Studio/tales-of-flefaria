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

public class Ghost extends GameObject {
    private final AnimationHandler animationHandler = new AnimationHandler();
    private boolean touched;
    private boolean isFlipped;
    private final TextureAtlas charset;

    public Ghost(float width, float height, Body body, boolean isFlipped) {
        super(width, height, body);
        body.setUserData(this);
        charset = new TextureAtlas(Gdx.files.internal("ghost/ghost.atlas"));
        animationHandler.add("ghost", new Animation<>(1 / 12f, charset.findRegions("ghost")));
        animationHandler.setCurrent("ghost");
        this.isFlipped = isFlipped;
        touched = false;
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
        if(isFlipped && !currentFrame.isFlipX()) currentFrame.flip(true, false);
        if(!touched) batch.draw(currentFrame, x * PPM - width*(3f/2f), y * PPM - height, currentFrame.getRegionWidth()/5, currentFrame.getRegionHeight()/5);
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    public boolean isTouched(){
        if(touched)
            return true;
        return false;
    }
}

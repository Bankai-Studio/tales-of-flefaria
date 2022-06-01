package com.mpt.objects.interactables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.mpt.handlers.AnimationHandler;
import com.mpt.objects.GameObject;
import com.mpt.objects.enemy.Enemy;
import com.mpt.objects.enemy.FinalBoss;
import com.mpt.platform.GameScreen;

import static com.mpt.constants.Constants.PPM;

public class GameOver extends GameObject {
    private final AnimationHandler animationHandler;
    private final TextureAtlas charset;
    private boolean visible;
    private final GameScreen gameScreen;

    public GameOver(float width, float height, Body body, GameScreen gameScreen) {
        super(width, height, body);
        body.setUserData(this);
        animationHandler = new AnimationHandler();
        charset = new TextureAtlas(Gdx.files.internal("./NPCs/Old_man/idle.atlas"));
        animationHandler.add("idle", new Animation<>(1 / 4f, charset.findRegions("idle")));
        animationHandler.setCurrent("idle");
        visible = false;
        this.gameScreen = gameScreen;
    }

    public void dispose(){
        if(visible) charset.dispose();
    }

    @Override
    public void update(float delta) {
        for (Enemy enemy : gameScreen.getEnemies())
            if (enemy instanceof FinalBoss && enemy.getEnemyState().equals(Enemy.EnemyState.DYING))
                visible = true;
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = animationHandler.getFrame();
        if (!currentFrame.isFlipX()) currentFrame.flip(true, false);
        if(visible) batch.draw(currentFrame, x * PPM - width, y * PPM - height / 2);
    }

    public boolean isVisible() {
        return visible;
    }
}

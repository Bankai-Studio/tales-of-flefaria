package com.mpt.objects.enemy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.mpt.handlers.AnimationHandler;
import com.mpt.objects.player.Player;
import com.mpt.platform.GameScreen;

import static com.mpt.constants.Constants.PPM;

public class Slime extends Enemy {
    private boolean setToDestroy = false; //boolean says if enemy still to be killed
    private boolean destroyed = false; //boolean says its dead
    private float xPos = 0; //initial xPos of slime
    private float yPos = 0; //initial yPos of slime
    private GameScreen gameScreen;
    private final float distanceFromPlayer = 2f;

    private final AnimationHandler animationHandler;
    private final float FRAME_TIME = 1 / 6f;

    public Slime(float width, float height, Body body, GameScreen gameScreen) {
        super(width, height, body);
        xPos = body.getPosition().x; //initial position of slime
        yPos = body.getPosition().y; //initial position of slime
        animationHandler = new AnimationHandler();
        loadPlayerSprites();
        this.gameScreen = gameScreen;
    }

    @Override
    public void update(float delta) {
        if(!playerSpotted(gameScreen.getPlayer())) {
            enemyMovements();
            System.out.println("players has NOT been spotted");
        }else {
            lurkTarget(gameScreen.getPlayer());
            System.out.println("player's health"+gameScreen.getPlayer().getHealth());
        }
    }
    @Override
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = animationHandler.getFrame();

        if(direction.equals("LEFT") && !currentFrame.isFlipX()) currentFrame.flip(true,false);
        if(direction.equals("RIGHT") && currentFrame.isFlipX()) currentFrame.flip(true,false);

        float tX=body.getPosition().x * PPM - 35f;
        float tY=body.getPosition().y * PPM - 12f;
        batch.begin();
            batch.draw(currentFrame, tX, tY);
        batch.end();
    }

    private void loadPlayerSprites() {
        TextureAtlas charset = new TextureAtlas(Gdx.files.internal("./enemies/Centipede/walk.atlas"));
        animationHandler.add("walk", new Animation<>(FRAME_TIME, charset.findRegions("walk")));
        animationHandler.setCurrent("walk");
    }
}

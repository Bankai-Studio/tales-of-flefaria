package com.mpt.objects.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mpt.handlers.AnimationHandler;
import com.mpt.handlers.MapHandler;
import com.mpt.objects.player.Player;
import com.mpt.objects.GameEntity;
import com.mpt.platform.GameScreen;

import javax.swing.*;

import static com.mpt.constants.Constants.PPM;

public abstract class Enemy extends GameEntity{
    private float actuallyPosX = body.getPosition().x;
    private float actuallyPosY = body.getPosition().y;
    private float playerMoveToX;
    private float playerMoveToY;
    private double diffY;
    private double diffX;
    private double angle;
    private float initialPosX; //initial xPos of slime
    private float initialPosY; //initial yPos of slime
    private int killCounter = 0; //player's frags
    final private float distance = 2f;
    private int damageToPlayer;
    private GameScreen gameScreen;
    private boolean playerHasBeenSpotted;
    private boolean playerHasBeenDefeat;
    private float xMaxLimitDX; //limit position on right side
    private float xMaxLimitSX; //limit position on left side
    private boolean switchDirectionToRight = false; //flag that says if enemy switched direction
    private boolean switchDirectionToLeft = false;
    protected float walkSpeed;
    private float x,y;
    // Enemy States
    public enum EnemyState {
        IDLE,
        WALKING,
        ATTACKING,
        DYING,
        HURT;
    }
    protected EnemyState enemyState;
    protected String direction;
    protected String enemyName;
    protected final AnimationHandler animationHandler;
    public Enemy(float width, float height, Body body, GameScreen gameScreen) {
        super(width, height, body);
        initialPosX = body.getPosition().x; //initial position of enemy
        initialPosY = body.getPosition().y; //initial position of enemy
        playerHasBeenSpotted = false;
        playerHasBeenDefeat = false;
        this.gameScreen = gameScreen;
        direction = "RIGHT";
        animationHandler = new AnimationHandler();
    }

    @Override
    public void update(float delta) {
        if(!playerSpotted(gameScreen.getPlayer())) {
            enemyMovements();
            //System.out.println("players has NOT been spotted");
        }else {
            lurkTarget(gameScreen.getPlayer());
            //System.out.println("player has BEEN spotted");
            //System.out.println("player's health"+gameScreen.getPlayer().getHealth());
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = animationHandler.getFrame();

        if(direction.equals("LEFT") && !currentFrame.isFlipX()) currentFrame.flip(true,false);
        if(direction.equals("RIGHT") && currentFrame.isFlipX()) currentFrame.flip(true,false);

        float tX=body.getPosition().x * PPM - 35f;
        float tY=body.getPosition().y * PPM - 12f;
        batch.draw(currentFrame, tX, tY);
    }

    public void enemyMovements(){
        enemyState = EnemyState.WALKING;
        xMaxLimitDX = initialPosX + 2.4f;
        xMaxLimitSX = initialPosX - 5f;
        switchDirectionToRight = true;
        if (body.getPosition().x < xMaxLimitDX && switchDirectionToRight){
            body.setLinearVelocity(walkSpeed * (3f), body.getLinearVelocity().y);
            setFacingLeft();
        }
        else {
            switchDirectionToRight = false;
            switchDirectionToLeft = true;
        }
        if (switchDirectionToLeft && body.getPosition().x > xMaxLimitSX){
            body.setLinearVelocity(walkSpeed * (-3f), body.getLinearVelocity().y);
            setFacingRight();
        }
        else {
            switchDirectionToLeft = false;
            switchDirectionToRight = true;
        }
    }

    public boolean playerSpotted(Player player) {
        //System.out.println(Math.abs(player.getBody().getPosition().x - body.getPosition().x));
        playerHasBeenSpotted = Math.abs(player.getBody().getPosition().x - body.getPosition().x) < 8f;
        return playerHasBeenSpotted;
    }

    public void lurkTarget(Player player){
        if(playerSpotted(player) && Math.abs((int)player.getBody().getPosition().x - (int)body.getPosition().x) != 0) {
            if (player.getBody().getPosition().x < this.getBody().getPosition().x) {
                body.setLinearVelocity(walkSpeed * (-3f), body.getLinearVelocity().y);
                setFacingRight();
            } else if(player.getBody().getPosition().x > this.getBody().getPosition().x) {
                body.setLinearVelocity(walkSpeed * (3f), body.getLinearVelocity().y);
                setFacingLeft();
            }
        }
        else if(Math.abs((int)player.getBody().getPosition().x - (int)body.getPosition().x) == 0) {
            this.getBody().setLinearVelocity(0, this.getBody().getLinearVelocity().y);
            enemyState = EnemyState.IDLE;
        }
    }

    protected void loadSprites() {
        TextureAtlas charset;

        charset = new TextureAtlas(Gdx.files.internal("./enemies/"+enemyName+"/attack.atlas"));
        float FRAME_TIME = 1 / 6f;
        this.animationHandler.add("attack", new Animation<>(FRAME_TIME, charset.findRegions("attack")));
/*
        charset = new TextureAtlas(Gdx.files.internal("./enemies/"+enemyName+"/attack2.atlas"));
        this.animationHandler.add("attack2", new Animation<>(FRAME_TIME, charset.findRegions("attack2")));

        charset = new TextureAtlas(Gdx.files.internal("./enemies/"+enemyName+"/attack3.atlas"));
        this.animationHandler.add("attack3", new Animation<>(FRAME_TIME, charset.findRegions("attack3")));

        charset = new TextureAtlas(Gdx.files.internal("./enemies/"+enemyName+"/attack4.atlas"));
        this.animationHandler.add("attack4", new Animation<>(FRAME_TIME, charset.findRegions("attack4")));
*/
        charset = new TextureAtlas(Gdx.files.internal("./enemies/"+enemyName+"/death.atlas"));
        this.animationHandler.add("death", new Animation<>(FRAME_TIME, charset.findRegions("death")));

        charset = new TextureAtlas(Gdx.files.internal("./enemies/"+enemyName+"/hurt.atlas"));
        this.animationHandler.add("hurt", new Animation<>(FRAME_TIME, charset.findRegions("hurt")));

        charset = new TextureAtlas(Gdx.files.internal("./enemies/"+enemyName+"/idle.atlas"));
        this.animationHandler.add("idle", new Animation<>(FRAME_TIME, charset.findRegions("idle")));
/*
        charset = new TextureAtlas(Gdx.files.internal("./enemies/"+enemyName+"/sneer.atlas"));
        this.animationHandler.add("sneer", new Animation<>(FRAME_TIME, charset.findRegions("sneer")));
*/
        charset = new TextureAtlas(Gdx.files.internal("./enemies/"+enemyName+"/walk.atlas"));
        this.animationHandler.add("walk", new Animation<>(FRAME_TIME, charset.findRegions("walk")));

        this.animationHandler.setCurrent("walk");
    }

    public void setEnemyState(EnemyState enemyState){this.enemyState=enemyState;}
    public void setFacingLeft() {
        direction = "LEFT";
    }
    public void setFacingRight() {
        direction = "RIGHT";
    }
    public int getHealth(){return health;}
    public void setHealth(int health){this.health=health;}
}

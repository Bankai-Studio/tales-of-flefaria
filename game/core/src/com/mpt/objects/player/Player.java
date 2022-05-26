package com.mpt.objects.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mpt.handlers.AnimationHandler;
import com.mpt.objects.GameEntity;
import com.mpt.objects.enemy.Enemy;

public class Player extends GameEntity {
    // Constants
    private int maxPlayerStamina = 100;

    // Player States
    public enum State {
        IDLE,
        WALKING,
        RUNNING,
        JUMPING,
        DYING,
        PUSHING,
        ATTACKING,
        HURT,
        FALLING,
        CLIMBING;
    }

    // Variables
    private State state;
    private int playerStamina;
    private Vector2 respawnPosition;
    private boolean canRespawn;
    private final AnimationHandler playerAnimations;
    private final float FRAME_TIME = 1 / 7f;
    private String direction;
    private int characterSelection;
    private final float chargedAttackMultiplier = 1.5f;
    private float playerSpeed;
    private int collectedCoins;
    public Player(float width, float height, Body body) {
        super(width, height, body);
        minDamage = 34;
        maxDamage = 50;
        health = 100;
        playerSpeed = 8f;
        playerStamina = maxPlayerStamina;
        canRespawn = true;
        playerAnimations = new AnimationHandler();
        characterSelection = 0;
        collectedCoins = 0;
        direction = "RIGHT";
        loadPlayerSprites();

        playerAnimations.setCurrent("idle");
        state = State.IDLE;

        respawnPosition = new Vector2(body.getPosition().x, body.getPosition().y);
        body.setUserData(this);
    }

    @Override
    public void update(float delta) {
        checkPlayerDeath();
        respawnOnVoidPosition();
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = playerAnimations.getFrame();

        if(direction.equals("LEFT") && !currentFrame.isFlipX()) currentFrame.flip(true,false);
        if(direction.equals("RIGHT") && currentFrame.isFlipX()) currentFrame.flip(true,false);

        float tX = x-15f, tY = y-17f;
        if(direction.equals("LEFT")) tX -= 17f;
        batch.draw(currentFrame, tX, tY, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
    }

    private void checkPlayerDeath() {
        if(state.equals(State.DYING) && canRespawn) {
            body.setTransform(respawnPosition.x, respawnPosition.y, body.getAngle());
            state = State.IDLE;
            canRespawn = false;
        }
        else
            canRespawn = true;
    }

    private void respawnOnVoidPosition() {
        if(body.getPosition().y < 0)
            state = State.DYING;
    }

    private void loadPlayerSprites() {
        String characterName;
        switch (characterSelection){
            case 0:
                characterName = "Woodcutter";
                break;
            case 1:
                characterName = "GraveRobber";
                break;
            case 2:
                characterName = "SteamMan";
                break;
            default:
                throw new java.lang.Error("Player.java: characterSelection has ambiguous value");
        }
        TextureAtlas charset;

        charset = new TextureAtlas(Gdx.files.internal("./characters/"+characterName+"/attack1.atlas"));
        playerAnimations.add("attack1", new Animation<>(FRAME_TIME, charset.findRegions("attack1")));

        charset = new TextureAtlas(Gdx.files.internal("./characters/"+characterName+"/attack2.atlas"));
        playerAnimations.add("attack2", new Animation<>(FRAME_TIME, charset.findRegions("attack2")));

        charset = new TextureAtlas(Gdx.files.internal("./characters/"+characterName+"/attack3.atlas"));
        playerAnimations.add("attack3", new Animation<>(FRAME_TIME, charset.findRegions("attack3")));

        charset = new TextureAtlas(Gdx.files.internal("./characters/"+characterName+"/climb.atlas"));
        playerAnimations.add("climb", new Animation<>(FRAME_TIME, charset.findRegions("climb")));

        charset = new TextureAtlas(Gdx.files.internal("./characters/"+characterName+"/craft.atlas"));
        playerAnimations.add("craft", new Animation<>(FRAME_TIME, charset.findRegions("craft")));

        charset = new TextureAtlas(Gdx.files.internal("./characters/"+characterName+"/death.atlas"));
        playerAnimations.add("death", new Animation<>(FRAME_TIME, charset.findRegions("death")));

        charset = new TextureAtlas(Gdx.files.internal("./characters/"+characterName+"/fall.atlas"));
        playerAnimations.add("fall", new Animation<>(FRAME_TIME, charset.findRegions("fall")));

        charset = new TextureAtlas(Gdx.files.internal("./characters/"+characterName+"/hurt.atlas"));
        playerAnimations.add("hurt", new Animation<>(FRAME_TIME, charset.findRegions("hurt")));

        charset = new TextureAtlas(Gdx.files.internal("./characters/"+characterName+"/idle.atlas"));
        playerAnimations.add("idle", new Animation<>(FRAME_TIME, charset.findRegions("idle")));

        charset = new TextureAtlas(Gdx.files.internal("./characters/"+characterName+"/jump.atlas"));
        playerAnimations.add("jump", new Animation<>(FRAME_TIME, charset.findRegions("jump")));

        charset = new TextureAtlas(Gdx.files.internal("./characters/"+characterName+"/push.atlas"));
        playerAnimations.add("push", new Animation<>(FRAME_TIME, charset.findRegions("push")));

        charset = new TextureAtlas(Gdx.files.internal("./characters/"+characterName+"/run.atlas"));
        playerAnimations.add("run", new Animation<>(FRAME_TIME, charset.findRegions("run")));

        charset = new TextureAtlas(Gdx.files.internal("./characters/"+characterName+"/walk.atlas"));
        playerAnimations.add("walk", new Animation<>(FRAME_TIME, charset.findRegions("walk")));
    }

    // Setters

    public void setPlayerState(State state) {
        this.state = state;
    }

    public void setFacingLeft() {
        direction = "LEFT";
    }

    public void setFacingRight() {
        direction = "RIGHT";
    }

    public void setVelocityX(float velocityValue) {
        velocityX = velocityValue;
    }

    public void setVelocityY(float velocityValue) {
        velocityX = velocityValue;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setPlayerSpeed(float speed) {
        this.playerSpeed = speed;
    }

    public void setPlayerStamina(int stamina) {
        this.playerStamina = stamina;
    }

    public void setRespawnPosition(Vector2 respawnPosition) {this.respawnPosition = respawnPosition;}

    public void setCollectedCoins(int collectedCoins) {this.collectedCoins = collectedCoins;}

    // Getters

    public int getHealth(){
        return health;
    }
    public float getVelocityX() {
        return velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public float getPlayerSpeed() {
        return playerSpeed;
    }

    public int getPlayerStamina() {
        return playerStamina;
    }

    public int getPlayerMaxStamina() {
        return maxPlayerStamina;
    }

    public State getPlayerState() {
        return state;
    }

    public Vector2 getRespawnPosition() {
        return respawnPosition;
    }

    public AnimationHandler getPlayerAnimations(){
        return playerAnimations;
    }

    public State getState(){
        return state;
    }

    public int getCollectedCoins() {return collectedCoins;}
}

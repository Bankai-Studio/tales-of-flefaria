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
    private int damageToEnemy; //danni al nemico
    private int minDmg = 50;  //50 di attacco
    private int maxDmg = 150; //150 di attacco
    private int player_health = 3; //3 vite all'inizio del livello
    private int damageValue;

    // Constants
    private int maxPlayerStamina = 100;

    // Player States
    public enum State {
        IDLE,
        WALKING,
        RUNNING,
        JUMPING,
        DYING
    }

    // Variables
    private State state;
    private int playerStamina;
    private Vector2 respawnPosition;
    private boolean canRespawn;
    private final AnimationHandler playerAnimations;
    private final float FRAME_TIME = 1 / 6f;
    private String direction;

    public Player(float width, float height, Body body) {
        super(width, height, body);
        playerSpeed = 8f;
        playerStamina = maxPlayerStamina;
        canRespawn = true;
        playerAnimations = new AnimationHandler();

        direction = "RIGHT";
        loadPlayerSprites();

        respawnPosition = new Vector2(body.getPosition().x, body.getPosition().y);
        body.setUserData(this);
    }

    @Override
    public void update(float delta) {
        checkPlayerDeath();
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = playerAnimations.getFrame();

        if(direction.equals("LEFT") && !currentFrame.isFlipX()) currentFrame.flip(true,false);
        if(direction.equals("RIGHT") && currentFrame.isFlipX()) currentFrame.flip(true,false);

        float tX = x-15f, tY = y-17f;
        if(direction.equals("LEFT")) tX -= 17f;
        batch.begin();
            batch.draw(currentFrame, tX, tY, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
        batch.end();
    }


    public void attackEnemy(Enemy enemy){
        damageToEnemy = (int)(Math.random()*(maxDmg-minDmg+1)+minDmg);
        damageValue = damageToEnemy;
        enemy.getDamaged(damageValue);
    }
    public void playerGetDamaged(int damageV){
        player_health -= damageV;
        if(health == 0)
            state = State.DYING;
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

    private void loadPlayerSprites() {
        TextureAtlas charset = new TextureAtlas(Gdx.files.internal("./characters/Woodcutter/walk.atlas"));
        playerAnimations.add("walk", new Animation<>(FRAME_TIME, charset.findRegions("walk")));

        charset = new TextureAtlas(Gdx.files.internal("./characters/Woodcutter/jump.atlas"));
        playerAnimations.add("jump", new Animation<>(FRAME_TIME, charset.findRegions("jump")));

        charset = new TextureAtlas(Gdx.files.internal("./characters/Woodcutter/run.atlas"));
        playerAnimations.add("run", new Animation<>(FRAME_TIME, charset.findRegions("run")));

        charset = new TextureAtlas(Gdx.files.internal("./characters/Woodcutter/idle.atlas"));
        playerAnimations.add("idle", new Animation<>(FRAME_TIME, charset.findRegions("idle")));

        playerAnimations.setCurrent("idle");
        state = State.IDLE;
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

    // Getters

    public float getHealth(){
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
}

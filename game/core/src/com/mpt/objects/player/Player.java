package com.mpt.objects.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mpt.objects.GameEntity;
import com.mpt.objects.enemy.Enemy;

public class Player extends GameEntity {
    private float damageToEnemy; //danni al nemico
    private float minDmg = 50;  //50 di attacco
    private float maxDmg = 150; //150 di attacco
    private float player_health = 3; //3 vite all'inizio del livello
    private boolean playerIsDead = false;
    private float damageValue;

    // Constants
    private final int maxPlayerStamina = 100;

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
    private Vector2 respawnPosition;
    private int playerStamina;
    private int playerHealth;
    private boolean canRespawn;

    public Player(float width, float height, Body body) {
        super(width, height, body);
        state = State.IDLE;
        playerSpeed = 8f;
        playerStamina = maxPlayerStamina;
        playerHealth = 3;
        canRespawn = true;
        playerIsDead = false;
        player_health = 3f;

        respawnPosition = new Vector2(body.getPosition().x, body.getPosition().y);
        body.setUserData(this);
    }

    @Override
    public void update(float delta) {
        checkPlayerDeath();
    }

    @Override
    public void render(SpriteBatch batch) {}

    private void checkPlayerDeath() {
        if(state.equals(State.DYING) && canRespawn) {
            body.setTransform(respawnPosition.x, respawnPosition.y, body.getAngle());
            state = State.IDLE;
            canRespawn = false;
        }
        else
            canRespawn = true;
    }

    // Setters

    public void setPlayerState(State state) {
        this.state = state;
    }

    public void setFacingLeft() {}

    public void setFacingRight() {}

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
        return player_health;
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
}

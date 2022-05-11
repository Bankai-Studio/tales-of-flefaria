package com.mpt.objects.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.mpt.objects.GameEntity;
import com.mpt.objects.enemy.Enemy;

public class Player extends GameEntity {
    public boolean playerDead = false;
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

    public Player(float width, float height, Body body) {
        super(width, height, body);

        state = State.IDLE;
        playerSpeed = 8f;
        playerStamina = maxPlayerStamina;
    }

    @Override
    public void update(float delta) {}

    @Override
    public void render(SpriteBatch batch) {}
    public void attackEnemy(Enemy enemy){
        damageToEnemy = (int)(Math.random()*(maxDmg-minDmg+1)+minDmg);
        damageValue = damageToEnemy;
        enemy.getDamaged(damageValue);
    }
    public void playerGetDamaged(int damageV){
        player_health -= damageV;
        if(health == 0)
            playerDead = true;
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

    // Getters

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

}

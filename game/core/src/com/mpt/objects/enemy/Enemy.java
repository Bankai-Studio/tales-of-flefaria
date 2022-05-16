package com.mpt.objects.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.mpt.objects.player.Player;
import com.mpt.objects.GameEntity;

public class Enemy extends GameEntity{
    final double damageValueToPlayer = 0.5;
    private float enemyHealth;
    private float actuallyPosX = body.getPosition().x;
    private float actuallyPosY = body.getPosition().y;
    private int maxHealth = 350;
    private int minHealth = 150;
    private float initialPosX; //initial xPos of slime
    private float initialPosY; //initial yPos of slime
    private int killCounter = 0; //player's frags
    final private float distance = 2f;
    private int damageToPlayer;
    private boolean playerHasBeenSpotted;
    private boolean playerHasBeenDefeat;
    private float xMaxLimitDX; //limit position on right side
    private float xMaxLimitSX; //limit position on left side
    private boolean switchDirectionToRight = false; //flag that says if enemy switched direction
    private boolean switchDirectionToLeft = false;
    private float walkSpeed = 1f;
    private float x = body.getPosition().x;
    private float velX;

    // Enemy States
    public enum EnemyState {
        WALKING,
        ATTACKING,
        DYING
    }
    private EnemyState enemyState;
    protected String direction;
    public Enemy(float width, float height, Body body) {
        super(width, height, body);
        health = (int)(Math.random()*(maxHealth-minHealth+1)+minHealth);
        initialPosX = body.getPosition().x; //initial position of enemy
        initialPosY = body.getPosition().y; //initial position of enemy
        playerHasBeenSpotted = false;
        playerHasBeenDefeat = false;
        direction = "RIGHT";
    }
    @Override
    public void update(float delta) {}

    @Override
    public void render(SpriteBatch batch) {}

    public void getDamaged(float damage){
        enemyHealth = -damage;
        if(enemyHealth <= 0)
            enemyState = EnemyState.DYING;
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
        playerHasBeenSpotted = Math.abs(player.getBody().getPosition().x - body.getPosition().x) < distance;
        return playerHasBeenSpotted;
    }

    public void lurkTarget(Player player){
        if(playerSpotted(player) && !player.getPlayerState().equals(Player.State.DYING)){
            actuallyPosX = player.getBody().getPosition().x;
            player.playerGetDamaged(damageToPlayer);
        }
    }

    public void killCounter(Player player){
        if(enemyState.equals(EnemyState.DYING))
            killCounter += 1;
        if(player.getPlayerState().equals(Player.State.DYING))
            killCounter = 0;
    }
    public void setFacingLeft() {
        direction = "LEFT";
    }
    public void setFacingRight() {
        direction = "RIGHT";
    }
}

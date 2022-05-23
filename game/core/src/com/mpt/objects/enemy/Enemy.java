package com.mpt.objects.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mpt.objects.player.Player;
import com.mpt.objects.GameEntity;
import com.mpt.platform.GameScreen;

import javax.swing.*;

public class Enemy extends GameEntity{
    public  int minDmg;
    public  int maxDmg;
    private float enemyHealth;
    private float actuallyPosX = body.getPosition().x;
    private float actuallyPosY = body.getPosition().y;
    //private GameScreen gameScreen;

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

    private float walkSpeed = 1f;

    private float x,y;

    // Enemy States
    public enum EnemyState {
        IDLE,
        WALKING,
        ATTACKING,
        DYING
    }
    protected EnemyState enemyState;
    protected String direction;
    public Enemy(float width, float height, Body body) {
        super(width, height, body);
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

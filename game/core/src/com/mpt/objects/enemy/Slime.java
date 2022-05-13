package com.mpt.objects.enemy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.mpt.objects.player.Player;
import com.mpt.platform.GameScreen;

public class Slime extends Enemy {
    private boolean setToDestroy = false; //boolean says if enemy still to be killed
    private boolean destroyed = false; //boolean says its dead
    private float xPos = 0; //initial xPos of slime
    private float yPos = 0; //initial yPos of slime
    private float xMaxLimitDX;
    private float xMaxLimitSX;

    private GameScreen gameScreen;
    private boolean switchDirectionToRight = false;
    private boolean switchDirectionToLeft = false;

    private boolean playerHasBeenSpotted = false;

    private boolean playerHasBeenDefeat = false;
    private float walkSpeed = 1f;

    private final float distanceFromPlayer = 2f;


    public Slime(float width, float height, Body body, GameScreen gameScreen) {
        super(width, height, body);
        xPos = body.getPosition().x; //initial position of slime
        yPos = body.getPosition().y; //initial position of slime
        setToDestroy = false;
        destroyed = false;
        playerHasBeenSpotted = false;
        playerHasBeenDefeat = false;
        this.gameScreen = gameScreen;
    }
    @Override
    public void update(float delta) {
        if(!playerSpotted(gameScreen.getPlayer()))
            movementSlime();
        else{
            //lurkPlayer(gameScreen.getPlayer());
            hitPlayer(gameScreen.getPlayer());
            hitByPlayer(gameScreen.getPlayer());
        }
    }

    @Override
    public void render(SpriteBatch batch) {

    }

    public void movementSlime() {
        xMaxLimitDX = xPos + 2.4f;
        xMaxLimitSX = xPos - 5f;
        switchDirectionToRight = true;
        if (body.getPosition().x < xMaxLimitDX && switchDirectionToRight)
            body.setLinearVelocity(walkSpeed * (3f), body.getLinearVelocity().y);
        else {
            switchDirectionToRight = false;
            switchDirectionToLeft = true;
        }
        if (switchDirectionToLeft && body.getPosition().x > xMaxLimitSX)
            body.setLinearVelocity(walkSpeed * (-3f), body.getLinearVelocity().y);
        else {
            switchDirectionToLeft = false;
            switchDirectionToRight = true;
        }
    }

    //function that return if player has been spotted by enemy
    public boolean playerSpotted(Player player) {
        System.out.println(Math.abs(player.getBody().getPosition().x - body.getPosition().x));
        if(Math.abs(player.getBody().getPosition().x - body.getPosition().x) < 1.5f)
            playerHasBeenSpotted = true;
        else
            playerHasBeenSpotted = false;
        return playerHasBeenSpotted;
    }

    //function that keep following the player
    /*public void lurkPlayer(Player player){
        if(playerSpotted(player))
            hitPlayer(player);
    }*/

    //function that damage player if he's still alive
    public void hitPlayer(Player player){
        attackPlayer(player);
        System.out.println("Player health:"+player.getHealth());

        if(player.getPlayerState().equals(Player.State.DYING))
            playerHasBeenDefeat = true;
    }

    //function which hit enemy by player
    public void hitByPlayer(Player player){
        if(!enemyIsDead() && playerSpotted(player)) {
            player.attackEnemy(this);
            System.out.println("Enemy health:"+ this.getEnemyHealth());
        }
    }


}

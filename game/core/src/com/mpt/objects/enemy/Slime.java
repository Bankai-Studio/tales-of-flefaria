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
    private float walkSpeed = 1;
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
      movementSlime();
      playerSpotted(gameScreen.getPlayer());
    }

    @Override
    public void render(SpriteBatch batch) {

    }

    public void movementSlime() {
        xMaxLimitDX = xPos + 2f;
        xMaxLimitSX = xPos - 6f;
        switchDirectionToRight = true;
        if (body.getPosition().x < xMaxLimitDX && switchDirectionToRight)
            body.setLinearVelocity(walkSpeed * (3), body.getLinearVelocity().y);
        else {
            switchDirectionToRight = false;
            switchDirectionToLeft = true;
        }
        if (switchDirectionToLeft && body.getPosition().x > xMaxLimitSX)
            body.setLinearVelocity(walkSpeed * (-3), body.getLinearVelocity().y);
        else {
            switchDirectionToLeft = false;
            switchDirectionToRight = true;
        }
    }

    //function that return if player has been spotted by enemy
    public boolean playerSpotted(Player player) {
        if(player.getBody().getPosition().x - xPos == 5)
            playerHasBeenSpotted = true;
        else
            playerHasBeenSpotted = false;
        return playerHasBeenSpotted;
    }

    //function that keep following the player
    public void lurkPlayer(Player player){
       // if(playerSpotted(player))

    }

    //function that damage player if he's still alive
    public void hitPlayer(Player player){
        if(!player.getPlayerState().equals(Player.State.DYING))
            attackPlayer(player);
        else
            playerHasBeenDefeat = true;
    }

    //function which hit enemy by player
    public void hitByPlayer(Player player){
        if(!enemyIsDead() && playerSpotted(player))
            player.attackEnemy(this);
        else
            setToDestroy = true;
    }


}

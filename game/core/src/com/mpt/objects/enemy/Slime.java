package com.mpt.objects.enemy;

import com.badlogic.gdx.physics.box2d.Body;
import com.mpt.objects.player.Player;

public class Slime extends Enemy {

    final static int rangeOfMovement = 450; //max position slime can reach

    private boolean setToDestroy = false; //boolean says if enemy still to be killed

    private boolean destroyed = false; //boolean says its dead

    private float xPos = 0; //initial xPos of slime

    private float yPos = 0; //initial yPos of slime

    private float xSlime = 0; //x of pos of slime

    private float velX = 10; //velocity of slime

    public Slime(float width, float height, Body body) {
        super(width, height, body);
        xPos = body.getPosition().x; //initial position of slime
        yPos = body.getPosition().y; //initial position of slime
        setToDestroy = false;
        destroyed = false;


    }


    public void movementSlime(){
        /*xSlime = xPos;
        if(xSlime > rangeOfMovement){ //if slime going right side, he'll go left side
            velX = -1; //decreasing position to left side
            //need sprite with opposite side
        }else
            velX = 1; //slime return walks on right side
        */
    }

    public void getHit(Player player){
        if(enemyIsDead())
            destroyed = true;
    }

    public void speedMove(){
        move(velX);
    }
}

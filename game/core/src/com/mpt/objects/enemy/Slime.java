package com.mpt.objects.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.mpt.objects.player.Player;

public class Slime extends Enemy {

    private boolean setToDestroy = false; //boolean says if enemy still to be killed

    private boolean destroyed = false; //boolean says its dead

    private float xPos = 0; //initial xPos of slime

    private float yPos = 0; //initial yPos of slime


    private float xMaxLimitDX;


    private float xMaxLimitSX;


    private boolean switchDirectionToRight = false;

    private boolean switchDirectionToLeft = false;

    private float walkSpeed = 1;
    public Slime(float width, float height, Body body) {
        super(width, height, body);
        xPos = body.getPosition().x; //initial position of slime
        yPos = body.getPosition().y; //initial position of slime
        setToDestroy = false;
        destroyed = false;


    }

    @Override
    public void update(float delta) {
      movementSlime();
    }

    @Override
    public void render(SpriteBatch batch) {

    }


    public void movementSlime(){
        xMaxLimitDX = xPos + 1f;
        xMaxLimitSX = xPos - 1f;
        if(body.getPosition().x < xMaxLimitDX)
            body.setLinearVelocity(walkSpeed * (1), body.getLinearVelocity().y);
        else
            switchDirectionToLeft = true;
        if(switchDirectionToLeft && body.getPosition().x > xMaxLimitSX)
            body.setLinearVelocity(walkSpeed * (-1), body.getLinearVelocity().y);




        /*
        if(xPos <= xMaxLimitDX)
            body.setLinearVelocity(walkSpeed*-1,body.getLinearVelocity().y);
        if(xPos >= xMaxLimitSX)
            body.setLinearVelocity(walkSpeed*1,body.getLinearVelocity().y);
        System.out.println(body.getPosition().x + " " + xMaxLimitSX + " " + xMaxLimitDX);
        */
    }

    public void getHit(Player player){
        if(enemyIsDead()){
            destroyed = true;
            killCounter(player);
        }
        destroyed = false;
    }
}

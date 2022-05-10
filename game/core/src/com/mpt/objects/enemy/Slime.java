package com.mpt.objects.enemy;

import com.badlogic.gdx.physics.box2d.Body;

public class Slime extends Enemy {

    final static int maxPosX = -50;

    final static int maxPosY = 50; //same qui

    final static int rangeOfAction = 1; //per ora non fa nulla
    public Slime(float width, float height, Body body) {
        super(width, height, body);

    }

    public void movementSlime(){

    }

    public void getX() {
        float x;
        x = body.getPosition().x;
    }

    public void getY() {
        float y;
        y = body.getPosition().y;
    }
}

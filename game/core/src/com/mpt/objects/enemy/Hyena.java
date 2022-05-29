package com.mpt.objects.enemy;

import com.badlogic.gdx.physics.box2d.Body;
import com.mpt.platform.GameScreen;

public class Hyena extends Enemy{
    public Hyena(float width, float height, Body body, GameScreen gameScreen) {
        super(width, height, body, gameScreen);
        adjustX =  - 35f;
        adjustY =  - 12f;
        walkSpeed = 1.5f;
        minDamage = 34;
        maxDamage = 50;
        health = 100;
        enemyName = "Hyena";
        loadSprites();
    }
}

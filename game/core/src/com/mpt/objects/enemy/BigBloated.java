package com.mpt.objects.enemy;

import com.badlogic.gdx.physics.box2d.Body;
import com.mpt.platform.GameScreen;

public class BigBloated extends Enemy {
    public BigBloated(float width, float height, Body body, GameScreen gameScreen) {
        super(width, height, body, gameScreen);
        walkSpeed = 1f;
        minDamage = 34;
        maxDamage = 50;
        health = 100;
        enemyName = "BigBloated";
        loadSprites();
    }
}

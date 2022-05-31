package com.mpt.objects.enemy;

import com.badlogic.gdx.physics.box2d.Body;
import com.mpt.platform.GameScreen;

public class TestingDummy extends Enemy {
    public TestingDummy(float width, float height, Body body, GameScreen gameScreen) {
        super(width, height, body, gameScreen);
        adjustX = -35f;
        adjustY = -20f;
        walkSpeed = 1f;
        minDamage = 0;
        maxDamage = 0;
        health = 100000000;
        enemyName = "Centipede";
        loadSprites();
    }
}

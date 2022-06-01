package com.mpt.objects.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.mpt.platform.GameScreen;

public class FinalBoss extends Enemy {
    private final Texture straightBulletDark;
    private final Texture straightBulletBright;
    private final Texture mortarStrikeDark;
    private final Texture mortarStrikeBright;
    public FinalBoss(float width, float height, Body body, GameScreen gameScreen) {
        super(width, height, body, gameScreen);
        adjustX = -35f;
        adjustY = -2f;
        walkSpeed = 1f;
        minDamage = 34;
        maxDamage = 50;
        health = 1000;
        enemyName = "FinalBoss";
        loadSprites();
        straightBulletDark = new Texture(Gdx.files.internal("./enemies/FinalBoss/Bullet1.png"));
        straightBulletBright = new Texture(Gdx.files.internal("./enemies/FinalBoss/Bullet2.png"));
        mortarStrikeDark = new Texture(Gdx.files.internal("./enemies/FinalBoss/Bullet3.png"));
        mortarStrikeBright = new Texture(Gdx.files.internal("./enemies/FinalBoss/Bullet4.png"));
    }

    public void disposeBullets(){
        straightBulletDark.dispose();
        straightBulletBright.dispose();
        mortarStrikeDark.dispose();
        mortarStrikeBright.dispose();
    }
}

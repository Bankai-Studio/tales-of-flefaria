package com.mpt.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class GameEntity {
    protected float x, y, velocityX, velocityY, playerSpeed;

    protected int health, damage;
    protected float width, height;
    protected Body body;

    public GameEntity(float width, float height, Body body) {
        this.x = body.getPosition().x;
        this.y = body.getPosition().y;
        this.width = width;
        this.height = height;
        this.body = body;
        this.velocityX = 0;
        this.velocityY = 0;
        this.playerSpeed = 0;
        this.health = 3;
        this.damage = 0;
    }

    public abstract void update(float delta);

    public abstract void render(SpriteBatch batch);

    public Body getBody() {
        return body;
    }
}

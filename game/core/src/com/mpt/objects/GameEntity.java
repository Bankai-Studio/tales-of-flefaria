package com.mpt.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.mpt.handlers.CombatHandler;

public abstract class GameEntity {

    protected float x, y, velocityX, velocityY;
    public int health, minDamage, maxDamage;
    protected float width, height;
    protected Body body;
    public CombatHandler combatHandler;

    public GameEntity(float width, float height, Body body) {
        x = body.getPosition().x;
        y = body.getPosition().y;
        this.width = width;
        this.height = height;
        this.body = body;
        velocityX = 0;
        velocityY = 0;
        combatHandler = new CombatHandler();
    }

    public abstract void update(float delta);
    public abstract void render(SpriteBatch batch);
    public Body getBody() {
        return body;
    }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
}

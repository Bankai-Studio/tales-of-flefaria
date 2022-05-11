package com.mpt.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class GameObject {
    protected float x, y;
    protected float width, height;

    protected Body body;

    public GameObject(float width, float height, Body body) {
        this.width = width;
        this.height = height;
        this.body = body;
        this.x = body.getPosition().x;
        this.y = body.getPosition().y;
    }

    public abstract void update(float delta);

    public abstract void render(SpriteBatch batch);

    public Body getBody() {
        return body;
    }
}

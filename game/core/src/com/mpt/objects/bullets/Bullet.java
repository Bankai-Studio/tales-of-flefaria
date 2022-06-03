package com.mpt.objects.bullets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mpt.modules.BodyModule;
import com.mpt.objects.GameObject;

import static com.mpt.constants.Constants.*;

public class Bullet extends GameObject {
    public final static float BULLET_WIDTH = 15;
    public final static float BULLET_HEIGHT = 9;
    public final static int DAMAGE = 50;
    public final  float SPEED = 10;
    private final Sprite sprite;
    private float direction;
    public boolean remove = false;
    private boolean hitByPlayer = false;

    public Bullet(Body body, Texture texture, float direction) {
        super(BULLET_WIDTH, BULLET_HEIGHT, body);
        sprite = new Sprite(texture);
        this.direction = direction;
        body.setUserData(this);
        body.setLinearVelocity(SPEED * direction, 0.416f);
    }

    public static Body createBody(float x, float y, World world) {
        return BodyModule.createBody(
                x * PPM,
                y * PPM,
                BULLET_WIDTH,
                BULLET_HEIGHT,
                false,
                true,
                0f,
                0f,
                BIT_BULLET,
                (short) (BIT_PLAYER | BIT_MAP | BIT_ENEMY),
                world
        );
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void render(SpriteBatch batch) {
        body.setLinearVelocity(body.getLinearVelocity().x, 0.416f);
        if (direction == 1f && !sprite.isFlipX()) sprite.flip(true, false);
        batch.draw(sprite, body.getPosition().x * PPM - width / 2, body.getPosition().y * PPM - width / 2, width, height);
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public void changeDirection(){
        direction = -direction;
        body.setLinearVelocity(SPEED * direction, 0.416f);
    }

    public boolean isHitByPlayer() {
        return hitByPlayer;
    }

    public void setHitByPlayer(boolean hitByPlayer) {
        this.hitByPlayer = hitByPlayer;
    }
}

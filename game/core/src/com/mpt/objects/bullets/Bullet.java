package com.mpt.objects.bullets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mpt.modules.BodyModule;
import com.mpt.objects.GameObject;
import com.mpt.platform.GameScreen;

import static com.mpt.constants.Constants.*;

public class Bullet extends GameObject {
    public final static float BULLET_WIDTH = 15;
    public final static float BULLET_HEIGHT = 9;
    public final int SPEED = 500;
    public final int DAMAGE = 50;
    private final Texture texture;
    private final GameScreen gameScreen;
    private final float angle;
    public boolean remove = false;

    public Bullet(Body body, Texture texture, GameScreen gameScreen, float angle) {
        super(BULLET_WIDTH, BULLET_HEIGHT, body);
        this.texture = texture;
        this.gameScreen = gameScreen;
        this.angle = angle;
        body.setUserData(this);
        body.setLinearVelocity(-10f, 0.416f);
    }

    public static Body createBody(float x, float y, World world) {
        Body body = BodyModule.createBody(
                x * PPM,
                y * PPM,
                BULLET_WIDTH,
                BULLET_HEIGHT,
                false,
                false,
                0f,
                0f,
                BIT_BULLET,
                (short) (BIT_PLAYER | BIT_MAP),
                world
        );
        return body;
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void render(SpriteBatch batch) {
        body.setLinearVelocity(body.getLinearVelocity().x, 0.416f);
        batch.draw(texture, body.getPosition().x * PPM - width / 2, body.getPosition().y * PPM - width / 2, width, height);
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }
}

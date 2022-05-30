package com.mpt.objects.checkpoint;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.mpt.objects.GameObject;

import static com.mpt.constants.Constants.PPM;

public class Checkpoint extends GameObject {

    private boolean isCheckpointClaimed;
    private final Texture textureUnclaimed;
    private final Texture textureClaimed;

    public Checkpoint(float width, float height, Body body) {
        super(width, height, body);
        isCheckpointClaimed = false;
        body.setUserData(this);
        textureUnclaimed = new Texture(Gdx.files.internal("./checkpoint/checkpointUnclaimed.png"));
        textureClaimed = new Texture(Gdx.files.internal("./checkpoint/checkpointClaimed.png"));
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void render(SpriteBatch batch) {
        if (isCheckpointClaimed)
            batch.draw(textureClaimed, body.getPosition().x * PPM - width / 2, body.getPosition().y * PPM - height / 2);
        else
            batch.draw(textureUnclaimed, body.getPosition().x * PPM - width / 2, body.getPosition().y * PPM - height / 2);
    }

    public boolean isCheckpointClaimed() {
        return isCheckpointClaimed;
    }

    public void setCheckpointClaimed() {
        isCheckpointClaimed = true;
    }

    public void setCheckpointUnclaimed() {
        isCheckpointClaimed = false;
    }
}

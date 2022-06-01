package com.mpt.objects.checkpoint;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.mpt.objects.GameObject;

import static com.mpt.constants.Constants.PPM;

public class Checkpoint extends GameObject {
    private boolean isCheckpointCurrent;
    private boolean isCheckpointClaimed;
    private final Texture textureUnclaimed;
    private final Texture textureClaimed;
    private final Texture textureDead;

    public Checkpoint(float width, float height, Body body) {
        super(width, height, body);
        isCheckpointClaimed = false;
        isCheckpointCurrent = false;
        body.setUserData(this);
        textureUnclaimed = new Texture(Gdx.files.internal("./checkpoint/checkpointUnclaimed.png"));
        textureClaimed = new Texture(Gdx.files.internal("./checkpoint/checkpointClaimed.png"));
        textureDead = new Texture(Gdx.files.internal("./checkpoint/checkpointDead.png"));
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!isCheckpointClaimed)
            batch.draw(textureUnclaimed, body.getPosition().x * PPM - width / 2, body.getPosition().y * PPM - height / 2);
        else if (isCheckpointCurrent)
            batch.draw(textureClaimed, body.getPosition().x * PPM - width / 2, body.getPosition().y * PPM - height / 2);
        else batch.draw(textureDead, body.getPosition().x * PPM - width / 2, body.getPosition().y * PPM - height / 2);
    }

    public void dispose(){
        textureUnclaimed.dispose();
        textureClaimed.dispose();
        textureDead.dispose();
    }

    public boolean isCheckpointClaimed() {
        return isCheckpointClaimed;
    }

    public boolean isCheckpointCurrent() {
        return isCheckpointClaimed;
    }

    public void setCheckpointClaimed() {
        isCheckpointClaimed = true;
    }

    public void setCheckpointCurrent(boolean isCheckpointCurrent) {
        this.isCheckpointCurrent = isCheckpointCurrent;
    }
}

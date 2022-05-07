package com.mpt.handlers;

import com.badlogic.gdx.physics.box2d.*;

import static com.mpt.constants.Constants.PPM;

public class BodyHandler {
    public BodyHandler() {}

    public static Body createBody(float x, float y, float width, float height, boolean isStatic, boolean isSensor, World world) {
        BodyDef bodyDef = new BodyDef();

        if(isStatic)
            bodyDef.type = BodyDef.BodyType.StaticBody;
        else
            bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set(x / PPM, y / PPM);
        bodyDef.fixedRotation = true;

        Body body = world.createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox((width / 2) / PPM, (height/2) / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.friction = 0;
        fixtureDef.isSensor = isSensor;

        body.createFixture(fixtureDef);
        polygonShape.dispose();
        return body;
    }
}

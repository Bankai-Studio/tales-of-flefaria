package com.mpt.modules;

import com.badlogic.gdx.physics.box2d.*;

import static com.mpt.constants.Constants.PPM;

public class BodyModule {
    public BodyModule() {}

    public static Body createBody(float x, float y, float width, float height, boolean isStatic, boolean isSensor, float friction, float restitution, World world) {
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
        fixtureDef.friction = friction;
        fixtureDef.isSensor = isSensor;
        fixtureDef.restitution = restitution;

        body.createFixture(fixtureDef);
        polygonShape.dispose();
        return body;
    }
}

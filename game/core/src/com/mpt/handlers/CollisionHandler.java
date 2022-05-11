package com.mpt.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.mpt.objects.checkpoint.Checkpoint;
import com.mpt.objects.player.Player;

public class CollisionHandler implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        System.out.println(fixtureA.getBody().getUserData());
        //Gdx.app.log("beginContact", "between " + fixtureA.toString() + " and " + fixtureB.toString());
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        //Gdx.app.log("endContact", "between " + fixtureA.toString() + " and " + fixtureB.toString());
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}

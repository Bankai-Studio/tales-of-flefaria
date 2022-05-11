package com.mpt.handlers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mpt.objects.checkpoint.Checkpoint;
import com.mpt.objects.player.Player;

public class CollisionHandler implements ContactListener {

    PreferencesHandler preferencesHandler;

    public CollisionHandler(PreferencesHandler preferencesHandler) {
        this.preferencesHandler = preferencesHandler;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        checkPlayerCheckpointCollision(fixtureA, fixtureB);
    }

    @Override
    public void endContact(Contact contact) {
        //Fixture fixtureA = contact.getFixtureA();
        //Fixture fixtureB = contact.getFixtureB();
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}

    private void checkPlayerCheckpointCollision(Fixture fixtureA, Fixture fixtureB) {
        if(fixtureA.getBody().getUserData() instanceof Player && fixtureB.getBody().getUserData() instanceof Checkpoint)
            setNewCheckpoint(fixtureA, fixtureB);
        else if(fixtureB.getBody().getUserData() instanceof Player && fixtureA.getBody().getUserData() instanceof Checkpoint)
            setNewCheckpoint(fixtureB, fixtureA);
    }

    private void setNewCheckpoint(Fixture fixtureA, Fixture fixtureB) {
        Player player = (Player) fixtureA.getBody().getUserData();
        Checkpoint checkpoint = (Checkpoint) fixtureB.getBody().getUserData();
        Vector2 checkpointPosition = new Vector2(checkpoint.getBody().getPosition().x - 2f, checkpoint.getBody().getPosition().y);
        if(!player.getRespawnPosition().equals(checkpointPosition)) {
            player.setRespawnPosition(checkpointPosition);
            preferencesHandler.setRespawnPosition(checkpointPosition);
        }
    }
}

package com.mpt.handlers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mpt.objects.checkpoint.Checkpoint;
import com.mpt.objects.interactables.Coin;
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

        checkCollision(fixtureA, fixtureB);
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

    private void checkCollision(Fixture fixtureA, Fixture fixtureB) {
        if(fixtureA.getBody().getUserData() instanceof Player && fixtureB.getBody().getUserData() instanceof Checkpoint)
            setNewCheckpoint(fixtureA, fixtureB);
        if(fixtureB.getBody().getUserData() instanceof Player && fixtureA.getBody().getUserData() instanceof Checkpoint)
            setNewCheckpoint(fixtureB, fixtureA);
        if(fixtureA.getBody().getUserData() instanceof Player && fixtureB.getBody().getUserData() instanceof Coin)
            collectCoin(fixtureA, fixtureB);
        if(fixtureB.getBody().getUserData() instanceof Player && fixtureA.getBody().getUserData() instanceof Coin)
            collectCoin(fixtureB, fixtureA);
    }

    private void setNewCheckpoint(Fixture fixtureA, Fixture fixtureB) {
        Player player = (Player) fixtureA.getBody().getUserData();
        Checkpoint checkpoint = (Checkpoint) fixtureB.getBody().getUserData();
        Vector2 checkpointPosition = new Vector2(checkpoint.getBody().getPosition().x - 2f, checkpoint.getBody().getPosition().y);
        if(!player.getRespawnPosition().equals(checkpointPosition) && !checkpoint.isCheckpointClaimed()) {
            player.setRespawnPosition(checkpointPosition);
            checkpoint.setCheckpointClaimed();
            preferencesHandler.setRespawnPosition(checkpointPosition);
        }
    }

    private void collectCoin(Fixture fixtureA, Fixture fixtureB) {
        Player player = (Player) fixtureA.getBody().getUserData();
        Coin coin = (Coin) fixtureB.getBody().getUserData();
        if(!coin.getIsCollected()){
            coin.setIsCollected(true);
            player.setCollectedCoins(player.getCollectedCoins() + 1);
        }
    }
}

package com.mpt.handlers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mpt.modules.MusicModule;
import com.mpt.objects.checkpoint.Checkpoint;
import com.mpt.objects.endpoint.Endpoint;
import com.mpt.objects.interactables.Coin;
import com.mpt.objects.interactables.GameOver;
import com.mpt.objects.interactables.Ghost;
import com.mpt.objects.interactables.KillBlock;
import com.mpt.objects.player.Player;
import com.mpt.platform.GameOverScreen;
import com.mpt.platform.GameScreen;
import com.mpt.platform.LoadingScreen;

public class CollisionHandler implements ContactListener {

    PreferencesHandler preferencesHandler;
    GameScreen gameScreen;

    public CollisionHandler(PreferencesHandler preferencesHandler, GameScreen gameScreen) {
        this.preferencesHandler = preferencesHandler;
        this.gameScreen = gameScreen;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        checkCollision(fixtureA, fixtureB);
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    private void checkCollision(Fixture fixtureA, Fixture fixtureB) {
        if (fixtureA.getBody().getUserData() instanceof Player && fixtureB.getBody().getUserData() instanceof Checkpoint)
            setNewCheckpoint(fixtureA, fixtureB);
        if (fixtureB.getBody().getUserData() instanceof Player && fixtureA.getBody().getUserData() instanceof Checkpoint)
            setNewCheckpoint(fixtureB, fixtureA);
        if (fixtureA.getBody().getUserData() instanceof Player && fixtureB.getBody().getUserData() instanceof Coin)
            collectCoin(fixtureA, fixtureB);
        if (fixtureB.getBody().getUserData() instanceof Player && fixtureA.getBody().getUserData() instanceof Coin)
            collectCoin(fixtureB, fixtureA);
        if (fixtureA.getBody().getUserData() instanceof Player && fixtureB.getBody().getUserData() instanceof KillBlock)
            collisionKillBlock(fixtureA);
        if (fixtureB.getBody().getUserData() instanceof Player && fixtureA.getBody().getUserData() instanceof KillBlock)
            collisionKillBlock(fixtureB);
        if (fixtureA.getBody().getUserData() instanceof Player && fixtureB.getBody().getUserData() instanceof Endpoint)
            endLevel(fixtureA, fixtureB);
        if (fixtureB.getBody().getUserData() instanceof Player && fixtureA.getBody().getUserData() instanceof Endpoint)
            endLevel(fixtureB, fixtureA);
        if (fixtureA.getBody().getUserData() instanceof Player && fixtureB.getBody().getUserData() instanceof Ghost)
            hideGhost(fixtureB);
        if (fixtureB.getBody().getUserData() instanceof Player && fixtureA.getBody().getUserData() instanceof Ghost)
            hideGhost(fixtureA);
        if (fixtureA.getBody().getUserData() instanceof Player && fixtureB.getBody().getUserData() instanceof GameOver)
            gameOver();
        if (fixtureB.getBody().getUserData() instanceof Player && fixtureA.getBody().getUserData() instanceof GameOver)
            gameOver();
    }

    private void setNewCheckpoint(Fixture fixtureA, Fixture fixtureB) {
        Player player = (Player) fixtureA.getBody().getUserData();
        Checkpoint checkpoint = (Checkpoint) fixtureB.getBody().getUserData();
        Vector2 checkpointPosition = new Vector2(checkpoint.getBody().getPosition().x, checkpoint.getBody().getPosition().y);
        if (!player.getRespawnPosition().equals(checkpointPosition) && !checkpoint.isCheckpointClaimed()) {
            MusicModule.getCheckPointMusic().play(0.1f);
            player.setRespawnPosition(checkpointPosition);
            checkpoint.setCheckpointClaimed();
            for(Checkpoint checkpointChecked : gameScreen.getCheckpoints())
                if (checkpointChecked.isCheckpointCurrent()) checkpointChecked.setCheckpointCurrent(false);
            checkpoint.setCheckpointCurrent(true);
        }
    }

    private void collectCoin(Fixture fixtureA, Fixture fixtureB) {
        Player player = (Player) fixtureA.getBody().getUserData();
        Coin coin = (Coin) fixtureB.getBody().getUserData();
        if (!coin.isCollected()) {
            coin.setIsCollected(true);
            MusicModule.getCollectCoinSound().play(0.3f);
            player.setCollectedCoins(player.getCollectedCoins() + 1);
            gameScreen.updateCoins(player.getCollectedCoins());
        }
    }

    private void collisionKillBlock(Fixture fixture) {
        Player player = (Player) fixture.getBody().getUserData();
        player.setPlayerState(Player.State.DYING);
        player.setPlayerHealth(0);
        MusicModule.getPlayerDeathSound().play(0.1f);
        player.getPlayerAnimations().setCurrent("death");
    }

    private void endLevel(Fixture fixtureA, Fixture fixtureB) {
        Player player = (Player) fixtureA.getBody().getUserData();
        Endpoint endpoint = (Endpoint) fixtureB.getBody().getUserData();
        MusicModule.getPortalSound().play(0.1f);
        ((Game) Gdx.app.getApplicationListener()).setScreen(new LoadingScreen(gameScreen));
    }

    private void hideGhost(Fixture fixture){
        Ghost ghost = (Ghost) fixture.getBody().getUserData();
        ghost.setTouched(true);
    }

    private void gameOver(){
        ((Game) Gdx.app.getApplicationListener()).setScreen(new GameOverScreen(gameScreen));
    }
}

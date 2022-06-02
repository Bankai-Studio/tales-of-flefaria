package com.mpt.handlers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mpt.modules.MusicModule;
import com.mpt.objects.block.Block;
import com.mpt.objects.bullets.Bullet;
import com.mpt.objects.enemy.Enemy;
import com.mpt.objects.interactables.*;
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
            endLevel();
        if (fixtureB.getBody().getUserData() instanceof Player && fixtureA.getBody().getUserData() instanceof Endpoint)
            endLevel();
        if (fixtureA.getBody().getUserData() instanceof Player && fixtureB.getBody().getUserData() instanceof Ghost)
            hideGhost(fixtureB);
        if (fixtureB.getBody().getUserData() instanceof Player && fixtureA.getBody().getUserData() instanceof Ghost)
            hideGhost(fixtureA);
        if (fixtureA.getBody().getUserData() instanceof Player && fixtureB.getBody().getUserData() instanceof GameOver)
            gameOver(fixtureB);
        if (fixtureB.getBody().getUserData() instanceof Player && fixtureA.getBody().getUserData() instanceof GameOver)
            gameOver(fixtureA);
        if (fixtureA.getBody().getUserData() instanceof Enemy && (fixtureB.getBody().getUserData() instanceof Block || fixtureB.getBody().getUserData() instanceof Box))
            collisionWithBlock(fixtureA);
        if (fixtureB.getBody().getUserData() instanceof Enemy && (fixtureA.getBody().getUserData() instanceof Block || fixtureA.getBody().getUserData() instanceof Box))
            collisionWithBlock(fixtureB);
        if (fixtureA.getBody().getUserData() instanceof Player && fixtureB.getBody().getUserData() instanceof Block)
            gameScreen.getMovementHandler().calculateFallingDamage();
        if (fixtureB.getBody().getUserData() instanceof Player && fixtureA.getBody().getUserData() instanceof Block)
            gameScreen.getMovementHandler().calculateFallingDamage();
        if (fixtureA.getBody().getUserData() instanceof Player && fixtureB.getBody().getUserData() instanceof Bullet)
            hitPlayer(fixtureA, fixtureB);
        if (fixtureB.getBody().getUserData() instanceof Player && fixtureA.getBody().getUserData() instanceof Bullet)
            hitPlayer(fixtureB, fixtureA);
        if (fixtureA.getBody().getUserData() instanceof Bullet && fixtureB.getBody().getUserData() instanceof Block)
            destroyBullet((Bullet) fixtureA.getBody().getUserData());
        if (fixtureB.getBody().getUserData() instanceof Bullet && fixtureA.getBody().getUserData() instanceof Block)
            destroyBullet((Bullet) fixtureB.getBody().getUserData());
    }

    private void setNewCheckpoint(Fixture fixtureA, Fixture fixtureB) {
        Player player = (Player) fixtureA.getBody().getUserData();
        Checkpoint checkpoint = (Checkpoint) fixtureB.getBody().getUserData();
        Vector2 checkpointPosition = new Vector2(checkpoint.getBody().getPosition().x, checkpoint.getBody().getPosition().y);
        if (!player.getRespawnPosition().equals(checkpointPosition) && !checkpoint.isCheckpointClaimed()) {
            MusicModule.getCheckPointMusic().play(0.1f);
            player.setRespawnPosition(checkpointPosition);
            checkpoint.setCheckpointClaimed();
            for (Checkpoint checkpointChecked : gameScreen.getCheckpoints())
                if (checkpointChecked.isCheckpointCurrent()) checkpointChecked.setCheckpointCurrent(false);
            checkpoint.setCheckpointCurrent(true);
        }
    }

    private void collectCoin(Fixture fixtureA, Fixture fixtureB) {
        Player player = (Player) fixtureA.getBody().getUserData();
        Coin coin = (Coin) fixtureB.getBody().getUserData();
        if (!coin.isCollected()) {
            coin.setIsCollected(true);
            MusicModule.getCollectCoinSound().play(0.1f);
            player.setCollectedCoins(player.getCollectedCoins() + 1);
            gameScreen.updateCoins(player.getCollectedCoins());
        }
    }

    private void collisionKillBlock(Fixture fixture) {
        Player player = (Player) fixture.getBody().getUserData();
        player.setPlayerState(Player.State.DYING);
        player.setPlayerHealth(0);
        player.getPlayerAnimations().setCurrent("death");
        gameScreen.updateHealthBar();
    }

    private void endLevel() {
        MusicModule.getPortalSound().play(0.1f);
        ((Game) Gdx.app.getApplicationListener()).setScreen(new LoadingScreen(gameScreen));
    }

    private void hideGhost(Fixture fixture) {
        Ghost ghost = (Ghost) fixture.getBody().getUserData();
        if (!ghost.isTouched()) {
            ghost.setTouched(true);
            MusicModule.getGhostSound().setVolume(0.4f);
            MusicModule.getGhostSound().play();
        }
    }

    private void gameOver(Fixture fixture) {
        GameOver gameOver = (GameOver) fixture.getBody().getUserData();
        if (gameOver.isVisible()) ((Game) Gdx.app.getApplicationListener()).setScreen(new GameOverScreen(gameScreen));
    }

    private void collisionWithBlock(Fixture fixture) {
        Enemy enemy = (Enemy) fixture.getBody().getUserData();
        enemy.getBody().setLinearVelocity(0f, enemy.getBody().getLinearVelocity().y);
    }

    private void hitPlayer(Fixture fixtureA, Fixture fixtureB) {
        Player player = (Player) fixtureA.getBody().getUserData();
        Bullet bullet = (Bullet) fixtureB.getBody().getUserData();
        player.setPlayerHealth(Math.max(player.getHealth() - bullet.DAMAGE, 0));
        if (player.getHealth() <= 0) {
            player.setPlayerState(Player.State.DYING);
            player.getPlayerAnimations().setCurrent("death", false);
        } else {
            player.setPlayerState(Player.State.HURT);
            player.getPlayerAnimations().setCurrent("hurt", false);
        }
        destroyBullet(bullet);
    }

    private void destroyBullet(Bullet bullet) {
        bullet.setRemove(true);
    }
}

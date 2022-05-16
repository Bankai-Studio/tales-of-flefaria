package com.mpt.objects.enemy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.mpt.objects.player.Player;
import com.mpt.platform.GameScreen;

public class Slime extends Enemy {
    private boolean setToDestroy = false; //boolean says if enemy still to be killed
    private boolean destroyed = false; //boolean says its dead
    private float xPos = 0; //initial xPos of slime
    private float yPos = 0; //initial yPos of slime
    private GameScreen gameScreen;
    private final float distanceFromPlayer = 2f;

    public Slime(float width, float height, Body body, GameScreen gameScreen) {
        super(width, height, body);
        xPos = body.getPosition().x; //initial position of slime
        yPos = body.getPosition().y; //initial position of slime
        this.gameScreen = gameScreen;
    }

    @Override
    public void update(float delta) {
        if(!playerSpotted(gameScreen.getPlayer())) {
            enemyMovements();
            System.out.println("players has NOT been spotted");
        }else {
            lurkTarget(gameScreen.getPlayer());
            System.out.println("player's health"+gameScreen.getPlayer().getHealth());
        }
    }

    @Override
    public void render(SpriteBatch batch) {

    }
}

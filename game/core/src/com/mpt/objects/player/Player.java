package com.mpt.objects.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import static com.mpt.constants.Constants.PPM;

public class Player extends GameEntity{

    private int jumpCounter;
    private boolean running;
    public boolean playerDead = false;
    private int damageToEnemy; //danni al nemico
    private int minDmg = 50;  //50 di attacco
    private int maxDmg = 150; //150 di attacco
    private int player_health;
    private int damageValue;

    public Player(float width, float height, Body body) {
        super(width, height, body);
        this.speed = 10f;
        this.jumpCounter = 0;
        player_health = 3; //3 vite all'inizio del livello
        damageToEnemy = (int)(Math.random()*(maxDmg-minDmg+1)+minDmg);
    }

    public void attackEnemy(Enemy enemy){
        damageValue = damageToEnemy;
        enemy.getDamaged(damageValue);
    }

    public void playerGetDamaged(int damageV){
        player_health -= damageV;
        if(health == 0)
            playerDead = true;
    }

    @Override
    public void update() {
        x = body.getPosition().x * PPM;
        y = body.getPosition().y * PPM;

        checkUserInput();
    }

    @Override
    public void render(SpriteBatch batch) {

    }

    private void checkUserInput() {
        velX = 0;
        if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))
            running = true;
        else
            running = false;
        if(Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            velX = 1;
        if(Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))
            velX = -1;
        if((!running || body.getLinearVelocity().y != 0) && speed > 10f) speed-=0.2f;
        if((running && body.getLinearVelocity().y == 0) && speed<= 16f) speed += 0.4f;
        if((Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) && jumpCounter < 2) {
            float force = body.getMass() * 9;
            body.setLinearVelocity(body.getLinearVelocity().x, 0);
            body.applyLinearImpulse(new Vector2(0, force), body.getPosition(), true);
            jumpCounter++;
        }

        if(body.getLinearVelocity().y == 0)
            jumpCounter = 0;

        body.setLinearVelocity(velX * speed, body.getLinearVelocity().y < 25 ? body.getLinearVelocity().y : 25);
    }

}

package com.mpt.objects.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.mpt.objects.player.Player;
import com.mpt.objects.GameEntity;

public class Enemy extends GameEntity {
    final double damageValueToPlayer = 0.5;
    private int health = 0;
    private int maxHealth = 350;
    private int minHealth = 150;
    private int kill_counter;
    private int damageToPlayer;
    private float x = body.getPosition().x;
    private float velX;
    private boolean isDead = false;

    public Enemy(float width, float height, Body body) {
        super(width, height, body);
        health = (int)(Math.random()*(maxHealth-minHealth+1)+minHealth);
    }
    @Override
    public void update(float delta) {}

    @Override
    public void render(SpriteBatch batch) {}

    public void getDamaged(int damage){
        health = -damage;
        if(health <= 0)
            isDead = true;
    }

    boolean enemyIsDead(){
        if(isDead)
            return true;
        return false;
    }

    public void killCounter(Player player){
        if(isDead)
            kill_counter += 1;
        if(player.getPlayerState().equals(Player.State.DYING))
            kill_counter = 0;
    }

    public void attackPlayer(Player player){
        player.playerGetDamaged(damageToPlayer);
    }

    public float getEnemyHealth(){
        return health;
    }

}

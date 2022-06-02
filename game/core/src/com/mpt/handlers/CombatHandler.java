package com.mpt.handlers;

import com.mpt.objects.GameEntity;
import com.mpt.objects.enemy.Enemy;
import com.mpt.objects.player.Player;

public class CombatHandler {

    public static void attack(GameEntity attacker, GameEntity victim) {
        Player player;
        Enemy enemy;

        if (attacker instanceof Player && victim instanceof Enemy) {
            player = (Player) attacker;
            enemy = (Enemy) victim;
            float damage = (float) (Math.random() * (player.maxDamage - player.minDamage + 1) + player.minDamage);
            if (player.getPlayerAnimations().isCurrent("heavyAttack")) damage *= player.HEAVY_ATTACK_MULTIPLIER;
            int health = enemy.getHealth() - (int) damage;
            enemy.setHealth(Math.max(health, 0));
            if (health <= 0) {
                enemy.setEnemyState(Enemy.EnemyState.DYING);
                enemy.getAnimationHandler().setCurrent("death", false);
            } else {
                enemy.setEnemyState(Enemy.EnemyState.HURT);
                enemy.getAnimationHandler().setCurrent("hurt", false);
            }
        } else if (attacker instanceof Enemy && victim instanceof Player) {
            enemy = (Enemy) attacker;
            player = (Player) victim;
            int damage = (int) (Math.random() * (enemy.maxDamage - enemy.minDamage + 1) + enemy.minDamage);
            int health = player.getHealth() - damage;
            player.setPlayerHealth(Math.max(health, 0));
            if (health <= 0) {
                player.setPlayerState(Player.State.DYING);
                player.getPlayerAnimations().setCurrent("death", false);
            } else {
                player.setPlayerState(Player.State.HURT);
                player.getPlayerAnimations().setCurrent("hurt", false);
            }
        }
    }

}
package com.mpt.handlers;

import com.mpt.objects.GameEntity;
import com.mpt.objects.enemy.Enemy;
import com.mpt.objects.player.Player;

public class CombatHandler {
    public CombatHandler() {};

    public static void attack(GameEntity attacker, GameEntity victim) {
        Player player;
        Enemy enemy;

        if(attacker instanceof Player && victim instanceof Enemy) {
            player = (Player) attacker;
            enemy = (Enemy) victim;
            int damagePlayer = (int)(Math.random()*(player.maxDamage - player.minDamage + 1) + player.minDamage);
            // Codice player attacca nemico
            int health = enemy.getHealth()-damagePlayer;
            enemy.setHealth(health);
            if(health<=0){
                enemy.setEnemyState(Enemy.EnemyState.DYING);
            }

        }
        else if(attacker instanceof Enemy && victim instanceof Player) {
            enemy = (Enemy) attacker;
            player = (Player) victim;

            int damageEnemy = (int)(Math.random()*(enemy.maxDamage - enemy.minDamage + 1) + enemy.minDamage);
            // Codice nemico attacca player
            int health = (int) (player.getHealth()-damageEnemy);
            enemy.setHealth(health);
            if(health<=0){
                player.setPlayerState(Player.State.DYING);
            }
        }
    }

}
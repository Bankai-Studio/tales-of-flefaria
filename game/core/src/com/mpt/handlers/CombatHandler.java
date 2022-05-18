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
            int damagePlayer = (int)(Math.random()*(player.MAX_DMG - player.MIN_DMG + 1) + player.MIN_DMG);
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

            int damageEnemy = (int)(Math.random()*(enemy.maxDmg - enemy.minDmg + 1) + enemy.minDmg);
            // Codice nemico attacca player
            int health = (int) (player.getHealth()-damageEnemy);
            enemy.setHealth(health);
            if(health<=0){
                player.setPlayerState(Player.State.DYING);
            }
        }
    }

}
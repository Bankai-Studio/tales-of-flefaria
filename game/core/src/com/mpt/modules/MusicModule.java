package com.mpt.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class MusicModule {
    //Musics
    private static Music mainMenuMusic;
    private static Music gameOverSound;
    //Sounds
    private static Sound footStepsSound;
    private static Sound rolloverSound;
    private static Sound collectCoinSound;
    private static Sound checkPointSound;
    private static Sound jumpSound;
    private static Sound doubleJumpSound;
    private static Sound portalSound;

    private static Sound playerDeathSound;
    private static Music enemyAttackSound;

    public static void setup() {
        mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/Poseidon's Realm.wav"));
        rolloverSound = Gdx.audio.newSound(Gdx.files.internal("audio/ui_sfx/OptionRollover.wav"));
        collectCoinSound = Gdx.audio.newSound(Gdx.files.internal("audio/game_sfx/CollectCoin.mp3"));
        footStepsSound = Gdx.audio.newSound(Gdx.files.internal("audio/game_sfx/FootstepSound.ogg"));
        checkPointSound = Gdx.audio.newSound(Gdx.files.internal("audio/game_sfx/Checkpoint.ogg"));
        gameOverSound = Gdx.audio.newMusic(Gdx.files.internal("audio/game_sfx/GameoverSound.wav"));
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("audio/game_sfx/jumpSound.mp3"));
        doubleJumpSound = Gdx.audio.newSound(Gdx.files.internal("audio/game_sfx/doubleJumpSound.mp3"));
        portalSound = Gdx.audio.newSound(Gdx.files.internal("audio/game_sfx/portalSound.mp3"));
        playerDeathSound = Gdx.audio.newSound(Gdx.files.internal("audio/game_sfx/humanDeath.mp3"));
        enemyAttackSound = Gdx.audio.newMusic(Gdx.files.internal("audio/game_sfx/enemyAttack.mp3"));
    }

    public static Music getGameOverSound() {return gameOverSound;}
    public static Music getMainMenuMusic() {
        return mainMenuMusic;
    }

    public static Sound getFootStepsMusic() {
        return footStepsSound;
    }
    public static Sound getRolloverSound() {
        return rolloverSound;
    }

    public static Sound getJumpSound() {
        return jumpSound;
    }
    public static Sound getDoubleJumpSound() {
        return doubleJumpSound;
    }
    public static Sound getCollectCoinSound() {
        return collectCoinSound;
    }
    public static Sound getCheckPointMusic() {
        return checkPointSound;
    }

    public static Sound getPortalSound() {
        return portalSound;
    }
    public static Sound getPlayerDeathSound() {
        return playerDeathSound;
    }
    public static Music getEnemyAttackSound(){return enemyAttackSound;}
    public static void dispose(){
        enemyAttackSound.dispose();
        playerDeathSound.dispose();
        portalSound.dispose();
        jumpSound.dispose();
        doubleJumpSound.dispose();
        gameOverSound.dispose();
        mainMenuMusic.dispose();
        rolloverSound.dispose();
        collectCoinSound.dispose();
        footStepsSound.dispose();
    }

}

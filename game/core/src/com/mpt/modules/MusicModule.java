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
    private static Sound jumpSound1;
    private static Sound jumpSound2;
    private static Sound doubleJumpSound;
    private static Sound portalSound;
    private static Sound playerDeathSound;
    private static Music enemyAttackSound;
    private static Music welcomeToFlefaria;
    private static Music theForest;

    public static void setup() {
        mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/Poseidon's Realm.wav"));
        rolloverSound = Gdx.audio.newSound(Gdx.files.internal("audio/ui_sfx/OptionRollover.wav"));
        collectCoinSound = Gdx.audio.newSound(Gdx.files.internal("audio/game_sfx/CollectCoin.mp3"));
        footStepsSound = Gdx.audio.newSound(Gdx.files.internal("audio/game_sfx/FootstepSound.ogg"));
        checkPointSound = Gdx.audio.newSound(Gdx.files.internal("audio/game_sfx/Checkpoint.ogg"));
        gameOverSound = Gdx.audio.newMusic(Gdx.files.internal("audio/game_sfx/GameoverSound.wav"));
        jumpSound1 = Gdx.audio.newSound(Gdx.files.internal("audio/game_sfx/Jump1.mp3"));
        jumpSound2 = Gdx.audio.newSound(Gdx.files.internal("audio/game_sfx/Jump2.mp3"));
        doubleJumpSound = Gdx.audio.newSound(Gdx.files.internal("audio/game_sfx/doubleJumpSound.mp3"));
        portalSound = Gdx.audio.newSound(Gdx.files.internal("audio/game_sfx/portalSound.mp3"));
        playerDeathSound = Gdx.audio.newSound(Gdx.files.internal("audio/game_sfx/humanDeath.mp3"));
        enemyAttackSound = Gdx.audio.newMusic(Gdx.files.internal("audio/game_sfx/enemyAttack.mp3"));
        welcomeToFlefaria = Gdx.audio.newMusic(Gdx.files.internal("audio/Welcome to Flefaria.wav"));
        theForest = Gdx.audio.newMusic(Gdx.files.internal("audio/The Forest.mp3"));
    }

    public static Music getGameOverSound() {return gameOverSound;}

    public static Music getMainMenuMusic() {
        return mainMenuMusic;
    }

    public static Sound getFootStepsMusic() {
        return footStepsSound;
    }

    public static Sound getRolloverSound() {return rolloverSound;}

    public static Sound getJumpSound1() {
        return jumpSound1;
    }

    public static Sound getJumpSound2() {
        return jumpSound2;
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

    public static Music getWorldMusic(String worldName) {
        switch(worldName) {
            case "MapTutorial": return welcomeToFlefaria;
            case "Map1": return theForest;
            default: return null;
        }
    }

    public static void dispose(){
        enemyAttackSound.dispose();
        playerDeathSound.dispose();
        portalSound.dispose();
        jumpSound1.dispose();
        jumpSound2.dispose();
        doubleJumpSound.dispose();
        gameOverSound.dispose();
        mainMenuMusic.dispose();
        rolloverSound.dispose();
        collectCoinSound.dispose();
        footStepsSound.dispose();
    }

}

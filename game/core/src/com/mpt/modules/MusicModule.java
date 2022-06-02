package com.mpt.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;

public class MusicModule {
    //Musics
    private static Music mainMenuMusic;
    private static Music gameOverSound;
    //Sounds
    private static Sound rolloverSound;
    private static Sound collectCoinSound;
    private static Sound checkPointSound;
    private static Sound jumpSound1;
    private static Sound jumpSound2;
    private static Sound doubleJumpSound;
    private static Sound portalSound;
    private static Music stepSound;
    private static Sound playerDeathSound;
    private static Music snakeMovementSound;
    private static Music snakeAttackSound;
    private static Music ghostSound;
    private static Music enemyAttackSound;


    private static HashMap<String, Music> worldMusics;

    public static void setup() {
        mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/Poseidon's Realm.wav"));
        rolloverSound = Gdx.audio.newSound(Gdx.files.internal("audio/ui_sfx/OptionRollover.wav"));
        collectCoinSound = Gdx.audio.newSound(Gdx.files.internal("audio/game_sfx/CollectCoin.mp3"));
        checkPointSound = Gdx.audio.newSound(Gdx.files.internal("audio/game_sfx/Checkpoint.ogg"));
        gameOverSound = Gdx.audio.newMusic(Gdx.files.internal("audio/game_sfx/GameOverSound.wav"));
        jumpSound1 = Gdx.audio.newSound(Gdx.files.internal("audio/game_sfx/Jump1.mp3"));
        jumpSound2 = Gdx.audio.newSound(Gdx.files.internal("audio/game_sfx/Jump2.mp3"));
        doubleJumpSound = Gdx.audio.newSound(Gdx.files.internal("audio/game_sfx/doubleJumpSound.mp3"));
        portalSound = Gdx.audio.newSound(Gdx.files.internal("audio/game_sfx/portalSound.mp3"));
        playerDeathSound = Gdx.audio.newSound(Gdx.files.internal("audio/game_sfx/humanDeath.mp3"));
        enemyAttackSound = Gdx.audio.newMusic(Gdx.files.internal("audio/game_sfx/enemyAttack.mp3"));
        snakeAttackSound =  Gdx.audio.newMusic(Gdx.files.internal("audio/game_sfx/snakeAttackSound.mp3"));
        ghostSound =  Gdx.audio.newMusic(Gdx.files.internal("audio/game_sfx/ghostSound.mp3"));
        stepSound = Gdx.audio.newMusic(Gdx.files.internal("audio/game_sfx/stepSound.mp3"));
        worldMusics = new HashMap<>();
        worldMusics.put("MapTutorial", Gdx.audio.newMusic(Gdx.files.internal("audio/Welcome to Flefaria.wav")));
        worldMusics.put("Map1", Gdx.audio.newMusic(Gdx.files.internal("audio/The Forest.mp3")));
        worldMusics.put("Map2", Gdx.audio.newMusic(Gdx.files.internal("audio/The Underground.mp3")));
        worldMusics.put("Map3", Gdx.audio.newMusic(Gdx.files.internal("audio/The Ancient Valley.mp3")));
        worldMusics.put("Map4", Gdx.audio.newMusic(Gdx.files.internal("audio/Flying Islands.mp3")));
        worldMusics.put("Map5", Gdx.audio.newMusic(Gdx.files.internal("audio/The Depths.wav")));
    }


    public static Music getMainMenuMusic() {
        return mainMenuMusic;
    }


    public static Sound getRolloverSound() {return rolloverSound;}

    public static Sound getJumpSound1() {
        return jumpSound1;
    }

    public static Sound getJumpSound2() {
        return jumpSound2;
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
    public static Music getStepSound() {
        return stepSound;
    }

    public static Music getEnemyAttackSound(){return enemyAttackSound;}

    public static Music getWorldMusic(String worldName) {
        return worldMusics.get(worldName);
    }
    public static Music getSnakeAttackSound() {
        return snakeAttackSound;
    }
    public static Music getGhostSound() {
        return ghostSound;
    }

    public static void dispose(){
        ghostSound.dispose();
        stepSound.dispose();
        snakeAttackSound.dispose();
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
    }

}

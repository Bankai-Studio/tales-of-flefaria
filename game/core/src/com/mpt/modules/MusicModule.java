package com.mpt.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class MusicModule {
    private static Music mainMenuMusic;
    private static Sound rolloverSound;
    private static Sound collectCoinSound;

    public static void setup() {
        mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/Poseidon's Realm.wav"));
        rolloverSound = Gdx.audio.newSound(Gdx.files.internal("audio/ui_sfx/OptionRollover.wav"));
        collectCoinSound = Gdx.audio.newSound(Gdx.files.internal("audio/game_sfx/CollectCoin.mp3"));
    }

    public static Music getMainMenuMusic() {
        return mainMenuMusic;
    }

    public static Sound getRolloverSound() {
        return rolloverSound;
    }

    public static Sound getCollectCoinSound() {
        return collectCoinSound;
    }

    public static void dispose() {
        mainMenuMusic.dispose();
        rolloverSound.dispose();
        collectCoinSound.dispose();
    }
}

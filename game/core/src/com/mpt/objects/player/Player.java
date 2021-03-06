package com.mpt.objects.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mpt.handlers.AnimationHandler;
import com.mpt.modules.MusicModule;
import com.mpt.objects.GameEntity;
import com.mpt.platform.GameScreen;

public class Player extends GameEntity {
    // Constants
    private final int MAX_PLAYER_STAMINA = 100;
    private final float FRAME_TIME = 1 / 7f;
    public final float HEAVY_ATTACK_MULTIPLIER = 1.5f;
    private int characterSelection;
    private final AnimationHandler playerAnimations;

    // Player States
    public enum State {
        IDLE,
        WALKING,
        RUNNING,
        JUMPING,
        DYING,
        PUSHING,
        ATTACKING,
        HURT,
        FALLING,
        CLIMBING
    }

    // Variables
    private State state;
    private int playerStamina;
    private Vector2 respawnPosition;
    private String direction;
    private float playerSpeed;
    private int collectedCoins;
    GameScreen gameScreen;

    public Player(float width, float height, Body body, int character, GameScreen gameScreen) {
        super(width, height, body);
        this.gameScreen = gameScreen;
        minDamage = 34;
        maxDamage = 50;
        health = 100;
        playerSpeed = 8f;
        playerStamina = MAX_PLAYER_STAMINA;
        characterSelection = character;
        playerAnimations = new AnimationHandler();
        collectedCoins = 0;
        adjustX = -15f;
        adjustY = -17f;
        direction = "RIGHT";
        loadPlayerSprites();

        playerAnimations.setCurrent("idle");
        state = State.IDLE;

        respawnPosition = new Vector2(body.getPosition().x, body.getPosition().y);
        body.setUserData(this);
    }

    @Override
    public void update(float delta) {
        if(this.getPlayerState().equals(State.DYING)){
            MusicModule.getPlayerDeathSound().pause();
            MusicModule.getPlayerDeathSound().setVolume(0.4f);
            MusicModule.getPlayerDeathSound().play();

        }
        respawnOnVoidPosition();
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = playerAnimations.getFrame();

        if (direction.equals("LEFT") && !currentFrame.isFlipX()) currentFrame.flip(true, false);
        if (direction.equals("RIGHT") && currentFrame.isFlipX()) currentFrame.flip(true, false);

        float tX = x + adjustX, tY = y + adjustY;
        if (direction.equals("LEFT")) tX -= 17f;
        batch.draw(currentFrame, tX, tY, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
    }

    public void checkPlayerDeath() {
        if (state.equals(State.DYING)) {
            body.setTransform(respawnPosition.x, respawnPosition.y, body.getAngle());
            state = State.IDLE;
            playerAnimations.setCurrent("idle");
            health = 100;
            gameScreen.updateHealthBar();
        }
    }

    private void respawnOnVoidPosition() {
        if (body.getPosition().y < 0) {
            state = State.DYING;
        }
    }
    private void loadPlayerSprites() {
        String characterName;
        switch (characterSelection) {
            case 0:
            case 1:
                characterName = "Woodcutter";
                break;
            case 2:
                characterName = "SteamMan";
                break;
            case 3:
            case 4:
                characterName = "GraveRobber";
                break;
            default:
                throw new java.lang.Error("Player.java: characterSelection has ambiguous value");
        }
        TextureAtlas charset;

        charset = new TextureAtlas(Gdx.files.internal("characters/" + characterName + "/lightAttack.atlas"));
        playerAnimations.add("lightAttack", new Animation<>(1 / 8f, charset.findRegions("lightAttack")));
        textureAtlases.add(charset);

        charset = new TextureAtlas(Gdx.files.internal("characters/" + characterName + "/heavyAttack.atlas"));
        playerAnimations.add("heavyAttack", new Animation<>(1 / 6f, charset.findRegions("heavyAttack")));
        textureAtlases.add(charset);

        charset = new TextureAtlas(Gdx.files.internal("characters/" + characterName + "/climb.atlas"));
        playerAnimations.add("climb", new Animation<>(FRAME_TIME, charset.findRegions("climb")));
        textureAtlases.add(charset);

        charset = new TextureAtlas(Gdx.files.internal("characters/" + characterName + "/death.atlas"));
        playerAnimations.add("death", new Animation<>(1 / 5f, charset.findRegions("death")));
        textureAtlases.add(charset);

        charset = new TextureAtlas(Gdx.files.internal("characters/" + characterName + "/fall.atlas"));
        playerAnimations.add("fall", new Animation<>(FRAME_TIME, charset.findRegions("fall")));
        textureAtlases.add(charset);

        charset = new TextureAtlas(Gdx.files.internal("characters/" + characterName + "/hurt.atlas"));
        playerAnimations.add("hurt", new Animation<>(FRAME_TIME, charset.findRegions("hurt")));
        textureAtlases.add(charset);

        charset = new TextureAtlas(Gdx.files.internal("characters/" + characterName + "/idle.atlas"));
        playerAnimations.add("idle", new Animation<>(FRAME_TIME, charset.findRegions("idle")));
        textureAtlases.add(charset);

        charset = new TextureAtlas(Gdx.files.internal("characters/" + characterName + "/jump.atlas"));
        playerAnimations.add("jump", new Animation<>(FRAME_TIME, charset.findRegions("jump")));
        textureAtlases.add(charset);

        charset = new TextureAtlas(Gdx.files.internal("characters/" + characterName + "/push.atlas"));
        playerAnimations.add("push", new Animation<>(FRAME_TIME, charset.findRegions("push")));
        textureAtlases.add(charset);

        charset = new TextureAtlas(Gdx.files.internal("characters/" + characterName + "/run.atlas"));
        playerAnimations.add("run", new Animation<>(FRAME_TIME, charset.findRegions("run")));
        textureAtlases.add(charset);

        charset = new TextureAtlas(Gdx.files.internal("characters/" + characterName + "/walk.atlas"));
        playerAnimations.add("walk", new Animation<>(FRAME_TIME, charset.findRegions("walk")));
        textureAtlases.add(charset);
    }

    // Setters

    public void setPlayerState(State state) {
        this.state = state;
    }

    public void setFacingLeft() {
        direction = "LEFT";
    }

    public void setFacingRight() {
        direction = "RIGHT";
    }

    public void setVelocityX(float velocityValue) {
        velocityX = velocityValue;
    }

    public void setVelocityY(float velocityValue) {
        velocityY = velocityValue;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setPlayerSpeed(float speed) {
        this.playerSpeed = speed;
    }

    public void setPlayerStamina(int stamina) {
        this.playerStamina = stamina;
    }

    public void setRespawnPosition(Vector2 respawnPosition) {
        this.respawnPosition = respawnPosition;
    }

    public void setCollectedCoins(int collectedCoins) {
        this.collectedCoins = collectedCoins;
    }

    public void setPlayerHealth(int health) {
        this.health = health;
    }

    public void setCharacterSelection(int characterSelection) {
        this.characterSelection = characterSelection;
    }

    // Getters

    public int getHealth() {
        return health;
    }

    public float getVelocityX() {
        return velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public float getPlayerSpeed() {
        return playerSpeed;
    }

    public int getPlayerStamina() {
        return playerStamina;
    }

    public int getPlayerMaxStamina() {
        return MAX_PLAYER_STAMINA;
    }

    public State getPlayerState() {
        return state;
    }

    public Vector2 getRespawnPosition() {
        return respawnPosition;
    }

    public AnimationHandler getPlayerAnimations() {
        return playerAnimations;
    }

    public State getState() {
        return state;
    }

    public int getCollectedCoins() {
        return collectedCoins;
    }

}

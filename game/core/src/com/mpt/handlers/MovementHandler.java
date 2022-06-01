package com.mpt.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.mpt.modules.MusicModule;
import com.mpt.objects.enemy.Enemy;
import com.mpt.objects.interactables.Box;
import com.mpt.objects.interactables.Ladder;
import com.mpt.objects.player.Player;
import com.mpt.objects.player.Player.State;
import com.mpt.platform.GameScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.mpt.constants.Constants.PPM;

public class MovementHandler {

    // Constants
    private final float DEFAULT_SPEED = 8f;
    private final float DOUBLE_JUMP_REGEN_TIME = 0f;
    private final float STAMINA_REGEN_TIME = 0.03f;

    // Input Keys
    enum InputKeys {
        LEFT,
        RIGHT,
        SPACE,
        SHIFT
    }

    // Variables
    private int jumpCounter;
    private boolean isDoubleJumpReady;
    private boolean wasLastFrameYVelocityZero;
    private boolean isSprintReloading;
    private float doubleJumpTimer;
    private float staminaTimer;
    private boolean jumpedFromBox = false;
    private float fallingStartingY;
    private Map<InputKeys, Boolean> inputKeys = new HashMap<>();

    Player player;
    GameScreen gameScreen;
    AnimationHandler playerAnimations;

    public MovementHandler(Player player, GameScreen gameScreen) {
        this.player = player;
        this.gameScreen = gameScreen;
        jumpCounter = 0;
        isDoubleJumpReady = true;
        wasLastFrameYVelocityZero = false;
        isSprintReloading = false;
        doubleJumpTimer = 0f;
        staminaTimer = 0f;
        playerAnimations = player.getPlayerAnimations();

        inputKeys.put(InputKeys.LEFT, false);
        inputKeys.put(InputKeys.RIGHT, false);
        inputKeys.put(InputKeys.SPACE, false);
        inputKeys.put(InputKeys.SHIFT, false);

    }

    public void leftPressed() {
        inputKeys.put(InputKeys.LEFT, true);
    }

    public void rightPressed() {
        inputKeys.put(InputKeys.RIGHT, true);
    }

    public void spacePressed() {
        inputKeys.put(InputKeys.SPACE, true);
    }

    public void shiftPressed() {
        inputKeys.put(InputKeys.SHIFT, true);
    }

    public void leftReleased() {
        inputKeys.put(InputKeys.LEFT, false);
    }

    public void rightReleased() {
        inputKeys.put(InputKeys.RIGHT, false);
    }

    public void shiftReleased() {
        inputKeys.put(InputKeys.SHIFT, false);
    }

    public void spaceReleased() {
        inputKeys.put(InputKeys.SPACE, false);
    }

    public void update(float delta) {

        player.setX(player.getBody().getPosition().x * PPM);
        player.setY(player.getBody().getPosition().y * PPM);

        regenStamina(delta);
        reloadDoubleJump(delta);

        if (!player.getPlayerState().equals(State.DYING)) checkUserInput();
        else if (player.getPlayerAnimations().isFinished()) player.checkPlayerDeath();
        else player.getBody().setLinearVelocity(0, player.getBody().getLinearVelocity().y);
    }

    private void checkUserInput() {
        if (playerAnimations.isFinished() && player.getState().equals(State.JUMPING) && jumpedFromBox)
            jumpedFromBox = false;
        if (isPlayerNearALadder(player.getBody().getPosition()) && !player.getState().equals(State.HURT) && !player.getState().equals(State.DYING) && (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP))) {
            player.setPlayerState(State.CLIMBING);
            playerAnimations.setCurrent("climb");
            float CLIMBING_SPEED = 4f;
            player.setPlayerSpeed(CLIMBING_SPEED);
            jumpCounter = 0;
        }

        if (player.getState().equals(State.CLIMBING)) {
            ladderMovement();
            return;
        } else if (player.getVelocityY() != 0f)  player.setVelocityY(0f);

        if (player.getBody().getLinearVelocity().y != 0 && !isPlayerNearABox(player.getBody().getPosition()) && !player.getState().equals(State.JUMPING) && !player.getState().equals(State.FALLING) && !player.getState().equals(State.ATTACKING) && !player.getState().equals(State.DYING) && !player.getState().equals(State.HURT)) {
            fallingStartingY = player.getBody().getPosition().y;
            player.setPlayerState(State.FALLING);
            playerAnimations.setCurrent("fall", false);
        }

        if ((player.getPlayerState().equals(State.PUSHING) && !isPlayerNearABox(player.getBody().getPosition())) || (playerAnimations.isFinished() && (player.getState().equals(State.ATTACKING) || player.getState().equals(State.HURT) || player.getState().equals(State.DYING))) || (player.getBody().getLinearVelocity().y == 0 && wasLastFrameYVelocityZero && player.getState().equals(State.JUMPING))) {
            if (playerAnimations.isFinished() && player.getState().equals(State.ATTACKING))
                for (Enemy enemy : getNearEnemies(player.getBody().getPosition()))
                    if (!enemy.getEnemyState().equals(Enemy.EnemyState.DYING)) CombatHandler.attack(player, enemy);
            player.setPlayerState(State.IDLE);
            playerAnimations.setCurrent("idle");
        }
        if (isPlayerNearABox(player.getBody().getPosition()) && player.getBody().getLinearVelocity().y == 0 && !player.getState().equals(State.PUSHING) && !player.getState().equals(State.HURT)) {
            player.setPlayerState(State.PUSHING);
            playerAnimations.setCurrent("push");
        }

        if (inputKeys.get(InputKeys.LEFT) && !player.getPlayerState().equals(State.DYING)) {
            //MusicModule.getFootStepsMusic().play();
            player.setFacingLeft();
            if (player.getVelocityY() == 0f && (!player.getState().equals(State.RUNNING) || !inputKeys.get(InputKeys.SHIFT)) && !player.getState().equals(State.JUMPING) && !player.getState().equals(State.ATTACKING) && !inputKeys.get(InputKeys.RIGHT) && !player.getState().equals(State.PUSHING) && !player.getState().equals(State.FALLING) && !player.getState().equals(State.CLIMBING) && !player.getState().equals(State.HURT)) {
                playerAnimations.setCurrent("walk");
                player.setPlayerState(State.WALKING);
            }
            player.setVelocityX(-1);
        }
        if (inputKeys.get(InputKeys.RIGHT) && !player.getPlayerState().equals(State.DYING)) {
            //MusicModule.getFootStepsMusic().play();
            player.setFacingRight();
            if (player.getVelocityY() == 0 && (!player.getState().equals(State.RUNNING) || !inputKeys.get(InputKeys.SHIFT)) && !player.getState().equals(State.JUMPING) && !player.getState().equals(State.ATTACKING) && !inputKeys.get(InputKeys.LEFT) && !player.getState().equals(State.PUSHING) && !player.getState().equals(State.FALLING) && !player.getState().equals(State.CLIMBING) && !player.getState().equals(State.HURT)) {
                playerAnimations.setCurrent("walk");
                player.setPlayerState(State.WALKING);
            }
            player.setVelocityX(1);
        }
        if ((inputKeys.get(InputKeys.LEFT) && inputKeys.get(InputKeys.RIGHT)) || (!inputKeys.get(InputKeys.LEFT) && !inputKeys.get(InputKeys.RIGHT)) && !player.getPlayerState().equals(State.DYING)) {
            if (!player.getState().equals(State.JUMPING) && !player.getState().equals(State.ATTACKING) && !player.getState().equals(State.IDLE) && !player.getState().equals(State.FALLING) && !player.getState().equals(State.HURT)) {
                playerAnimations.setCurrent("idle");
                player.setPlayerState(State.IDLE);
            }
            player.setVelocityX(0);
        }
        if (inputKeys.get(InputKeys.SPACE) && !player.getPlayerState().equals(State.DYING) && !player.getState().equals(State.HURT)) {
            inputKeys.put(InputKeys.SPACE, false);
            if (player.getPlayerStamina() >= 10) {
                player.setPlayerState(State.JUMPING);
                if (jumpCounter == 0 || (jumpCounter == 1 && isDoubleJumpReady)) {
                    if (jumpCounter == 1) {
                        MusicModule.getJumpSound1().play(0.4f);
                        isDoubleJumpReady = false;
                        jumpedFromBox = false;
                        doubleJumpTimer = 0f;
                        playerAnimations.setCurrent("idle"); // To be able to reset the sprite
                    }
                    playerAnimations.setCurrent("jump", false);
                    player.setPlayerState(State.JUMPING);
                    MusicModule.getJumpSound2().play(0.4f);
                    fallingStartingY = player.getBody().getPosition().y;
                    float force = player.getBody().getMass() * 9;
                    player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
                    player.getBody().applyLinearImpulse(new Vector2(0, force), player.getBody().getPosition(), true);
                    jumpCounter++;
                    player.setPlayerStamina(player.getPlayerStamina() - 10);
                }
            }
        }

        if (inputKeys.get(InputKeys.SHIFT) && !player.getPlayerState().equals(State.DYING) && !player.getState().equals(State.JUMPING) && !player.getState().equals(State.FALLING) && !player.getState().equals(State.HURT)) {
            if (player.getPlayerStamina() == 0) {
                isSprintReloading = true;
                playerAnimations.setCurrent("walk");
                player.setPlayerState(State.WALKING);
            }
            if (player.getBody().getLinearVelocity().y == 0 && player.getPlayerSpeed() <= 14f && player.getPlayerStamina() > 0 && !isSprintReloading && !player.getState().equals(State.PUSHING))
                player.setPlayerSpeed(player.getPlayerSpeed() + 0.4f);
            if (player.getBody().getLinearVelocity().x != 0 && player.getBody().getLinearVelocity().y == 0 && player.getPlayerStamina() > 0 && !isSprintReloading)
                player.setPlayerStamina(player.getPlayerStamina() - 1);
            if (!player.getState().equals(State.RUNNING) && !player.getState().equals(State.JUMPING) && player.getVelocityX() != 0 && !isSprintReloading && !player.getState().equals(State.ATTACKING) && !player.getState().equals(State.PUSHING) && !player.getState().equals(State.CLIMBING)) {
                player.setPlayerState(State.RUNNING);
                playerAnimations.setCurrent("run");
            }
        }

        if (player.getPlayerState().equals(State.DYING)) player.setVelocityX(0);

        if (isSprintReloading && player.getPlayerStamina() == player.getPlayerMaxStamina())
            isSprintReloading = false;

        if ((!player.getPlayerState().equals(State.RUNNING) || player.getBody().getLinearVelocity().y != 0 || isSprintReloading) && player.getPlayerSpeed() > DEFAULT_SPEED)
            player.setPlayerSpeed(player.getPlayerSpeed() - 0.2f);

        if (isPlayerNearABox(player.getBody().getPosition()) && !player.getPlayerState().equals(State.IDLE) && !jumpedFromBox) {
            jumpedFromBox = true;
            jumpCounter = 0;
            playerAnimations.setCurrent("idle");
            player.setPlayerState(State.IDLE);
        }


        if (player.getBody().getLinearVelocity().y == 0) {
            if (wasLastFrameYVelocityZero) {
                jumpCounter = 0;
                if (player.getState().equals(State.FALLING)) {
                    playerAnimations.setCurrent("idle");
                    player.setPlayerState(State.IDLE);
                }
                fallingDamage();
            } else wasLastFrameYVelocityZero = true;
        } else wasLastFrameYVelocityZero = false;

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !player.getState().equals(State.WALKING) && !player.getState().equals(State.RUNNING) && !player.getState().equals(State.ATTACKING) && !player.getState().equals(State.PUSHING) && !player.getState().equals(State.CLIMBING)) {
            playerAnimations.setCurrent("lightAttack", false);
            player.setPlayerState(State.ATTACKING);
        }
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && !player.getState().equals(State.WALKING) && !player.getState().equals(State.RUNNING) && !player.getState().equals(State.ATTACKING) && !player.getState().equals(State.PUSHING) && !player.getState().equals(State.CLIMBING)) {
            playerAnimations.setCurrent("heavyAttack", false);
            player.setPlayerState(State.ATTACKING);
        }

        player.getBody().setLinearVelocity(player.getVelocityX() * player.getPlayerSpeed(), player.getBody().getLinearVelocity().y < 25 ? player.getBody().getLinearVelocity().y : 25);
    }

    private void ladderMovement() {
        player.setVelocityX(0f);
        player.setVelocityY(0.104f);
        player.getPlayerAnimations().setStopped(true);

        if ((Gdx.input.isKeyPressed(Input.Keys.SPACE))) {
            player.setPlayerState(State.JUMPING);
            player.getPlayerAnimations().setCurrent("jump", false);
            player.setPlayerSpeed(DEFAULT_SPEED);
        }
        if (!isPlayerNearALadder(player.getBody().getPosition())) {
            player.setPlayerState(State.FALLING);
            player.getPlayerAnimations().setCurrent("fall", false);
            player.setPlayerSpeed(DEFAULT_SPEED);
        }

        if (inputKeys.get(InputKeys.LEFT)) {
            player.setVelocityX(-1f);
            player.getPlayerAnimations().setStopped(false);
        }
        if (inputKeys.get(InputKeys.RIGHT)) {
            player.setVelocityX(1f);
            player.getPlayerAnimations().setStopped(false);
        }
        if (inputKeys.get(InputKeys.LEFT) && inputKeys.get(InputKeys.RIGHT)) {
            player.setVelocityX(0);
            player.getPlayerAnimations().setStopped(true);
        }
        if ((Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP))) {
            player.setVelocityY(1f);
            player.getPlayerAnimations().setStopped(false);
        }
        if ((Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN))) {
            if (isTheLadderOver(player.getBody().getPosition())) {
                player.setPlayerState(State.FALLING);
                player.getPlayerAnimations().setCurrent("fall", false);
                player.setPlayerSpeed(DEFAULT_SPEED);
                player.setVelocityY(0f);
            }
            player.setVelocityY(-1f);
            player.getPlayerAnimations().setStopped(false);
        }

        player.getBody().setLinearVelocity(player.getVelocityX() * player.getPlayerSpeed(), player.getVelocityY() * player.getPlayerSpeed());
    }

    private void regenStamina(float delta) {
        staminaTimer += delta;
        if ((!player.getPlayerState().equals(State.RUNNING) || player.getBody().getLinearVelocity().x == 0 || isSprintReloading) && player.getPlayerStamina() < player.getPlayerMaxStamina() && staminaTimer > STAMINA_REGEN_TIME) {
            player.setPlayerStamina(player.getPlayerStamina() + 1);
            staminaTimer = 0f;
        }
    }

    private void reloadDoubleJump(float delta) {
        doubleJumpTimer += delta;
        if (!isDoubleJumpReady && doubleJumpTimer > DOUBLE_JUMP_REGEN_TIME)
            isDoubleJumpReady = true;
    }

    private boolean isPlayerNearABox(Vector2 playerPosition) {
        ArrayList<Box> boxes = gameScreen.getBoxes();
        for (Box box : boxes) {
            Vector2 ladderPosition = box.getBody().getPosition();
            if ((Math.abs(playerPosition.x - ladderPosition.x) < (box.getWidth() / 2 / PPM + player.getWidth() / 2 / PPM + 0.02)) && (Math.abs(playerPosition.y - ladderPosition.y) < (box.getHeight() / 2 / PPM + player.getHeight() / 2 / PPM + 0.02)))
                return true;
        }
        return false;
    }

    private boolean isPlayerNearALadder(Vector2 playerPosition) {
        ArrayList<Ladder> ladders = gameScreen.getLadders();
        for (Ladder ladder : ladders) {
            Vector2 ladderPosition = ladder.getBody().getPosition();
            if (Math.abs(playerPosition.x - ladderPosition.x) < ladder.getWidth() / 2 / PPM && Math.abs(playerPosition.y - ladderPosition.y) < ladder.getHeight() / 2 / PPM)
                return true;
        }
        return false;
    }

    private ArrayList<Enemy> getNearEnemies(Vector2 playerPosition) {
        ArrayList<Enemy> nearEnemies = new ArrayList<>();
        ArrayList<Enemy> enemies = gameScreen.getEnemies();
        for (Enemy enemy : enemies) {
            Vector2 enemyPosition = enemy.getBody().getPosition();
            if (Math.abs(playerPosition.x - enemyPosition.x) < (enemy.getWidth() / PPM + player.getWidth() / PPM) && Math.abs(playerPosition.y - enemyPosition.y) < (enemy.getHeight() / 2 / PPM + player.getHeight() / PPM))
                nearEnemies.add(enemy);
        }
        return nearEnemies;
    }

    private boolean isTheLadderOver(Vector2 playerPosition) {
        ArrayList<Ladder> ladders = gameScreen.getLadders();
        for (Ladder ladder : ladders) {
            Vector2 ladderPosition = ladder.getBody().getPosition();
            if (Math.abs(playerPosition.x - ladderPosition.x) < ladder.getWidth() / 2 / PPM)
                if ((ladderPosition.y - ladder.getHeight() / 2 / PPM - 0.02f < playerPosition.y - player.getHeight() / 2 / PPM) && (ladderPosition.y - ladder.getHeight() / 2 / PPM + 0.02f > playerPosition.y - player.getHeight() / 2 / PPM))
                    return true;
        }
        return false;
    }

    private void fallingDamage() {
        float distance = player.getBody().getPosition().y - fallingStartingY;
        if (distance < -4f) {
            float damage = -distance + 4f + (float) Math.pow(1.2, -distance + 5f);
            float health = player.getHealth() - damage;
            player.setPlayerHealth((int) health);
            gameScreen.updateHealthLabel(player.getHealth(), (int) damage);
            if (health <= 0) {
                player.setPlayerState(Player.State.DYING);
                player.getPlayerAnimations().setCurrent("death", false);
                player.setPlayerHealth(0);
            } else {
                player.setPlayerState(Player.State.HURT);
                player.getPlayerAnimations().setCurrent("hurt", false);
            }
        }
        fallingStartingY = 0;
    }
}
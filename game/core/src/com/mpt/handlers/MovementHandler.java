package com.mpt.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
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
    private float doubleJumpRegenTime;
    private float staminaTimer;
    private float staminaRegenTime;
    private GameScreen gameScreen;
    private boolean jumpedFromBox = false;

    static Map<InputKeys, Boolean> inputKeys = new HashMap<>();
    static {
        inputKeys.put(InputKeys.LEFT, false);
        inputKeys.put(InputKeys.RIGHT, false);
        inputKeys.put(InputKeys.SPACE, false);
        inputKeys.put(InputKeys.SHIFT, false);
    }

    Player player;

    public MovementHandler(Player player, GameScreen gameScreen) {
        this.player = player;
        jumpCounter = 0;
        isDoubleJumpReady = true;
        wasLastFrameYVelocityZero = false;
        isSprintReloading = false;
        doubleJumpTimer = 0f;
        staminaTimer = 0f;
        doubleJumpRegenTime = 0f;
        staminaRegenTime = 0.03f;
        this.gameScreen = gameScreen;
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

        checkUserInput();
    }

    private void checkUserInput() {
        AnimationHandler playerAnimations = player.getPlayerAnimations();

        if(playerAnimations.isFinished() && player.getState() == State.JUMPING && jumpedFromBox) jumpedFromBox = false;

        if(isPlayerNearALadder(player.getBody().getPosition()) && player.getState() != State.CLIMBING){
            player.setPlayerState(State.CLIMBING);
            playerAnimations.setCurrent("climb");
        }

        if(player.getBody().getLinearVelocity().y!=0 && getNearestBoxXDistance(player.getBody().getPosition().x) > 1f && getNearestBoxYDistance(player.getBody().getPosition().y) > 1f && player.getState() != State.JUMPING && player.getState() != State.FALLING && player.getState() != State.ATTACKING && player.getState() != State.DYING){
            player.setPlayerState(State.FALLING);
            playerAnimations.setCurrent("fall", false);
        }

        if((player.getPlayerState().equals(State.PUSHING) && getNearestBoxXDistance(player.getBody().getPosition().x) > 1f) || (playerAnimations.isFinished() && player.getState() == State.ATTACKING) || (player.getBody().getLinearVelocity().y==0 && wasLastFrameYVelocityZero && player.getState() == State.JUMPING)){
            player.setPlayerState(State.IDLE);
            playerAnimations.setCurrent("idle");
        }
        if(getNearestBoxXDistance(player.getBody().getPosition().x) < 1f && getNearestBoxYDistance(player.getBody().getPosition().y) < 1f && player.getBody().getLinearVelocity().y == 0 && player.getState() != State.PUSHING) {
            player.setPlayerState(State.PUSHING);
            playerAnimations.setCurrent("push");
        }

        if(inputKeys.get(InputKeys.LEFT) && !player.getPlayerState().equals(State.DYING)) {
            player.setFacingLeft();
            if(player.getVelocityY()==0f && (player.getState() != State.RUNNING || !inputKeys.get(InputKeys.SHIFT)) && player.getState() != State.JUMPING && player.getState() != State.ATTACKING && !inputKeys.get(InputKeys.RIGHT) && player.getState() != State.PUSHING && player.getState() != State.FALLING && player.getState() != State.CLIMBING){
                playerAnimations.setCurrent("walk");
                player.setPlayerState(State.WALKING);
            }
            player.setVelocityX(-1);
        }
        if(inputKeys.get(InputKeys.RIGHT) && !player.getPlayerState().equals(State.DYING)) {
            player.setFacingRight();
            if(player.getVelocityY()==0 && (player.getState() != State.RUNNING || !inputKeys.get(InputKeys.SHIFT)) && player.getState() != State.JUMPING && player.getState() != State.ATTACKING && !inputKeys.get(InputKeys.LEFT) && player.getState() != State.PUSHING  && player.getState() != State.FALLING && player.getState() != State.CLIMBING){
                playerAnimations.setCurrent("walk");
                player.setPlayerState(State.WALKING);
            }
            player.setVelocityX(1);
        }
        if((inputKeys.get(InputKeys.LEFT) && inputKeys.get(InputKeys.RIGHT)) || (!inputKeys.get(InputKeys.LEFT) && !inputKeys.get(InputKeys.RIGHT)) && !player.getPlayerState().equals(State.DYING)){
            if(player.getState() != State.JUMPING && player.getState() != State.ATTACKING && player.getState() != State.IDLE  && player.getState() != State.FALLING){
                playerAnimations.setCurrent("idle");
                player.setPlayerState(State.IDLE);
            }
            player.setVelocityX(0);
        }
        if(inputKeys.get(InputKeys.SPACE) && !player.getPlayerState().equals(State.DYING)) {
            inputKeys.put(InputKeys.SPACE, false);
            if(player.getPlayerStamina() >= 10) {
                player.setPlayerState(State.JUMPING);
                if(jumpCounter == 0 || (jumpCounter == 1 && isDoubleJumpReady)) {
                    if(jumpCounter == 1) {
                        isDoubleJumpReady = false;
                        jumpedFromBox = false;
                        doubleJumpTimer = 0f;
                        playerAnimations.setCurrent("idle"); // To be able to reset the sprite
                    }
                    playerAnimations.setCurrent("jump", false);
                    player.setPlayerState(State.JUMPING);

                    float force = player.getBody().getMass() * 9;
                    player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
                    player.getBody().applyLinearImpulse(new Vector2(0, force), player.getBody().getPosition(), true);
                    jumpCounter++;
                    player.setPlayerStamina(player.getPlayerStamina() - 10);
                }
            }
        }

        if(inputKeys.get(InputKeys.SHIFT) && !player.getPlayerState().equals(State.DYING) && player.getState() != State.JUMPING && player.getState() != State.FALLING) {
            if(player.getPlayerStamina() == 0){
                isSprintReloading = true;
                playerAnimations.setCurrent("walk");
                player.setPlayerState(State.WALKING);
            }
            if(player.getBody().getLinearVelocity().y == 0 && player.getPlayerSpeed() <= 14f && player.getPlayerStamina() > 0 && !isSprintReloading && player.getState() != State.PUSHING)
                player.setPlayerSpeed(player.getPlayerSpeed() + 0.4f);
            if(player.getBody().getLinearVelocity().x != 0 && player.getBody().getLinearVelocity().y == 0 && player.getPlayerStamina() > 0 && !isSprintReloading)
                player.setPlayerStamina(player.getPlayerStamina() - 1);
            if(player.getState() != State.RUNNING  && player.getState() != State.JUMPING && player.getVelocityX()!=0 && !isSprintReloading && player.getState() != State.ATTACKING && player.getState() != State.PUSHING && player.getState() != State.CLIMBING){
                player.setPlayerState(State.RUNNING);
                playerAnimations.setCurrent("run");
            }
        }

        if(player.getPlayerState().equals(State.DYING))
            player.setVelocityX(0);

        if(isSprintReloading && player.getPlayerStamina() == player.getPlayerMaxStamina())
            isSprintReloading = false;

        if((!player.getPlayerState().equals(State.RUNNING) || player.getBody().getLinearVelocity().y != 0 || isSprintReloading) && player.getPlayerSpeed() > DEFAULT_SPEED)
            player.setPlayerSpeed(player.getPlayerSpeed() - 0.2f);

        if(getNearestBoxXDistance(player.getBody().getPosition().x) < 1f && getNearestBoxYDistance(player.getBody().getPosition().y) < 1f && !player.getPlayerState().equals(State.IDLE) && !jumpedFromBox){
            jumpedFromBox = true;
            jumpCounter = 0;
            playerAnimations.setCurrent("idle");
            player.setPlayerState(State.IDLE);
        }


        if(player.getBody().getLinearVelocity().y == 0){
            if(wasLastFrameYVelocityZero){
                jumpCounter = 0;
                if(player.getState() == State.FALLING) {
                    playerAnimations.setCurrent("idle");
                    player.setPlayerState(State.IDLE);
                }
            }
            else wasLastFrameYVelocityZero = true;
        } else wasLastFrameYVelocityZero = false;

        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && player.getState() != State.WALKING && player.getState() != State.RUNNING && player.getState() != State.ATTACKING && player.getState() != State.PUSHING && player.getState() != State.CLIMBING){
            playerAnimations.setCurrent("attack1", false);
            player.setPlayerState(State.ATTACKING);
        }
        if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && player.getState() != State.WALKING && player.getState() != State.RUNNING && player.getState() != State.ATTACKING && player.getState() != State.PUSHING && player.getState() != State.CLIMBING){
            playerAnimations.setCurrent("attack2", false);
            player.setPlayerState(State.ATTACKING);
        }

        player.getBody().setLinearVelocity(player.getVelocityX() * player.getPlayerSpeed(), player.getBody().getLinearVelocity().y < 25 ? player.getBody().getLinearVelocity().y : 25);
    }

    private void regenStamina(float delta) {
        staminaTimer += delta;
        if((!player.getPlayerState().equals(State.RUNNING) || player.getBody().getLinearVelocity().x == 0 || isSprintReloading) && player.getPlayerStamina() < player.getPlayerMaxStamina() && staminaTimer > staminaRegenTime) {
            player.setPlayerStamina(player.getPlayerStamina() + 1);
            staminaTimer = 0f;
        }
    }

    private void reloadDoubleJump(float delta) {
        doubleJumpTimer += delta;
        if(!isDoubleJumpReady && doubleJumpTimer > doubleJumpRegenTime)
            isDoubleJumpReady = true;
    }

    private float getNearestBoxXDistance(float playerX){
        ArrayList<Box> boxes = gameScreen.getBoxes();
        float minX = 100f;
        for(int i=0; i<boxes.size(); i++)
            if(boxes.get(i).getBody().getPosition().x < minX) minX = boxes.get(i).getBody().getPosition().x;
        return Math.abs(playerX-minX);
    }
    private float getNearestBoxYDistance(float playerY){
        ArrayList<Box> boxes = gameScreen.getBoxes();
        float minY = 100f;
        for(int i=0; i<boxes.size(); i++)
            if(boxes.get(i).getBody().getPosition().y < minY) minY = boxes.get(i).getBody().getPosition().y;
        return Math.abs(playerY-minY);
    }

    private boolean isPlayerNearALadder(Vector2 playerPosition){
        ArrayList<Ladder> ladders = gameScreen.getLadders();
        for(Ladder ladder : ladders){
            Vector2 ladderPosition = ladder.getBody().getPosition();
            if(Math.abs(playerPosition.x - ladderPosition.x) < 1f && Math.abs(playerPosition.y - ladderPosition.y) < ladder.getHeight()/2) return true;
        }
        return false;
    }
}
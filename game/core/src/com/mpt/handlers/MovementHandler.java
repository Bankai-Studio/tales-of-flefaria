package com.mpt.handlers;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.mpt.objects.player.Player;
import com.mpt.objects.player.Player.State;
import sun.tools.jconsole.AboutDialog;

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
    private float staminaRegenTimer;

    static Map<InputKeys, Boolean> inputKeys = new HashMap<InputKeys, Boolean>();
    static {
        inputKeys.put(InputKeys.LEFT, false);
        inputKeys.put(InputKeys.RIGHT, false);
        inputKeys.put(InputKeys.SPACE, false);
        inputKeys.put(InputKeys.SHIFT, false);
    }

    Player player;

    public MovementHandler(Player player) {
        this.player = player;
        jumpCounter = 0;
        isDoubleJumpReady = true;
        wasLastFrameYVelocityZero = false;
        isSprintReloading = false;
        doubleJumpTimer = 0f;
        staminaRegenTimer = 0f;
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
        player.update(delta);
    }

    private void checkUserInput() {
        if(inputKeys.get(InputKeys.LEFT)) {
            //player.setFacingLeft();
            player.setPlayerState(State.WALKING);
            player.setVelocityX(-1);
        }
        if(inputKeys.get(InputKeys.RIGHT)) {
            //player.setFacingRight();
            player.setPlayerState(State.WALKING);
            player.setVelocityX(1);
        }
        if((inputKeys.get(InputKeys.LEFT) && inputKeys.get(InputKeys.RIGHT)) || (!inputKeys.get(InputKeys.LEFT) && !inputKeys.get(InputKeys.RIGHT))){
            player.setPlayerState(State.IDLE);
            player.setVelocityX(0);
        }
        if(inputKeys.get(InputKeys.SPACE)) {
            inputKeys.put(InputKeys.SPACE, false);
            if(player.getPlayerStamina() >= 10) {
                player.setPlayerState(State.JUMPING);
                if(jumpCounter == 0 || (jumpCounter == 1 && isDoubleJumpReady)) {
                    if(jumpCounter == 1) {
                        isDoubleJumpReady = false;
                        doubleJumpTimer = 0f;
                    }
                    float force = player.getBody().getMass() * 9;
                    player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
                    player.getBody().applyLinearImpulse(new Vector2(0, force), player.getBody().getPosition(), true);
                    jumpCounter++;
                    player.setPlayerStamina(player.getPlayerStamina() - 10);
                }
            }
        }
        if(inputKeys.get(InputKeys.SHIFT)) {
            player.setPlayerState(State.RUNNING);
            if(player.getPlayerStamina() == 0) isSprintReloading = true;
            if(player.getBody().getLinearVelocity().y == 0 && player.getPlayerSpeed() <= 14f && player.getPlayerStamina() > 0 && !isSprintReloading)
                player.setPlayerSpeed(player.getPlayerSpeed() + 0.4f);
            if(player.getBody().getLinearVelocity().x != 0 && player.getBody().getLinearVelocity().y == 0 && player.getPlayerStamina() > 0 && !isSprintReloading)
                player.setPlayerStamina(player.getPlayerStamina() - 1);

        }

        if(isSprintReloading && player.getPlayerStamina() == player.getPlayerMaxStamina())
            isSprintReloading = false;

        if((!player.getPlayerState().equals(State.RUNNING) || player.getBody().getLinearVelocity().y != 0 || isSprintReloading) && player.getPlayerSpeed() > DEFAULT_SPEED)
            player.setPlayerSpeed(player.getPlayerSpeed() - 0.2f);

        if(player.getBody().getLinearVelocity().y == 0)
            if(wasLastFrameYVelocityZero)
                jumpCounter = 0;
            else
                wasLastFrameYVelocityZero = true;
        else
            wasLastFrameYVelocityZero = false;
        player.getBody().setLinearVelocity(player.getVelocityX() * player.getPlayerSpeed(), player.getBody().getLinearVelocity().y < 25 ? player.getBody().getLinearVelocity().y : 25);
    }

    private void regenStamina(float delta) {
        staminaRegenTimer += delta;
        if((!player.getPlayerState().equals(State.RUNNING) || player.getBody().getLinearVelocity().x == 0 || isSprintReloading) && player.getPlayerStamina() < player.getPlayerMaxStamina() && staminaRegenTimer > 0.03f) {
            player.setPlayerStamina(player.getPlayerStamina() + 1);
            staminaRegenTimer = 0f;
        }
    }

    private void reloadDoubleJump(float delta) {
        doubleJumpTimer += delta;
        if(!isDoubleJumpReady && doubleJumpTimer > 2f)
            isDoubleJumpReady = true;
    }

}
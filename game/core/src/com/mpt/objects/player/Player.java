package com.mpt.objects.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import static com.mpt.constants.Constants.DEBUGGING;
import static com.mpt.constants.Constants.PPM;

public class Player extends GameEntity{

    private int jumpCounter = 0; //number of jumps done
    private boolean running; //true if left shift is pressed
    public boolean playerDead = false;
    private int damageToEnemy; //danni al nemico
    private int minDmg = 50;  //50 di attacco
    private int maxDmg = 150; //150 di attacco
    private int player_health = 3; //3 vite all'inizio del livello
    private int damageValue;
    private final int MAXSTM = 100; //Maximum of stamina
    private int stamina = MAXSTM; //Current stamina
    private final int STMXJ = 10; //Stamina reduction for every jump
    private final float TSTM = 0.03f; //Stamina reloading speed
    private float timerStm = 0f; //Timer used to regenerate stamina
    private boolean sprintReloading = false; //True if the sprinting is in reloading
    private int minStm2Sprint = MAXSTM; //Stamina required to sprint again
    private boolean doubleJumpReady = true; //True if the double jump is ready
    private float timerDoubleJump = 0f; //Timer used to reload the double jump
    private final float TDJMP = 2f; //Time required to reload the double jump
    private boolean lastFrameVelYWas0 = false; //True if in the last frame the Y velocity was 0
    private final float MAXSPEED = 14f; //Maximum of speed
    private final float WALKSPEED = 8f; //Walking speed
    private final float SPEEDREDUCTION = 0.2f; //Speed decreasing every cycle while not springing
    private final float SPEEDGAIN = 0.4f; //Speed increasing for every cycle while springing

    public Player(float width, float height, Body body) {
        super(width, height, body);
        this.speed = WALKSPEED;
        damageToEnemy = (int)(Math.random()*(maxDmg-minDmg+1)+minDmg); //??????
    }

    public void attackEnemy(Enemy enemy){
        damageValue = damageToEnemy;
        enemy.getDamaged(damageValue);
    }

    public void playerGetDamaged(int damageV){
        player_health -= damageV;
        if(health == 0)
            playerDead = true;
    }

    @Override
    public void update() {
        x = body.getPosition().x * PPM;
        y = body.getPosition().y * PPM;

        //Stamina reloading system
        timerStm += Gdx.graphics.getDeltaTime();
        if((!running || body.getLinearVelocity().x == 0 || sprintReloading) && stamina < MAXSTM && timerStm > TSTM){
            stamina++;
            timerStm = 0f;
        }
        if(DEBUGGING) System.out.println(stamina);

        //Double jump reloading system
        timerDoubleJump += Gdx.graphics.getDeltaTime();
        if(!doubleJumpReady && timerDoubleJump > TDJMP)
            doubleJumpReady = true;

        checkUserInput();
    }

    @Override
    public void render(SpriteBatch batch) {

    }

    private void checkUserInput() {
        velX = 0;
        running = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT);
        if(Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            velX = 1;
        if(Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))
            velX = -1;
        if(stamina == 0 && running) sprintReloading = true;
        if(sprintReloading && stamina >= minStm2Sprint) sprintReloading = false;
        if((!running || body.getLinearVelocity().y != 0 || sprintReloading) && speed > WALKSPEED) speed -= SPEEDREDUCTION;
        if((running && body.getLinearVelocity().y == 0) && speed<= MAXSPEED && stamina > 0 && !sprintReloading) speed += SPEEDGAIN;
        if(running && body.getLinearVelocity().x != 0 && body.getLinearVelocity().y == 0 && stamina > 0 && !sprintReloading) stamina -= 1;
        if((Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) && stamina >= STMXJ) {
            if(jumpCounter == 0 || (jumpCounter == 1 && doubleJumpReady)){
                if(jumpCounter == 1){
                    doubleJumpReady = false;
                    timerDoubleJump = 0f;
                }
                float force = body.getMass() * 9;
                body.setLinearVelocity(body.getLinearVelocity().x, 0);
                body.applyLinearImpulse(new Vector2(0, force), body.getPosition(), true);
                jumpCounter++;
                stamina -= STMXJ;
            }
        }

        if(body.getLinearVelocity().y == 0){
            if(lastFrameVelYWas0) jumpCounter = 0;
            else lastFrameVelYWas0 = true;
        } else lastFrameVelYWas0 = false;

        body.setLinearVelocity(velX * speed, body.getLinearVelocity().y < 25 ? body.getLinearVelocity().y : 25);
    }

}

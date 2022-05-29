package com.mpt.objects.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.mpt.handlers.AnimationHandler;
import com.mpt.handlers.CombatHandler;
import com.mpt.objects.player.Player;
import com.mpt.objects.GameEntity;
import com.mpt.platform.GameScreen;

import static com.mpt.constants.Constants.PPM;

public abstract class Enemy extends GameEntity {

    private boolean distanceToAttackPlayer;
    private float initialPosX; //initial xPos of slime
    private float initialPosY; //initial yPos of slime
    private int killCounter; //player's frags
    private int damageToPlayer;
    private GameScreen gameScreen;
    private boolean playerHasBeenSpotted;
    private boolean playerHasBeenDefeat;
    private float xMaxLimitDX; //limit position on right side
    private float xMaxLimitSX; //limit position on left side
    private boolean switchDirectionToRight = false; //flag that says if enemy switched direction
    private boolean switchDirectionToLeft = false;
    protected float walkSpeed;
    private float attackTimer = 0f;
    private final float ATTACK_DELAY = 1f;

    // Enemy States
    public enum EnemyState {
        IDLE,
        WALKING,
        ATTACKING,
        DYING,
        HURT
    }

    protected EnemyState enemyState;
    protected String direction;
    protected String enemyName;
    protected final AnimationHandler animationHandler;

    public Enemy(float width, float height, Body body, GameScreen gameScreen) {
        super(width, height, body);
        initialPosX = body.getPosition().x; //initial position of enemy
        initialPosY = body.getPosition().y; //initial position of enemy
        playerHasBeenSpotted = false;
        playerHasBeenDefeat = false;
        distanceToAttackPlayer = false;
        this.gameScreen = gameScreen;
        direction = "RIGHT";
        animationHandler = new AnimationHandler();
        enemyState = EnemyState.IDLE;
    }

    @Override
    public void update(float delta) {
        attackTimer += Gdx.graphics.getDeltaTime();
        if (enemyState != EnemyState.DYING) {
            if (animationHandler.isFinished() && (enemyState.equals(EnemyState.HURT) || enemyState.equals(EnemyState.ATTACKING))) {
                if (enemyState.equals(EnemyState.ATTACKING)) {
                    if (enemyReadyToAttack(gameScreen.getPlayer())) CombatHandler.attack(this, gameScreen.getPlayer());
                    attackTimer = 0;
                }
                enemyState = EnemyState.IDLE;
                animationHandler.setCurrent("idle");
            }
            if (!playerSpotted(gameScreen.getPlayer())) enemyMovements();
            else lurkTarget(gameScreen.getPlayer());
        } else body.setLinearVelocity(0, body.getLinearVelocity().y);

    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = animationHandler.getFrame();

        if (direction.equals("RIGHT") && !currentFrame.isFlipX()) currentFrame.flip(true, false);
        if (direction.equals("LEFT") && currentFrame.isFlipX()) currentFrame.flip(true, false);

        float tX = body.getPosition().x * PPM - 35f;
        float tY = body.getPosition().y * PPM - 12f;
        if (enemyName.equals("Scorpio") && direction.equals("RIGHT")) tX += 25f;
        batch.draw(currentFrame, tX, tY);
    }


    private void checkPlayerDeath() {
        if (enemyState.equals(EnemyState.DYING)) {
            //body.setTransform(respawnPosition.x, respawnPosition.y, body.getAngle());
            enemyState = EnemyState.IDLE;
            //animationHandler.setCurrent("idle");
        }
    }

    private void respawnOnVoidPosition() {
        if (body.getPosition().y < 0) {
            enemyState = EnemyState.DYING;
            //animationHandler.setCurrent("death", false);
        }
    }

    public void enemyAttackPlayer(Player player) {
        if (enemyReadyToAttack(player) && !enemyState.equals(EnemyState.HURT) && !enemyState.equals(EnemyState.DYING) && !player.getPlayerState().equals(Player.State.DYING)) {
            enemyState = EnemyState.ATTACKING;
            animationHandler.setCurrent("attack", false);
        }
    }


    public void enemyMovements() {
        if (!enemyState.equals(EnemyState.HURT) && !enemyState.equals(EnemyState.ATTACKING)) {
            enemyState = EnemyState.WALKING;
            animationHandler.setCurrent("walk");
        }

        xMaxLimitDX = initialPosX + 2.4f;
        xMaxLimitSX = initialPosX - 5f;
        switchDirectionToRight = true;
        if (body.getPosition().x < xMaxLimitDX && switchDirectionToRight) {
            body.setLinearVelocity(walkSpeed * (3f), body.getLinearVelocity().y);
            setFacingRight();
        } else {
            switchDirectionToRight = false;
            switchDirectionToLeft = true;
        }
        if (switchDirectionToLeft && body.getPosition().x > xMaxLimitSX) {
            body.setLinearVelocity(walkSpeed * (-3f), body.getLinearVelocity().y);
            setFacingLeft();
        } else {
            switchDirectionToLeft = false;
            switchDirectionToRight = true;
        }
    }

    public boolean playerSpotted(Player player) {
        playerHasBeenSpotted = Math.abs(player.getBody().getPosition().x - body.getPosition().x) < 8f && Math.abs(player.getBody().getPosition().y - body.getPosition().y) < 3f;
        return playerHasBeenSpotted;
    }

    public boolean enemyReadyToAttack(Player player) {
        return Math.abs(player.getBody().getPosition().x - body.getPosition().x) < (width / 2 / PPM + player.getWidth() / PPM) && attackTimer >= ATTACK_DELAY && (Math.abs(player.getBody().getPosition().y - body.getPosition().y) < (height / 2 / PPM + player.getHeight() / PPM));
    }

    public void lurkTarget(Player player) {
        if (!enemyReadyToAttack(player)) {
            if (playerSpotted(player) && Math.abs((int) player.getBody().getPosition().x - (int) body.getPosition().x) != 0) {
                if (player.getBody().getPosition().x < this.getBody().getPosition().x) {
                    body.setLinearVelocity(walkSpeed * (-3f), body.getLinearVelocity().y);
                    setFacingLeft();
                } else if (player.getBody().getPosition().x > this.getBody().getPosition().x) {
                    body.setLinearVelocity(walkSpeed * (3f), body.getLinearVelocity().y);
                    setFacingRight();
                }
                if (!enemyState.equals(EnemyState.HURT) && !enemyState.equals(EnemyState.ATTACKING)) {
                    enemyState = EnemyState.WALKING;
                    animationHandler.setCurrent("walk");
                }
            } else if (Math.abs((int) player.getBody().getPosition().x - (int) body.getPosition().x) == 0) {
                this.getBody().setLinearVelocity(0, this.getBody().getLinearVelocity().y);
                if (!enemyState.equals(EnemyState.HURT) && !enemyState.equals(EnemyState.ATTACKING)) {
                    enemyState = EnemyState.IDLE;
                    animationHandler.setCurrent("idle");
                }
            }
        }
        enemyAttackPlayer(player);
    }

    protected void loadSprites() {
        TextureAtlas charset;

        charset = new TextureAtlas(Gdx.files.internal("./enemies/" + enemyName + "/attack.atlas"));
        float FRAME_TIME = 1 / 6f;
        this.animationHandler.add("attack", new Animation<>(FRAME_TIME, charset.findRegions("attack")));

        charset = new TextureAtlas(Gdx.files.internal("./enemies/" + enemyName + "/death.atlas"));
        this.animationHandler.add("death", new Animation<>(FRAME_TIME, charset.findRegions("death")));

        charset = new TextureAtlas(Gdx.files.internal("./enemies/" + enemyName + "/hurt.atlas"));
        this.animationHandler.add("hurt", new Animation<>(FRAME_TIME, charset.findRegions("hurt")));

        charset = new TextureAtlas(Gdx.files.internal("./enemies/" + enemyName + "/idle.atlas"));
        this.animationHandler.add("idle", new Animation<>(FRAME_TIME, charset.findRegions("idle")));

        charset = new TextureAtlas(Gdx.files.internal("./enemies/" + enemyName + "/walk.atlas"));
        this.animationHandler.add("walk", new Animation<>(FRAME_TIME, charset.findRegions("walk")));

        this.animationHandler.setCurrent("walk");
    }

    public void setEnemyState(EnemyState enemyState) {
        this.enemyState = enemyState;
    }

    public void setFacingLeft() {
        direction = "LEFT";
    }

    public void setFacingRight() {
        direction = "RIGHT";
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public AnimationHandler getAnimationHandler() {
        return animationHandler;
    }

    public EnemyState getEnemyState() {
        return enemyState;
    }
}

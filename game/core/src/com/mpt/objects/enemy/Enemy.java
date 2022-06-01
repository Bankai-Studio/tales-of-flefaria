package com.mpt.objects.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.mpt.handlers.AnimationHandler;
import com.mpt.handlers.CombatHandler;
import com.mpt.modules.MusicModule;
import com.mpt.objects.player.Player;
import com.mpt.objects.GameEntity;
import com.mpt.platform.GameScreen;

import static com.mpt.constants.Constants.PPM;

public abstract class Enemy extends GameEntity {
    private final float initialPosX;
    private final GameScreen gameScreen;
    private boolean playerHasBeenSpotted;
    public boolean switchDirectionToRight;
    public boolean switchDirectionToLeft;
    protected float walkSpeed;
    private float attackTimer = 0f;

    // Enemy States
    public enum EnemyState {
        IDLE, WALKING, ATTACKING, DYING, HURT
    }

    protected EnemyState enemyState;
    protected String direction;
    protected String enemyName;
    protected final AnimationHandler animationHandler;

    public Enemy(float width, float height, Body body, GameScreen gameScreen) {
        super(width, height, body);
        initialPosX = body.getPosition().x; //initial position of enemy
        playerHasBeenSpotted = false;
        body.setUserData(this);
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
                    if (enemyReadyToAttack(gameScreen.getPlayer())) {
                        int damageProvided = gameScreen.getPlayer().getHealth();
                        CombatHandler.attack(this, gameScreen.getPlayer());
                        damageProvided -= gameScreen.getPlayer().getHealth();
                        gameScreen.updateHealthLabel(gameScreen.getPlayer().getHealth(), damageProvided);
                    }
                    attackTimer = 0;
                }
                enemyState = EnemyState.IDLE;
                animationHandler.setCurrent("idle");
            }
            if(body.getLinearVelocity().x == 0 && !playerSpotted(gameScreen.getPlayer())) enemyMovementsCollision();
            if (!playerSpotted(gameScreen.getPlayer())) enemyMovements();
            else lurkTarget(gameScreen.getPlayer());
        } else body.setLinearVelocity(0, body.getLinearVelocity().y);

    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = animationHandler.getFrame();

        if (direction.equals("RIGHT") && !currentFrame.isFlipX()) currentFrame.flip(true, false);
        if (direction.equals("LEFT") && currentFrame.isFlipX()) currentFrame.flip(true, false);

        float tX = body.getPosition().x * PPM + adjustX;
        float tY = body.getPosition().y * PPM + adjustY;
        if (enemyName.equals("Scorpio") && direction.equals("RIGHT")) tX += 25f;
        if (enemyName.equals("Snake") && direction.equals("RIGHT")) tX += 25f;
        if(enemyName.equals("FinalBoss")) batch.draw(currentFrame, tX - width/2, tY - height/2, width*1.8f, height*1.8f);
        else batch.draw(currentFrame, tX, tY);
    }

    public void enemyAttackPlayer(Player player) {
        if (enemyReadyToAttack(player) && !enemyState.equals(EnemyState.HURT) && !enemyState.equals(EnemyState.DYING) && !player.getPlayerState().equals(Player.State.DYING)) {
            enemyState = EnemyState.ATTACKING;
            animationHandler.setCurrent("attack", false);
        }
    }

    public void enemyMovements() {
        float xMaxLimitDX; //limit position on right side
        float xMaxLimitSX; //limit position on left side
        if (!enemyState.equals(EnemyState.HURT) && !enemyState.equals(EnemyState.ATTACKING)) {
            enemyState = EnemyState.WALKING;
            animationHandler.setCurrent("walk");
        }
        if (this.getBody().getLinearVelocity().x > 0) {
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
    }

    public void enemyMovementsCollision() {
        switchDirectionToRight = true;
        if (body.getPosition().x < initialPosX + 0.1f && switchDirectionToRight) {
            body.setLinearVelocity(walkSpeed * (3f), body.getLinearVelocity().y);
            setFacingRight();
        } else {
            switchDirectionToLeft = true;
            switchDirectionToRight = false;
        }
        if (body.getPosition().x > initialPosX - 0.1f && switchDirectionToLeft) {
            body.setLinearVelocity(walkSpeed * (-3f), body.getLinearVelocity().y);
            setFacingLeft();
        } else{
            switchDirectionToLeft = false;
            switchDirectionToRight = true;
        }
    }

    public boolean playerSpotted(Player player) {
        playerHasBeenSpotted = Math.abs(player.getBody().getPosition().x - body.getPosition().x) < 8f && Math.abs(player.getBody().getPosition().y - body.getPosition().y) < 2f;
        return playerHasBeenSpotted;
    }

    public boolean enemyReadyToAttack(Player player) {
        final float ATTACK_DELAY = 1f;
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
        } else if (enemyReadyToAttack(player)) {
            MusicModule.getEnemyAttackSound().setVolume(0.1f);
            MusicModule.getEnemyAttackSound().play();
            enemyAttackPlayer(player);
        }
    }

    protected void loadSprites() {
        TextureAtlas charset;
        float FRAME_TIME = 1 / 6f;

        if(enemyName.equals("FinalBoss")){
            loadBossSprites();
            return;
        }

        charset = new TextureAtlas(Gdx.files.internal("./enemies/" + enemyName + "/attack.atlas"));
        this.animationHandler.add("attack", new Animation<>(FRAME_TIME, charset.findRegions("attack")));
        textureAtlases.add(charset);

        charset = new TextureAtlas(Gdx.files.internal("./enemies/" + enemyName + "/death.atlas"));
        this.animationHandler.add("death", new Animation<>(FRAME_TIME, charset.findRegions("death")));
        textureAtlases.add(charset);

        charset = new TextureAtlas(Gdx.files.internal("./enemies/" + enemyName + "/hurt.atlas"));
        this.animationHandler.add("hurt", new Animation<>(FRAME_TIME, charset.findRegions("hurt")));
        textureAtlases.add(charset);

        charset = new TextureAtlas(Gdx.files.internal("./enemies/" + enemyName + "/idle.atlas"));
        this.animationHandler.add("idle", new Animation<>(FRAME_TIME, charset.findRegions("idle")));
        textureAtlases.add(charset);

        charset = new TextureAtlas(Gdx.files.internal("./enemies/" + enemyName + "/walk.atlas"));
        this.animationHandler.add("walk", new Animation<>(FRAME_TIME, charset.findRegions("walk")));
        textureAtlases.add(charset);

        this.animationHandler.setCurrent("walk");
    }

    protected void loadBossSprites() {
        TextureAtlas charset;
        float FRAME_TIME = 1 / 6f;

        charset = new TextureAtlas(Gdx.files.internal("./enemies/" + enemyName + "/attack1.atlas"));
        this.animationHandler.add("attack1", new Animation<>(FRAME_TIME, charset.findRegions("attack1")));
        textureAtlases.add(charset);

        charset = new TextureAtlas(Gdx.files.internal("./enemies/" + enemyName + "/attack2.atlas"));
        this.animationHandler.add("attack", new Animation<>(FRAME_TIME, charset.findRegions("attack2")));
        textureAtlases.add(charset);

        charset = new TextureAtlas(Gdx.files.internal("./enemies/" + enemyName + "/attack3.atlas"));
        this.animationHandler.add("attack3", new Animation<>(FRAME_TIME, charset.findRegions("attack3")));
        textureAtlases.add(charset);

        charset = new TextureAtlas(Gdx.files.internal("./enemies/" + enemyName + "/attack4.atlas"));
        this.animationHandler.add("attack4", new Animation<>(FRAME_TIME, charset.findRegions("attack4")));
        textureAtlases.add(charset);

        charset = new TextureAtlas(Gdx.files.internal("./enemies/" + enemyName + "/death.atlas"));
        this.animationHandler.add("death", new Animation<>(FRAME_TIME, charset.findRegions("death")));
        textureAtlases.add(charset);

        charset = new TextureAtlas(Gdx.files.internal("./enemies/" + enemyName + "/hurt.atlas"));
        this.animationHandler.add("hurt", new Animation<>(FRAME_TIME, charset.findRegions("hurt")));
        textureAtlases.add(charset);

        charset = new TextureAtlas(Gdx.files.internal("./enemies/" + enemyName + "/idle.atlas"));
        this.animationHandler.add("idle", new Animation<>(FRAME_TIME, charset.findRegions("idle")));
        textureAtlases.add(charset);

        charset = new TextureAtlas(Gdx.files.internal("./enemies/" + enemyName + "/walk.atlas"));
        this.animationHandler.add("walk", new Animation<>(FRAME_TIME, charset.findRegions("walk")));
        textureAtlases.add(charset);

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

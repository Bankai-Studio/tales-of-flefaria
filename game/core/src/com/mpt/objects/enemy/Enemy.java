package com.mpt.objects.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.mpt.handlers.AnimationHandler;
import com.mpt.handlers.CombatHandler;
import com.mpt.modules.MusicModule;
import com.mpt.objects.GameEntity;
import com.mpt.objects.bullets.Bullet;
import com.mpt.objects.player.Player;
import com.mpt.platform.GameScreen;

import static com.mpt.constants.Constants.PPM;

public abstract class Enemy extends GameEntity {
    protected final AnimationHandler animationHandler;
    private final float initialPosX;
    private final float initialPosY;
    private final GameScreen gameScreen;
    private final Texture attackTexture;
    private final float ATTACK_DELAY = 1f;
    private final float BOSS_ATTACK_DELAY = 3f;
    public boolean switchDirectionToRight;
    public boolean switchDirectionToLeft;
    protected float walkSpeed;
    protected EnemyState enemyState;
    protected String direction;
    protected String enemyName;
    private boolean bossSpottedPlayer;
    private boolean playerHasBeenSpotted;
    private float attackTimer = 0f;

    public Enemy(float width, float height, Body body, GameScreen gameScreen) {
        super(width, height, body);
        initialPosX = body.getPosition().x; //initial position of enemy
        initialPosY = body.getPosition().y; //initial y position
        playerHasBeenSpotted = false;
        bossSpottedPlayer = false;
        body.setUserData(this);
        this.gameScreen = gameScreen;
        direction = "RIGHT";
        animationHandler = new AnimationHandler();
        enemyState = EnemyState.IDLE;
        attackTexture = new Texture(Gdx.files.internal("enemies/exclamationPoint.png"));
    }

    @Override
    public void update(float delta) {
        attackTimer += Gdx.graphics.getDeltaTime();
        if (enemyState != EnemyState.DYING) {
            if (animationHandler.isFinished() && (enemyState.equals(EnemyState.HURT) || enemyState.equals(EnemyState.ATTACKING))) {
                if (enemyState.equals(EnemyState.ATTACKING)) {
                    if (bossSpottedPlayer) {
                        if (animationHandler.isCurrent("attack") && Math.abs(gameScreen.getPlayer().getBody().getPosition().x - body.getPosition().x) < (width / 2 / PPM + gameScreen.getPlayer().getWidth() / PPM))
                            CombatHandler.attack(this, gameScreen.getPlayer());
                        if (animationHandler.isCurrent("shoot"))
                            shootPlayer(gameScreen.getPlayer());
                        if (animationHandler.isCurrent("mortarStrike"))
                            strikePlayer(gameScreen.getPlayer());
                        gameScreen.updateHealthBar();
                        attackTimer = 0;
                    } else if (enemyReadyToAttack(gameScreen.getPlayer())) {
                        CombatHandler.attack(this, gameScreen.getPlayer());
                        gameScreen.updateHealthBar();
                        attackTimer = 0;
                    }
                }
                enemyState = EnemyState.IDLE;
                animationHandler.setCurrent("idle");
            }
            if (body.getLinearVelocity().x == 0 && !playerSpotted(gameScreen.getPlayer())) {
                enemyMovementsCollision();
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

        float tX = body.getPosition().x * PPM + adjustX;
        float tY = body.getPosition().y * PPM + adjustY;
        if (enemyName.equals("Scorpio") && direction.equals("RIGHT")) tX += 25f;
        if (enemyName.equals("Snake") && direction.equals("RIGHT")) tX += 25f;
        if (enemyName.equals("FinalBoss"))
            batch.draw(currentFrame, tX - width / 2, tY - height / 2, width * 1.8f, height * 1.8f);
        else batch.draw(currentFrame, tX, tY);
        if (playerSpotted(gameScreen.getPlayer()) && !enemyState.equals(EnemyState.DYING) && !bossSpottedPlayer)
            batch.draw(attackTexture, body.getPosition().x * PPM, body.getPosition().y * PPM + height, (float) attackTexture.getWidth() / 40, (float) attackTexture.getHeight() / 40);
    }

    public void enemyAttackPlayer(Player player) {
        if (!bossSpottedPlayer && enemyReadyToAttack(player) && !enemyState.equals(EnemyState.HURT) && !enemyState.equals(EnemyState.DYING) && !player.getPlayerState().equals(Player.State.DYING)) {
            this.getBody().setLinearVelocity(0, this.getBody().getLinearVelocity().y);
            enemyState = EnemyState.ATTACKING;
            animationHandler.setCurrent("attack", false);
        }
        if (bossSpottedPlayer && !enemyState.equals(EnemyState.DYING) && !player.getPlayerState().equals(Player.State.DYING) && enemyReadyToAttack(gameScreen.getPlayer()) && !enemyState.equals(EnemyState.ATTACKING)) {
            this.getBody().setLinearVelocity(0, this.getBody().getLinearVelocity().y);
            enemyState = EnemyState.ATTACKING;
            if (Math.abs(gameScreen.getPlayer().getBody().getPosition().x - body.getPosition().x) < (width / 2 / PPM + gameScreen.getPlayer().getWidth() / PPM))
                animationHandler.setCurrent("attack", false);
            else if (true)
                animationHandler.setCurrent("shoot", false);
            else animationHandler.setCurrent("mortarStrike", false);
        }
    }

    public void enemyMovements() {
        float xMaxLimitDX; //limit position on right side
        float xMaxLimitSX; //limit position on left side
        if (!enemyState.equals(EnemyState.HURT) && !enemyState.equals(EnemyState.ATTACKING)) {
            enemyState = EnemyState.WALKING;
            animationHandler.setCurrent("walk");
        }
        xMaxLimitDX = initialPosX + 3f;
        xMaxLimitSX = initialPosX - 3f;
        switchDirectionToRight = true;
        if (body.getPosition().x < xMaxLimitDX && switchDirectionToRight) {
            body.setLinearVelocity(walkSpeed * (2f), body.getLinearVelocity().y);
            setFacingRight();
        } else {
            switchDirectionToRight = false;
            switchDirectionToLeft = true;
        }
        if (switchDirectionToLeft && body.getPosition().x > xMaxLimitSX) {
            body.setLinearVelocity(walkSpeed * (-2f), body.getLinearVelocity().y);
            setFacingLeft();
        } else {
            switchDirectionToLeft = false;
            switchDirectionToRight = true;
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
        if (body.getPosition().x > initialPosX - 0.1f && switchDirectionToLeft && body.getPosition().y != initialPosY) {
            body.setLinearVelocity(walkSpeed * (-3f), body.getLinearVelocity().y);
            setFacingLeft();
        } else {
            switchDirectionToLeft = false;
            switchDirectionToRight = true;
        }
    }

    public boolean playerSpotted(Player player) {
        if (this instanceof FinalBoss) {
            bossSpottedPlayer = Math.abs(player.getBody().getPosition().x - body.getPosition().x) < 1000 + width / 2 / PPM;
            return bossSpottedPlayer;
        } else {
            playerHasBeenSpotted = Math.abs(player.getBody().getPosition().x - body.getPosition().x) < 6f && Math.abs(player.getBody().getPosition().y - body.getPosition().y) < 2f && player.getBody().getPosition().y >= this.getBody().getPosition().y - 0.4f;
            return playerHasBeenSpotted;
        }
    }

    public boolean enemyReadyToAttack(Player player) {
        if (bossSpottedPlayer)
            return attackTimer >= BOSS_ATTACK_DELAY;
        if (playerHasBeenSpotted)
            return Math.abs(player.getBody().getPosition().x - body.getPosition().x) < (width / 2 / PPM + player.getWidth() / PPM) && attackTimer >= ATTACK_DELAY && (Math.abs(player.getBody().getPosition().y - body.getPosition().y) < (height / 2 / PPM + player.getHeight() / PPM));
        return false;
    }

    public void shootPlayer(Player player) {
        FinalBoss finalBoss = (FinalBoss) this;
        float direction = 1 * ((player.getBody().getPosition().x - finalBoss.getBody().getPosition().x) / Math.abs(player.getBody().getPosition().x - finalBoss.getBody().getPosition().x));
        Bullet bullet1 = new Bullet(Bullet.createBody(finalBoss.getBody().getPosition().x + 1f, player.getBody().getPosition().y + player.getHeight() / 2 / PPM, gameScreen.getWorld()), finalBoss.getStraightBulletBright(), direction);
        Bullet bullet2 = new Bullet(Bullet.createBody(finalBoss.getBody().getPosition().x, player.getBody().getPosition().y + player.getHeight() / 2 / PPM, gameScreen.getWorld()), finalBoss.getStraightBulletDark(), direction);
        gameScreen.addBullet(bullet1);
        gameScreen.addBullet(bullet2);
    }

    public void strikePlayer(Player player) {

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
        } else if (enemyReadyToAttack(player) && !player.getPlayerState().equals(Player.State.DYING)) {
            if (enemyName.equals("Snake")) {
                MusicModule.getSnakeAttackSound().setVolume(0.1f);
                MusicModule.getSnakeAttackSound().play();
            } else if (enemyName.equals("Centipede") || enemyName.equals("BigBloated") || enemyName.equals("FinalBoss")) {
                MusicModule.getEnemyAttackSound().setVolume(0.1f);
                MusicModule.getEnemyAttackSound().play();
            }
            enemyAttackPlayer(player);
        }
    }

    protected void loadSprites() {
        TextureAtlas charset;
        float FRAME_TIME = 1 / 6f;

        if (enemyName.equals("FinalBoss")) {
            loadBossSprites();
            return;
        }

        charset = new TextureAtlas(Gdx.files.internal("enemies/" + enemyName + "/attack.atlas"));
        this.animationHandler.add("attack", new Animation<>(FRAME_TIME, charset.findRegions("attack")));
        textureAtlases.add(charset);

        charset = new TextureAtlas(Gdx.files.internal("enemies/" + enemyName + "/death.atlas"));
        this.animationHandler.add("death", new Animation<>(FRAME_TIME, charset.findRegions("death")));
        textureAtlases.add(charset);

        charset = new TextureAtlas(Gdx.files.internal("enemies/" + enemyName + "/hurt.atlas"));
        this.animationHandler.add("hurt", new Animation<>(FRAME_TIME, charset.findRegions("hurt")));
        textureAtlases.add(charset);

        charset = new TextureAtlas(Gdx.files.internal("enemies/" + enemyName + "/idle.atlas"));
        this.animationHandler.add("idle", new Animation<>(FRAME_TIME, charset.findRegions("idle")));
        textureAtlases.add(charset);

        charset = new TextureAtlas(Gdx.files.internal("enemies/" + enemyName + "/walk.atlas"));
        this.animationHandler.add("walk", new Animation<>(FRAME_TIME, charset.findRegions("walk")));
        textureAtlases.add(charset);

        this.animationHandler.setCurrent("walk");
    }

    protected void loadBossSprites() {
        TextureAtlas charset;
        float FRAME_TIME = 1 / 6f;

        charset = new TextureAtlas(Gdx.files.internal("enemies/" + enemyName + "/attack2.atlas"));
        this.animationHandler.add("attack", new Animation<>(FRAME_TIME, charset.findRegions("attack2")));
        textureAtlases.add(charset);

        charset = new TextureAtlas(Gdx.files.internal("enemies/" + enemyName + "/attack3.atlas"));
        this.animationHandler.add("mortarStrike", new Animation<>(FRAME_TIME, charset.findRegions("attack3")));
        textureAtlases.add(charset);

        charset = new TextureAtlas(Gdx.files.internal("enemies/" + enemyName + "/attack4.atlas"));
        this.animationHandler.add("shoot", new Animation<>(FRAME_TIME, charset.findRegions("attack4")));
        textureAtlases.add(charset);

        charset = new TextureAtlas(Gdx.files.internal("enemies/" + enemyName + "/death.atlas"));
        this.animationHandler.add("death", new Animation<>(FRAME_TIME, charset.findRegions("death")));
        textureAtlases.add(charset);

        charset = new TextureAtlas(Gdx.files.internal("enemies/" + enemyName + "/hurt.atlas"));
        this.animationHandler.add("hurt", new Animation<>(FRAME_TIME, charset.findRegions("hurt")));
        textureAtlases.add(charset);

        charset = new TextureAtlas(Gdx.files.internal("enemies/" + enemyName + "/idle.atlas"));
        this.animationHandler.add("idle", new Animation<>(FRAME_TIME, charset.findRegions("idle")));
        textureAtlases.add(charset);

        charset = new TextureAtlas(Gdx.files.internal("enemies/" + enemyName + "/walk.atlas"));
        this.animationHandler.add("walk", new Animation<>(FRAME_TIME, charset.findRegions("walk")));
        textureAtlases.add(charset);

        this.animationHandler.setCurrent("walk");
    }

    public void setFacingLeft() {
        direction = "LEFT";
    }

    public void setFacingRight() {
        direction = "RIGHT";
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public AnimationHandler getAnimationHandler() {
        return animationHandler;
    }

    public EnemyState getEnemyState() {
        return enemyState;
    }

    public void setEnemyState(EnemyState enemyState) {
        this.enemyState = enemyState;
    }

    // Enemy States
    public enum EnemyState {
        IDLE, WALKING, ATTACKING, DYING, HURT
    }
}

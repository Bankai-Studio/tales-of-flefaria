package com.mpt.platform;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mpt.handlers.*;
import com.mpt.modules.InterfaceModule;
import com.mpt.modules.MusicModule;
import com.mpt.objects.endpoint.Endpoint;
import com.mpt.objects.enemy.*;
import com.mpt.objects.interactables.*;
import com.mpt.objects.checkpoint.Checkpoint;
import com.mpt.objects.player.Player;

import java.util.ArrayList;

import static com.mpt.constants.Constants.DEBUGGING;
import static com.mpt.constants.Constants.PPM;

public class GameScreen extends ScreenAdapter implements InputProcessor {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Stage stage;
    private final World world;
    private final Box2DDebugRenderer box2DDebugRenderer;
    private OrthogonalBleedingHandler orthogonalTiledMapRenderer;
    private final MapHandler mapHandler;
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Checkpoint> checkpoints;
    private Endpoint endpoint;
    private ExtendViewport extendViewport;
    private ScreenViewport screenViewport;
    private MovementHandler movementHandler;
    private PreferencesHandler preferencesHandler;
    private ArrayList<Box> boxes;
    private ArrayList<Ladder> ladders;
    private ArrayList<Coin> coins;
    private ArrayList<KillBlock> killBlocks;
    private Array<Body> bodies;
    private ArrayList<Ghost> ghosts;
    private InputMultiplexer inputMultiplexer;
    private Label coinValueLabel;
    private Label playerHealthLabel;
    private Image healthImage;
    private Image healthBar;
    private Image staminaImage;
    private Image staminaBar;
    private AssetManager assetManager;
    private String currentMap;
    private GameOver gameOver;

    private int currentCharacter;
    private int screenWidth, screenHeight;

    public GameScreen() {
        batch = new SpriteBatch();
        assetManager = new AssetManager();
        loadAssets();
        world = new World(new Vector2(0, -25f), false);
        enemies = new ArrayList<>();
        checkpoints = new ArrayList<>();
        boxes = new ArrayList<>();
        ladders = new ArrayList<>();
        coins = new ArrayList<>();
        killBlocks = new ArrayList<>();
        ghosts = new ArrayList<>();
        box2DDebugRenderer = new Box2DDebugRenderer();

        preferencesHandler = new PreferencesHandler();

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        extendViewport = new ExtendViewport(30 * PPM, 20 * PPM);
        camera = (OrthographicCamera) extendViewport.getCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);

        screenViewport = new ScreenViewport();
        stage = new Stage(screenViewport, batch);
        setupInterface();

        inputMultiplexer = new InputMultiplexer(this, stage);


        mapHandler = new MapHandler(this);
        currentMap = "MapTutorial";
        currentCharacter = 0;
        loadMap(currentMap, currentCharacter);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (MusicModule.getMainMenuMusic().isPlaying()) {
                    if (MusicModule.getMainMenuMusic().getVolume() >= 0.02f)
                        MusicModule.getMainMenuMusic().setVolume(Math.max(0, MusicModule.getMainMenuMusic().getVolume() - 0.02f));
                    else {
                        this.cancel();
                        MusicModule.getMainMenuMusic().stop();
                    }
                }
            }
        }, 0f, 0.1f);

    }

    @Override
    public void render(float delta) {
        world.step(1 / 60f, 6, 2);

        this.update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        //36f/255f,61f/255f,71f/255f
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        extendViewport.apply();
        batch.begin();

        if (orthogonalTiledMapRenderer != null)
            orthogonalTiledMapRenderer.render(); // Renders the map
        if (mapHandler != null)
            mapHandler.renderTiledMapTileMapObject(); // Renders background objects first

        for (KillBlock killBlock : killBlocks) killBlock.render(batch);
        for (Checkpoint checkpoint : checkpoints) checkpoint.render(batch);
        for (Coin coin : coins) coin.render(batch);
        for (Enemy enemy : enemies) enemy.render(batch);
        for (Ghost ghost : ghosts) ghost.render(batch);
        if (endpoint != null)
            endpoint.render(batch);
        if (player != null)
            player.render(batch);
        if (gameOver != null)
            gameOver.render(batch);
        for (Box box : boxes) box.render(batch);

        batch.end();

        screenViewport.apply();
        stage.act();
        stage.draw();

        if (box2DDebugRenderer != null)
            if (DEBUGGING) box2DDebugRenderer.render(world, camera.combined.scl(PPM));
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        //font.dispose();
        world.dispose();
        assetManager.dispose();
        box2DDebugRenderer.dispose();
        player.dispose();
        for (Box box : boxes) box.dispose();
        for(Enemy enemy : enemies) {
            enemy.dispose();
            if(enemy instanceof FinalBoss) ((FinalBoss) enemy).disposeBullets();
        }
        for(Ghost ghost : ghosts) ghost.dispose();
        for(Checkpoint checkpoint : checkpoints) checkpoint.dispose();
        for(KillBlock killBlock : killBlocks) killBlock.dispose();
        if(gameOver != null)
            gameOver.dispose();
        endpoint.dispose();
    }

    @Override
    public void resize(int width, int height) {
        extendViewport.update(width, height);
        screenViewport.update(width, height);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
        stage.addAction(Actions.fadeIn(1f));
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        stage.addAction(Actions.fadeOut(1f));
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.LEFT || keycode == Input.Keys.A) {
            movementHandler.leftPressed();
            return true;
        }
        if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D) {
            movementHandler.rightPressed();
            return true;
        }
        if (keycode == Input.Keys.SPACE || keycode == Input.Keys.W || keycode == Input.Keys.UP) {
            movementHandler.spacePressed();
            return true;
        }
        if (keycode == Input.Keys.SHIFT_LEFT) {
            movementHandler.shiftPressed();
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.LEFT || keycode == Input.Keys.A) {
            movementHandler.leftReleased();
            return true;
        }
        if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D) {
            movementHandler.rightReleased();
            return true;
        }
        if (keycode == Input.Keys.SPACE || keycode == Input.Keys.W || keycode == Input.Keys.UP) {
            movementHandler.spaceReleased();
            return true;
        }
        if (keycode == Input.Keys.SHIFT_LEFT) {
            movementHandler.shiftReleased();
            return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    private void update(float delta) {
        this.cameraUpdate();

        batch.setProjectionMatrix(camera.combined);
        orthogonalTiledMapRenderer.setView(camera);

        player.update(delta);
        movementHandler.update(delta);

        // To be moved to map handler
        for (Enemy enemy : enemies) enemy.update(delta);
        if (gameOver != null)
            gameOver.update(delta);

    }

    private void cameraUpdate() {
        Vector3 position = camera.position;
        position.x = player.getBody().getPosition().x * PPM;
        position.y = player.getBody().getPosition().y * PPM;
        camera.position.set(position);
        camera.update();
    }

    private void setupInterface() {
        Table root = new Table();
        root.setFillParent(true);

        Table coins = new Table();
        Image image = new Image(assetManager.get("interfaceAssets/coins.png", Texture.class));
        image.setScale(2.5f);
        coinValueLabel = new Label("0", InterfaceModule.setupFont(30, Color.WHITE));
        coins.add(image);
        coins.add(coinValueLabel).padLeft(25f).padBottom(30f);

        Table health = new Table();
        healthBar = new Image(assetManager.get("interfaceAssets/healthBar.png", Texture.class));
        healthBar.setScaleY(0.8f);
        healthBar.setScaleX(1.010f);
        healthImage = new Image(assetManager.get("interfaceAssets/health.png", Texture.class));
        healthImage.setScaleX(500f);
        healthImage.setScaleY(0.7f);
        playerHealthLabel = new Label("100", InterfaceModule.setupFont(30, Color.WHITE));
        staminaBar = new Image(assetManager.get("interfaceAssets/staminaBar.png", Texture.class));
        staminaBar.setScaleY(0.7f);
        staminaBar.setScaleX(300f);
        staminaImage = new Image(assetManager.get("interfaceAssets/stamina.png", Texture.class));
        staminaImage.setScaleY(0.7f);
        staminaImage.setScaleX(300f);

        health.add(healthBar).padBottom(-10f);
        health.add(healthImage).padLeft(-1002f).padBottom(-7f);
        health.add(playerHealthLabel).padLeft(-517f);
        health.add(staminaBar).padLeft(-800f).padBottom(-100f);
        health.add(staminaImage).padLeft(-800f).padBottom(-100f);

        root.add(coins).padLeft(15f).padTop(10f).expand().top().left();
        root.add(health).padBottom(100f).expand().bottom().left().row();

        stage.addActor(root);
    }

    public void updateCoins(int currentCollectedCoins) {
        coinValueLabel.setText(currentCollectedCoins);
    }

    private void loadAssets() {
        assetManager.load("interfaceAssets/coins.png", Texture.class);
        assetManager.load("interfaceAssets/health.png", Texture.class);
        assetManager.load("interfaceAssets/healthBar.png", Texture.class);
        assetManager.load("interfaceAssets/stamina.png", Texture.class);
        assetManager.load("interfaceAssets/staminaBar.png", Texture.class);
        assetManager.finishLoading();
    }

    public void updateHealthLabel(int currentPlayerHealth, int damageProvided) {
        if(currentPlayerHealth >= 0)
            playerHealthLabel.setText(currentPlayerHealth);
        else
            playerHealthLabel.setText(0);
        float scaleToBeRemoved = healthImage.getScaleX() - (damageProvided * 5f);
        if((damageProvided * 5f) > healthImage.getScaleX())
            scaleToBeRemoved = 0;
        healthImage.setScaleX(scaleToBeRemoved);
        /*
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (healthImage.getScaleX() > healthImage.getScaleX() - damageProvided * 5f && !player.hasRespawned()) {
                    healthImage.setScaleX(Math.max(healthImage.getScaleX() - damageProvided * 5f, healthImage.getScaleX() - 5f));
                    System.out.println(player.hasRespawned());
                }
                else {
                    this.cancel();
                }
            }
        }, 0f, 0.1f);
        */
    }

    public void updateStamina(int stamina) {
        staminaImage.setScaleX(stamina * 3f);
    }

    public void resetHealthLabel() {
        playerHealthLabel.setText(player.getHealth());
        healthImage.setScaleX(player.getHealth() * 5f);
    }

    public void loadMap(String mapName, int character) {
        if(currentMap != mapName && MusicModule.getWorldMusic(currentMap).isPlaying())
            MusicModule.getWorldMusic(currentMap).stop();
        currentMap = mapName;
        currentCharacter = character;
        Music worldMusic = MusicModule.getWorldMusic(currentMap);
        if(worldMusic != null) {
            worldMusic.play();
            worldMusic.setLooping(true);
            worldMusic.setVolume(0f);
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    if (!MusicModule.getMainMenuMusic().isPlaying() && worldMusic.isPlaying()) {
                        if (worldMusic.getVolume() < 0.3f) {
                            worldMusic.setVolume(Math.min(0.1f, worldMusic.getVolume() + 0.01f));
                        }
                        else {
                            this.cancel();
                        }
                    }
                }
            }, 0f, 0.1f);
        }
        orthogonalTiledMapRenderer = mapHandler.setup(1f, batch, currentMap, currentCharacter);
        movementHandler = new MovementHandler(player, this);
        world.setContactListener(new CollisionHandler(preferencesHandler, this));
    }

    public void clearBodies() {
        boxes.clear();
        coins.clear();
        ladders.clear();
        killBlocks.clear();
        enemies.clear();
        checkpoints.clear();
        ghosts.clear();

        bodies = new Array<Body>(world.getBodyCount());
        world.getBodies(bodies);
        for (Body body : bodies)
            world.destroyBody(body);
    }

    public String selectNextMap() {
        switch (currentMap) {
            case "MapTutorial":
                return "Map1";
            case "Map1":
                return "Map2";
            case "Map2":
                return "Map3";
            case "Map3":
                return "Map4";
            case "Map4":
                return "Map5";
        }
        return "MapTutorial";
    }

    public int selectNexCharacter() {
        switch (currentCharacter) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
        }
        return 0;
    }

    // Getters
    public World getWorld() {
        return world;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public PreferencesHandler getPreferencesHandler() {
        return preferencesHandler;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setGameOver(GameOver gameOver) {
        this.gameOver = gameOver;
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    public void addCheckpoint(Checkpoint checkpoint) {
        checkpoints.add(checkpoint);
    }

    public void addBox(Box box) {
        boxes.add(box);
    }

    public void addLadder(Ladder ladder) {
        ladders.add(ladder);
    }

    public void addCoin(Coin coin) {
        coins.add(coin);
    }

    public void addKillBlock(KillBlock killBlock) {
        killBlocks.add(killBlock);
    }

    public void addGhost(Ghost ghost) {
        ghosts.add(ghost);
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Box> getBoxes() {
        return boxes;
    }

    public ArrayList<Ladder> getLadders() {
        return ladders;
    }

    public ArrayList<Coin> getCoins() {
        return coins;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public ArrayList<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public ArrayList<Ghost> getGhosts() {
        return ghosts;
    }

}

package com.mpt.platform;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mpt.handlers.*;
import com.mpt.modules.InterfaceModule;
import com.mpt.modules.MusicModule;
import com.mpt.objects.endpoint.Endpoint;
import com.mpt.objects.enemy.*;
import com.mpt.objects.interactables.Box;
import com.mpt.objects.checkpoint.Checkpoint;
import com.mpt.objects.interactables.Coin;
import com.mpt.objects.interactables.KillBlock;
import com.mpt.objects.interactables.Ladder;
import com.mpt.objects.player.Player;

import java.util.ArrayList;

import static com.mpt.constants.Constants.DEBUGGING;
import static com.mpt.constants.Constants.PPM;

public class GameScreen extends ScreenAdapter implements InputProcessor {

    private final OrthographicCamera camera;
    private SpriteBatch batch;
    private Stage stage;
    private final World world;
    private final Box2DDebugRenderer box2DDebugRenderer;
    private final OrthogonalBleedingHandler orthogonalTiledMapRenderer;
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
    private InputMultiplexer inputMultiplexer;
    private Label coinValueLabel;

    private int screenWidth, screenHeight;

    public GameScreen() {
        batch = new SpriteBatch();
        world = new World(new Vector2(0, -25f), false);
        enemies = new ArrayList<>();
        checkpoints = new ArrayList<>();
        boxes = new ArrayList<>();
        ladders = new ArrayList<>();
        coins = new ArrayList<>();
        killBlocks = new ArrayList<>();
        box2DDebugRenderer = new Box2DDebugRenderer();

        preferencesHandler = new PreferencesHandler();

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        mapHandler = new MapHandler(this);
        orthogonalTiledMapRenderer = mapHandler.setup(1f, batch, "Map1");

        extendViewport = new ExtendViewport(30 * PPM, 20 * PPM);
        camera = (OrthographicCamera) extendViewport.getCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);

        screenViewport = new ScreenViewport();
        stage = new Stage(screenViewport, batch);
        setupInterface();

        inputMultiplexer = new InputMultiplexer(this, stage);

        movementHandler = new MovementHandler(player, this);

        world.setContactListener(new CollisionHandler(preferencesHandler, this));
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if(MusicModule.getMainMenuMusic().isPlaying()) {
                    if(MusicModule.getMainMenuMusic().getVolume() >= 0.01f)
                        MusicModule.getMainMenuMusic().setVolume(Math.max(0, MusicModule.getMainMenuMusic().getVolume() - 0.01f));
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
        this.update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        //36f/255f,61f/255f,71f/255f
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        extendViewport.apply();
        batch.begin();

        for(KillBlock killBlock : killBlocks) killBlock.render(batch);

        orthogonalTiledMapRenderer.render(); // Renders the map
        mapHandler.renderTiledMapTileMapObject(); // Renders background objects first

        for(Checkpoint checkpoint : checkpoints) checkpoint.render(batch);
        for(Coin coin : coins) if(!coin.getIsCollected()) coin.render(batch);
        for(Enemy enemy : enemies) {enemy.render(batch);}
        endpoint.render(batch);

        player.render(batch);
        for(Box box : boxes) box.render(batch);


        batch.end();

        screenViewport.apply();
        stage.act();
        stage.draw();

        if(DEBUGGING) box2DDebugRenderer.render(world, camera.combined.scl(PPM));
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        //font.dispose();
        world.dispose();
        for(Box box : boxes) box.dispose();
        box2DDebugRenderer.dispose();
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
        if(keycode == Input.Keys.LEFT || keycode == Input.Keys.A) {
            movementHandler.leftPressed();
            return true;
        }
        if(keycode == Input.Keys.RIGHT || keycode == Input.Keys.D) {
            movementHandler.rightPressed();
            return true;
        }
        if(keycode == Input.Keys.SPACE || keycode == Input.Keys.W || keycode == Input.Keys.UP) {
            movementHandler.spacePressed();
            return true;
        }
        if(keycode == Input.Keys.SHIFT_LEFT) {
            movementHandler.shiftPressed();
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.LEFT || keycode == Input.Keys.A) {
            movementHandler.leftReleased();
            return true;
        }
        if(keycode == Input.Keys.RIGHT || keycode == Input.Keys.D) {
            movementHandler.rightReleased();
            return true;
        }
        if(keycode == Input.Keys.SPACE || keycode == Input.Keys.W || keycode == Input.Keys.UP) {
            movementHandler.spaceReleased();
            return true;
        }
        if(keycode == Input.Keys.SHIFT_LEFT) {
            movementHandler.shiftReleased();
            return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {return false;}
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {return false;}
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {return false;}
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {return false;}
    @Override
    public boolean mouseMoved(int screenX, int screenY) {return false;}
    @Override
    public boolean scrolled(float amountX, float amountY) {return false;}

    private void update(float delta) {
        world.step(1/60f, 6, 2);
        this.cameraUpdate();

        batch.setProjectionMatrix(camera.combined);
        orthogonalTiledMapRenderer.setView(camera);

        player.update(delta);
        movementHandler.update(delta);

        // To be moved to map handler
        for(Enemy enemy : enemies) {enemy.update(delta);}

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

        Table main = new Table();
        Image image = new Image(new Texture(Gdx.files.internal("coin/coins.png")));
        image.setScale(2f);
        coinValueLabel = new Label("0", InterfaceModule.setupFont(36, Color.WHITE));

        main.add(image);
        main.add(coinValueLabel).padLeft(25f).padBottom(30f);
        root.add(main).padLeft(10f).padBottom(15f).expand().bottom().left();

        stage.addActor(root);
    }

    public void updateCoins(int currentCollectedCoins) {
        coinValueLabel.setText(currentCollectedCoins);
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

    public void setEndpoint(Endpoint endpoint) {this.endpoint = endpoint;}

    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }
    public void addCheckpoint(Checkpoint checkpoint) {
        checkpoints.add(checkpoint);
    }
    public void addBox(Box box) {boxes.add(box);}
    public void addLadder(Ladder ladder) { ladders.add(ladder);}
    public void addCoin(Coin coin) {
        coins.add(coin);
    }
    public void addKillBlock(KillBlock killBlock) {killBlocks.add(killBlock);}
    public Player getPlayer() {
        return player;
    }
    public ArrayList<Box> getBoxes(){return boxes;}
    public ArrayList<Ladder> getLadders(){return ladders;}
    public ArrayList<Coin> getCoins(){return coins;}
    public ArrayList<Enemy> getEnemies() {return enemies;}
}

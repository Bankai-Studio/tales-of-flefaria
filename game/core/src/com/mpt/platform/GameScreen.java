package com.mpt.platform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mpt.handlers.*;
import com.mpt.objects.GameEntity;
import com.mpt.objects.checkpoint.Checkpoint;
import com.mpt.objects.enemy.Enemy;
import com.mpt.objects.enemy.Slime;
import com.mpt.objects.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.mpt.constants.Constants.DEBUGGING;
import static com.mpt.constants.Constants.PPM;

public class GameScreen extends ScreenAdapter implements InputProcessor {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;
    private OrthogonalBleedingHandler orthogonalTiledMapRenderer;
    private MapHandler mapHandler;
    private Player player;
    private HashMap<String, Enemy> enemies;
    private ArrayList<Checkpoint> checkpoints;
    private Viewport viewport;
    private MovementHandler movementHandler;
    private PreferencesHandler preferencesHandler;

    private int screenWidth, screenHeight;

    public GameScreen() {
        batch = new SpriteBatch();
        world = new World(new Vector2(0, -25f), false);
        enemies = new HashMap<>();
        checkpoints = new ArrayList<>();
        box2DDebugRenderer = new Box2DDebugRenderer();

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        preferencesHandler = new PreferencesHandler();

        mapHandler = new MapHandler(this);
        orthogonalTiledMapRenderer = mapHandler.setup(1f, batch);

        viewport = new ExtendViewport(30 * PPM, 20 * PPM);
        camera = (OrthographicCamera) viewport.getCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);

        movementHandler = new MovementHandler(player);

        world.setContactListener(new CollisionHandler(preferencesHandler));
    }

    @Override
    public void render(float delta) {
        this.update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();

        batch.begin();
        orthogonalTiledMapRenderer.render();
        // Render the batch of sprites here
        batch.end();

        if(DEBUGGING) box2DDebugRenderer.render(world, camera.combined.scl(PPM));

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.LEFT || keycode == Input.Keys.A)
            movementHandler.leftPressed();
        if(keycode == Input.Keys.RIGHT || keycode == Input.Keys.D)
            movementHandler.rightPressed();
        if(keycode == Input.Keys.SPACE || keycode == Input.Keys.W || keycode == Input.Keys.UP)
            movementHandler.spacePressed();
        if(keycode == Input.Keys.SHIFT_LEFT)
            movementHandler.shiftPressed();
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.LEFT || keycode == Input.Keys.A)
            movementHandler.leftReleased();
        if(keycode == Input.Keys.RIGHT || keycode == Input.Keys.D)
            movementHandler.rightReleased();
        if(keycode == Input.Keys.SPACE || keycode == Input.Keys.W || keycode == Input.Keys.UP)
            movementHandler.spaceReleased();
        if(keycode == Input.Keys.SHIFT_LEFT)
            movementHandler.shiftReleased();
        return true;
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
        movementHandler.update(delta);
        for(Map.Entry<String,Enemy>  enemy : enemies.entrySet()) {
            if(enemy.getKey().equals("Slime")) {
                Slime slime = (Slime) enemy.getValue();
                slime.update(delta);
            }
        }

    }

    private void cameraUpdate() {
        Vector3 position = camera.position;
        position.x = player.getBody().getPosition().x * PPM;
        position.y = player.getBody().getPosition().y * PPM;
        camera.position.set(position);
        camera.update();
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

    public void addEnemy(String type, Enemy enemy) {
        enemies.put(type, enemy);
    }
    public void addCheckpoint(Checkpoint checkpoint) {
        checkpoints.add(checkpoint);
    }
}

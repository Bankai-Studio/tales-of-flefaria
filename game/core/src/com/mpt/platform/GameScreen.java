package com.mpt.platform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mpt.handlers.MapHandler;
import com.mpt.handlers.OrthogonalBleedingHandler;
import com.mpt.objects.player.Player;

import static com.mpt.constants.Constants.DEBUGGING;
import static com.mpt.constants.Constants.PPM;

/*
ScreenAdapter is a convenience implementation of the Screen class, allows you to override what you need.
    public class GameScreen implements Screen {}
*/

public class GameScreen extends ScreenAdapter {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;
    private OrthogonalBleedingHandler orthogonalTiledMapRenderer;
    private MapHandler mapHandler;
    private Player player;
    private Viewport viewport;
    private int screenWidth, screenHeight;

    public GameScreen() {
        batch = new SpriteBatch();
        world = new World(new Vector2(0, -25f), false);
        box2DDebugRenderer = new Box2DDebugRenderer();
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        mapHandler = new MapHandler(this);
        orthogonalTiledMapRenderer = mapHandler.setup(1f, batch);

        viewport = new ExtendViewport(30 * PPM, 20 * PPM);
        camera = (OrthographicCamera) viewport.getCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);

    }

    @Override
    public void render(float delta) {
        this.update();

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

    private void update() {
        world.step(1/60f, 6, 2);
        this.cameraUpdate();

        batch.setProjectionMatrix(camera.combined);
        orthogonalTiledMapRenderer.setView(camera);
        player.update();
    }

    private void cameraUpdate() {
        Vector3 position = camera.position;
        position.x = player.getBody().getPosition().x * PPM;
        position.y = player.getBody().getPosition().y * PPM;
        camera.position.set(position);
        camera.update();
    }

    public World getWorld() {
        return world;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}

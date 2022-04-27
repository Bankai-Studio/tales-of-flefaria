package com.mpt.platform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mpt.handlers.MapHandler;
import com.mpt.objects.player.Player;
import sun.jvm.hotspot.runtime.posix.POSIXSignals;

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
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private MapHandler mapHandler;
    private Player player;
    private int screenWidth, screenHeight;

    public GameScreen() {
        this.camera = new OrthographicCamera();
        this.batch = new SpriteBatch();
        this.world = new World(new Vector2(0, -25f), false);
        this.box2DDebugRenderer = new Box2DDebugRenderer();
        this.screenWidth = Gdx.graphics.getWidth();
        this.screenHeight = Gdx.graphics.getHeight();

        this.camera.setToOrtho(false, screenWidth, screenHeight);

        this.mapHandler = new MapHandler(this);
        this.orthogonalTiledMapRenderer = mapHandler.setup();
    }

    @Override
    public void render(float delta) {
        this.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        orthogonalTiledMapRenderer.render();
        batch.begin();
        // Render the batch of sprites here
        batch.end();

        box2DDebugRenderer.render(world, camera.combined.scl(PPM));

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
        position.x = Math.round(player.getBody().getPosition().x * PPM * 10) / 10f;
        position.y = Math.round(player.getBody().getPosition().y * PPM * 10) / 10f;
        camera.position.set(position);
        camera.update();
    }

    public World getWorld() {
        return world;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}

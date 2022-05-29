package com.mpt.handlers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.mpt.modules.BodyModule;
import com.mpt.objects.endpoint.Endpoint;
import com.mpt.objects.enemy.*;
import com.mpt.objects.interactables.Box;
import com.mpt.objects.checkpoint.Checkpoint;
import com.mpt.objects.interactables.Coin;
import com.mpt.objects.interactables.Ladder;
import com.mpt.objects.player.Player;
import com.mpt.platform.GameScreen;

import static com.mpt.constants.Constants.PPM;

public class MapHandler {
    private TiledMap tiledMap;
    private final GameScreen gameScreen;
    private SpriteBatch spriteBatch;

    public MapHandler(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public OrthogonalBleedingHandler setup(float unitScale, SpriteBatch batch, String mapName) {
        this.spriteBatch = batch;
        tiledMap = new TmxMapLoader().load("maps/" + mapName + "/Platform.tmx");
        parseMapObjects(tiledMap.getLayers().get("Objects").getObjects());
        return new OrthogonalBleedingHandler(tiledMap, unitScale, batch);
    }

    private void parseMapObjects(MapObjects mapObjects) {
        
        if(mapObjects.get("Spawnpoint") != null && mapObjects.get("Spawnpoint") instanceof RectangleMapObject) {
            Rectangle rectangle = (((RectangleMapObject) mapObjects.get("Spawnpoint")).getRectangle());
            gameScreen.getPreferencesHandler().setDefaultSpawn(new Vector2((rectangle.getX() + rectangle.getWidth() / 2) /  PPM, (rectangle.getY() + rectangle.getHeight() / 2) / PPM));
        }

        for(MapObject mapObject : mapObjects) {
            if(mapObject instanceof PolygonMapObject)
                createStaticObject((PolygonMapObject) mapObject);
            if(mapObject instanceof RectangleMapObject) {
                Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
                String rectangleName;
                if(mapObject.getName() != null)
                    rectangleName = mapObject.getName();
                else
                    throw new NullPointerException("There is a rectangle object with a null name.");

                if(rectangleName.equals("Player")) {
                    Body body = BodyModule.createBody(
                            rectangle.getX() + rectangle.getWidth() / 2,
                            rectangle.getY() + rectangle.getHeight() / 2,
                            rectangle.getWidth(),
                            rectangle.getHeight(),
                            false,
                            false,
                            0f,
                            0f,
                            gameScreen.getWorld()
                    );
                    gameScreen.setPlayer(new Player(rectangle.getWidth(), rectangle.getHeight(), body));
                    body.setTransform(gameScreen.getPreferencesHandler().getRespawnPosition(), body.getAngle());
                }
                if(rectangleName.equals("Checkpoint")) {
                    Body body = BodyModule.createBody(
                            rectangle.getX() + rectangle.getWidth() / 2,
                            rectangle.getY() + rectangle.getHeight() / 2,
                            rectangle.getWidth(),
                            rectangle.getHeight(),
                            true,
                            true,
                            0f,
                            0f,
                            gameScreen.getWorld()
                    );
                    gameScreen.addCheckpoint(new Checkpoint(rectangle.getWidth(), rectangle.getHeight(), body));
                }
                if(rectangleName.equals("Endpoint")) {
                    Body body = BodyModule.createBody(
                            rectangle.getX() + rectangle.getWidth() / 2,
                            rectangle.getY() + rectangle.getHeight() / 2,
                            rectangle.getWidth(),
                            rectangle.getHeight(),
                            true,
                            true,
                            0f,
                            0f,
                            gameScreen.getWorld()
                    );
                    gameScreen.setEndpoint(new Endpoint(rectangle.getWidth(), rectangle.getHeight(), body));
                }
                if(rectangleName.equals("Box")) {
                    Body body = BodyModule.createBody(
                            rectangle.getX() + rectangle.getWidth() / 2,
                            rectangle.getY() + rectangle.getHeight() / 2,
                            rectangle.getWidth(),
                            rectangle.getHeight(),
                            false,
                            false,
                            1000f,
                            0f,
                            gameScreen.getWorld()
                    );
                    gameScreen.addBox(new Box(rectangle.getWidth(), rectangle.getHeight(), body));
                }
                if(rectangleName.equals("Ladder")) {
                    Body body = BodyModule.createBody(
                            rectangle.getX() + rectangle.getWidth() / 2,
                            rectangle.getY() + rectangle.getHeight() / 2,
                            rectangle.getWidth(),
                            rectangle.getHeight(),
                            true,
                            true,
                            0f,
                            0f,
                            gameScreen.getWorld()
                    );
                    gameScreen.addLadder(new Ladder(rectangle.getWidth(), rectangle.getHeight(), body));
                }
                if(rectangleName.equals("Coin")) {
                    Body body = BodyModule.createBody(
                            rectangle.getX() + rectangle.getWidth() / 2,
                            rectangle.getY() + rectangle.getHeight() / 2,
                            rectangle.getWidth(),
                            rectangle.getHeight(),
                            true,
                            true,
                            0f,
                            0f,
                            gameScreen.getWorld()
                    );
                    gameScreen.addCoin(new Coin(rectangle.getWidth(), rectangle.getHeight(), body));
                }
                if(rectangleName.equals("Centipede")) {
                    Body body = createEnemyBody(rectangle);
                    gameScreen.addEnemy(new Centipede(rectangle.getWidth(), rectangle.getHeight(), body, gameScreen));
                }
                if(rectangleName.equals("Hyena")) {
                    Body body = createEnemyBody(rectangle);
                    gameScreen.addEnemy(new Hyena(rectangle.getWidth(), rectangle.getHeight(), body, gameScreen));
                }
                if(rectangleName.equals("BattleTurtle")) {
                    Body body = createEnemyBody(rectangle);
                    gameScreen.addEnemy(new BattleTurtle(rectangle.getWidth(), rectangle.getHeight(), body, gameScreen));
                }
                if(rectangleName.equals("BigBloated")) {
                    Body body = createEnemyBody(rectangle);
                    gameScreen.addEnemy(new BigBloated(rectangle.getWidth(), rectangle.getHeight(), body, gameScreen));
                }
                if(rectangleName.equals("Deceased")) {
                    Body body = createEnemyBody(rectangle);
                    gameScreen.addEnemy(new Deceased(rectangle.getWidth(), rectangle.getHeight(), body, gameScreen));
                }
                if(rectangleName.equals("Mummy")) {
                    Body body = createEnemyBody(rectangle);
                    gameScreen.addEnemy(new Mummy(rectangle.getWidth(), rectangle.getHeight(), body, gameScreen));
                }
                if(rectangleName.equals("Scorpio")) {
                    Body body = createEnemyBody(rectangle);
                    gameScreen.addEnemy(new Scorpio(rectangle.getWidth(), rectangle.getHeight(), body, gameScreen));
                }
                if(rectangleName.equals("Snake")) {
                    Body body = createEnemyBody(rectangle);
                    gameScreen.addEnemy(new Snake(rectangle.getWidth(), rectangle.getHeight(), body, gameScreen));
                }
                if(rectangleName.equals("Vulture")) {
                    Body body = createEnemyBody(rectangle);
                    gameScreen.addEnemy(new Vulture(rectangle.getWidth(), rectangle.getHeight(), body, gameScreen));
                }
            }
        }
    }

    private Body createEnemyBody(Rectangle rectangle){
        return BodyModule.createBody(
                rectangle.getX() + rectangle.getWidth() / 2,
                rectangle.getY() + rectangle.getHeight() / 2,
                rectangle.getWidth(),
                rectangle.getHeight(),
                false,
                false,
                0f,
                0f,
                gameScreen.getWorld()
        );
    }

    private void createStaticObject(PolygonMapObject polygonMapObject) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = gameScreen.getWorld().createBody(bodyDef);
        Shape shape = createPolygonShape(polygonMapObject);
        body.createFixture(shape, 1000);
        shape.dispose();
    }

    private Shape createPolygonShape(PolygonMapObject polygonMapObject) {
        float[] vertices = polygonMapObject.getPolygon().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for(int i = 0; i < vertices.length / 2; i++) {
            Vector2 currentVector = new Vector2(vertices[i * 2] / PPM, vertices[(i * 2) + 1]/PPM);
            worldVertices[i] = currentVector;
        }

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.set(worldVertices);
        return polygonShape;
    }

    public void renderTiledMapTileMapObject() {
        MapObjects mapObjects = tiledMap.getLayers().get("TileObjects").getObjects();
        for(MapObject mapObject : mapObjects) {
            if(mapObject instanceof TiledMapTileMapObject) {
                TiledMapTileMapObject tiledMapTileMapObject = (TiledMapTileMapObject) mapObject;
                TextureRegion textureRegion = tiledMapTileMapObject.getTile().getTextureRegion();

                float rotation = -tiledMapTileMapObject.getRotation();
                float scaleX = tiledMapTileMapObject.getScaleX();
                float scaleY = tiledMapTileMapObject.getScaleY();
                float xPos = tiledMapTileMapObject.getX();
                float yPos = tiledMapTileMapObject.getY();

                textureRegion.flip(tiledMapTileMapObject.isFlipHorizontally(), tiledMapTileMapObject.isFlipVertically());
                spriteBatch.draw(
                        textureRegion,
                        xPos,
                        yPos,
                        tiledMapTileMapObject.getOriginX() * scaleX,
                        tiledMapTileMapObject.getOriginY() * scaleY,
                        textureRegion.getRegionWidth() * scaleX,
                        textureRegion.getRegionHeight() * scaleY,
                        1f,
                        1f,
                        rotation);
                textureRegion.flip(tiledMapTileMapObject.isFlipHorizontally(), tiledMapTileMapObject.isFlipVertically());
            }
        }
    }
}
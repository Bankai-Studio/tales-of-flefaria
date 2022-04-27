package com.mpt.handlers;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class MapHandler {
    private TiledMap tiledMap;


    public MapHandler() {}

    public OrthogonalTiledMapRenderer setup() {
        tiledMap = new TmxMapLoader().load("maps/Testing.tmx");
        return new OrthogonalTiledMapRenderer(tiledMap);
    }

}

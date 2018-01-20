package com.unlucky.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.unlucky.animation.AnimationManager;
import com.unlucky.entity.Entity;
import com.unlucky.resource.ResourceManager;
import com.unlucky.resource.Util;

import java.util.Random;

/**
 * Creates a tilemap from a text file.
 * A map file has the format:
 *
 * mapWidth
 * mapHeight
 * t, t, t, t, ... -> mapWidth length
 * t, ...
 * t, ...
 * t, ...
 * mapHeight length
 * topLayer (0 - no top layer, 1 - top layer)
 * (if top layer)
 * s, s, s, s, ... -> mapWidth length
 * s, ...
 * s, ...
 * s, ...
 * mapHeight length
 *
 * t can be one of the following:
 * - tileID
 * - e[entityID]|[tileID] (means an Entity is placed on top of a tile)
 * - a[animIndex]|[numFrames]|[framesPerSecond] (an animated tile)
 *
 * s is the tileID of a NON-BLOCKED tile
 * if s is 0 then there is no tile
 *
 * @author Ming Li
 */
public class TileMap {

    // Tiles
    public int tileSize;

    // Map
    public String mapInfo;
    public String[] mapInfoLines;
    // array containing map information
    public Tile[] tileMap;
    public TextureRegion[] topLayer;
    public boolean hasTopLayer;
    public int mapWidth;
    public int mapHeight;
    public boolean[] collisionMap;

    public Vector2 origin;

    private Random rand;

    // res
    private ResourceManager rm;

    public TileMap(int tileSize, String path, Vector2 origin, ResourceManager rm) {
        this.tileSize = tileSize;
        this.origin = origin;
        this.rm = rm;

        rand = new Random();

        // read file into a String
        FileHandle file = Gdx.files.internal(path);
        mapInfo = file.readString();
        // split string by newlines
        mapInfoLines = mapInfo.split("\\r?\\n");
        mapWidth = Integer.parseInt(mapInfoLines[0]);
        mapHeight = Integer.parseInt(mapInfoLines[1]);

        tileMap = new Tile[mapWidth * mapHeight];
        topLayer = new TextureRegion[mapWidth * mapHeight];

        createTileMap();
        createTopLayer();

        collisionMap = new boolean[mapWidth * mapHeight];
        for (int i = 0; i < collisionMap.length; i++) {
            collisionMap[i] = tileMap[i].isBlocked();
        }
    }

    /**
     * Converts lines 2->n of the mapInfo to a 1d array of Tiles
     * representing a tile for each element
     */
    private void createTileMap() {
        for (int i = 2; i < mapHeight + 2; i++) {
            String[] row = mapInfoLines[i].split(",");
            // remove all whitespace from text so the map files can be more readable with spaces
            String[] trimmed = new String[row.length];
            for (int j = 0; j < row.length; j++) {
                trimmed[j] = row[j].replaceAll(" ", "");
            }
            for (int j = 0; j < mapWidth; j++) {
                String temp = trimmed[row.length - 1 - j];

                int k = (mapWidth * mapHeight - 1) - ((i - 2) * mapWidth + j);
                int l = rm.tiles16x16[0].length;

                int y = k / mapWidth;
                int x = k % mapWidth;

                Tile t;

                // check for Entity and tile format "e[Entity ID]|[tile id]" meaning
                // an Entity is placed on a certain tile
                if (temp.startsWith("e")) {
                    String removeSymbol = temp.substring(1, temp.length());
                    String[] bivalue = removeSymbol.split("\\|");

                    int entityID = Integer.parseInt(bivalue[0]);
                    int tileID = Integer.parseInt(bivalue[1]) - 1;

                    t = new Tile(tileID, rm.tiles16x16[tileID / l][tileID % l], new Vector2(x, y), rand);
                    t.addEntity(Util.getEntity(entityID, toMapCoords(x, y), this, rm));
                }
                // check for animated tile format
                else if (temp.startsWith("a")) {
                    String removeSymbol = temp.substring(1, temp.length());
                    String[] trivalue = removeSymbol.split("\\|");

                    int animIndex = Integer.parseInt(trivalue[0]);
                    int numFrames = Integer.parseInt(trivalue[1]);
                    int fps = Integer.parseInt(trivalue[2]);

                    AnimationManager anim = new AnimationManager(rm.atiles16x16, numFrames, animIndex, (float) 1 / fps);

                    t = new Tile(animIndex + 96, anim, new Vector2(x, y), rand);
                }
                else {
                    int index = Integer.parseInt(trimmed[row.length - 1 - j]) - 1;
                    t = new Tile(index, rm.tiles16x16[index / l][index % l], new Vector2(x, y), rand);
                }
                tileMap[k] = t;
            }
        }
    }

    /**
     * Creates the top layer of the map if there is one,
     * converting lines n+2 -> n+2+height into a 1d array of TextureRegions
     */
    private void createTopLayer() {
        // get boolean hasTopLayer
        int h = Integer.parseInt(mapInfoLines[mapHeight + 2]);
        hasTopLayer = h == 1 ? true : false;
        if (!hasTopLayer) return;

        for (int i = mapHeight + 3; i < 2 * mapHeight + 3; i++) {
            String[] row = mapInfoLines[i].split(",");
            String[] trimmed = new String[row.length];
            for (int j = 0; j < trimmed.length; j++) {
                trimmed[j] = row[j].replaceAll(" ", "");
            }
            for (int j = 0; j < mapWidth; j++) {
                int k = (mapWidth * mapHeight - 1) - ((i - mapHeight - 3) * mapWidth + j);
                int l = rm.tiles16x16[0].length;

                // index of -1 is null
                int index = Integer.parseInt(trimmed[row.length - 1 - j]) - 1;

                if (index == -1) topLayer[k] = null;
                else topLayer[k] = rm.tiles16x16[index / l][index % l];
            }
        }
    }

    public void update(float dt) {
        for (int i = 0; i < tileMap.length; i++) {
            if (tileMap[i].containsEntity()) {
                tileMap[i].getEntity().update(dt);
            }
            if (tileMap[i].animated) {
                tileMap[i].anim.update(dt);
            }
        }
    }

    /**
     * Renders the image representation of the map
     *
     * @param batch
     */
    public void render(SpriteBatch batch) {
        for (int i = 0; i < tileMap.length; i++) {
            int r = i / mapWidth;
            int c = i % mapWidth;

            if (tileMap[i].animated)
                batch.draw(tileMap[i].anim.getKeyFrame(true), origin.x + c * tileSize, origin.y + r * tileSize);
            else
                batch.draw(tileMap[i].sprite, origin.x + c * tileSize, origin.y + r * tileSize);

            // drawing an entity on a Tile
            if (tileMap[i].containsEntity()) {
                tileMap[i].getEntity().render(batch, true);
            }
        }
    }

    /**
     * Renders the top layer on top of the map
     *
     * @param batch
     */
    public void renderTopLayer(SpriteBatch batch) {
        for (int i = 0; i < topLayer.length; i++) {
            int r = i / mapWidth;
            int c = i % mapWidth;

            if (topLayer[i] != null) batch.draw(topLayer[i], origin.x + c * tileSize, origin.y + r * tileSize);
        }
    }

    /**
     * Adds an Entity to a specific tile on the map
     *
     * @param entity
     * @param tileX
     * @param tileY
     */
    public void addEntity(Entity entity, int tileX, int tileY) {
        tileMap[tileY * mapWidth + tileX].addEntity(entity);
    }

    /**
     * Vector2 version
     *
     * @param entity
     * @param coords
     */
    public void addEntity(Entity entity, Vector2 coords) {
        tileMap[(int) (coords.y * mapWidth + coords.x)].addEntity(entity);
    }

    /**
     * Removes an Entity at a specific tile on the map
     *
     * @param tileX
     * @param tileY
     */
    public void removeEntity(int tileX, int tileY) {
        tileMap[tileY * mapWidth + tileX].removeEntity();
    }

    /**
     * Vector2 version
     *
     * @param coords
     */
    public void removeEntity(Vector2 coords) {
        tileMap[(int) (coords.y * mapWidth + coords.x)].removeEntity();
    }

    /**
     * Gets an Entity from a specific tile on the map
     *
     * @param tileX
     * @param tileY
     * @return
     */
    public Entity getEntity(int tileX, int tileY) {
        return tileMap[tileY * mapWidth + tileX].getEntity();
    }

    /**
     * Vector2 version
     *
     * @param coords
     * @return
     */
    public Entity getEntity(Vector2 coords) {
        return tileMap[(int) (coords.y * mapWidth + coords.x)].getEntity();
    }

    /**
     * Determines if there's an Entity on a specific tile on the map
     *
     * @param tileX
     * @param tileY
     * @return
     */
    public boolean containsEntity(int tileX, int tileY) {
        return tileMap[tileY * mapWidth + tileX].containsEntity();
    }

    /**
     * Vector2 version
     *
     * @param coords
     * @return
     */
    public boolean containsEntity(Vector2 coords) {
        return tileMap[(int) (coords.y * mapWidth + coords.x)].containsEntity();
    }

    /**
     * Replaces a Tile on a tile map
     *
     * @param tileX
     * @param tileY
     */
    public void setTile(int tileX, int tileY, Tile tile) {
        tileMap[tileY * mapWidth + tileX] = tile;
    }

    /**
     * Replaces a Tile by tile id
     *
     * @param tileX
     * @param tileY
     * @param id
     */
    public void setTile(int tileX, int tileY, int id) {
        int r = id / rm.tiles16x16[0].length;
        int c = id % rm.tiles16x16.length;
        tileMap[tileY * mapWidth + tileX] = new Tile(id, rm.tiles16x16[r][c], new Vector2(tileX, tileY), rand);
    }

    /**
     * Converts tile coordinates to map coordinates
     *
     * @param tileX
     * @param tileY
     * @return
     */
    public Vector2 toMapCoords(int tileX, int tileY) {
        return new Vector2(tileX * tileSize, tileY * tileSize);
    }

    public Vector2 toMapCoords(Vector2 coords) {
        return new Vector2(coords.x * tileSize, coords.y * tileSize);
    }

    /**
     * Converts map coordinates to tile coordinates
     *
     * @param mapX
     * @param mapY
     * @return
     */
    public Vector2 toTileCoords(int mapX, int mapY) {
        return new Vector2(mapX / tileSize, mapY / tileSize);
    }

    public Vector2 toTileCoords(Vector2 coords) {
        return new Vector2(coords.x / tileSize, coords.y / tileSize);
    }

    /**
     * Returns a tile from the tile map at (x,y) tile position
     *
     * @return Tile
     */
    public Tile getTile(int tileX, int tileY) {
        return tileMap[tileY * mapWidth + tileX];
    }

    public Tile getTile(Vector2 coords) {
        return tileMap[(int) (coords.y * mapWidth + coords.x)];
    }

    /**
     * Does the tile map contain some Tile?
     *
     * @param tile
     * @return Boolean
     */
    public boolean mapContains(Tile tile) {
        for (int i = 0; i < tileMap.length; i++) {
            if (tileMap[i].equals(tile)) return true;
        }
        return false;
    }

    /**
     * Does the tile map contain a Tile that has a given id?
     *
     * @param id
     * @return Boolean
     */
    public boolean mapContains(int id) {
        for (int i = 0; i < tileMap.length; i++) {
            if (tileMap[i].id == id) return true;
        }
        return false;
    }

}

package com.unlucky.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.unlucky.entity.Entity;
import com.unlucky.resource.ResourceManager;
import com.unlucky.resource.Util;

import java.util.Random;

/**
 * Creates a tilemap from a text file
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

        convert();

        collisionMap = new boolean[mapWidth * mapHeight];
        for (int i = 0; i < collisionMap.length; i++) {
            collisionMap[i] = tileMap[i].isBlocked();
        }
    }

    /**
     * Converts lines 2->n of the mapInfo to a 1d array of TextureRegions
     * representing a tile for each element
     */
    private void convert() {
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
                    String[] bivalue = removeSymbol.split("|");
                    int entityID = Integer.parseInt(bivalue[0]);
                    int tileID = Integer.parseInt(bivalue[2]);

                    t = new Tile(tileID, rm.tiles16x16[tileID / l][tileID % l], new Vector2(x, y), rand);
                    t.addEntity(Util.getEntity(entityID, toMapCoords(x, y), this, rm));
                }
                else {
                    int index = Integer.parseInt(trimmed[row.length - 1 - j]);

                    // tiles file has 16 tiles across
                    int r = index / rm.tiles16x16[0].length;
                    int c = index % rm.tiles16x16[0].length;

                    t = new Tile(index, rm.tiles16x16[r][c], new Vector2(x, y), rand);
                }
                tileMap[k] = t;
            }
        }
    }

    public void update(float dt) {
        for (int i = 0; i < tileMap.length; i++) {
            if (tileMap[i].containsEntity()) {
                tileMap[i].getEntity().update(dt);
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

            batch.draw(tileMap[i].sprite, origin.x + c * tileSize, origin.y + r * tileSize);

            // drawing an entity on a Tile
            if (tileMap[i].containsEntity()) {
                tileMap[i].getEntity().render(batch, true);
            }
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

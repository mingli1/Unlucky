package com.unlucky.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.unlucky.resource.ResourceManager;

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

    public Vector2 origin;

    // res
    private ResourceManager rm;

    public TileMap(int tileSize, String path, Vector2 origin, ResourceManager rm) {
        this.tileSize = tileSize;
        this.origin = origin;
        this.rm = rm;

        // read file into a String
        FileHandle file = Gdx.files.internal(path);
        mapInfo = file.readString();
        // split string by newlines
        mapInfoLines = mapInfo.split("\\r?\\n");
        mapWidth = Integer.parseInt(mapInfoLines[0]);
        mapHeight = Integer.parseInt(mapInfoLines[1]);

        tileMap = new Tile[mapWidth * mapHeight];

        convert();
    }

    /**
     * Converts lines 2->n of the mapInfo to a 1d array of TextureRegions
     * representing a tile for each element
     */
    private void convert() {
        for (int i = 2; i < mapHeight + 2; i++) {
            String[] row = mapInfoLines[i].split(",");
            for (int j = 0; j < mapWidth; j++) {
                int index = Integer.parseInt(row[row.length - 1 - j]);

                int y = (i - 2) * mapWidth + j / mapWidth;
                int x = (i - 2) * mapWidth + j % mapWidth;

                int k = (mapWidth * mapHeight - 1) - ((i - 2) * mapWidth + j);

                // tiles file has 16 tiles across
                int r = index / rm.tiles16x16[0].length;
                int c = index % rm.tiles16x16[0].length;

                Tile t = new Tile(index, rm.tiles16x16[r][c], new Vector2(x, y));
                tileMap[k] = t;
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
        }
    }

    /**
     * Replaces a Tile on a tile map
     *
     * @param tileX
     * @param tileY
     */
    public void setTile(int tileX, int tileY, Tile tile) {
        tileMap[tileX * mapWidth + tileY] = tile;
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
        tileMap[tileX * mapWidth + tileY] = new Tile(id, rm.tiles16x16[r][c], new Vector2(tileX, tileY));
    }

    /**
     * Returns a tile from the tile map at (x,y) tile position
     *
     * @return Tile
     */
    public Tile getTile(int tileX, int tileY) {
        return tileMap[tileX * mapWidth + tileY];
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

package com.unlucky.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.unlucky.entity.Entity;

/**
 * Stores information about each tile on a map, including what it contains (Entity)
 * and how the player interacts with it
 *
 * @author Ming Li
 */
public class Tile {

    // Image representation of tile
    public TextureRegion sprite;

    /**
     * Types of Tiles
     * - normal: Player can pass through
     * - blocked: Player cannot go through
     */
    public static final byte NORMAL = 0;
    public static final byte BLOCKED = 1;

    // Each tile has a unique identifier
    public int id;
    // Tile type is determined by id
    public int type;

    // coords
    public Vector2 tilePosition;

    // The Entity a Tile could contain
    public Entity hold;

    public Tile(int id, TextureRegion sprite, Vector2 tilePosition) {
        this.id = id;
        this.sprite = sprite;
        this.tilePosition = tilePosition;

        // a Tile originally has no Entity
        hold = null;

        if (id > 8 && id < 16) type = BLOCKED;
        else type = NORMAL;
    }

    public void addEntity(Entity e) {
        if (!containsEntity()) hold = e;
    }

    public boolean containsEntity() {
        return hold == null;
    }

    public boolean isBlocked() {
        return type == 0;
    }

}

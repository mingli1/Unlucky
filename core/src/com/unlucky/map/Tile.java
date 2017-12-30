package com.unlucky.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.unlucky.entity.Entity;

import java.util.Random;

/**
 * Stores information about each tile on a map, including what it contains (Entity)
 * and how the player interacts with it
 *
 * @author Ming Li
 */
public class Tile {

    private Random rand;

    // Image representation of tile
    public TextureRegion sprite;

    /**
     * Types of Tiles
     * - normal: Player can pass through
     * - blocked: Player cannot go through
     * - change: Player loses or gains movement magnitude after stepping on it
     */
    public static final byte NORMAL = 0;
    public static final byte BLOCKED = 1;
    public static final byte CHANGE = 2;

    // Each tile has a unique identifier
    public int id;
    // Tile type is determined by id
    public int type;

    // coords
    public Vector2 tilePosition;

    // The Entity a Tile could contain
    public Entity hold;

    // Tile properties
    public int magOffset;

    public Tile(int id, TextureRegion sprite, Vector2 tilePosition, Random rand) {
        this.id = id;
        this.sprite = sprite;
        this.tilePosition = tilePosition;
        this.rand = rand;

        // a Tile originally has no Entity
        hold = null;

        if (id == 9) type = BLOCKED;
        else if (id == 5) type = CHANGE;
        else type = NORMAL;

        // magOffset can either be -1 or 1
        if (isChange()) {
            int r = rand.nextInt(2);
            if (r == 0) magOffset = 1;
            if (r == 1) magOffset = -1;
        }
    }

    public void addEntity(Entity e) {
        if (!containsEntity()) hold = e;
    }

    public void removeEntity() {
        if (hold != null) hold = null;
    }

    public Entity getEntity() { return hold; }

    public boolean containsEntity() {
        return hold != null;
    }

    public boolean isBlocked() {
        return type == BLOCKED;
    }

    public boolean isChange() { return type == CHANGE; }

    public int getMagOffset() { return magOffset; }

}

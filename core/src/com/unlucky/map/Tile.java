package com.unlucky.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.unlucky.animation.AnimationManager;
import com.unlucky.entity.Entity;
import com.unlucky.resource.Util;

/**
 * Stores information about each tile on a map, including what it contains (Entity)
 * and how the player interacts with it
 *
 * @author Ming Li
 */
public class Tile {

    // Image representation of tile
    public TextureRegion sprite;
    // animation of a tile
    public AnimationManager anim;

    /**
     * Types of Tiles
     * - normal: Player can pass through
     * - blocked: Player cannot go through
     * - change: Player goes forwards or backwards from the tile in the direction they entered
     * - in and out: Player goes 1 tile in a random direction not the direction they entered the tile on
     * - stop: Player's movement is stopped when this tile is stepped on
     * - down, up, right, left: Player's direction is changed to 1 tile in the direction
     * of the tile stepped on
     * - question mark: Player can obtain gold, healing, or items from stepping on it. Once
     * stepped on, it disappears
     * - ice: Player slides on the ice, moving until it reaches the end of the ice or a blocked tile
     * - teleport: Player teleports to another random teleportation tile on the map
     * - exclamation mark: Player gets damaged or loses a random amount of gold
     * - end: stepping on this tile means the player has completed the map
     */
    public static final byte NORMAL = 0;
    public static final byte BLOCKED = 1;
    public static final byte CHANGE = 2;
    public static final byte IN_AND_OUT = 3;
    public static final byte STOP = 4;
    public static final byte DOWN = 5;
    public static final byte UP = 6;
    public static final byte RIGHT = 7;
    public static final byte LEFT = 8;
    public static final byte QUESTION_MARK = 9;
    public static final byte ICE = 10;
    public static final byte TELEPORT = 11;
    public static final byte EXCLAMATION_MARK = 12;
    public static final byte END = 13;

    // Each tile has a unique identifier
    public int id;
    // Tile type is determined by id
    public int type;
    // tiles can be animated
    public boolean animated;

    // coords
    public Vector2 tilePosition;

    // The Entity a Tile could contain
    public Entity hold;

    /**
     * A regular non-animated tile
     *
     * @param id
     * @param sprite
     * @param tilePosition
     */
    public Tile(int id, TextureRegion sprite, Vector2 tilePosition) {
        this.id = id;
        this.sprite = sprite;
        this.tilePosition = tilePosition;

        // a Tile originally has no Entity
        hold = null;

        animated = false;

        if (Util.isBlockedTile(id)) type = BLOCKED;
        else if ((id >= 171 && id <= 173) || (id >= 187 && id <= 189) || (id >= 203 && id <= 205)
            || id == 220 || id == 221 || id == 236 || id == 237) type = ICE;
        else type = NORMAL;
    }

    /**
     * An animated tile
     *
     * @param id is animIndex + 96 since all animated tile ids will begin at 96
     * @param anim
     * @param tilePosition
     */
    public Tile(int id, AnimationManager anim, Vector2 tilePosition) {
        this.id = id;
        this.anim = anim;
        this.tilePosition = tilePosition;

        hold = null;

        animated = true;

        if (Util.isBlockedAnimatedTile(id)) type = BLOCKED;
        else if (id == 99) type = CHANGE;
        else if (id == 100) type = IN_AND_OUT;
        else if (id == 101) type = STOP;
        else if (id == 102) type = DOWN;
        else if (id == 103) type = UP;
        else if (id == 104) type = RIGHT;
        else if (id == 105) type = LEFT;
        else if (id == 106) type = QUESTION_MARK;
        else if (id == 107) type = TELEPORT;
        else if (id == 108) type = EXCLAMATION_MARK;
        else if (id == 110) type = END;
        else type = NORMAL;
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

    public boolean isInAndOut() { return type == IN_AND_OUT; }

    public boolean isStop() { return type == STOP; }

    public boolean isDown() { return type == DOWN; }

    public boolean isUp() { return type == UP; }

    public boolean isRight() { return type == RIGHT; }

    public boolean isLeft() { return type == LEFT; }

    public boolean isQuestionMark() { return type == QUESTION_MARK; }

    public boolean isIce() { return type == ICE; }

    public boolean isTeleport() { return type == TELEPORT; }

    public boolean isExclamationMark() { return type == EXCLAMATION_MARK; }

    public boolean isEnd() { return type == END; }

    /**
     * A special tile is any tile not normal or blocked
     *
     * @return
     */
    public boolean isSpecial() {
        return type != NORMAL && type != BLOCKED;
    }

    /**
     * A moving tile is any tile where the player cannot stay on
     *
     * @return
     */
    public boolean isMoving() {
        return type == 2 || type == 3 || (type > 4 && type < 9) || type == ICE;
    }

}
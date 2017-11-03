package com.unlucky.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.unlucky.animation.AnimationManager;
import com.unlucky.map.Tile;
import com.unlucky.map.TileMap;
import com.unlucky.resource.ResourceManager;

/**
 * The superclass of all things in the game (player, enemies, items, etc.)
 *
 * @author Ming Li
 */
public class Entity {

    protected String id;
    protected ResourceManager rm;

    // animation
    protected AnimationManager am;

    /**
     * 0 - down
     * 1 - up
     * 2 - right
     * 3 - left
     */
    protected int dir;

    // position (x,y) in tile coordinates
    protected Vector2 position;
    // the target position the Entity is going towards
    protected Vector2 target;
    /**
     * 0 - idle
     * 1 - down
     * 2 - up
     * 3 - right
     * 4 - left
     */
    protected int movement;
    protected boolean moving;
    protected float speed;

    // removing an Entity
    protected boolean shouldDestroy;
    protected boolean destroyed;

    // map
    protected TileMap tileMap;

    public Entity(Vector2 position, TileMap tileMap, ResourceManager rm) {
        this.position = position;
        this.tileMap = tileMap;
        this.rm = rm;

        shouldDestroy = destroyed = false;
    }

    public void update(float dt) {
        if (!destroyed) {
            move();
            am.update(dt);
        }
    }

    public void render(SpriteBatch batch, boolean looping) {
        if (!destroyed) {
            batch.draw(am.getKeyFrame(looping),
                    position.x * tileMap.tileSize - am.width / 2,
                    position.y * tileMap.tileSize - am.height / 2);
        }
    }

    /**
     * A key input is processed into one of 4 directions
     *
     * @param movement
     */
    public void setMovement(int movement) {
        if (moving) return;
        this.movement = movement;
        moving = canMove();
    }

    /**
     * Determines if an Entity is able to move to a target position. (collision detection)
     *
     * @return moving
     */
    public boolean canMove() {
        if (moving) return true;

        // If the player's next position is out of bounds or in a blocked tile, moving = 0
        switch (movement) {
            // down
            case 1:
                if (position.y - 1 == -1 || tileMap.getTile((int) position.x, (int) position.y - 1).type == Tile.BLOCKED)
                    return false;
                else
                    target.y -= 1;
                break;
            // up
            case 2:
                if (position.y + 1 == tileMap.mapHeight || tileMap.getTile((int) position.x, (int) position.y + 1).type == Tile.BLOCKED)
                    return false;
                else
                    target.y += 1;
                break;
            // right
            case 3:
                if (position.x + 1 == tileMap.mapWidth || tileMap.getTile((int) position.x + 1, (int) position.y).type == Tile.BLOCKED)
                    return false;
                else
                    target.x += 1;
                break;
            // left
            case 4:
                if (position.x - 1 == -1 || tileMap.getTile((int) position.x - 1, (int) position.y).type == Tile.BLOCKED)
                    return false;
                else
                    target.x -= 1;
                break;
        }

        return true;
    }

    /**
     * Moves an Entity from its current position to its target position
     */
    public void move() {
        if (position.x == target.x && position.y == target.y) movement = 0;

        // down
        if (movement == 1 && position.y > target.y) position.y -= speed;
        else movement = 0;
        if (movement == 1 && position.y < target.y) position.y = target.y;

        // up
        if (movement == 2 && position.y < target.y) position.y += speed;
        else movement = 0;
        if (movement == 2 && position.y > target.y) position.y = target.y;

        // right
        if (movement == 3 && position.x < target.x) position.x += speed;
        else movement = 0;
        if (movement == 3 && position.x > target.x) position.x = target.x;

        // left
        if (movement == 4 && position.x > target.x) position.x -= speed;
        else movement = 0;
        if (movement == 4 && position.x < target.x) position.x = target.x;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setTarget(Vector2 target) {
        this.target = target;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public void setShouldDestroy(boolean shouldDestroy) {
        this.shouldDestroy = shouldDestroy;
    }

    public String getId() {
        return id;
    }

    public int getDir() {
        return dir;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getTarget() {
        return target;
    }

    public boolean getMoving() {
        return moving;
    }

    public boolean isShouldDestroy() {
        return shouldDestroy;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

}

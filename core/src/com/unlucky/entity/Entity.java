package com.unlucky.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.unlucky.animation.AnimationManager;
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

    // position (x,y) in map coordinates (tile * tileSize)
    protected Vector2 position;
    /**
     * 0 - down
     * 1 - up
     * 2 - right
     * 3 - left
     */
    protected boolean[] moving;
    protected float speed;
    // the Entity's current tile coordinates
    protected int currentTileX;
    protected int currentTileY;
    // The amount of tiles for a direction to move
    protected int magnitude;

    // removing an Entity
    protected boolean shouldDestroy;
    protected boolean destroyed;

    // map
    protected TileMap tileMap;

    public Entity(String id, Vector2 position, TileMap tileMap, ResourceManager rm) {
        this.id = id;
        this.position = position;
        this.tileMap = tileMap;
        this.rm = rm;

        moving = new boolean[4];
        for (int i = 0; i < 4; i++) moving[i] = false;

        shouldDestroy = destroyed = false;
    }

    public void update(float dt) {
        if (!destroyed) {
            // movement
            handleMovement();

            // animation
            am.update(dt);
        }
    }

    public void render(SpriteBatch batch, boolean looping) {
        if (!destroyed) {
            batch.draw(am.getKeyFrame(looping), position.x + 1, position.y);
        }
    }

    /**
     * Moves an entity to a target position with a given magnitude.
     * Player movement triggered by input
     *
     * @param dir
     */
    public void move(int dir, int mag) {
        currentTileX = (int) (position.x / tileMap.tileSize);
        currentTileY = (int) (position.y / tileMap.tileSize);
        magnitude = mag;

        moving[dir] = true;
    }

    /**
     * Checks if movement boolean array is all false
     *
     * @return
     */
    public boolean canMove() {
        for (boolean i : moving) if (i) return false;
        return true;
    }

    /**
     * If an entity cannot move the full magnitude in a direction, this calculates
     * the farthest it can go before it has to stop
     *
     * @param dir
     * @return
     */
    public int adjustMagnitude(int dir) {
        switch (dir) {
            case 0: // down
                for (int i = currentTileY; i >= currentTileY - magnitude; i--) {
                    if (tileMap.getTile(currentTileX, i).isBlocked() || i < 0) {
                        if (i == currentTileY - 1) return currentTileY;
                        else return i + 1;
                    }
                }
                return currentTileY - magnitude;
            case 1: // up
                for (int i = currentTileY; i <= currentTileY + magnitude; i++) {
                    if (tileMap.getTile(currentTileX, i).isBlocked() || i == tileMap.mapHeight) {
                        if (i == currentTileY + 1) return currentTileY;
                        else return i - 1;
                    }
                }
                return currentTileY + magnitude;
            case 2: // right
                for (int i = currentTileX; i <= currentTileX + magnitude; i++) {
                    if (tileMap.getTile(i, currentTileY).isBlocked() || i >= tileMap.mapWidth - 1) {
                        if (i == currentTileX + 1) return currentTileX;
                        else return i - 1;
                    }
                }
                return currentTileX + magnitude;
            case 3: // left
                for (int i = currentTileX; i >= currentTileX - magnitude; i--) {
                    if (tileMap.getTile(i, currentTileY).isBlocked() || i == 0) {
                        if (i == currentTileX - 1) return currentTileX;
                        else return i + 1;
                    }
                }
                return currentTileX - magnitude;
        }
        return 0;
    }

    /**
     * Updates every tick and moves an Entity if not on the tile map grid
     */
    public void handleMovement() {
        // down
        if (moving[0]) {
            int targetY = adjustMagnitude(0);
            if (targetY == currentTileY) {
                moving[0] = false;
            } else {
                position.y -= speed;
                if (position.y <= targetY * tileMap.tileSize) {
                    position.y = targetY * tileMap.tileSize;
                    moving[0] = false;
                }
            }
        }
        // up
        else if (moving[1]) {
            int targetY = adjustMagnitude(1);
            if (targetY == currentTileY) {
                moving[1] = false;
            } else {
                position.y += speed;
                if (position.y >= targetY * tileMap.tileSize) {
                    position.y = targetY * tileMap.tileSize;
                    moving[1] = false;
                }
            }
        }
        // right
        else if (moving[2]) {
            int targetX = adjustMagnitude(2);
            if (targetX == currentTileX) {
                moving[2] = false;
            } else {
                position.x += speed;
                if (position.x >= targetX * tileMap.tileSize) {
                    position.x = targetX * tileMap.tileSize;
                    moving[2] = false;
                }
            }
        }
        // left
        else if (moving[3]) {
            int targetX = adjustMagnitude(3);
            if (targetX == currentTileX) {
                moving[3] = false;
            } else {
                position.x -= speed;
                if (position.x <= targetX * tileMap.tileSize) {
                    position.x = targetX * tileMap.tileSize;
                    moving[3] = false;
                }
            }
        }
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setShouldDestroy(boolean shouldDestroy) {
        this.shouldDestroy = shouldDestroy;
    }

    public AnimationManager getAm() { return am; }

    public String getId() {
        return id;
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean isMoving(int dir) {
        return moving[dir];
    }

    public boolean isShouldDestroy() {
        return shouldDestroy;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

}

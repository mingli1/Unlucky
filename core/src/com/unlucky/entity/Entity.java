package com.unlucky.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.unlucky.animation.AnimationManager;
import com.unlucky.battle.Moveset;
import com.unlucky.map.TileMap;
import com.unlucky.resource.ResourceManager;

import java.util.Random;

/**
 * The superclass of all things in the game (player, enemies, items, etc.)
 *
 * @author Ming Li
 */
public class Entity {

    protected String id;
    protected ResourceManager rm;
    protected Random rand;

    // animation
    protected AnimationManager am;
    // battle scene animation
    protected AnimationManager bam;

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

    // map
    protected TileMap tileMap;

    /******** RPG ASPECTS *********/

    protected Moveset moveset;

    protected int hp;
    protected int maxHp;
    // for animation to keep track of hp difference between attacks
    protected int previousHp;
    // for applying the hp change after the dialogue is finished
    protected int damage = 0;
    protected int healing = 0;
    // 0-100 in % points
    protected int accuracy;
    // damage range
    protected int minDamage;
    protected int maxDamage;

    // level information
    protected int level;
    protected int exp;
    protected int maxExp;

    public Entity(String id, Vector2 position, TileMap tileMap, ResourceManager rm) {
        this.id = id;
        this.position = position;
        this.tileMap = tileMap;
        this.rm = rm;

        rand = new Random();

        moving = new boolean[4];
        for (int i = 0; i < 4; i++) moving[i] = false;
    }

    public void update(float dt) {
        // movement
        handleMovement();

        // handle RPG elements
        if (hp > maxHp) hp = maxHp;
        if (hp <= 0) {
            hp = 0;
        }

        // animation
        am.update(dt);
    }

    public void render(SpriteBatch batch, boolean looping) {
        // draw shadow
        batch.draw(rm.shadow11x6, position.x + 3, position.y - 3);
        batch.draw(am.getKeyFrame(looping), position.x, position.y);
    }

    /**
     * An entity's hp is decreased by damage taken
     *
     * @param damage
     */
    public void hit(int damage) {
        this.damage = damage;
    }

    /**
     * An entity's hp is increased by healing
     *
     * @param healing
     */
    public void heal(int healing) {
        this.healing = healing;
    }

    /**
     * Returns true if the entity is dead after a change in hp
     */
    public boolean applyDamage() {
        previousHp = hp;
        hp -= damage;
        damage = 0;
        if (hp <= 0) {
            hp = 0;
            return true;
        }
        return false;
    }

    public void applyHeal() {
        previousHp = hp;
        hp += healing;
        healing = 0;
        if (hp > maxHp) hp = maxHp;
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
        magnitude += adjustForOffset(dir);

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
     * If an entity encounters another Entity in its path, the Entity will go on the same
     * tile as the Entity encountered to trigger an interaction
     *
     * @param dir
     * @return
     */
    public int adjustMagnitude(int dir) {
        switch (dir) {
            case 0: // down
                for (int i = currentTileY; i >= currentTileY - magnitude; i--) {
                    if (tileMap.getTile(currentTileX, i).isBlocked() || i <= 0) {
                        if (i == currentTileY - 1) {
                            return currentTileY;
                        }
                        else return i + 1;
                    }
                    if (tileMap.containsEntity(currentTileX, i)) {
                        return i;
                    }
                }
                return currentTileY - magnitude;
            case 1: // up
                for (int i = currentTileY; i <= currentTileY + magnitude; i++) {
                    if (tileMap.getTile(currentTileX, i).isBlocked() || i >= tileMap.mapHeight - 1) {
                        if (i == currentTileY + 1) {
                            return currentTileY;
                        }
                        else return i - 1;
                    }
                    if (tileMap.containsEntity(currentTileX, i)) {
                        return i;
                    }
                }
                return currentTileY + magnitude;
            case 2: // right
                for (int i = currentTileX; i <= currentTileX + magnitude; i++) {
                    if (tileMap.getTile(i, currentTileY).isBlocked() || i >= tileMap.mapWidth - 1) {
                        if (i == currentTileX + 1) {
                            return currentTileX;
                        }
                        else return i - 1;
                    }
                    if (tileMap.containsEntity(i, currentTileY)) {
                        return i;
                    }
                }
                return currentTileX + magnitude;
            case 3: // left
                for (int i = currentTileX; i >= currentTileX - magnitude; i--) {
                    if (tileMap.getTile(i, currentTileY).isBlocked() || i <= 0) {
                        if (i == currentTileX - 1) {
                            return currentTileX;
                        }
                        else return i + 1;
                    }
                    if (tileMap.containsEntity(i, currentTileY)) {
                        return i;
                    }
                }
                return currentTileX - magnitude;
        }
        return 0;
    }

    /**
     * Determines if there is a slow or boost tile in the path of the Entity
     */
    public int adjustForOffset(int dir) {
        switch (dir) {
            case 0: // down
                for (int i = currentTileY; i >= currentTileY - magnitude; i--) {
                    if (i > 0) {
                        if (tileMap.getTile(currentTileX, i).isChange())
                            return tileMap.getTile(currentTileX, i).getMagOffset();
                    }
                }
                return 0;
            case 1: // up
                for (int i = currentTileY; i <= currentTileY + magnitude; i++) {
                    if (i < tileMap.mapHeight - 1) {
                        if (tileMap.getTile(currentTileX, i).isChange())
                            return tileMap.getTile(currentTileX, i).getMagOffset();
                    }
                }
                return 0;
            case 2: // right
                for (int i = currentTileX; i <= currentTileX + magnitude; i++) {
                    if (i < tileMap.mapWidth - 1) {
                        if (tileMap.getTile(i, currentTileY).isChange())
                            return tileMap.getTile(i, currentTileY).getMagOffset();
                    }
                }
                return 0;
            case 3: // left
                for (int i = currentTileX; i >= currentTileX - magnitude; i--) {
                    if (i > 0) {
                        if (tileMap.getTile(i, currentTileY).isChange())
                            return tileMap.getTile(i, currentTileY).getMagOffset();
                    }
                }
                return 0;
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

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void setMaxExp(int maxExp) {
        this.maxExp = maxExp;
    }

    public int getCurrentTileX() {
        return currentTileX;
    }

    public int getCurrentTileY() {
        return currentTileY;
    }

    public TileMap getTileMap() {
        return tileMap;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
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

    public Moveset getMoveset() { return moveset; }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getPreviousHp() { return previousHp; }

    public void setPreviousHp(int previousHp) { this.previousHp = previousHp; }

    public int getLevel() {
        return level;
    }

    public int getExp() {
        return exp;
    }

    public int getMaxExp() {
        return maxExp;
    }

    public Random getRandom() {
        return rand;
    }

    public int getMinDamage() { return minDamage; }

    public int getMaxDamage() { return maxDamage; }

    public int getAccuracy() { return accuracy; }

}

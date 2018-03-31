package com.unlucky.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.unlucky.animation.AnimationManager;
import com.unlucky.battle.Moveset;
import com.unlucky.map.Tile;
import com.unlucky.map.TileMap;
import com.unlucky.resource.ResourceManager;

/**
 * Handles entity attributes and movement on the tile map
 *
 * @author Ming Li
 */
public abstract class Entity {

    protected String id;
    protected ResourceManager rm;

    // animation
    protected AnimationManager am;
    protected boolean pauseAnim = false;
    // battle scene animation
    protected AnimationManager bam;

    // position (x,y) in map coordinates (tile * tileSize)
    protected Vector2 position;

    // map
    protected TileMap tileMap;

    /******** RPG ASPECTS *********/

    protected Moveset moveset;

    protected boolean dead = false;

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

    // shield
    protected boolean hasShield = false;
    protected int shield;
    protected int maxShield;
    protected int prevShield;

    // level up information
    protected int level;

    // move type used default -1
    protected int prevMoveUsed = -1;
    protected int moveUsed = -1;

    public Entity(String id, ResourceManager rm) {
        this.id = id;
        this.rm = rm;

        position = new Vector2();
    }

    public Entity(String id, Vector2 position, TileMap tileMap, ResourceManager rm) {
        this(id, rm);
        this.position = position;
        this.tileMap = tileMap;
    }

    public void update(float dt) {
        // handle RPG elements
        if (hp > maxHp) hp = maxHp;
        if (hp <= 0) {
            hp = 0;
        }

        // animation
        if (!pauseAnim) am.update(dt);
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
     * Adds a shield to the entity's health bar
     *
     * @param mshield
     */
    public void setShield(int mshield) {
        hasShield = true;
        this.shield = this.maxShield = this.prevShield = mshield;
    }

    /**
     * Resets the player's shield to 0
     */
    public void resetShield() {
        hasShield = false;
        this.shield = this.maxShield = this.prevShield = 0;
    }

    /**
     * Applies the damage done by the previous move
     */
    public void applyDamage() {
        // with a shield the damage is applied to the shield first
        if (hasShield) {
            if (damage == 0) {
                hasShield = false;
                return;
            }
            prevShield = shield;
            moveUsed = prevMoveUsed;
            prevMoveUsed = -1;

            // if the damage breaks through the shield then the player's hp bar is damaged
            if (shield - damage < 0) {
                damage = Math.abs(shield - damage);
                shield = 0;
                damageHp();
            }
            // damage breaks through the entire shield but does not damage hp bar
            else if (shield - damage == 0) {
                shield = 0;
            }
            // damage damages the shield
            else {
                shield -= damage;
                damage = 0;
            }

        }
        else {
            moveUsed = prevMoveUsed;
            prevMoveUsed = -1;
            damageHp();
        }
    }

    private void damageHp() {
        previousHp = hp;
        hp -= damage;
        damage = 0;
        if (hp <= 0) {
            hp = 0;
            dead = true;
        }
    }

    public void applyHeal() {
        previousHp = hp;
        moveUsed = prevMoveUsed;
        prevMoveUsed = -1;
        hp += healing;
        healing = 0;
        if (hp > maxHp) hp = maxHp;
    }

    public void setMap(TileMap map) {
        this.tileMap = map;
        this.position.set(map.toMapCoords(map.playerSpawn));
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public TileMap getTileMap() {
        return tileMap;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public AnimationManager getAm() { return am; }

    public AnimationManager getBam() { return bam; }

    public String getId() {
        return id;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Moveset getMoveset() { return moveset; }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getPreviousHp() { return previousHp; }

    public void setPreviousHp(int previousHp) { this.previousHp = previousHp; }

    public void setLevel(int level) { this.level = level; }

    public int getLevel() {
        return level;
    }

    public int getMinDamage() { return minDamage; }

    public int getMaxDamage() { return maxDamage; }

    public void setMinDamage(int minDamage) {
        this.minDamage = minDamage;
    }

    public void setMaxDamage(int maxDamage) {
        this.maxDamage = maxDamage;
    }

    public int getAccuracy() { return accuracy; }

    public boolean isDead() { return dead; }

    public void setDead(boolean dead) { this.dead = dead; }

    public int getMoveUsed() { return moveUsed; }

    public void useMove(int move) {
        this.prevMoveUsed = move;
    }

    public int getPrevMoveUsed() { return prevMoveUsed; }

    public void setMoveUsed(int moveUsed) {
        this.moveUsed = moveUsed;
    }

    public void setPrevMoveUsed(int prevMoveUsed) { this.prevMoveUsed = prevMoveUsed; }

    /**
     * Returns the tile the Entity is currently standing on
     * @return
     */
    public Tile getCurrentTile() {
        return tileMap.getTile(tileMap.toTileCoords(position));
    }

    public boolean isHasShield() {
        return hasShield;
    }

    public void setHasShield(boolean hasShield) {
        this.hasShield = hasShield;
    }

    public int getShield() {
        return shield;
    }

    public int getMaxShield() {
        return maxShield;
    }

    public void setMaxShield(int maxShield) {
        this.maxShield = maxShield;
    }

    public int getPrevShield() {
        return prevShield;
    }

    public void setPrevShield(int prevShield) {
        this.prevShield = prevShield;
    }

    /**
     * Returns if the entity's hp is below or equal to a certain threshold
     *
     * @param percentage
     * @return
     */
    public boolean healthBelow(int percentage) {
        return hp <= (int) ((percentage / 100f) * (float) maxHp);
    }

}
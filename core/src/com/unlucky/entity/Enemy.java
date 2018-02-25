package com.unlucky.entity;

import com.badlogic.gdx.math.Vector2;
import com.unlucky.animation.AnimationManager;
import com.unlucky.battle.Moveset;
import com.unlucky.battle.StatusSet;
import com.unlucky.map.TileMap;
import com.unlucky.resource.ResourceManager;
import com.unlucky.resource.Util;

/**
 * An Entity that the Player can battle if encountered
 * A Tile will hold an Enemy
 * An Enemy will not be able to move
 *
 * @author Ming Li
 */
public class Enemy extends Entity {

    // 5% chance that an enemy will be a "elite" version with increased hp and damage
    protected boolean isElite = false;
    // for boss version of an enemy
    protected boolean isBoss = false;
    protected int bossIndex = -1;

    // amount of gold an Enemy will drop
    protected int goldDrop;

    // battle status effects
    public StatusSet statusEffects;

    public Enemy(String id, Vector2 position, TileMap tileMap, ResourceManager rm) {
        super(id, position, tileMap, rm);
        speed = 0;
        moveset = new Moveset(rm);
        statusEffects = new StatusSet(false, rm);
    }

    /**
     * Creates a specific enemy with animation
     *
     * @param id name of enemy
     * @param position spawn position on map
     * @param tileMap
     * @param rm
     * @param numFrames
     * @param animIndex
     * @param delay
     */
    public Enemy(String id, Vector2 position, TileMap tileMap, ResourceManager rm,
                 int numFrames, int animIndex, float delay)
    {
        this(id, position, tileMap, rm);
        // create tilemap animation
        am = new AnimationManager(rm.sprites16x16, numFrames, animIndex, delay);
        // create battle scene animation
        bam = new AnimationManager(rm.battleSprites96x96, 2, animIndex, delay);

        // determine if boss
        isElite = Util.isSuccess(Util.ELITE_CHANCE);
        if (isElite) this.id = "[ELITE] " + id;

        statusEffects = new StatusSet(false, rm);
    }

    /**
     * Creates a boss
     *
     * @param id boss name
     * @param bossIndex unique boss id to get a boss's moveset
     * @param position
     * @param tileMap
     * @param rm
     * @param numFrames
     * @param animIndex
     * @param delay
     */
    public Enemy(String id, int bossIndex, Vector2 position, TileMap tileMap, ResourceManager rm,
                 int numFrames, int animIndex, float delay)
    {
        this(id, position, tileMap, rm);
        am = new AnimationManager(rm.sprites16x16, numFrames, animIndex, delay);
        bam = new AnimationManager(rm.battleSprites96x96, 2, animIndex, delay);
        isBoss = true;
        this.bossIndex = bossIndex;

        statusEffects = new StatusSet(false, rm);
    }

    public int getGoldDrop() {
        return goldDrop;
    }

    public boolean isElite() { return isElite; }

    public boolean isBoss() { return isBoss; }

    public int getBossIndex() { return bossIndex; }

    public void setMaxHp(int maxHp) {
        this.maxHp = this.hp = this.previousHp = maxHp;
    }

    public void setMinDamage(int minDamage) {
        this.minDamage = minDamage;
    }

    public void setMaxDamage(int maxDamage) {
        this.maxDamage = maxDamage;
    }

}

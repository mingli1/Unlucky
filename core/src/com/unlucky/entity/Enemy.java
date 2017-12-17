package com.unlucky.entity;

import com.badlogic.gdx.math.Vector2;
import com.unlucky.animation.AnimationManager;
import com.unlucky.map.TileMap;
import com.unlucky.resource.ResourceManager;

/**
 * An Entity that the Player can battle if encountered
 * A Tile will hold an Enemy
 * An Enemy will not be able to move
 *
 * @author Ming Li
 */
public class Enemy extends Entity {

    // 5% chance that an enemy will be a "boss" version with increased hp and damage
    protected boolean isBoss = false;

    // amount of exp an Enemy will give when defeated
    protected int expDrop;
    // amount of gold an Enemy will drop
    protected int goldDrop;

    public Enemy(String id, Vector2 position, TileMap tileMap, ResourceManager rm) {
        super(id, position, tileMap, rm);
        speed = 0;
    }

    /**
     * Creates a specific enemy with animation
     *
     * @param id
     * @param position
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
        am = new AnimationManager(rm.sprites16x16, numFrames, animIndex, delay);

        // determine if boss
        int k = rand.nextInt(100);
        isBoss = k < 5 ? true : false;
        this.id = "[BOSS] " + id;
    }

    public int getExpDrop() {
        return expDrop;
    }

    public int getGoldDrop() {
        return goldDrop;
    }

    public boolean isBoss() { return isBoss; }

    public void setMaxHp(int maxHp) {
        this.maxHp = this.hp = maxHp;
    }

    public void setMinDamage(int minDamage) {
        this.minDamage = minDamage;
    }

    public void setMaxDamage(int maxDamage) {
        this.maxDamage = maxDamage;
    }

}

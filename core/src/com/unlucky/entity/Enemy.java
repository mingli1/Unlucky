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
        super(id, position, tileMap, rm);
        speed = 0;
        am = new AnimationManager(rm.sprites16x16, numFrames, animIndex, delay);
    }

    public int getExpDrop() {
        return expDrop;
    }

    public int getGoldDrop() {
        return goldDrop;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public void setMinDamage(int minDamage) {
        this.minDamage = minDamage;
    }

    public void setMaxDamage(int maxDamage) {
        this.maxDamage = maxDamage;
    }

}

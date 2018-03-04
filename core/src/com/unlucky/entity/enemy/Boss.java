package com.unlucky.entity.enemy;

import com.badlogic.gdx.math.Vector2;
import com.unlucky.animation.AnimationManager;
import com.unlucky.map.TileMap;
import com.unlucky.resource.ResourceManager;

/**
 * A boss enemy with special attributes
 *
 * @author Ming Li
 */
public class Boss extends Enemy {

    // the unique identifier for bosses
    public int bossId;

    public Boss(String id, Vector2 position, TileMap tileMap, ResourceManager rm) {
        super(id, position, tileMap, rm);
    }

    public Boss(String id, int bossId, Vector2 position, TileMap tileMap, ResourceManager rm,
                int numFrames, int animIndex, float delay) {
        this(id, position, tileMap, rm);
        this.bossId = bossId;

        // create tilemap animation
        am = new AnimationManager(rm.sprites16x16, numFrames, animIndex, delay);
        // create battle scene animation
        bam = new AnimationManager(rm.battleSprites96x96, 2, animIndex, delay);
    }

    @Override
    public boolean isElite() {
        return false;
    }

    @Override
    public boolean isBoss() {
        return true;
    }

}

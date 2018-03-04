package com.unlucky.entity.enemy;

import com.badlogic.gdx.math.Vector2;
import com.unlucky.animation.AnimationManager;
import com.unlucky.map.TileMap;
import com.unlucky.resource.ResourceManager;
import com.unlucky.resource.Util;

/**
 * A normal enemy that can either be an elite or regular version
 *
 * @author Ming Li
 */
public class Normal extends Enemy {

    // chance for enemy to be elite which has higher stats than normal
    public boolean isElite = false;

    public Normal(String id, Vector2 position, TileMap tileMap, ResourceManager rm) {
        super(id, position, tileMap, rm);
    }

    public Normal(String id, Vector2 position, TileMap tileMap, ResourceManager rm,
                  int numFrames, int animIndex, float delay) {
        this(id, position, tileMap, rm);

        // create tilemap animation
        am = new AnimationManager(rm.sprites16x16, numFrames, animIndex, delay);
        // create battle scene animation
        bam = new AnimationManager(rm.battleSprites96x96, 2, animIndex, delay);

        // determine if elite
        isElite = Util.isSuccess(Util.ELITE_CHANCE);
        if (isElite) this.id = "[ELITE] " + id;
    }

    @Override
    public boolean isElite() {
        return isElite;
    }

    @Override
    public boolean isBoss() {
        return false;
    }

}

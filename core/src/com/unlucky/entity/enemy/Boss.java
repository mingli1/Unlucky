package com.unlucky.entity.enemy;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.unlucky.animation.AnimationManager;
import com.unlucky.map.TileMap;
import com.unlucky.resource.ResourceManager;
import com.unlucky.resource.Util;

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
                int worldIndex, int startIndex, int numFrames, float delay) {
        this(id, position, tileMap, rm);
        this.bossId = bossId;

        // create tilemap animation
        am = new AnimationManager(rm.sprites16x16, worldIndex, startIndex, numFrames, delay);
        // create battle scene animation
        bam = new AnimationManager(rm.battleSprites96x96, worldIndex, startIndex, 2, delay);
    }

    @Override
    public boolean isElite() {
        return false;
    }

    @Override
    public boolean isBoss() {
        return true;
    }

    @Override
    /**
     * Sets the stats of the boss based on level and the boss index
     */
    public void setStats() {
        int mhp = 0;
        int minDmg = 0;
        int maxDmg = 0;

        switch (bossId) {
            case 0: // king slime
                // has lower hp because its passive compensates for it
                int mhpSeed0 = (int) (Math.pow(level, 2.05) + 10);
                mhp = Util.getDeviatedRandomValue(mhpSeed0, 1);
                minDmg = MathUtils.random(4, 8);
                maxDmg = MathUtils.random(9, 13);
                for (int i = 0; i < level - 1; i++) {
                    minDmg += MathUtils.random(1, 3) - MathUtils.random(1);
                    maxDmg += MathUtils.random(1, 3) + MathUtils.random(1);
                }
                break;
            case 1: // red reaper
                int mhpSeed1 = (int) (Math.pow(level, 2) + 14);
                mhp = Util.getDeviatedRandomValue(mhpSeed1, 3);
                minDmg = MathUtils.random(3, 8);
                maxDmg = MathUtils.random(9, 15);
                for (int i = 0; i < level - 1; i++) {
                    minDmg += MathUtils.random(1, 2) - MathUtils.random(2);
                    maxDmg += MathUtils.random(1, 2) + MathUtils.random(2);
                }
                break;
        }

        this.setMaxHp(mhp);
        this.setMinDamage(minDmg);
        this.setMaxDamage(maxDmg);
        this.setAccuracy(MathUtils.random(Util.ENEMY_MIN_ACCURACY, Util.ENEMY_MAX_ACCURACY));
    }

    /**
     * Returns a description of a boss's passive based on bossId
     *
     * @return
     */
    public String getPassiveDescription() {
        switch (bossId) {
            // king slime
            case 0: return "Slime Revival (Respawns after death with half health points up to 4 times).";
            // red reaper
            case 1: return "Phantom Presence (Causes the player's accuracy to be decreased by 30% for all attacks).";
        }
        return "";
    }

}

package com.unlucky.entity.enemy;

import com.badlogic.gdx.math.MathUtils;
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

    @Override
    public void setStats() {
        // if the enemy is an elite then its stats are multiplied by an elite multiplier
        float eliteMultiplier = MathUtils.random(Util.MIN_ELITE_MULTIPLIER, Util.MAX_ELITE_MULTIPLIER);

        int mhp = MathUtils.random(Util.ENEMY_INIT_MIN_MHP, Util.ENEMY_INIT_MAX_MHP);
        int minDmg = MathUtils.random(Util.ENEMY_INIT_MIN_MINDMG, Util.ENEMY_INIT_MAX_MINDMG);
        int maxDmg = MathUtils.random(Util.ENEMY_INIT_MIN_MAXDMG, Util.ENEMY_INIT_MAX_MAXDMG);

        for (int i = 0; i < this.level - 1; i++) {
            mhp += MathUtils.random(Util.ENEMY_MIN_HP_INCREASE, Util.ENEMY_MAX_HP_INCREASE);

            int dmgMean = MathUtils.random(Util.ENEMY_MIN_DMG_INCREASE, Util.ENEMY_MAX_DMG_INCREASE);
            int minDmgIncrease = (dmgMean - MathUtils.random(2));
            int maxDmgIncrease = (dmgMean + MathUtils.random(2));

            minDmg += minDmgIncrease;
            maxDmg += maxDmgIncrease;
        }

        // sets a random accuracy initially
        this.setAccuracy(MathUtils.random(Util.ENEMY_MIN_ACCURACY, Util.ENEMY_MAX_ACCURACY));

        // finalize stats
        this.setMaxHp(isElite ? (int) (eliteMultiplier * mhp) : mhp);
        this.setMinDamage(isElite ? (int) (eliteMultiplier * minDmg) : minDmg);
        this.setMaxDamage(isElite ? (int) (eliteMultiplier * maxDmg) : maxDmg);
    }

}

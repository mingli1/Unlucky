package com.unlucky.resource;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.unlucky.battle.SpecialMove;
import com.unlucky.entity.enemy.Boss;
import com.unlucky.entity.Entity;
import com.unlucky.entity.enemy.Normal;
import com.unlucky.map.TileMap;

/**
 * Stores useful constants and functions
 *
 * @author Ming Li
 */
public class Util {

    // rates
    public static final float TEXT_SPEED = 0.03f;
    public static final float HP_BAR_DECAY_RATE = 50.f;
    public static final float TRANSITION_SCREEN_SPEED = 300.f;

    public static final int RAINDROP_X = 50;
    public static final int RAINDROP_Y_DEVIATED = 20;
    public static final int SNOWFLAKE_X = 30;
    public static final int SNOWFLAKE_Y_DEVIATED = 30;

    // Animation indexes
    public static final int PLAYER_WALKING = 0;

    // Animation delay
    public static final float PLAYER_WALKING_DELAY = 1 / 6f;

    // Directional pad button positions
    public static final int DIR_PAD_SIZE = 40;
    public static final int DIR_PAD_OFFSET = 10;

    // Special move indexes
    public static final int S_MOVE_CD = 3; // num of turns cooldown for special moves
    public static final int NUM_SPECIAL_MOVES = 8;
    public static final int DISTRACT = 0;
    public static final int FOCUS = 1;
    public static final int INTIMIDATE = 2;
    public static final int REFLECT = 3;
    public static final int STUN = 4;
    public static final int INVERT = 5;
    public static final int SACRIFICE = 6;
    public static final int SHIELD = 7;

    public static final SpecialMove S_DISTRACT = new SpecialMove(DISTRACT,
            "Distract", "Next enemy attack\n-" + Util.P_DISTRACT + "% ACC");
    public static final SpecialMove S_FOCUS = new SpecialMove(FOCUS,
            "Focus", "Next attack 100% ACC\nand +" + Util.P_FOCUS_CRIT + "% crit chance");
    public static final SpecialMove S_INTIMIDATE = new SpecialMove(INTIMIDATE,
            "Intimidate", "Next attack is\namplified by " + Util.P_INTIMIDATE + "%");
    public static final SpecialMove S_REFLECT = new SpecialMove(REFLECT,
            "Reflect", "Next enemy attack\nis reflected back");
    public static final SpecialMove S_STUN = new SpecialMove(STUN,
            "Stun", Util.P_STUN + "% chance to\nstun enemy");
    public static final SpecialMove S_INVERT = new SpecialMove(INVERT,
            "Invert", "Heal moves damage\nDamage moves heal");
    public static final SpecialMove S_SACRIFICE = new SpecialMove(SACRIFICE,
            "Sacrifice", "Sacrifice all but 1 hp\nfor increased dmg");
    public static final SpecialMove S_SHIELD = new SpecialMove(SHIELD,
            "Shield", "Summon a shield that\nabsorbs " + Util.P_SHIELD + "% of max hp");

    // Button dimensions
    public static final int MOVE_WIDTH = 145;
    public static final int MOVE_HEIGHT = 50;

    // Probabilities, Percentages, and Multipliers
    public static final int RUN_FROM_BATTLE = 7;
    public static final int SAVED_FROM_BATTLE = 1;
    public static final int ELITE_CHANCE = 5;
    public static final int PLAYER_ACCURACY = 80;
    public static final int ENEMY_MIN_ACCURACY = 75;
    public static final int ENEMY_MAX_ACCURACY = 95;
    public static final int REVIVAL = 1;
    public static final int ENCHANT = 50;
    public static final int DESTROY_ITEM_IF_FAIL = 50;
    public static final int TILE_INTERATION = 70;

    // Battle special moves percentages
    public static final int P_DISTRACT = 50;
    public static final int P_INTIMIDATE = 40;
    public static final float INTIMIDATE_MULT = 1.4f;
    public static final int P_FOCUS_CRIT = 30;
    public static final int P_STUN = 60;
    public static final int P_SHIELD = 20;

    public static final int CRIT_MULTIPLIER = 3;
    public static final float MIN_ELITE_MULTIPLIER = 1.3f;
    public static final float MAX_ELITE_MULTIPLIER = 1.6f;

    public static final int NORMAL_ITEM_DROP = 40;
    public static final int ELITE_ITEM_DROP = 60;
    public static final int BOSS_ITEM_DROP = 80;
    public static final int COMMON_ITEM_RNG_INDEX = 60;
    public static final int RARE_ITEM_RNG_INDEX = 90;
    public static final int EPIC_ITEM_RNG_INDEX = 99;
    public static final int LEGENDARY_ITEM_RNG_INDEX = 100;

    public static final float COMMON_ENCHANT_MIN = 1.f;
    public static final float COMMON_ENCHANT_MAX = 1.2f;
    public static final float RARE_ENCHANT_MIN = 1.1f;
    public static final float RARE_ENCHANT_MAX = 1.3f;
    public static final float EPIC_ENCHANT_MIN = 1.2f;
    public static final float EPIC_ENCHANT_MAX = 1.4f;
    public static final float LEGENDARY_ENCHANT_MIN = 1.3f;
    public static final float LEGENDARY_ENCHANT_MAX = 1.6f;

    // Level up scaling
    public static final int PLAYER_INIT_MAX_HP = 65;
    public static final int PLAYER_INIT_MIN_DMG = 12;
    public static final int PLAYER_INIT_MAX_DMG = 18;
    public static final int PLAYER_MIN_HP_INCREASE = 7;
    public static final int PLAYER_MAX_HP_INCREASE = 15;
    public static final int PLAYER_MIN_DMG_INCREASE = 3;
    public static final int PLAYER_MAX_DMG_INCREASE = 5;

    public static final int ENEMY_INIT_MIN_MHP = 24;
    public static final int ENEMY_INIT_MAX_MHP = 36;
    public static final int ENEMY_INIT_MIN_MINDMG = 3;
    public static final int ENEMY_INIT_MAX_MINDMG = 5;
    public static final int ENEMY_INIT_MIN_MAXDMG = 6;
    public static final int ENEMY_INIT_MAX_MAXDMG = 9;

    //public static final int ENEMY_MIN_HP_INCREASE = 5;
    public static final int ENEMY_MIN_HP_INCREASE = 10;
    //public static final int ENEMY_MAX_HP_INCREASE = 13;
    public static final int ENEMY_MAX_HP_INCREASE = 26;
    public static final int ENEMY_MIN_DMG_INCREASE = 2;
    public static final int ENEMY_MAX_DMG_INCREASE = 5;

    // Experience

    /**
     * Current max exp formula is:
     * EXP = 2 * level * (1.25 ^ (level / 3)) + offset
     * Slow exponential growth
     *
     * @param level the level of the player
     * @param offset the initial max exp at level 1 acting as an offset to the curve (random)
     * @return max experience at a given level
     */
    public static int calculateMaxExp(int level, int offset) {
        return (int) (2 * level * (Math.pow(1.25, level / 3)) + offset);
    }

    /**
     * The exp given to the player after it's defeated
     * EXP = (enemyLevel ^ 1.1) + offset
     *
     * @param enemyLevel
     * @param offset
     * @return
     */
    public static int calculateExpEarned(int enemyLevel, int offset) {
        return (int) (Math.pow(enemyLevel, 1.1)) + offset;
    }

    // Random helper functions

    /**
     * Returns if an event was successful given a probability
     *
     * @param p
     * @return
     */
    public static boolean isSuccess(int p) {
        int k = MathUtils.random(99);
        return k < p;
    }

    /**
     * Returns a random number in a range produced by a mean and standard deviation
     *
     * @param mu avg
     * @param sigma deviation
     * @return
     */
    public static int getDeviatedRandomValue(int mu, int sigma) {
        int n0 = mu - sigma;
        int n1 = mu + sigma;
        return MathUtils.random(n0, n1);
    }

    // Map

    // all blocked tile ids
    public static final int[] BLOCKED_TILE_IDS = {
        5, 6, 7, 9, 10, 11, 12, 13, 14, 15, 21, 22, 23, 25, 26, 27, 28, 29, 30, 31,
        38, 41, 42, 48, 49, 50, 51, 54, 55, 56, 57, 64, 65, 66, 67, 96, 109
    };

    public static boolean isBlockedTile(int id) {
        for (int i = 0; i < BLOCKED_TILE_IDS.length; i++) {
            if (id == BLOCKED_TILE_IDS[i]) return true;
        }
        return false;
    }

    /**
     * Returns an instance of an Entity based on numerical Entity id
     *
     * @param id
     * @param position
     * @param map
     * @param rm
     * @return
     */
    public static Entity getEntity(int id, Vector2 position, TileMap map, ResourceManager rm) {
        switch (id) {
            case 2: return new Normal("slime", position, map, rm, 2, 1, 1 / 3f);
            case 3: return new Boss("king slime", 0, position, map, rm, 2, 2, 1 / 3f);
            case 4: return new Boss("logan paul", 1, position, map, rm, 2, 3, 1 / 3f);
        }
        return null;
    }

}

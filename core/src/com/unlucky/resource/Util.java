package com.unlucky.resource;

import com.badlogic.gdx.math.Vector2;
import com.unlucky.entity.Enemy;
import com.unlucky.entity.Entity;
import com.unlucky.map.TileMap;

import java.util.Random;

/**
 * Stores useful constants and functions
 *
 * @author Ming Li
 */
public class Util {

    // rates
    public static final float TEXT_SPEED = 0.02f;
    public static final int HP_BAR_DECAY_RATE = 1;
    public static final int TRANSITION_SCREEN_SPEED = 5;

    // Animation indexes
    public static final int PLAYER_WALKING = 0;

    // Animation delay
    public static final float PLAYER_WALKING_DELAY = 1 / 6f;

    // Directional pad button positions
    public static final int DIR_PAD_SIZE = 40;
    public static final int DIR_PAD_OFFSET = 10;

    // Button dimensions
    public static final int MOVE_WIDTH = 145;
    public static final int MOVE_HEIGHT = 50;

    public static final int STD_MED_WIDTH = 110;
    public static final int STD_MED_HEIGHT = 50;

    // Probabilities, Percentages, and Multipliers
    public static final int RUN_FROM_BATTLE = 7;
    public static final int SAVED_FROM_BATTLE = 1;
    public static final int ELITE_CHANCE = 5;
    public static final int PLAYER_ACCURACY = 85;
    public static final int ENEMY_ACCURACY = 95;
    public static final int P_DISTRACT = 45;
    public static final int P_INTIMIDATE = 25;
    public static final float INTIMIDATE_MULT = 1.25f;
    public static final int REVIVAL = 1;
    public static final int ENCHANT = 40;
    public static final int DESTROY_ITEM_IF_FAIL = 60;

    public static final int CRIT_MULTIPLIER = 3;
    public static final float MIN_ELITE_MULTIPLIER = 1.3f;
    public static final float MAX_ELITE_MULTIPLIER = 1.8f;
    public static final int MIN_ENEMY_HP_SCALING = 2;
    public static final int MAX_ENEMY_HP_SCALING = 4;
    public static final int MIN_ENEMY_DMG_SCALING = 8;
    public static final int MAX_ENEMY_DMG_SCALING = 12;

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
    public static final int PLAYER_INIT_MAX_HP = 85;
    public static final int PLAYER_INIT_MIN_DMG = 12;
    public static final int PLAYER_INIT_MAX_DMG = 18;
    public static final int PLAYER_MIN_HP_INCREASE = 11;
    public static final int PLAYER_MAX_HP_INCREASE = 19;
    public static final int PLAYER_MIN_DMG_INCREASE = 13;
    public static final int PLAYER_MAX_DMG_INCREASE = 17;

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

    // Random

    /**
     * Returns whether or not an event is successful given a probability in %
     *
     * @param p
     * @param rand
     * @return
     */
    public static boolean isSuccess(int p, Random rand) {
        int k = rand.nextInt(100);
        return k < p;
    }

    /**
     * Returns a random number within in a range
     *
     * @param n0 min
     * @param n1 max
     * @param rand
     * @return
     */
    public static int getRandomValue(int n0, int n1, Random rand) {
        return rand.nextInt((n1 - n0) + 1) + n0;
    }

    /**
     * Returns a random number in a range produced by a mean and standard deviation
     *
     * @param mu avg
     * @param sigma deviation
     * @param rand
     * @return
     */
    public static int getDeviatedRandomValue(int mu, int sigma, Random rand) {
        int n0 = mu - sigma;
        int n1 = mu + sigma;
        return rand.nextInt((n1 - n0) + 1) + n0;
    }

    // Map

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
            case 2: return new Enemy("slime", position, map, rm, 2, 1, 1 / 3f);
            case 3: return new Enemy("king slime", 0, position, map, rm, 2, 2, 1 / 3f);
        }
        return null;
    }

}

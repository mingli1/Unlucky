package com.unlucky.resource;

import com.badlogic.gdx.math.Vector2;
import com.unlucky.entity.Enemy;
import com.unlucky.entity.Entity;
import com.unlucky.entity.monsters.Slime;
import com.unlucky.map.TileMap;

import java.util.Random;

/**
 * Stores useful constants and functions
 *
 * @author Ming Li
 */
public class Util {

    // rates
    public static final float TEXT_SPEED = 0.03f;

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

    // Random

    /**
     * Returns whether or not an event is successful given a probability in %
     *
     * @param p
     * @param rand
     * @return
     */
    public static boolean isSuccess(int p, Random rand) {
        int k = rand.nextInt(101);
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
            case 2: return new Enemy("slime", position, map, rm, 2, 2, 1 / 3f);
        }
        return null;
    }

}

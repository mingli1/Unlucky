package com.unlucky.resource;

import java.util.Random;

/**
 * Stores useful constants and functions
 *
 * @author Ming Li
 */
public class Util {

    // Animation indexes
    public static final int PLAYER_WALKING = 0;

    // Animation delays
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
        return k <= p;
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

}

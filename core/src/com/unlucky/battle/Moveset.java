package com.unlucky.battle;

import com.badlogic.gdx.utils.Array;
import com.unlucky.resource.ResourceManager;

import java.util.Random;

/**
 * A Moveset is a set of 4 random moves that an Entity gets each battle
 *
 * @author Ming Li
 */
public class Moveset {

    private Random rand;
    private ResourceManager rm;

    /**
     * Index:
     * 0 - accurate
     * 1 - wide
     * 2 - crit
     * 3 - heal
     */
    public Move[] moveset;
    public String[] names;
    public String[] descriptions;

    public Moveset(ResourceManager rm) {
        this.rm = rm;
        rand = new Random();

        moveset = new Move[4];
        names = new String[4];
        descriptions = new String[4];
    }

    /**
     * Resets a Moveset with a set of 4 new random Moves
     */
    public void reset(int min, int max, int hp) {
        moveset = getRandomMoves();
        int dmg = 0;

        for (int i = 0; i < 4; i++) {
            // reset damage seed for a new value between player's dmg range each iteration
            dmg = rand.nextInt((max - min) + 1) + min;
            if (moveset[i].type == 3) moveset[i].setHeal(hp);
            else moveset[i].setDamage(dmg);

            names[i] = moveset[i].name;
            // Concatenates move info into a full description
            if (moveset[i].type < 2) {
                descriptions[i] = "Damage: " + Math.round(moveset[i].minDamage)
                        + "-" + Math.round(moveset[i].maxDamage);
            } else if (moveset[i].type == 2) {
                descriptions[i] = "Damage: " + Math.round(moveset[i].minDamage) + " + "
                        + moveset[i].crit + "% to crit";
            } else {
                descriptions[i] = "Heals: " + Math.round(moveset[i].minHeal)
                        + "-" + Math.round(moveset[i].maxHeal);
            }
        }
    }

    /**
     * Returns a Move array  with 4 unique moves chosen from all possible Moves
     *
     * @return
     */
    private Move[] getRandomMoves() {
        Array<Move> all = new Array<Move>();
        all.addAll(rm.accurateMoves);
        all.addAll(rm.wideMoves);
        all.addAll(rm.critMoves);
        all.addAll(rm.healMoves);

        Move[] ret = new Move[4];

        int index;
        for (int i = 0; i < ret.length; i++) {
            index = rand.nextInt(all.size);
            ret[i] = all.get(index);
            all.removeIndex(index);
        }

        return ret;
    }

    public Move getAccurateMove() {
        return moveset[0];
    }

    public Move getWideMove() {
        return moveset[1];
    }

    public Move getCritMove() {
        return moveset[2];
    }

    public Move getHealMove() {
        return moveset[3];
    }

}

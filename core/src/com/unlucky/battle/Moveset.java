package com.unlucky.battle;

/**
 * A Moveset is a set of 4 random moves that an Entity gets each battle
 * This class also draws the moves and their descriptions
 *
 * @author Ming Li
 */
public class Moveset {

    private final Move[] ACCURATE_MOVES = {
            new Move(0, "Electrocute", "Fires a ball of electricity that electrocutes the enemy", 3, 5),
            new Move(0, "Shock", "A small lightning bolt comes down and briefly shocks the enemy", 1, 2),
    };

}

package com.unlucky.battle;

/**
 * A special move enhances the player, empowers regular moves, or debuffs the enemy
 *
 * @author Ming Li
 */
public class SpecialMove {

    public int id;
    // information displayed on button
    public String name;
    public String desc;

    public SpecialMove(int id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

}

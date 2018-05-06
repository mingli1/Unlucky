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
    // level that the player has to be to unlock this smove
    public int levelUnlocked;

    public SpecialMove(int id, String name, String desc, int levelUnlocked) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.levelUnlocked = levelUnlocked;
    }

}

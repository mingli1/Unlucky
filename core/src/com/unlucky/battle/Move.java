package com.unlucky.battle;

/**
 * A Move is an attack or ability that can deal damage or other effects to Entities.
 *
 * @author Ming Li
 */
public class Move {

    // basic descriptors
    public String name;
    public String description;

    // Damage range of a Move
    public int minDamage;
    public int maxDamage;

    // Damage seed is an Entity's average damage used to scale a Move's damage range
    public int damageSeed;

    public Move(String name, String description, int minDamage, int maxDamage) {
        this.name = name;
        this.description = description;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
    }

}

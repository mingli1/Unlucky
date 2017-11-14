package com.unlucky.battle;

/**
 * A Move is an attack or ability that can deal damage or other effects to Entities.
 *
 * There are 4 types of moves separated into 4 categories:
 * - Accurate: close damage range with little standard deviation for most consistent damage
 * - Wide: high risk high reward type; very large damage range
 * - Crit: fixed damage but has chance for critical strike that does massive damage
 * - Healing: heals the Entity based on set range
 *
 * In terms of maximum damage: Crit > Wide > Accurate
 * In terms of damage over time: Accurate > Wide > Crit
 * In terms of least possible damage: Wide > Crit > Accurate
 *
 * @author Ming Li
 */
public class Move {

    // basic descriptors
    public String name;

    /*
    0 - Accurate
    1 - Wide
    2 - Crit
    3 - Healing
     */
    public int type;

    // Damage range of a Move
    public float minDamage;
    public float maxDamage;

    // Healing of a Move
    public float minHeal;
    public float maxHeal;

    // Crit chance in %
    public int crit;

    /**
     * Constructor for Accurate, Wide, and Healing types
     *
     * @param type
     * @param name
     * @param min
     * @param max
     */
    public Move(int type, String name, float min, float max) {
        this.type = type;
        this.name = name;

        if (type == 0 || type == 1) {
            this.minDamage = min;
            this.maxDamage = max;
            minHeal = maxHeal = crit = -1;
        }
        else {
            this.minHeal = min;
            this.maxHeal = max;
            minDamage = maxDamage = crit = -1;
        }
    }

    /**
     * Constructor for Crit type
     *
     * @param name
     * @param damage CANNOT BE 0 OR 1
     * @param crit
     */
    public Move(String name, float damage, int crit) {
        type = 2;
        this.name = name;

        minDamage = maxDamage = damage;
        minHeal = maxHeal = -1;
        this.crit = crit;
    }

    /**
     * Somewhat scaling formula for calculating the true damage range based on an Entity's range
     *
     * @param damageSeed is the "average" damage of an Entity calculated from its range
     */
    public void setDamage(float damageSeed) {
        if (type == 3) return;

        // For accurate damage, the min and max Move damage will deviate little from the mean
        if (type == 0) {
            minDamage = damageSeed - (minDamage * (damageSeed / 24));
            maxDamage = damageSeed + (maxDamage * (damageSeed / 24));
        }
        // Wide damage has large deviation from the mean
        else if (type == 1) {
            minDamage = damageSeed - (minDamage * (damageSeed / 2));
            maxDamage = damageSeed + (maxDamage * (damageSeed / 12));
        }
        // Crit damage has fixed damage that is less than the mean
        else if (type == 2) {
            minDamage = maxDamage = damageSeed - (damageSeed / minDamage);
        }
    }

    /**
     * Some strange formula for scaling hp
     *
     * @param hpSeed max hp of the Entity
     */
    public void setHeal(int hpSeed) {
        if (type != 3) return;
        minHeal = (hpSeed / 16) * minHeal;
        maxHeal = (hpSeed / 16) * maxHeal;
    }

}

package com.unlucky.save;

import com.unlucky.resource.Statistics;

/**
 * Provides a serializable object that represents the data of the player
 * that is considered important for save files.
 *
 * @author Ming Li
 */
public class PlayerAccessor  {

    // status fields
    public int hp;
    public int maxHp;
    public int level;
    public int exp;
    public int maxExp;
    public int gold;
    public int minDamage;
    public int maxDamage;
    public int accuracy;
    public int smoveCd;

    // inventory and equips consist of ItemAccessors to reduce unnecessary fields
    public ItemAccessor[] inventory;
    public ItemAccessor[] equips;

    // smoveset is simply an array of integers representing smove ids
    public int[] smoveset;

    // statistics
    public Statistics stats;

}

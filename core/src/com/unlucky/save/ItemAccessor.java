package com.unlucky.save;

/**
 * Provides a serializable object that represents the data of items
 * that are considered important for save files.
 *
 * @author Ming Li
 */
public class ItemAccessor {

    // descriptors
    public String name;
    public String labelName;
    public String desc;

    // the composite key for the image actor
    public int type;
    public int imgIndex;

    // stats
    public int rarity;
    public int hp;
    public int mhp;
    public int dmg;
    public int acc;
    public int sell;
    public int exp;
    public int enchants;
    public int enchantCost;
    public int bonusEnchantChance;
    public int eChance;

}

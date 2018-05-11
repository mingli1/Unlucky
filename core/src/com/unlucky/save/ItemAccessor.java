package com.unlucky.save;

import com.unlucky.inventory.Item;

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

    /**
     * Updates the fields of this accessor with data from the item
     * @param item
     */
    public void load(Item item) {
        this.name = item.name;
        this.desc = item.desc;
        this.labelName = item.labelName;

        this.type = item.type;
        this.imgIndex = item.imgIndex;

        this.rarity = item.rarity;
        this.hp = item.hp;
        this.mhp = item.mhp;
        this.dmg = item.dmg;
        this.acc = item.acc;
        this.sell = item.sell;
        this.exp = item.exp;
        this.enchants = item.enchants;
        this.enchantCost = item.enchantCost;
        this.bonusEnchantChance = item.bonusEnchantChance;
        this.eChance = item.eChance;
    }

}

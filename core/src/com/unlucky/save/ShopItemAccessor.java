package com.unlucky.save;

import com.unlucky.inventory.ShopItem;

/**
 * Provides a serializable object that represents the data of shop items
 * that are considered important for save files.
 *
 * @author Ming Li
 */
public class ShopItemAccessor extends ItemAccessor {

    public int price;

    public void load(ShopItem item) {
        this.name = item.name;
        this.desc = item.desc;
        this.labelName = item.labelName;

        this.index = item.index;

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
        this.price = item.price;
    }

}

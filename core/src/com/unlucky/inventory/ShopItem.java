package com.unlucky.inventory;

import com.unlucky.resource.ResourceManager;

/**
 * A special type of item that is only sold in the shop
 * and has a certain cost in gold
 *
 * @author Ming Li
 */
public class ShopItem extends Item {

    // price of the item in the shop
    public int price;

    /**
     * For potions
     * Only can be consumed for hp or sold for gold
     *
     * @param name
     * @param desc
     * @param rarity
     * @param imgIndex for textureregion in spritesheet
     * @param hp
     * @param sell
     */
    public ShopItem(ResourceManager rm, String name, String desc, int rarity,
                    int imgIndex, int hp, int exp, int sell, int price) {
        super(rm, name, desc, rarity, imgIndex, hp, exp, sell);
        this.price = price;
    }

    /**
     * For all types of equips
     * Gives increased stats and can be sold for gold
     *
     * @param name
     * @param desc
     * @param type
     * @param rarity
     * @param imgIndex
     * @param mhp
     * @param dmg
     * @param acc
     * @param sell
     */
    public ShopItem(ResourceManager rm, String name, String desc, int type, int rarity,
                    int imgIndex, int mhp, int dmg, int acc, int sell, int price) {
        super(rm, name, desc, type, rarity, imgIndex, mhp, dmg, acc, sell);
        this.price = price;
    }

    /**
     * For enchant scrolls
     *
     * @param rm
     * @param name
     * @param desc
     * @param rarity
     * @param imgIndex
     * @param eChance
     * @param sell
     * @param price
     */
    public ShopItem(ResourceManager rm, String name, String desc, int rarity, int imgIndex,
                    int eChance, int sell, int price) {
        super(rm, name, desc, rarity, imgIndex, eChance, sell);
        this.price = price;
    }

}

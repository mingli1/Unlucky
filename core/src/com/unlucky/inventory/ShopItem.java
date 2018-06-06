package com.unlucky.inventory;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
                    int imgIndex, int level, int hp, int exp, int sell, int price) {
        super(rm, name, desc, rarity, imgIndex, level, level, hp, exp, sell);
        this.price = price;
        actor = new Image(rm.shopitems[0][imgIndex]);
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
                    int imgIndex, int level, int mhp, int dmg, int acc, int sell, int price) {
        super(rm, name, desc, type, rarity, imgIndex, level, level, mhp, dmg, acc, sell);
        this.price = price;
        actor = new Image(rm.shopitems[type - 1][imgIndex]);
        int enchantSeed = MathUtils.random(75, 225);
        for (int i = 0; i < level; i++) enchantCost += enchantSeed;
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
    public ShopItem(ResourceManager rm, String name, String desc, int rarity, int imgIndex, int level,
                    int eChance, int sell, int price) {
        super(rm, name, desc, rarity, imgIndex, level, level, eChance, sell);
        this.price = price;
        actor = new Image(rm.shopitems[9][imgIndex]);
    }

}

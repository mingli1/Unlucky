package com.unlucky.inventory;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * An Item is held by an inventory slot and can be one of:
 * - potion (restores current hp)
 * - equip (several categories of equips)
 * - misc (some other useless thing)
 *
 * @author Ming Li
 */
public class Item {

    // id
    public String name;
    // for rendering onto tooltip
    public String desc;
    // type of item
    /**
     * 0 - potion
     * 1 - misc
     * 2 - helmet
     * 3 - armor
     * 4 - weapon
     * 5 - gloves
     * 6 - shoes
     * 7 - necklace
     * 8 - shield
     * 9 - ring
     */
    public int type;

    /**
     * items are weighted with rarity meaning
     * different likelihoods to drop
     * 0 - common (60% chance out of all items)
     * 1 - rare (25% chance)
     * 2 - epic (10% chance)
     * 3 - legendary (5% chance)
     */
    public int rarity;

    // item stats
    public int hp = 0;
    public int mhp = 0;
    public int dmg = 0;
    public int acc = 0;
    public int sell = 0;

    // rendering
    public Image actor;

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
    public Item(String name, String desc, int rarity, int imgIndex, int hp, int sell) {
        this.name = name;
        this.desc = desc;
        this.rarity = rarity;
        this.hp = hp;
        this.sell = sell;
        type = 0;
        // actor =
    }

    /**
     * For misc items
     * Only can be sold for gold
     *
     * @param name
     * @param desc
     * @param rarity
     * @param imgIndex
     * @param sell
     */
    public Item(String name, String desc, int rarity, int imgIndex, int sell) {
        this.name = name;
        this.desc = desc;
        this.rarity = rarity;
        this.sell = sell;
        type = 1;
        // actor =
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
    public Item(String name, String desc, int type, int rarity, int imgIndex, int mhp, int dmg, int acc, int sell) {
        this.name = name;
        this.desc = desc;
        this.type = type;
        this.rarity = rarity;
        this.mhp = mhp;
        this.dmg = dmg;
        this.acc = acc;
        this.sell = sell;
        // actor =
    }


}

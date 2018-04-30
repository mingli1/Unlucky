package com.unlucky.inventory;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.unlucky.resource.ResourceManager;
import com.unlucky.resource.Util;

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
    // name displayed on tooltip
    public String labelName;
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

    // an item's index in the inventory
    public int index;
    // whether or not this item is equipped
    public boolean equipped = false;
    // the number of successful enchants on the item
    public int enchants = 0;
    public int enchantCost;

    // rendering
    public Image actor;
    public int imgIndex;

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
    public Item(ResourceManager rm, String name, String desc, int rarity, int imgIndex, int hp, int sell) {
        this.name = name;
        this.desc = desc;
        this.rarity = rarity;
        this.imgIndex = imgIndex;
        this.hp = hp;
        this.sell = sell;
        type = 0;
        actor = new Image(rm.items20x20[0][imgIndex]);
        labelName = name;
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
    public Item(ResourceManager rm, String name, String desc, int rarity, int imgIndex, int sell) {
        this.name = name;
        this.desc = desc;
        this.rarity = rarity;
        this.imgIndex = imgIndex;
        this.sell = sell;
        type = 1;
        actor = new Image(rm.items20x20[1][imgIndex]);
        labelName = name;
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
    public Item(ResourceManager rm, String name, String desc, int type, int rarity, int imgIndex, int mhp, int dmg, int acc, int sell) {
        this.name = name;
        this.desc = desc;
        this.type = type;
        this.rarity = rarity;
        this.imgIndex = imgIndex;
        this.mhp = mhp;
        this.dmg = dmg;
        this.acc = acc;
        this.sell = sell;
        actor = new Image(rm.items20x20[type][imgIndex]);
        labelName = name;
    }

    /**
     * Adjusts the stats/attributes of an Item based on a given level
     * Only called once per item's existence
     *
     * @param level
     */
    public void adjust(int level) {
        // max hp will be scaled by 5-7 parts of original item stat added on each level
        // dmg is scaled 4-6 parts of original per level
        int mhpSeed = mhp / MathUtils.random(5, 7);
        int dmgSeed = dmg / MathUtils.random(4, 6);
        // set initial enchant cost
        int enchantSeed = MathUtils.random(50, 100);
        for (int i = 0; i < level - 1; i++) {
            mhp += mhpSeed;
            dmg += dmgSeed;
        }
        for (int i = 0; i < level; i++) {
            enchantCost += enchantSeed;
        }

        // @TODO scale sell value
    }

    /**
     * Returns the full description with all stats and descriptions
     * concatenated into a single string
     *
     * @return
     */
    public String getFullDesc() {
        String ret = "";
        if (type == 0) {
            ret = desc + "\nHEALS FOR " + hp + " HP";
        } else if (type == 1) {
            ret = desc;
        } else {
            ret = desc + "\n";
            if (mhp != 0) ret += "+" + mhp + " HP\n";
            if (dmg != 0) ret += "+" + dmg + " DAMAGE\n";
            if (acc != 0) ret += "+" + acc + "% ACCURACY";
        }
        // remove newline from end of string if there is one
        ret = ret.trim();
        return ret;
    }

    /**
     * Enchanting an item (equip) causes its stats (except accuracy)
     * to be multiplied by random values depending on the item's rarity
     * There is a 50% chance that the enchant succeeds
     * If it fails, there is a 40% chance that the item gets destroyed
     * "+(num success)" is added to the item's name
     * An item can be enchanted as many times as possible
     *
     * This method deals with enchant success
     *
     */
    public void enchant() {
        float multiplier = 1.f;

        switch (rarity) {
            // common
            case 0:
                multiplier = MathUtils.random(Util.COMMON_ENCHANT_MIN, Util.COMMON_ENCHANT_MAX);
                break;
            // rare
            case 1:
                multiplier = MathUtils.random(Util.RARE_ENCHANT_MIN, Util.RARE_ENCHANT_MAX);
                break;
            // epic
            case 2:
                multiplier = MathUtils.random(Util.EPIC_ENCHANT_MIN, Util.EPIC_ENCHANT_MAX);
                break;
            // legendary
            case 3:
                multiplier = MathUtils.random(Util.LEGENDARY_ENCHANT_MIN, Util.LEGENDARY_ENCHANT_MAX);
                break;
        }

        mhp = (int) (mhp * multiplier);
        dmg = (int) (dmg * multiplier);
        sell = (int) (sell * multiplier);
        enchantCost = (int) (enchantCost * multiplier);

        enchants++;
        // every 5 enchants an item goes up 1 rarity
        if (enchants % 5 == 0) rarity++;
        if (rarity > 3) rarity = 3;
        // enchant number indicator
        labelName = "+" + enchants + " " + name;
    }

    /**
     * Returns the item's name as [RARITY] [name] for dialog box
     *
     * @return
     */
    public String getDialogName() {
        String ret = "";
        switch (rarity) {
            case 0:
                ret = "[COMMON] " + name;
                break;
            case 1:
                ret = "[RARE] " + name;
                break;
            case 2:
                ret = "[EPIC] " + name;
                break;
            case 3:
                ret = "[LEGENDARY] " + name;
                break;
        }
        return ret;
    }

}

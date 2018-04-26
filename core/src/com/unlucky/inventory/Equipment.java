package com.unlucky.inventory;

import com.badlogic.gdx.math.Vector2;

/**
 * The collection of equips that the player has equipped
 *
 * @author Ming Li
 */
public class Equipment {

    /**
     * equip slot indices (item indices offset by 2):
     * 0 - helmet
     * 1 - armor
     * 2 - weapon
     * 3 - gloves
     * 4 - shoes
     * 5 - necklace
     * 6 - shield
     * 7 - ring
     */
    public static final int NUM_SLOTS = 8;

    public Item[] equips;
    // stores the positions of equip slots relative to inventory
    public Vector2[] positions;

    public Equipment() {
        equips = new Item[NUM_SLOTS];
        positions = new Vector2[NUM_SLOTS];

        positions[0] = new Vector2(42, 42);
        positions[1] = new Vector2(42, 26);
        positions[2] = new Vector2(26, 26);
        positions[3] = new Vector2(58, 26);
        positions[4] = new Vector2(42, 10);
        positions[5] = new Vector2(10, 42);
        positions[6] = new Vector2(10, 26);
        positions[7] = new Vector2(10, 10);
    }

    /**
     * The player equips an item and it gets placed into the correct slot
     * Returns false if cannot be equipped
     *
     * @param equip
     * @return
     */
    public boolean addEquip(Item equip) {
        if (equips[equip.type - 2] == null) {
            equips[equip.type - 2] = equip;
            return true;
        }
        return false;
    }

    /**
     * Removes an equip at an index and returns the Item
     *
     * @param index
     * @return
     */
    public Item removeEquip(int index) {
        Item ret = null;
        if (equips[index] != null) {
            ret = equips[index];
            equips[index] = null;
            return ret;
        }
        return null;
    }

    /**
     * Returns the equip from a specific index but does not remove it
     *
     * @param index
     * @return
     */
    public Item getEquipAt(int index) {
        return equips[index];
    }

}

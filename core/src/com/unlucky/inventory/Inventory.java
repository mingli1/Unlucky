package com.unlucky.inventory;

/**
 * An Inventory is a collection of Items arranged in a grid
 * This acts mainly as a collection class and functions to implement inventory management
 *
 * @author Ming Li
 */
public class Inventory {

    // inventory dimensions
    public static final int NUM_SLOTS = 24;
    public static final int NUM_COLS = 6;
    public static final int NUM_ROWS = 4;

    public Item[] items;

    public Inventory() {
        items = new Item[NUM_SLOTS];
    }

    /**
     * Returns the index of the first empty slot in the inventory
     * Returns -1 if there are no free slots
     *
     * @return
     */
    public int getFirstFreeSlotIndex() {
        for (int i = 0; i < NUM_SLOTS; i++) {
            if (items[i] == null) return i;
        }
        return -1;
    }

    /**
     * Returns the Item in the inventory at a given index
     * but does not remove the item from the inventory
     *
     * @param index
     * @return
     */
    public Item getItem(int index) {
        return items[index];
    }

    /**
     * Returns whether or not a slot at an index is empty
     *
     * @param index
     * @return
     */
    public boolean isFreeSlot(int index) {
        return items[index] == null;
    }

    /**
     * Adds an Item to the inventory that is placed in the first available slot
     * Returns false if item cannot be added
     *
     * @param item
     * @return
     */
    public boolean addItem(Item item) {
        int i = getFirstFreeSlotIndex();
        if (i != -1) {
            items[i] = item;
            item.index = i;
            return true;
        }
        return false;
    }

    /**
     * Adds an Item at a specific index
     * Returns false if item cannot be added
     *
     * @param item
     * @param index
     * @return
     */
    public boolean addItemAtIndex(Item item, int index) {
        if (isFreeSlot(index)) {
            items[index] = item;
            item.index = index;
            return true;
        }
        return false;
    }

    /**
     * Removes an Item from the inventory at a specific index
     *
     * @param index
     */
    public void removeItem(int index) {
        if (items[index] != null) items[index] = null;
    }

    /**
     * Removes an Item from the inventory and returns the Item
     *
     * @param index
     * @return
     */
    public Item takeItem(int index) {
        Item ret = null;
        if (items[index] != null) {
            ret = items[index];
            items[index] = null;
            return ret;
        }
        return null;
    }

    /**
     * Returns whether or not the inventory is full
     *
     * @return
     */
    public boolean isFull() {
        for (int i = 0; i < NUM_SLOTS; i++) {
            if (items[i] == null) return false;
        }
        return true;
    }

    /**
     * Clears every item in the inventory
     */
    public void clear() {
        for (int i = 0; i < NUM_SLOTS; i++) {
            removeItem(i);
        }
    }

}

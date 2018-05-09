package com.unlucky.battle;

import com.badlogic.gdx.utils.Array;
import com.unlucky.resource.Util;

/**
 * A set of special moves
 * The player can have a set amount of special moves
 * A special moveset cannot have more than 2 of same moves
 *
 * @author Ming Li
 */
public class SpecialMoveset {

    // maximum numbers of smoves in a set
    public static final int MAX_MOVES = 5;
    public Array<SpecialMove> smoveset;

    public SpecialMoveset() {
        smoveset = new Array<SpecialMove>();
    }

    /**
     * Adds a special move based on id to the set
     * if constraints are met
     *
     * @param id
     */
    public void addSMove(int id) {
        if (canAdd(id)) {
            smoveset.add(getMove(id));
        }
    }

    public SpecialMove getMoveAt(int i) {
        return smoveset.get(i);
    }

    /**
     * Clears the smoveset
     */
    public void clear() {
        smoveset.clear();
    }

    public boolean isFull() {
        return smoveset.size == MAX_MOVES;
    }

    /**
     * Removes an smove from a given index
     * @param index
     */
    public void remove(int index) {
        smoveset.removeIndex(index);
    }

    public String toString() {
        String ret = "[ ";
        for (int i = 0; i < smoveset.size; i++) {
            ret += smoveset.get(i).name + (i == smoveset.size - 1 ? "" : ", ");
        }
        ret += " ]";
        return ret;
    }

    /**
     * Returns whether it's possible to add a certain smove to the set
     * Constraint 1: must be enough space
     * Constraint 2: move must not already appear twice
     *
     * @param id
     * @return
     */
    public boolean canAdd(int id) {
        if (smoveset.size == MAX_MOVES) return false;
        int count = 0;
        for (int i = 0; i < smoveset.size; i++) {
            if (smoveset.get(i).id == id) count++;
        }
        return count < 2;
    }

    /**
     * Returns the smove associated with a id
     *
     * @param id
     * @return
     */
    private SpecialMove getMove(int id) {
        switch (id) {
            case Util.DISTRACT: return Util.S_DISTRACT;
            case Util.FOCUS: return Util.S_FOCUS;
            case Util.INTIMIDATE: return Util.S_INTIMIDATE;
            case Util.REFLECT: return Util.S_REFLECT;
            case Util.STUN: return Util.S_STUN;
            case Util.INVERT: return Util.S_INVERT;
            case Util.SACRIFICE: return Util.S_SACRIFICE;
            case Util.SHIELD: return Util.S_SHIELD;
        }
        return null;
    }

}

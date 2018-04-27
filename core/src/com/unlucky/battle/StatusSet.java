package com.unlucky.battle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.unlucky.resource.ResourceManager;

/**
 * Container for status effect icons and their rendering
 * Renders status icons in a horizontal line
 * If a status is removed then the line shrinks
 * There are no duplicate effects in a set
 *
 * @author Ming Li
 */
public class StatusSet {

    // status icons
    public Array<StatusEffect> effects;

    // whether or not it's the player's status icons
    private boolean player;
    private ResourceManager rm;

    public StatusSet(boolean player, ResourceManager rm) {
        this.player = player;
        this.rm = rm;
        effects = new Array<StatusEffect>();
    }

    /**
     * Adds an effect to the list if the effect is not already in it
     *
     * @param effect
     */
    public void addEffect(int effect) {
        if (findEffect(effect) == -1) {
            effects.add(new StatusEffect(effect, rm));
        }
    }

    public void clear() {
        effects.clear();
    }

    /**
     * Removes all multi turn status effects
     */
    public void clearAllButSingleTurnEffects() {
        for (int i = 0; i < effects.size; i++) {
            if (effects.get(i).type == StatusEffect.DMG_RED) {
                effects.removeIndex(i);
            }
        }
    }

    /**
     * Removes all single turn status effects
     */
    public void clearAllButMultiTurnEffects() {
        for (int i = 0; i < effects.size; i++) {
            if (effects.get(i).type != StatusEffect.DMG_RED) {
                effects.removeIndex(i);
            }
        }
    }

    /**
     * Returns if the set contains a certain effect
     *
     * @param effect
     * @return
     */
    public boolean contains(int effect) {
        return findEffect(effect) != -1;
    }

    /**
     * Returns the index of an effect
     * Returns -1 if not in set
     *
     * @param effect
     * @return
     */
    public int findEffect(int effect) {
        for (int i = 0; i < effects.size; i++) {
            if (effects.get(i).type == effect)
                return effects.get(i).type;
        }
        return -1;
    }

    /**
     * Player's status bar renders from left to right
     * Enemy's renders from right to left
     *
     * @param batch
     */
    public void render(SpriteBatch batch) {
        for (int i = 0; i < effects.size; i++) {
            StatusEffect s = effects.get(i);
            if (player) {
                if (s != null) batch.draw(s.icon, 1 + (i * 11), 90);
            } else {
                if (s != null) batch.draw(s.icon, 189 - (i * 11), 90);
            }
        }
    }

}

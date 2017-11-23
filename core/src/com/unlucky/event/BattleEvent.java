package com.unlucky.event;

/**
 * All transitions between events to be processed
 *
 * @author Ming Li
 */
public enum BattleEvent {

    // placeholder event for nothing happening
    NONE,
    // the enemy runs at the sight of the player
    ENEMY_FLEES,
    // the enemy engages in battle
    ENEMY_ENGAGES

}

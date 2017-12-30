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
    END_BATTLE,
    // the enemy engages in battle
    PLAYER_TURN,
    // the enemy's turn
    ENEMY_TURN,
    // player levels up
    LEVEL_UP

}

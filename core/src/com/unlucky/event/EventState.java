package com.unlucky.event;

/**
 * Different states for events such as walking, battling, picking up items
 *
 * @author Ming Li
 */
public enum EventState {
    NONE,
    MOVING,
    BATTLING,
    TRANSITION,
    LEVEL_UP,
    INVENTORY,
    TILE_EVENT
}

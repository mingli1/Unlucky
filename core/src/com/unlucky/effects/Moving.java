package com.unlucky.effects;

import com.badlogic.gdx.math.Vector2;

/**
 * Stores "moving" coordinates that allow for a start position
 * to be moved to a target position with some velocity.
 *
 * @author Ming Li
 */
public class Moving {

    // position
    public Vector2 position;

    // start
    public Vector2 origin;
    // end
    public Vector2 target;

    // speed in pixels/tick
    public float speed = 0;
    public boolean horizontal;
    public boolean shouldStart = false;

    public Moving(Vector2 origin, Vector2 target, float speed) {
        this.position = origin;
        this.origin = origin;
        this.target = target;
        this.speed = speed;

        // determine if it's moving horizontally or vertically
        horizontal = origin.y == target.y;
    }

    /**
     * Starts the movement from either origin->target or vice versa
     */
    public void start() {
        shouldStart = true;
    }

    public void update(float dt) {
        if (shouldStart) {
            if (horizontal) {
                // moving right
                if (origin.x < target.x) {
                    if (position.x < target.x && position.x + speed * dt < target.x) {
                        float next = position.x + speed * dt;
                        position.set(next, position.y);
                    } else {
                        position.set(target.x, target.y);
                        shouldStart = false;
                    }
                }
                // moving left
                else {
                    if (position.x > target.x && position.x - speed * dt > target.x) {
                        float next = position.x - speed * dt;
                        position.set(next, position.y);
                    } else {
                        position.set(target.x, target.y);
                        shouldStart = false;
                    }
                }
            } else {
                // moving up
                if (origin.y < target.y && position.y + speed * dt < target.y) {
                    if (position.y < target.y) {
                        float next = position.y + speed * dt;
                        position.set(position.x, next);
                    } else {
                        position.set(target.x, target.y);
                        shouldStart = false;
                    }
                }
                // moving down
                else {
                    if (position.y > target.y && position.y - speed * dt > target.y) {
                        float next = position.y - speed * dt;
                        position.set(position.x, next);
                    } else {
                        position.set(target.x, target.y);
                        shouldStart = false;
                    }
                }
            }
        }
    }

}

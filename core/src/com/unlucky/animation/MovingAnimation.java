package com.unlucky.animation;

import com.badlogic.gdx.math.Vector2;
import com.unlucky.screen.GameScreen;

/**
 * Specifically for rendering animations that move from point A to point B
 *
 * @author Ming Li
 */
public class MovingAnimation {

    public AnimationManager anim;

    public Vector2 position;
    public Vector2 origin;
    public Vector2 target;

    public int speed;
    private boolean horizontal;
    private boolean shouldStart = false;

    private GameScreen gameScreen;

    public MovingAnimation(AnimationManager anim, GameScreen gameScreen, Vector2 origin, Vector2 target, int speed) {
        this.anim = anim;
        this.gameScreen = gameScreen;
        this.origin = this.position = origin;
        this.target = target;
        this.speed = speed;

        // determine if it's moving horizontally or vertically
        horizontal = origin.y == target.y;
    }

    public void start() {
        shouldStart = true;
    }

    public void update(float dt) {
        if (shouldStart) {
            if (horizontal) {
                // moving right
                if (origin.x < target.x) {
                    if (position.x < target.x) {
                        position.x += speed;
                    } else {
                        position = target;
                        shouldStart = false;
                    }
                }
                // moving left
                else {
                    if (position.x > target.x) {
                        position.x -= speed;
                    } else {
                        position = target;
                        shouldStart = false;
                    }
                }
            } else {
                // moving up
                if (origin.y < target.y) {
                    if (position.y < target.y) {
                        position.y += speed;
                    } else {
                        position = target;
                        shouldStart = false;
                    }
                }
                // moving down
                else {
                    if (position.y > target.y) {
                        position.y -= speed;
                    } else {
                        position = target;
                        shouldStart = false;
                    }
                }
            }
        }
    }

    public void render(float dt) {
        gameScreen.getBatch().draw(anim.getKeyFrame(true), position.x, position.y);
    }

}

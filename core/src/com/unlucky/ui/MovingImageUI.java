package com.unlucky.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * An Image Button representing a custom image UI applied with a move animation when
 * first set visible or after a battle event
 *
 * @author Ming Li
 */
public class MovingImageUI extends Image {

    // start
    private Vector2 origin;
    // end
    private Vector2 target;

    // speed in pixels/tick
    private float speed = 0;
    private boolean horizontal;
    private boolean shouldStart = false;

    public MovingImageUI(TextureRegion style) {
        super(style);
    }

    /**
     * For single non animated images
     *
     * @param skin
     * @param origin
     * @param target
     * @param speed
     * @param w
     * @param h
     */
    public MovingImageUI(TextureRegion skin, Vector2 origin, Vector2 target, float speed, int w, int h) {
        this(skin);
        this.origin = origin;
        this.target = target;
        this.speed = speed;

        this.setSize(w, h);
        this.setPosition(origin.x, origin.y);
        this.setTouchable(Touchable.disabled);

        // determine if it's moving horizontally or vertically
        horizontal = origin.y == target.y;
    }

    /**
     * Starts the movement animation from either origin->target or vice versa
     */
    public void start() {
        shouldStart = true;
    }

    public void update(float dt) {
        if (shouldStart) {
            if (horizontal) {
                // moving right
                if (origin.x < target.x) {
                    if (getX() < target.x && getX() + speed * dt < target.x) {
                        float next = getX() + speed * dt;
                        setPosition(next, getY());
                    } else {
                        setPosition(target.x, target.y);
                        shouldStart = false;
                    }
                }
                // moving left
                else {
                    if (getX() > target.x && getX() - speed * dt > target.x) {
                        float next = getX() - speed * dt;
                        setPosition(next, getY());
                    } else {
                        setPosition(target.x, target.y);
                        shouldStart = false;
                    }
                }
            } else {
                // moving up
                if (origin.y < target.y && getY() + speed * dt < target.y) {
                    if (getY() < target.y) {
                        float next = getY() + speed * dt;
                        setPosition(getX(), next);
                    } else {
                        setPosition(target.x, target.y);
                        shouldStart = false;
                    }
                }
                // moving down
                else {
                    if (getY() > target.y && getY() - speed * dt > target.y) {
                        float next = getY() - speed * dt;
                        setPosition(getX(), next);
                    } else {
                        setPosition(target.x, target.y);
                        shouldStart = false;
                    }
                }
            }
        }
    }

    public Vector2 getOrigin() {
        return origin;
    }

    public void setOrigin(Vector2 origin) {
        this.origin = origin;
    }

    public Vector2 getTarget() {
        return target;
    }

    public void setTarget(Vector2 target) {
        this.target = target;
    }

    public boolean isShouldStart() { return shouldStart; }

    public void setImage(TextureRegion image) {
        this.setDrawable(new TextureRegionDrawable(image));
    }

}

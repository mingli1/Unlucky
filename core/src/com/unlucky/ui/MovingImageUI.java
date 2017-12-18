package com.unlucky.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * An Image Button representing a custom image UI applied with a move animation when
 * first set visible or after a battle event
 *
 * @author Ming Li
 */
public class MovingImageUI extends ImageButton {

    // start
    private Vector2 origin;
    // end
    private Vector2 target;

    // speed in pixels/tick
    private int speed = 0;
    private boolean horizontal;
    private boolean shouldStart = false;

    public MovingImageUI(ImageButtonStyle style) {
        super(style);
    }

    public MovingImageUI(TextureRegion skin, Vector2 origin, Vector2 target, int speed, int w, int h) {
        this(new ImageButtonStyle());
        this.getStyle().imageUp = new TextureRegionDrawable(skin);
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
                    if (getX() < target.x) {
                        float next = getX() + speed;
                        setPosition(next, getY());
                    } else {
                        setPosition(target.x, target.y);
                        shouldStart = false;
                    }
                }
                // moving left
                else {
                    if (getX() > target.x) {
                        float next = getX() - speed;
                        setPosition(next, getY());
                    } else {
                        setPosition(target.x, target.y);
                        shouldStart = false;
                    }
                }
            } else {
                // moving up
                if (origin.y < target.y) {
                    if (getY() < target.y) {
                        float next = getY() + speed;
                        setPosition(getX(), next);
                    } else {
                        setPosition(target.x, target.y);
                        shouldStart = false;
                    }
                }
                // moving down
                else {
                    if (getY() > target.y) {
                        float next = getY() - speed;
                        setPosition(getX(), next);
                    } else {
                        setPosition(target.x, target.y);
                        shouldStart = false;
                    }
                }
            }
        }
    }

}

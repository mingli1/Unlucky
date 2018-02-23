package com.unlucky.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.unlucky.effects.Moving;

/**
 * An Image Button representing a custom image UI applied with a move animation when
 * first set visible or after a battle event
 *
 * @author Ming Li
 */
public class MovingImageUI extends Image {

    public Moving moving;

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
        moving = new Moving(origin, target, speed);

        this.setSize(w, h);
        this.setPosition(origin.x, origin.y);
        this.setTouchable(Touchable.disabled);
    }

    /**
     * Starts the movement animation from either origin->target or vice versa
     */
    public void start() {
        moving.start();
    }

    public void update(float dt) {
        moving.update(dt);
        this.setPosition(moving.position.x, moving.position.y);
    }

}

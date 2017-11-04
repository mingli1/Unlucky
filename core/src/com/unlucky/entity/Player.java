package com.unlucky.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.unlucky.animation.AnimationManager;
import com.unlucky.input.InputManager;
import com.unlucky.map.TileMap;
import com.unlucky.resource.ResourceManager;
import com.unlucky.resource.Util;

/**
 * The protagonist of the game.
 *
 * @author Ming Li
 */
public class Player extends Entity {

    private InputManager im;

    public Player(String id, Vector2 position, TileMap tileMap, ResourceManager rm, InputManager im) {
        super(id, position, tileMap, rm);
        this.im = im;

        am = new AnimationManager(rm.sprites16x16, Util.PLAYER_WALKING, Util.PLAYER_WALKING_DELAY);
    }

    public void update(float dt) {
        super.update(dt);

        handleInput();
    }

    /**
     * Handles input for player movement (directional keypad)
     */
    public void handleInput() {
        if (im.clickedDown()) {
            am.setAnimation(AnimationManager.DOWN);
            this.setMovement(1);
        }
        else if (im.clickedUp()) {
            am.setAnimation(AnimationManager.UP);
            this.setMovement(2);
        }
        else if (im.clickedRight()) {
            am.setAnimation(AnimationManager.RIGHT);
            this.setMovement(3);
        }
        else if (im.clickedLeft()) {
            am.setAnimation(AnimationManager.LEFT);
            this.setMovement(4);
        }
    }

    public void render(SpriteBatch batch) {
        super.render(batch, true);
    }

}

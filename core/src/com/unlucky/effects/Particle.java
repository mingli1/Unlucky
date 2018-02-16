package com.unlucky.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.unlucky.animation.AnimationManager;

/**
 * A particle that has velocity
 * Can be animated or non-animated
 *
 * @author Ming Li
 */
public class Particle implements Pool.Poolable {

    // types of particles
    public static final byte RAINDROP = 0;
    public static final byte SNOWFLAKE = 1;

    // a particle's position relative to a camera
    public Vector2 position;
    // a particle's velocity
    public Vector2 velocity;

    public int type;
    public boolean animated;

    // for static particles
    public TextureRegion sprite;
    // for animated particles
    public AnimationManager anim;

    // a particle's "death" animation which is played after the particle should be removed
    public AnimationManager deathAnim;

    // a particle's lifespan
    public float lifespan;
    // end particle's life
    public boolean shouldRemove = false;

    private float stateTime = 0;

    /**
     * Default constructor
     */
    public Particle() {}

    /**
     * Initializes a non-animated particle's components
     *
     * @param position
     * @param velocity
     * @param lifespan
     */
    public void init(int type, Vector2 position, Vector2 velocity, float lifespan, TextureRegion sprite, AnimationManager deathAnim) {
        shouldRemove = false;
        this.type = type;
        this.position = position;
        this.velocity = velocity;
        this.lifespan = lifespan;
        animated = false;
        this.sprite = sprite;
        this.deathAnim = deathAnim;
    }

    /**
     * Initializes an animated particle's components
     *
     * @param type
     * @param position
     * @param velocity
     * @param lifespan
     * @param numFrames
     * @param delay
     */
    public void init(int type, Vector2 position, Vector2 velocity, float lifespan, int numFrames, float delay) {
        init(type, position, velocity, lifespan, null, deathAnim);
        animated = true;
        if (anim == null) anim = null;
    }

    /**
     * Resets the properties of the particle each time it's freed
     * and put back into the Pool
     */
    @Override
    public void reset() {
        shouldRemove = false;
        this.position.set(0, 0);
        this.velocity.set(0, 0);
        this.lifespan = 0;
    }

    public void update(float dt) {
        // update position
        if (!shouldRemove) {
            position.x += velocity.x * dt;
            position.y += velocity.y * dt;
        }

        stateTime += dt;
        if (stateTime >= lifespan) {
            stateTime = 0;
            shouldRemove = true;
        }

        if (shouldRemove) {
            if (deathAnim != null) deathAnim.update(dt);
        }
        else {
            if (animated) anim.update(dt);
        }
    }

    public void render(SpriteBatch batch) {
        if (shouldRemove) {
            if (deathAnim != null) batch.draw(deathAnim.getKeyFrame(false), position.x, position.y);
        }
        else {
            if (animated && anim != null) batch.draw(anim.getKeyFrame(true), position.x, position.y);
            else {
                if (sprite != null) batch.draw(sprite, position.x, position.y);
            }
        }
    }

}

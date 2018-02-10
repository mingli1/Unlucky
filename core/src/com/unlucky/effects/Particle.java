package com.unlucky.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.unlucky.animation.AnimationManager;
import com.unlucky.resource.ResourceManager;

/**
 * A single animated or non animated sprite representing
 * a particle that has position and velocity
 *
 * @author Ming Li
 */
public class Particle {

    // types of particles
    public static final byte RAINDROP = 0;
    public static final byte SNOWFLAKE = 1;

    // a particle's position relative to a camera
    public Vector2 position;
    // a particle's velocity
    public Vector2 velocity;

    public int type;
    public boolean animated;

    public TextureRegion sprite;
    public AnimationManager anim;

    // a particle's lifespan
    public float lifespan;
    // end particle's life
    public boolean shouldRemove = false;

    private float stateTime = 0;

    /**
     * Non animated particle
     *
     * @param type
     * @param position
     * @param velocity
     * @param rm
     */
    public Particle(int type, Vector2 position, Vector2 velocity, float lifespan, ResourceManager rm) {
        this.type = type;
        this.position = position;
        this.velocity = velocity;
        this.lifespan = lifespan;

        animated = false;
        //sprite = rm.particles[type];
    }

    /**
     * Animated particle
     *
     * @param type
     * @param position
     * @param velocity
     * @param rm
     */
    public Particle(int type, Vector2 position, Vector2 velocity, int numFrames, float delay, float lifespan, ResourceManager rm) {
        this(type, position, velocity, lifespan, rm);

        animated = true;
        //anim = new AnimationManager(rm.particlesWxH[type], numFrames, delay);
        anim = new AnimationManager(rm.raindrop16x16, numFrames, 0, delay);
    }

    public void update(float dt) {
        // update position
        position.x += velocity.x * dt;
        position.y += velocity.y * dt;

        stateTime += dt;
        if (stateTime >= lifespan) {
            stateTime = 0;
            shouldRemove = true;
        }

        if (animated) {
            anim.update(dt);
        }
    }

    public void render(SpriteBatch batch) {
        if (animated)
            batch.draw(anim.getKeyFrame(true), position.x, position.y);
        else
            batch.draw(sprite, position.x, position.y);
    }

}

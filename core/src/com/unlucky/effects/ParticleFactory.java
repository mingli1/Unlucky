package com.unlucky.effects;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.unlucky.resource.ResourceManager;
import com.unlucky.resource.Util;

import java.util.Random;

/**
 * Stores and manages particles
 * Creates particle effects such as rain and snow
 *
 * @author Ming Li
 */
public class ParticleFactory {

    // type of particle being generated
    public int type;
    // max num of particles to generate
    public int numParticles;
    // velocity of particles
    public Vector2 velocity;

    public Array<Particle> particles;

    // camera viewport
    private int viewWidth;
    private int viewHeight;

    private Random rand;
    private OrthographicCamera cam;
    private ResourceManager rm;

    public ParticleFactory(int type, int numParticles, Vector2 velocity, OrthographicCamera cam, Random rand, ResourceManager rm) {
        this.type = type;
        this.numParticles = numParticles;
        this.velocity = velocity;
        this.cam = cam;
        this.viewWidth = (int) cam.viewportWidth;
        this.viewHeight = (int) cam.viewportHeight;
        this.rand = rand;
        this.rm = rm;

        particles = new Array<Particle>();
    }

    public ParticleFactory(OrthographicCamera cam, Random rand, ResourceManager rm) {
        this.cam = cam;
        this.viewWidth = (int) cam.viewportWidth;
        this.viewHeight = (int) cam.viewportHeight;
        this.rand = rand;
        this.rm = rm;

        particles = new Array<Particle>();
    }

    public void update(float dt) {
        // update all particles
        for (int i = 0; i < particles.size; i++) {
            particles.get(i).update(dt);
            if (particles.get(i).shouldRemove) {
                particles.removeIndex(i);
                i--;
                spawn();
            }
        }
    }

    public void render(SpriteBatch batch) {
        // render all particles
        for (int i = 0; i < particles.size; i++) {
            particles.get(i).render(batch);
        }
    }

    /**
     * Resets the factory to generate a new type of particle
     *
     * @param type
     * @param numParticles
     */
    public void set(int type, int numParticles, Vector2 velocity) {
        particles.clear();
        this.type = type;
        this.numParticles = numParticles;
        this.velocity = velocity;
        populate();
    }

    /**
     * Spawns one particle of a certain type
     */
    public void spawn() {
        switch (type) {
            case Particle.RAINDROP:
                float lifespan = rand.nextFloat() + 0.8f;
                Vector2 velocity = new Vector2(this.velocity.x,
                        Util.getDeviatedRandomValue((int) this.velocity.y, Util.RAINDROP_Y_DEVIATED, rand));
                Particle raindrop = new Particle(Particle.RAINDROP,
                        new Vector2(cam.position.x + Util.getRandomValue(-viewWidth / 2, viewWidth / 2, rand),
                                cam.position.y + Util.getRandomValue(-viewHeight / 2, viewHeight / 2, rand)),
                        velocity, 8, 1 / 8f, lifespan, rm);
                particles.add(raindrop);
                break;
        }
    }

    /**
     * Populates the array of particles with an initial
     * numParticles amount of particles
     */
    public void populate() {
        for (int i = 0; i < numParticles; i++) {
            spawn();
        }
    }

}

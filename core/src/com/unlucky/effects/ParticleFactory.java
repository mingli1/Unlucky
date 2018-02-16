package com.unlucky.effects;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.unlucky.animation.AnimationManager;
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

    // data
    public Array<Particle> particles;
    // pool
    public Pool<Particle> particlePool;

    // camera viewport
    private int viewWidth;
    private int viewHeight;

    private Random rand;
    private OrthographicCamera cam;
    private final ResourceManager rm;

    public ParticleFactory(int type, int numParticles, Vector2 velocity, OrthographicCamera cam, Random rand, final ResourceManager rm) {
        this.type = type;
        this.numParticles = numParticles;
        this.velocity = velocity;
        this.cam = cam;
        this.viewWidth = (int) cam.viewportWidth;
        this.viewHeight = (int) cam.viewportHeight;
        this.rand = rand;
        this.rm = rm;

        particles = new Array<Particle>();
        particlePool = new Pool<Particle>() {
            @Override
            protected Particle newObject() {
                return new Particle();
            }
        };
    }

    public ParticleFactory(OrthographicCamera cam, Random rand, final ResourceManager rm) {
        this.cam = cam;
        this.viewWidth = (int) cam.viewportWidth;
        this.viewHeight = (int) cam.viewportHeight;
        this.rand = rand;
        this.rm = rm;

        particles = new Array<Particle>();
        particlePool = new Pool<Particle>() {
            @Override
            protected Particle newObject() {
                return new Particle();
            }
        };
    }

    public void update(float dt) {
        // update all particles and free them if necessary
        for (int i = particles.size - 1; i >= 0; i--) {
            Particle p = particles.get(i);
            p.update(dt);
            if (p.shouldRemove) {
                if (p.deathAnim != null) {
                    if (p.deathAnim.currentAnimation.isAnimationFinished()) {
                        particles.removeIndex(i);
                        particlePool.free(p);
                        spawn();
                    }
                }
                else {
                    particles.removeIndex(i);
                    particlePool.free(p);
                    spawn();
                }
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
        particlePool.clear();
        this.type = type;
        this.numParticles = numParticles;
        this.velocity = velocity;
        populate();
    }

    /**
     * Spawns one particle of a certain type from the Pool
     */
    public void spawn() {
        Particle item = particlePool.obtain();
        Vector2 weatherParticleSpawnPosition = new Vector2(cam.position.x + Util.getRandomValue(-viewWidth / 2, viewWidth / 2, rand),
                cam.position.y + Util.getRandomValue(-viewHeight / 2, viewHeight / 2, rand));
        switch (type) {
            case Particle.RAINDROP:
                float rls = rand.nextFloat() + 0.4f;
                Vector2 rv = new Vector2(this.velocity.x,
                        Util.getDeviatedRandomValue((int) this.velocity.y, Util.RAINDROP_Y_DEVIATED, rand));
                AnimationManager rd = new AnimationManager(rm.raindropAnim16x16, 3, 0, 1 / 6f);
                item.init(type, weatherParticleSpawnPosition, rv, rls, rm.raindrop, rd);
                break;
            case Particle.SNOWFLAKE:
                float sls = rand.nextFloat() + 0.3f;
                Vector2 sv = new Vector2(this.velocity.x,
                        Util.getDeviatedRandomValue((int) this.velocity.y, Util.SNOWFLAKE_Y_DEVIATED, rand));
                item.init(type, weatherParticleSpawnPosition, sv, sls, rm.snowflake, null);
                break;
        }
        particles.add(item);
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

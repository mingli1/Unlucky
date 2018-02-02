package com.unlucky.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.unlucky.resource.ResourceManager;
import com.unlucky.screen.GameScreen;

/**
 * "Unlucky" is a RPG/Dungeon Crawler based on RNG
 * The player will go through various levels with numerous enemies
 * and attempt to complete each level by collecting a certain item (TBD)
 *
 * @author Ming Li
 */
public class Unlucky extends Game {

    public static final String TITLE = "Unlucky Version 0.5.9.5";

    // Desktop screen dimensions
    public static final int V_WIDTH = 200;
    public static final int V_HEIGHT = 120;
    public static final int V_SCALE = 6;

    // Rendering utilities
    public SpriteBatch batch;

    // Resources
    public ResourceManager rm;

	public void create() {
        batch = new SpriteBatch();
        rm = new ResourceManager();

        this.setScreen(new GameScreen(this, rm));
	}

	public void render() {
        Gdx.graphics.setTitle(TITLE + " | " + Gdx.graphics.getFramesPerSecond() + " fps");
        super.render();
	}

	public void dispose() {
        batch.dispose();
        super.dispose();
	}

}

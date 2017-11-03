package com.unlucky.main;

import com.badlogic.gdx.Game;
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

    public static final String TITLE = "Unlucky";

    // Desktop screen dimensions
    public static final int V_WIDTH = 160;
    public static final int V_HEIGHT = V_WIDTH / 12 * 9;
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
        super.render();
	}

	public void dispose() {
        batch.dispose();
        super.dispose();
	}

}

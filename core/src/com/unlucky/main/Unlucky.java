package com.unlucky.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * "Unlucky" is a RPG/Dungeon Crawler based on RNG
 * The player will go through various levels with numerous enemies
 * and attempt to complete each level by collecting a certain item (TBD)
 *
 * @author Ming Li
 */
public class Unlucky extends ApplicationAdapter {

    public static final String TITLE = "Unlucky";

    // Desktop screen dimensions
    public static final int V_WIDTH = 160;
    public static final int V_HEIGHT = V_WIDTH / 12 * 9;
    public static final int V_SCALE = 4;

    // Rendering utilities
    public SpriteBatch batch;

	public void create() {
        batch = new SpriteBatch();
	}

	public void render() {
        super.render();
	}

	public void dispose() {
        batch.dispose();
	}

}

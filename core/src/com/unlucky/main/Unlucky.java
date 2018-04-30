package com.unlucky.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.unlucky.entity.Player;
import com.unlucky.parallax.Background;
import com.unlucky.resource.ResourceManager;
import com.unlucky.screen.*;
import com.unlucky.ui.inventory.InventoryUI;

/**
 * "Unlucky" is a RPG/Dungeon Crawler based on RNG
 * The player will go through various levels with numerous enemies
 * and attempt to complete each level by collecting a certain item (TBD)
 *
 * @author Ming Li
 */
public class Unlucky extends Game {

    public static final String VERSION = "0.7.5";
    public static final String TITLE = "Unlucky Version " + VERSION;

    // Desktop screen dimensions
    public static final int V_WIDTH = 200;
    public static final int V_HEIGHT = 120;
    public static final int V_SCALE = 6;

    // Rendering utilities
    public SpriteBatch batch;

    // Resources
    public ResourceManager rm;

    // universal player
    public Player player;

    // Screens
    public MenuScreen menuScreen;
    public GameScreen gameScreen;
    public WorldSelectScreen worldSelectScreen;
    public LevelSelectScreen levelSelectScreen;
    public InventoryScreen inventoryScreen;
    public StatisticsScreen statisticsScreen;
    public InventoryUI inventoryUI;

    // main bg
    public Background[] menuBackground;

    // debugging
    public Label fps;

	public void create() {
        batch = new SpriteBatch();
        rm = new ResourceManager();
        player = new Player("player", rm);

        // debugging
        fps = new Label("", new Label.LabelStyle(rm.pixel10, Color.RED));
        fps.setFontScale(0.5f);

        inventoryUI = new InventoryUI(this, player, rm);
        menuScreen = new MenuScreen(this, rm);
        gameScreen = new GameScreen(this, rm);
        worldSelectScreen = new WorldSelectScreen(this, rm);
        levelSelectScreen = new LevelSelectScreen(this, rm);
        inventoryScreen = new InventoryScreen(this, rm);
        statisticsScreen = new StatisticsScreen(this, rm);

        // create parallax background
        menuBackground = new Background[3];

        // ordered by depth
        // sky
        menuBackground[0] = new Background(rm.titleScreenBackground[0],
            (OrthographicCamera) menuScreen.getStage().getCamera(), new Vector2(0, 0));
        menuBackground[0].setVector(0, 0);
        // back clouds
        menuBackground[1] = new Background(rm.titleScreenBackground[2],
            (OrthographicCamera) menuScreen.getStage().getCamera(), new Vector2(0.3f, 0));
        menuBackground[1].setVector(20, 0);
        // front clouds
        menuBackground[2] = new Background(rm.titleScreenBackground[1],
            (OrthographicCamera) menuScreen.getStage().getCamera(), new Vector2(0.3f, 0));
        menuBackground[2].setVector(60, 0);

        // profiler
        GLProfiler.enable();

        this.setScreen(menuScreen);
	}

	public void render() {
        fps.setText(Gdx.graphics.getFramesPerSecond() + " fps");
        super.render();
	}

	public void dispose() {
        batch.dispose();
        super.dispose();

        rm.dispose();
        menuScreen.dispose();
        gameScreen.dispose();
        worldSelectScreen.dispose();
        levelSelectScreen.dispose();
        inventoryScreen.dispose();
        statisticsScreen.dispose();
        inventoryUI.dispose();

        GLProfiler.disable();
	}

    /**
     * Logs profile for SpriteBatch calls
     */
	public void profile(String source) {
        System.out.println("Profiling " + source + "..." + "\n" +
            "  Drawcalls: " + GLProfiler.drawCalls +
            ", Calls: " + GLProfiler.calls +
            ", TextureBindings: " + GLProfiler.textureBindings +
            ", ShaderSwitches:  " + GLProfiler.shaderSwitches +
            " vertexCount: " + GLProfiler.vertexCount.value);
        GLProfiler.reset();
    }

}

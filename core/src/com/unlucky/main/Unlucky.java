package com.unlucky.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.unlucky.entity.Player;
import com.unlucky.inventory.Item;
import com.unlucky.parallax.Background;
import com.unlucky.resource.ResourceManager;
import com.unlucky.save.Save;
import com.unlucky.screen.*;
import com.unlucky.screen.game.VictoryScreen;
import com.unlucky.ui.inventory.InventoryUI;

/**
 * "Unlucky" is a RPG/Dungeon Crawler based on RNG
 * The player will go through various levels with numerous enemies
 * and attempt to complete each level by reaching the end tile.
 *
 * @author Ming Li
 */
public class Unlucky extends Game {

    public static final String VERSION = "0.8.6";
    public static final String TITLE = "Unlucky Version " + VERSION;

    // Links
    public static final String GITHUB = "https://github.com/mingli1/Unlucky";
    public static final String YOUTUBE = "https://www.youtube.com/channel/UC-oA-vkeYrgEy23Sq2PLC8w/videos?shelf_id=0&sort=dd&view=0";

    // Desktop screen dimensions
    public static final int V_WIDTH = 200;
    public static final int V_HEIGHT = 120;
    public static final int V_SCALE = 6;

    // Rendering utilities
    public SpriteBatch batch;

    // Resources
    public ResourceManager rm;

    // Universal player
    public Player player;

    // Game save
    public Save save;

    // Screens
    public MenuScreen menuScreen;
    public GameScreen gameScreen;
    public WorldSelectScreen worldSelectScreen;
    public LevelSelectScreen levelSelectScreen;
    public InventoryScreen inventoryScreen;
    public ShopScreen shopScreen;
    public SpecialMoveScreen smoveScreen;
    public StatisticsScreen statisticsScreen;
    public InventoryUI inventoryUI;
    public VictoryScreen victoryScreen;
    public SettingsScreen settingsScreen;

    // main bg
    public Background[] menuBackground;

    // debugging
    public Label fps;

	public void create() {
        batch = new SpriteBatch();
        rm = new ResourceManager();
        player = new Player("player", rm);

        save = new Save(player, "save.json");
        save.load(rm);
        player.levelUp(3000);
        player.applyLevelUp();
        player.maxLevel = 12;
        player.maxWorld = 2;

        // debugging
        fps = new Label("", new Label.LabelStyle(rm.pixel10, Color.RED));
        fps.setFontScale(0.5f);
        fps.setVisible(player.settings.showFps);

        inventoryUI = new InventoryUI(this, player, rm);
        menuScreen = new MenuScreen(this, rm);
        gameScreen = new GameScreen(this, rm);
        worldSelectScreen = new WorldSelectScreen(this, rm);
        levelSelectScreen = new LevelSelectScreen(this, rm);
        inventoryScreen = new InventoryScreen(this, rm);
        shopScreen = new ShopScreen(this, rm);
        smoveScreen = new SpecialMoveScreen(this, rm);
        statisticsScreen = new StatisticsScreen(this, rm);
        victoryScreen = new VictoryScreen(this, rm);
        settingsScreen = new SettingsScreen(this, rm);

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

        // temp save key
        if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
            save.save();
        }
	}

	private String getTestItem(int minLevel, int maxLevel) {
	    int level = MathUtils.random(minLevel, maxLevel);
	    Item item = rm.getRandomItem(MathUtils.random(minLevel, maxLevel));
        if (item == null) return "NULL";
	    item.adjust(level);
	    return "name: " + item.name + "\n" +
            "level: " + level + "\n" +
            "mhp: " + item.mhp + "\n" +
            "dmg: " + item.dmg + "\n" +
            "sell: " + item.sell + "\n" +
            "enchantCost: " + item.enchantCost + "\n";
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
        shopScreen.dispose();
        statisticsScreen.dispose();
        inventoryUI.dispose();
        victoryScreen.dispose();
        settingsScreen.dispose();

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

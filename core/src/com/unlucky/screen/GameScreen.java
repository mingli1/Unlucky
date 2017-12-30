package com.unlucky.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.unlucky.entity.Player;
import com.unlucky.event.Battle;
import com.unlucky.event.EventState;
import com.unlucky.main.Unlucky;
import com.unlucky.map.TileMap;
import com.unlucky.parallax.Background;
import com.unlucky.resource.ResourceManager;
import com.unlucky.resource.Util;
import com.unlucky.ui.BattleUIHandler;
import com.unlucky.ui.Hud;

import java.util.Random;

/**
 * Handles all gameplay.
 *
 * @author Ming Li
 */
public class GameScreen extends AbstractScreen {

    public EventState currentEvent;

    public TileMap map;
    public Player player;
    public Hud hud;
    public BattleUIHandler battleUIHandler;
    public Battle battle;
    public BattleTransition transition;
    public LevelUpScreen levelUp;

    // battle background
    private Background[] bg;

    public GameScreen(final Unlucky game, final ResourceManager rm) {
        super(game, rm);

        currentEvent = EventState.MOVING;

        map = new TileMap(16, "maps/test_map.txt", new Vector2(0, 0), rm);
        player = new Player("player", map.toMapCoords(5, 8), map, rm);
        battle = new Battle(this, map, player);
        hud = new Hud(this, map, player, rm);
        battleUIHandler = new BattleUIHandler(this, map, player, battle, rm);
        transition = new BattleTransition(this, battle, battleUIHandler, hud, player);
        levelUp = new LevelUpScreen(this, map, player, rm);

        // create bg
        createBackground(0);

        // input multiplexer
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(hud.stage);
        multiplexer.addProcessor(battleUIHandler.stage);
        multiplexer.addProcessor(levelUp.stage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    /**
     * Creates the dynamic background
     * @TODO make it work for any number of background images
     *
     * @param bgIndex is the theme of bg
     */
    private void createBackground(int bgIndex) {
        bg = new Background[2];

        // background image array is ordered by depth
        TextureRegion[] images = rm.battleBackgrounds400x240[bgIndex];
        // sky
        bg[0] = new Background(images[0], (OrthographicCamera) battleUIHandler.stage.getCamera(), new Vector2(0.3f, 0), 2);
        bg[0].setVector(40, 0);
        // field
        bg[1] = new Background(images[1], (OrthographicCamera) battleUIHandler.stage.getCamera(), new Vector2(0, 0), 2);
        bg[1].setVector(0, 0);
    }

    public void update(float dt) {
        if (currentEvent == EventState.MOVING) {
            // camera directs on the player
            cam.position.x = player.getPosition().x + 8;
            cam.position.y = player.getPosition().y + 4;
            cam.update();

            player.update(dt);
            map.update(dt);

            // engage in battle if found
            if (player.isBattling()) {
                setCurrentEvent(EventState.TRANSITION);
                transition.start(EventState.MOVING, EventState.BATTLING);
            }
        }

        if (currentEvent == EventState.BATTLING) {
            // update bg
            for (int i = 0; i < bg.length; i++) {
                bg[i].update(dt);
            }
            battleUIHandler.update(dt);
        }

        if (currentEvent == EventState.TRANSITION) transition.update(dt);
        if (currentEvent == EventState.LEVEL_UP) levelUp.update(dt);
    }

    public void render(float dt) {
        update(dt);

        // clear screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        // bg camera
        game.batch.setProjectionMatrix(battleUIHandler.stage.getCamera().combined);
        if (currentEvent == EventState.BATTLING || transition.shouldRenderBattle()) {
            for (int i = 0; i < bg.length; i++) {
                bg[i].render(game.batch);
            }
        }

        // map camera
        game.batch.setProjectionMatrix(cam.combined);
        if (currentEvent == EventState.MOVING || transition.shouldRenderMap()) {
            map.render(game.batch);
            player.render(game.batch);
        }

        game.batch.end();

        if (currentEvent == EventState.MOVING) hud.render(dt);
        if (currentEvent == EventState.BATTLING || transition.shouldRenderBattle()) battleUIHandler.render(dt);
        if (currentEvent == EventState.LEVEL_UP || transition.shouldRenderLevelUp()) levelUp.render(dt);
        if (currentEvent == EventState.TRANSITION) transition.render(dt);
    }

    public void dispose() {
        super.dispose();
        hud.dispose();
        battleUIHandler.dispose();
        levelUp.dispose();
    }

    /**
     * @TODO: Add some sort of transitioning between events
     * @param event
     */
    public void setCurrentEvent(EventState event) {
        this.currentEvent = event;
    }

}

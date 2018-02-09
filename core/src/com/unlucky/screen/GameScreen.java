package com.unlucky.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
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
import com.unlucky.ui.BattleUIHandler;
import com.unlucky.ui.Hud;
import com.unlucky.ui.InventoryUI;

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
    public TransitionScreen transition;
    public LevelUpScreen levelUp;
    public DialogScreen dialog;
    public InventoryUI inventoryUI;

    // battle background
    private Background[] bg;

    public GameScreen(final Unlucky game, final ResourceManager rm) {
        super(game, rm);

        currentEvent = EventState.MOVING;

        map = new TileMap(16, "maps/test_map.txt", new Vector2(0, 0), rm);
        player = new Player("player", map.toMapCoords(11, 30), map, rm);
        battle = new Battle(this, map, player);
        hud = new Hud(this, map, player, rm);
        battleUIHandler = new BattleUIHandler(this, map, player, battle, rm);
        transition = new TransitionScreen(this, battle, battleUIHandler, hud, player);
        levelUp = new LevelUpScreen(this, map, player, rm);
        dialog = new DialogScreen(this, map, player, rm);
        inventoryUI = new InventoryUI(this, map, player, rm);

        // create bg
        createBackground(0);

        // input multiplexer
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(hud.stage);
        multiplexer.addProcessor(battleUIHandler.stage);
        multiplexer.addProcessor(levelUp.stage);
        multiplexer.addProcessor(dialog.stage);
        multiplexer.addProcessor(inventoryUI.stage);
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
                hud.toggle(false);
                setCurrentEvent(EventState.TRANSITION);
                transition.start(EventState.MOVING, EventState.BATTLING);
            }
            // player stepped on interaction tile
            if (player.isTileInteraction()) {
                hud.toggle(false);
                setCurrentEvent(EventState.TILE_EVENT);
                // @TODO: change level scaling to map level
                if (player.getCurrentTile().isQuestionMark())
                    dialog.startDialog(player.getQuestionMarkDialog(player.getLevel()), EventState.MOVING, EventState.MOVING);
                else if (player.getCurrentTile().isExclamationMark())
                    dialog.startDialog(player.getExclamDialog(player.getLevel()), EventState.MOVING, EventState.MOVING);
            }
            // player stepped on teleport tile
            if (player.isTeleporting()) {
                hud.toggle(false);
                setCurrentEvent(EventState.TRANSITION);
                transition.start(EventState.MOVING, EventState.MOVING);
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
        if (currentEvent == EventState.TILE_EVENT) dialog.update(dt);
        if (currentEvent == EventState.INVENTORY) inventoryUI.update(dt);
    }

    public void render(float dt) {
        update(dt);

        // clear screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        // fix fading
        game.batch.setColor(Color.WHITE);

        if (currentEvent == EventState.BATTLING || transition.shouldRenderBattle()) {
            // bg camera
            game.batch.setProjectionMatrix(battleUIHandler.stage.getCamera().combined);
            for (int i = 0; i < bg.length; i++) {
                bg[i].render(game.batch);
            }
        }

        if (currentEvent == EventState.MOVING || currentEvent == EventState.INVENTORY ||
                transition.shouldRenderMap() || currentEvent == EventState.TILE_EVENT)
        {
            // map camera
            game.batch.setProjectionMatrix(cam.combined);
            map.renderBottomLayer(game.batch, cam);

            player.render(game.batch);

            map.render(game.batch, cam);
            map.renderTopLayer(game.batch, cam);

            game.batch.setBlendFunction(GL20.GL_DST_COLOR, GL20.GL_ONE_MINUS_SRC_ALPHA);
            game.batch.draw(rm.lightmap, 0, 0);
            game.batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        }

        game.batch.end();

        if (currentEvent == EventState.MOVING) hud.render(dt);
        if (currentEvent == EventState.BATTLING || transition.shouldRenderBattle()) battleUIHandler.render(dt);
        if (currentEvent == EventState.LEVEL_UP || transition.shouldRenderLevelUp()) levelUp.render(dt);
        if (currentEvent == EventState.TILE_EVENT) dialog.render(dt);
        if (currentEvent == EventState.INVENTORY) inventoryUI.render(dt);
        if (currentEvent == EventState.TRANSITION) transition.render(dt);
    }

    public void dispose() {
        super.dispose();
        hud.dispose();
        battleUIHandler.dispose();
        dialog.dispose();
        levelUp.dispose();
        inventoryUI.dispose();
    }

    /**
     * @TODO: Add some sort of transitioning between events
     * @param event
     */
    public void setCurrentEvent(EventState event) {
        this.currentEvent = event;
    }

}
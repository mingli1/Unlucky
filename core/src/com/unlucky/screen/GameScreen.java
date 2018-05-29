package com.unlucky.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.unlucky.event.Battle;
import com.unlucky.event.EventState;
import com.unlucky.main.Unlucky;
import com.unlucky.map.GameMap;
import com.unlucky.parallax.Background;
import com.unlucky.resource.ResourceManager;
import com.unlucky.screen.game.DialogScreen;
import com.unlucky.screen.game.LevelUpScreen;
import com.unlucky.screen.game.TransitionScreen;
import com.unlucky.ui.battleui.BattleUIHandler;
import com.unlucky.ui.Hud;

/**
 * Handles all gameplay.
 *
 * @author Ming Li
 */
public class GameScreen extends AbstractScreen {

    public EventState currentEvent;

    public GameMap gameMap;
    public Hud hud;
    public BattleUIHandler battleUIHandler;
    public Battle battle;
    public TransitionScreen transition;
    public LevelUpScreen levelUp;
    public DialogScreen dialog;

    // input
    public InputMultiplexer multiplexer;

    // battle background
    private Background[] bg;

    public GameScreen(final Unlucky game, final ResourceManager rm) {
        super(game, rm);

        currentEvent = EventState.MOVING;

        gameMap = new GameMap(0, 0, this, game.player, rm);
        battle = new Battle(this, gameMap.tileMap, gameMap.player);
        hud = new Hud(this, gameMap.tileMap, gameMap.player, rm);
        battleUIHandler = new BattleUIHandler(this, gameMap.tileMap, gameMap.player, battle, rm);
        transition = new TransitionScreen(this, battle, battleUIHandler, hud, gameMap.player);
        levelUp = new LevelUpScreen(this, gameMap.tileMap, gameMap.player, rm);
        dialog = new DialogScreen(this, gameMap.tileMap, gameMap.player, rm);

        // create bg
        createBackground(0);

        // input multiplexer
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(hud.getStage());
        multiplexer.addProcessor(battleUIHandler.getStage());
        multiplexer.addProcessor(levelUp.getStage());
        multiplexer.addProcessor(dialog.getStage());
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(multiplexer);
        hud.toggle(true);
        hud.startLevelDescriptor();
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
        bg[0] = new Background(images[0], (OrthographicCamera) battleUIHandler.getStage().getCamera(), new Vector2(0.3f, 0));
        bg[0].setVector(40, 0);
        // field
        bg[1] = new Background(images[1], (OrthographicCamera) battleUIHandler.getStage().getCamera(), new Vector2(0, 0));
        bg[1].setVector(0, 0);
    }

    /**
     * When the player dies it shows a "click to continue" message along with what they lost
     */
    public void die() {
        // reset player's hp after dying
        gameMap.player.setHp(gameMap.player.getMaxHp());
        setCurrentEvent(EventState.DEATH);
        hud.toggle(false);
        hud.deathGroup.setVisible(true);
    }

    /**
     * Updates the camera position to follow the player unless he's on the edges of the map
     */
    public void updateCamera() {
        // camera directs on the player
        if (gameMap.player.getPosition().x <= gameMap.tileMap.mapWidth * 16 - 7 * 16 &&
            gameMap.player.getPosition().x >= 6 * 16)
            cam.position.x = gameMap.player.getPosition().x + 8;
        if (gameMap.player.getPosition().y <= gameMap.tileMap.mapHeight * 16 - 4 * 16 &&
            gameMap.player.getPosition().y >= 4 * 16 - 8)
            cam.position.y = gameMap.player.getPosition().y + 4;
        cam.update();

        if (gameMap.player.getPosition().x < 6 * 16) cam.position.x = 104;
        if (gameMap.player.getPosition().y < 4 * 16 - 8) cam.position.y = 60.5f;
        if (gameMap.player.getPosition().x > gameMap.tileMap.mapWidth * 16 - 7 * 16) cam.position.x = 280;
        if (gameMap.player.getPosition().y > gameMap.tileMap.mapHeight * 16 - 4 * 16) cam.position.y = 324;
    }

    public void update(float dt) {
        if (currentEvent == EventState.MOVING) {
            updateCamera();

            gameMap.update(dt);
            hud.update(dt);
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
        if (currentEvent == EventState.INVENTORY) game.inventoryUI.update(dt);
    }

    public void render(float dt) {
        update(dt);

        // clear screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        // fix fading
        game.batch.setColor(Color.WHITE);

        if (currentEvent == EventState.BATTLING || transition.renderBattle) {
            // bg camera
            game.batch.setProjectionMatrix(battleUIHandler.getStage().getCamera().combined);
            for (int i = 0; i < bg.length; i++) {
                bg[i].render(game.batch);
            }
        }

        if (currentEvent == EventState.MOVING || currentEvent == EventState.INVENTORY ||
            transition.renderMap || currentEvent == EventState.TILE_EVENT || currentEvent == EventState.DEATH) {
            // map camera
            game.batch.setProjectionMatrix(cam.combined);
            // render map and player
            gameMap.render(dt, game.batch, cam);
        }

        game.batch.end();

        if (currentEvent == EventState.MOVING || currentEvent == EventState.DEATH)
            hud.render(dt);
        if (currentEvent == EventState.BATTLING || transition.renderBattle)
            battleUIHandler.render(dt);
        if (currentEvent == EventState.LEVEL_UP || transition.renderLevelUp)
            levelUp.render(dt);
        if (currentEvent == EventState.TILE_EVENT) dialog.render(dt);
        if (currentEvent == EventState.INVENTORY) game.inventoryUI.render(dt);
        if (currentEvent == EventState.TRANSITION) transition.render(dt);
    }

    public void dispose() {
        super.dispose();
        hud.dispose();
        battleUIHandler.dispose();
        dialog.dispose();
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
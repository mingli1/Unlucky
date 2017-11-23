package com.unlucky.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.unlucky.entity.Player;
import com.unlucky.entity.monsters.Slime;
import com.unlucky.event.EventState;
import com.unlucky.main.Unlucky;
import com.unlucky.map.TileMap;
import com.unlucky.resource.ResourceManager;
import com.unlucky.ui.BattleUI;
import com.unlucky.ui.Hud;

/**
 * Handles all gameplay.
 *
 * @author Ming Li
 */
public class GameScreen extends AbstractScreen {

    private EventState currentEvent;

    private TileMap map;
    private Player player;
    private Hud hud;
    private BattleUI battle;

    public GameScreen(final Unlucky game, final ResourceManager rm) {
        super(game, rm);

        currentEvent = EventState.MOVING;

        map = new TileMap(16, "maps/test_map.txt", new Vector2(0, 0), rm);
        player = new Player("player", map.toMapCoords(5, 8), map, rm);
        hud = new Hud(player, game.batch, rm);
        battle = new BattleUI(player, game.batch, rm);

        // input multiplexer
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(hud.stage);
        multiplexer.addProcessor(battle.stage);
        Gdx.input.setInputProcessor(multiplexer);

        map.getTile(4, 4).addEntity(new Slime("slime", map.toMapCoords(4, 4), map, rm));
    }

    public void update(float dt) {
        if (currentEvent == EventState.MOVING) {
            // camera directs on the player
            cam.position.x = player.getPosition().x + 8;
            cam.position.y = player.getPosition().y + 4;
            cam.update();

            player.update(dt);
            map.update(dt);
        }

        if (currentEvent == EventState.BATTLING) battle.update(dt);
    }

    public void render(float dt) {
        update(dt);

        // clear screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();

        if (currentEvent == EventState.MOVING) {
            map.render(game.batch);
            player.render(game.batch);
        }

        game.batch.end();

        if (currentEvent == EventState.MOVING) hud.render(dt);
        if (currentEvent == EventState.BATTLING) battle.render(dt);
    }

    public void dispose() {
        super.dispose();
        hud.dispose();
        battle.dispose();
    }

    /**
     * @TODO: Add some sort of transitioning between events
     * @param event
     */
    public void setCurrentEvent(EventState event) {
        this.currentEvent = event;
    }

}

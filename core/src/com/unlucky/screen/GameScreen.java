package com.unlucky.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.unlucky.entity.Player;
import com.unlucky.main.Unlucky;
import com.unlucky.map.TileMap;
import com.unlucky.resource.ResourceManager;

/**
 * Handles all gameplay.
 *
 * @author Ming Li
 */
public class GameScreen extends AbstractScreen {

    private TileMap test;
    private Player player;

    public GameScreen(final Unlucky game, final ResourceManager rm) {
        super(game, rm);

        test = new TileMap(16, "maps/test_map.txt", new Vector2(0, 0), rm);
        player = new Player("player", new Vector2(2, 2), test, rm, im);
    }

    public void update(float dt) {
        // camera directs on the player
        cam.position.x = player.getPosition().x * test.tileSize;
        cam.position.y = player.getPosition().y * test.tileSize;

        player.update(dt);

        cam.update();
    }

    public void render(float dt) {
        update(dt);

        // clear screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();

        test.render(game.batch);
        player.render(game.batch);

        game.batch.end();
    }

}

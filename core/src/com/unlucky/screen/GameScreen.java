package com.unlucky.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.unlucky.entity.Player;
import com.unlucky.main.Unlucky;
import com.unlucky.map.TileMap;
import com.unlucky.resource.ResourceManager;
import com.unlucky.scene.Hud;

/**
 * Handles all gameplay.
 *
 * @author Ming Li
 */
public class GameScreen extends AbstractScreen {

    private TileMap test;
    private Player player;
    private Hud hud;

    public GameScreen(final Unlucky game, final ResourceManager rm) {
        super(game, rm);

        test = new TileMap(16, "maps/test_map.txt", new Vector2(0, 0), rm);
        player = new Player("player", test.toMapCoords(5, 8), test, rm);
        hud = new Hud(player, game.batch, rm);
    }

    public void update(float dt) {
        // camera directs on the player
        cam.position.x = player.getPosition().x + 8;
        cam.position.y = player.getPosition().y+ 4;

        player.update(dt);

        cam.update();

        //System.out.println(player.isMoving());
        //System.out.println(player.canMove());

        /*
        System.out.println("up: " + player.getMovement(1));
        System.out.println("down: " + player.getMovement(0));
        System.out.println("right: " + player.getMovement(2));
        System.out.println("left: " + player.getMovement(3));
        */
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

        hud.stage.act(dt);
        hud.stage.draw();
    }

    public void dispose() {
        super.dispose();
        hud.dispose();
    }

}

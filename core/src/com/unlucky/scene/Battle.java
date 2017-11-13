package com.unlucky.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.unlucky.entity.Enemy;
import com.unlucky.entity.Player;
import com.unlucky.main.Unlucky;
import com.unlucky.resource.ResourceManager;

import java.util.Random;

/**
 * The UI for a battle scene with the Player against some Enemy
 *
 * @author Ming Li
 */
public class Battle {

    private Random rand;
    private ResourceManager rm;
    private Player player;
    private Enemy enemy;

    // Scene2D
    public Stage stage;
    private Viewport viewport;

    public Battle(Player player, Enemy enemy, SpriteBatch batch, ResourceManager rm) {
        rand = new Random();

        this.player = player;
        this.enemy = enemy;
        this.rm = rm;

        viewport = new ExtendViewport(Unlucky.V_WIDTH * 2, Unlucky.V_HEIGHT * 2, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        Gdx.input.setInputProcessor(stage);
    }

}

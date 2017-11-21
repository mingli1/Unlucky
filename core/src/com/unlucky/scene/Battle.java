package com.unlucky.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
    private MoveUI moveUI;
    private DialogBox dialogBox;

    // graphics
    private ShapeRenderer shapeRenderer;

    public Battle(Player player, Enemy enemy, SpriteBatch batch, ResourceManager rm) {
        rand = new Random();

        this.player = player;
        this.enemy = enemy;
        this.rm = rm;

        shapeRenderer = new ShapeRenderer();

        viewport = new ExtendViewport(Unlucky.V_WIDTH * 2, Unlucky.V_HEIGHT * 2, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        moveUI = new MoveUI(rand, player, stage, rm);
        dialogBox = new DialogBox(stage, rm);

        moveUI.toggleMoveAndOptionUI(false);

        Gdx.input.setInputProcessor(stage);
    }

    public void update(float dt) {

    }

    public void render(float dt) {
        shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
        // render bg
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(193 / 255.f, 193 / 255.f, 193 / 255.f, 1);
        shapeRenderer.rect(0, 0, Unlucky.V_WIDTH * 2, Unlucky.V_HEIGHT * 2);
        shapeRenderer.end();

        moveUI.render(dt);

        stage.act(dt);
        stage.draw();
    }

}

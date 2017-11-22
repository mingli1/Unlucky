package com.unlucky.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.unlucky.entity.Enemy;
import com.unlucky.entity.Player;
import com.unlucky.main.Unlucky;
import com.unlucky.resource.ResourceManager;
import com.unlucky.resource.Util;

import java.util.Random;

/**
 * The UI for a battle scene with the Player against some Enemy
 *
 * @author Ming Li
 */
public class BattleUI implements Disposable {

    private Random rand;
    private ResourceManager rm;
    private Player player;
    private Enemy enemy;

    // Scene2D
    public Stage stage;
    private Viewport viewport;
    private com.unlucky.scene.MoveUI moveUI;
    private com.unlucky.ui.DialogBox dialogBox;

    // graphics
    private ShapeRenderer shapeRenderer;

    // FSM
    private BattleState currentState;

    public BattleUI(Player player, SpriteBatch batch, ResourceManager rm) {
        rand = new Random();

        this.player = player;
        this.rm = rm;

        shapeRenderer = new ShapeRenderer();

        viewport = new ExtendViewport(Unlucky.V_WIDTH * 2, Unlucky.V_HEIGHT * 2, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        moveUI = new com.unlucky.scene.MoveUI(rand, player, stage, rm);
        dialogBox = new com.unlucky.ui.DialogBox(stage, rm);

        moveUI.toggleMoveAndOptionUI(false);
        dialogBox.endDialog();

        Gdx.input.setInputProcessor(stage);
    }

    public void update(float dt) {
        dialogBox.update(dt);
    }

    public void render(float dt) {
        shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
        // render bg
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(193 / 255.f, 193 / 255.f, 193 / 255.f, 1);
        shapeRenderer.rect(0, 0, Unlucky.V_WIDTH * 2, Unlucky.V_HEIGHT * 2);
        shapeRenderer.end();

        stage.act(dt);
        stage.draw();

        moveUI.render(dt, shapeRenderer);
        dialogBox.render(dt, shapeRenderer);
    }

    /**
     * When the player first encounters the enemy and engages in battle
     * There's a 1% chance that the enemy doesn't want to fight
     *
     * @param enemy
     */
    public void engage(Enemy enemy) {
        this.enemy = enemy;

        String[] intro;
        boolean saved = Util.isSuccess(1, rand);
        if (saved) {
            intro = new String[] {
                    "you encountered " + enemy.getId() + "! " +
                            "maybe there's a chance it doesn't want to fight...",
                    "the enemy stares at you and decides flee the battle."
            };
            dialogBox.startDialog(intro);
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}

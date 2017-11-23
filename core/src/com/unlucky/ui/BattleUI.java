package com.unlucky.ui;

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
import com.unlucky.screen.GameScreen;

import java.util.Random;

/**
 * The UI for a battle scene with the Player against some Enemy
 *
 * @author Ming Li
 */
public class BattleUI extends UI implements Disposable {

    // Scene2D
    public Stage stage;
    private Viewport viewport;
    private MoveUI moveUI;
    private DialogBox dialogBox;

    private Enemy enemy;

    public BattleUI(GameScreen gameScreen, Player player, SpriteBatch batch, ResourceManager rm) {
        super(gameScreen, player, rm);

        enemy = null;

        viewport = new ExtendViewport(Unlucky.V_WIDTH * 2, Unlucky.V_HEIGHT * 2, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        moveUI = new MoveUI(gameScreen, player, stage, rm);
        dialogBox = new DialogBox(gameScreen, player, stage, rm);

        moveUI.toggleMoveAndOptionUI(false);
        dialogBox.endDialog();
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

        moveUI.render(dt);
        dialogBox.render(dt);
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
        boolean saved = Util.isSuccess(100, rand);
        if (saved) {
            intro = new String[] {
                    "you encountered " + enemy.getId() + "! " +
                            "maybe there's a chance it doesn't want to fight...",
                    "the enemy stares at you and decides to flee the battle."
            };
            dialogBox.startDialog(intro);
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}

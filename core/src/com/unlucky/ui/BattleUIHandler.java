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
import com.unlucky.event.Battle;
import com.unlucky.event.BattleEvent;
import com.unlucky.main.Unlucky;
import com.unlucky.map.TileMap;
import com.unlucky.resource.ResourceManager;
import com.unlucky.resource.Util;
import com.unlucky.screen.GameScreen;

/**
 * Handles all UI for battle scenes
 *
 * @author Ming Li
 */
public class BattleUIHandler extends UI implements Disposable {

    // Scene2D
    public Stage stage;
    public Viewport viewport;
    public MoveUI moveUI;
    public DialogBox dialogBox;
    public BattleScene battleScene;

    // battle
    private Battle battle;
    public BattleState currentState;

    public BattleUIHandler(GameScreen gameScreen, TileMap tileMap, Player player, Battle battle, SpriteBatch batch, ResourceManager rm) {
        super(gameScreen, tileMap, player, rm);

        this.battle = battle;

        viewport = new ExtendViewport(Unlucky.V_WIDTH * 2, Unlucky.V_HEIGHT * 2, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        currentState = BattleState.NONE;

        moveUI = new MoveUI(gameScreen, tileMap, player, battle, this, stage, rm);
        dialogBox = new DialogBox(gameScreen, tileMap, player, battle, this, stage, rm);
        battleScene = new BattleScene(gameScreen, tileMap, player, battle, this, stage, rm);

        moveUI.toggleMoveAndOptionUI(false);
        dialogBox.endDialog();
    }

    public void update(float dt) {
        if (currentState == BattleState.DIALOG) dialogBox.update(dt);
        battleScene.update(dt);
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

        battleScene.render(dt);
        if (currentState == BattleState.MOVE) moveUI.render(dt);
        if (currentState == BattleState.DIALOG) dialogBox.render(dt);
    }

    /**
     * When the player first encounters the enemy and engages in battle
     * There's a 1% chance that the enemy doesn't want to fight
     * @TODO 50-50 chance for first attack
     *
     * @param enemy
     */
    public void engage(Enemy enemy) {
        battleScene.toggle(true);
        currentState = BattleState.DIALOG;

        String[] intro;
        boolean saved = Util.isSuccess(1, rand);

        if (saved) {
            intro = new String[] {
                    "you encountered " + enemy.getId() + "! " +
                            "maybe there's a chance it doesn't want to fight...",
                    "the enemy stares at you and decides to flee the battle."
            };
            dialogBox.startDialog(intro, BattleEvent.ENEMY_FLEES);
        }
        else {
            intro = new String[] {
                    "you encountered " + enemy.getId() + "! " +
                            "maybe there's a chance it doesn't want to fight...",
                    "the enemy glares at you and decides to engage in battle!"
            };
            dialogBox.startDialog(intro, BattleEvent.ENEMY_ENGAGES);
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}

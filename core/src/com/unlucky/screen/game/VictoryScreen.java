package com.unlucky.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.unlucky.main.Unlucky;
import com.unlucky.map.GameMap;
import com.unlucky.resource.ResourceManager;
import com.unlucky.screen.AbstractScreen;

/**
 * The screen that appears after the player successfully
 * completes a level
 * Shows time of completion, gold obtained, items obtained, exp obtained, etc
 *
 * @author Ming Li
 */
public class VictoryScreen extends AbstractScreen {

    private GameMap gameMap;

    public VictoryScreen(final Unlucky game, final ResourceManager rm) {
        super(game, rm);
    }

    public void show(GameMap gameMap) {
        this.gameMap = gameMap;

        game.fps.setPosition(5, 115);
        stage.addActor(game.fps);

        Gdx.input.setInputProcessor(stage);
        renderBatch = false;
        batchFade = true;
        // fade in animation
        stage.addAction(Actions.sequence(Actions.alpha(0), Actions.run(new Runnable() {
            @Override
            public void run() {
                renderBatch = true;
            }
        }), Actions.fadeIn(0.5f)));
    }

    public void update(float dt) {}

    public void render(float dt) {
        update(dt);

        if (renderBatch) {
            stage.getBatch().setProjectionMatrix(stage.getCamera().combined);
            stage.getBatch().begin();

            // fix fading
            if (batchFade) stage.getBatch().setColor(Color.WHITE);

            // render world background corresponding to the selected world
            stage.getBatch().draw(rm.worldSelectBackgrounds[gameMap.worldIndex], 0, 0);

            stage.getBatch().end();
        }

        super.render(dt);
    }

}

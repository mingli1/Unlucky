package com.unlucky.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.unlucky.main.Unlucky;
import com.unlucky.resource.ResourceManager;

/**
 * Allows the player to select the world to battle in
 * Displays options for worlds with a scroll pane and renders an background
 * according to the world
 * For each world, displays the description and level range of the world
 *
 * @author Ming Li
 */
public class WorldSelectScreen extends DoubleDimensionScreen {

    // current world selection index that determines bg and descriptions
    private int currentWorldIndex = 0;

    public WorldSelectScreen(final Unlucky game, final ResourceManager rm) {
        super(game, rm);
    }

    public void show() {
        Gdx.input.setInputProcessor(stage);
        renderBatch = false;
        // fade in animation
        stage.addAction(Actions.sequence(Actions.alpha(0), Actions.run(new Runnable() {
            @Override
            public void run() {
                renderBatch = true;
            }
        }), Actions.fadeIn(0.5f)));
    }

    public void update(float dt) {

    }

    public void render(float dt) {
        update(dt);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (renderBatch) {
            stage.getBatch().setProjectionMatrix(stage.getCamera().combined);
            stage.getBatch().begin();

            // render world background corresponding to the selected world
            // possibly expensive call?
            stage.getBatch().draw(rm.worldSelectBackgrounds[currentWorldIndex], 0, 0,
                Unlucky.V_WIDTH * 2, Unlucky.V_HEIGHT * 2);

            game.profile("WorldSelectScreen");

            stage.getBatch().end();
        }

        super.render(dt);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}

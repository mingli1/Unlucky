package com.unlucky.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.unlucky.main.Unlucky;
import com.unlucky.resource.ResourceManager;

/**
 * A screen that is an extension of the main menu meaning it has the same
 * background and a transition that slides off the main menu
 *
 * @author Ming Li
 */
public class MenuExtensionScreen extends AbstractScreen {

    public MenuExtensionScreen(final Unlucky game, final ResourceManager rm) {
        super(game, rm);
    }

    /**
     * Shows the screen when entering from a slide right or slide left transition
     */
    public void showSlide(boolean right) {
        Gdx.input.setInputProcessor(stage);
        renderBatch = true;
        batchFade = true;

        stage.addAction(Actions.sequence(Actions.moveTo(
            right ? Unlucky.V_WIDTH : -Unlucky.V_WIDTH, 0), Actions.moveTo(0, 0, 0.3f)));
    }

    public void update(float dt) {
        for (int i = 0; i < game.menuBackground.length; i++) {
            game.menuBackground[i].update(dt);
        }
    }

    public void render(float dt) {
        for (int i = 0; i < game.menuBackground.length; i++) {
            game.menuBackground[i].render((SpriteBatch) stage.getBatch());
        }
    }

}

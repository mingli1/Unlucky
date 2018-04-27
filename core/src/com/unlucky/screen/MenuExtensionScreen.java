package com.unlucky.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.unlucky.main.Unlucky;
import com.unlucky.resource.ResourceManager;

/**
 * A screen that is an extension of the main menu meaning it has the same
 * background and a transition that slides off the main menu
 *
 * @author Ming Li
 */
public abstract class MenuExtensionScreen extends AbstractScreen {

    protected ImageButton exitButton;

    public MenuExtensionScreen(final Unlucky game, final ResourceManager rm) {
        super(game, rm);

        // init exit button
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(rm.menuExitButton[0][0]);
        style.imageDown = new TextureRegionDrawable(rm.menuExitButton[1][0]);
        exitButton = new ImageButton(style);
        exitButton.setSize(18, 18);
        exitButton.setPosition(177, 99);
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

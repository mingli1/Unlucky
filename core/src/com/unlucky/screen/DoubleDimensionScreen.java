package com.unlucky.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.unlucky.main.Unlucky;
import com.unlucky.resource.ResourceManager;

/**
 * An abstract screen with twice the resolution for text rendering
 *
 * @author Ming Li
 */
public abstract class DoubleDimensionScreen extends AbstractScreen {

    // exit button
    protected ImageButton exitButton;

    // enter button group
    protected Group enterButtonGroup;
    protected ImageButton enterButton;
    protected Label enterLabel;

    public DoubleDimensionScreen(final Unlucky game, final ResourceManager rm) {
        super(game, rm);

        // Override
        viewport = new ExtendViewport(Unlucky.V_WIDTH * 2, Unlucky.V_HEIGHT * 2, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);

        // init exit button
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(rm.menuExitButton[0][0]);
        style.imageDown = new TextureRegionDrawable(rm.menuExitButton[1][0]);
        exitButton = new ImageButton(style);
        exitButton.setSize(36, 36);

        // init enter button
        enterButtonGroup = new Group();
        enterButtonGroup.setSize(158, 56);

        ImageButton.ImageButtonStyle enterStyle = new ImageButton.ImageButtonStyle();
        enterStyle.imageUp = new TextureRegionDrawable(rm.enterButton[0][0]);
        enterStyle.imageDown = new TextureRegionDrawable(rm.enterButton[1][0]);
        enterButton = new ImageButton(enterStyle);

        enterLabel = new Label("ENTER", new Label.LabelStyle(rm.pixel10, new Color(79 / 255.f, 79 / 255.f, 117 / 255.f, 1)));
        enterLabel.setTouchable(Touchable.disabled);
        enterLabel.setSize(158, 56);
        enterLabel.setAlignment(Align.center);
        enterLabel.setFontScale(3.f);

        enterButtonGroup.addActor(enterButton);
        enterButtonGroup.addActor(enterLabel);
    }

    /**
     * Handles the position and events of the exit button
     */
    protected abstract void handleExitButton();

    /**
     * Handles the position and events of the enter button
     */
    protected abstract void handleEnterButton();

}

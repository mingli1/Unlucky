package com.unlucky.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.unlucky.main.Unlucky;
import com.unlucky.resource.ResourceManager;

/**
 * An abstract screen representing select screens with scroll panes
 *
 * @author Ming Li
 */
public abstract class SelectScreen extends AbstractScreen {

    // exit button
    protected ImageButton exitButton;

    // enter button group
    protected Group enterButtonGroup;
    protected ImageButton enterButton;
    protected Label enterLabel;

    // screen banner
    protected Label bannerLabel;
    protected Image banner;

    // side description
    protected Image descField;
    protected Label fullDescLabel;

    // Scene2D scroll pane
    protected Table scrollTable;
    protected Table selectionContainer;
    protected ScrollPane scrollPane;
    protected Label.LabelStyle nameStyle;
    protected Label.LabelStyle descStyle;
    protected TextButton.TextButtonStyle buttonSelected;
    protected Array<TextButton> scrollButtons;

    public SelectScreen(final Unlucky game, final ResourceManager rm) {
        super(game, rm);

        // init enter button
        enterButtonGroup = new Group();
        enterButtonGroup.setSize(79, 28);
        enterButtonGroup.setTransform(false);

        ImageButton.ImageButtonStyle enterStyle = new ImageButton.ImageButtonStyle();
        enterStyle.imageUp = new TextureRegionDrawable(rm.enterButton[0][0]);
        enterStyle.imageDown = new TextureRegionDrawable(rm.enterButton[1][0]);
        enterButton = new ImageButton(enterStyle);

        enterLabel = new Label("ENTER", new Label.LabelStyle(rm.pixel10, new Color(79 / 255.f, 79 / 255.f, 117 / 255.f, 1)));
        enterLabel.setTouchable(Touchable.disabled);
        enterLabel.setSize(79, 28);
        enterLabel.setAlignment(Align.center);
        enterLabel.setFontScale(1.5f);

        enterButtonGroup.addActor(enterButton);
        enterButtonGroup.addActor(enterLabel);

        // create title label
        banner = new Image(rm.skin, "default-slider");
        banner.setPosition(7, 102);
        banner.setSize(101, 12);
        stage.addActor(banner);

        bannerLabel = new Label("", rm.skin);
        bannerLabel.setStyle(new Label.LabelStyle(rm.pixel10, new Color(1, 212 / 255.f, 0, 1)));
        bannerLabel.setSize(50, 12);
        bannerLabel.setTouchable(Touchable.disabled);
        bannerLabel.setPosition(10, 102);
        bannerLabel.setAlignment(Align.left);
        stage.addActor(bannerLabel);

        // create side description
        descField = new Image(rm.skin, "default-slider");
        descField.setPosition(114, 36);
        descField.setSize(79, 64);
        stage.addActor(descField);

        fullDescLabel = new Label("", new Label.LabelStyle(rm.pixel10, Color.WHITE));
        fullDescLabel.setPosition(118, 40);
        fullDescLabel.setSize(75, 56);
        fullDescLabel.setTouchable(Touchable.disabled);
        fullDescLabel.setFontScale(0.5f);
        fullDescLabel.setWrap(true);
        fullDescLabel.setAlignment(Align.topLeft);
        stage.addActor(fullDescLabel);
    }

    @Override
    public void show() {
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

    /**
     * Renders the background and stage actors
     *
     * @param dt
     * @param index
     */
    public void render(float dt, int index) {
        update(dt);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (renderBatch) {
            stage.getBatch().setProjectionMatrix(stage.getCamera().combined);
            stage.getBatch().begin();

            // fix fading
            if (batchFade) stage.getBatch().setColor(Color.WHITE);

            // render world background corresponding to the selected world
            // possibly expensive scaling call?
            // @TODO: change 0 to index
            stage.getBatch().draw(rm.worldSelectBackgrounds[0], 0, 0);

            //game.profile("WorldSelectScreen");

            stage.getBatch().end();
        }

        super.render(dt);
    }

    /**
     * Handles the position and events of the exit button
     */
    protected void handleExitButton(final Screen screen) {
        // init exit button
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(rm.menuExitButton[0][0]);
        style.imageDown = new TextureRegionDrawable(rm.menuExitButton[1][0]);
        exitButton = new ImageButton(style);
        exitButton.setSize(18, 18);
        exitButton.setPosition(177, 99);
        stage.addActor(exitButton);

        // fade back to previous screen
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.menuScreen.transitionIn = 0;
                setFadeScreen(screen);
            }
        });
    }

    /**
     * Selects the button from the scroll pane at a given index
     * and unselects all buttons that are not at the index
     *
     * @param index
     */
    protected void selectAt(int index) {
        for (TextButton t : scrollButtons) {
            if (t.isChecked()) t.setChecked(false);
        }
        scrollButtons.get(index).setChecked(true);
    }

    /**
     * Handles the position and events of the enter button
     */
    protected abstract void handleEnterButton();

    /**
     * Creates the scrollable world selections and handles button events
     */
    protected abstract void createScrollPane();

    @Override
    public void dispose() {
        stage.dispose();
    }

}

package com.unlucky.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
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

        // create title label
        banner = new Image(rm.skin, "default-slider");
        banner.setPosition(15, 205);
        banner.setSize(202, 24);
        stage.addActor(banner);

        bannerLabel = new Label("", rm.skin);
        bannerLabel.getStyle().fontColor = new Color(1, 212 / 255.f, 0, 1);
        bannerLabel.setSize(100, 24);
        bannerLabel.setTouchable(Touchable.disabled);
        bannerLabel.setPosition(15 + 5, 205);
        bannerLabel.setFontScale(2.f);
        bannerLabel.setAlignment(Align.left);
        stage.addActor(bannerLabel);

        // create side description
        descField = new Image(rm.skin, "default-slider");
        descField.setPosition(228, 72);
        descField.setSize(158, 128);
        stage.addActor(descField);

        fullDescLabel = new Label("", new Label.LabelStyle(rm.pixel10, Color.WHITE));
        fullDescLabel.setPosition(236, 80);
        fullDescLabel.setSize(150, 112);
        fullDescLabel.setTouchable(Touchable.disabled);
        fullDescLabel.setWrap(true);
        fullDescLabel.setAlignment(Align.topLeft);
        stage.addActor(fullDescLabel);
    }

    @Override
    public void show() {
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
            stage.getBatch().draw(rm.worldSelectBackgrounds[0], 0, 0,
                Unlucky.V_WIDTH * 2, Unlucky.V_HEIGHT * 2);

            //game.profile("WorldSelectScreen");

            stage.getBatch().end();
        }

        super.render(dt);
    }

    /**
     * Handles the position and events of the exit button
     */
    protected void handleExitButton(final Screen screen) {
        exitButton.setPosition(355, 199);
        stage.addActor(exitButton);

        // fade back to previous screen
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                batchFade = false;
                // fade out animation
                stage.addAction(Actions.sequence(Actions.fadeOut(0.3f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            game.setScreen(screen);
                        }
                    })));
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

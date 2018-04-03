package com.unlucky.screen;

import com.badlogic.gdx.Gdx;
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
 * Allows the player to select the world to battle in
 * Displays options for worlds with a scroll pane and renders an background
 * according to the world
 * For each world, displays the description and level range of the world
 *
 * @author Ming Li
 */
public class WorldSelectScreen extends DoubleDimensionScreen {

    private static final int NUM_WORLDS = 10;
    private static final int WORLDS_ENABLED = 1;

    // current world selection index that determines bg and descriptions
    private int currentWorldIndex = 0;

    // Strings
    private String[] worldNames = {
        "SLIME FOREST",
        "????????????????",
        "????????????????",
        "????????????????",
        "????????????????",
        "????????????????",
        "????????????????",
        "????????????????",
        "????????????????",
        "????????????????",
    };

    private String[] worldDescs = {
        "LV. 1-7\nBOSS: KING SLIME",
        "LV. ???-???\nBOSS: ???",
        "LV. ???-???\nBOSS: ???",
        "LV. ???-???\nBOSS: ???",
        "LV. ???-???\nBOSS: ???",
        "LV. ???-???\nBOSS: ???",
        "LV. ???-???\nBOSS: ???",
        "LV. ???-???\nBOSS: ???",
        "LV. ???-???\nBOSS: ???",
        "LV. ???-???\nBOSS: ???"
    };

    private String[] worldFullDescs = {
        "Insert a long, drawn out, made up description of the world " +
            "with some lore that has no relevance to the game whatsoever.",
        "????????",
        "????????",
        "????????",
        "????????",
        "????????",
        "????????",
        "????????",
        "????????",
        "????????"
    };

    // screen banner
    private Label bannerLabel;
    private Image banner;

    // Scene2D scroll pane
    private Table scrollTable;
    private Table selectionContainer;
    private ScrollPane scrollPane;
    private Label.LabelStyle nameStyle;
    private Label.LabelStyle descStyle;
    private TextButton.TextButtonStyle buttonSelected;
    private Array<TextButton> scrollButtons;

    // side description
    private Image descField;
    private Label fullDescLabel;

    public WorldSelectScreen(final Unlucky game, final ResourceManager rm) {
        super(game, rm);

        // create title label
        banner = new Image(rm.skin, "default-slider");
        banner.setPosition(15, 205);
        banner.setSize(202, 24);
        stage.addActor(banner);

        bannerLabel = new Label("SELECT A WORLD", rm.skin);
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

        fullDescLabel = new Label(worldFullDescs[currentWorldIndex],
            new Label.LabelStyle(rm.pixel10, Color.WHITE));
        fullDescLabel.setPosition(236, 80);
        fullDescLabel.setSize(150, 112);
        fullDescLabel.setTouchable(Touchable.disabled);
        fullDescLabel.setWrap(true);
        fullDescLabel.setAlignment(Align.topLeft);
        stage.addActor(fullDescLabel);

        handleExitButton();
        handleEnterButton();
        createScollPane();
    }

    /**
     * Handles the position and events of the exit button
     */
    protected void handleExitButton() {
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
                       game.setScreen(game.menuScreen);
                   }
               })));
           }
        });
    }

    /**
     * Handles the position and events of the enter button
     */
    protected void handleEnterButton() {
        enterButtonGroup.setPosition(228, 8);
        stage.addActor(enterButtonGroup);
    }

    /**
     * Creates the scrollable world selections and handles button events
     */
    private void createScollPane() {
        scrollButtons = new Array<TextButton>();

        nameStyle = new Label.LabelStyle(rm.pixel10, new Color(150 / 255.f, 1, 1, 1));
        descStyle = new Label.LabelStyle(rm.pixel10, Color.WHITE);
        buttonSelected = new TextButton.TextButtonStyle();
        buttonSelected.up = new TextureRegionDrawable(rm.skin.getRegion("default-round-down"));

        scrollTable = new Table();
        scrollTable.setFillParent(true);
        stage.addActor(scrollTable);

        selectionContainer = new Table();
        for (int i = 0; i < NUM_WORLDS; i++) {
            final int index = i;

            // button and label group
            Group g = new Group();
            g.setSize(180, 60);

            final TextButton b = new TextButton("", rm.skin);
            b.getStyle().checked = b.getStyle().down;
            b.getStyle().over = null;
            if (i == 0) b.setChecked(true);
            scrollButtons.add(b);

            // disable worlds not available
            /*
            if (i > WORLDS_ENABLED - 1) {
                b.setDisabled(true);
                b.setTouchable(Touchable.disabled);
            }
            */

            // select world
            b.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    currentWorldIndex = index;
                    selectAt(currentWorldIndex);
                    fullDescLabel.setText(worldFullDescs[currentWorldIndex]);
                }
            });
            b.setFillParent(true);

            Label name = new Label(worldNames[i], nameStyle);
            name.setPosition(10, 40);
            name.setFontScale(1.7f);
            name.setTouchable(Touchable.disabled);
            Label desc = new Label(worldDescs[i], descStyle);
            desc.setPosition(10, 15);
            desc.setTouchable(Touchable.disabled);

            g.addActor(b);
            g.addActor(name);
            g.addActor(desc);

            selectionContainer.add(g).padBottom(8).size(180, 60).row();
        }
        selectionContainer.pack();
        selectionContainer.setTransform(true);
        selectionContainer.setOrigin(selectionContainer.getWidth() / 2,
            selectionContainer.getHeight() / 2);

        scrollPane = new ScrollPane(selectionContainer, rm.skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.layout();
        scrollTable.add(scrollPane).size(210, 202).fill();
        scrollTable.setPosition(-85, -20);
    }

    /**
     * Selects the button from the scroll pane at a given index
     * and unselects all buttons that are not at the index
     *
     * @param index
     */
    private void selectAt(int index) {
        for (TextButton t : scrollButtons) {
            if (t.isChecked()) t.setChecked(false);
        }
        scrollButtons.get(index).setChecked(true);
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

        // automatically scroll to the position of the currently selected world button
        float r = (float) currentWorldIndex / (NUM_WORLDS - 1);
        scrollPane.setScrollPercentY(r);
    }

    public void update(float dt) {

    }

    public void render(float dt) {
        update(dt);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (renderBatch) {
            stage.getBatch().setProjectionMatrix(stage.getCamera().combined);
            stage.getBatch().begin();

            // fix fading
            if (batchFade) stage.getBatch().setColor(Color.WHITE);

            // render world background corresponding to the selected world
            // possibly expensive scaling call?
            stage.getBatch().draw(rm.worldSelectBackgrounds[0], 0, 0,
                Unlucky.V_WIDTH * 2, Unlucky.V_HEIGHT * 2);

            //game.profile("WorldSelectScreen");

            stage.getBatch().end();
        }

        super.render(dt);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}

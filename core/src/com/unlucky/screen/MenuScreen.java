package com.unlucky.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.unlucky.effects.Moving;
import com.unlucky.main.Unlucky;
import com.unlucky.resource.ResourceManager;

/**
 * The main menu screen of the game that holds all access points for playing,
 * managing the player's inventory, bank, shop, etc, and the settings
 *
 * @author Ming Li
 */
public class MenuScreen extends MenuExtensionScreen {

    // whether to fade or slide in after a transition from another screen
    // 0 - fade in
    // 1 - slide in right
    // 2 - slide in left
    public int transitionIn = 0;

    // title animation (each letter moves down at descending speeds)
    private Moving[] titleMoves;
    private Image[] letters;

    // label style
    private Label.LabelStyle menuStyle;
    private Label battleLabel;

    // play button
    private ImageButton playButton;
    // other buttons
    private ImageButton[] optionButtons;

    private static final int NUM_BUTTONS = 6;

    public MenuScreen(final Unlucky game, final ResourceManager rm) {
        super(game, rm);

        menuStyle = new Label.LabelStyle(rm.pixel10, new Color(79 / 255.f, 79 / 255.f, 117 / 255.f, 1));

        // one for each letter
        titleMoves = new Moving[7];
        letters = new Image[7];
        for (int i = 0; i < 7; i++) {
            titleMoves[i] = new Moving(new Vector2(), new Vector2(), 0);
            letters[i] = new Image(rm.title[i]);
            stage.addActor(letters[i]);
        }

        handlePlayButton();
        handleOptionButtons();

        battleLabel = new Label("Battle", menuStyle);
        battleLabel.setSize(80, 40);
        battleLabel.setFontScale(1.5f);
        battleLabel.setTouchable(Touchable.disabled);
        battleLabel.setAlignment(Align.center);
        battleLabel.setPosition(60, 35);

        stage.addActor(battleLabel);
    }

    @Override
    public void show() {
        game.fps.setPosition(5, 115);
        stage.addActor(game.fps);

        Gdx.input.setInputProcessor(stage);
        renderBatch = false;
        batchFade = true;
        resetTitleAnimation();

        if (transitionIn == 0) {
            // fade in animation
            stage.addAction(Actions.sequence(Actions.alpha(0), Actions.run(new Runnable() {
                @Override
                public void run() {
                    renderBatch = true;
                }
            }), Actions.fadeIn(0.5f)));
        }
        else {
            renderBatch = true;
            // slide in animation
            stage.addAction(Actions.sequence(Actions.moveTo(
                transitionIn == 1 ? Unlucky.V_WIDTH : -Unlucky.V_WIDTH, 0), Actions.moveTo(0, 0, 0.3f)));
        }
    }

    private void handlePlayButton() {
        ImageButton.ImageButtonStyle s = new ImageButton.ImageButtonStyle();
        s.imageUp = new TextureRegionDrawable(rm.playButton[0][0]);
        s.imageDown = new TextureRegionDrawable(rm.playButton[1][0]);
        playButton = new ImageButton(s);
        playButton.setPosition(60, 35);
        stage.addActor(playButton);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setFadeScreen(game.worldSelectScreen);
            }
        });
    }

    private void handleOptionButtons() {
        ImageButton.ImageButtonStyle[] styles = rm.loadImageButtonStyles(NUM_BUTTONS, rm.menuButtons);
        optionButtons = new ImageButton[NUM_BUTTONS];
        for (int i = 0; i < NUM_BUTTONS; i++) {
            optionButtons[i] = new ImageButton(styles[i]);
            optionButtons[i].setSize(20, 20);
            optionButtons[i].getImage().setFillParent(true);
            stage.addActor(optionButtons[i]);
        }
        // inventory button
        optionButtons[0].setPosition(6, 85);
        // settings button
        optionButtons[1].setPosition(171, 85);
        // shop button
        optionButtons[2].setPosition(6, 50);
        // smove button
        optionButtons[3].setPosition(6, 15);
        // statistics button
        optionButtons[4].setPosition(170, 50);
        // credits button
        optionButtons[5].setPosition(170, 15);

        // TODO: ABSTRACT THIS
        optionButtons[4].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setSlideScreen(game.statisticsScreen, true);
            }
        });
    }

    public void update(float dt) {
        for (int i = 0; i < 7; i++) {
            titleMoves[i].update(dt);
            letters[i].setPosition(titleMoves[i].position.x, titleMoves[i].position.y);
        }
        super.update(dt);
    }

    public void render(float dt) {
        update(dt);

        // clear screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (renderBatch) {
            stage.getBatch().setProjectionMatrix(stage.getCamera().combined);
            stage.getBatch().begin();
            // fix fading
            if (batchFade) stage.getBatch().setColor(Color.WHITE);
            super.render(dt);
            stage.getBatch().end();
        }

        stage.act(dt);
        stage.draw();

        //game.profile("MenuScreen");
    }

    /**
     * Resets and starts the title animation on every transition to this screen
     */
    private void resetTitleAnimation() {
        // entire title text starts at x = 74
        for (int i = 0; i < titleMoves.length; i++) {
            titleMoves[i].origin.set(new Vector2(37 + i * 18, 120 + 24));
            titleMoves[i].target.set(new Vector2(37 + i * 18, 120 - 35));
            titleMoves[i].speed = (275 - i * 24) / 2;
            titleMoves[i].horizontal = false;
            titleMoves[i].start();
        }
    }

}

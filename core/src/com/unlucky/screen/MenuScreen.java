package com.unlucky.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.unlucky.parallax.Background;
import com.unlucky.resource.ResourceManager;

import java.awt.*;

/**
 * The main menu screen of the game that holds all access points for playing,
 * managing the player's inventory, bank, shop, etc, and the settings
 *
 * @author Ming Li
 */
public class MenuScreen extends AbstractScreen {

    // title animation (each letter moves down at descending speeds)
    private Moving[] titleMoves;
    private Image[] letters;

    // title screen bg
    private Background[] bg;

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

        // background
        createBackground();

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
        Gdx.input.setInputProcessor(stage);
        renderBatch = false;
        batchFade = true;
        resetTitleAnimation();
        // fade in animation
        stage.addAction(Actions.sequence(Actions.alpha(0), Actions.run(new Runnable() {
            @Override
            public void run() {
                renderBatch = true;
            }
        }), Actions.fadeIn(0.5f)));
    }

    /**
     * Creates the parallax background
     */
    private void createBackground() {
        bg = new Background[3];

        // ordered by depth
        // sky
        bg[0] = new Background(rm.titleScreenBackground[0], (OrthographicCamera) stage.getCamera(), new Vector2(0, 0));
        bg[0].setVector(0, 0);
        // back clouds
        bg[1] = new Background(rm.titleScreenBackground[2], (OrthographicCamera) stage.getCamera(), new Vector2(0.3f, 0));
        bg[1].setVector(20, 0);
        // front clouds
        bg[2] = new Background(rm.titleScreenBackground[1], (OrthographicCamera) stage.getCamera(), new Vector2(0.3f, 0));
        bg[2].setVector(60, 0);
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
                setScreen(game.worldSelectScreen);
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
                setScreen(game.statisticsScreen);
            }
        });
    }

    public void update(float dt) {
        for (int i = 0; i < 7; i++) {
            titleMoves[i].update(dt);
            letters[i].setPosition(titleMoves[i].position.x, titleMoves[i].position.y);
        }

        for (int i = 0; i < bg.length; i++) {
            bg[i].update(dt);
        }
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

            for (int i = 0; i < bg.length; i++) {
                bg[i].render((SpriteBatch) stage.getBatch());
            }
            stage.getBatch().end();
        }

        super.render(dt);
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

    @Override
    public void dispose() {
      super.dispose();
    }

}

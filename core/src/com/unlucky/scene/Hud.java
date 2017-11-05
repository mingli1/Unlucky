package com.unlucky.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.unlucky.animation.AnimationManager;
import com.unlucky.entity.Player;
import com.unlucky.main.Unlucky;
import com.unlucky.resource.ResourceManager;
import com.unlucky.resource.Util;

import java.util.Random;

/**
 * Handles button input and everything not in the game camera
 *
 * @author Ming Li
 */
public class Hud implements Disposable {

    private Random rand;
    private ResourceManager rm;
    private Player player;

    // Scene2D
    public Stage stage;
    private Viewport viewport;

    // Buttons
    // --------------------------------------------------------------------
    // directional pad: index i 0 - down, 1 - up, 2 - right, 3 - left
    private ImageButton[] dirPad;
    // random magnitudes for each direction
    private int[] mags;
    // labels for magnitudes
    private Label[] magLabels;

    public Hud(Player player, SpriteBatch batch, ResourceManager rm) {
        this.player = player;
        this.rm = rm;
        rand = new Random();

        viewport = new ExtendViewport(Unlucky.V_WIDTH, Unlucky.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        Gdx.input.setInputProcessor(stage);

        mags = new int[4];
        shuffleMagnitudes();

        createDirPad();
        createMagLabels();
    }

    /**
     * Sets a random magnitude for each direction
     */
    private void shuffleMagnitudes() {
        for (int i = 0; i < 4; i++) {
            // each magnitude between 1 and 4
            mags[i] = rand.nextInt(4) + 1;
        }
    }

    /**
     * Updates the DPAD numbers after a click
     */
    private void updateMagLabels() {
        for (int i = 0; i < 4; i++) {
            magLabels[i].setText(String.valueOf(mags[i]));
        }
    }

    /**
     * Draws the directional pad and applies Drawable effects
     * Unfortunately have to do each one separately
     */
    private void createDirPad() {
        dirPad = new ImageButton[4];

        // when each button is pressed it changes for a more visible effect
        TextureRegionDrawable downUp = new TextureRegionDrawable(rm.dirpad20x20[0][0]);
        TextureRegionDrawable downDown = new TextureRegionDrawable(rm.dirpad20x20[1][0]);
        TextureRegionDrawable upUp = new TextureRegionDrawable(rm.dirpad20x20[0][1]);
        TextureRegionDrawable upDown = new TextureRegionDrawable(rm.dirpad20x20[1][1]);
        TextureRegionDrawable rightUp = new TextureRegionDrawable(rm.dirpad20x20[0][2]);
        TextureRegionDrawable rightDown = new TextureRegionDrawable(rm.dirpad20x20[1][2]);
        TextureRegionDrawable leftUp = new TextureRegionDrawable(rm.dirpad20x20[0][3]);
        TextureRegionDrawable leftDown = new TextureRegionDrawable(rm.dirpad20x20[1][3]);

        ImageButton.ImageButtonStyle downStyle = new ImageButton.ImageButtonStyle();
        downStyle.imageUp = downUp;
        downStyle.imageDown = downDown;
        ImageButton.ImageButtonStyle upStyle = new ImageButton.ImageButtonStyle();
        upStyle.imageUp = upUp;
        upStyle.imageDown = upDown;
        ImageButton.ImageButtonStyle rightStyle = new ImageButton.ImageButtonStyle();
        rightStyle.imageUp = rightUp;
        rightStyle.imageDown = rightDown;
        ImageButton.ImageButtonStyle leftStyle = new ImageButton.ImageButtonStyle();
        leftStyle.imageUp = leftUp;
        leftStyle.imageDown = leftDown;

        // down
        dirPad[0] = new ImageButton(downStyle);
        dirPad[0].setPosition(Util.DIR_PAD_SIZE + Util.DIR_PAD_OFFSET, Util.DIR_PAD_OFFSET);
        // up
        dirPad[1] = new ImageButton(upStyle);
        dirPad[1].setPosition(Util.DIR_PAD_SIZE + Util.DIR_PAD_OFFSET, (Util.DIR_PAD_SIZE * 2) + Util.DIR_PAD_OFFSET);
        // right
        dirPad[2] = new ImageButton(rightStyle);
        dirPad[2].setPosition((Util.DIR_PAD_SIZE * 2) + Util.DIR_PAD_OFFSET, Util.DIR_PAD_SIZE + Util.DIR_PAD_OFFSET);
        // left
        dirPad[3] = new ImageButton(leftStyle);
        dirPad[3].setPosition(Util.DIR_PAD_OFFSET, Util.DIR_PAD_SIZE + Util.DIR_PAD_OFFSET);

        handleDirPadEvents();

        for (int i = 0; i < dirPad.length; i++) {
            stage.addActor(dirPad[i]);
        }
    }

    /**
     * Draws the labels representing the random magnitudes on the dPad
     */
    private void createMagLabels() {
        magLabels = new Label[4];

        BitmapFont bitmapFont = rm.assetManager.get("arial.ttf", BitmapFont.class);
        Label.LabelStyle font = new Label.LabelStyle(bitmapFont, new Color(0, 0, 0, 255));

        for (int i = 0; i < 4; i++) {
            magLabels[i] = new Label(String.valueOf(mags[i]), font);
            magLabels[i].setSize(Util.DIR_PAD_SIZE, Util.DIR_PAD_SIZE);
            magLabels[i].setAlignment(Align.center);
            magLabels[i].setTouchable(Touchable.disabled);
        }
        magLabels[0].setPosition(Util.DIR_PAD_SIZE + Util.DIR_PAD_OFFSET, Util.DIR_PAD_OFFSET);
        magLabels[1].setPosition(Util.DIR_PAD_SIZE + Util.DIR_PAD_OFFSET, (Util.DIR_PAD_SIZE * 2) + Util.DIR_PAD_OFFSET);
        magLabels[2].setPosition((Util.DIR_PAD_SIZE * 2) + Util.DIR_PAD_OFFSET, Util.DIR_PAD_SIZE + Util.DIR_PAD_OFFSET);
        magLabels[3].setPosition(Util.DIR_PAD_OFFSET, Util.DIR_PAD_SIZE + Util.DIR_PAD_OFFSET);

        for (int i = 0; i < 4; i++) stage.addActor(magLabels[i]);
    }

    /**
     * Handles player movement commands
     */
    private void handleDirPadEvents() {
        dirPad[0].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (player.canMove()) {
                    player.move(0, mags[0]);
                    player.getAm().setAnimation(AnimationManager.DOWN);
                    shuffleMagnitudes();
                    updateMagLabels();
                }
            }
        });
        dirPad[1].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (player.canMove()) {
                    player.move(1, mags[1]);
                    player.getAm().setAnimation(AnimationManager.UP);
                    shuffleMagnitudes();
                    updateMagLabels();
                }
            }
        });
        dirPad[2].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (player.canMove()) {
                    player.move(2, mags[2]);
                    player.getAm().setAnimation(AnimationManager.RIGHT);
                    shuffleMagnitudes();
                    updateMagLabels();
                }
            }
        });
        dirPad[3].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (player.canMove()) {
                    player.move(3, mags[3]);
                    player.getAm().setAnimation(AnimationManager.LEFT);
                    shuffleMagnitudes();
                    updateMagLabels();
                }
            }
        });
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

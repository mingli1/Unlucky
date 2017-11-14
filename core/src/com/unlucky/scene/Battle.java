package com.unlucky.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.unlucky.entity.Enemy;
import com.unlucky.entity.Player;
import com.unlucky.main.Unlucky;
import com.unlucky.resource.ResourceManager;
import com.unlucky.resource.Util;

import java.util.Random;

/**
 * The UI for a battle scene with the Player against some Enemy
 *
 * @author Ming Li
 */
public class Battle {

    private Random rand;
    private ResourceManager rm;
    private Player player;
    private Enemy enemy;

    // Scene2D
    public Stage stage;
    private Viewport viewport;

    // Buttons
    private ImageButton[] moveButtons;

    // Labels
    private Label[] moveNameLabels;
    private Label[] moveDescLabels;

    public Battle(Player player, Enemy enemy, SpriteBatch batch, ResourceManager rm) {
        rand = new Random();

        this.player = player;
        this.enemy = enemy;
        this.rm = rm;

        viewport = new ExtendViewport(Unlucky.V_WIDTH * 2, Unlucky.V_HEIGHT * 2, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        Gdx.input.setInputProcessor(stage);

        createMoveButtons();
        createMoveLabels();
    }

    /**
     * Creates and places the move buttons based on the Player's random moveset
     */
    private void createMoveButtons() {
        moveButtons = new ImageButton[4];

        // load in button textures
        ImageButton.ImageButtonStyle[] styles = rm.loadImageButtonStyles(4, rm.movebutton110x40);

        // Match the Button styles to their Move
        for (int i = 0; i < 4; i++) {
            moveButtons[i] = new ImageButton(styles[player.getMoveset().moveset[i].type]);
        }
        // set positions
        moveButtons[0].setPosition(0, Util.MOVE_HEIGHT);
        moveButtons[1].setPosition(Util.MOVE_WIDTH, Util.MOVE_HEIGHT);
        moveButtons[2].setPosition(0, 0);
        moveButtons[3].setPosition(Util.MOVE_WIDTH, 0);

        for (int i = 0; i < 4; i++) stage.addActor(moveButtons[i]);
    }

    private void createMoveLabels() {
        moveNameLabels = new Label[4];
        moveDescLabels = new Label[4];

        BitmapFont bitmapFont = rm.assetManager.get("arial.ttf", BitmapFont.class);
        Label.LabelStyle font = new Label.LabelStyle(bitmapFont, new Color(0, 0, 255, 255));

        for (int i = 0; i < 4; i++) {
            // move names
            moveNameLabels[i] = new Label(player.getMoveset().names[i], font);
            moveNameLabels[i].setSize(Util.MOVE_WIDTH, Util.MOVE_HEIGHT);
            moveNameLabels[i].setAlignment(Align.topLeft);
            moveNameLabels[i].setFontScale(1.3f);
            moveNameLabels[i].setTouchable(Touchable.disabled);

            moveDescLabels[i] = new Label(player.getMoveset().descriptions[i], font);
            moveDescLabels[i].setSize(Util.MOVE_WIDTH, Util.MOVE_HEIGHT);
            moveDescLabels[i].setAlignment(Align.left);
            moveDescLabels[i].setTouchable(Touchable.disabled);
        }
        moveNameLabels[0].setPosition(15, Util.MOVE_HEIGHT - 10);
        moveNameLabels[1].setPosition(Util.MOVE_WIDTH + 15, Util.MOVE_HEIGHT - 10);
        moveNameLabels[2].setPosition(15, -10);
        moveNameLabels[3].setPosition(Util.MOVE_WIDTH + 15, -10);

        moveDescLabels[0].setPosition(15, Util.MOVE_HEIGHT - 5);
        moveDescLabels[1].setPosition(Util.MOVE_WIDTH + 15, Util.MOVE_HEIGHT - 5);
        moveDescLabels[2].setPosition(15, -5);
        moveDescLabels[3].setPosition(Util.MOVE_WIDTH + 15, -5);

        for (int i = 0; i < 4; i++) {
            stage.addActor(moveNameLabels[i]);
            stage.addActor(moveDescLabels[i]);
        }
    }

}

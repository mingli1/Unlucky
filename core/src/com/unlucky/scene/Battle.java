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
    private ImageButton[] optionButtons;

    // Styles
    private ImageButton.ImageButtonStyle[] moveStyles;
    private ImageButton.ImageButtonStyle[] optionStyles;

    // Labels
    private Label[] moveNameLabels;
    private Label[] moveDescLabels;
    private Label[] optionNameLabels;
    private Label[] optionDescLabels;
    private String[] buffs = { "Distract", "Focus", "Intimidate" };
    private String[] buffDescs = {
            "Next enemy attack\nhas -35% ACC",
            "Next attack has\n100% ACC",
            "Next attack has\n+15% DMG"
    };

    public Battle(Player player, Enemy enemy, SpriteBatch batch, ResourceManager rm) {
        rand = new Random();

        this.player = player;
        this.enemy = enemy;
        this.rm = rm;

        viewport = new ExtendViewport(Unlucky.V_WIDTH * 2, Unlucky.V_HEIGHT * 2, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        Gdx.input.setInputProcessor(stage);

        createMoveUI();
        createOptionUI();
    }

    /**
     * Resets the Move ImageButtons and their Labels whenever a turn ends and new moves are set
     */
    public void resetMoves() {
        player.getMoveset().reset(player.getMinDamage(), player.getMaxDamage(), player.getMaxHp());
        for (int i = 0; i < 4; i++) {
            moveButtons[i].setStyle(moveStyles[player.getMoveset().moveset[i].type]);
            moveNameLabels[i].setText(player.getMoveset().names[i]);
            moveDescLabels[i].setText(player.getMoveset().descriptions[i]);
        }
    }

    /**
     * Creates the UI for the Player's random moveset
     */
    private void createMoveUI() {
        // Buttons
        moveButtons = new ImageButton[4];

        // load in button textures
        moveStyles = rm.loadImageButtonStyles(4, rm.movebutton145x50);

        // Match the Button styles to their Move
        for (int i = 0; i < 4; i++) {
            moveButtons[i] = new ImageButton(moveStyles[player.getMoveset().moveset[i].type]);
        }
        // set positions
        moveButtons[0].setPosition(0, Util.MOVE_HEIGHT);
        moveButtons[1].setPosition(Util.MOVE_WIDTH, Util.MOVE_HEIGHT);
        moveButtons[2].setPosition(0, 0);
        moveButtons[3].setPosition(Util.MOVE_WIDTH, 0);

        // Labels
        moveNameLabels = new Label[4];
        moveDescLabels = new Label[4];

        BitmapFont bitmapFont = rm.pixel10;
        Label.LabelStyle font = new Label.LabelStyle(bitmapFont, new Color(255, 255, 255, 255));

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
        moveNameLabels[0].setPosition(15, Util.MOVE_HEIGHT - 15);
        moveNameLabels[1].setPosition(Util.MOVE_WIDTH + 15, Util.MOVE_HEIGHT - 15);
        moveNameLabels[2].setPosition(15, -15);
        moveNameLabels[3].setPosition(Util.MOVE_WIDTH + 15, -15);

        moveDescLabels[0].setPosition(15, Util.MOVE_HEIGHT - 5);
        moveDescLabels[1].setPosition(Util.MOVE_WIDTH + 15, Util.MOVE_HEIGHT - 5);
        moveDescLabels[2].setPosition(15, -5);
        moveDescLabels[3].setPosition(Util.MOVE_WIDTH + 15, -5);

        for (int i = 0; i < 4; i++) {
            stage.addActor(moveButtons[i]);
            stage.addActor(moveNameLabels[i]);
            stage.addActor(moveDescLabels[i]);
        }
    }

    /**
     * Creates the UI for the Run and ______ buttons
     * Run will have some random low percentage to escape the battle
     * The other option can be one of
     * - Distract: The Enemy's accuracy is decreased by 35% for one turn
     * - Focus: The player's next move is guaranteed to hit
     * - Intimidate: The player's next move has 15% increased damage
     */
    private void createOptionUI() {
        // make buttons
        optionButtons = new ImageButton[2];
        optionStyles = rm.loadImageButtonStyles(1, rm.stdmedbutton110x50);
        for (int i = 0; i < 2; i++) optionButtons[i] = new ImageButton(optionStyles[0]);

        // set pos
        // ____ button
        optionButtons[0].setPosition(2 * Util.MOVE_WIDTH, Util.MOVE_HEIGHT);
        // run button
        optionButtons[1].setPosition(2 * Util.MOVE_WIDTH, 0);

        // make labels
        optionNameLabels = new Label[2];
        optionDescLabels = new Label[2];

        BitmapFont bitmapFont = rm.pixel10;
        Label.LabelStyle font = new Label.LabelStyle(bitmapFont, new Color(255, 255, 255, 255));

        int index = rand.nextInt(3);
        String randBuff = buffs[index];
        String desc =  buffDescs[index];

        optionNameLabels[0] = new Label(randBuff, font);
        optionNameLabels[0].setAlignment(Align.topLeft);
        optionNameLabels[0].setSize(110, 50);
        optionNameLabels[0].setFontScale(1.3f);
        optionNameLabels[0].setTouchable(Touchable.disabled);
        optionNameLabels[0].setPosition(2 * Util.MOVE_WIDTH + 15, Util.MOVE_HEIGHT - 13);

        optionDescLabels[0] = new Label(desc, font);
        optionDescLabels[0].setAlignment(Align.topLeft);
        optionDescLabels[0].setSize(110, 50);
        optionDescLabels[0].setFontScale(0.8f);
        optionDescLabels[0].setTouchable(Touchable.disabled);
        optionDescLabels[0].setPosition(2 * Util.MOVE_WIDTH + 15, Util.MOVE_HEIGHT - 25);

        optionNameLabels[1] = new Label("Run", font);
        optionNameLabels[1].setAlignment(Align.topLeft);
        optionNameLabels[1].setSize(110, 50);
        optionNameLabels[1].setFontScale(1.3f);
        optionNameLabels[1].setTouchable(Touchable.disabled);
        optionNameLabels[1].setPosition(2 * Util.MOVE_WIDTH + 15, -13);

        optionDescLabels[1] = new Label("7% chance to run\nfrom a battle", font);
        optionDescLabels[1].setAlignment(Align.topLeft);
        optionDescLabels[1].setSize(110, 50);
        optionDescLabels[1].setFontScale(0.8f);
        optionDescLabels[1].setTouchable(Touchable.disabled);
        optionDescLabels[1].setPosition(2 * Util.MOVE_WIDTH + 15, -25);

        for (int i = 0; i < 2; i++) {
            stage.addActor(optionButtons[i]);
            stage.addActor(optionNameLabels[i]);
            stage.addActor(optionDescLabels[i]);
        }
    }

}

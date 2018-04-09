package com.unlucky.ui.battleui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.unlucky.battle.Move;
import com.unlucky.battle.SpecialMove;
import com.unlucky.battle.StatusEffect;
import com.unlucky.entity.Player;
import com.unlucky.event.*;
import com.unlucky.map.TileMap;
import com.unlucky.resource.ResourceManager;
import com.unlucky.resource.Util;
import com.unlucky.screen.GameScreen;

/**
 * Creates and handle the random move buttons and two other options in the battle phase
 *
 * @author Ming Li
 */
public class MoveUI extends BattleUI {

    private Stage stage;

    // Buttons
    private ImageButton[] moveButtons;
    private ImageButton[] optionButtons;

    // Styles
    private ImageButton.ImageButtonStyle[] moveStyles;
    private ImageButton.ImageButtonStyle[] optionStyles;
    private ImageButton.ImageButtonStyle[] disabled;

    // Labels
    private Label[] moveNameLabels;
    private Label[] moveDescLabels;
    private Label[] optionNameLabels;
    private Label[] optionDescLabels;
    private boolean[] optionButtonTouchable = new boolean[2];

    // Special moves
    private SpecialMove smove;
    private boolean onCd = false;
    private int turnCounter = 0;

    public MoveUI(GameScreen gameScreen, TileMap tileMap, Player player, Battle battle,
                  com.unlucky.ui.battleui.BattleUIHandler uiHandler, Stage stage, ResourceManager rm) {
        super(gameScreen, tileMap, player, battle, uiHandler, rm);

        this.stage = stage;

        createMoveUI();
        createOptionUI();
    }

    boolean p = false;

    public void update(float dt) {
        // reset and generate new random special move after cooldown
        //if (turnCounter == Util.S_MOVE_CD) {
        if (turnCounter == player.smoveCd) {
            onCd = false;
            turnCounter = 0;
            if(p) resetSpecialMoves();
            p = false;
        }
        else {
            //if (onCd) optionDescLabels[0].setText(Util.S_MOVE_CD - turnCounter + " turn(s) until\n" +
            if (onCd) optionDescLabels[0].setText(player.smoveCd - turnCounter + " turn(s) until\n" +
                    "new special move");
        }
    }

    public void render(float dt) {}

    /**
     * Resetting variables that are only set once the entire battle
     */
    public void init() {
        turnCounter = 0;
        onCd = false;
        optionDescLabels[1].setText("7% chance to run\nfrom a battle");
        for (int i = 0; i < 2; i++) {
            optionButtonTouchable[i] = true;
            optionButtons[i].setStyle(optionStyles[1 - i]);
        }
        resetSpecialMoves();
        resetMoves();
    }

    /**
     * Hides and disables or shows and enables the move button UI
     */
    public void toggleMoveAndOptionUI(boolean toggle) {
        for (int i = 0; i < 4; i++) {
            moveButtons[i].setTouchable(toggle ? Touchable.enabled : Touchable.disabled);
            moveButtons[i].setVisible(toggle);
            moveNameLabels[i].setVisible(toggle);
            moveDescLabels[i].setVisible(toggle);
        }
        for (int i = 0; i < 2; i++) {
            if (optionButtonTouchable[i])
                optionButtons[i].setTouchable(toggle ? Touchable.enabled : Touchable.disabled);
            optionButtons[i].setVisible(toggle);
            optionNameLabels[i].setVisible(toggle);
            optionDescLabels[i].setVisible(toggle);
        }
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
     * Generates new random special move
     */
    private void resetSpecialMoves() {
        smove = player.smoveset.smoveset.random();
        if (smove != null) {
            optionNameLabels[0].setText(smove.name);
            optionDescLabels[0].setText(smove.desc);
            optionButtons[0].setStyle(optionStyles[1]);
            optionButtons[0].setTouchable(Touchable.enabled);
            optionButtonTouchable[0] = true;
        }
        else {
            optionNameLabels[0].setText("NONE");
            optionDescLabels[0].setText("No special\nmoves equipped");
            optionButtons[0].setStyle(disabled[0]);
            optionButtons[0].setTouchable(Touchable.disabled);
            optionButtonTouchable[0] = false;
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
            moveNameLabels[i].setFontScale(1.25f);
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

        handleMoveEvents();
    }

    /**
     * Creates the UI for the Run and ______ buttons
     * Run will have some random low percentage to escape the battle
     */
    private void createOptionUI() {
        // make buttons
        optionButtons = new ImageButton[2];
        disabled = new ImageButton.ImageButtonStyle[2];
        optionStyles = rm.loadImageButtonStyles(2, rm.stdmedbutton110x50);
        for (int i = 0; i < 2; i++) {
            optionButtons[i] = new ImageButton(optionStyles[1 - i]);
            disabled[i] = new ImageButton.ImageButtonStyle();
            disabled[i].imageUp = new TextureRegionDrawable(rm.stdmedbutton110x50[1][1 - i]);
        }

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

        optionNameLabels[0] = new Label("", font);
        optionNameLabels[0].setAlignment(Align.topLeft);
        optionNameLabels[0].setSize(110, 50);
        optionNameLabels[0].setFontScale(1.25f);
        optionNameLabels[0].setTouchable(Touchable.disabled);
        optionNameLabels[0].setPosition(2 * Util.MOVE_WIDTH + 15, Util.MOVE_HEIGHT - 13);

        optionDescLabels[0] = new Label("", font);
        optionDescLabels[0].setAlignment(Align.topLeft);
        optionDescLabels[0].setSize(110, 50);
        optionDescLabels[0].setFontScale(0.7f);
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
        optionDescLabels[1].setFontScale(0.7f);
        optionDescLabels[1].setTouchable(Touchable.disabled);
        optionDescLabels[1].setPosition(2 * Util.MOVE_WIDTH + 15, -25);

        for (int i = 0; i < 2; i++) {
            stage.addActor(optionButtons[i]);
            stage.addActor(optionNameLabels[i]);
            stage.addActor(optionDescLabels[i]);
        }

        handleOptionEvents();
    }

    /**
     * Handles the attack from a move of the player
     */
    private void handleMoveEvents() {
        for (int i = 0; i < 4; i++) {
            final int index = i;
            moveButtons[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // the move the player clicked
                    if (onCd) turnCounter++;
                    // when not on cooldown reset special moves every turn
                    else resetSpecialMoves();

                    Move move = player.getMoveset().moveset[index];
                    uiHandler.currentState = com.unlucky.event.BattleState.DIALOG;
                    uiHandler.moveUI.toggleMoveAndOptionUI(false);
                    // reshuffle moveset for next turn
                    resetMoves();
                    String[] dialog = battle.handleMove(move);
                    uiHandler.battleEventHandler.startDialog(dialog, BattleEvent.PLAYER_TURN, BattleEvent.ENEMY_TURN);
                }
            });
        }
    }

    /**
     * Handles the two options of the player
     * Once used, the options are disabled for the rest of the battle
     */
    private void handleOptionEvents() {
        // buff button
        optionButtons[0].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                uiHandler.currentState = com.unlucky.event.BattleState.DIALOG;
                uiHandler.moveUI.toggleMoveAndOptionUI(false);
                battle.buffs[smove.id] = true;

                uiHandler.battleEventHandler.startDialog(battle.getSpecialMoveDialog(smove.id),
                        BattleEvent.PLAYER_TURN, BattleEvent.PLAYER_TURN);

                // add status icons that should show immediately after dialog
                if (battle.buffs[Util.DISTRACT]) battle.opponent.statusEffects.addEffect(StatusEffect.DISTRACT);
                if (battle.buffs[Util.FOCUS]) player.statusEffects.addEffect(StatusEffect.FOCUS);
                if (battle.buffs[Util.INTIMIDATE]) player.statusEffects.addEffect(StatusEffect.INTIMIDATE);
                if (battle.buffs[Util.REFLECT]) battle.opponent.statusEffects.addEffect(StatusEffect.REFLECT);
                if (battle.buffs[Util.INVERT]) player.statusEffects.addEffect(StatusEffect.INVERT);
                if (battle.buffs[Util.SACRIFICE]) player.statusEffects.addEffect(StatusEffect.SACRIFICE);
                if (battle.buffs[Util.SHIELD]) player.statusEffects.addEffect(StatusEffect.SHIELD);

                // disable button until cooldown over
                onCd = true;
                optionButtons[0].setTouchable(Touchable.disabled);
                optionButtons[0].setStyle(disabled[0]);
                optionNameLabels[0].setText("ON COOLDOWN");
                optionButtonTouchable[0] = false;

                p = true;
            }
        });

        // run button
        optionButtons[1].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                uiHandler.currentState = com.unlucky.event.BattleState.DIALOG;
                uiHandler.moveUI.toggleMoveAndOptionUI(false);
                if (onCd) turnCounter++;
                else resetSpecialMoves();
                // 7% chance to run from the battle
                if (Util.isSuccess(Util.RUN_FROM_BATTLE)) {
                    uiHandler.battleEventHandler.startDialog(new String[]{
                            "You successfully ran from the battle!"
                    }, BattleEvent.PLAYER_TURN, BattleEvent.END_BATTLE);
                } else {
                    uiHandler.battleEventHandler.startDialog(new String[]{
                            "You couldn't run from the battle!"
                    }, BattleEvent.PLAYER_TURN, BattleEvent.ENEMY_TURN);
                }
                optionButtons[1].setTouchable(Touchable.disabled);
                optionButtons[1].setStyle(disabled[1]);
                optionDescLabels[1].setText("cannot run again");
                optionButtonTouchable[1] = false;
            }
        });
    }

}

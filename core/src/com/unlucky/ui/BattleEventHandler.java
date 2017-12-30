package com.unlucky.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.unlucky.entity.Player;
import com.unlucky.event.Battle;
import com.unlucky.event.BattleEvent;
import com.unlucky.event.EventState;
import com.unlucky.map.TileMap;
import com.unlucky.resource.ResourceManager;
import com.unlucky.resource.Util;
import com.unlucky.screen.GameScreen;

/**
 * Renders a dialog box that creates text animations given text
 * Text animations are initiated with arrays of Strings where each element represents
 * one cycle of animation and a transition from one element to the next resets the animation.
 *
 * Handles battle events after text events are finished.
 *
 * @author Ming Li
 */
public class BattleEventHandler extends BattleUI {

    private Stage stage;
    private float stateTime = 0;

    // the ui for displaying text
    // It's an ImageButton so it can be hidden and hold images
    private ImageButton ui;
    // Label for text animation
    private Label textLabel;
    // invisible Label for clicking the window
    private Label clickLabel;

    // text animation
    private String currentText = "";
    private String[] currentDialog = new String[0];
    private int dialogIndex = 0;
    private String[] anim;
    private String resultingText = "";
    private int animIndex = 0;

    private boolean beginCycle = false;
    private boolean endCycle = false;
    private BattleEvent prevEvent = BattleEvent.NONE;
    private BattleEvent nextEvent = BattleEvent.NONE;

    // creates the blinking triangle effect when text is done animating
    private boolean posSwitch = false;
    private float posTime = 0;

    public BattleEventHandler(GameScreen gameScreen, TileMap tileMap, Player player, Battle battle,
                              BattleUIHandler uiHandler, Stage stage, ResourceManager rm) {
        super(gameScreen, tileMap, player, battle, uiHandler, rm);

        this.stage = stage;

        // create main UI
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(rm.dialogBox400x80);
        ui = new ImageButton(style);
        ui.setSize(400, 80);
        ui.setPosition(0, 0);
        ui.setTouchable(Touchable.disabled);

        stage.addActor(ui);

        // create Labels
        BitmapFont bitmapFont = rm.pixel10;
        Label.LabelStyle font = new Label.LabelStyle(bitmapFont, new Color(0, 0, 0, 255));

        textLabel = new Label("", font);
        textLabel.setWrap(true);
        textLabel.setTouchable(Touchable.disabled);
        textLabel.setFontScale(1.8f);
        textLabel.setPosition(16, 12);
        textLabel.setSize(350, 52);
        textLabel.setAlignment(Align.topLeft);
        stage.addActor(textLabel);

        clickLabel = new Label("", font);
        clickLabel.setSize(400, 80);
        clickLabel.setPosition(0, 0);

        clickLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (dialogIndex + 1 == currentDialog.length && endCycle) {
                    // the text animation has run through every element of the text array
                    endDialog();
                    handleBattleEvent(nextEvent);
                }
                // after a cycle of text animation ends, clicking the UI goes to the next cycle
                else if (endCycle && dialogIndex < currentDialog.length) {
                    dialogIndex++;
                    reset();
                    currentText = currentDialog[dialogIndex];
                    anim = currentText.split("");
                    beginCycle = true;
                }
            }
        });
        stage.addActor(clickLabel);
    }

    /**
     * Starts the text animation process given an array of Strings
     * Also takes in a BattleEvent that is called after the dialog is done
     *
     * @param dialog
     * @param next
     */
    public void startDialog(String[] dialog, BattleEvent prev, BattleEvent next) {
        ui.setVisible(true);
        textLabel.setVisible(true);
        clickLabel.setVisible(true);
        clickLabel.setTouchable(Touchable.enabled);

        currentDialog = dialog;
        currentText = currentDialog[0];
        anim = currentText.split("");

        prevEvent = prev;
        nextEvent = next;
        beginCycle = true;
    }

    public void endDialog() {
        reset();
        ui.setVisible(false);
        textLabel.setVisible(false);
        clickLabel.setVisible(false);
        clickLabel.setTouchable(Touchable.disabled);
        dialogIndex = 0;
        currentDialog = new String[0];
    }

    /**
     * Reset all variables
     */
    public void reset() {
        stateTime = 0;
        currentText = "";
        textLabel.setText("");
        resultingText = "";
        animIndex = 0;
        anim = new String[0];
        beginCycle = false;
        endCycle = false;
    }

    public void update(float dt) {
        if (beginCycle) {
            stateTime += dt;

            if (animIndex >= anim.length) endCycle = true;
            // a new character is appended to the animation every TEXT_SPEED delta time
            if (stateTime > Util.TEXT_SPEED && animIndex < anim.length && !endCycle) {
                resultingText += anim[animIndex];
                textLabel.setText(resultingText);
                animIndex++;
                stateTime = 0;
            }
        }
    }

    public void render(float dt) {
        if (endCycle) {
            // blinking indicator
            posTime += dt;
            if (posTime >= 0.5f) {
                posTime = 0;
                posSwitch = !posSwitch;
            }

            gameScreen.getBatch().setProjectionMatrix(stage.getCamera().combined);
            gameScreen.getBatch().begin();
            // render red arrow to show when a text animation cycle is complete
            if (posSwitch) gameScreen.getBatch().draw(rm.redarrow10x9, 365, 20);
            else gameScreen.getBatch().draw(rm.redarrow10x9, 365, 25);
            gameScreen.getBatch().end();
        }
    }

    /**
     * @TODO: Clean this up
     * @param event
     */
    public void handleBattleEvent(BattleEvent event) {
        switch (event) {
            case NONE:
                return;
            case END_BATTLE:
                gameScreen.setCurrentEvent(EventState.TRANSITION);
                gameScreen.transition.start(EventState.BATTLING, EventState.MOVING);
                break;
            case PLAYER_TURN:
                uiHandler.moveUI.toggleMoveAndOptionUI(true);
                uiHandler.currentState = BattleState.MOVE;
                if (prevEvent == BattleEvent.ENEMY_TURN) {
                    // player dead
                    if (player.applyDamage()) {
                        // reset animation
                        battle.opponent.setPrevMoveUsed(-1);
                        battle.opponent.setMoveUsed(-1);

                        uiHandler.moveUI.toggleMoveAndOptionUI(false);
                        uiHandler.currentState = BattleState.DIALOG;
                        // 1% chance for revival after dead
                        if (Util.isSuccess(Util.REVIVAL, player.getRandom())) {
                            startDialog(new String[] {
                                    "You took fatal damage and died!",
                                    "However, it looks like luck was on your side and you revived!"
                            }, BattleEvent.PLAYER_TURN, BattleEvent.PLAYER_TURN);
                            player.setHp(player.getMaxHp());
                            player.setDead(false);
                            return;
                        }
                        else {
                            startDialog(new String[]{
                                    "Oh no, you took too much damage and died!",
                                    "Luckily, this game is still in development," +
                                            " so you can't really die yet."
                            }, BattleEvent.PLAYER_TURN, BattleEvent.END_BATTLE);
                            player.setHp(player.getMaxHp());
                            return;
                        }
                    }
                    battle.opponent.applyHeal();
                }
                break;
            case ENEMY_TURN:
                if (prevEvent == BattleEvent.PLAYER_TURN) {
                    // enemy dead
                    if (battle.opponent.applyDamage()) {
                        // reset animation
                        player.setPrevMoveUsed(-1);
                        player.setMoveUsed(-1);

                        // 1% chance for enemy revival
                        if (Util.isSuccess(Util.REVIVAL, battle.opponent.getRandom())) {
                            startDialog(new String[] {
                                    "The enemy took fatal damage and died!",
                                    "Oh no, it looks like the enemy has been revived!"
                            }, BattleEvent.ENEMY_TURN, BattleEvent.ENEMY_TURN);
                            battle.opponent.setHp(battle.opponent.getMaxHp());
                            battle.opponent.setDead(false);
                            return;
                        }
                        // defeated enemy and gained experience
                        else {
                            int expGained = battle.getBattleExp();

                            // level up occurs
                            if (player.getExp() + expGained >= player.getMaxExp()) {
                                int remainder = (player.getExp() + expGained) - player.getMaxExp();
                                player.levelUp(remainder);
                                startDialog(new String[] {
                                        "You defeated " + battle.opponent.getId() + "!",
                                        "You gained " + expGained + " experience.",
                                        "You leveled up!"
                                }, BattleEvent.ENEMY_TURN, BattleEvent.LEVEL_UP);
                                return;
                            }
                            else {
                                player.addExp(expGained);
                                startDialog(new String[] {
                                        "You defeated " + battle.opponent.getId() + "!",
                                        "You gained " + expGained + " experience."
                                }, BattleEvent.ENEMY_TURN, BattleEvent.END_BATTLE);
                                return;
                            }
                        }
                    }
                    player.applyHeal();
                }
                String[] dialog = battle.enemyTurn();
                startDialog(dialog, BattleEvent.ENEMY_TURN, BattleEvent.PLAYER_TURN);
                break;
            case LEVEL_UP:
                gameScreen.setCurrentEvent(EventState.LEVEL_UP);
                gameScreen.levelUp.start();
                break;
        }
    }

}

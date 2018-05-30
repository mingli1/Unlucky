package com.unlucky.screen.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.unlucky.entity.Player;
import com.unlucky.event.EventState;
import com.unlucky.map.Tile;
import com.unlucky.map.TileMap;
import com.unlucky.resource.ResourceManager;
import com.unlucky.resource.Util;
import com.unlucky.screen.GameScreen;
import com.unlucky.ui.UI;

/**
 * Puts in a dialog box that handles events from the event state.
 * Basically the same as the BattleEventHandler but for map events
 *
 * @author Ming Li
 */
public class DialogScreen extends UI {

    private float stateTime = 0;

    // the ui for displaying text
    private Image ui;
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
    private EventState prevEvent = EventState.NONE;
    private EventState nextEvent = EventState.NONE;

    // creates the blinking triangle effect when text is done animating
    private boolean posSwitch = false;
    private float posTime = 0;

    public DialogScreen(GameScreen gameScreen, TileMap tileMap, Player player, ResourceManager rm) {
        super(gameScreen, tileMap, player, rm);

        // create main UI
        ui = new Image(rm.dialogBox400x80);
        ui.setSize(200, 40);
        ui.setPosition(0, 0);
        ui.setTouchable(Touchable.disabled);

        stage.addActor(ui);

        // create Labels
        BitmapFont bitmapFont = rm.pixel10;
        Label.LabelStyle font = new Label.LabelStyle(bitmapFont, new Color(0, 0, 0, 255));

        textLabel = new Label("", font);
        textLabel.setWrap(true);
        textLabel.setTouchable(Touchable.disabled);
        textLabel.setFontScale(1.7f / 2);
        textLabel.setPosition(8, 6);
        textLabel.setSize(350 / 2, 52 / 2);
        textLabel.setAlignment(Align.topLeft);
        stage.addActor(textLabel);

        clickLabel = new Label("", font);
        clickLabel.setSize(200, 120);
        clickLabel.setPosition(0, 0);

        clickLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (dialogIndex + 1 == currentDialog.length && endCycle) {
                    // the text animation has run through every element of the text array
                    endDialog();
                    handleEvent(nextEvent);
                }
                // after a cycle of text animation ends, clicking the UI goes to the next cycle
                else if (endCycle && dialogIndex < currentDialog.length) {
                    dialogIndex++;
                    reset();
                    currentText = currentDialog[dialogIndex];
                    anim = currentText.split("");
                    beginCycle = true;
                }
                // clicking on the box during a text animation completes it early
                else if (beginCycle && !endCycle) {
                    resultingText = currentText;
                    textLabel.setText(resultingText);
                    beginCycle = false;
                    endCycle = true;
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
    public void startDialog(String[] dialog, EventState prev, EventState next) {
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
        stage.act(dt);
        stage.draw();

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
            if (posSwitch) gameScreen.getBatch().draw(rm.redarrow10x9, 182, 10);
            else gameScreen.getBatch().draw(rm.redarrow10x9, 182, 12);
            gameScreen.getBatch().end();
        }
    }

    public void handleEvent(EventState event) {
        switch (event) {
            case MOVING:
                player.finishTileInteraction();
                TextureRegion none = null;
                gameScreen.gameMap.tileMap.setTile(gameScreen.gameMap.tileMap.toTileCoords(player.getPosition()),
                        new Tile(-1, none, gameScreen.gameMap.tileMap.toTileCoords(player.getPosition())));
                // player died from tile
                if (player.getHp() <= 0) {
                    gameScreen.gameMap.setDeath();
                    gameScreen.die();
                    return;
                }
                gameScreen.setCurrentEvent(EventState.MOVING);
                gameScreen.hud.toggle(true);
                break;
        }
    }

}
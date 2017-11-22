package com.unlucky.scene;

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
import com.badlogic.gdx.utils.Array;
import com.unlucky.resource.ResourceManager;
import com.unlucky.resource.Util;

/**
 * Renders a dialog box that creates text animations given text
 * Text animations are initiated with arrays of Strings where each element represents
 * one cycle of animation and a transition from one element to the next resets the animation.
 *
 * @author Ming Li
 */
public class DialogBox {

    private Stage stage;
    private ResourceManager rm;
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

    public DialogBox(Stage stage, ResourceManager rm) {
        this.stage = stage;
        this.rm = rm;

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
        textLabel.setSize(372, 52);
        textLabel.setAlignment(Align.topLeft);
        stage.addActor(textLabel);

        clickLabel = new Label("", font);
        clickLabel.setSize(400, 80);
        clickLabel.setPosition(0, 0);

        clickLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (dialogIndex + 1 == currentDialog.length) {
                    // the text animation has run through every element of the text array
                    endDialog();
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

    public void startDialog(String[] dialog) {
        ui.setVisible(true);
        textLabel.setVisible(true);

        currentDialog = dialog;
        currentText = currentDialog[dialogIndex];
        anim = currentText.split("");
        beginCycle = true;
    }

    public void endDialog() {
        reset();
        ui.setVisible(false);
        textLabel.setVisible(false);
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
        for (int i = 0; i < anim.length; i++) {
            anim[i] = "";
        }
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

    public void render(float dt, ShapeRenderer shapeRenderer) {
        // any shapes to render?
    }

}

package com.unlucky.scene;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.unlucky.resource.ResourceManager;

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

    // text animation
    private String currentText = "";
    private Array<String> currentDialog = new Array<String>();
    private int dialogIndex = 0;
    private String[] anim;
    private String resultingText = "";
    private int animIndex = 0;

    private boolean beginCycle = false;
    private boolean endCycle = false;

    public DialogBox(Stage stage, ResourceManager rm) {
        this.stage = stage;
        this.rm = rm;

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(rm.dialogBox400x80);
        ui = new ImageButton(style);
        ui.setSize(400, 80);
        ui.setPosition(0, 0);
        ui.setTouchable(Touchable.disabled);

        stage.addActor(ui);
    }

}

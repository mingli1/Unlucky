package com.unlucky.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.unlucky.main.Unlucky;
import com.unlucky.map.Level;
import com.unlucky.resource.ResourceManager;

/**
 * Allows the player to select a level from a world
 * Displays the levels using a scroll pane
 *
 * WorldSelectScreen will always come before this screen and pass world data
 *
 * @author Ming Li
 */
public class LevelSelectScreen extends SelectScreen {

    // the world these levels are in
    private int worldIndex;
    private int numLevels;

    // current level selection
    private int currentLevelIndex;

    public LevelSelectScreen(final Unlucky game, final ResourceManager rm) {
        super(game, rm);

        bannerLabel.setText("SELECT A LEVEL");

        handleExitButton();
        handleEnterButton();
        createScrollPane();
    }

    @Override
    public void show() {
        super.show();

        scrollTable.remove();
        createScrollPane();
    }

    /**
     * To know know what world this screen is in
     *
     * @param worldIndex
     */
    public void setWorldIndex(int worldIndex) {
        this.worldIndex = worldIndex;
    }

    /**
     * To init number of levels when switch from WorldSelect to LevelSelect
     *
     * @param numLevels
     */
    public void setNumLevels(int numLevels) {
        this.numLevels = numLevels;
    }

    protected void handleExitButton() {
        super.handleExitButton(game.worldSelectScreen);
    }

    protected void handleEnterButton() {
        enterButtonGroup.setPosition(228, 8);
        stage.addActor(enterButtonGroup);
    }

    protected void createScrollPane() {
        scrollButtons = new Array<TextButton>();

        nameStyle = new Label.LabelStyle(rm.pixel10, Color.WHITE);
        descStyle = new Label.LabelStyle(rm.pixel10, Color.WHITE);
        buttonSelected = new TextButton.TextButtonStyle();
        buttonSelected.up = new TextureRegionDrawable(rm.skin.getRegion("default-round-down"));

        scrollTable = new Table();
        scrollTable.setFillParent(true);
        stage.addActor(scrollTable);

        selectionContainer = new Table();
        for (int i = 0; i < numLevels; i++) {
            final int index = i;

            // button and label group
            Group g = new Group();
            g.setSize(180, 40);

            final TextButton b = new TextButton("", rm.skin);
            b.getStyle().checked = b.getStyle().down;
            b.getStyle().over = null;
            if (i == 0) b.setChecked(true);
            scrollButtons.add(b);

            // select level
            b.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    currentLevelIndex = index;
                }
            });
            b.setFillParent(true);

            Level l = rm.worlds.get(worldIndex).levels[index];

            Label name = new Label(l.name, nameStyle);
            name.setPosition(10, 24);
            name.setFontScale(1.33f);
            name.setTouchable(Touchable.disabled);
            Label desc = new Label("Average level: " + l.avgLevel, descStyle);
            desc.setPosition(10, 12);
            desc.setTouchable(Touchable.disabled);

            g.addActor(b);
            g.addActor(name);
            g.addActor(desc);

            selectionContainer.add(g).padBottom(8).size(180, 40).row();
        }
        selectionContainer.pack();
        selectionContainer.setTransform(true);
        selectionContainer.setOrigin(selectionContainer.getWidth() / 2,
            selectionContainer.getHeight() / 2);

        scrollPane = new ScrollPane(selectionContainer, rm.skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.layout();
        scrollTable.add(scrollPane).size(210, 202).fill();
        scrollTable.setPosition(-85, -20);
    }

    public void render(float dt) {
        super.render(dt, worldIndex);
    }

}

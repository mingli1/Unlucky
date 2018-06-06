package com.unlucky.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
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
    private int numLevels;

    // current level selection
    private int currentLevelIndex;

    // player stats to be displayed
    private String playerStats;

    public LevelSelectScreen(final Unlucky game, final ResourceManager rm) {
        super(game, rm);

        handleExitButton();
        handleEnterButton();
        createScrollPane();
    }

    @Override
    public void show() {
        super.show();

        bannerLabel.setText(rm.worlds.get(worldIndex).name);
        bannerLabel.setStyle(new Label.LabelStyle(rm.pixel10, new Color(150 / 255.f, 1, 1, 1)));

        playerStats = "Player\n-----------------------------------\n" +
            "LEVEL: " + game.player.getLevel() +
            "\nHP: " + game.player.getHp() + "/" + game.player.getMaxHp() +
            "\nDAMAGE: " + game.player.getMinDamage() + "-" + game.player.getMaxDamage() +
            "\nSPECIAL MOVESET: \n" + game.player.smoveset.toString();

        // the side description will show player stats and level name
        String levelName = rm.worlds.get(worldIndex).levels[rm.worlds.get(worldIndex).currentLevelIndex].name;
        fullDescLabel.setText(levelName + "\n\n" + playerStats);

        scrollTable.remove();
        createScrollPane();

        if (this.worldIndex == game.player.maxWorld) this.currentLevelIndex = game.player.maxLevel;

        // automatically scroll to the position of the currently selected world button
        float r = (float) rm.worlds.get(worldIndex).currentLevelIndex / numLevels;
        scrollPane.setScrollPercentY(r);
    }

    /**
     * To know know what world this screen is in
     *
     * @param worldIndex
     */
    public void setWorld(int worldIndex) {
        this.worldIndex = worldIndex;
        this.numLevels = rm.worlds.get(worldIndex).numLevels;
    }

    protected void handleExitButton() {
        super.handleExitButton(game.worldSelectScreen);
    }

    protected void handleEnterButton() {
        enterButtonGroup.setPosition(114, 4);
        stage.addActor(enterButtonGroup);
        enterButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // if the player's inventory is full give a warning
                if (game.player.inventory.isFull()) {
                    new Dialog("Warning", rm.dialogSkin) {
                        {
                            Label l = new Label("Your inventory is full.\nAre you sure you want to proceed?", rm.dialogSkin);
                            l.setFontScale(0.5f);
                            l.setAlignment(Align.center);
                            text(l);
                            getButtonTable().defaults().width(40);
                            getButtonTable().defaults().height(15);
                            button("Yes", "yes");
                            button("No", "no");
                        }

                        @Override
                        protected void result(Object object) {
                            if (object.equals("yes")) enterGame();
                        }

                    }.show(stage).getTitleLabel().setAlignment(Align.center);
                }
                else {
                    enterGame();
                }
            }
        });
    }

    /**
     * Enters the map with the corresponding world, level key
     */
    private void enterGame() {
        game.gameScreen.init(worldIndex, currentLevelIndex);
        setFadeScreen(game.gameScreen);
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
            g.setSize(90, 20);
            g.setTransform(false);

            Level l = rm.worlds.get(worldIndex).levels[index];

            Label name;
            // on last level (boss level) the name is red
            if (i == numLevels - 1)
                name = new Label(l.name, new Label.LabelStyle(rm.pixel10, new Color(225 / 255.f, 0, 0, 1)));
            else
                name = new Label(l.name, nameStyle);
            name.setPosition(5, 10);
            name.setFontScale(0.66f);
            name.setTouchable(Touchable.disabled);
            Label desc = new Label("Average level: " + l.avgLevel, descStyle);
            desc.setPosition(5, 4);
            desc.setFontScale(0.5f);
            desc.setTouchable(Touchable.disabled);

            final TextButton b = new TextButton("", rm.skin);
            b.getStyle().checked = b.getStyle().down;
            b.getStyle().over = null;
            if (i == rm.worlds.get(worldIndex).currentLevelIndex) b.setChecked(true);

            // only enable the levels the player has defeated
            if (index > game.player.maxLevel) {
                b.setTouchable(Touchable.disabled);
                name.setText("???????????????");
                desc.setText("Average level:  ???");
            }
            else {
                b.setTouchable(Touchable.enabled);
                scrollButtons.add(b);
                name.setText(l.name);
                desc.setText("Average level: " + l.avgLevel);
            }

            // select level
            b.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    currentLevelIndex = index;
                    rm.worlds.get(worldIndex).currentLevelIndex = currentLevelIndex;
                    selectAt(currentLevelIndex);
                    String levelName = rm.worlds.get(worldIndex).levels[currentLevelIndex].name;
                    fullDescLabel.setText(levelName + "\n\n" + playerStats);
                }
            });
            b.setFillParent(true);

            g.addActor(b);
            g.addActor(name);
            g.addActor(desc);

            selectionContainer.add(g).padBottom(4).size(90, 20).row();
        }
        selectionContainer.pack();
        selectionContainer.setTransform(false);
        selectionContainer.setOrigin(selectionContainer.getWidth() / 2,
            selectionContainer.getHeight() / 2);

        scrollPane = new ScrollPane(selectionContainer, rm.skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.layout();
        scrollTable.add(scrollPane).size(112, 101).fill();
        scrollTable.setPosition(-38, -10);
    }

    public void render(float dt) {
        super.render(dt, worldIndex);
    }

}

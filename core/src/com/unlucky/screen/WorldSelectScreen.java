package com.unlucky.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.unlucky.main.Unlucky;
import com.unlucky.map.World;
import com.unlucky.resource.ResourceManager;

/**
 * Allows the player to select the world to battle in
 * Displays options for worlds with a scroll pane and renders an background
 * according to the world
 * For each world, displays the description and level range of the world
 *
 * @author Ming Li
 */
public class WorldSelectScreen extends SelectScreen {

    // worlds that can be accessed
    private static final int WORLDS_ENABLED = 3;

    // current world selection index that determines bg and descriptions
    private int currentWorldIndex = 0;

    public WorldSelectScreen(final Unlucky game, final ResourceManager rm) {
        super(game, rm);

        bannerLabel.setText("SELECT A WORLD");
        fullDescLabel.setText(rm.worlds.get(currentWorldIndex).longDesc);

        handleExitButton();
        handleEnterButton();
        createScrollPane();
    }

    @Override
    public void show() {
        super.show();

        // automatically scroll to the position of the currently selected world button
        float r = (float) currentWorldIndex / (rm.worlds.size - 1);
        scrollPane.setScrollPercentY(r);
    }

    protected void handleExitButton() {
        super.handleExitButton(game.menuScreen);
    }

    protected void handleEnterButton() {
        enterButtonGroup.setPosition(228, 8);
        stage.addActor(enterButtonGroup);
        enterLabel.setText("SELECT");
        enterButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (currentWorldIndex < WORLDS_ENABLED) {
                    batchFade = false;
                    // fade out animation
                    stage.addAction(Actions.sequence(Actions.fadeOut(0.3f),
                        Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            game.levelSelectScreen.setNumLevels(rm.worlds.get(currentWorldIndex).numLevels);
                            game.levelSelectScreen.setWorldIndex(currentWorldIndex);
                            game.setScreen(game.levelSelectScreen);
                        }
                    })));
                }
            }
        });
    }

    protected void createScrollPane() {
        scrollButtons = new Array<TextButton>();

        nameStyle = new Label.LabelStyle(rm.pixel10, new Color(150 / 255.f, 1, 1, 1));
        descStyle = new Label.LabelStyle(rm.pixel10, Color.WHITE);
        buttonSelected = new TextButton.TextButtonStyle();
        buttonSelected.up = new TextureRegionDrawable(rm.skin.getRegion("default-round-down"));

        scrollTable = new Table();
        scrollTable.setFillParent(true);
        stage.addActor(scrollTable);

        selectionContainer = new Table();
        for (int i = 0; i < rm.worlds.size; i++) {
            final int index = i;

            // button and label group
            Group g = new Group();
            g.setSize(180, 60);

            final TextButton b = new TextButton("", rm.skin);
            b.getStyle().checked = b.getStyle().down;
            b.getStyle().over = null;
            if (i == 0) b.setChecked(true);
            scrollButtons.add(b);

            // disable worlds not available
            /*
            if (i > WORLDS_ENABLED - 1) {
                b.setDisabled(true);
                b.setTouchable(Touchable.disabled);
            }
            */

            // select world
            b.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    currentWorldIndex = index;
                    selectAt(currentWorldIndex);
                    fullDescLabel.setText(rm.worlds.get(currentWorldIndex).longDesc);
                }
            });
            b.setFillParent(true);

            Label name = new Label(rm.worlds.get(i).name, nameStyle);
            name.setPosition(10, 40);
            name.setFontScale(1.7f);
            name.setTouchable(Touchable.disabled);
            Label desc = new Label(rm.worlds.get(i).shortDesc, descStyle);
            desc.setPosition(10, 15);
            desc.setTouchable(Touchable.disabled);

            g.addActor(b);
            g.addActor(name);
            g.addActor(desc);

            selectionContainer.add(g).padBottom(8).size(180, 60).row();
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

    /**
     * Selects the button from the scroll pane at a given index
     * and unselects all buttons that are not at the index
     *
     * @param index
     */
    private void selectAt(int index) {
        for (TextButton t : scrollButtons) {
            if (t.isChecked()) t.setChecked(false);
        }
        scrollButtons.get(index).setChecked(true);
    }

    public void render(float dt) {
        super.render(dt, currentWorldIndex);
    }

}

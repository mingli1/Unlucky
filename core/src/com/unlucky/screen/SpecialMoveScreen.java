package com.unlucky.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.unlucky.battle.SpecialMove;
import com.unlucky.battle.SpecialMoveset;
import com.unlucky.entity.Player;
import com.unlucky.main.Unlucky;
import com.unlucky.resource.ResourceManager;
import com.unlucky.resource.Util;
import com.unlucky.ui.smove.SMoveTooltip;

/**
 * Screen for managing the player's special moveset
 * The player can drag and drop unlocked special moves onto their moveset
 *
 * @author Ming Li
 */
public class SpecialMoveScreen extends MenuExtensionScreen {

    private Player player;

    // screen banner
    private Image banner;
    private Label bannerLabel;

    // bg layer
    private Image bg;

    // ui
    private Label.LabelStyle white;
    private Label.LabelStyle red;
    private Label.LabelStyle green;
    private Label.LabelStyle headerStyle;
    private Label turnPrompt;
    // the smove slots to be arranged in a pentagon
    private Image[] slots;
    private Array<Image> garbage;
    private SMoveTooltip tooltip;
    private Image selectedSlot;
    // 0 - add button, 1 - remove button
    private ImageButton[] smoveButtons;
    private Label[] smoveButtonLabels;
    // 0 - enabled, 1 - disabled
    private ImageButton.ImageButtonStyle[] addButtonStyle;
    private ImageButton.ImageButtonStyle[] removeButtonStyle;
    private SpecialMove smoveToAdd = null;
    private int smoveToRemove = -1;
    private Label smoveset;

    // scroll pane
    private Table scrollTable;
    private Table selectionContainer;
    private ScrollPane scrollPane;

    public SpecialMoveScreen(final Unlucky game, final ResourceManager rm) {
        super(game, rm);
        this.player = game.player;

        garbage = new Array<Image>();

        white = new Label.LabelStyle(rm.pixel10, Color.WHITE);
        red = new Label.LabelStyle(rm.pixel10, Color.RED);
        green = new Label.LabelStyle(rm.pixel10, new Color(0, 225 / 255.f, 0, 1));
        headerStyle = new Label.LabelStyle(rm.pixel10, new Color(1, 150 / 255.f, 66 / 255.f, 1));

        // create banner
        banner = new Image(rm.skin, "default-slider");
        banner.setPosition(8, 102);
        banner.setSize(164, 12);
        stage.addActor(banner);

        bannerLabel = new Label("MANAGE SPECIAL MOVES", rm.skin);
        bannerLabel.setStyle(headerStyle);
        bannerLabel.setSize(50, 12);
        bannerLabel.setTouchable(Touchable.disabled);
        bannerLabel.setPosition(14, 102);
        bannerLabel.setAlignment(Align.left);
        stage.addActor(bannerLabel);

        stage.addActor(exitButton);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                unselectSlot();
                smoveButtons[0].setStyle(addButtonStyle[1]);
                smoveButtons[0].setTouchable(Touchable.disabled);
                smoveButtons[1].setStyle(removeButtonStyle[1]);
                smoveButtons[1].setTouchable(Touchable.disabled);
                smoveToRemove = -1;
                smoveToAdd = null;
                game.menuScreen.transitionIn = 1;
                setSlideScreen(game.menuScreen, true);
            }
        });

        // create bg
        bg = new Image(rm.skin, "default-slider");
        bg.setPosition(8, 6);
        bg.setSize(184, 92);
        bg.setTouchable(Touchable.enabled);
        stage.addActor(bg);

        turnPrompt = new Label("", white);
        turnPrompt.setFontScale(0.5f);
        turnPrompt.setPosition(12, 92);
        turnPrompt.setTouchable(Touchable.disabled);
        stage.addActor(turnPrompt);

        // create slots
        slots = new Image[SpecialMoveset.MAX_MOVES];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = new Image(rm.smoveSlots[0]);
            Vector2 pos = getSlotPositions(i);
            slots[i].setPosition(pos.x, pos.y);
            stage.addActor(slots[i]);
        }
        scrollTable = new Table();

        tooltip = new SMoveTooltip(rm.skin, headerStyle);
        stage.addActor(tooltip);
        selectedSlot = new Image(rm.smoveSlots[1]);
        selectedSlot.setVisible(false);
        stage.addActor(selectedSlot);

        bg.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                unselectSlot();
                smoveButtons[1].setStyle(removeButtonStyle[1]);
                smoveButtons[1].setTouchable(Touchable.disabled);
                smoveToRemove = -1;
            }
        });

        smoveset = new Label("Special\nMoveset", headerStyle);
        smoveset.setFontScale(0.5f);
        smoveset.setPosition(130, 40);
        smoveset.setAlignment(Align.center);
        stage.addActor(smoveset);

        createSmoveButtons();
    }

    @Override
    public void show() {
        game.fps.setPosition(2, 2);
        stage.addActor(game.fps);

        super.showSlide(false);
        scrollTable.remove();
        createScrollPane();
        smoveButtons[0].toFront();
        smoveButtons[1].toFront();
        smoveButtonLabels[0].toFront();
        smoveButtonLabels[1].toFront();
        addSmoveActors();
        // update turn cd
        turnPrompt.setText("Special moves can be used in battle every " + player.smoveCd + " turns.");
    }

    /**
     * Creates the two add and remove smove buttons
     */
    private void createSmoveButtons() {
        smoveButtons = new ImageButton[2];
        smoveButtonLabels = new Label[2];
        addButtonStyle = new ImageButton.ImageButtonStyle[2];
        removeButtonStyle = new ImageButton.ImageButtonStyle[2];
        String[] str = new String[] { "ADD", "REMOVE" };

        for (int i = 0; i < 2; i++) {
            addButtonStyle[i] = new ImageButton.ImageButtonStyle();
            removeButtonStyle[i] = new ImageButton.ImageButtonStyle();
            smoveButtonLabels[i] = new Label(str[i], white);
            smoveButtonLabels[i].setFontScale(0.5f);
            smoveButtonLabels[i].setSize(38, 18);
            smoveButtonLabels[i].setAlignment(Align.center);
            smoveButtonLabels[i].setTouchable(Touchable.disabled);
        }
        // enabled add
        addButtonStyle[0].imageUp = new TextureRegionDrawable(rm.smoveButtons[0][0]);
        addButtonStyle[0].imageDown = new TextureRegionDrawable(rm.smoveButtons[1][0]);
        // disabled add
        addButtonStyle[1].imageUp = new TextureRegionDrawable(rm.smoveButtons[2][0]);
        // enabled remove
        removeButtonStyle[0].imageUp = new TextureRegionDrawable(rm.smoveButtons[0][1]);
        removeButtonStyle[0].imageDown = new TextureRegionDrawable(rm.smoveButtons[1][1]);
        // disabled remove
        removeButtonStyle[1].imageUp = new TextureRegionDrawable(rm.smoveButtons[2][1]);

        smoveButtons[0] = new ImageButton(addButtonStyle[1]);
        smoveButtons[1] = new ImageButton(removeButtonStyle[1]);

        smoveButtons[0].setPosition(96, 64);
        smoveButtons[1].setPosition(96, 14);
        smoveButtons[0].setTouchable(Touchable.disabled);
        smoveButtons[1].setTouchable(Touchable.disabled);
        smoveButtonLabels[0].setPosition(94, 64);
        smoveButtonLabels[1].setPosition(98, 14);
        stage.addActor(smoveButtons[0]);
        stage.addActor(smoveButtons[1]);
        stage.addActor(smoveButtonLabels[0]);
        stage.addActor(smoveButtonLabels[1]);

        // add button
        smoveButtons[0].addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                add();
            }
        });

        // remove button
        smoveButtons[1].addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (smoveToRemove != -1) {
                    unselectSlot();
                    smoveButtons[1].setStyle(removeButtonStyle[1]);
                    smoveButtons[1].setTouchable(Touchable.disabled);
                    player.smoveset.remove(smoveToRemove);
                    addSmoveActors();
                }
            }
        });
    }

    /**
     * Adds an smove from the selection to the player's smoveset
     */
    private void add() {
        if (player.smoveset.isFull()) {
            new Dialog("Warning", rm.dialogSkin) {
                {
                    Label l = new Label("Your special moveset is full.", rm.dialogSkin);
                    l.setFontScale(0.5f);
                    l.setAlignment(Align.center);
                    text(l);
                    getButtonTable().defaults().width(40);
                    getButtonTable().defaults().height(15);
                    button("OK", "ok");
                }

                @Override
                protected void result(Object object) {}
            }.show(stage).getTitleLabel().setAlignment(Align.center);
            return;
        }
        if (smoveToAdd != null) {
            // already two of a kind in the set
            if (!player.smoveset.canAdd(smoveToAdd.id)) {
                new Dialog("Warning", rm.dialogSkin) {
                    {
                        Label l = new Label("Cannot have more than\n2 of " + smoveToAdd.name + ".", rm.dialogSkin);
                        l.setFontScale(0.5f);
                        l.setAlignment(Align.center);
                        text(l);
                        getButtonTable().defaults().width(40);
                        getButtonTable().defaults().height(15);
                        button("OK", "ok");
                    }

                    @Override
                    protected void result(Object object) {}
                }.show(stage).getTitleLabel().setAlignment(Align.center);
                return;
            }
            // add smove
            player.smoveset.addSMove(smoveToAdd.id);
            addSmoveActors();
        }
    }

    /**
     * Adds the icons of the currently equipped smoves
     */
    private void addSmoveActors() {
        // clear garbage and remove from stage
        for (Image i : garbage) i.remove();
        garbage.clear();
        Array<SpecialMove> set = player.smoveset.smoveset;
        for (int i = 0; i < set.size; i++) {
            Image icon = new Image(set.get(i).icon.getDrawable());
            garbage.add(icon);
            Vector2 pos = getSlotPositions(i);
            icon.setPosition(pos.x + 1, pos.y + 1);
            addSmoveEvent(icon, i, set.get(i));
            stage.addActor(icon);
        }
    }

    /**
     * Returns the positions of the slot at a given smove index
     *
     * @param index
     * @return
     */
    private Vector2 getSlotPositions(int index) {
        // middle left
        if (index == 0) return new Vector2(116, 40);
        // top
        if (index == 1) return new Vector2(138, 69);
        // top right
        if (index == 2) return new Vector2(170, 56);
        // bottom right
        if (index == 3) return new Vector2(170, 24);
        // bottom left
        else return new Vector2(138, 10);
    }

    /**
     * Shows the smove tooltip and enables the remove button
     * @param icon
     * @param index
     */
    private void addSmoveEvent(final Image icon, final int index, final SpecialMove smove) {
        final Vector2 pos = getSlotPositions(index);
        icon.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                // unselect slots
                if (selectedSlot.isVisible()) {
                    selectedSlot.setVisible(false);
                    tooltip.setVisible(false);
                    smoveButtons[1].setStyle(removeButtonStyle[1]);
                    smoveButtons[1].setTouchable(Touchable.disabled);
                    smoveToRemove = -1;
                }
                else {
                    // show selected slot
                    selectedSlot.setPosition(pos.x, pos.y);
                    selectedSlot.setVisible(true);
                    // show tooltip
                    Vector2 t = getTooltipCoords(pos, index);
                    tooltip.show(smove, t.x, t.y);
                    tooltip.toFront();

                    // enable remove button
                    smoveButtons[1].setStyle(removeButtonStyle[0]);
                    smoveButtons[1].setTouchable(Touchable.enabled);
                    smoveToRemove = index;
                }
            }
        });
    }

    private Vector2 getTooltipCoords(Vector2 pos, int index) {
        Vector2 ret = new Vector2();
        if (index == 0) ret.set(pos.x + 16, pos.y - tooltip.getHeight() / 4);
        if (index == 1 || index == 2) ret.set(pos.x + 10, pos.y - tooltip.getHeight());
        if (index == 3 || index == 4) ret.set(pos.x - 4, pos.y + tooltip.getHeight() / 2 + 2);
        return ret;
    }

    private void unselectSlot() {
        // unselect slots
        if (selectedSlot.isVisible()) {
            selectedSlot.setVisible(false);
            tooltip.setVisible(false);
        }
    }

    /**
     * Creates the scroll pane displaying all the special moves, icons and descs
     */
    private void createScrollPane() {
        scrollTable = new Table();
        scrollTable.setFillParent(true);
        stage.addActor(scrollTable);
        selectionContainer = new Table();

        ButtonGroup bg = new ButtonGroup();
        bg.setMaxCheckCount(1);
        bg.setMinCheckCount(0);

        for (int i = 0; i < Util.SMOVES_ORDER_BY_LVL.length; i++) {
            SpecialMove smove = Util.SMOVES_ORDER_BY_LVL[i];
            Group smoveGroup = new Group();
            smoveGroup.setSize(80, 30);
            smoveGroup.setTransform(false);

            Image frame = new Image(rm.smoveFrame);
            frame.setPosition(3, 7);
            frame.setTouchable(Touchable.disabled);
            Image icon = new Image(smove.icon.getDrawable());
            icon.setPosition(4, 8);
            icon.setTouchable(Touchable.disabled);
            Label name = new Label(smove.name, headerStyle);
            name.setFontScale(0.5f);
            name.setPosition(20, 20);
            name.setTouchable(Touchable.disabled);
            Label desc = new Label(smove.desc, white);
            desc.setFontScale(0.5f);
            desc.setPosition(20, 6);
            desc.setTouchable(Touchable.disabled);
            Label status;
            // green label if unlocked
            if (player.getLevel() >= smove.levelUnlocked) status = new Label("UNLOCKED", green);
            // red label with level to unlock
            else status = new Label("UNLOCKED AT LV." + smove.levelUnlocked, red);
            status.setFontScale(0.5f);
            status.setPosition(20, 3);
            status.setTouchable(Touchable.disabled);

            final TextButton button = new TextButton("", rm.skin);
            button.setFillParent(true);
            button.setTouchable(player.getLevel() >= smove.levelUnlocked ?
                Touchable.enabled : Touchable.disabled);
            bg.add(button);
            addScrollPaneEvents(button, smove);

            smoveGroup.addActor(button);
            smoveGroup.addActor(frame);
            smoveGroup.addActor(icon);
            smoveGroup.addActor(name);
            smoveGroup.addActor(desc);
            smoveGroup.addActor(status);

            selectionContainer.add(smoveGroup).padBottom(2).size(80, 30).row();
        }
        selectionContainer.pack();
        selectionContainer.setTransform(false);
        selectionContainer.setOrigin(selectionContainer.getWidth() / 2,
            selectionContainer.getHeight() / 2);

        scrollPane = new ScrollPane(selectionContainer, rm.skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(true);
        // remove scroll bar
        scrollPane.setupFadeScrollBars(0, 0);
        scrollPane.layout();
        scrollTable.add(scrollPane).size(120, 80).fill();
        scrollTable.setPosition(-48, -12);
    }

    /**
     * Handles button checked events for the smove scroll pane
     * @param button
     * @param smove
     */
    private void addScrollPaneEvents(final TextButton button, final SpecialMove smove) {
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                unselectSlot();
                smoveButtons[1].setStyle(removeButtonStyle[1]);
                smoveButtons[1].setTouchable(Touchable.disabled);
                if (button.isChecked()) {
                    smoveToAdd = smove;
                    smoveButtons[0].setStyle(addButtonStyle[0]);
                    smoveButtons[0].setTouchable(Touchable.enabled);
                }
                else {
                    smoveToAdd = null;
                    smoveButtons[0].setStyle(addButtonStyle[1]);
                    smoveButtons[0].setTouchable(Touchable.disabled);
                }
            }
        });
    }

}

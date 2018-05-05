package com.unlucky.ui.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.unlucky.entity.Player;
import com.unlucky.event.EventState;
import com.unlucky.inventory.Equipment;
import com.unlucky.inventory.Inventory;
import com.unlucky.inventory.Item;
import com.unlucky.main.Unlucky;
import com.unlucky.resource.ResourceManager;
import com.unlucky.resource.Util;
import com.unlucky.ui.MovingImageUI;
import com.unlucky.ui.UI;

/**
 * InventoryUI UI that allows for management of items and equips
 * Also shows player stats
 *
 * @author Ming Li
 */
public class InventoryUI extends UI {

    // whether or not the player is in battle or in the menu screen
    // if in battle, the player cannot enchant, sell, or equip items and can only use potions
    // if in menu, the player can fully access the inventory
    public boolean inMenu;

    private boolean ended = false;
    public boolean renderHealthBars = false;

    // UI
    // dimensions to render the inventory at
    private static final int NUM_COLS = 6;
    // main background ui
    private MovingImageUI ui;
    // exit button
    private ImageButton exitButton;
    // headers
    private Label[] headers;
    private String[] headerStrs = { "STATUS", "EQUIPMENT", "INVENTORY" };
    // stats labels
    // 0 - hp, 1 - dmg, 2 - acc, 3 - exp, 4 - gold
    private Label[] stats;
    // health bar (no need for dynamic one)
    private int maxBarWidth = 62;
    private int hpBarWidth = 0;
    private int expBarWidth = 0;
    // selected slot
    private Image selectedSlot;
    // item tooltip
    private ItemTooltip tooltip;
    // inventory buttons that can be applied to each item
    // 0 - enchant, 1 - sell
    private ImageButton[] invButtons;
    private ImageButton.ImageButtonStyle enabled;
    private ImageButton.ImageButtonStyle disabled;
    private Label[] invButtonLabels;

    // constants
    private static final int SLOT_WIDTH = 16;
    private static final int SLOT_HEIGHT = 16;
    private static final Rectangle EQUIPS_AREA = new Rectangle(11, 11, 69, 57);
    private static final Rectangle INVENTORY_AREA = new Rectangle(90, 14, 96, 64);

    // event handling
    private boolean dragging = false;
    // to differentiate between dragging and clicking
    private int prevX, prevY;
    private boolean itemSelected = false;
    private Item currentItem;

    public InventoryUI(final Unlucky game, Player player, ResourceManager rm) {
        super(game, player, rm);

        ui = new MovingImageUI(rm.inventoryui372x212, new Vector2(200, 7), new Vector2(7, 7), 225.f, 186, 106);
        ui.setTouchable(Touchable.enabled);

        // create exit button
        ImageButton.ImageButtonStyle exitStyle = new ImageButton.ImageButtonStyle();
        exitStyle.imageUp = new TextureRegionDrawable(rm.exitbutton18x18[0][0]);
        exitStyle.imageDown = new TextureRegionDrawable(rm.exitbutton18x18[1][0]);
        exitButton = new ImageButton(exitStyle);
        exitButton.setSize(9, 9);

        // Fonts and Colors
        Label.LabelStyle[] labelColors = new Label.LabelStyle[] {
            new Label.LabelStyle(rm.pixel10, new Color(1, 1, 1, 1)), // white
            new Label.LabelStyle(rm.pixel10, new Color(0, 190 / 255.f, 1, 1)), // blue
            new Label.LabelStyle(rm.pixel10, new Color(1, 212 / 255.f, 0, 1)), // yellow
            new Label.LabelStyle(rm.pixel10, new Color(0, 1, 60 / 255.f, 1)), // green
            new Label.LabelStyle(rm.pixel10, new Color(220 / 255.f, 0, 0, 1)) // red
        };

        // create headers
        headers = new Label[3];
        for (int i = 0; i < headers.length; i++) {
            headers[i] = new Label(headerStrs[i], labelColors[0]);
            headers[i].setSize(62, 4);
            headers[i].setFontScale(0.5f);
            headers[i].setTouchable(Touchable.disabled);
            headers[i].setAlignment(Align.left);
        }

        // create stats
        stats = new Label[5];
        for (int i = 0; i < stats.length; i++) {
            stats[i] = new Label("", labelColors[0]);
            stats[i].setSize(62, 4);
            stats[i].setFontScale(0.5f);
            stats[i].setTouchable(Touchable.disabled);
            stats[i].setAlignment(Align.left);
        }
        stats[0].setStyle(labelColors[3]);
        stats[1].setStyle(labelColors[4]);
        stats[2].setStyle(labelColors[1]);
        stats[3].setStyle(labelColors[2]);
        stats[4].setStyle(labelColors[2]);

        selectedSlot = new Image(rm.selectedslot28x28);
        selectedSlot.setVisible(false);
        tooltip = new ItemTooltip(rm.skin);
        tooltip.setPosition(90, 15);

        enabled = new ImageButton.ImageButtonStyle();
        enabled.imageUp = new TextureRegionDrawable(rm.invbuttons92x28[0][0]);
        enabled.imageDown = new TextureRegionDrawable(rm.invbuttons92x28[1][0]);
        disabled = new ImageButton.ImageButtonStyle();
        disabled.imageUp = new TextureRegionDrawable(rm.invbuttons92x28[2][0]);
        invButtons = new ImageButton[2];
        invButtonLabels = new Label[2];
        String[] texts = { "ENCHANT", "SELL" };
        for (int i = 0; i < 2; i++) {
            invButtons[i] = new ImageButton(disabled);
            invButtons[i].setTouchable(Touchable.disabled);
            invButtonLabels[i] = new Label(texts[i], labelColors[0]);
            invButtonLabels[i].setFontScale(0.5f);
            invButtonLabels[i].setTouchable(Touchable.disabled);
            invButtonLabels[i].setSize(46, 14);
            invButtonLabels[i].setAlignment(Align.center);
        }

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                end();
                if (inMenu) {
                    removeInventoryActors();
                    game.menuScreen.transitionIn = 1;
                    renderHealthBars = false;
                    game.inventoryScreen.setSlideScreen(game.menuScreen, true);
                }
                else {
                    Gdx.input.setInputProcessor(gameScreen.multiplexer);
                }
            }
        });

        handleStageEvents();
        handleInvButtonEvents();
    }

    /**
     * Initializes the type of inventory (menu or in game) and the stage
     * Adds everything to the stage
     *
     * @param inMenu
     * @param s
     */
    public void init(boolean inMenu, Stage s) {
        this.inMenu = inMenu;
        this.gameScreen = game.gameScreen;
        if (inMenu) this.stage = s;

        stage.addActor(ui);
        stage.addActor(exitButton);
        for (int i = 0; i < headers.length; i++) stage.addActor(headers[i]);
        for (int i = 0; i < stats.length; i++) stage.addActor(stats[i]);
        stage.addActor(selectedSlot);
        stage.addActor(tooltip);
        for (int i = 0; i < 2; i++) {
            stage.addActor(invButtons[i]);
            stage.addActor(invButtonLabels[i]);
        }

        if (!inMenu) {
            // reset the stage position after actions
            stage.addAction(Actions.moveTo(0, 0));
            Gdx.input.setInputProcessor(this.stage);
            renderHealthBars = true;
        }
    }

    /**
     * Adds inventory items to the stage
     */
    private void addInventory() {
        for (int i = 0; i < Inventory.NUM_SLOTS; i++) {
            Item item = player.inventory.getItem(i);
            if (item != null) {
                stage.addActor(item.actor);
            }
        }
    }

    /**
     * Adds equips to the stage
     */
    private void addEquips() {
        for (int i = 0; i < Equipment.NUM_SLOTS; i++) {
            Item item = player.equips.getEquipAt(i);
            if (item != null) {
                stage.addActor(item.actor);
            }
        }
    }

    /**
     * Resets the item actors
     */
    private void removeInventoryActors() {
        for (int i = 0; i < Inventory.NUM_SLOTS; i++) {
            Item item = player.inventory.getItem(i);
            if (item != null) {
                item.actor.remove();
            }
        }
        for (int i = 0; i < Equipment.NUM_SLOTS; i++) {
            Item item = player.equips.getEquipAt(i);
            if (item != null) {
                item.actor.remove();
            }
        }
    }

    /**
     * Handles drag and drop events for items
     *
     * Dragging allows changing of item positions and equipping
     * Clicking once on an item brings up its tooltip that displays its stats
     *
     */
    private void handleInventoryEvents() {
        for (int i = 0; i < Inventory.NUM_SLOTS; i++) {
            final Item item = player.inventory.getItem(i);
            if (item != null) {
                addInventoryEvent(item);
            }
        }
    }

    /**
     * Adds inventory events to a given item
     * @param item
     */
    private void addInventoryEvent(final Item item) {
        item.actor.clearListeners();
        item.actor.addListener(new DragListener() {

            @Override
            public void dragStart(InputEvent event, float x, float y, int pointer) {
                // can't allow dragging equips off while in game
                if (inMenu || !item.equipped) {
                    dragging = true;
                    tooltip.hide();
                    unselectItem();

                    // original positions
                    prevX = (int) (item.actor.getX() + item.actor.getWidth() / 2);
                    prevY = (int) (item.actor.getY() + item.actor.getHeight() / 2);

                    item.actor.toFront();
                    selectedSlot.setVisible(false);
                    if (!item.equipped) player.inventory.removeItem(item.index);
                    else player.equips.removeEquip(item.type - 2);
                }
            }

            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                item.actor.moveBy(x - item.actor.getWidth() / 2, y - item.actor.getHeight() / 2);
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                dragging = false;

                selectedSlot.setVisible(false);
                // origin positions
                int ax = (int) (item.actor.getX() + item.actor.getWidth() / 2);
                int ay = (int) (item.actor.getY() + item.actor.getHeight() / 2);

                if (item.equipped && inMenu) {
                    if (INVENTORY_AREA.contains(ax, ay)) {
                        int hi = getHoveredIndex(ax, ay);
                        if (hi == -1)
                            player.equips.addEquip(item);
                        else {
                            if (player.inventory.isFreeSlot(hi)) {
                                player.inventory.addItemAtIndex(item, hi);
                                item.equipped = false;
                                player.unequip(item);
                                updateText();
                            }
                            else {
                                player.equips.addEquip(item);
                            }
                        }
                    }
                    else {
                        player.equips.addEquip(item);
                    }
                }
                else {
                    // dropping into equips slots
                    if (EQUIPS_AREA.contains(ax, ay)) {
                        if (item.type >= 2 && item.type <= 9 && inMenu) {
                            item.equipped = true;
                            player.equip(item);
                            updateText();
                            if (!player.equips.addEquip(item)) {
                                // replace the equip with the item of same type
                                Item swap = player.equips.removeEquip(item.type - 2);
                                swap.equipped = false;
                                player.unequip(swap);
                                player.equips.addEquip(item);
                                player.inventory.addItemAtIndex(swap, item.index);
                                updateText();
                            }
                        } else {
                            player.inventory.addItemAtIndex(item, item.index);
                        }
                    }
                    // dropping into inventory slots
                    else {
                        int hi = getHoveredIndex(ax, ay);

                        if (hi == -1)
                            player.inventory.addItemAtIndex(item, item.index);
                        else {
                            // if dropped into an occupied slot, swap item positions
                            if (!player.inventory.addItemAtIndex(item, hi)) {
                                Item eq = player.inventory.getItem(hi);
                                // dragging an enchant scroll onto an equip
                                if (item.type == 10 && eq.type >= 2 && eq.type <= 9) {
                                    applyEnchantBonus(eq, item);
                                }
                                else {
                                    Item swap = player.inventory.takeItem(hi);
                                    player.inventory.addItemAtIndex(swap, item.index);
                                    player.inventory.addItemAtIndex(item, hi);
                                }
                            }
                        }
                    }
                }
            }

        });

        item.actor.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // original positions
                prevX = (int) (item.actor.getX() + item.actor.getWidth() / 2);
                prevY = (int) (item.actor.getY() + item.actor.getHeight() / 2);

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // new positions
                int ax = (int) (item.actor.getX() + item.actor.getWidth() / 2);
                int ay = (int) (item.actor.getY() + item.actor.getHeight() / 2);
                // a true click and not a drag
                if (prevX == ax && prevY == ay) {
                    // item selected
                    if (selectedSlot.isVisible()) {
                        unselectItem();
                    }
                    else {
                        itemSelected = true;
                        currentItem = item;
                        showSelectedSlot(item);
                        if (inMenu) toggleInventoryButtons(true);
                        tooltip.toFront();
                        Vector2 tpos = getCoords(item);
                        // make sure items at the bottom don't get covered by the tooltip
                        if (tpos.y <= 31)
                            tooltip.show(item, tpos.x + 8, tpos.y + tooltip.getHeight() / 2);
                        else
                            tooltip.show(item, tpos.x + 8, tpos.y - tooltip.getHeight());
                    }
                }
            }

        });

        // handle double clicks for item usage and equip
        item.actor.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (getTapCount() == 2) {
                    tooltip.setVisible(false);
                    // consuming potions
                    if (item.type == 0) {
                        itemSelected = true;
                        currentItem = item;
                        consume();
                    }
                    // equip items with double click
                    else if (item.type >= 2 && item.type <= 9 && inMenu) {
                        unselectItem();
                        selectedSlot.setVisible(false);
                        if (!item.equipped) {
                            item.equipped = true;
                            player.equip(item);
                            player.inventory.removeItem(item.index);
                            updateText();
                            if (!player.equips.addEquip(item)) {
                                // replace the equip with the item of same type
                                Item swap = player.equips.removeEquip(item.type - 2);
                                swap.equipped = false;
                                player.unequip(swap);
                                player.equips.addEquip(item);
                                player.inventory.addItemAtIndex(swap, item.index);
                                updateText();
                            }
                        }
                        // double clicking an equipped item unequips it and places it
                        // in the first open slot if it exists
                        else {
                            player.equips.removeEquip(item.type - 2);
                            if (!player.inventory.addItem(item)) {
                                player.equips.addEquip(item);
                            } else {
                                item.equipped = false;
                                player.unequip(item);
                            }
                            updateText();
                        }
                    }
                }
            }

        });
    }

    /**
     * If the ui is touched while an item is selected, it removes the tooltip
     * and unselects the item
     */
    private void handleStageEvents() {
        ui.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (itemSelected) {
                    unselectItem();
                }
                return true;
            }
        });
    }

    /**
     * Handles enchanting and selling
     */
    private void handleInvButtonEvents() {
        // enchanting
        invButtons[0].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                tooltip.setVisible(false);
                // only equips can be enchanted
                if (currentItem != null && currentItem.type >= 2 && currentItem.type <= 9) {
                    new Dialog("Enchant", rm.dialogSkin) {
                        {
                            Label l = new Label("Enchant " + currentItem.labelName + "\nfor " + currentItem.enchantCost + " g?", rm.dialogSkin);
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
                            if (object.equals("yes")) {
                                enchant();
                            }
                        }

                    }.show(stage).getTitleLabel().setAlignment(Align.center);
                }
            }
        });

        // sell
        invButtons[1].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (currentItem != null) {
                    new Dialog("Sell", rm.dialogSkin) {
                        {
                            Label l = new Label("Are you sure you want\nto sell " + currentItem.labelName + "?", rm.dialogSkin);
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
                            if (object.equals("yes")) {
                                player.addGold(currentItem.sell);
                                player.inventory.items[currentItem.index].actor.remove();
                                player.inventory.removeItem(currentItem.index);
                                unselectItem();
                                updateText();
                            }
                        }

                    }.show(stage).getTitleLabel().setAlignment(Align.center);
                }
            }
        });
    }

    /**
     * Dialog event after dragging an enchant scroll onto an equip
     * @param item bonus to be put on item
     * @param scroll enchant scroll
     */
    private void applyEnchantBonus(final Item item, final Item scroll) {
        new Dialog("Enchant scroll", rm.dialogSkin) {
            {
                Label l = new Label("Use enchant scroll on\n" + item.labelName + "?", rm.dialogSkin);
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
                if (object.equals("yes")) {
                    item.bonusEnchantChance = scroll.eChance;
                    scroll.actor.remove();
                    player.inventory.removeItem(scroll.index);
                }
                else {
                    player.inventory.addItemAtIndex(scroll, scroll.index);
                }
            }
        }.show(stage).getTitleLabel().setAlignment(Align.center);
    }

    /**
     * Handles enchanting events
     */
    private void enchant() {
        if (player.getGold() < currentItem.enchantCost) {
            new Dialog("Cannot enchant", rm.dialogSkin) {
                {
                    Label l = new Label("You do not have enough\ngold to enchant this item.", rm.dialogSkin);
                    l.setFontScale(0.5f);
                    l.setAlignment(Align.center);
                    text(l);
                    getButtonTable().defaults().width(40);
                    getButtonTable().defaults().height(15);
                    button("OK", "next");
                }

                @Override
                protected void result(Object object) {
                    tooltip.setVisible(false);
                }

            }.show(stage).getTitleLabel().setAlignment(Align.center);
            return;
        }
        // transaction
        player.addGold(-currentItem.enchantCost);
        updateText();

        // 50% success plus bonus enchant chance from scroll
        if (Util.isSuccess(Util.ENCHANT + currentItem.bonusEnchantChance)) {
            currentItem.enchant();
            // update item tooltip
            tooltip.updateText(currentItem);
            new Dialog("Success!", rm.dialogSkin) {
                {
                    Label l = new Label("Enchanting succeeded.\nThe item has been upgraded.", rm.dialogSkin);
                    l.setFontScale(0.5f);
                    l.setAlignment(Align.center);
                    text(l);
                    getButtonTable().defaults().width(40);
                    getButtonTable().defaults().height(15);
                    button("OK", "next");
                }

                @Override
                protected void result(Object object) {
                    tooltip.setVisible(true);
                    invButtonLabels[0].setText("ENCHANT FOR\n" + currentItem.enchantCost + " g");
                    invButtonLabels[1].setText("SELL FOR\n" + currentItem.sell + " g");
                }

            }.show(stage).getTitleLabel().setAlignment(Align.center);
        }
        // enchant failed
        else {
            // 40% chance to destroy item
            if (Util.isSuccess(Util.DESTROY_ITEM_IF_FAIL)) {
                new Dialog("Fail!", rm.dialogSkin) {
                    {
                        Label l = new Label("Enchanting failed.\nThe item has been destroyed.", rm.dialogSkin);
                        l.setFontScale(0.5f);
                        l.setAlignment(Align.center);
                        text(l);
                        getButtonTable().defaults().width(40);
                        getButtonTable().defaults().height(15);
                        button("OK", "next");
                    }

                    @Override
                    protected void result(Object object) {
                        player.inventory.items[currentItem.index].actor.remove();
                        player.inventory.removeItem(currentItem.index);
                        unselectItem();
                    }

                }.show(stage).getTitleLabel().setAlignment(Align.center);
            } else {
                new Dialog("Fail!", rm.dialogSkin) {
                    {
                        Label l = new Label("Enchanting failed.\nThe item is intact.", rm.dialogSkin);
                        l.setFontScale(0.5f);
                        l.setAlignment(Align.center);
                        text(l);
                        getButtonTable().defaults().width(40);
                        getButtonTable().defaults().height(15);
                        button("OK", "next");
                    }

                    @Override
                    protected void result(Object object) {
                        tooltip.setVisible(true);
                    }

                }.show(stage).getTitleLabel().setAlignment(Align.center);
            }
        }
    }

    /**
     * Handles consuming potions
     */
    private void consume() {
        new Dialog("Consume", rm.dialogSkin) {
            {
                Label l = new Label("Heal for " +
                    (currentItem.hp < 0 ? (int) ((-currentItem.hp / 100f) * player.getMaxHp()) : currentItem.hp)
                    + " HP\nusing this potion?", rm.dialogSkin);
                if (currentItem.exp > 0) {
                    l.setText("Gain " + (int) ((currentItem.exp / 100f) * player.getMaxExp()) + " EXP\nfrom this potion?");
                }
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
                if (object.equals("yes")) {
                    if (currentItem.hp < 0) player.percentagePotion(-currentItem.hp);
                    else if (currentItem.exp > 0) player.addExp((int) ((currentItem.exp / 100f) * player.getMaxExp()));
                    else player.potion(currentItem.hp);
                    player.inventory.items[currentItem.index].actor.remove();
                    player.inventory.removeItem(currentItem.index);
                    unselectItem();
                    updateText();
                }
            }

        }.show(stage).getTitleLabel().setAlignment(Align.center);
    }

    private void unselectItem() {
        itemSelected = false;
        currentItem = null;
        selectedSlot.setVisible(false);
        toggleInventoryButtons(false);
        tooltip.hide();
    }

    /**
     * Shows the golden highlight around the slot clicked
     *
     * @param item
     */
    private void showSelectedSlot(Item item) {
        Vector2 pos = getCoords(item);
        selectedSlot.setPosition(pos.x, pos.y);
        selectedSlot.setVisible(true);
    }

    /**
     * Returns a Vector2 containing the x y coordinates of the slot at a
     * given index of an item in the inventory or equips.
     *
     * @param item
     * @return
     */
    private Vector2 getCoords(Item item) {
        Vector2 ret = new Vector2();
        if (item.equipped) {
            ret.set(7 + (player.equips.positions[item.type - 2].x - 2),
                7 + (player.equips.positions[item.type - 2].y - 2));
        }
        else {
            int i = item.index;
            int x = i % NUM_COLS;
            int y = i / NUM_COLS;
            ret.set(91 + (x * 16), 63 - (y * 16));
        }
        return ret;
    }

    /**
     * Returns the calculated inventory index the mouse or finger is currently
     * hovering when dragging an item so it can be dropped in the correct location
     * Returns -1 if outside of inventory range
     *
     * @param x
     * @param y
     * @return
     */
    private int getHoveredIndex(int x, int y) {
        for (int i = 0; i < Inventory.NUM_SLOTS; i++) {
            int xx = i % NUM_COLS;
            int yy = i / NUM_COLS;
            if (x >= 90 + (xx * SLOT_WIDTH) && x < 90 + (xx * SLOT_WIDTH) + SLOT_WIDTH &&
                    y >= 57 - (yy * SLOT_HEIGHT) && y < 57 - (yy * SLOT_HEIGHT) + SLOT_HEIGHT)
            {
                return i;
            }
        }
        // outside of inventory range
        return -1;
    }

    /**
     * Initializes the inventoryUI screen
     */
    public void start() {
        if (!inMenu) {
            game.fps.setPosition(2, 2);
            stage.addActor(game.fps);

            // ui slides left to right
            ui.setPosition(200, 7);
            ui.moving.origin.set(200, 7);
            ui.moving.target.set(7, 7);
            ui.start();
        }
        else {
            ui.setPosition(7, 7);
        }

        updateText();
        addInventory();
        addEquips();

        handleInventoryEvents();
    }

    /**
     * Resets all animation variables
     * Activated by the exit button
     */
    public void end() {
        unselectItem();

        if (!inMenu) {
            // ui slides off screen right to left
            ui.moving.target.set(200, 7);
            ui.moving.origin.set(7, 7);
            ui.start();

            ended = true;
        }
    }

    /**
     * Switches back to the next state
     */
    public void next() {
        removeInventoryActors();
        renderHealthBars = false;

        gameScreen.setCurrentEvent(EventState.MOVING);
        gameScreen.hud.toggle(true);
        ended = false;
    }

    /**
     * Updates the player's stats and gold
     */
    private void updateText() {
        // update all text
        headers[0].setText("LV. " + player.getLevel() + " PLAYER");
        stats[0].setText("HP: " + player.getHp() + "/" + player.getMaxHp());
        stats[1].setText("DAMAGE: " + player.getMinDamage() + "-" + player.getMaxDamage());
        stats[2].setText("ACCURACY: " + player.getAccuracy() + "%");
        stats[3].setText("EXP: " + player.getExp() + "/" + player.getMaxExp());
        stats[4].setText("GOLD: " + player.getGold());
    }

    /**
     * Toggles inventory buttons if in battle and an item is selected or unselected
     */
    private void toggleInventoryButtons(boolean toggle) {
        if (toggle) {
            if (!currentItem.equipped) {
                for (int i = 0; i < 2; i++) {
                    invButtons[i].setTouchable(Touchable.enabled);
                    if (currentItem.type < 2 || currentItem.type == 10) {
                        invButtons[0].setTouchable(Touchable.disabled);
                        invButtons[0].setStyle(disabled);
                    }
                    invButtons[i].setStyle(enabled);
                    // add enchant cost of item to button
                    if (currentItem.type >= 2 && currentItem.type <= 9)
                        invButtonLabels[0].setText("ENCHANT FOR\n" + currentItem.enchantCost + " g");
                    // add sell value of item to button
                    invButtonLabels[1].setText("SELL FOR\n" + currentItem.sell + " g");
                }
            }
        } else {
            for (int i = 0; i < 2; i++) {
                invButtons[i].setTouchable(Touchable.disabled);
                invButtons[i].setStyle(disabled);
                invButtonLabels[0].setText("ENCHANT");
                invButtonLabels[1].setText("SELL");
            }
        }
    }

    public void update(float dt) {
        if (!inMenu) ui.update(dt);
        if (ended && ui.getX() == 200) {
            next();
        }
        else {
            // update all positions
            exitButton.setPosition(ui.getX() + 181, ui.getY() + 101);
            headers[0].setPosition(ui.getX() + 8, ui.getY() + 96);
            headers[1].setPosition(ui.getX() + 8, ui.getY() + 55);
            headers[2].setPosition(ui.getX() + 84, ui.getY() + 96);
            stats[0].setPosition(ui.getX() + 8, ui.getY() + 91);
            stats[1].setPosition(ui.getX() + 8, ui.getY() + 74);
            stats[2].setPosition(ui.getX() + 8, ui.getY() + 68);
            stats[3].setPosition(ui.getX() + 8, ui.getY() + 83);
            stats[4].setPosition(ui.getX() + 140, ui.getY() + 96);

            for (int i = 0; i < 2; i++) {
                invButtons[i].setPosition(ui.getX() + 84 + (i * 48), ui.getY() + 74);
                invButtonLabels[i].setPosition(ui.getX() + 84 + (i * 48), ui.getY() + 74);
            }

            if (!dragging) {
                // update inventory positions
                for (int i = 0; i < Inventory.NUM_SLOTS; i++) {
                    Item item = player.inventory.getItem(i);
                    int x = i % NUM_COLS;
                    int y = i / NUM_COLS;
                    if (item != null) {
                        item.actor.setPosition(ui.getX() + 86 + (x * 16), ui.getY() + (58 - (y * 16)));
                    }
                }
                // update equips positions
                for (int i = 0; i < Equipment.NUM_SLOTS; i++) {
                    float x = player.equips.positions[i].x;
                    float y = player.equips.positions[i].y;
                    if (player.equips.getEquipAt(i) != null) {
                        player.equips.getEquipAt(i).actor.setPosition(ui.getX() + x, ui.getY() + y);
                    }
                }
            }
        }

        // update bars
        hpBarWidth = (int) (maxBarWidth / ((float) player.getMaxHp() / player.getHp()));
        expBarWidth = (int) (maxBarWidth / ((float) player.getMaxExp() / player.getExp()));
    }

    public void render(float dt) {
        if (!inMenu) {
            stage.act(dt);
            stage.draw();
        }

        if (renderHealthBars) {
            // draw bars
            shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            // health bar
            shapeRenderer.setColor(60 / 255.f, 60 / 255.f, 60 / 255.f, 1);
            shapeRenderer.rect(ui.getX() + 8, ui.getY() + 88, maxBarWidth, 2);
            shapeRenderer.setColor(0, 225 / 255.f, 0, 1);
            shapeRenderer.rect(ui.getX() + 8, ui.getY() + 89, hpBarWidth, 1);
            shapeRenderer.setColor(0, 175 / 255.f, 0, 1);
            shapeRenderer.rect(ui.getX() + 8, ui.getY() + 88, hpBarWidth, 1);
            // exp bar
            shapeRenderer.setColor(60 / 255.f, 60 / 255.f, 60 / 255.f, 1);
            shapeRenderer.rect(ui.getX() + 8, ui.getY() + 80, maxBarWidth, 2);
            shapeRenderer.setColor(1, 212 / 255.f, 0, 1);
            shapeRenderer.rect(ui.getX() + 8, ui.getY() + 81, expBarWidth, 1);
            shapeRenderer.setColor(200 / 255.f, 170 / 255.f, 0, 1);
            shapeRenderer.rect(ui.getX() + 8, ui.getY() + 80, expBarWidth, 1);
            shapeRenderer.end();
        }
    }

}

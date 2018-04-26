package com.unlucky.ui.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
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
import com.unlucky.map.TileMap;
import com.unlucky.resource.ResourceManager;
import com.unlucky.resource.Util;
import com.unlucky.screen.GameScreen;
import com.unlucky.ui.MovingImageUI;
import com.unlucky.ui.UI;

/**
 * InventoryUI UI that allows for management of items and equips
 * Also shows player stats
 *
 * @author Ming Li
 */
public class InventoryUI extends UI {

    private boolean ended = false;

    // UI
    // main background ui
    private MovingImageUI ui;
    // exit button
    private ImageButton exitButton;
    // headers
    private Label[] headers;
    private String[] headerStrs = { "STATUS", "EQUIPMENT", "INVENTORY" };
    // stats labels
    private Label hp;
    private Label damage;
    private Label accuracy;
    private Label exp;
    private Label gold;
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
    private boolean showTooltipAfterEnchant = false;

    public InventoryUI(GameScreen gameScreen, TileMap tileMap, Player player, ResourceManager rm) {
        super(gameScreen, tileMap, player, rm);

        ui = new MovingImageUI(rm.inventoryui372x212, new Vector2(200, 7), new Vector2(7, 7), 225.f, 186, 106);
        ui.setTouchable(Touchable.enabled);
        stage.addActor(ui);

        // create exit button
        ImageButton.ImageButtonStyle exitStyle = new ImageButton.ImageButtonStyle();
        exitStyle.imageUp = new TextureRegionDrawable(rm.exitbutton18x18[0][0]);
        exitStyle.imageDown = new TextureRegionDrawable(rm.exitbutton18x18[1][0]);
        exitButton = new ImageButton(exitStyle);
        exitButton.setSize(9, 9);
        exitButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               end();
           }
        });
        stage.addActor(exitButton);

        // Fonts and Colors
        BitmapFont font = rm.pixel10;
        Label.LabelStyle stdWhite = new Label.LabelStyle(font, new Color(1, 1, 1, 1));
        Label.LabelStyle blue = new Label.LabelStyle(font, new Color(0, 190 / 255.f, 1, 1));
        Label.LabelStyle yellow = new Label.LabelStyle(font, new Color(1, 212 / 255.f, 0, 1));
        Label.LabelStyle green = new Label.LabelStyle(font, new Color(0, 1, 60 / 255.f, 1));
        Label.LabelStyle red = new Label.LabelStyle(font, new Color(220 / 255.f, 0, 0, 1));

        // create headers
        headers = new Label[3];
        for (int i = 0; i < headers.length; i++) {
            headers[i] = new Label(headerStrs[i], stdWhite);
            headers[i].setSize(62, 4);
            headers[i].setFontScale(0.5f);
            headers[i].setTouchable(Touchable.disabled);
            headers[i].setAlignment(Align.left);
            stage.addActor(headers[i]);
        }

        // create stats
        hp = new Label("", green);
        hp.setSize(62, 4);
        hp.setFontScale(0.5f);
        hp.setTouchable(Touchable.disabled);
        hp.setAlignment(Align.left);
        stage.addActor(hp);

        damage = new Label("", red);
        damage.setSize(62, 4);
        damage.setFontScale(0.5f);
        damage.setTouchable(Touchable.disabled);
        damage.setAlignment(Align.left);
        stage.addActor(damage);

        accuracy = new Label("", blue);
        accuracy.setSize(62, 4);
        accuracy.setFontScale(0.5f);
        accuracy.setTouchable(Touchable.disabled);
        accuracy.setAlignment(Align.left);
        stage.addActor(accuracy);

        exp = new Label("", yellow);
        exp.setSize(62, 4);
        exp.setFontScale(0.5f);
        exp.setTouchable(Touchable.disabled);
        exp.setAlignment(Align.left);
        stage.addActor(exp);

        gold = new Label("", yellow);
        gold.setSize(62, 4);
        gold.setFontScale(0.5f);
        gold.setTouchable(Touchable.disabled);
        gold.setAlignment(Align.left);
        stage.addActor(gold);

        addInventory();
        addEquips();

        selectedSlot = new Image(rm.selectedslot28x28);
        selectedSlot.setVisible(false);
        stage.addActor(selectedSlot);

        tooltip = new ItemTooltip(rm.skin);
        tooltip.setPosition(90, 15);
        stage.addActor(tooltip);

        enabled = new ImageButton.ImageButtonStyle();
        enabled.imageUp = new TextureRegionDrawable(rm.invbuttons92x28[0][0]);
        enabled.imageDown = new TextureRegionDrawable(rm.invbuttons92x28[1][0]);
        disabled = new ImageButton.ImageButtonStyle();
        disabled.imageUp = new TextureRegionDrawable(rm.invbuttons92x28[2][0]);
        createInventoryButtons(stdWhite);

        //handleInventoryEvents();
        handleStageEvents();
        handleInvButtonEvents();
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
     * Creates the enchant and sell buttons
     * Originally disabled and grayed out until an item is selected
     */
    private void createInventoryButtons(Label.LabelStyle textStyle) {
        invButtons = new ImageButton[2];
        invButtonLabels = new Label[2];

        String[] texts = { "ENCHANT", "SELL" };
        for (int i = 0; i < 2; i++) {
            invButtons[i] = new ImageButton(disabled);
            invButtons[i].setTouchable(Touchable.disabled);
            invButtonLabels[i] = new Label(texts[i], textStyle);
            invButtonLabels[i].setFontScale(0.5f);
            invButtonLabels[i].setTouchable(Touchable.disabled);
            invButtonLabels[i].setSize(46, 14);
            invButtonLabels[i].setAlignment(Align.center);
            stage.addActor(invButtons[i]);
            stage.addActor(invButtonLabels[i]);
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
                item.actor.clearListeners();
                item.actor.addListener(new DragListener() {

                    @Override
                    public void dragStart(InputEvent event, float x, float y, int pointer) {
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

                        if (item.equipped) {
                            if (INVENTORY_AREA.contains(ax, ay)) {
                                int hi = getHoveredIndex(ax, ay);
                                if (hi == -1)
                                    player.equips.addEquip(item);
                                else {
                                    if (player.inventory.isFreeSlot(hi)) {
                                        player.inventory.addItemAtIndex(item, hi);
                                        item.equipped = false;
                                        player.unequip(item);
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
                                if (item.type > 1) {
                                    item.equipped = true;
                                    player.equip(item);
                                    if (!player.equips.addEquip(item)) {
                                        // replace the equip with the item of same type
                                        Item swap = player.equips.removeEquip(item.type - 2);
                                        swap.equipped = false;
                                        player.unequip(swap);
                                        player.equips.addEquip(item);
                                        //player.inventory.removeItem(item.index);
                                        player.inventory.addItemAtIndex(swap, item.index);
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
                                        Item swap = player.inventory.takeItem(hi);
                                        player.inventory.addItemAtIndex(swap, item.index);
                                        player.inventory.addItemAtIndex(item, hi);
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
                                tooltip.toFront();
                                Vector2 tpos = getTooltipCoords(item);
                                tooltip.show(item, tpos.x + tooltip.getWidth() / 2, tpos.y - tooltip.getHeight() / 2);
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
                            else if (item.type > 1) {
                                unselectItem();
                                selectedSlot.setVisible(false);
                                if (!item.equipped) {
                                    item.equipped = true;
                                    player.equip(item);
                                    player.inventory.removeItem(item.index);
                                    if (!player.equips.addEquip(item)) {
                                        // replace the equip with the item of same type
                                        Item swap = player.equips.removeEquip(item.type - 2);
                                        swap.equipped = false;
                                        player.unequip(swap);
                                        player.equips.addEquip(item);
                                        player.inventory.addItemAtIndex(swap, item.index);
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
                                }
                            }
                        }
                    }

                });
            }
        }
    }

    /**
     * If the ui is touched while an item is selected, it removes the tooltip
     * and unselects the item
     */
    private void handleStageEvents() {
        ui.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println(x + ", " + y);
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
                if (currentItem != null && currentItem.type > 1) {
                    new Dialog("Enchant", rm.dialogSkin) {
                        {
                            Label l = new Label("Are you sure you want\nto enchant " + currentItem.labelName + "?", rm.dialogSkin);
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
                            }
                        }

                    }.show(stage).getTitleLabel().setAlignment(Align.center);
                }
            }
        });
    }

    /**
     * Handles enchanting events
     */
    private void enchant() {
        // 50% success
        if (Util.isSuccess(Util.ENCHANT)) {
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
                    if (object.equals("next")) showTooltipAfterEnchant = true;
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
                        if (object.equals("next")) {
                            showTooltipAfterEnchant = true;
                        }
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
                Label l = new Label("Heal for " + currentItem.hp + " HP\nusing this potion?", rm.dialogSkin);
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
                    player.potion(currentItem.hp);
                    player.inventory.items[currentItem.index].actor.remove();
                    player.inventory.removeItem(currentItem.index);
                    unselectItem();
                }
            }

        }.show(stage).getTitleLabel().setAlignment(Align.center);
    }

    private void unselectItem() {
        itemSelected = false;
        currentItem = null;
        selectedSlot.setVisible(false);
        invButtonLabels[1].setText("SELL");
        tooltip.hide();
    }

    /**
     * Shows the golden highlight around the slot clicked
     *
     * @param item
     */
    private void showSelectedSlot(Item item) {
        if (item.equipped) {
            selectedSlot.setPosition(7 + (player.equips.positions[item.type - 2].x - 2),
                    7 + (player.equips.positions[item.type - 2].y - 2));
            selectedSlot.setVisible(true);
        }
        else {
            int i = item.index;
            int x = i % Inventory.NUM_COLS;
            int y = i / Inventory.NUM_COLS;
            selectedSlot.setPosition(91 + (x * 16), 63 - (y * 16));
            selectedSlot.setVisible(true);
        }
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
            int xx = i % Inventory.NUM_COLS;
            int yy = i / Inventory.NUM_COLS;
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
     * Returns a Vector2 containing the x y coordinates of the slot at a
     * given index of an item in the inventory or equips.
     *
     * @param item
     * @return
     */
    private Vector2 getTooltipCoords(Item item) {
        Vector2 ret = new Vector2();
        if (item.equipped) {
            ret.set(7 + (player.equips.positions[item.type - 2].x - 2),
                    7 + (player.equips.positions[item.type - 2].y - 2));
        }
        else {
            int i = item.index;
            int x = i % Inventory.NUM_COLS;
            int y = i / Inventory.NUM_COLS;
            ret.set(91 + (x * 16), 63 - (y * 16));
        }
        return ret;
    }

    /**
     * Initializes the inventoryUI screen
     */
    public void start() {
        // ui slides left to right
        ui.moving.origin.set(new Vector2(200, 7));
        ui.moving.target.set(new Vector2(7, 7));
        ui.start();

        exitButton.setDisabled(false);
        exitButton.setTouchable(Touchable.enabled);

        addInventory();
        addEquips();

        handleInventoryEvents();
    }

    /**
     * Resets all animation variables
     * Activated by the exit button
     */
    public void end() {
        itemSelected = false;
        currentItem = null;
        selectedSlot.setVisible(false);
        tooltip.setVisible(false);
        exitButton.setDisabled(true);
        exitButton.setTouchable(Touchable.disabled);

        // ui slides off screen right to left
        ui.moving.target.set(new Vector2(200, 7));
        ui.moving.origin.set(new Vector2(7, 7));
        ui.start();

        ended = true;
    }

    /**
     * Switches back to the next state
     */
    public void next() {
        removeInventoryActors();

        gameScreen.setCurrentEvent(EventState.MOVING);
        gameScreen.hud.toggle(true);
        ended = false;
    }

    public void update(float dt) {
        ui.update(dt);
        if (ended && ui.getX() == 200 && ui.getY() == 7) {
            next();
        }

        // update bars
        hpBarWidth = (int) (maxBarWidth / ((float) player.getMaxHp() / player.getHp()));
        expBarWidth = (int) (maxBarWidth / ((float) player.getMaxExp() / player.getExp()));

        // update all text
        headers[0].setText("LV. " + player.getLevel() + " PLAYER");
        hp.setText("HP: " + player.getHp() + "/" + player.getMaxHp());
        damage.setText("DAMAGE: " + player.getMinDamage() + "-" + player.getMaxDamage());
        accuracy.setText("ACCURACY: " + player.getAccuracy() + "%");
        exp.setText("EXP: " + player.getExp() + "/" + player.getMaxExp());
        gold.setText("GOLD: " + player.getGold());

        // update all positions
        exitButton.setPosition(ui.getX() + 181, ui.getY() + 101);
        headers[0].setPosition(ui.getX() + 8, ui.getY() + 96);
        headers[1].setPosition(ui.getX() + 8, ui.getY() + 55);
        headers[2].setPosition(ui.getX() + 84, ui.getY() + 96);
        hp.setPosition(ui.getX() + 8, ui.getY() + 91);
        damage.setPosition(ui.getX() + 8, ui.getY() + 74);
        accuracy.setPosition(ui.getX() + 8, ui.getY() + 68);
        exp.setPosition(ui.getX() + 8, ui.getY() + 83);
        gold.setPosition(ui.getX() + 140, ui.getY() + 96);

        for (int i = 0; i < 2; i++) {
            invButtons[i].setPosition(ui.getX() + 84 + (i * 48), ui.getY() + 74);
            invButtonLabels[i].setPosition(ui.getX() + 84 + (i * 48), ui.getY() + 74);
        }

        if (!dragging) {
            // update inventory positions
            for (int i = 0; i < Inventory.NUM_SLOTS; i++) {
                Item item = player.inventory.getItem(i);
                int x = i % Inventory.NUM_COLS;
                int y = i / Inventory.NUM_COLS;
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

        // enable inventory buttons when item is selected
        if (itemSelected && !currentItem.equipped) {
            for (int i = 0; i < 2; i++) {
                invButtons[i].setTouchable(Touchable.enabled);
                if (currentItem.type < 2) {
                    invButtons[0].setTouchable(Touchable.disabled);
                    invButtons[0].setStyle(disabled);
                }
                invButtons[i].setStyle(enabled);
                // add sell value of item to button
                invButtonLabels[1].setText("SELL FOR\n" + currentItem.sell + " g");
            }
        } else {
            for (int i = 0; i < 2; i++) {
                invButtons[i].setTouchable(Touchable.disabled);
                invButtons[i].setStyle(disabled);
                invButtonLabels[1].setText("SELL");
            }
        }

        if (showTooltipAfterEnchant) {
            tooltip.setVisible(true);
            showTooltipAfterEnchant = false;
        }
    }

    public void render(float dt) {
        stage.act(dt);
        stage.draw();

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

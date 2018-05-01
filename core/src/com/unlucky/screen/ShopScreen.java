package com.unlucky.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.unlucky.entity.Player;
import com.unlucky.inventory.Inventory;
import com.unlucky.inventory.Item;
import com.unlucky.main.Unlucky;
import com.unlucky.resource.ResourceManager;
import com.unlucky.ui.inventory.ItemTooltip;

/**
 * The screen for the shop UI where the player can buy/sell items
 * The shop has unique items that can't be dropped by monsters and
 * every item is rare or higher
 *
 * @author Ming Li
 */
public class ShopScreen extends MenuExtensionScreen {

    private Player player;

    private static final int NUM_COLS = 4;
    private static final int SLOT_SIZE = 16;

    // ui
    private Image ui;
    // exit button
    private ImageButton exitButton;
    // headers
    private Label[] headers;
    private String[] headerStrs = new String[] { "SHOP", "INVENTORY" };
    private Label gold;

    // 0 - buy, 1 - sell
    private ImageButton[] invButtons;
    private ImageButton.ImageButtonStyle enabled;
    private ImageButton.ImageButtonStyle disabled;
    private Label[] invButtonLabels;

    // inventory ui
    private Image selectedSlot;
    private ItemTooltip tooltip;
    private Item currentItem = null;
    private boolean itemSelected = false;

    public ShopScreen(final Unlucky game, final ResourceManager rm) {
        super(game, rm);
        this.player = game.player;

        ui = new Image(rm.shopui);
        ui.setPosition(4, 4);
        ui.setTouchable(Touchable.enabled);
        stage.addActor(ui);

        // create exit button
        ImageButton.ImageButtonStyle exitStyle = new ImageButton.ImageButtonStyle();
        exitStyle.imageUp = new TextureRegionDrawable(rm.exitbutton18x18[0][0]);
        exitStyle.imageDown = new TextureRegionDrawable(rm.exitbutton18x18[1][0]);
        exitButton = new ImageButton(exitStyle);
        exitButton.setSize(9, 9);
        exitButton.setPosition(188, 108);
        exitButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                removeInventoryActors();
                unselectItem();
                game.menuScreen.transitionIn = 1;
                setSlideScreen(game.menuScreen, true);
            }
        });
        stage.addActor(exitButton);

        headers = new Label[2];
        Label.LabelStyle headerStyle = new Label.LabelStyle(rm.pixel10, Color.WHITE);
        for (int i = 0; i < headers.length; i++) {
            headers[i] = new Label(headerStrs[i], headerStyle);
            headers[i].setFontScale(0.5f);
            headers[i].setPosition(4 + 10 + (i * 109), 105);
            stage.addActor(headers[i]);
        }

        gold = new Label("", new Label.LabelStyle(rm.pixel10, new Color(1, 212 / 255.f, 0, 1)));
        gold.setFontScale(0.5f);
        gold.setPosition(70, 109);
        stage.addActor(gold);

        // inventory ui
        selectedSlot = new Image(rm.selectedslot28x28);
        selectedSlot.setVisible(false);
        stage.addActor(selectedSlot);
        tooltip = new ItemTooltip(rm.skin);
        tooltip.hide();
        stage.addActor(tooltip);

        enabled = new ImageButton.ImageButtonStyle();
        enabled.imageUp = new TextureRegionDrawable(rm.invbuttons92x28[0][0]);
        enabled.imageDown = new TextureRegionDrawable(rm.invbuttons92x28[1][0]);
        disabled = new ImageButton.ImageButtonStyle();
        disabled.imageUp = new TextureRegionDrawable(rm.invbuttons92x28[2][0]);
        invButtons = new ImageButton[2];
        invButtonLabels = new Label[2];
        for (int i = 0; i < 2; i++) {
            invButtons[i] = new ImageButton(disabled);
            invButtons[i].setTouchable(Touchable.disabled);
            invButtons[i].setPosition(13 + (i * 48), 12);
            invButtonLabels[i] = new Label("", headerStyle);
            invButtonLabels[i].setFontScale(0.5f);
            invButtonLabels[i].setTouchable(Touchable.disabled);
            invButtonLabels[i].setSize(46, 14);
            invButtonLabels[i].setAlignment(Align.center);
            invButtonLabels[i].setPosition(13 + (i * 48), 12);
            stage.addActor(invButtons[i]);
            stage.addActor(invButtonLabels[i]);
        }

        handleStageEvents();
    }

    public void show() {
        game.fps.setPosition(2, 2);
        stage.addActor(game.fps);

        super.showSlide(false);
        addInventoryActors();
        handleInventoryEvents();

        // update labels
        gold.setText("GOLD: " + player.getGold());
    }

    private void handleInventoryEvents() {
        for (int i = 0; i < Inventory.NUM_SLOTS; i++) {
            final Item item = player.inventory.getItem(i);
            if (item != null) {
                item.actor.clearListeners();
                item.actor.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        // select item
                        if (selectedSlot.isVisible()) {
                            unselectItem();
                        }
                        else {
                            itemSelected = true;
                            currentItem = item;
                            showSelectedSlot(item);
                            tooltip.toFront();
                            Vector2 tpos = getCoords(item);
                            // make sure items at the bottom don't get covered by the tooltip
                            if (tpos.y <= 42)
                                tooltip.show(item, tpos.x + 8, tpos.y + tooltip.getHeight() / 2);
                            else
                                tooltip.show(item, tpos.x + 8, tpos.y - tooltip.getHeight());
                        }
                    }
                });
            }
        }
    }

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

    private void addInventoryActors() {
        for (int i = 0; i < Inventory.NUM_SLOTS; i++) {
            Item item = player.inventory.getItem(i);
            if (item != null) {
                stage.addActor(item.actor);
            }
        }
    }

    private void removeInventoryActors() {
        for (int i = 0; i < Inventory.NUM_SLOTS; i++) {
            Item item = player.inventory.getItem(i);
            if (item != null) {
                item.actor.remove();
            }
        }
    }

    private void unselectItem() {
        itemSelected = false;
        currentItem = null;
        selectedSlot.setVisible(false);
        tooltip.hide();
    }

    private void showSelectedSlot(Item item) {
        Vector2 pos = getCoords(item);
        selectedSlot.setPosition(pos.x, pos.y);
        selectedSlot.setVisible(true);
    }

    /**
     * Returns a Vector2 containing the x y coordinates of a slot in the inventory
     *
     * @param item
     * @return
     */
    private Vector2 getCoords(Item item) {
        Vector2 ret = new Vector2();

        int i = item.index;
        int x = i % NUM_COLS;
        int y = i / NUM_COLS;
        ret.set(123 + (x * 16), 90 - (y * 16));

        return ret;
    }

    public void update(float dt) {
        // update inventory positions
        for (int i = 0; i < Inventory.NUM_SLOTS; i++) {
            Item item = player.inventory.getItem(i);
            int x = i % NUM_COLS;
            int y = i / NUM_COLS;
            if (item != null) {
                item.actor.setPosition(125 + (x * 16), 92 - (y * 16));
            }
        }
    }

}

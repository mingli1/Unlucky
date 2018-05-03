package com.unlucky.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.unlucky.entity.Player;
import com.unlucky.inventory.Inventory;
import com.unlucky.inventory.Item;
import com.unlucky.inventory.Shop;
import com.unlucky.inventory.ShopItem;
import com.unlucky.main.Unlucky;
import com.unlucky.resource.ResourceManager;
import com.unlucky.resource.Util;
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

    // ui
    private Image ui;
    private Label.LabelStyle whiteStyle;
    private Label.LabelStyle goldStyle;
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

    // shop ui
    // main table
    private Table shopTable;
    private Shop shop;
    private Table[] tabContents;
    private ShopItem currentShopItem = null;

    public ShopScreen(final Unlucky game, final ResourceManager rm) {
        super(game, rm);
        this.player = game.player;

        whiteStyle = new Label.LabelStyle(rm.pixel10, Color.WHITE);

        createInventoryUI();
        createShopUI();

        handleStageEvents();
        handleInvButtonEvents();
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

    private void createInventoryUI() {
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
        for (int i = 0; i < headers.length; i++) {
            headers[i] = new Label(headerStrs[i], whiteStyle);
            headers[i].setFontScale(0.5f);
            headers[i].setPosition(4 + 10 + (i * 109), 105);
            stage.addActor(headers[i]);
        }

        goldStyle = new Label.LabelStyle(rm.pixel10, new Color(1, 212 / 255.f, 0, 1));
        gold = new Label("", goldStyle);
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
            invButtons[i].setPosition(13 + (i * 48), 10);
            invButtonLabels[i] = new Label("", whiteStyle);
            invButtonLabels[i].setFontScale(0.5f);
            invButtonLabels[i].setTouchable(Touchable.disabled);
            invButtonLabels[i].setSize(46, 14);
            invButtonLabels[i].setAlignment(Align.center);
            invButtonLabels[i].setPosition(13 + (i * 48), 10);
            stage.addActor(invButtons[i]);
            stage.addActor(invButtonLabels[i]);
        }
        invButtonLabels[0].setText("BUY");
        invButtonLabels[1].setText("SELL");
    }

    /**
     * Creates the shop UI with a tabbed interface and scroll panes
     */
    private void createShopUI() {
        shop = new Shop(rm);

        shopTable = new Table();
        shopTable.setSize(93, 80);
        shopTable.setPosition(14, 26);

        // create tabs
        HorizontalGroup tabGroup = new HorizontalGroup();
        tabGroup.setTransform(false);
        TextButton.TextButtonStyle tabStyle = new TextButton.TextButtonStyle();
        tabStyle.font = rm.pixel10;
        tabStyle.fontColor = Color.WHITE;
        tabStyle.up = new TextureRegionDrawable(rm.shoptab[1][0]);
        tabStyle.checked = new TextureRegionDrawable(rm.shoptab[0][0]);

        final TextButton[] tabButtons = new TextButton[3];
        String[] tabStrs = new String[] { "POTIONS", "EQUIPS", "ACCS" };
        ButtonGroup tabs = new ButtonGroup();
        tabs.setMinCheckCount(1);
        tabs.setMaxCheckCount(1);
        for (int i = 0; i < 3; i++) {
            tabButtons[i] = new TextButton(tabStrs[i], tabStyle);
            tabButtons[i].getLabel().setFontScale(0.5f);
            tabs.add(tabButtons[i]);
            tabGroup.addActor(tabButtons[i]);
        }
        shopTable.add(tabGroup);
        shopTable.row();

        // tab contents
        Stack content = new Stack();
        createTabContents();
        for (int i = 0; i < 3; i++) {
            content.addActor(tabContents[i]);
        }
        shopTable.add(content).expand().fill();
        tabContents[0].setVisible(true);
        tabContents[1].setVisible(false);
        tabContents[2].setVisible(false);

        // show the correct content for each tab
        for (int i = 0; i < 3; i++) {
            final int index = i;
            tabButtons[i].addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    tabContents[index].setVisible(tabButtons[index].isChecked());
                }
            });
        }

        stage.addActor(shopTable);
    }

    /**
     * Creates the content in the form of item catalogs for each of the tabs
     */
    private void createTabContents() {
        ButtonGroup itemButtonGroup = new ButtonGroup();
        itemButtonGroup.setMinCheckCount(0);
        itemButtonGroup.setMaxCheckCount(1);

        tabContents = new Table[3];
        for (int i = 0; i < 3; i++) {
            tabContents[i] = new Table();
            tabContents[i].setFillParent(true);
            Table selectionContainer = new Table();

            for (int j = 0; j < shop.items.get(i).size; j++) {
                Group itemGroup = new Group();
                itemGroup.setTransform(false);
                Table itemTable = new Table();
                itemTable.setFillParent(true);

                final TextButton b = new TextButton("", rm.skin);
                b.setFillParent(true);
                itemButtonGroup.add(b);

                final ShopItem item = shop.items.get(i).get(j);
                item.actor.setPosition(10, 3);
                item.actor.setTouchable(Touchable.disabled);
                Label itemName = new Label(item.labelName, Util.getItemColor(item.rarity, rm));
                itemName.setFontScale(0.5f);
                itemName.setTouchable(Touchable.disabled);
                itemName.setAlignment(Align.left);
                Label itemDesc = new Label(item.getFullDesc(), whiteStyle);
                itemDesc.setWrap(true);
                itemDesc.setFontScale(0.5f);
                itemDesc.setTouchable(Touchable.disabled);
                itemDesc.setAlignment(Align.left);
                Label itemPrice = new Label("PRICE: " + item.price, goldStyle);
                itemPrice.setFontScale(0.5f);
                itemPrice.setTouchable(Touchable.disabled);
                itemPrice.setAlignment(Align.left);

                itemTable.add(itemName).size(68, 8).padBottom(4).padTop(-4).padLeft(12).row();
                itemTable.add(itemDesc).size(68, itemDesc.getPrefHeight()).padLeft(12).row();
                itemTable.add(itemPrice).padTop(4).padBottom(-4).padLeft(12).size(68, 8);

                itemGroup.addActor(b);
                itemGroup.addActor(item.actor);
                itemGroup.addActor(itemTable);

                // handle item selection
                b.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        if (b.isChecked()) {
                            currentShopItem = item;
                            // enable buying
                            invButtons[0].setTouchable(Touchable.enabled);
                            invButtons[0].setStyle(enabled);
                            invButtonLabels[0].setText("BUY FOR\n" + item.price + " g");
                        }
                        else {
                            currentShopItem = null;
                            invButtons[0].setTouchable(Touchable.disabled);
                            invButtons[0].setStyle(disabled);
                            invButtonLabels[0].setText("BUY");
                        }
                    }
                });

                int height = (int) (itemTable.getPrefHeight() + itemTable.getPrefHeight() / 2);
                item.actor.setPosition(4, height / 2 - 5);

                selectionContainer.add(itemGroup).padLeft(-1).padBottom(2).size(89, height).row();
            }
            selectionContainer.pack();
            selectionContainer.setTransform(false);
            selectionContainer.setOrigin(selectionContainer.getWidth() / 2,
                selectionContainer.getHeight() / 2);

            ScrollPane scrollPane = new ScrollPane(selectionContainer, rm.skin);
            scrollPane.setScrollingDisabled(true, false);
            scrollPane.setFadeScrollBars(true);
            //scrollPane.setupFadeScrollBars(0, 0);
            scrollPane.layout();
            tabContents[i].add(scrollPane).size(108, 66).fill();
        }
    }

    private void handleInventoryEvents() {
        for (int i = 0; i < Inventory.NUM_SLOTS; i++) {
            final Item item = player.inventory.getItem(i);
            if (item != null) {
                addInventoryEvent(item);
            }
        }
    }

    /**
     * Adds the necessary events to a given item
     * @param i
     */
    private void addInventoryEvent(Item i) {
        final Item item = i;
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
                    // enable selling
                    invButtons[1].setTouchable(Touchable.enabled);
                    invButtons[1].setStyle(enabled);
                    invButtonLabels[1].setText("SELL FOR\n" + item.sell + " g");
                }
            }
        });
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

    private void handleInvButtonEvents() {
        // buy
        invButtons[0].addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                buy();
            }
        });
        // sell
        invButtons[1].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sell();
            }
        });
    }

    private void buy() {
        if (player.inventory.isFull()) {
            new Dialog("Warning", rm.dialogSkin) {
                {
                    Label l = new Label("Your inventory is full.", rm.dialogSkin);
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
        if (currentShopItem != null) {
            // item is too expensive to buy
            if (player.getGold() < currentShopItem.price) {
                new Dialog("Warning", rm.dialogSkin) {
                    {
                        Label l = new Label("You do not have enough\ngold to buy " + currentShopItem.labelName + ".", rm.dialogSkin);
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
            new Dialog("BUY", rm.dialogSkin) {
                {
                    Label l = new Label("Are you sure you want\nto buy " + currentShopItem.labelName + "?", rm.dialogSkin);
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
                        player.addGold(-currentShopItem.price);
                        // a copy of the shop item
                        Item item;
                        if (currentShopItem.type == 0) {
                            item = new Item(rm, currentShopItem.name, currentShopItem.desc, currentShopItem.rarity,
                                currentShopItem.imgIndex, currentShopItem.hp, currentShopItem.exp, currentShopItem.sell);
                        }
                        else {
                            item = new Item(rm, currentShopItem.name, currentShopItem.desc, currentShopItem.type, currentShopItem.rarity,
                                currentShopItem.imgIndex, currentShopItem.mhp, currentShopItem.dmg, currentShopItem.acc, currentShopItem.sell);
                        }
                        player.inventory.addItem(item);
                        stage.addActor(item.actor);
                        addInventoryEvent(item);
                        gold.setText("GOLD: " + player.getGold());
                        new Dialog("Success", rm.dialogSkin) {
                            {
                                Label l = new Label("You successfully\npurchased " + currentShopItem.labelName + ".", rm.dialogSkin);
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
                    }
                }

            }.show(stage).getTitleLabel().setAlignment(Align.center);
        }
    }

    private void sell() {
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
                        gold.setText("GOLD: " + player.getGold());
                    }
                }

            }.show(stage).getTitleLabel().setAlignment(Align.center);
        }
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
        invButtons[1].setTouchable(Touchable.disabled);
        invButtons[1].setStyle(disabled);
        invButtonLabels[1].setText("SELL");
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

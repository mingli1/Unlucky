package com.unlucky.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.unlucky.inventory.Item;

/**
 * Window UI that acts as a tooltip for an item displaying its stats
 * The window should adjust its size according to how much information an item has
 *
 * @author Ming Li
 */
public class ItemTooltip extends Window {

    private Skin skin;
    private Label desc;

    // label styles for different color names based on item rarity
    private Label.LabelStyle common;
    private Label.LabelStyle rare;
    private Label.LabelStyle epic;
    private Label.LabelStyle legendary;

    public ItemTooltip(Skin skin) {
        super("", skin);
        this.skin = skin;
        desc = new Label("", skin);

        common = new Label.LabelStyle(skin.getFont("default-font"), new Color(1, 1, 1, 1));
        rare = new Label.LabelStyle(skin.getFont("default-font"), new Color(0, 200 / 255.f, 0, 1));
        epic = new Label.LabelStyle(skin.getFont("default-font"), new Color(0, 180 / 255.f, 1, 1));
        legendary = new Label.LabelStyle(skin.getFont("default-font"), new Color(164 / 255.f, 80 / 255.f, 1, 1));

        left();
        add(desc);
        pack();
        this.setVisible(false);
        this.setMovable(false);
        this.setOrigin(Align.bottomLeft);
    }

    /**
     * Sets the title color to the item's rarity and sets the descriptions
     * common - white
     * rare - green
     * epic - blue
     * legendary - purple
     *
     * @param item
     * @param x
     * @param y
     */
    public void show(Item item, float x, float y) {
        this.setPosition(x, y);
        this.setVisible(true);
        updateText(item);
    }

    public void updateText(Item item) {
        switch (item.rarity) {
            case 0:
                this.getTitleLabel().setStyle(common);
                break;
            case 1:
                this.getTitleLabel().setStyle(rare);
                break;
            case 2:
                this.getTitleLabel().setStyle(epic);
                break;
            case 3:
                this.getTitleLabel().setStyle(legendary);
                break;
        }
        this.getTitleLabel().setText(item.labelName);
        desc.setText(item.getFullDesc());
        pack();
    }

    public void hide() {
        this.setVisible(false);
    }

}

package com.unlucky.screen;

import com.unlucky.main.Unlucky;
import com.unlucky.resource.ResourceManager;

/**
 * Screen that shows the player's inventory, equips, and stats
 * and allows the player to enchant, sell, and manage items.
 *
 * @author Ming Li
 */
public class InventoryScreen extends MenuExtensionScreen {

    public InventoryScreen(final Unlucky game, final ResourceManager rm) {
        super(game, rm);
    }

    @Override
    public void show() {
        game.fps.setPosition(2, 2);
        stage.addActor(game.fps);

        super.showSlide(false);
        game.inventoryUI.init(true, this.stage);
        game.inventoryUI.start();
    }

    public void update(float dt) {
        game.inventoryUI.update(dt);
    }

    public void render(float dt) {
        super.render(dt);
        if (renderBatch) game.inventoryUI.render(dt);
    }

}

package com.unlucky.ui.battleui;

import com.unlucky.entity.Player;
import com.unlucky.event.Battle;
import com.unlucky.map.TileMap;
import com.unlucky.resource.ResourceManager;
import com.unlucky.screen.GameScreen;
import com.unlucky.ui.UI;

/**
 * Superclass for all UI related to battle events
 *
 * @author Ming Li
 */
public abstract class BattleUI extends UI {

    protected Battle battle;
    protected BattleUIHandler uiHandler;

    public BattleUI(GameScreen gameScreen, TileMap tileMap, Player player, Battle battle,
                    BattleUIHandler uiHandler, ResourceManager rm) {
        super(gameScreen, tileMap, player, rm);
        this.battle = battle;
        this.uiHandler = uiHandler;
    }

}

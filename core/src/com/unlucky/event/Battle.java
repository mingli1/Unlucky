package com.unlucky.event;

import com.unlucky.entity.Enemy;
import com.unlucky.entity.Player;
import com.unlucky.map.TileMap;
import com.unlucky.screen.GameScreen;

/**
 * Strings together battle events and manages calculations
 *
 * @author Ming Li
 */
public class Battle {

    // the enemy the player is facing
    public Enemy opponent;

    private GameScreen gameScreen;
    private TileMap tileMap;
    private Player player;

    public Battle(GameScreen gameScreen, TileMap tileMap, Player player) {
        this.gameScreen = gameScreen;
        this.tileMap = tileMap;
        this.player = player;
    }

    public void update(float dt) {

    }

    public void render(float dt) {

    }

    /**
     * Sets and scales the enemy's stats according to how strong the player is
     *
     * @param opponent
     */
    public void begin(Enemy opponent) {
        this.opponent = opponent;
    }

    /**
     * Returns back to the map state
     */
    public void end() {
        opponent = null;
        tileMap.removeEntity(tileMap.toTileCoords(player.getPosition()));
        player.finishBattling();
        gameScreen.setCurrentEvent(EventState.MOVING);
    }

}

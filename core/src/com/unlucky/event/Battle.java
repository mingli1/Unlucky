package com.unlucky.event;

import com.unlucky.entity.Enemy;
import com.unlucky.entity.Player;
import com.unlucky.map.TileMap;
import com.unlucky.resource.Util;
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
     * If the enemy is a boss, then its stats are between 1.3-1.8x higher
     * @TODO Scale enemies based on level of map
     * @TODO exp and gold calculations
     *
     * @param opponent
     */
    public void begin(Enemy opponent) {
        this.opponent = opponent;

        float bossMultiplier = (player.getRandom().nextFloat() * 0.5f) + 1.3f;

        // choose damage num from player's damage range
        int seed = Util.getRandomValue(player.getMinDamage(), player.getMaxDamage(), player.getRandom());
        // an enemy will take around 3-6 hits to defeat
        // maxHp = rand(3, 6) * seed
        int multiplier = Util.getRandomValue(3, 6, player.getRandom());
        opponent.setMaxHp(opponent.isBoss() ? (int) bossMultiplier * (seed * multiplier) : seed * multiplier);

        // the estimated num of hits for an enemy to kill the player, between 8 and 12 scaled for move damage
        int numHitsToKill = Util.getRandomValue(8, 12, player.getRandom());
        int dmgSeed = player.getMaxHp() / numHitsToKill;
        // enemy's damage range is scaled to dmgSeed +- 4-6x dmgSeed
        int sigma = dmgSeed / Util.getRandomValue(4, 6, player.getRandom());
        opponent.setMinDamage(opponent.isBoss() ? (int) bossMultiplier * (dmgSeed - sigma) : dmgSeed - sigma);
        opponent.setMaxDamage(opponent.isBoss() ? (int) bossMultiplier * (dmgSeed + sigma) : dmgSeed + sigma);
    }

    /**
     * Returns back to the map state
     */
    public void end() {
        opponent = null;
        tileMap.removeEntity(tileMap.toTileCoords(player.getPosition()));
        player.finishBattling();
        gameScreen.setCurrentEvent(EventState.MOVING);
        gameScreen.hud.toggle(true);
    }

}

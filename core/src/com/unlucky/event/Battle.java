package com.unlucky.event;

import com.unlucky.battle.Move;
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
        this.opponent.setMaxHp(opponent.isBoss() ? (int) bossMultiplier * (seed * multiplier) : seed * multiplier);

        // the estimated num of hits for an enemy to kill the player, between 8 and 12 scaled for move damage
        int numHitsToKill = Util.getRandomValue(8, 12, player.getRandom());
        int dmgSeed = player.getMaxHp() / numHitsToKill;
        // enemy's damage range is scaled to dmgSeed +- 4-6x dmgSeed
        int sigma = dmgSeed / Util.getRandomValue(4, 6, player.getRandom());
        this.opponent.setMinDamage(opponent.isBoss() ? (int) bossMultiplier * (dmgSeed - sigma) : dmgSeed - sigma);
        this.opponent.setMaxDamage(opponent.isBoss() ? (int) bossMultiplier * (dmgSeed + sigma) : dmgSeed + sigma);
    }

    /**
     * Handles and applies the damage/heal of a move to an Entity
     *
     * @param move
     * @return a string array for the dialog ui description
     */
    public String[] handleMove(Move move) {
        String[] dialog = null;

        // accounting for player accuracy
        if (Util.isSuccess(player.getAccuracy(), player.getRandom())) {
            // accurate or wide
            if (move.type < 2) {
                int damage = Util.getRandomValue(Math.round(move.minDamage), Math.round(move.maxDamage), player.getRandom());
                opponent.hit(damage);
                dialog = new String[] {
                        "You used " + move.name + "!",
                        "It did " + damage + " damage to " + opponent.getId() + "."
                };
            }
            // crit (3x damage if success)
            else if (move.type == 2) {
                int damage = Math.round(move.minDamage);
                if (Util.isSuccess(move.crit, player.getRandom())) {
                    damage *= 3;
                    opponent.hit(damage);
                    dialog = new String[] {
                            "You used " + move.name + "!",
                            "It's a critical strike!",
                            "It did " + damage + " damage to " + opponent.getId() + "."
                    };
                } else {
                    opponent.hit(damage);
                    dialog = new String[] {
                            "You used " + move.name + "!",
                            "It did " + damage + " damage to " + opponent.getId() + "."
                    };
                }
            }
            // heal
            else if (move.type == 3) {
                int heal = Util.getRandomValue(Math.round(move.minHeal), Math.round(move.maxHeal), player.getRandom());
                player.heal(heal);
                dialog = new String[] {
                        "You used " + move.name + "!",
                        "You healed for " + heal + " health points."
                };
            }
        }
        else {
            // move missed; enemy turn
            dialog = new String[] {"Oh no, your attack missed!"};
        }

        return dialog;
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

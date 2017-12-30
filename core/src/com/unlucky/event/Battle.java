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

    public void update(float dt) {}

    public void render(float dt) {}

    /**
     * Sets and scales the enemy's stats according to its level
     * If the enemy is an elite, then its stats are between 1.3-1.8x higher
     * @TODO Scale enemies based on level of map
     * @TODO exp and gold calculations
     * @TODO CHANGE ALL THESE CALCULATIONS TO SCALE OFF ENEMY LEVEL
     *
     * @param opponent
     */
    public void begin(Enemy opponent) {
        this.opponent = opponent;

        // TEMPORARY: set opponent level at around player's level

        // set to at or above player's level by 3
        opponent.setLevel(Util.getRandomValue(player.getLevel(), player.getLevel() + 3, opponent.getRandom()));

        float eliteMultiplier = (player.getRandom().nextFloat() *
                (Util.MAX_ELITE_MULTIPLIER - Util.MIN_ELITE_MULTIPLIER)) + Util.MIN_ELITE_MULTIPLIER;

        // choose damage num from player's damage range
        int seed = Util.getRandomValue(player.getMinDamage(), player.getMaxDamage(), player.getRandom());
        // an enemy will take around 3-6 hits to defeat
        // maxHp = rand(3, 6) * seed
        int multiplier = Util.getRandomValue(Util.MIN_ENEMY_HP_SCALING, Util.MAX_ENEMY_HP_SCALING, player.getRandom());
        this.opponent.setMaxHp(opponent.isElite() ? (int) (eliteMultiplier * (seed * multiplier)) : seed * multiplier);

        // the estimated num of hits for an enemy to kill the player, between 8 and 12 scaled for move damage
        int numHitsToKill = Util.getRandomValue(Util.MIN_ENEMY_DMG_SCALING, Util.MAX_ENEMY_DMG_SCALING, player.getRandom());
        int dmgSeed = player.getMaxHp() / numHitsToKill;
        // enemy's damage range is scaled to dmgSeed +- 4-6x dmgSeed
        int sigma = dmgSeed / Util.getRandomValue(4, 6, player.getRandom());
        this.opponent.setMinDamage(opponent.isElite() ? (int) (eliteMultiplier * (dmgSeed - sigma)) : dmgSeed - sigma);
        this.opponent.setMaxDamage(opponent.isElite() ? (int) (eliteMultiplier * (dmgSeed + sigma)) : dmgSeed + sigma);

        // TEMPORARY BOSS SCALING
        if (opponent.isBoss()) {
            opponent.setMaxHp(opponent.getMaxHp() * 3);
            opponent.setMinDamage(opponent.getMinDamage() * 3);
            opponent.setMaxDamage(opponent.getMaxDamage() * 3);
        }
    }

    /**
     * Handles and applies the damage/heal of a move to an Entity
     *
     * @param move
     * @return a string array for the dialog ui description
     */
    public String[] handleMove(Move move, boolean[] options) {
        String[] dialog = null;

        // distract/enemy debuff
        if (options[0]) opponent.setAccuracy(Util.ENEMY_ACCURACY - Util.P_DISTRACT);
        else opponent.setAccuracy(Util.ENEMY_ACCURACY);

        // accounting for player accuracy or accuracy buff
        if (Util.isSuccess(player.getAccuracy(), player.getRandom()) || options[1]) {
            player.useMove(move.type);
            // accurate or wide
            if (move.type < 2) {
                int damage = Util.getRandomValue(Math.round(move.minDamage), Math.round(move.maxDamage), player.getRandom());
                if (options[2]) damage *= Util.INTIMIDATE_MULT;
                opponent.hit(damage);
                dialog = new String[] {
                        "You used " + move.name + "!",
                        "It did " + damage + " damage to " + opponent.getId() + "."
                };
            }
            // crit (3x damage if success)
            else if (move.type == 2) {
                int damage = Math.round(move.minDamage);
                if (options[2]) damage *= Util.INTIMIDATE_MULT;
                if (Util.isSuccess(move.crit, player.getRandom())) {
                    damage *= Util.CRIT_MULTIPLIER;
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
            dialog = new String[] {"Oh no, your move missed!"};
        }

        // buffs used up so reset
        for (int i = 0; i < options.length; i++) options[i] = false;

        return dialog;
    }

    /**
     * Enemy picks a random move out of its random moveset
     *
     * @return the dialog of the enemy's move and damage
     */
    public String[] enemyTurn() {
        // get special boss moves
        if (opponent.isBoss()) {
            opponent.getMoveset().reset(opponent.getMinDamage(), opponent.getMaxDamage(), opponent.getMaxHp(), opponent.getBossIndex());
        }
        else {
            opponent.getMoveset().reset(opponent.getMinDamage(), opponent.getMaxDamage(), opponent.getMaxHp());
        }
        String[] dialog = null;
        Move move = opponent.getMoveset().moveset[opponent.getRandom().nextInt(4)];

        if (Util.isSuccess(opponent.getAccuracy(), opponent.getRandom())) {
            opponent.useMove(move.type);
            // accurate or wide
            if (move.type < 2) {
                int damage = Util.getRandomValue(Math.round(move.minDamage), Math.round(move.maxDamage), opponent.getRandom());
                player.hit(damage);
                dialog = new String[] {
                        opponent.getId() + " used " + move.name + "!",
                        "It did " + damage + " damage to you."
                };
            }
            // crit (3x damage if success)
            else if (move.type == 2) {
                int damage = Math.round(move.minDamage);
                if (Util.isSuccess(move.crit, opponent.getRandom())) {
                    damage *= Util.CRIT_MULTIPLIER;
                    player.hit(damage);
                    dialog = new String[] {
                            opponent.getId() + " used " + move.name + "!",
                            "It's a critical strike!",
                            "It did " + damage + " damage to you."
                    };
                } else {
                    player.hit(damage);
                    dialog = new String[] {
                            opponent.getId() + " used " + move.name + "!",
                            "It did " + damage + " damage to you."
                    };
                }
            }
            // heal
            else if (move.type == 3) {
                int heal = Util.getRandomValue(Math.round(move.minHeal), Math.round(move.maxHeal), opponent.getRandom());
                opponent.heal(heal);
                dialog = new String[] {
                        opponent.getId() + " used " + move.name + "!",
                        opponent.getId() + " healed for " + heal + " health points."
                };
            }
        }
        else {
            dialog = new String[] { opponent.getId() + "'s move missed!"};
        }

        return dialog;
    }

    /**
     * 1-3 extra exp from enemy to balance exp growth
     * Elite monsters give 1.5x exp and bosses give 3x exp
     *
     * @return
     */
    public int getBattleExp() {
        if (opponent.isElite())
            return (int) (1.5 * Util.calculateExpEarned(opponent.getLevel(), opponent.getRandom().nextInt(3) + 1));
        else if (opponent.isBoss())
            return (3 * Util.calculateExpEarned(opponent.getLevel(), opponent.getRandom().nextInt(3) + 1));
        else
            return Util.calculateExpEarned(opponent.getLevel(), opponent.getRandom().nextInt(3) + 1);
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

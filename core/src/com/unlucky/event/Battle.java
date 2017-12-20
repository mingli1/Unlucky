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
     * Sets and scales the enemy's stats according to how strong the player is
     * If the enemy is a boss, then its stats are between 1.3-1.8x higher
     * @TODO Scale enemies based on level of map
     * @TODO exp and gold calculations
     *
     * @param opponent
     */
    public void begin(Enemy opponent) {
        this.opponent = opponent;

        float bossMultiplier = (player.getRandom().nextFloat() *
                (Util.MAX_BOSS_MULTIPLIER - Util.MIN_BOSS_MULTIPLIER)) + Util.MIN_BOSS_MULTIPLIER;
        System.out.println(bossMultiplier);

        // choose damage num from player's damage range
        int seed = Util.getRandomValue(player.getMinDamage(), player.getMaxDamage(), player.getRandom());
        // an enemy will take around 3-6 hits to defeat
        // maxHp = rand(3, 6) * seed
        int multiplier = Util.getRandomValue(Util.MIN_ENEMY_HP_SCALING, Util.MAX_ENEMY_HP_SCALING, player.getRandom());
        this.opponent.setMaxHp(opponent.isBoss() ? (int) (bossMultiplier * (seed * multiplier)) : seed * multiplier);
        System.out.println("max hp with boss: " + (int) (bossMultiplier * (seed * multiplier)));
        System.out.println("max hp without: " + seed * multiplier);

        // the estimated num of hits for an enemy to kill the player, between 8 and 12 scaled for move damage
        int numHitsToKill = Util.getRandomValue(Util.MIN_ENEMY_DMG_SCALING, Util.MAX_ENEMY_DMG_SCALING, player.getRandom());
        int dmgSeed = player.getMaxHp() / numHitsToKill;
        // enemy's damage range is scaled to dmgSeed +- 4-6x dmgSeed
        int sigma = dmgSeed / Util.getRandomValue(4, 6, player.getRandom());
        this.opponent.setMinDamage(opponent.isBoss() ? (int) (bossMultiplier * (dmgSeed - sigma)) : dmgSeed - sigma);
        this.opponent.setMaxDamage(opponent.isBoss() ? (int) (bossMultiplier * (dmgSeed + sigma)) : dmgSeed + sigma);
        System.out.println("max dmg with boss: " + (int) (bossMultiplier * (dmgSeed + sigma)));
        System.out.println("max dmg without: " + (dmgSeed + sigma));
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
        opponent.getMoveset().reset(opponent.getMinDamage(), opponent.getMaxDamage(), opponent.getMaxHp());
        String[] dialog = null;
        Move move = opponent.getMoveset().moveset[opponent.getRandom().nextInt(4)];

        if (Util.isSuccess(opponent.getAccuracy(), opponent.getRandom())) {
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

package com.unlucky.resource;

/**
 * Stores data for game statistics
 * (battle, map, etc)
 *
 * @author Ming Li
 */
public class Statistics {

    // Player statistics

    // total amount of exp the player has gained
    public int cumulativeExp;
    // total amount of hp lost
    public int damageTaken;
    // total amount of healing done by player
    public int hpHealed;
    // total amount of gold the player has gained
    public int cumulativeGold;
    // max amount of gold the player has had at a time
    public MaxStat maxGold;
    // total num of successful enchantments the player
    public int numEnchants;
    // number of deaths
    public int numDeaths;

    // Map statistics

    // number of steps the player has moved in all maps
    public int numSteps;
    // number of dungeons successfully defeated
    public int numDungeonsWon;
    // max score in a single dungeon
    public MaxStat maxDungeonScore;

    // Battle statistics

    // total damage dealt over all battles
    public int damageDealt;
    // max damage dealt in a single hit
    public MaxStat maxDamageSingleHit;
    // max damage dealt in a single battle
    public MaxStat maxDamageSingleBattle;
    // max heal in a single move
    public MaxStat maxHealSingleMove;
    // max heal healed in a single battle
    public MaxStat maxHealSingleBattle;
    // number of moves missed by player
    public int numMovesMissed;

    // number of total enemies defeated including elites and bosses
    public int enemiesDefeated;
    public int elitesDefeated;
    public int bossesDefeated;

    // number of elite enemies encountered
    public int eliteEncountered;
    // number of boss enemies encountered
    public int bossEncountered;

    public Statistics() {
        maxGold = new MaxStat();
        maxDamageSingleBattle = new MaxStat();
        maxDamageSingleHit = new MaxStat();
        maxDungeonScore = new MaxStat();
        maxHealSingleMove = new MaxStat();
        maxHealSingleBattle = new MaxStat();
    }

    /**
     * Considers a candidate stat and checks if it's greater than the current max stat
     *
     * @param maxStat
     * @param candidate
     */
    public void updateMax(MaxStat maxStat, int candidate) {
        if (candidate > maxStat.stat) {
            maxStat.stat = candidate;
        }
    }

    /**
     * Returns a list of statistics descriptions
     * @return
     */
    public String[] getDescList() {
        return new String[] {
            "player statistics",
            "Total exp gained: ",
            "Total gold earned: ",
            "Damage taken: ",
            "HP Healed: ",
            "Number of deaths: ",
            "Number of successful enchants: ",
            "map statistics",
            "Total number of steps: ",
            "battle statistics",
            "Damage dealt: ",
            "Most damage in single hit: ",
            "Most damage in single battle: ",
            "Most healing in single move: ",
            "Most healing in single battle: ",
            "Number of moves missed: ",
            "Number of enemies defeated: ",
            "Number of elites defeated: ",
            "Number of bosses defeated: ",
            "Number of elites encountered: ",
            "Number of bosses encountered: "
        };
    }

    /**
     * Returns a list of statistics numbers
     * @return
     */
    public String[] getStatsList() {
        return new String[] {
            "",
            "" + cumulativeExp,
            "" + cumulativeGold,
            "" + damageTaken,
            "" + hpHealed,
            "" + numDeaths,
            "" + numEnchants,
            "",
            "" + numSteps,
            "",
            "" + damageDealt,
            "" + maxDamageSingleHit.stat,
            "" + maxDamageSingleBattle.stat,
            "" + maxHealSingleMove.stat,
            "" + maxHealSingleBattle.stat,
            "" + numMovesMissed,
            "" + enemiesDefeated,
            "" + elitesDefeated,
            "" + bossesDefeated,
            "" + eliteEncountered,
            "" + bossEncountered
        };
    }

}

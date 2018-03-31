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
     * All statistics represented by a string
     *
     * @return
     */
    public String toString() {
        return "PLAYER STATISTICS\n"
             + "Total exp gained: " + cumulativeExp + "\n"
             + "Total gold earned: " + cumulativeGold + "\n"
             + "Damage taken: " + damageTaken + "\n"
             + "HP Healed: " + hpHealed + "\n"
             + "Number of deaths: " + numDeaths + "\n"
             + "Number of successful enchants: " + numEnchants + "\n"
             + "MAP STATISTICS\n"
             + "Total number of steps: " + numSteps + "\n"
             + "BATTLE STATISTICS\n"
             + "Damage dealt: " + damageDealt + "\n"
             + "Most damage in single hit: " + maxDamageSingleHit.stat + "\n"
             + "Most damage in single battle: " + maxDamageSingleBattle.stat + "\n"
             + "Most healing in single move: " + maxHealSingleMove.stat + "\n"
             + "Most healing in single battle: " + maxHealSingleBattle.stat + "\n"
             + "Number of moves missed: " + numMovesMissed + "\n"
             + "Number of enemies defeated: " + enemiesDefeated + "\n"
             + "Number of elites defeated: " + elitesDefeated + "\n"
             + "Number of bosses defeated: " + bossesDefeated + "\n"
             + "Number of elites encountered: " + eliteEncountered + "\n"
             + "Number of bosses encountered: " + bossEncountered;
    }

    /**
     * Returns all statistics represented by list of strings
     *
     * @return
     */
    public String[] toList() {
        return toString().split("\\r?\\n");
    }

}

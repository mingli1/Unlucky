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
    // num of shop items bought
    public int numShopItemsBought;

    // Map statistics

    // number of steps the player has moved in all maps
    public int numSteps;
    // number of maps successfully defeated
    public int numDungeonsWon;
    // number of items dropped from monsters
    public int numItemsFromMonsters;
    // number of common items dropped
    public int numCommonItems;
    // number of rare items dropped
    public int numRareItems;
    // number of epic items dropped
    public int numEpicItems;
    // number of legendary items dropped
    public int numLegendaryItems;
    // gold obtained from maps
    public int goldGainedFromMaps;
    // num of ? tiles stepped on
    public int numQuestionTiles;
    // num of ! tiles stepped on
    public int numExclamTiles;
    // num times teleported
    public int numTeleports;

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
    // number of moves used
    public int numMovesUsed;
    // number of moves missed by player
    public int numMovesMissed;
    // number of special moves used
    public int numSMovesUsed;

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
            "Number of shop items bought: ",
            "map statistics",
            "Total number of steps: ",
            "Number of maps completed: ",
            "Number of items from monsters: ",
            "Number of common items dropped: ",
            "Number of rare items dropped: ",
            "Number of epic items dropped: ",
            "Number of legendary dropped: ",
            "Total gold obtained from maps: ",
            "Number of ? tiles stepped on: ",
            "Number of ! tiles stepped on: ",
            "Number of times teleported: ",
            "battle statistics",
            "Damage dealt: ",
            "Most damage in single hit: ",
            "Most damage in single battle: ",
            "Most healing in single move: ",
            "Most healing in single battle: ",
            "Number of moves used: ",
            "Number of moves missed: ",
            "Number of special moves used: ",
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
            "" + numShopItemsBought,
            "",
            "" + numSteps,
            "" + numDungeonsWon,
            "" + numItemsFromMonsters,
            "" + numCommonItems,
            "" + numRareItems,
            "" + numEpicItems,
            "" + numLegendaryItems,
            "" + goldGainedFromMaps,
            "" + numQuestionTiles,
            "" + numExclamTiles,
            "" + numTeleports,
            "",
            "" + damageDealt,
            "" + maxDamageSingleHit.stat,
            "" + maxDamageSingleBattle.stat,
            "" + maxHealSingleMove.stat,
            "" + maxHealSingleBattle.stat,
            "" + numMovesUsed,
            "" + numMovesMissed,
            "" + numSMovesUsed,
            "" + enemiesDefeated,
            "" + elitesDefeated,
            "" + bossesDefeated,
            "" + eliteEncountered,
            "" + bossEncountered
        };
    }

}

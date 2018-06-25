package com.unlucky.map;

/**
 * A World has a theme, lore, and boss
 * Each world has a certain number of levels with the boss appearing at the end
 *
 * @author Ming Li
 */
public class World {

    // name of the world
    public String name;
    // description to be displayed on the world's button
    public String shortDesc;
    // description of the lore, theme, etc
    public String longDesc;

    // the number of levels in the world
    public int numLevels;
    // the number of levels the player is allowed to access
    public int numLevelsEnabled;

    // the levels this world contains
    public Level[] levels;

    public World(String name, String shortDesc, String longDesc, int numLevels, Level[] levels) {
        this.name = name;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
        this.numLevels = numLevels;
        this.levels = levels;
        this.numLevelsEnabled = 3;
    }

}

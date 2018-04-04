package com.unlucky.map;

/**
 * A Level has a name and average map level with a composite key (worldIndex, levelIndex)
 *
 * @author Ming Li
 */
public class Level {

    // key
    public int worldIndex;
    public int levelIndex;

    public String name;
    public int avgLevel;

    public Level(int worldIndex, int levelIndex, String name, int avgLevel) {
        this.worldIndex = worldIndex;
        this.levelIndex = levelIndex;
        this.name = name;
        this.avgLevel = avgLevel;
    }

}

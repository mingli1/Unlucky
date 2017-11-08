package com.unlucky.entity;

import com.badlogic.gdx.math.Vector2;
import com.unlucky.map.TileMap;
import com.unlucky.resource.ResourceManager;

/**
 * An Entity that the Player can battle if encountered
 * A Tile will hold an Enemy
 * An Enemy will not be able to move
 *
 * @author Ming Li
 */
public class Enemy extends Entity {

    // amount of exp an Enemy will give when defeated
    protected int expDrop;
    // amount of gold an Enemy will drop
    protected int goldDrop;

    public Enemy(String id, Vector2 position, TileMap tileMap, ResourceManager rm) {
        super(id, position, tileMap, rm);
    }

}

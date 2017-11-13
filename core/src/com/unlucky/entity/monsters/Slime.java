package com.unlucky.entity.monsters;

import com.badlogic.gdx.math.Vector2;
import com.unlucky.animation.AnimationManager;
import com.unlucky.entity.Enemy;
import com.unlucky.map.TileMap;
import com.unlucky.resource.ResourceManager;

/**
 * A harmless? slime
 *
 * @author Ming Li
 */
public class Slime extends Enemy {

    public Slime(String id, Vector2 position, TileMap tileMap, ResourceManager rm) {
        super(id, position, tileMap, rm);

        am = new AnimationManager(rm.sprites16x16, 2, 2, 1 / 3f);
    }
}

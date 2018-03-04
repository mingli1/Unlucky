package com.unlucky.entity.enemy;

import com.badlogic.gdx.math.Vector2;
import com.unlucky.animation.AnimationManager;
import com.unlucky.battle.Moveset;
import com.unlucky.battle.StatusSet;
import com.unlucky.entity.Entity;
import com.unlucky.map.TileMap;
import com.unlucky.resource.ResourceManager;
import com.unlucky.resource.Util;

/**
 * An Entity that the Player can battle if encountered
 * A Tile will hold an Enemy
 * An Enemy will not be able to move
 *
 * @author Ming Li
 */
public abstract class Enemy extends Entity {

    // battle status effects
    public StatusSet statusEffects;

    public Enemy(String id, Vector2 position, TileMap tileMap, ResourceManager rm) {
        super(id, position, tileMap, rm);
        speed = 0;
        moveset = new Moveset(rm);
        statusEffects = new StatusSet(false, rm);
    }

    public abstract boolean isElite();

    public abstract boolean isBoss();

    @Override
    public void setMaxHp(int maxHp) {
        this.maxHp = this.hp = this.previousHp = maxHp;
    }

}

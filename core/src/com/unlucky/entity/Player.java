package com.unlucky.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.unlucky.animation.AnimationManager;
import com.unlucky.battle.Moveset;
import com.unlucky.map.TileMap;
import com.unlucky.resource.ResourceManager;
import com.unlucky.resource.Util;

/**
 * The protagonist of the game.
 *
 * @author Ming Li
 */
public class Player extends Entity {

    // Battle
    private Enemy opponent;
    private boolean battling = false;

    public Player(String id, Vector2 position, TileMap tileMap, ResourceManager rm) {
        super(id, position, tileMap, rm);

        // attributes
        hp = maxHp = 100;
        accuracy = 85;
        minDamage = 12;
        maxDamage = 18;

        speed = 1;

        am = new AnimationManager(rm.sprites16x16, Util.PLAYER_WALKING, Util.PLAYER_WALKING_DELAY);
        moveset = new Moveset(rm);
        // damage seed is a random number between the damage range
        moveset.reset(minDamage, maxDamage, maxHp);
    }

    public void update(float dt) {
        super.update(dt);

        // Stop animation when player isn't moving
        if (canMove()) am.stopAnimation();

        // check for Entity interaction
        if (tileMap.containsEntity(tileMap.toTileCoords(position)) && canMove()) {
            opponent = (Enemy) tileMap.getEntity(tileMap.toTileCoords(position));
            battling = true;
        }
    }

    public void render(SpriteBatch batch) {
        if (!destroyed) {
            batch.draw(am.getKeyFrame(true), position.x + 1, position.y);
        }
    }

    public Enemy getOpponent() {
        return opponent;
    }

    public void finishBattling() {
        battling = false;
        opponent = null;
    }

    public boolean isBattling() {
        return battling;
    }

}

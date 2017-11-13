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

        opponent = null;

        // attributes
        hp = maxHp = 60;
        accuracy = 85;
        minDamage = 12;
        maxDamage = 18;

        speed = 1;

        am = new AnimationManager(rm.sprites16x16, Util.PLAYER_WALKING, Util.PLAYER_WALKING_DELAY);
        moveset = new Moveset(rm);
        // damage seed is a random number between the damage range
        moveset.reset(rand.nextInt((maxDamage - minDamage) + 1) + minDamage, maxHp);
    }

    public void update(float dt) {
        super.update(dt);

        // Stop animation when player isn't moving
        if (canMove()) am.stopAnimation();

        // check for Entity interaction
        if (tileMap.getTile(tileMap.toTileCoords(position)).containsEntity()) {
            opponent = (Enemy) tileMap.getTile(tileMap.toTileCoords(position)).getEntity();
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

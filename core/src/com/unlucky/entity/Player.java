package com.unlucky.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.unlucky.animation.AnimationManager;
import com.unlucky.battle.Moveset;
import com.unlucky.inventory.Equipment;
import com.unlucky.inventory.Inventory;
import com.unlucky.inventory.Item;
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

    // exp and level up
    private int exp;
    private int maxExp;

    private int hpIncrease = 0;
    private int minDmgIncrease = 0;
    private int maxDmgIncrease = 0;
    private int accuracyIncrease = 0;
    private int maxExpIncrease = 0;

    // gold
    private int gold = 0;

    // inventory and equips
    public Inventory inventory;
    public Equipment equips;

    public Player(String id, Vector2 position, TileMap tileMap, ResourceManager rm) {
        super(id, position, tileMap, rm);

        inventory = new Inventory();
        equips = new Equipment();

        inventory.addItem(rm.getItem(0, 0));
        inventory.addItem(rm.getItem(0, 1));
        inventory.addItem(rm.getItem(0, 2));
        inventory.addItem(rm.getItem(0, 4));
        inventory.addItem(rm.getItem(0, 5));
        inventory.addItem(rm.getItem(0, 6));
        inventory.addItem(rm.getItem(3, 2));
        inventory.addItem(rm.getItem(0, 10));
        inventory.addItem(rm.getItem(0, 11));
        inventory.addItem(rm.getItem(0, 12));
        inventory.addItem(rm.getItem(0, 14));
        inventory.addItem(rm.getItem(0, 15));
        inventory.addItem(rm.getItem(0, 16));
        inventory.addItem(rm.getItem(0, 17));
        inventory.addItem(rm.getItem(0, 18));
        inventory.addItem(rm.getItem(1, 2));
        inventory.addItem(rm.getItem(2, 2));
        inventory.addItem(rm.getItem(2, 0));
        inventory.addItem(rm.getItem(2, 1));
        inventory.addItem(rm.getItem(3, 0));
        inventory.addItem(rm.getItem(3, 1));
        inventory.addItem(rm.getItem(3, 3));

        // attributes
        hp = maxHp = previousHp = Util.PLAYER_INIT_MAX_HP;
        accuracy = Util.PLAYER_ACCURACY;
        minDamage = Util.PLAYER_INIT_MIN_DMG;
        maxDamage = Util.PLAYER_INIT_MAX_DMG;

        level = 1;
        speed = 1;

        exp = 0;
        // offset between 3 and 5
        maxExp = Util.calculateMaxExp(1, Util.getRandomValue(3, 5, rand));
        //maxExp = 2;

        // create tilemap animation
        am = new AnimationManager(rm.sprites16x16, Util.PLAYER_WALKING, Util.PLAYER_WALKING_DELAY);
        // create battle scene animation
        bam = new AnimationManager(rm.battleSprites96x96, 2, Util.PLAYER_WALKING, 2 / 5f);

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
        // draw shadow
        batch.draw(rm.shadow11x6, position.x + 3, position.y - 3);
        batch.draw(am.getKeyFrame(true), position.x + 1, position.y);
    }

    /**
     * Increments level and recalculates max exp
     * Sets increase variables to display on screen
     * Recursively accounts for n consecutive level ups from remaining exp
     *
     * @param remainder the amount of exp left after a level up
     */
    public void levelUp(int remainder) {
        level++;

        hpIncrease += Util.getRandomValue(Util.PLAYER_MIN_HP_INCREASE, Util.PLAYER_MAX_HP_INCREASE, rand);
        int dmgMean = Util.getRandomValue(Util.PLAYER_MIN_DMG_INCREASE, Util.PLAYER_MAX_DMG_INCREASE, rand);

        // deviates from mean by 0 to 3
        minDmgIncrease += (dmgMean - rand.nextInt(4));
        maxDmgIncrease += (dmgMean + rand.nextInt(4));
        // accuracy increases by 1% every 10 levels
        accuracyIncrease += level % 10 == 0 ? 1 : 0;

        int prevMaxExp = maxExp;
        maxExp = Util.calculateMaxExp(level, Util.getRandomValue(3, 5, rand));
        //maxExp = 2;
        maxExpIncrease += (maxExp - prevMaxExp);

        // another level up
        if (remainder >= maxExp) {
            levelUp(remainder - maxExp);
        } else {
            exp = remainder;
        }
    }

    /**
     * Increases the actual stats by their level up amounts
     */
    public void applyLevelUp() {
        maxHp += hpIncrease;
        hp = maxHp;
        minDamage += minDmgIncrease;
        maxDamage += maxDmgIncrease;
        accuracy += accuracyIncrease;

        // reset variables
        hpIncrease = 0;
        minDmgIncrease = 0;
        maxDmgIncrease = 0;
        accuracyIncrease = 0;
        maxExpIncrease = 0;
    }

    /**
     * Applies the stats of an equipable item
     *
     * @param item
     */
    public void equip(Item item) {
        maxHp += item.mhp;
        minDamage += item.dmg;
        maxDamage += item.dmg;
        accuracy += item.acc;
        if (hp > maxHp) hp = maxHp;
    }

    /**
     * Removes the stats of an equipable item
     *
     * @param item
     */
    public void unequip(Item item) {
        maxHp -= item.mhp;
        minDamage -= item.dmg;
        maxDamage -= item.dmg;
        accuracy -= item.acc;
        if (hp > maxHp) hp = maxHp;
    }

    public Enemy getOpponent() {
        return opponent;
    }

    public void finishBattling() {
        battling = false;
        opponent = null;
    }

    public void potion(int heal) {
        hp += heal;
        if (hp > maxHp) hp = maxHp;
    }

    public boolean isBattling() {
        return battling;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void addExp(int exp) {
        this.exp += exp;
    }

    public void setMaxExp(int maxExp) {
        this.maxExp = maxExp;
    }

    public int getExp() {
        return exp;
    }

    public int getMaxExp() {
        return maxExp;
    }

    public int getHpIncrease() {
        return hpIncrease;
    }

    public void setHpIncrease(int hpIncrease) {
        this.hpIncrease = hpIncrease;
    }

    public int getMinDmgIncrease() {
        return minDmgIncrease;
    }

    public void setMinDmgIncrease(int minDmgIncrease) {
        this.minDmgIncrease = minDmgIncrease;
    }

    public int getMaxDmgIncrease() {
        return maxDmgIncrease;
    }

    public void setMaxDmgIncrease(int maxDmgIncrease) {
        this.maxDmgIncrease = maxDmgIncrease;
    }

    public int getAccuracyIncrease() {
        return accuracyIncrease;
    }

    public void setAccuracyIncrease(int accuracyIncrease) {
        this.accuracyIncrease = accuracyIncrease;
    }

    public int getMaxExpIncrease() { return maxExpIncrease; }

    public void addGold(int g) { this.gold += g; }

    public int getGold() { return gold; }

}

package com.unlucky.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.unlucky.animation.AnimationManager;
import com.unlucky.battle.Moveset;
import com.unlucky.map.Tile;
import com.unlucky.map.TileMap;
import com.unlucky.resource.ResourceManager;

/**
 * Handles entity attributes and movement on the tile map
 *
 * @author Ming Li
 */
public abstract class Entity {

    protected String id;
    protected ResourceManager rm;

    // animation
    protected AnimationManager am;
    protected boolean pauseAnim = false;
    // battle scene animation
    protected AnimationManager bam;

    // position (x,y) in map coordinates (tile * tileSize)
    protected Vector2 position;
    /**
     * -1 - stop
     * 0 - down
     * 1 - up
     * 2 - right
     * 3 - left
     */
    protected int moving;
    // entity is in a continuous movement
    protected boolean continueMoving;
    protected float speed;
    // the Entity's current tile coordinates
    protected int currentTileX;
    protected int currentTileY;
    protected int prevDir = -1;
    protected boolean onSpecialTile = false;
    // tile causing a dialog event
    protected boolean tileInteraction = false;
    // teleportation tiles
    protected boolean teleporting = false;

    // map
    protected TileMap tileMap;

    /******** RPG ASPECTS *********/

    protected Moveset moveset;

    protected boolean dead = false;

    protected int hp;
    protected int maxHp;
    // for animation to keep track of hp difference between attacks
    protected int previousHp;
    // for applying the hp change after the dialogue is finished
    protected int damage = 0;
    protected int healing = 0;
    // 0-100 in % points
    protected int accuracy;
    // damage range
    protected int minDamage;
    protected int maxDamage;

    // shield
    protected boolean hasShield = false;
    protected int shield;
    protected int maxShield;
    protected int prevShield;

    // level up information
    protected int level;

    // move type used default -1
    protected int prevMoveUsed = -1;
    protected int moveUsed = -1;

    public Entity(String id, ResourceManager rm) {
        this.id = id;
        this.rm = rm;

        position = new Vector2();
        moving = -1;
    }

    public Entity(String id, Vector2 position, TileMap tileMap, ResourceManager rm) {
        this(id, rm);
        this.position = position;
        this.tileMap = tileMap;
    }

    public void update(float dt) {
        // movement
        handleMovement(dt);
        // special tile handling
        handleSpecialTiles();

        // handle RPG elements
        if (hp > maxHp) hp = maxHp;
        if (hp <= 0) {
            hp = 0;
        }

        // animation
        if (!pauseAnim) am.update(dt);
    }

    public void render(SpriteBatch batch, boolean looping) {
        // draw shadow
        batch.draw(rm.shadow11x6, position.x + 3, position.y - 3);
        batch.draw(am.getKeyFrame(looping), position.x, position.y);
    }

    /**
     * An entity's hp is decreased by damage taken
     *
     * @param damage
     */
    public void hit(int damage) {
        this.damage = damage;
    }

    /**
     * An entity's hp is increased by healing
     *
     * @param healing
     */
    public void heal(int healing) {
        this.healing = healing;
    }

    /**
     * Adds a shield to the entity's health bar
     *
     * @param mshield
     */
    public void setShield(int mshield) {
        hasShield = true;
        this.shield = this.maxShield = this.prevShield = mshield;
    }

    /**
     * Applies the damage done by the previous move
     */
    public void applyDamage() {
        // with a shield the damage is applied to the shield first
        if (hasShield) {
            if (damage == 0) {
                hasShield = false;
                return;
            }
            prevShield = shield;
            moveUsed = prevMoveUsed;
            prevMoveUsed = -1;

            // if the damage breaks through the shield then the player's hp bar is damaged
            if (shield - damage < 0) {
                damage = Math.abs(shield - damage);
                shield = 0;
                damageHp();
            }
            // damage breaks through the entire shield but does not damage hp bar
            else if (shield - damage == 0) {
                shield = 0;
            }
            // damage damages the shield
            else {
                shield -= damage;
                damage = 0;
            }

        }
        else {
            moveUsed = prevMoveUsed;
            prevMoveUsed = -1;
            damageHp();
        }
    }

    private void damageHp() {
        previousHp = hp;
        hp -= damage;
        damage = 0;
        if (hp <= 0) {
            hp = 0;
            dead = true;
        }
    }

    public void applyHeal() {
        previousHp = hp;
        moveUsed = prevMoveUsed;
        prevMoveUsed = -1;
        hp += healing;
        healing = 0;
        if (hp > maxHp) hp = maxHp;
    }

    /**
     * Moves an entity to a target position with a given magnitude.
     * Player movement triggered by input
     *
     * @param dir
     */
    public void move(int dir) {
        currentTileX = (int) (position.x / tileMap.tileSize);
        currentTileY = (int) (position.y / tileMap.tileSize);
        prevDir = dir;
        moving = dir;
    }

    /**
     * Checks if movement boolean array is all false
     *
     * @return
     */
    public boolean canMove() {
        return moving == -1;
    }

    /**
     * This method is to fix a problem where the player can reset their
     * movement magnitudes continuously on a blocked tile
     *
     * @param dir
     * @return
     */
    public boolean nextTileBlocked(int dir) {
        currentTileX = (int) (position.x / tileMap.tileSize);
        currentTileY = (int) (position.y / tileMap.tileSize);
        switch (dir) {
            case 0: // down
                return tileMap.getTile(currentTileX, currentTileY - 1).isBlocked();
            case 1: // up
                return tileMap.getTile(currentTileX, currentTileY + 1).isBlocked();
            case 2: // right
                return tileMap.getTile(currentTileX + 1, currentTileY).isBlocked();
            case 3: // left
                return tileMap.getTile(currentTileX - 1, currentTileY).isBlocked();
        }
        return false;
    }

    /**
     * Returns the next tile coordinate to move to either
     * currentPos +/- 1 or currentPos if the next tile is blocked
     *
     * @param dir
     * @return
     */
    public int nextPosition(int dir) {
        switch (dir) {
            case 0: // down
                Tile d = tileMap.getTile(currentTileX, currentTileY - 1);
                if (d.isBlocked() || currentTileY - 1 <= 0) {
                    return currentTileY;
                }
                return currentTileY - 1;
            case 1: // up
                Tile u = tileMap.getTile(currentTileX, currentTileY + 1);
                if (u.isBlocked() || currentTileY + 1 >= tileMap.mapHeight - 1) {
                    return currentTileY;
                }
                return currentTileY + 1;
            case 2: // right
                Tile r = tileMap.getTile(currentTileX + 1, currentTileY);
                if (r.isBlocked() || currentTileX + 1 >= tileMap.mapWidth - 1) {
                    return currentTileX;
                }
                return currentTileX + 1;
            case 3: // left
                Tile l = tileMap.getTile(currentTileX - 1, currentTileY);
                if (l.isBlocked() || currentTileX - 1 <= 0) {
                    return currentTileX;
                }
                return currentTileX - 1;
        }
        return 0;
    }

    /**
     * Handles the player's next movements when standing on a special tile
     */
    public void handleSpecialTiles() {
        int cx = (int) (position.x / tileMap.tileSize);
        int cy = (int) (position.y / tileMap.tileSize);
        Tile currentTile = tileMap.getTile(cx, cy);

        if (currentTile.isSpecial()) onSpecialTile = true;
        else onSpecialTile = false;

        if (canMove()) {
            // Player goes forwards or backwards from the tile in the direction they entered
            if (currentTile.isChange()) {
                boolean k = MathUtils.randomBoolean();
                switch (prevDir) {
                    case 0: // down
                        if (k) changeDirection(1);
                        else changeDirection(0);
                        break;
                    case 1: // up
                        if (k) changeDirection(0);
                        else changeDirection(1);
                        break;
                    case 2: // right
                        if (k) changeDirection(3);
                        else changeDirection(2);
                        break;
                    case 3: // left
                        if (k) changeDirection(2);
                        else changeDirection(3);
                        break;
                }
            }
            // Player goes 1 tile in a random direction not the direction they entered the tile on
            else if (currentTile.isInAndOut()) {
                // output direction (all other directions other than input direction)
                int odir = MathUtils.random(2);
                switch (prevDir) {
                    case 0: // down
                        if (odir == 0) changeDirection(3);
                        else if (odir == 1) changeDirection(2);
                        else changeDirection(0);
                        break;
                    case 1: // up
                        if (odir == 0) changeDirection(3);
                        else if (odir == 1) changeDirection(2);
                        else changeDirection(1);
                        break;
                    case 2: // right
                        if (odir == 0) changeDirection(0);
                        else if (odir == 1) changeDirection(1);
                        else changeDirection(2);
                        break;
                    case 3: // left
                        if (odir == 0) changeDirection(0);
                        else if (odir == 1) changeDirection(1);
                        else changeDirection(3);
                        break;
                }
            }
            else if (currentTile.isDown()) {
                changeDirection(0);
            }
            else if (currentTile.isUp()) {
                changeDirection(1);
            }
            else if (currentTile.isRight()) {
                changeDirection(2);
            }
            else if (currentTile.isLeft()) {
                changeDirection(3);
            }
            // trigger dialog event
            else if (currentTile.isQuestionMark() || currentTile.isExclamationMark()) {
                tileInteraction = true;
            }
            // trigger teleport event
            else if (currentTile.isTeleport()) {
                teleporting = true;
            }
            // ice sliding
            else if (currentTile.isIce()) {
                if (!nextTileBlocked(prevDir)) {
                    move(prevDir);
                    am.setAnimation(prevDir);
                    am.stopAnimation();
                    pauseAnim = true;
                }
            }
            else {
                pauseAnim = false;
            }
        }
    }

    public void changeDirection(int dir) {
        move(dir);
        prevDir = dir;
        am.setAnimation(dir);
    }

    /**
     * Updates every tick and moves an Entity if not on the tile map grid
     * @TODO: FIX MOVEMENT
     */
    public void handleMovement(float dt) {
        // down
        if (moving == 0) {
            int targetY = nextPosition(0);
            if (targetY == currentTileY) {
                moving = -1;
            } else {
                position.y -= speed * dt;
                if (position.y <= targetY * tileMap.tileSize &&
                        position.y - speed * dt <= targetY * tileMap.tileSize) {
                    position.y = targetY * tileMap.tileSize;
                    moving = -1;
                }
            }
        }
        // up
        if (moving == 1) {
            int targetY = nextPosition(1);
            if (targetY == currentTileY) {
                moving = -1;
            } else {
                position.y += speed * dt;
                if (position.y >= targetY * tileMap.tileSize &&
                        position.y + speed * dt >= targetY * tileMap.tileSize) {
                    position.y = targetY * tileMap.tileSize;
                    moving = -1;
                }
            }
        }
        // right
        if (moving == 2) {
            int targetX = nextPosition(2);
            if (targetX == currentTileX) {
                moving = -1;
            } else {
                position.x += speed * dt;
                if (position.x >= targetX * tileMap.tileSize &&
                        position.x + speed * dt >= targetX * tileMap.tileSize) {
                    position.x = targetX * tileMap.tileSize;
                    moving = -1;
                }
            }
        }
        // left
        if (moving == 3) {
            int targetX = nextPosition(3);
            if (targetX == currentTileX) {
                moving = -1;
            } else {
                position.x -= speed * dt;
                if (position.x <= targetX * tileMap.tileSize &&
                        position.x - speed * dt <= targetX * tileMap.tileSize) {
                    position.x = targetX * tileMap.tileSize;
                    moving = -1;
                }
            }
        }
    }

    public void setMap(TileMap map) {
        this.tileMap = map;
        this.position.set(map.toMapCoords(map.playerSpawn));
    }

    public void setContinueMoving(boolean p) {
        continueMoving = p;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public int getCurrentTileX() {
        return currentTileX;
    }

    public int getCurrentTileY() {
        return currentTileY;
    }

    public TileMap getTileMap() {
        return tileMap;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public AnimationManager getAm() { return am; }

    public AnimationManager getBam() { return bam; }

    public String getId() {
        return id;
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean isMoving() {
        return moving != -1;
    }

    public Moveset getMoveset() { return moveset; }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getPreviousHp() { return previousHp; }

    public void setPreviousHp(int previousHp) { this.previousHp = previousHp; }

    public void setLevel(int level) { this.level = level; }

    public int getLevel() {
        return level;
    }

    public int getMinDamage() { return minDamage; }

    public int getMaxDamage() { return maxDamage; }

    public void setMinDamage(int minDamage) {
        this.minDamage = minDamage;
    }

    public void setMaxDamage(int maxDamage) {
        this.maxDamage = maxDamage;
    }

    public int getAccuracy() { return accuracy; }

    public boolean isDead() { return dead; }

    public void setDead(boolean dead) { this.dead = dead; }

    public int getMoveUsed() { return moveUsed; }

    public void useMove(int move) {
        this.prevMoveUsed = move;
    }

    public int getPrevMoveUsed() { return prevMoveUsed; }

    public void setMoveUsed(int moveUsed) {
        this.moveUsed = moveUsed;
    }

    public void setPrevMoveUsed(int prevMoveUsed) { this.prevMoveUsed = prevMoveUsed; }

    public boolean isOnSpecialTile() { return onSpecialTile; }

    public boolean isTileInteraction() { return tileInteraction; }

    public boolean isTeleporting() { return teleporting; }

    /**
     * Returns the tile the Entity is currently standing on
     * @return
     */
    public Tile getCurrentTile() {
        return tileMap.getTile(tileMap.toTileCoords(position));
    }

    public boolean isHasShield() {
        return hasShield;
    }

    public void setHasShield(boolean hasShield) {
        this.hasShield = hasShield;
    }

    public int getShield() {
        return shield;
    }

    public int getMaxShield() {
        return maxShield;
    }

    public void setMaxShield(int maxShield) {
        this.maxShield = maxShield;
    }

    public int getPrevShield() {
        return prevShield;
    }

    public void setPrevShield(int prevShield) {
        this.prevShield = prevShield;
    }

    /**
     * Returns if the entity's hp is below or equal to a certain threshold
     *
     * @param percentage
     * @return
     */
    public boolean healthBelow(int percentage) {
        return hp <= (int) ((percentage / 100f) * (float) maxHp);
    }

}
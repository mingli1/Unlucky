package com.unlucky.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.unlucky.entity.Entity;
import com.unlucky.resource.Util;

/**
 * Renders an Entity's health bar and creates a nice hp animation when getting hit
 *
 * @author Ming Li
 */
public class HealthBar {

    private Stage stage;
    private ShapeRenderer shapeRenderer;

    private Entity entity;

    // hp bar rectangles
    private float maxHpBarWidth;
    private float hpBarWidth;
    private float hpBarHeight;
    private Vector2 position;
    // the pure color of the health bar (top rect)
    private Color color;

    // damage or heal health bar animation
    private float decayingHpBarWidth = 0;
    private Color damageColor = new Color(1, 0, 0, 1);
    private Color healColor = new Color(70 / 255.f, 190 / 255.f, 1, 1);
    private boolean initialized = false;

    public HealthBar(Entity entity, Stage stage, ShapeRenderer shapeRenderer, int max, int hpBarHeight, Vector2 position, Color color) {
        this.entity = entity;
        this.stage = stage;
        this.shapeRenderer = shapeRenderer;
        this.maxHpBarWidth = max;
        this.hpBarHeight = hpBarHeight;
        this.position = position;
        this.color = color;
    }

    public void update(float dt) {
        if (entity.isHasShield()) {
            // calculate health bar width based on player's current shield
            hpBarWidth = (maxHpBarWidth / ((float) entity.getMaxShield() / entity.getShield()));

            // entity damaged
            if (entity.getShield() < entity.getPrevShield()) {
                if (!initialized) {
                    // the width of the decaying hp bar is the difference between the previous and current hp
                    decayingHpBarWidth = (maxHpBarWidth / ((float) entity.getMaxShield() / (entity.getPrevShield() - entity.getShield())));
                    initialized = true;
                }
                if (decayingHpBarWidth < 0) {
                    // reset
                    decayingHpBarWidth = 0;
                    entity.setHasShield(false);
                    initialized = false;
                }
                decayingHpBarWidth -= Util.HP_BAR_DECAY_RATE * dt;
            }
        }
        else {
            // calculate health bar width based on player's current hp
            hpBarWidth = (maxHpBarWidth / ((float) entity.getMaxHp() / entity.getHp()));

            // entity damaged
            if (entity.getHp() < entity.getPreviousHp()) {
                if (!initialized) {
                    // the width of the decaying hp bar is the difference between the previous and current hp
                    decayingHpBarWidth = (maxHpBarWidth / ((float) entity.getMaxHp() / (entity.getPreviousHp() - entity.getHp())));
                    initialized = true;
                }
                if (decayingHpBarWidth < 0) {
                    // reset
                    decayingHpBarWidth = 0;
                    entity.setPreviousHp(entity.getHp());
                    initialized = false;
                }
                decayingHpBarWidth -= Util.HP_BAR_DECAY_RATE * dt;
            }
            // entity healed
            else if (entity.getHp() > entity.getPreviousHp()) {
                if (!initialized) {
                    decayingHpBarWidth = (-maxHpBarWidth / ((float) entity.getMaxHp() / (entity.getHp() - entity.getPreviousHp())));
                    initialized = true;
                }
                if (decayingHpBarWidth > 0) {
                    // reset
                    decayingHpBarWidth = 0;
                    entity.setPreviousHp(entity.getHp());
                    initialized = false;
                }
                decayingHpBarWidth += Util.HP_BAR_DECAY_RATE * dt;
            }
        }
    }

    /**
     * A health bar consists of 3 rectangles:
     * The bottom most is a black rect representing the "ground"
     * Then there are two rects layered on top representing shades of a color
     *
     * The health bar animation consists of a red rectangle spanning from an entity's
     * original hp to its damaged hp and diminishes itself until its width reaches 0
     * For healing, a blue rectangle spans from an entity's original hp to its increased hp
     * and its health bar increases until the blue bar disappears.
     *
     * @param dt
     */
    public void render(float dt) {
        shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // create black bg
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(position.x, position.y, maxHpBarWidth, hpBarHeight);

        if (entity.isHasShield()) {
            // render actual health bar
            // top rect
            shapeRenderer.setColor(new Color(225 / 255.f, 225 / 255.f, 225 / 255.f, 1));
            shapeRenderer.rect(position.x, position.y + (hpBarHeight / 2), hpBarWidth, hpBarHeight / 2);
            // bottom rect
            shapeRenderer.setColor(new Color(175 / 255.f, 175 / 255.f, 175 / 255.f, 1));
            shapeRenderer.rect(position.x, position.y, hpBarWidth, hpBarHeight / 2);
            // render hp animation
            // entity damaged
            if (entity.getShield() < entity.getPrevShield()) {
                shapeRenderer.setColor(damageColor);
                shapeRenderer.rect(position.x + hpBarWidth, position.y, decayingHpBarWidth, hpBarHeight);
            }
        }
        else {
            // render actual health bar
            // top rect
            shapeRenderer.setColor(color);
            shapeRenderer.rect(position.x, position.y + (hpBarHeight / 2), hpBarWidth, hpBarHeight / 2);
            // bottom rect
            shapeRenderer.setColor(color.r > 0 ? 175 / 255.f : 0, color.g > 0 ? 175 / 255.f : 0, color.b > 0 ? 175 / 255.f : 0, 1);
            shapeRenderer.rect(position.x, position.y, hpBarWidth, hpBarHeight / 2);
            // render hp animation
            // entity damaged
            if (entity.getHp() < entity.getPreviousHp()) {
                shapeRenderer.setColor(damageColor);
                shapeRenderer.rect(position.x + hpBarWidth, position.y, decayingHpBarWidth, hpBarHeight);
            }
            // entity healed
            else if (entity.getHp() > entity.getPreviousHp()) {
                shapeRenderer.setColor(healColor);
                shapeRenderer.rect(position.x + hpBarWidth, position.y, decayingHpBarWidth, hpBarHeight);
            }
        }

        shapeRenderer.end();
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

}

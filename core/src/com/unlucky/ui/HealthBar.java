package com.unlucky.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.unlucky.entity.Entity;

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
    private int maxHpBarWidth;
    private int hpBarWidth;
    private Vector2 position;
    // the pure color of the health bar (top rect)
    private Color color;

    public HealthBar(Entity entity, Stage stage, ShapeRenderer shapeRenderer, int max, Vector2 position, Color color) {
        this.entity = entity;
        this.stage = stage;
        this.shapeRenderer = shapeRenderer;
        this.maxHpBarWidth = max;
        this.position = position;
        this.color = color;
    }

    public void update(float dt) {
        // calculate health bar width based on player's current hp
        hpBarWidth = (int) (maxHpBarWidth / ((float) entity.getMaxHp() / entity.getHp()));
    }

    /**
     * A health bar consists of 3 rectangles:
     * The bottom most is a black rect representing the "ground"
     * Then there are two rects layered on top representing shades of a color
     *
     * @TODO Health bar hit animation
     * The health bar animation consists of a red rectangle spanning from an entity's
     * original hp to its damaged hp and diminishes itself until its width reaches 0
     *
     * @param dt
     */
    public void render(float dt) {
        shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // create black bg
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(position.x, position.y, maxHpBarWidth, 8);
        // render actual health bar
        // top rect
        shapeRenderer.setColor(color);
        shapeRenderer.rect(position.x, position.y + 4, hpBarWidth, 4);
        // bottom rect
        shapeRenderer.setColor(color.r > 0 ? 175 / 255.f : 0, color.g > 0 ? 175 / 255.f : 0, color.b > 0 ? 175 / 255.f : 0, 1);
        shapeRenderer.rect(position.x, position.y, hpBarWidth, 4);

        shapeRenderer.end();
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

}

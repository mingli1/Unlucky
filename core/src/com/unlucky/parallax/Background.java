package com.unlucky.parallax;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.unlucky.main.Unlucky;

/**
 * Dynamic parallax background for battle scenes or other screens
 *
 * @author Ming Li
 */
public class Background {

    public TextureRegion image;
    private OrthographicCamera cam;
    public Vector2 scale;

    private float ax;
    private float ay;
    private int numDrawX;
    private int numDrawY;

    // bg movement
    private float dx;
    private float dy;

    public Background(OrthographicCamera cam, Vector2 scale) {
        this.cam = cam;
        this.scale = scale;
    }

    public Background(TextureRegion image, OrthographicCamera cam, Vector2 scale) {
        this.image = image;
        this.cam = cam;
        this.scale = scale;
        numDrawX = (Unlucky.V_WIDTH * 2) / image.getRegionWidth() + 1;
        numDrawY = (Unlucky.V_HEIGHT * 2) / image.getRegionHeight() + 1;

        fixBleeding(image);
    }

    public void setImage(TextureRegion image) {
        this.image = image;
        numDrawX = (Unlucky.V_WIDTH * 2) / image.getRegionWidth() + 1;
        numDrawY = (Unlucky.V_HEIGHT * 2) / image.getRegionHeight() + 1;
        fixBleeding(image);
    }

    /**
     * Fixes the slight 1 pixel offset when moving the background to create
     * a smooth cycling image
     *
     * @param region
     */
    public void fixBleeding(TextureRegion region) {
        float fix = 0.01f;

        float x = region.getRegionX();
        float y = region.getRegionY();
        float width = region.getRegionWidth();
        float height = region.getRegionHeight();
        float invTexWidth = 1f / region.getTexture().getWidth();
        float invTexHeight = 1f / region.getTexture().getHeight();
        region.setRegion((x + fix) * invTexWidth, (y + fix) * invTexHeight, (x + width - fix) * invTexWidth, (y + height - fix) * invTexHeight);
    }

    public void setVector(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void update(float dt) {
        ax += (dx * scale.x) * dt;
        ay += (dy * scale.y) * dt;
    }

    public void render(SpriteBatch batch) {
        // bg not moving
        if (dx == 0 && dy == 0) {
            batch.draw(image, 0, 0);
        }
        else {
            float x = ((ax + cam.viewportWidth / 2 - cam.position.x) * scale.x) % image.getRegionWidth();
            float y = ((ay + cam.viewportHeight / 2 - cam.position.y) * scale.y) % image.getRegionHeight();

            int colOffset = x > 0 ? -1 : 0;
            int rowOffset = y > 0 ? -1 : 0;

            for (int row = 0; row < numDrawY; row++) {
                for (int col = 0; col < numDrawX; col++) {
                    batch.draw(image,
                        x + (col + colOffset) * image.getRegionWidth(),
                        y + (row + rowOffset) * image.getRegionHeight());
                }
            }
        }
    }

}

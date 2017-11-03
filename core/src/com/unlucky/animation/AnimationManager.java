package com.unlucky.animation;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * This class manages animations CustomAnimations for entities that are able to move in all four directions-
 * up, down, left, right, and entities that have any number of animations. It retrieves animation states
 * and plays and stops animations accordingly.
 *
 * @author Ming Li
 */
public class AnimationManager {

    // animation index constants
    public static final int DOWN = 0;
    public static final int UP = 1;
    public static final int RIGHT = 2;
    public static final int LEFT = 3;

    public float width;
    public float height;

    // animation holder instance
    public CustomAnimation currentAnimation;

    // for multi-directional animations
    // 0 - up, 1 - down, 2 - left, 3 - right
    private CustomAnimation[] animations;
    private TextureRegion[][] animationFrames;

    /**
     * Sets up for single animations
     *
     * @param sprites 2d array of sprites so that different sized animations can be used
     * @param numFrames the amount of frames in the single animation
     * @param delay
     */
    public AnimationManager(TextureRegion[][] sprites, int numFrames, int index, float delay) {
        TextureRegion[] frames = new TextureRegion[numFrames];

        width = sprites[index][0].getRegionWidth();
        height = sprites[index][0].getRegionHeight();

        for (int i = 0; i < numFrames; i++) {
            frames[i] = sprites[index][i];
        }

        currentAnimation = new CustomAnimation(delay, frames);
    }

    /**
     * Sets up the animation array for multi-directional animations
     *
     * @param sprites 2d array of sprites so that different sized animations can be used
     * @param numAnimations the number of animations in a row, not the number of frames
     * @param index a sprite's animation index/row on the spritesheet
     * @param numFrames the number of frames in one animation
     * @param delay
     */
    public AnimationManager(TextureRegion[][] sprites, int numAnimations, int index, int numFrames, float delay) {
        animations = new CustomAnimation[numAnimations];
        animationFrames = new TextureRegion[numAnimations][numFrames];

        width = sprites[index][0].getRegionWidth();
        height = sprites[index][0].getRegionHeight();

        // converting the animations frames row in the sprite texture to a 2d array to match the animation array
        for (int i = 0; i < sprites[index].length / numAnimations; i++) {
            for (int j = 0; j < animationFrames[0].length; j++) {
                animationFrames[i][j] = sprites[index][(j % numAnimations) + (i * numAnimations)];
            }
        }
        for (int i = 0; i < animations.length; i++) {
            animations[i] = new CustomAnimation(delay, animationFrames[i]);
        }

        currentAnimation = animations[0]; // initially set frame to idle down facing frame
    }

    /**
     * Specifically handles four directional animations animations
     *
     * @param sprites
     * @param index
     * @param delay
     */
    public AnimationManager(TextureRegion[][] sprites, int index, float delay) {
        animations = new CustomAnimation[4];
        animationFrames = new TextureRegion[4][4];

        width = sprites[index][0].getRegionWidth();
        height = sprites[index][0].getRegionHeight();

        // converting the animations frames row in the sprite texture to a 2d array to match the animation array
        for (int i = 0; i < sprites[index].length / 4; i++) {
            for (int j = 0; j < animationFrames[0].length; j++) {
                animationFrames[i][j] = sprites[index][(j % 4) + (i * 4)];
            }
        }
        for (int i = 0; i < animations.length; i++) {
            animations[i] = new CustomAnimation(delay, animationFrames[i]);
        }

        currentAnimation = animations[0]; // initially set frame to idle down facing frame
    }

    public void update(float dt) {
        currentAnimation.update(dt);
        currentAnimation.play();
    }

    /**
     * Sets currentAnimation to an animation established in the animation array
     *
     * @param index
     */
    public void setAnimation(int index) {
        currentAnimation = animations[index];
    }

    public void stopAnimation() {
        currentAnimation.stop();
    }

    public TextureRegion getKeyFrame(boolean looping) {
        return currentAnimation.getKeyFrame(looping);
    }


}

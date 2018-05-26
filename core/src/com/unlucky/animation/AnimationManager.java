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
     * Sets up for animations based on world index
     * Used for storing multiple animations on a single row sorted by worlds
     *
     * @param sprites
     * @param worldIndex
     * @param startIndex
     * @param numFrames
     * @param delay
     */
    public AnimationManager(TextureRegion[][] sprites, int worldIndex, int startIndex, int numFrames, float delay) {
        TextureRegion[] frames = new TextureRegion[numFrames];

        width = sprites[worldIndex][startIndex].getRegionWidth();
        height = sprites[worldIndex][startIndex].getRegionHeight();

        for (int i = startIndex; i < startIndex + numFrames; i++) {
            frames[i] = sprites[worldIndex][i];
        }

        currentAnimation = new CustomAnimation(delay, frames);
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

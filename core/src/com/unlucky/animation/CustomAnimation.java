package com.unlucky.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Extension of the LibGDX Animation class which replaces the stateTime argument with various methods.
 *
 * @author Ivan Vinski
 * @since 1.0
 */
public class CustomAnimation extends Animation {

    private float stateTime;
    private boolean playing;

    public CustomAnimation(float frameDuration, Array<? extends TextureRegion> keyFrames) {
        super(frameDuration, keyFrames);
    }

    public CustomAnimation(float frameDuration, Array<? extends TextureRegion> keyFrames, PlayMode playMode) {
        super(frameDuration, keyFrames, playMode);
    }

    public CustomAnimation(float frameDuration, TextureRegion... keyFrames) {
        super(frameDuration, keyFrames);
    }

    public void play() {
        if (!playing) {
            playing = true;
        }
    }

    public void pause() {
        if (playing) {
            playing = false;
        }
    }

    public void stop() {
        if (stateTime != 0f) {
            stateTime = 0f;
        }
        if (playing) {
            playing = false;
        }
    }

    public void update(float delta) {
        if (playing) {
            stateTime += delta;
        }
    }

    public TextureRegion getKeyFrame(boolean looping) {
        return getKeyFrame(stateTime, looping);
    }

    public TextureRegion getKeyFrame() {
        return getKeyFrame(stateTime);
    }

    public int getKeyFrameIndex() {
        return getKeyFrameIndex(stateTime);
    }

    public boolean isAnimationFinished() {
        return isAnimationFinished(stateTime);
    }

    public boolean isPlaying() {
        return playing;
    }

}
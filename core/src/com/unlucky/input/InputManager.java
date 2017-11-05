package com.unlucky.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.unlucky.resource.Util;

/**
 * Handles input for Android and desktop
 * The game only uses one input: left mouse button for Desktop and single-touch for Android
 *
 * @author Ming Li
 */
public class InputManager implements InputProcessor {

    private OrthographicCamera cam;

    // input can be the mouse or a touch
    public Vector3 input;
    public boolean dragging;

    public InputManager(OrthographicCamera cam) {
        this.cam = cam;
        input = new Vector3();
    }

    /**
     * Returns whether or not the user touched or clicked in a certain area
     *
     * @return boolean
     */
    public boolean clickedAt(int x0, int y0, int x1, int y1) {
        if (input.x >= x0 && input.x <= x1 && input.y >= y0 && input.y <= y1 && Gdx.input.isTouched()) return true;
        return false;
    }

    // Player key pad
    // Each button is AxB and x & y axis offset from bottom left is i

    public boolean clickedUp() {
        return clickedAt(Util.DIR_PAD_SIZE + Util.DIR_PAD_OFFSET,
                (Util.DIR_PAD_SIZE * 2) + Util.DIR_PAD_OFFSET,
                (Util.DIR_PAD_SIZE * 2) + Util.DIR_PAD_OFFSET,
                (Util.DIR_PAD_SIZE * 3) + Util.DIR_PAD_OFFSET);
    }

    public boolean clickedDown() {
        return clickedAt(Util.DIR_PAD_SIZE + Util.DIR_PAD_OFFSET,
                Util.DIR_PAD_OFFSET,
                (Util.DIR_PAD_SIZE * 2) + Util.DIR_PAD_OFFSET,
                Util.DIR_PAD_SIZE + Util.DIR_PAD_OFFSET);
    }

    public boolean clickedLeft() {
        return clickedAt(Util.DIR_PAD_OFFSET,
                Util.DIR_PAD_SIZE + Util.DIR_PAD_OFFSET,
                Util.DIR_PAD_SIZE + Util.DIR_PAD_OFFSET,
                (Util.DIR_PAD_SIZE * 2) + Util.DIR_PAD_OFFSET);
    }

    public boolean clickedRight() {
        return clickedAt((Util.DIR_PAD_SIZE * 2) + Util.DIR_PAD_OFFSET,
                Util.DIR_PAD_SIZE + Util.DIR_PAD_OFFSET,
                (Util.DIR_PAD_SIZE * 3) + Util.DIR_PAD_OFFSET,
                (Util.DIR_PAD_SIZE * 2) + Util.DIR_PAD_OFFSET);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // no multitouch and only uses left mouse button
        if (button != Input.Buttons.LEFT || pointer > 0) return false;
        input.set(screenX, screenY, 0);
        dragging = true;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // no multitouch and only uses left mouse button
        if (button != Input.Buttons.LEFT || pointer > 0) return false;
        input.set(screenX, screenY, 0);
        dragging = false;
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (!dragging) return false;
        input.set(screenX, screenY, 0);
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // Only for desktop
        input.set(screenX, screenY, 0);
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

}

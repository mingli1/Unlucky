package com.unlucky.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.unlucky.effects.Moving;
import com.unlucky.main.Unlucky;
import com.unlucky.resource.ResourceManager;

/**
 * The main menu screen of the game that holds all access points for playing,
 * managing the player's inventory, bank, shop, etc, and the settings
 *
 * @author Ming Li
 */
public class MenuScreen extends AbstractScreen {

    // title animation (each letter moves down at descending speeds)
    private Moving[] titleMoves;
    private Image[] letters;

    public MenuScreen(final Unlucky game, final ResourceManager rm) {
        super(game, rm);

        // Override
        viewport = new ExtendViewport(Unlucky.V_WIDTH * 2, Unlucky.V_HEIGHT * 2, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);

        // one for each letter
        titleMoves = new Moving[7];
        letters = new Image[7];
        for (int i = 0; i < 7; i++) {
            titleMoves[i] = new Moving(new Vector2(), new Vector2(), 0);
            letters[i] = new Image(rm.title[i]);
            stage.addActor(letters[i]);
        }
    }

    @Override
    public void show() {
        resetTitleAnimation();
    }

    public void update(float dt) {
        for (int i = 0; i < 7; i++) {
            titleMoves[i].update(dt);
            letters[i].setPosition(titleMoves[i].position.x, titleMoves[i].position.y);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            game.setScreen(game.gameScreen);
        }
    }

    public void render(float dt) {
        super.render(dt);
        update(dt);

        // clear screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(dt);
        stage.draw();
    }

    /**
     * Resets and starts the title animation on every transition to this screen
     */
    private void resetTitleAnimation() {
        // entire title text starts at x = 74
        for (int i = 0; i < titleMoves.length; i++) {
            titleMoves[i].origin.set(new Vector2(74 + i * 36, 240 + 48));
            titleMoves[i].target.set(new Vector2(74 + i * 36, 240 - 70));
            titleMoves[i].speed = 275 - i * 24;
            titleMoves[i].horizontal = false;
            titleMoves[i].start();
        }
    }

    @Override
    public void dispose() {
      super.dispose();
    }

}

package com.unlucky.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.unlucky.main.Unlucky;
import com.unlucky.resource.ResourceManager;

/**
 * Screen template for game states.
 *
 * @author Ming Li
 */
public abstract class AbstractScreen implements Screen {

    protected final Unlucky game;
    protected final ResourceManager rm;

    // camera that focuses on the player
    protected OrthographicCamera cam;
    // viewport that keeps aspect ratios of the game when resizing
    protected Viewport viewport;
    // main stage of each screen
    protected Stage stage;

    // to delay the batch rendering until after transition finishes
    protected boolean renderBatch = false;
    // to toggle color fading for batch draw calls
    protected boolean batchFade = true;
    // to remove previous clicks buffered before switching the screen
    protected boolean clickable = true;

    public AbstractScreen(final Unlucky game, final ResourceManager rm) {
        this.game = game;
        this.rm = rm;

        cam = new OrthographicCamera(Unlucky.V_WIDTH, Unlucky.V_HEIGHT);
        cam.setToOrtho(false);
        // the game will retain it's scaled dimensions regardless of resizing
        viewport = new StretchViewport(Unlucky.V_WIDTH, Unlucky.V_HEIGHT, cam);

        stage = new Stage(viewport, game.batch);
    }

    @Override
    public void render(float dt) {
        stage.act(dt);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void show() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }

    public Stage getStage() {
        return stage;
    }

    public OrthographicCamera getCamera() {
        return cam;
    }

    public SpriteBatch getBatch() {
        return game.batch;
    }

    public Unlucky getGame() { return game; }

    /**
     * Switches to a new screen while handling fading buffer
     * Fade transition
     *
     * @param screen
     */
    public void setFadeScreen(final Screen screen) {
        if (clickable) {
            clickable = false;
            batchFade = false;
            // fade out animation
            stage.addAction(Actions.sequence(Actions.fadeOut(0.3f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        clickable = true;
                        game.setScreen(screen);
                    }
                })));
        }
    }

    /**
     * Switches to a new screen while handling fading buffer
     * Slide transition either to the left or right
     *
     * @param screen
     * @param right
     */
    public void setSlideScreen(final Screen screen, boolean right) {
        if (clickable) {
            clickable = false;
            batchFade = true;
            // slide animation
            stage.addAction(Actions.sequence(
                Actions.moveBy(right ? -Unlucky.V_WIDTH : Unlucky.V_WIDTH, 0, 0.15f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        clickable = true;
                        game.setScreen(screen);
                    }
                })));
        }
    }

    public boolean isRenderBatch() {
        return renderBatch;
    }

    public void setRenderBatch(boolean renderBatch) {
        this.renderBatch = renderBatch;
    }

    public boolean isBatchFade() {
        return batchFade;
    }

    public void setBatchFade(boolean batchFade) {
        this.batchFade = batchFade;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

}

package com.unlucky.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
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

    public AbstractScreen(final Unlucky game, final ResourceManager rm) {
        this.game = game;
        this.rm = rm;

        cam = new OrthographicCamera(Unlucky.V_WIDTH, Unlucky.V_HEIGHT);
        cam.setToOrtho(false);
        // the game will retain it's scaled dimensions regardless of resizing
        viewport = new ExtendViewport(Unlucky.V_WIDTH, Unlucky.V_HEIGHT, cam);

        stage = new Stage(viewport, game.batch);
    }

    @Override
    public void render(float dt) {
        stage.act(dt);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

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

}

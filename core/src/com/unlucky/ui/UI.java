package com.unlucky.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.unlucky.entity.Player;
import com.unlucky.main.Unlucky;
import com.unlucky.map.TileMap;
import com.unlucky.resource.ResourceManager;
import com.unlucky.screen.GameScreen;

/**
 * Superclass for all UI
 * Contains useful variables and references
 *
 * @author Ming Li
 */
public abstract class UI implements Disposable {

    protected Stage stage;
    protected Viewport viewport;

    protected ResourceManager rm;
    protected TileMap tileMap;
    protected Player player;
    protected GameScreen gameScreen;
    protected Unlucky game;

    // graphics
    protected ShapeRenderer shapeRenderer;

    public UI(final Unlucky game, Player player, ResourceManager rm) {
        this.game = game;
        this.player = player;
        this.rm = rm;

        viewport = new StretchViewport(Unlucky.V_WIDTH, Unlucky.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);

        shapeRenderer = new ShapeRenderer();
    }

    public UI(GameScreen gameScreen, TileMap tileMap, Player player, ResourceManager rm) {
        this.game = gameScreen.getGame();
        this.gameScreen = gameScreen;
        this.tileMap = tileMap;
        this.player = player;
        this.rm = rm;

        viewport = new StretchViewport(Unlucky.V_WIDTH, Unlucky.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, gameScreen.getBatch());

        shapeRenderer = new ShapeRenderer();
    }

    public abstract void update(float dt);

    public abstract void render(float dt);

    public Stage getStage() {
        return stage;
    }

    @Override
    public void dispose() {
        stage.dispose();
        shapeRenderer.dispose();
    }

    public void setTileMap(TileMap tileMap) { this.tileMap = tileMap; }

}
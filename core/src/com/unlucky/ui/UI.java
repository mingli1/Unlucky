package com.unlucky.ui;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unlucky.entity.Player;
import com.unlucky.map.TileMap;
import com.unlucky.resource.ResourceManager;
import com.unlucky.screen.GameScreen;

/**
 * Superclass for all UI
 * Contains useful variables and references
 *
 * @author Ming Li
 */
public abstract class UI {

    protected ResourceManager rm;
    protected TileMap tileMap;
    protected Player player;
    protected GameScreen gameScreen;

    // graphics
    protected ShapeRenderer shapeRenderer;

    public UI(GameScreen gameScreen, TileMap tileMap, Player player, ResourceManager rm) {
        this.gameScreen = gameScreen;
        this.tileMap = tileMap;
        this.player = player;
        this.rm = rm;

        shapeRenderer = new ShapeRenderer();
    }

    public abstract void update(float dt);

    public abstract void render(float dt);

}
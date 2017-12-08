package com.unlucky.ui;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unlucky.entity.Player;
import com.unlucky.event.Battle;
import com.unlucky.event.BattleEvent;
import com.unlucky.map.TileMap;
import com.unlucky.resource.ResourceManager;
import com.unlucky.screen.GameScreen;

import java.util.Random;

/**
 * Superclass for all UI
 * Contains useful variables and references
 * Handles events
 *
 * @author Ming Li
 */
public abstract class UI {

    protected Random rand;
    protected ResourceManager rm;
    protected TileMap tileMap;
    protected Player player;
    protected GameScreen gameScreen;
    protected Battle battle;

    // graphics
    protected ShapeRenderer shapeRenderer;

    // FSM
    protected BattleState currentState;

    public UI(GameScreen gameScreen, TileMap tileMap, Player player, Battle battle, ResourceManager rm) {
        this.gameScreen = gameScreen;
        this.tileMap = tileMap;
        this.player = player;
        this.battle = battle;
        this.rm = rm;

        rand = new Random();
        shapeRenderer = new ShapeRenderer();
    }

    public abstract void update(float dt);

    public abstract void render(float dt);

    /**
     * Triggers the next actions given a BattleEvent
     *
     * @param event
     */
    public abstract void handleBattleEvent(BattleEvent event);

}
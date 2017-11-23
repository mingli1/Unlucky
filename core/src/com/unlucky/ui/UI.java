package com.unlucky.ui;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unlucky.entity.Player;
import com.unlucky.event.BattleEvent;
import com.unlucky.event.EventState;
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
    protected Player player;
    protected GameScreen gameScreen;

    // graphics
    protected ShapeRenderer shapeRenderer;

    // FSM
    protected BattleState currentState;

    public UI(GameScreen gameScreen, Player player, ResourceManager rm) {
        this.gameScreen = gameScreen;
        this.player = player;
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
    public void handleBattleEvent(BattleEvent event) {
        switch (event) {
            case NONE:
                return;
            case ENEMY_FLEES:
                gameScreen.setCurrentEvent(EventState.MOVING);
                break;
            case ENEMY_ENGAGES:
                break;
        }
    }

}
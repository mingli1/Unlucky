package com.unlucky.screen;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unlucky.entity.Player;
import com.unlucky.event.Battle;
import com.unlucky.event.EventState;
import com.unlucky.main.Unlucky;
import com.unlucky.resource.Util;
import com.unlucky.ui.BattleUIHandler;
import com.unlucky.ui.Hud;

import java.util.Random;

/**
 * Renders a random transition screen between two EventStates
 *
 * @author Ming Li
 */
public class TransitionScreen {

    private GameScreen gameScreen;
    private Battle battle;
    private BattleUIHandler uiHandler;
    private Hud hud;
    private Player player;
    private Random rand;

    // determine which one to render when entering and exiting battle
    private boolean renderMap = false;
    private boolean renderBattle = false;
    private boolean renderLevelUp = false;

    // render black rectangles
    private ShapeRenderer shapeRenderer;

    private EventState prev;
    private EventState next;
    private boolean shouldStart = false;
    private int transitionIndex;

    private static final int NUM_TRANSITIONS = 6;

    /*
    transition indices
    0 - horizontal rectangle left to right
    1 - vertical rectangle top to bottom
    2 - horizontal rectangle strips
    3 - vertical rectangle strips
    4 - horizontal two rect split from center
    5 - vertical two rect split from center
     */

    // Variables for animation
    // sliding
    private int x, y;
    // spliting
    private int x0, x1;
    private int y0, y1;

    public TransitionScreen(GameScreen gameScreen, Battle battle, BattleUIHandler uiHandler, Hud hud, Player player) {
        this.gameScreen = gameScreen;
        this.battle = battle;
        this.uiHandler = uiHandler;
        this.hud = hud;
        this.player = player;

        rand = new Random();
        shapeRenderer = new ShapeRenderer();

        transitionIndex = rand.nextInt(NUM_TRANSITIONS);
    }

    /**
     * Sets the appropriate variables for each transition
     *
     * @param prev
     * @param next
     */
    public void start(EventState prev, EventState next) {
        shouldStart = true;
        this.prev = prev;
        this.next = next;

        // set rendering
        if (prev == EventState.MOVING && next == EventState.BATTLING) renderMap = true;
        else if (prev == EventState.BATTLING && next == EventState.MOVING) renderBattle = true;
        else if (prev == EventState.LEVEL_UP && next == EventState.MOVING) renderLevelUp = true;

        transitionIndex = rand.nextInt(NUM_TRANSITIONS);

        switch (transitionIndex) {
            case 0: x = 0; break;
            case 1: y = Unlucky.V_HEIGHT * 2; break;
            case 2: x0 = 0; x1 = Unlucky.V_WIDTH * 2; break;
            case 3: y0 = 0; y1 = Unlucky.V_HEIGHT * 2; break;
            case 4: x0 = 0; x1 = Unlucky.V_WIDTH * 2; break;
            case 5: y0 = 0; y1 = Unlucky.V_HEIGHT * 2; break;
        }
    }

    /**
     * Handles the event triggers that result after the transition finishes
     */
    public void end() {
        shouldStart = false;
        renderMap = renderBattle = renderLevelUp = false;

        // transition into battle
        if (prev == EventState.MOVING && next == EventState.BATTLING) {
            battle.begin(player.getOpponent());
            uiHandler.engage(player.getOpponent());
            hud.toggle(false);
            gameScreen.setCurrentEvent(EventState.BATTLING);
        }
        // transition out of battle
        else if (prev == EventState.BATTLING && next == EventState.MOVING) {
            battle.end();
        }
        // transition out of level up screen
        else if (prev == EventState.LEVEL_UP && next == EventState.MOVING) {
            battle.end();
        }
    }

    public void update(float dt) {
        if (shouldStart) {
            switch (transitionIndex) {
                // horizontal slide l2r
                case 0:
                    x += Util.TRANSITION_SCREEN_SPEED;
                    if (x >= Unlucky.V_WIDTH * 2) end();
                    break;
                // vertical slide t2b
                case 1:
                    y -= Util.TRANSITION_SCREEN_SPEED;
                    if (y <= 0) end();
                    break;
                // horizontal strips
                case 2:
                    x0 += Util.TRANSITION_SCREEN_SPEED;
                    x1 -= Util.TRANSITION_SCREEN_SPEED;
                    if (x0 >= Unlucky.V_WIDTH * 2 && x1 <= 0) end();
                    break;
                // vertical strips
                case 3:
                    y0 += Util.TRANSITION_SCREEN_SPEED;
                    y1 -= Util.TRANSITION_SCREEN_SPEED;
                    if (y0 >= Unlucky.V_HEIGHT * 2 && y1 <= 0) end();
                    break;
                // horizontal split
                case 4:
                    x0 += Util.TRANSITION_SCREEN_SPEED;
                    x1 -= Util.TRANSITION_SCREEN_SPEED;
                    if (x0 >= Unlucky.V_WIDTH && x1 <= Unlucky.V_WIDTH) end();
                    break;
                // vertical split
                case 5:
                    y0 += Util.TRANSITION_SCREEN_SPEED;
                    y1 -= Util.TRANSITION_SCREEN_SPEED;
                    if (y0 >= Unlucky.V_HEIGHT && y1 <= Unlucky.V_HEIGHT) end();
                    break;
            }
        }
        //System.out.println("x: " + x + ", y: " + y);
    }

    public void render(float dt) {
        shapeRenderer.setProjectionMatrix(gameScreen.battleUIHandler.stage.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(0, 0, 0, 1);

        if (shouldStart) {
            switch (transitionIndex) {
                // horizontal slide l2r
                case 0:
                    shapeRenderer.rect(x, 0, -Unlucky.V_WIDTH * 2, Unlucky.V_HEIGHT * 2);
                    break;
                // vertical slide t2b
                case 1:
                    shapeRenderer.rect(0, y, Unlucky.V_WIDTH * 2, Unlucky.V_HEIGHT * 2);
                    break;
                // horizontal strips (height: 240 = 8 stripes of height 30)
                case 2:
                    for (int i = 0; i < 8; i++) {
                        if (i % 2 == 0) shapeRenderer.rect(x0, i * 30, -Unlucky.V_WIDTH * 2, 30);
                        else shapeRenderer.rect(x1, i * 30, Unlucky.V_WIDTH * 2, 30);
                    }
                    break;
                // vertical strips (width: 400 = 10 stripes of width 40)
                case 3:
                    for (int i = 0; i < 10; i++) {
                        if (i % 2 == 0) shapeRenderer.rect(i * 40, y0, 40, -Unlucky.V_HEIGHT * 2);
                        else shapeRenderer.rect(i * 40, y1, 40, Unlucky.V_HEIGHT * 2);
                    }
                    break;
                // horizontal split
                case 4:
                    shapeRenderer.rect(x0, 0, -Unlucky.V_WIDTH, Unlucky.V_HEIGHT * 2);
                    shapeRenderer.rect(x1, 0, Unlucky.V_WIDTH, Unlucky.V_HEIGHT * 2);
                    break;
                // vertical split
                case 5:
                    shapeRenderer.rect(0, y0, Unlucky.V_WIDTH * 2, -Unlucky.V_HEIGHT);
                    shapeRenderer.rect(0, y1, Unlucky.V_WIDTH * 2, Unlucky.V_HEIGHT);
                    break;
            }
        }

        shapeRenderer.end();
    }

    public boolean shouldRenderMap() { return renderMap; }

    public boolean shouldRenderBattle() { return renderBattle; }

    public boolean shouldRenderLevelUp() { return renderLevelUp; }

}

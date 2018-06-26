package com.unlucky.screen.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.unlucky.entity.Player;
import com.unlucky.event.Battle;
import com.unlucky.event.EventState;
import com.unlucky.main.Unlucky;
import com.unlucky.map.WeatherType;
import com.unlucky.resource.ResourceManager;
import com.unlucky.resource.Util;
import com.unlucky.screen.GameScreen;
import com.unlucky.ui.battleui.BattleUIHandler;
import com.unlucky.ui.Hud;

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
    private ResourceManager rm;

    // determine which one to render when entering and exiting battle
    public boolean renderMap = false;
    public boolean renderBattle = false;
    public boolean renderLevelUp = false;

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
    private float x, y;
    // spliting
    private float x0, x1;
    private float y0, y1;

    public TransitionScreen(GameScreen gameScreen, Battle battle, BattleUIHandler uiHandler, Hud hud, Player player, ResourceManager rm) {
        this.gameScreen = gameScreen;
        this.battle = battle;
        this.uiHandler = uiHandler;
        this.hud = hud;
        this.player = player;
        this.rm = rm;

        shapeRenderer = new ShapeRenderer();

        transitionIndex = MathUtils.random(NUM_TRANSITIONS - 1);
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
        else if (prev == EventState.MOVING && next == EventState.MOVING) renderMap = true;
        else if (prev == EventState.BATTLING) renderBattle = true;
        else if (prev == EventState.LEVEL_UP && next == EventState.MOVING) renderLevelUp = true;

        if (prev == EventState.MOVING && next == EventState.MOVING)
            transitionIndex = MathUtils.random(2, 3);
        else
            transitionIndex = MathUtils.random(NUM_TRANSITIONS - 1);

        switch (transitionIndex) {
            case 0: x = 0; break;
            case 1: y = Unlucky.V_HEIGHT; break;
            case 2: x0 = 0; x1 = Unlucky.V_WIDTH; break;
            case 3: y0 = 0; y1 = Unlucky.V_HEIGHT; break;
            case 4: x0 = 0; x1 = Unlucky.V_WIDTH; break;
            case 5: y0 = 0; y1 = Unlucky.V_HEIGHT; break;
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
            if (!player.settings.muteMusic) {
                rm.battleTheme.setLooping(true);
                rm.battleTheme.play();
            }
            uiHandler.engage(player.getOpponent());
            hud.toggle(false);
            gameScreen.setCurrentEvent(EventState.BATTLING);
        }
        else if (prev == EventState.MOVING && next == EventState.MOVING) {
            player.teleport();
            gameScreen.updateCamera();
            gameScreen.setCurrentEvent(EventState.MOVING);
            hud.toggle(true);
            player.finishTeleporting();
        }
        // transition out of battle
        else if (prev == EventState.BATTLING && next == EventState.MOVING) {
            battle.end();
            if (!player.settings.muteMusic) gameScreen.gameMap.mapTheme.play();
            if (!player.settings.muteSfx) {
                if (gameScreen.gameMap.weather == WeatherType.RAIN) {
                    gameScreen.gameMap.soundId = rm.lightrain.play(player.settings.sfxVolume);
                    rm.lightrain.setLooping(gameScreen.gameMap.soundId, true);
                }
                else if (gameScreen.gameMap.weather == WeatherType.HEAVY_RAIN || gameScreen.gameMap.weather == WeatherType.THUNDERSTORM) {
                    gameScreen.gameMap.soundId = rm.heavyrain.play(player.settings.sfxVolume);
                    rm.heavyrain.setLooping(gameScreen.gameMap.soundId, true);
                }
            }
        }
        // transition out of level up screen
        else if (prev == EventState.LEVEL_UP && next == EventState.MOVING) {
            battle.end();
            if (!player.settings.muteMusic) gameScreen.gameMap.mapTheme.play();
            if (!player.settings.muteSfx) {
                if (gameScreen.gameMap.weather == WeatherType.RAIN) {
                    gameScreen.gameMap.soundId = rm.lightrain.play(player.settings.sfxVolume);
                    rm.lightrain.setLooping(gameScreen.gameMap.soundId, true);
                }
                else if (gameScreen.gameMap.weather == WeatherType.HEAVY_RAIN || gameScreen.gameMap.weather == WeatherType.THUNDERSTORM) {
                    gameScreen.gameMap.soundId = rm.heavyrain.play(player.settings.sfxVolume);
                    rm.heavyrain.setLooping(gameScreen.gameMap.soundId, true);
                }
            }
        }
        // transition into death screen
        else if (prev == EventState.BATTLING && next == EventState.DEATH) {
            battle.end();
            gameScreen.die();
        }
    }

    public void update(float dt) {
        if (shouldStart) {
            switch (transitionIndex) {
                // horizontal slide l2r
                case 0:
                    x += Util.TRANSITION_SCREEN_SPEED * dt;
                    if (x >= Unlucky.V_WIDTH) end();
                    break;
                // vertical slide t2b
                case 1:
                    y -= Util.TRANSITION_SCREEN_SPEED * dt;
                    if (y <= 0) end();
                    break;
                // horizontal strips
                case 2:
                    x0 += Util.TRANSITION_SCREEN_SPEED * dt;
                    x1 -= Util.TRANSITION_SCREEN_SPEED * dt;
                    if (x0 >= Unlucky.V_WIDTH && x1 <= 0) end();
                    break;
                // vertical strips
                case 3:
                    y0 += Util.TRANSITION_SCREEN_SPEED * dt;
                    y1 -= Util.TRANSITION_SCREEN_SPEED * dt;
                    if (y0 >= Unlucky.V_HEIGHT && y1 <= 0) end();
                    break;
                // horizontal split
                case 4:
                    x0 += (Util.TRANSITION_SCREEN_SPEED / 2) * dt;
                    x1 -= (Util.TRANSITION_SCREEN_SPEED / 2) * dt;
                    if (x0 >= Unlucky.V_WIDTH / 2 && x1 <= Unlucky.V_WIDTH / 2) end();
                    break;
                // vertical split
                case 5:
                    y0 += (Util.TRANSITION_SCREEN_SPEED / 2) * dt;
                    y1 -= (Util.TRANSITION_SCREEN_SPEED / 2) * dt;
                    if (y0 >= Unlucky.V_HEIGHT / 2 && y1 <= Unlucky.V_HEIGHT / 2) end();
                    break;
            }
        }
    }

    public void render(float dt) {
        shapeRenderer.setProjectionMatrix(gameScreen.battleUIHandler.getStage().getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(0, 0, 0, 1);

        if (shouldStart) {
            switch (transitionIndex) {
                // horizontal slide l2r
                case 0:
                    shapeRenderer.rect(x, 0, -Unlucky.V_WIDTH, Unlucky.V_HEIGHT);
                    break;
                // vertical slide t2b
                case 1:
                    shapeRenderer.rect(0, y, Unlucky.V_WIDTH, Unlucky.V_HEIGHT);
                    break;
                // horizontal strips (height: 120 = 8 stripes of height 15)
                case 2:
                    for (int i = 0; i < 8; i++) {
                        if (i % 2 == 0) shapeRenderer.rect(x0, i * 15, -Unlucky.V_WIDTH, 15);
                        else shapeRenderer.rect(x1, i * 15, Unlucky.V_WIDTH, 15);
                    }
                    break;
                // vertical strips (width: 200 = 10 stripes of width 20)
                case 3:
                    for (int i = 0; i < 10; i++) {
                        if (i % 2 == 0) shapeRenderer.rect(i * 20, y0, 20, -Unlucky.V_HEIGHT);
                        else shapeRenderer.rect(i * 20, y1, 20, Unlucky.V_HEIGHT);
                    }
                    break;
                // horizontal split
                case 4:
                    shapeRenderer.rect(x0, 0, -Unlucky.V_WIDTH / 2, Unlucky.V_HEIGHT);
                    shapeRenderer.rect(x1, 0, Unlucky.V_WIDTH / 2, Unlucky.V_HEIGHT);
                    break;
                // vertical split
                case 5:
                    shapeRenderer.rect(0, y0, Unlucky.V_WIDTH, -Unlucky.V_HEIGHT / 2);
                    shapeRenderer.rect(0, y1, Unlucky.V_WIDTH, Unlucky.V_HEIGHT / 2);
                    break;
            }
        }

        shapeRenderer.end();
    }

}

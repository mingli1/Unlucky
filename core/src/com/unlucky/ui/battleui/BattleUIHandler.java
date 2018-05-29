package com.unlucky.ui.battleui;

import com.badlogic.gdx.math.MathUtils;
import com.unlucky.entity.enemy.Boss;
import com.unlucky.entity.enemy.Enemy;
import com.unlucky.entity.Player;
import com.unlucky.event.*;
import com.unlucky.map.TileMap;
import com.unlucky.resource.ResourceManager;
import com.unlucky.resource.Util;
import com.unlucky.screen.GameScreen;
import com.unlucky.ui.UI;

/**
 * Handles all UI for battle scenes
 *
 * @author Ming Li
 */
public class BattleUIHandler extends UI {

    public MoveUI moveUI;
    public BattleEventHandler battleEventHandler;
    public BattleScene battleScene;

    // battle
    public BattleState currentState;

    public BattleUIHandler(GameScreen gameScreen, TileMap tileMap, Player player, Battle battle, ResourceManager rm) {
        super(gameScreen, tileMap, player, rm);

        currentState = BattleState.NONE;

        battleScene = new BattleScene(gameScreen, tileMap, player, battle, this, stage, rm);
        moveUI = new MoveUI(gameScreen, tileMap, player, battle, this, stage, rm);
        battleEventHandler = new BattleEventHandler(gameScreen, tileMap, player, battle, this, stage, rm);

        moveUI.toggleMoveAndOptionUI(false);
        battleEventHandler.endDialog();
    }

    public void update(float dt) {
        if (currentState == BattleState.MOVE) moveUI.update(dt);
        if (currentState == BattleState.DIALOG) battleEventHandler.update(dt);
        battleScene.update(dt);
    }

    public void render(float dt) {
        battleScene.render(dt);

        stage.act(dt);
        stage.draw();

        if (currentState == BattleState.MOVE) moveUI.render(dt);
        if (currentState == BattleState.DIALOG) battleEventHandler.render(dt);
    }

    /**
     * When the player first encounters the enemy and engages in battle
     * There's a 1% chance that the enemy doesn't want to fight
     *
     * @param enemy
     */
    public void engage(Enemy enemy) {
        player.setDead(false);
        moveUI.init();
        battleScene.resetPositions();
        battleScene.toggle(true);
        currentState = BattleState.DIALOG;

        String[] intro;
        boolean saved = Util.isSuccess(Util.SAVED_FROM_BATTLE);

        if (enemy.isElite()) player.stats.eliteEncountered++;
        else if (enemy.isBoss()) player.stats.bossEncountered++;

        if (enemy.isBoss()) {
            if (MathUtils.randomBoolean()) {
                intro = new String[] {
                        "you encountered the boss " + enemy.getId() + "!",
                        "its power is far greater than any regular enemy.",
                        "Passive: " + ((Boss) enemy).getPassiveDescription()
                };
                battleEventHandler.startDialog(intro, BattleEvent.NONE, BattleEvent.PLAYER_TURN);
            } else {
                intro = new String[] {
                        "you encountered the boss " + enemy.getId() + "!",
                        "its power is far greater than any regular enemy.",
                        "Passive: " + ((Boss) enemy).getPassiveDescription(),
                        enemy.getId() + " strikes first!"
                };
                battleEventHandler.startDialog(intro, BattleEvent.NONE, BattleEvent.ENEMY_TURN);
            }
        }
        else {
            if (saved) {
                intro = new String[]{
                        "you encountered " + enemy.getId() + "! " +
                                "maybe there's a chance it doesn't want to fight...",
                        "the enemy stares at you and decides to flee the battle."
                };
                battleEventHandler.startDialog(intro, BattleEvent.NONE, BattleEvent.END_BATTLE);
            } else {
                // 50-50 chance for first attack from enemy or player
                if (MathUtils.randomBoolean()) {
                    intro = new String[]{
                            "you encountered " + enemy.getId() + "! " +
                                    "maybe there's a chance it doesn't want to fight...",
                            "the enemy glares at you and decides to engage in battle!"
                    };
                    battleEventHandler.startDialog(intro, BattleEvent.NONE, BattleEvent.PLAYER_TURN);
                } else {
                    intro = new String[]{
                            "you encountered " + enemy.getId() + "! " +
                                    "maybe there's a chance it doesn't want to fight...",
                            "the enemy glares at you and decides to engage in battle!",
                            enemy.getId() + " attacks first!"
                    };
                    battleEventHandler.startDialog(intro, BattleEvent.NONE, BattleEvent.ENEMY_TURN);
                }
            }
        }
    }

}

package com.unlucky.ui.battleui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.unlucky.battle.StatusEffect;
import com.unlucky.entity.Player;
import com.unlucky.entity.enemy.Boss;
import com.unlucky.event.Battle;
import com.unlucky.event.BattleEvent;
import com.unlucky.event.EventState;
import com.unlucky.inventory.Item;
import com.unlucky.map.TileMap;
import com.unlucky.resource.ResourceManager;
import com.unlucky.resource.Util;
import com.unlucky.screen.GameScreen;
import com.unlucky.event.BattleState;

/**
 * Renders a dialog box that creates text animations given text
 * Text animations are initiated with arrays of Strings where each element represents
 * one cycle of animation and a transition from one element to the next resets the animation.
 *
 * Handles battle events after text events are finished.
 *
 * @author Ming Li
 */
public class BattleEventHandler extends BattleUI {

    private Stage stage;
    private float stateTime = 0;

    // the ui for displaying text
    private Image ui;
    // Label for text animation
    private Label textLabel;
    // invisible Label for clicking the window
    private Label clickLabel;

    // text animation
    private String currentText = "";
    private String[] currentDialog = new String[0];
    private int dialogIndex = 0;
    private String[] anim;
    private String resultingText = "";
    private int animIndex = 0;

    private boolean beginCycle = false;
    private boolean endCycle = false;
    private BattleEvent prevEvent = BattleEvent.NONE;
    private BattleEvent nextEvent = BattleEvent.NONE;

    // creates the blinking triangle effect when text is done animating
    private boolean posSwitch = false;
    private float posTime = 0;

    public BattleEventHandler(GameScreen gameScreen, TileMap tileMap, Player player, Battle battle,
                              BattleUIHandler uiHandler, Stage stage, final ResourceManager rm) {
        super(gameScreen, tileMap, player, battle, uiHandler, rm);

        this.stage = stage;

        // create main UI
        ui = new Image(rm.dialogBox400x80);
        ui.setSize(200, 40);
        ui.setPosition(0, 0);
        ui.setTouchable(Touchable.disabled);

        stage.addActor(ui);

        // create Labels
        BitmapFont bitmapFont = rm.pixel10;
        Label.LabelStyle font = new Label.LabelStyle(bitmapFont, new Color(0, 0, 0, 255));

        textLabel = new Label("", font);
        textLabel.setWrap(true);
        textLabel.setTouchable(Touchable.disabled);
        textLabel.setFontScale(1.7f / 2);
        textLabel.setPosition(8, 6);
        textLabel.setSize(175, 26);
        textLabel.setAlignment(Align.topLeft);
        stage.addActor(textLabel);

        clickLabel = new Label("", font);
        clickLabel.setSize(200, 120);
        clickLabel.setPosition(0, 0);

        final Player p = player;
        clickLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (dialogIndex + 1 == currentDialog.length && endCycle) {
                    if (!p.settings.muteSfx) rm.textprogression.play(p.settings.sfxVolume);
                    // the text animation has run through every element of the text array
                    endDialog();
                    handleBattleEvent(nextEvent);
                }
                // after a cycle of text animation ends, clicking the UI goes to the next cycle
                else if (endCycle && dialogIndex < currentDialog.length) {
                    if (!p.settings.muteSfx) rm.textprogression.play(p.settings.sfxVolume);
                    dialogIndex++;
                    reset();
                    currentText = currentDialog[dialogIndex];
                    anim = currentText.split("");
                    beginCycle = true;
                }
                // clicking on the box during a text animation completes it early
                else if (beginCycle && !endCycle) {
                    resultingText = currentText;
                    textLabel.setText(resultingText);
                    beginCycle = false;
                    endCycle = true;
                }
            }
        });
        stage.addActor(clickLabel);
    }

    /**
     * Starts the text animation process given an array of Strings
     * Also takes in a BattleEvent that is called after the dialog is done
     *
     * @param dialog
     * @param next
     */
    public void startDialog(String[] dialog, BattleEvent prev, BattleEvent next) {
        ui.setVisible(true);
        textLabel.setVisible(true);
        clickLabel.setVisible(true);
        clickLabel.setTouchable(Touchable.enabled);

        currentDialog = dialog;
        currentText = currentDialog[0];
        anim = currentText.split("");

        prevEvent = prev;
        nextEvent = next;
        beginCycle = true;
    }

    public void endDialog() {
        reset();
        ui.setVisible(false);
        textLabel.setVisible(false);
        clickLabel.setVisible(false);
        clickLabel.setTouchable(Touchable.disabled);
        dialogIndex = 0;
        currentDialog = new String[0];
    }

    /**
     * Reset all variables
     */
    public void reset() {
        stateTime = 0;
        currentText = "";
        textLabel.setText("");
        resultingText = "";
        animIndex = 0;
        anim = new String[0];
        beginCycle = false;
        endCycle = false;
    }

    public void update(float dt) {
        if (beginCycle) {
            stateTime += dt;

            if (animIndex >= anim.length) endCycle = true;
            // a new character is appended to the animation every TEXT_SPEED delta time
            if (stateTime > Util.TEXT_SPEED && animIndex < anim.length && !endCycle) {
                resultingText += anim[animIndex];
                textLabel.setText(resultingText);
                animIndex++;
                stateTime = 0;
            }
        }
    }

    public void render(float dt) {
        if (endCycle) {
            // blinking indicator
            posTime += dt;
            if (posTime >= 0.5f) {
                posTime = 0;
                posSwitch = !posSwitch;
            }

            gameScreen.getBatch().setProjectionMatrix(stage.getCamera().combined);
            gameScreen.getBatch().begin();
            // render red arrow to show when a text animation cycle is complete
            if (posSwitch) gameScreen.getBatch().draw(rm.redarrow10x9, 182, 10);
            else gameScreen.getBatch().draw(rm.redarrow10x9, 182, 12);
            gameScreen.getBatch().end();
        }
    }

    /**
     * Handles battle events and turn based system
     *
     * @param event
     */
    public void handleBattleEvent(BattleEvent event) {
        switch (event) {
            case NONE:
                return;
            case END_BATTLE:
                // update battle stats
                player.stats.updateMax(player.stats.maxDamageSingleBattle, battle.cumulativeDamage);
                player.stats.updateMax(player.stats.maxHealSingleBattle, battle.cumulativeHealing);
                battle.cumulativeDamage = battle.cumulativeHealing = 0;
                rm.battleTheme.stop();

                player.resetShield();
                battle.resetBuffs();
                player.statusEffects.clear();
                gameScreen.setCurrentEvent(EventState.TRANSITION);
                gameScreen.transition.start(EventState.BATTLING, EventState.MOVING);
                break;
            case PLAYER_TURN:
                uiHandler.moveUI.toggleMoveAndOptionUI(true);
                uiHandler.currentState = BattleState.MOVE;

                // sacrifice move sets player hp to 1
                if (battle.buffs[Util.SACRIFICE]) {
                    battle.psacrifice = ((player.getHp() - 1) / (float) player.getMaxHp()) + 1;
                    player.hit(player.getHp() - 1);
                    player.applyDamage();
                    gameScreen.battleUIHandler.battleScene.playerHudLabel.setText("HP: " + player.getHp() + "/" + player.getMaxHp());
                }

                if (prevEvent == BattleEvent.ENEMY_TURN) {
                    player.statusEffects.clear();
                    if (battle.opponent.statusEffects.contains(StatusEffect.DMG_RED))
                        battle.opponent.statusEffects.clearAllButMultiTurnEffects();
                    else
                        battle.opponent.statusEffects.clear();

                    if (battle.buffs[Util.REFLECT]) {
                        battle.resetBuffs();
                        // double heal
                        if (battle.opponent.getPrevMoveUsed() != -1) {
                            battle.opponent.applyHeal();
                        }
                        // damage move
                        else {
                            player.setMoveUsed(player.getPrevMoveUsed());
                            player.setPrevMoveUsed(-1);
                            if (applyEnemyDamage()) return;
                        }
                    }
                    else {
                        if (applyPlayerDamage()) return;
                        battle.opponent.applyHeal();
                    }
                }
                break;
            case ENEMY_TURN:
                if (prevEvent == BattleEvent.PLAYER_TURN) {
                    // shield
                    if (battle.buffs[Util.SHIELD]) {
                        player.setShield((int) ((Util.P_SHIELD / 100f) * (float) player.getMaxHp()));
                    }
                    if (battle.opponent.statusEffects.contains(StatusEffect.DMG_RED))
                        battle.opponent.statusEffects.clearAllButSingleTurnEffects();
                    if (applyEnemyDamage()) return;
                    player.applyHeal();
                }
                String[] dialog = battle.enemyTurn();
                startDialog(dialog, BattleEvent.ENEMY_TURN, BattleEvent.PLAYER_TURN);
                break;
            case LEVEL_UP:
                gameScreen.setCurrentEvent(EventState.LEVEL_UP);
                gameScreen.levelUp.start();
                break;
            case PLAYER_DEAD:
                // update battle stats
                player.stats.updateMax(player.stats.maxDamageSingleBattle, battle.cumulativeDamage);
                player.stats.updateMax(player.stats.maxHealSingleBattle, battle.cumulativeHealing);
                battle.cumulativeDamage = battle.cumulativeHealing = 0;
                rm.battleTheme.stop();
                player.inMap = false;

                player.resetShield();
                player.statusEffects.clear();
                gameScreen.setCurrentEvent(EventState.TRANSITION);
                gameScreen.transition.start(EventState.BATTLING, EventState.DEATH);
                break;
        }
    }

    /**
     * Applies damage dealt to player and checks if they are dead
     *
     * @return
     */
    private boolean applyPlayerDamage() {
        player.applyDamage();
        gameScreen.battleUIHandler.battleScene.playerHudLabel.setText("HP: " + player.getHp() + "/" + player.getMaxHp());
        // player dead
        if (player.isDead()) {
            // reset animation
            battle.opponent.setPrevMoveUsed(-1);
            battle.opponent.setMoveUsed(-1);
            player.resetShield();
            battle.resetBuffs();
            player.statusEffects.clear();

            uiHandler.moveUI.toggleMoveAndOptionUI(false);
            uiHandler.currentState = BattleState.DIALOG;
            // 1% chance for revival after dead
            if (Util.isSuccess(Util.REVIVAL)) {
                startDialog(new String[] {
                        "You took fatal damage and died!",
                        "However, it looks like luck was on your side and you revived!"
                }, BattleEvent.PLAYER_TURN, BattleEvent.PLAYER_TURN);
                player.setHp(player.getMaxHp());
                player.setDead(false);
                return true;
            }
            else {
                if (!player.settings.muteSfx) rm.death.play(player.settings.sfxVolume);
                startDialog(new String[] {
                        "Oh no, you took fatal damage and died!",
                        "You will lose " + Util.DEATH_PENALTY +
                            "% of your exp and gold and all the items obtained in this level as a penalty."
                }, BattleEvent.PLAYER_TURN, BattleEvent.PLAYER_DEAD);
                gameScreen.gameMap.setDeath();

                player.stats.numDeaths++;
                return true;
            }
        }
        return false;
    }

    /**
     * Applies damage dealt to enemy and check if they are dead
     *
     * @return
     */
    private boolean applyEnemyDamage() {
        battle.opponent.applyDamage();
        // enemy dead
        if (battle.opponent.isDead()) {
            // reset animation
            player.setPrevMoveUsed(-1);
            player.setMoveUsed(-1);
            player.statusEffects.clear();
            battle.resetBuffs();

            uiHandler.moveUI.toggleMoveAndOptionUI(false);
            uiHandler.currentState = BattleState.DIALOG;

            if (bossDeathEvents()) return true;

            // 1% chance for enemy revival (bosses can't revive)
            if (Util.isSuccess(Util.REVIVAL) && !battle.opponent.isBoss()) {
                startDialog(new String[] {
                        "The enemy took fatal damage and died!",
                        "Oh no, it looks like the enemy has been revived!"
                }, BattleEvent.ENEMY_TURN, BattleEvent.ENEMY_TURN);
                battle.opponent.setHp(battle.opponent.getMaxHp());
                battle.opponent.setDead(false);
                return true;
            }
            // defeated enemy and gained experience and gold
            // maybe the player gets an item
            else {
                if (!player.settings.muteSfx) rm.death.play(player.settings.sfxVolume);

                int expGained = battle.getBattleExp();
                int goldGained = battle.getGoldGained();
                Item itemGained = battle.getItemObtained(rm);

                // add things obtained to map record
                gameScreen.gameMap.expObtained += expGained;
                gameScreen.gameMap.goldObtained += goldGained;

                player.addGold(goldGained);
                player.stats.cumulativeGold += goldGained;
                player.stats.cumulativeExp += expGained;

                if (battle.opponent.isElite()) player.stats.elitesDefeated++;
                else if (battle.opponent.isBoss()) player.stats.bossesDefeated++;
                else player.stats.enemiesDefeated++;

                // level up occurs
                if (player.getExp() + expGained >= player.getMaxExp()) {
                    int remainder = (player.getExp() + expGained) - player.getMaxExp();
                    player.levelUp(remainder);
                    startDialog(new String[] {
                            "You defeated " + battle.opponent.getId() + "!",
                            "You obtained " + goldGained + " gold.",
                            battle.getItemDialog(itemGained),
                            "You gained " + expGained + " experience.",
                            "You leveled up!"
                    }, BattleEvent.ENEMY_TURN, BattleEvent.LEVEL_UP);
                    return true;
                }
                else {
                    player.addExp(expGained);
                    startDialog(new String[] {
                            "You defeated " + battle.opponent.getId() + "!",
                            "You obtained " + goldGained + " gold.",
                            battle.getItemDialog(itemGained),
                            "You gained " + expGained + " experience."
                    }, BattleEvent.ENEMY_TURN, BattleEvent.END_BATTLE);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * A boss may have a special death event
     *
     * @return
     */
    private boolean bossDeathEvents() {
        if (battle.opponent.isBoss()) {
            Boss b = (Boss) battle.opponent;

            // king slime respawn
            if (b.bossId == 0) {
                if (battle.opponent.numRespawn + 1 < 4) {
                    battle.opponent.numRespawn++;
                    // shrink king slime
                    battle.opponent.battleSize -= 8;
                    battle.opponent.setOnlyMaxHp((int) Math.ceil(battle.opponent.getMaxHp() / 2));
                    battle.opponent.setPreviousHp(0);
                    battle.opponent.setHp(battle.opponent.getMaxHp());
                    battle.opponent.setDead(false);

                    startDialog(new String[] {
                            "King Slime respawned with half its health points!",
                            "It will respawn " + (3 - battle.opponent.numRespawn) + " more time(s)!"
                    }, BattleEvent.ENEMY_TURN, BattleEvent.ENEMY_TURN);
                    return true;
                }
            }
        }
        return false;
    }

}

package com.unlucky.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.unlucky.entity.Player;
import com.unlucky.event.Battle;
import com.unlucky.event.BattleEvent;
import com.unlucky.map.TileMap;
import com.unlucky.resource.ResourceManager;
import com.unlucky.screen.GameScreen;

/**
 * Displays health bars, battle animations, move animations, etc.
 *
 * @author Ming Li
 */
public class BattleScene extends BattleUI {

    private Stage stage;

    // Health bars
    // player
    private MovingImageUI playerHud;
    private HealthBar playerHpBar;
    private Label playerHudLabel;
    // enemy
    private MovingImageUI enemyHud;
    private HealthBar enemyHpBar;
    private Label enemyHudLabel;

    // Battle scene sprites
    private MovingImageUI playerSprite;
    private MovingImageUI enemySprite;

    public BattleScene(GameScreen gameScreen, TileMap tileMap, Player player, Battle battle,
                       BattleUIHandler uiHandler, Stage stage, ResourceManager rm) {
        super(gameScreen, tileMap, player, battle, uiHandler, rm);

        this.stage = stage;

        BitmapFont font = rm.pixel10;
        Label.LabelStyle ls = new Label.LabelStyle(font, new Color(255, 255, 255, 255));

        // create player hud
        playerHud = new MovingImageUI(rm.playerhpbar145x40, new Vector2(-145, 200), new Vector2(0, 200), 5, 145, 40);
        playerHpBar = new HealthBar(player, stage, shapeRenderer, 97, new Vector2(), new Color(0, 225 / 255.f, 0, 1));
        playerHudLabel = new Label("", ls);
        playerHudLabel.setSize(99, 12);
        playerHudLabel.setTouchable(Touchable.disabled);

        // create enemy hud
        enemyHud = new MovingImageUI(rm.enemyhpbar145x40, new Vector2(400, 200), new Vector2(255, 200), 5, 145, 40);
        enemyHpBar = new HealthBar(null, stage, shapeRenderer, 97, new Vector2(), new Color(225 / 255.f, 0, 0, 1));
        enemyHudLabel = new Label("", ls);
        enemyHudLabel.setSize(99, 12);
        enemyHudLabel.setTouchable(Touchable.disabled);

        // create player sprite
        playerSprite = new MovingImageUI(rm.battleSprites96x96[0][0], new Vector2(-96, 100), new Vector2(70, 100), 3, 96, 96);
        // create enemy sprite
        enemySprite = new MovingImageUI(rm.battleSprites96x96[0][0], new Vector2(400, 100), new Vector2(240, 100), 3, 96, 96);

        stage.addActor(playerHud);
        stage.addActor(playerHudLabel);
        stage.addActor(enemyHud);
        stage.addActor(enemyHudLabel);
        stage.addActor(playerSprite);
        stage.addActor(enemySprite);
    }

    public void toggle(boolean toggle) {
        playerHud.setVisible(toggle);
        playerHud.setDisabled(!toggle);
        playerHudLabel.setVisible(toggle);
        playerHud.start();

        if (toggle) enemyHpBar.setEntity(battle.opponent);

        enemyHud.setVisible(toggle);
        enemyHud.setDisabled(!toggle);
        enemyHudLabel.setVisible(toggle);
        enemyHud.start();

        playerSprite.setVisible(toggle);
        playerSprite.setDisabled(!toggle);
        enemySprite.setVisible(toggle);
        enemySprite.setDisabled(!toggle);
        playerSprite.start();
        enemySprite.start();
    }

    /**
     * Resets all UI back to their starting point so the animations can begin
     * for a new battle
     */
    public void resetPositions() {
        playerHud.setPosition(playerHud.getOrigin().x, playerHud.getOrigin().y);
        enemyHud.setPosition(enemyHud.getOrigin().x, enemyHud.getOrigin().y);
        playerSprite.setPosition(playerSprite.getOrigin().x, playerSprite.getOrigin().y);
        enemySprite.setPosition(enemySprite.getOrigin().x, enemySprite.getOrigin().y);
    }

    public void update(float dt) {
        playerHud.update(dt);
        enemyHud.update(dt);

        player.getBam().update(dt);
        if (battle.opponent.getBam() != null) battle.opponent.getBam().update(dt);
        playerSprite.setImage(player.getBam().getKeyFrame(true));
        enemySprite.setImage(battle.opponent.getBam().getKeyFrame(true));
        playerSprite.update(dt);
        enemySprite.update(dt);

        // show health bar animation after an entity uses its move
        playerHpBar.update(dt);
        enemyHpBar.update(dt);

        playerHudLabel.setText("HP: " + player.getHp() + "/" + player.getMaxHp());
        enemyHudLabel.setText(battle.opponent.getId());

        // set positions relative to hud position
        playerHpBar.setPosition(new Vector2(playerHud.getX() + 40, playerHud.getY() + 8));
        playerHudLabel.setPosition(playerHud.getX() + 40, playerHud.getY() + 20);
        enemyHpBar.setPosition(new Vector2(enemyHud.getX() + 8, enemyHud.getY() + 8));
        enemyHudLabel.setPosition(enemyHud.getX() + 12, enemyHud.getY() + 20);
    }

    public void render(float dt) {
        playerHpBar.render(dt);
        enemyHpBar.render(dt);
    }

    public void handleBattleEvent(BattleEvent event) {

    }

}

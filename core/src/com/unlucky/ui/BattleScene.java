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

    // health bars
    private MovingImageUI playerHud;
    private HealthBar playerHpBar;
    private Label playerHudLabel;

    public BattleScene(GameScreen gameScreen, TileMap tileMap, Player player, Battle battle,
                       BattleUIHandler uiHandler, Stage stage, ResourceManager rm) {
        super(gameScreen, tileMap, player, battle, uiHandler, rm);

        this.stage = stage;

        BitmapFont font = rm.pixel10;
        Label.LabelStyle ls = new Label.LabelStyle(font, new Color(255, 255, 255, 255));

        // create player hud
        playerHud = new MovingImageUI(rm.playerhpbar145x40, new Vector2(-145, 200), new Vector2(0, 200), 10, 145, 40);
        playerHpBar = new HealthBar(player, stage, shapeRenderer, 97, new Vector2(), new Color(0, 225, 0, 1));
        playerHudLabel = new Label("", ls);
        playerHudLabel.setSize(99, 12);
        playerHudLabel.setTouchable(Touchable.disabled);

        stage.addActor(playerHud);
        stage.addActor(playerHudLabel);
    }

    public void toggle(boolean toggle) {
        playerHud.setVisible(toggle);
        playerHud.setDisabled(!toggle);
        playerHudLabel.setVisible(toggle);
        playerHud.start();
    }

    public void update(float dt) {
        playerHud.update(dt);
        playerHpBar.update(dt);
        playerHudLabel.setText("HP: " + player.getHp() + "/" + player.getMaxHp());
        // set positions relative to hud position
        playerHpBar.setPosition(new Vector2(playerHud.getX() + 40, playerHud.getY() + 8));
        playerHudLabel.setPosition(playerHud.getX() + 40, playerHud.getY() + 20);
    }

    public void render(float dt) {
        playerHpBar.render(dt);
    }

    public void handleBattleEvent(BattleEvent event) {

    }

}

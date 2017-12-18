package com.unlucky.ui;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
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

    public BattleScene(GameScreen gameScreen, TileMap tileMap, Player player, Battle battle,
                       BattleUIHandler uiHandler, Stage stage, ResourceManager rm) {
        super(gameScreen, tileMap, player, battle, uiHandler, rm);

        this.stage = stage;

        // create player hud
        playerHud = new MovingImageUI(rm.playerhpbar145x40, new Vector2(-145, 200), new Vector2(0, 200), 5, 145, 40);

        stage.addActor(playerHud);
    }

    public void start() {
        playerHud.setVisible(true);
        playerHud.setDisabled(false);
        playerHud.start();
    }

    public void reset() {
        playerHud.setVisible(false);
        playerHud.setDisabled(true);
    }

    public void update(float dt) {
        playerHud.update(dt);
    }

    public void render(float dt) {

    }

    public void handleBattleEvent(BattleEvent event) {

    }

}

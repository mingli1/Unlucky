package com.unlucky.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.unlucky.animation.AnimationManager;
import com.unlucky.effects.Moving;
import com.unlucky.effects.ParticleFactory;
import com.unlucky.entity.Entity;
import com.unlucky.entity.Player;
import com.unlucky.event.Battle;
import com.unlucky.event.BattleEvent;
import com.unlucky.map.TileMap;
import com.unlucky.map.WeatherType;
import com.unlucky.resource.ResourceManager;
import com.unlucky.resource.Util;
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

    // Battle scene sprite positions
    private Moving playerSprite;
    private Moving enemySprite;
    private boolean renderPlayer = true;
    private boolean renderEnemy = true;

    // battle animations
    private AnimationManager[] attackAnims;
    private AnimationManager healAnim;

    // blinking hit animation
    private boolean showHitAnim = false;
    private float hitAnimDurationTimer = 0;
    private float hitAnimAlternateTimer = 0;
    private int lastHit = -1;

    // name colors based on enemy level
    /**
     * 3 or more levels lower than player = gray
     * 1 or 2 levels lower than player = green
     * same level as player = white
     * 1 or 2 levels higher than player = orange
     * 3 or more levels higher than player = red
     */
    private Label.LabelStyle weakest;
    private Label.LabelStyle weaker;
    private Label.LabelStyle same;
    private Label.LabelStyle stronger;
    private Label.LabelStyle strongest;

    // weather conditions
    private ParticleFactory factory;

    public BattleScene(GameScreen gameScreen, TileMap tileMap, Player player, Battle battle,
                       BattleUIHandler uiHandler, Stage stage, ResourceManager rm) {
        super(gameScreen, tileMap, player, battle, uiHandler, rm);

        this.stage = stage;

        BitmapFont font = rm.pixel10;
        Label.LabelStyle ls = new Label.LabelStyle(font, new Color(255, 255, 255, 255));

        weakest = new Label.LabelStyle(font, new Color(200 / 255.f, 200 / 255.f, 200 / 255.f, 1));
        weaker = new Label.LabelStyle(font, new Color(0, 225 / 255.f, 0, 1));
        same = new Label.LabelStyle(font, new Color(1, 1, 1, 1));
        stronger = new Label.LabelStyle(font, new Color(1, 175 / 255.f, 0, 1));
        strongest = new Label.LabelStyle(font, new Color(225 / 255.f, 0, 0, 1));

        // create player hud
        playerHud = new MovingImageUI(rm.playerhpbar145x40, new Vector2(-145, 200), new Vector2(0, 200), 200.f, 145, 40);
        playerHpBar = new HealthBar(player, stage, shapeRenderer, 97, 8, new Vector2(), new Color(0, 225 / 255.f, 0, 1));
        playerHudLabel = new Label("", ls);
        playerHudLabel.setSize(99, 12);
        playerHudLabel.setTouchable(Touchable.disabled);

        // create enemy hud
        enemyHud = new MovingImageUI(rm.enemyhpbar145x40, new Vector2(400, 200), new Vector2(255, 200), 200.f, 145, 40);
        enemyHpBar = new HealthBar(null, stage, shapeRenderer, 97, 8, new Vector2(), new Color(225 / 255.f, 0, 0, 1));
        enemyHudLabel = new Label("", ls);
        enemyHudLabel.setSize(99, 12);
        enemyHudLabel.setTouchable(Touchable.disabled);

        // create player sprite
        playerSprite = new Moving(new Vector2(-96, 100), new Vector2(70, 100), 150.f);
        // create enemy sprite
        enemySprite = new Moving(new Vector2(400, 100), new Vector2(240, 100), 150.f);

        // create animations
        attackAnims = new AnimationManager[3];
        for (int i = 0; i < 3; i++) {
            attackAnims[i] = new AnimationManager(rm.battleAttacks64x64, 3, i, 1 / 6f);
        }
        healAnim = new AnimationManager(rm.battleHeal96x96, 3, 0, 1 / 5f);

        factory = new ParticleFactory((OrthographicCamera) stage.getCamera(), rm);

        stage.addActor(playerHud);
        stage.addActor(playerHudLabel);
        stage.addActor(enemyHud);
        stage.addActor(enemyHudLabel);
    }

    public void toggle(boolean toggle) {
        playerHud.setVisible(toggle);
        playerHudLabel.setVisible(toggle);
        playerHud.start();

        if (toggle) {
            enemyHpBar.setEntity(battle.opponent);

            if (battle.opponent.isBoss()) {
                // boss's name is always red
                enemyHudLabel.setStyle(strongest);
            }
            else {
                int diff = battle.opponent.getLevel() - player.getLevel();
                if (diff <= -3) enemyHudLabel.setStyle(weakest);
                else if (diff == -1 || diff == -2) enemyHudLabel.setStyle(weaker);
                else if (diff == 0) enemyHudLabel.setStyle(same);
                else if (diff == 1 || diff == 2) enemyHudLabel.setStyle(stronger);
                else if (diff >= 3) enemyHudLabel.setStyle(strongest);
            }
        }

        enemyHud.setVisible(toggle);
        enemyHudLabel.setVisible(toggle);
        enemyHud.start();

        playerSprite.start();
        enemySprite.start();

        if (gameScreen.gameMap.weather == WeatherType.RAIN) {
            factory.set(2, 40, new Vector2(Util.RAINDROP_X * 2, -200));
        } else if (gameScreen.gameMap.weather == WeatherType.HEAVY_RAIN ||
                gameScreen.gameMap.weather == WeatherType.THUNDERSTORM) {
            factory.set(2, 75, new Vector2(Util.RAINDROP_X * 2, -240));
        } else if (gameScreen.gameMap.weather == WeatherType.SNOW) {
            factory.set(3, 100, new Vector2(Util.SNOWFLAKE_X * 2, -120));
        } else if (gameScreen.gameMap.weather == WeatherType.BLIZZARD) {
            factory.set(3, 300, new Vector2(Util.SNOWFLAKE_X + 100, -160));
        }

    }

    /**
     * Resets all UI back to their starting point so the animations can begin
     * for a new battle
     */
    public void resetPositions() {
        playerHud.setPosition(playerHud.moving.origin.x, playerHud.moving.origin.y);
        enemyHud.setPosition(enemyHud.moving.origin.x, enemyHud.moving.origin.y);
        playerSprite.position.set(playerSprite.origin.x, playerSprite.origin.y);
        enemySprite.position.set(enemySprite.origin.x, enemySprite.origin.y);
    }

    public void update(float dt) {
        playerHud.update(dt);
        enemyHud.update(dt);

        if (gameScreen.gameMap.weather != WeatherType.NORMAL) factory.update(dt);

        // entity sprite animations
        player.getBam().update(dt);
        if (battle.opponent.getBam() != null) battle.opponent.getBam().update(dt);

        playerSprite.update(dt);
        enemySprite.update(dt);

        // when enemy dies, its sprite falls off the screen
        if (player.isDead()) {
            float dy = playerSprite.position.y - 4;
            playerSprite.position.y = dy;
            if (playerSprite.position.y < -96) playerSprite.position.y = -96;
        }
        if (battle.opponent.isDead()) {
            float dy = enemySprite.position.y - 4;
            enemySprite.position.y = dy;
            if (enemySprite.position.y < -96) enemySprite.position.y = -96;
        }

        // render player and enemy sprites based on moving positions
        // hit animation
        if (showHitAnim) {
            hitAnimDurationTimer += dt;
            if (hitAnimDurationTimer < 0.7f) {
                hitAnimAlternateTimer += dt;
                if (hitAnimAlternateTimer > 0.1f) {
                    if (lastHit == 1) renderPlayer = !renderPlayer;
                    else renderEnemy = !renderEnemy;
                    hitAnimAlternateTimer = 0;
                }
            } else {
                hitAnimDurationTimer = 0;
                showHitAnim = false;
            }
        }
        else {
            renderPlayer = renderEnemy = true;
        }

        // show health bar animation after an entity uses its move
        playerHpBar.update(dt);
        enemyHpBar.update(dt);

        playerHudLabel.setText("HP: " + player.getHp() + "/" + player.getMaxHp());
        // show enemy level
        if (battle.opponent.isBoss()) {
            enemyHudLabel.setText(battle.opponent.getId());
        }
        else {
            enemyHudLabel.setText("LV." + battle.opponent.getLevel() + " " + battle.opponent.getId());
        }

        // set positions relative to hud position
        playerHpBar.setPosition(new Vector2(playerHud.getX() + 40, playerHud.getY() + 8));
        playerHudLabel.setPosition(playerHud.getX() + 40, playerHud.getY() + 20);
        enemyHpBar.setPosition(new Vector2(enemyHud.getX() + 8, enemyHud.getY() + 8));
        enemyHudLabel.setPosition(enemyHud.getX() + 12, enemyHud.getY() + 20);

        if (player.getMoveUsed() != -1) updateBattleAnimations(player, dt);
        if (battle.opponent.getMoveUsed() != -1) updateBattleAnimations(battle.opponent, dt);
    }

    /**
     * Update attack and heal animations after a move is used and its dialogue is finished
     *
     * @param entity either player or enemy
     * @param dt
     */
    private void updateBattleAnimations(Entity entity, float dt) {
        // damaging moves
        if (entity.getMoveUsed() < 3 && entity.getMoveUsed() >= 0) {
            if (attackAnims[entity.getMoveUsed()].currentAnimation.isAnimationFinished()) {
                attackAnims[entity.getMoveUsed()].currentAnimation.stop();
                entity.setMoveUsed(-1);
                // start hit animation
                showHitAnim = true;
                if (entity == player) lastHit = 0;
                else lastHit = 1;
            } else {
                attackAnims[entity.getMoveUsed()].update(dt);
            }
        }
        // heal
        else if (entity.getMoveUsed() == 3 && entity.getMoveUsed() >= 0) {
            if (healAnim.currentAnimation.isAnimationFinished()) {
                healAnim.currentAnimation.stop();
                entity.setMoveUsed(-1);
            } else {
                healAnim.update(dt);
            }
        }
    }

    public void render(float dt) {
        gameScreen.getBatch().begin();
        if (renderPlayer)
            gameScreen.getBatch().draw(player.getBam().getKeyFrame(true), playerSprite.position.x, playerSprite.position.y);
        if (renderEnemy)
            gameScreen.getBatch().draw(battle.opponent.getBam().getKeyFrame(true), enemySprite.position.x, enemySprite.position.y);

        // render attack or heal animations
        // player side
        if (player.getMoveUsed() != -1) {
            if (player.getMoveUsed() < 3) {
                gameScreen.getBatch().draw(attackAnims[player.getMoveUsed()].getKeyFrame(false), 255, 115);
            } else if (player.getMoveUsed() == 3) {
                gameScreen.getBatch().draw(healAnim.getKeyFrame(false), 70, 100);
            }
        }
        // enemy side
        if (battle.opponent.getMoveUsed() != -1) {
            if (battle.opponent.getMoveUsed() < 3) {
                gameScreen.getBatch().draw(attackAnims[battle.opponent.getMoveUsed()].getKeyFrame(false), 85, 115);
            } else if (battle.opponent.getMoveUsed() == 3) {
                gameScreen.getBatch().draw(healAnim.getKeyFrame(false), 240, 100);
            }
        }

        // render weather and lighting conditions if any
        if (gameScreen.gameMap.weather != WeatherType.NORMAL) factory.render(gameScreen.getBatch());

        if (gameScreen.gameMap.isDark) {
            gameScreen.getBatch().setBlendFunction(GL20.GL_DST_COLOR, GL20.GL_ONE_MINUS_SRC_ALPHA);
            gameScreen.getBatch().draw(rm.battledarkness, 0, 0);
            gameScreen.getBatch().setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        }
        gameScreen.getBatch().end();

        playerHpBar.render(dt);
        enemyHpBar.render(dt);
    }

}

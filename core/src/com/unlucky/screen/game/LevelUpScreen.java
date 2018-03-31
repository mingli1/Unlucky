package com.unlucky.screen.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.unlucky.animation.AnimationManager;
import com.unlucky.entity.Player;
import com.unlucky.event.EventState;
import com.unlucky.main.Unlucky;
import com.unlucky.map.TileMap;
import com.unlucky.resource.ResourceManager;
import com.unlucky.screen.GameScreen;
import com.unlucky.ui.UI;

import java.util.Arrays;

/**
 * Displays a level up screen showing the player's old stats and an animation
 * increasing the stats to their new values
 *
 * @author Ming Li
 */
public class LevelUpScreen extends UI implements Disposable {

    public Stage stage;
    public Viewport viewport;

    // Scene2D
    private ImageButton ui;
    private Label title;
    private Label levelDesc;
    private Label[] statsDescs;
    private String[] statNames = { "MAX EXP:", "ACCURACY:", "MAX DMG:", "MIN DMG:", "MAX HP:" };
    private Label[] stats;
    private Label[] increases;
    private Label clickToContinue;

    // animation
    private AnimationManager levelUpAnim;
    private float stateTime = 0;
    private boolean showClick = true;

    // event
    // stats number animation
    private boolean sAnimFinished = false;
    private boolean startAnim = false;
    private int[] statsNum = new int[5];
    private int[] increasedStats = new int[5];
    private float statsTime = 0;

    public LevelUpScreen(GameScreen gameScreen, TileMap tileMap, Player player, ResourceManager rm) {
        super(gameScreen, tileMap, player, rm);

        viewport = new ExtendViewport(Unlucky.V_WIDTH * 2, Unlucky.V_HEIGHT * 2, new OrthographicCamera());
        stage = new Stage(viewport, gameScreen.getBatch());

        // create bg
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(rm.levelupscreen400x240);
        ui = new ImageButton(style);
        ui.setSize(400, 240);
        ui.setPosition(0, 0);
        ui.setTouchable(Touchable.disabled);
        stage.addActor(ui);

        handleClick();

        // create animation
        levelUpAnim = new AnimationManager(rm.levelUp96x96, 4, 0, 1 / 4f);

        // create labels
        BitmapFont font = rm.pixel10;
        Label.LabelStyle titleFont = new Label.LabelStyle(font, new Color(0, 205 / 255.f, 20 / 255.f, 1));
        Label.LabelStyle stdWhite = new Label.LabelStyle(font, new Color(1, 1, 1, 1));
        Label.LabelStyle yellow = new Label.LabelStyle(font, new Color(1, 212 / 255.f, 0, 1));
        Label.LabelStyle blue = new Label.LabelStyle(font, new Color(0, 190 / 255.f, 1, 1));

        title = new Label("LEVEL UP!", titleFont);
        title.setSize(400, 40);
        title.setPosition(0, 190);
        title.setFontScale(5.f);
        title.setAlignment(Align.center);
        title.setTouchable(Touchable.disabled);
        stage.addActor(title);

        levelDesc = new Label("You reached level 1", stdWhite);
        levelDesc.setSize(400, 40);
        levelDesc.setPosition(0, 160);
        levelDesc.setFontScale(2.f);
        levelDesc.setAlignment(Align.center);
        levelDesc.setTouchable(Touchable.disabled);
        stage.addActor(levelDesc);

        statsDescs = new Label[statNames.length];
        stats = new Label[statNames.length];
        increases = new Label[statNames.length];

        for (int i = 0; i < statNames.length; i++) {
            statsDescs[i] = new Label(statNames[i], stdWhite);
            statsDescs[i].setSize(20, 20);
            statsDescs[i].setFontScale(1.3f);
            statsDescs[i].setPosition(200, 35 + (i * 25));
            statsDescs[i].setAlignment(Align.left);
            statsDescs[i].setTouchable(Touchable.disabled);
            stage.addActor(statsDescs[i]);

            stats[i] = new Label("1330", blue);
            stats[i].setSize(20, 20);
            stats[i].setFontScale(1.3f);
            stats[i].setPosition(280, 35 + (i * 25));
            stats[i].setAlignment(Align.left);
            stats[i].setTouchable(Touchable.disabled);
            stage.addActor(stats[i]);

            increases[i] = new Label("+20", yellow);
            increases[i].setSize(20, 20);
            increases[i].setFontScale(1.3f);
            increases[i].setPosition(340, 35 + (i * 25));
            increases[i].setAlignment(Align.left);
            increases[i].setTouchable(Touchable.disabled);
            stage.addActor(increases[i]);
        }

        clickToContinue = new Label("Click to continue", stdWhite);
        clickToContinue.setSize(400, 20);
        clickToContinue.setPosition(0, 5);
        clickToContinue.setAlignment(Align.center);
        clickToContinue.setTouchable(Touchable.disabled);
        stage.addActor(clickToContinue);
    }

    public void start() {
        reset();
        statsNum[0] = player.getMaxExp();
        statsNum[1] = player.getAccuracy();
        statsNum[2] = player.getMaxDamage();
        statsNum[3] = player.getMinDamage();
        statsNum[4] = player.getMaxHp();
        increasedStats[0] = statsNum[0] + player.getMaxExpIncrease();
        increasedStats[1] = statsNum[1] + player.getAccuracyIncrease();
        increasedStats[2] = statsNum[2] + player.getMaxDmgIncrease();
        increasedStats[3] = statsNum[3] + player.getMinDmgIncrease();
        increasedStats[4] = statsNum[4] + player.getHpIncrease();

        increases[0].setText("+" + player.getMaxExpIncrease());
        increases[1].setText("+" + player.getAccuracyIncrease());
        increases[2].setText("+" + player.getMaxDmgIncrease());
        increases[3].setText("+" + player.getMinDmgIncrease());
        increases[4].setText("+" + player.getHpIncrease());

        for (int i = 0; i < 5; i++) {
            stats[i].setText(String.valueOf(statsNum[i]));
        }
        for (int i = 0; i < statNames.length; i++) {
            increases[i].setVisible(true);
        }
        ui.setTouchable(Touchable.enabled);
        // update information
        levelDesc.setText("You reached level " + player.getLevel());
    }

    private void handleClick() {
        ui.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (sAnimFinished) {
                    // switch to transition screen
                    gameScreen.setCurrentEvent(EventState.TRANSITION);
                    gameScreen.transition.start(EventState.LEVEL_UP, EventState.MOVING);
                    reset();
                }
                // start
                else if (!sAnimFinished && !startAnim) {
                    // start stats animation
                    startAnim = true;
                    for (int i = 0; i < statNames.length; i++) {
                        increases[i].setVisible(false);
                    }
                    player.applyLevelUp();
                }
                // finish animation early
                else if (!sAnimFinished && startAnim) {
                    for (int i = 0; i < 5; i++) {
                        statsNum[i] = increasedStats[i];
                        stats[i].setText(String.valueOf(statsNum[i]));
                    }
                    sAnimFinished = true;
                }
            }
        });
    }

    /**
     * Mainly just setting labels back to visible
     */
    public void reset() {
        startAnim = false;
        sAnimFinished = false;
        statsTime = stateTime = 0;
        ui.setTouchable(Touchable.disabled);
    }

    public void update(float dt) {
        // update animation
        levelUpAnim.update(dt);
        stateTime += dt;
        if (stateTime > 0.5f) {
            showClick = !showClick;
            stateTime = 0;
        }

        // animation
        if (startAnim) {
            if (!sAnimFinished) {
                statsTime += dt;
                if (statsTime > 0.05f) {
                    for (int i = 0; i < 5; i++) {
                        if (statsNum[i] < increasedStats[i])
                            statsNum[i]++;
                        stats[i].setText(String.valueOf(statsNum[i]));
                    }
                    statsTime = 0;
                }
                // animation finished
                if (Arrays.equals(statsNum, increasedStats)) sAnimFinished = true;
            }
        }

        if (showClick) clickToContinue.setVisible(true);
        else clickToContinue.setVisible(false);

    }

    public void render(float dt) {
        stage.act(dt);
        stage.draw();

        gameScreen.getBatch().setProjectionMatrix(gameScreen.battleUIHandler.stage.getCamera().combined);
        gameScreen.getBatch().begin();
        gameScreen.getBatch().draw(levelUpAnim.getKeyFrame(true), 46, 55);
        gameScreen.getBatch().end();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}

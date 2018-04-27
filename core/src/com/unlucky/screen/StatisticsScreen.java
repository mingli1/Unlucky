package com.unlucky.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.unlucky.main.Unlucky;
import com.unlucky.resource.ResourceManager;

/**
 * A screen with a scroll pane displaying all game statistics
 *
 * @author Ming Li
 */
public class StatisticsScreen extends MenuExtensionScreen {

    // lines of statistics
    private String[] statsStr;

    // scroll pane
    private Table scrollTable;
    private Table selectionContainer;
    private ScrollPane scrollPane;

    private Label test;
    private ImageButton playButton;

    public StatisticsScreen(final Unlucky game, final ResourceManager rm) {
        super(game, rm);

        ImageButton.ImageButtonStyle s = new ImageButton.ImageButtonStyle();
        s.imageUp = new TextureRegionDrawable(rm.playButton[0][0]);
        s.imageDown = new TextureRegionDrawable(rm.playButton[1][0]);
        playButton = new ImageButton(s);
        playButton.setPosition(60, 35);
        stage.addActor(playButton);

        test = new Label("TESTING123", rm.skin);
        test.setPosition(100, 80);
        test.setFontScale(0.5f);
        stage.addActor(test);

    }

    @Override
    public void show() {
        super.showSlide(true);
        // update statistics every screen show
        statsStr = game.player.stats.toGroupedList();
    }

    private void createScrollPane() {

    }

    public void update(float dt) {
        super.update(dt);
    }

    public void render(float dt) {
        update(dt);

        // clear screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (renderBatch) {
            stage.getBatch().setProjectionMatrix(stage.getCamera().combined);
            stage.getBatch().begin();
            // fix fading
            if (batchFade) stage.getBatch().setColor(Color.WHITE);
            super.render(dt);
            stage.getBatch().end();
        }

        stage.act(dt);
        stage.draw();
    }

}

package com.unlucky.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.unlucky.main.Unlucky;
import com.unlucky.resource.ResourceManager;

/**
 * A screen with a scroll pane displaying all game statistics
 *
 * @author Ming Li
 */
public class StatisticsScreen extends MenuExtensionScreen {

    // screen banner
    protected Label bannerLabel;
    protected Image banner;

    // lines of statistics
    private String[] statsDescs;
    private String[] statsNums;

    private Image scrollBackground;

    // scroll pane
    private Table scrollTable;
    private Table selectionContainer;
    private ScrollPane scrollPane;

    public StatisticsScreen(final Unlucky game, final ResourceManager rm) {
        super(game, rm);

        // create title label
        banner = new Image(rm.skin, "default-slider");
        banner.setPosition(8, 102);
        banner.setSize(164, 12);
        stage.addActor(banner);

        bannerLabel = new Label("STATISTICS", rm.skin);
        bannerLabel.setStyle(new Label.LabelStyle(rm.pixel10, new Color(1, 212 / 255.f, 0, 1)));
        bannerLabel.setSize(50, 12);
        bannerLabel.setTouchable(Touchable.disabled);
        bannerLabel.setPosition(14, 102);
        bannerLabel.setAlignment(Align.left);
        stage.addActor(bannerLabel);

        ImageButton.ImageButtonStyle s = new ImageButton.ImageButtonStyle();
        s.imageUp = new TextureRegionDrawable(rm.playButton[0][0]);
        s.imageDown = new TextureRegionDrawable(rm.playButton[1][0]);

        // handle exit button
        stage.addActor(exitButton);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.menuScreen.transitionIn = 2;
                setSlideScreen(game.menuScreen, false);
            }
        });

        scrollBackground = new Image(rm.skin, "default-slider");
        scrollBackground.setPosition(8, 8);
        scrollBackground.setSize(184, 88);
        stage.addActor(scrollBackground);

        scrollTable = new Table();
    }

    @Override
    public void show() {
        game.fps.setPosition(2, 2);
        stage.addActor(game.fps);

        super.showSlide(true);
        // update statistics every screen show
        statsDescs = game.player.stats.getDescList();
        statsNums = game.player.stats.getStatsList();
        scrollTable.remove();
        createScrollPane();
    }

    private void createScrollPane() {
        scrollTable = new Table();
        scrollTable.setFillParent(true);
        stage.addActor(scrollTable);
        selectionContainer = new Table();

        Label.LabelStyle[] headerStyles = new Label.LabelStyle[] {
            new Label.LabelStyle(rm.pixel10, new Color(150 / 255.f, 1, 1, 1)),
            new Label.LabelStyle(rm.pixel10, new Color(0, 195 / 255.f, 0, 1)),
            new Label.LabelStyle(rm.pixel10, new Color(230 / 255.f, 30 / 255.f, 0, 1))
        };
        Label.LabelStyle statStyle = new Label.LabelStyle(rm.pixel10, Color.WHITE);

        for (int i = 0; i < statsDescs.length; i++) {
            Label statDesc;
            if (statsDescs[i].startsWith("player")) {
                statDesc = new Label(statsDescs[i], headerStyles[0]);
                statDesc.setFontScale(0.8f);
            } else if (statsDescs[i].startsWith("map")) {
                statDesc = new Label(statsDescs[i], headerStyles[1]);
                statDesc.setFontScale(0.8f);
            } else if (statsDescs[i].startsWith("battle")) {
                statDesc = new Label(statsDescs[i], headerStyles[2]);
                statDesc.setFontScale(0.8f);
            } else {
                statDesc = new Label(statsDescs[i], statStyle);
                statDesc.setFontScale(0.5f);
            }
            Label statNum = new Label(statsNums[i], statStyle);
            statNum.setFontScale(0.5f);

            if (statsDescs[i].startsWith("player") || statsDescs[i].startsWith("map") || statsDescs[i].startsWith("battle")) {
                selectionContainer.add(statDesc).padBottom(6).align(Align.left).row();
            }
            else {
                selectionContainer.add(statDesc).padBottom(6).align(Align.left);
                selectionContainer.add(statNum).padBottom(6).padRight(-60).align(Align.right);
                selectionContainer.row();
            }
        }

        selectionContainer.pack();
        selectionContainer.setTransform(true);
        selectionContainer.setOrigin(selectionContainer.getWidth() / 2,
            selectionContainer.getHeight() / 2);

        scrollPane = new ScrollPane(selectionContainer, rm.skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.layout();
        scrollTable.add(scrollPane).size(254, 80).fill();
        scrollTable.setPosition(-34, -8);
    }

}

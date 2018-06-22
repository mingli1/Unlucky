package com.unlucky.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.unlucky.main.Unlucky;
import com.unlucky.resource.ResourceManager;

/**
 *
 * Screen that allows the player to modify settings of the game such as
 * music and sfx volume, toggle animations, show fps, etc.
 * Can be accessed either in game while paused or through the main menu.
 *
 * @author Ming Li
 */
public class SettingsScreen extends MenuExtensionScreen {

    // to be set when the player accesses the screen from in game
    public int worldIndex;

    // whether or not the player is accessing settings from in game
    public boolean inGame = false;

    // ui
    private Image banner;
    private Label bannerLabel;
    private Image bg;
    private Label.LabelStyle white;
    private Label description;


    public SettingsScreen(final Unlucky game, final ResourceManager rm) {
        super(game, rm);

        // exit button
        stage.addActor(exitButton);
        exitButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (inGame) {
                    game.gameScreen.resetGame = false;
                    setFadeScreen(game.gameScreen);
                    game.gameScreen.hud.settingsDialog.show(game.gameScreen.hud.getStage());
                }
                else {
                    game.menuScreen.transitionIn = 2;
                    setSlideScreen(game.menuScreen, false);
                }
            }
        });

        // create title label
        banner = new Image(rm.skin, "default-slider");
        banner.setPosition(8, 102);
        banner.setSize(164, 12);
        stage.addActor(banner);

        bannerLabel = new Label("SETTINGS", rm.skin);
        bannerLabel.setStyle(new Label.LabelStyle(rm.pixel10, new Color(1, 212 / 255.f, 0, 1)));
        bannerLabel.setSize(50, 12);
        bannerLabel.setTouchable(Touchable.disabled);
        bannerLabel.setPosition(14, 102);
        bannerLabel.setAlignment(Align.left);
        stage.addActor(bannerLabel);

        bg = new Image(rm.skin, "default-slider");
        bg.setPosition(8, 8);
        bg.setSize(184, 88);
        stage.addActor(bg);

        white = new Label.LabelStyle(rm.pixel10, Color.WHITE);
        description = new Label("SOUND                                 MISC", white);
        description.setFontScale(0.75f);
        description.setTouchable(Touchable.disabled);
        description.setPosition(14, 85);
        stage.addActor(description);
    }

    public void show() {
        game.fps.setPosition(2, 2);
        stage.addActor(game.fps);

        // fade in transition if in game
        if (inGame) {
            Gdx.input.setInputProcessor(stage);
            renderBatch = false;
            batchFade = true;
            stage.addAction(Actions.sequence(Actions.alpha(0), Actions.run(new Runnable() {
                @Override
                public void run() {
                    renderBatch = true;
                }
            }), Actions.fadeIn(0.5f)));
        }
        // slide in transition if in menu
        else {
            super.showSlide(true);
        }
    }

    @Override
    public void render(float dt) {
        update(dt);

        if (!inGame) {
            for (int i = 0; i < game.menuBackground.length; i++) {
                game.menuBackground[i].update(dt);
            }
        }

        // clear screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (renderBatch) {
            stage.getBatch().setProjectionMatrix(stage.getCamera().combined);
            stage.getBatch().begin();
            // fix fading
            if (batchFade) stage.getBatch().setColor(Color.WHITE);

            // if in game the background is the background of the current world
            if (inGame) {
                stage.getBatch().draw(rm.worldSelectBackgrounds[worldIndex], 0, 0);
            }
            else {
                for (int i = 0; i < game.menuBackground.length; i++) {
                    game.menuBackground[i].render((SpriteBatch) stage.getBatch());
                }
            }
            stage.getBatch().end();
        }

        stage.act(dt);
        stage.draw();
    }

}

package com.unlucky.screen;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.unlucky.main.Unlucky;
import com.unlucky.resource.ResourceManager;

/**
 * An abstract screen with twice the resolution for text rendering
 *
 * @author Ming Li
 */
public abstract class DoubleDimensionScreen extends AbstractScreen {

    public DoubleDimensionScreen(final Unlucky game, final ResourceManager rm) {
        super(game, rm);

        // Override
        viewport = new ExtendViewport(Unlucky.V_WIDTH * 2, Unlucky.V_HEIGHT * 2, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);
    }

}

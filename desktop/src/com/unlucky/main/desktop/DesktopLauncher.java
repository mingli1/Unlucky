package com.unlucky.main.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.unlucky.main.Unlucky;

/**
 * Desktop version access
 */
public class DesktopLauncher {

	public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = Unlucky.V_WIDTH * Unlucky.V_SCALE;
        config.height = Unlucky.V_HEIGHT * Unlucky.V_SCALE;
        config.title = Unlucky.TITLE;
        config.resizable = false;
        config.vSyncEnabled = false;
        config.backgroundFPS = 10;
        config.foregroundFPS = 60;
        config.addIcon("desktop_icon128.png", Files.FileType.Internal);
        config.addIcon("desktop_icon32.png", Files.FileType.Internal);
        config.addIcon("desktop_icon16.png", Files.FileType.Internal);
        new LwjglApplication(new Unlucky(), config);
    }

}

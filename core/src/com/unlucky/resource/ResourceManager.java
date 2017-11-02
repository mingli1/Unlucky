package com.unlucky.resource;

import com.badlogic.gdx.assets.AssetManager;

/**
 * Main resource loading and storage class. Uses an AssetManager to manage textures, sounds,
 * musics, etc. Contains convenience methods to load and get resources from the asset manager.
 *
 * @author Ming Li
 */
public class ResourceManager {

    private AssetManager assetManager;

    public ResourceManager() {
        assetManager = new AssetManager();
    }

    public void dispose() {
        assetManager.dispose();
    }

}

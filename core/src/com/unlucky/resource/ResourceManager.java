package com.unlucky.resource;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;


/**
 * Main resource loading and storage class. Uses an AssetManager to manage textures, sounds,
 * musics, etc. Contains convenience methods to load and get resources from the asset manager.
 *
 * @author Ming Li
 */
public class ResourceManager {

    private AssetManager assetManager;

    // 2D TextureRegion arrays that stores sprites of various sizes for easy animation
    public TextureRegion[][] sprites16x16;
    public TextureRegion[][] tiles16x16;
    public TextureRegion[][] dirpad20x20;

    public ResourceManager() {
        assetManager = new AssetManager();

        assetManager.load("sprites/16x16_sprites.png", Texture.class);
        assetManager.load("sprites/16x16_tiles.png", Texture.class);
        assetManager.load("ui/dir_pad.png", Texture.class);

        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        FreetypeFontLoader.FreeTypeFontLoaderParameter font = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        font.fontFileName = "arial.ttf";
        font.fontParameters.size = 12;
        font.fontParameters.minFilter = Texture.TextureFilter.Nearest;
        font.fontParameters.magFilter = Texture.TextureFilter.Nearest;
        assetManager.load("arial.ttf", BitmapFont.class, font);

        assetManager.finishLoading();

        sprites16x16 = TextureRegion.split(
                assetManager.get("sprites/16x16_sprites.png", Texture.class), 16, 16);
        tiles16x16 = TextureRegion.split(
                assetManager.get("sprites/16x16_tiles.png", Texture.class), 16, 16);
        dirpad20x20 = TextureRegion.split(assetManager.get("ui/dir_pad.png", Texture.class), 20, 20);
    }

    public void dispose() {
        assetManager.dispose();
    }

}

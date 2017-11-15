package com.unlucky.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.unlucky.battle.Move;

/**
 * Main resource loading and storage class. Uses an AssetManager to manage textures, sounds,
 * musics, etc. Contains convenience methods to load and get resources from the asset manager.
 *
 * @author Ming Li
 */
public class ResourceManager {

    public AssetManager assetManager;

    // 2D TextureRegion arrays that stores sprites of various sizes for easy animation
    public TextureRegion[][] sprites16x16;
    public TextureRegion[][] tiles16x16;
    public TextureRegion[][] dirpad20x20;
    public TextureRegion[][] movebutton110x40;

    // Arrays for each type of Move
    // Contains the entire pool of moves for each type
    public final Array<Move> accurateMoves = new Array<Move>();
    public final Array<Move> wideMoves = new Array<Move>();
    public final Array<Move> critMoves = new Array<Move>();
    public final Array<Move> healMoves = new Array<Move>();

    // Fonts
    public final BitmapFont consolas10 = new BitmapFont(Gdx.files.internal("fonts/consolas.fnt"),
                                                        Gdx.files.internal("fonts/consolas.png"), false);
    public final BitmapFont pixel10 = new BitmapFont(Gdx.files.internal("fonts/pixel.fnt"),
                                                    Gdx.files.internal("fonts/pixel.png"), false);

    public ResourceManager() {
        assetManager = new AssetManager();

        assetManager.load("sprites/16x16_sprites.png", Texture.class);
        assetManager.load("sprites/16x16_tiles.png", Texture.class);
        assetManager.load("ui/dir_pad.png", Texture.class);
        assetManager.load("ui/move_buttons.png", Texture.class);

        loadMoves();

        assetManager.finishLoading();

        sprites16x16 = TextureRegion.split(
                assetManager.get("sprites/16x16_sprites.png", Texture.class), 16, 16);
        tiles16x16 = TextureRegion.split(
                assetManager.get("sprites/16x16_tiles.png", Texture.class), 16, 16);
        dirpad20x20 = TextureRegion.split(assetManager.get("ui/dir_pad.png", Texture.class), 40, 40);
        movebutton110x40 = TextureRegion.split(assetManager.get("ui/move_buttons.png", Texture.class), Util.MOVE_WIDTH, Util.MOVE_HEIGHT);
    }

    /**
     * Loads the ImageButton styles for buttons with up and down image effects
     *
     * @param numButtons
     * @param sprites
     * @return a style array for ImageButtons
     */
    public ImageButton.ImageButtonStyle[] loadImageButtonStyles(int numButtons, TextureRegion[][] sprites) {
        ImageButton.ImageButtonStyle[] ret = new ImageButton.ImageButtonStyle[numButtons];
        for (int i = 0; i < numButtons; i++) {
            ret[i] = new ImageButton.ImageButtonStyle();
            ret[i].imageUp = new TextureRegionDrawable(sprites[0][i]);
            ret[i].imageDown = new TextureRegionDrawable(sprites[1][i]);
        }
        return ret;
    }

    private void loadMoves() {
        // parse moves.json
        JsonReader jsonReader = new JsonReader();
        JsonValue base = jsonReader.parse(Gdx.files.internal("moves/moves.json"));

        // accurate Moves
        for (JsonValue move : base.get("accurate")) {
            Move m = new Move(move.getInt("type"), move.getString("name"),
                    move.getFloat("minDamage"), move.getFloat("maxDamage"));
            accurateMoves.add(m);
        }
        // wide Moves
        for (JsonValue move : base.get("wide")) {
            Move m = new Move(move.getInt("type"), move.getString("name"),
                    move.getFloat("minDamage"), move.getFloat("maxDamage"));
            wideMoves.add(m);
        }
        // crit Moves
        for (JsonValue move : base.get("crit")) {
            Move m = new Move(move.getString("name"), move.getFloat("damage"), move.getInt("crit"));
            critMoves.add(m);
        }
        // heal Moves
        for (JsonValue move : base.get("healing")) {
            Move m = new Move(move.getInt("type"), move.getString("name"),
                    move.getFloat("minHeal"), move.getFloat("maxHeal"));
            healMoves.add(m);
        }
    }

    public void dispose() {
        assetManager.dispose();
    }

}

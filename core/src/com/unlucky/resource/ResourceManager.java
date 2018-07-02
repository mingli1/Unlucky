package com.unlucky.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.unlucky.battle.Move;
import com.unlucky.inventory.Item;
import com.unlucky.inventory.ShopItem;
import com.unlucky.map.Level;
import com.unlucky.map.World;

/**
 * Main resource loading and storage class. Uses an AssetManager to manage textures, sounds,
 * musics, etc. Contains convenience methods to load and get resources from the asset manager.
 *
 * @author Ming Li
 */
public class ResourceManager {

    public AssetManager assetManager;
    // json
    private JsonReader jsonReader;

    // Texture Atlas that contains every sprite
    public TextureAtlas atlas;

    // 2D TextureRegion arrays that stores sprites of various sizes for easy animation
    public TextureRegion[][] sprites16x16;
    public TextureRegion[][] tiles16x16;
    public TextureRegion[][] atiles16x16;
    public TextureRegion[][] items20x20;
    public TextureRegion[][] shopitems;
    public TextureRegion[][] battleSprites96x96;
    public TextureRegion[][] battleBackgrounds400x240;
    public TextureRegion[][] battleAttacks64x64;
    public TextureRegion[][] battleHeal96x96;
    public TextureRegion[][] levelUp96x96;
    public TextureRegion raindrop;
    public TextureRegion[][] raindropAnim16x16;
    public TextureRegion snowflake;
    public TextureRegion bigsnow;
    public TextureRegion bigrain;
    public TextureRegion lightning;
    public TextureRegion shade;
    public TextureRegion[] smoveicons;

    // Menu
    public TextureRegion[] title;
    public TextureRegion[] titleScreenBackground;
    public TextureRegion[][] playButton;
    public TextureRegion[][] menuButtons;
    public TextureRegion[] worldSelectBackgrounds;
    public TextureRegion[][] menuExitButton;
    public TextureRegion[][] enterButton;

    // Lighting
    public TextureRegion darkness;
    public TextureRegion battledarkness;

    // UI
    public TextureRegion[][] dirpad20x20;
    public TextureRegion[][] movebutton145x50;
    public TextureRegion[][] stdmedbutton110x50;
    public TextureRegion[][] optionbutton32x32;
    public TextureRegion[][] exitbutton18x18;
    public TextureRegion[][] invbuttons92x28;
    public TextureRegion[] statuseffects20x20;
    public TextureRegion dialogBox400x80;
    public TextureRegion playerhpbar145x40;
    public TextureRegion enemyhpbar145x40;
    public TextureRegion levelupscreen400x240;
    public TextureRegion inventoryui372x212;
    public TextureRegion selectedslot28x28;
    public TextureRegion[] creditsicons;
    public TextureRegion shopui;
    public TextureRegion[][] shoptab;
    public TextureRegion[] smoveSlots;
    public TextureRegion[][] smoveButtons;
    public TextureRegion smoveFrame;

    // Skin
    public Skin skin;
    public Skin dialogSkin;

    // misc
    public TextureRegion shadow11x6;
    public TextureRegion redarrow10x9;

    // Music
    public Music menuTheme;
    public Music slimeForestTheme;
    public Music spookyGraveyardTheme;
    public Music frostyCaveTheme;
    public Music battleTheme;

    // Sound Effects
    public Sound buttonclick0;
    public Sound buttonclick1;
    public Sound buttonclick2;
    public Sound invselectclick;
    public Sound moveselectclick;
    public Sound textprogression;
    public Sound hit;
    public Sound blueattack;
    public Sound redattack;
    public Sound yellowattack;
    public Sound heal;
    public Sound death;
    public Sound thunder;
    public Sound lightrain;
    public Sound heavyrain;
    public Sound teleport;
    public Sound movement;
    public Sound finish;
    public Sound battlestart;

    // Worlds
    public Array<World> worlds = new Array<World>();

    // Arrays for each type of Move
    // Contains the entire pool of moves for each type
    public final Array<Move> accurateMoves = new Array<Move>();
    public final Array<Move> wideMoves = new Array<Move>();
    public final Array<Move> critMoves = new Array<Move>();
    public final Array<Move> healMoves = new Array<Move>();

    // contains the movepools of each boss referenced by bossIndex
    public final Array<Array<Move>> bossMoves = new Array<Array<Move>>();

    // contains all the items separated by rarity
    public final Array<Array<Item>> items = new Array<Array<Item>>();
    // contains all the shop items separated by rarity
    public final Array<Array<ShopItem>> shopItems = new Array<Array<ShopItem>>();

    // Fonts
    public final BitmapFont pixel10;

    public ResourceManager() {
        assetManager = new AssetManager();
        jsonReader = new JsonReader();

        assetManager.load("textures.atlas", TextureAtlas.class);
        assetManager.load("skins/ui.atlas", TextureAtlas.class);
        assetManager.load("skins/dialog.atlas", TextureAtlas.class);

        assetManager.load("music/menu_theme.ogg", Music.class);
        assetManager.load("music/slime_forest_theme.ogg", Music.class);
        assetManager.load("music/spooky_graveyard_theme.ogg", Music.class);
        assetManager.load("music/frosty_cave_theme.ogg", Music.class);
        assetManager.load("music/battle_theme.ogg", Music.class);

        assetManager.load("sfx/button_click0.ogg", Sound.class);
        assetManager.load("sfx/button_click1.ogg", Sound.class);
        assetManager.load("sfx/button_click2.ogg", Sound.class);
        assetManager.load("sfx/inventory_select_click.ogg", Sound.class);
        assetManager.load("sfx/move_select_click.ogg", Sound.class);
        assetManager.load("sfx/text_progression.wav", Sound.class);
        assetManager.load("sfx/hit.ogg", Sound.class);
        assetManager.load("sfx/blue_attack.ogg", Sound.class);
        assetManager.load("sfx/red_attack.ogg", Sound.class);
        assetManager.load("sfx/yellow_attack.ogg", Sound.class);
        assetManager.load("sfx/heal.ogg", Sound.class);
        assetManager.load("sfx/death.ogg", Sound.class);
        assetManager.load("sfx/thunder.ogg", Sound.class);
        assetManager.load("sfx/light_rain.ogg", Sound.class);
        assetManager.load("sfx/heavy_rain.ogg", Sound.class);
        assetManager.load("sfx/teleport.ogg", Sound.class);
        assetManager.load("sfx/movement.ogg", Sound.class);
        assetManager.load("sfx/finish.ogg", Sound.class);
        assetManager.load("sfx/battle_start.ogg", Sound.class);

        assetManager.finishLoading();

        atlas = assetManager.get("textures.atlas", TextureAtlas.class);

        // load font
        pixel10 = new BitmapFont(Gdx.files.internal("fonts/pixel.fnt"), atlas.findRegion("pixel"), false);

        skin = new Skin(atlas);
        skin.add("default-font", pixel10);
        skin.load(Gdx.files.internal("skins/ui.json"));

        dialogSkin = new Skin(assetManager.get("skins/dialog.atlas", TextureAtlas.class));
        dialogSkin.add("default-font", pixel10);
        dialogSkin.load(Gdx.files.internal("skins/dialog.json"));

        // sprites
        sprites16x16 = atlas.findRegion("16x16_sprites").split(16, 16);
        tiles16x16 = atlas.findRegion("16x16_tiles").split(16, 16);
        atiles16x16 = atlas.findRegion("16x16_atiles").split(16, 16);
        items20x20 = atlas.findRegion("20x20_items").split(10, 10);
        shopitems = atlas.findRegion("shop_items").split(10, 10);
        battleSprites96x96 = atlas.findRegion("96x96_battle_sprites").split(48, 48);
        battleBackgrounds400x240 = atlas.findRegion("battle_bgs").split(200, 120);
        shadow11x6 = atlas.findRegion("11x6_shadow");
        redarrow10x9 = atlas.findRegion("10x9_redarrow");
        battleAttacks64x64 = atlas.findRegion("64x64_battle_attacks").split(32, 32);
        battleHeal96x96 = atlas.findRegion("96x96_battle_heal").split(48, 48);
        levelUp96x96 = atlas.findRegion("96x96_level_up").split(48, 48);
        levelupscreen400x240 = atlas.findRegion("level_up");
        raindrop = atlas.findRegion("raindrop");
        raindropAnim16x16 = atlas.findRegion("raindrop_anim").split(16, 16);
        snowflake = atlas.findRegion("snowflake");
        lightning = atlas.findRegion("lightning");
        shade = atlas.findRegion("shade");
        bigrain = atlas.findRegion("big_rain");
        bigsnow = atlas.findRegion("big_snow");
        smoveicons = atlas.findRegion("smove_icons").split(14, 14)[0];

        // menu
        title = atlas.findRegion("unlucky_title").split(18, 24)[0];
        titleScreenBackground = atlas.findRegion("title_bg").split(200, 120)[0];
        playButton = atlas.findRegion("play_button").split(80, 40);
        menuButtons = atlas.findRegion("menu_buttons").split(16, 16);
        worldSelectBackgrounds = atlas.findRegion("stage_select_bg").split(200, 120)[0];
        menuExitButton = atlas.findRegion("menu_exit_button").split(14, 14);
        enterButton = atlas.findRegion("enter_button").split(79, 28);

        // light
        darkness = atlas.findRegion("darkness");
        battledarkness = atlas.findRegion("battle_darkness");

        // ui
        dirpad20x20 = atlas.findRegion("dir_pad").split(20, 20);
        movebutton145x50 = atlas.findRegion("move_buttons").split(72, 25);
        stdmedbutton110x50 = atlas.findRegion("standard_med_button").split(55, 25);
        dialogBox400x80 = atlas.findRegion("dialog_box");
        playerhpbar145x40 = atlas.findRegion("player_hp_bar");
        enemyhpbar145x40 = atlas.findRegion("enemy_hp_bar");
        optionbutton32x32 = atlas.findRegion("option_buttons").split(16, 16);
        inventoryui372x212 = atlas.findRegion("inv_ui");
        exitbutton18x18 = atlas.findRegion("exit_button").split(9, 9);
        selectedslot28x28 = atlas.findRegion("selected_slot");
        invbuttons92x28 = atlas.findRegion("inv_buttons").split(46, 14);
        statuseffects20x20 = atlas.findRegion("20x20status_effects").split(10, 10)[0];
        creditsicons = atlas.findRegion("creditsicons").split(17, 17)[0];
        shopui = atlas.findRegion("shop_ui");
        shoptab = atlas.findRegion("shop_tab").split(31, 9);
        smoveSlots = atlas.findRegion("smove_slot").split(16, 16)[0];
        smoveButtons = atlas.findRegion("smove_buttons").split(38, 18);
        smoveFrame = atlas.findRegion("smove_frame");

        // fix font spacing
        pixel10.setUseIntegerPositions(false);

        // load music
        menuTheme = assetManager.get("music/menu_theme.ogg", Music.class);
        slimeForestTheme = assetManager.get("music/slime_forest_theme.ogg", Music.class);
        spookyGraveyardTheme = assetManager.get("music/spooky_graveyard_theme.ogg", Music.class);
        frostyCaveTheme = assetManager.get("music/frosty_cave_theme.ogg", Music.class);
        battleTheme = assetManager.get("music/battle_theme.ogg", Music.class);

        // load sfx
        buttonclick0 = assetManager.get("sfx/button_click0.ogg", Sound.class);
        buttonclick1 = assetManager.get("sfx/button_click1.ogg", Sound.class);
        buttonclick2 = assetManager.get("sfx/button_click2.ogg", Sound.class);
        invselectclick = assetManager.get("sfx/inventory_select_click.ogg", Sound.class);
        moveselectclick = assetManager.get("sfx/move_select_click.ogg", Sound.class);
        textprogression = assetManager.get("sfx/text_progression.wav", Sound.class);
        hit = assetManager.get("sfx/hit.ogg", Sound.class);
        blueattack = assetManager.get("sfx/blue_attack.ogg", Sound.class);
        redattack = assetManager.get("sfx/red_attack.ogg", Sound.class);
        yellowattack = assetManager.get("sfx/yellow_attack.ogg", Sound.class);
        heal = assetManager.get("sfx/heal.ogg", Sound.class);
        death = assetManager.get("sfx/death.ogg", Sound.class);
        thunder = assetManager.get("sfx/thunder.ogg", Sound.class);
        lightrain = assetManager.get("sfx/light_rain.ogg", Sound.class);
        heavyrain = assetManager.get("sfx/heavy_rain.ogg", Sound.class);
        teleport = assetManager.get("sfx/teleport.ogg", Sound.class);
        movement = assetManager.get("sfx/movement.ogg", Sound.class);
        finish = assetManager.get("sfx/finish.ogg", Sound.class);
        battlestart = assetManager.get("sfx/battle_start.ogg", Sound.class);

        loadWorlds();
        loadMoves();
        loadItems();

        // set smove icons
        for (int i = 0; i < Util.SMOVES_ORDER_BY_ID.length; i++) {
            Util.SMOVES_ORDER_BY_ID[i].icon = new Image(smoveicons[i]);
        }
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

    private void loadWorlds() {
        // parse worlds.json
        JsonValue base = jsonReader.parse(Gdx.files.internal("maps/worlds.json"));

        int worldIndex = 0;
        for (JsonValue world : base.get("worlds")) {
            String worldName = world.getString("name");
            String shortDesc = world.getString("shortDesc");
            String longDesc = world.getString("longDesc");
            int numLevels = world.getInt("numLevels");

            int levelIndex = 0;
            Level[] temp = new Level[numLevels];
            // load levels
            for (JsonValue level : world.get("levels")) {
                temp[levelIndex] = new Level(worldIndex, levelIndex, level.getString("name"), level.getInt("avgLevel"));
                levelIndex++;
            }
            worlds.add(new World(worldName, shortDesc, longDesc, numLevels, temp));

            worldIndex++;
        }
    }

    private void loadMoves() {
        // parse moves.json
        JsonValue base = jsonReader.parse(Gdx.files.internal("moves/moves.json"));
        JsonValue boss = jsonReader.parse(Gdx.files.internal("moves/boss_moves.json"));

        // accurate Moves
        for (JsonValue move : base.get("accurate")) {
            Move m = new Move(move.getInt("type"), move.getString("name"),
                    move.getFloat("minDamage"), move.getFloat("maxDamage"));
            accurateMoves.add(m);
        }
        //System.out.println("accurate: " + accurateMoves.size);
        // wide Moves
        for (JsonValue move : base.get("wide")) {
            Move m = new Move(move.getInt("type"), move.getString("name"),
                    move.getFloat("minDamage"), move.getFloat("maxDamage"));
            wideMoves.add(m);
        }
        //System.out.println("wide: " + wideMoves.size);
        // crit Moves
        for (JsonValue move : base.get("crit")) {
            Move m = new Move(move.getString("name"), move.getFloat("damage"), move.getInt("crit"));
            critMoves.add(m);
        }
        //System.out.println("crit: " + critMoves.size);
        // heal Moves
        for (JsonValue move : base.get("healing")) {
            Move m = new Move(move.getString("name"), move.getFloat("minHeal"),
                    move.getFloat("maxHeal"), move.getInt("dmgReduction"));
            healMoves.add(m);
        }
        //System.out.println("heal: " + healMoves.size);

        Array<Move> slimeMoves = new Array<Move>();
        // load boss moves
        for (JsonValue move : boss.get("slime")) {
            if (move.getInt("type") == 1)
                slimeMoves.add(new Move(1, move.getString("name"),
                        move.getFloat("minDamage"), move.getFloat("maxDamage")));
            else
                slimeMoves.add(new Move(move.getString("name"),
                        move.getFloat("minHeal"), move.getFloat("maxHeal"), move.getInt("dmgReduction")));
        }
        bossMoves.add(slimeMoves);

        Array<Move> rrMoves = new Array<Move>();
        // load boss moves
        for (JsonValue move : boss.get("redreaper")) {
            if (move.getInt("type") == 2)
                rrMoves.add(new Move(move.getString("name"),
                    move.getFloat("damage"), move.getInt("crit")));
            else
                rrMoves.add(new Move(move.getString("name"),
                    move.getFloat("minHeal"), move.getFloat("maxHeal"), move.getInt("dmgReduction")));
        }
        bossMoves.add(rrMoves);

        Array<Move> igMoves = new Array<Move>();
        for (JsonValue move : boss.get("icegolem")) {
            if (move.getInt("type") == 0)
                igMoves.add(new Move(0, move.getString("name"),
                    move.getFloat("minDamage"), move.getFloat("maxDamage")));
        }
        bossMoves.add(igMoves);
    }

    private void loadItems() {
        // parse items.json
        JsonValue itemPool = jsonReader.parse(Gdx.files.internal("items/items.json"));
        // parse shopitems.json
        JsonValue shopitemPool = jsonReader.parse(Gdx.files.internal("items/shopitems.json"));

        // load by rarity
        for (int i = 0; i < 4; i++) {
            loadItems(itemPool, i, "rare" + i);
            loadShopItems(shopitemPool, i, "rare" + i);
        }
    }

    private void loadItems(JsonValue itemPool, int rarity, String r) {
        Array<Item> rare = new Array<Item>();
        for (JsonValue i : itemPool.get(r)) {
            int type = i.getInt("type");
            if (type == 0) {
                rare.add(new Item(this, i.getString("name"), i.getString("desc"),
                    rarity, i.getInt("imgIndex"), i.getInt("minLevel"), i.getInt("maxLevel"),
                    i.getInt("hp"), i.getInt("exp"), i.getInt("sell")));
            }
            else if (type == 1) {
                rare.add(new Item(this, i.getString("name"), i.getString("desc"),
                    rarity, i.getInt("imgIndex"), i.getInt("minLevel"), i.getInt("maxLevel"), i.getInt("sell")));
            }
            else if (type >= 2 && type <= 9) {
                rare.add(new Item(this, i.getString("name"), i.getString("desc"),
                    i.getInt("type"), rarity, i.getInt("imgIndex"), i.getInt("minLevel"), i.getInt("maxLevel"),
                    i.getInt("mhp"), i.getInt("dmg"), i.getInt("acc"), i.getInt("sell")));
            }
            else if (type == 10) {
                rare.add(new Item(this, i.getString("name"), i.getString("desc"), rarity, i.getInt("imgIndex"),
                    i.getInt("minLevel"), i.getInt("maxLevel"), i.getInt("eChance"), i.getInt("sell")));
            }
        }
        items.add(rare);
    }

    private void loadShopItems(JsonValue itemPool, int rarity, String r) {
        Array<ShopItem> rare = new Array<ShopItem>();
        for (JsonValue i : itemPool.get(r)) {
            int type = i.getInt("type");
            if (type == 0) {
                rare.add(new ShopItem(this, i.getString("name"), i.getString("desc"),
                    rarity, i.getInt("imgIndex"), i.getInt("level"), i.getInt("hp"),
                    i.getInt("exp"), i.getInt("sell"), i.getInt("price")));
            }
            else if (type >= 2 && type <= 9) {
                rare.add(new ShopItem(this, i.getString("name"), i.getString("desc"), type, rarity, i.getInt("imgIndex"),
                    i.getInt("level"), i.getInt("mhp"), i.getInt("dmg"), i.getInt("acc"), i.getInt("sell"), i.getInt("price")));
            }
            else if (type == 10) {
                rare.add(new ShopItem(this, i.getString("name"), i.getString("desc"), rarity, i.getInt("imgIndex"),
                    i.getInt("level"), i.getInt("eChance"), i.getInt("sell"), i.getInt("price")));
            }
        }
        shopItems.add(rare);
    }

    private Item getItemCopy(Item item) {
        if (item.type == 0)
            return new Item(this, item.name, item.desc, item.rarity, item.imgIndex, item.minLevel, item.maxLevel, item.hp, item.exp, item.sell);
        else if (item.type == 1)
            return new Item(this, item.name, item.desc, item.rarity, item.imgIndex, item.minLevel, item.maxLevel, item.sell);
        else if (item.type >= 2 && item.type <= 9)
            return new Item(this, item.name, item.desc, item.type, item.rarity, item.imgIndex, item.minLevel, item.maxLevel,
                item.mhp, item.dmg, item.acc, item.sell);
        else
            return new Item(this, item.name, item.desc, item.rarity, item.imgIndex, item.minLevel, item.maxLevel, item.eChance, item.sell);
    }

    /**
     * Returns a copy of a random item with weighted rarity from the pool
     * Will only return items that have ranges that contain a given level
     * Used for monster drops based on enemy level
     * Returns null if no item that fits the level
     *
     * @param rarity
     * @param level
     * @return
     */
    public Item getItem(int rarity, int level) {
        // items sorted by level range and rarity
        Array<Item> levelItems = new Array<Item>();
        for (Item item : items.get(rarity)) {
            if (level >= item.minLevel && level <= item.maxLevel) {
                levelItems.add(item);
            }
        }
        if (levelItems.size == 0) return null;
        Item selected = levelItems.get(MathUtils.random(levelItems.size - 1));
        return getItemCopy(selected);
    }

    /**
     * Returns a copy of a random item from the item pool given rarity
     * This was made so that duplicate Item actors could be created
     *
     * @param rarity
     * @return
     */
    public Item getItem(int rarity) {
        Item item = items.get(rarity).get(MathUtils.random(items.get(rarity).size - 1));
        return getItemCopy(item);
    }

    /**
     * Returns a copy of an indexed Item from the item pool
     *
     * @param rarity
     * @param index
     * @return
     */
    public Item getItemFromKey(int rarity, int index) {
        Item item = items.get(rarity).get(index);
        return getItemCopy(item);
    }

    /**
     * Returns a random Item from the pool with weighted rarity and in a given level range
     * @param level
     * @return
     */
    public Item getRandomItem(int level) {
        int k = MathUtils.random(99);
        // common
        if (k < Util.COMMON_ITEM_RNG_INDEX) return getItem(0, level);
            // rare
        else if (k < Util.RARE_ITEM_RNG_INDEX) return getItem(1, level);
            // epic
        else if (k < Util.EPIC_ITEM_RNG_INDEX) return getItem(2, level);
            // legendary
        else if (k < Util.LEGENDARY_ITEM_RNG_INDEX) return getItem(3, level);

        return null;
    }

    /**
     * Returns a random Item from the item pool with weighted rarity
     *
     * @return
     */
    public Item getRandomItem() {
        int k = MathUtils.random(99);
        // common
        if (k < Util.COMMON_ITEM_RNG_INDEX) return getItem(0);
        // rare
        else if (k < Util.RARE_ITEM_RNG_INDEX) return getItem(1);
        // epic
        else if (k < Util.EPIC_ITEM_RNG_INDEX) return getItem(2);
        // legendary
        else if (k < Util.LEGENDARY_ITEM_RNG_INDEX) return getItem(3);

        return null;
    }

    /**
     * Returns a random Item from the item pool regardless of rarity
     *
     * @return
     */
    public Item getRandomItemFromPool() {
        return getItem(MathUtils.random(3));
    }

    /**
     * Sets the volume of all music in the game
     * @param volume
     */
    public void setMusicVolume(float volume) {
        menuTheme.setVolume(volume);
        slimeForestTheme.setVolume(volume);
        spookyGraveyardTheme.setVolume(volume);
    }

    public void dispose() {
        assetManager.dispose();
        pixel10.dispose();
        atlas.dispose();
        skin.dispose();
        dialogSkin.dispose();

        menuTheme.dispose();
        slimeForestTheme.dispose();
        spookyGraveyardTheme.dispose();
        frostyCaveTheme.dispose();
        battleTheme.dispose();

        buttonclick0.dispose();
        buttonclick1.dispose();
        buttonclick2.dispose();
        invselectclick.dispose();
        moveselectclick.dispose();
        textprogression.dispose();
        hit.dispose();
        blueattack.dispose();
        redattack.dispose();
        yellowattack.dispose();
        heal.dispose();
        death.dispose();
        thunder.dispose();
        lightrain.dispose();
        heavyrain.dispose();
        teleport.dispose();
        movement.dispose();
        finish.dispose();
        battlestart.dispose();
    }

}

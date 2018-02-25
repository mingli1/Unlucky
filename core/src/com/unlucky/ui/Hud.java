package com.unlucky.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.unlucky.entity.Enemy;
import com.unlucky.entity.Player;
import com.unlucky.event.EventState;
import com.unlucky.inventory.Inventory;
import com.unlucky.inventory.Item;
import com.unlucky.main.Unlucky;
import com.unlucky.map.TileMap;
import com.unlucky.map.WeatherType;
import com.unlucky.resource.ResourceManager;
import com.unlucky.resource.Util;
import com.unlucky.screen.GameScreen;

/**
 * Handles button input and everything not in the game camera
 *
 * @author Ming Li
 */
public class Hud extends UI implements Disposable {

    // Scene2D
    public Stage stage;
    private Viewport viewport;

    // Buttons
    // --------------------------------------------------------------------
    // directional pad: index i 0 - down, 1 - up, 2 - right, 3 - left
    private ImageButton[] dirPad;
    // random magnitudes for each direction
    private int[] mags;
    // labels for magnitudes
    private Label[] magLabels;

    // option buttons: inventoryUI and settings
    private ImageButton[] optionButtons;

    // debug
    private Label util;

    public Hud(GameScreen gameScreen, TileMap tileMap, Player player, ResourceManager rm) {
        super(gameScreen, tileMap, player, rm);

        // the Hud needs more pixels to render text
        viewport = new ExtendViewport(Unlucky.V_WIDTH * 2, Unlucky.V_HEIGHT * 2, new OrthographicCamera());
        stage = new Stage(viewport, gameScreen.getBatch());

        mags = new int[4];
        shuffleMagnitudes();

        createDirPad();
        createMagLabels();
        createOptionButtons();

        BitmapFont f = rm.pixel10;
        Label.LabelStyle lp = new Label.LabelStyle(f, new Color(1, 1, 1, 1));
        util = new Label("", lp);
        util.setPosition(10, 220);
        util.setTouchable(Touchable.disabled);

        stage.addActor(util);
    }

    public void update(float dt) {}

    public void render(float dt) {
        stage.act(dt);
        stage.draw();

        util.setText(String.valueOf(Gdx.graphics.getFramesPerSecond()) + " fps\n" +
                "(" + (int) (player.getPosition().x / 16) + ", " +
                (int) (player.getPosition().y / 16) + ")");
    }

    /**
     * Turns the HUD on and off when another event occurs
     *
     * @param toggle
     */
    public void toggle(boolean toggle) {
        for (int i = 0; i < 4; i++) {
            dirPad[i].setDisabled(!toggle);
            dirPad[i].setTouchable(toggle ? Touchable.enabled : Touchable.disabled);
        }
        for (int i = 0; i < 2; i++) {
            optionButtons[i].setDisabled(!toggle);
            optionButtons[i].setTouchable(toggle ? Touchable.enabled : Touchable.disabled);
        }
    }

    /**
     * Sets a random magnitude for each direction
     */
    private void shuffleMagnitudes() {
        for (int i = 0; i < 4; i++) {
            // each magnitude between 1 and 4
            mags[i] = MathUtils.random(3) + 1;
        }
    }

    /**
     * Updates the DPAD numbers after a click
     */
    private void updateMagLabels() {
        for (int i = 0; i < 4; i++) {
            magLabels[i].setText(String.valueOf(mags[i]));
        }
    }

    /**
     * Draws the directional pad and applies Drawable effects
     * Unfortunately have to do each one separately
     */
    private void createDirPad() {
        dirPad = new ImageButton[4];

        // when each button is pressed it changes for a more visible effect
        ImageButton.ImageButtonStyle[] styles = rm.loadImageButtonStyles(4, rm.dirpad20x20);

        // down
        dirPad[0] = new ImageButton(styles[0]);
        dirPad[0].setPosition(Util.DIR_PAD_SIZE + Util.DIR_PAD_OFFSET, Util.DIR_PAD_OFFSET);
        // up
        dirPad[1] = new ImageButton(styles[1]);
        dirPad[1].setPosition(Util.DIR_PAD_SIZE + Util.DIR_PAD_OFFSET, (Util.DIR_PAD_SIZE * 2) + Util.DIR_PAD_OFFSET);
        // right
        dirPad[2] = new ImageButton(styles[2]);
        dirPad[2].setPosition((Util.DIR_PAD_SIZE * 2) + Util.DIR_PAD_OFFSET, Util.DIR_PAD_SIZE + Util.DIR_PAD_OFFSET);
        // left
        dirPad[3] = new ImageButton(styles[3]);
        dirPad[3].setPosition(Util.DIR_PAD_OFFSET, Util.DIR_PAD_SIZE + Util.DIR_PAD_OFFSET);

        handleDirPadEvents();

        for (int i = 0; i < dirPad.length; i++) {
            stage.addActor(dirPad[i]);
        }
    }

    /**
     * Draws the labels representing the random magnitudes on the dPad
     */
    private void createMagLabels() {
        magLabels = new Label[4];

        BitmapFont bitmapFont = rm.pixel10;
        Label.LabelStyle font = new Label.LabelStyle(bitmapFont, new Color(0, 0, 0, 255));

        for (int i = 0; i < 4; i++) {
            magLabels[i] = new Label(String.valueOf(mags[i]), font);
            magLabels[i].setSize(Util.DIR_PAD_SIZE, Util.DIR_PAD_SIZE);
            magLabels[i].setFontScale(2.f);
            magLabels[i].setAlignment(Align.center);
            magLabels[i].setTouchable(Touchable.disabled);
        }
        magLabels[0].setPosition(Util.DIR_PAD_SIZE + Util.DIR_PAD_OFFSET, Util.DIR_PAD_OFFSET);
        magLabels[1].setPosition(Util.DIR_PAD_SIZE + Util.DIR_PAD_OFFSET, (Util.DIR_PAD_SIZE * 2) + Util.DIR_PAD_OFFSET);
        magLabels[2].setPosition((Util.DIR_PAD_SIZE * 2) + Util.DIR_PAD_OFFSET, Util.DIR_PAD_SIZE + Util.DIR_PAD_OFFSET);
        magLabels[3].setPosition(Util.DIR_PAD_OFFSET, Util.DIR_PAD_SIZE + Util.DIR_PAD_OFFSET);

        for (int i = 0; i < 4; i++) stage.addActor(magLabels[i]);
    }

    /**
     * Creates the two option buttons: inventoryUI and settings
     */
    private void createOptionButtons() {
        optionButtons = new ImageButton[2];

        ImageButton.ImageButtonStyle[] styles = rm.loadImageButtonStyles(2, rm.optionbutton32x32);
        for (int i = 0; i < 2; i++) {
            optionButtons[i] = new ImageButton(styles[i]);
            optionButtons[i].setPosition(310 + (i * 50), 200);
            stage.addActor(optionButtons[i]);
        }
        handleOptionEvents();
    }

    /**
     * Handles player movement commands
     */
    private void handleDirPadEvents() {
        for (int i = 0; i < 4; i++) {
            final int index = i;
            dirPad[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    movePlayer(index);
                }
            });
        }
    }

    /**
     * Handles two option button commands
     */
    private void handleOptionEvents() {
        // inventoryUI
        optionButtons[0].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toggle(false);
                gameScreen.setCurrentEvent(EventState.INVENTORY);
                gameScreen.inventoryUI.start();
            }
        });

        // @TODO CHANGE
        // command prompt for now
        optionButtons[1].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.getTextInput(new Input.TextInputListener() {
                    @Override
                    public void input(String text) {
                        handleCommands(text);
                    }

                    @Override
                    public void canceled() {

                    }
                }, "Debug Command Prompt", "", "");
            }
        });
    }

    /**
     * Simple commands:
     * /heal
     * /tp [tileX] [tileY] (teleports the player to a tile coordinate)
     * /sethp [hp] (sets hp of player)
     * /setmaxhp [maxHp] (sets max hp of player)
     * /randitem (adds a random item from the item pool weighted by rarity)
     * /item [rarity] (adds a random item of a given rarity 0-3)
     * /setweather [weatherId] (0 - none, 1 - rain, 2 - heavy rain, 3 - thunderstorm, 4 - snow, 5 - blizzard)
     * /addentity [entityID] [tileX] [tileY] (adds an entity to a a tile position)
     * /removeentity [tileX] [tileY] (removes the entity at a tile position)
     * /togglenight [boolean] (toggles night time on or off with true or false)
     * /fillinv (fills the inventory with random items)
     * /clearinv (clear the inventory)
     * /fillrarity [rarity] (fills the inventory with items of a given rarity)
     * /levelup [exp] (levels up the player based on a given amount of exp)
     * /battle [entityId] (automatically starts a battle with the given entity id)
     * /setacc [acc] (sets the accuracy of the player)
     * /setsmovecd [cd] (sets the cooldown of special moves based on num of turns; 0 for no cd to test the icons)
     *
     * @param command
     */
    private void handleCommands(String command) {
        String cmd = command.trim();
        if (eq(cmd, "/heal")) player.setHp(player.getMaxHp());
        if (cmd.startsWith("/tp")) {
            String[] input = cmd.split(" ");
            if (input.length == 3) {
                int x = Integer.parseInt(input[1]);
                int y = Integer.parseInt(input[2]);
                if (x >= 0 && x < tileMap.mapWidth && y >= 0 && y < tileMap.mapHeight) {
                    player.setPosition(tileMap.toMapCoords(new Vector2(x, y)));
                }
            }
        }
        if (cmd.startsWith("/sethp")) {
            String[] input = cmd.split(" ");
            if (input.length == 2) {
                player.setHp(Integer.parseInt(input[1]));
            }
        }
        if (cmd.startsWith("/setmaxhp")) {
            String[] input = cmd.split(" ");
            if (input.length == 2) {
                player.setMaxHp(Integer.parseInt(input[1]));
            }
        }
        if (eq(cmd, "/randitem")) {
            Item i = rm.getRandomItem();
            i.adjust(player.getLevel());
            player.inventory.addItem(i);
        }
        if (cmd.startsWith("/item")) {
            String[] input = cmd.split(" ");
            if (input.length == 2) {
                int rarity = Integer.parseInt(input[1]);
                if (rarity >= 0 && rarity < 4) {
                    Item i = rm.getItem(rarity);
                    i.adjust(player.getLevel());
                    player.inventory.addItem(i);
                }
            }
        }
        if (cmd.startsWith("/setweather")) {
            String[] input = cmd.split(" ");
            if (input.length == 2) {
                int wid = Integer.parseInt(input[1]);
                if (wid >= 0 && wid < 6) {
                    if (wid == 0) gameScreen.gameMap.setWeather(WeatherType.NORMAL);
                    if (wid == 1) gameScreen.gameMap.setWeather(WeatherType.RAIN);
                    if (wid == 2) gameScreen.gameMap.setWeather(WeatherType.HEAVY_RAIN);
                    if (wid == 3) gameScreen.gameMap.setWeather(WeatherType.THUNDERSTORM);
                    if (wid == 4) gameScreen.gameMap.setWeather(WeatherType.SNOW);
                    if (wid == 5) gameScreen.gameMap.setWeather(WeatherType.BLIZZARD);
                }
            }
        }
        if (cmd.startsWith("/addentity")) {
            String[] input = cmd.split(" ");
            if (input.length == 4) {
                int entityId = Integer.parseInt(input[1]);
                int x = Integer.parseInt(input[2]);
                int y = Integer.parseInt(input[3]);
                if (x >= 0 && x < tileMap.mapWidth && y >= 0 && y < tileMap.mapHeight) {
                    tileMap.addEntity(Util.getEntity(entityId, tileMap.toMapCoords(x, y), tileMap, rm), x, y);
                }
            }
        }
        if (cmd.startsWith("/removeentity")) {
            String[] input = cmd.split(" ");
            if (input.length == 3) {
                int x = Integer.parseInt(input[1]);
                int y = Integer.parseInt(input[2]);
                if (x >= 0 && x < tileMap.mapWidth && y >= 0 && y < tileMap.mapHeight) {
                    tileMap.removeEntity(x, y);
                }
            }
        }
        if (cmd.startsWith("/togglenight")) {
            String[] input = cmd.split(" ");
            if (input.length == 2) {
                boolean toggle = Boolean.parseBoolean(input[1]);
                gameScreen.gameMap.setDarkness(toggle);
            }
        }
        if (eq(cmd, "/fillinv")) {
            player.inventory.clear();
            for (int i = 0; i < Inventory.NUM_SLOTS; i++) {
                Item item = rm.getRandomItem();
                item.adjust(player.getLevel());
                player.inventory.addItem(item);
            }
        }
        if (eq(cmd, "/clearinv")) {
            player.inventory.clear();
        }
        if (cmd.startsWith("/fillrarity")) {
            player.inventory.clear();
            String[] input = cmd.split(" ");
            if (input.length == 2) {
                int rarity = Integer.parseInt(input[1]);
                for (int i = 0; i < Inventory.NUM_SLOTS; i++) {
                    Item item = rm.getItem(rarity);
                    item.adjust(player.getLevel());
                    player.inventory.addItem(item);
                }
            }
        }
        if (cmd.startsWith("/levelup")) {
            String[] input = cmd.split(" ");
            if (input.length == 2) {
                int exp = Integer.parseInt(input[1]);
                player.levelUp(exp);
                player.applyLevelUp();
            }
        }
        if (cmd.startsWith("/battle")) {
            String[] input = cmd.split(" ");
            if (input.length == 2) {
                int entityId = Integer.parseInt(input[1]);
                player.setBattling((Enemy) Util.getEntity(entityId, new Vector2(), tileMap, rm));
            }
        }
        if (cmd.startsWith("/setacc")) {
            String[] input = cmd.split(" ");
            if (input.length == 2) {
                int acc = Integer.parseInt(input[1]);
                if (acc >= 0) player.setAccuracy(acc);
            }
        }
        if (cmd.startsWith("/setsmovecd")) {
            String[] input = cmd.split(" ");
            if (input.length == 2) {
                int cd = Integer.parseInt(input[1]);
                if (cd >= 0) player.smoveCd = cd;
            }
        }
    }

    private boolean eq(String s1, String s2) {
        return s1.equalsIgnoreCase(s2);
    }

    private void movePlayer(int dir) {
        if (player.canMove()) player.getAm().setAnimation(dir);
        if (player.canMove() && !player.nextTileBlocked(dir)) {
            player.move(dir, mags[dir]);
            shuffleMagnitudes();
            updateMagLabels();
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
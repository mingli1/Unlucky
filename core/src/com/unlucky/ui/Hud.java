package com.unlucky.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.unlucky.entity.Player;
import com.unlucky.event.EventState;
import com.unlucky.inventory.Item;
import com.unlucky.main.Unlucky;
import com.unlucky.map.TileMap;
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
    private Label fps;

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
        fps = new Label("", lp);
        fps.setPosition(10, 230);
        fps.setTouchable(Touchable.disabled);

        stage.addActor(fps);
    }

    public void update(float dt) {}

    public void render(float dt) {
        stage.act(dt);
        stage.draw();

        fps.setText(String.valueOf(Gdx.graphics.getFramesPerSecond()) + " fps");
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
            mags[i] = rand.nextInt(4) + 1;
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
     * Commands:
     * /heal
     * /tp [tileX] [tileY] (teleports the player to a tile coordinate)
     * /sethp [hp] (sets hp of player)
     * /setmaxhp [maxHp] (sets max hp of player)
     * /randitem (adds a random item from the item pool weighted by rarity)
     * /item [rarity] (adds a random item of a given rarity 0-3)
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
            Item i = rm.getRandomItem(rand);
            i.adjust(player.getLevel(), rand);
            player.inventory.addItem(i);
        }
        if (cmd.startsWith("/item")) {
            String[] input = cmd.split(" ");
            if (input.length == 2) {
                int rarity = Integer.parseInt(input[1]);
                if (rarity >= 0 && rarity < 4) {
                    Item i = rm.getItem(rarity, rand);
                    i.adjust(player.getLevel(), rand);
                    player.inventory.addItem(i);
                }
            }
        }
    }

    private boolean eq(String s1, String s2) {
        return s1.equalsIgnoreCase(s2);
    }

    private void movePlayer(int dir) {
        if (player.canMove() && !player.nextTileBlocked(dir)) {
            player.move(dir, mags[dir]);
            player.getAm().setAnimation(dir);
            shuffleMagnitudes();
            updateMagLabels();
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
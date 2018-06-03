package com.unlucky.map;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.unlucky.effects.Particle;
import com.unlucky.effects.ParticleFactory;
import com.unlucky.entity.Player;
import com.unlucky.event.EventState;
import com.unlucky.inventory.Inventory;
import com.unlucky.inventory.Item;
import com.unlucky.resource.ResourceManager;
import com.unlucky.resource.Util;
import com.unlucky.screen.GameScreen;

/**
 * Stores a tile map and the player configured with map
 * properties (night, day, lighting, weather, etc.)
 *
 * Tile maps will be separated by worlds and map levels
 * All maps in a world have similar themes
 * File names for maps are in the format:
 * "w[world index]_l[level index]"
 *
 * @author Ming Li
 */
public class GameMap {

    // composite id
    public int worldIndex;
    public int levelIndex;

    // weather of map
    public WeatherType weather;
    public boolean isDark;

    public TileMap tileMap;
    public Player player;
    private ParticleFactory particleFactory;
    public GameScreen gameScreen;
    private ResourceManager rm;

    public boolean renderLight;
    private float lightningTime = 0;
    private float durationTime = 0;

    // what the player obtained during the map
    public Array<Item> itemsObtained;
    public int expObtained;
    public int goldObtained;

    // time
    public float time = 0;

    // to fix screen switching bug
    private boolean switchable = true;

    public GameMap(GameScreen gameScreen, Player player, ResourceManager rm) {
        this.gameScreen = gameScreen;
        this.player = player;
        this.rm = rm;
        itemsObtained = new Array<Item>();
        particleFactory = new ParticleFactory(gameScreen.getCamera(), rm);
    }

    /**
     * Loads a tile map from file based on world and level key
     * @param worldIndex
     * @param levelIndex
     */
    public void init(int worldIndex, int levelIndex) {
        this.worldIndex = worldIndex;
        this.levelIndex = levelIndex;

        // reset
        itemsObtained.clear();
        expObtained = 0;
        goldObtained = 0;
        time = 0;
        player.completedMap = false;
        player.getAm().setAnimation(0);

        tileMap = new TileMap(16, "maps/w" + worldIndex + "_l" + levelIndex + ".txt", new Vector2(0, 0), rm);
        // set lighting
        setDarkness(tileMap.dark);
        // set weather
        setWeather(tileMap.weather);
        player.setMap(tileMap);
    }

    /**
     * Changes the weather and sets the particle factory according to the weather
     *
     * @param weather
     */
    public void setWeather(int weather) {
        if (weather == 0) this.weather = WeatherType.NORMAL;
        else if (weather == 1) {
            this.weather = WeatherType.RAIN;
            particleFactory.set(Particle.RAINDROP, 40, Util.RAIN_VELOCITY);
        }
        else if (weather == 2) {
            this.weather = WeatherType.HEAVY_RAIN;
            particleFactory.set(Particle.RAINDROP, 75, Util.HEAVY_RAIN_VELOCITY);
        }
        else if (weather == 3) {
            this.weather = WeatherType.THUNDERSTORM;
            particleFactory.set(Particle.RAINDROP, 75, Util.HEAVY_RAIN_VELOCITY);
        }
        else if (weather == 4) {
            this.weather = WeatherType.SNOW;
            particleFactory.set(Particle.SNOWFLAKE, 100, Util.SNOW_VELOCITY);
        }
        else if (weather == 5) {
            this.weather = WeatherType.BLIZZARD;
            particleFactory.set(Particle.SNOWFLAKE, 300, Util.BLIZZARD_VELOCITY);
        }
    }

    /**
     * Sets the darkness of the map
     */
    public void setDarkness(boolean isDark) {
        this.isDark = isDark;
        renderLight = isDark;
    }

    /**
     * Calculates death penalties and sets the death message corresponding to the losses
     * Applies the penalties to the player
     */
    public void setDeath() {
        // gold and exp lost
        int goldLost = (int) ((Util.DEATH_PENALTY / 100f) * (float) player.getGold());
        int expLost = (int) ((Util.DEATH_PENALTY / 100f) * (float) player.getExp());
        String itemText = "";
        if (itemsObtained.size != 0) {
            for (int i = 0; i < itemsObtained.size; i++) {
                Item item = itemsObtained.get(i);
                if (i == itemsObtained.size - 1) {
                    itemText += item.name + ".\n\nClick to continue...";
                    break;
                }
                itemText += item.name + ", ";
            }
        }
        String deathText = "You lost " + goldLost + " G and " + expLost + " EXP.\n" +
            (itemsObtained.size == 0 ? "\nClick to continue..." : "You also lost the following items: " + itemText);
        gameScreen.hud.setDeathText(deathText);

        // apply death penalties
        player.addGold(-goldLost);
        player.addExp(-expLost);
        if (itemsObtained.size != 0) {
            for (Item item : itemsObtained) {
                for (int i = 0; i < Inventory.NUM_SLOTS; i++) {
                    if (player.inventory.getItem(i) == item)
                        player.inventory.removeItem(i);
                }
            }
        }
    }

    public void update(float dt) {
        player.update(dt);
        tileMap.update(dt);

        // engage in battle if found
        if (player.isBattling()) {
            gameScreen.hud.toggle(false);
            gameScreen.setCurrentEvent(EventState.TRANSITION);
            gameScreen.transition.start(EventState.MOVING, EventState.BATTLING);
        }
        // player stepped on interaction tile
        if (player.isTileInteraction()) {
            gameScreen.hud.toggle(false);
            gameScreen.setCurrentEvent(EventState.TILE_EVENT);
            // @TODO: change level scaling to map level
            if (player.getCurrentTile().isQuestionMark())
                gameScreen.dialog.startDialog(player.getQuestionMarkDialog(player.getLevel(), this), EventState.MOVING, EventState.MOVING);
            else if (player.getCurrentTile().isExclamationMark())
                gameScreen.dialog.startDialog(player.getExclamDialog(player.getLevel(), this), EventState.MOVING, EventState.MOVING);
        }
        // player stepped on teleport tile
        if (player.isTeleporting()) {
            gameScreen.hud.toggle(false);
            gameScreen.setCurrentEvent(EventState.TRANSITION);
            gameScreen.transition.start(EventState.MOVING, EventState.MOVING);
        }
        // player won the map and switch to victory screen
        if (player.completedMap) {
            player.getAm().stopAnimation();
            gameScreen.setCurrentEvent(EventState.PAUSE);
            player.moving = -1;
            gameScreen.getGame().victoryScreen.init(this);
            if (switchable) {
                switchable = false;
                gameScreen.hud.getStage().addAction(Actions.sequence(Actions.fadeOut(0.3f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            switchable = true;
                            gameScreen.setClickable(true);
                            gameScreen.getGame().setScreen(gameScreen.getGame().victoryScreen);
                        }
                    })));
            }
        }

        // update particles
        if (weather != WeatherType.NORMAL) particleFactory.update(dt);
        if (weather == WeatherType.THUNDERSTORM) lightningTime += dt;
    }

    public void render(float dt, SpriteBatch batch, OrthographicCamera cam) {
        tileMap.renderBottomLayer(batch, cam);

        player.render(batch);

        tileMap.render(batch, cam);
        tileMap.renderTopLayer(batch, cam);

        // render particles
        if (weather != WeatherType.NORMAL) particleFactory.render(batch);

        if (weather == WeatherType.THUNDERSTORM) {
            // render flash of white lightning
            if (lightningTime >= 7) {
                durationTime += dt;
                if (durationTime < 0.2f) {
                    if (isDark) renderLight = false;
                    batch.draw(rm.lightning, player.getPosition().x - 182, player.getPosition().y - 102);
                }
                if (durationTime > 0.2f) {
                    lightningTime = 0;
                    durationTime = 0;
                    if (isDark) renderLight = true;
                }
            }
        }

        if (renderLight) {
            batch.setBlendFunction(GL20.GL_DST_COLOR, GL20.GL_ONE_MINUS_SRC_ALPHA);
            batch.draw(rm.darkness, player.getPosition().x - 182, player.getPosition().y - 102);
            batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        }
    }

}

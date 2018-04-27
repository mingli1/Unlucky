package com.unlucky.map;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.unlucky.effects.Particle;
import com.unlucky.effects.ParticleFactory;
import com.unlucky.entity.Player;
import com.unlucky.event.EventState;
import com.unlucky.main.Unlucky;
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
 * "w[world index]_m[map index]"
 *
 * @author Ming Li
 */
public class GameMap {

    // composite id
    public int worldIndex;
    public int mapIndex;

    // weather of map
    public WeatherType weather;
    // lightmap of map
    public TextureRegion lightmap;
    public boolean isDark;

    public TileMap tileMap;
    public Player player;
    private ParticleFactory particleFactory;
    private GameScreen gameScreen;
    private ResourceManager rm;

    public boolean renderLight;
    private float lightningTime = 0;
    private float durationTime = 0;

    public GameMap(int worldIndex, int mapIndex, GameScreen gameScreen, Player player, ResourceManager rm) {
        this.worldIndex = worldIndex;
        this.mapIndex = mapIndex;
        this.gameScreen = gameScreen;
        this.player = player;
        this.rm = rm;

        //tileMap = new TileMap(16, "maps/w" + worldIndex + "_m" + mapIndex + ".txt", new Vector2(0, 0), rm);

        tileMap = new TileMap(16, "maps/test_map.txt", new Vector2(0, 0), rm);
        player.setMap(tileMap);

        lightmap = rm.darkness;
        setDarkness(true);

        // @TODO set weather and lightmap based on map composite id
        // set lightmapIndex from tilemap file

        particleFactory = new ParticleFactory(gameScreen.getCamera(), rm);

        setWeather(WeatherType.RAIN);
    }

    /**
     * Changes the weather and sets the particle factory according to the weather
     *
     * @param weather
     */
    public void setWeather(WeatherType weather) {
        this.weather = weather;
        if (weather == WeatherType.RAIN) {
            particleFactory.set(Particle.RAINDROP, 40, Util.RAIN_VELOCITY);
        }
        else if (weather == WeatherType.HEAVY_RAIN || weather == WeatherType.THUNDERSTORM) {
            particleFactory.set(Particle.RAINDROP, 75, Util.HEAVY_RAIN_VELOCITY);
        }
        else if (weather == WeatherType.SNOW) {
            particleFactory.set(Particle.SNOWFLAKE, 100, Util.SNOW_VELOCITY);
        }
        else if (weather == WeatherType.BLIZZARD) {
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
                gameScreen.dialog.startDialog(player.getQuestionMarkDialog(player.getLevel()), EventState.MOVING, EventState.MOVING);
            else if (player.getCurrentTile().isExclamationMark())
                gameScreen.dialog.startDialog(player.getExclamDialog(player.getLevel()), EventState.MOVING, EventState.MOVING);
        }
        // player stepped on teleport tile
        if (player.isTeleporting()) {
            gameScreen.hud.toggle(false);
            gameScreen.setCurrentEvent(EventState.TRANSITION);
            gameScreen.transition.start(EventState.MOVING, EventState.MOVING);
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
                    batch.draw(rm.lightning, cam.position.x - cam.viewportWidth / 2, cam.position.y - cam.viewportHeight / 2);
                }
                if (durationTime > 0.2f) {
                    lightningTime = 0;
                    durationTime = 0;
                    if (isDark) renderLight = true;
                }
            }
        }

        if (renderLight) {
            if (lightmap != null) {
                batch.setBlendFunction(GL20.GL_DST_COLOR, GL20.GL_ONE_MINUS_SRC_ALPHA);
                batch.draw(lightmap, cam.position.x - Unlucky.V_WIDTH / 2, cam.position.y - Unlucky.V_HEIGHT / 2);
                batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            }
        }
    }

}

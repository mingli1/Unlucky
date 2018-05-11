package com.unlucky.save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.unlucky.entity.Player;

/**
 * Handles the reading and writing of save data to json files.
 *
 * @author Ming Li
 */
public class Save {

    // for saving and loading
    private Player player;
    public PlayerAccessor psave;
    private Json json;
    private FileHandle file;

    public Save(Player player, String path) {
        this.player = player;
        psave = new PlayerAccessor();
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        json.setUsePrototypes(false);
        file = Gdx.files.local(path);
    }

    /**
     * Loads the player data into the PlayerAccessor then
     * writes the player save data to the json file
     */
    public void save() {
        // load player data
        psave.load(player);
        // write data to save json
        file.writeString(json.prettyPrint(psave), false);
    }

}

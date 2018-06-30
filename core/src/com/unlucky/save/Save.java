package com.unlucky.save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.unlucky.battle.SpecialMoveset;
import com.unlucky.entity.Player;
import com.unlucky.inventory.Equipment;
import com.unlucky.inventory.Inventory;
import com.unlucky.inventory.Item;
import com.unlucky.inventory.ShopItem;
import com.unlucky.resource.ResourceManager;

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
        file.writeString(Base64Coder.encodeString(json.prettyPrint(psave)), false);
    }

    /**
     * Reads the player data from the save json file and then
     * loads the data into the game through the player
     */
    public void load(ResourceManager rm) {
        if (!file.exists()) save();
        psave = json.fromJson(PlayerAccessor.class, Base64Coder.decodeString(file.readString()));

        // load atomic fields
        player.setHp(psave.hp);
        player.setMaxHp(psave.maxHp);
        player.setLevel(psave.level);
        player.setExp(psave.exp);
        player.setMaxExp(psave.maxExp);
        player.setGold(psave.gold);
        player.setMinDamage(psave.minDamage);
        player.setMaxDamage(psave.maxDamage);
        player.setAccuracy(psave.accuracy);
        player.smoveCd = psave.smoveCd;
        player.maxWorld = psave.maxWorld;
        player.maxLevel = psave.maxLevel;

        // load inventory and equips
        loadInventory(rm);
        loadEquips(rm);

        // load smoveset
        for (int i = 0; i < SpecialMoveset.MAX_MOVES; i++) {
            if (psave.smoveset[i] != -1) {
                player.smoveset.addSMove(psave.smoveset[i]);
            }
        }

        // load statistics
        player.stats = psave.stats;

        // load and apply settings
        player.settings = psave.settings;
        if (player.settings.muteMusic) rm.setMusicVolume(0f);
        else rm.setMusicVolume(player.settings.musicVolume);
    }

    /**
     * Helper method for loading and converting ItemAccessors to Items in the inventory
     */
    private void loadInventory(ResourceManager rm) {
        for (int i = 0; i < Inventory.NUM_SLOTS; i++) {
            ItemAccessor ia = psave.inventory[i];
            if (ia != null) {
                // shop items
                if (ia instanceof ShopItemAccessor) {
                    ShopItem sitem = null;
                    if (ia.type == 0)
                        sitem = new ShopItem(rm, ia.name, ia.desc, ia.rarity, ia.imgIndex, 0,
                            ia.hp, ia.exp, ia.sell, ((ShopItemAccessor) ia).price);
                    else if (ia.type >= 2 && ia.type <= 9) {
                        sitem = new ShopItem(rm, ia.name, ia.desc, ia.type, ia.rarity, ia.imgIndex, 0,
                            ia.mhp, ia.dmg, ia.acc, ia.sell, ((ShopItemAccessor) ia).price);
                        sitem.enchantCost = ia.enchantCost;
                    }
                    else if (ia.type == 10)
                        sitem = new ShopItem(rm, ia.name, ia.desc, ia.rarity, ia.imgIndex, 0,
                            ia.eChance, ia.sell, ((ShopItemAccessor) ia).price);
                    player.inventory.addItemAtIndex(sitem, ia.index);
                }
                else {
                    Item item = null;
                    if (ia.type == 0)
                        item = new Item(rm, ia.name, ia.desc, ia.rarity, ia.imgIndex, 0, 0,
                            ia.hp, ia.exp, ia.sell);
                    else if (ia.type == 1)
                        item = new Item(rm, ia.name, ia.desc, ia.rarity, ia.imgIndex, 0, 0, ia.sell);
                    else if (ia.type >= 2 && ia.type <= 9) {
                        item = new Item(rm, ia.name, ia.desc, ia.type, ia.rarity, ia.imgIndex, 0, 0,
                            ia.mhp, ia.dmg, ia.acc, ia.sell);
                        item.enchantCost = ia.enchantCost;
                    }
                    else if (ia.type == 10)
                        item = new Item(rm, ia.name, ia.desc, ia.rarity, ia.imgIndex, 0, 0,
                            ia.eChance, ia.sell);
                    player.inventory.addItemAtIndex(item, ia.index);
                }
            }
        }
    }

    /**
     * Helper method for loading and converting ItemAccessors to Items in the inventory
     */
    private void loadEquips(ResourceManager rm) {
        for (int i = 0; i < Equipment.NUM_SLOTS; i++) {
            ItemAccessor ia = psave.equips[i];
            if (ia != null) {
                // shop items
                if (ia instanceof ShopItemAccessor) {
                    ShopItem sitem = null;
                    if (ia.type == 0)
                        sitem = new ShopItem(rm, ia.name, ia.desc, ia.rarity, ia.imgIndex, 0,
                            ia.hp, ia.exp, ia.sell, ((ShopItemAccessor) ia).price);
                    else if (ia.type >= 2 && ia.type <= 9)
                        sitem = new ShopItem(rm, ia.name, ia.desc, ia.type, ia.rarity, ia.imgIndex, 0,
                            ia.mhp, ia.dmg, ia.acc, ia.sell, ((ShopItemAccessor) ia).price);
                    else if (ia.type == 10)
                        sitem = new ShopItem(rm, ia.name, ia.desc, ia.rarity, ia.imgIndex, 0,
                            ia.eChance, ia.sell, ((ShopItemAccessor) ia).price);
                    player.equips.addEquip(sitem);
                }
                else {
                    Item item = null;
                    if (ia.type == 0)
                        item = new Item(rm, ia.name, ia.desc, ia.rarity, ia.imgIndex, 0, 0,
                            ia.hp, ia.exp, ia.sell);
                    else if (ia.type == 1)
                        item = new Item(rm, ia.name, ia.desc, ia.rarity, ia.imgIndex, 0, 0, ia.sell);
                    else if (ia.type >= 2 && ia.type <= 9)
                        item = new Item(rm, ia.name, ia.desc, ia.type, ia.rarity, ia.imgIndex, 0, 0,
                            ia.mhp, ia.dmg, ia.acc, ia.sell);
                    else if (ia.type == 10)
                        item = new Item(rm, ia.name, ia.desc, ia.rarity, ia.imgIndex, 0, 0,
                            ia.eChance, ia.sell);
                    player.equips.addEquip(item);
                }
            }
        }
    }

}

package com.unlucky.inventory;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.unlucky.resource.ResourceManager;

/**
 * A shop selling potions and unique equips
 * The shop contains everything from beginner equips to endgame equips
 *
 * @author Ming Li
 */
public class Shop {

    // items sold separated by type
    // 0 - potions, 1 - equips, 2 - accessories (rings and necklaces)
    public Array<Array<ShopItem>> items;

    public Shop(ResourceManager rm) {
        items = new Array<Array<ShopItem>>();
        for (int i = 0; i < 3; i++) items.add(new Array<ShopItem>());

        // fill shop items with items
        for (int rarity = 0; rarity < rm.items.size; rarity++) {
            for (int i = 0; i < rm.items.get(rarity).size; i++) {
                Item item = rm.items.get(rarity).get(i);
                ShopItem shopItem;
                // potion
                if (item.type == 0) {
                    shopItem = new ShopItem(rm, item.name, item.desc, item.rarity,
                        item.imgIndex, item.hp, item.exp, item.sell, MathUtils.random(10, 1000));
                    items.get(0).add(shopItem);
                }
                // equip
                else if (item.type >= 2 && item.type <= 6) {
                    shopItem = new ShopItem(rm, item.name, item.desc, item.type, item.rarity, item.imgIndex,
                        item.mhp, item.dmg, item.acc, item.sell, MathUtils.random(30, 5000));
                    items.get(1).add(shopItem);
                }
                // accs
                else if (item.type >= 7 && item.type <= 9) {
                    shopItem = new ShopItem(rm, item.name, item.desc, item.type, item.rarity, item.imgIndex,
                        item.mhp, item.dmg, item.acc, item.sell, MathUtils.random(30, 5000));
                    items.get(1).add(shopItem);
                }
            }
        }
    }

}

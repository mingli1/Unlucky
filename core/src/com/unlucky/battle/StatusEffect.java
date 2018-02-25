package com.unlucky.battle;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.unlucky.resource.ResourceManager;

/**
 * Container class for a status effect's texture and type
 *
 * @author Ming Li
 */
public class StatusEffect {

    // types of status effects
    public static final int DMG_RED = 0;
    public static final int ACC_RED = 1;
    public static final int ACC_INC = 2;
    public static final int DMG_INC = 3;
    public static final int REFLECT = 4;
    public static final int STUN = 5;
    public static final int INVERT = 6;

    public int type;
    public TextureRegion icon;

    public StatusEffect(int type, ResourceManager rm) {
        this.type = type;
        icon = rm.statuseffects20x20[type];
    }

}

package com.unlucky.save;

/**
 * Stores the settings of the game including volume of music
 * and sfx, animation toggles, etc.
 *
 * @author Ming Li
 */
public class Settings {

    // volume
    // values between 0.f and 1.f (min and max)
    public float musicVolume = 1.f;
    public float sfxVolume = 1.f;
    public boolean muteMusic = false;
    public boolean muteSfx = false;

    // toggles
    public boolean showScreenAnimations = true;
    public boolean showWeatherAnimations = true;
    public boolean showFps = false;

}

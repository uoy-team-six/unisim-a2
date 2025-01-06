package io.github.unisim;

/**
 * A class which contains global settings for the game such as volume and difficulty.
 */
public class Settings {
    private Difficulty difficulty = Difficulty.NORMAL;
    private float volume = 1.0f;
    private boolean debugKeysEnabled;

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public void setDebugKeysEnabled(boolean debugKeysEnabled) {
        this.debugKeysEnabled = debugKeysEnabled;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public float getVolume() {
        return volume;
    }

    public boolean areDebugKeysEnabled() {
        return debugKeysEnabled;
    }
}

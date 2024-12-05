package io.github.unisim;

/**
 * A class which contains global settings for the game such as volume and difficulty.
 */
public class Settings {
    private Difficulty difficulty = Difficulty.NORMAL;
    private float volume = 1.0f;

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public float getVolume() {
        return volume;
    }
}

package io.github.unisim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MusicManager {
    private final List<String> availableMusic;
    private Music music;

    public MusicManager() {
        availableMusic = List.of("music/box-jump.ogg", "music/candy.ogg", "music/deep-blue.ogg");
    }

    private void startNewMusic() {
        if (music != null) {
            music.dispose();
        }
        int randomIndex = ThreadLocalRandom.current().nextInt(availableMusic.size());
        music = Gdx.audio.newMusic(Gdx.files.internal(availableMusic.get(randomIndex)));
        music.play();
    }

    public void update() {
        if (music == null || !music.isPlaying()) {
            startNewMusic();
            return;
        }
        music.setVolume(GlobalState.settings.getVolume());
    }
}

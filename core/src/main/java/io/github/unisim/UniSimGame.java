package io.github.unisim;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import io.github.unisim.screen.GameScreen;
import io.github.unisim.screen.SettingsScreen;
import io.github.unisim.screen.StartMenuScreen;

/**
 * A class implementing the main game loop by extending from {@link Game}.
 */
public class UniSimGame extends Game {
    private Screen startMenuScreen;
    private Screen settingsScreen;
    private Screen gameScreen;

    @Override
    public void create() {
        // Create all of our stateless screens.
        startMenuScreen = new StartMenuScreen(this);
        settingsScreen = new SettingsScreen(this);

        // Start in the start menu screen.
        setScreen(startMenuScreen);
    }

    @Override
    public void render() {
        // Delegate to the active screen.
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();

        // Call dispose manually for our screens, Game::dispose() only calls Screen::hide() for the active screen.
        if (gameScreen != null) {
            gameScreen.dispose();
        }
        settingsScreen.dispose();
        startMenuScreen.dispose();
    }

    @Override
    public void resize(int width, int height) {
        if (width + height == 0) {
            return;
        }
        ((FullscreenInputProcessor) GlobalState.fullscreenInputProcessor).resize(width, height);
        startMenuScreen.resize(width, height);
        settingsScreen.resize(width, height);
        if (gameScreen != null) {
            gameScreen.resize(width, height);
        }
    }

    public Screen createGameScreen() {
        if (gameScreen != null) {
            gameScreen.dispose();
        }
        gameScreen = new GameScreen(this);
        return gameScreen;
    }

    public Screen getStartMenuScreen() {
        return startMenuScreen;
    }

    public Screen getSettingsScreen() {
        return settingsScreen;
    }
}

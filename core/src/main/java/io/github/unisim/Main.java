package io.github.unisim;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main extends Game {
    private Screen currentScreen;

    @Override
    public void create() {
        GlobalState.currentScreen = GlobalState.startScreen;
    }

    @Override
    public void render() {
        if (currentScreen != GlobalState.currentScreen) {
            currentScreen = GlobalState.currentScreen;
            setScreen(currentScreen);
            currentScreen.resume();
        }
        super.render(); // Ensures the active screen is rendered
    }

    @Override
    public void dispose() {
    }

    @Override
    public void resize(int width, int height) {
        if (width + height == 0) {
            return;
        }
        ((FullscreenInputProcessor) GlobalState.fullscreenInputProcessor).resize(width, height);
        GlobalState.gameScreen.resize(width, height);
        GlobalState.settingScreen.resize(width, height);
        GlobalState.startScreen.resize(width, height);
    }
}

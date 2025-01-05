package io.github.unisim;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import io.github.unisim.screen.GameScreen;
import io.github.unisim.screen.LeaderboardScreen;
import io.github.unisim.screen.SettingsScreen;
import io.github.unisim.screen.StartMenuScreen;

import java.util.HashMap;
import java.util.Map;

/**
 * A class implementing the main game loop by extending from {@link Game}.
 */
public class UniSimGame extends Game {
    private Map<GameCursor, Cursor> cursorMap;
    private Leaderboard leaderboard;
    private Screen startMenuScreen;
    private Screen leaderboardScreen;
    private Screen settingsScreen;
    private Screen gameScreen;

    @Override
    public void create() {
        // Create a map from our cursors to GDX cursors.
        cursorMap = new HashMap<>();
        leaderboard = new Leaderboard();

        // Load all the cursors.
        for (var cursor : GameCursor.values()) {
            var pixmap = new Pixmap(Gdx.files.internal(cursor.getPath()));
            cursorMap.put(cursor, Gdx.graphics.newCursor(pixmap, 0, 0));
        }

        // Create all of our stateless screens.
        startMenuScreen = new StartMenuScreen(this);
        leaderboardScreen = new LeaderboardScreen(this);
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
        leaderboardScreen.resize(width, height);
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

    public void setCursor(GameCursor cursor) {
        var gdxCursor = cursorMap.get(cursor);
        Gdx.graphics.setCursor(gdxCursor);
    }

    public Leaderboard getLeaderboard() {
        return leaderboard;
    }

    public Screen getStartMenuScreen() {
        return startMenuScreen;
    }

    public Screen getLeaderboardScreen() {
        return leaderboardScreen;
    }

    public Screen getSettingsScreen() {
        return settingsScreen;
    }
}

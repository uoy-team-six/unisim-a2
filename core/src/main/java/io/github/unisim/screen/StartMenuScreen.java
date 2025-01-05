package io.github.unisim.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.unisim.GameCursor;
import io.github.unisim.GlobalState;
import io.github.unisim.UniSimGame;

/**
 * The start menu screen which presents the player with the option to start the
 * game
 * or access the settings menu.
 */
public class StartMenuScreen extends ScreenAdapter {
    private final UniSimGame game;
    private final Stage stage;
    private final InputMultiplexer inputMultiplexer = new InputMultiplexer();

    /**
     * Create a new StartMenuScreen and draw the initial UI layout.
     */
    public StartMenuScreen(UniSimGame game) {
        this.game = game;
        stage = new Stage();

        final var playButton = new TextButton("Play", GlobalState.defaultSkin);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Switch to the game screen.
                game.setScreen(game.createGameScreen());
            }
        });

        final var leaderboardButton = new TextButton("Leaderboard", GlobalState.defaultSkin);
        leaderboardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Switch to the leaderboard screen.
                game.setScreen(game.getLeaderboardScreen());
            }
        });

        final var settingsButton = new TextButton("Settings", GlobalState.defaultSkin);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Switch to the settings screen.
                game.setScreen(game.getSettingsScreen());
            }
        });

        final var quitButton = new TextButton("Quit", GlobalState.defaultSkin);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // Add UI elements to the stage in a table.
        final var table = new Table();
        table.setFillParent(true);
        table.pad(100);
        table.add(playButton).center().width(250).height(100).padBottom(10).row();
        table.add(leaderboardButton).center().width(250).height(67).padBottom(10).row();
        table.add(settingsButton).center().width(250).height(67).padBottom(10).row();
        table.add(quitButton).center().width(250).height(67);
        stage.addActor(table);

        inputMultiplexer.addProcessor(GlobalState.fullscreenInputProcessor);
        inputMultiplexer.addProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        ScreenUtils.clear(GlobalState.UISecondaryColour);

        // Set the pointer cursor.
        game.setCursor(GameCursor.POINTER);

        // Draw the stage containing buttons
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

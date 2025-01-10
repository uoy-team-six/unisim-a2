package io.github.unisim.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import io.github.unisim.GameCursor;
import io.github.unisim.GameLogic;
import io.github.unisim.GlobalState;
import io.github.unisim.UniSimGame;
import io.github.unisim.ui.GameOverUiStage;
import io.github.unisim.ui.MainUiStage;
import io.github.unisim.world.UiInputProcessor;
import io.github.unisim.world.World;
import io.github.unisim.world.WorldInputProcessor;

/**
 * Game screen where the main game is rendered and controlled.
 * Supports pausing the game with a pause menu.
 */
public class GameScreen extends ScreenAdapter {
    private final UniSimGame game;
    private final World world;
    private final GameLogic gameLogic;
    private final WorldInputProcessor worldInputProcessor;

    private final MainUiStage mainUiStage;
    private final GameOverUiStage gameOverUiStage;
    private final InputMultiplexer mainInputMultiplexer;
    private final InputMultiplexer gameOverInputMultiplexer;
    private boolean wasGameOver;

    /**
     * Constructor for the GameScreen.
     */
    public GameScreen(UniSimGame game) {
        this.game = game;
        world = new World();
        world.loadMap("map/medium_map.tmx");
        gameLogic = new GameLogic(world, GlobalState.settings.getDifficulty());
        worldInputProcessor = new WorldInputProcessor(world, gameLogic);

        mainUiStage = new MainUiStage(this);
        gameOverUiStage = new GameOverUiStage(game);

        mainInputMultiplexer = new InputMultiplexer();
        mainInputMultiplexer.addProcessor(GlobalState.fullscreenInputProcessor);
        mainInputMultiplexer.addProcessor(mainUiStage);
        mainInputMultiplexer.addProcessor(new UiInputProcessor(mainUiStage));
        mainInputMultiplexer.addProcessor(worldInputProcessor);

        gameOverInputMultiplexer = new InputMultiplexer();
        gameOverInputMultiplexer.addProcessor(GlobalState.fullscreenInputProcessor);
        gameOverInputMultiplexer.addProcessor(gameOverUiStage);
    }

    @Override
    public void render(float deltaTime) {
        // Set cursor.
        if (world.isZoomingIn()) {
            game.setCursor(GameCursor.ZOOM_IN);
        } else if (world.isZoomingOut()) {
            game.setCursor(GameCursor.ZOOM_OUT);
        } else if (worldInputProcessor.isPanning()) {
            game.setCursor(GameCursor.HAND_OPEN);
        } else {
            game.setCursor(GameCursor.POINTER);
        }

        if (GlobalState.settings.areDebugKeysEnabled() && Gdx.input.isKeyPressed(Input.Keys.F1)) {
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                deltaTime *= 100.0f;
            } else {
                deltaTime *= 10.0f;
            }
        }

        // Update game logic and active UI stage.
        final var activeUiStage = gameLogic.isGameOver() ? gameOverUiStage : mainUiStage;
        gameLogic.update(deltaTime);
        activeUiStage.act(deltaTime);

        // Game over camera handling.
        if (gameLogic.isGameOver()) {
            if (!wasGameOver) {
                wasGameOver = true;
                Gdx.input.setInputProcessor(gameOverInputMultiplexer);
                gameOverUiStage.onGameOver(gameLogic.calculateScore());
            }
            world.selectedBuilding = null;
            world.zoom((world.getMaxZoom() - world.getZoom()) * 2f);
            world.pan((150 - world.getCameraPos().x) / 10, -world.getCameraPos().y / 10);
        }

        // Render the world.
        world.render(gameLogic.isPaused() ? 0.0f : deltaTime);

        // Render the active UI last.
        activeUiStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        world.resize(width, height);
        mainUiStage.resize(width, height);
        gameOverUiStage.resize(width, height);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(mainInputMultiplexer);
    }

    @Override
    public void pause() {
        // Pause game timer if game loses focus.
        gameLogic.pause();
    }

    @Override
    public void dispose() {
        world.dispose();
        mainUiStage.dispose();
    }

    public UniSimGame getGame() {
        return game;
    }

    public World getWorld() {
        return world;
    }

    public GameLogic getGameLogic() {
        return gameLogic;
    }
}

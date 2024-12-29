package io.github.unisim.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import io.github.unisim.GameCursor;
import io.github.unisim.GameLogic;
import io.github.unisim.GlobalState;
import io.github.unisim.UniSimGame;
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
    private final InputMultiplexer mainInputMultiplexer;

    /**
     * Constructor for the GameScreen.
     */
    public GameScreen(UniSimGame game) {
        this.game = game;
        world = new World();
        gameLogic = new GameLogic(world);
        worldInputProcessor = new WorldInputProcessor(world, gameLogic);

        mainUiStage = new MainUiStage(this);

        final var uiInputProcessor = new UiInputProcessor(mainUiStage);
        mainInputMultiplexer = new InputMultiplexer();
        mainInputMultiplexer.addProcessor(GlobalState.fullscreenInputProcessor);
        mainInputMultiplexer.addProcessor(mainUiStage);
        mainInputMultiplexer.addProcessor(uiInputProcessor);
        mainInputMultiplexer.addProcessor(worldInputProcessor);
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

        // Update game logic and active UI stage.
        gameLogic.update(deltaTime);
        mainUiStage.act(deltaTime);

        // Game over camera handling.
        if (gameLogic.isGameOver()) {
            world.zoom((world.getMaxZoom() - world.getZoom()) * 2f);
            world.pan((150 - world.getCameraPos().x) / 10, -world.getCameraPos().y / 10);
        }

        // Render the world.
        world.render();

        // Render the active UI last.
        mainUiStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        world.resize(width, height);
        mainUiStage.resize(width, height);
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

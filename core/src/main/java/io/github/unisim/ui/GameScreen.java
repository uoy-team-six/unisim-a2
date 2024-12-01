package io.github.unisim.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.unisim.GameLogic;
import io.github.unisim.GlobalState;
import io.github.unisim.world.UiInputProcessor;
import io.github.unisim.world.World;
import io.github.unisim.world.WorldInputProcessor;

/**
 * Game screen where the main game is rendered and controlled.
 * Supports pausing the game with a pause menu.
 */
public class GameScreen implements Screen {
    private World world = new World();
    private final GameLogic gameLogic = new GameLogic();
    private Stage stage = new Stage(new ScreenViewport());
    private InfoBar infoBar;
    private BuildingMenu buildingMenu;
    private InputProcessor uiInputProcessor = new UiInputProcessor(stage);
    private InputProcessor worldInputProcessor = new WorldInputProcessor(world, gameLogic);
    private InputMultiplexer inputMultiplexer = new InputMultiplexer();
    private GameOverMenu gameOverMenu = new GameOverMenu();

    /**
     * Constructor for the GameScreen.
     */
    public GameScreen() {
        infoBar = new InfoBar(stage, gameLogic, world);
        buildingMenu = new BuildingMenu(stage, world, gameLogic);

        inputMultiplexer.addProcessor(GlobalState.fullscreenInputProcessor);
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(uiInputProcessor);
        inputMultiplexer.addProcessor(worldInputProcessor);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float deltaTime) {
        world.render();
        gameLogic.update(deltaTime);

        if (gameLogic.isGameOver()) {
            Gdx.input.setInputProcessor(gameOverMenu.getInputProcessor());
        }

        stage.act(deltaTime);
        infoBar.update();
        buildingMenu.update();
        stage.draw();
        if (gameLogic.isGameOver()) {
            world.zoom((world.getMaxZoom() - world.getZoom()) * 2f);
            world.pan((150 - world.getCameraPos().x) / 10, -world.getCameraPos().y / 10);
            gameOverMenu.render(deltaTime);
        }
    }

    @Override
    public void resize(int width, int height) {
        world.resize(width, height);
        stage.getViewport().update(width, height, true);
        infoBar.resize(width, height);
        buildingMenu.resize(width, height);
        gameOverMenu.resize(width, height);
    }

    @Override
    public void pause() {
        // Pause game timer if game loses focus.
        gameLogic.pause();
    }

    @Override
    public void resume() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        world.dispose();
        stage.dispose();
    }
}
